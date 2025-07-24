/**
 * PathSuggestions - сервис для генерации подсказок путей файлов
 * Анализирует существующую структуру навигации и предлагает варианты путей
 */

export class PathSuggestions {
  constructor() {
    this.navigationElement = null;
    this.existingPaths = [];
    this.folderStructure = new Map();
  }

  /**
   * Инициализация с DOM элементом навигации
   */
  initialize(navigationElement) {
    this.navigationElement = navigationElement;
    this.analyzeExistingStructure();
  }

  /**
   * Анализирует существующую структуру навигации
   */
  analyzeExistingStructure() {
    if (!this.navigationElement) return;

    this.existingPaths = [];
    this.folderStructure.clear();

    const links = this.navigationElement.querySelectorAll('a[href]');
    
    for (const link of links) {
      const href = link.getAttribute('href');
      if (!href || href.startsWith('#') || href.startsWith('http')) continue;

      this.existingPaths.push({
        path: href,
        title: link.textContent.trim(),
        type: this.determinePathType(href),
        element: link
      });

      // Анализируем структуру папок
      this.addToFolderStructure(href);
    }

    console.log('📁 Analyzed paths:', this.existingPaths.length);
    console.log('📂 Folder structure:', this.folderStructure);
  }

  /**
   * Определяет тип пути (folder/document)
   */
  determinePathType(path) {
    if (path.endsWith('/')) return 'folder';
    if (path.includes('.')) return 'document';
    return 'document'; // по умолчанию
  }

  /**
   * Добавляет путь в структуру папок
   */
  addToFolderStructure(path) {
    const parts = path.split('/').filter(part => part.length > 0);
    
    for (let i = 0; i < parts.length - 1; i++) {
      const folderPath = '/' + parts.slice(0, i + 1).join('/') + '/';
      
      if (!this.folderStructure.has(folderPath)) {
        this.folderStructure.set(folderPath, new Set());
      }
      
      // Добавляем дочерние элементы
      if (i + 1 < parts.length) {
        const childPath = i + 1 === parts.length - 1 ? 
          '/' + parts.slice(0, i + 2).join('/') : 
          '/' + parts.slice(0, i + 2).join('/') + '/';
        
        this.folderStructure.get(folderPath).add(childPath);
      }
    }
  }

  /**
   * Генерирует подсказки для введенного пути
   */
  generateSuggestions(inputPath) {
    const suggestions = [];
    const normalizedInput = inputPath.toLowerCase().trim();

    if (!normalizedInput) {
      return this.getTopLevelSuggestions();
    }

    // 1. Поиск по началу пути
    suggestions.push(...this.findByPathPrefix(normalizedInput));

    // 2. Поиск по названию файла
    suggestions.push(...this.findByFileName(normalizedInput));

    // 3. Автокомплит для папок
    suggestions.push(...this.getDirectoryAutoComplete(normalizedInput));

    // 4. Фузи поиск
    suggestions.push(...this.fuzzySearch(normalizedInput));

    // Убираем дубликаты и ограничиваем количество
    const uniqueSuggestions = this.removeDuplicates(suggestions);
    
    return uniqueSuggestions.slice(0, 8);
  }

  /**
   * Возвращает подсказки верхнего уровня
   */
  getTopLevelSuggestions() {
    const topLevel = this.existingPaths
      .filter(path => path.path.split('/').filter(p => p).length <= 2)
      .slice(0, 6);

    const commonFolders = [
      { path: '/docs/', title: 'docs', type: 'folder' },
      { path: '/guides/', title: 'guides', type: 'folder' },
      { path: '/api/', title: 'api', type: 'folder' },
      { path: '/tutorials/', title: 'tutorials', type: 'folder' }
    ];

    return [...topLevel, ...commonFolders];
  }

  /**
   * Поиск по началу пути
   */
  findByPathPrefix(input) {
    return this.existingPaths
      .filter(item => item.path.toLowerCase().startsWith(input))
      .slice(0, 4);
  }

  /**
   * Поиск по названию файла
   */
  findByFileName(input) {
    return this.existingPaths
      .filter(item => {
        const fileName = item.path.split('/').pop().toLowerCase();
        return fileName.includes(input) || item.title.toLowerCase().includes(input);
      })
      .slice(0, 3);
  }

