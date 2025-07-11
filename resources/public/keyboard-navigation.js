// Keyboard navigation for Previous/Next pages
document.addEventListener('keydown', function (event) {
  // Only handle arrow keys if not typing in an input field
  if (event.target.tagName === 'INPUT' || event.target.tagName === 'TEXTAREA' || event.target.contentEditable === 'true') {
    return;
  }

  // Don't handle Alt+Arrow combinations (system hotkeys)
  if (event.altKey) {
    return;
  }

  // Left arrow key (Previous page)
  if (event.key === 'ArrowLeft') {
    const buttons = Array.from(document.querySelectorAll('a[href*="/"]'));
    const prevButton = buttons.find(btn => btn.textContent.trim().includes('Previous'));
    if (prevButton) {
      event.preventDefault();
      // Use direct navigation instead of HTMX for keyboard
      window.location.href = prevButton.href;
    }
  }

  // Right arrow key (Next page)
  if (event.key === 'ArrowRight') {
    const buttons = Array.from(document.querySelectorAll('a[href*="/"]'));
    const nextButton = buttons.find(btn => btn.textContent.trim().includes('Next'));
    if (nextButton) {
      event.preventDefault();
      // Use direct navigation instead of HTMX for keyboard
      window.location.href = nextButton.href;
    }
  }
});

document.addEventListener('DOMContentLoaded', function() {
    // Focus search input if on search page
    const searchInput = document.getElementById('search-input');
    if (searchInput && window.location.pathname === '/search') {
        searchInput.focus();
    }

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        // Cmd/Ctrl + K for search
        if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
            e.preventDefault();
            window.location.href = '/search';
        }
    });

    // Handle Ctrl+click to open links in new tab
    document.addEventListener('click', function(e) {
        // Check if Ctrl key (or Cmd on Mac) is pressed during click
        if (e.ctrlKey || e.metaKey) {
            const target = e.target.closest('a[href]');
            if (target && target.href) {
                // Prevent HTMX from handling this click
                e.stopPropagation();
                e.preventDefault();
                
                // Open link in new tab using browser's default behavior
                window.open(target.href, '_blank');
            }
        }
    }, true); // Use capture phase to handle before HTMX
});
