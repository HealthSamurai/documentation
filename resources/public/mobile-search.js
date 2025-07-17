// Mobile search toggle functionality
document.addEventListener('DOMContentLoaded', function() {
  const mobileSearchToggle = document.getElementById('mobile-search-toggle');
  const mobileSearchContainer = document.getElementById('mobile-search-container');
  const mobileSearchInput = document.getElementById('mobile-search-input');
  const mobileSearchClose = document.getElementById('mobile-search-close');
  const desktopSearchInput = document.getElementById('search-input');
  
  if (!mobileSearchToggle || !mobileSearchContainer) return;
  
  // Toggle mobile search
  mobileSearchToggle.addEventListener('click', function(e) {
    e.preventDefault();
    e.stopPropagation();
    
    if (mobileSearchContainer.classList.contains('hidden')) {
      mobileSearchContainer.classList.remove('hidden');
      // Focus and select input after a small delay to ensure it's visible
      setTimeout(() => {
        mobileSearchInput.focus();
        mobileSearchInput.select();
      }, 100);
    } else {
      mobileSearchContainer.classList.add('hidden');
      mobileSearchInput.value = '';
      // Clear mobile dropdown
      const mobileDropdown = document.getElementById('mobile-search-dropdown');
      if (mobileDropdown) {
        mobileDropdown.innerHTML = '';
      }
    }
  });
  
  // Close mobile search
  if (mobileSearchClose) {
    mobileSearchClose.addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();
      mobileSearchContainer.classList.add('hidden');
      mobileSearchInput.value = '';
      // Clear mobile dropdown
      const mobileDropdown = document.getElementById('mobile-search-dropdown');
      if (mobileDropdown) {
        mobileDropdown.innerHTML = '';
      }
    });
  }
  
  // Close mobile search on outside click
  document.addEventListener('click', function(e) {
    if (!mobileSearchContainer.classList.contains('hidden') &&
        !mobileSearchContainer.contains(e.target) &&
        !mobileSearchToggle.contains(e.target)) {
      mobileSearchContainer.classList.add('hidden');
      mobileSearchInput.value = '';
      // Clear mobile dropdown
      const mobileDropdown = document.getElementById('mobile-search-dropdown');
      if (mobileDropdown) {
        mobileDropdown.innerHTML = '';
      }
    }
  });
  
  // Handle mobile search keyboard navigation
  if (mobileSearchInput) {
    mobileSearchInput.addEventListener('keydown', function(event) {
      const dropdown = document.getElementById('mobile-search-dropdown');
      
      // Check if dropdown is visible and has content
      if (dropdown && (dropdown.hasAttribute('data-search-dropdown') || dropdown.querySelector('a[data-search-result-index]'))) {
        const results = dropdown.querySelectorAll('a[data-search-result-index]');
        
        // Only handle navigation keys if dropdown has visible results
        if (results.length > 0 && (event.key === 'ArrowDown' || event.key === 'ArrowUp' || event.key === 'Enter')) {
          let currentIndex = -1;
          
          // Find currently selected item
          for (let i = 0; i < results.length; i++) {
            if (results[i].classList.contains('bg-warning-2')) {
              currentIndex = i;
              break;
            }
          }
          
          if (event.key === 'ArrowDown') {
            event.preventDefault();
            currentIndex = currentIndex < results.length - 1 ? currentIndex + 1 : 0;
            updateMobileSelectedResult(results, currentIndex);
            return;
          } else if (event.key === 'ArrowUp') {
            event.preventDefault();
            currentIndex = currentIndex <= 0 ? results.length - 1 : currentIndex - 1;
            updateMobileSelectedResult(results, currentIndex);
            return;
          } else if (event.key === 'Enter') {
            event.preventDefault();
            if (currentIndex >= 0 && currentIndex < results.length) {
              results[currentIndex].click();
              // Close mobile search after navigation
              mobileSearchContainer.classList.add('hidden');
              mobileSearchInput.value = '';
            }
            return;
          }
        }
        
        // Handle Escape
        if (event.key === 'Escape') {
          event.preventDefault();
          if (dropdown.innerHTML !== '') {
            dropdown.innerHTML = '';
          } else {
            // Close mobile search if dropdown is already empty
            mobileSearchContainer.classList.add('hidden');
            mobileSearchInput.value = '';
          }
          return;
        }
      } else if (event.key === 'Escape') {
        // Close mobile search if no dropdown
        event.preventDefault();
        mobileSearchContainer.classList.add('hidden');
        mobileSearchInput.value = '';
      }
    });
  }
  
  // Update selected result function for mobile
  function updateMobileSelectedResult(results, selectedIndex) {
    results.forEach((result, index) => {
      const isSectionEntry = result.querySelector('.border-l-2') !== null;
      
      if (index === selectedIndex) {
        result.classList.add('bg-warning-2', 'text-tint-strong');
        result.classList.remove('hover:bg-tint-2', 'hover:text-tint-strong');
        
        if (isSectionEntry) {
          result.classList.remove('text-tint-10');
          const childElements = result.querySelectorAll('p.text-tint-10');
          childElements.forEach(child => {
            child.classList.remove('text-tint-10');
            child.classList.add('text-tint-strong');
          });
        } else {
          result.classList.remove('text-tint');
        }
        
        const fileIcon = result.querySelector('div:first-of-type');
        if (fileIcon) {
          fileIcon.classList.add('text-warning-9');
          fileIcon.classList.remove('text-tint-9', 'group-hover:text-tint-strong');
        }
        
        const enterIcon = result.querySelector('.search-action-button');
        if (enterIcon) {
          enterIcon.classList.add('text-orange-600', 'opacity-100');
          enterIcon.classList.remove('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
          enterIcon.innerHTML = '<div class="px-2 py-1 rounded border text-xs font-medium flex items-center justify-center bg-orange-600 text-white border-orange-700">↵</div>';
        }
        
        result.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
      } else {
        result.classList.remove('bg-warning-2', 'text-tint-strong');
        
        if (isSectionEntry) {
          result.classList.add('text-tint-10');
          const childElements = result.querySelectorAll('p.text-tint-strong');
          childElements.forEach(child => {
            child.classList.remove('text-tint-strong');
            child.classList.add('text-tint-10');
          });
        } else {
          result.classList.add('text-tint');
        }
        
        const fileIcon = result.querySelector('div:first-of-type');
        if (fileIcon) {
          fileIcon.classList.remove('text-warning-9');
          fileIcon.classList.add('text-tint-9', 'group-hover:text-tint-strong');
        }
        
        const enterIcon = result.querySelector('.search-action-button');
        if (enterIcon) {
          enterIcon.classList.remove('text-orange-600', 'opacity-100');
          enterIcon.classList.add('text-tint-9', 'opacity-60', 'group-hover:opacity-100');
          enterIcon.innerHTML = '<svg class="size-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" stroke-width="2"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"></path></svg>';
        }
      }
    });
  }
});