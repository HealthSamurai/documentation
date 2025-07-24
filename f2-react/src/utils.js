// Utility functions for parsing DOM and server communication

/**
 * Parse the existing navigation DOM into a tree structure
 * @param {Element} navigationElement - The #navigation element
 * @returns {Array} Array of navigation tree items
 */
export function parseNavigationDOM(navigationElement) {
  const parseItem = (element) => {
    if (element.tagName === 'DETAILS') {
      // Handle collapsible section (details element)
      const summary = element.querySelector('summary');
      const summaryLink = summary?.querySelector('a');
      const childContainer = element.querySelector('div.ml-6');
      
      const item = {
        id: Math.random().toString(36).substr(2, 9),
        type: 'section',
        title: summaryLink?.textContent?.trim() || summary?.textContent?.trim() || 'Untitled Section',
        href: summaryLink?.href || '',
        isOpen: element.hasAttribute('open'),
        children: []
      };
      
      if (childContainer) {
        // Parse children from the container
        const childElements = Array.from(childContainer.children);
        for (const child of childElements) {
          if (child.tagName === 'A') {
            // Direct link child
            item.children.push({
              id: Math.random().toString(36).substr(2, 9),
              type: 'page',
              title: child.textContent?.trim() || 'Untitled Page',
              href: child.href || '',
              children: []
            });
          } else if (child.tagName === 'DETAILS') {
            // Nested section
            item.children.push(parseItem(child));
          }
        }
      }
      
      return item;
    } else if (element.tagName === 'A') {
      // Handle direct link
      return {
        id: Math.random().toString(36).substr(2, 9),
        type: 'page',
        title: element.textContent?.trim() || 'Untitled Page',
        href: element.href || '',
        children: []
      };
    }
    
    return null;
  };

  const result = [];
  
  // Parse top-level navigation sections
  const sections = navigationElement.querySelectorAll('div.break-words');
  
  for (const section of sections) {
    const items = Array.from(section.children);
    
    for (const item of items) {
      if (item.tagName === 'DETAILS' || item.tagName === 'A') {
        const parsed = parseItem(item);
        if (parsed) {
          result.push(parsed);
        }
      }
    }
  }
  
  console.log('📋 Parsed navigation tree:', result);
  return result;
}

/**
 * Send navigation changes to the server
 * @param {Array} changes - Array of changes to apply
 * @returns {Promise} Promise that resolves when changes are saved
 */
export async function saveNavigationChanges(changes) {
  try {
    const response = await fetch('/api/navigation/save', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        changes: changes,
        timestamp: Date.now()
      })
    });
    
    if (!response.ok) {
      throw new Error(`Server error: ${response.status}`);
    }
    
    const result = await response.json();
    console.log('✅ Navigation changes saved:', result);
    return result;
  } catch (error) {
    console.error('❌ Failed to save navigation changes:', error);
    throw error;
  }
}

/**
 * Convert tree changes to file system operations
 * @param {Object} oldTree - Original tree structure
 * @param {Object} newTree - Modified tree structure
 * @returns {Array} Array of file operations
 */
export function calculateTreeChanges(oldTree, newTree) {
  const changes = [];
  
  // This is a simplified version - in reality we'd need more sophisticated diffing
  // For now, we'll just track the new structure
  const extractPaths = (items, parentPath = '') => {
    const paths = [];
    
    for (const item of items) {
      const currentPath = parentPath ? `${parentPath}/${item.title}` : item.title;
      
      paths.push({
        type: item.type,
        title: item.title,
        href: item.href,
        path: currentPath,
        isOpen: item.isOpen
      });
      
      if (item.children && item.children.length > 0) {
        paths.push(...extractPaths(item.children, currentPath));
      }
    }
    
    return paths;
  };
  
  const newPaths = extractPaths(newTree);
  
  changes.push({
    type: 'structure_update',
    data: newPaths
  });
  
  return changes;
}