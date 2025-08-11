# f2-go - GitBook Documentation Manager

A TUI (Terminal User Interface) application for managing GitBook documentation structure, written in Go with a TDD approach.

## Project Structure

```
f2-go/
├── cmd/f2/                 # Main application entry point
├── internal/
│   ├── core/              # Core business logic
│   │   ├── interfaces.go   # Core interfaces
│   │   ├── types.go       # Data types
│   │   ├── summary.go     # SUMMARY.md parsing and management
│   │   └── redirect.go    # Redirect management
│   ├── parser/            # Parsing utilities
│   │   └── links.go       # Markdown link parsing and updating
│   ├── ui/                # UI components (to be implemented)
│   └── utils/             # Utilities
│       └── filesystem.go  # File system abstraction
├── test/
│   ├── fixtures/          # Test data
│   └── integration/       # Integration tests
└── go.mod
```

## Features Implemented

### Core Functionality
- ✅ **SUMMARY.md Parser**: Parse GitBook table of contents
- ✅ **Move Operations**: Move entries with hierarchy preservation
- ✅ **Rename Operations**: Rename entries with validation
- ✅ **Delete Operations**: Delete entries (prevents deleting parents with children)
- ✅ **Link Parser**: Find and update markdown links
- ✅ **Redirect Manager**: Manage .gitbook.yaml redirects with circular dependency detection
- ✅ **File System Abstraction**: Memory and OS file systems using afero

### Test Coverage
- ✅ Unit tests for all core components
- ✅ Integration tests for complete operations
- ✅ Memory file system for testing
- ⚠️  Circular dependency detection in moves (needs improvement)

## Architecture Decisions

### 1. **Interface-Based Design**
All major components are defined as interfaces, making the code testable and extensible:
- `FileSystem` - File operations abstraction
- `SummaryManager` - SUMMARY.md operations
- `LinkUpdater` - Link parsing and updating
- `RedirectManager` - Redirect management

### 2. **Separation of Concerns**
- **Core**: Business logic without external dependencies
- **Parser**: Specialized parsing logic
- **Utils**: Reusable utilities and adapters
- **UI**: Terminal interface (to be implemented)

### 3. **Test-Driven Development**
- Tests written before implementation
- Table-driven tests for comprehensive coverage
- Integration tests for end-to-end scenarios

## Usage (To Be Implemented)

```bash
# Run the TUI application
go run cmd/f2/main.go

# Navigation
↑/↓     Navigate entries
←/→     Collapse/expand folders
Enter   Move entry
r       Rename entry
d       Delete entry
Ctrl+S  Save changes
Ctrl+Z  Undo
q       Quit
```

## Testing

```bash
# Run all tests
go test ./...

# Run with coverage
go test -cover ./...

# Run specific package tests
go test ./internal/core -v
go test ./internal/parser -v
go test ./test/integration -v
```

## Known Issues & TODOs

1. **Circular Dependency Detection**: The logic for detecting circular dependencies when moving entries needs refinement
2. **Relative Link Updates**: Link updates currently don't handle relative path transformations
3. **UI Implementation**: The terminal UI using bubbletea needs to be implemented
4. **File Operations**: Actual file operations need to be integrated with the business logic
5. **Undo/Redo**: Operation history tracking needs implementation

## Development Status

This is a work-in-progress implementation focusing on:
- Clean architecture with testable code
- Comprehensive test coverage
- Separation of UI from business logic
- Extensible design for future enhancements

The core logic is implemented and tested. The next phase would be implementing the terminal UI and integrating all components into a working application.