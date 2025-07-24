import React from 'react';

const NestingIndicator = ({ currentLevel = 0, previewLevel = 0, isVisible = false }) => {
  if (!isVisible) return null;

  const indicators = [];
  for (let i = 0; i <= 3; i++) {
    const isActive = i === previewLevel;
    const isInvalid = previewLevel > 3 && i === 3; // Show invalid state on last dot
    const isPassed = i < previewLevel && previewLevel <= 3;
    
    indicators.push(
      <div 
        key={i}
        className={`nesting-dot ${isActive ? 'active' : ''} ${isInvalid ? 'invalid' : ''} ${isPassed ? 'passed' : ''}`}
        role="presentation"
        aria-hidden="true"
      />
    );
  }
  
  return (
    <div 
      className="nesting-indicator"
      role="status"
      aria-label={`Nesting level ${Math.min(previewLevel, 3)} of 3`}
    >
      {indicators}
    </div>
  );
};

export default NestingIndicator;