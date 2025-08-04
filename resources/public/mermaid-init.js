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
    console.log(`Found ${mermaidElements.length} Mermaid diagrams to render`);
    
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

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
  initializeMermaid();
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function() {
  // Small delay to ensure DOM is fully updated
  setTimeout(initializeMermaid, 50);
});