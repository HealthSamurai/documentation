# Сценарий: Точное позиционирование в папке

## Обзор
Система для точного размещения элементов в любом месте папки: в начале, между существующими элементами, или в конце.

## Ключевые принципы
- Показ всех возможных позиций для вставки
- Адаптивные drop-зоны в зависимости от размера папки
- Автопрокрутка длинных списков
- Визуальное предпросмотр результата

## Детальная механика

### 1. Инициализация drop-зон в папке

```javascript
const initializeDropZones = (folderElement) => {
  const children = folderElement.querySelectorAll('.navigation-item');
  const dropZones = [];
  
  // Зона перед первым элементом
  dropZones.push({
    id: `${folderElement.id}-start`,
    type: 'before',
    index: 0,
    element: children[0],
    rect: calculateBeforeRect(children[0])
  });
  
  // Зоны между элементами
  for (let i = 0; i < children.length - 1; i++) {
    dropZones.push({
      id: `${folderElement.id}-between-${i}`,
      type: 'between',
      index: i + 1,
      prevElement: children[i],
      nextElement: children[i + 1],
      rect: calculateBetweenRect(children[i], children[i + 1])
    });
  }
  
  // Зона после последнего элемента
  dropZones.push({
    id: `${folderElement.id}-end`,
    type: 'after',
    index: children.length,
    element: children[children.length - 1],
    rect: calculateAfterRect(children[children.length - 1])
  });
  
  return dropZones;
};
```

### 2. Адаптивная высота drop-зон

```javascript
const calculateDropZoneHeight = (context) => {
  const baseHeight = 8; // минимальная высота
  const contextHeight = context.folderHeight / context.itemCount;
  
  // Адаптируем в зависимости от плотности контента
  if (contextHeight > 60) {
    return Math.min(contextHeight * 0.3, 20); // до 30% от высоты элемента
  } else if (contextHeight > 40) {
    return 12;
  } else {
    return baseHeight; // минимальная для плотных списков
  }
};
```

### 3. Визуализация drop-зон

```css
/* Базовая drop-зона */
.drop-zone {
  position: absolute;
  left: 0;
  right: 0;
  height: 8px;
  background: transparent;
  transition: all 0.2s ease;
  z-index: 100;
}

/* Активная drop-зона при наведении */
.drop-zone.active {
  background: rgba(33, 150, 243, 0.1);
  border: 2px solid #2196f3;
  border-radius: 4px;
  height: 12px;
}

/* Линия-индикатор */
.drop-zone.active::before {
  content: '';
  position: absolute;
  left: 8px;
  right: 8px;
  top: 50%;
  height: 2px;
  background: #2196f3;
  transform: translateY(-50%);
}

/* Круглые маркеры на концах */
.drop-zone.active::after {
  content: '';
  position: absolute;
  left: 8px;
  top: 50%;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #2196f3;
  transform: translateY(-50%);
  box-shadow: calc(100% - 16px) 0 0 0 #2196f3;
}

/* Специальная зона для начала списка */
.drop-zone.start-zone {
  border-radius: 8px 8px 4px 4px;
}

/* Специальная зона для конца списка */
.drop-zone.end-zone {
  border-radius: 4px 4px 8px 8px;
}
```

### 4. Обработка длинных списков с прокруткой

```javascript
const handleScrollableFolders = (folderElement) => {
  const folderHeight = folderElement.clientHeight;
  const scrollHeight = folderElement.scrollHeight;
  
  if (scrollHeight > folderHeight) {
    // Папка имеет прокрутку
    initializeAutoScroll(folderElement);
  }
};

const initializeAutoScroll = (container) => {
  const SCROLL_ZONE_HEIGHT = 30; // пикселей от края
  const SCROLL_SPEED = 5; // пикселей за кадр
  
  const handleDragMove = (evt) => {
    const rect = container.getBoundingClientRect();
    const relativeY = evt.clientY - rect.top;
    
    // Зона прокрутки вверх
    if (relativeY < SCROLL_ZONE_HEIGHT) {
      const intensity = (SCROLL_ZONE_HEIGHT - relativeY) / SCROLL_ZONE_HEIGHT;
      scrollContainer(container, -SCROLL_SPEED * intensity);
    }
    // Зона прокрутки вниз
    else if (relativeY > rect.height - SCROLL_ZONE_HEIGHT) {
      const intensity = (relativeY - (rect.height - SCROLL_ZONE_HEIGHT)) / SCROLL_ZONE_HEIGHT;
      scrollContainer(container, SCROLL_SPEED * intensity);
    }
  };
  
  // Плавная прокрутка
  let scrollAnimation;
  const scrollContainer = (element, speed) => {
    if (scrollAnimation) cancelAnimationFrame(scrollAnimation);
    
    scrollAnimation = requestAnimationFrame(() => {
      element.scrollTop += speed;
      // Обновляем позиции drop-зон после прокрутки
      updateDropZonePositions(element);
    });
  };
};
```

### 5. Числовые индикаторы позиций

```javascript
const PositionIndicators = ({ dropZones, activeZone }) => {
  return (
    <div className="position-indicators">
      {dropZones.map((zone, index) => (
        <div
          key={zone.id}
          className={`position-indicator ${zone.id === activeZone?.id ? 'active' : ''}`}
          style={{
            top: zone.rect.top + zone.rect.height / 2,
            left: zone.rect.left - 25
          }}
        >
          {index + 1}
        </div>
      ))}
    </div>
  );
};
```

