// Keyboard navigation for Previous/Next pages
document.addEventListener('keydown', function (event) {
  // Only handle arrow keys if not typing in an input field (except for search input)
  if ((event.target.tagName === 'INPUT' || event.target.tagName === 'TEXTAREA' || event.target.contentEditable === 'true') && event.target.id !== 'search-input') {
    return;
  }

  // Don't handle Alt+Arrow combinations (system hotkeys)
  if (event.altKey) {
    return;
  }

  // Special handling for search input dropdown navigation
  if (event.target.id === 'search-input') {
    const dropdown = document.getElementById('search-dropdown');

    // Check if dropdown is visible and has content
    if (dropdown && (dropdown.hasAttribute('data-search-dropdown') || dropdown.querySelector('a[data-search-result-index]'))) {
      const results = dropdown.querySelectorAll('a[data-search-result-index]');

      // Only handle navigation keys if dropdown has visible results
      if (results.length > 0 && (event.key === 'ArrowDown' || event.key === 'ArrowUp' || event.key === 'Enter')) {
        let currentIndex = -1;

        // Find currently selected item
        for (let i = 0; i < results.length; i++) {
          if (results[i].classList.contains('bg-warning-2')) {
            currentIndex = i;
            break;
          }
        }

        if (event.key === 'ArrowDown') {
          event.preventDefault();
          // If no item selected, select first item (0), otherwise move down
          currentIndex = currentIndex < results.length - 1 ? currentIndex + 1 : 0;
          updateSelectedResult(results, currentIndex);
          return;
        } else if (event.key === 'ArrowUp') {
          event.preventDefault();
          // If no item selected, select last item, otherwise move up
          currentIndex = currentIndex <= 0 ? results.length - 1 : currentIndex - 1;
          updateSelectedResult(results, currentIndex);
          return;
        } else if (event.key === 'Enter') {
          event.preventDefault();
          if (currentIndex >= 0 && currentIndex < results.length) {
            results[currentIndex].click();
            event.target.blur(); // Remove focus from search input after navigation
          }
          return;
        }
      }

      // Handle Escape regardless of results
      if (event.key === 'Escape') {
        event.preventDefault();
        dropdown.innerHTML = '';
        return;
      }
    }
  }

  // F2 key (Toggle fullscreen navigation - only in dev mode)
  if (event.key === 'F2' && window.DEV_MODE) {
    event.preventDefault();
    console.log('F2 pressed, DEV_MODE:', window.DEV_MODE);
    toggleFullscreenNavigation();
    return;
  }

  // Left arrow key (Previous page)
  if (event.key === 'ArrowLeft') {
    // Find prev button - look for buttons with HTMX attributes first
    let prevButton = null;

    // Try to find HTMX-enabled prev button
    const htmxButtons = document.querySelectorAll('a[hx-get][href]');
    for (const btn of htmxButtons) {
      if (btn.textContent.includes('Previous')) {
        prevButton = btn;
        break;
      }
    }

    // Fallback to any prev button
    if (!prevButton) {
      const allButtons = document.querySelectorAll('a[href*="/"]');
      for (const btn of allButtons) {
        if (btn.textContent.includes('Previous')) {
          prevButton = btn;
          break;
        }
      }
    }

    if (prevButton) {
      event.preventDefault();

      // Use HTMX if available and button has HTMX attributes
      if (typeof htmx !== 'undefined' && prevButton.hasAttribute('hx-get')) {
        prevButton.click(); // Trigger HTMX request
      } else {
        // Fallback to direct navigation
        window.location.href = prevButton.href;
      }
    }
  }

  // Right arrow key (Next page)
  if (event.key === 'ArrowRight') {
    // Find next button - look for buttons with HTMX attributes first
    let nextButton = null;

    // Try to find HTMX-enabled next button
    const htmxButtons = document.querySelectorAll('a[hx-get][href]');
    for (const btn of htmxButtons) {
      if (btn.textContent.includes('Next')) {
        nextButton = btn;
        break;
      }
    }

    // Fallback to any next button
    if (!nextButton) {
      const allButtons = document.querySelectorAll('a[href*="/"]');
      for (const btn of allButtons) {
        if (btn.textContent.includes('Next')) {
          nextButton = btn;
          break;
        }
      }
    }

    if (nextButton) {
      event.preventDefault();

      // Use HTMX if available and button has HTMX attributes
      if (typeof htmx !== 'undefined' && nextButton.hasAttribute('hx-get')) {
        nextButton.click(); // Trigger HTMX request
      } else {
        // Fallback to direct navigation
        window.location.href = nextButton.href;
      }
    }
  }
});

