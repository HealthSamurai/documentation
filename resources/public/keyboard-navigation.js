// Keyboard navigation for Previous/Next pages
document.addEventListener('keydown', function (event) {
  // Only handle arrow keys if not typing in an input field
  if (event.target.tagName === 'INPUT' || event.target.tagName === 'TEXTAREA' || event.target.contentEditable === 'true') {
    return;
  }

  // Left arrow key (Previous page)
  if (event.key === 'ArrowLeft') {
    const buttons = Array.from(document.querySelectorAll('a[href*="/"]'));
    const prevButton = buttons.find(btn => btn.textContent.trim().includes('Previous'));
    if (prevButton) {
      event.preventDefault();
      // Use direct navigation instead of HTMX for keyboard
      window.location.href = prevButton.href;
    }
  }

  // Right arrow key (Next page)
  if (event.key === 'ArrowRight') {
    const buttons = Array.from(document.querySelectorAll('a[href*="/"]'));
    const nextButton = buttons.find(btn => btn.textContent.trim().includes('Next'));
    if (nextButton) {
      event.preventDefault();
      // Use direct navigation instead of HTMX for keyboard
      window.location.href = nextButton.href;
    }
  }
});
