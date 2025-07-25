// Navigation click handler - removed custom htmx.ajax() to avoid conflicts
// Now relies on standard HTMX boost and attributes

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
    a.classList.remove('active');
  });

  // Try exact match first
  let matchingLink = document.querySelector(`#navigation a[href="${pathname}"]`);

  // If no exact match, try normalized paths (remove trailing slash and query params)
  if (!matchingLink) {
    const normalizedPathname = pathname.replace(/\/$/, '').split('?')[0];

    // Check all links for normalized match
    const navLinks = document.querySelectorAll('#navigation a');
    for (const link of navLinks) {
      const linkHref = link.getAttribute('href');
      const normalizedHref = linkHref.replace(/\/$/, '').split('?')[0];

      if (normalizedPathname === normalizedHref) {
        matchingLink = link;
        break;
      }
    }
  }

  if (matchingLink) {
    matchingLink.classList.add('active');

    // Open parent details if needed
    const details = matchingLink.closest('details');
    if (details && !details.open) {
      details.open = true;
    }
  }
}

// Make function globally available
window.updateActiveNavItem = updateActiveNavItem;

// Function to extract and update page title from content
function updatePageTitle() {
  try {
    // Try to extract title from the main content
    const h1 = document.querySelector('#content h1');
    if (h1) {
      const pageTitle = h1.textContent.trim();
      document.title = `${pageTitle} | Aidbox User Docs`;
      return;
    }
    
    // Fallback: try to extract from breadcrumbs or page content
    const breadcrumb = document.querySelector('.breadcrumb');
    if (breadcrumb) {
      const lastCrumb = breadcrumb.querySelector('a:last-child, span:last-child');
      if (lastCrumb) {
        const pageTitle = lastCrumb.textContent.trim();
        document.title = `${pageTitle} | Aidbox User Docs`;
        return;
      }
    }
    
    // Final fallback: use pathname
    const pathname = window.location.pathname;
    const segments = pathname.split('/').filter(s => s);
    if (segments.length > 0) {
      const pageTitle = segments[segments.length - 1].replace(/-/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
      document.title = `${pageTitle} | Aidbox User Docs`;
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

// Clear any existing HTMX cache to prevent quota issues
document.addEventListener('DOMContentLoaded', function() {
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
document.addEventListener('htmx:historyCacheError', function(event) {
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

document.addEventListener('htmx:responseError', function(event) {
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
