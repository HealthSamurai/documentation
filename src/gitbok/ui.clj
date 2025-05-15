(ns gitbok.ui
  (:require
   [cheshire.core]
   [http]
   [gitbok.indexing.impl.summary :as summary]
   [system]
   [uui]
   [uui.heroicons :as ico]))

(defn render-menu [items & [open]]
  (if (:children items)
    [:details {:class "" :open open}
     [:summary {:class "flex items-center"} [:div {:class "flex-1"} (:title items)] (ico/chevron-right "chevron size-5 text-gray-400")]
     [:div {:class "ml-4 border-l border-gray-200"}
      (for [c (:children items)] (render-menu c))]]
    [:div {:class ""} (:title items)]))

(defn menu [summary]
  [:div
   [:a {:href "/admin/broken" :class "block px-5 py-1"} "Broken Links"]
   (for [item summary]
     [:div
      [:div {:class "pl-4 mt-4 mb-2"} [:b (:title item)]]
      (for [ch (:children item)]
        (render-menu ch))])])

;; Inline CSS style for code highlighting
(def code-highlight-css
  "
  /* Code Block Styling */
  pre { border-radius: 0.375rem; padding: 1rem; margin-top: 1.5rem; margin-bottom: 1.5rem; overflow-x: auto; background-color: #1e293b; color: #f8fafc; font-size: 0.875rem; line-height: 1.7; }
  code { font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New', monospace; }

  /* Syntax Highlighting - using inline styles for maximum compatibility */
  span[style*='color:#94a3b8'] { color: #94a3b8; } /* slate-400 - comments */
  span[style*='color:#86efac'] { color: #86efac; } /* green-300 - strings */
  span[style*='color:#93c5fd'] { color: #93c5fd; } /* blue-300 - keywords */
  span[style*='color:#c4b5fd'] { color: #c4b5fd; } /* violet-300 - functions */
  span[style*='color:#fda4af'] { color: #fda4af; } /* rose-300 - numbers */
  span[style*='color:#fb923c'] { color: #fb923c; } /* orange-400 - commands */
  span[style*='color:#64748b'] { color: #64748b; } /* slate-500 - delimiters */
  span[style*='color:#fbbf24'] { color: #fbbf24; } /* amber-400 - decorators */
  span[style*='color:#e2e8f0'] { color: #e2e8f0; } /* slate-200 - identifiers */

  /* Add italic style */
  span[style*='font-style:italic'] { font-style: italic; }

  /* Add bold style */
  span[style*='font-weight:bold'] { font-weight: bold; }

  /* Better spacing for code blocks */
  .code-block-container { margin-top: 1.5rem; margin-bottom: 1.5rem; }
  .code-block-container + .code-block-container { margin-top: 2rem; }

  /* Add language indicator */
  .language-indicator { font-size: 0.75rem; text-align: right; margin-bottom: 0.25rem; color: #6b7280; }

  /* Add some spacing after headers */
  h1, h2, h3, h4, h5, h6 { margin-top: 1.5rem; margin-bottom: 1rem; }

  /* Make sure code blocks inside lists are formatted properly */
  li pre { margin-top: 0.75rem; margin-bottom: 0.75rem; }
  ")

;; Inline JavaScript for syntax highlighting
(def syntax-highlight-js
  "
  // Apply syntax highlighting on page load
  document.addEventListener('DOMContentLoaded', function() {
    console.log('Syntax highlighting script loaded');

    // Add spacing between consecutive code blocks
    const codeContainers = document.querySelectorAll('pre');
    console.log('Found ' + codeContainers.length + ' code containers');

    // Wrap code blocks in container divs if they're not already wrapped
    codeContainers.forEach(function(container, index) {
      // Only wrap if not already in a code-block-container
      if (!container.parentNode.classList.contains('code-block-container')) {
        container.classList.add('code-block');
        const wrapper = document.createElement('div');
        wrapper.className = 'code-block-container';
        container.parentNode.insertBefore(wrapper, container);
        wrapper.appendChild(container);
        console.log('Wrapped code block ' + index);
      }
    });

    // Make sure all code blocks have correct styling
    const allCodeBlocks = document.querySelectorAll('pre code');
    console.log('Found ' + allCodeBlocks.length + ' code blocks for highlighting');

    allCodeBlocks.forEach(function(block, index) {
      // Apply base styling if not already styled
      if (!block.hasAttribute('style')) {
        block.style.fontFamily = 'ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, \"Liberation Mono\", \"Courier New\", monospace';
        block.style.color = '#e2e8f0';
        console.log('Applied base styling to code block ' + index);
      }

      // Apply base styling to pre element
      const pre = block.parentNode;
      if (pre && pre.tagName === 'PRE' && !pre.hasAttribute('style')) {
        pre.style.backgroundColor = '#1e293b';
        pre.style.borderRadius = '0.375rem';
        pre.style.padding = '1rem';
        pre.style.overflow = 'auto';
        pre.style.color = '#f8fafc';
        pre.style.fontSize = '0.875rem';
        pre.style.lineHeight = '1.7';
      }

      // Add language indicator if missing
      const container = pre?.parentNode;
      if (container && container.classList.contains('code-block-container')) {
        // Get language from class
        let language = '';
        Array.from(block.classList).forEach(function(cls) {
          if (cls.startsWith('language-')) {
            language = cls.replace('language-', '');
          }
        });

        // Check if language indicator already exists
        let hasLanguageIndicator = false;
        Array.from(container.children).forEach(function(child) {
          if (child.classList && child.classList.contains('language-indicator')) {
            hasLanguageIndicator = true;
          }
        });

        // Add language indicator if not present
        if (!hasLanguageIndicator && language) {
          const label = document.createElement('div');
          label.className = 'language-indicator';
          label.textContent = 'Language: ' + language;
          container.insertBefore(label, pre);
          console.log('Added language indicator for ' + language);
        }
      }
    });
  });
  ")

;; Inline JavaScript for spacing code blocks
(def code-spacing-js
  "
  // Simple script to add spacing between consecutive code blocks
  document.addEventListener('DOMContentLoaded', function() {
    // Find all pre elements
    var pres = document.querySelectorAll('pre');

    // Add space between consecutive pre elements
    for (var i = 1; i < pres.length; i++) {
      var current = pres[i];
      var previous = pres[i-1];

      // Check if they are consecutive (no other elements between them)
      var nextElement = previous.nextElementSibling;
      while (nextElement && nextElement.nodeType !== 1) {
        nextElement = nextElement.nextElementSibling;
      }

      if (nextElement === current) {
        current.style.marginTop = '2rem';
      }
    }
  });
  ")

(defn layout [context request content]
  (if (uui/hx-target request)
    (uui/response [:div#content {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"} content])
    (uui/boost-response
     context request
     [:div {:class "flex items-top"}
      ;; Add tabs.js script
      [:script {:src "/static/tabs.js"}]

      ;; Add inline styles for code highlighting
      [:style code-highlight-css]

      ;; Add inline JavaScript for code spacing
      [:script code-spacing-js]

      [:div.nav {:class "px-6 py-6 w-80 text-sm h-screen overflow-auto bg-gray-50 shadow-md"}
       (menu (summary/get context))]
      [:div#content {:class "m-x-auto flex-1 py-6 px-12  h-screen overflow-auto"} content]])))
