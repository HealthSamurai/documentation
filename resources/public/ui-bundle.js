function switchTab(button, tabIndex) {
  const tabContainer = button.closest('.bg-tint-1');
  const buttons = tabContainer.querySelectorAll('button[data-tab]');
  const contents = tabContainer.querySelectorAll('.tab-content');

  buttons.forEach(btn => {
    btn.classList.remove('border-primary-9', 'text-primary-9');
    btn.classList.add('border-transparent', 'text-tint-9');
  });

  contents.forEach(content => {
    content.classList.add('hidden');
    content.classList.remove('block');
  });

  button.classList.remove('border-transparent', 'text-tint-9');
  button.classList.add('border-primary-9', 'text-primary-9');

  const targetContent = tabContainer.querySelector(`.tab-content[data-tab="${tabIndex}"]`);
  if (targetContent) {
    targetContent.classList.remove('hidden');
    targetContent.classList.add('block');
  }
}



window.addEventListener("popstate", () => {
  // Add delay to ensure DOM is updated
  setTimeout(() => {
    if (document.body) {
      updateActiveNavItem(window.location.pathname);
    }
  }, 100);
});

function updateActiveNavItem(pathname) {
  if (!document.body) return;

  const allLinks = document.querySelectorAll('#navigation a');

  allLinks.forEach(a => {
    // Remove active class from all links, including cross-section ones
    a.classList.remove('active');
  });

  // Normalize the pathname for comparison
  const normalizedPathname = pathname.replace(/\/$/, '').split('?')[0];

  // Find all links that match the pathname, excluding cross-section links
  const matchingLinks = [];
  allLinks.forEach(link => {
    // Skip cross-section links (they have data-cross-section="true" attribute)
    if (link.hasAttribute('data-cross-section')) {
      return;
    }

    const linkHref = link.getAttribute('href');
    const normalizedHref = linkHref.replace(/\/$/, '').split('?')[0];

    if (normalizedPathname === normalizedHref) {
      matchingLinks.push(link);
    }
  });

  // Use the first matching non-cross-section link
  const matchingLink = matchingLinks[0];

  if (matchingLink) {
    matchingLink.classList.add('active');

    // Open parent details if needed
    let currentElement = matchingLink;
    while (currentElement) {
      const details = currentElement.closest('details');
      if (details && !details.open) {
        details.open = true;
        currentElement = details.parentElement;
      } else {
        break;
      }
    }
  }
}

// Make function globally available
window.updateActiveNavItem = updateActiveNavItem;

// Function to extract and update page title from content
function updatePageTitle() {
  try {
    // Get product name from og:site_name meta tag
    const productName = document.querySelector('meta[property="og:site_name"]')?.content || 'Aidbox User Docs';

    // Try to extract title from the main content
    const h1 = document.querySelector('#content h1');
    if (h1) {
      const pageTitle = h1.textContent.trim();
      document.title = `${pageTitle} | ${productName}`;
      return;
    }

    // Fallback: try to extract from breadcrumbs or page content
    const breadcrumb = document.querySelector('.breadcrumb');
    if (breadcrumb) {
      const lastCrumb = breadcrumb.querySelector('a:last-child, span:last-child');
      if (lastCrumb) {
        const pageTitle = lastCrumb.textContent.trim();
        document.title = `${pageTitle} | ${productName}`;
        return;
      }
    }

    // Final fallback: use pathname
    const pathname = window.location.pathname;
    const segments = pathname.split('/').filter(s => s);
    if (segments.length > 0) {
      const pageTitle = segments[segments.length - 1].replace(/-/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
      document.title = `${pageTitle} | ${productName}`;
    }
  } catch (e) {
    console.warn('Could not update page title:', e);
  }
}

// Make function globally available
window.updatePageTitle = updatePageTitle;


document.addEventListener('keydown', function (e) {
  if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
    e.preventDefault();
    const searchInput = document.querySelector('#search-input');
    if (searchInput) {
      searchInput.focus();
    } else if (document.body && document.querySelector('#content')) {
      htmx.ajax('GET', '/search', { target: '#content', swap: 'innerHTML' })
        .then(() => {
          const searchInput = document.querySelector('#search-input');
          if (searchInput) {
            searchInput.focus();
          }
        });
    } else {
      // Fallback to regular navigation
      window.location.href = '/search';
    }
  }
});


