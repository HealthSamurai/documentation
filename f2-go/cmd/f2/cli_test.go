package main

import (
	"bytes"
	"fmt"
	"os"
	"strings"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// MockFileSystem for testing
type MockFileSystem struct {
	mock.Mock
	files map[string][]byte
	dirs  map[string]bool
}

func NewMockFileSystem() *MockFileSystem {
	return &MockFileSystem{
		files: make(map[string][]byte),
		dirs:  make(map[string]bool),
	}
}

func (m *MockFileSystem) ReadFile(path string) ([]byte, error) {
	if content, ok := m.files[path]; ok {
		return content, nil
	}
	return nil, fmt.Errorf("file not found: %s", path)
}

func (m *MockFileSystem) WriteFile(path string, data []byte, perm os.FileMode) error {
	m.files[path] = data
	return nil
}

func (m *MockFileSystem) Exists(path string) bool {
	_, ok := m.files[path]
	if ok {
		return true
	}
	_, ok = m.dirs[path]
	return ok
}

func (m *MockFileSystem) Remove(path string) error {
	delete(m.files, path)
	return nil
}

func (m *MockFileSystem) MkdirAll(path string, perm os.FileMode) error {
	m.dirs[path] = true
	return nil
}

func (m *MockFileSystem) MoveFile(src, dst string) error {
	if content, ok := m.files[src]; ok {
		m.files[dst] = content
		delete(m.files, src)
		return nil
	}
	return fmt.Errorf("source file not found: %s", src)
}

func (m *MockFileSystem) ReadDir(path string) ([]os.DirEntry, error) {
	return []os.DirEntry{}, nil
}

func (m *MockFileSystem) Walk(root string, walkFn func(string, bool) error) error {
	// Simple implementation for testing
	for path := range m.files {
		if strings.HasPrefix(path, root) {
			isDir := strings.HasSuffix(path, "/")
			walkFn(path, isDir)
		}
	}
	return nil
}

// mockFileInfo implements os.FileInfo for testing
type mockFileInfo struct {
	name string
}

func (m *mockFileInfo) Name() string       { return m.name }
func (m *mockFileInfo) Size() int64        { return 0 }
func (m *mockFileInfo) Mode() os.FileMode  { return 0644 }
func (m *mockFileInfo) ModTime() time.Time { return time.Now() }
func (m *mockFileInfo) IsDir() bool        { return false }
func (m *mockFileInfo) Sys() interface{}   { return nil }

// Test helper to setup mock filesystem
func setupMockFS() *MockFileSystem {
	fs := NewMockFileSystem()
	
	// Create SUMMARY.md
	summaryContent := `# Table of contents

* [Getting Started](getting-started/README.md)
  * [Quick Start](getting-started/quick-start.md)
  * [Installation](getting-started/installation.md)
* [Tutorials](tutorials/README.md)
  * [First Tutorial](tutorials/first-tutorial.md)
`
	fs.files["docs/SUMMARY.md"] = []byte(summaryContent)
	
	// Create .gitbook.yaml
	gitbookContent := `redirects:
  old-page: new-page.md
`
	fs.files[".gitbook.yaml"] = []byte(gitbookContent)
	
	// Create test files
	fs.files["docs/getting-started/README.md"] = []byte("# Getting Started\n\nWelcome.")
	fs.files["docs/getting-started/quick-start.md"] = []byte("# Quick Start\n\nGuide.")
	fs.files["docs/getting-started/installation.md"] = []byte("# Installation\n\nSteps.")
	fs.files["docs/tutorials/README.md"] = []byte("# Tutorials\n\nLearn.")
	fs.files["docs/tutorials/first-tutorial.md"] = []byte("# First Tutorial\n\nContent.")
	
	return fs
}

func TestCLI_Execute(t *testing.T) {
	tests := []struct {
		name        string
		args        []string
		stdin       string
		wantErr     bool
		wantOutput  string
		errContains string
	}{
		{
			name:    "no arguments launches TUI",
			args:    []string{"f2"},
			wantErr: true,
			errContains: "TUI mode not implemented",
		},
		{
			name:    "rename with correct args",
			args:    []string{"f2", "rename", "getting-started/quick-start.md", "New Name"},
			wantErr: false,
			wantOutput: "✓ Rename completed successfully",
		},
		{
			name:    "rename with wrong args",
			args:    []string{"f2", "rename", "file.md"},
			wantErr: true,
			errContains: "Usage:",
		},
		{
			name:    "move with correct args",
			args:    []string{"f2", "move", "getting-started/installation.md", "tutorials/installation.md"},
			stdin:   "2\n",
			wantErr: false,
			wantOutput: "✓ Move completed successfully",
		},
		{
			name:    "move with wrong args",
			args:    []string{"f2", "move", "file.md"},
			wantErr: true,
			errContains: "Usage:",
		},
		{
			name:    "delete with confirmation",
			args:    []string{"f2", "delete", "tutorials/first-tutorial.md"},
			stdin:   "y\n",
			wantErr: false,
			wantOutput: "✓ Delete completed successfully",
		},
		{
			name:    "delete with wrong args",
			args:    []string{"f2", "delete"},
			wantErr: true,
			errContains: "Usage:",
		},
		{
			name:    "unknown command",
			args:    []string{"f2", "unknown"},
			wantErr: true,
			errContains: "Unknown command",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			fs := setupMockFS()
			stdin := strings.NewReader(tt.stdin)
			stdout := &bytes.Buffer{}
			stderr := &bytes.Buffer{}
			
			cli := NewTestCLI(fs, stdin, stdout, stderr)
			err := cli.Execute(tt.args)
			
			if tt.wantErr {
				assert.Error(t, err)
				if tt.errContains != "" {
					assert.Contains(t, err.Error(), tt.errContains)
				}
			} else {
				assert.NoError(t, err)
				if tt.wantOutput != "" {
					assert.Contains(t, stdout.String(), tt.wantOutput)
				}
			}
		})
	}
}

