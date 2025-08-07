// Heading link copy functionality for h2 and h3 elements
document.addEventListener('DOMContentLoaded', function() {
  setTimeout(initializeHeadingLinks, 100);
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function() {
  setTimeout(initializeHeadingLinks, 100);
});

// Handle window resize to hide buttons on mobile
window.addEventListener('resize', function() {
  const linkButtons = document.querySelectorAll('button[data-heading-link]');
  linkButtons.forEach(button => {
    if (window.innerWidth <= 768) {
      button.style.display = 'none';
      button.style.opacity = '0';
    }
  });
});

function initializeHeadingLinks() {
  // Find all h2 and h3 elements
  const headings = document.querySelectorAll('h2[id], h3[id]');
  
  headings.forEach((heading) => {
    // Skip if already has link button
    if (heading.querySelector('button[data-heading-link]')) {
      return;
    }
    
    // Skip if no ID (no anchor point)
    if (!heading.id) {
      return;
    }
    
    // Create link button
    const linkButton = document.createElement('button');
    linkButton.setAttribute('data-heading-link', 'true');
    linkButton.style.cssText = 'position: absolute; left: -24px; top: 50%; transform: translateY(-50%); padding: 4px; font-size: 16px; background: transparent; color: #9b9b9b; border: none; cursor: pointer; opacity: 0; transition: opacity 0.2s, color 0.2s; z-index: 10; display: none;';
    linkButton.innerHTML = '<svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z" fill="currentColor"/></svg>';
    linkButton.title = 'Link to section';
    
    // Add click handler
    linkButton.addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();
      
      // Update the URL in the browser
      window.history.replaceState(null, '', '#' + heading.id);
      
      // Scroll to the heading (CSS scroll-margin-top handles the offset)
      heading.scrollIntoView({ behavior: 'smooth', block: 'start' });
    });
    
    // Make heading container relative for absolute positioning
    heading.style.position = 'relative';
    
    // Add hover effect to show button (only on desktop)
    heading.addEventListener('mouseenter', function() {
      if (window.innerWidth > 768) {
        linkButton.style.display = 'block';
        linkButton.style.opacity = '1';
      }
    });
    
    heading.addEventListener('mouseleave', function() {
      if (window.innerWidth > 768) {
        linkButton.style.opacity = '0';
        setTimeout(() => {
          if (linkButton.style.opacity === '0') {
            linkButton.style.display = 'none';
          }
        }, 200);
      }
    });
    
    // Also show on button hover
    linkButton.addEventListener('mouseenter', function() {
      if (window.innerWidth > 768) {
        linkButton.style.opacity = '1';
        linkButton.style.color = '#4b5563';
      }
    });
    
    // Reset when mouse leaves button
    linkButton.addEventListener('mouseleave', function() {
      if (window.innerWidth > 768) {
        this.style.color = '#9b9b9b';
      }
    });
    
    // Insert button
    heading.appendChild(linkButton);
  });
}