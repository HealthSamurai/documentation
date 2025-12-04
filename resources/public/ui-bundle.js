// ============================================================================
// UI BUNDLE - Consolidated JavaScript for all UI functionality
// ============================================================================

// Configure Prism.js to manual mode before it loads (prevents automatic highlighting)
window.Prism = window.Prism || {};
window.Prism.manual = true;

// ============================================================================
// TAB FUNCTIONALITY
// ============================================================================
function switchTab(button, tabIndex) {
  // Find the parent container using data attribute
  const tabContainer = button.closest('[data-tab-container]');
  if (!tabContainer) return;

  // Find all tab buttons in this container
  const tabButtons = tabContainer.querySelectorAll('button[data-tab]');

  // Update active tab button - toggle tab-active class
  tabButtons.forEach(btn => {
    if (parseInt(btn.getAttribute('data-tab')) === tabIndex) {
      btn.classList.add('tab-active');
    } else {
      btn.classList.remove('tab-active');
    }
  });

  // Update active tab content
  const tabContents = tabContainer.querySelectorAll('.tab-content');
  tabContents.forEach(content => {
    const contentTabIndex = parseInt(content.getAttribute('data-tab'));
    if (contentTabIndex === tabIndex) {
      content.classList.remove('hidden');
      content.classList.add('block');
    } else {
      content.classList.remove('block');
      content.classList.add('hidden');
    }
  });
}

// ============================================================================
// NAVIGATION FUNCTIONALITY
// ============================================================================
function updateActiveNavItem(pathname) {
  if (!pathname) return;

  // Remove all active classes first from left navigation
  document.querySelectorAll('#navigation .active').forEach(item => {
    item.classList.remove('active');
  });

  // Also handle mobile menu nav items
  document.querySelectorAll('.nav-item').forEach(item => {
    item.classList.remove('active', 'text-primary-9', 'bg-tint-2', 'border-primary-9');
    const icon = item.querySelector('.nav-icon');
    if (icon) {
      icon.classList.remove('text-primary-9');
    }
  });

  // Find all links in the left navigation
  const leftNavLinks = document.querySelectorAll('#navigation a[href]');
  let bestMatch = null;
  let bestMatchLength = 0;

  leftNavLinks.forEach(link => {
    const href = link.getAttribute('href');
    if (!href || href === '#') return;

    // Normalize the href to match pathname format
    const normalizedHref = href.replace(/\/$/, '');
    const normalizedPathname = pathname.replace(/\/$/, '');

    // Check for exact match
    if (normalizedPathname === normalizedHref) {
      bestMatch = link;
      bestMatchLength = normalizedHref.length;
    }
  });

  // Apply active state to the best match
  if (bestMatch) {
    bestMatch.classList.add('active');

    // Open all parent nav-item elements
    let parent = bestMatch.closest('.nav-item');
    while (parent) {
      parent.classList.add('open');
      const children = parent.querySelector('.nav-children');
      if (children) children.hidden = false;
      const button = parent.querySelector('.nav-summary');
      if (button) button.setAttribute('aria-expanded', 'true');
      parent = parent.parentElement ? parent.parentElement.closest('.nav-item') : null;
    }
  }

  // Also handle mobile menu nav items
  const navLinks = document.querySelectorAll('.nav-item[data-nav-path], .nav-item a[href]');
  bestMatch = null;
  bestMatchLength = 0;

  navLinks.forEach(item => {
    const linkElement = item.tagName === 'A' ? item : item.querySelector('a');
    if (!linkElement) return;

    const href = linkElement.getAttribute('href');
    if (!href || href === '#') return;

    // Normalize the href to match pathname format
    const normalizedHref = href.replace(/\/$/, '');
    const normalizedPathname = pathname.replace(/\/$/, '');

    // Check for exact match or prefix match
    if (normalizedPathname === normalizedHref ||
      normalizedPathname.startsWith(normalizedHref + '/')) {
      const matchLength = normalizedHref.length;
      if (matchLength > bestMatchLength) {
        bestMatch = item.classList.contains('nav-item') ? item : item.closest('.nav-item');
        bestMatchLength = matchLength;
      }
    }
  });

  // Apply active state to the best match
  if (bestMatch) {
    bestMatch.classList.add('active', 'text-primary-9', 'bg-tint-2', 'border-primary-9');
    const icon = bestMatch.querySelector('.nav-icon');
    if (icon) {
      icon.classList.add('text-primary-9');
    }
  }
}

// Scroll navigation to show active item
function scrollToActiveNavItem() {
  const navigation = document.getElementById('navigation');
  const activeItem = navigation ? navigation.querySelector('.active') : null;

  if (!navigation || !activeItem) return;

  // Get the position of the active item relative to the page
  const activeRect = activeItem.getBoundingClientRect();
  const navRect = navigation.getBoundingClientRect();

  // Calculate the active item's position relative to the navigation container
  const activeOffsetTop = activeItem.offsetTop;
  const activeHeight = activeRect.height;
  const navHeight = navRect.height;

  // Calculate the target scroll position to center the active item
  // We want the active item to be roughly in the middle of the visible area
  const targetScrollTop = activeOffsetTop - (navHeight / 2) + (activeHeight / 2);

  // Smooth scroll to the target position
  navigation.scrollTop = Math.max(0, targetScrollTop);
}