func TestCLI_runRename(t *testing.T) {
	tests := []struct {
		name        string
		filePath    string
		newName     string
		setupFS     func(*MockFileSystem)
		wantErr     bool
		errContains string
		checkResult func(*testing.T, *MockFileSystem, *bytes.Buffer)
	}{
		{
			name:     "successful rename",
			filePath: "getting-started/quick-start.md",
			newName:  "Getting Started Guide",
			wantErr:  false,
			checkResult: func(t *testing.T, fs *MockFileSystem, stdout *bytes.Buffer) {
				// Check file was renamed
				assert.True(t, fs.Exists("docs/getting-started/getting-started-guide.md"))
				assert.False(t, fs.Exists("docs/getting-started/quick-start.md"))
				
				// Check H1 was updated
				content, _ := fs.ReadFile("docs/getting-started/getting-started-guide.md")
				assert.Contains(t, string(content), "# Getting Started Guide")
				
				// Check output
				assert.Contains(t, stdout.String(), "✓ Rename completed successfully")
			},
		},
		{
			name:     "file not in summary",
			filePath: "non-existent.md",
			newName:  "New Name",
			wantErr:  true,
			errContains: "not found in SUMMARY.md",
		},
		{
			name:     "collision detection",
			filePath: "getting-started/quick-start.md",
			newName:  "Installation",
			setupFS: func(fs *MockFileSystem) {
				// File already exists
				fs.files["docs/getting-started/installation.md"] = []byte("# Existing")
			},
			wantErr:  true,
			errContains: "already exists",
		},
		{
			name:     "empty title",
			filePath: "getting-started/quick-start.md",
			newName:  "",
			wantErr:  true,
			errContains: "title cannot be empty",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			fs := setupMockFS()
			if tt.setupFS != nil {
				tt.setupFS(fs)
			}
			
			stdin := strings.NewReader("")
			stdout := &bytes.Buffer{}
			stderr := &bytes.Buffer{}
			
			cli := NewTestCLI(fs, stdin, stdout, stderr)
			err := cli.runRename(tt.filePath, tt.newName)
			
			if tt.wantErr {
				assert.Error(t, err)
				if tt.errContains != "" && err != nil {
					assert.Contains(t, err.Error(), tt.errContains)
				}
			} else {
				assert.NoError(t, err)
				if tt.checkResult != nil {
					tt.checkResult(t, fs, stdout)
				}
			}
		})
	}
}

