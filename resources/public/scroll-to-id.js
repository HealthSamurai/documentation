// Scroll to ID script
// Waits for page load and checks for Scroll-To-Id header from backend

function scrollToIdFromHeader() {
  // Check if we have a Scroll-To-Id header in the current response
  // For HTMX requests, we can get it from the event detail
  // For regular page loads, we need to check if it was passed via meta tag or data attribute

  // Method 1: Check for HTMX response header
  if (window.htmx && window.htmx.lastXHR) {
    const scrollToId = window.htmx.lastXHR.getResponseHeader('Scroll-To-Id');
    if (scrollToId) {
      const element = document.getElementById(scrollToId);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 100);
        return;
      }
    }
  }

  // Method 2: Check for meta tag (for regular page loads)
  const metaTag = document.querySelector('meta[name="scroll-to-id"]');
  if (metaTag) {
    const scrollToId = metaTag.getAttribute('content');
    if (scrollToId) {
      const element = document.getElementById(scrollToId);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 100);
        return;
      }
    }
  }
}

// Run on DOM content loaded
document.addEventListener('DOMContentLoaded', function() {
  scrollToIdFromHeader();
});

// Run after HTMX content swaps
document.addEventListener('htmx:afterSwap', function(evt) {
  // Store the XHR object for later access
  if (evt.detail && evt.detail.xhr) {
    window.htmx = window.htmx || {};
    window.htmx.lastXHR = evt.detail.xhr;
  }

  // Check for Scroll-To-Id header in HTMX response
  if (evt.detail && evt.detail.xhr) {
    const scrollToId = evt.detail.xhr.getResponseHeader('Scroll-To-Id');
    if (scrollToId) {
      const element = document.getElementById(scrollToId);
      if (element) {
        setTimeout(() => {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }, 100);
      }
    }
  }
});

// Run after HTMX settles (for cases where content is dynamically added)
document.addEventListener('htmx:afterSettle', function() {
  scrollToIdFromHeader();
});

// Also run on window load for cases where content loads after DOMContentLoaded
window.addEventListener('load', function() {
  scrollToIdFromHeader();
});
