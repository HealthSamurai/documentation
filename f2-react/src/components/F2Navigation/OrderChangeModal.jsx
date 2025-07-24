import React, { useState, useEffect, useRef } from 'react';

const OrderChangeModal = ({ 
  isOpen, 
  onClose, 
  currentPage, 
  siblings = [], 
  onSaveOrder 
}) => {
  const [orderedPages, setOrderedPages] = useState([]);
  const [draggedIndex, setDraggedIndex] = useState(null);
  const containerRef = useRef(null);

  useEffect(() => {
    if (isOpen && siblings.length > 0) {
      setOrderedPages([...siblings]);
    }
  }, [isOpen, siblings]);

  const handleDragStart = (e, index) => {
    setDraggedIndex(index);
    e.dataTransfer.effectAllowed = 'move';
    e.dataTransfer.setData('text/html', '');
    
    // Добавляем визуальную обратную связь
    e.target.style.opacity = '0.5';
  };

  const handleDragEnd = (e) => {
    e.target.style.opacity = '';
    setDraggedIndex(null);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    e.dataTransfer.dropEffect = 'move';
  };

  const handleDrop = (e, dropIndex) => {
    e.preventDefault();
    
    if (draggedIndex === null || draggedIndex === dropIndex) {
      return;
    }

    const newOrderedPages = [...orderedPages];
    const draggedItem = newOrderedPages[draggedIndex];
    
    // Удаляем перетаскиваемый элемент
    newOrderedPages.splice(draggedIndex, 1);
    
    // Вставляем в новую позицию
    const insertIndex = draggedIndex < dropIndex ? dropIndex - 1 : dropIndex;
    newOrderedPages.splice(insertIndex, 0, draggedItem);
    
    setOrderedPages(newOrderedPages);
  };

  const handleSave = () => {
    onSaveOrder(orderedPages);
    onClose();
  };

  const hasChanges = () => {
    if (orderedPages.length !== siblings.length) return true;
    
    return orderedPages.some((page, index) => 
      siblings[index]?.path !== page.path
    );
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-lg w-full mx-4 max-h-[70vh] flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">
            Reorder Pages
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
            aria-label="Close"
          >
            ✕
          </button>
        </div>

        {/* Instructions */}
        <div className="p-4 bg-blue-50 border-b border-blue-200">
          <p className="text-sm text-blue-800">
            Drag pages to reorder them within this section. The current page is highlighted.
          </p>
        </div>

        {/* Pages List */}
        <div 
          ref={containerRef}
          className="flex-1 overflow-y-auto p-4 space-y-2"
        >
          {orderedPages.map((page, index) => {
            const isCurrentPage = page.path === currentPage?.path;
            const isDraggedOver = draggedIndex !== null && draggedIndex !== index;
            
            return (
              <div
                key={page.path || index}
                draggable
                onDragStart={(e) => handleDragStart(e, index)}
                onDragEnd={handleDragEnd}
                onDragOver={handleDragOver}
                onDrop={(e) => handleDrop(e, index)}
                className={`
                  flex items-center gap-3 p-3 border rounded-lg cursor-move transition-all
                  ${isCurrentPage 
                    ? 'bg-yellow-50 border-yellow-300 ring-2 ring-yellow-200' 
                    : 'bg-white border-gray-200 hover:border-gray-300'
                  }
                  ${isDraggedOver ? 'border-blue-400 bg-blue-50' : ''}
                `}
              >
                {/* Drag Handle */}
                <div className="text-gray-400 hover:text-gray-600 transition-colors">
                  <svg width="12" height="20" viewBox="0 0 12 20" fill="currentColor">
                    <circle cx="3" cy="3" r="2"/>
                    <circle cx="9" cy="3" r="2"/>
                    <circle cx="3" cy="10" r="2"/>
                    <circle cx="9" cy="10" r="2"/>
                    <circle cx="3" cy="17" r="2"/>
                    <circle cx="9" cy="17" r="2"/>
                  </svg>
                </div>

                {/* Page Info */}
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    {/* File Icon */}
                    <span className="text-sm">
                      {page.type === 'folder' ? '📁' : '📄'}
                    </span>
                    
                    {/* Title */}
                    <span className={`font-medium truncate ${
                      isCurrentPage ? 'text-yellow-800' : 'text-gray-800'
                    }`}>
                      {page.title}
                    </span>

                    {/* Current Page Badge */}
                    {isCurrentPage && (
                      <span className="bg-yellow-200 text-yellow-800 text-xs px-2 py-1 rounded-full font-medium">
                        current
                      </span>
                    )}
                  </div>
                  
                  {/* Path */}
                  <div className="text-xs text-gray-500 truncate mt-1">
                    {page.path}
                  </div>
                </div>

                {/* Position Indicator */}
                <div className="text-xs text-gray-400 bg-gray-100 px-2 py-1 rounded">
                  #{index + 1}
                </div>
              </div>
            );
          })}
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between p-4 border-t border-gray-200">
          <div className="text-sm text-gray-600">
            {orderedPages.length} page{orderedPages.length !== 1 ? 's' : ''} in this section
          </div>
          
          <div className="flex items-center gap-3">
            <button
              onClick={onClose}
              className="px-4 py-2 text-gray-600 hover:text-gray-800 transition-colors"
            >
              Cancel
            </button>
            
            <button
              onClick={handleSave}
              disabled={!hasChanges()}
              className={`px-4 py-2 rounded-md font-medium transition-colors ${
                hasChanges()
                  ? 'bg-blue-600 text-white hover:bg-blue-700'
                  : 'bg-gray-300 text-gray-500 cursor-not-allowed'
              }`}
            >
              Save Order
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderChangeModal;