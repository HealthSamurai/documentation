// careful here
document.addEventListener('click', function (e) {
  const link = e.target.closest('.clickable-summary a');

  if (!link) return;

  e.preventDefault();

  const details = link.closest('details');
  const href = link.getAttribute('href');

  if (details && !details.open) {
    details.open = true;
  }

  setTimeout(() => {
    htmx.ajax('GET', href, { target: '#content', swap: 'outerHTML' });
  }, 150);
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
