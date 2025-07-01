// careful here
document.addEventListener('click', function (e) {
  const link = e.target.closest('.clickable-summary a, #navigation a');

  if (!link) return;

  e.preventDefault();

  const details = link.closest('details');
  const href = link.getAttribute('href');


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

  setTimeout(() => {
    // htmx.ajax('GET', href, {
    //   target: '#content',
    //   swap: 'outerHTML'
    // });

    htmx.ajax('GET', '/toc' + href, {
      target: '.toc-container',
      swap: 'outerHTML'
    });
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


document.querySelectorAll('a[data-hx-nav]')
  .forEach(el => {
    el.addEventListener('click', function (e) {
      if (!e.ctrlKey && !e.metaKey && !e.shiftKey && !e.altKey && e.button === 0) {
        e.preventDefault();
      }
    });
  });