  /**
   * Автокомплит для директорий
   */
  getDirectoryAutoComplete(input) {
    const lastSlashIndex = input.lastIndexOf('/');
    
    if (lastSlashIndex === -1) {
      return [];
    }

    const basePath = input.substring(0, lastSlashIndex + 1);
    const partialName = input.substring(lastSlashIndex + 1);

    // Ищем все пути в этой директории
    const suggestions = this.existingPaths
      .filter(item => {
        const itemDir = item.path.substring(0, item.path.lastIndexOf('/') + 1);
        const itemName = item.path.substring(item.path.lastIndexOf('/') + 1);
        
        return itemDir === basePath && 
               itemName.toLowerCase().startsWith(partialName.toLowerCase());
      })
      .slice(0, 5);

    return suggestions;
  }

  /**
   * Фузи поиск
   */
  fuzzySearch(input) {
    if (input.length < 2) return [];

    return this.existingPaths
      .map(item => ({
        ...item,
        score: this.calculateFuzzyScore(input, item.path + ' ' + item.title)
      }))
      .filter(item => item.score > 0.3)
      .sort((a, b) => b.score - a.score)
      .slice(0, 3);
  }

  /**
   * Вычисляет оценку для фузи поиска
   */
  calculateFuzzyScore(needle, haystack) {
    const needleLower = needle.toLowerCase();
    const haystackLower = haystack.toLowerCase();

    if (haystackLower.includes(needleLower)) {
      const exactMatchBonus = haystackLower.indexOf(needleLower) === 0 ? 0.5 : 0.2;
      return 0.8 + exactMatchBonus;
    }

    // Простой алгоритм подсчета совпадающих символов
    let score = 0;
    let lastIndex = -1;

    for (const char of needleLower) {
      const index = haystackLower.indexOf(char, lastIndex + 1);
      if (index > lastIndex) {
        score += 1;
        lastIndex = index;
      }
    }

    return score / needleLower.length;
  }

  /**
   * Убирает дубликаты из массива подсказок
   */
  removeDuplicates(suggestions) {
    const seen = new Set();
    return suggestions.filter(item => {
      if (seen.has(item.path)) {
        return false;
      }
      seen.add(item.path);
      return true;
    });
  }

  /**
   * Предлагает возможные названия файлов на основе существующих
   */
  suggestFileNames(basePath) {
    const suggestions = [];
    const commonNames = [
      'index.md',
      'overview.md', 
      'getting-started.md',
      'installation.md',
      'configuration.md',
      'api.md',
      'examples.md',
      'troubleshooting.md'
    ];

    for (const name of commonNames) {
      const fullPath = basePath + (basePath.endsWith('/') ? '' : '/') + name;
      
      // Проверяем, что такого файла еще нет
      if (!this.existingPaths.some(p => p.path === fullPath)) {
        suggestions.push({
          path: fullPath,
          title: name.replace('.md', '').replace('-', ' '),
          type: 'document',
        });
      }
    }

    return suggestions.slice(0, 3);
  }

  /**
   * Мок данные для демонстрации (когда навигация недоступна)
   */
  getMockSuggestions(input) {
    const mockPaths = [
      { path: '/docs/getting-started.md', title: 'Getting Started', type: 'document' },
      { path: '/docs/installation.md', title: 'Installation', type: 'document' },
      { path: '/docs/configuration.md', title: 'Configuration', type: 'document' },
      { path: '/docs/api/', title: 'API Documentation', type: 'folder' },
      { path: '/docs/api/overview.md', title: 'API Overview', type: 'document' },
      { path: '/docs/api/authentication.md', title: 'Authentication', type: 'document' },
      { path: '/docs/guides/', title: 'Guides', type: 'folder' },
      { path: '/docs/guides/deployment.md', title: 'Deployment Guide', type: 'document' },
      { path: '/docs/tutorials/', title: 'Tutorials', type: 'folder' },
      { path: '/docs/examples/', title: 'Examples', type: 'folder' }
    ];

    if (!input) {
      return mockPaths.slice(0, 6);
    }

    const filtered = mockPaths.filter(item => 
      item.path.toLowerCase().includes(input.toLowerCase()) ||
      item.title.toLowerCase().includes(input.toLowerCase())
    );

    return filtered.slice(0, 8);
  }

  /**
   * Основной метод для получения подсказок
   */
  getSuggestions(input) {
    try {
      if (!this.navigationElement || this.existingPaths.length === 0) {
        console.warn('Navigation not initialized, using mock suggestions');
        return this.getMockSuggestions(input);
      }

      return this.generateSuggestions(input);
    } catch (error) {
      console.error('Error generating suggestions:', error);
      return this.getMockSuggestions(input);
    }
  }
}

// Синглтон экземпляр
export const pathSuggestions = new PathSuggestions();