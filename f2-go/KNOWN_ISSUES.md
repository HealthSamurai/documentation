# Known Issues and Limitations

## UI/UX Issues

### 1. Folder Expand/Collapse
- **Issue**: Space and arrow keys don't expand/collapse folders that contain child pages
- **Workaround**: Currently, only folders with indented children (like "Advanced Topics" with "Performance" and "Security") are recognized as expandable
- **Root Cause**: The HasChildren detection works correctly, but the expand/collapse functionality might be blocked by the move mode logic

### 2. Help Key (?)
- **Issue**: The "?" key doesn't trigger help
- **Root Cause**: Might be intercepted by terminal or shell

### 3. Insert Position Indicator
- **Issue**: The "↳ [Insert position]" indicator can be confusing when it appears before the target
- **Explanation**: The indicator shows where the item will be inserted in the tree structure, which might be at a different indentation level than the visual position suggests

## Technical Limitations

### 1. Relative Links
- Links are not updated when using relative paths (../../other-section/file.md)
- Only absolute paths from documentation root are currently supported

### 2. Circular Dependencies
- The circular dependency detection in Move operations has a known issue (test is commented out)
- Needs to be fixed to prevent creating invalid structures

### 3. Terminal Compatibility
- The application requires a terminal that supports:
  - Unicode characters (✂, ↳, ▶, ▼)
  - 256 colors
  - Mouse support (not implemented yet)

## Testing

To properly test the expand/collapse functionality:

1. Create a SUMMARY.md with nested structure:
```markdown
# Table of contents

* [Chapter 1](chapter1.md)
  * [Section 1.1](chapter1/section1.md)
  * [Section 1.2](chapter1/section2.md)
* [Chapter 2](chapter2.md)
  * [Section 2.1](chapter2/section1.md)
```

2. The folders "Chapter 1" and "Chapter 2" should be expandable

## Future Improvements

1. Add visual tree lines to show hierarchy more clearly
2. Implement breadcrumb navigation
3. Add search functionality
4. Support for drag-and-drop (when terminal supports it)
5. Undo/Redo functionality
6. Better keyboard shortcuts (vim-style navigation)