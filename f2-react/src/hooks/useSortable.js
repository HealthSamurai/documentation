import { useEffect, useRef } from 'react';

/**
 * Custom hook for managing SortableJS lifecycle with React
 * Handles initialization, configuration, and cleanup automatically
 */
export const useSortable = (containerRef, options = {}) => {
  const sortableInstanceRef = useRef(null);

  useEffect(() => {
    // Only initialize if we have a container and Sortable is available
    if (!containerRef.current || !window.Sortable) {
      console.warn('🚫 SortableJS not available or container not found', {
        hasContainer: !!containerRef.current,
        hasSortable: !!window.Sortable,
        containerChildren: containerRef.current?.children?.length || 0
      });
      return;
    }

    // Дополнительная проверка что контейнер готов
    const container = containerRef.current;
    if (!container.children || container.children.length === 0) {
      console.warn('🚫 Container has no children, skipping initialization');
      return;
    }

    // Destroy existing instance if it exists
    if (sortableInstanceRef.current) {
      sortableInstanceRef.current.destroy();
      sortableInstanceRef.current = null;
    }

    try {
      console.log('🚀 Initializing SortableJS instance', {
        containerTag: container.tagName,
        containerClass: container.className,
        childrenCount: container.children.length,
        draggableElements: container.querySelectorAll('.navigation-item').length,
        dragHandles: container.querySelectorAll('.drag-handle').length
      });
      
      // Create SortableJS instance
      sortableInstanceRef.current = window.Sortable.create(containerRef.current, {
        // Default options
        group: {
          name: 'navigation-tree',
          pull: true,
          put: true
        },
        animation: 200,
        easing: "cubic-bezier(1, 0, 0, 1)",
        fallbackOnBody: true,
        swapThreshold: 0.5,
        direction: 'vertical',
        
        // CSS classes for visual feedback
        ghostClass: 'sortable-ghost',
        chosenClass: 'sortable-chosen', 
        dragClass: 'sortable-drag',
        
        // Drag handle - УБРАНО для полного drag-and-drop
        // handle: '.drag-handle',
        
        // What elements can be dragged
        draggable: '.navigation-item',
        
        // Prevent dragging on specific elements (rename buttons, etc)
        filter: '.rename-icon, .no-drag',
        preventOnFilter: false,
        
        // Override with custom options
        ...options,
        
        // Wrap event handlers to ensure they're called
        onStart: (evt) => {
          console.log('🎬 Drag started');
          evt.item.classList.add('dragging');
          options.onStart?.(evt);
        },
        
        onEnd: (evt) => {
          console.log('🎬 Drag ended');
          evt.item.classList.remove('dragging');
          options.onEnd?.(evt);
        },
        
        onMove: (evt) => {
          // Calculate nesting level based on cursor position
          if (evt.originalEvent) {
            const rect = evt.to.getBoundingClientRect();
            const offsetX = evt.originalEvent.clientX - rect.left;
            const nestingLevel = Math.max(0, Math.min(3, Math.floor(offsetX / 20)));
            evt.nestingLevel = nestingLevel;
          }
          
          return options.onMove?.(evt);
        },
        
        onSort: (evt) => {
          console.log('🔄 Sort event triggered');
          options.onSort?.(evt);
        }
      });
      
      console.log('✅ SortableJS instance created successfully');
      
    } catch (error) {
      console.error('❌ Failed to create SortableJS instance:', error);
    }

    // Cleanup function с лучшей обработкой ошибок
    return () => {
      if (sortableInstanceRef.current) {
        console.log('🧹 Cleaning up SortableJS instance');
        try {
          const instance = sortableInstanceRef.current;
          if (instance && typeof instance.destroy === 'function') {
            instance.destroy();
          }
          sortableInstanceRef.current = null;
        } catch (error) {
          console.error('❌ Error destroying SortableJS instance:', error);
          // Принудительно очищаем ссылку даже при ошибке
          sortableInstanceRef.current = null;
        }
      }
    };
  }, [containerRef, options.onStart, options.onEnd, options.onMove, options.onSort, options.disabled]); // добавляем disabled

  // Return the instance for manual control if needed
  return sortableInstanceRef.current;
};

/**
 * Helper function to check if SortableJS is available
 */
export const isSortableAvailable = () => {
  return typeof window !== 'undefined' && window.Sortable;
};

/**
 * Default sortable options for navigation tree
 */
export const getDefaultSortableOptions = () => ({
  group: {
    name: 'navigation-tree',
    pull: true,
    put: (to, from, dragEl) => {
      // Basic validation - can be extended
      return dragEl && to && from;
    }
  },
  animation: 200,
  easing: "cubic-bezier(1, 0, 0, 1)",
  fallbackOnBody: true,
  swapThreshold: 0.5,
  direction: 'vertical',
  ghostClass: 'sortable-ghost',
  chosenClass: 'sortable-chosen',
  dragClass: 'sortable-drag',
  // handle: '.drag-handle', // УБРАНО для полного drag-and-drop
  draggable: '.navigation-item',
  filter: '.rename-icon, .no-drag',
  preventOnFilter: false
});