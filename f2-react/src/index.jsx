import React from 'react';
import { createRoot } from 'react-dom/client';
import F2Navigation from './components/F2Navigation';

// Global state for React app
let f2Root = null;
let f2Component = null;

// Simple F2 component that we can control directly  
const SimpleF2Component = ({ isVisible, onClose }) => {
  console.log('🎯 SimpleF2Component rendering, isVisible:', isVisible);
  
  if (!isVisible) {
    return null;
  }
  
  return <F2Navigation onClose={onClose} isVisible={isVisible} />;
};

// Global state
let isF2Visible = false;

// Initialize React root
function initializeF2Root() {
  console.log('🚀 Initializing F2 React root');
  
  let container = document.getElementById('f2-navigation-react-root');
  if (!container) {
    container = document.createElement('div');
    container.id = 'f2-navigation-react-root';
    document.body.appendChild(container);
    console.log('📦 Created F2 container');
  }
  
  if (!f2Root) {
    f2Root = createRoot(container);
    console.log('⚛️ Created React root');
  }
  
  return container;
}

// Render F2 component
function renderF2Component() {
  console.log('🎨 Rendering F2 component, visible:', isF2Visible);
  
  if (!f2Root) {
    initializeF2Root();
  }
  
  f2Root.render(
    <SimpleF2Component 
      isVisible={isF2Visible} 
      onClose={() => hideF2Navigation()} 
    />
  );
}

// Show F2 Navigation
function showF2Navigation() {
  console.log('🔥 showF2Navigation called');
  isF2Visible = true;
  renderF2Component();
}

// Hide F2 Navigation
function hideF2Navigation() {
  console.log('🔥 hideF2Navigation called');
  isF2Visible = false;
  renderF2Component();
}


// Listen for F2 key press
function setupF2KeyListener() {
  console.log('🎯 Setting up F2 key listener...');
  
  document.addEventListener('keydown', function(event) {
    console.log('🔍 Key pressed:', event.key, 'Code:', event.code);
    
    if (event.key === 'F2') {
      console.log('🚀 F2 key detected! Preventing default and toggling F2 mode...');
      event.preventDefault();
      
      // Check current F2 state
      console.log('📍 Current F2 state:', isF2Visible);
      
      if (isF2Visible) {
        console.log('🔄 Hiding F2 navigation...');
        hideF2Navigation();
      } else {
        console.log('🔄 Showing F2 navigation...');
        showF2Navigation();
      }
    }
  });
  
  console.log('✅ F2 key listener setup complete');
}

// Auto-initialize when script loads
document.addEventListener('DOMContentLoaded', function() {
  console.log('🔧 Setting up F2 Navigation React integration');
  initializeF2Root();
  setupF2KeyListener();
  
  // Render initial state (hidden)
  renderF2Component();
});

// Export functions to global scope for external usage
window.F2NavigationReact = {
  show: showF2Navigation,
  hide: hideF2Navigation,
  init: initializeF2Root
};

console.log('✅ F2 Navigation React module loaded');