import React, { useEffect, useRef } from 'react';
import Sortable from 'sortablejs';

const F2Navigation = ({ onClose, isVisible = false }) => {
  const containerRef = useRef(null);
  const isInitialized = useRef(false);

  useEffect(() => {
    if (isVisible && !isInitialized.current) {
      console.log('🚀 Initializing Vanilla JS F2 Navigation');
      initializeVanillaF2Navigation();
      isInitialized.current = true;
    } else if (!isVisible && isInitialized.current) {
      console.log('🔄 Cleaning up F2 Navigation');
      cleanupF2Navigation();
      isInitialized.current = false;
    }
  }, [isVisible]);

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      if (isInitialized.current) {
        cleanupF2Navigation();
      }
    };
  }, []);

  function initializeVanillaF2Navigation() {
    // Add enhanced sortable CSS
    addEnhancedSortableCSS();
    
    // Get navigation content
    const leftNav = document.querySelector('#navigation');
    if (!leftNav) {
      console.error('❌ Left navigation not found');
      return;
    }

    // Build the fullscreen navigation
    const navClone = leftNav.cloneNode(true);
    setupFullscreenNavigation(navClone);
    
    // Initialize SortableJS
    initializeSortableJS(navClone);
    
    // Setup change tracking
    initializePendingChanges();
  }

  function setupFullscreenNavigation(navClone) {
    if (!containerRef.current) return;
    
    // Clear container
    containerRef.current.innerHTML = '';
    
    // Create header
    const header = createNavigationHeader();
    containerRef.current.appendChild(header);
    
    // Create main content
    const mainContent = document.createElement('div');
    mainContent.className = 'flex-1 overflow-auto p-6';
    mainContent.appendChild(navClone);
    containerRef.current.appendChild(mainContent);
    
    // Create changes panel
    const changesPanel = createChangesPanel();
    containerRef.current.appendChild(changesPanel);
  }

  function createNavigationHeader() {
    const header = document.createElement('div');
    header.className = 'flex items-center justify-between p-4 bg-white border-b border-gray-200';
    header.innerHTML = `
      <div class="flex items-center gap-4">
        <div class="bg-blue-600 text-white px-3 py-2 rounded-md text-sm font-semibold">
          F2 Navigation Mode
        </div>
        <div class="text-sm text-gray-600">
          Press F2 to exit • Drag & Drop Mode
        </div>
      </div>
      <div class="flex items-center gap-2">
        <button id="f2-close-btn" class="bg-red-500 text-white px-4 py-2 rounded-md text-sm hover:bg-red-600 transition-colors">
          ✕ Close
        </button>
      </div>
    `;
    
    // Add close handler
    const closeBtn = header.querySelector('#f2-close-btn');
    closeBtn.addEventListener('click', () => {
      if (onClose) onClose();
    });
    
    return header;
  }

  function createChangesPanel() {
    const panel = document.createElement('div');
    panel.className = 'p-4 bg-gray-50 border-t border-gray-200';
    panel.innerHTML = `
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <div class="text-sm">
            <span class="font-medium">Pending Changes: </span>
            <span id="changes-count" class="font-bold text-gray-400">0</span>
          </div>
          <div id="changes-list" class="text-xs text-gray-500 max-w-md truncate">
            No pending changes - drag items to reorganize
          </div>
        </div>
        <div class="flex items-center gap-2">
          <button id="reset-btn" class="px-4 py-2 text-sm border border-gray-300 rounded-md hover:bg-gray-50 transition-colors" disabled>
            🔄 Reset
          </button>
          <button id="save-btn" class="px-4 py-2 text-sm bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors" disabled>
            💾 Save Changes
          </button>
        </div>
      </div>
    `;
    return panel;
  }

  function initializeSortableJS(navElement) {
    console.log('🎯 Initializing SortableJS');
    
    // Find all sortable containers - more specific selectors
    const containers = navElement.querySelectorAll('div[class*="ml-"], div[class*="border-l"], details > div');
    
    console.log(`🔍 Found ${containers.length} potential containers`);
    
    containers.forEach((container, index) => {
      // Check if container has draggable children (a tags or other links)
      const draggableChildren = container.querySelectorAll('a, details');
      
      if (draggableChildren.length > 0) {
        console.log(`✅ Initializing container ${index + 1} with ${draggableChildren.length} draggable children`);
        
        const sortable = Sortable.create(container, {
          group: 'shared',
          animation: 200,
          ghostClass: 'sortable-ghost',
          chosenClass: 'sortable-chosen',
          dragClass: 'sortable-drag',
          handle: 'a, summary', // Specific drag handles
          draggable: 'a, details', // What can be dragged
          
          onStart: function(evt) {
            console.log('🎬 Drag started on:', evt.item.textContent?.slice(0, 50));
            highlightValidDropZones(evt.item);
            evt.item.classList.add('dragging');
            document.body.classList.add('is-dragging');
          },
          
          onMove: function(evt) {
            const isValidMove = !isInvalidMove(evt.dragged, evt.related);
            updateDropZoneHighlight(evt.to);
            console.log('🔄 Move validation:', isValidMove);
            return isValidMove;
          },
          
          onEnd: function(evt) {
            console.log('🏁 Drag ended');
            clearDropZoneHighlights();
            evt.item.classList.remove('dragging');
            document.body.classList.remove('is-dragging');
            
            // Track the change
            if (evt.oldIndex !== evt.newIndex || evt.from !== evt.to) {
              console.log(`📝 Change detected: ${evt.oldIndex} -> ${evt.newIndex}`);
              trackMoveChange(evt);
              updateChangesPanel();
            }
          }
        });
        
        container.setAttribute('data-sortable-initialized', 'true');
      } else {
        console.log(`⏭️ Skipping container ${index + 1} - no draggable children`);
      }
    });
  }

  function initializePendingChanges() {
    window.f2PendingChanges = {
      moves: [],
      renames: [],
      reorders: []
    };
  }

  function trackMoveChange(evt) {
    const item = evt.item;
    const itemText = item.textContent.trim().substring(0, 50);
    
    const change = {
      type: 'move',
      item: itemText,
      fromIndex: evt.oldIndex,
      toIndex: evt.newIndex,
      fromContainer: evt.from,
      toContainer: evt.to,
      timestamp: Date.now()
    };
    
    window.f2PendingChanges.moves.push(change);
    console.log('📝 Tracked move:', change);
  }

  function updateChangesPanel() {
    const changesCount = document.getElementById('changes-count');
    const changesList = document.getElementById('changes-list');
    const resetBtn = document.getElementById('reset-btn');
    const saveBtn = document.getElementById('save-btn');
    
    const totalChanges = window.f2PendingChanges.moves.length + 
                        window.f2PendingChanges.renames.length + 
                        window.f2PendingChanges.reorders.length;
    
    if (changesCount) changesCount.textContent = totalChanges;
    
    if (totalChanges > 0) {
      if (changesList) {
        const recentChanges = window.f2PendingChanges.moves.slice(-3).map(c => 
          `Moved "${c.item.substring(0, 20)}"`
        ).join(', ');
        changesList.textContent = recentChanges;
      }
      if (resetBtn) resetBtn.disabled = false;
      if (saveBtn) saveBtn.disabled = false;
    } else {
      if (changesList) changesList.textContent = 'No pending changes - drag items to reorganize';
      if (resetBtn) resetBtn.disabled = true;
      if (saveBtn) saveBtn.disabled = true;
    }
  }

  // Utility functions from backup file
  function isInvalidMove(dragged, related) {
    if (dragged.contains && dragged.contains(related)) {
      return true;
    }
    
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

  function highlightValidDropZones(draggedItem) {
    const allContainers = document.querySelectorAll('[data-sortable-initialized]');
    allContainers.forEach(container => {
      container.classList.add('sortable-container-active');
      container.classList.add('valid-drop-zone');
    });
  }

  function updateDropZoneHighlight(targetContainer) {
    document.querySelectorAll('.drop-zone-hover').forEach(el => {
      el.classList.remove('drop-zone-hover');
    });
    
    if (targetContainer) {
      targetContainer.classList.add('drop-zone-hover');
    }
  }

  function clearDropZoneHighlights() {
    const highlights = document.querySelectorAll('.valid-drop-zone, .invalid-drop-zone, .drop-zone-hover, .sortable-container-active');
    highlights.forEach(el => {
      el.classList.remove('valid-drop-zone', 'invalid-drop-zone', 'drop-zone-hover', 'sortable-container-active');
    });
  }

  function addEnhancedSortableCSS() {
    if (document.getElementById('vanilla-sortable-css')) return;

    const style = document.createElement('style');
    style.id = 'vanilla-sortable-css';
    style.textContent = `
      /* Fix pointer-events for React overlay */
      #f2-navigation-react-root {
        pointer-events: none !important;
      }
      
      /* Only allow pointer events for specific interactive elements */
      #f2-navigation-react-root button,
      #f2-navigation-react-root [data-sortable-initialized],
      #f2-navigation-react-root [data-sortable-initialized] *,
      #f2-navigation-react-root details,
      #f2-navigation-react-root summary,
      #f2-navigation-react-root a {
        pointer-events: auto !important;
      }

      /* Enhanced Vanilla SortableJS Styling */
      .sortable-ghost {
        opacity: 0.6 !important;
        background: linear-gradient(135deg, #e3f2fd, #f3e5f5) !important;
        border: 2px dashed #2196f3 !important;
        border-radius: 8px !important;
        transform: scale(0.98) !important;
        transition: all 0.2s ease !important;
        box-shadow: 0 4px 12px rgba(33, 150, 243, 0.2) !important;
        pointer-events: auto !important;
      }
      
      .sortable-chosen {
        background: linear-gradient(135deg, #fff8e1, #fffde7) !important;
        border: 2px solid #ff9800 !important;
        border-radius: 8px !important;
        box-shadow: 0 4px 16px rgba(255, 152, 0, 0.3) !important;
        transform: scale(1.02) !important;
        transition: all 0.2s ease !important;
        pointer-events: auto !important;
      }
      
      .sortable-drag {
        opacity: 0.9 !important;
        transform: rotate(2deg) scale(1.05) !important;
        box-shadow: 0 8px 32px rgba(0,0,0,0.25) !important;
        z-index: 1000 !important;
        pointer-events: auto !important;
      }
      
      .dragging {
        cursor: grabbing !important;
        user-select: none !important;
        pointer-events: auto !important;
      }
      
      .valid-drop-zone {
        background: rgba(76, 175, 80, 0.1) !important;
        border: 2px solid #4caf50 !important;
        border-radius: 8px !important;
        transition: all 0.2s ease !important;
        pointer-events: auto !important;
      }
      
      .drop-zone-hover {
        background: rgba(33, 150, 243, 0.15) !important;
        border: 2px solid #2196f3 !important;
        transform: scale(1.01) !important;
        box-shadow: 0 4px 16px rgba(33, 150, 243, 0.2) !important;
        pointer-events: auto !important;
      }
      
      .sortable-container-active {
        background: rgba(76, 175, 80, 0.05) !important;
        border-radius: 8px !important;
        transition: all 0.2s ease !important;
        pointer-events: auto !important;
      }
      
      /* Enhanced hover effects */
      [data-sortable-initialized] li:hover {
        background: rgba(33, 150, 243, 0.05);
        border-radius: 4px;
        cursor: grab;
        transition: all 0.2s ease;
      }
      
      [data-sortable-initialized] a:hover {
        text-decoration: none;
        border-bottom: 2px solid #007bff;
        transform: translateX(2px);
        transition: all 0.2s ease;
      }
    `;
    
    document.head.appendChild(style);
  }

  function cleanupF2Navigation() {
    // Clear pending changes
    if (window.f2PendingChanges) {
      delete window.f2PendingChanges;
    }
    
    // Remove CSS
    const style = document.getElementById('vanilla-sortable-css');
    if (style) {
      style.remove();
    }
    
    console.log('🧹 F2 Navigation cleaned up');
  }

  if (!isVisible) {
    return null;
  }

  return (
    <div className="fixed inset-0 bg-white z-50 flex flex-col">
      <div ref={containerRef} className="flex-1 flex flex-col overflow-hidden" />
    </div>
  );
};

export default F2Navigation;