// Toggle navigation items on button click (chevron only, not links)
function initializeNavToggle() {
  document.addEventListener('click', function(e) {
    const button = e.target.closest('.nav-summary');
    if (!button) return;

    // Don't toggle if clicking on the link (let HTMX handle navigation)
    if (e.target.closest('a')) return;

    e.preventDefault();
    e.stopPropagation();

    const navItem = button.closest('.nav-item');
    const children = navItem.querySelector('.nav-children');
    if (!children) return;

    const isOpen = navItem.classList.contains('open');

    if (isOpen) {
      navItem.classList.remove('open');
      children.hidden = true;
      button.setAttribute('aria-expanded', 'false');
    } else {
      navItem.classList.add('open');
      children.hidden = false;
      button.setAttribute('aria-expanded', 'true');
    }
  });
}

// Make functions globally available
window.updateActiveNavItem = updateActiveNavItem;
window.scrollToActiveNavItem = scrollToActiveNavItem;
window.switchTab = switchTab;
window.initializeNavToggle = initializeNavToggle;

function updatePageTitle() {
  // Find the first h1 in the content
  const contentH1 = document.querySelector('#content h1, main h1, article h1, #blog-body h1');
  if (contentH1) {
    const h1Text = contentH1.textContent || contentH1.innerText;
    if (h1Text) {
      // Get the product name from meta tag or use a default
      const productMeta = document.querySelector('meta[property="og:site_name"]');
      const productName = productMeta ? productMeta.getAttribute('content') : 'Documentation';

      // Add fire emoji for article pages
      const path = window.location.pathname;
      const isArticle = path.startsWith('/articles/');
      const prefix = isArticle ? '🔥 ' : '';

      // Update the title
      document.title = `${prefix}${h1Text.trim()} | ${productName}`;
    }
  }
}

window.updatePageTitle = updatePageTitle;

// ============================================================================
// SCROLL TO ID FUNCTIONALITY
// ============================================================================
function scrollToIdFromHeader() {
  // First check meta tag for scroll-to-id
  const metaScrollToId = document.querySelector('meta[name="scroll-to-id"]');
  if (metaScrollToId) {
    const scrollToId = metaScrollToId.getAttribute('content');
    if (scrollToId) {
      setTimeout(() => {
        const element = document.getElementById(scrollToId);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      }, 100);
      return;
    }
  }

  // Check for HTMX response header
  if (window.htmx && window.htmx.lastXHR) {
    const scrollToId = window.htmx.lastXHR.getResponseHeader('Scroll-To-Id');
    if (scrollToId) {
      setTimeout(() => {
        const element = document.getElementById(scrollToId);
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      }, 100);
    }
  }
}

// ============================================================================
// HEADING LINKS FUNCTIONALITY
// ============================================================================
function initializeHeadingLinks() {
  const headings = document.querySelectorAll('h2[id], h3[id], h4[id], h5[id], h6[id]');

  headings.forEach(heading => {
    // Remove existing button if present to ensure fresh event listeners
    const existingButton = heading.querySelector('.heading-link-button');
    if (existingButton) {
      existingButton.remove();
    }

    // Skip h4 elements that are inside li elements
    if (heading.tagName.toLowerCase() === 'h4' && heading.closest('li')) {
      return;
    }

    // Check if heading contains an inline anchor tag with an id
    const inlineAnchor = heading.querySelector('a[id]');
    let id = heading.getAttribute('id');

    // If there's an inline anchor with an id, use that id for the link
    // This handles cases where the heading has both its own id and an inline anchor
    if (inlineAnchor) {
      const anchorId = inlineAnchor.getAttribute('id');
      if (anchorId) {
        id = anchorId; // Use the inline anchor's id for the button link
      }
    }

    if (!id) return;

    const button = document.createElement('button');
    button.className = 'heading-link-button';
    button.setAttribute('aria-label', 'Copy link to this section');

    // Add inline styles for positioning and hover
    button.style.cssText = `
      display: none;
      position: absolute;
      left: -2rem;
      top: 50%;
      transform: translateY(-50%);
      padding: 0.25rem;
      background: transparent;
      border: none;
      cursor: pointer;
      color: #9ca3af;
      transition: color 0.2s;
    `;

    // SVG icon for link
    button.innerHTML = `
      <svg class="heading-link-icon" width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1">
        </path>
      </svg>
    `;

    // Make heading relative positioned
    if (getComputedStyle(heading).position === 'static') {
      heading.style.position = 'relative';
    }

    // Show/hide button with delay to allow reaching it
    let hideTimeout;

    heading.addEventListener('mouseenter', function () {
      clearTimeout(hideTimeout);
      button.style.display = 'block';
    });

    heading.addEventListener('mouseleave', function () {
      hideTimeout = setTimeout(() => {
        button.style.display = 'none';
      }, 300);
    });

    // Button hover effect
    button.addEventListener('mouseenter', function () {
      clearTimeout(hideTimeout);
      this.style.color = '#D95640';
    });

    button.addEventListener('mouseleave', function () {
      this.style.color = '#9ca3af';
      hideTimeout = setTimeout(() => {
        button.style.display = 'none';
      }, 300);
    });

    button.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();

      // Scroll to the heading with offset for header
      const headerHeight = 64; // 4rem = 64px
      const rect = heading.getBoundingClientRect();
      const targetPosition = rect.top + window.scrollY - headerHeight;

      window.scrollTo({
        top: targetPosition,
        behavior: 'smooth'
      });

      // Update URL with the hash - use the id we determined earlier (might be from inline anchor)
      window.history.replaceState(null, '', '#' + id);
    });

    heading.appendChild(button);
  });
}

// ============================================================================
// MOBILE MENU FUNCTIONALITY
// ============================================================================
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

