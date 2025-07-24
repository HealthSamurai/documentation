# Сценарий: Превращение документа в папку

## Обзор
Пользователь может создать новую папку, перетащив один документ на другой. Документ-цель становится папкой, а перетаскиваемый документ становится его первым дочерним элементом.

## Начальное состояние
```
📁 Getting Started
  📄 Introduction
  📄 Installation
  📄 Configuration     <- целевой документ
  📄 First Steps       <- перетаскиваемый документ
📁 Advanced Topics
  📄 Performance
```

## Пошаговое выполнение

### 1. Начало перетаскивания

**Действие**: Пользователь начинает перетаскивать "First Steps".

**Состояние**:
```javascript
dragState: {
  isDragging: true,
  draggedItem: {
    id: "first-steps",
    title: "First Steps",
    type: "document",
    parentId: "getting-started"
  }
}
```

### 2. Наведение на другой документ

**Действие**: Пользователь наводит на документ "Configuration".

**Определение намерения**:
```javascript
const detectDropIntent = (target, draggedItem) => {
  if (target.type === 'document' && draggedItem.type === 'document') {
    return {
      intent: 'CREATE_FOLDER',
      targetWillBecome: 'folder',
      draggedWillBecome: 'child'
    };
  }
};
```

**Визуальная индикация**:
```css
/* Документ, который станет папкой */
.document-will-become-folder {
  border: 2px dashed #4caf50;
  background: rgba(76, 175, 80, 0.1);
  position: relative;
  transition: all 0.2s ease;
}

/* Анимированная иконка превращения */
.document-will-become-folder::before {
  content: '📄';
  position: absolute;
  left: -25px;
  animation: morph-to-folder 1s ease-in-out infinite;
}

@keyframes morph-to-folder {
  0%, 100% { content: '📄'; opacity: 1; }
  50% { content: '📁'; opacity: 0.8; transform: scale(1.1); }
}

/* Индикатор будущей структуры */
.folder-preview {
  position: absolute;
  left: 100%;
  top: 0;
  margin-left: 20px;
  padding: 8px;
  background: white;
  border: 1px solid #4caf50;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  font-size: 12px;
  white-space: nowrap;
}

.folder-preview::before {
  content: '→';
  position: absolute;
  left: -15px;
  top: 50%;
  transform: translateY(-50%);
  color: #4caf50;
}
```

**Превью будущей структуры**:
```html
<div class="folder-preview">
  📁 Configuration
    └─ 📄 First Steps
</div>
```

### 3. Подтверждение операции (Drop)

**Действие**: Пользователь отпускает "First Steps" на "Configuration".

**Валидация**:
```javascript
const validateFolderCreation = (source, target) => {
  // Нельзя дропнуть документ сам на себя
  if (source.id === target.id) {
    return { valid: false, reason: 'Cannot drop item on itself' };
  }
  
  // Проверяем, не существует ли уже папка с таким именем
  const parentNode = findNode(target.parentId);
  const duplicate = parentNode.children.find(
    child => child.type === 'folder' && child.title === target.title
  );
  
  if (duplicate) {
    return { 
      valid: false, 
      reason: 'A folder with this name already exists',
      suggestion: 'MERGE_WITH_EXISTING'
    };
  }
  
  return { valid: true };
};
```

### 4. Создание операции конвертации

```javascript
const createConversionOperation = (source, target) => {
  return {
    type: 'conversion',
    operations: [
      {
        action: 'CONVERT_TO_FOLDER',
        nodeId: target.id,
        fromType: 'document',
        toType: 'folder',
        preserveContent: true // сохраняем контент документа
      },
      {
        action: 'MOVE',
        nodeId: source.id,
        fromParent: source.parentId,
        toParent: target.id, // новая папка
        toIndex: 0 // первый элемент в новой папке
      }
    ],
    timestamp: Date.now()
  };
};
```

### 5. Обновление структуры

**Результат операции**:
```
📁 Getting Started
  📄 Introduction
  📄 Installation
  📁 Configuration     <- стал папкой
    📄 First Steps     <- перемещен внутрь
📁 Advanced Topics
  📄 Performance
```

**Изменения в данных**:
```javascript
// До:
{
  id: "configuration",
  type: "document",
  title: "Configuration",
  content: "Configuration content...",
  children: null
}

// После:
{
  id: "configuration",
  type: "folder",
  title: "Configuration",
  originalContent: "Configuration content...", // сохраняем
  hasOwnContent: true, // флаг для папок с собственным контентом
  children: [
    {
      id: "first-steps",
      type: "document",
      title: "First Steps",
      content: "First steps content..."
    }
  ]
}
```

