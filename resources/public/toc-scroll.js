// TOC Scroll Spy - tracks active section on scroll
document.addEventListener('DOMContentLoaded', function() {

  const tocLinks = document.querySelectorAll('.toc a[href^="#"]');
  const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');

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

    // Remove active class from all links
    tocLinks.forEach(link => {
      link.classList.remove('active');
    });

    // Add active class to current section
    if (currentSection) {
      const escapedId = escapeCssSelector(currentSection);
      const activeLink = document.querySelector(`.toc a[href="#${escapedId}"]`);
      if (activeLink) {
        activeLink.classList.add('active');
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

document.addEventListener('htmx:afterSwap', function(event) {
  if (event.target.id === 'content' || event.target.tagName === 'BODY') {

    // Reinitialize TOC spy after loading new content
    setTimeout(() => {
      const tocLinks = document.querySelectorAll('.toc a[href^="#"]');
      const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');


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
