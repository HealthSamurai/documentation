// Keyboard navigation for Previous/Next pages
document.addEventListener('keydown', function (event) {
  // Only handle arrow keys if not typing in an input field (except for search input)
  if ((event.target.tagName === 'INPUT' || event.target.tagName === 'TEXTAREA' || event.target.contentEditable === 'true') && event.target.id !== 'search-input') {
    return;
  }

  // Don't handle Alt+Arrow combinations (system hotkeys)
  if (event.altKey) {
    return;
  }

  // Special handling for search input dropdown navigation
  if (event.target.id === 'search-input') {
    const dropdown = document.getElementById('search-dropdown');

    // Check if dropdown is visible and has content
    if (dropdown && (dropdown.hasAttribute('data-search-dropdown') || dropdown.querySelector('a[data-search-result-index]'))) {
      const results = dropdown.querySelectorAll('a[data-search-result-index]');

      // Only handle navigation keys if dropdown has visible results
      if (results.length > 0 && (event.key === 'ArrowDown' || event.key === 'ArrowUp' || event.key === 'Enter')) {
        let currentIndex = -1;

        // Find currently selected item
        for (let i = 0; i < results.length; i++) {
          if (results[i].classList.contains('bg-warning-2')) {
            currentIndex = i;
            break;
          }
        }

        if (event.key === 'ArrowDown') {
          event.preventDefault();
          // If no item selected, select first item (0), otherwise move down
          currentIndex = currentIndex < results.length - 1 ? currentIndex + 1 : 0;
          updateSelectedResult(results, currentIndex);
          return;
        } else if (event.key === 'ArrowUp') {
          event.preventDefault();
          // If no item selected, select last item, otherwise move up
          currentIndex = currentIndex <= 0 ? results.length - 1 : currentIndex - 1;
          updateSelectedResult(results, currentIndex);
          return;
        } else if (event.key === 'Enter') {
          event.preventDefault();
          if (currentIndex >= 0 && currentIndex < results.length) {
            results[currentIndex].click();
          }
          return;
        }
      }

      // Handle Escape regardless of results
      if (event.key === 'Escape') {
        event.preventDefault();
        dropdown.innerHTML = '';
        return;
      }
    }
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

// Helper function to update selected result in dropdown
function updateSelectedResult(results, selectedIndex) {
  results.forEach((result, index) => {
    // Check if this is a section entry (has border-l-2 element inside)
    const isSectionEntry = result.querySelector('.border-l-2') !== null;

    if (index === selectedIndex) {
      result.classList.add('bg-warning-2', 'text-tint-strong');
      result.classList.remove('hover:bg-tint-2', 'hover:text-tint-strong');

      // Remove appropriate text color based on entry type
      if (isSectionEntry) {
        result.classList.remove('text-tint-10');
        // Also update all child p elements with text-tint-10 class (these contain the actual text)
        const childElements = result.querySelectorAll('p.text-tint-10');
        childElements.forEach(child => {
          child.classList.remove('text-tint-10');
          child.classList.add('text-tint-strong');
        });
      } else {
        result.classList.remove('text-tint');
      }

      // Update the file icon color
      const fileIcon = result.querySelector('div:first-of-type');
      if (fileIcon) {
        fileIcon.classList.add('text-warning-9');
        fileIcon.classList.remove('text-tint-9', 'group-hover:text-tint-strong');
      }
      // Update the enter icon - replace with Enter key
      const enterIcon = result.querySelector('.search-action-button');
      if (enterIcon) {
        enterIcon.classList.add('text-orange-600', 'opacity-100');
        enterIcon.classList.remove('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
        enterIcon.innerHTML = '<div class="px-2 py-1 rounded border text-xs font-medium flex items-center justify-center bg-orange-600 text-white border-orange-700">↵</div>';
      }
      // Scroll selected item into view
      result.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    } else {
      result.classList.remove('bg-warning-2', 'text-tint-strong');

      // Restore appropriate text color based on entry type
      if (isSectionEntry) {
        result.classList.add('text-tint-10');
        // Also restore all child p elements with text-tint-strong class back to text-tint-10
        const childElements = result.querySelectorAll('p.text-tint-strong');
        childElements.forEach(child => {
          child.classList.remove('text-tint-strong');
          child.classList.add('text-tint-10');
        });
      } else {
        result.classList.add('text-tint');
      }

      // Update the file icon color
      const fileIcon = result.querySelector('div:first-of-type');
      if (fileIcon) {
        fileIcon.classList.remove('text-warning-9');
        fileIcon.classList.add('text-tint-9', 'group-hover:text-tint-strong');
      }
      // Update the enter icon - restore arrow icon
      const enterIcon = result.querySelector('.search-action-button');
      if (enterIcon) {
        enterIcon.classList.remove('text-orange-600', 'opacity-100');
        enterIcon.classList.add('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
        enterIcon.innerHTML = '<svg class="size-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"></path></svg>';
      }
    }
  });
}

document.addEventListener('DOMContentLoaded', function () {
  // Focus search input if on search page
  const searchInput = document.getElementById('search-input');
  if (searchInput && window.location.pathname === '/search') {
    searchInput.focus();
  }

  // Handle search shortcut visibility
  const searchShortcut = document.getElementById('search-shortcut');
  if (searchInput && searchShortcut) {
    // Hide shortcut when input has focus or content
    function updateShortcutVisibility() {
      if (searchInput.value.length > 0 || document.activeElement === searchInput) {
        searchShortcut.style.opacity = '0';
      } else {
        searchShortcut.style.opacity = '1';
      }
    }

    // Add event listeners
    searchInput.addEventListener('focus', updateShortcutVisibility);
    searchInput.addEventListener('blur', updateShortcutVisibility);
    searchInput.addEventListener('input', updateShortcutVisibility);

    // Initial check
    updateShortcutVisibility();
  }

  // Keyboard shortcuts
  document.addEventListener('keydown', function (e) {
    // Cmd/Ctrl + K for search
    if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
      e.preventDefault();
      const searchInput = document.getElementById('search-input');
      if (searchInput) {
        searchInput.focus();
        searchInput.select();
      } else {
        // Fallback to search page if no search input found
        window.location.href = '/search';
      }
    }

    // ESC key to clear search results on main search page
    if (e.key === 'Escape' && window.location.pathname === '/search') {
      const searchInput = document.getElementById('search-input');
      const searchResults = document.getElementById('search-results');
      if (searchInput && searchResults) {
        e.preventDefault();
        searchInput.value = '';
        searchResults.innerHTML = '<div class="text-center text-tint-9 py-8"><div class="text-lg font-medium mb-2">Search Documentation</div></div>';
        searchInput.focus();
      }
    }
  });

  // Handle Ctrl+click to open links in new tab
  document.addEventListener('click', function (e) {
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

  // Close search dropdown when clicking outside
  document.addEventListener('click', function (e) {
    const searchInput = document.getElementById('search-input');
    const dropdown = document.getElementById('search-dropdown');

    if (searchInput && dropdown && !searchInput.contains(e.target) && !dropdown.contains(e.target)) {
      dropdown.innerHTML = '';
    }
  });
});
