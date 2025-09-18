// Letter Glitch Background Effect with 10x10 Pattern - OPTIMIZED VERSION
(function() {
  'use strict';

  // Character set - NO SPACES
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|\\;:\'",.>?/`~';

  // Configuration
  const config = {
    patternSize: 10, // 10x10 grid
    cellSize: 20, // Size of each cell in pixels
    shuffleInterval: 1500, // Shuffle every 1.5 seconds
  };

  let container;
  let patterns = []; // Array of pattern instances with cached elements
  let basePattern = []; // The base 10x10 pattern
  let animationFrame = null;

  // Initialize on DOM ready
  function init() {
    container = document.querySelector('.letter-glitch-container');
    if (!container) return;

    // Create base pattern
    createBasePattern();

    // Clone pattern across container
    fillContainer();

    // Start shuffling
    startShuffling();

    // Handle resize with debounce
    let resizeTimeout;
    window.addEventListener('resize', () => {
      clearTimeout(resizeTimeout);
      resizeTimeout = setTimeout(() => {
        resetPatterns();
      }, 250);
    });
  }

  function createBasePattern() {
    basePattern = [];

    // Create 10x10 grid of characters
    for (let row = 0; row < config.patternSize; row++) {
      basePattern[row] = [];
      for (let col = 0; col < config.patternSize; col++) {
        basePattern[row][col] = {
          char: getRandomChar(),
          glowing: false
        };
      }
      // Select one random column per row to always glow
      const glowingCol = Math.floor(Math.random() * config.patternSize);
      basePattern[row][glowingCol].glowing = true;
    }
  }

  function getRandomChar() {
    const char = characters[Math.floor(Math.random() * characters.length)];
    return char || 'A'; // Fallback to 'A' just in case
  }

  function fillContainer() {
    // Clear existing patterns
    container.innerHTML = '';
    patterns = [];

    // Calculate how many patterns we need
    const containerWidth = container.offsetWidth;
    const containerHeight = container.offsetHeight;
    const patternPixelSize = config.patternSize * config.cellSize;

    const patternsX = Math.ceil(containerWidth / patternPixelSize);
    const patternsY = Math.ceil(containerHeight / patternPixelSize);

    // Create container for all patterns
    const patternsContainer = document.createElement('div');
    patternsContainer.className = 'patterns-container';
    patternsContainer.style.position = 'relative';
    patternsContainer.style.width = '100%';
    patternsContainer.style.height = '100%';
    container.appendChild(patternsContainer);

    // Create pattern instances with cached elements
    for (let py = 0; py < patternsY; py++) {
      for (let px = 0; px < patternsX; px++) {
        const patternData = createPatternElement(px, py);
        patternsContainer.appendChild(patternData.element);
        patterns.push(patternData);
      }
    }
  }

  function createPatternElement(gridX, gridY) {
    const patternEl = document.createElement('div');
    patternEl.className = 'letter-pattern-grid';
    patternEl.style.position = 'absolute';
    patternEl.style.left = (gridX * config.patternSize * config.cellSize) + 'px';
    patternEl.style.top = (gridY * config.patternSize * config.cellSize) + 'px';
    patternEl.style.width = (config.patternSize * config.cellSize) + 'px';
    patternEl.style.height = (config.patternSize * config.cellSize) + 'px';
    patternEl.style.display = 'grid';
    patternEl.style.gridTemplateColumns = `repeat(${config.patternSize}, ${config.cellSize}px)`;
    patternEl.style.gridTemplateRows = `repeat(${config.patternSize}, ${config.cellSize}px)`;

    // Cache letter elements in a 2D array for fast access
    const letterElements = [];

    for (let row = 0; row < config.patternSize; row++) {
      letterElements[row] = [];
      for (let col = 0; col < config.patternSize; col++) {
        const letter = document.createElement('div');
        letter.className = 'glitch-letter';
        letter.textContent = basePattern[row][col].char;

        // Check if this letter is glowing
        if (basePattern[row][col].glowing) {
          letter.classList.add('active');
        }

        patternEl.appendChild(letter);
        letterElements[row][col] = letter; // Cache the element
      }
    }

    return {
      element: patternEl,
      letters: letterElements // Return cached letters for fast access
    };
  }

  function startShuffling() {
    // Use requestAnimationFrame for smoother updates
    let lastShuffleTime = 0;

    function animate(currentTime) {
      if (currentTime - lastShuffleTime >= config.shuffleInterval) {
        shuffleAllRows();
        lastShuffleTime = currentTime;
      }
      animationFrame = requestAnimationFrame(animate);
    }

    animationFrame = requestAnimationFrame(animate);
  }

  function shuffleAllRows() {
    // Batch DOM updates
    const updates = [];

    // For each row, shuffle the letters within that row
    for (let row = 0; row < config.patternSize; row++) {
      // Create copy of row for shuffling
      const rowData = [...basePattern[row]];

      // Fisher-Yates shuffle
      for (let i = rowData.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [rowData[i], rowData[j]] = [rowData[j], rowData[i]];
      }

      // Update base pattern
      basePattern[row] = rowData;

      // Prepare updates for this row
      for (let col = 0; col < config.patternSize; col++) {
        updates.push({
          row: row,
          col: col,
          char: basePattern[row][col].char,
          glowing: basePattern[row][col].glowing
        });
      }
    }

    // Apply all updates at once using cached elements
    requestAnimationFrame(() => {
      patterns.forEach(patternData => {
        updates.forEach(update => {
          const letterEl = patternData.letters[update.row][update.col];
          if (letterEl) {
            letterEl.textContent = update.char;

            // Toggle active class
            if (update.glowing) {
              if (!letterEl.classList.contains('active')) {
                letterEl.classList.add('active');
              }
            } else {
              if (letterEl.classList.contains('active')) {
                letterEl.classList.remove('active');
              }
            }
          }
        });
      });
    });
  }

  function resetPatterns() {
    // Cancel animation frame
    if (animationFrame) {
      cancelAnimationFrame(animationFrame);
    }

    // Recreate everything on resize
    createBasePattern();
    fillContainer();
    startShuffling();
  }

  // Check for reduced motion preference
  function prefersReducedMotion() {
    return window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  }

  // Initialize when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
      if (!prefersReducedMotion()) {
        init();
      }
    });
  } else {
    if (!prefersReducedMotion()) {
      init();
    }
  }
})();