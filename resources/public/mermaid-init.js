// Mermaid initialization for both initial page load and HTMX navigation
let mermaidInitialized = false;

// Initialize mermaid library once
function initializeMermaidLibrary() {
  if (typeof mermaid === 'undefined') {
    console.error('Mermaid library not loaded');
    return false;
  }

  if (typeof window.MERMAID_CONFIG === 'undefined') {
    console.error('Mermaid config not loaded');
    return false;
  }

  if (!mermaidInitialized) {
    mermaid.initialize(window.MERMAID_CONFIG);
    mermaidInitialized = true;
  }
  
  return true;
}

// Export to window for inline script calls
window.initializeMermaidLibrary = initializeMermaidLibrary;

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
  initializeMermaidLibrary();
  // initializeContent will be called by inline script
});

// Re-initialize after HTMX swaps - this is critical for navigation
document.addEventListener('htmx:afterSwap', function(event) {
  // Ensure mermaid is initialized
  initializeMermaidLibrary();
  
  // Call the combined initialization function
  if (typeof window.initializeContent === 'function') {
    setTimeout(function() {
      window.initializeContent();
    }, 100);
  }
});

// Handle HTMX history restore (back/forward navigation)
document.addEventListener('htmx:historyRestore', function() {
  if (typeof window.initializeContent === 'function') {
    setTimeout(function() {
      window.initializeContent();
    }, 100);
  }
});

// Handle browser back/forward navigation
window.addEventListener('popstate', function() {
  if (typeof window.initializeContent === 'function') {
    setTimeout(function() {
      window.initializeContent();
    }, 100);
  }
});