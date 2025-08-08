// Mermaid initialization for both initial page load and HTMX navigation
let mermaidInitialized = false;

function initializeMermaid() {
  // Check if mermaid is loaded
  if (typeof mermaid === 'undefined') {
    console.error('Mermaid library not loaded');
    return;
  }

  // Initialize mermaid only once
  if (!mermaidInitialized) {
    mermaid.initialize({
      startOnLoad: false, // We'll manually trigger rendering
      theme: 'default',
      securityLevel: 'loose', // Changed to loose to allow more flexibility
      flowchart: {
        useMaxWidth: true,
        htmlLabels: true
      }
    });
    mermaidInitialized = true;
  }

  // Find all unprocessed mermaid diagrams
  const mermaidElements = document.querySelectorAll('pre.mermaid:not([data-processed="true"])');
  
  if (mermaidElements.length > 0) {
    mermaidElements.forEach((element, index) => {
      // Mark as processed to avoid re-rendering
      element.setAttribute('data-processed', 'true');
      
      // Generate unique ID
      const id = `mermaid-${Date.now()}-${index}`;
      
      // Get the diagram text
      const graphDefinition = element.textContent || element.innerText;
      
      // Create a container div
      const container = document.createElement('div');
      container.className = 'mermaid-container';
      
      // Insert container after the pre element
      element.parentNode.insertBefore(container, element.nextSibling);
      
      // Hide the original pre element
      element.style.display = 'none';
      
      // Render the diagram
      try {
        mermaid.render(id, graphDefinition).then((result) => {
          container.innerHTML = result.svg;
        }).catch((error) => {
          console.error('Error rendering Mermaid diagram:', error);
          // Show original on error
          element.style.display = 'block';
          element.removeAttribute('data-processed');
          container.remove();
        });
      } catch (error) {
        console.error('Error initializing Mermaid diagram:', error);
        // Show original on error
        element.style.display = 'block';
        element.removeAttribute('data-processed');
        container.remove();
      }
    });
  }
}

// Helper function to reset mermaid elements for re-rendering
function resetMermaidElements() {
  document.querySelectorAll('pre.mermaid[data-processed="true"]').forEach(element => {
    element.removeAttribute('data-processed');
    element.style.display = 'block';
    // Remove any existing rendered containers
    const nextSibling = element.nextElementSibling;
    if (nextSibling && nextSibling.classList.contains('mermaid-container')) {
      nextSibling.remove();
    }
  });
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
  initializeMermaid();
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function() {
  // Small delay to ensure DOM is fully updated
  setTimeout(initializeMermaid, 50);
});

// Handle HTMX history restore (back/forward navigation)
document.addEventListener('htmx:historyRestore', function() {
  resetMermaidElements();
  setTimeout(initializeMermaid, 50);
});

// Handle browser back/forward navigation (fallback for non-HTMX navigation)
window.addEventListener('popstate', function() {
  resetMermaidElements();
  setTimeout(initializeMermaid, 50);
});

// Handle page show event (for bfcache restoration)
window.addEventListener('pageshow', function(event) {
  if (event.persisted) {
    resetMermaidElements();
    setTimeout(initializeMermaid, 50);
  }
});

// Also handle HTMX beforeSwap to clean up before new content
document.addEventListener('htmx:beforeSwap', function(event) {
  // Only reset if we're swapping content that might have mermaid diagrams
  if (event.detail.target && event.detail.target.querySelector) {
    const mermaidInTarget = event.detail.target.querySelectorAll('pre.mermaid[data-processed="true"]');
    if (mermaidInTarget.length > 0) {
      mermaidInTarget.forEach(element => {
        element.removeAttribute('data-processed');
        element.style.display = 'block';
        const nextSibling = element.nextElementSibling;
        if (nextSibling && nextSibling.classList.contains('mermaid-container')) {
          nextSibling.remove();
        }
      });
    }
  }
});