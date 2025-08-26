// Highlight.js initialization for both initial page load and HTMX navigation

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
  if (typeof hljs !== 'undefined') {
    hljs.highlightAll();
  }
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function(event) {
  if (typeof hljs !== 'undefined') {
    // Only highlight code blocks within the swapped content
    const target = event.detail.target;
    if (target) {
      const codeBlocks = target.querySelectorAll('pre code:not(.hljs)');
      codeBlocks.forEach(block => {
        hljs.highlightElement(block);
      });
    }
  }
});

// Handle HTMX history restore (back/forward navigation)
document.addEventListener('htmx:historyRestore', function() {
  if (typeof hljs !== 'undefined') {
    // Small delay to ensure DOM is ready
    setTimeout(() => {
      hljs.highlightAll();
    }, 50);
  }
});