# Сценарий: Перемещение документа между папками

## Начальное состояние
```
📁 Getting Started
  📄 Introduction
  📄 Installation     <- перемещаем этот документ
  📄 Configuration
📁 Advanced Topics (closed)
  📄 Performance
  📄 Security
📁 API Reference
  📄 Overview
  📄 Authentication
```

## Цель операции
Переместить "Installation" из "Getting Started" в "Advanced Topics" между "Performance" и "Security".

## Пошаговое выполнение

### 1. Начало перетаскивания

**Действие**: Пользователь начинает перетаскивать "Installation".

**Состояние**:
```javascript
dragState: {
  isDragging: true,
  draggedItem: {
    id: "installation",
    title: "Installation",
    parentId: "getting-started",
    type: "document"
  },
  originalParent: "getting-started",
  originalIndex: 1
}
```

**Визуально**:
- "Installation" становится полупрозрачным
- Появляется ghost элемент под курсором

### 2. Наведение на закрытую папку

**Действие**: Пользователь наводит ghost элемент на "Advanced Topics" (закрытая папка).

**События**:
```javascript
onHoverFolder: (folder) => {
  if (!folder.open && isDragging) {
    startHoverTimer(folder.id, 500); // 500ms задержка
  }
}
```

**Визуально**:
- Папка "Advanced Topics" подсвечивается синей рамкой
- Появляется индикатор загрузки (circular progress) около иконки папки
- Через 500ms папка автоматически раскрывается

### 3. Автоматическое раскрытие папки

**После 500ms задержки**:
```javascript
autoExpandFolder: (folderId) => {
  // Раскрываем папку
  dispatch({ 
    type: 'EXPAND_FOLDER', 
    payload: { folderId, temporary: true }
  });
  
  // Помечаем как временно раскрытую
  tempExpandedFolders.add(folderId);
}
```

**Новое состояние**:
```
📁 Getting Started
  📄 Introduction
  📄 Installation     <- перетаскивается
  📄 Configuration
📂 Advanced Topics (auto-expanded) ✨
  📄 Performance
  📄 Security
📁 API Reference
  📄 Overview
  📄 Authentication
```

**Визуально**:
- Папка плавно раскрывается (animation: 200ms)
- Содержимое появляется с fade-in эффектом
- Синяя рамка остается вокруг папки

### 4. Позиционирование внутри папки

**Действие**: Пользователь перемещает курсор между "Performance" и "Security".

**События**:
```javascript
onMove: (evt) => {
  // Определяем позицию для вставки
  const dropPosition = calculateDropPosition(evt.clientY);
  // Показываем индикатор между Performance и Security
  showDropIndicator("after-performance");
}
```

**Визуально**:
- Между "Performance" и "Security" появляется горизонтальная синяя линия
- Линия имеет отступ, соответствующий уровню вложенности
- При движении курсора линия перемещается между элементами

### 5. Завершение перемещения (Drop)

**Действие**: Пользователь отпускает кнопку мыши.

**События**:
```javascript
onEnd: (evt) => {
  const change = {
    type: "move",
    nodeId: "installation",
    fromParent: "getting-started",
    toParent: "advanced-topics",
    fromIndex: 1,
    toIndex: 1, // между Performance (0) и Security (1)
    timestamp: Date.now()
  };
  
  // Добавляем в pending changes
  addPendingChange(change);
}
```

### 6. Обновление состояния

**Новая структура**:
```
📁 Getting Started
  📄 Introduction
  📄 Configuration    <- Installation удален отсюда
📂 Advanced Topics
  📄 Performance
  📄 Installation     <- добавлен сюда
  📄 Security
📁 API Reference
  📄 Overview
  📄 Authentication
```

**Обновление путей**:
```javascript
// Старый путь: /getting-started/installation
// Новый путь: /advanced-topics/installation
updateNodePath("installation", "/advanced-topics/installation");
```

**Pending changes**:
```javascript
pendingChanges: {
  moves: [{
    id: "move-001",
    nodeId: "installation",
    fromParent: "getting-started",
    toParent: "advanced-topics",
    fromIndex: 1,
    toIndex: 1,
    oldPath: "/getting-started/installation",
    newPath: "/advanced-topics/installation",
    timestamp: 1643723456789
  }],
  renames: [],
  reorders: []
}
```

### 7. Визуальная обратная связь

- Появляется оранжевая полоса слева
- Пульсирующая точка показывает "1"
- В панели изменений:
  ```
  ➡️ Installation: Getting Started → Advanced Topics
  ```

### 8. Закрытие временно раскрытых папок

**После завершения drag операции**:
```javascript
cleanupDragState: () => {
  // Закрываем временно раскрытые папки
  tempExpandedFolders.forEach(folderId => {
    if (!permanentlyExpandedFolders.has(folderId)) {
      dispatch({ type: 'COLLAPSE_FOLDER', payload: { folderId }});
    }
  });
  tempExpandedFolders.clear();
}
```

## Особенности реализации

### 1. Hover Timer Management
```javascript
const hoverTimers = new Map();

const startHoverTimer = (folderId, delay) => {
  // Отменяем предыдущий таймер если есть
  if (hoverTimers.has(folderId)) {
    clearTimeout(hoverTimers.get(folderId));
  }
  
  const timer = setTimeout(() => {
    autoExpandFolder(folderId);
    hoverTimers.delete(folderId);
  }, delay);
  
  hoverTimers.set(folderId, timer);
};

const cancelHoverTimer = (folderId) => {
  if (hoverTimers.has(folderId)) {
    clearTimeout(hoverTimers.get(folderId));
    hoverTimers.delete(folderId);
  }
};
```

### 2. Визуальный индикатор ожидания
```css
.folder-expanding::before {
  content: '';
  position: absolute;
  width: 16px;
  height: 16px;
  border: 2px solid #2196f3;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  left: -20px;
  top: 50%;
  transform: translateY(-50%);
}

@keyframes spin {
  to { transform: translateY(-50%) rotate(360deg); }
}
```

### 3. Определение позиции для вставки
```javascript
const calculateDropPosition = (clientY, container) => {
  const items = container.querySelectorAll('.navigation-item');
  let dropIndex = items.length; // по умолчанию в конец
  
  for (let i = 0; i < items.length; i++) {
    const rect = items[i].getBoundingClientRect();
    const midpoint = rect.top + rect.height / 2;
    
    if (clientY < midpoint) {
      dropIndex = i;
      break;
    }
  }
  
  return dropIndex;
};
```

## Альтернативные сценарии

### 1. Отмена auto-expand
Если пользователь быстро уводит курсор с папки до истечения 500ms:
- Таймер отменяется
- Папка не раскрывается
- Визуальный индикатор исчезает

### 2. Drop на саму папку (не внутрь)
Если пользователь отпускает на заголовке папки:
- Элемент добавляется в конец содержимого папки
- Папка автоматически раскрывается для показа результата

### 3. Быстрое перемещение через несколько папок
- Все hover таймеры отменяются при уходе с папки
- Раскрывается только та папка, над которой курсор задержался 500ms+

### 4. Перемещение в пустую папку
- Показывается специальный drop zone внутри пустой папки
- Текст-подсказка: "Drop here to add first item"