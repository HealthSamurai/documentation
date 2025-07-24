import React from 'react';

const ChangePanel = ({ pendingChanges, onReset, onSave }) => {
  const totalChanges = Object.values(pendingChanges).reduce((sum, changes) => sum + changes.length, 0);
  
  const getChangesDescription = () => {
    if (totalChanges === 0) {
      return 'No pending changes - drag items to reorganize';
    }
    
    const changeDescriptions = [];
    if (pendingChanges.moves && pendingChanges.moves.length > 0) {
      changeDescriptions.push(`${pendingChanges.moves.length} moves`);
    }
    if (pendingChanges.renames && pendingChanges.renames.length > 0) {
      changeDescriptions.push(`${pendingChanges.renames.length} renames`);
    }
    if (pendingChanges.reorders && pendingChanges.reorders.length > 0) {
      changeDescriptions.push(`${pendingChanges.reorders.length} reorders`);
    }
    if (pendingChanges.conversions && pendingChanges.conversions.length > 0) {
      changeDescriptions.push(`${pendingChanges.conversions.length} conversions`);
    }
    
    return changeDescriptions.join(', ');
  };

  return (
    <div className="px-6 py-4 bg-gray-100 border-t border-gray-200 flex-shrink-0">
      <div className="flex items-center justify-between max-w-full">
        <div className="flex items-center gap-4 flex-1 min-w-0">
          <div className="text-sm whitespace-nowrap">
            <span className="font-medium text-gray-700">Pending Changes: </span>
            <span className="font-bold text-gray-600">{totalChanges}</span>
          </div>
          <div className="text-xs text-gray-500 overflow-hidden text-ellipsis whitespace-nowrap max-w-96">
            {getChangesDescription()}
          </div>
        </div>
        <div className="flex items-center gap-2 flex-shrink-0">
          <button 
            className={`px-4 py-2 text-sm rounded-md border transition-all font-medium ${
              totalChanges === 0 
                ? 'bg-white text-gray-400 border-gray-300 cursor-not-allowed opacity-50' 
                : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50 hover:border-gray-400'
            }`}
            onClick={onReset}
            disabled={totalChanges === 0}
          >
            🔄 Reset
          </button>
          <button 
            className={`px-4 py-2 text-sm rounded-md transition-all font-medium ${
              totalChanges === 0 
                ? 'bg-blue-300 text-white cursor-not-allowed opacity-50' 
                : 'bg-blue-600 text-white hover:bg-blue-700'
            }`}
            onClick={onSave}
            disabled={totalChanges === 0}
          >
            💾 Save Changes
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChangePanel;