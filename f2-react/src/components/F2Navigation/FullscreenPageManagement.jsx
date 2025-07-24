import React, { useState, useEffect, useRef } from 'react';
import PathAutoComplete from './PathAutoComplete';
import ChangedFilesList from './ChangedFilesList';
import DiffViewer from './DiffViewer';
import { pathSuggestions } from './services/PathSuggestions';
import { siblingsFinder } from './services/SiblingsFinder';
import { fileOperationsService } from './services/FileOperationsService';

const FullscreenPageManagement = ({ 
  isOpen, 
  onClose, 
  currentPath, 
  onMoveRename,
  currentPage,
  siblings = [],
  onSaveOrder 
}) => {
  const [activeTab, setActiveTab] = useState('path'); // 'path' or 'order'
  const [hasAppliedChanges, setHasAppliedChanges] = useState(false);
  const [changedFiles, setChangedFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  
  // Tab 1: Path Management State
  const [newFilePath, setNewFilePath] = useState('');
  const [showAutoComplete, setShowAutoComplete] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const pathInputRef = useRef(null);
  
  // Tab 2: Order Management State
  const [orderedPages, setOrderedPages] = useState([]);
  const [sectionsWithPages, setSectionsWithPages] = useState([]);
  const [draggedIndex, setDraggedIndex] = useState(null);

  // Initialize state when modal opens
  useEffect(() => {
    if (isOpen) {
      initializeCurrentPageInfo();
      
      if (siblings.length > 0) {
        setOrderedPages([...siblings]);
        // Load sections with real structure
        const sections = siblingsFinder.getSiblingsWithSections(currentPath);
        setSectionsWithPages(sections);
      }

      // Reset state when reopening
      setHasAppliedChanges(false);
      setChangedFiles([]);
      setSelectedFile(null);
      setError(null);
    }
  }, [isOpen, currentPath, siblings]);

  // Initialize current page info from backend
  const initializeCurrentPageInfo = async () => {
    if (!currentPath) return;
    
    try {
      setIsLoading(true);
      const pageInfo = await fileOperationsService.getCurrentPageInfo(currentPath);
      
      if (pageInfo && pageInfo.filepath) {
        setNewFilePath(pageInfo.filepath);
      } else {
        // Fallback to URL-to-path conversion
        const filePath = currentPath.endsWith('.md') ? currentPath : currentPath + '.md';
        setNewFilePath(filePath);
      }
    } catch (error) {
      console.error('Error getting current page info:', error);
      // Fallback to URL-to-path conversion
      const filePath = currentPath.endsWith('.md') ? currentPath : currentPath + '.md';
      setNewFilePath(filePath);
    } finally {
      setIsLoading(false);
    }
  };

  // Generate URL from file path using backend service
  const generateUrl = async (filePath) => {
    if (!filePath) return '';
    
    try {
      const url = await fileOperationsService.previewNewUrl(filePath);
      return url;
    } catch (error) {
      console.error('Error previewing URL:', error);
      // Fallback to client-side generation
      return filePath.replace(/\.md$/, '').replace(/\/README$/, '');
    }
  };

  // Client-side URL generation for immediate feedback
  const generateUrlSync = (filePath) => {
    if (!filePath) return '';
    return filePath.replace(/\.md$/, '').replace(/\/README$/, '');
  };

  // Tab 1: Path Management Logic
  const detectOperation = () => {
    const currentFilePath = currentPath ? 
      (currentPath.endsWith('.md') ? currentPath : currentPath + '.md') : '';
    
    if (!currentFilePath || !newFilePath || currentFilePath === newFilePath) {
      return 'NO_CHANGE';
    }
    
    const currentDir = currentFilePath.substring(0, currentFilePath.lastIndexOf('/'));
    const currentName = currentFilePath.substring(currentFilePath.lastIndexOf('/') + 1);
    const newDir = newFilePath.substring(0, newFilePath.lastIndexOf('/'));
    const newName = newFilePath.substring(newFilePath.lastIndexOf('/') + 1);
    
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

  const handlePathSubmit = async (e) => {
    e.preventDefault();
    
    const operation = detectOperation();
    if (operation === 'NO_CHANGE') {
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      // Get current page info to get the correct current filepath
      const pageInfo = await fileOperationsService.getCurrentPageInfo(currentPath);
      
      if (!pageInfo || !pageInfo.filepath) {
        throw new Error('Could not determine current file path');
      }

      // Perform the relocation using the backend service
      const result = await fileOperationsService.relocatePage(
        pageInfo.filepath,
        newFilePath.trim()
      );

      if (result.success) {
        // Use the real changed files from backend
        setChangedFiles(result.changedFiles || []);
        setHasAppliedChanges(true);
        setSelectedFile(result.changedFiles?.[0] || null);

        const changeData = {
          operation,
          oldPath: currentPath,
          newPath: result.newUrl || generateUrlSync(newFilePath.trim())
        };
        
        onMoveRename(changeData);
      } else {
        setError(result.error || 'Failed to relocate page');
      }
    } catch (error) {
      console.error('Error relocating page:', error);
      setError(error.message || 'An error occurred while relocating the page');
    } finally {
      setIsLoading(false);
    }
  };

  const handlePathInputChange = (e) => {
    const value = e.target.value;
    setNewFilePath(value);
    setShowAutoComplete(value.length > 0);
  };

  const handlePathKeyDown = (e) => {
    if (e.key === 'Tab' && newFilePath.trim()) {
      e.preventDefault();
      setShowAutoComplete(true);
    }
  };

  const handleAutoCompleteSelect = (selectedPath) => {
    // Convert URL suggestion to file path
    const filePath = selectedPath.endsWith('.md') ? selectedPath : selectedPath + '.md';
    setNewFilePath(filePath);
    setShowAutoComplete(false);
    pathInputRef.current?.focus();
  };

  // Tab 2: Order Management Logic
  const handleDragStart = (e, index) => {
    setDraggedIndex(index);
    e.dataTransfer.effectAllowed = 'move';
    e.dataTransfer.setData('text/html', '');
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
    newOrderedPages.splice(draggedIndex, 1);
    const insertIndex = draggedIndex < dropIndex ? dropIndex - 1 : dropIndex;
    newOrderedPages.splice(insertIndex, 0, draggedItem);
    
    setOrderedPages(newOrderedPages);
  };

  const handleSaveOrder = () => {
    // Generate list of changed files for order change
    const mockChangedFiles = generateOrderChangedFiles(orderedPages);
    setChangedFiles(mockChangedFiles);
    setHasAppliedChanges(true);
    setSelectedFile(mockChangedFiles[0]);
    
    onSaveOrder(orderedPages);
  };

  const hasOrderChanges = () => {
    if (orderedPages.length !== siblings.length) return true;
    return orderedPages.some((page, index) => 
      siblings[index]?.path !== page.path
    );
  };

  // Mock function to generate changed files for path changes
  const generateChangedFiles = (changeData) => {
    const files = [];
    
    // SUMMARY.md always changes
    files.push({
      path: 'docs/SUMMARY.md',
      type: 'modified',
      description: 'Updated navigation structure'
    });

    // .gitbook.yaml might change
    files.push({
      path: 'docs/.gitbook.yaml',
      type: 'modified', 
      description: 'Updated GitBook configuration'
    });

    // Current file (moved/renamed)
    files.push({
      path: newFilePath,
      type: changeData.operation === 'MOVE' ? 'moved' : 
           changeData.operation === 'RENAME' ? 'renamed' :
           'moved-renamed',
      description: `${changeData.operation.toLowerCase().replace('_', ' and ')} from ${currentPath}`
    });

    // Files with links to this file
    files.push({
      path: 'docs/getting-started/overview.md',
      type: 'modified',
      description: 'Updated internal links'
    });

    files.push({
      path: 'docs/api/related-endpoints.md',
      type: 'modified', 
      description: 'Updated cross-references'
    });

    return files;
  };

  // Mock function to generate changed files for order changes
  const generateOrderChangedFiles = (newOrder) => {
    return [
      {
        path: 'docs/SUMMARY.md',
        type: 'modified',
        description: 'Reordered navigation structure'
      },
      {
        path: 'docs/.gitbook.yaml',
        type: 'modified',
        description: 'Updated page ordering'
      }
    ];
  };

  const handleFileSelect = (file) => {
    setSelectedFile(file);
  };

  const handleClose = () => {
    setHasAppliedChanges(false);
    setChangedFiles([]);
    setSelectedFile(null);
    onClose();
  };

  const renderPathTab = () => (
    <div className="p-6 space-y-6">
      <div className="space-y-4">
        <form onSubmit={handlePathSubmit} className="space-y-4">
          <div className="relative">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              File Path
            </label>
            <input
              ref={pathInputRef}
              type="text"
              value={newFilePath}
              onChange={handlePathInputChange}
              onKeyDown={handlePathKeyDown}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Enter file path (e.g., /docs/api/overview.md)..."
              autoComplete="off"
            />
            
            <PathAutoComplete
              isVisible={showAutoComplete}
              currentPath={generateUrlSync(newFilePath)} // Pass URL for suggestions
              onSelect={handleAutoCompleteSelect}
              onClose={() => setShowAutoComplete(false)}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              URL (auto-generated)
            </label>
            <div className="px-3 py-2 border border-gray-300 rounded-md bg-gray-50 text-gray-600 text-sm">
              {generateUrlSync(newFilePath) || 'Enter file path above to see URL...'}
            </div>
          </div>

          <div className="text-sm text-gray-600">
            <strong>Operation:</strong> {detectOperation().replace(/_/g, ' ').toLowerCase()}
          </div>

          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-md">
              <div className="text-sm text-red-800">
                <strong>Error:</strong> {error}
              </div>
            </div>
          )}

          <div className="flex items-center gap-3">
            <button
              type="submit"
              disabled={detectOperation() === 'NO_CHANGE' || isLoading}
              className={`px-4 py-2 rounded-md font-medium transition-colors ${
                detectOperation() !== 'NO_CHANGE' && !isLoading
                  ? 'bg-blue-600 text-white hover:bg-blue-700'
                  : 'bg-gray-300 text-gray-500 cursor-not-allowed'
              }`}
            >
              {isLoading ? 'Applying...' : 'Apply Changes'}
            </button>
            
            <div className="text-xs text-gray-500">
              Press Tab for path suggestions
            </div>
          </div>
        </form>
      </div>
    </div>
  );

  const renderOrderTab = () => (
    <div className="p-6 space-y-4">
      <div className="text-sm text-blue-800 bg-blue-50 p-3 rounded-md">
        Drag pages to reorder them within this section. The current page is highlighted.
      </div>

      <div className="max-h-96 overflow-y-auto space-y-1">
        {sectionsWithPages.map((item, index) => {
          if (item.type === 'section') {
            return (
              <div key={item.id} className="py-2 px-3 bg-gray-100 rounded-md">
                <div className="text-xs font-semibold text-gray-600 uppercase tracking-wide">
                  {item.name}
                </div>
              </div>
            );
          }

          const page = item;
          const isCurrentPage = page.path === currentPage?.path;
          const pageIndex = orderedPages.findIndex(p => p.path === page.path);
          const isDraggedOver = draggedIndex !== null && draggedIndex !== pageIndex;
          
          return (
            <div
              key={page.path || index}
              draggable
              onDragStart={(e) => handleDragStart(e, pageIndex)}
              onDragEnd={handleDragEnd}
              onDragOver={handleDragOver}
              onDrop={(e) => handleDrop(e, pageIndex)}
              className={`
                flex items-center gap-3 p-3 border rounded-lg cursor-move transition-all
                ${isCurrentPage 
                  ? 'bg-yellow-50 border-yellow-300 ring-2 ring-yellow-200' 
                  : 'bg-white border-gray-200 hover:border-gray-300'
                }
                ${isDraggedOver ? 'border-blue-400 bg-blue-50' : ''}
              `}
            >
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

              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <span className="text-sm">
                    {page.type === 'folder' ? '📁' : '📄'}
                  </span>
                  
                  <span className={`font-medium truncate ${
                    isCurrentPage ? 'text-yellow-800' : 'text-gray-800'
                  }`}>
                    {page.title}
                  </span>

                  {isCurrentPage && (
                    <span className="bg-yellow-200 text-yellow-800 text-xs px-2 py-1 rounded-full font-medium">
                      current
                    </span>
                  )}
                </div>
                
                <div className="text-xs text-gray-500 truncate mt-1">
                  {page.path}
                </div>
              </div>

              <div className="text-xs text-gray-400 bg-gray-100 px-2 py-1 rounded">
                #{pageIndex + 1}
              </div>
            </div>
          );
        })}
      </div>

      {/* Save Order Button */}
      <div className="flex items-center justify-between pt-4 border-t border-gray-200">
        <div className="text-sm text-gray-600">
          {orderedPages.length} page{orderedPages.length !== 1 ? 's' : ''} in this section
        </div>
        
        <button
          onClick={handleSaveOrder}
          disabled={!hasOrderChanges()}
          className={`px-4 py-2 rounded-md font-medium transition-colors ${
            hasOrderChanges()
              ? 'bg-blue-600 text-white hover:bg-blue-700'
              : 'bg-gray-300 text-gray-500 cursor-not-allowed'
          }`}
        >
          Apply Order Changes
        </button>
      </div>
    </div>
  );

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-white z-50 flex">
      {/* Left Panel - Form */}
      <div className="w-1/2 border-r border-gray-200 flex flex-col">
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200 bg-white">
          <h2 className="text-lg font-semibold text-gray-900">
            Page Management
          </h2>
          <button
            onClick={handleClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
            aria-label="Close"
          >
            ✕
          </button>
        </div>

        {/* Tabs */}
        <div className="flex border-b border-gray-200 bg-white">
          <button
            onClick={() => setActiveTab('path')}
            className={`flex-1 px-4 py-3 text-sm font-medium transition-colors ${
              activeTab === 'path'
                ? 'text-blue-600 border-b-2 border-blue-600 bg-blue-50' 
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Change file path and URL
          </button>
          <button
            onClick={() => setActiveTab('order')}
            className={`flex-1 px-4 py-3 text-sm font-medium transition-colors ${
              activeTab === 'order'
                ? 'text-blue-600 border-b-2 border-blue-600 bg-blue-50' 
                : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            Change page order
          </button>
        </div>

        {/* Tab Content */}
        <div className="flex-1 overflow-auto bg-gray-50">
          {activeTab === 'path' ? renderPathTab() : renderOrderTab()}
        </div>
      </div>

      {/* Right Panel - Changed Files & Diff */}
      <div className="w-1/2 flex flex-col bg-gray-25">
        {hasAppliedChanges ? (
          <>
            <div className="p-4 border-b border-gray-200 bg-white">
              <h3 className="text-lg font-semibold text-gray-900">Changed Files</h3>
              <p className="text-sm text-gray-600 mt-1">
                Files that will be modified by your changes
              </p>
            </div>
            
            <ChangedFilesList 
              files={changedFiles}
              selectedFile={selectedFile}
              onFileSelect={handleFileSelect}
            />
            
            <DiffViewer 
              file={selectedFile}
            />
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center text-gray-500">
            <div className="text-center">
              <div className="text-4xl mb-4">📄</div>
              <p className="text-lg">Apply changes to see affected files</p>
              <p className="text-sm mt-2">Changes will show up here after you click "Apply Changes"</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default FullscreenPageManagement;