// Initialize navigation on page load
document.addEventListener('DOMContentLoaded', function () {
  if (!document.body) return;
  updateActiveNavItem(window.location.pathname);
  updatePageTitle(); // Also update title on initial load
});

// Also update after HTMX swaps content
document.addEventListener('htmx:afterSwap', function (event) {
  // Small delay to ensure DOM is fully updated
  setTimeout(() => {
    updateActiveNavItem(window.location.pathname);
    updatePageTitle();
  }, 10);
});

// Clear any existing HTMX cache to prevent quota issues
document.addEventListener('DOMContentLoaded', function () {
  try {
    // Clear localStorage entries that might be used by HTMX
    for (let i = localStorage.length - 1; i >= 0; i--) {
      const key = localStorage.key(i);
      if (key && key.startsWith('htmx-')) {
        localStorage.removeItem(key);
      }
    }
  } catch (e) {
    console.warn('Could not clear HTMX cache:', e);
  }
});

// Handle HTMX errors
document.addEventListener('htmx:historyCacheError', function (event) {
  console.warn('HTMX history cache error:', event.detail);

  // Clear storage to prevent further quota issues
  try {
    localStorage.clear();
    sessionStorage.clear();
  } catch (e) {
    console.warn('Could not clear storage:', e);
  }

  // Prevent the error from bubbling up
  event.preventDefault();
  event.stopPropagation();

  // Force reload the current page to avoid broken state
  window.location.reload();
});

document.addEventListener('htmx:responseError', function (event) {
  console.warn('HTMX response error:', event.detail);
});

// Handle modifier key clicks for navigation links
document.addEventListener('click', function (e) {
  if (!document.body) return;

  const link = e.target.closest('a');
  if (link && (e.ctrlKey || e.metaKey || e.shiftKey || e.altKey || e.button !== 0)) {

    // For Ctrl+click or Cmd+click, open in new tab
    if (e.ctrlKey || e.metaKey) {
      e.preventDefault();
      e.stopPropagation();
      window.open(link.href, '_blank');
      return;
    }

    // For other modifier keys, allow default behavior
    return;
  }
});
// Scroll to ID script
// Waits for page load and checks for Scroll-To-Id header from backend

function scrollToIdFromHeader() {
  // Check if we have a Scroll-To-Id header in the current response
  // For HTMX requests, we can get it from the event detail
  // For regular page loads, we need to check if it was passed via meta tag or data attribute

  // Method 1: Check for HTMX response header
  if (window.htmx && window.htmx.lastXHR) {
    const scrollToId = window.htmx.lastXHR.getResponseHeader('Scroll-To-Id');
    if (scrollToId) {
      const element = document.getElementById(scrollToId);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 100);
        return;
      }
    }
  }

  // Method 2: Check for meta tag (for regular page loads)
  const metaTag = document.querySelector('meta[name="scroll-to-id"]');
  if (metaTag) {
    const scrollToId = metaTag.getAttribute('content');
    if (scrollToId) {
      const element = document.getElementById(scrollToId);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 100);
        return;
      }
    }
  }
}

// Run on DOM content loaded
document.addEventListener('DOMContentLoaded', function () {
  scrollToIdFromHeader();
});

// Run after HTMX content swaps
document.addEventListener('htmx:afterSwap', function (evt) {
  // Store the XHR object for later access
  if (evt.detail && evt.detail.xhr) {
    window.htmx = window.htmx || {};
    window.htmx.lastXHR = evt.detail.xhr;
  }

  // Check for Scroll-To-Id header in HTMX response
  if (evt.detail && evt.detail.xhr) {
    const scrollToId = evt.detail.xhr.getResponseHeader('Scroll-To-Id');
    if (scrollToId) {
      const element = document.getElementById(scrollToId);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 100);
      }
    }
  }
});

// Run after HTMX settles (for cases where content is dynamically added)
document.addEventListener('htmx:afterSettle', function () {
  scrollToIdFromHeader();
});

