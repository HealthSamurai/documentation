import React from 'react';

const PendingChangesIndicator = ({ count = 0 }) => {
  if (count === 0) return null;
  
  return (
    <div 
      className="pending-changes-indicator"
      role="status"
      aria-label={`${count} pending changes`}
    >
      <div className="orange-bar" />
      <div className="pulse-dot">
        {count > 99 ? '99+' : count}
      </div>
    </div>
  );
};

export default PendingChangesIndicator;