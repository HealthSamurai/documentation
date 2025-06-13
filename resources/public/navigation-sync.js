// Navigation synchronization for HTMX
document.addEventListener('htmx:afterSwap', function(event) {
  if (event.target.id === 'content') {
    console.log('Content updated, syncing navigation state');
    
    // Get current URL from the loaded content or window location
    const currentUrl = window.location.pathname;
    
    // Update navigation highlighting
    updateNavigationState(currentUrl);
  }
});

function updateNavigationState(currentUrl) {
  console.log('Updating navigation state for URL:', currentUrl);
  
  // Remove all active classes from navigation
  const allNavLinks = document.querySelectorAll('#navigation a');
  allNavLinks.forEach(link => {
    link.classList.remove('active');
  });
  
  // Remove all opened states from details
  const allDetails = document.querySelectorAll('#navigation details');
  allDetails.forEach(detail => {
    detail.removeAttribute('open');
  });
  
  // Find and highlight current page
  const currentLink = document.querySelector(`#navigation a[href="${currentUrl}"]`);
  if (currentLink) {
    currentLink.classList.add('active');
    
    // Open parent details elements
    let parent = currentLink.closest('details');
    while (parent) {
      parent.setAttribute('open', '');
      parent = parent.parentElement.closest('details');
    }
  }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
  const currentUrl = window.location.pathname;
  updateNavigationState(currentUrl);
}); 