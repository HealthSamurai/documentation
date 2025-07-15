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
    // Find prev button - look for buttons with HTMX attributes first
    let prevButton = null;
    
    // Try to find HTMX-enabled prev button
    const htmxButtons = document.querySelectorAll('a[hx-get][href]');
    for (const btn of htmxButtons) {
      if (btn.textContent.includes('Previous')) {
        prevButton = btn;
        break;
      }
    }
    
    // Fallback to any prev button
    if (!prevButton) {
      const allButtons = document.querySelectorAll('a[href*="/"]');
      for (const btn of allButtons) {
        if (btn.textContent.includes('Previous')) {
          prevButton = btn;
          break;
        }
      }
    }
    
    if (prevButton) {
      event.preventDefault();
      
      // Use HTMX if available and button has HTMX attributes
      if (typeof htmx !== 'undefined' && prevButton.hasAttribute('hx-get')) {
        prevButton.click(); // Trigger HTMX request
      } else {
        // Fallback to direct navigation
        window.location.href = prevButton.href;
      }
    }
  }

  // Right arrow key (Next page)
  if (event.key === 'ArrowRight') {
    // Find next button - look for buttons with HTMX attributes first
    let nextButton = null;
    
    // Try to find HTMX-enabled next button
    const htmxButtons = document.querySelectorAll('a[hx-get][href]');
    for (const btn of htmxButtons) {
      if (btn.textContent.includes('Next')) {
        nextButton = btn;
        break;
      }
    }
    
    // Fallback to any next button
    if (!nextButton) {
      const allButtons = document.querySelectorAll('a[href*="/"]');
      for (const btn of allButtons) {
        if (btn.textContent.includes('Next')) {
          nextButton = btn;
          break;
        }
      }
    }
    
    if (nextButton) {
      event.preventDefault();
      
      // Use HTMX if available and button has HTMX attributes
      if (typeof htmx !== 'undefined' && nextButton.hasAttribute('hx-get')) {
        nextButton.click(); // Trigger HTMX request
      } else {
        // Fallback to direct navigation
        window.location.href = nextButton.href;
      }
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
