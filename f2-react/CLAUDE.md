# F2 Documentation Reorganization Feature

You are senior frontend developer. You know perfectly:
- react
- tailwind
- drag and drop

# Goal
Provide a fullscreen documentation reorganization interface accessible via the F2 key in development mode, 
offering a GitBook-style editing experience for restructuring documentation through drag-and-drop operations.

# How to develop
1. Run `npm build` on every change. 
2. Always check the browser using playwright mcp (localhost:8081).
3. If you want to change clojure backend: see ../CLAUDE.md and ../src.
4. Example of the feature in vanilla js: ../resources/public/f2-navigation.js.backup
5. Write or update playwright tests if necessary.

## Requirements

### User Interface
- **Activation**: F2 key
- **Toggle behavior**: F2 again returns to normal view
- **Fullscreen mode**: Navigation spans entire screen
- **Visual feedback**: Orange left border + pulsing dot for pending changes
- **Control buttons**: "Open All" / "Close All" for bulk operations
- **Rename icon** on the page.
- **Change preview**: Real-time display of pending changes (MOVE, RENAME operations)
- **Drop zones**: Visual highlighting showing where items can be dropped
- **Save button**: Only applies changes when pressed (no auto-save)

### Drag-and-Drop Functionality
- **Reorder siblings**: Move documents within same parent directory
- **Cross-directory moves**: Move documents between different directories
- **Create new parent**: Drop document onto another document to make it a subdirectory
- **Nested restructuring**: Support for complex hierarchical changes
- **File renaming**: Rename files with user input (only on Save)
- **Visual feedback**: Show drop zones and preview changes before saving
- **Change preview**: Display pending changes in UI before applying

# DRAG AND DROP RULES

🧭 **Цель**: навигация с drag-and-drop, как в GitBook

Пользователь должен уметь:

- Перемещать элементы вверх/вниз (менять порядок).
- Менять уровень вложенности (вправо/влево).
- Создавать древовидную структуру из страниц и разделов.

---

## 🧠 Архитектурные основы

Структура данных навигации (пример на JSON, в реальности — HTML):

```json
[
  {
    "id": "1",
    "title": "Introduction",
    "children": []
  },
  {
    "id": "2",
    "title": "Guide",
    "children": [
      {
        "id": "3",
        "title": "Getting Started",
        "children": []
      }
    ]
  }
]
```

Каждый элемент может быть:
- 📄 Страницей (leaf node)
- 📁 Группой / разделом (имеет children)

## 🖱️ Логика Drag-and-Drop
📌 1. Перемещение между уровнями

- Горизонтальная позиция курсора определяет уровень вложенности:
  - Смещение курсора вправо → вложенность +1
  - Влево → вложенность -1

- Используйте пиксели смещения (например, 20px за уровень вложенности).

📂 2. Дроп в конкретный родительский элемент

    Наведение на родитель должно подсвечивать его (highlight).

    Дроп может означать:

        Добавление в конец children

        Вставку между дочерними элементами (если children.length > 0)

🧱 Поведение GitBook (имитируем точно)
Сценарий	Поведение
Перетаскиваю страницу на новый уровень	Страница становится дочерней или поднимается
Перетаскиваю между элементами	Горизонтальная линия показывает вставку
Навожу на группу	Группа подсвечивается, можно вложить внутрь
Дроп невалидный (например, в листовой узел)	Дроп отменяется
Дубли вложенности	Запрещено вложение элемента внутрь самого себя или его потомка

🔄 Алгоритмы: пересчёт структуры после drop
На onDragEnd:

    Получить active и over элементы.

    Рассчитать:

        Новый родитель

        Новый индекс в списке

        Новый уровень вложенности

    Обновить структуру дерева:

        Удалить элемент из старого места

        Вставить в новое (можно использовать immer или lodash.move)

🔐 Ограничения

    ❌ Запрет вложения внутрь себя:

        Нужно проверять, что active.id не является предком over.id

    ❌ Максимальная глубина вложенности (например, depth <= 3)

    ❌ Нельзя перетаскивать за пределы области — scroll lock или auto scroll

💅 UI-индикация и UX

    🔵 При drag:

        Тень под курсором

        Элемент подсвечен

    ➖ Подсказка: "Drop here" или горизонтальная линия

    ➕ При hover на группу — выделение

    ⛔ При недопустимом дропе — серый курсор, нельзя отпустить