// Also run on window load for cases where content loads after DOMContentLoaded
window.addEventListener('load', function () {
  scrollToIdFromHeader();
});
// Heading link copy functionality for h2 and h3 elements
document.addEventListener('DOMContentLoaded', function () {
  setTimeout(initializeHeadingLinks, 100);
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function () {
  setTimeout(initializeHeadingLinks, 100);
});

// Handle window resize to hide buttons on mobile
window.addEventListener('resize', function () {
  const linkButtons = document.querySelectorAll('button[data-heading-link]');
  linkButtons.forEach(button => {
    if (window.innerWidth <= 768) {
      button.style.display = 'none';
      button.style.opacity = '0';
    }
  });
});

function initializeHeadingLinks() {
  // Find all h2 and h3 elements
  const headings = document.querySelectorAll('h2[id], h3[id]');

  headings.forEach((heading) => {
    // Skip if already has link button
    if (heading.querySelector('button[data-heading-link]')) {
      return;
    }

    // Skip if no ID (no anchor point)
    if (!heading.id) {
      return;
    }

    // Create link button
    const linkButton = document.createElement('button');
    linkButton.setAttribute('data-heading-link', 'true');
    linkButton.style.cssText = 'position: absolute; left: -24px; top: 50%; transform: translateY(-50%); padding: 4px; font-size: 16px; background: transparent; color: #9b9b9b; border: none; cursor: pointer; opacity: 0; transition: opacity 0.2s, color 0.2s; z-index: 10; display: none;';
    linkButton.innerHTML = '<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z" fill="currentColor"/></svg>';
    linkButton.title = 'Link to section';

    // Add click handler
    linkButton.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();

      // Update the URL in the browser
      window.history.replaceState(null, '', '#' + heading.id);

      // Scroll to the heading (CSS scroll-margin-top handles the offset)
      heading.scrollIntoView({ behavior: 'smooth', block: 'start' });
    });

    // Make heading container relative for absolute positioning
    heading.style.position = 'relative';

    // Add hover effect to show button (only on desktop)
    heading.addEventListener('mouseenter', function () {
      if (window.innerWidth > 768) {
        linkButton.style.display = 'block';
        linkButton.style.opacity = '1';
      }
    });

    heading.addEventListener('mouseleave', function () {
      if (window.innerWidth > 768) {
        linkButton.style.opacity = '0';
        setTimeout(() => {
          if (linkButton.style.opacity === '0') {
            linkButton.style.display = 'none';
          }
        }, 200);
      }
    });

    // Also show on button hover
    linkButton.addEventListener('mouseenter', function () {
      if (window.innerWidth > 768) {
        linkButton.style.opacity = '1';
        linkButton.style.color = '#4b5563';
      }
    });

    // Reset when mouse leaves button
    linkButton.addEventListener('mouseleave', function () {
      if (window.innerWidth > 768) {
        this.style.color = '#9b9b9b';
      }
    });

    // Insert button
    heading.appendChild(linkButton);
  });
}// Mobile menu functionality
function toggleMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');

  if (navigation && overlay) {
    const isOpen = navigation.classList.contains('mobile-open');

    if (isOpen) {
      closeMobileMenu();
    } else {
      openMobileMenu();
    }
  }
}

function openMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');

  if (navigation && overlay) {
    navigation.classList.add('mobile-open');
    overlay.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

function closeMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');

  if (navigation && overlay) {
    navigation.classList.remove('mobile-open');
    overlay.classList.remove('active');
    document.body.style.overflow = '';
  }
}

// Close menu when clicking on overlay
document.addEventListener('click', function (event) {
  const overlay = document.querySelector('.mobile-menu-overlay');
  if (event.target === overlay) {
    closeMobileMenu();
  }
});

// Close menu when clicking on a navigation link
document.addEventListener('click', function (event) {
  if (event.target.closest('#navigation a')) {
    // Small delay to allow HTMX to process the click
    setTimeout(() => {
      closeMobileMenu();
    }, 100);
  }
});

// Close menu on escape key
document.addEventListener('keydown', function (event) {
  if (event.key === 'Escape') {
    const navigation = document.getElementById('navigation');
    if (navigation && navigation.classList.contains('mobile-open')) {
      closeMobileMenu();
    }
  }
});

// Close menu on window resize if switching to desktop
window.addEventListener('resize', function () {
  if (window.innerWidth >= 1024) {
    closeMobileMenu();
  }
});

// Initialize mobile menu when DOM is loaded
document.addEventListener('DOMContentLoaded', function () {
  initializeMobileMenu();
});

// Re-initialize mobile menu after HTMX content swaps
document.addEventListener('htmx:afterSwap', function () {
  initializeMobileMenu();
});

