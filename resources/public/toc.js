// Navigation click handler
document.addEventListener('click', function (e) {
  const link = e.target.closest('.clickable-summary a, #navigation a');

  if (!link) return;

  // Allow default behavior for modifier key clicks (Ctrl+click, Cmd+click, etc.)
  if (e.ctrlKey || e.metaKey || e.shiftKey || e.altKey || e.button !== 0) {
    return;
  }

  // Allow default behavior for external links
  const href = link.getAttribute('href');
  if (href && (href.startsWith('http://') || href.startsWith('https://'))) {
    return;
  }

  // Only handle internal navigation links
  if (!link.hasAttribute('data-hx-nav')) {
    return;
  }

  e.preventDefault();

  const details = link.closest('details');

  // Remove active class from all navigation links
  const allLinks = document.querySelectorAll('#navigation a');
  allLinks.forEach(a => {
    a.classList.remove('active');
  });

  // Add active class to clicked link
  link.classList.add('active');

  if (details && !details.open) {
    details.open = true;
  }

  // Use HTMX to load content
  htmx.ajax('GET', '/toc' + href, {
    target: '#toc-container',
    swap: 'outerHTML',
    pushUrl: href
  });
});

window.addEventListener("popstate", () => {
  updateActiveNavItem(window.location.pathname);
});

function updateActiveNavItem(pathname) {
  // Remove active class from all navigation links
  const allLinks = document.querySelectorAll('#navigation a');
  allLinks.forEach(a => {
    a.classList.remove('active');
  });

  // Find the matching link and add active class
  const matchingLink = document.querySelector(`#navigation a[href="${pathname}"]`);
  if (matchingLink) {
    matchingLink.classList.add('active');

    // Open parent details if needed
    const details = matchingLink.closest('details');
    if (details && !details.open) {
      details.open = true;
    }
  }
}

document.addEventListener('keydown', function (e) {
  if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
    e.preventDefault();
    const searchInput = document.querySelector('#search-input');
    if (searchInput) {
      searchInput.focus();
    } else {
      htmx.ajax('GET', '/search', { target: '#content', swap: 'innerHTML' })
        .then(() => {
          const searchInput = document.querySelector('#search-input');
          if (searchInput) {
            searchInput.focus();
          }
        });
    }
  }
});


// Disable HTMX boost for all navigation links
document.addEventListener('DOMContentLoaded', function() {
  const navLinks = document.querySelectorAll('#navigation a');
  navLinks.forEach(link => {
    link.setAttribute('data-hx-boost', 'false');
  });

  // Update active navigation item on page load
  updateActiveNavItem(window.location.pathname);
});

// Re-disable HTMX boost after HTMX content swaps
document.addEventListener('htmx:afterSwap', function() {
  const navLinks = document.querySelectorAll('#navigation a');
  navLinks.forEach(link => {
    link.setAttribute('data-hx-boost', 'false');
  });
});

// Handle modifier key clicks for navigation links
document.addEventListener('click', function(e) {
  const link = e.target.closest('a');
  if (link && (e.ctrlKey || e.metaKey || e.shiftKey || e.altKey || e.button !== 0)) {
    console.log('Modifier key click detected on link:', link.href);

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