func TestCLI_runMove(t *testing.T) {
	tests := []struct {
		name        string
		srcPath     string
		dstPath     string
		position    string
		setupFS     func(*MockFileSystem)
		wantErr     bool
		errContains string
		checkResult func(*testing.T, *MockFileSystem, *bytes.Buffer)
	}{
		{
			name:     "successful move",
			srcPath:  "getting-started/installation.md",
			dstPath:  "tutorials/installation.md",
			position: "2\n",
			wantErr:  false,
			checkResult: func(t *testing.T, fs *MockFileSystem, stdout *bytes.Buffer) {
				// Check file was moved
				assert.True(t, fs.Exists("docs/tutorials/installation.md"))
				assert.False(t, fs.Exists("docs/getting-started/installation.md"))
				assert.Contains(t, stdout.String(), "✓ Move completed successfully")
			},
		},
		{
			name:     "file not in summary",
			srcPath:  "non-existent.md",
			dstPath:  "new-location.md",
			position: "1\n",
			wantErr:  true,
			errContains: "not found in SUMMARY.md",
		},
		{
			name:     "invalid position",
			srcPath:  "getting-started/quick-start.md",
			dstPath:  "tutorials/quick-start.md",
			position: "999\n",
			wantErr:  true,
			errContains: "Invalid position",
		},
		{
			name:     "non-numeric position",
			srcPath:  "getting-started/quick-start.md",
			dstPath:  "tutorials/quick-start.md",
			position: "abc\n",
			wantErr:  true,
			errContains: "Invalid position",
		},
		{
			name:     "same position detection",
			srcPath:  "getting-started/quick-start.md",
			dstPath:  "getting-started/quick-start.md",
			position: "2\n",
			wantErr:  false,
			checkResult: func(t *testing.T, fs *MockFileSystem, stdout *bytes.Buffer) {
				assert.Contains(t, stdout.String(), "already at this position")
			},
		},
		{
			name:     "collision detection",
			srcPath:  "getting-started/quick-start.md",
			dstPath:  "getting-started/installation.md", // This file already exists in setupMockFS
			position: "2\n",
			wantErr:  true,
			errContains: "already exists",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			fs := setupMockFS()
			if tt.setupFS != nil {
				tt.setupFS(fs)
			}
			
			stdin := strings.NewReader(tt.position)
			stdout := &bytes.Buffer{}
			stderr := &bytes.Buffer{}
			
			cli := NewTestCLI(fs, stdin, stdout, stderr)
			err := cli.runMove(tt.srcPath, tt.dstPath)
			
			if tt.wantErr {
				assert.Error(t, err)
				if tt.errContains != "" && err != nil {
					assert.Contains(t, err.Error(), tt.errContains)
				}
			} else {
				assert.NoError(t, err)
				if tt.checkResult != nil {
					tt.checkResult(t, fs, stdout)
				}
			}
		})
	}
}

func TestCLI_runDelete(t *testing.T) {
	tests := []struct {
		name        string
		filePath    string
		response    string
		wantErr     bool
		errContains string
		checkResult func(*testing.T, *MockFileSystem, *bytes.Buffer)
	}{
		{
			name:     "successful delete with confirmation",
			filePath: "tutorials/first-tutorial.md",
			response: "y\n",
			wantErr:  false,
			checkResult: func(t *testing.T, fs *MockFileSystem, stdout *bytes.Buffer) {
				// Check file was deleted
				assert.False(t, fs.Exists("docs/tutorials/first-tutorial.md"))
				assert.Contains(t, stdout.String(), "✓ Delete completed successfully")
				assert.Contains(t, stdout.String(), "Warning:")
			},
		},
		{
			name:     "delete cancelled",
			filePath: "tutorials/first-tutorial.md",
			response: "n\n",
			wantErr:  false,
			checkResult: func(t *testing.T, fs *MockFileSystem, stdout *bytes.Buffer) {
				// Check file still exists
				assert.True(t, fs.Exists("docs/tutorials/first-tutorial.md"))
				assert.Contains(t, stdout.String(), "Deletion cancelled")
			},
		},
		{
			name:     "file not in summary",
			filePath: "non-existent.md",
			response: "y\n",
			wantErr:  true,
			errContains: "not found in SUMMARY.md",
		},
		{
			name:     "no response (just enter)",
			filePath: "tutorials/first-tutorial.md",
			response: "\n",
			wantErr:  false,
			checkResult: func(t *testing.T, fs *MockFileSystem, stdout *bytes.Buffer) {
				// Should be cancelled (default is No)
				assert.True(t, fs.Exists("docs/tutorials/first-tutorial.md"))
				assert.Contains(t, stdout.String(), "Deletion cancelled")
			},
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			fs := setupMockFS()
			stdin := strings.NewReader(tt.response)
			stdout := &bytes.Buffer{}
			stderr := &bytes.Buffer{}
			
			cli := NewTestCLI(fs, stdin, stdout, stderr)
			err := cli.runDelete(tt.filePath)
			
			if tt.wantErr {
				assert.Error(t, err)
				if tt.errContains != "" && err != nil {
					assert.Contains(t, err.Error(), tt.errContains)
				}
			} else {
				assert.NoError(t, err)
				if tt.checkResult != nil {
					tt.checkResult(t, fs, stdout)
				}
			}
		})
	}
}

