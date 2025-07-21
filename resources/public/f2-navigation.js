// F2 Fullscreen Navigation Functionality
document.addEventListener('keydown', function (event) {
  // F2 key (Toggle fullscreen navigation)
  if (event.key === 'F2') {
    event.preventDefault();
    console.log('F2 pressed');
    toggleFullscreenNavigation();
    return;
  }
});



document.addEventListener('DOMContentLoaded', function () {
  // Initialize F2 fullscreen navigation functionality
  console.log('🚀 Initializing F2 fullscreen navigation functionality');
  initializeFullscreenNavigation();
});

// F2 Fullscreen Navigation Functionality
let isFullscreenMode = false;
let originalContent = null;
let fullscreenElements = null;
let horizontalScrollbar = null;
let documentTreeStructure = null;

// Global pending changes storage
let pendingChanges = {
  moves: [],      // Array of move operations
  renames: [],    // Array of rename operations
  reorders: []    // Array of reorder operations
};

function initializeFullscreenNavigation() {
  console.log('🔧 F2 fullscreen navigation initialized');
  console.log('📋 Checking required libraries...');
  
  // Check if required libraries are loaded
  if (typeof Sortable === 'undefined') {
    console.error('❌ SortableJS not loaded!');
    return false;
  } else {
    console.log('✅ SortableJS loaded:', Sortable.version || 'version unknown');
  }
  
  if (typeof Scrollbar === 'undefined') {
    console.warn('⚠️ Smooth-scrollbar not loaded, will use fallback');
  } else {
    console.log('✅ Smooth-scrollbar loaded');
  }
  

  
  return true;
}



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

  // Clean up scrollbar
  if (horizontalScrollbar) {
    try {
      horizontalScrollbar.destroy();
      horizontalScrollbar = null;
      console.log('✅ Smooth scrollbar destroyed');
    } catch (e) {
      console.warn('⚠️ Error destroying scrollbar:', e);
    }
  }

  if (originalContent) {
    document.body.innerHTML = originalContent;
    document.body.style.cssText = '';
    originalContent = null;
    fullscreenElements = null;
    documentTreeStructure = null;
  }

  isFullscreenMode = false;
  console.log('Normal mode restored');
}

function createFullscreenNavigation(leftNav) {
  // Clone the navigation
  const navClone = leftNav.cloneNode(true);

  // Build tree structure for better management
  documentTreeStructure = buildTreeStructure(navClone);

  // Close all <details> elements to show compact structure
  const allDetails = navClone.querySelectorAll('details');
  allDetails.forEach(detail => {
    detail.removeAttribute('open');
  });

  // Initialize SortableJS for all navigation elements
  initializeSortableJS(navClone);

  // Create fullscreen container with enhanced layout
  const container = document.createElement('div');
  container.id = 'fullscreen-nav-container';
  container.style.cssText = `
    columns: auto;
    column-width: 320px;
    column-gap: 20px;
    column-fill: auto;
    height: calc(100vh - 120px);
    padding: 20px;
    font-size: 14px;
    background: #fafafa;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    position: relative;
    overflow: hidden;
  `;

  // Create scrollable wrapper
  const scrollableWrapper = document.createElement('div');
  scrollableWrapper.id = 'scrollable-wrapper';
  scrollableWrapper.style.cssText = `
    width: 100%;
    height: 100%;
    overflow: auto;
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
    padding: 10px 0;
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

  // Add navigation content to scrollable wrapper
  scrollableWrapper.appendChild(navClone);
  container.appendChild(scrollableWrapper);

  // Initialize smooth scrollbar for horizontal scrolling
  if (typeof Scrollbar !== 'undefined') {
    try {
      horizontalScrollbar = Scrollbar.init(scrollableWrapper, {
        damping: 0.1,
        renderByPixels: true,
        alwaysShowTracks: true,
        continuousScrolling: false
      });
      console.log('✅ Smooth scrollbar initialized');
    } catch (e) {
      console.warn('⚠️ Could not initialize smooth scrollbar:', e);
    }
  }

  // Initialize changes panel
  updateChangesPanel();

  // Add exit instruction with drag & drop help (moved to right)
  const exitInfo = document.createElement('div');
  exitInfo.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: linear-gradient(135deg, #e3f2fd, #f3e5f5);
    padding: 15px 20px;
    border-radius: 8px;
    font-size: 14px;
    color: #333;
    z-index: 1000;
    box-shadow: 0 4px 20px rgba(0,0,0,0.15);
    border: 1px solid #2196f3;
    max-width: 350px;
    line-height: 1.4;
  `;
  exitInfo.innerHTML = `
    <div style="font-weight: bold; margin-bottom: 8px;">🎯 Drag & Drop Mode</div>
    <div style="font-size: 13px; margin-bottom: 6px;">• Hover over items to see drag handles (⋮⋮)</div>
    <div style="font-size: 13px; margin-bottom: 6px;">• Drag documents between sections</div>
    <div style="font-size: 13px; margin-bottom: 6px;">• Use Rename button to rename files</div>
    <div style="font-size: 13px; margin-bottom: 8px;">• Drop on documents to create folders</div>
    <div style="display: flex; gap: 8px; margin-bottom: 8px;">
      <button id="open-all-btn-top" style="padding: 6px 12px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">📂 Open All</button>
      <button id="close-all-btn-top" style="padding: 6px 12px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">📁 Close All</button>
      <button id="rename-btn-top" style="padding: 6px 12px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">✏️ Rename</button>
    </div>
    <div style="font-size: 12px; color: #666; border-top: 1px solid #ddd; padding-top: 8px;">Press F2 to exit</div>
  `;

  // Add event listeners for the top buttons
  const openAllBtnTop = exitInfo.querySelector('#open-all-btn-top');
  const closeAllBtnTop = exitInfo.querySelector('#close-all-btn-top');
  const renameBtnTop = exitInfo.querySelector('#rename-btn-top');

  openAllBtnTop.addEventListener('click', openAllSections);
  closeAllBtnTop.addEventListener('click', closeAllSections);
  renameBtnTop.addEventListener('click', () => {
    // Show rename panel for the first dragged element or prompt to select
    const draggedElement = document.querySelector('.sortable-chosen');
    if (draggedElement) {
      showRenamePanel(draggedElement);
    } else {
      alert('Please drag a file first, then click Rename to rename it');
    }
  });

  // Find and focus on current page
  focusCurrentPage(navClone);

  // Return DOM elements instead of HTML string
  return {
    container: container,
    exitInfo: exitInfo
  };
}

