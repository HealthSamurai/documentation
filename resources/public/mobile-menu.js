// Mobile menu functionality
function toggleMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');
  
  if (navigation && overlay) {
    const isOpen = navigation.classList.contains('mobile-open');
    
    if (isOpen) {
      // Close menu
      navigation.classList.remove('mobile-open');
      overlay.classList.remove('active');
      document.body.style.overflow = '';
    } else {
      // Open menu
      navigation.classList.add('mobile-open');
      overlay.classList.add('active');
      document.body.style.overflow = 'hidden';
    }
  }
}

// Close menu when clicking on a navigation link
document.addEventListener('click', function(event) {
  if (event.target.closest('#navigation a')) {
    // Small delay to allow HTMX to process the click
    setTimeout(() => {
      toggleMobileMenu();
    }, 100);
  }
});

// Close menu on escape key
document.addEventListener('keydown', function(event) {
  if (event.key === 'Escape') {
    const navigation = document.getElementById('navigation');
    if (navigation && navigation.classList.contains('mobile-open')) {
      toggleMobileMenu();
    }
  }
});

// Close menu on window resize if switching to desktop
window.addEventListener('resize', function() {
  if (window.innerWidth >= 768) {
    const navigation = document.getElementById('navigation');
    const overlay = document.querySelector('.mobile-menu-overlay');
    
    if (navigation) {
      navigation.classList.remove('mobile-open');
    }
    if (overlay) {
      overlay.classList.remove('active');
    }
    document.body.style.overflow = '';
  }
}); 