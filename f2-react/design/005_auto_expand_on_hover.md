# Сценарий: Автоматическое раскрытие папок при наведении

## Обзор функциональности
Система автоматического раскрытия папок при drag-and-drop для удобного размещения элементов внутри закрытых папок.

## Ключевые параметры
- **Задержка раскрытия**: 500ms
- **Визуальная индикация**: прогресс-индикатор
- **Временное раскрытие**: папки закрываются после завершения операции
- **Умное поведение**: отмена при быстром проходе курсором

## Детальная механика

### 1. Обнаружение наведения на закрытую папку

```javascript
const handleDragOver = (element, evt) => {
  if (!isDragging) return;
  
  const isFolder = element.dataset.nodeType === 'folder';
  const isClosed = !element.hasAttribute('open');
  
  if (isFolder && isClosed) {
    const folderId = element.dataset.nodeId;
    
    // Проверяем, можно ли вообще дропнуть в эту папку
    if (canDropInFolder(draggedItem, folderId)) {
      initiateFolderExpansion(folderId);
    }
  }
};
```

### 2. Инициализация таймера раскрытия

```javascript
const expansionTimers = new Map();
const expansionStartTimes = new Map();

const initiateFolderExpansion = (folderId) => {
  // Если таймер уже запущен, ничего не делаем
  if (expansionTimers.has(folderId)) return;
  
  const startTime = Date.now();
  expansionStartTimes.set(folderId, startTime);
  
  // Сразу показываем визуальную индикацию
  showExpansionProgress(folderId, 0);
  
  // Запускаем анимацию прогресса
  animateExpansionProgress(folderId);
  
  // Ставим таймер на раскрытие
  const timer = setTimeout(() => {
    expandFolder(folderId);
    expansionTimers.delete(folderId);
    expansionStartTimes.delete(folderId);
  }, 500);
  
  expansionTimers.set(folderId, timer);
};
```

### 3. Анимация прогресс-индикатора

```javascript
const animateExpansionProgress = (folderId) => {
  const animate = () => {
    if (!expansionStartTimes.has(folderId)) return;
    
    const elapsed = Date.now() - expansionStartTimes.get(folderId);
    const progress = Math.min(elapsed / 500, 1);
    
    showExpansionProgress(folderId, progress);
    
    if (progress < 1) {
      requestAnimationFrame(animate);
    }
  };
  
  requestAnimationFrame(animate);
};
```

**Визуальный индикатор**:
```css
.folder-expansion-indicator {
  position: absolute;
  left: -20px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  height: 16px;
}

.folder-expansion-indicator svg {
  width: 100%;
  height: 100%;
}

.folder-expansion-indicator circle {
  fill: none;
  stroke: #2196f3;
  stroke-width: 2;
  stroke-dasharray: 44; /* 2 * PI * 7 (radius) */
  stroke-dashoffset: 44;
  transform: rotate(-90deg);
  transform-origin: center;
  transition: stroke-dashoffset 0.5s linear;
}

.folder-expansion-indicator.active circle {
  stroke-dashoffset: 0;
}
```

### 4. Отмена раскрытия при уходе курсора

```javascript
const handleDragLeave = (element) => {
  const folderId = element.dataset.nodeId;
  
  if (expansionTimers.has(folderId)) {
    // Отменяем таймер
    clearTimeout(expansionTimers.get(folderId));
    expansionTimers.delete(folderId);
    expansionStartTimes.delete(folderId);
    
    // Убираем визуальную индикацию
    hideExpansionProgress(folderId);
  }
};
```

### 5. Раскрытие папки

```javascript
const expandFolder = (folderId) => {
  const folder = document.querySelector(`[data-node-id="${folderId}"]`);
  
  // Помечаем как временно раскрытую
  folder.setAttribute('data-temp-expanded', 'true');
  folder.setAttribute('open', 'true');
  
  // Анимация раскрытия
  animateFolderExpansion(folder);
  
  // Добавляем в список временно раскрытых
  tempExpandedFolders.add(folderId);
  
  // Визуальная индикация временного раскрытия
  folder.classList.add('temp-expanded');
};
```

### 6. Визуальные состояния папок

```css
/* Обычная закрытая папка при наведении во время drag */
.folder-closed.drag-hover {
  background: rgba(33, 150, 243, 0.1);
  border: 1px solid #2196f3;
}

/* Папка в процессе ожидания раскрытия */
.folder-expanding {
  background: rgba(33, 150, 243, 0.15);
  border: 1px solid #2196f3;
  position: relative;
}

/* Временно раскрытая папка */
.folder.temp-expanded {
  background: rgba(33, 150, 243, 0.05);
  border: 1px dashed #2196f3;
}

.folder.temp-expanded::before {
  content: '⏱';
  position: absolute;
  right: 8px;
  top: 8px;
  font-size: 12px;
  opacity: 0.5;
}

/* Анимация раскрытия */
@keyframes folder-expand {
  from {
    max-height: 40px;
    opacity: 0.8;
  }
  to {
    max-height: 1000px;
    opacity: 1;
  }
}

.folder-expanding-content {
  animation: folder-expand 0.3s ease-out;
  overflow: hidden;
}
```

### 7. Закрытие временных папок после drop

```javascript
const handleDragEnd = () => {
  // Закрываем все временно раскрытые папки
  tempExpandedFolders.forEach(folderId => {
    const folder = document.querySelector(`[data-node-id="${folderId}"]`);
    
    // Проверяем, не была ли папка раскрыта пользователем до этого
    if (!userExpandedFolders.has(folderId)) {
      // Анимированное закрытие
      animateFolderCollapse(folder).then(() => {
        folder.removeAttribute('open');
        folder.removeAttribute('data-temp-expanded');
        folder.classList.remove('temp-expanded');
      });
    }
  });
  
  // Очищаем списки
  tempExpandedFolders.clear();
  
  // Отменяем все активные таймеры
  expansionTimers.forEach(timer => clearTimeout(timer));
  expansionTimers.clear();
  expansionStartTimes.clear();
};
```

## Сценарии использования

### Сценарий 1: Успешное раскрытие и drop
1. Пользователь начинает drag
2. Наводит на закрытую папку
3. Ждет 500ms → папка раскрывается
4. Перемещает элемент в нужное место внутри
5. Drop → элемент размещен, папка закрывается

### Сценарий 2: Быстрый проход
1. Пользователь начинает drag
2. Быстро проводит курсором через несколько папок
3. Ни одна не раскрывается (таймеры отменяются)
4. Останавливается на нужной папке
5. Только она раскрывается через 500ms

### Сценарий 3: Множественное раскрытие
1. Пользователь перетаскивает элемент
2. Раскрывает папку A (ждет 500ms)
3. Внутри A наводит на вложенную папку B
4. B тоже раскрывается через 500ms
5. Drop → обе папки закрываются в порядке B → A

### Сценарий 4: Отмена операции
1. Несколько папок временно раскрыты
2. Пользователь нажимает ESC
3. Все временные папки закрываются
4. Состояние возвращается к исходному

## Оптимизация UX

### 1. Умная задержка
```javascript
// Адаптивная задержка в зависимости от скорости движения курсора
const calculateDelay = (velocity) => {
  const baseDelay = 500;
  const minDelay = 300;
  const maxDelay = 800;
  
  // Быстрое движение = больше задержка
  // Медленное движение = меньше задержка
  const adjustedDelay = baseDelay + (velocity - 50) * 2;
  
  return Math.max(minDelay, Math.min(maxDelay, adjustedDelay));
};
```

### 2. Предсказание намерений
```javascript
// Если курсор движется к папке по прямой, начинаем раскрытие раньше
const predictFolderTarget = (cursorPath) => {
  const trajectory = calculateTrajectory(cursorPath);
  const targetFolder = findFolderInPath(trajectory);
  
  if (targetFolder && trajectoryConfidence > 0.8) {
    // Начинаем раскрытие с уменьшенной задержкой
    initiateFolderExpansion(targetFolder.id, 300);
  }
};
```

### 3. Запоминание предпочтений
```javascript
// Запоминаем, какие папки пользователь часто открывает при drag
const frequentDropTargets = new Map();

const recordDropTarget = (folderId) => {
  const count = frequentDropTargets.get(folderId) || 0;
  frequentDropTargets.set(folderId, count + 1);
  
  // Часто используемые папки раскрываются быстрее
  if (count > 5) {
    folderExpansionDelays.set(folderId, 300); // вместо 500ms
  }
};
```

## Обработка ошибок

### 1. Защита от зацикливания
```javascript
const MAX_EXPANSION_DEPTH = 5;
let currentExpansionDepth = 0;

const expandFolderSafe = (folderId) => {
  if (currentExpansionDepth >= MAX_EXPANSION_DEPTH) {
    console.warn('Maximum expansion depth reached');
    return;
  }
  
  currentExpansionDepth++;
  expandFolder(folderId);
  // После закрытия уменьшаем счетчик
};
```

### 2. Обработка быстрых действий
```javascript
// Debounce для предотвращения множественных раскрытий
const debouncedExpansion = debounce((folderId) => {
  if (stillHoveringOver(folderId)) {
    expandFolder(folderId);
  }
}, 500);
```