# Task 006: Validation Logic

## Objective
Implement comprehensive validation for all drag-and-drop operations to ensure data integrity and prevent invalid states.

## Requirements

### 1. Core Validation Functions

#### Circular Reference Check
```javascript
const isDescendantOf = (potentialChild, potentialParent) => {
  if (!potentialParent.children || potentialParent.children.length === 0) {
    return false;
  }
  
  for (const child of potentialParent.children) {
    if (child.id === potentialChild.id) {
      return true;
    }
    if (isDescendantOf(potentialChild, child)) {
      return true;
    }
  }
  
  return false;
};

// Also check if node is ancestor
const isAncestorOf = (potentialParent, potentialChild) => {
  let current = potentialChild;
  while (current.parentId) {
    if (current.parentId === potentialParent.id) {
      return true;
    }
    current = findNodeById(tree, current.parentId);
  }
  return false;
};
```

#### Depth Validation
```javascript
const getNodeDepth = (node, tree) => {
  let depth = 0;
  let current = node;
  
  while (current.parentId) {
    depth++;
    current = findNodeById(tree, current.parentId);
  }
  
  return depth;
};

const getSubtreeDepth = (node) => {
  if (!node.children || node.children.length === 0) {
    return 0;
  }
  
  const childDepths = node.children.map(child => getSubtreeDepth(child));
  return 1 + Math.max(...childDepths);
};

const validateDepth = (draggedNode, targetParent, maxDepth = 3) => {
  const targetDepth = getNodeDepth(targetParent);
  const draggedSubtreeDepth = getSubtreeDepth(draggedNode);
  const totalDepth = targetDepth + draggedSubtreeDepth + 1;
  
  return {
    valid: totalDepth <= maxDepth,
    currentDepth: totalDepth,
    maxDepth: maxDepth,
    error: totalDepth > maxDepth ? `Maximum depth of ${maxDepth} would be exceeded` : null
  };
};
```

### 2. Drop Target Validation

```javascript
const canDropInContainer = (container, draggedNode, dropPosition) => {
  // Cannot drop on self
  if (container.dataset.nodeId === draggedNode.id) {
    return false;
  }
  
  const targetNode = findNodeById(tree, container.dataset.nodeId);
  
  // Check if dropping inside a document (will convert to folder)
  if (dropPosition === 'inside' && targetNode.type === 'document') {
    // Allow, but mark for conversion
    return {
      allowed: true,
      willConvert: true,
      conversionTarget: targetNode.id
    };
  }
  
  // Validate circular references
  if (isDescendantOf(targetNode, draggedNode) || isAncestorOf(draggedNode, targetNode)) {
    return false;
  }
  
  // Validate depth
  const depthCheck = validateDepth(draggedNode, targetNode);
  if (!depthCheck.valid) {
    return false;
  }
  
  return true;
};
```

### 3. Name Validation

```javascript
const validateNodeName = (name, parentId, excludeId = null) => {
  // Basic validation
  if (!name || name.trim().length === 0) {
    return { valid: false, error: 'Name cannot be empty' };
  }
  
  if (name.length > 255) {
    return { valid: false, error: 'Name too long (max 255 characters)' };
  }
  
  // Invalid characters
  const invalidChars = /[<>:"|?*\x00-\x1f]/;
  if (invalidChars.test(name)) {
    return { valid: false, error: 'Name contains invalid characters' };
  }
  
  // Check for duplicates in same parent
  const parent = parentId ? findNodeById(tree, parentId) : { children: tree };
  const duplicate = parent.children.some(child => 
    child.title === name && child.id !== excludeId
  );
  
  if (duplicate) {
    return { valid: false, error: 'A document with this name already exists' };
  }
  
  return { valid: true };
};
```

### 4. Path Validation

