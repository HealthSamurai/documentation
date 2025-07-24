# Риски реализации F2 Drag-and-Drop

## Обзор критических рисков

Анализ потенциальных подводных камней при реализации F2 navigation с drag-and-drop функциональностью.

**Приоритеты**: 🔥 Критично | ⚠️ Важно | 🔴 Нужно учесть

---

## 🔥 КРИТИЧНЫЙ: React vs SortableJS DOM конфликты

### Проблема
SortableJS напрямую манипулирует DOM элементами, в то время как React управляет Virtual DOM. Это может привести к:
- Потере React компонентов из Virtual DOM
- Inconsistent state между DOM и React
- Неработающие event handlers после drag операций

### Конкретные сценарии
```javascript
// Проблемный сценарий:
const NavigationItem = ({ item }) => {
  const [isActive, setIsActive] = useState(false);
  
  // После SortableJS drag этот handler может перестать работать
  const handleClick = () => setIsActive(!isActive);
  
  return <a onClick={handleClick}>{item.title}</a>;
};
```

### Решения

#### 1. Использование refs для sync
```javascript
const useSortableWithReact = (containerRef, options) => {
  useEffect(() => {
    if (!containerRef.current) return;
    
    const sortable = Sortable.create(containerRef.current, {
      ...options,
      onEnd: (evt) => {
        // Форсируем React re-render после DOM manipulation
        const event = new CustomEvent('sortable-changed', {
          detail: { from: evt.from, to: evt.to, item: evt.item }
        });
        evt.to.dispatchEvent(event);
        
        // Вызываем оригинальный handler
        if (options.onEnd) options.onEnd(evt);
      }
    });
    
    return () => sortable.destroy();
  }, []);
};
```

#### 2. React-SortableJS wrapper
```javascript
import { ReactSortable } from 'react-sortablejs';

const NavigationContainer = ({ items, setItems }) => {
  return (
    <ReactSortable 
      list={items} 
      setList={setItems}
      ghostClass="sortable-ghost"
    >
      {items.map(item => (
        <NavigationItem key={item.id} item={item} />
      ))}
    </ReactSortable>
  );
};
```

#### 3. State reconciliation
```javascript
const reconcileStateAfterDrag = (evt) => {
  // Принудительно обновляем React state основываясь на DOM
  const newOrder = Array.from(evt.to.children).map(el => 
    el.dataset.itemId
  );
  
  dispatch({ 
    type: 'RECONCILE_AFTER_DRAG', 
    payload: { newOrder, containerId: evt.to.id }
  });
};
```

---

## 🔥 КРИТИЧНЫЙ: CSS columns breaking при drag

### Проблема
CSS multi-column layout может вести себя непредсказуемо во время drag операций:
- Элементы "прыгают" между колонками
- Ghost element позиционируется неправильно
- Drop zones появляются не там где ожидается

### Визуальный пример проблемы
```css
/* Проблематичный CSS */
.navigation-container {
  columns: 3;
  column-gap: 20px;
}

/* При drag элемент может оказаться в любой колонке */
```

### Решения

#### 1. Временное отключение columns
```javascript
const handleDragStart = (evt) => {
  const container = evt.from.closest('.navigation-container');
  
  // Отключаем columns на время drag
  container.style.columns = 'none';
  container.style.display = 'flex';
  container.style.flexWrap = 'wrap';
  
  // Сохраняем оригинальные стили
  container.dataset.originalColumns = container.style.columns;
};

const handleDragEnd = (evt) => {
  const container = evt.to.closest('.navigation-container');
  
  // Восстанавливаем columns
  setTimeout(() => {
    container.style.columns = container.dataset.originalColumns || '3';
    container.style.display = '';
    container.style.flexWrap = '';
  }, 100); // небольшая задержка для плавности
};
```

#### 2. Fixed positioning для ghost
```css
.sortable-ghost {
  position: fixed !important;
  z-index: 10000;
  pointer-events: none;
  /* Отрываем от column layout */
  column-span: none;
}
```

#### 3. Alternative layout во время drag
```javascript
const DragModeLayout = ({ isDragging, children }) => {
  if (isDragging) {
    // Используем grid вместо columns
    return (
      <div style={{ 
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
        gap: '20px'
      }}>
        {children}
      </div>
    );
  }
  
  // Обычный column layout
  return (
    <div style={{ columns: 3, columnGap: '20px' }}>
      {children}
    </div>
  );
};
```

---

## ⚠️ ВАЖНО: Z-index конфликты overlay элементов

### Проблема
Множественные overlay элементы могут перекрывать друг друга:
- Ghost element
- Drop zones
- Progress indicators  
- Hover tooltips
- Expansion timers

### Решение: Z-index иерархия
```css
/* Четкая иерархия z-index */
:root {
  --z-drag-ghost: 10000;
  --z-drop-zones: 9000;
  --z-progress-indicators: 8000;
  --z-hover-tooltips: 7000;
  --z-expansion-indicators: 6000;
}

.sortable-ghost { z-index: var(--z-drag-ghost); }
.drop-zone { z-index: var(--z-drop-zones); }
.progress-indicator { z-index: var(--z-progress-indicators); }
.hover-tooltip { z-index: var(--z-hover-tooltips); }
.expansion-indicator { z-index: var(--z-expansion-indicators); }
```

### Stacking contexts management
```javascript
const createStackingContext = (element) => {
  // Создаем новый stacking context для изоляции
  element.style.transform = 'translateZ(0)';
  element.style.position = 'relative';
};
```

---

## ⚠️ ВАЖНО: Scroll behavior конфликты

