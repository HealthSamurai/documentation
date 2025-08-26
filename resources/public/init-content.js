// Combined initialization for hljs and mermaid after content loads

function initializeContent() {
  // Initialize highlight.js
  if (typeof hljs !== 'undefined') {
    hljs.highlightAll();
  }
  
  // Initialize mermaid diagrams
  if (typeof mermaid !== 'undefined' && typeof window.MERMAID_CONFIG !== 'undefined') {
    // Find all mermaid elements
    const mermaidElements = document.querySelectorAll('pre.mermaid');
    
    if (mermaidElements.length > 0) {
      // First clean up any existing containers
      document.querySelectorAll('.mermaid-container').forEach(container => {
        container.remove();
      });
      
      mermaidElements.forEach((element, index) => {
        // Reset the element
        element.removeAttribute('data-processed');
        element.style.display = 'block';
        
        // Generate unique ID
        const id = `mermaid-${Date.now()}-${index}-${Math.random().toString(36).substr(2, 9)}`;
        
        // Get the diagram text
        const graphDefinition = element.textContent || element.innerText;
        
        // Create a container div
        const container = document.createElement('div');
        container.className = 'mermaid-container';
        
        // Insert container after the pre element
        element.parentNode.insertBefore(container, element.nextSibling);
        
        // Hide the original pre element
        element.style.display = 'none';
        element.setAttribute('data-processed', 'true');
        
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
}

// Export to window for inline script calls
window.initializeContent = initializeContent;