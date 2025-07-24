import React, { useState, useEffect, useRef } from 'react';
import PathAutoComplete from './PathAutoComplete';

const PageManagementModal = ({ 
  isOpen, 
  onClose, 
  currentPath, 
  onMoveRename, 
  onChangeOrder 
}) => {
  const [newPath, setNewPath] = useState('');
  const [showSuggestions, setShowSuggestions] = useState(false);
  const inputRef = useRef(null);

  useEffect(() => {
    if (isOpen && currentPath) {
      setNewPath(currentPath);
      // Автофокус на input при открытии
      setTimeout(() => {
        if (inputRef.current) {
          inputRef.current.focus();
          inputRef.current.select();
        }
      }, 100);
    }
  }, [isOpen, currentPath]);

  // Определение типа операции
  const detectOperation = () => {
    if (!currentPath || !newPath || currentPath === newPath) {
      return 'NO_CHANGE';
    }

    const currentDir = currentPath.substring(0, currentPath.lastIndexOf('/'));
    const currentName = currentPath.substring(currentPath.lastIndexOf('/') + 1);
    const newDir = newPath.substring(0, newPath.lastIndexOf('/'));
    const newName = newPath.substring(newPath.lastIndexOf('/') + 1);

    if (currentDir !== newDir && currentName !== newName) {
      return 'MOVE_AND_RENAME';
    }
    if (currentDir !== newDir) {
      return 'MOVE';
    }
    if (currentName !== newName) {
      return 'RENAME';
    }
    return 'NO_CHANGE';
  };

  const getOperationLabel = () => {
    const operation = detectOperation();
    switch (operation) {
      case 'MOVE': return 'Move Page';
      case 'RENAME': return 'Rename Page';
      case 'MOVE_AND_RENAME': return 'Move & Rename';
      default: return 'No Changes';
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const operation = detectOperation();
    
    if (operation !== 'NO_CHANGE') {
      onMoveRename({
        operation,
        oldPath: currentPath,
        newPath: newPath.trim()
      });
    }
    
    onClose();
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Escape') {
      onClose();
    }
    if (e.key === 'Tab') {
      e.preventDefault();
      setShowSuggestions(true);
    }
  };

  const handleInputChange = (e) => {
    setNewPath(e.target.value);
    setShowSuggestions(true);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4">
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">
            Page Management
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
            aria-label="Close"
          >
            ✕
          </button>
        </div>

        {/* Content */}
        <div className="p-6">
          <form onSubmit={handleSubmit}>
            {/* Current Path */}
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Current Path:
              </label>
              <div className="text-sm text-gray-600 bg-gray-50 px-3 py-2 rounded border">
                {currentPath}
              </div>
            </div>

            {/* New Path Input */}
            <div className="mb-6 relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                New Path:
                <span className="text-xs text-gray-500 ml-2">
                  (Press Tab for suggestions)
                </span>
              </label>
              <input
                ref={inputRef}
                type="text"
                value={newPath}
                onChange={handleInputChange}
                onKeyDown={handleKeyDown}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="/docs/your-page.md"
              />
              
              {/* Auto Complete */}
              <PathAutoComplete
                isVisible={showSuggestions}
                currentPath={newPath}
                onSelect={(path) => {
                  setNewPath(path);
                  setShowSuggestions(false);
                  inputRef.current?.focus();
                }}
                onClose={() => setShowSuggestions(false)}
              />
            </div>

            {/* Operation Preview */}
            <div className="mb-6 p-3 bg-blue-50 border border-blue-200 rounded">
              <div className="text-sm">
                <span className="font-medium text-blue-900">Operation: </span>
                <span className="text-blue-700">{getOperationLabel()}</span>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex items-center gap-3">
              <button
                type="submit"
                disabled={detectOperation() === 'NO_CHANGE'}
                className={`px-4 py-2 rounded-md font-medium transition-colors ${
                  detectOperation() === 'NO_CHANGE'
                    ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                    : 'bg-blue-600 text-white hover:bg-blue-700'
                }`}
              >
                {getOperationLabel()}
              </button>
              
              <button
                type="button"
                onClick={onChangeOrder}
                className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors"
              >
                Change Order
              </button>
              
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-gray-600 hover:text-gray-800 transition-colors"
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PageManagementModal;