### Проблема
Auto-scroll может конфликтовать с:
- Natural scrolling на трекпадах
- Existing scroll containers
- Smooth scrolling behavior
- Momentum scrolling на iOS

### Решения

#### 1. Device detection
```javascript
const detectScrollCapabilities = () => {
  const isTouchDevice = 'ontouchstart' in window;
  const hasTrackpad = window.navigator.maxTouchPoints > 1;
  const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
  
  return { isTouchDevice, hasTrackpad, isIOS };
};

const configureAutoScroll = () => {
  const { isTouchDevice, hasTrackpad, isIOS } = detectScrollCapabilities();
  
  return {
    enabled: !hasTrackpad, // отключаем на трекпадах
    speed: isIOS ? 2 : 5,  // медленнее на iOS
    acceleration: isTouchDevice ? 1.2 : 1.5
  };
};
```

#### 2. Scroll conflict prevention
```javascript
const preventScrollConflicts = (dragContainer) => {
  let isAutoScrolling = false;
  
  const handleAutoScroll = (direction, speed) => {
    if (isAutoScrolling) return;
    
    isAutoScrolling = true;
    
    // Временно отключаем smooth scrolling
    dragContainer.style.scrollBehavior = 'auto';
    
    const scroll = () => {
      dragContainer.scrollTop += direction * speed;
      
      if (shouldContinueScrolling()) {
        requestAnimationFrame(scroll);
      } else {
        isAutoScrolling = false;
        dragContainer.style.scrollBehavior = '';
      }
    };
    
    requestAnimationFrame(scroll);
  };
};
```

---

## ⚠️ ВАЖНО: Visual feedback overload

### Проблема
Слишком много визуальных индикаторов одновременно:
- Ghost element
- Multiple drop zones
- Progress indicators
- Hover effects  
- Expansion animations
- Nesting level indicators

### Решение: Progressive disclosure
```javascript
const VisualFeedbackManager = () => {
  const [activeIndicators, setActiveIndicators] = useState({
    ghost: false,
    dropZones: false,
    progress: false,
    hover: false,
    expansion: false,
    nesting: false
  });
  
  // Показываем только критически важные индикаторы
  const prioritizeIndicators = (context) => {
    if (context.isDragging) {
      return {
        ghost: true,
        dropZones: true,
        nesting: context.isNesting,
        // Скрываем менее важные
        hover: false,
        progress: false,
        expansion: false
      };
    }
  };
};
```

### Minimal feedback approach
```css
/* Простые, понятные индикаторы */
.drop-zone-simple {
  background: rgba(33, 150, 243, 0.1);
  border: 1px dashed #2196f3;
  /* Без сложных animations */
}

.ghost-simple {
  opacity: 0.5;
  /* Без shadows и transforms */
}
```

---

## 🔴 Testing challenges для drag-and-drop

### Проблема
Автоматическое тестирование drag-and-drop крайне сложно:
- Synthetic events отличаются от реальных
- Timing issues с анимациями
- Browser-specific behavior
- Touch vs mouse events

### Решения

#### 1. Playwright integration tests
```javascript
// playwright.config.js
const config = {
  use: {
    // Включаем реальные mouse events
    hasTouch: false,
    isMobile: false,
  }
};

// tests/drag-drop.spec.js
test('should reorder navigation items', async ({ page }) => {
  await page.goto('/f2-mode');
  await page.keyboard.press('F2');
  
  // Используем real drag-and-drop
  await page.locator('[data-item="installation"]').dragTo(
    page.locator('[data-item="configuration"]')
  );
  
  await expect(page.locator('.navigation-item').nth(2))
    .toHaveText('Installation');
});
```

#### 2. Manual testing framework
```javascript
const ManualTestRunner = {
  scenarios: [
    {
      name: 'Basic reorder',
      steps: [
        'Press F2',
        'Drag "Installation" below "Configuration"',
        'Verify order changed',
        'Press Save'
      ],
      expectedResult: 'Installation appears after Configuration'
    }
  ],
  
  runScenario: (scenarioName) => {
    const scenario = this.scenarios.find(s => s.name === scenarioName);
    console.log(`Testing: ${scenario.name}`);
    scenario.steps.forEach((step, i) => {
      console.log(`${i + 1}. ${step}`);
    });
    console.log(`Expected: ${scenario.expectedResult}`);
  }
};
```

#### 3. Synthetic event utilities
```javascript
const simulateDragDrop = (source, target) => {
  const dragStartEvent = new DragEvent('dragstart', {
    bubbles: true,
    cancelable: true,
    dataTransfer: new DataTransfer()
  });
  
  const dropEvent = new DragEvent('drop', {
    bubbles: true,
    cancelable: true,
    dataTransfer: dragStartEvent.dataTransfer
  });
  
  source.dispatchEvent(dragStartEvent);
  target.dispatchEvent(dropEvent);
};
```

---

## Митигация стратегии

### Pre-implementation checklist
- [ ] Выбрать React-compatible drag-and-drop solution
- [ ] Протестировать CSS columns behavior
- [ ] Определить z-index иерархию
- [ ] Настроить scroll handling для разных устройств
- [ ] Создать minimal visual feedback design
- [ ] Подготовить manual testing scenarios

### Early detection
- Создать простой prototype для проверки React-SortableJS интеграции
- Тестировать на разных browsers и devices
- Мониторить performance metrics с самого начала

### Fallback plans
- Если SortableJS не работает с React → переход на react-beautiful-dnd
- Если CSS columns проблематичны → fallback на flexbox/grid
- Если auto-scroll конфликтует → отключение для проблемных устройств