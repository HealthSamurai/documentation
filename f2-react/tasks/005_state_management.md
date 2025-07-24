# Task 005: State Management

## Objective
Implement robust state management for tracking all changes and maintaining UI consistency.

## ⚠️ RISK WARNING
**Scroll behavior conflicts**: Auto-scroll during drag can conflict with natural scrolling on trackpads and cause unpredictable scroll behavior. Implement device detection and configurable scroll parameters. See `design/009_implementation_risks.md`.

## Requirements

### 1. State Structure
```javascript
const initialState = {
  // Mode control
  isFullscreenMode: false,
  
  // Navigation data
  navigationTree: [],
  originalTree: [], // For reset functionality
  
  // Drag state
  dragState: {
    isDragging: false,
    draggedItem: null,
    draggedOver: null,
    dropPosition: null, // 'before', 'after', 'inside'
    nestingLevel: 0
  },
  
  // Change tracking
  pendingChanges: {
    moves: [],
    renames: [],
    reorders: [],
    conversions: [] // document to folder conversions
  },
  
  // UI state
  expandedNodes: new Set(),
  selectedNode: null,
  renamingNode: null,
  
  // Save state
  isSaving: false,
  saveError: null,
  lastSaveTime: null
};
```

### 2. State Management Hooks
```javascript
// Main state hook
export const useF2NavigationState = () => {
  const [state, dispatch] = useReducer(navigationReducer, initialState);
  
  // Computed values
  const hasChanges = useMemo(() => {
    return Object.values(state.pendingChanges)
      .some(changes => changes.length > 0);
  }, [state.pendingChanges]);
  
  const changeCount = useMemo(() => {
    return Object.values(state.pendingChanges)
      .reduce((sum, changes) => sum + changes.length, 0);
  }, [state.pendingChanges]);
  
  return { state, dispatch, hasChanges, changeCount };
};
```

### 3. Action Types
```javascript
const ActionTypes = {
  // Mode
  TOGGLE_FULLSCREEN: 'TOGGLE_FULLSCREEN',
  
  // Navigation
  SET_NAVIGATION_TREE: 'SET_NAVIGATION_TREE',
  UPDATE_NODE: 'UPDATE_NODE',
  
  // Drag operations
  START_DRAG: 'START_DRAG',
  UPDATE_DRAG: 'UPDATE_DRAG',
  END_DRAG: 'END_DRAG',
  
  // Changes
  ADD_CHANGE: 'ADD_CHANGE',
  REMOVE_CHANGE: 'REMOVE_CHANGE',
  CLEAR_CHANGES: 'CLEAR_CHANGES',
  
  // UI
  TOGGLE_NODE_EXPANSION: 'TOGGLE_NODE_EXPANSION',
  SELECT_NODE: 'SELECT_NODE',
  START_RENAME: 'START_RENAME',
  END_RENAME: 'END_RENAME',
  
  // Save
  START_SAVE: 'START_SAVE',
  SAVE_SUCCESS: 'SAVE_SUCCESS',
  SAVE_ERROR: 'SAVE_ERROR'
};
```

### 4. Reducer Implementation
```javascript
const navigationReducer = (state, action) => {
  switch (action.type) {
    case ActionTypes.ADD_CHANGE: {
      const { type, change } = action.payload;
      return {
        ...state,
        pendingChanges: {
          ...state.pendingChanges,
          [type]: [...state.pendingChanges[type], change]
        }
      };
    }
    
    case ActionTypes.UPDATE_NODE: {
      const { nodeId, updates } = action.payload;
      return {
        ...state,
        navigationTree: updateTreeNode(state.navigationTree, nodeId, updates)
      };
    }
    
    case ActionTypes.START_DRAG: {
      const { item, from, index } = action.payload;
      return {
        ...state,
        dragState: {
          isDragging: true,
          draggedItem: item,
          originalParent: from,
          originalIndex: index,
          draggedOver: null,
          dropPosition: null,
          nestingLevel: 0
        }
      };
    }
    
    // ... more cases
    
    default:
      return state;
  }
};
```

### 5. Action Creators
```javascript
export const actions = {
  addMove: (from, to, node) => ({
    type: ActionTypes.ADD_CHANGE,
    payload: {
      type: 'moves',
      change: {
        id: generateId(),
        nodeId: node.id,
        fromParent: from.parentId,
        toParent: to.parentId,
        fromIndex: from.index,
        toIndex: to.index,
        timestamp: Date.now()
      }
    }
  }),
  
  addRename: (node, newName) => ({
    type: ActionTypes.ADD_CHANGE,
    payload: {
      type: 'renames',
      change: {
        id: generateId(),
        nodeId: node.id,
        oldName: node.title,
        newName: newName,
        oldPath: node.path,
        newPath: generateNewPath(node, newName),
        timestamp: Date.now()
      }
    }
  }),
  
  // ... more action creators
};
```

### 6. Tree Update Utilities
```javascript
// Immutable tree update
const updateTreeNode = (tree, nodeId, updates) => {
  return tree.map(node => {
    if (node.id === nodeId) {
      return { ...node, ...updates };
    }
    if (node.children) {
      return {
        ...node,
        children: updateTreeNode(node.children, nodeId, updates)
      };
    }
    return node;
  });
};

// Move node between parents
const moveNode = (tree, nodeId, newParentId, newIndex) => {
  // Extract node
  const { node, treeWithoutNode } = extractNode(tree, nodeId);
  
  // Insert at new position
  return insertNode(treeWithoutNode, node, newParentId, newIndex);
};

// Convert document to folder
const convertToFolder = (tree, nodeId) => {
  return updateTreeNode(tree, nodeId, {
    type: 'folder',
    children: []
  });
};
```

### 7. Change Validation
```javascript
const validateChange = (change, currentTree) => {
  switch (change.type) {
    case 'move':
      return validateMove(change, currentTree);
    case 'rename':
      return validateRename(change, currentTree);
    default:
      return { valid: true };
  }
};

const validateMove = (move, tree) => {
  const node = findNodeById(tree, move.nodeId);
  const newParent = findNodeById(tree, move.toParent);
  
  // Check circular reference
  if (isDescendantOf(newParent, node)) {
    return { valid: false, error: 'Cannot move parent into child' };
  }
  
  // Check depth
  const newDepth = getNodeDepth(newParent) + getSubtreeDepth(node);
  if (newDepth > 3) {
    return { valid: false, error: 'Maximum nesting depth exceeded' };
  }
  
  return { valid: true };
};
```

## Acceptance Criteria
- [ ] State updates are immutable
- [ ] All changes tracked properly
- [ ] Computed values efficient (memoized)
- [ ] State persists during mode toggle
- [ ] Undo/redo capability
- [ ] No unnecessary re-renders

## Performance Considerations
- Use React.memo for pure components
- Implement proper memoization
- Batch related state updates
- Use structural sharing for tree updates