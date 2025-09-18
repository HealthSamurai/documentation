// Letter Glitch Background Effect with 10x10 Pattern
(function() {
  'use strict';

  // Character set - NO SPACES
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|\\;:\'",.<>?/`~';

  // Configuration
  const config = {
    patternSize: 10, // 10x10 grid
    cellSize: 20, // Size of each cell in pixels
    shuffleInterval: 1500, // Shuffle every 1.5 seconds
  };

  let container;
  let patterns = []; // Array of pattern instances
  let basePattern = []; // The base 10x10 pattern
  let glowingColumns = []; // One glowing column per row

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

    // Handle resize
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
    glowingColumns = [];

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
    // Make sure we never return empty space
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

    // Create pattern instances
    for (let py = 0; py < patternsY; py++) {
      for (let px = 0; px < patternsX; px++) {
        const patternEl = createPatternElement(px, py);
        patternsContainer.appendChild(patternEl);
        patterns.push(patternEl);
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

    // Fill with letters from base pattern
    for (let row = 0; row < config.patternSize; row++) {
      for (let col = 0; col < config.patternSize; col++) {
        const letter = document.createElement('div');
        letter.className = 'glitch-letter';
        letter.textContent = basePattern[row][col].char;
        letter.dataset.row = row;
        letter.dataset.col = col;

        // Check if this letter is glowing
        if (basePattern[row][col].glowing) {
          letter.classList.add('active');
        }

        patternEl.appendChild(letter);
      }
    }

    return patternEl;
  }

  function startShuffling() {
    // Shuffle rows every 1.5 seconds
    setInterval(() => {
      shuffleAllRows();
    }, config.shuffleInterval);
  }

  function shuffleAllRows() {
    // For each row, shuffle the letters within that row
    for (let row = 0; row < config.patternSize; row++) {
      // Create copy of row for shuffling
      const rowData = [...basePattern[row]];

      // Fisher-Yates shuffle - this shuffles the entire objects (char + glowing state)
      for (let i = rowData.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [rowData[i], rowData[j]] = [rowData[j], rowData[i]];
      }

      // Update base pattern
      basePattern[row] = rowData;

      // Update all pattern instances
      patterns.forEach(patternEl => {
        for (let col = 0; col < config.patternSize; col++) {
          const letterEl = patternEl.querySelector(`.glitch-letter[data-row="${row}"][data-col="${col}"]`);
          if (letterEl) {
            // Update character
            letterEl.textContent = basePattern[row][col].char;

            // Update glowing state - the glowing letter now moves with the shuffle!
            if (basePattern[row][col].glowing) {
              letterEl.classList.add('active');
            } else {
              letterEl.classList.remove('active');
            }
          }
        }
      });
    }
  }

  function resetPatterns() {
    // Recreate everything on resize
    createBasePattern();
    fillContainer();
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