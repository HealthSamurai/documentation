// Unified Content Renderer for hljs, mermaid, KaTeX, and TOC scroll spy
// Handles initialization and rendering for all content types after page load and HTMX navigation

(function () {
  'use strict';
  

  // Configuration and state
  const config = {
    hljs: {
      enabled: typeof hljs !== 'undefined',
      initialized: false
    },
    mermaid: {
      enabled: typeof mermaid !== 'undefined',
      initialized: false
    },
    katex: {
      enabled: typeof katex !== 'undefined',
      initialized: false
    },
    tocScrollSpy: {
      enabled: true,
      initialized: false,
      scrollTimeout: null,
      scrollHandler: null
    }
  };

  // Helper function to escape CSS selector
  function escapeCssSelector(selector) {
    if (!selector) return '';
    return selector.replace(/[!"#$%&'()*+,.\/:;<=>?@[\\\]^`{|}~]/g, '\\$&');
  }

  // Initialize libraries that need one-time setup
  function initializeLibraries() {
    // Initialize Mermaid
    try {
      if (config.mermaid.enabled && !config.mermaid.initialized) {
        if (typeof window.MERMAID_CONFIG !== 'undefined') {
          mermaid.initialize(window.MERMAID_CONFIG);
          config.mermaid.initialized = true;
        }
      }
    } catch (error) {
      console.error('Error initializing Mermaid:', error);
      config.mermaid.enabled = false; // Disable to prevent repeated errors
    }

    // hljs doesn't need initialization
    if (config.hljs.enabled) {
      config.hljs.initialized = true;
    }

    // KaTeX doesn't need initialization
    if (config.katex.enabled) {
      config.katex.initialized = true;
    }

    // TOC scroll spy initialization
    if (config.tocScrollSpy.enabled) {
      config.tocScrollSpy.initialized = true;
    }
  }

  // TOC Scroll Spy - Update active section
  function updateActiveTocSection() {
    const tocLinks = document.querySelectorAll('#toc-container a[href^="#"]');
    const headings = document.querySelectorAll('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');
    
    if (tocLinks.length === 0 || headings.length === 0) return;
    
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
      const activeLink = document.querySelector(`#toc-container a[href="#${escapedId}"]`);
      if (activeLink) {
        activeLink.classList.add('active');
      }
    }
  }

  // TOC Scroll handler with debounce
  function handleTocScroll() {
    if (config.tocScrollSpy.scrollTimeout) {
      clearTimeout(config.tocScrollSpy.scrollTimeout);
    }
    config.tocScrollSpy.scrollTimeout = setTimeout(updateActiveTocSection, 10);
  }

  // Initialize TOC scroll spy - only scroll tracking, no DOM manipulation
  function initializeTocScrollSpy() {
    if (!config.tocScrollSpy.enabled || !config.tocScrollSpy.initialized) return;

    // Check if we have TOC and headings
    const hasToc = document.querySelector('#toc-container a[href^="#"]');
    const hasHeadings = document.querySelector('h1[id], h2[id], h3[id], h4[id], h5[id], h6[id]');

    if (!hasToc || !hasHeadings) return;

    // Remove old scroll event listeners if they exist
    if (config.tocScrollSpy.scrollHandler) {
      window.removeEventListener('scroll', config.tocScrollSpy.scrollHandler);
      window.removeEventListener('resize', config.tocScrollSpy.scrollHandler);
    }

    // Create new scroll handler
    config.tocScrollSpy.scrollHandler = handleTocScroll;

    // Add scroll event listeners
    window.addEventListener('scroll', config.tocScrollSpy.scrollHandler);
    window.addEventListener('resize', config.tocScrollSpy.scrollHandler);

    // Initialize active section highlighting
    updateActiveTocSection();
  }
  
  // Separate function for TOC link click handlers
  function initializeTocClickHandlers() {
    const tocLinks = document.querySelectorAll('#toc-container a[href^="#"]');
    
    tocLinks.forEach(link => {
      // Skip if handler already exists
      if (link._tocClickHandler) return;
      
      // Create click handler
      link._tocClickHandler = function(e) {
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
      };
      
      // Add the click handler
      link.addEventListener('click', link._tocClickHandler);
    });
  }

  // Render code highlighting
  function renderHighlightJS(container) {
    if (!config.hljs.enabled || !config.hljs.initialized) return;

    try {
      if (container) {
        // Only highlight unprocessed code blocks in the container
        const codeBlocks = container.querySelectorAll('pre code:not(.hljs)');
        codeBlocks.forEach(block => {
          hljs.highlightElement(block);
        });
        
        // Also trigger copy button initialization for new content
        if (typeof initializeCopyButtons === 'function') {
          setTimeout(initializeCopyButtons, 50);
        }
      } else {
        // Highlight all unprocessed code blocks
        hljs.highlightAll();
      }
    } catch (error) {
      console.error('Error rendering code highlighting:', error);
    }
  }

  // Render Mermaid diagrams
  function renderMermaid(container) {
    if (!config.mermaid.enabled || !config.mermaid.initialized) return;

    const selector = container ? container.querySelectorAll('pre.mermaid') : document.querySelectorAll('pre.mermaid');

    if (selector.length === 0) return;

    selector.forEach((element, index) => {
      // Skip if already processed
      if (element.getAttribute('data-processed') === 'true') return;

      // Generate unique ID
      const id = `mermaid-${Date.now()}-${index}-${Math.random().toString(36).substring(2, 11)}`;

      // Get the diagram text
      const graphDefinition = element.textContent || element.innerText;

      // Create a container div
      const containerDiv = document.createElement('div');
      containerDiv.className = 'mermaid-container';

      // Insert container after the pre element
      element.parentNode.insertBefore(containerDiv, element.nextSibling);

      // Hide the original pre element
      element.style.display = 'none';
      element.setAttribute('data-processed', 'true');

      // Render the diagram
      try {
        mermaid.render(id, graphDefinition).then((result) => {
          containerDiv.innerHTML = result.svg;
        }).catch((error) => {
          console.error('Error rendering Mermaid diagram:', error);
          // Show original on error
          element.style.display = 'block';
          element.removeAttribute('data-processed');
          containerDiv.remove();
        });
      } catch (error) {
        console.error('Error initializing Mermaid diagram:', error);
        // Show original on error
        element.style.display = 'block';
        element.removeAttribute('data-processed');
        containerDiv.remove();
      }
    });
  }

  // Render LaTeX math with KaTeX
  function renderKaTeX(container) {
    if (!config.katex.enabled || !config.katex.initialized) {
      return;
    }

    try {
      const targetContainer = container || document.body;

      // Render display math blocks ($$...$$)
      const displayMathElements = targetContainer.querySelectorAll('.katex-display:not([data-katex-rendered])');
      displayMathElements.forEach(element => {
        const tex = element.textContent || element.innerText;
        try {
          katex.render(tex, element, {
            displayMode: true,
            throwOnError: false,
            errorColor: '#cc0000'
          });
          element.setAttribute('data-katex-rendered', 'true');
        } catch (error) {
          console.error('Error rendering display math:', error);
          element.style.color = '#cc0000';
        }
      });

      // Render inline math blocks ($...$)
      const inlineMathElements = targetContainer.querySelectorAll('.katex-inline:not([data-katex-rendered])');
      inlineMathElements.forEach(element => {
        const tex = element.textContent || element.innerText;
        try {
          katex.render(tex, element, {
            displayMode: false,
            throwOnError: false,
            errorColor: '#cc0000'
          });
          element.setAttribute('data-katex-rendered', 'true');
        } catch (error) {
          console.error('Error rendering inline math:', error);
          element.style.color = '#cc0000';
        }
      });

    } catch (error) {
      console.error('Error in KaTeX rendering:', error);
    }
  }

  // Main rendering function
  function renderContent(container) {
    // Initialize libraries if needed
    initializeLibraries();

    // Render all content types - each in its own try-catch to ensure all run
    try {
      renderHighlightJS(container);
    } catch (error) {
      console.error('Error in highlight.js rendering:', error);
    }

    try {
      renderMermaid(container);
    } catch (error) {
      console.error('Error in Mermaid rendering:', error);
    }

    try {
      renderKaTeX(container);
    } catch (error) {
      console.error('Error in KaTeX rendering:', error);
    }

    try {
      initializeTocScrollSpy();
    } catch (error) {
      console.error('Error in TOC scroll spy initialization:', error);
    }
  }

  // Clean up function for HTMX swaps
  function cleanupContent(container) {
    if (container) {
      // Clean up Mermaid containers and reset state
      container.querySelectorAll('.mermaid-container').forEach(el => el.remove());
      container.querySelectorAll('pre.mermaid[data-processed="true"]').forEach(el => {
        el.removeAttribute('data-processed');
        el.style.display = 'block';
      });
    }
  }


  // Event listeners
  document.addEventListener('DOMContentLoaded', function () {
    renderContent();
    // Initialize TOC click handlers separately, after content is rendered
    setTimeout(initializeTocClickHandlers, 50);
    
    // Try using htmx.on() API if available
    if (typeof htmx !== 'undefined' && htmx.on) {
      
      htmx.on('htmx:beforeSwap', function (event) {
        cleanupContent(event.detail.target);
      });
      
      htmx.on('htmx:afterSwap', function (event) {
        const target = event.detail.target;
        // Render immediately without delay
        renderContent(target);
      });
      
      htmx.on('htmx:afterSettle', function (event) {
        renderContent(event.detail.target);
        // Re-initialize TOC click handlers after content settles
        setTimeout(initializeTocClickHandlers, 50);
      });
      
      // Also listen for load event which HTMX triggers after content is loaded
      htmx.on('htmx:load', function (event) {
        renderContent(event.detail.elt);
      });
    } else {
      // Fallback to regular event listeners
      
      document.body.addEventListener('htmx:beforeSwap', function (event) {
        cleanupContent(event.detail.target);
      });

      document.body.addEventListener('htmx:afterSwap', function (event) {
        const target = event.detail.target;
        setTimeout(() => {
          renderContent(target);
        }, 10);
      });
    }
  });

  document.addEventListener('htmx:historyRestore', function () {
    setTimeout(() => {
      renderContent();
    }, 10);
  });

  // Handle browser back/forward navigation
  window.addEventListener('popstate', function () {
    setTimeout(() => {
      renderContent();
    }, 10);
  });
  
  // Also handle afterSettle event which fires after afterSwap
  document.addEventListener('htmx:afterSettle', function (event) {
    renderContent(event.detail.target);
  });

  // Export functions for global access if needed
  window.escapeCssSelector = escapeCssSelector;
  window.updateActiveTocSection = updateActiveTocSection;
  window.initializeTocScrollSpy = initializeTocScrollSpy;
  window.initializeTocClickHandlers = initializeTocClickHandlers;

})();