# Сценарий: Перемещение страницы вниз на том же уровне

## Начальное состояние
```
📁 Getting Started
  📄 Introduction
  📄 Installation     <- перемещаем эту страницу
  📄 Configuration
  📄 First Steps
```

## Пошаговое выполнение операции

### 1. Начало перетаскивания (Drag Start)

**Действие пользователя**: Наводит курсор на "Installation", 
появляется drag handle (⋮⋮), зажимает и начинает тащить.

**События SortableJS**:
```javascript
onStart: (evt) => {
  // evt.item = <a>Installation</a>
  // evt.oldIndex = 1
  // evt.from = container элемент Getting Started
}
```

**Изменения состояния**:
```javascript
dragState: {
  isDragging: true,
  draggedItem: {
    id: "installation",
    title: "Installation",
    type: "document",
    parentId: "getting-started",
    order: 1
  },
  originalParent: "getting-started",
  originalIndex: 1
}
```

**Визуальные изменения**:
- Элемент "Installation" становится полупрозрачным (opacity: 0.4)
- Появляется ghost элемент под курсором
- Оригинальный элемент скрывается (opacity: 0)

### 2. Перемещение курсора (Drag Over)

**Действие пользователя**: Перемещает курсор вниз, проходя мимо "Configuration".

**События**:
```javascript
onMove: (evt) => {
  // evt.related = <a>Configuration</a>
  // evt.willInsertAfter = true
}
```

**Визуальные изменения**:
- Между "Configuration" и "First Steps" появляется горизонтальная синяя линия (drop indicator)
- Линия показывает, куда будет вставлен элемент
- Отступ линии соответствует текущему уровню вложенности

### 3. Отпускание элемента (Drop)

**Действие пользователя**: Отпускает кнопку мыши между "Configuration" и "First Steps".

**События SortableJS**:
```javascript
onEnd: (evt) => {
  // evt.item = <a>Installation</a>
  // evt.to = container элемент Getting Started (тот же)
  // evt.from = container элемент Getting Started
  // evt.oldIndex = 1
  // evt.newIndex = 2
}
```

### 4. Обработка изменения

**Определение типа операции**:
```javascript
const operation = determineOperation(evt);
// Результат: { type: 'reorder', sameParent: true }
```

**Создание записи изменения**:
```javascript
const change = {
  id: "change-001",
  type: "reorder",
  nodeId: "installation",
  parentId: "getting-started",
  fromIndex: 1,
  toIndex: 2,
  timestamp: 1643723456789
};
```

**Обновление pendingChanges**:
```javascript
pendingChanges: {
  moves: [],
  renames: [],
  reorders: [{
    id: "change-001",
    nodeId: "installation",
    parentId: "getting-started", 
    fromIndex: 1,
    toIndex: 2,
    timestamp: 1643723456789
  }],
  conversions: []
}
```

### 5. Обновление UI

**Оптимистичное обновление дерева**:
```javascript
// Новый порядок в navigationTree:
[
  { title: "Introduction", order: 0 },
  { title: "Configuration", order: 1 },    // был order: 2
  { title: "Installation", order: 2 },     // был order: 1
  { title: "First Steps", order: 3 }
]
```

**Визуальные индикаторы**:
- Появляется оранжевая полоса слева (4px width)
- Появляется пульсирующая точка с цифрой "1" (количество изменений)
- Кнопка Save становится активной и показывает "Save Changes (1)"

### 6. Отображение в панели изменений

```javascript
<ChangesPanel>
  <div className="change-group">
    <h4>Reorders (1)</h4>
    <div className="change-item">
      <span className="icon">↕️</span>
      <span className="description">
        Installation: position 2 → 3 in Getting Started
      </span>
      <button className="undo">↶</button>
    </div>
  </div>
</ChangesPanel>
```

## Конечное состояние
```
📁 Getting Started
  📄 Introduction
  📄 Configuration
  📄 Installation     <- новая позиция
  📄 First Steps
```

## Особенности реализации

### 1. Оптимизация для простого reorder
- Не создаем запись в `moves`, только в `reorders`
- Минимальный payload для бэкенда
- Быстрое применение изменений

### 2. Визуальная плавность
- CSS transitions для всех перемещений (200ms)
- Анимация вставки на новое место
- Плавное исчезновение drop indicator

### 3. Возможность отмены
- Клик на кнопку ↶ возвращает элемент на исходную позицию
- Удаляет запись из pendingChanges
- Обновляет счетчик изменений

### 4. Валидация
- Проверка, что элемент действительно изменил позицию
- Игнорирование drop на то же место
- Проверка прав доступа (если применимо)

## Альтернативные сценарии

### Если пользователь передумал
- ESC во время перетаскивания = отмена
- Drop за пределами валидной зоны = возврат на место
- Визуальная анимация возврата

### Если есть другие pending changes
- Новое изменение добавляется в список
- Проверка на конфликты с существующими изменениями
- Возможность просмотра всех изменений перед сохранением
