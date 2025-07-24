# Сценарий: Вложение папки в папку

## Начальное состояние
```
📁 Documentation
  📁 Getting Started      <- перемещаем эту папку целиком
    📄 Introduction
    📄 Installation
    📁 Quick Examples
      📄 Hello World
      📄 Basic Setup
  📁 Advanced Topics
    📄 Performance
    📄 Security
  📁 API Reference
    📄 Overview
```

## Цель операции
Переместить папку "Getting Started" со всем содержимым внутрь "Advanced Topics".

## Пошаговое выполнение

### 1. Начало перетаскивания папки

**Действие**: Пользователь начинает перетаскивать "Getting Started" за summary элемент.

**События**:
```javascript
onStart: (evt) => {
  // При перетаскивании summary, берем всю папку
  const folderElement = evt.item.parentElement; // <details>
  const folderId = folderElement.dataset.nodeId;
  
  // Собираем информацию о поддереве
  const subtreeInfo = collectSubtreeInfo(folderId);
}
```

**Анализ поддерева**:
```javascript
subtreeInfo: {
  nodeId: "getting-started",
  type: "folder",
  totalNodes: 5, // папка + 4 документа внутри
  maxDepth: 2,  // Quick Examples добавляет еще один уровень
  structure: {
    folders: 1,
    documents: 4
  }
}
```

**Визуально**:
- Вся папка "Getting Started" становится полупрозрачной
- Ghost элемент показывает папку с индикатором количества элементов: "📁 Getting Started (5)"

### 2. Проверка валидности при наведении

**Действие**: Пользователь наводит на "Advanced Topics".

**Валидация глубины**:
```javascript
validateFolderDrop: (draggedFolder, targetFolder) => {
  const targetDepth = getNodeDepth(targetFolder); // 1
  const draggedSubtreeDepth = subtreeInfo.maxDepth; // 2
  const resultingDepth = targetDepth + draggedSubtreeDepth + 1; // 4
  
  return {
    valid: resultingDepth <= MAX_DEPTH, // false если MAX_DEPTH = 3
    resultingDepth,
    maxAllowed: MAX_DEPTH
  };
}
```

**Визуально при превышении глубины**:
- Курсор становится красным (not-allowed)
- Папка-цель подсвечивается красной рамкой
- Появляется tooltip: "Cannot drop here: maximum depth (3) would be exceeded"

### 3. Корректное перемещение (в папку без превышения глубины)

**Действие**: Пользователь наводит на "API Reference" (глубина позволяет).

**Валидация**:
```javascript
// API Reference на уровне 1, Getting Started имеет глубину 2
// Результат: 1 + 2 + 1 = 4, но если MAX_DEPTH увеличен или структура другая
validation: {
  valid: true,
  circular: false, // Getting Started не является предком API Reference
  depthOk: true
}
```

**Автоматическое раскрытие**:
- Через 500ms "API Reference" автоматически раскрывается
- Показывается содержимое для точного позиционирования

### 4. Позиционирование и drop

**Действие**: Пользователь отпускает папку после "Overview" в "API Reference".

**События**:
```javascript
onEnd: (evt) => {
  const moveOperation = {
    type: "move",
    nodeId: "getting-started",
    nodeType: "folder",
    fromParent: "documentation",
    toParent: "api-reference", 
    fromIndex: 0,
    toIndex: 1, // после Overview
    affectedNodes: ["getting-started", "introduction", "installation", 
                   "quick-examples", "hello-world", "basic-setup"],
    timestamp: Date.now()
  };
}
```

### 5. Обновление структуры

**Новое состояние**:
```
📁 Documentation
  📁 Advanced Topics
    📄 Performance
    📄 Security
  📁 API Reference
    📄 Overview
    📁 Getting Started     <- перемещена сюда
      📄 Introduction
      📄 Installation
      📁 Quick Examples
        📄 Hello World
        📄 Basic Setup
```

