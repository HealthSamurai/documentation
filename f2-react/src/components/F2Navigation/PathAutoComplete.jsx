import React, { useState, useEffect, useRef } from 'react';
import { pathSuggestions } from './services/PathSuggestions';

const PathAutoComplete = ({ isVisible, currentPath, onSelect, onClose }) => {
  const [suggestions, setSuggestions] = useState([]);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const dropdownRef = useRef(null);

  useEffect(() => {
    if (isVisible && currentPath) {
      // Используем PathSuggestions service для генерации подсказок
      const filtered = pathSuggestions.getSuggestions(currentPath);
      setSuggestions(filtered);
      setSelectedIndex(0);
    }
  }, [isVisible, currentPath]);

  const handleKeyDown = (e) => {
    if (!isVisible || suggestions.length === 0) return;

    switch (e.key) {
      case 'ArrowDown':
        e.preventDefault();
        setSelectedIndex(prev => (prev + 1) % suggestions.length);
        break;
      case 'ArrowUp':
        e.preventDefault();
        setSelectedIndex(prev => prev === 0 ? suggestions.length - 1 : prev - 1);
        break;
      case 'Enter':
      case 'Tab':
        e.preventDefault();
        if (suggestions[selectedIndex]) {
          onSelect(suggestions[selectedIndex]);
        }
        break;
      case 'Escape':
        onClose();
        break;
    }
  };

  useEffect(() => {
    if (isVisible) {
      document.addEventListener('keydown', handleKeyDown);
      return () => document.removeEventListener('keydown', handleKeyDown);
    }
  }, [isVisible, suggestions, selectedIndex]);

  // Клик вне компонента для закрытия
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        onClose();
      }
    };

    if (isVisible) {
      document.addEventListener('mousedown', handleClickOutside);
      return () => document.removeEventListener('mousedown', handleClickOutside);
    }
  }, [isVisible, onClose]);

  if (!isVisible || suggestions.length === 0) {
    return null;
  }

  return (
    <div 
      ref={dropdownRef}
      className="absolute top-full left-0 right-0 bg-white border border-gray-300 rounded-md shadow-lg z-10 max-h-60 overflow-y-auto"
    >
      {/* Header */}
      <div className="px-3 py-2 bg-gray-50 border-b border-gray-200 text-xs text-gray-600">
        Path suggestions (use ↑↓ to navigate, Enter/Tab to select)
      </div>

      {/* Suggestions */}
      {suggestions.map((suggestion, index) => {
        const isFolder = suggestion.type === 'folder' || suggestion.path.endsWith('/');
        const fileName = suggestion.path.split('/').pop();
        const folderPath = suggestion.path.substring(0, suggestion.path.lastIndexOf('/'));

        return (
          <div
            key={suggestion.path}
            className={`px-3 py-2 cursor-pointer flex items-center gap-2 transition-colors ${
              index === selectedIndex 
                ? 'bg-blue-100 text-blue-800' 
                : 'hover:bg-gray-50'
            }`}
            onClick={() => onSelect(suggestion.path)}
          >
            {/* File/Folder Icon */}
            <span className="text-xs opacity-60">
              {isFolder ? '📁' : '📄'}
            </span>

            {/* Path Display */}
            <div className="flex-1 min-w-0">
              <div className="truncate">
                {folderPath && (
                  <span className="text-gray-500 text-sm">
                    {folderPath}/
                  </span>
                )}
                <span className={`text-sm ${
                  index === selectedIndex ? 'font-medium' : ''
                }`}>
                  {suggestion.title || fileName || 'folder'}
                </span>
              </div>
            </div>

            {/* Type Badge */}
            <span className={`text-xs px-2 py-1 rounded ${
              isFolder 
                ? 'bg-blue-100 text-blue-700' 
                : 'bg-green-100 text-green-700'
            }`}>
              {isFolder ? 'folder' : 'file'}
            </span>
          </div>
        );
      })}

      {/* Footer */}
      <div className="px-3 py-2 bg-gray-50 border-t border-gray-200 text-xs text-gray-500">
        {suggestions.length} suggestion{suggestions.length !== 1 ? 's' : ''} • ESC to close
      </div>
    </div>
  );
};

export default PathAutoComplete;