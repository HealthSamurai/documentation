// Unified Content Renderer with HTMX Best Practices
// Uses htmx.onLoad() for initialization and proper cleanup handlers
(function () {
  'use strict';
  
  // Library loading state
  const libraries = {
    mermaid: { loaded: false, loading: false },
    katex: { loaded: false, loading: false }
  };

  // Helper function to escape CSS selector
  function escapeCssSelector(selector) {
    if (!selector) return '';
    return selector.replace(/[!"#$%&'()*+,.\/:;<=>?@[\\\]^`{|}~]/g, '\\$&');
  }
  
  // Global function for filtering features (used in examples page)
  window.filterFeatures = function(searchTerm) {
    const items = document.querySelectorAll('.feature-item');
    const term = searchTerm.toLowerCase();
    items.forEach(item => {
      const featureName = item.dataset.featureName;
      if (featureName && featureName.includes(term)) {
        item.style.display = 'block';
      } else {
        item.style.display = 'none';
      }
    });
  };
  
  // Load Mermaid library dynamically
  function loadMermaid(callback) {
    if (libraries.mermaid.loaded || libraries.mermaid.loading) {
      if (callback && libraries.mermaid.loaded) callback();
      return;
    }
    
    console.log('[Content Renderer] Loading Mermaid.js...');
    libraries.mermaid.loading = true;
    
    const script = document.createElement('script');
    script.src = '/docs/static/mermaid.min.js';
    
    script.onload = function() {
      libraries.mermaid.loaded = true;
      libraries.mermaid.loading = false;
      
      // Initialize mermaid with config
      if (typeof mermaid !== 'undefined' && window.MERMAID_CONFIG) {
        mermaid.initialize(window.MERMAID_CONFIG);
      }
      
      console.log('[Content Renderer] Mermaid.js loaded successfully');
      if (callback) callback();
    };
    
    script.onerror = function() {
      libraries.mermaid.loading = false;
      console.error('[Content Renderer] Failed to load Mermaid.js');
    };
    
    document.head.appendChild(script);
  }
  
  // Load KaTeX library dynamically
  function loadKatex(callback) {
    if (libraries.katex.loaded || libraries.katex.loading) {
      if (callback && libraries.katex.loaded) callback();
      return;
    }
    
    console.log('[Content Renderer] Loading KaTeX...');
    libraries.katex.loading = true;
    
    // Load CSS first
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = '/docs/static/katex.min.css';
    document.head.appendChild(link);
    
    // Load JS
    const script = document.createElement('script');
    script.src = '/docs/static/katex.min.js';
    
    script.onload = function() {
      libraries.katex.loaded = true;
      libraries.katex.loading = false;
      console.log('[Content Renderer] KaTeX loaded successfully');
      if (callback) callback();
    };
    
    script.onerror = function() {
      libraries.katex.loading = false;
      console.error('[Content Renderer] Failed to load KaTeX');
    };
    
    document.head.appendChild(script);
  }

  // Render code highlighting
  function renderHighlightJS(container) {
    if (typeof hljs === 'undefined') return;

    // Find unprocessed code blocks
    const codeBlocks = container.querySelectorAll('pre code:not([data-hljs-processed])');
    codeBlocks.forEach(block => {
      if (!block.classList.contains('hljs')) {
        hljs.highlightElement(block);
      }
      block.setAttribute('data-hljs-processed', 'true');
    });
    
    // Trigger copy button initialization if available
    if (typeof initializeCopyButtons === 'function') {
      setTimeout(initializeCopyButtons, 50);
    }
  }

  // Render Mermaid diagrams
  function renderMermaid(container) {
    if (typeof mermaid === 'undefined') return;
    
    // Find unprocessed mermaid blocks
    const mermaidBlocks = container.querySelectorAll('pre.mermaid:not([data-mermaid-processed])');
    
    mermaidBlocks.forEach((element, index) => {
      // Mark as processed immediately to prevent double processing
      element.setAttribute('data-mermaid-processed', 'true');
      
      // Generate unique ID
      const id = `mermaid-${Date.now()}-${index}-${Math.random().toString(36).substring(2, 11)}`;
      
      // Get the diagram text
      const graphDefinition = element.textContent || element.innerText;
      
      // Create a container div
      const containerDiv = document.createElement('div');
      containerDiv.className = 'mermaid-container';
      containerDiv.setAttribute('data-mermaid-id', id);
      
      // Insert container after the pre element
      element.parentNode.insertBefore(containerDiv, element.nextSibling);
      
      // Hide the original pre element
      element.style.display = 'none';
      
      // Render the diagram
      try {
        mermaid.render(id, graphDefinition).then((result) => {
          containerDiv.innerHTML = result.svg;
        }).catch((error) => {
          console.error('Error rendering Mermaid diagram:', error);
          // Show original on error
          element.style.display = 'block';
          element.removeAttribute('data-mermaid-processed');
          containerDiv.remove();
        });
      } catch (error) {
        console.error('Error initializing Mermaid diagram:', error);
        // Show original on error
        element.style.display = 'block';
        element.removeAttribute('data-mermaid-processed');
        containerDiv.remove();
      }
    });
  }

  // Render LaTeX math with KaTeX
  function renderKaTeX(container) {
    if (typeof katex === 'undefined') return;
    
    // Render display math blocks
    const displayMathElements = container.querySelectorAll('.katex-display:not([data-katex-processed])');
    displayMathElements.forEach(element => {
      element.setAttribute('data-katex-processed', 'true');
      const tex = element.textContent || element.innerText;
      try {
        katex.render(tex, element, {
          displayMode: true,
          throwOnError: false,
          errorColor: '#cc0000'
        });
      } catch (error) {
        console.error('Error rendering display math:', error);
        element.style.color = '#cc0000';
      }
    });
    
    // Render inline math blocks
    const inlineMathElements = container.querySelectorAll('.katex-inline:not([data-katex-processed])');
    inlineMathElements.forEach(element => {
      element.setAttribute('data-katex-processed', 'true');
      const tex = element.textContent || element.innerText;
      try {
        katex.render(tex, element, {
          displayMode: false,
          throwOnError: false,
          errorColor: '#cc0000'
        });
      } catch (error) {
        console.error('Error rendering inline math:', error);
        element.style.color = '#cc0000';
      }
    });
  }

  // Initialize TOC scroll spy
  function initializeTocScrollSpy() {
    const tocLinks = document.querySelectorAll('#toc-container a[href^="#"]');
    const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');
    
    if (tocLinks.length === 0 || headings.length === 0) return;
    
    // Remove old scroll handler if exists
    if (window._tocScrollHandler) {
      window.removeEventListener('scroll', window._tocScrollHandler);
      window.removeEventListener('resize', window._tocScrollHandler);
    }
    
    // Create new scroll handler with debounce
    let scrollTimeout;
    window._tocScrollHandler = function() {
      if (scrollTimeout) clearTimeout(scrollTimeout);
      scrollTimeout = setTimeout(updateActiveTocSection, 10);
    };
    
    // Add event listeners
    window.addEventListener('scroll', window._tocScrollHandler);
    window.addEventListener('resize', window._tocScrollHandler);
    
    // Initialize active section
    updateActiveTocSection();
  }
  
  // Update active TOC section
  function updateActiveTocSection() {
    const tocLinks = document.querySelectorAll('#toc-container a[href^="#"]');
    const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');
    
    if (tocLinks.length === 0 || headings.length === 0) return;
    
    const scrollPosition = window.scrollY + 100;
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
    
    // Update active classes
    tocLinks.forEach(link => {
      link.classList.remove('active');
    });
    
    if (currentSection) {
      const escapedId = escapeCssSelector(currentSection);
      const activeLink = document.querySelector(`#toc-container a[href="#${escapedId}"]`);
      if (activeLink) {
        activeLink.classList.add('active');
      }
    }
  }
  
  // Initialize TOC click handlers
  function initializeTocClickHandlers() {
    const tocLinks = document.querySelectorAll('#toc-container a[href^="#"]:not([data-toc-handler])');
    
    tocLinks.forEach(link => {
      link.setAttribute('data-toc-handler', 'true');
      
      link.addEventListener('click', function(e) {
        const href = this.getAttribute('href');
        if (href && href.startsWith('#')) {
          e.preventDefault();
          const targetId = href.substring(1);
          const targetElement = document.getElementById(targetId);
          
          if (targetElement) {
            const headerHeight = 64; // 4rem = 64px
            const targetPosition = targetElement.offsetTop - headerHeight - 20;
            
            // Update URL with the hash
            if (history.pushState) {
              history.pushState(null, null, href);
            } else {
              window.location.hash = targetId;
            }
            
            window.scrollTo({
              top: targetPosition,
              behavior: 'smooth'
            });
          }
        }
      });
    });
  }

  // Process content (check for libraries and render)
  function processContent(target) {
    // Check if we need to load Mermaid
    const hasMermaid = target.querySelectorAll('.language-mermaid, .mermaid, pre code.language-mermaid, pre.mermaid').length > 0;
    if (hasMermaid && !libraries.mermaid.loaded && !libraries.mermaid.loading) {
      loadMermaid(() => {
        renderMermaid(target);
      });
    } else if (libraries.mermaid.loaded) {
      renderMermaid(target);
    }
    
    // Check if we need to load KaTeX
    const hasKatex = target.querySelectorAll('.katex, .math, .latex, .katex-display, .katex-inline').length > 0;
    if (hasKatex && !libraries.katex.loaded && !libraries.katex.loading) {
      loadKatex(() => {
        renderKaTeX(target);
      });
    } else if (libraries.katex.loaded) {
      renderKaTeX(target);
    }
    
    // Always render highlight.js if available
    renderHighlightJS(target);
    
    // Initialize TOC if on full page
    if (target === document.body || target === document) {
      initializeTocScrollSpy();
      initializeTocClickHandlers();
    }
  }

  // Cleanup function for element removal
  function cleanupElement(element) {
    // Check if element is a valid DOM element with querySelectorAll method
    if (!element || typeof element.querySelectorAll !== 'function') {
      return;
    }
    
    // Clean up Mermaid containers
    element.querySelectorAll('.mermaid-container').forEach(el => el.remove());
    
    // Reset mermaid blocks
    element.querySelectorAll('pre.mermaid[data-mermaid-processed]').forEach(el => {
      el.removeAttribute('data-mermaid-processed');
      el.style.display = 'block';
    });
    
    // Clean up any other resources if needed
    // This is where you'd clean up event listeners, timers, etc.
  }

  // Main initialization using htmx.onLoad (HTMX best practice)
  if (typeof htmx !== 'undefined') {
    // This runs on initial page load and after every HTMX swap
    htmx.onLoad(function(target) {
      // Process the newly loaded content
      processContent(target);
    });
    
    // Clean up before element removal
    htmx.on('htmx:beforeCleanupElement', function(evt) {
      cleanupElement(evt.detail.elt);
    });
    
    // Additional handler for afterSettle to catch any missed content
    htmx.on('htmx:afterSettle', function(evt) {
      // Re-initialize TOC handlers after settle
      setTimeout(() => {
        initializeTocClickHandlers();
      }, 50);
    });
  }
  
  // Fallback for non-HTMX page loads
  document.addEventListener('DOMContentLoaded', function() {
    if (typeof htmx === 'undefined') {
      processContent(document.body);
    }
  });
  
  // Handle browser back/forward navigation
  window.addEventListener('popstate', function() {
    setTimeout(() => {
      processContent(document.body);
    }, 10);
  });
  
  // Export functions for global access if needed
  window.renderContent = processContent;
  window.updateActiveTocSection = updateActiveTocSection;
  window.initializeTocScrollSpy = initializeTocScrollSpy;
  window.initializeTocClickHandlers = initializeTocClickHandlers;
})();