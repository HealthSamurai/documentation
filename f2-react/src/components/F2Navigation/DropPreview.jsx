import React from 'react';

const DropPreview = ({ position, level, isVisible = false }) => {
  if (!isVisible) return null;

  const style = {
    marginLeft: `${Math.max(0, Math.min(3, level)) * 24}px`,
    transform: position ? `translateY(${position}px)` : 'translateY(0)',
    willChange: 'transform, opacity'
  };
  
  return (
    <div 
      className="drop-preview-line active" 
      style={style}
      role="presentation"
      aria-hidden="true"
    />
  );
};

export default DropPreview;