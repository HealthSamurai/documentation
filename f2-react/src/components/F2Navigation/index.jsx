import React, { useState, useEffect, useRef, useCallback } from 'react';
import NavigationTree from './NavigationTree';
import ChangePanel from './ChangePanel';
import DropPreview from './DropPreview';
import NestingIndicator from './NestingIndicator';
import PendingChangesIndicator from './PendingChangesIndicator';
import FullscreenPageManagement from './FullscreenPageManagement';
import { useSortable } from '../../hooks/useSortable';
import { siblingsFinder } from './services/SiblingsFinder';
import { pathSuggestions } from './services/PathSuggestions';
import './drag-styles.css';

const F2Navigation = ({ onClose, isVisible = false }) => {
  // Core state management
  const [isFullscreenMode, setIsFullscreenMode] = useState(false);
  const [navigationData, setNavigationData] = useState([]);
  const [pendingChanges, setPendingChanges] = useState({
    moves: [],
    renames: [],
    reorders: [],
    conversions: []
  });

  // Refs for sortable containers
  const navigationTreeRef = useRef(null);

  // Page Management Modal States
  const [showFullscreenForm, setShowFullscreenForm] = useState(true); // Показываем сразу по F2
  const [currentPagePath, setCurrentPagePath] = useState('');
  const [siblingPages, setSiblingPages] = useState([]);
  
  // Enhanced drag state
  const [dragState, setDragState] = useState({
    isDragging: false,
    draggedItem: null,
    draggedOver: null,
    dropPosition: null,
    nestingLevel: 0,
    originalParent: null,
    originalIndex: null
  });

  // Initialize when component becomes visible
  useEffect(() => {
    if (isVisible && !isFullscreenMode) {
      console.log('🚀 Initializing F2 Navigation Mode');
      initializeServices();
      setCurrentPagePath(window.location.pathname);
      setIsFullscreenMode(true);
    } else if (!isVisible && isFullscreenMode) {
      console.log('🔄 Exiting F2 Navigation Mode');
      setIsFullscreenMode(false);
      setShowFullscreenForm(false);
      setNavigationData([]);
      setPendingChanges({ moves: [], renames: [], reorders: [], conversions: [] });
    }
  }, [isVisible]);

  // Initialize siblings when form opens and path is available
  useEffect(() => {
    if (showFullscreenForm && currentPagePath) {
      handleInitializeSiblings();
    }
  }, [showFullscreenForm, currentPagePath]);

  const initializeServices = () => {
    // Инициализируем services
    const leftNav = document.querySelector('#navigation') || document.querySelector('nav');
    if (leftNav) {
      siblingsFinder.initialize(leftNav);
      pathSuggestions.initialize(leftNav);
      console.log('📋 Services initialized with navigation element');
    } else {
      console.warn('⚠️ Navigation element not found, using mock data');
    }

    // Также инициализируем старую логику для совместимости
    initializeNavigationData();
  };

  const initializeNavigationData = () => {
    // Try multiple selectors for navigation element
    const selectors = ['#navigation', 'nav#navigation', '.navigation', 'nav'];
    let leftNav = null;
    
    for (const selector of selectors) {
      leftNav = document.querySelector(selector);
      if (leftNav && leftNav.querySelectorAll('a').length > 10) {
        console.log(`✅ Found navigation using selector: ${selector}`);
        break;
      }
    }

    if (!leftNav) {
      console.error('❌ Left navigation element not found with any selector');
      // Показать пустое состояние вместо возврата
      setNavigationData([]);
      return;
    }

    console.log('🔍 Parsing navigation DOM...', leftNav.querySelectorAll('a').length, 'links found');
    const parsedData = parseNavigationDOM(leftNav);
    setNavigationData(parsedData);
    console.log('✅ Navigation data parsed:', parsedData.length, 'items');
  };

  const parseNavigationDOM = (navElement) => {
    const items = [];
    
    // Find all navigation items (both links and folders)
    const allElements = navElement.querySelectorAll('a, details');
    
    allElements.forEach((element, index) => {
      if (element.tagName === 'A') {
        // Document/link item
        const level = getElementLevel(element);
        const title = element.textContent.trim();
        
        items.push({
          id: `doc-${index}`,
          type: 'document',
          title: title,
          href: element.getAttribute('href') || '#',
          level: level,
          isMainSection: isMainSection(element),
          element: element
        });
      } else if (element.tagName === 'DETAILS') {
        // Folder item
        const summary = element.querySelector('summary');
        if (summary) {
          const level = getElementLevel(element);
          const title = summary.textContent.trim();
          
          items.push({
            id: `folder-${index}`,
            type: 'folder',
            title: title,
            isOpen: element.hasAttribute('open'),
            level: level,
            isMainSection: isMainSection(summary),
            children: [], // Will be populated in a second pass if needed
            element: element
          });
        }
      }
    });

    // Sort items to maintain document order
    items.sort((a, b) => {
      const aRect = a.element.getBoundingClientRect();
      const bRect = b.element.getBoundingClientRect();
      return aRect.top - bRect.top;
    });

    return items;
  };

  const getElementLevel = (element) => {
    // Calculate nesting level based on details elements and CSS classes
    let level = 0;
    let parent = element.parentElement;
    
    while (parent && parent.id !== 'navigation') {
      // Count details elements (folders)
      if (parent.tagName === 'DETAILS') {
        level++;
      }
      
      // Also check for margin-left classes that indicate nesting
      if (parent.classList.contains('ml-6') || 
          parent.classList.contains('ml-4') || 
          parent.classList.contains('ml-8')) {
        // Don't double count if we already counted the details
        if (parent.tagName !== 'DETAILS') {
          level += 0.5; // Half level for visual indentation
        }
      }
      
      parent = parent.parentElement;
    }
    
    return Math.floor(level);
  };

  const isMainSection = (element) => {
    // Определяем главные разделы (level 0 или 1) для разделителей
    const level = getElementLevel(element);
    const text = element.textContent.trim();
    
    // Главные разделы обычно короткие и на верхнем уровне
    const isTopLevel = level <= 1;
    const isShortTitle = text.length < 50;
    const isMainKeyword = /^(getting started|tutorials|overview|api|modules|reference|database|deployment)/i.test(text);
    
    return isTopLevel && (isShortTitle || isMainKeyword);
  };

  const handleClose = () => {
    console.log('🔄 Closing F2 Navigation Mode');
    setIsFullscreenMode(false);
    if (onClose) {
      onClose();
    }
  };

  const handleResetChanges = () => {
    console.log('🔄 Resetting pending changes');
    setPendingChanges({ moves: [], renames: [], reorders: [], conversions: [] });
  };

  const handleSaveChanges = () => {
    console.log('💾 Saving changes:', pendingChanges);
    // TODO: Implement save functionality in future tasks
    alert('Save functionality will be implemented in Task 007');
    handleResetChanges();
  };

  const handleRename = (item) => {
    console.log('✏️ Rename requested for:', item.title);
    // Открываем Fullscreen Form для rename
    setCurrentPagePath(item.href || window.location.pathname);
    setShowFullscreenForm(true);
  };

  // Page Management Handlers
  const handleMoveRename = (changeData) => {
    console.log('📝 Move/Rename operation:', changeData);
    
    const change = createChangeRecord({ type: 'renames' }, {
      operation: changeData.operation,
      oldPath: changeData.oldPath,
      newPath: changeData.newPath,
      reason: 'pageManagement'
    });
    
    setPendingChanges(prev => ({
      ...prev,
      renames: [...prev.renames, change]
    }));
    
    console.log('✅ Page management change created:', change);
  };

  const handleInitializeSiblings = () => {
    console.log('🔄 Initializing siblings for:', currentPagePath);
    
    // Получаем соседние страницы для табbed modal
    const siblings = siblingsFinder.getSiblings(currentPagePath);
    setSiblingPages(siblings);
  };

  const handleSaveOrder = (newOrder) => {
    console.log('💾 Saving new page order:', newOrder);
    
    const orderChange = createChangeRecord({ type: 'reorders' }, {
      pages: newOrder.map(page => page.path),
      reason: 'orderChange'
    });
    
    setPendingChanges(prev => ({
      ...prev,
      reorders: [...prev.reorders, orderChange]
    }));
    
    console.log('✅ Order change created:', orderChange);
  };

  // Helper Functions for Drag-and-Drop
  
  const findNodeById = (nodeId) => {
    return navigationData.find(item => item.id === nodeId);
  };

  const calculateNestingLevel = (clientX) => {
    if (!navigationTreeRef.current) return 0;
    
    const baseX = navigationTreeRef.current.getBoundingClientRect().left;
    const offset = clientX - baseX;
    const levelWidth = 24; // pixels per nesting level
    return Math.max(0, Math.min(3, Math.floor(offset / levelWidth)));
  };

  const canDrop = (draggedNode, targetNode, nestingLevel) => {
    if (!draggedNode || !targetNode) return false;
    
    // Prevent self-drop
    if (draggedNode.id === targetNode.id) return false;
    
    // Prevent circular reference (simplified check)
    if (targetNode.type === 'folder' && draggedNode.type === 'folder') {
      // More complex circular check would be needed for real implementation
      return true;
    }
    
    // Check depth limit
    const targetDepth = targetNode.level || 0;
    if (targetDepth + nestingLevel > 3) return false;
    
    return true;
  };

  const createChangeRecord = (operation, details) => {
    return {
      id: `${operation.type}-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
      type: operation.type,
      timestamp: Date.now(),
      ...details
    };
  };

  const determineOperation = (from, to, oldIndex, newIndex) => {
    const fromContainerId = from.dataset?.containerId || 'root';
    const toContainerId = to.dataset?.containerId || 'root';
    
    if (fromContainerId !== toContainerId) {
      return { type: 'moves' };
    } else if (oldIndex !== newIndex) {
      return { type: 'reorders' };
    }
    
    return { type: 'reorders' }; // default
  };

  // Special Cases Handling

  const handleDropOnDocument = (draggedNode, targetDocument) => {
    // Convert document to folder and make draggedNode its first child
    const conversionChange = createChangeRecord({ type: 'conversions' }, {
      nodeId: targetDocument.id,
      fromType: 'document',
      toType: 'folder',
      newChild: draggedNode.id,
      reason: 'dropOnDocument'
    });
    
    console.log('📁 Converting document to folder:', targetDocument.title);
    
    setPendingChanges(prev => ({
      ...prev,
      conversions: [...(prev.conversions || []), conversionChange]
    }));
    
    return conversionChange;
  };

  const handleNestingChange = (draggedNode, newLevel) => {
    const oldLevel = draggedNode.level || 0;
    
    if (oldLevel !== newLevel) {
      const nestingChange = createChangeRecord({ type: 'moves' }, {
        nodeId: draggedNode.id,
        fromLevel: oldLevel,
        toLevel: newLevel,
        reason: 'nestingChange'
      });
      
      console.log(`📊 Nesting level changed: ${draggedNode.title} from ${oldLevel} to ${newLevel}`);
      
      return nestingChange;
    }
    
    return null;
  };

  const isDropOnDocument = (targetNode, dropPosition) => {
    return targetNode?.type === 'document' && dropPosition === 'inside';
  };

  const calculateDropPosition = (evt) => {
    if (!evt.originalEvent) return 'after';
    
    const rect = evt.related.getBoundingClientRect();
    const y = evt.originalEvent.clientY;
    const relativeY = y - rect.top;
    const height = rect.height;
    
    if (relativeY < height * 0.25) return 'before';
    if (relativeY > height * 0.75) return 'after';
    return 'inside';
  };

  // Enhanced Drag Event Handlers - стабилизированы с useCallback
  const handleDragStart = useCallback((evt) => {
    const draggedElement = evt.item;
    const nodeId = draggedElement.dataset.nodeId;
    const draggedNode = findNodeById(nodeId);
    
    console.log('🎬 Drag started:', {
      nodeId,
      title: draggedNode?.title || 'unknown',
      element: draggedElement,
      hasDataset: !!draggedElement.dataset,
      allDatasets: Object.keys(draggedElement.dataset || {})
    });
    
    setDragState({
      isDragging: true,
      draggedItem: draggedNode,
      originalParent: evt.from.dataset.containerId || 'root',
      originalIndex: evt.oldIndex,
      draggedOver: null,
      dropPosition: null,
      nestingLevel: 0
    });
    
    // Add visual feedback
    document.body.classList.add('dragging-active');
    draggedElement.classList.add('dragging');
  }, [navigationData]); // зависимость от navigationData для findNodeById

  const handleDragMove = useCallback((evt) => {
    if (!evt.originalEvent || !dragState.draggedItem) return true;
    
    const { dragged, related } = evt;
    const targetNode = related ? findNodeById(related.dataset.nodeId) : null;
    
    // Calculate nesting level and drop position
    const nestingLevel = calculateNestingLevel(evt.originalEvent.clientX);
    const dropPosition = calculateDropPosition(evt);
    
    // Update drag state
    setDragState(prev => ({
      ...prev,
      draggedOver: targetNode,
      nestingLevel: nestingLevel,
      dropPosition: dropPosition
    }));
    
    // Special case: dropping on document
    if (isDropOnDocument(targetNode, dropPosition)) {
      console.log('📁 Will convert document to folder:', targetNode.title);
      // Allow drop - conversion will happen in handleDragEnd
      return true;
    }
    
    // Validate drop
    if (!canDrop(dragState.draggedItem, targetNode, nestingLevel)) {
      console.log('🚫 Invalid drop target');
      return false; // Prevent drop
    }
    
    console.log('✅ Valid drop target:', targetNode?.title, 'at level', nestingLevel, 'position', dropPosition);
    return true;
  }, [dragState.draggedItem, navigationData]); // зависимости от dragState и navigationData

  const handleDragEnd = useCallback((evt) => {
    const { item, to, from, oldIndex, newIndex } = evt;
    const draggedNode = dragState.draggedItem;
    const targetNode = dragState.draggedOver;
    
    console.log('🎬 Drag ended:', draggedNode?.title || 'unknown');
    
    // Remove visual feedback
    document.body.classList.remove('dragging-active');
    item.classList.remove('dragging');
    
    // Check if item was actually moved
    if (oldIndex !== newIndex || from !== to) {
      const changes = [];
      
      // Handle special case: drop on document (convert to folder)
      if (isDropOnDocument(targetNode, dragState.dropPosition)) {
        const conversionChange = handleDropOnDocument(draggedNode, targetNode);
        changes.push(conversionChange);
      }
      
      // Handle nesting level change
      const nestingChange = handleNestingChange(draggedNode, dragState.nestingLevel);
      if (nestingChange) {
        changes.push(nestingChange);
      }
      
      // Create main operation change record
      const operation = determineOperation(from, to, oldIndex, newIndex);
      const mainChange = createChangeRecord(operation, {
        nodeId: draggedNode.id,
        fromContainer: from.dataset.containerId || 'root',
        toContainer: to.dataset.containerId || 'root',
        fromIndex: oldIndex,
        toIndex: newIndex,
        nestingLevel: dragState.nestingLevel,
        dropPosition: dragState.dropPosition,
        targetNode: targetNode,
        draggedNode: draggedNode
      });
      
      console.log('📦 Created change record:', mainChange);
      changes.push(mainChange);
      
      // Update pending changes with all changes
      setPendingChanges(prev => {
        const updated = { ...prev };
        
        changes.forEach(change => {
          const changeType = change.type.endsWith('s') ? change.type : change.type + 's';
          if (!updated[changeType]) updated[changeType] = [];
          updated[changeType] = [...updated[changeType], change];
        });
        
        return updated;
      });
      
      console.log(`📊 Total changes created: ${changes.length}`);
    }
    
    // Reset drag state
    setDragState({
      isDragging: false,
      draggedItem: null,
      draggedOver: null,
      dropPosition: null,
      nestingLevel: 0,
      originalParent: null,
      originalIndex: null
    });
  }, [dragState.draggedItem, dragState.draggedOver, dragState.dropPosition, dragState.nestingLevel, setPendingChanges]); // все необходимые зависимости

  // Initialize SortableJS
  useSortable(navigationTreeRef, {
    onStart: handleDragStart,
    onEnd: handleDragEnd,
    onMove: handleDragMove,
    disabled: navigationData.length === 0
  });

  // Don't render if not in fullscreen mode
  if (!isVisible || !isFullscreenMode) {
    return null;
  }

  return (
    <>
      {/* Fullscreen Page Management - Показывается сразу при F2 */}
      <FullscreenPageManagement
        isOpen={showFullscreenForm}
        onClose={() => {
          setShowFullscreenForm(false);
          handleClose(); // Закрываем весь F2 режим если закрыли форму
        }}
        currentPath={currentPagePath}
        onMoveRename={handleMoveRename}
        currentPage={siblingPages.find(p => p.path === currentPagePath)}
        siblings={siblingPages}
        onSaveOrder={handleSaveOrder}
      />

      {/* Pending Changes Indicator - показываем всегда когда есть изменения */}
      <PendingChangesIndicator 
        count={Object.values(pendingChanges).reduce((sum, changes) => sum + changes.length, 0)}
      />

      {/* Legacy Fullscreen Mode - только для debugging/compatibility */}
      {isFullscreenMode && false && ( // Отключаем старый интерфейс
        <div className="fixed inset-0 bg-white z-40 flex flex-col font-sans">
          {/* Header */}
          <div className="flex items-center justify-between px-6 py-4 bg-white border-b border-gray-200 flex-shrink-0">
            <div className="flex items-center gap-4">
              <div className="bg-blue-600 text-white px-3 py-2 rounded-md text-sm font-semibold tracking-wide">
                F2 Navigation Mode (Legacy)
              </div>
              <div className="text-sm text-gray-500">
                Press F2 to exit • New Page Management UI active
              </div>
            </div>
            <div className="flex items-center gap-2">
              <button 
                className="bg-red-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-red-700 transition-colors duration-200"
                onClick={handleClose}
                type="button"
              >
                ✕ Close
              </button>
            </div>
          </div>

          {/* Main Content */}
          <div className="flex-1 overflow-auto px-6 py-6 bg-gray-50">
            <NavigationTree 
              ref={navigationTreeRef}
              items={navigationData}
              dragState={dragState}
              onRename={handleRename}
              onItemChange={(change) => {
                console.log('Navigation change:', change);
              }}
            />
          </div>

          {/* Changes Panel */}
          <ChangePanel 
            pendingChanges={pendingChanges}
            onReset={handleResetChanges}
            onSave={handleSaveChanges}
          />
        </div>
      )}
    </>
  );
};

export default F2Navigation;