function initializeMobileMenu() {
  const mobileMenuButton = document.getElementById('mobile-menu-toggle');
  const mobileMenuOverlay = document.querySelector('.mobile-menu-overlay');

  if (mobileMenuButton) {
    mobileMenuButton.removeEventListener('click', toggleMobileMenu);
    mobileMenuButton.addEventListener('click', toggleMobileMenu);
  }

  if (mobileMenuOverlay) {
    mobileMenuOverlay.removeEventListener('click', closeMobileMenu);
    mobileMenuOverlay.addEventListener('click', closeMobileMenu);
  }

  // Close menu when clicking on a navigation link
  const navigation = document.getElementById('navigation');
  if (navigation) {
    navigation.addEventListener('click', function(e) {
      if (e.target.closest('a')) {
        setTimeout(() => {
          closeMobileMenu();
        }, 100);
      }
    });
  }
}

// ============================================================================
// ELLIPSIS MENU FUNCTIONALITY
// ============================================================================
function initializeEllipsisMenu() {
  const ellipsisToggle = document.getElementById('ellipsis-menu-toggle');
  const ellipsisDropdown = document.getElementById('ellipsis-dropdown');

  if (!ellipsisToggle || !ellipsisDropdown) return;

  ellipsisToggle.addEventListener('click', function (e) {
    e.stopPropagation();
    toggleEllipsisMenu();
  });

  document.addEventListener('click', function (e) {
    if (!ellipsisDropdown.contains(e.target) && !ellipsisToggle.contains(e.target)) {
      closeEllipsisMenu();
    }
  });
}

function toggleEllipsisMenu() {
  const ellipsisDropdown = document.getElementById('ellipsis-dropdown');
  if (ellipsisDropdown) {
    const isOpen = !ellipsisDropdown.classList.contains('hidden');
    if (isOpen) {
      closeEllipsisMenu();
    } else {
      openEllipsisMenu();
    }
  }
}

function openEllipsisMenu() {
  const ellipsisDropdown = document.getElementById('ellipsis-dropdown');
  if (ellipsisDropdown) {
    ellipsisDropdown.classList.remove('hidden');
  }
}

function closeEllipsisMenu() {
  const ellipsisDropdown = document.getElementById('ellipsis-dropdown');
  if (ellipsisDropdown) {
    ellipsisDropdown.classList.add('hidden');
  }
}

// ============================================================================
// PAGE ACTIONS DROPDOWN FUNCTIONALITY
// ============================================================================
function initializePageActionsDropdown() {
  const toggle = document.getElementById('page-actions-toggle');
  const dropdown = document.getElementById('page-actions-dropdown');

  if (!toggle || !dropdown) return;

  // Skip if already initialized (check for data attribute)
  if (toggle.dataset.initialized) {
    console.log('already initialized, skipping');
    return;
  }
  toggle.dataset.initialized = 'true';
  console.log('initializing toggle');

  // Click handler for toggle
  toggle.addEventListener('click', function (e) {
    console.log('click handler fired');
    e.stopPropagation();
    togglePageActionsDropdown();
    console.log('dropdown toggled');
  });

  // Remove old document click handler if exists
  if (window._pageActionsClickHandler) {
    document.removeEventListener('click', window._pageActionsClickHandler);
  }

  // Create new click handler for closing when clicking outside
  window._pageActionsClickHandler = function (e) {
    const currentDropdown = document.getElementById('page-actions-dropdown');
    const currentToggle = document.getElementById('page-actions-toggle');
    console.log('document click handler, target:', e.target, 'inDropdown:', currentDropdown?.contains(e.target), 'inToggle:', currentToggle?.contains(e.target));
    if (currentDropdown && currentToggle) {
      if (!currentDropdown.contains(e.target) && !currentToggle.contains(e.target)) {
        console.log('closing from document handler');
        closePageActionsDropdown();
      }
    }
  };

  // Close dropdown when clicking outside
  document.addEventListener('click', window._pageActionsClickHandler);
}

function togglePageActionsDropdown() {
  const dropdown = document.getElementById('page-actions-dropdown');
  if (dropdown) {
    const isOpen = !dropdown.classList.contains('hidden');
    console.log('togglePageActionsDropdown: isOpen=', isOpen);
    if (isOpen) {
      console.log('closing dropdown');
      closePageActionsDropdown();
    } else {
      console.log('opening dropdown');
      openPageActionsDropdown();
    }
    console.log('after toggle, hidden=', dropdown.classList.contains('hidden'));
  }
}

function openPageActionsDropdown() {
  const dropdown = document.getElementById('page-actions-dropdown');
  if (dropdown) {
    dropdown.classList.remove('hidden');
  }
}

function closePageActionsDropdown() {
  const dropdown = document.getElementById('page-actions-dropdown');
  if (dropdown) {
    dropdown.classList.add('hidden');
  }
}

function trackPageAction(action, targetUrl) {
  // Track action in PostHog
  if (typeof posthog !== 'undefined') {
    posthog.capture('docs_copy_page', {
      action: action,
      page_url: window.location.href,
      target_url: targetUrl
    });
  }
}

function openInChatGPT() {
  const fullUrl = window.location.origin + window.location.pathname;
  const prompt = "Read " + fullUrl + " and answer questions about the content.";
  const chatgptUrl = "https://chat.openai.com/?q=" + encodeURIComponent(prompt);

  // Track in PostHog
  trackPageAction('open_chatgpt', chatgptUrl);

  // Open in new tab
  window.open(chatgptUrl, '_blank');
}