// Enhanced SortableJS Implementation
function initializeSortableJS(navElement) {
  console.log('🔧 Initializing enhanced SortableJS for navigation tree');
  console.log('📝 Nav element:', navElement);
  
  if (!navElement) {
    console.error('❌ Navigation element is null/undefined!');
    return;
  }

  // Find all sortable containers - handle both section headers and collapsible sections
  console.log('🔍 Searching for containers in navigation structure...');
  
  // Type 1: Collapsible sections (details > div.ml-6) - like "Getting Started"
  const collapsibleContainers = navElement.querySelectorAll('details div.ml-6');
  
  // Type 2: Section header containers (div.break-words with direct children) - like "Overview" 
  const sectionHeaderContainers = navElement.querySelectorAll('#navigation > div.break-words');
  
  // Type 3: Generic containers with links
  const genericContainers = navElement.querySelectorAll('div[class*="border-l"], .ml-6');
  
  // Combine all container types
  const allContainers = [...collapsibleContainers, ...sectionHeaderContainers, ...genericContainers];
  
  // Filter to only include containers that have direct <a> children
  const sortableContainers = Array.from(new Set(allContainers)).filter(container => {
    const directLinks = Array.from(container.children).filter(child => child.tagName === 'A');
    const hasDirectLinks = directLinks.length > 0;
    console.log(`🔍 Container check: ${container.tagName}.${container.className} - Direct links: ${directLinks.length}`);
    return hasDirectLinks;
  });
  
  console.log('📋 Container types found:');
  console.log(`  - Collapsible containers: ${collapsibleContainers.length}`);
  console.log(`  - Section header containers: ${sectionHeaderContainers.length}`);
  console.log(`  - Generic containers: ${genericContainers.length}`);
  console.log(`  - Total unique containers: ${allContainers.length}`);
  console.log(`  - Containers with direct links: ${sortableContainers.length}`);
  
  sortableContainers.forEach((container, i) => {
    const hasLinks = container.querySelectorAll('a').length;
    console.log(`📋 Container ${i + 1}: ${container.tagName}#${container.id || 'no-id'} (${hasLinks} links)`);
  });

  sortableContainers.forEach((container, index) => {
    console.log(`🔄 Processing container ${index + 1}/${sortableContainers.length}:`, container);
    
    // Skip if already initialized
    if (container.hasAttribute('data-sortable-initialized')) {
      console.log('⏭️ Skipping already initialized container');
      return;
    }

    console.log('✨ Creating SortableJS instance for:', container.tagName, container.id || container.className);
    
    try {
      // Create enhanced sortable instance
      const sortable = new Sortable(container, {
        group: {
          name: 'tree-docs',
          pull: false, // Don't clone, just move
          put: function(to, from, dragEl, evt) {
            // Enhanced logic for allowing drops
            return canDropInContainer(to, from, dragEl);
          }
        },
        // CRITICAL FIX: Only allow <a> elements to be dragged (individual pages)
        draggable: 'a',  // This ensures only links can be dragged, not entire sections
        
        // Alternative option for more specific targeting:
        // draggable: 'a[href]', // Only links with href attributes
        
        animation: 200,
        easing: "cubic-bezier(1, 0, 0, 1)",
        fallbackOnBody: true,
        swapThreshold: 0.5,
        invertSwap: false,
        direction: 'vertical',
        
        // Visual feedback classes
        ghostClass: 'sortable-ghost',
        chosenClass: 'sortable-chosen',
        dragClass: 'sortable-drag',
        
        // Enhanced drag and drop event handlers
        onStart: function(evt) {
          console.log('🎯 === DRAG STARTED ===');
          console.log('📦 Dragged item:', evt.item);
          console.log('📦 Item text:', evt.item.textContent?.trim());
          console.log('📦 From container:', evt.from);
          console.log('📦 From container classes:', evt.from.className);
          console.log('📦 Old index:', evt.oldIndex);
          console.log('🎯 === DRAG START DEBUG END ===');
          
          evt.item.classList.add('dragging');
          evt.item.setAttribute('data-original-parent', evt.from.id || 'root');
          evt.item.setAttribute('data-original-index', evt.oldIndex);
          
          // Add visual feedback to valid drop zones
          highlightValidDropZones(evt.item);
        },
        
        onMove: function(evt) {
          const related = evt.related;
          const dragged = evt.dragged;
          
          // Enhanced prevention logic
          if (isInvalidMove(dragged, related)) {
            return false;
          }
          
          // Visual feedback for drop zones
          updateDropZoneHighlight(evt.to);
          
          return true;
        },
        
        onEnd: function(evt) {
          console.log('🎯 === DRAG ENDED ===');
          console.log('📦 Dragged item:', evt.item);
          console.log('📦 Item text:', evt.item.textContent?.trim());
          console.log('📦 From container:', evt.from);
          console.log('📦 To container:', evt.to);
          console.log('📦 Old index:', evt.oldIndex);
          console.log('📦 New index:', evt.newIndex);
          console.log('📦 Different containers?', evt.from !== evt.to);
          console.log('📦 Different indices?', evt.oldIndex !== evt.newIndex);
          console.log('🎯 === DRAG END DEBUG - CALLING handleEnhancedSortableDrop ===');
          
          // Store original file path before processing if not already stored
          if (!evt.item.hasAttribute('data-original-path')) {
            const docInfo = extractDocumentInfo(evt.item);
            if (docInfo.filePath) {
              evt.item.setAttribute('data-original-path', docInfo.filePath);
              console.log('📋 Stored original path:', docInfo.filePath);
            }
          }
          
          evt.item.classList.remove('dragging');
          evt.item.removeAttribute('data-original-parent');
          evt.item.removeAttribute('data-original-index');
          
          // Remove all visual feedback
          clearDropZoneHighlights();
          
          // Handle the enhanced drop logic
          handleEnhancedSortableDrop(evt);
        },
        
        onClone: function(evt) {
          console.log('📋 Item cloned for cross-container move');
        }
      });

      // Mark as initialized with enhanced features
      container.setAttribute('data-sortable-initialized', 'true');
      container.setAttribute('data-sortable-enhanced', 'true');
      console.log('✅ Enhanced SortableJS initialized successfully for:', container);
      
    } catch (error) {
      console.error('❌ Failed to initialize SortableJS for container:', container, error);
    }
  });

  console.log('🎉 SortableJS initialization completed!');
  console.log(`📊 Total containers processed: ${sortableContainers.length}`);

  // Add enhanced CSS for visual feedback
  addEnhancedSortableCSS();
  
  // Add context menu for rename functionality
  addContextMenuHandlers(navElement);
}

// Enhanced logic for determining if a drop is allowed
function canDropInContainer(to, from, dragEl) {
  // Don't allow dropping parent into its own children
  if (dragEl && dragEl.nodeType === Node.ELEMENT_NODE && dragEl.contains && typeof dragEl.contains === 'function' && to && to.nodeType === Node.ELEMENT_NODE) {
    if (dragEl.contains(to)) {
      return false;
    }
  }
  
  // Check for circular references
  let currentParent = to;
  while (currentParent && currentParent.nodeType === Node.ELEMENT_NODE) {
    if (currentParent === dragEl) {
      return false;
    }
    currentParent = currentParent.parentElement;
  }
  
  return true;
}

// Add context menu handlers for rename functionality
function addContextMenuHandlers(navElement) {
  console.log('🔧 Adding visual feedback for links');
  
  // Add visual feedback to all links
  const allLinks = navElement.querySelectorAll('a');
  allLinks.forEach(link => {
    // Add visual feedback on hover
    link.addEventListener('mouseenter', function() {
      link.style.cursor = 'grab';
      link.title = 'Drag to move, use Rename button to rename ✏️';
      // Add subtle visual indicator
      link.style.borderBottom = '1px dotted #007bff';
    });
    link.addEventListener('mouseleave', function() {
      link.style.borderBottom = '';
    });
  });
  
  console.log(`✅ Added visual feedback to ${allLinks.length} links`);
}

// Check if a move is invalid
function isInvalidMove(dragged, related) {
  // Prevent dropping parent inside its own children
  if (dragged.contains && dragged.contains(related)) {
    return true;
  }
  
  // Prevent circular references
  let currentElement = related;
  while (currentElement) {
    if (currentElement === dragged) {
      return true;
    }
    currentElement = currentElement.parentElement?.closest('[data-sortable-initialized]');
    if (!currentElement) break;
  }
  
  return false;
}

// Highlight valid drop zones
function highlightValidDropZones(draggedItem) {
  const allContainers = document.querySelectorAll('[data-sortable-initialized]');
  allContainers.forEach(container => {
    if (canDropInContainer(container, null, draggedItem)) {
      container.classList.add('valid-drop-zone');
    } else {
      container.classList.add('invalid-drop-zone');
    }
  });
}

// Update drop zone highlight during move
function updateDropZoneHighlight(targetContainer) {
  // Remove previous hover highlights
  document.querySelectorAll('.drop-zone-hover').forEach(el => {
    el.classList.remove('drop-zone-hover');
  });
  
  // Add hover highlight to target
  if (targetContainer) {
    targetContainer.classList.add('drop-zone-hover');
  }
}

// Clear all drop zone highlights
function clearDropZoneHighlights() {
  const highlights = document.querySelectorAll('.valid-drop-zone, .invalid-drop-zone, .drop-zone-hover');
  highlights.forEach(el => {
    el.classList.remove('valid-drop-zone', 'invalid-drop-zone', 'drop-zone-hover');
  });
}

