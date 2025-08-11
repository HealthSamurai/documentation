package utils

import (
	"io/fs"
	"path/filepath"

	"github.com/f2-go/internal/core"
	"github.com/spf13/afero"
)

// AferoAdapter adapts afero.Fs to our FileSystem interface
type AferoAdapter struct {
	fs afero.Fs
}

// NewFileSystem creates a new filesystem adapter
func NewFileSystem(fs afero.Fs) core.FileSystem {
	return &AferoAdapter{fs: fs}
}

// NewMemoryFileSystem creates an in-memory filesystem for testing
func NewMemoryFileSystem() core.FileSystem {
	return &AferoAdapter{fs: afero.NewMemMapFs()}
}

// NewOsFileSystem creates a filesystem adapter for the real OS filesystem
func NewOsFileSystem() core.FileSystem {
	return &AferoAdapter{fs: afero.NewOsFs()}
}

// ReadFile reads a file
func (a *AferoAdapter) ReadFile(path string) ([]byte, error) {
	return afero.ReadFile(a.fs, path)
}

// WriteFile writes a file
func (a *AferoAdapter) WriteFile(path string, data []byte, perm fs.FileMode) error {
	// Ensure directory exists
	dir := filepath.Dir(path)
	if err := a.MkdirAll(dir, 0755); err != nil {
		return err
	}
	return afero.WriteFile(a.fs, path, data, perm)
}

// MoveFile moves a file from src to dst
func (a *AferoAdapter) MoveFile(src, dst string) error {
	// Ensure destination directory exists
	dstDir := filepath.Dir(dst)
	if err := a.MkdirAll(dstDir, 0755); err != nil {
		return err
	}
	
	// Read source file
	data, err := a.ReadFile(src)
	if err != nil {
		return err
	}
	
	// Get file info for permissions
	info, err := a.fs.Stat(src)
	if err != nil {
		return err
	}
	
	// Write to destination
	if err := a.WriteFile(dst, data, info.Mode()); err != nil {
		return err
	}
	
	// Remove source
	return a.Remove(src)
}

// Remove removes a file or directory
func (a *AferoAdapter) Remove(path string) error {
	return a.fs.RemoveAll(path)
}

// Exists checks if a path exists
func (a *AferoAdapter) Exists(path string) bool {
	exists, _ := afero.Exists(a.fs, path)
	return exists
}

// MkdirAll creates a directory and all parents
func (a *AferoAdapter) MkdirAll(path string, perm fs.FileMode) error {
	return a.fs.MkdirAll(path, perm)
}

// ReadDir reads a directory
func (a *AferoAdapter) ReadDir(path string) ([]fs.DirEntry, error) {
	infos, err := afero.ReadDir(a.fs, path)
	if err != nil {
		return nil, err
	}
	
	// Convert FileInfo to DirEntry
	entries := make([]fs.DirEntry, len(infos))
	for i, info := range infos {
		entries[i] = fileInfoToDirEntry{info}
	}
	return entries, nil
}

// fileInfoToDirEntry adapts os.FileInfo to fs.DirEntry
type fileInfoToDirEntry struct {
	fs.FileInfo
}

func (f fileInfoToDirEntry) Type() fs.FileMode {
	return f.Mode().Type()
}

func (f fileInfoToDirEntry) Info() (fs.FileInfo, error) {
	return f.FileInfo, nil
}

// Walk walks the file tree rooted at root, calling walkFn for each file or directory
func (a *AferoAdapter) Walk(root string, walkFn func(path string, isDir bool) error) error {
	return afero.Walk(a.fs, root, func(path string, info fs.FileInfo, err error) error {
		if err != nil {
			return err
		}
		return walkFn(path, info.IsDir())
	})
}