function openInClaude() {
  const fullUrl = window.location.origin + window.location.pathname;
  const prompt = "Read from " + fullUrl + " so I can ask questions about it.";
  const claudeUrl = "https://claude.ai/new?q=" + encodeURIComponent(prompt);

  // Track in PostHog
  trackPageAction('open_claude', claudeUrl);

  // Open in new tab
  window.open(claudeUrl, '_blank');
}

function copyPageAsMarkdown(button) {
  const mdUrl = button.getAttribute('data-md-url');
  if (!mdUrl) {
    console.error('No markdown URL found');
    return;
  }

  // Track copy action in PostHog
  if (typeof posthog !== 'undefined') {
    posthog.capture('docs_copy_page', {
      action: 'copy_page',
      page_url: window.location.href,
      md_url: mdUrl
    });
  }

  // Fetch markdown content
  fetch(mdUrl)
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to fetch markdown');
      }
      return response.text();
    })
    .then(markdown => {
      // Copy to clipboard
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(markdown).then(() => {
          showCopySuccessForButton(button);
          closePageActionsDropdown();
        }).catch(err => {
          console.error('Failed to copy:', err);
        });
      }
    })
    .catch(err => {
      console.error('Failed to fetch markdown:', err);
    });
}

function showCopySuccessForButton(button) {
  const originalHTML = button.innerHTML;

  // Check if this is the main split button (has a span with text)
  const textSpan = button.querySelector('span:last-child');
  if (textSpan && button.id === 'copy-page-button') {
    // For split button, only change the text span
    const originalTextContent = textSpan.textContent;
    textSpan.textContent = 'Copied!';
    setTimeout(() => {
      textSpan.textContent = originalTextContent;
    }, 2000);
  } else {
    // For dropdown menu item, replace entire content
    button.innerHTML = '<span class="text-sm">Copied!</span>';
    setTimeout(() => {
      button.innerHTML = originalHTML;
    }, 2000);
  }
}

// ============================================================================
// SEARCH FUNCTIONALITY (Meilisearch)
// ============================================================================
function initializeSearch() {
  const mobileSearchToggle = document.getElementById('mobile-search-toggle');
  const mobileSearchContainer = document.getElementById('mobile-search-container');
  const mobileSearchClose = document.getElementById('mobile-search-close');
  const meilisearchInput = document.getElementById('meilisearch-input');
  const mobileMeilisearchInput = document.getElementById('mobile-meilisearch-input');

  if (!mobileSearchToggle || !mobileSearchContainer) return;

  // Toggle mobile search
  mobileSearchToggle.addEventListener('click', function (e) {
    e.preventDefault();
    e.stopPropagation();
    mobileSearchContainer.classList.remove('hidden');
    if (mobileMeilisearchInput) {
      mobileMeilisearchInput.focus();
      // Copy value from desktop search if it has content
      if (meilisearchInput && meilisearchInput.value) {
        mobileMeilisearchInput.value = meilisearchInput.value;
      }
    }
  });

  // Close mobile search
  if (mobileSearchClose) {
    mobileSearchClose.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();
      mobileSearchContainer.classList.add('hidden');
      // Copy value back to desktop search
      if (mobileMeilisearchInput && meilisearchInput) {
        meilisearchInput.value = mobileMeilisearchInput.value;
      }
    });
  }

  // Keep mobile and desktop search in sync
  if (mobileMeilisearchInput && meilisearchInput) {
    mobileMeilisearchInput.addEventListener('input', function () {
      meilisearchInput.value = this.value;
      // Trigger input event on desktop search to update results
      meilisearchInput.dispatchEvent(new Event('input', { bubbles: true }));
    });

    meilisearchInput.addEventListener('input', function () {
      mobileMeilisearchInput.value = this.value;
    });
  }

  // Close on escape key
  if (mobileMeilisearchInput) {
    mobileMeilisearchInput.addEventListener('keydown', function (e) {
      if (e.key === 'Escape') {
        mobileSearchContainer.classList.add('hidden');
      }
    });
  }

  // Handle Cmd/Ctrl + K for search
  document.addEventListener('keydown', function (e) {
    if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
      e.preventDefault();
      if (meilisearchInput) {
        meilisearchInput.focus();
      }
    }
  });
}

// ============================================================================
// COPY CODE FUNCTIONALITY
// ============================================================================
function initializeCopyButtons() {
  // Find all pre elements that contain code blocks
  const preElements = document.querySelectorAll('pre');

  preElements.forEach((pre, index) => {
    // Skip if already has copy button
    if (pre.querySelector('button[data-copy-code]')) {
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
    copyButton.setAttribute('data-copy-code', 'true');
    copyButton.className = 'copy-code-btn';
    copyButton.innerHTML = 'Copy';
    copyButton.title = 'Copy code';

    // Add click handler
    copyButton.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();

      const textToCopy = codeBlock.textContent;

      // Use modern clipboard API if available
      if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(textToCopy).then(function () {
          showCopySuccess(copyButton);
        }).catch(function (err) {
          console.error('Failed to copy: ', err);
        });
      }
    });

    // Make pre container relative for absolute positioning
    pre.style.position = 'relative';

    // Add hover effect to show button (only on desktop)
    pre.addEventListener('mouseenter', function () {
      if (window.innerWidth > 768) {
        copyButton.style.display = 'block';
        copyButton.style.opacity = '1';
      }
    });

    pre.addEventListener('mouseleave', function () {
      if (window.innerWidth > 768) {
        copyButton.style.opacity = '0';
        setTimeout(() => {
          if (copyButton.style.opacity === '0') {
            copyButton.style.display = 'none';
          }
        }, 200);
      }
    });

    // Keep button visible on button hover
    copyButton.addEventListener('mouseenter', function () {
      if (window.innerWidth > 768) {
        copyButton.style.opacity = '1';
      }
    });

    // Show button on mobile tap
    pre.addEventListener('touchstart', function () {
      copyButton.style.display = 'block';
      copyButton.style.opacity = '1';
      setTimeout(() => {
        copyButton.style.opacity = '0';
        setTimeout(() => {
          copyButton.style.display = 'none';
        }, 200);
      }, 3000);
    }, { passive: true });

    // Insert button into pre element
    pre.appendChild(copyButton);
  });
}