```javascript
const validatePath = (path) => {
  // Maximum path length (filesystem dependent)
  const maxPathLength = 260; // Windows limit
  
  if (path.length > maxPathLength) {
    return { valid: false, error: `Path too long (${path.length}/${maxPathLength} characters)` };
  }
  
  // Reserved names (Windows)
  const reservedNames = ['CON', 'PRN', 'AUX', 'NUL', 'COM1', 'LPT1'];
  const segments = path.split('/');
  
  for (const segment of segments) {
    if (reservedNames.includes(segment.toUpperCase())) {
      return { valid: false, error: `"${segment}" is a reserved name` };
    }
  }
  
  return { valid: true };
};
```

### 5. Batch Operation Validation

```javascript
const validatePendingChanges = (changes, currentTree) => {
  const errors = [];
  const warnings = [];
  
  // Check for conflicts
  const moveConflicts = findMoveConflicts(changes.moves);
  if (moveConflicts.length > 0) {
    errors.push(...moveConflicts);
  }
  
  // Check rename conflicts
  const renameConflicts = findRenameConflicts(changes.renames);
  if (renameConflicts.length > 0) {
    errors.push(...renameConflicts);
  }
  
  // Simulate applying changes to check final state
  let simulatedTree = cloneDeep(currentTree);
  
  try {
    // Apply moves first
    for (const move of changes.moves) {
      simulatedTree = applyMove(simulatedTree, move);
    }
    
    // Then renames
    for (const rename of changes.renames) {
      simulatedTree = applyRename(simulatedTree, rename);
    }
    
    // Validate final tree structure
    const structureErrors = validateTreeStructure(simulatedTree);
    errors.push(...structureErrors);
    
  } catch (error) {
    errors.push({
      type: 'simulation',
      message: 'Failed to simulate changes: ' + error.message
    });
  }
  
  return {
    valid: errors.length === 0,
    errors,
    warnings
  };
};
```

### 6. Real-time Validation Hooks

```javascript
export const useDropValidation = (draggedNode, dropTarget, dropPosition) => {
  const [validation, setValidation] = useState({ valid: true });
  
  useEffect(() => {
    if (!draggedNode || !dropTarget) {
      setValidation({ valid: true });
      return;
    }
    
    const result = validateDrop(draggedNode, dropTarget, dropPosition);
    setValidation(result);
  }, [draggedNode, dropTarget, dropPosition]);
  
  return validation;
};

export const useNameValidation = (name, parentId, nodeId) => {
  const [validation, setValidation] = useState({ valid: true });
  
  useEffect(() => {
    const debounced = debounce(() => {
      const result = validateNodeName(name, parentId, nodeId);
      setValidation(result);
    }, 300);
    
    debounced();
    return () => debounced.cancel();
  }, [name, parentId, nodeId]);
  
  return validation;
};
```

### 7. Error Recovery

```javascript
const rollbackChange = (tree, change) => {
  switch (change.type) {
    case 'move':
      // Restore to original position
      return moveNode(tree, change.nodeId, change.fromParent, change.fromIndex);
      
    case 'rename':
      // Restore original name
      return updateTreeNode(tree, change.nodeId, {
        title: change.oldName,
        path: change.oldPath
      });
      
    default:
      return tree;
  }
};

const validateAndApplyChanges = async (changes, tree) => {
  const applied = [];
  let currentTree = tree;
  
  try {
    for (const change of changes) {
      const validation = validateChange(change, currentTree);
      
      if (!validation.valid) {
        // Rollback all applied changes
        for (const appliedChange of applied.reverse()) {
          currentTree = rollbackChange(currentTree, appliedChange);
        }
        
        throw new ValidationError(validation.error, change);
      }
      
      currentTree = applyChange(currentTree, change);
      applied.push(change);
    }
    
    return { success: true, tree: currentTree };
    
  } catch (error) {
    return { 
      success: false, 
      error: error.message,
      failedChange: error.change
    };
  }
};
```

## Acceptance Criteria
- [ ] All drop operations validated
- [ ] Circular references prevented
- [ ] Depth limits enforced
- [ ] Name conflicts detected
- [ ] Path length validated
- [ ] Batch operations checked for conflicts
- [ ] Clear error messages provided
- [ ] Validation performance optimized

## Edge Cases
- Moving a parent into its own child
- Renaming to existing name
- Exceeding path length limits
- Special characters in names
- Reserved system names
- Concurrent modifications