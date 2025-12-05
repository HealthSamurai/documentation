// ============================================================================
// LINK PREVIEW FUNCTIONALITY
// Shows tooltip with title, description, and breadcrumbs on link hover
// ============================================================================
(function() {
  'use strict';

  // Cache for preview data (persists across HTMX navigations)
  const previewsCache = new Map();
  let batchLoading = false;
  let previewTooltip = null;
  let hoverTimeout = null;
  let currentLink = null;

  // Get product path from current URL (e.g., /docs/aidbox from /docs/aidbox/getting-started)
  function getProductPath() {
    const path = window.location.pathname;
    // Match first two path segments: /docs/aidbox, /docs/fhirbase, etc.
    const match = path.match(/^(\/[^\/]+\/[^\/]+)/);
    return match ? match[1] : '/docs/aidbox';
  }

  // Check if href is an internal link (including anchor links to other pages)
  function isInternalLink(href) {
    if (!href) return false;
    if (href.startsWith('http://') || href.startsWith('https://')) return false;
    if (href.startsWith('mailto:')) return false;
    if (href.startsWith('tel:')) return false;
    if (href.startsWith('javascript:')) return false;
    // Pure anchor links (#section) on same page - skip
    if (href.startsWith('#')) return false;
    // Skip .md links (used by "Copy Page" button)
    if (href.endsWith('.md')) return false;
    return true;
  }

  // Check if link is anchor-only (jumps to section on same page)
  function isAnchorOnlyLink(href) {
    return href && href.startsWith('#');
  }

  // Extract base URL and anchor from href
  function parseHref(href) {
    const hashIndex = href.indexOf('#');
    if (hashIndex > 0) {
      return {
        baseUrl: href.substring(0, hashIndex),
        anchor: href.substring(hashIndex + 1)
      };
    }
    return { baseUrl: href, anchor: null };
  }

  // Format anchor text for display (kebab-case to Title Case)
  function formatAnchorText(anchor) {
    if (!anchor) return '';
    return anchor
      .replace(/-/g, ' ')
      .replace(/\b\w/g, c => c.toUpperCase());
  }

  // Collect all internal hrefs from #content that are not yet cached
  // For anchor links (/page#section), we cache by base URL
  function collectInternalHrefs() {
    const content = document.getElementById('content');
    if (!content) return [];

    const links = content.querySelectorAll('a[href]');
    const hrefs = new Set();

    links.forEach(link => {
      const href = link.getAttribute('href');
      if (isInternalLink(href)) {
        const { baseUrl } = parseHref(href);
        if (!previewsCache.has(baseUrl)) {
          hrefs.add(baseUrl);
        }
      }
    });

    return Array.from(hrefs);
  }

  // Load previews from server
  async function loadBatchPreviews(hrefs) {
    if (batchLoading || hrefs.length === 0) return;

    batchLoading = true;
    const productPath = getProductPath();

    try {
      const response = await fetch(`${productPath}/batch-previews`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ hrefs: hrefs })
      });

      if (response.ok) {
        const data = await response.json();
        Object.entries(data).forEach(([href, preview]) => {
          previewsCache.set(href, preview);
        });
      }
    } catch (error) {
      // Silently fail - previews are nice-to-have
    } finally {
      batchLoading = false;
    }
  }

  // Schedule previews load using requestIdleCallback
  function schedulePreviewsLoad() {
    const hrefs = collectInternalHrefs();
    if (hrefs.length === 0) return;

    if ('requestIdleCallback' in window) {
      requestIdleCallback(() => {
        loadBatchPreviews(hrefs);
      }, { timeout: 2000 });
    } else {
      // Fallback for Safari
      setTimeout(() => {
        loadBatchPreviews(hrefs);
      }, 100);
    }
  }

  // Create tooltip element if not exists
  function getOrCreateTooltip() {
    if (!previewTooltip) {
      previewTooltip = document.createElement('div');
      previewTooltip.id = 'link-preview-tooltip';
      previewTooltip.className = 'fixed z-[9999] hidden pointer-events-none';
      previewTooltip.style.cssText = 'transition: opacity 0.15s ease-out;';
      document.body.appendChild(previewTooltip);
    }
    return previewTooltip;
  }

  // Link icon SVG (top-right corner)
  const linkIconSvg = '<svg class="w-4 h-4 text-gray-400 dark:text-gray-500 shrink-0" viewBox="0 0 17 17" fill="none"><path d="M10.4 2.5H14.4V6.5" stroke="currentColor" stroke-width="1.25" stroke-linecap="round" stroke-linejoin="round"/><path d="M7.07 9.83L14.4 2.5" stroke="currentColor" stroke-width="1.25" stroke-linecap="round" stroke-linejoin="round"/><path d="M12.4 9.17V13.17C12.4 13.52 12.26 13.86 12.01 14.11C11.76 14.36 11.42 14.5 11.07 14.5H3.73C3.38 14.5 3.04 14.36 2.79 14.11C2.54 13.86 2.4 13.52 2.4 13.17V5.83C2.4 5.48 2.54 5.14 2.79 4.89C3.04 4.64 3.38 4.5 3.73 4.5H7.73" stroke="currentColor" stroke-width="1.25" stroke-linecap="round" stroke-linejoin="round"/></svg>';

  // Page icon SVG (before title - like GitBook)
  const pageIconSvg = '<svg class="w-5 h-5 text-gray-400 dark:text-gray-500 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M19 3H5a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2V5a2 2 0 00-2-2z"/><path d="M9 7h6M9 11h6M9 15h4"/></svg>';

  // Show preview tooltip
  function showPreview(link, preview, anchor) {
    const tooltip = getOrCreateTooltip();
    const href = link.getAttribute('href');

    let html = '<div class="relative">';
    // Main box
    html += '<div class="bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-md shadow-md max-w-sm overflow-hidden">';
    html += '<div class="p-3">';

    if (anchor) {
      // JUMP TO SECTION
      html += '<div class="mb-2">';
      html += '<div class="text-[11px] text-gray-400 dark:text-gray-500 font-semibold uppercase tracking-wide">Jump to section</div>';
      html += '</div>';
      html += '<div class="flex items-center gap-2">';
      html += '<svg class="w-5 h-5 text-gray-400 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M7 20l4-16m2 16l4-16M6 9h14M4 15h14"/></svg>';
      html += `<div class="font-semibold text-gray-900 dark:text-gray-100 text-[15px]">${escapeHtml(formatAnchorText(anchor))}</div>`;
      html += '</div>';
    } else if (preview) {
      // Internal link
      html += '<div class="flex items-start justify-between gap-3 mb-2">';
      if (preview.b && preview.b.length > 0) {
        html += `<div class="text-[11px] text-gray-400 dark:text-gray-500 font-semibold uppercase tracking-wide">${escapeHtml(preview.b.join('  >  '))}</div>`;
      } else {
        html += '<div></div>';
      }
      html += linkIconSvg;
      html += '</div>';
      html += '<div class="flex items-start gap-2.5">';
      html += pageIconSvg;
      html += '<div class="flex-1 min-w-0">';
      if (preview.t) {
        html += `<div class="font-semibold text-gray-900 dark:text-gray-100 text-[15px] leading-snug">${escapeHtml(preview.t)}</div>`;
      }
      if (preview.d) {
        html += `<div class="text-gray-500 dark:text-gray-400 text-[13px] mt-1 leading-relaxed line-clamp-2">${escapeHtml(preview.d)}</div>`;
      }
      html += '</div></div>';
    } else {
      // External link
      html += '<div class="flex items-start justify-between gap-3 mb-2">';
      html += '<div class="text-[11px] text-gray-400 dark:text-gray-500 font-semibold uppercase tracking-wide">External link to</div>';
      html += linkIconSvg;
      html += '</div>';
      html += '<div class="flex items-start gap-2">';
      html += '<svg class="w-5 h-5 text-gray-400 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="10"/><path d="M2 12h20M12 2a15.3 15.3 0 014 10 15.3 15.3 0 01-4 10 15.3 15.3 0 01-4-10 15.3 15.3 0 014-10z"/></svg>';
      // Format URL with word-break opportunities after / and -
      const formattedUrl = escapeHtml(href).replace(/([\/\-])/g, '$1<wbr>');
      html += `<div class="text-black dark:text-white text-[13px] font-medium break-all max-w-[280px]">${formattedUrl}</div>`;
      html += '</div>';
    }

    html += '</div></div>';

    // Arrow (pointing down by default, will flip if needed)
    html += '<div id="tooltip-arrow" class="absolute left-1/2 -translate-x-1/2 w-3 h-3 bg-white dark:bg-gray-900 border-r border-b border-gray-200 dark:border-gray-700 rotate-45"></div>';
    html += '</div>';

    tooltip.innerHTML = html;
    tooltip.classList.remove('hidden');

    // Positioning - use first line rect for multi-line links
    const rects = link.getClientRects();
    const linkRect = rects.length > 0 ? rects[0] : link.getBoundingClientRect();
    const ttRect = tooltip.getBoundingClientRect();
    const arrow = tooltip.querySelector('#tooltip-arrow');

    let left = linkRect.left + linkRect.width / 2 - ttRect.width / 2;
    let top = linkRect.top - ttRect.height - 6;
    let showAbove = true;

    if (top < 8) {
      top = linkRect.bottom + 6;
      showAbove = false;
    }

    const MIN = 16;
    if (left < MIN) left = MIN;
    if (left + ttRect.width > window.innerWidth - MIN) {
      left = window.innerWidth - ttRect.width - MIN;
    }

    tooltip.style.left = `${left}px`;
    tooltip.style.top = `${top}px`;

    // Arrow position
    if (showAbove) {
      arrow.style.bottom = '-6px';
      arrow.style.top = 'auto';
      arrow.className = 'absolute left-1/2 -translate-x-1/2 w-3 h-3 bg-white dark:bg-gray-900 border-r border-b border-gray-200 dark:border-gray-700 rotate-45';
    } else {
      arrow.style.top = '-6px';
      arrow.style.bottom = 'auto';
      arrow.className = 'absolute left-1/2 -translate-x-1/2 w-3 h-3 bg-white dark:bg-gray-900 border-l border-t border-gray-200 dark:border-gray-700 rotate-45';
    }

    tooltip.style.opacity = '1';
  }

  // Hide preview tooltip
  function hidePreview() {
    if (previewTooltip) {
      previewTooltip.classList.add('hidden');
      previewTooltip.style.opacity = '0';
    }
  }

  // Escape HTML to prevent XSS
  function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

  // Handle link hover
  function handleLinkHover(link) {
    // Skip big-link cards - they already show preview info
    if (link.closest('.big-link')) return;

    const href = link.getAttribute('href');

    if (isAnchorOnlyLink(href)) {
      // Same-page anchor link (#section) - show JUMP TO SECTION
      const anchor = href.substring(1);
      showPreview(link, null, anchor);
    } else if (isInternalLink(href)) {
      // Internal link to another page (ignore anchor part)
      const { baseUrl } = parseHref(href);
      const preview = previewsCache.get(baseUrl);
      if (preview) {
        showPreview(link, preview, null);
      }
    } else if (href && (href.startsWith('http://') || href.startsWith('https://'))) {
      // External link - show URL
      showPreview(link, null, null);
    }
  }

  // Initialize link preview functionality
  function initializeLinkPreviews() {
    // Event delegation for hover
    document.addEventListener('mouseover', function(e) {
      const link = e.target.closest('#content a[href]');
      if (!link || link === currentLink) return;

      currentLink = link;
      clearTimeout(hoverTimeout);

      hoverTimeout = setTimeout(function() {
        handleLinkHover(link);
      }, 200);
    });

    document.addEventListener('mouseout', function(e) {
      const link = e.target.closest('#content a[href]');
      if (link === currentLink) {
        clearTimeout(hoverTimeout);
        currentLink = null;
        hidePreview();
      }
    });

    // Schedule initial load
    schedulePreviewsLoad();
  }

  // Initialize on DOMContentLoaded
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeLinkPreviews);
  } else {
    initializeLinkPreviews();
  }

  // Reload previews after HTMX navigation
  document.addEventListener('htmx:afterSwap', function() {
    schedulePreviewsLoad();
  });
})();