**CSS для индикаторов**:
```css
.position-indicator {
  position: fixed;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: bold;
  color: #666;
  opacity: 0;
  transition: all 0.2s;
  z-index: 1000;
}

.position-indicator.active {
  opacity: 1;
  border-color: #2196f3;
  background: #2196f3;
  color: white;
  transform: scale(1.2);
}

/* Показываем все индикаторы при drag */
.dragging .position-indicator {
  opacity: 0.7;
}
```

### 6. Предпросмотр результата

```javascript
const DropPreview = ({ dropZone, draggedItem, folderItems }) => {
  const previewItems = [...folderItems];
  previewItems.splice(dropZone.index, 0, draggedItem);
  
  return (
    <div className="drop-preview-overlay">
      <div className="preview-title">Preview:</div>
      <div className="preview-list">
        {previewItems.map((item, index) => (
          <div
            key={item.id}
            className={`preview-item ${item.id === draggedItem.id ? 'new-item' : ''}`}
          >
            <span className="item-number">{index + 1}.</span>
            <span className="item-title">{item.title}</span>
          </div>
        ))}
      </div>
    </div>
  );
};
```

**CSS для предпросмотра**:
```css
.drop-preview-overlay {
  position: fixed;
  top: 50%;
  right: 20px;
  transform: translateY(-50%);
  background: white;
  border: 1px solid #2196f3;
  border-radius: 8px;
  padding: 12px;
  max-width: 300px;
  max-height: 400px;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  z-index: 1001;
}

.preview-title {
  font-weight: bold;
  margin-bottom: 8px;
  color: #2196f3;
}

.preview-item {
  display: flex;
  align-items: center;
  padding: 4px 0;
  font-size: 13px;
}

.preview-item.new-item {
  background: rgba(76, 175, 80, 0.1);
  border-radius: 4px;
  padding: 4px 8px;
  border: 1px solid #4caf50;
}

.item-number {
  color: #666;
  margin-right: 8px;
  min-width: 20px;
}
```

### 7. Обработка edge cases

#### Пустая папка
```javascript
const handleEmptyFolder = (folderElement) => {
  const emptyDropZone = {
    id: `${folderElement.id}-empty`,
    type: 'empty',
    index: 0,
    rect: {
      top: folderElement.offsetTop + 40,
      left: folderElement.offsetLeft + 20,
      width: folderElement.offsetWidth - 40,
      height: 60
    }
  };
  
  return [emptyDropZone];
};
```

#### Очень длинный список
```javascript
const optimizeForLongLists = (dropZones) => {
  if (dropZones.length > 50) {
    // Показываем только зоны рядом с курсором
    return dropZones.filter((zone, index) => {
      const cursorIndex = getCurrentCursorIndex();
      return Math.abs(index - cursorIndex) <= 10;
    });
  }
  return dropZones;
};
```

#### Вложенные папки
```javascript
const handleNestedFolders = (evt) => {
  // Определяем уровень вложенности по горизонтальной позиции
  const indentLevel = Math.floor((evt.clientX - containerLeft) / INDENT_WIDTH);
  
  // Находим подходящую папку на этом уровне
  const targetFolder = findFolderAtLevel(evt.target, indentLevel);
  
  return initializeDropZones(targetFolder);
};
```

## Сценарии использования

### Сценарий 1: Вставка в середину списка
1. Пользователь перетаскивает элемент
2. Наводит на папку → она раскрывается
3. Перемещает курсор между 3-м и 4-м элементом
4. Появляется drop-зона с линией и номером "4"
5. Drop → элемент вставляется на позицию 4

### Сценарий 2: Длинный список с прокруткой
1. Папка содержит 100 элементов
2. Пользователь начинает drag
3. Наводит на папку → она раскрывается
4. Двигает курсор к нижней части → автоматическая прокрутка
5. Находит нужное место → drop на позицию 87

### Сценарий 3: Множественные уровни
1. Перетаскивание в папку с вложенными папками
2. Курсор определяет уровень по X-координате
3. Показываются drop-зоны только для выбранного уровня
4. Возможность изменить уровень движением влево/вправо

## Оптимизация производительности

### 1. Виртуализация drop-зон
```javascript
const useVirtualDropZones = (allZones, viewportRect) => {
  return useMemo(() => {
    return allZones.filter(zone =>
      zone.rect.top < viewportRect.bottom &&
      zone.rect.bottom > viewportRect.top
    );
  }, [allZones, viewportRect]);
};
```

### 2. Debounce обновлений
```javascript
const debouncedUpdateZones = useCallback(
  debounce((folderElement) => {
    setDropZones(initializeDropZones(folderElement));
  }, 100),
  []
);
```

### 3. Efficient collision detection
```javascript
const findActiveZone = (cursorY, zones) => {
  // Бинарный поиск для быстрого нахождения зоны
  let left = 0;
  let right = zones.length - 1;
  
  while (left <= right) {
    const mid = Math.floor((left + right) / 2);
    const zone = zones[mid];
    
    if (cursorY >= zone.rect.top && cursorY <= zone.rect.bottom) {
      return zone;
    } else if (cursorY < zone.rect.top) {
      right = mid - 1;
    } else {
      left = mid + 1;
    }
  }
  
  return null;
};
```