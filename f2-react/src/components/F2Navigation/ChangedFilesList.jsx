import React from 'react';

const ChangedFilesList = ({ files, selectedFile, onFileSelect }) => {
  const getFileIcon = (type) => {
    switch (type) {
      case 'modified':
        return '📝';
      case 'moved':
        return '📂';
      case 'renamed':
        return '🏷️';
      case 'moved-renamed':
        return '🔄';
      default:
        return '📄';
    }
  };

  const getTypeColor = (type) => {
    switch (type) {
      case 'modified':
        return 'text-blue-600 bg-blue-50 border-blue-200';
      case 'moved':
        return 'text-green-600 bg-green-50 border-green-200';
      case 'renamed':
        return 'text-purple-600 bg-purple-50 border-purple-200';
      case 'moved-renamed':
        return 'text-orange-600 bg-orange-50 border-orange-200';
      default:
        return 'text-gray-600 bg-gray-50 border-gray-200';
    }
  };

  const getTypeLabel = (type) => {
    switch (type) {
      case 'modified':
        return 'Modified';
      case 'moved':
        return 'Moved';
      case 'renamed':
        return 'Renamed';
      case 'moved-renamed':
        return 'Moved & Renamed';
      default:
        return 'Changed';
    }
  };

  if (!files || files.length === 0) {
    return (
      <div className="flex-1 flex items-center justify-center text-gray-500 p-6">
        <div className="text-center">
          <div className="text-3xl mb-2">📁</div>
          <p>No files changed</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 border-b border-gray-200 bg-white">
      <div className="p-4">
        <div className="space-y-2">
          {files.map((file, index) => {
            const isSelected = selectedFile?.path === file.path;
            
            return (
              <div
                key={file.path}
                onClick={() => onFileSelect(file)}
                className={`
                  p-3 rounded-lg border cursor-pointer transition-all
                  ${isSelected 
                    ? 'border-blue-300 bg-blue-50 ring-2 ring-blue-200' 
                    : 'border-gray-200 hover:border-gray-300 hover:bg-gray-50'
                  }
                `}
              >
                <div className="flex items-start gap-3">
                  {/* File Icon */}
                  <div className="text-lg flex-shrink-0 mt-0.5">
                    {getFileIcon(file.type)}
                  </div>

                  {/* File Details */}
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-medium text-gray-900 truncate">
                        {file.path}
                      </span>
                      
                      <span className={`
                        inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium border
                        ${getTypeColor(file.type)}
                      `}>
                        {getTypeLabel(file.type)}
                      </span>
                    </div>
                    
                    {file.description && (
                      <p className="text-sm text-gray-600 truncate">
                        {file.description}
                      </p>
                    )}
                  </div>

                  {/* Selection Indicator */}
                  {isSelected && (
                    <div className="flex-shrink-0 text-blue-600">
                      <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                      </svg>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Summary */}
      <div className="px-4 py-3 border-t border-gray-100 bg-gray-50">
        <div className="flex items-center justify-between">
          <span className="text-sm text-gray-600">
            {files.length} file{files.length !== 1 ? 's' : ''} will be changed
          </span>
          
          <div className="flex items-center gap-4 text-xs">
            {files.filter(f => f.type === 'modified').length > 0 && (
              <span className="flex items-center gap-1 text-blue-600">
                <span className="w-2 h-2 bg-blue-400 rounded-full"></span>
                {files.filter(f => f.type === 'modified').length} modified
              </span>
            )}
            {files.filter(f => f.type === 'moved').length > 0 && (
              <span className="flex items-center gap-1 text-green-600">
                <span className="w-2 h-2 bg-green-400 rounded-full"></span>
                {files.filter(f => f.type === 'moved').length} moved
              </span>
            )}
            {files.filter(f => f.type === 'renamed').length > 0 && (
              <span className="flex items-center gap-1 text-purple-600">
                <span className="w-2 h-2 bg-purple-400 rounded-full"></span>
                {files.filter(f => f.type === 'renamed').length} renamed
              </span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChangedFilesList;