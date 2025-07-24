# Сценарий: Сложные комбинированные операции

## Обзор
Обработка сложных случаев, которые включают множественные операции, конфликты, граничные условия и recovery сценарии.

## Сценарий 1: Множественные операции подряд

### Начальное состояние
```
📁 Documentation
  📁 Getting Started
    📄 Introduction
    📄 Installation
  📁 Advanced
    📄 Performance
  📄 FAQ
```

### Последовательность операций
1. **Rename**: "Getting Started" → "Quick Start"
2. **Move**: "FAQ" → внутрь "Quick Start"
3. **Convert**: "Performance" + "Installation" → "Performance" становится папкой
4. **Move folder**: "Quick Start" → внутрь "Advanced"

### Обработка в системе

**Накопление изменений**:
```javascript
// После 4 операций:
pendingChanges: {
  renames: [{
    id: "ren-001",
    nodeId: "getting-started",
    oldName: "Getting Started",
    newName: "Quick Start",
    timestamp: 1643723456789
  }],
  moves: [
    {
      id: "mov-001", 
      nodeId: "faq",
      fromParent: "documentation",
      toParent: "getting-started", // старый ID, система отследит rename
      timestamp: 1643723456790
    },
    {
      id: "mov-002",
      nodeId: "installation", 
      fromParent: "getting-started",
      toParent: "performance",
      timestamp: 1643723456792
    },
    {
      id: "mov-003",
      nodeId: "getting-started",
      fromParent: "documentation", 
      toParent: "advanced",
      timestamp: 1643723456793
    }
  ],
  conversions: [{
    id: "conv-001",
    nodeId: "performance",
    fromType: "document",
    toType: "folder",
    timestamp: 1643723456791
  }]
}
```

**Разрешение зависимостей**:
```javascript
const resolveDependencies = (changes) => {
  const operations = [];
  
  // 1. Сначала переименования (не влияют на структуру)
  operations.push(...changes.renames);
  
  // 2. Конвертации (создают новые контейнеры)
  operations.push(...changes.conversions);
  
  // 3. Moves в правильном порядке (учитывая переименования)
  const sortedMoves = sortMovesByDependency(changes.moves);
  operations.push(...sortedMoves);
  
  return operations;
};
```

**Итоговое состояние**:
```
📁 Documentation
  📁 Advanced
    📁 Performance        <- конвертирован из документа
      📄 Installation     <- перемещен сюда
    📁 Quick Start        <- переименован и перемещен
      📄 Introduction
      📄 FAQ              <- перемещен сюда
```

## Сценарий 2: Конфликт операций

### Проблема
Пользователь пытается:
1. Переместить папку "A" в папку "B"
2. Одновременно переместить папку "B" в папку "A"

### Обнаружение конфликта
```javascript
const detectCircularMoves = (moves) => {
  const moveGraph = new Map();
  
  // Строим граф перемещений
  moves.forEach(move => {
    if (!moveGraph.has(move.fromParent)) {
      moveGraph.set(move.fromParent, []);
    }
    moveGraph.get(move.fromParent).push(move.toParent);
  });
  
  // Ищем циклы
  const visited = new Set();
  const recursionStack = new Set();
  
  const hasCycle = (node) => {
    if (recursionStack.has(node)) return true;
    if (visited.has(node)) return false;
    
    visited.add(node);
    recursionStack.add(node);
    
    const neighbors = moveGraph.get(node) || [];
    for (const neighbor of neighbors) {
      if (hasCycle(neighbor)) return true;
    }
    
    recursionStack.delete(node);
    return false;
  };
  
  for (const [node] of moveGraph) {
    if (hasCycle(node)) {
      return true;
    }
  }
  
  return false;
};
```

### Резолюция конфликта
```javascript
const ConflictResolutionDialog = ({ conflicts, onResolve }) => {
  return (
    <div className="conflict-dialog">
      <div className="dialog-header">
        <h3>⚠️ Conflicting Operations Detected</h3>
      </div>
      
      <div className="conflicts-list">
        {conflicts.map(conflict => (
          <div key={conflict.id} className="conflict-item">
            <div className="conflict-description">
              {conflict.type === 'circular' && (
                <>
                  <strong>Circular dependency:</strong> {conflict.description}
                </>
              )}
            </div>
            
            <div className="resolution-options">
              <button onClick={() => onResolve(conflict.id, 'priority')}>
                Use operation order priority
              </button>
              <button onClick={() => onResolve(conflict.id, 'manual')}>
                Resolve manually
              </button>
              <button onClick={() => onResolve(conflict.id, 'cancel')}>
                Cancel conflicting operations
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
```

## Сценарий 3: Превышение лимитов системы

### Ограничения
```javascript
const SYSTEM_LIMITS = {
  MAX_DEPTH: 5,
  MAX_CHILDREN_PER_FOLDER: 1000,
  MAX_PATH_LENGTH: 260,
  MAX_PENDING_OPERATIONS: 100,
  MAX_OPERATION_SIZE: 10000 // количество затронутых узлов
};
```

### Обработка превышений
```javascript
const validateSystemLimits = (operation) => {
  const violations = [];
  
  // Проверка глубины
  if (operation.resultingDepth > SYSTEM_LIMITS.MAX_DEPTH) {
    violations.push({
      type: 'depth',
      limit: SYSTEM_LIMITS.MAX_DEPTH,
      current: operation.resultingDepth,
      message: `Maximum nesting depth (${SYSTEM_LIMITS.MAX_DEPTH}) would be exceeded`
    });
  }
  
  // Проверка количества детей
  if (operation.targetChildrenCount > SYSTEM_LIMITS.MAX_CHILDREN_PER_FOLDER) {
    violations.push({
      type: 'children',
      limit: SYSTEM_LIMITS.MAX_CHILDREN_PER_FOLDER,
      current: operation.targetChildrenCount,
      message: `Folder would exceed maximum children limit (${SYSTEM_LIMITS.MAX_CHILDREN_PER_FOLDER})`
    });
  }
  
  // Проверка длины пути
  if (operation.resultingPath?.length > SYSTEM_LIMITS.MAX_PATH_LENGTH) {
    violations.push({
      type: 'path',
      limit: SYSTEM_LIMITS.MAX_PATH_LENGTH,
      current: operation.resultingPath.length,
      message: `Path too long (${operation.resultingPath.length}/${SYSTEM_LIMITS.MAX_PATH_LENGTH})`
    });
  }
  
  return violations;
};
```

### Предложение альтернатив
```javascript
const suggestAlternatives = (violation, operation) => {
  switch (violation.type) {
    case 'depth':
      return {
        action: 'RESTRUCTURE',
        message: 'Consider flattening the folder structure',
        suggestions: [
          'Move some subfolders to a higher level',
          'Combine related folders',
          'Split large folders into multiple smaller ones'
        ]
      };
      
    case 'children':
      return {
        action: 'ORGANIZE',
        message: 'This folder has too many items',
        suggestions: [
          'Create subfolders to group related items',
          'Move some items to other locations',
          'Archive old items'
        ]
      };
      
    case 'path':
      return {
        action: 'SHORTEN',
        message: 'Path is too long',
        suggestions: [
          'Use shorter folder names',
          'Reduce nesting levels',
          'Move folder closer to root'
        ]
      };
  }
};
```

## Сценарий 4: Сбой и восстановление

### Сохранение промежуточного состояния
```javascript
const saveIntermediateState = () => {
  const state = {
    pendingChanges: store.getState().pendingChanges,
    navigationTree: store.getState().navigationTree,
    dragState: store.getState().dragState,
    timestamp: Date.now(),
    sessionId: getSessionId()
  };
  
  // Сохраняем в localStorage для recovery
  localStorage.setItem('f2-recovery-state', JSON.stringify(state));
};

// Автосохранение каждые 30 секунд во время активности  
setInterval(() => {
  if (hasUnsavedChanges()) {
    saveIntermediateState();
  }
}, 30000);
```

### Восстановление после сбоя
```javascript
const recoverFromFailure = () => {
  const savedState = localStorage.getItem('f2-recovery-state');
  
  if (savedState) {
    try {
      const state = JSON.parse(savedState);
      
      // Проверяем актуальность состояния (не старше 1 часа)
      if (Date.now() - state.timestamp < 3600000) {
        showRecoveryDialog(state);
      }
    } catch (error) {
      console.warn('Failed to parse recovery state:', error);
      localStorage.removeItem('f2-recovery-state');
    }
  }
};

const RecoveryDialog = ({ recoveryState, onDecision }) => {
  return (
    <div className="recovery-dialog">
      <h3>🔄 Session Recovery</h3>
      <p>Found unsaved changes from {formatTime(recoveryState.timestamp)}:</p>
      
      <div className="recovery-summary">
        <div>Pending operations: {recoveryState.pendingChanges.length}</div>
        <div>Last action: {recoveryState.lastAction}</div>
      </div>
      
      <div className="recovery-actions">
        <button onClick={() => onDecision('restore')}>
          Restore Changes
        </button>
        <button onClick={() => onDecision('discard')}>
          Start Fresh
        </button>
        <button onClick={() => onDecision('review')}>
          Review Changes First
        </button>
      </div>
    </div>
  );
};
```

## Сценарий 5: Массовая операция с прогрессом

### Обработка больших объемов
```javascript
const handleBulkOperation = async (operations) => {
  const BATCH_SIZE = 50;
  const totalOps = operations.length;
  let completed = 0;
  
  // Показываем прогресс-бар
  const progressId = showProgressDialog({
    title: 'Applying Changes',
    total: totalOps,
    current: 0
  });
  
  try {
    // Обрабатываем батчами
    for (let i = 0; i < operations.length; i += BATCH_SIZE) {
      const batch = operations.slice(i, i + BATCH_SIZE);
      
      // Применяем батч
      await applyOperationBatch(batch);
      
      completed += batch.length;
      
      // Обновляем прогресс
      updateProgress(progressId, {
        current: completed,
        message: `Applied ${completed}/${totalOps} operations`
      });
      
      // Даем UI время на обновление
      await new Promise(resolve => setTimeout(resolve, 10));
    }
    
    hideProgress(progressId);
    showSuccessMessage(`Successfully applied ${totalOps} changes`);
    
  } catch (error) {
    hideProgress(progressId);
    handleBulkOperationError(error, completed, totalOps);
  }
};
```

### Обработка ошибок в массовых операциях
```javascript
const handleBulkOperationError = (error, completed, total) => {
  const remaining = total - completed;
  
  showErrorDialog({
    title: 'Bulk Operation Failed',
    message: `Applied ${completed}/${total} operations before failure.`,
    error: error.message,
    actions: [
      {
        label: 'Retry Remaining',
        action: () => retryFailedOperations()
      },
      {
        label: 'Rollback All',
        action: () => rollbackCompletedOperations()
      },
      {
        label: 'Keep Current State',
        action: () => acceptPartialCompletion()
      }
    ]
  });
};
```

## Сценарий 6: Конкурентное редактирование

### Обнаружение конфликтов
```javascript
const detectConcurrentChanges = async (localChanges) => {
  const serverState = await fetchCurrentState();
  const conflicts = [];
  
  for (const change of localChanges) {
    const serverNode = findNodeInState(serverState, change.nodeId);
    const localNode = findNodeInState(localState, change.nodeId);
    
    if (serverNode.lastModified > change.timestamp) {
      conflicts.push({
        type: 'concurrent_modification',
        nodeId: change.nodeId,
        localChange: change,
        serverState: serverNode,
        conflictType: determineConflictType(change, serverNode)
      });
    }
  }
  
  return conflicts;
};
```

### Разрешение конкурентных изменений
```javascript
const ConcurrentEditDialog = ({ conflicts, onResolve }) => {
  return (
    <div className="concurrent-edit-dialog">
      <h3>🔄 Concurrent Modifications Detected</h3>
      
      {conflicts.map(conflict => (
        <div key={conflict.nodeId} className="conflict-resolution">
          <div className="conflict-header">
            <strong>{conflict.nodeTitle}</strong> was modified by another user
          </div>
          
          <div className="conflict-options">
            <div className="option">
              <h4>Your version:</h4>
              <div>{renderChange(conflict.localChange)}</div>
              <button onClick={() => onResolve(conflict.nodeId, 'local')}>
                Use My Changes
              </button>
            </div>
            
            <div className="option">
              <h4>Server version:</h4>
              <div>{renderServerState(conflict.serverState)}</div>
              <button onClick={() => onResolve(conflict.nodeId, 'server')}>
                Use Server Changes
              </button>
            </div>
            
            <div className="option">
              <h4>Merge:</h4>
              <button onClick={() => onResolve(conflict.nodeId, 'merge')}>
                Try Automatic Merge
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
```

## Метрики и мониторинг

### Отслеживание производительности
```javascript
const performanceMetrics = {
  operationTimes: new Map(),
  
  startOperation(operationId, type) {
    this.operationTimes.set(operationId, {
      type,
      startTime: performance.now(),
      endTime: null
    });
  },
  
  endOperation(operationId) {
    const operation = this.operationTimes.get(operationId);
    if (operation) {
      operation.endTime = performance.now();
      operation.duration = operation.endTime - operation.startTime;
      
      // Логируем медленные операции
      if (operation.duration > 1000) {
        console.warn(`Slow operation detected: ${operation.type} took ${operation.duration}ms`);
      }
    }
  }
};
```

### Аналитика использования
```javascript
const analyticsTracker = {
  trackDragOperation(fromType, toType, distance, duration) {
    // Отправляем анонимные метрики
    analytics.track('drag_operation', {
      from_type: fromType,
      to_type: toType,
      distance_pixels: distance,
      duration_ms: duration,
      timestamp: Date.now()
    });
  },
  
  trackComplexScenario(scenarioType, operationCount) {
    analytics.track('complex_scenario', {
      scenario: scenarioType,
      operations: operationCount,
      timestamp: Date.now()
    });
  }
};
```