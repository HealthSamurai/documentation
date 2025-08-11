# Руководство по тестированию f2

## Запуск тестов

### Основные команды

```bash
# Запустить все тесты
go test ./...

# Запустить тесты с покрытием
go test ./cmd/f2 -cover

# Запустить тесты с детальным покрытием
go test ./cmd/f2 -cover -coverprofile=coverage.out
go tool cover -func=coverage.out

# Генерация HTML отчета покрытия
go test ./cmd/f2 -cover -coverprofile=coverage.out
go tool cover -html=coverage.out -o coverage.html
# Открыть coverage.html в браузере

# Запустить конкретный тест
go test ./cmd/f2 -run TestCLI_Execute

# Запустить тесты с подробным выводом
go test ./cmd/f2 -v

# Запустить бенчмарки
go test ./cmd/f2 -bench=.

# Запустить тесты с race detector
go test ./cmd/f2 -race

# Запустить интеграционные тесты
go test ./cmd/f2 -tags=integration
```

## Структура тестов

### 1. Unit тесты (cli_test.go)

Тестируют отдельные функции в изоляции:

```go
// Тест генерации имени файла
func TestGenerateFilename(t *testing.T)

// Тест преобразования пути в URL  
func TestPathToURL(t *testing.T)

// Тест обновления H1 заголовка
func TestUpdateH1Header(t *testing.T)

// Тест настройки относительных ссылок
func TestAdjustRelativeLinks(t *testing.T)
```

### 2. Функциональные тесты

Тестируют основные команды CLI:

```go
// Тест команды rename
func TestCLI_runRename(t *testing.T)

// Тест команды move
func TestCLI_runMove(t *testing.T)

// Тест команды delete
func TestCLI_runDelete(t *testing.T)
```

### 3. Интеграционные тесты

Тестируют полный цикл работы команд:

```go
// Тест всего CLI
func TestCLI_Execute(t *testing.T)

// Тест инициализации менеджеров
func TestCLI_initializeManagers(t *testing.T)
```

## Тестовые сценарии

### Тестирование команды rename

```bash
# Успешное переименование
./f2 rename "getting-started/quick-start.md" "New Name"

# Переименование с спецсимволами
./f2 rename "file.md" "Title with @#$% symbols"

# Переименование несуществующего файла
./f2 rename "non-existent.md" "New Name"

# Переименование с коллизией имен
./f2 rename "file1.md" "Existing File Name"

# Переименование с пустым заголовком
./f2 rename "file.md" ""
```

### Тестирование команды move

```bash
# Успешное перемещение
./f2 move "getting-started/file.md" "tutorials/file.md"
# Выбрать позицию: 2

# Перемещение в ту же позицию
./f2 move "file.md" "file.md"
# Выбрать текущую позицию

# Перемещение с некорректной позицией
./f2 move "file.md" "new-location.md"
# Ввести: 999

# Перемещение с коллизией файлов
./f2 move "file1.md" "existing-file.md"
```

### Тестирование команды delete

```bash
# Успешное удаление с подтверждением
./f2 delete "file.md"
# Ввести: y

# Отмена удаления
./f2 delete "file.md"
# Ввести: n

# Удаление несуществующего файла
./f2 delete "non-existent.md"
```

## Mock объекты

### MockFileSystem

Имитирует файловую систему для тестирования без реальных файлов:

```go
fs := NewMockFileSystem()
fs.files["docs/SUMMARY.md"] = []byte("# Table of contents...")
fs.files[".gitbook.yaml"] = []byte("redirects:...")
```

## Тестирование Edge Cases

### 1. Файловая система
- Файл не существует в SUMMARY.md
- Файл существует на диске, но не в SUMMARY.md
- Права доступа на запись отсутствуют
- Диск заполнен

### 2. SUMMARY.md
- Пустой SUMMARY.md
- Некорректный формат SUMMARY.md
- Дубликаты в SUMMARY.md
- Очень глубокая вложенность (>10 уровней)

### 3. Названия и пути
- Очень длинные названия (>255 символов)
- Специальные символы в названиях
- Unicode символы (эмодзи, кириллица, иероглифы)
- Пути с пробелами
- Относительные vs абсолютные пути

### 4. Ссылки
- Циклические ссылки
- Битые ссылки
- Ссылки с якорями (#section)
- Внешние ссылки (http://)
- Reference-style ссылки

### 5. Redirects
- Циклические редиректы
- Множественные редиректы
- Конфликтующие редиректы

## Проверка покрытия

### Текущее покрытие: 74.6%

### Непокрытые области:

1. **main() - 0%** 
   - Точка входа, минимальная логика
   - Тестируется через интеграционные тесты

2. **TUI функции - 0%**
   - Требуют специального подхода с mock терминала
   - Можно использовать github.com/charmbracelet/bubbletea/teatest

3. **Обработка ошибок**
   - Некоторые error пути не покрыты
   - Требуют специфичных условий (нет места на диске и т.д.)

## Continuous Integration

### GitHub Actions workflow

```yaml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up Go
      uses: actions/setup-go@v4
      with:
        go-version: '1.21'
    
    - name: Run tests
      run: go test -v -cover -race ./...
    
    - name: Generate coverage
      run: |
        go test -coverprofile=coverage.out ./...
        go tool cover -func=coverage.out
    
    - name: Upload coverage
      uses: codecov/codecov-action@v3
      with:
        file: ./coverage.out
```

## Отладка тестов

### Запуск с отладочной информацией

```bash
# Verbose mode
go test -v ./cmd/f2

# С таймаутом
go test -timeout 30s ./cmd/f2

# Только неуспешные тесты
go test -failfast ./cmd/f2

# С подробным стеком вызовов
go test -v -trace trace.out ./cmd/f2
go tool trace trace.out
```

### Использование отладчика

```bash
# С delve debugger
dlv test ./cmd/f2 -- -test.run TestCLI_Execute
```

## Производительность

### Бенчмарки

```bash
# Запустить все бенчмарки
go test -bench=. ./cmd/f2

# Запустить конкретный бенчмарк
go test -bench=BenchmarkGenerateFilename ./cmd/f2

# С профилированием памяти
go test -bench=. -benchmem ./cmd/f2

# Сравнение производительности
go test -bench=. -count=10 ./cmd/f2 > old.txt
# После изменений
go test -bench=. -count=10 ./cmd/f2 > new.txt
benchstat old.txt new.txt
```

## Лучшие практики

1. **Изоляция тестов** - каждый тест должен быть независимым
2. **Чистка после тестов** - используйте defer cleanup()
3. **Таблично-управляемые тесты** - для множественных сценариев
4. **Мокирование внешних зависимостей** - не трогать реальную ФС
5. **Проверка граничных условий** - пустые строки, nil, большие значения
6. **Именование тестов** - описательные имена TestFunction_Condition_ExpectedResult
7. **Параллельные тесты** - t.Parallel() для независимых тестов
8. **Фикстуры** - переиспользуемые тестовые данные

## Проблемы и решения

### Проблема: Тесты падают с "file not found"
**Решение**: Проверьте, что тесты запускаются из корня проекта

### Проблема: Race conditions в тестах
**Решение**: Используйте -race флаг и синхронизацию

### Проблема: Медленные тесты
**Решение**: 
- Используйте t.Parallel()
- Минимизируйте I/O операции
- Используйте in-memory моки

### Проблема: Нестабильные тесты (flaky tests)
**Решение**:
- Избегайте зависимости от времени
- Используйте детерминированные данные
- Изолируйте тесты друг от друга