function addEnhancedSortableCSS() {
  // Check if CSS is already added
  if (document.getElementById('enhanced-sortable-css')) {
    return;
  }

  const style = document.createElement('style');
  style.id = 'enhanced-sortable-css';
  style.textContent = `
    /* Enhanced drag and drop styling */
    .sortable-ghost {
      opacity: 0.4;
      background: linear-gradient(135deg, #e3f2fd, #f3e5f5);
      border: 2px dashed #1976d2;
      border-radius: 8px;
      transform: scale(1.02);
      transition: all 0.2s ease;
    }
    
    .sortable-chosen {
      background: linear-gradient(135deg, #fff3e0, #fffde7);
      border: 2px solid #ff9800;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(255, 152, 0, 0.3);
      transform: scale(1.05);
      transition: all 0.2s ease;
    }
    
    .sortable-drag {
      opacity: 0.8;
      transform: rotate(3deg) scale(1.1);
      box-shadow: 0 8px 25px rgba(0,0,0,0.2);
      z-index: 1000;
      transition: all 0.2s ease;
    }
    
    .dragging {
      cursor: grabbing !important;
      user-select: none;
    }
    
    /* Enhanced drop zone highlighting */
    .valid-drop-zone {
      background: rgba(76, 175, 80, 0.1);
      border: 2px solid #4caf50;
      border-radius: 8px;
      transition: all 0.3s ease;
    }
    
    .invalid-drop-zone {
      background: rgba(244, 67, 54, 0.1);
      border: 2px solid #f44336;
      border-radius: 8px;
      opacity: 0.6;
      transition: all 0.3s ease;
    }
    
    .drop-zone-hover {
      background: rgba(33, 150, 243, 0.2);
      border: 2px solid #2196f3;
      border-radius: 8px;
      transform: scale(1.02);
      transition: all 0.2s ease;
    }
    
    /* Enhanced nested containers */
    details[data-sortable-initialized] {
      min-height: 24px;
      border-radius: 6px;
      transition: all 0.2s ease;
      position: relative;
    }
    
    details[data-sortable-initialized] > summary {
      cursor: grab;
      position: relative;
      padding-left: 8px;
    }
    
    details[data-sortable-initialized] > summary::before {
      content: "⋮⋮";
      position: absolute;
      left: -8px;
      top: 50%;
      transform: translateY(-50%);
      color: #ccc;
      font-size: 10px;
      opacity: 0;
      transition: opacity 0.2s ease;
      line-height: 1;
    }
    
    details[data-sortable-initialized] > summary:hover::before {
      opacity: 0.7;
    }
    
    details[data-sortable-initialized]:hover {
      background: rgba(33, 150, 243, 0.05);
      border-radius: 6px;
    }
    
    details[data-sortable-initialized]:empty {
      background: linear-gradient(135deg, #f5f5f5, #fafafa);
      border: 2px dashed #ddd;
      border-radius: 8px;
      padding: 12px;
      margin: 4px 0;
      transition: all 0.3s ease;
    }
    
    details[data-sortable-initialized]:empty:hover {
      border-color: #2196f3;
      background: linear-gradient(135deg, #e3f2fd, #f3e5f5);
    }
    
    details[data-sortable-initialized]:empty::before {
      content: "📄 Drop documentation pages here";
      color: #666;
      font-style: italic;
      display: block;
      text-align: center;
    }
    
    /* Visual feedback for page items */
    [data-sortable-initialized] a {
      border-radius: 4px;
      transition: all 0.2s ease;
      padding: 2px 4px;
      cursor: grab;
      position: relative;
    }
    
    [data-sortable-initialized] a::before {
      content: "⋮⋮";
      position: absolute;
      left: -12px;
      top: 50%;
      transform: translateY(-50%);
      color: #ccc;
      font-size: 10px;
      opacity: 0;
      transition: opacity 0.2s ease;
      line-height: 1;
    }
    
    [data-sortable-initialized] a:hover {
      background: rgba(33, 150, 243, 0.1);
      transform: translateX(2px);
    }
    
    [data-sortable-initialized] a:hover::before {
      opacity: 0.7;
    }
    
    /* Leaf to parent conversion indicator */
    .converting-to-parent {
      background: linear-gradient(135deg, #fff3e0, #f3e5f5);
      border: 2px dashed #ff9800;
      border-radius: 8px;
      animation: pulse 1s infinite;
    }
    
    @keyframes pulse {
      0% { opacity: 0.7; }
      50% { opacity: 1; }
      100% { opacity: 0.7; }
    }
    
    /* Enhanced fullscreen container styling */
    #fullscreen-nav-container {
      background: linear-gradient(135deg, #fafafa, #f5f5f5);
    }
    
    #scrollable-wrapper::-webkit-scrollbar {
      width: 8px;
      height: 8px;
    }
    
    #scrollable-wrapper::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 4px;
    }
    
    #scrollable-wrapper::-webkit-scrollbar-thumb {
      background: linear-gradient(135deg, #2196f3, #1976d2);
      border-radius: 4px;
    }
    
    #scrollable-wrapper::-webkit-scrollbar-thumb:hover {
      background: linear-gradient(135deg, #1976d2, #1565c0);
    }
    
    /* Fix width for pages with nested elements */
    #fullscreen-nav-container {
      max-width: 100%;
      overflow-x: auto;
    }
    
    #fullscreen-nav-container .break-words {
      min-width: 200px;
      max-width: 300px;
    }
    
    #fullscreen-nav-container .ml-6 {
      min-width: 180px;
      max-width: 280px;
    }
    
    /* Styles for elements with pending changes */
    .pending-save {
      position: relative;
      background: linear-gradient(90deg, rgba(255, 193, 7, 0.1), rgba(255, 193, 7, 0.05));
      border-left: 3px solid #ffc107;
      transition: all 0.3s ease;
    }
    
    .pending-save::after {
      content: "●";
      color: #ff9800;
      font-weight: bold;
      margin-left: 5px;
      font-size: 12px;
      animation: pulse 2s infinite;
    }
    
    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.5; }
    }
    
    .pending-save:hover {
      background: linear-gradient(90deg, rgba(255, 193, 7, 0.15), rgba(255, 193, 7, 0.08));
    }
  `;
  
  document.head.appendChild(style);
  console.log('✅ Enhanced sortable CSS added');
}

// Keep backward compatibility
function addSortableCSS() {
  addEnhancedSortableCSS();
}

// Enhanced drop handling with comprehensive error handling
function handleEnhancedSortableDrop(evt) {
  console.log('🎯 === DROP EVENT STARTED ===');
  console.log('🔄 Handling enhanced sortable drop:', evt);
  console.log('📋 Event details:', {
    type: 'drop',
    item: evt.item,
    from: evt.from,
    to: evt.to,
    oldIndex: evt.oldIndex,
    newIndex: evt.newIndex
  });
  
  try {
    const item = evt.item;
    const from = evt.from;
    const to = evt.to;
    const oldIndex = evt.oldIndex;
    const newIndex = evt.newIndex;
    
    // Validate drop operation
    const validation = validateDropOperation(evt);
    if (!validation.valid) {
      console.error('❌ Invalid drop operation:', validation.error);
      showErrorNotification('Drop operation failed: ' + validation.error);
      revertDropOperation(evt);
      return;
    }
    
    console.log('📊 Enhanced drop details:', {
      item: item.tagName,
      itemText: item.textContent?.trim().substring(0, 50),
      from: from.tagName,
      to: to.tagName,
      oldIndex,
      newIndex,
      fromId: from.id,
      toId: to.id
    });
    
    // Extract document information
    const docInfo = extractDocumentInfo(item);
    console.log('📄 Document info:', docInfo);
    
    // Validate document info
    if (!docInfo.text || !docInfo.href) {
      console.error('❌ Invalid document info:', docInfo);
      showErrorNotification('Cannot process document: missing required information');
      revertDropOperation(evt);
      return;
    }
    
    // Check if we need to convert leaf to parent
    if (shouldConvertToParent(item, to)) {
      console.log('🌱 Converting leaf to parent');
      try {
        convertLeafToParent(item, to);
      } catch (error) {
        console.error('❌ Error converting leaf to parent:', error);
        showErrorNotification('Failed to convert structure: ' + error.message);
        revertDropOperation(evt);
        return;
      }
    }
    
    // Remove empty parents with recursive cleanup
    try {
      recursiveCleanupEmptyParents(from);
    } catch (error) {
      console.error('❌ Error cleaning up empty parents:', error);
      // This is non-critical, so we continue
    }
    
    // Plan file system changes
    try {
      const changesPlan = planFileSystemChanges(docInfo, from, to, newIndex);
      console.log('📋 File system changes plan:', changesPlan);
      
      // Validate the planned changes
      if (!validateChangesPlan(changesPlan)) {
        showErrorNotification('Invalid file operation planned');
        revertDropOperation(evt);
        return;
      }
      
      // Log the new structure for backend sync
      logEnhancedTreeStructure(item, to, newIndex, changesPlan);
      
      // Add to pending changes
      if (changesPlan && changesPlan.moveFile) {
        const { from, to } = changesPlan.moveFile;
        addPendingMove(from, to, evt.item, from, to, newIndex);
        console.log('📋 Added move to pending changes:', from, '→', to);
      }
    } catch (error) {
      console.error('❌ Error planning file system changes:', error);
      showErrorNotification('Failed to plan file changes: ' + error.message);
      revertDropOperation(evt);
      return;
    }
    
    console.log('✅ === DROP EVENT COMPLETED SUCCESSFULLY ===');
    
  } catch (error) {
    console.error('❌ === DROP EVENT FAILED ===');
    console.error('❌ Unexpected error in handleEnhancedSortableDrop:', error);
    console.error('📋 Error stack:', error.stack);
    showErrorNotification('An unexpected error occurred during the drop operation: ' + error.message);
    revertDropOperation(evt);
  }
}

