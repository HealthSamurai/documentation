# F2 Drag-and-Drop Implementation Plan

## Current Status ✅
- F2 активация работает
- SortableJS инициализация работает (123 контейнера)
- Drag-and-drop перемещение работает
- API endpoint `/api/reorganize-docs` работает
- Базовые кнопки управления есть

## Issues Found ❌
1. **Кнопки управления** - все кнопки вызывают "Save Changes" вместо своих функций
2. **Pending changes** - drag-and-drop изменения не сохраняются как pending
3. **UI элементы** - отсутствуют форма переименования и панель изменений
4. **Визуальная обратная связь** - нет индикаторов изменений

## Implementation Plan

### 2. Fix Control Buttons
**Problem**: All buttons call "Save Changes" instead of their intended functions
**Solution**: 
- Fix event handlers for "Open All", "Close All", "Save Changes", "Reset"
- Ensure each button calls the correct function
- Add proper button identification (classes or data attributes)

### 3. Implement Pending Changes System
**Problem**: Drag-and-drop changes are not saved as pending
**Solution**:
- Create a pending changes data structure
- Store changes when drag-and-drop occurs
- Mark elements with visual indicators (orange border, pulsing dot)
- Only apply changes when "Save" is pressed
- Show pending changes in UI

### 4. Add Missing UI Elements
**Problem**: Rename form and changes panel are missing
**Solution**:
- Create rename form with old/new URL display
- Create changes panel showing pending operations
- Add visual feedback for drop zones
- Implement proper save/reset functionality

## Priority Order
1. **Fix Control Buttons** (highest priority - blocking other functionality)
2. **Implement Pending Changes** (core functionality)
3. **Add Missing UI Elements** (user experience)

## Success Criteria
- [x] Each control button works correctly
- [x] Drag-and-drop changes are stored as pending
- [x] Visual indicators show pending changes
- [x] Rename form allows file renaming
- [x] Changes panel shows pending operations
- [x] Save button applies all pending changes
- [x] Reset button clears all pending changes

## Current Progress ✅
- **Control Buttons Fixed** - Each button now calls its correct function
- **Rename Panel Implemented** - Right-click opens rename form with real-time URL preview
- **UI Elements Added** - Rename form with validation and cancel functionality
- **Pending Changes System** - Working with visual indicators and changes panel

## UI Issues Fixed ✅
- **Control Panel Visibility** - ✅ Removed control panel, moved instructions right
- **Save Button Contrast** - ✅ Fixed contrast (gray background with white text when disabled, orange gradient when active)
- **Rename UI Visibility** - ✅ Rename panel properly positioned (top: 80px, right: 20px, z-index: 1002)
- **Buttons Integration** - ✅ Moved all control buttons to changes panel for better organization

## Current UI Issues Fixed ✅
- **Open/Close All Buttons** - ✅ Moved to Drag & Drop Mode instructions, always accessible
- **Rename Instructions** - ✅ Made bold with icon ✏️, added hover effects on links
- **Visual Feedback** - ✅ Added dotted border and tooltip on link hover

## New Issues Fixed ✅
- **Auto Close All** - ✅ All sections now close when entering F2 mode
- **Rename Button** - ✅ Replaced right-click with dedicated Rename button in instructions
- **Drag-and-Drop Bug** - ✅ Fixed duplication by changing `pull: true` to `pull: false`
- **Navigation Prevention** - ✅ Fixed click handlers to prevent navigation in F2 mode

## Current Issues Fixed ✅
- **Click on Links** - ✅ Removed click handlers that interfered with navigation
- **Page Width** - ✅ Added CSS to fix width for pages with nested elements
- **Save-Only Changes** - ✅ Changes are stored in pending and only applied on "Save"
- **Move Validation Bug** - ✅ Fixed "No actual move occurred" error by allowing reordering
- **Rename Button** - ✅ Updated to work with dragged elements instead of clicked ones 