func TestCLI_initializeManagers(t *testing.T) {
	t.Run("successful initialization", func(t *testing.T) {
		fs := setupMockFS()
		cli := NewTestCLI(fs, nil, nil, nil)
		
		summary, summaryMgr, redirectMgr, linkUpdater, err := cli.initializeManagers()
		
		assert.NoError(t, err)
		assert.NotNil(t, summary)
		assert.NotNil(t, summaryMgr)
		assert.NotNil(t, redirectMgr)
		assert.NotNil(t, linkUpdater)
		assert.Len(t, summary.Entries, 5) // Based on mock SUMMARY.md
	})
	
	t.Run("missing SUMMARY.md", func(t *testing.T) {
		fs := NewMockFileSystem()
		cli := NewTestCLI(fs, nil, nil, nil)
		
		_, _, _, _, err := cli.initializeManagers()
		
		assert.Error(t, err)
		assert.Contains(t, err.Error(), "SUMMARY.md not found")
	})
	
	t.Run("invalid SUMMARY.md", func(t *testing.T) {
		fs := NewMockFileSystem()
		fs.files["docs/SUMMARY.md"] = []byte("invalid markdown [unclosed")
		cli := NewTestCLI(fs, nil, nil, nil)
		
		_, _, _, _, err := cli.initializeManagers()
		
		// The parser is resilient and handles invalid markdown
		// It won't error on unclosed brackets
		assert.NoError(t, err)
	})
	
	t.Run("missing .gitbook.yaml", func(t *testing.T) {
		fs := NewMockFileSystem()
		fs.files["docs/SUMMARY.md"] = []byte("* [Test](test.md)")
		cli := NewTestCLI(fs, nil, nil, nil)
		
		// Should succeed even without .gitbook.yaml
		_, _, redirectMgr, _, err := cli.initializeManagers()
		
		assert.NoError(t, err)
		assert.NotNil(t, redirectMgr)
	})
}

func TestCLI_updateLinksInFiles(t *testing.T) {
	fs := setupMockFS()
	
	// Add files with links
	fs.files["docs/file1.md"] = []byte("[Link](getting-started/quick-start.md)")
	fs.files["docs/file2.md"] = []byte("[Another](tutorials/first-tutorial.md)")
	
	// Mock ReadDir to return our files
	fs.On("ReadDir", "docs").Return([]os.DirEntry{
		&mockDirEntry{name: "file1.md", isDir: false},
		&mockDirEntry{name: "file2.md", isDir: false},
	}, nil)
	
	cli := NewTestCLI(fs, nil, nil, nil)
	_, _, _, linkUpdater, _ := cli.initializeManagers()
	
	pathChanges := map[string]string{
		"getting-started/quick-start.md": "new-location/renamed.md",
	}
	
	updatedFiles := cli.updateLinksInFiles(linkUpdater, pathChanges)
	
	// Check that files were updated
	assert.NotEmpty(t, updatedFiles)
	
	// Check content was updated
	content1, _ := fs.ReadFile("docs/file1.md")
	assert.Contains(t, string(content1), "new-location/renamed.md")
	
	// File2 should not be changed
	content2, _ := fs.ReadFile("docs/file2.md")
	assert.Contains(t, string(content2), "tutorials/first-tutorial.md")
}

func TestCLI_getMarkdownFiles(t *testing.T) {
	fs := setupMockFS()
	
	// Mock ReadDir to return entries
	fs.On("ReadDir", mock.Anything).Return([]os.DirEntry{}, nil)
	
	cli := NewTestCLI(fs, nil, nil, nil)
	files := cli.getMarkdownFiles("docs")
	
	// Should return empty for mocked ReadDir
	assert.Empty(t, files)
}

