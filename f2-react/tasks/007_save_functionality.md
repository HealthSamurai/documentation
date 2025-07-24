# Task 007: Save Functionality

## Objective
Implement the save system that applies all pending changes to the backend and updates the UI accordingly.

## Requirements

### 1. Save Button Component
```javascript
const SaveButton = ({ changes, onSave, isSaving }) => {
  const changeCount = Object.values(changes)
    .reduce((sum, arr) => sum + arr.length, 0);
  
  const isDisabled = changeCount === 0 || isSaving;
  
  return (
    <button
      className={`save-button ${changeCount > 0 ? 'has-changes' : ''}`}
      onClick={onSave}
      disabled={isDisabled}
    >
      {isSaving ? (
        <>
          <span className="spinner" />
          Saving...
        </>
      ) : (
        <>
          <span className="icon">💾</span>
          Save Changes ({changeCount})
        </>
      )}
    </button>
  );
};
```

### 2. Save Process Flow
```javascript
const handleSave = async () => {
  try {
    // 1. Validate all changes
    dispatch({ type: 'START_SAVE' });
    
    const validation = validatePendingChanges(
      state.pendingChanges,
      state.navigationTree
    );
    
    if (!validation.valid) {
      dispatch({ 
        type: 'SAVE_ERROR', 
        payload: { errors: validation.errors }
      });
      return;
    }
    
    // 2. Prepare API payload
    const payload = prepareSavePayload(state.pendingChanges);
    
    // 3. Send to backend
    const response = await api.reorganizeNavigation(payload);
    
    // 4. Handle response
    if (response.success) {
      // Update local state with confirmed changes
      dispatch({ 
        type: 'SAVE_SUCCESS',
        payload: { 
          newTree: response.navigationTree,
          timestamp: Date.now()
        }
      });
      
      // Show success notification
      showNotification('Changes saved successfully', 'success');
      
    } else {
      throw new Error(response.error || 'Save failed');
    }
    
  } catch (error) {
    dispatch({ 
      type: 'SAVE_ERROR',
      payload: { error: error.message }
    });
    
    showNotification(`Save failed: ${error.message}`, 'error');
  }
};
```

### 3. API Payload Preparation
```javascript
const prepareSavePayload = (pendingChanges) => {
  return {
    moves: pendingChanges.moves.map(move => ({
      nodeId: move.nodeId,
      fromParent: move.fromParent,
      toParent: move.toParent,
      fromIndex: move.fromIndex,
      toIndex: move.toIndex
    })),
    
    renames: pendingChanges.renames.map(rename => ({
      nodeId: rename.nodeId,
      newName: rename.newName,
      newPath: rename.newPath
    })),
    
    reorders: pendingChanges.reorders.map(reorder => ({
      parentId: reorder.parentId,
      nodeId: reorder.nodeId,
      newIndex: reorder.toIndex
    })),
    
    conversions: pendingChanges.conversions.map(conversion => ({
      nodeId: conversion.nodeId,
      toType: conversion.toType,
      children: conversion.children || []
    })),
    
    timestamp: Date.now(),
    clientId: getClientId() // For conflict resolution
  };
};
```

### 4. Backend API Integration
```javascript
const api = {
  reorganizeNavigation: async (payload) => {
    const response = await fetch('/api/navigation/reorganize', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload)
    });
    
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Server error');
    }
    
    return response.json();
  }
};
```

### 5. Conflict Resolution
```javascript
const handleSaveConflict = async (localChanges, serverState) => {
  // Show conflict resolution dialog
  const resolution = await showConflictDialog({
    localChanges,
    serverState,
    conflicts: detectConflicts(localChanges, serverState)
  });
  
  switch (resolution.action) {
    case 'overwrite':
      // Force save local changes
      return api.reorganizeNavigation({
        ...prepareSavePayload(localChanges),
        force: true
      });
      
    case 'merge':
      // Merge changes
      const merged = mergeChanges(localChanges, serverState);
      return api.reorganizeNavigation(prepareSavePayload(merged));
      
    case 'discard':
      // Discard local changes
      dispatch({ type: 'CLEAR_CHANGES' });
      dispatch({ 
        type: 'SET_NAVIGATION_TREE', 
        payload: serverState 
      });
      return { success: true };
      
    default:
      throw new Error('Conflict resolution cancelled');
  }
};
```

### 6. Progress Indication
```javascript
const SaveProgress = ({ progress }) => {
  return (
    <div className="save-progress">
      <div className="progress-bar">
        <div 
          className="progress-fill"
          style={{ width: `${progress.percent}%` }}
        />
      </div>
      <div className="progress-text">
        {progress.message} ({progress.current}/{progress.total})
      </div>
    </div>
  );
};

// In save handler
const trackProgress = (current, total, message) => {
  dispatch({
    type: 'UPDATE_SAVE_PROGRESS',
    payload: {
      current,
      total,
      percent: (current / total) * 100,
      message
    }
  });
};
```

### 7. Error Recovery
```javascript
const SaveErrorDisplay = ({ errors, onRetry, onCancel }) => {
  return (
    <div className="save-error-dialog">
      <h3>Save Failed</h3>
      <div className="error-list">
        {errors.map((error, index) => (
          <div key={index} className="error-item">
            <span className="error-icon">⚠️</span>
            <span className="error-message">{error.message}</span>
          </div>
        ))}
      </div>
      <div className="error-actions">
        <button onClick={onRetry}>Retry</button>
        <button onClick={onCancel}>Cancel</button>
      </div>
    </div>
  );
};
```

### 8. Auto-save Option
```javascript
const useAutoSave = (changes, saveHandler, enabled = false, interval = 30000) => {
  const changeCount = Object.values(changes)
    .reduce((sum, arr) => sum + arr.length, 0);
  
  useEffect(() => {
    if (!enabled || changeCount === 0) return;
    
    const timer = setTimeout(() => {
      console.log('Auto-saving changes...');
      saveHandler();
    }, interval);
    
    return () => clearTimeout(timer);
  }, [changes, enabled, interval]);
};
```

### 9. Undo Functionality
```javascript
const handleUndo = () => {
  const lastChange = getLastChange(state.pendingChanges);
  
  if (!lastChange) return;
  
  dispatch({
    type: 'REMOVE_CHANGE',
    payload: {
      type: lastChange.type,
      changeId: lastChange.id
    }
  });
  
  // Revert UI optimistically
  const revertedTree = revertChange(state.navigationTree, lastChange);
  dispatch({
    type: 'SET_NAVIGATION_TREE',
    payload: revertedTree
  });
};
```

## Acceptance Criteria
- [ ] Save button shows change count
- [ ] Loading state during save
- [ ] Success/error notifications
- [ ] Conflict resolution UI
- [ ] Progress indication for large saves
- [ ] Error recovery options
- [ ] Undo last change functionality
- [ ] Auto-save option (configurable)

## Error Scenarios
- Network failure
- Server validation errors
- Concurrent modifications
- Permission denied
- Quota exceeded
- Invalid data format

## Performance Considerations
- Batch API calls
- Compress large payloads
- Progress updates for long operations
- Optimistic UI updates
- Debounce auto-save