function showCopySuccess(button) {
  const originalText = button.innerHTML;
  button.innerHTML = 'Copied!';

  setTimeout(() => {
    button.innerHTML = originalText;
  }, 2000);
}

// ============================================================================
// UNIFIED CONTENT RENDERER
// Handles HTMX content updates, Mermaid, KaTeX, TOC, and more
// ============================================================================
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
  window.filterFeatures = function (searchTerm) {
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

  // Update last updated timestamp
  function updateLastUpdated() {
    const element = document.getElementById('lastupdated');
    if (element) {
      const updatedAt = element.getAttribute('data-updated-at');
      if (updatedAt) {
        const date = new Date(updatedAt);
        const now = new Date();
        const diffMs = now - date;
        const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
        const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
        const diffMinutes = Math.floor(diffMs / (1000 * 60));

        let relativeTime;
        if (diffMinutes < 1) {
          relativeTime = 'just now';
        } else if (diffMinutes < 60) {
          relativeTime = `${diffMinutes} minute${diffMinutes === 1 ? '' : 's'} ago`;
        } else if (diffHours < 24) {
          relativeTime = `${diffHours} hour${diffHours === 1 ? '' : 's'} ago`;
        } else if (diffDays < 7) {
          relativeTime = `${diffDays} day${diffDays === 1 ? '' : 's'} ago`;
        } else if (diffDays < 30) {
          const weeks = Math.floor(diffDays / 7);
          relativeTime = `${weeks} week${weeks === 1 ? '' : 's'} ago`;
        } else if (diffDays < 365) {
          const months = Math.floor(diffDays / 30);
          relativeTime = `${months} month${months === 1 ? '' : 's'} ago`;
        } else {
          const years = Math.floor(diffDays / 365);
          relativeTime = `${years} year${years === 1 ? '' : 's'} ago`;
        }

        element.textContent = relativeTime;
      }
    }
  }

  // Make updateLastUpdated globally available
  window.updateLastUpdated = updateLastUpdated;

  // Load Mermaid library dynamically
  function loadMermaid(callback) {
    if (libraries.mermaid.loaded || libraries.mermaid.loading) {
      if (callback && libraries.mermaid.loaded) callback();
      return;
    }

    libraries.mermaid.loading = true;

    const script = document.createElement('script');
    script.src = '/docs/static/mermaid.min.js';

    script.onload = function () {
      libraries.mermaid.loaded = true;
      libraries.mermaid.loading = false;

      // Initialize mermaid with correct theme based on current mode
      if (typeof mermaid !== 'undefined') {
        const isDark = document.documentElement.classList.contains('dark');
        const config = isDark ? window.MERMAID_CONFIG_DARK : window.MERMAID_CONFIG;
        if (config) {
          mermaid.initialize(config);
        }
      }

      if (callback) callback();
    };

    script.onerror = function () {
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

    libraries.katex.loading = true;

    // Load CSS first
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = '/docs/static/katex.min.css';
    document.head.appendChild(link);

    // Load JS
    const script = document.createElement('script');
    script.src = '/docs/static/katex.min.js';

    script.onload = function () {
      libraries.katex.loaded = true;
      libraries.katex.loading = false;
      if (callback) callback();
    };

    script.onerror = function () {
      libraries.katex.loading = false;
      console.error('[Content Renderer] Failed to load KaTeX');
    };

    document.head.appendChild(script);
  }

  // Render code highlighting with Prism.js
  function renderCodeHighlighting(container) {
    if (typeof Prism !== 'undefined') {
      // Find unprocessed code blocks
      const codeBlocks = container.querySelectorAll('pre code:not([data-prism-processed])');
      codeBlocks.forEach(block => {
        const pre = block.parentElement;

        // Remove language class from pre if it exists (Prism adds it automatically)
        if (pre && pre.className) {
          pre.className = pre.className.replace(/\blanguage-\S+/g, '').trim();
        }

        // Highlight with Prism (it will add language class to pre automatically)
        Prism.highlightElement(block);
        block.setAttribute('data-prism-processed', 'true');
      });

      // Initialize copy buttons after highlighting
      setTimeout(initializeCopyButtons, 50);
    }
  }


  // Render Mermaid diagrams
  function renderMermaid(container) {
    if (typeof mermaid === 'undefined') return;

    // Find unprocessed mermaid blocks
    const mermaidBlocks = container.querySelectorAll('pre.mermaid:not([data-mermaid-processed])');

    mermaidBlocks.forEach((element, index) => {
      // Save original code for theme switching (before processing)
      if (!element.hasAttribute('data-original-code')) {
        element.setAttribute('data-original-code', element.textContent || element.innerText);
      }

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
    window._tocScrollHandler = function () {
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

    const headerHeight = 64; // 4rem = 64px
    const scrollTop = window.scrollY;

    let currentSection = null;

    // Special case: if we're at the exact position from a click
    // Check which heading is at the top of the viewport (below header)
    for (let i = 0; i < headings.length; i++) {
      const heading = headings[i];
      const rect = heading.getBoundingClientRect();

      // If heading is at or just below the header, it's our active section
      if (rect.top >= headerHeight - 5 && rect.top <= headerHeight + 15) {
        currentSection = heading.id;
        break;
      }
    }

    // If no exact match, find the last heading that's above the viewport
    if (!currentSection) {
      const viewportTop = scrollTop + headerHeight + 5;

      for (let i = headings.length - 1; i >= 0; i--) {
        const heading = headings[i];
        const rect = heading.getBoundingClientRect();
        const headingTop = rect.top + scrollTop;

        if (headingTop <= viewportTop) {
          currentSection = heading.id;
          break;
        }
      }
    }

    // If we're at the very top of the page, select the first heading
    if (!currentSection && scrollTop < 10 && headings.length > 0) {
      currentSection = headings[0].id;
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

      link.addEventListener('click', function (e) {
        const href = this.getAttribute('href');
        if (href && href.startsWith('#')) {
          e.preventDefault();
          const targetId = href.substring(1);
          const targetElement = document.getElementById(targetId);

          if (targetElement) {
            const headerHeight = 64; // 4rem = 64px
            // Position the heading exactly at the header line
            const rect = targetElement.getBoundingClientRect();
            const targetPosition = rect.top + window.scrollY - headerHeight;

            // Update URL with the hash
            if (history.pushState) {
              history.pushState(null, null, href);
            } else {
              window.location.hash = targetId;
            }

            // Smooth scroll with animation (300ms ease-out)
            window.scrollTo({
              top: targetPosition,
              behavior: 'smooth'
            });

            // Force immediate update of active section
            requestAnimationFrame(() => {
              updateActiveTocSection();
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

    // Always render code highlighting (Prism.js or highlight.js)
    renderCodeHighlighting(target);

    // Initialize TOC if on full page
    if (target === document.body || target === document) {
      initializeTocScrollSpy();
      initializeTocClickHandlers();
    }

    // Re-initialize page actions dropdown after HTMX content updates
    initializePageActionsDropdown();
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

  // Export functions for global access
  window.renderContent = processContent;
  window.updateActiveTocSection = updateActiveTocSection;
  window.initializeTocScrollSpy = initializeTocScrollSpy;
  window.initializeTocClickHandlers = initializeTocClickHandlers;

  // ============================================================================
  // CONSOLIDATED EVENT LISTENERS
  // ============================================================================

  // Main initialization using htmx.onLoad (HTMX best practice)
  if (typeof htmx !== 'undefined') {
    // This runs on initial page load and after every HTMX swap
    htmx.onLoad(function (target) {
      // Process the newly loaded content
      processContent(target);
      // Update last updated timestamp after content load
      updateLastUpdated();
      // Initialize heading links for new content
      initializeHeadingLinks();
    });

    // Clean up before element removal
    htmx.on('htmx:beforeCleanupElement', function (evt) {
      cleanupElement(evt.detail.elt);
    });

    // Additional handler for afterSettle to catch any missed content
    htmx.on('htmx:afterSettle', function (evt) {
      // Re-initialize TOC handlers after settle
      setTimeout(() => {
        initializeTocClickHandlers();
        // Update last updated timestamp after settle
        updateLastUpdated();
      }, 50);
    });
  }

  // Single DOMContentLoaded handler for all initialization
  document.addEventListener('DOMContentLoaded', function () {
    // Clear HTMX localStorage cache
    try {
      for (let i = localStorage.length - 1; i >= 0; i--) {
        const key = localStorage.key(i);
        if (key && key.startsWith('htmx-')) {
          localStorage.removeItem(key);
        }
      }
    } catch (e) {
      console.warn('Could not clear HTMX cache:', e);
    }

    // Navigation and UI initialization
    if (document.body) {
      updateActiveNavItem(window.location.pathname);
      updatePageTitle();
      // Scroll to active item only on initial page load (not HTMX navigation)
      scrollToActiveNavItem();
    }

    // Initialize all UI components
    scrollToIdFromHeader();
    initializeHeadingLinks();
    initializeMobileMenu();
    initializeEllipsisMenu();
    initializePageActionsDropdown();
    initializeSearch();
    initializeCopyButtons();
    initializeNavToggle();

    // Process content if HTMX not available
    if (typeof htmx === 'undefined') {
      processContent(document.body);
    }

    // Update last updated timestamp on initial load
    updateLastUpdated();

    // Update every minute to keep relative time fresh
    setInterval(updateLastUpdated, 60000);
  });

  // Single HTMX afterSwap handler for all updates
  document.addEventListener('htmx:afterSwap', function (evt) {
    // Store the XHR object for later access
    if (evt.detail && evt.detail.xhr) {
      window.htmx = window.htmx || {};
      window.htmx.lastXHR = evt.detail.xhr;
    }

    // Small delay to ensure DOM is fully updated
    setTimeout(() => {
      updateActiveNavItem(window.location.pathname);
      updatePageTitle();
      scrollToIdFromHeader();
      initializeHeadingLinks();
      initializeMobileMenu();
      initializeCopyButtons();
    }, 10);
  });

  // Handle browser back/forward navigation
  window.addEventListener('popstate', function () {
    updateActiveNavItem(window.location.pathname);
    setTimeout(() => {
      processContent(document.body);
      updateLastUpdated();
    }, 10);
  });

  // Handle window resize
  window.addEventListener('resize', function () {
    // Close mobile menu on desktop resize
    if (window.innerWidth > 768) {
      closeMobileMenu();
    }
  });

  // Set Prism to manual mode immediately (before it loads)
  // This prevents automatic highlighting on DOMContentLoaded
  window.Prism = window.Prism || {};
  window.Prism.manual = true;

  // Export renderMermaid for theme switching
  window.renderMermaid = function() {
    renderMermaid(document.body);
  };
})();

// ============================================================================
// FIX FOR CTRL/CMD/SHIFT+CLICK ON HTMX NAVIGATION LINKS
// ============================================================================
(function() {
  'use strict';

  // Fix for Ctrl/Cmd/Shift+click on HTMX navigation links
  document.addEventListener('htmx:configRequest', function(evt) {
    const triggeringEvent = evt.detail.triggeringEvent;
    const target = evt.detail.elt;

    // Only handle navigation links
    if (target && target.closest('#navigation')) {
      const href = target.getAttribute('href');

      if (href && triggeringEvent) {
        // Ctrl/Cmd+click - open in new tab
        if (triggeringEvent.ctrlKey || triggeringEvent.metaKey) {
          evt.preventDefault();
          window.open(href, '_blank');
        }
        // Shift+click - open in new window
        else if (triggeringEvent.shiftKey) {
          evt.preventDefault();
          window.open(href, '_blank', 'noopener,noreferrer');
        }
      }
    }
  });
})();

// ============================================================================
// THEME MANAGEMENT (DARK/LIGHT MODE)
// ============================================================================
(function() {
  'use strict';

  function toggleTheme() {
    const html = document.documentElement;
    const isDark = html.classList.contains('dark');

    if (isDark) {
      html.classList.remove('dark');
      localStorage.setItem('theme', 'light');
    } else {
      html.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    }

    updateThemeIcon();
    updatePrismTheme();
    updateMermaidTheme();
  }

  function updateThemeIcon() {
    const isDark = document.documentElement.classList.contains('dark');
    const sunIcon = document.querySelector('#sun-icon');
    const moonIcon = document.querySelector('#moon-icon');

    if (sunIcon && moonIcon) {
      if (isDark) {
        sunIcon.style.display = 'block';
        moonIcon.style.display = 'none';
      } else {
        sunIcon.style.display = 'none';
        moonIcon.style.display = 'block';
      }
    }
  }

  function updatePrismTheme() {
    const isDark = document.documentElement.classList.contains('dark');
    const prismLink = document.getElementById('prism-theme');

    if (prismLink) {
      const currentHref = prismLink.href;
      const basePath = currentHref.replace(/prism(-dark)?\.css.*$/, '');
      const newHref = isDark ? basePath + 'prism-dark.css' : basePath + 'prism.css';

      if (prismLink.href !== newHref) {
        prismLink.href = newHref;
      }
    }
  }

  function updateMermaidTheme() {
    if (typeof mermaid === 'undefined') return;

    const isDark = document.documentElement.classList.contains('dark');
    const config = isDark ? window.MERMAID_CONFIG_DARK : window.MERMAID_CONFIG;

    // Find all processed diagrams
    const elements = document.querySelectorAll('pre.mermaid[data-original-code]');

    if (elements.length === 0) return;

    // Restore original code
    elements.forEach(element => {
      const originalCode = element.getAttribute('data-original-code');
      if (originalCode) {
        // Remove SVG container if exists
        const container = element.nextElementSibling;
        if (container && container.classList.contains('mermaid-container')) {
          container.remove();
        }

        // Restore original text
        element.textContent = originalCode;
        element.style.display = '';
        element.removeAttribute('data-mermaid-processed');
      }
    });

    // Reinitialize with new theme
    if (config) {
      mermaid.initialize(config);
    }

    // Re-render
    if (typeof window.renderMermaid === 'function') {
      window.renderMermaid();
    }
  }

  // Initialize on page load
  document.addEventListener('DOMContentLoaded', function() {
    updateThemeIcon();
    updatePrismTheme();
    // Don't call updateMermaidTheme on initial load - Mermaid will initialize
    // with correct theme when it loads (see loadMermaid function)
  });

  // Export toggleTheme to window for use in HTML onclick handlers
  window.toggleTheme = toggleTheme;
})();

// ============================================================================
// COOKIE CONSENT BANNER
// ============================================================================
(function() {
  'use strict';

  function showCookieBanner() {
    const banner = document.getElementById('cookie-consent-banner');
    if (banner) {
      console.log('[Cookie Banner] Showing banner');
      banner.classList.remove('hidden');
      banner.style.display = 'block';
    } else {
      console.error('[Cookie Banner] Banner element not found in DOM!');
    }
  }

  function hideCookieBanner() {
    const banner = document.getElementById('cookie-consent-banner');
    if (banner) {
      console.log('[Cookie Banner] Hiding banner');
      banner.classList.add('hidden');
      banner.style.display = 'none';
    }
  }

  function handleAcceptCookies() {
    try {
      // Set consent flag in localStorage
      localStorage.setItem('cookie_consent', 'accepted');

      // PostHog: Switch from memory-only to full persistence with cookies
      if (typeof posthog !== 'undefined') {
        console.log('[PostHog] Switching to full tracking with cookies');
        posthog.set_config({ persistence: 'localStorage+cookie' });
        posthog.opt_in_capturing();
      }

      // Update Google Consent Mode to granted for full tracking
      if (typeof gtag !== 'undefined') {
        gtag('consent', 'update', {
          'analytics_storage': 'granted',
          'ad_storage': 'granted'
        });
      }

      hideCookieBanner();

      // Dynamically load PushEngage using centralized function
      loadPushEngage();

      // No reload needed - changes applied immediately
    } catch (error) {
      console.error('[Cookie Banner] Error accepting cookies:', error);
    }
  }

  function handleRejectCookies() {
    try {
      // Set reject flag in localStorage
      localStorage.setItem('cookie_consent', 'rejected');

      // PostHog: Switch to memory-only persistence (no cookies, anonymous tracking)
      if (typeof posthog !== 'undefined') {
        console.log('[PostHog] Switching to anonymous tracking (memory-only)');
        posthog.set_config({ persistence: 'memory' });
        // Don't call opt_out_capturing() - we still want anonymous events!
      }

      // Update Google Consent Mode to explicitly denied
      // This tells GTM that user actively rejected (not just waiting for decision)
      // GTM will send cookieless pings for behavioral modeling
      if (typeof gtag !== 'undefined') {
        gtag('consent', 'update', {
          'analytics_storage': 'denied',
          'ad_storage': 'denied'
        });
        console.log('[GTM] Consent explicitly denied - cookieless pings enabled for modeling');
      }

      hideCookieBanner();

      // No reload needed - anonymous tracking continues immediately
    } catch (error) {
      console.error('[Cookie Banner] Error rejecting cookies:', error);
    }
  }

  // EU + EEA countries + UK where GDPR applies
  var EU_COUNTRIES = [
    'AT', 'BE', 'BG', 'HR', 'CY', 'CZ', 'DK', 'EE', 'FI', 'FR',
    'DE', 'GR', 'HU', 'IE', 'IT', 'LV', 'LT', 'LU', 'MT', 'NL',
    'PL', 'PT', 'RO', 'SK', 'SI', 'ES', 'SE',
    'GB', // UK
    'IS', 'LI', 'NO', // EEA
    'CH' // Switzerland (has similar privacy laws)
  ];

  function handleCountryDetection(country) {
    if (country && EU_COUNTRIES.indexOf(country) === -1) {
      // Non-EU user - auto accept without showing banner
      console.log('[Cookie Banner] Non-EU user (' + country + ') - auto-accepting cookies');

      // Set consent to accepted
      localStorage.setItem('cookie_consent', 'accepted');

      // Update Google Consent Mode
      if (typeof gtag !== 'undefined') {
        gtag('consent', 'update', {
          'analytics_storage': 'granted',
          'ad_storage': 'granted'
        });
        console.log('[GTM] Consent auto-granted for non-EU user');
      }

      // Enable PostHog tracking (already in localStorage+cookie mode by default)
      if (typeof posthog !== 'undefined') {
        posthog.opt_in_capturing();
        console.log('[PostHog] Enabled tracking for non-EU user');
      }

      // Hide banner
      hideCookieBanner();

      // Load PushEngage
      loadPushEngage();

    } else {
      // EU user or unknown - show banner
      console.log('[Cookie Banner] EU user (' + (country || 'unknown') + ') - showing banner');
      showCookieBanner();
    }
  }

  function checkGeoAndConsent() {
    try {
      // Check if user already made a choice
      var existingChoice = localStorage.getItem('cookie_consent');
      if (existingChoice === 'accepted' || existingChoice === 'rejected') {
        // User already decided - hide banner and load PushEngage if accepted
        console.log('[Cookie Banner] User already made choice:', existingChoice);
        hideCookieBanner();
        if (existingChoice === 'accepted') {
          loadPushEngage();
        }
        return;
      }

      // No choice yet - check geo location
      console.log('[Cookie Banner] No prior consent, checking geo location...');

      fetch('https://cloudflare.com/cdn-cgi/trace')
        .then(function(response) { return response.text(); })
        .then(function(data) {
          var countryMatch = data.match(/loc=([A-Z]{2})/);
          var country = countryMatch ? countryMatch[1] : null;
          handleCountryDetection(country);
        })
        .catch(function(error) {
          // Geo detection failed - show banner (safe default for GDPR compliance)
          console.error('[Cookie Banner] Geo detection failed, showing banner as safe default:', error);
          showCookieBanner();
        });

    } catch (error) {
      // Any error - show banner (safe default)
      console.error('[Cookie Banner] Error in geo check, showing banner:', error);
      showCookieBanner();
    }
  }

  // Initialize on page load with geo-targeting
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', checkGeoAndConsent);
  } else {
    checkGeoAndConsent();
  }

  function loadPushEngage() {
    // Only load if not already loaded
    if (window.PushEngage) {
      console.log('[PushEngage] Already loaded, skipping');
      return;
    }

    try {
      console.log('[PushEngage] Loading PushEngage SDK...');
      (function(w, d) {
        w.PushEngage = w.PushEngage || [];
        w._peq = w._peq || [];
        PushEngage.push(['init', { appId: '795c4eea-7a69-42d7-bff3-882774303fcf' }]);
        var e = d.createElement('script');
        e.src = 'https://clientcdn.pushengage.com/sdks/pushengage-web-sdk.js';
        e.async = true;
        e.type = 'text/javascript';
        d.head.appendChild(e);
        console.log('[PushEngage] SDK script tag inserted');
      })(window, document);
    } catch (error) {
      console.error('[PushEngage] Error loading SDK:', error);
    }
  }

  function showCookieSettings() {
    // Clear consent choice and show banner again
    localStorage.removeItem('cookie_consent');
    showCookieBanner();
  }

  // Export functions to window for use in onclick handlers and server-side code
  window.handleAcceptCookies = handleAcceptCookies;
  window.handleRejectCookies = handleRejectCookies;
  window.showCookieSettings = showCookieSettings;
  window.loadPushEngage = loadPushEngage;
})();