// Test helper functions
func TestGenerateFilename(t *testing.T) {
	tests := []struct {
		name     string
		title    string
		dir      string
		expected string
	}{
		{
			name:     "simple title",
			title:    "My Page",
			dir:      "docs",
			expected: "docs/my-page.md",
		},
		{
			name:     "title with special characters",
			title:    "My Page! @#$%",
			dir:      "docs",
			expected: "docs/my-page.md",
		},
		{
			name:     "title with dots and slashes",
			title:    "My.Page/With/Slashes",
			dir:      "docs",
			expected: "docs/mypagewithslashes.md",
		},
		{
			name:     "empty title",
			title:    "",
			dir:      "docs",
			expected: "docs/.md",
		},
		{
			name:     "multiple spaces",
			title:    "My    New    Page",
			dir:      "docs",
			expected: "docs/my-new-page.md",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := generateFilename(tt.title, tt.dir)
			assert.Equal(t, tt.expected, result)
		})
	}
}

func TestPathToURL(t *testing.T) {
	tests := []struct {
		name     string
		path     string
		expected string
	}{
		{
			name:     "simple path",
			path:     "page.md",
			expected: "/page",
		},
		{
			name:     "nested path",
			path:     "docs/page.md",
			expected: "/docs/page",
		},
		{
			name:     "README.md in subdirectory",
			path:     "docs/README.md",
			expected: "/docs",
		},
		{
			name:     "root README.md",
			path:     "README.md",
			expected: "",
		},
		{
			name:     "deeply nested",
			path:     "docs/tutorials/advanced/page.md",
			expected: "/docs/tutorials/advanced/page",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := pathToURL(tt.path)
			assert.Equal(t, tt.expected, result)
		})
	}
}

func TestUpdateH1Header(t *testing.T) {
	tests := []struct {
		name     string
		content  []byte
		newTitle string
		expected string
	}{
		{
			name:     "simple H1",
			content:  []byte("# Old Title\n\nContent"),
			newTitle: "New Title",
			expected: "# New Title\n\nContent",
		},
		{
			name:     "H1 with spaces",
			content:  []byte("#   Old Title   \n\nContent"),
			newTitle: "New Title",
			expected: "# New Title\n\nContent",
		},
		{
			name:     "no H1",
			content:  []byte("## Subtitle\n\nContent"),
			newTitle: "New Title",
			expected: "## Subtitle\n\nContent",
		},
		{
			name:     "multiple H1s",
			content:  []byte("# First\n\n# Second\n\nContent"),
			newTitle: "New Title",
			expected: "# New Title\n\n# Second\n\nContent",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := updateH1Header(tt.content, tt.newTitle)
			assert.Equal(t, tt.expected, string(result))
		})
	}
}

func TestAdjustRelativeLinks(t *testing.T) {
	tests := []struct {
		name     string
		content  []byte
		oldDepth int
		newDepth int
		expected string
	}{
		{
			name:     "same depth",
			content:  []byte("[Link](page.md)"),
			oldDepth: 1,
			newDepth: 1,
			expected: "[Link](page.md)",
		},
		{
			name:     "moving deeper",
			content:  []byte("[Link](../page.md)"),
			oldDepth: 1,
			newDepth: 2,
			expected: "[Link](../../page.md)",
		},
		{
			name:     "moving shallower",
			content:  []byte("[Link](../page.md)"),
			oldDepth: 2,
			newDepth: 1,
			expected: "[Link](page.md)",
		},
	}
	
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := adjustRelativeLinks(tt.content, tt.oldDepth, tt.newDepth)
			assert.Equal(t, tt.expected, string(result))
		})
	}
}

// Benchmark tests
func BenchmarkGenerateFilename(b *testing.B) {
	for i := 0; i < b.N; i++ {
		generateFilename("My Complex Page Title! @#$%", "docs/sub/dir")
	}
}

func BenchmarkPathToURL(b *testing.B) {
	for i := 0; i < b.N; i++ {
		pathToURL("docs/tutorials/advanced/README.md")
	}
}

func BenchmarkUpdateH1Header(b *testing.B) {
	content := []byte("# Old Title\n\n" + strings.Repeat("Content line\n", 100))
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		updateH1Header(content, "New Title")
	}
}

func BenchmarkAdjustRelativeLinks(b *testing.B) {
	content := []byte(strings.Repeat("[Link](../page.md)\n", 100))
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		adjustRelativeLinks(content, 2, 1)
	}
}