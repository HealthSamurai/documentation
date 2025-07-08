// Hide TOC container when search is triggered
document.addEventListener('DOMContentLoaded', function() {
  // Listen for HTMX requests to search
  document.addEventListener('htmx:beforeRequest', function(event) {
    const url = event.detail.xhr.url;
    if (url && url.includes('/search')) {
      const tocContainer = document.getElementById('toc-container');
      if (tocContainer) {
        tocContainer.style.display = 'none';
      }
    }
  });

  // Also hide TOC when clicking on search button
  document.addEventListener('click', function(event) {
    const searchLink = event.target.closest('a[href="/search"], .search-button');
    if (searchLink) {
      const tocContainer = document.getElementById('toc-container');
      if (tocContainer) {
        tocContainer.style.display = 'none';
      }
    }
  });

  // Show TOC again when leaving search page
  document.addEventListener('htmx:afterSwap', function(event) {
    const url = window.location.pathname;
    if (!url.includes('/search')) {
      const tocContainer = document.getElementById('toc-container');
      if (tocContainer) {
        tocContainer.style.display = '';
      }
    }
  });

  // Handle Ctrl+K keyboard shortcut
  document.addEventListener('keydown', function(event) {
    if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
      const tocContainer = document.getElementById('toc-container');
      if (tocContainer) {
        tocContainer.style.display = 'none';
      }
    }
  });
}); 