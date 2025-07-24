/**
 * SiblingsFinder - сервис для поиска соседних страниц в навигации
 * Анализирует DOM структуру навигации и находит страницы того же уровня
 */

export class SiblingsFinder {
  constructor() {
    this.navigationElement = null;
  }

  /**
   * Инициализация с DOM элементом навигации
   */
  initialize(navigationElement) {
    this.navigationElement = navigationElement;
  }

  /**
   * Найти текущую страницу на основе URL
   */
  findCurrentPage() {
    if (!this.navigationElement) return null;

    const currentUrl = window.location.pathname;
    const links = this.navigationElement.querySelectorAll('a[href]');
    
    for (const link of links) {
      const href = link.getAttribute('href');
      if (href === currentUrl || href.endsWith(currentUrl)) {
        return this.createPageObject(link);
      }
    }

    return null;
  }

  /**
   * Найти всех соседей (siblings) для текущей страницы
   */
  findSiblings(currentPage = null) {
    if (!currentPage) {
      currentPage = this.findCurrentPage();
    }

    if (!currentPage) {
      console.warn('Current page not found, cannot find siblings');
      return [];
    }

    // Находим DOM элемент текущей страницы
    const currentElement = this.findElementByPath(currentPage.path);
    if (!currentElement) return [];

    // Определяем уровень вложенности текущей страницы
    const currentLevel = this.getElementLevel(currentElement);
    
    // Находим родительский контейнер
    const parentContainer = this.findParentContainer(currentElement, currentLevel);
    
    // Собираем всех соседей того же уровня
    const siblings = this.collectSiblingsInContainer(parentContainer, currentLevel);
    
    return siblings;
  }

  /**
   * Создает объект страницы из DOM элемента
   */
  createPageObject(element) {
    const href = element.getAttribute('href') || '';
    const title = element.textContent.trim();
    const level = this.getElementLevel(element);
    const type = this.determineType(element);

    return {
      path: href,
      title: title,
      level: level,
      type: type,
      element: element
    };
  }

  /**
   * Находит DOM элемент по пути
   */
  findElementByPath(path) {
    if (!this.navigationElement) return null;

    const links = this.navigationElement.querySelectorAll('a[href]');
    for (const link of links) {
      if (link.getAttribute('href') === path) {
        return link;
      }
    }

    return null;
  }

  /**
   * Определяет уровень вложенности элемента
   */
  getElementLevel(element) {
    let level = 0;
    let parent = element.parentElement;
    
    while (parent && parent !== this.navigationElement) {
      // Считаем details элементы (складывающиеся секции)
      if (parent.tagName === 'DETAILS') {
        level++;
      }
      
      // Также проверяем CSS классы для отступов
      if (parent.classList.contains('ml-6') || 
          parent.classList.contains('ml-4') || 
          parent.classList.contains('ml-8')) {
        if (parent.tagName !== 'DETAILS') {
          level += 0.5; // Половинный уровень для визуальных отступов
        }
      }
      
      parent = parent.parentElement;
    }
    
    return Math.floor(level);
  }

  /**
   * Определяет тип элемента (folder/document)
   */
  determineType(element) {
    // Если это summary элемент внутри details, то это папка
    if (element.tagName === 'SUMMARY' || 
        element.closest('details') === element.parentElement) {
      return 'folder';
    }
    
    // Если ссылка заканчивается на '/', то это папка
    const href = element.getAttribute('href') || '';
    if (href.endsWith('/')) {
      return 'folder';
    }
    
    return 'document';
  }

  /**
   * Находит родительский контейнер для элемента
   */
  findParentContainer(element, currentLevel) {
    let parent = element.parentElement;
    
    while (parent && parent !== this.navigationElement) {
      // Ищем details элемент соответствующего уровня
      if (parent.tagName === 'DETAILS') {
        const parentLevel = this.getElementLevel(parent);
        if (parentLevel === currentLevel - 1) {
          return parent;
        }
      }
      parent = parent.parentElement;
    }
    
    // Если не нашли конкретный родительский контейнер, возвращаем корень
    return this.navigationElement;
  }

  /**
   * Собирает всех соседей в контейнере
   */
  collectSiblingsInContainer(container, targetLevel) {
    const siblings = [];
    const elements = container.querySelectorAll('a[href], summary');
    
    for (const element of elements) {
      const elementLevel = this.getElementLevel(element);
      
      // Включаем только элементы нужного уровня
      if (elementLevel === targetLevel) {
        siblings.push(this.createPageObject(element));
      }
    }
    
    return siblings;
  }