// Validate drop operation
function validateDropOperation(evt) {
  const item = evt.item;
  const from = evt.from;
  const to = evt.to;
  
  // Check if all required elements exist
  if (!item || !from || !to) {
    return { valid: false, error: 'Missing required drop elements' };
  }
  
  // Check if dropping into same container at same position
  if (from === to && evt.oldIndex === evt.newIndex) {
    return { valid: false, error: 'No actual move occurred' };
  }
  
  // Allow moving within the same container (reordering)
  if (from === to && evt.oldIndex !== evt.newIndex) {
    return { valid: true };
  }
  
  // Check if item contains destination (preventing circular drops)
  if (item.contains && item.contains(to)) {
    return { valid: false, error: 'Cannot drop parent into its own child' };
  }
  
  // Check if item has required content
  const link = item.querySelector('a') || (item.tagName === 'A' ? item : null);
  if (!link || !link.href || !link.textContent.trim()) {
    return { valid: false, error: 'Item does not contain valid documentation link' };
  }
  
  return { valid: true };
}

// Validate changes plan
function validateChangesPlan(changesPlan) {
  if (!changesPlan) {
    return false;
  }
  
  // If there's a file move planned, validate it
  if (changesPlan.moveFile) {
    const { from, to } = changesPlan.moveFile;
    
    if (!from || !to) {
      console.error('❌ Invalid file move plan: missing from/to paths');
      return false;
    }
    
    if (from === to) {
      console.error('❌ Invalid file move plan: source and destination are the same');
      return false;
    }
    
    // Basic path validation
    if (!from.endsWith('.md') || !to.endsWith('.md')) {
      console.error('❌ Invalid file move plan: paths must be markdown files');
      return false;
    }
    
    // Check for dangerous paths
    if (from.includes('..') || to.includes('..')) {
      console.error('❌ Invalid file move plan: paths cannot contain parent directory references');
      return false;
    }
  }
  
  return true;
}

// Revert drop operation
function revertDropOperation(evt) {
  console.log('🔄 Reverting drop operation');
  
  try {
    const item = evt.item;
    const from = evt.from;
    const oldIndex = evt.oldIndex;
    
    // Move item back to original position
    if (from && item && typeof oldIndex === 'number') {
      if (oldIndex < from.children.length) {
        from.insertBefore(item, from.children[oldIndex]);
      } else {
        from.appendChild(item);
      }
      console.log('✅ Drop operation reverted successfully');
    } else {
      console.warn('⚠️ Could not revert drop operation: missing original position info');
    }
  } catch (error) {
    console.error('❌ Error reverting drop operation:', error);
    showErrorNotification('Could not revert the operation. Please refresh the page.');
  }
}

