// TOC Scroll Spy - tracks active section on scroll
document.addEventListener('DOMContentLoaded', function() {
  console.log('TOC Scroll Spy initialized');
  
  const tocLinks = document.querySelectorAll('.toc a[href^="#"]');
  const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');
  
  console.log('Found TOC links:', tocLinks.length);
  console.log('Found headings:', headings.length);
  
  // Debug: log all TOC links
  tocLinks.forEach((link, index) => {
    console.log(`TOC link ${index}:`, link.href, link.textContent);
  });
  
  // Debug: log all headings
  headings.forEach((heading, index) => {
    console.log(`Heading ${index}:`, heading.id, heading.textContent);
  });
  
  if (tocLinks.length === 0 || headings.length === 0) return;

  // Function to escape CSS selector
  function escapeCssSelector(selector) {
    return selector.replace(/[!"#$%&'()*+,.\/:;<=>?@[\\\]^`{|}~]/g, '\\$&');
  }

  // Function to determine active section
  function updateActiveSection() {
    const scrollPosition = window.scrollY + 100; // Small offset for better UX
    
    let currentSection = null;
    
    // Find current section
    for (let i = headings.length - 1; i >= 0; i--) {
      const heading = headings[i];
      const rect = heading.getBoundingClientRect();
      const offsetTop = rect.top + window.scrollY;
      
      if (scrollPosition >= offsetTop) {
        currentSection = heading.id;
        break;
      }
    }
    
    console.log('Current scroll position:', scrollPosition);
    console.log('Current section:', currentSection);
    
    // Remove active class from all links
    tocLinks.forEach(link => {
      link.classList.remove('active');
    });
    
    // Add active class to current section
    if (currentSection) {
      const escapedId = escapeCssSelector(currentSection);
      const activeLink = document.querySelector(`.toc a[href="#${escapedId}"]`);
      console.log('Looking for link with href:', `#${escapedId}`);
      console.log('Found active link:', activeLink);
      if (activeLink) {
        activeLink.classList.add('active');
        console.log('Added active class to:', activeLink.textContent);
      }
    }
  }
  
  // Scroll handler with debounce for performance
  let scrollTimeout;
  function handleScroll() {
    if (scrollTimeout) {
      clearTimeout(scrollTimeout);
    }
    scrollTimeout = setTimeout(updateActiveSection, 10);
  }
  
  // Add event listeners
  window.addEventListener('scroll', handleScroll);
  window.addEventListener('resize', handleScroll);
  
  // Initialize on page load
  updateActiveSection();
});

// Handle HTMX events to update TOC on navigation
document.addEventListener('htmx:afterSwap', function(event) {
  if (event.target.id === 'content') {
    console.log('HTMX content swap detected, reinitializing TOC spy');
    
    // Reinitialize TOC spy after loading new content
    setTimeout(() => {
      const tocLinks = document.querySelectorAll('.toc a[href^="#"]');
      const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');
      
      console.log('After HTMX - Found TOC links:', tocLinks.length);
      console.log('After HTMX - Found headings:', headings.length);
      
      if (tocLinks.length > 0 && headings.length > 0) {
        // Update active section
        const scrollPosition = window.scrollY + 100;
        let currentSection = null;
        
        for (let i = headings.length - 1; i >= 0; i--) {
          const heading = headings[i];
          const rect = heading.getBoundingClientRect();
          const offsetTop = rect.top + window.scrollY;
          
          if (scrollPosition >= offsetTop) {
            currentSection = heading.id;
            break;
          }
        }
        
        tocLinks.forEach(link => {
          link.classList.remove('active');
        });
        
        if (currentSection) {
          const escapedId = escapeCssSelector(currentSection);
          const activeLink = document.querySelector(`.toc a[href="#${escapedId}"]`);
          if (activeLink) {
            activeLink.classList.add('active');
          }
        }
      }
    }, 100);
  }
}); 