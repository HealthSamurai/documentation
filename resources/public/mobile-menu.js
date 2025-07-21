// Mobile menu functionality
function toggleMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');

  if (navigation && overlay) {
    const isOpen = navigation.classList.contains('mobile-open');

    if (isOpen) {
      closeMobileMenu();
    } else {
      openMobileMenu();
    }
  }
}

function openMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');

  if (navigation && overlay) {
    navigation.classList.add('mobile-open');
    overlay.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

function closeMobileMenu() {
  const navigation = document.getElementById('navigation');
  const overlay = document.querySelector('.mobile-menu-overlay');

  if (navigation && overlay) {
    navigation.classList.remove('mobile-open');
    overlay.classList.remove('active');
    document.body.style.overflow = '';
  }
}

// Close menu when clicking on overlay
document.addEventListener('click', function(event) {
  const overlay = document.querySelector('.mobile-menu-overlay');
  if (event.target === overlay) {
    closeMobileMenu();
  }
});

// Close menu when clicking on a navigation link
document.addEventListener('click', function(event) {
  if (event.target.closest('#navigation a')) {
    // Small delay to allow HTMX to process the click
    setTimeout(() => {
      closeMobileMenu();
    }, 100);
  }
});

// Close menu on escape key
document.addEventListener('keydown', function(event) {
  if (event.key === 'Escape') {
    const navigation = document.getElementById('navigation');
    if (navigation && navigation.classList.contains('mobile-open')) {
      closeMobileMenu();
    }
  }
});

// Close menu on window resize if switching to desktop
window.addEventListener('resize', function() {
  if (window.innerWidth >= 1024) {
    closeMobileMenu();
  }
});

// Initialize mobile menu when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
  initializeMobileMenu();
});

// Re-initialize mobile menu after HTMX content swaps
document.addEventListener('htmx:afterSwap', function() {
  initializeMobileMenu();
});

function initializeMobileMenu() {
  // Ensure mobile menu button exists and has proper event listener
  const mobileMenuButton = document.querySelector('#mobile-menu-toggle');
  if (mobileMenuButton) {
    // Remove any existing onclick attribute and add proper event listener
    mobileMenuButton.removeAttribute('onclick');

    // Remove existing event listeners to prevent duplicates
    const newButton = mobileMenuButton.cloneNode(true);
    mobileMenuButton.parentNode.replaceChild(newButton, mobileMenuButton);

    newButton.addEventListener('click', function(e) {
      e.preventDefault();
      toggleMobileMenu();
    });
  }

  // Initialize ellipsis menu
  initializeEllipsisMenu();
}

// Ellipsis menu functionality
function initializeEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');
  
  if (ellipsisButton && ellipsisDropdown) {
    // Remove existing event listeners to prevent duplicates
    const newButton = ellipsisButton.cloneNode(true);
    ellipsisButton.parentNode.replaceChild(newButton, ellipsisButton);
    
    newButton.addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();
      toggleEllipsisMenu();
    });
  }
}

function toggleEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');
  
  if (ellipsisButton && ellipsisDropdown) {
    const isOpen = !ellipsisDropdown.classList.contains('hidden');
    
    if (isOpen) {
      closeEllipsisMenu();
    } else {
      openEllipsisMenu();
    }
  }
}

function openEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');
  
  if (ellipsisButton && ellipsisDropdown) {
    ellipsisDropdown.classList.remove('hidden');
    ellipsisButton.setAttribute('aria-expanded', 'true');
  }
}

function closeEllipsisMenu() {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');
  
  if (ellipsisButton && ellipsisDropdown) {
    ellipsisDropdown.classList.add('hidden');
    ellipsisButton.setAttribute('aria-expanded', 'false');
  }
}

// Close ellipsis menu when clicking outside
document.addEventListener('click', function(event) {
  const ellipsisButton = document.querySelector('#ellipsis-menu-toggle');
  const ellipsisDropdown = document.querySelector('#ellipsis-dropdown');
  
  if (ellipsisButton && ellipsisDropdown) {
    if (!ellipsisButton.contains(event.target) && !ellipsisDropdown.contains(event.target)) {
      closeEllipsisMenu();
    }
  }
});

// Close ellipsis menu on escape key
document.addEventListener('keydown', function(event) {
  if (event.key === 'Escape') {
    closeEllipsisMenu();
  }
});
