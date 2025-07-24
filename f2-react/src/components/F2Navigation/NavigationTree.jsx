import React from 'react';

const NavigationTree = React.forwardRef(({ items, dragState, onItemChange, onRename }, ref) => {
  const renderNavigationItem = (item, index, allItems) => {
    const { id, type, title, href, level, isOpen, isMainSection } = item;
    
    // Calculate Tailwind classes for indentation
    const indentClass = `ml-${Math.min(level * 6, 24)} ${level > 0 ? 'pl-3' : ''}`;
    
    // Check if we need a section divider before this item
    const prevItem = index > 0 ? allItems[index - 1] : null;
    const needsDivider = isMainSection && prevItem && 
                        (prevItem.isMainSection || prevItem.level === 0) &&
                        level <= 1;

    // Determine visual state based on drag state
    const isDraggedOver = dragState?.draggedOver?.id === id;
    const isDragging = dragState?.isDragging;
    const isBeingDragged = dragState?.draggedItem?.id === id;
    const willBecome = isDragging && isDraggedOver && 
                      dragState?.dropPosition === 'inside' && 
                      type === 'document' ? 'folder' : null;
    
    // Add visual classes for drag feedback
    let visualClasses = '';
    if (willBecome === 'folder') {
      visualClasses += ' will-become-folder';
    }
    if (isDragging && isDraggedOver && !isBeingDragged) {
      visualClasses += ' drop-zone-active';
    }
    if (isDragging && isBeingDragged) {
      visualClasses += ' navigation-item-dragging';
    }

    const itemContent = type === 'folder' ? (
      <div className={`flex items-center gap-2 p-2 rounded transition-all cursor-pointer ${
        level === 0 ? 'font-semibold text-sm text-gray-900' : 
        level === 1 ? 'text-sm text-gray-700 border-l-2 border-gray-200 pl-4' : 
        level === 2 ? 'text-xs text-gray-600 border-l border-gray-100 pl-5' : 
        'text-xs text-gray-500 border-l border-gray-100 pl-6'
      } ${isMainSection ? 'bg-blue-50 border-l-4 border-l-blue-600' : ''} hover:bg-gray-100`}>
        <span className="drag-handle inline-flex items-center justify-center w-4 h-4 text-xs text-gray-400 cursor-grab opacity-0 transition-opacity hover:opacity-100 hover:text-gray-600" title="Drag to reorder">
          <svg width="12" height="20" viewBox="0 0 12 20" fill="currentColor">
            <circle cx="3" cy="3" r="2"/>
            <circle cx="9" cy="3" r="2"/>
            <circle cx="3" cy="10" r="2"/>
            <circle cx="9" cy="10" r="2"/>
            <circle cx="3" cy="17" r="2"/>
            <circle cx="9" cy="17" r="2"/>
          </svg>
        </span>
        <span className="flex items-center min-w-0">
          {level > 0 && <span className="text-xs text-gray-400 mr-1 font-mono">{'└─ '.repeat(Math.min(level, 3))}</span>}
        </span>
        <span className="text-sm opacity-80">
          {isOpen ? '📂' : '📁'}
        </span>
        <span className="flex-1 line-height-1.4 min-w-0 break-words">{title}</span>
        <button 
          className="rename-icon" 
          onClick={(e) => {
            e.preventDefault();
            e.stopPropagation();
            onRename && onRename(item);
          }}
          title="Rename"
          type="button"
        >
          ✏️
        </button>
        {level > 0 && <span className="bg-gray-200 text-gray-600 text-xs font-semibold px-2 py-1 rounded-full min-w-5 text-center">{level}</span>}
      </div>
    ) : (
      <a 
        href={href} 
        className={`flex items-center gap-2 p-2 rounded transition-all no-underline ${
          level === 0 ? 'font-semibold text-sm text-gray-900' : 
          level === 1 ? 'text-sm text-gray-700 border-l-2 border-gray-200 pl-4' : 
          level === 2 ? 'text-xs text-gray-600 border-l border-gray-100 pl-5' : 
          'text-xs text-gray-500 border-l border-gray-100 pl-6'
        } ${isMainSection ? 'bg-blue-50 border-l-4 border-l-blue-600' : ''} hover:bg-gray-100 hover:text-gray-900 hover:translate-x-0.5`}
        onClick={(e) => e.preventDefault()} // Prevent navigation in F2 mode
      >
        <span className="drag-handle inline-flex items-center justify-center w-4 h-4 text-xs text-gray-400 cursor-grab opacity-0 transition-opacity hover:opacity-100 hover:text-gray-600" title="Drag to reorder">
          <svg width="12" height="20" viewBox="0 0 12 20" fill="currentColor">
            <circle cx="3" cy="3" r="2"/>
            <circle cx="9" cy="3" r="2"/>
            <circle cx="3" cy="10" r="2"/>
            <circle cx="9" cy="10" r="2"/>
            <circle cx="3" cy="17" r="2"/>
            <circle cx="9" cy="17" r="2"/>
          </svg>
        </span>
        <span className="flex items-center min-w-0">
          {level > 0 && <span className="text-xs text-gray-400 mr-1 font-mono">{'└─ '.repeat(Math.min(level, 3))}</span>}
        </span>
        <span className="text-sm opacity-80">📄</span>
        <span className="flex-1 line-height-1.4 min-w-0 break-words">{title}</span>
        <button 
          className="rename-icon" 
          onClick={(e) => {
            e.preventDefault();
            e.stopPropagation();
            onRename && onRename(item);
          }}
          title="Rename"
          type="button"
        >
          ✏️
        </button>
        {level > 0 && <span className="bg-gray-200 text-gray-600 text-xs font-semibold px-2 py-1 rounded-full min-w-5 text-center">{level}</span>}
      </a>
    );

    return (
      <React.Fragment key={id}>
        {needsDivider && <div className="h-px bg-gradient-to-r from-transparent via-gray-300 to-transparent my-4 relative">
          <div className="absolute left-1/2 top-0 w-1 h-1 bg-gray-300 rounded-full -translate-x-1/2 -translate-y-0.5"></div>
        </div>}
        <div 
          className={`mb-1 rounded transition-all relative navigation-item navigation-${type} ${isMainSection ? 'main-section' : ''} ${indentClass}${visualClasses}`}
          data-level={level}
          data-node-id={id}
          data-item-type={type}
        >
          {itemContent}
        </div>
      </React.Fragment>
    );
  };

  // Don't sort - keep original DOM order
  const processedItems = items;

  return (
    <div className="max-w-full bg-white rounded-lg p-5 shadow-sm">
      {processedItems.length === 0 ? (
        <div className="text-center py-15 px-5 text-gray-500">
          <p className="mb-2">No navigation items found</p>
          <p className="text-sm text-gray-400">
            Checking for navigation elements...
          </p>
        </div>
      ) : (
        <div ref={ref} className="navigation-items navigation-container relative min-h-10">
          {processedItems.map((item, index) => 
            renderNavigationItem(item, index, processedItems)
          )}
        </div>
      )}
    </div>
  );
});

export default NavigationTree;