// Show error notification
function showErrorNotification(message) {
  console.error('🚨 Error notification:', message);
  
  const notification = document.createElement('div');
  notification.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: #f44336;
    color: white;
    padding: 15px 25px;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(244, 67, 54, 0.3);
    z-index: 3000;
    font-weight: 500;
    max-width: 400px;
    border-left: 4px solid #d32f2f;
    animation: slideInFromRight 0.3s ease;
  `;
  
  notification.innerHTML = `
    <div style="display: flex; align-items: center; gap: 10px;">
      <span style="font-size: 20px;">⚠️</span>
      <span>${message}</span>
      <button onclick="this.parentElement.parentElement.remove()" style="
        background: none;
        border: none;
        color: white;
        font-size: 20px;
        cursor: pointer;
        margin-left: auto;
      ">×</button>
    </div>
  `;
  
  // Add slide-in animation CSS if not already present
  if (!document.getElementById('notification-css')) {
    const css = document.createElement('style');
    css.id = 'notification-css';
    css.textContent = `
      @keyframes slideInFromRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
      }
      @keyframes slideOutToRight {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
      }
    `;
    document.head.appendChild(css);
  }
  
  document.body.appendChild(notification);
  
  // Auto-remove after 8 seconds
  setTimeout(() => {
    if (notification.parentElement) {
      notification.style.animation = 'slideOutToRight 0.3s ease';
      setTimeout(() => notification.remove(), 300);
    }
  }, 8000);
}

// Keep backward compatibility
function handleSortableDrop(evt) {
  handleEnhancedSortableDrop(evt);
}

// Extract document information from dropped item
function extractDocumentInfo(item) {
  console.log('🔍 Extracting document info from item:', item);
  
  const link = item.querySelector('a') || item;
  const href = link.href || '';
  const text = link.textContent?.trim() || '';
  
  console.log('📋 Link details:', { href, text, tagName: link.tagName });
  
  // Extract file path from href with improved logic
  let filePath = '';
  if (href) {
    try {
      const url = new URL(href);
      let pathname = url.pathname.replace(/^\//, ''); // Remove leading slash
      
      // Handle different URL patterns
      if (pathname === '' || pathname === '/') {
        filePath = 'README.md';
      } else {
        // If already has .md extension, use as-is, otherwise add .md
        filePath = pathname.endsWith('.md') ? pathname : pathname + '.md';
        
        // Handle root pages (convert "/page" to "page.md")
        if (!filePath.includes('/') && !filePath.startsWith('readme/')) {
          // Check if this might be a root-level page by examining the current location
          const segments = pathname.split('/').filter(s => s);
          if (segments.length === 1) {
            // This is likely a section root, convert to README.md
            filePath = segments[0] + '/README.md';
          }
        }
      }
      
      console.log('📄 Calculated filePath:', filePath, 'from pathname:', pathname);
      
    } catch (error) {
      console.error('❌ Error parsing URL:', href, error);
      // Fallback: try to extract from relative href
      let pathname = href.replace(/^\//, '');
      filePath = pathname.endsWith('.md') ? pathname : pathname + '.md';
    }
  }
  
  const result = {
    text: text,
    href: href,
    filePath: filePath,
    element: item
  };
  
  console.log('✅ Extracted document info:', result);
  return result;
}

// Plan file system changes based on the drop operation
function planFileSystemChanges(docInfo, fromContainer, toContainer, newIndex) {
  console.log('🎯 === PLANNING FILE SYSTEM CHANGES ===');
  console.log('📄 Document info:', docInfo);
  console.log('📂 From container:', fromContainer.tagName, fromContainer.className, fromContainer.id);
  console.log('📂 To container:', toContainer.tagName, toContainer.className, toContainer.id);
  console.log('📊 New index:', newIndex);
  
  const changes = {
    moveFile: null,
    updateSummary: null,
    addRedirect: null,
    updateReferences: []
  };
  
  if (!docInfo.filePath) {
    console.warn('⚠️ No file path available in docInfo, returning empty changes');
    return changes;
  }
  
  // Calculate old and new file paths
  const oldPath = docInfo.filePath;
  console.log('📋 Original file path:', oldPath);
  
  const newPath = calculateNewFilePath(docInfo, toContainer, newIndex);
  console.log('📋 Calculated new path:', newPath);
  
  // Critical comparison with detailed logging
  console.log('🔍 Path comparison:');
  console.log('  - Old path:', JSON.stringify(oldPath));
  console.log('  - New path:', JSON.stringify(newPath));
  console.log('  - Are equal?', oldPath === newPath);
  console.log('  - Old path length:', oldPath.length);
  console.log('  - New path length:', newPath.length);
  
  if (oldPath !== newPath) {
    console.log('✅ Paths are different, planning file move operation');
    
    changes.moveFile = {
      from: oldPath,
      to: newPath
    };
    
    changes.updateSummary = {
      oldEntry: `[${docInfo.text}](${oldPath})`,
      newEntry: `[${docInfo.text}](${newPath})`,
      newIndex: newIndex
    };
    
    changes.addRedirect = {
      from: oldPath.replace(/\.md$/, ''),
      to: newPath.replace(/\.md$/, '')
    };
    
    // Plan reference updates (placeholder for now)
    changes.updateReferences.push({
      searchPattern: oldPath,
      replaceWith: newPath
    });
    
    console.log('📋 Planned changes:', changes);
  } else {
    console.warn('❌ Paths are identical - NO CHANGES PLANNED');
    console.warn('    This will result in "No changes to save" message');
    console.warn('    Check path calculation logic above for issues');
  }
  
  console.log('🎯 === FILE SYSTEM CHANGES PLANNING COMPLETE ===');
  return changes;
}

// Helper function to convert section title to directory segment
function convertTitleToSegment(sectionTitle) {
  const lowerTitle = sectionTitle.toLowerCase();
  
  // Handle specific known section mappings
  if (lowerTitle.includes('getting started')) {
    return 'getting-started';
  } else if (lowerTitle.includes('overview')) {
    return 'overview';
  } else if (lowerTitle.includes('tutorial')) {
    return 'tutorials';
  } else if (lowerTitle.includes('configuration')) {
    return 'configuration';
  } else if (lowerTitle.includes('api')) {
    return 'api';
  } else if (lowerTitle.includes('access control')) {
    return 'access-control';
  } else if (lowerTitle.includes('database')) {
    return 'database';
  } else if (lowerTitle.includes('deployment')) {
    return 'deployment-and-maintenance';
  } else if (lowerTitle.includes('storage')) {
    return 'storage';
  } else if (lowerTitle.includes('reference')) {
    return 'reference';
  } else if (lowerTitle.includes('module')) {
    return 'modules';
  } else {
    // Generic transformation
    return sectionTitle
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-|-$/g, '');
  }
}

// Calculate new file path based on container hierarchy
function calculateNewFilePath(docInfo, container, index) {
  console.log('🔍 Calculating new file path for:', docInfo.text, 'in container:', container);
  
  let pathSegments = [];
  
  // Find the closest section by walking up the DOM tree
  let currentElement = container;
  let foundSections = [];
  
  // Walk up the DOM tree to find section headers
  while (currentElement && currentElement.id !== 'navigation') {
    console.log('🔍 Examining element:', currentElement.tagName, currentElement.className, currentElement.id);
    
    // Type 1: Check if this is a details element (collapsible section)
    if (currentElement.tagName === 'DETAILS') {
      const summary = currentElement.querySelector('summary');
      if (summary) {
        const sectionTitle = summary.textContent.trim();
        console.log('📂 Found collapsible section:', sectionTitle);
        
        // Process collapsible section...
        const segment = convertTitleToSegment(sectionTitle);
        if (segment) {
          foundSections.unshift(segment);
          console.log('✅ Added collapsible section segment:', segment);
        }
      }
    }
    
    // Type 2: Check if this is a section header container (div.break-words)
    else if (currentElement.tagName === 'DIV' && 
             currentElement.classList.contains('break-words') && 
             currentElement.parentElement && 
             currentElement.parentElement.id === 'navigation') {
      
      // Look for section title in the span element
      const sectionSpan = currentElement.querySelector('span.text-mini');
      if (sectionSpan) {
        const sectionTitle = sectionSpan.textContent.trim();
        console.log('📂 Found section header:', sectionTitle);
        
        // Process section header...
        const segment = convertTitleToSegment(sectionTitle);
        if (segment) {
          foundSections.unshift(segment);
          console.log('✅ Added section header segment:', segment);
        }
      }
    }
    
    // Move up to parent element
    currentElement = currentElement.parentElement;
  }
  
  pathSegments = foundSections;
  console.log('📂 Section path segments found:', pathSegments);
  
  // Additional debugging for mixed navigation detection
  console.log('🔍 Navigation structure analysis:');
  console.log('  - Container type:', container.tagName, container.className);
  console.log('  - Container parent:', container.parentElement?.tagName, container.parentElement?.className);
  console.log('  - Is in details?', !!container.closest('details'));
  console.log('  - Is in section header?', container.parentElement?.classList.contains('break-words'));
  
  if (foundSections.length === 0) {
    console.warn('⚠️ No sections found! This may be why paths are identical.');
    console.warn('    Check if section detection logic matches actual DOM structure.');
  }
  
  // Generate filename - try to preserve original filename when possible
  let filename;
  if (docInfo.filePath && docInfo.filePath.includes('/')) {
    // Extract the original filename from the existing path
    const originalFilename = docInfo.filePath.split('/').pop();
    console.log('📋 Using original filename:', originalFilename);
    filename = originalFilename;
  } else {
    // Generate filename from document title
    filename = docInfo.text
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '-')
      .replace(/^-|-$/g, '') + '.md';
    console.log('📋 Generated filename from title:', filename);
  }
  
  pathSegments.push(filename);
  
  const newPath = pathSegments.join('/');
  console.log('📄 Calculated new path:', newPath);
  console.log('📋 Original path was:', docInfo.filePath);
  
  // Additional validation
  if (newPath === docInfo.filePath) {
    console.warn('⚠️ WARNING: New path is identical to original path!');
    console.warn('   This suggests the document is being moved to the same location');
    console.warn('   Check if the target container detection is working correctly');
  }
  
  return newPath;
}

// Enhanced tree structure logging
function logEnhancedTreeStructure(item, container, index, changesPlan) {
  const logData = {
    timestamp: new Date().toISOString(),
    itemId: item.dataset.id || item.textContent?.trim(),
    containerId: container.dataset.id || container.id,
    newIndex: index,
    changesPlan: changesPlan,
    treeStructure: documentTreeStructure
  };
  
  console.log('📋 Enhanced tree structure update:', logData);
  
  // Send update to backend via HTMX
  console.log('🌐 Attempting to send HTMX request...');
  console.log('📋 HTMX available:', typeof htmx !== 'undefined');
  console.log('📋 Changes plan has moveFile:', !!changesPlan.moveFile);
  
  if (typeof htmx !== 'undefined' && changesPlan.moveFile) {
    console.log('🚀 Sending HTMX request to /api/reorganize-docs');
    
    const payload = {
      action: 'move_document',
      changes: JSON.stringify(changesPlan),
      timestamp: logData.timestamp
    };
    console.log('📤 Request payload:', payload);
    console.log('📤 Payload stringified:', JSON.stringify(payload));
    
    // Try different request formats to debug the parsing issue
    console.log('🔍 Testing HTMX request format...');
    
    // Try form-urlencoded first
    htmx.ajax('POST', '/api/reorganize-docs', {
      values: payload,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Accept': 'application/json'
      },
      swap: 'none'  // Don't swap any content
    }).then(response => {
      console.log('✅ Form request completed successfully:', response);
      console.log('✅ Response type:', typeof response);
      console.log('✅ Response content:', response);
    }).catch(error => {
      console.error('❌ Form request failed, trying JSON:', error);
      
      // Fallback: try JSON format
      return fetch('/api/reorganize-docs', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify(payload)
      }).then(response => {
        console.log('✅ JSON fallback request completed, status:', response.status);
        return response.json();
      }).then(data => {
        console.log('✅ JSON response data:', data);
      }).catch(jsonError => {
        console.error('❌ Both form and JSON requests failed:', jsonError);
        showErrorNotification('Backend request failed: ' + jsonError.message);
      });
    });
    
    console.log('🚀 Enhanced HTMX request sent to backend');
  } else {
    if (typeof htmx === 'undefined') {
      console.error('❌ HTMX not available!');
      showErrorNotification('HTMX library not loaded');
    } else if (!changesPlan.moveFile) {
      console.warn('⚠️ No file changes needed, skipping backend request');
    }
  }
}

// Enhanced leaf-to-parent conversion with better detection
function shouldConvertToParent(draggedItem, dropTarget) {
  // Enhanced logic for determining conversion
  const isLeafElement = dropTarget.tagName === 'A' || 
                       (dropTarget.tagName === 'DIV' && !dropTarget.closest('details')) ||
                       (dropTarget.tagName === 'SUMMARY' && !dropTarget.parentElement.querySelector('div'));
  
  // Don't convert if dropping in the same container
  if (draggedItem.parentElement === dropTarget.parentElement) {
    return false;
  }
  
  // Check if target is actually a leaf that can become a parent
  const hasLink = dropTarget.querySelector('a') || dropTarget.tagName === 'A';
  
  console.log('🔍 Checking conversion:', {
    isLeafElement,
    hasLink,
    shouldConvert: isLeafElement && hasLink
  });
  
  return isLeafElement && hasLink;
}

function convertLeafToParent(draggedItem, leafTarget) {
  console.log('🌱 Converting leaf to parent:', { draggedItem, leafTarget });
  
  // Add visual feedback during conversion
  leafTarget.classList.add('converting-to-parent');
  
  // Create new details structure with enhanced styling
  const details = document.createElement('details');
  details.setAttribute('open', '');
  details.className = 'converted-parent';
  
  // Create summary from the leaf with enhanced styling
  const summary = document.createElement('summary');
  if (leafTarget.tagName === 'A') {
    // If target is a link, preserve its structure
    summary.innerHTML = leafTarget.outerHTML;
  } else {
    // If target contains a link, extract and use it
    const link = leafTarget.querySelector('a');
    if (link) {
      summary.innerHTML = link.outerHTML;
    } else {
      summary.innerHTML = leafTarget.innerHTML;
    }
  }
  summary.className = 'text-tint font-medium cursor-pointer';
  
  // Create enhanced container for children
  const childrenContainer = document.createElement('div');
  childrenContainer.className = 'ml-6 mt-2 border-l-2 border-tint-8 pl-4';
  childrenContainer.setAttribute('data-converted-container', 'true');
  
  // Add the dragged item to the container
  childrenContainer.appendChild(draggedItem);
  
  // Assemble structure with animation
  details.appendChild(summary);
  details.appendChild(childrenContainer);
  
  // Replace leaf with parent (with animation)
  leafTarget.style.transition = 'all 0.3s ease';
  leafTarget.style.opacity = '0.5';
  leafTarget.style.transform = 'scale(0.95)';
  
  setTimeout(() => {
    leafTarget.parentNode.replaceChild(details, leafTarget);
    
    // Initialize SortableJS for the new container
    initializeSortableJS(childrenContainer);
    
    // Add success animation
    details.style.opacity = '0';
    details.style.transform = 'scale(0.95)';
    details.offsetHeight; // Force reflow
    details.style.transition = 'all 0.3s ease';
    details.style.opacity = '1';
    details.style.transform = 'scale(1)';
    
    console.log('✅ Leaf successfully converted to parent');
    
    // Remove conversion indicator after animation
    setTimeout(() => {
      details.classList.remove('converted-parent');
    }, 300);
  }, 150);
}

function cleanupEmptyParents(container) {
  if (container.tagName === 'DETAILS') {
    const childrenContainer = container.querySelector('div[data-converted-container], div.ml-6');
    if (childrenContainer && childrenContainer.children.length === 0) {
      console.log('🧹 Cleaning up empty parent:', container);
      
      // Convert back to leaf with animation
      const summary = container.querySelector('summary');
      if (summary) {
        // Create leaf element from summary
        const leafElement = document.createElement('div');
        const link = summary.querySelector('a');
        if (link) {
          leafElement.appendChild(link.cloneNode(true));
        } else {
          leafElement.innerHTML = summary.innerHTML;
        }
        leafElement.className = 'text-tint hover:text-tint-strong transition-colors';
        
        // Animate the conversion
        container.style.transition = 'all 0.3s ease';
        container.style.opacity = '0.5';
        container.style.transform = 'scale(0.95)';
        
        setTimeout(() => {
          container.parentNode.replaceChild(leafElement, container);
          
          // Add success animation to new leaf
          leafElement.style.opacity = '0';
          leafElement.style.transform = 'scale(0.95)';
          leafElement.offsetHeight; // Force reflow
          leafElement.style.transition = 'all 0.3s ease';
          leafElement.style.opacity = '1';
          leafElement.style.transform = 'scale(1)';
          
          console.log('✅ Empty parent converted back to leaf');
        }, 150);
      }
    }
  }
}

// Enhanced parent cleanup with recursive checking
function recursiveCleanupEmptyParents(startContainer) {
  let currentContainer = startContainer;
  
  while (currentContainer && currentContainer.tagName === 'DETAILS') {
    const childrenContainer = currentContainer.querySelector('div[data-converted-container], div.ml-6');
    
    if (childrenContainer && childrenContainer.children.length === 0) {
      const parentContainer = currentContainer.parentElement?.closest('details');
      cleanupEmptyParents(currentContainer);
      currentContainer = parentContainer;
    } else {
      break;
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

// Update save button state based on pending changes
function updateSaveButtonState() {
  const saveButton = document.querySelector('#save-changes-btn');
  
  if (saveButton) {
    const totalChanges = getTotalPendingChanges();
    const originalText = '💾 Save Changes';
    
    if (totalChanges > 0) {
      saveButton.textContent = `💾 Save Changes (${totalChanges})`;
      saveButton.style.background = 'linear-gradient(135deg, #ff9800, #f57c00)';
      saveButton.style.color = 'white';
      saveButton.style.fontWeight = 'bold';
      saveButton.style.animation = 'pulse 2s infinite';
      saveButton.disabled = false;
    } else {
      saveButton.textContent = originalText;
      saveButton.style.background = '#6c757d';
      saveButton.style.color = 'white';
      saveButton.style.fontWeight = 'normal';
      saveButton.style.animation = 'none';
      saveButton.disabled = true;
    }
    
    console.log(`📋 Updated save button state: ${totalChanges} pending changes`);
  }
}

// Clear all pending save attributes after successful save
function clearPendingSaveAttributes() {
  console.log('🧹 Clearing all pending changes');
  clearPendingChanges();
  console.log('✅ All pending changes cleared');
}

function createControlButton(text, onClick) {
  const button = document.createElement('button');
  button.textContent = text;
  button.onclick = onClick;
  
  // Add data attributes for identification
  if (text.includes('Open All')) {
    button.setAttribute('data-action', 'open-all');
  } else if (text.includes('Close All')) {
    button.setAttribute('data-action', 'close-all');
  } else if (text.includes('Save Changes')) {
    button.setAttribute('data-action', 'save-changes');
  } else if (text.includes('Reset')) {
    button.setAttribute('data-action', 'reset');
  }
  
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

// Build tree structure for better document management
function buildTreeStructure(navElement) {
  const structure = [];
  const processElement = (element, parent = null) => {
    if (element.tagName === 'A' && element.href) {
      const item = {
        id: element.href,
        text: element.textContent.trim(),
        href: element.href,
        element: element,
        parent: parent,
        children: [],
        type: 'leaf'
      };
      if (parent) parent.children.push(item);
      else structure.push(item);
      return item;
    } else if (element.tagName === 'DETAILS') {
      const summary = element.querySelector('summary');
      const item = {
        id: summary ? summary.textContent.trim() : 'unknown',
        text: summary ? summary.textContent.trim() : 'Unknown Section',
        element: element,
        parent: parent,
        children: [],
        type: 'parent'
      };
      if (parent) parent.children.push(item);
      else structure.push(item);
      
      // Process children
      const childElements = element.children;
      for (let child of childElements) {
        if (child !== summary) {
          const grandChildren = child.querySelectorAll('a, details');
          for (let grandChild of grandChildren) {
            processElement(grandChild, item);
          }
        }
      }
      return item;
    }
  };

  // Process all top-level elements
  const topLevelElements = navElement.querySelectorAll('a, details');
  for (let element of topLevelElements) {
    processElement(element);
  }

  console.log('📊 Document tree structure built:', structure);
  return structure;
}

// Enhanced control panel with additional functionality
function createEnhancedControlPanel() {
  const controlPanel = document.createElement('div');
  controlPanel.style.cssText = `
    position: fixed;
    top: 20px;
    left: 20px;
    z-index: 1001;
    display: flex;
    gap: 10px;
    background: white;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.15);
    border: 1px solid #e0e0e0;
    backdrop-filter: blur(10px);
  `;

  const openAllBtn = createControlButton('📂 Open All', openAllSections);
  const closeAllBtn = createControlButton('📁 Close All', closeAllSections);
  const saveChangesBtn = createControlButton('💾 Save Changes', saveDocumentChanges);
  const resetBtn = createControlButton('🔄 Reset', resetDocumentStructure);

  controlPanel.appendChild(openAllBtn);
  controlPanel.appendChild(closeAllBtn);
  controlPanel.appendChild(saveChangesBtn);
  controlPanel.appendChild(resetBtn);

  return controlPanel;
}

function createRenamePanel() {
  const renamePanel = document.createElement('div');
  renamePanel.id = 'rename-panel';
  renamePanel.style.cssText = `
    position: fixed;
    top: 80px;
    right: 20px;
    width: 350px;
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.15);
    border: 1px solid #e0e0e0;
    backdrop-filter: blur(10px);
    z-index: 1002;
  `;

  renamePanel.innerHTML = `
    <h3 style="margin: 0 0 15px 0; color: #333; font-size: 16px;">📝 Rename File</h3>
    <div style="margin-bottom: 15px;">
      <label style="display: block; margin-bottom: 5px; font-weight: 500; color: #555;">Current URL:</label>
      <div id="current-url" style="background: #f8f9fa; padding: 8px; border-radius: 4px; font-family: monospace; font-size: 12px; color: #666; border: 1px solid #dee2e6;">No file selected</div>
    </div>
    <div style="margin-bottom: 15px;">
      <label style="display: block; margin-bottom: 5px; font-weight: 500; color: #555;">New filename:</label>
      <input type="text" id="new-filename" placeholder="Enter new filename (without .md)" style="width: 100%; padding: 8px; border: 1px solid #dee2e6; border-radius: 4px; font-size: 14px;">
    </div>
    <div style="margin-bottom: 15px;">
      <label style="display: block; margin-bottom: 5px; font-weight: 500; color: #555;">New URL:</label>
      <div id="new-url" style="background: #f8f9fa; padding: 8px; border-radius: 4px; font-family: monospace; font-size: 12px; color: #666; border: 1px solid #dee2e6;">-</div>
    </div>
    <div style="display: flex; gap: 10px;">
      <button id="rename-btn" style="flex: 1; padding: 8px 12px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px;">Rename</button>
      <button id="cancel-rename-btn" style="flex: 1; padding: 8px 12px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 14px;">Cancel</button>
    </div>
  `;

  // Add event listeners
  const newFilenameInput = renamePanel.querySelector('#new-filename');
  const newUrlDiv = renamePanel.querySelector('#new-url');
  const renameBtn = renamePanel.querySelector('#rename-btn');
  const cancelBtn = renamePanel.querySelector('#cancel-rename-btn');

  // Update new URL as user types
  newFilenameInput.addEventListener('input', () => {
    const currentUrl = document.getElementById('current-url').textContent;
    if (currentUrl !== 'No file selected') {
      const currentPath = currentUrl.replace('http://localhost:8081/', '');
      const pathParts = currentPath.split('/');
      const filename = pathParts.pop();
      const extension = filename.includes('.') ? filename.split('.').pop() : 'md';
      const newFilename = newFilenameInput.value.trim();
      
      if (newFilename) {
        const newPath = [...pathParts, `${newFilename}.${extension}`].join('/');
        newUrlDiv.textContent = newPath;
        newUrlDiv.style.color = '#28a745';
      } else {
        newUrlDiv.textContent = '-';
        newUrlDiv.style.color = '#666';
      }
    }
  });

  // Handle rename button click
  renameBtn.addEventListener('click', () => {
    const currentUrl = document.getElementById('current-url').textContent;
    const newFilename = newFilenameInput.value.trim();
    
    if (currentUrl === 'No file selected') {
      alert('Please select a file to rename');
      return;
    }
    
    if (!newFilename) {
      alert('Please enter a new filename');
      return;
    }
    
    // Get the selected element
    const selectedElement = document.querySelector('[data-selected-for-rename="true"]');
    if (!selectedElement) {
      alert('No file selected for rename');
      return;
    }
    
    // Calculate new path
    const currentPath = currentUrl;
    const pathParts = currentPath.split('/');
    const filename = pathParts.pop();
    const extension = filename.includes('.') ? filename.split('.').pop() : 'md';
    const newPath = [...pathParts, `${newFilename}.${extension}`].join('/');
    
    // Add to pending changes
    addPendingRename(currentPath, newPath, selectedElement);
    
    // Update element text
    selectedElement.textContent = newFilename;
    
    // Hide rename panel
    hideRenamePanel();
    
    console.log('✅ Rename added to pending changes:', currentPath, '→', newPath);
  });

  // Handle cancel button click
  cancelBtn.addEventListener('click', () => {
    hideRenamePanel();
  });

  return renamePanel;
}

function showRenamePanel(selectedElement) {
  // Clear previous selection
  document.querySelectorAll('[data-selected-for-rename="true"]').forEach(el => {
    el.removeAttribute('data-selected-for-rename');
  });
  
  // Mark current element as selected
  if (selectedElement) {
    selectedElement.setAttribute('data-selected-for-rename', 'true');
  }
  
  const renamePanel = document.getElementById('rename-panel');
  if (!renamePanel) {
    const panel = createRenamePanel();
    document.body.appendChild(panel);
  }
  
  const currentUrlDiv = document.getElementById('current-url');
  const newFilenameInput = document.getElementById('new-filename');
  const newUrlDiv = document.getElementById('new-url');
  
  if (selectedElement && selectedElement.href) {
    const currentPath = selectedElement.href.replace('http://localhost:8081/', '');
    const filename = currentPath.split('/').pop();
    const nameWithoutExt = filename.replace(/\.md$/, '');
    
    currentUrlDiv.textContent = currentPath;
    newFilenameInput.value = nameWithoutExt;
    newFilenameInput.focus();
    
    // Trigger input event to update new URL
    newFilenameInput.dispatchEvent(new Event('input'));
  }
}

function hideRenamePanel() {
  const renamePanel = document.getElementById('rename-panel');
  if (renamePanel) {
    renamePanel.remove();
  }
  
  // Clear selection
  document.querySelectorAll('[data-selected-for-rename="true"]').forEach(el => {
    el.removeAttribute('data-selected-for-rename');
  });
}

// Pending changes management functions
function addPendingRename(oldPath, newPath, element) {
  const renameOp = {
    type: 'rename',
    oldPath: oldPath,
    newPath: newPath,
    element: element,
    timestamp: new Date().toISOString()
  };
  
  // Remove any existing rename for this element
  pendingChanges.renames = pendingChanges.renames.filter(r => r.element !== element);
  
  // Add new rename operation
  pendingChanges.renames.push(renameOp);
  
  // Mark element as pending
  element.setAttribute('data-pending-rename', 'true');
  element.setAttribute('data-old-path', oldPath);
  element.setAttribute('data-new-path', newPath);
  
  // Add visual indicator
  element.style.borderLeft = '3px solid #ff9800';
  element.style.paddingLeft = '8px';
  
  console.log('📝 Added pending rename:', renameOp);
  updateChangesPanel();
  updateSaveButtonState();
}

function addPendingMove(fromPath, toPath, element, fromContainer, toContainer, newIndex) {
  const moveOp = {
    type: 'move',
    fromPath: fromPath,
    toPath: toPath,
    element: element,
    fromContainer: fromContainer,
    toContainer: toContainer,
    newIndex: newIndex,
    timestamp: new Date().toISOString()
  };
  
  // Remove any existing move for this element
  pendingChanges.moves = pendingChanges.moves.filter(m => m.element !== element);
  
  // Add new move operation
  pendingChanges.moves.push(moveOp);
  
  // Mark element as pending
  element.setAttribute('data-pending-move', 'true');
  element.setAttribute('data-from-path', fromPath);
  element.setAttribute('data-to-path', toPath);
  
  // Add visual indicator
  element.style.borderLeft = '3px solid #ff9800';
  element.style.paddingLeft = '8px';
  
  console.log('📦 Added pending move:', moveOp);
  updateChangesPanel();
  updateSaveButtonState();
}

function clearPendingChanges() {
  // Clear all pending attributes
  document.querySelectorAll('[data-pending-rename], [data-pending-move]').forEach(element => {
    element.removeAttribute('data-pending-rename');
    element.removeAttribute('data-pending-move');
    element.removeAttribute('data-old-path');
    element.removeAttribute('data-new-path');
    element.removeAttribute('data-from-path');
    element.removeAttribute('data-to-path');
    element.style.borderLeft = '';
    element.style.paddingLeft = '';
  });
  
  // Clear pending changes storage
  pendingChanges = {
    moves: [],
    renames: [],
    reorders: []
  };
  
  console.log('🧹 Cleared all pending changes');
  updateChangesPanel();
  updateSaveButtonState();
}

function getTotalPendingChanges() {
  return pendingChanges.moves.length + pendingChanges.renames.length + pendingChanges.reorders.length;
}

function createChangesPanel() {
  const changesPanel = document.createElement('div');
  changesPanel.id = 'changes-panel';
  changesPanel.style.cssText = `
    position: fixed;
    bottom: 20px;
    right: 20px;
    width: 400px;
    max-height: 300px;
    background: white;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.15);
    border: 1px solid #e0e0e0;
    backdrop-filter: blur(10px);
    z-index: 1003;
    overflow-y: auto;
  `;

  changesPanel.innerHTML = `
    <h3 style="margin: 0 0 15px 0; color: #333; font-size: 16px; display: flex; align-items: center; gap: 8px;">
      📋 Pending Changes
      <span id="changes-count" style="background: #ff9800; color: white; padding: 2px 8px; border-radius: 10px; font-size: 12px;">0</span>
    </h3>
    <div id="changes-list" style="font-size: 13px; margin-bottom: 15px;">
      <div style="color: #666; font-style: italic;">No pending changes</div>
    </div>
    <div style="display: flex; gap: 8px; flex-wrap: wrap;">
      <button id="save-changes-btn" style="padding: 6px 12px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;" disabled>💾 Save Changes</button>
      <button id="reset-btn" style="padding: 6px 12px; background: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px;">🔄 Reset</button>
    </div>
  `;

  // Add event listeners for the buttons
  const saveChangesBtn = changesPanel.querySelector('#save-changes-btn');
  const resetBtn = changesPanel.querySelector('#reset-btn');

  saveChangesBtn.addEventListener('click', saveDocumentChanges);
  resetBtn.addEventListener('click', resetDocumentStructure);

  return changesPanel;
}

function updateChangesPanel() {
  let changesPanel = document.getElementById('changes-panel');
  if (!changesPanel) {
    changesPanel = createChangesPanel();
    document.body.appendChild(changesPanel);
  }

  const changesCount = document.getElementById('changes-count');
  const changesList = document.getElementById('changes-list');
  
  const totalChanges = getTotalPendingChanges();
  
  if (changesCount) {
    changesCount.textContent = totalChanges;
    changesCount.style.background = totalChanges > 0 ? '#ff9800' : '#ccc';
  }
  
  if (changesList) {
    if (totalChanges === 0) {
      changesList.innerHTML = '<div style="color: #666; font-style: italic;">No pending changes</div>';
    } else {
      let changesHtml = '';
      
      // Add renames
      pendingChanges.renames.forEach((rename, index) => {
        changesHtml += `
          <div style="margin-bottom: 8px; padding: 8px; background: #fff3e0; border-left: 3px solid #ff9800; border-radius: 4px;">
            <div style="font-weight: 500; color: #333;">📝 Rename</div>
            <div style="color: #666; font-size: 11px; margin-top: 2px;">
              ${rename.oldPath} → ${rename.newPath}
            </div>
          </div>
        `;
      });
      
      // Add moves
      pendingChanges.moves.forEach((move, index) => {
        changesHtml += `
          <div style="margin-bottom: 8px; padding: 8px; background: #e3f2fd; border-left: 3px solid #2196f3; border-radius: 4px;">
            <div style="font-weight: 500; color: #333;">📦 Move</div>
            <div style="color: #666; font-size: 11px; margin-top: 2px;">
              ${move.fromPath} → ${move.toPath}
            </div>
          </div>
        `;
      });
      
      changesList.innerHTML = changesHtml;
    }
  }
}

// Save document structure changes
function saveDocumentChanges() {
  console.log('💾 Saving document structure changes...');
  
  const saveButton = document.querySelector('#save-changes-btn');
  if (saveButton) {
    saveButton.disabled = true;
    saveButton.textContent = '💾 Saving...';
  }
  
  // Show loading state
  const loadingDiv = document.createElement('div');
  loadingDiv.id = 'save-loading';
  loadingDiv.style.cssText = `
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: white;
    padding: 20px 30px;
    border-radius: 12px;
    box-shadow: 0 8px 32px rgba(0,0,0,0.3);
    border: 1px solid #e0e0e0;
    z-index: 2000;
    display: flex;
    align-items: center;
    gap: 15px;
    font-size: 16px;
  `;
  loadingDiv.innerHTML = `
    <div style="
      width: 24px; 
      height: 24px; 
      border: 3px solid #f3f3f3; 
      border-top: 3px solid #2196f3; 
      border-radius: 50%; 
      animation: spin 1s linear infinite;
    "></div>
    <span>Saving documentation structure changes...</span>
  `;
  
  // Add spinning animation CSS if not already present
  if (!document.getElementById('spinner-css')) {
    const spinnerCSS = document.createElement('style');
    spinnerCSS.id = 'spinner-css';
    spinnerCSS.textContent = `
      @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
      }
    `;
    document.head.appendChild(spinnerCSS);
  }
  
  document.body.appendChild(loadingDiv);
  
  // Collect all pending changes
  const allChanges = collectAllPendingChanges();
  console.log('📋 Collected changes for save:', allChanges);
  
  if (allChanges.length === 0) {
    showSaveResult('No changes to save', false);
    return;
  }
  
  // Process changes sequentially
  processSaveChangesSequentially(allChanges, 0);
}

// Collect all pending changes
function collectAllPendingChanges() {
  console.log('🔍 Collecting all pending changes...');
  
  const changes = [];
  
  // Add renames
  pendingChanges.renames.forEach(rename => {
    changes.push({
      type: 'rename',
      from: rename.oldPath,
      to: rename.newPath,
      element: rename.element,
      operation: rename
    });
  });
  
  // Add moves
  pendingChanges.moves.forEach(move => {
    changes.push({
      type: 'move',
      from: move.fromPath,
      to: move.toPath,
      element: move.element,
      operation: move
    });
  });
  
  // Add reorders (if any)
  pendingChanges.reorders.forEach(reorder => {
    changes.push({
      type: 'reorder',
      from: reorder.oldIndex,
      to: reorder.newIndex,
      element: reorder.element,
      operation: reorder
    });
  });
  
  console.log(`📋 Found ${changes.length} changes to process`);
  return changes;
}

// Process save changes sequentially
function processSaveChangesSequentially(changes, index) {
  if (index >= changes.length) {
    // Clear all pending save attributes after successful save
    clearPendingSaveAttributes();
    showSaveResult(`Successfully saved ${changes.length} changes!`, true);
    return;
  }
  
  const change = changes[index];
  console.log(`💾 Processing change ${index + 1}/${changes.length}:`, change);
  
  // Send to backend based on change type
  if (typeof htmx !== 'undefined') {
    let action, changesData;
    
    if (change.type === 'rename') {
      action = 'rename_document';
      changesData = {
        renameFile: { from: change.from, to: change.to }
      };
    } else if (change.type === 'move') {
      action = 'move_document';
      changesData = {
        moveFile: { from: change.from, to: change.to }
      };
    } else {
      action = 'reorder_document';
      changesData = {
        reorderFile: { from: change.from, to: change.to }
      };
    }
    
    htmx.ajax('POST', '/api/reorganize-docs', {
      values: {
        action: action,
        changes: JSON.stringify(changesData),
        timestamp: new Date().toISOString()
      },
      swap: 'none'
    }).then(() => {
      console.log(`✅ Successfully processed ${change.type} change ${index + 1}`);
      processSaveChangesSequentially(changes, index + 1);
    }).catch((error) => {
      console.error(`❌ Error processing ${change.type} change ${index + 1}:`, error);
      showSaveResult(`Error saving ${change.type} change ${index + 1}: ${error.message}`, false);
    });
  } else {
    console.warn('⚠️ HTMX not available, simulating save');
    setTimeout(() => processSaveChangesSequentially(changes, index + 1), 500);
  }
}

// Show save result
function showSaveResult(message, success) {
  // Remove loading indicator
  const loadingDiv = document.getElementById('save-loading');
  if (loadingDiv) {
    loadingDiv.remove();
  }
  
  // Restore save button
  const saveButton = document.querySelector('#save-changes-btn');
  if (saveButton) {
    saveButton.disabled = false;
    saveButton.textContent = '💾 Save Changes';
  }
  
  // Show result
  const resultDiv = document.createElement('div');
  resultDiv.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px 25px;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.15);
    z-index: 2000;
    font-weight: 500;
    max-width: 400px;
    background: ${success ? '#4caf50' : '#f44336'};
    color: white;
    border: 1px solid ${success ? '#45a049' : '#d32f2f'};
  `;
  resultDiv.textContent = message;
  
  document.body.appendChild(resultDiv);
  
  // Auto-remove after 5 seconds
  setTimeout(() => {
    if (resultDiv.parentElement) {
      resultDiv.style.transition = 'all 0.3s ease';
      resultDiv.style.opacity = '0';
      resultDiv.style.transform = 'translateX(100%)';
      setTimeout(() => resultDiv.remove(), 300);
    }
  }, 5000);
}

// Reset document structure to original state
function resetDocumentStructure() {
  console.log('🔄 Resetting document structure...');
  if (confirm('Are you sure you want to reset all changes?')) {
    clearPendingChanges();
    exitFullscreenMode();
    setTimeout(() => enterFullscreenMode(), 100);
  }
}

function initializeFullscreenNavigation() {
  console.log('Fullscreen navigation initialized');
}