function initializeMobileMenu() {
  // Ensure mobile menu button exists and has proper event listener
  const mobileMenuButton = document.querySelector('#mobile-menu-toggle');
  if (mobileMenuButton) {
    // Remove any existing onclick attribute and add proper event listener
    mobileMenuButton.removeAttribute('onclick');

    // Remove existing event listeners to prevent duplicates
    const newButton = mobileMenuButton.cloneNode(true);
    mobileMenuButton.parentNode.replaceChild(newButton, mobileMenuButton);

    newButton.addEventListener('click', function (e) {
      e.preventDefault();
      toggleMobileMenu();
    });
  }

  // Initialize ellipsis menu
  initializeEllipsisMenu();
}

// Ellipsis menu functionality
function initializeEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');

  if (ellipsisButton && ellipsisDropdown) {
    // Remove existing event listeners to prevent duplicates
    const newButton = ellipsisButton.cloneNode(true);
    ellipsisButton.parentNode.replaceChild(newButton, ellipsisButton);

    newButton.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();
      toggleEllipsisMenu();
    });
  }
}

function toggleEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');

  if (ellipsisButton && ellipsisDropdown) {
    const isOpen = !ellipsisDropdown.classList.contains('hidden');

    if (isOpen) {
      closeEllipsisMenu();
    } else {
      openEllipsisMenu();
    }
  }
}

function openEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');

  if (ellipsisButton && ellipsisDropdown) {
    ellipsisDropdown.classList.remove('hidden');
    ellipsisButton.setAttribute('aria-expanded', 'true');
  }
}

function closeEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');

  if (ellipsisButton && ellipsisDropdown) {
    ellipsisDropdown.classList.add('hidden');
    ellipsisButton.setAttribute('aria-expanded', 'false');
  }
}

// Close ellipsis menu when clicking outside
document.addEventListener('click', function (event) {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');

  if (ellipsisButton && ellipsisDropdown) {
    if (!ellipsisButton.contains(event.target) && !ellipsisDropdown.contains(event.target)) {
      closeEllipsisMenu();
    }
  }
});