// Helper function to update selected result in dropdown
function updateSelectedResult(results, selectedIndex) {
  results.forEach((result, index) => {
    // Check if this is a section entry (has border-l-2 element inside)
    const isSectionEntry = result.querySelector('.border-l-2') !== null;

    if (index === selectedIndex) {
      result.classList.add('bg-warning-2', 'text-tint-strong');
      result.classList.remove('hover:bg-tint-2', 'hover:text-tint-strong');

      // Remove appropriate text color based on entry type
      if (isSectionEntry) {
        result.classList.remove('text-tint-10');
        // Also update all child p elements with text-tint-10 class (these contain the actual text)
        const childElements = result.querySelectorAll('p.text-tint-10');
        childElements.forEach(child => {
          child.classList.remove('text-tint-10');
          child.classList.add('text-tint-strong');
        });
      } else {
        result.classList.remove('text-tint');
      }

      // Update the file icon color
      const fileIcon = result.querySelector('div:first-of-type');
      if (fileIcon) {
        fileIcon.classList.add('text-warning-9');
        fileIcon.classList.remove('text-tint-9', 'group-hover:text-tint-strong');
      }
      // Update the enter icon - replace with Enter key
      const enterIcon = result.querySelector('.search-action-button');
      if (enterIcon) {
        enterIcon.classList.add('text-orange-600', 'opacity-100');
        enterIcon.classList.remove('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
        enterIcon.innerHTML = '<div class="px-2 py-1 rounded border text-xs font-medium flex items-center justify-center bg-orange-600 text-white border-orange-700">↵</div>';
      }
      // Scroll selected item into view
      result.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    } else {
      result.classList.remove('bg-warning-2', 'text-tint-strong');

      // Restore appropriate text color based on entry type
      if (isSectionEntry) {
        result.classList.add('text-tint-10');
        // Also restore all child p elements with text-tint-strong class back to text-tint-10
        const childElements = result.querySelectorAll('p.text-tint-strong');
        childElements.forEach(child => {
          child.classList.remove('text-tint-strong');
          child.classList.add('text-tint-10');
        });
      } else {
        result.classList.add('text-tint');
      }

      // Update the file icon color
      const fileIcon = result.querySelector('div:first-of-type');
      if (fileIcon) {
        fileIcon.classList.remove('text-warning-9');
        fileIcon.classList.add('text-tint-9', 'group-hover:text-tint-strong');
      }
      // Update the enter icon - restore arrow icon
      const enterIcon = result.querySelector('.search-action-button');
      if (enterIcon) {
        enterIcon.classList.remove('text-orange-600', 'opacity-100');
        enterIcon.classList.add('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
        enterIcon.innerHTML = '<svg class="size-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"></path></svg>';
      }
    }
  });
}

document.addEventListener('DOMContentLoaded', function () {
  // Focus search input if on search page
  const searchInput = document.getElementById('search-input');
  if (searchInput && window.location.pathname === '/search') {
    searchInput.focus();
  }

  // Handle search shortcut visibility
  const searchShortcut = document.getElementById('search-shortcut');
  if (searchInput && searchShortcut) {
    // Hide shortcut when input has focus or content
    function updateShortcutVisibility() {
      if (searchInput.value.length > 0 || document.activeElement === searchInput) {
        searchShortcut.style.opacity = '0';
      } else {
        searchShortcut.style.opacity = '1';
      }
    }

    // Add event listeners
    searchInput.addEventListener('focus', updateShortcutVisibility);
    searchInput.addEventListener('blur', updateShortcutVisibility);
    searchInput.addEventListener('input', updateShortcutVisibility);

    // Initial check
    updateShortcutVisibility();
  }

  // Keyboard shortcuts
  document.addEventListener('keydown', function (e) {
    // Cmd/Ctrl + K for search
    if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
      e.preventDefault();
      const searchInput = document.getElementById('search-input');
      if (searchInput) {
        searchInput.focus();
        searchInput.select();
      } else {
        // Fallback to search page if no search input found
        window.location.href = '/search';
      }
    }

    // ESC key to clear search results on main search page
    if (e.key === 'Escape' && window.location.pathname === '/search') {
      const searchInput = document.getElementById('search-input');
      const searchResults = document.getElementById('search-results');
      if (searchInput && searchResults) {
        e.preventDefault();
        searchInput.value = '';
        searchResults.innerHTML = '<div class="text-center text-tint-9 py-8"><div class="text-lg font-medium mb-2">Search Documentation</div></div>';
        searchInput.focus();
      }
    }
  });

  // Handle Ctrl+click to open links in new tab
  document.addEventListener('click', function (e) {
    // Check if Ctrl key (or Cmd on Mac) is pressed during click
    if (e.ctrlKey || e.metaKey) {
      const target = e.target.closest('a[href]');
      if (target && target.href) {
        // Prevent HTMX from handling this click
        e.stopPropagation();
        e.preventDefault();

        // Open link in new tab using browser's default behavior
        window.open(target.href, '_blank');
      }
    }
  }, true); // Use capture phase to handle before HTMX

  // Close search dropdown when clicking outside
  document.addEventListener('click', function (e) {
    const searchInput = document.getElementById('search-input');
    const dropdown = document.getElementById('search-dropdown');

    if (searchInput && dropdown && !searchInput.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.innerHTML = '';
    }
  });

  // Initialize F2 fullscreen navigation functionality if in dev mode
  console.log('DEV_MODE:', window.DEV_MODE);
  if (window.DEV_MODE) {
    console.log('Initializing F2 fullscreen navigation functionality');
    initializeFullscreenNavigation();
  } else {
    console.log('DEV_MODE is false, F2 functionality not initialized');
  }
});

// F2 Fullscreen Navigation Functionality
let isFullscreenMode = false;
let originalContent = null;
let fullscreenElements = null;

function toggleFullscreenNavigation() {
  console.log('toggleFullscreenNavigation called, current mode:', isFullscreenMode);

  if (isFullscreenMode) {
    exitFullscreenMode();
  } else {
    enterFullscreenMode();
  }
}

function enterFullscreenMode() {
  console.log('Entering fullscreen navigation mode');

  // Save original content
  originalContent = document.body.innerHTML;

  // Get left navigation content
  const leftNav = document.querySelector('#navigation');
  if (!leftNav) {
    console.error('Left navigation not found');
    return;
  }

  // Create fullscreen navigation with DOM elements
  fullscreenElements = createFullscreenNavigation(leftNav);

  // Clear body and append elements
  document.body.innerHTML = '';
  document.body.appendChild(fullscreenElements.container);
  document.body.appendChild(fullscreenElements.controlPanel);
  document.body.appendChild(fullscreenElements.exitInfo);

  // Add fullscreen styles
  document.body.style.cssText = `
    margin: 0;
    padding: 20px;
    background: white;
    overflow-x: auto;
    height: 100vh;
    box-sizing: border-box;
  `;

  isFullscreenMode = true;
  console.log('Fullscreen mode activated');
}

function exitFullscreenMode() {
  console.log('Exiting fullscreen navigation mode');

  if (originalContent) {
    document.body.innerHTML = originalContent;
    document.body.style.cssText = '';
    originalContent = null;
    fullscreenElements = null;
  }

  isFullscreenMode = false;
  console.log('Normal mode restored');
}

function createFullscreenNavigation(leftNav) {
  // Clone the navigation
  const navClone = leftNav.cloneNode(true);

  // Expand all <details> elements to show full structure
  const allDetails = navClone.querySelectorAll('details');
  allDetails.forEach(detail => {
    detail.setAttribute('open', '');
  });

  // Initialize SortableJS for all navigation elements
  initializeSortableJS(navClone);

  // Create fullscreen container with CSS columns
  const container = document.createElement('div');
  container.style.cssText = `
    columns: auto;
    column-width: 300px;
    column-gap: 10px;
    column-fill: auto;
    height: calc(100vh - 80px);
    overflow-x: auto;
    overflow-y: hidden;
    padding: 20px;
    font-size: 14px;
  `;

  // Style the cloned navigation for better readability
  navClone.style.cssText = `
    width: auto;
    max-width: none;
    height: auto;
    max-height: none;
    position: static;
    overflow: visible;
    background: transparent;
    border: none;
    margin: 0;
    padding: 0;
  `;

  // Reduce spacing between navigation elements
  const allNavElements = navClone.querySelectorAll('*');
  allNavElements.forEach(element => {
    // Reduce margins and padding for compactness
    if (element.tagName === 'DIV' && element.classList.contains('space-y-4')) {
      element.style.cssText = 'margin-bottom: 10px;';
    }
    if (element.tagName === 'DIV' && element.classList.contains('mt-4')) {
      element.style.cssText = 'margin-top: 8px; margin-bottom: 4px;';
    }
    if (element.tagName === 'DETAILS') {
      element.style.cssText = 'margin-bottom: 8px; break-inside: avoid;';
    }
    if (element.tagName === 'SUMMARY') {
      element.style.cssText = 'margin-bottom: 4px;';
    }
  });

  // Add navigation content
  container.appendChild(navClone);

  // Create control panel with working buttons
  const controlPanel = createControlPanel();

  // Add exit instruction
  const exitInfo = document.createElement('div');
  exitInfo.style.cssText = `
    position: fixed;
    top: 10px;
    right: 20px;
    background: #f0f0f0;
    padding: 10px 20px;
    border-radius: 5px;
    font-size: 14px;
    color: #666;
    z-index: 1000;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  `;
  exitInfo.textContent = 'Press F2 to exit fullscreen navigation';

  // Find and focus on current page
  focusCurrentPage(navClone);

  // Return DOM elements instead of HTML string
  return {
    container: container,
    controlPanel: controlPanel,
    exitInfo: exitInfo
  };
}

// SortableJS Implementation
function initializeSortableJS(navElement) {
  console.log('Initializing SortableJS for navigation tree');

  // Find all sortable containers (details elements and navigation root)
  const sortableContainers = navElement.querySelectorAll('details, #navigation');

  sortableContainers.forEach(container => {
    // Skip if already initialized
    if (container.hasAttribute('data-sortable-initialized')) {
      return;
    }

    // Create sortable instance
    const sortable = new Sortable(container, {
      group: {
        name: 'tree-docs',
        pull: true,
        put: function(to, from, dragEl, evt) {
          // Allow dropping anywhere in the tree
          return true;
        }
      },
      animation: 150,
      fallbackOnBody: true,
      swapThreshold: 0.65,
      
      // Visual feedback classes
      ghostClass: 'sortable-ghost',
      chosenClass: 'sortable-chosen',
      dragClass: 'sortable-drag',
      
      // Handle drag and drop events
      onStart: function(evt) {
        console.log('📦 Drag started:', evt.item);
        evt.item.classList.add('dragging');
      },
      
      onEnd: function(evt) {
        console.log('🎯 Drag ended:', evt.item);
        evt.item.classList.remove('dragging');
        
        // Handle the drop logic
        handleSortableDrop(evt);
      },
      
      onMove: function(evt) {
        const related = evt.related;
        const dragged = evt.dragged;
        
        // Prevent dropping parent inside its own children
        if (dragged.contains && dragged.contains(related)) {
          return false;
        }
        
        return true;
      }
    });

    // Mark as initialized
    container.setAttribute('data-sortable-initialized', 'true');
    console.log('✅ SortableJS initialized for:', container);
  });

  // Add CSS for visual feedback
  addSortableCSS();
}

function addSortableCSS() {
  // Check if CSS is already added
  if (document.getElementById('sortable-css')) {
    return;
  }

  const style = document.createElement('style');
  style.id = 'sortable-css';
  style.textContent = `
    .sortable-ghost {
      opacity: 0.5;
      background: #e3f2fd;
      border: 2px dashed #1976d2;
      border-radius: 4px;
    }
    
    .sortable-chosen {
      background: #fff3e0;
      border: 2px solid #ff9800;
      border-radius: 4px;
    }
    
    .sortable-drag {
      opacity: 0.7;
      transform: rotate(5deg);
    }
    
    .dragging {
      cursor: grabbing !important;
    }
    
    /* Highlight drop zones */
    .sortable-ghost + * {
      border-top: 2px solid #4caf50;
    }
    
    /* Nested containers styling */
    details[data-sortable-initialized] {
      min-height: 20px;
    }
    
    details[data-sortable-initialized]:empty {
      background: #f5f5f5;
      border: 1px dashed #ccc;
      border-radius: 4px;
      padding: 10px;
    }
    
    details[data-sortable-initialized]:empty::before {
      content: "Drop items here";
      color: #999;
      font-style: italic;
    }
  `;
  
  document.head.appendChild(style);
  console.log('✅ Sortable CSS added');
}

function handleSortableDrop(evt) {
  console.log('🔄 Handling sortable drop:', evt);
  
  const item = evt.item;
  const from = evt.from;
  const to = evt.to;
  const oldIndex = evt.oldIndex;
  const newIndex = evt.newIndex;
  
  console.log('📊 Drop details:', {
    item: item.tagName,
    from: from.tagName,
    to: to.tagName,
    oldIndex,
    newIndex
  });
  
  // Check if we need to convert leaf to parent
  if (shouldConvertToParent(item, to)) {
    console.log('🌱 Converting leaf to parent');
    convertLeafToParent(item, to);
  }
  
  // Remove empty parents
  cleanupEmptyParents(from);
  
  // Log the new structure for backend sync
  logTreeStructure(item, to, newIndex);
}

function shouldConvertToParent(draggedItem, dropTarget) {
  // Check if we're dropping onto a leaf element (not a details element)
  return dropTarget.tagName === 'A' || 
         (dropTarget.tagName === 'DIV' && !dropTarget.closest('details'));
}

function convertLeafToParent(draggedItem, leafTarget) {
  console.log('🔄 Converting leaf to parent:', { draggedItem, leafTarget });
  
  // Create new details structure
  const details = document.createElement('details');
  details.setAttribute('open', '');
  
  // Create summary from the leaf
  const summary = document.createElement('summary');
  summary.innerHTML = leafTarget.innerHTML;
  summary.className = leafTarget.className;
  
  // Create container for children
  const childrenContainer = document.createElement('div');
  childrenContainer.className = 'ml-6 border-l-2 border-tint-8';
  childrenContainer.appendChild(draggedItem);
  
  // Assemble structure
  details.appendChild(summary);
  details.appendChild(childrenContainer);
  
  // Replace leaf with parent
  leafTarget.parentNode.replaceChild(details, leafTarget);
  
  // Initialize SortableJS for the new container
  initializeSortableJS(details);
  
  console.log('✅ Leaf converted to parent');
}

function cleanupEmptyParents(container) {
  if (container.tagName === 'DETAILS') {
    const childrenContainer = container.querySelector('div');
    if (childrenContainer && childrenContainer.children.length === 0) {
      // Convert back to leaf
      const summary = container.querySelector('summary');
      if (summary) {
        const leafElement = document.createElement('div');
        leafElement.innerHTML = summary.innerHTML;
        leafElement.className = summary.className;
        
        container.parentNode.replaceChild(leafElement, container);
        console.log('🧹 Empty parent converted back to leaf');
      }
    }
  }
}

function logTreeStructure(item, container, index) {
  console.log('📋 Tree structure update:', {
    itemId: item.dataset.id || item.textContent.trim(),
    containerId: container.dataset.id || container.id,
    newIndex: index,
    timestamp: new Date().toISOString()
  });
  
  // Send update to backend via HTMX
  if (typeof htmx !== 'undefined') {
    htmx.ajax('POST', '/api/reorder-docs', {
      values: {
        itemId: item.dataset.id || item.textContent.trim(),
        parentId: container.dataset.id || container.id,
        newIndex: index,
        timestamp: new Date().toISOString()
      }
    });
    console.log('🚀 HTMX request sent to backend');
  } else {
    console.warn('⚠️ HTMX not available, skipping backend sync');
  }
}

// Control functions for sections
function openAllSections() {
  const allDetails = document.querySelectorAll('details');
  allDetails.forEach(detail => {
    detail.setAttribute('open', '');
  });
}

function closeAllSections() {
  const allDetails = document.querySelectorAll('details');
  allDetails.forEach(detail => {
    detail.removeAttribute('open');
  });
}

function createControlButton(text, onClick) {
  const button = document.createElement('button');
  button.textContent = text;
  button.onclick = onClick;
  button.style.cssText = `
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 4px;
    padding: 6px 12px;
    font-size: 12px;
    cursor: pointer;
    transition: all 0.2s;
    font-weight: 500;
  `;

  // Hover effects
  button.addEventListener('mouseenter', () => {
    button.style.background = '#e9ecef';
    button.style.borderColor = '#adb5bd';
  });

  button.addEventListener('mouseleave', () => {
    button.style.background = '#f8f9fa';
    button.style.borderColor = '#dee2e6';
  });

  return button;
}

function createControlPanel() {
  const controlPanel = document.createElement('div');
  controlPanel.style.cssText = `
    position: fixed;
    top: 20px;
    left: 20px;
    z-index: 1001;
    display: flex;
    gap: 10px;
    background: white;
    padding: 10px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    border: 1px solid #ddd;
  `;

  const openAllBtn = createControlButton('Open all', openAllSections);
  const closeAllBtn = createControlButton('Close all', closeAllSections);

  controlPanel.appendChild(openAllBtn);
  controlPanel.appendChild(closeAllBtn);

  return controlPanel;
}

function focusCurrentPage(navClone) {
  // Find the currently active page (with 'active' class)
  const activePage = navClone.querySelector('.active, [class*="active"]');
  if (activePage) {
    // Add a small delay to ensure the DOM is ready
    setTimeout(() => {
      activePage.scrollIntoView({ 
        behavior: 'smooth', 
        block: 'center',
        inline: 'center'
      });
      // Add temporary highlighting
      activePage.style.boxShadow = '0 0 10px rgba(255, 0, 0, 0.5)';
      activePage.style.borderRadius = '4px';
      setTimeout(() => {
        activePage.style.boxShadow = '';
        activePage.style.borderRadius = '';
      }, 2000);
    }, 100);
  }
}

function initializeFullscreenNavigation() {
  console.log('Fullscreen navigation initialized');
}