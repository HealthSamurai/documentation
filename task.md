# F2 Documentation Reorganization Feature

## Goal
Provide a fullscreen documentation reorganization interface accessible via the F2 key in development mode, offering a GitBook-style editing experience for restructuring documentation through drag-and-drop operations.

## Requirements

### User Interface
- **Activation**: F2 key (only in development mode)
- **Toggle behavior**: F2 again returns to normal view
- **Fullscreen mode**: Navigation spans entire screen
- **Multi-column layout**: Content flows to next column when needed
- **Visual feedback**: Orange left border + pulsing dot for pending changes
- **Control buttons**: "Open All" / "Close All" for bulk operations
- **Rename form**: Small form on the right showing old/new URLs and filename input
- **Change preview**: Real-time display of pending changes (MOVE, RENAME operations)
- **Drop zones**: Visual highlighting showing where items can be dropped
- **Save button**: Only applies changes when pressed (no auto-save)

### Drag-and-Drop Functionality
- **Reorder siblings**: Move documents within same parent directory
- **Cross-directory moves**: Move documents between different directories
- **Create new parent**: Drop document onto another document to make it a subdirectory
- **Nested restructuring**: Support for complex hierarchical changes
- **File renaming**: Rename files with user input (only on Save)
- **Visual feedback**: Show drop zones and preview changes before saving
- **Change preview**: Display pending changes in UI before applying

### Examples

#### 1. Rename Page
**User action**: Rename "Licensing and Support" to "Support and Licensing"

**Before**:
```markdown
* [Licensing and Support](overview/licensing-and-support.md)
```

**After**:
```markdown
* [Support and Licensing](overview/support-and-licensing.md)
```

**File changes**:
- `docs/overview/licensing-and-support.md` → `docs/overview/support-and-licensing.md`
- `docs/SUMMARY.md`: Updated link text and path
- `.gitbook.yaml`: Added redirect `overview/licensing-and-support: /overview/support-and-licensing`
- All `docs/**/*.md` files: Updated relative links from `../overview/licensing-and-support.md` to `../overview/support-and-licensing.md`

#### 2. Move Page Between Directories
**User action**: Move "Settings" from "Configuration" to "Overview"

**Before**:
```markdown
## Configuration
* [Settings](configuration/settings.md)
```

**After**:
```markdown
## Overview
* [Settings](overview/settings.md)
```

**File changes**:
- `docs/configuration/settings.md` → `docs/overview/settings.md`
- `docs/SUMMARY.md`: Updated path in link
- `.gitbook.yaml`: Added redirect `configuration/settings: /overview/settings`
- All `docs/**/*.md` files: Updated relative links from `../configuration/settings.md` to `../overview/settings.md`

#### 3. Move Directory with Children
**User action**: Move "Aidbox user portal" directory from "Overview" to "Configuration"

**Before**:
```markdown
## Overview
* [Aidbox user portal](overview/aidbox-user-portal/README.md)
  * [Projects](overview/aidbox-user-portal/projects.md)
  * [Licenses](overview/aidbox-user-portal/licenses.md)
```

**After**:
```markdown
## Configuration
* [Aidbox user portal](configuration/aidbox-user-portal/README.md)
  * [Projects](configuration/aidbox-user-portal/projects.md)
  * [Licenses](configuration/aidbox-user-portal/licenses.md)
```

**File changes**:
- `docs/overview/aidbox-user-portal/` → `docs/configuration/aidbox-user-portal/` (entire directory)
- `docs/SUMMARY.md`: Updated all paths in the section
- `.gitbook.yaml`: Added redirects for all moved files
- All `docs/**/*.md` files: Updated relative links to all moved files

#### 4. Reorder Within Same Parent
**User action**: Move "Release Notes" before "Versioning" in Overview section

**Before**:
```markdown
* [Versioning](overview/versioning.md)
* [Release Notes](overview/release-notes.md)
```

**After**:
```markdown
* [Release Notes](overview/release-notes.md)
* [Versioning](overview/versioning.md)
```

**File changes**:
- `docs/SUMMARY.md`: Only order changed, no file moves
- No redirects needed
- No link updates needed (paths unchanged)

### File System Changes
- **Physical file movement**: Move `.md` files from old location to new location
- **SUMMARY.md updates**: Modify documentation structure file
- **Redirect creation**: Add entries to `.gitbook.yaml` for URL compatibility
- **Directory creation**: Create new directories as needed
- **Link updates**: Update relative links in all `docs/**/*.md` files when paths change
- **README handling**: `README.md` files create directory URLs (e.g., `a/b/README.md` → URL `a/b`)

### Processing Logic
- **No auto-save**: Changes only applied when "Save" button is pressed
- **Change detection**: 
  - Path changes → Update links and create redirects
  - Order changes only → Update SUMMARY.md only
  - Rename → Update filename, links, and create redirects
- **Validation**: 
  - Cannot move files into themselves
  - Cannot move SUMMARY.md
  - Cannot create circular references
- **Link processing**: Only relative links in `docs/**/*.md` files, no absolute links except http/https

### Save Workflow
- **Change detection**: Identify moved elements automatically
- **Visual feedback**: Show loading state during processing
- **Error handling**: Display error messages for failed operations
- **Cleanup**: Clear tracking attributes on success
- **Batch processing**: Apply all changes in correct order (moves, renames, link updates)
- **Rollback**: If any operation fails, revert all changes
- **Redirect generation**: Create `.gitbook.yaml` redirects for all moved/renamed files

### Technical Requirements
- **Conditional loading**: F2 script only loads in development mode
- **File separation**: F2 functionality in separate file from core navigation
- **Backend API**: Endpoints for file operations and validation
- **Error handling**: Graceful handling of file system and validation errors

## Success Criteria
- ✅ F2 toggle functionality works
- ✅ Drag-and-drop operations function correctly
- ✅ File system changes are applied properly
- ✅ SUMMARY.md and redirects are updated
- ✅ Visual feedback system works
- ✅ Conditional loading in development mode only
- ✅ Clean separation of concerns
