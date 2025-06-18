// Navigation synchronization for HTMX
document.addEventListener('htmx:afterSwap', function(event) {
  if (event.target.id === 'content' || event.target.tagName === 'BODY') {
    console.log('Content updated, syncing navigation state');
    
    // Get current URL from the loaded content or window location
    const currentUrl = window.location.pathname;
    
    // Update navigation highlighting
    updateNavigationState(currentUrl);
  }
});

// Function to update navigation state
function updateNavigationState(currentUrl) {
  // Remove active class from all navigation links
  const allLinks = document.querySelectorAll('.clickable-summary a');
  allLinks.forEach(link => {
    link.classList.remove('active');
  });
  
  // Add active class to current page link
  const currentLink = document.querySelector(`.clickable-summary a[href="${currentUrl}"]`);
  if (currentLink) {
    currentLink.classList.add('active');
  }
  
  // Open parent details if link is inside a collapsed section
  const currentDetails = currentLink?.closest('details');
  if (currentDetails && !currentDetails.open) {
    currentDetails.open = true;
  }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
  const currentUrl = window.location.pathname;
  updateNavigationState(currentUrl);
}); 