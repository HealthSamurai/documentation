(ns gitbok.ui
  (:require
   [cheshire.core]
   [gitbok.markdown]
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
  
  /* Syntax Highlighting */
  .kwd { color: #93c5fd; } /* blue-300 - keywords */
  .str { color: #86efac; } /* green-300 - strings */
  .com { color: #94a3b8; font-style: italic; } /* slate-400 - comments */
  .ident { color: #e2e8f0; } /* slate-200 - identifiers */
  .fn { color: #c4b5fd; } /* violet-300 - functions */
  .num { color: #fda4af; } /* rose-300 - numbers */
  .cmd { color: #fb923c; font-weight: bold; } /* orange-400 - shell commands */
  .dockerfile-directive { color: #fb923c; font-weight: bold; } /* orange-400 - Docker directives */
  .key { color: #93c5fd; } /* blue-300 - JSON keys */
  .value { color: #86efac; } /* green-300 - JSON values */
  
  /* HTTP specific */
  .http-body-separator { color: #1e293b; } /* Make the separator invisible */
  
  /* Better spacing for code blocks */
  .code-block-container { margin-top: 1.5rem; margin-bottom: 1.5rem; }
  .code-block-container + .code-block-container { margin-top: 2rem; }
  
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
    
    codeContainers.forEach(function(container, index) {
      container.classList.add('code-block');
      const wrapper = document.createElement('div');
      wrapper.className = 'code-block-container';
      container.parentNode.insertBefore(wrapper, container);
      wrapper.appendChild(container);
      console.log('Wrapped code block ' + index);
    });
    
    // Apply syntax highlighting
    const codeBlocks = document.querySelectorAll('pre code');
    console.log('Found ' + codeBlocks.length + ' code blocks for highlighting');
    
    codeBlocks.forEach(function(block, index) {
      // Get language from class
      let language = '';
      console.log('Block ' + index + ' classes: ' + block.className);
      
      Array.from(block.classList).forEach(function(cls) {
        if (cls.startsWith('language-')) {
          language = cls.replace('language-', '');
        }
      });
      
      console.log('Block ' + index + ' language: ' + (language || 'none'));
      
      if (!language) return;
      
      // Apply simple coloring based on language
      simpleCodeHighlight(block, language);
    });
  });
  
  // Simple function to add minimal highlighting
  function simpleCodeHighlight(block, language) {
    // Add a language label
    const label = document.createElement('div');
    label.className = 'text-xs text-right mb-1 text-slate-500';
    label.textContent = 'Language: ' + language;
    block.parentNode.parentNode.insertBefore(label, block.parentNode);
    
    // Apply base color
    block.style.color = '#e2e8f0';
    
    // Apply a background color to the pre element
    const pre = block.parentNode;
    if (pre && pre.tagName === 'PRE') {
      pre.style.backgroundColor = '#1e293b';
      pre.style.borderRadius = '0.375rem';
      pre.style.padding = '1rem';
      pre.style.overflow = 'auto';
      pre.style.color = '#f8fafc';
      pre.style.fontSize = '0.875rem';
      pre.style.lineHeight = '1.7';
    }
    
    // Simple string replacement for key patterns
    let html = block.innerHTML;
    
    if (language === 'shell' || language === 'bash') {
      // Comments starting with #
      html = html.replace(/(^|\\n)(\\s*#.*?)($|\\n)/g, '$1<span style=\"color:#94a3b8;font-style:italic\">$2</span>$3');
      // Commands at line start
      html = html.replace(/(^|\\n)(\\s*\\w+\\b)/g, '$1<span style=\"color:#fb923c;font-weight:bold\">$2</span>');
      // Strings
      html = html.replace(/\"([^\"]*)\"/g, '<span style=\"color:#86efac\">\"$1\"</span>');
    }
    else if (language === 'docker') {
      // Docker directives
      html = html.replace(/\\b(FROM|RUN|CMD|LABEL|MAINTAINER|EXPOSE|ENV|ADD|COPY|ENTRYPOINT|VOLUME|USER|WORKDIR|ARG)\\b/g, 
        '<span style=\"color:#fb923c;font-weight:bold\">$1</span>');
      // Comments starting with #
      html = html.replace(/(^|\\n)(\\s*#.*?)($|\\n)/g, '$1<span style=\"color:#94a3b8;font-style:italic\">$2</span>$3');
    }
    else if (language === 'json') {
      // JSON keys
      html = html.replace(/\"([^\"]+)\":/g, '<span style=\"color:#93c5fd\">\"$1\"</span>:');
      // JSON string values
      html = html.replace(/:\\s*\"([^\"]*)\"/g, ': <span style=\"color:#86efac\">\"$1\"</span>');
    }
    
    // Only update if changes were made
    if (html !== block.innerHTML) {
      block.innerHTML = html;
      console.log('Applied simple highlighting to ' + language + ' block');
    }
  }
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
