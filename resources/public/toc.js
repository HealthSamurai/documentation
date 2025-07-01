// Navigation click handler
document.addEventListener('click', function (e) {
  const link = e.target.closest('.clickable-summary a, #navigation a');

  if (!link) return;

  // Allow default behavior for modifier key clicks (Ctrl+click, Cmd+click, etc.)
  if (e.ctrlKey || e.metaKey || e.shiftKey || e.altKey || e.button !== 0) {
    console.log('Modifier key detected, allowing default behavior');
    return;
  }

  // Allow default behavior for external links
  const href = link.getAttribute('href');
  if (href && (href.startsWith('http://') || href.startsWith('https://'))) {
    console.log('External link detected, allowing default behavior');
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
  htmx.ajax('GET', href + '?partial=true', {
    target: '#content',
    swap: 'outerHTML',
    pushUrl: href
  });
});

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
