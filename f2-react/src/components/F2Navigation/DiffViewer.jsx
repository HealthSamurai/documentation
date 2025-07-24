import React, { useState, useEffect } from 'react';
import { fileOperationsService } from './services/FileOperationsService';

const DiffViewer = ({ file }) => {
  const [diff, setDiff] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  // Load diff when file changes
  useEffect(() => {
    if (file) {
      loadFileDiff(file);
    } else {
      setDiff(null);
      setError(null);
    }
  }, [file]);

  const loadFileDiff = async (file) => {
    setIsLoading(true);
    setError(null);
    
    try {
      const diffData = await fileOperationsService.getFileDiff(file.path);
      setDiff(diffData);
    } catch (error) {
      console.error('Error loading diff:', error);
      setError(error.message);
      // Fallback to mock diff
      setDiff(generateMockDiff(file));
    } finally {
      setIsLoading(false);
    }
  };

  // Generate mock diff content based on file type and path
  const generateMockDiff = (file) => {
    if (!file) return null;

    if (file.path === 'docs/SUMMARY.md') {
      return {
        language: 'markdown',
        changes: [
          { type: 'context', content: '# Table of contents', lineNumber: 1 },
          { type: 'context', content: '', lineNumber: 2 },
          { type: 'context', content: '* [Aidbox FHIR platform documentation](readme/README.md)', lineNumber: 3 },
          { type: 'context', content: '  * [Features](readme/features.md)', lineNumber: 4 },
          { type: 'removed', content: '  * [Old API Overview](api/old-overview.md)', lineNumber: 5 },
          { type: 'added', content: '  * [New API Overview](api/new-overview.md)', lineNumber: 6 },
          { type: 'context', content: '  * [Architecture](readme/architecture.md)', lineNumber: 7 },
          { type: 'context', content: '* [Getting Started](getting-started/README.md)', lineNumber: 8 },
        ]
      };
    }

    if (file.path === 'docs/.gitbook.yaml') {
      return {
        language: 'yaml',
        changes: [
          { type: 'context', content: 'title: Aidbox Documentation', lineNumber: 1 },
          { type: 'context', content: 'description: Complete Aidbox documentation', lineNumber: 2 },
          { type: 'context', content: '', lineNumber: 3 },
          { type: 'context', content: 'structure:', lineNumber: 4 },
          { type: 'removed', content: '  readme: README.md', lineNumber: 5 },
          { type: 'added', content: '  readme: docs/README.md', lineNumber: 6 },
          { type: 'context', content: '  summary: SUMMARY.md', lineNumber: 7 },
        ]
      };
    }

    if (file.path.endsWith('.md')) {
      return {
        language: 'markdown',
        changes: [
          { type: 'context', content: '# API Overview', lineNumber: 1 },
          { type: 'context', content: '', lineNumber: 2 },
          { type: 'context', content: 'This document provides an overview of the Aidbox API.', lineNumber: 3 },
          { type: 'context', content: '', lineNumber: 4 },
          { type: 'removed', content: 'See also: [Old Reference](old-reference.md)', lineNumber: 5 },
          { type: 'added', content: 'See also: [New Reference](new-reference.md)', lineNumber: 6 },
          { type: 'context', content: '', lineNumber: 7 },
          { type: 'context', content: '## Getting Started', lineNumber: 8 },
        ]
      };
    }

    return {
      language: 'text',
      changes: [
        { type: 'context', content: 'File content preview not available', lineNumber: 1 },
        { type: 'added', content: 'Changes will be applied when you save', lineNumber: 2 },
      ]
    };
  };

  if (!file) {
    return (
      <div className="flex-1 flex items-center justify-center text-gray-500 p-6">
        <div className="text-center">
          <div className="text-3xl mb-2">👁️</div>
          <p>Select a file to view changes</p>
        </div>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="flex-1 flex items-center justify-center text-gray-500 p-6">
        <div className="text-center">
          <div className="text-3xl mb-2">⏳</div>
          <p>Loading diff...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex-1 flex items-center justify-center text-red-500 p-6">
        <div className="text-center">
          <div className="text-3xl mb-2">⚠️</div>
          <p>Error loading diff: {error}</p>
          <button 
            onClick={() => loadFileDiff(file)}
            className="mt-2 text-sm text-blue-600 hover:text-blue-800"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  const getLineTypeStyles = (type) => {
    switch (type) {
      case 'added':
        return 'bg-green-50 border-l-4 border-green-400 text-green-800';
      case 'removed':
        return 'bg-red-50 border-l-4 border-red-400 text-red-800';
      case 'context':
        return 'bg-white border-l-4 border-transparent text-gray-700';
      default:
        return 'bg-white border-l-4 border-transparent text-gray-700';
    }
  };

  const getLinePrefix = (type) => {
    switch (type) {
      case 'added':
        return '+';
      case 'removed':
        return '-';
      case 'context':
        return ' ';
      default:
        return ' ';
    }
  };

  return (
    <div className="flex-1 flex flex-col bg-white">
      {/* Diff Header */}
      <div className="p-4 border-b border-gray-200 bg-gray-50">
        <div className="flex items-center justify-between">
          <div>
            <h4 className="font-medium text-gray-900">{file.path}</h4>
            <p className="text-sm text-gray-600 mt-1">{file.description}</p>
          </div>
          
          <div className="flex items-center gap-2">
            <span className={`
              inline-flex items-center px-2 py-1 rounded-full text-xs font-medium
              ${file.type === 'modified' ? 'bg-blue-100 text-blue-700' :
                file.type === 'moved' ? 'bg-green-100 text-green-700' :
                file.type === 'renamed' ? 'bg-purple-100 text-purple-700' :
                'bg-orange-100 text-orange-700'}
            `}>
              {file.type}
            </span>
            
            <span className="text-xs text-gray-500 font-mono">
              {diff?.language || 'text'}
            </span>
          </div>
        </div>
      </div>

      {/* Diff Content */}
      <div className="flex-1 overflow-auto">
        <div className="font-mono text-sm">
          {diff?.changes.map((change, index) => (
            <div
              key={index}
              className={`flex ${getLineTypeStyles(change.type)}`}
            >
              {/* Line Number */}
              <div className="w-12 px-2 py-1 text-gray-400 text-xs text-right flex-shrink-0 select-none">
                {change.lineNumber}
              </div>
              
              {/* Change Prefix */}
              <div className="w-6 px-1 py-1 text-center text-xs flex-shrink-0 select-none font-bold">
                {getLinePrefix(change.type)}
              </div>
              
              {/* Content */}
              <div className="flex-1 px-2 py-1 whitespace-pre-wrap break-all">
                {change.content}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Diff Stats */}
      <div className="p-3 border-t border-gray-200 bg-gray-50">
        <div className="flex items-center justify-between text-xs text-gray-600">
          <div className="flex items-center gap-4">
            <span className="flex items-center gap-1">
              <span className="w-2 h-2 bg-green-400 rounded-full"></span>
              {diff?.changes.filter(c => c.type === 'added').length || 0} additions
            </span>
            <span className="flex items-center gap-1">
              <span className="w-2 h-2 bg-red-400 rounded-full"></span>
              {diff?.changes.filter(c => c.type === 'removed').length || 0} deletions
            </span>
          </div>
          
          <div className="text-gray-500">
            {diff?.changes.length || 0} lines total
          </div>
        </div>
      </div>
    </div>
  );
};

export default DiffViewer;