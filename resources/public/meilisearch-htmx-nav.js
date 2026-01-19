(function () {
  'use strict';

  // This file provides keyboard navigation for HTMX-rendered Meilisearch results

  let currentSelectedIndex = -1;
  let isHtmxMode = false;
  let searchEventDebounceTimer = null;
  let pendingSearchEvent = null; // Store pending search event data
  let lastSearchResultsTimestamp = null; // Track when results were shown

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

  function trackSearchClick(resultElement, inputElement) {
    if (typeof posthog === 'undefined' || !resultElement) return;
    try {
      const query = inputElement ? inputElement.value : '';
      const position = parseInt(resultElement.getAttribute('data-result-index'), 10);
      const targetUrl = resultElement.href || '';

      // If there's a pending search event (debounce hasn't fired yet), send it immediately
      if (searchEventDebounceTimer !== null && pendingSearchEvent !== null) {
        clearTimeout(searchEventDebounceTimer);
        searchEventDebounceTimer = null;

        // Send the pending search event first
        posthog.capture('docs_search', pendingSearchEvent);
        pendingSearchEvent = null;
      }

      // Calculate time to click
      const timeToClickMs = lastSearchResultsTimestamp
        ? Date.now() - lastSearchResultsTimestamp
        : null;

      // Then send the click event
      posthog.capture('docs_search_click', {
        'query': query,
        'position': position,
        'target_url': targetUrl,
        'url': targetUrl,
        'time_to_click_ms': timeToClickMs
      });
    } catch (e) {
      console.error('Failed to track search click:', e);
    }
  }

  function setupClickTracking(dropdown, input) {
    dropdown.addEventListener('click', function (e) {
      const result = e.target.closest('[data-result-index]');
      if (result) {
        trackSearchClick(result, input);
      }
    });
  }

  function setupHtmxHandlers() {
    // Setup for desktop
    const desktopInput = document.getElementById('meilisearch-input');
    const desktopDropdown = document.getElementById('meilisearch-dropdown');
    if (desktopInput && desktopDropdown) {
      setupInputHandlers(desktopInput, desktopDropdown);
      setupClickTracking(desktopDropdown, desktopInput);
    }

    // Setup for mobile
    const mobileInput = document.getElementById('mobile-meilisearch-input');
    const mobileDropdown = document.getElementById('mobile-meilisearch-dropdown');
    if (mobileInput && mobileDropdown) {
      setupInputHandlers(mobileInput, mobileDropdown);
      setupClickTracking(mobileDropdown, mobileInput);
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
        lastSearchResultsTimestamp = Date.now(); // Track when results were shown

        // Track search in PostHog (frontend)
        if (typeof posthog !== 'undefined') {
          try {
            const inputId = evt.detail.target.id === 'mobile-meilisearch-dropdown'
              ? 'mobile-meilisearch-input'
              : 'meilisearch-input';
            const input = document.getElementById(inputId);
            const query = input ? input.value : '';

            // Only track if query is non-empty and contains only ASCII characters (no Cyrillic, Chinese, etc.)
            const isValidQuery = query && query.length > 0 && /^[\x20-\x7E]+$/.test(query);

            if (isValidQuery) {
              // Clear previous debounce timer
              if (searchEventDebounceTimer) {
                clearTimeout(searchEventDebounceTimer);
              }

              // Capture dropdown and results for the setTimeout closure
              const dropdown = evt.detail.target;
              const results = dropdown.querySelectorAll('[data-result-index]');
              const resultsCount = results.length;

              // Get product from current page context
              const productMatch = window.location.pathname.match(/^\/([^\/]+)/);
              const product = productMatch ? productMatch[1] : 'unknown';

              // Store pending search event data
              pendingSearchEvent = {
                'query': query,
                'results_count': resultsCount,
                'product': product
              };

              // Set new debounce timer (1.5 seconds)
              searchEventDebounceTimer = setTimeout(() => {
                posthog.capture('docs_search', pendingSearchEvent);
                pendingSearchEvent = null;
                searchEventDebounceTimer = null;
              }, 1500);
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
              trackSearchClick(selected, input);
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
  // Global function for no-results feedback form
  window.submitNoResultsFeedback = function(query, event) {
    if (event) {
      event.stopPropagation();
      event.preventDefault();
    }
    var input = document.getElementById('no-results-feedback-input');
    var feedback = input ? input.value.trim() : '';
    if (!feedback) return;

    // Send to PostHog if available (production)
    if (typeof posthog !== 'undefined') {
      posthog.capture('survey sent', {
        '$survey_id': '019bb7b8-e453-0000-b3b7-8149745b99cc',
        '$survey_response': feedback,
        'search_query': query
      });
    }

    // Always show thank you message
    var form = document.getElementById('no-results-feedback-form');
    if (form) {
      form.innerHTML = '<div class="text-on-surface-strong font-medium">Thanks for letting us know!</div><div class="text-on-surface-placeholder text-xs mt-1">We will do our best to add this soon.</div>';
    }
  };
})();
