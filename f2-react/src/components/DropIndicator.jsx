import React from 'react';
import { cn } from '../lib/utils';

const DropIndicator = ({ isVisible, position = 'between', isValidDrop = true }) => {
  if (!isVisible) return null;

  return (
    <div
      className={cn(
        "transition-all duration-200 pointer-events-none",
        position === 'between' && "h-0.5 w-full my-1",
        position === 'inside' && "border-2 border-dashed rounded p-2 m-1",
        isValidDrop ? "bg-green-400 border-green-400" : "bg-red-400 border-red-400"
      )}
    >
      {position === 'inside' && (
        <div className={cn(
          "text-xs text-center py-1",
          isValidDrop ? "text-green-600" : "text-red-600"
        )}>
          {isValidDrop ? "Drop here to add to this section" : "Cannot drop here"}
        </div>
      )}
    </div>
  );
};

export default DropIndicator;