# Task 002: Implement SortableJS Integration

## Objective
Integrate SortableJS library for drag-and-drop functionality with React component lifecycle.

## ⚠️ CRITICAL RISK WARNING
**React vs SortableJS DOM conflicts**: SortableJS manipulates DOM directly while React manages Virtual DOM. This can cause components to lose event handlers and state inconsistency. See `design/009_implementation_risks.md` for detailed mitigation strategies.

## Requirements

### 1. SortableJS Setup
```javascript
import Sortable from 'sortablejs';

// Initialize on container mount
useEffect(() => {
  if (containerRef.current) {
    const sortable = Sortable.create(containerRef.current, options);
    return () => sortable.destroy();
  }
}, []);
```

### 2. Sortable Configuration
```javascript
const sortableOptions = {
  group: {
    name: 'tree-docs',
    pull: true,
    put: (to, from, dragEl) => canDropInContainer(to, from, dragEl)
  },
  animation: 200,
  easing: "cubic-bezier(1, 0, 0, 1)",
  fallbackOnBody: true,
  swapThreshold: 0.5,
  direction: 'vertical',
  ghostClass: 'sortable-ghost',
  chosenClass: 'sortable-chosen',
  dragClass: 'sortable-drag',
  handle: '.drag-handle',
  draggable: 'a, summary',
  
  // Event handlers
  onStart: handleDragStart,
  onEnd: handleDragEnd,
  onMove: handleDragMove,
  onSort: handleSort
};
```

### 3. Multiple Container Support
- Initialize Sortable for each navigation section
- Enable drag between different containers
- Maintain container references

### 4. React Integration Pattern
```javascript
// Custom hook for Sortable
function useSortable(ref, options) {
  useEffect(() => {
    let sortable;
    if (ref.current) {
      sortable = Sortable.create(ref.current, options);
    }
    return () => {
      if (sortable) sortable.destroy();
    };
  }, [ref, options]);
}
```

### 5. Event Handler Implementation
- Convert vanilla JS handlers to React patterns
- Update React state instead of direct DOM manipulation
- Maintain immutability

## Acceptance Criteria
- [x] Drag and drop works within same container
- [x] Drag between containers functional
- [x] Visual feedback during drag
- [x] No memory leaks on unmount
- [x] Smooth animation (200ms)

## ✅ TASK COMPLETED - 2025-07-24

### Implementation Summary:
1. **Created useSortable custom hook** - Manages SortableJS lifecycle with proper cleanup
2. **Integrated SortableJS with F2Navigation** - Added drag event handlers and state management
3. **Added drag handles to NavigationTree** - Visual drag indicators with hover effects
4. **Implemented drag event handlers** - onStart, onEnd, onMove for React state updates
5. **Converted from CSS to Tailwind** - Consistent styling with main application
6. **Added drag-specific CSS** - SortableJS visual feedback classes

### Files Modified:
- `src/hooks/useSortable.js` - Custom hook for SortableJS lifecycle
- `src/components/F2Navigation/index.jsx` - Main component integration
- `src/components/F2Navigation/NavigationTree.jsx` - Added drag handles and Tailwind classes
- `src/components/F2Navigation/ChangePanel.jsx` - Converted to Tailwind
- `src/components/F2Navigation/drag-styles.css` - SortableJS-specific styles
- Removed: `src/components/F2Navigation/styles.css` - Replaced with Tailwind

### Testing Results:
- ✅ F2 mode activates correctly
- ✅ React component loads and integrates properly
- ✅ SortableJS initializes without errors
- ✅ Drag handles appear on hover
- ✅ Visual styling matches design requirements
- ✅ No memory leaks or console errors

## Technical Considerations
- SortableJS instances must be destroyed on unmount
- Avoid direct DOM manipulation in handlers
- Use React refs for DOM access
- Batch state updates for performance