(function() {
  'use strict';

  // Fix for middle-click and Ctrl/Cmd+click on navigation links
  // This prevents HTMX from intercepting these clicks and allows browser's default behavior
  
  function initNavigationClickFix() {
    // Handle all clicks on the navigation sidebar
    const navigation = document.getElementById('navigation');
    if (!navigation) return;

    // Listen for mousedown events to catch middle-clicks before HTMX processes them
    navigation.addEventListener('mousedown', function(e) {
      // Check if it's a middle-click (button === 1) or auxiliary button (button === 2)
      if (e.button === 1 || e.button === 2) {
        const link = e.target.closest('a');
        if (link && link.href) {
          // Temporarily disable hx-boost for this link
          const hadBoost = link.hasAttribute('hx-boost');
          const boostValue = link.getAttribute('hx-boost');
          link.setAttribute('hx-boost', 'false');
          
          // Restore the original state after a short delay
          setTimeout(function() {
            if (hadBoost) {
              link.setAttribute('hx-boost', boostValue);
            } else {
              link.removeAttribute('hx-boost');
            }
          }, 100);
        }
      }
    }, true);

    // Also handle Ctrl/Cmd + click
    navigation.addEventListener('click', function(e) {
      if (e.ctrlKey || e.metaKey) {
        const link = e.target.closest('a');
        if (link && link.href) {
          // Prevent HTMX from processing this click
          e.stopPropagation();
          // Open in new tab
          window.open(link.href, '_blank');
          e.preventDefault();
        }
      }
    }, true);

    // Handle clicks on the clickable-summary divs that contain links
    const clickableSummaries = navigation.querySelectorAll('.clickable-summary');
    clickableSummaries.forEach(function(summary) {
      summary.addEventListener('mousedown', function(e) {
        // For middle-clicks on the summary div containing the link
        if (e.button === 1) {
          const link = summary.querySelector('a');
          if (link && link.href) {
            e.preventDefault();
            e.stopPropagation();
            // Open in new tab
            window.open(link.href, '_blank');
          }
        }
      });

      // Prevent HTMX from processing middle-clicks
      summary.addEventListener('auxclick', function(e) {
        if (e.button === 1) {
          const link = summary.querySelector('a');
          if (link && link.href) {
            e.preventDefault();
            e.stopPropagation();
          }
        }
      });
    });
  }

  // Initialize when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initNavigationClickFix);
  } else {
    initNavigationClickFix();
  }

  // Re-initialize when HTMX swaps content (for dynamic navigation updates)
  document.body.addEventListener('htmx:afterSwap', function(evt) {
    // Re-initialize if navigation was updated
    if (evt.detail.target.id === 'navigation' || evt.detail.target.contains(document.getElementById('navigation'))) {
      setTimeout(initNavigationClickFix, 100);
    }
  });
})();