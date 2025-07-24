# Task 001: Setup Base Component

## Objective
Create the base React component structure for F2 navigation mode with proper event handling and state management.

## ⚠️ RISK WARNING
**CSS columns layout issues**: Multi-column layout can break during drag operations causing elements to jump unpredictably between columns. Plan for temporary layout switching during drag. See `design/009_implementation_risks.md`.

## Requirements
 

### 1. Create F2Navigation Component
```javascript
// src/components/F2Navigation.jsx
- Main container component
- Handle F2 key press for activation/deactivation
- Manage fullscreen mode state
- Clone and prepare navigation tree
```

### 2. Component Structure
```
F2Navigation/
├── index.jsx           // Main component
├── NavigationTree.jsx  // Tree rendering
├── DragLayer.jsx      // Drag preview layer
├── ChangePanel.jsx    // Pending changes display
└── styles.css         // Component styles
```

### 3. Initial State Setup
- `isFullscreenMode`: boolean
- `navigationData`: parsed from DOM
- `pendingChanges`: empty object
- `originalContent`: saved DOM snapshot

### 4. Key Event Handler
```javascript
useEffect(() => {
  const handleKeyDown = (e) => {
    if (e.key === 'F2') {
      e.preventDefault();
      toggleFullscreenMode();
    }
  };
  
  document.addEventListener('keydown', handleKeyDown);
  return () => document.removeEventListener('keydown', handleKeyDown);
}, []);
```

### 5. DOM Manipulation
- Save original body content
- Replace with fullscreen navigation
- Restore on exit

## Acceptance Criteria
- [ ] F2 key toggles fullscreen mode
- [ ] Original content preserved and restored
- [ ] Component renders without errors
- [ ] Basic structure matches design spec
- [ ] Event listeners properly cleaned up

## Implementation Notes
- Use React 18 features (already in package.json)
- Follow existing code style from vanilla JS example
- Ensure compatibility with existing CSS