// Close ellipsis menu on escape key
document.addEventListener('keydown', function (event) {
  if (event.key === 'Escape') {
    closeEllipsisMenu();
  }
});
// Mobile search toggle functionality
document.addEventListener('DOMContentLoaded', function () {
  const mobileSearchToggle = document.getElementById('mobile-search-toggle');
  const mobileSearchContainer = document.getElementById('mobile-search-container');
  const mobileSearchInput = document.getElementById('mobile-search-input');
  const mobileSearchClose = document.getElementById('mobile-search-close');
  const desktopSearchInput = document.getElementById('search-input');

  if (!mobileSearchToggle || !mobileSearchContainer) return;

  // Toggle mobile search
  mobileSearchToggle.addEventListener('click', function (e) {
    e.preventDefault();
    e.stopPropagation();

    if (mobileSearchContainer.classList.contains('hidden')) {
      mobileSearchContainer.classList.remove('hidden');
      // Focus and select input after a small delay to ensure it's visible
      setTimeout(() => {
        mobileSearchInput.focus();
        mobileSearchInput.select();
      }, 100);
    } else {
      mobileSearchContainer.classList.add('hidden');
      mobileSearchInput.value = '';
      // Clear mobile dropdown
      const mobileDropdown = document.getElementById('mobile-search-dropdown');
      if (mobileDropdown) {
        mobileDropdown.innerHTML = '';
      }
    }
  });

  // Close mobile search
  if (mobileSearchClose) {
    mobileSearchClose.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();
      mobileSearchContainer.classList.add('hidden');
      mobileSearchInput.value = '';
      // Clear mobile dropdown
      const mobileDropdown = document.getElementById('mobile-search-dropdown');
      if (mobileDropdown) {
        mobileDropdown.innerHTML = '';
      }
    });
  }

  // Close mobile search on outside click
  document.addEventListener('click', function (e) {
    if (!mobileSearchContainer.classList.contains('hidden') &&
      !mobileSearchContainer.contains(e.target) &&
      !mobileSearchToggle.contains(e.target)) {
      mobileSearchContainer.classList.add('hidden');
      mobileSearchInput.value = '';
      // Clear mobile dropdown
      const mobileDropdown = document.getElementById('mobile-search-dropdown');
      if (mobileDropdown) {
        mobileDropdown.innerHTML = '';
      }
    }
  });

  // Handle mobile search keyboard navigation
  if (mobileSearchInput) {
    mobileSearchInput.addEventListener('keydown', function (event) {
      const dropdown = document.getElementById('mobile-search-dropdown');

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
            currentIndex = currentIndex < results.length - 1 ? currentIndex + 1 : 0;
            updateMobileSelectedResult(results, currentIndex);
            return;
          } else if (event.key === 'ArrowUp') {
            event.preventDefault();
            currentIndex = currentIndex <= 0 ? results.length - 1 : currentIndex - 1;
            updateMobileSelectedResult(results, currentIndex);
            return;
          } else if (event.key === 'Enter') {
            event.preventDefault();
            if (currentIndex >= 0 && currentIndex < results.length) {
              results[currentIndex].click();
              // Close mobile search after navigation
              mobileSearchContainer.classList.add('hidden');
              mobileSearchInput.value = '';
            }
            return;
          }
        }

        // Handle Escape
        if (event.key === 'Escape') {
          event.preventDefault();
          if (dropdown.innerHTML !== '') {
            dropdown.innerHTML = '';
          } else {
            // Close mobile search if dropdown is already empty
            mobileSearchContainer.classList.add('hidden');
            mobileSearchInput.value = '';
          }
          return;
        }
      } else if (event.key === 'Escape') {
        // Close mobile search if no dropdown
        event.preventDefault();
        mobileSearchContainer.classList.add('hidden');
        mobileSearchInput.value = '';
      }
    });
  }

  // Update selected result function for mobile
  function updateMobileSelectedResult(results, selectedIndex) {
    results.forEach((result, index) => {
      const isSectionEntry = result.querySelector('.border-l-2') !== null;

      if (index === selectedIndex) {
        result.classList.add('bg-warning-2', 'text-tint-strong');
        result.classList.remove('hover:bg-tint-2', 'hover:text-tint-strong');

        if (isSectionEntry) {
          result.classList.remove('text-tint-10');
          const childElements = result.querySelectorAll('p.text-tint-10');
          childElements.forEach(child => {
            child.classList.remove('text-tint-10');
            child.classList.add('text-tint-strong');
          });
        } else {
          result.classList.remove('text-tint');
        }

        const fileIcon = result.querySelector('div:first-of-type');
        if (fileIcon) {
          fileIcon.classList.add('text-warning-9');
          fileIcon.classList.remove('text-tint-9', 'group-hover:text-tint-strong');
        }

        const enterIcon = result.querySelector('.search-action-button');
        if (enterIcon) {
          enterIcon.classList.add('text-orange-600', 'opacity-100');
          enterIcon.classList.remove('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
          enterIcon.innerHTML = '<div class="px-2 py-1 rounded border text-xs font-medium flex items-center justify-center bg-orange-600 text-white border-orange-700">↵</div>';
        }

        result.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
      } else {
        result.classList.remove('bg-warning-2', 'text-tint-strong');

        if (isSectionEntry) {
          result.classList.add('text-tint-10');
          const childElements = result.querySelectorAll('p.text-tint-strong');
          childElements.forEach(child => {
            child.classList.remove('text-tint-strong');
            child.classList.add('text-tint-10');
          });
        } else {
          result.classList.add('text-tint');
        }

        const fileIcon = result.querySelector('div:first-of-type');
        if (fileIcon) {
          fileIcon.classList.remove('text-warning-9');
          fileIcon.classList.add('text-tint-9', 'group-hover:text-tint-strong');
        }

        const enterIcon = result.querySelector('.search-action-button');
        if (enterIcon) {
          enterIcon.classList.remove('text-orange-600', 'opacity-100');
          enterIcon.classList.add('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
          enterIcon.innerHTML = '<svg class="size-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"></path></svg>';
        }
      }
    });
  }
}); function formatRelativeTime(date) {
  const now = new Date();
  const then = new Date(date);
  const diffMs = now - then;
  const seconds = Math.floor(diffMs / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const weeks = Math.floor(days / 7);
  const months = Math.floor(days / 30);
  const years = Math.floor(days / 365);

  if (years > 0) {
    return years === 1 ? "a year ago" : years + " years ago";
  } else if (months > 0) {
    return months === 1 ? "a month ago" : months + " months ago";
  } else if (weeks > 0) {
    return weeks === 1 ? "a week ago" : weeks + " weeks ago";
  } else if (days > 0) {
    return days === 1 ? "yesterday" : days + " days ago";
  } else if (hours > 0) {
    return hours === 1 ? "an hour ago" : hours + " hours ago";
  } else if (minutes > 0) {
    return minutes === 1 ? "a minute ago" : minutes + " minutes ago";
  } else {
    return "just now";
  }
}

function updateLastUpdated() {
  const el = document.getElementById("lastupdated");
  if (el) {
    const isoTime = el.getAttribute("data-updated-at");
    if (isoTime) {
      const relative = formatRelativeTime(isoTime);
      el.textContent = "Last updated " + relative;
    }
  }
}

// Update on page load
document.addEventListener('DOMContentLoaded', updateLastUpdated);

// Update every minute to keep relative time fresh
setInterval(updateLastUpdated, 60000);// Copy code functionality
document.addEventListener('DOMContentLoaded', function () {
  // Wait a bit for highlight.js to finish
  setTimeout(initializeCopyButtons, 100);
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function () {
  // Wait a bit for highlight.js to finish
  setTimeout(initializeCopyButtons, 100);
});

// Also initialize after highlight.js runs
if (typeof hljs !== 'undefined') {
  const originalHighlightAll = hljs.highlightAll;
  hljs.highlightAll = function () {
    originalHighlightAll.call(this);
    setTimeout(initializeCopyButtons, 50);
  };
}

// Handle window resize to hide buttons on mobile
window.addEventListener('resize', function () {
  const copyButtons = document.querySelectorAll('button[data-copy-code]');
  copyButtons.forEach(button => {
    if (window.innerWidth <= 768) {
      button.style.display = 'none';
      button.style.opacity = '0';
    }
  });
});

function initializeCopyButtons() {
  // Find all pre elements that contain code blocks
  const preElements = document.querySelectorAll('pre');

  preElements.forEach((pre, index) => {
    // Skip if already has copy button
    if (pre.querySelector('button[data-copy-code]')) {
      return;
    }

    // Skip mermaid diagrams
    if (pre.classList.contains('mermaid')) {
      return;
    }

    const codeBlock = pre.querySelector('code');
    if (!codeBlock) {
      return;
    }

    // Create copy button
    const copyButton = document.createElement('button');
    copyButton.setAttribute('data-copy-code', 'true');
    copyButton.style.cssText = 'position: absolute; top: 8px; right: 8px; padding: 4px 8px; font-size: 12px; background: rgba(255, 255, 255, 0.1); backdrop-filter: blur(8px); color: #374151; border: 1px solid #d1d5db; border-radius: 4px; cursor: pointer; opacity: 0; transition: opacity 0.2s; z-index: 10; box-shadow: 0 2px 4px rgba(0,0,0,0.1); font-weight: 500; display: none;';
    copyButton.innerHTML = 'Copy';
    copyButton.title = 'Copy code';

    // Add click handler
    copyButton.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();

      const textToCopy = codeBlock.textContent;

      // Use modern clipboard API if available
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(textToCopy).then(function () {
          showCopySuccess(copyButton);
        }).catch(function (err) {
          console.error('Failed to copy: ', err);
        });
      }
    });

    // Make pre container relative for absolute positioning
    pre.style.position = 'relative';

    // Add hover effect to show button (only on desktop)
    pre.addEventListener('mouseenter', function () {
      if (window.innerWidth > 768) {
        copyButton.style.display = 'block';
        copyButton.style.opacity = '1';
      }
    });

    pre.addEventListener('mouseleave', function () {
      if (window.innerWidth > 768) {
        copyButton.style.opacity = '0';
        setTimeout(() => {
          if (copyButton.style.opacity === '0') {
            copyButton.style.display = 'none';
          }
        }, 200);
      }
    });

    // Also show on button hover
    copyButton.addEventListener('mouseenter', function () {
      if (window.innerWidth > 768) {
        copyButton.style.opacity = '1';
        copyButton.style.background = 'rgba(255, 255, 255, 0.8)';
      }
    });

    // Reset text when mouse leaves button
    copyButton.addEventListener('mouseleave', function () {
      if (window.innerWidth > 768) {
        // Reset to original text and style if it was changed
        if (this.innerHTML === 'Copied!') {
          this.innerHTML = 'Copy';
        }
        this.style.background = 'rgba(255, 255, 255, 0.1)';
      }
    });

    // Insert button
    pre.appendChild(copyButton);
  });
}

function showCopySuccess(button) {
  const originalHTML = button.innerHTML;
  const originalStyle = button.style.cssText;

  button.innerHTML = 'Copied!';

  setTimeout(() => {
    button.innerHTML = originalHTML;
    button.style.cssText = originalStyle;
  }, 2000);
}