  /**
   * Мок данные для демонстрации (когда навигация недоступна)
   */
  getMockSiblings(currentPath) {
    const mockData = [
      {
        path: '/readme',
        title: 'Introduction',
        level: 1,
        type: 'document'
      },
      {
        path: '/getting-started',
        title: 'Getting Started',
        level: 1,
        type: 'document'
      },
      {
        path: '/installation',
        title: 'Installation',
        level: 1,
        type: 'document'
      },
      {
        path: '/configuration',
        title: 'Configuration',
        level: 1,
        type: 'document'
      },
      {
        path: '/api-overview',
        title: 'API Overview',
        level: 1,
        type: 'document'
      }
    ];

    // Включаем текущую страницу в список
    const currentPageInMock = mockData.find(page => page.path === currentPath);
    if (!currentPageInMock && currentPath) {
      mockData.push({
        path: currentPath,
        title: 'Current Page',
        level: 1,
        type: 'document'
      });
    }

    return mockData;
  }

  /**
   * Найти секцию (родительский details элемент) для страницы
   */
  findSectionForPage(pageElement) {
    if (!pageElement) return null;
    
    let parent = pageElement.parentElement;
    while (parent && parent !== this.navigationElement) {
      if (parent.tagName === 'DETAILS') {
        const summary = parent.querySelector('summary');
        if (summary) {
          return {
            name: summary.textContent.trim(),
            element: parent,
            level: this.getElementLevel(parent)
          };
        }
      }
      parent = parent.parentElement;
    }
    
    return null;
  }

  /**
   * Группировать соседей по секциям с реальными заголовками
   */
  getSiblingsWithSections(currentPath = null) {
    try {
      const siblings = this.getSiblings(currentPath);
      const sectionsMap = new Map();
      const result = [];

      for (const sibling of siblings) {
        const element = this.findElementByPath(sibling.path);
        const section = this.findSectionForPage(element);
        
        const sectionName = section ? section.name : 'Other';
        
        if (!sectionsMap.has(sectionName)) {
          sectionsMap.set(sectionName, []);
          // Add section header
          result.push({
            type: 'section',
            name: sectionName,
            id: `section-${sectionName.toLowerCase().replace(/\s+/g, '-')}`
          });
        }
        
        // Add page under section
        result.push({
          type: 'page',
          ...sibling,
          sectionName: sectionName
        });
      }

      return result;
    } catch (error) {
      console.error('Error getting siblings with sections:', error);
      return this.getMockSiblingsWithSections(currentPath);
    }
  }

  /**
   * Мок данные с секциями для демонстрации
   */
  getMockSiblingsWithSections(currentPath) {
    const mockSections = [
      { type: 'section', name: 'Overview', id: 'section-overview' },
      { type: 'page', path: '/readme', title: 'Introduction', level: 1, type: 'document' },
      { type: 'page', path: '/getting-started', title: 'Getting Started', level: 1, type: 'document' },
      
      { type: 'section', name: 'Configuration', id: 'section-configuration' },
      { type: 'page', path: '/installation', title: 'Installation', level: 1, type: 'document' },
      { type: 'page', path: '/configuration', title: 'Configuration', level: 1, type: 'document' },
      
      { type: 'section', name: 'API', id: 'section-api' },
      { type: 'page', path: '/api-overview', title: 'API Overview', level: 1, type: 'document' }
    ];

    // Include current page if not found
    const hasCurrentPage = mockSections.some(item => 
      item.type === 'page' && item.path === currentPath
    );
    
    if (!hasCurrentPage && currentPath) {
      mockSections.push({
        type: 'page',
        path: currentPath,
        title: 'Current Page',
        level: 1,
        type: 'document'
      });
    }

    return mockSections;
  }

  /**
   * Основной метод для получения соседей
   * Пытается найти реальных соседей, если не получается - возвращает мок данные
   */
  getSiblings(currentPath = null) {
    try {
      if (!this.navigationElement) {
        console.warn('Navigation element not initialized, using mock data');
        return this.getMockSiblings(currentPath);
      }

      const currentPage = currentPath ? 
        { path: currentPath, element: this.findElementByPath(currentPath) } : 
        this.findCurrentPage();

      if (!currentPage) {
        console.warn('Current page not found, using mock data');
        return this.getMockSiblings(currentPath);
      }

      const siblings = this.findSiblings(currentPage);
      
      if (siblings.length === 0) {
        console.warn('No siblings found, using mock data');
        return this.getMockSiblings(currentPath);
      }

      return siblings;
    } catch (error) {
      console.error('Error finding siblings:', error);
      return this.getMockSiblings(currentPath);
    }
  }
}

// Синглтон экземпляр
export const siblingsFinder = new SiblingsFinder();