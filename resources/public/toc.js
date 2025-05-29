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
