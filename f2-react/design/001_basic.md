# F2 Navigation Drag-and-Drop Architecture

## Overview
React-based fullscreen documentation reorganization interface with drag-and-drop functionality, activated via F2 key.

## Core Architecture

### State Structure
```javascript
{
  isFullscreenMode: boolean,
  navigationTree: NavigationNode[],
  pendingChanges: {
    moves: MoveOperation[],
    renames: RenameOperation[],
    reorders: ReorderOperation[]
  },
  dragState: {
    isDragging: boolean,
    draggedItem: NavigationNode | null,
    dropTarget: DropTarget | null,
    dragOffset: { x: number, y: number }
  }
}
```

### Navigation Node Structure
```javascript
interface NavigationNode {
  id: string,
  title: string,
  path: string,
  type: 'document' | 'folder',
  children: NavigationNode[],
  level: number,
  parentId: string | null,
  order: number
}
```

### Change Operations
```javascript
interface MoveOperation {
  nodeId: string,
  fromParentId: string | null,
  toParentId: string | null,
  fromIndex: number,
  toIndex: number,
  timestamp: number
}

interface RenameOperation {
  nodeId: string,
  oldName: string,
  newName: string,
  oldPath: string,
  newPath: string,
  timestamp: number
}

interface ReorderOperation {
  parentId: string | null,
  nodeId: string,
  fromIndex: number,
  toIndex: number,
  timestamp: number
}
```

## Drag-and-Drop Logic

### 1. Drag Start
- User clicks and holds on draggable element (link or summary)
- SortableJS triggers `onStart` event
- System captures:
  - Original parent container
  - Original index in parent
  - Node data

### 2. Drag Move
- Track cursor position for visual feedback
- Calculate potential drop zones:
  - Between siblings (horizontal line indicator)
  - Into containers (highlight container)
  - Nesting level based on horizontal offset

### 3. Drop Validation
```javascript
function canDrop(draggedNode, targetNode, dropPosition) {
  // Prevent circular dependencies
  if (isAncestor(draggedNode, targetNode)) return false;
  
  // Check depth limit
  if (getDepth(targetNode) + getSubtreeDepth(draggedNode) > 3) return false;
  
  // Check if target can accept children
  if (dropPosition === 'inside' && targetNode.type === 'document') {
    // Document will be converted to folder
    return true;
  }
  
  return true;
}
```

### 4. Drop Execute
- Calculate final position and parent
- Create appropriate change operation
- Update local state optimistically
- Add visual indicator (orange border + pulsing dot)

## Visual Feedback System

### Drag States
1. **Idle**: Normal navigation display
2. **Hover**: Show drag handle (⋮⋮) on hoverable items
3. **Dragging**: 
   - Semi-transparent ghost element
   - Original element hidden
   - Drop zones highlighted
4. **Invalid Drop**: 
   - Red cursor
   - No drop zones shown
   - Original position highlighted

### Drop Zones
1. **Between Items**: Horizontal blue line (2px)
2. **Inside Container**: Blue border around container
3. **Create Folder**: Dashed border on document target
4. **Nesting Preview**: Indentation preview based on cursor X position

## Multi-Column Layout

### Responsive Columns
```css
.navigation-container {
  columns: auto 300px;
  column-gap: 28px;
  column-fill: balance;
  column-rule: 1px solid rgba(224, 224, 224, 0.5);
}

.navigation-item {
  break-inside: avoid;
}
```

### Smooth Scrollbar Integration
```javascript
Scrollbar.init(container, {
  damping: 0.1,
  alwaysShowTracks: true,
  continuousScrolling: false
});
```

## Change Tracking

### Pending Changes Display
- Fixed panel showing all pending operations
- Grouped by operation type
- Each change shows:
  - Operation type icon
  - Source → Destination
  - Timestamp
  - Undo button

### Save Process
1. Validate all pending changes
2. Check for conflicts
3. Send batch update to backend
4. Show progress indicator
5. Update UI on success
6. Clear pending changes

## Performance Optimizations

### Virtual Scrolling
- Only render visible columns
- Lazy load navigation tree branches
- Debounce drag move events

### State Updates
- Use React.memo for navigation items
- Batch state updates
- Optimistic UI updates

## Integration Points

### Backend API
```javascript
POST /api/navigation/reorganize
{
  moves: MoveOperation[],
  renames: RenameOperation[],
  reorders: ReorderOperation[]
}
```

### Event System
- F2 key: Toggle fullscreen mode
- ESC: Cancel current drag
- Ctrl+S: Save changes
- Ctrl+Z: Undo last operation

## Error Handling

### Validation Errors
- Show inline error messages
- Highlight problematic items
- Prevent save until resolved

### Network Errors
- Retry mechanism
- Offline queue
- Conflict resolution UI