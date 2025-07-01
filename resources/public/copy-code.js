// Copy code functionality
document.addEventListener('DOMContentLoaded', function() {
  // Wait a bit for highlight.js to finish
  setTimeout(initializeCopyButtons, 100);
});

// Re-initialize after HTMX content swaps
document.addEventListener('htmx:afterSwap', function() {
  // Wait a bit for highlight.js to finish
  setTimeout(initializeCopyButtons, 100);
});

// Also initialize after highlight.js runs
if (typeof hljs !== 'undefined') {
  const originalHighlightAll = hljs.highlightAll;
  hljs.highlightAll = function() {
    originalHighlightAll.call(this);
    setTimeout(initializeCopyButtons, 50);
  };
}

// Handle window resize to hide buttons on mobile
window.addEventListener('resize', function() {
  const copyButtons = document.querySelectorAll('.copy-code-btn');
  copyButtons.forEach(button => {
    if (window.innerWidth <= 768) {
      button.style.display = 'none';
      button.style.opacity = '0';
    }
  });
});

function initializeCopyButtons() {
  // Find all pre elements that contain code blocks
  const preElements = document.querySelectorAll('pre');

  preElements.forEach((pre, index) => {
    // Skip if already has copy button
    if (pre.querySelector('.copy-code-btn')) {
      return;
    }

    // Skip mermaid diagrams
    if (pre.classList.contains('mermaid')) {
      return;
    }

    const codeBlock = pre.querySelector('code');
    if (!codeBlock) {
      return;
    }

    // Create copy button
    const copyButton = document.createElement('button');
    copyButton.className = 'copy-code-btn';
    copyButton.style.cssText = 'position: absolute; top: 8px; right: 8px; padding: 4px 8px; font-size: 12px; background: white; color: #374151; border: 1px solid #d1d5db; border-radius: 4px; cursor: pointer; opacity: 0; transition: opacity 0.2s; z-index: 10; box-shadow: 0 2px 4px rgba(0,0,0,0.1); font-weight: 500; display: none;';
    copyButton.innerHTML = 'Copy';
    copyButton.title = 'Copy code';

    // Add click handler
    copyButton.addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();

      const textToCopy = codeBlock.textContent;

      // Use modern clipboard API if available
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(textToCopy).then(function() {
          showCopySuccess(copyButton);
        }).catch(function(err) {
          console.error('Failed to copy: ', err);
        });
      }
    });

    // Make pre container relative for absolute positioning
    pre.style.position = 'relative';

    // Add hover effect to show button (only on desktop)
    pre.addEventListener('mouseenter', function() {
      if (window.innerWidth > 768) {
        copyButton.style.display = 'block';
        copyButton.style.opacity = '1';
      }
    });

    pre.addEventListener('mouseleave', function() {
      if (window.innerWidth > 768) {
        copyButton.style.opacity = '0';
        setTimeout(() => {
          if (copyButton.style.opacity === '0') {
            copyButton.style.display = 'none';
          }
        }, 200);
      }
    });

    // Also show on button hover
    copyButton.addEventListener('mouseenter', function() {
      if (window.innerWidth > 768) {
        copyButton.style.opacity = '1';
      }
    });

    // Reset text when mouse leaves button
    copyButton.addEventListener('mouseleave', function() {
      if (window.innerWidth > 768) {
        // Reset to original text and style if it was changed
        if (this.innerHTML === 'Copied!') {
          this.innerHTML = 'Copy';
          this.style.background = 'white';
        }
      }
    });

    // Insert button
    pre.appendChild(copyButton);
  });
}

function showCopySuccess(button) {
  const originalHTML = button.innerHTML;
  const originalStyle = button.style.cssText;

  button.innerHTML = 'Copied!';

  setTimeout(() => {
    button.innerHTML = originalHTML;
    button.style.cssText = originalStyle;
  }, 2000);
}