**Массовое обновление путей**:
```javascript
pathUpdates: [
  { old: "/getting-started", new: "/api-reference/getting-started" },
  { old: "/getting-started/introduction", new: "/api-reference/getting-started/introduction" },
  { old: "/getting-started/installation", new: "/api-reference/getting-started/installation" },
  { old: "/getting-started/quick-examples", new: "/api-reference/getting-started/quick-examples" },
  { old: "/getting-started/quick-examples/hello-world", new: "/api-reference/getting-started/quick-examples/hello-world" },
  { old: "/getting-started/quick-examples/basic-setup", new: "/api-reference/getting-started/quick-examples/basic-setup" }
]
```

### 6. Визуальная обратная связь

**Индикатор изменений**:
```javascript
<ChangesPanel>
  <div className="change-item complex">
    <span className="icon">📁➡️📁</span>
    <span className="description">
      Getting Started (with 5 items) → API Reference
    </span>
    <details className="affected-items">
      <summary>Affected paths (6)</summary>
      <ul>
        <li>/getting-started → /api-reference/getting-started</li>
        <li>/getting-started/introduction → ...</li>
        {/* остальные пути */}
      </ul>
    </details>
  </div>
</ChangesPanel>
```

## Особенности реализации

### 1. Предварительный просмотр структуры
```javascript
const StructurePreview = ({ draggedFolder, targetFolder }) => {
  const preview = generateTreePreview(draggedFolder, targetFolder);
  
  return (
    <div className="structure-preview">
      <div className="preview-title">Result preview:</div>
      <div className="preview-tree">
        {renderTree(preview, { highlight: draggedFolder.id })}
      </div>
      <div className="preview-stats">
        Total depth: {preview.maxDepth} / {MAX_DEPTH}
      </div>
    </div>
  );
};
```

### 2. Оптимизация для больших поддеревьев
```javascript
// Батчинг обновлений путей
const batchUpdatePaths = async (pathUpdates) => {
  // Группируем по 50 обновлений
  const chunks = chunk(pathUpdates, 50);
  
  for (const chunk of chunks) {
    await updatePaths(chunk);
    // Обновляем прогресс
    updateProgress(processed / total * 100);
  }
};
```

### 3. Визуализация сложности операции
```css
.folder-drag-ghost {
  position: relative;
}

.folder-drag-ghost::after {
  content: attr(data-item-count);
  position: absolute;
  top: -8px;
  right: -8px;
  background: #2196f3;
  color: white;
  border-radius: 10px;
  padding: 2px 6px;
  font-size: 11px;
  font-weight: bold;
}

/* Для больших поддеревьев */
.folder-drag-ghost.large-tree::after {
  background: #ff9800;
  animation: pulse 1s infinite;
}
```

## Проверки и ограничения

### 1. Циклические зависимости
```javascript
// Нельзя переместить папку внутрь себя или своих потомков
if (isDescendantOf(targetFolder, draggedFolder)) {
  return {
    valid: false,
    error: "Cannot move a folder into itself or its descendants"
  };
}
```

### 2. Права доступа
```javascript
// Проверка прав на перемещение всех вложенных элементов
const checkPermissions = async (folder) => {
  const allNodes = collectAllNodes(folder);
  const permissions = await checkBulkPermissions(allNodes);
  
  return permissions.every(p => p.canMove);
};
```

### 3. Размер операции
```javascript
// Предупреждение для больших операций
if (subtreeInfo.totalNodes > 100) {
  showWarning({
    title: "Large operation",
    message: `This will move ${subtreeInfo.totalNodes} items. Continue?`,
    actions: ["Continue", "Cancel"]
  });
}
```

## Альтернативные сценарии

### 1. Перемещение в корень
- Папка становится элементом верхнего уровня
- Проверка максимального количества корневых элементов

### 2. Слияние папок
- При drop папки на папку с таким же именем
- Показ диалога: "Merge folders?" / "Replace?" / "Keep both?"

### 3. Отмена большой операции
- Возможность отменить во время применения изменений
- Откат уже примененных изменений