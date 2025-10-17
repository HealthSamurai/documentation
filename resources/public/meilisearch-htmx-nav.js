(function () {
  'use strict';

  // This file provides keyboard navigation for HTMX-rendered Meilisearch results

  let currentSelectedIndex = -1;
  let isHtmxMode = false;

  // Initialize when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initialize);
  } else {
    initialize();
  }

  function initialize() {
    // Check if we're using HTMX mode (presence of hx-get attribute)
    const desktopInput = document.getElementById('meilisearch-input');

    if (desktopInput && desktopInput.hasAttribute('hx-get')) {
      isHtmxMode = true;
      setupHtmxHandlers();
    }

    // Handle Ctrl+K shortcut globally
    document.addEventListener('keydown', function (e) {
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        const input = document.getElementById('meilisearch-input');
        if (input) {
          input.focus();
        }
      }
    });
  }

  function setupHtmxHandlers() {
    // Setup for desktop
    const desktopInput = document.getElementById('meilisearch-input');
    const desktopDropdown = document.getElementById('meilisearch-dropdown');
    if (desktopInput && desktopDropdown) {
      setupInputHandlers(desktopInput, desktopDropdown);
    }

    // Setup for mobile
    const mobileInput = document.getElementById('mobile-meilisearch-input');
    const mobileDropdown = document.getElementById('mobile-meilisearch-dropdown');
    if (mobileInput && mobileDropdown) {
      setupInputHandlers(mobileInput, mobileDropdown);
    }

    // Handle close on click outside
    document.addEventListener('click', function (e) {
      const desktopWrapper = document.getElementById('meilisearch-wrapper');
      const desktopDropdown = document.getElementById('meilisearch-dropdown');
      const mobileContainer = document.getElementById('mobile-search-container');
      const mobileDropdown = document.getElementById('mobile-meilisearch-dropdown');

      // Check if click is outside desktop search area
      if (desktopWrapper && desktopDropdown) {
        // Check if the click is outside both the input wrapper and the dropdown
        if (!desktopWrapper.contains(e.target) && !desktopDropdown.contains(e.target)) {
          desktopDropdown.innerHTML = '';
          currentSelectedIndex = -1;
        }
      }

      // Check if click is outside mobile search area
      if (mobileContainer && mobileDropdown) {
        if (!mobileContainer.contains(e.target) && !mobileDropdown.contains(e.target)) {
          mobileDropdown.innerHTML = '';
          currentSelectedIndex = -1;
        }
      }
    });

    // Reset selection when HTMX loads new content
    document.body.addEventListener('htmx:afterSwap', function (evt) {
      if (evt.detail.target.id === 'meilisearch-dropdown' ||
        evt.detail.target.id === 'mobile-meilisearch-dropdown') {
        currentSelectedIndex = -1;

        // Track search in PostHog (frontend)
        if (typeof posthog !== 'undefined') {
          try {
            const inputId = evt.detail.target.id === 'mobile-meilisearch-dropdown'
              ? 'mobile-meilisearch-input'
              : 'meilisearch-input';
            const input = document.getElementById(inputId);
            const query = input ? input.value : '';

            if (query && query.length > 0) {
              const dropdown = evt.detail.target;
              const results = dropdown.querySelectorAll('[data-result-index]');
              const resultsCount = results.length;

              // Get product from current page context
              const productMatch = window.location.pathname.match(/^\/([^\/]+)/);
              const product = productMatch ? productMatch[1] : 'unknown';

              posthog.capture('docs_search', {
                'query': query,
                'results_count': resultsCount,
                'product': product
              });
            }
          } catch (e) {
            console.error('Failed to track search in PostHog:', e);
          }
        }
      }
    });

    // Hide shortcut and show spinner during HTMX request
    document.body.addEventListener('htmx:beforeRequest', function (evt) {
      if (evt.detail.elt.id === 'meilisearch-input') {
        const shortcut = document.getElementById('meilisearch-shortcut');
        if (shortcut) shortcut.classList.add('hidden');
      }
    });

    document.body.addEventListener('htmx:afterRequest', function (evt) {
      if (evt.detail.elt.id === 'meilisearch-input') {
        const shortcut = document.getElementById('meilisearch-shortcut');
        if (shortcut) shortcut.classList.remove('hidden');
      }
    });
  }

  function setupInputHandlers(input, dropdown) {
    // Handle arrow keys and Enter
    input.addEventListener('keydown', function (e) {
      const results = dropdown.querySelectorAll('[data-result-index]');
      if (results.length === 0) return;

      switch (e.key) {
        case 'ArrowDown':
          e.preventDefault();
          e.stopPropagation();
          navigateResults(1, results);
          break;
        case 'ArrowUp':
          e.preventDefault();
          e.stopPropagation();
          navigateResults(-1, results);
          break;
        case 'Enter':
          e.preventDefault();
          if (currentSelectedIndex >= 0 && currentSelectedIndex < results.length) {
            const selected = results[currentSelectedIndex];
            if (selected && selected.href) {
              window.location.href = selected.href;
            }
          }
          break;
      }
    });

    // Reset selection on input
    input.addEventListener('input', function () {
      currentSelectedIndex = -1;
    });

    // Handle mobile search close button
    const closeButton = document.getElementById('mobile-search-close');
    if (closeButton) {
      closeButton.addEventListener('click', function (e) {
        e.preventDefault();
        const container = document.getElementById('mobile-search-container');
        const mobileDropdown = document.getElementById('mobile-meilisearch-dropdown');
        if (container) {
          container.classList.add('hidden');
        }
        if (mobileDropdown) {
          mobileDropdown.innerHTML = '';
        }
        currentSelectedIndex = -1;
      });
    }

    // Handle mobile search toggle
    const mobileToggle = document.getElementById('mobile-search-toggle');
    if (mobileToggle) {
      mobileToggle.addEventListener('click', function (e) {
        e.preventDefault();
        const container = document.getElementById('mobile-search-container');
        const mobileInput = document.getElementById('mobile-meilisearch-input');
        if (container) {
          container.classList.toggle('hidden');
          if (!container.classList.contains('hidden') && mobileInput) {
            setTimeout(() => mobileInput.focus(), 100);
          }
        }
      });
    }
  }

  function navigateResults(direction, results) {
    if (results.length === 0) return;

    // Update selected index
    currentSelectedIndex += direction;

    // Wrap around
    if (currentSelectedIndex < 0) {
      currentSelectedIndex = results.length - 1;
    } else if (currentSelectedIndex >= results.length) {
      currentSelectedIndex = 0;
    }

    // Update visual selection
    updateSelectedResult(results);
  }

  function updateSelectedResult(results) {
    results.forEach((el, idx) => {
      if (idx === currentSelectedIndex) {
        el.classList.add('bg-warning-2');
        el.classList.remove('hover:bg-tint-hover');
        // Scroll into view if needed
        el.scrollIntoView({ block: 'nearest', behavior: 'smooth' });
      } else {
        el.classList.remove('bg-warning-2');
        el.classList.add('hover:bg-tint-hover');
      }
    });
  }
})();
