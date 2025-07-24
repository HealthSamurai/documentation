class FileOperationsService {
  constructor() {
    this.baseUrl = window.location.origin;
  }

  /**
   * Get current page information from URI
   */
  async getCurrentPageInfo(uri) {
    try {
      const response = await fetch(`${this.baseUrl}/dev/page-info?uri=${encodeURIComponent(uri)}`);
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error getting current page info:', error);
      throw error;
    }
  }

  /**
   * Preview new URL based on file path
   */
  async previewNewUrl(newFilePath) {
    try {
      const response = await fetch(`${this.baseUrl}/dev/preview-url`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          'new-file-path': newFilePath
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      return result['new-url'] || result.newUrl;
    } catch (error) {
      console.error('Error previewing URL:', error);
      throw error;
    }
  }

  /**
   * Validate file path
   */
  async validateFilePath(filePath) {
    try {
      const response = await fetch(`${this.baseUrl}/api/validate-move`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          to: filePath
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error validating file path:', error);
      return { valid: false, error: error.message };
    }
  }

  /**
   * Relocate page (move/rename)
   */
  async relocatePage(currentFilePath, newFilePath) {
    try {
      const response = await fetch(`${this.baseUrl}/dev/relocate-page`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          'current-filepath': currentFilePath,
          'new-filepath': newFilePath
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      
      // Transform the response to match our expected format
      return {
        success: result.success,
        error: result.error,
        oldFilepath: result['old-filepath'] || result.oldFilepath,
        newFilepath: result['new-filepath'] || result.newFilepath,
        oldUrl: result['old-url'] || result.oldUrl,
        newUrl: result['new-url'] || result.newUrl,
        updatedReferenceFiles: result['updated-reference-files'] || result.updatedReferenceFiles || 0,
        referenceFiles: result['reference-files'] || result.referenceFiles || [],
        changedFiles: this.generateChangedFilesList(result)
      };
    } catch (error) {
      console.error('Error relocating page:', error);
      throw error;
    }
  }

  /**
   * Generate list of changed files from relocation result
   */
  generateChangedFilesList(result) {
    const files = [];

    // Always include SUMMARY.md
    files.push({
      path: 'docs/SUMMARY.md',
      type: 'modified',
      description: 'Updated navigation structure'
    });

    // Include .gitbook.yaml if URLs changed
    if (result['old-url'] !== result['new-url']) {
      files.push({
        path: '.gitbook.yaml',
        type: 'modified',
        description: `Added redirect: ${result['old-url']} → ${result['new-url']}`
      });
    }

    // Include the moved/renamed file
    if (result['old-filepath'] && result['new-filepath']) {
      const isSameDirectory = this.getDirectory(result['old-filepath']) === this.getDirectory(result['new-filepath']);
      const isSameFilename = this.getFilename(result['old-filepath']) === this.getFilename(result['new-filepath']);

      files.push({
        path: `docs/${result['new-filepath']}`,
        type: isSameDirectory && !isSameFilename ? 'renamed' : 'moved',
        description: isSameDirectory && !isSameFilename 
          ? `Renamed from ${this.getFilename(result['old-filepath'])}` 
          : `Moved from docs/${result['old-filepath']}`
      });
    }

    // Include all reference files that were updated
    if (result['reference-files'] && Array.isArray(result['reference-files'])) {
      result['reference-files'].forEach(filePath => {
        files.push({
          path: filePath,
          type: 'modified',
          description: 'Updated internal references'
        });
      });
    }

    return files;
  }

  /**
   * Get directory path from file path
   */
  getDirectory(filePath) {
    const lastSlash = filePath.lastIndexOf('/');
    return lastSlash === -1 ? '' : filePath.substring(0, lastSlash);
  }

  /**
   * Get filename from file path
   */
  getFilename(filePath) {
    const lastSlash = filePath.lastIndexOf('/');
    return lastSlash === -1 ? filePath : filePath.substring(lastSlash + 1);
  }

  /**
   * Get file diff (mock implementation - could be extended to fetch real diffs)
   */
  async getFileDiff(filePath) {
    // For now, return mock diff data
    // This could be extended to call a backend endpoint that provides git diffs
    return {
      language: this.detectLanguage(filePath),
      changes: [
        {
          type: 'info',
          content: `File: ${filePath}`,
          lineNumber: null
        },
        {
          type: 'info',
          content: 'Real diff data would be available here after implementing git diff endpoint',
          lineNumber: null
        }
      ]
    };
  }

  /**
   * Detect file language for syntax highlighting
   */
  detectLanguage(filePath) {
    const extension = filePath.split('.').pop()?.toLowerCase();
    const languageMap = {
      'md': 'markdown',
      'js': 'javascript',
      'jsx': 'javascript',
      'ts': 'typescript',
      'tsx': 'typescript',
      'json': 'json',
      'yaml': 'yaml',
      'yml': 'yaml',
      'css': 'css',
      'html': 'html',
      'clj': 'clojure',
      'cljs': 'clojure'
    };
    return languageMap[extension] || 'text';
  }

  /**
   * Reorganize documentation (enhanced API endpoint)
   */
  async reorganizeDocs(action, changes, timestamp) {
    try {
      const formData = new URLSearchParams();
      formData.append('action', action);
      formData.append('changes', JSON.stringify(changes));
      if (timestamp) {
        formData.append('timestamp', timestamp.toString());
      }

      const response = await fetch(`${this.baseUrl}/api/reorganize-docs`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      
      // Add changed files list if successful
      if (result.success && !result.changedFiles) {
        result.changedFiles = this.generateChangedFilesList(result);
      }

      return result;
    } catch (error) {
      console.error('Error reorganizing docs:', error);
      throw error;
    }
  }

  /**
   * Preview structure changes
   */
  async previewStructure(changes) {
    try {
      const formData = new URLSearchParams();
      formData.append('changes', JSON.stringify(changes));

      const response = await fetch(`${this.baseUrl}/api/preview-structure`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      return await response.json();
    } catch (error) {
      console.error('Error previewing structure:', error);
      throw error;
    }
  }
}

// Export singleton instance
export const fileOperationsService = new FileOperationsService();
export default fileOperationsService;