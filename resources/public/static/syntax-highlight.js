// Simple syntax highlighting script
document.addEventListener('DOMContentLoaded', function() {
  // Add spacing between consecutive code blocks
  const addSpacingBetweenCodeBlocks = function() {
    const codeContainers = document.querySelectorAll('pre');
    
    codeContainers.forEach(function(container, index) {
      // Add class to container
      container.classList.add('code-block');
      
      // Wrap in a container for spacing
      const wrapper = document.createElement('div');
      wrapper.className = 'code-block-container my-6';
      container.parentNode.insertBefore(wrapper, container);
      wrapper.appendChild(container);
    });
  };
  
  // Apply syntax highlighting to code blocks
  const applySyntaxHighlighting = function() {
    const codeBlocks = document.querySelectorAll('pre code');
    
    codeBlocks.forEach(function(block) {
      // Get language from class
      let language = '';
      block.classList.forEach(function(cls) {
        if (cls.startsWith('language-')) {
          language = cls.replace('language-', '');
        }
      });
      
      // Apply language-specific highlighting
      if (language) {
        applyHighlightingByLanguage(block, language);
      }
    });
  };
  
  // Apply language-specific syntax highlighting
  const applyHighlightingByLanguage = function(block, language) {
    // Simple patterns for each language
    const patterns = {
      'shell': [
        { pattern: /^(\s*#.*)$/gm, className: 'com' },  // Comments
        { pattern: /"([^"]*)"/g, className: 'str' },   // Double quoted strings
        { pattern: /'([^']*)'/g, className: 'str' },   // Single quoted strings
        { pattern: /\b(if|then|else|fi|while|do|done|for|in|case|esac|function)\b/g, className: 'kwd' }, // Keywords
        { pattern: /\$\w+/g, className: 'ident' },    // Variables
        { pattern: /\b\d+\b/g, className: 'num' },    // Numbers
        { pattern: /^\s*(\w+)\b/gm, className: 'cmd' } // Commands at line start
      ],
      'docker': [
        { pattern: /^(\s*#.*)$/gm, className: 'com' },  // Comments
        { pattern: /"([^"]*)"/g, className: 'str' },   // Double quoted strings
        { pattern: /'([^']*)'/g, className: 'str' },   // Single quoted strings
        { pattern: /\b(FROM|RUN|CMD|LABEL|MAINTAINER|EXPOSE|ENV|ADD|COPY|ENTRYPOINT|VOLUME|USER|WORKDIR|ARG|ONBUILD|STOPSIGNAL|HEALTHCHECK|SHELL)\b/g, className: 'dockerfile-directive' }, // Docker directives
        { pattern: /\$\w+/g, className: 'ident' },    // Variables
        { pattern: /\b\d+\b/g, className: 'num' }     // Numbers
      ],
      'json': [
        { pattern: /"([^"]*)":/g, className: 'key' },   // JSON keys
        { pattern: /:\s*"([^"]*)"/g, className: 'str' }, // JSON string values
        { pattern: /:\s*(true|false|null)/g, className: 'kwd' }, // JSON keywords
        { pattern: /:\s*\d+/g, className: 'num' }       // JSON numbers
      ]
    };
    
    // Get patterns for this language
    const langPatterns = patterns[language] || patterns['shell'];
    if (!langPatterns) return;
    
    // Get the text content
    let content = block.textContent;
    let highlightedContent = content;
    
    // Replace with marked spans
    langPatterns.forEach(function(p) {
      highlightedContent = highlightedContent.replace(
        p.pattern, 
        match => `<span class="${p.className}">${match}</span>`
      );
    });
    
    // Replace content if changed
    if (highlightedContent !== content) {
      block.innerHTML = highlightedContent;
    }
  };
  
  // Run all our highlighting functions
  addSpacingBetweenCodeBlocks();
  applySyntaxHighlighting();
}); 