### 6. Отображение в UI

**Папка с собственным контентом**:
```javascript
const FolderWithContent = ({ folder }) => {
  return (
    <details className="folder-with-content">
      <summary>
        <span className="folder-icon">📁</span>
        <span className="folder-title">{folder.title}</span>
        {folder.hasOwnContent && (
          <span className="own-content-indicator" title="This folder has its own content">
            📝
          </span>
        )}
      </summary>
      <div className="folder-children">
        {folder.hasOwnContent && (
          <a className="folder-own-content" href={`${folder.path}/index`}>
            📄 {folder.title} (Overview)
          </a>
        )}
        {folder.children.map(child => <Node key={child.id} node={child} />)}
      </div>
    </details>
  );
};
```

### 7. Запись изменений

```javascript
pendingChanges: {
  conversions: [{
    id: "conv-001",
    nodeId: "configuration",
    fromType: "document",
    toType: "folder",
    preservedContent: true,
    timestamp: 1643723456789
  }],
  moves: [{
    id: "move-001",
    nodeId: "first-steps",
    fromParent: "getting-started",
    toParent: "configuration",
    fromIndex: 3,
    toIndex: 0,
    timestamp: 1643723456790
  }]
}
```

## Особые случаи

### 1. Конфликт имен
Если папка с таким именем уже существует:

```javascript
const handleNameConflict = async (source, target, existingFolder) => {
  const choice = await showDialog({
    title: "Folder already exists",
    message: `A folder named "${target.title}" already exists. What would you like to do?`,
    options: [
      { id: 'merge', label: 'Add to existing folder', icon: '🔀' },
      { id: 'rename', label: 'Create with different name', icon: '✏️' },
      { id: 'cancel', label: 'Cancel', icon: '❌' }
    ]
  });
  
  switch (choice) {
    case 'merge':
      return { action: 'MOVE_TO_EXISTING', targetId: existingFolder.id };
    case 'rename':
      const newName = await promptNewName(target.title);
      return { action: 'CREATE_WITH_NAME', newName };
    default:
      return { action: 'CANCEL' };
  }
};
```

### 2. Множественное превращение
Если на документ дропают несколько элементов подряд:

```javascript
// Первый drop: Configuration становится папкой
// Второй drop: добавляем в уже созданную папку
const handleSubsequentDrops = (target, source) => {
  if (target.type === 'folder' && target.wasRecentlyConverted) {
    // Просто добавляем в папку
    return { action: 'ADD_TO_FOLDER', folderId: target.id };
  }
};
```

### 3. Обратная операция
Превращение папки обратно в документ (когда удаляют последний элемент):

```javascript
const checkFolderToDocument = (folder) => {
  if (folder.children.length === 0 && folder.hasOwnContent) {
    return {
      suggestion: 'CONVERT_BACK_TO_DOCUMENT',
      message: 'This folder is now empty. Convert back to document?'
    };
  }
};
```

## Анимации и переходы

### 1. Морфинг иконки
```javascript
const IconMorph = ({ from, to, duration = 300 }) => {
  const [currentIcon, setCurrentIcon] = useState(from);
  
  useEffect(() => {
    const timer = setTimeout(() => setCurrentIcon(to), duration / 2);
    return () => clearTimeout(timer);
  }, [to, duration]);
  
  return (
    <span className="icon-morph" style={{ transition: `all ${duration}ms` }}>
      {currentIcon}
    </span>
  );
};
```

### 2. Плавное появление структуры
```css
@keyframes structure-appear {
  from {
    opacity: 0;
    max-height: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    max-height: 200px;
    transform: translateX(0);
  }
}

.folder-children-new {
  animation: structure-appear 0.3s ease-out;
  overflow: hidden;
}
```

## Отмена операции

При нажатии отмены в панели изменений:
1. Папка превращается обратно в документ
2. Дочерний элемент возвращается на исходное место
3. Восстанавливается оригинальный контент документа

```javascript
const revertConversion = (conversionChange, moveChange) => {
  // 1. Возвращаем документ на место
  moveNode(moveChange.nodeId, moveChange.fromParent, moveChange.fromIndex);
  
  // 2. Превращаем папку обратно в документ
  updateNode(conversionChange.nodeId, {
    type: 'document',
    content: conversionChange.preservedContent,
    children: null
  });
};
```