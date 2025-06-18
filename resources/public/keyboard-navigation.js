// Keyboard navigation for Previous/Next pages
document.addEventListener('keydown', function(event) {
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

// Add visual feedback for keyboard navigation
document.addEventListener('keydown', function(event) {
  if (event.key === 'ArrowLeft' || event.key === 'ArrowRight') {
    const buttons = Array.from(document.querySelectorAll('a[href*="/"]'));
    const button = event.key === 'ArrowLeft' 
      ? buttons.find(btn => btn.textContent.trim().includes('Previous'))
      : buttons.find(btn => btn.textContent.trim().includes('Next'));
    
    if (button) {
      button.classList.add('ring-2', 'ring-primary-9', 'ring-opacity-50');
      setTimeout(() => {
        button.classList.remove('ring-2', 'ring-primary-9', 'ring-opacity-50');
      }, 200);
    }
  }
});

// Handle HTMX navigation events
document.addEventListener('htmx:beforeRequest', function(event) {
  // Store current scroll position before navigation
  sessionStorage.setItem('scrollPosition', window.scrollY);
});

document.addEventListener('htmx:afterSwap', function(event) {
  // Scroll to top after content swap
  window.scrollTo(0, 0);
  
  // Re-initialize syntax highlighting
  if (window.hljs) {
    hljs.highlightAll();
  }
  
  // Update navigation state
  updateNavigationState();
});

// Update navigation state after page load
document.addEventListener('DOMContentLoaded', function() {
  updateNavigationState();
});

// Function to update navigation state
function updateNavigationState() {
  const buttons = document.querySelectorAll('a[href*="/"]');
  buttons.forEach(button => {
    if (button.textContent.trim().includes('Previous') || button.textContent.trim().includes('Next')) {
      // Remove any existing event listeners
      button.removeEventListener('click', handleNavigationClick);
      // Add new event listener
      button.addEventListener('click', handleNavigationClick);
    }
  });
}

// Handle navigation button clicks
function handleNavigationClick(event) {
  // For navigation buttons, use direct navigation to ensure proper state
  if (this.textContent.trim().includes('Previous') || this.textContent.trim().includes('Next')) {
    event.preventDefault();
    window.location.href = this.href;
  }
}

// Ensure proper initialization on page load
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', updateNavigationState);
} else {
  updateNavigationState();
} 