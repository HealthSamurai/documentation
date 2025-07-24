# F2 Navigation React

React-based drag-and-drop navigation editor for F2 mode in the documentation system.

## Overview

This is a separate React application that provides advanced drag-and-drop functionality for the F2 navigation mode. It uses `@dnd-kit` for robust drag-and-drop handling and compiles to a single JavaScript file.

## Features

- ✅ **Drag and Drop**: Both individual pages and sections (details elements)
- ✅ **Auto-open Sections**: Collapsed sections automatically open when dragging over them
- ✅ **Multi-column Layout**: Responsive column layout for better navigation
- ✅ **Rename Functionality**: Click the ✏️ icon to rename items
- ✅ **Tree Structure**: Full support for nested navigation
- ✅ **Real-time Updates**: Changes are tracked and can be saved to the server

## Architecture

- **React 18** with hooks
- **@dnd-kit** for drag and drop
- **Webpack** for building to a single file
- **DOM parsing** to integrate with existing Clojure/Hiccup navigation

## Development

```bash
# Install dependencies
npm install

# Build for production
npm run build

# Build with watch mode for development
npm run dev

# Or use the build script
./build.sh
```

## Integration

The compiled JavaScript file is automatically placed in `../resources/public/f2-navigation-react.js` and loaded in dev mode through `layout.clj`:

```clojure
(when (gitbok.http/get-dev-mode context)
  [:script {:defer true
            :src "/static/f2-navigation-react.js"}])
```

## Usage

1. Press **F2** to enter navigation mode
2. The React app will parse the existing DOM navigation
3. Drag items using the ⋮⋮ handle
4. Drop items on sections to move them inside
5. Click ✏️ to rename items
6. Click **Save Changes** to persist changes
7. Press **F2** again or click ✕ to exit

## Components

- **`F2Navigation.jsx`**: Main container component
- **`TreeView.jsx`**: Drag-and-drop tree implementation with dnd-kit
- **`TreeItem.jsx`**: Individual tree node component
- **`utils.js`**: DOM parsing and server communication utilities
- **`index.jsx`**: Entry point and global integration

## Benefits over Vanilla JS

- **Cleaner Code**: React state management vs complex DOM manipulation
- **Better DnD**: dnd-kit vs SortableJS for complex nested scenarios
- **Auto-opening**: Trivial to implement with React state
- **Multi-column**: CSS-in-JS with responsive breakpoints
- **Type Safety**: Better development experience
- **Maintainable**: Component-based architecture