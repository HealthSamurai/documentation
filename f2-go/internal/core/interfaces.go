package core

import "io/fs"

// FileSystem provides an abstraction for file operations
type FileSystem interface {
	ReadFile(path string) ([]byte, error)
	WriteFile(path string, data []byte, perm fs.FileMode) error
	MoveFile(src, dst string) error
	Remove(path string) error
	Exists(path string) bool
	MkdirAll(path string, perm fs.FileMode) error
	ReadDir(path string) ([]fs.DirEntry, error)
	Walk(root string, walkFn func(path string, isDir bool) error) error
}

// SummaryManager handles SUMMARY.md operations
type SummaryManager interface {
	Move(entryID string, targetPosition int, newLevel int) error
	Rename(entryID string, newTitle, newPath string) error
	Delete(entryID string) error
	Serialize() []byte
	FindEntry(id string) *Entry
	GetEntries() []Entry
}

// LinkUpdater handles link operations
type LinkUpdater interface {
	FindLinks(content []byte) []Link
	UpdateLinks(content []byte, pathChanges map[string]string) ([]byte, bool, int)
	CalculateRelativePath(from, to string) string
	ResolveReferences(content []byte) map[string]string
}

// RedirectManager handles redirect operations
type RedirectManager interface {
	LoadRedirects(content []byte) (*RedirectConfig, error)
	AddRedirect(from, to string) error
	RemoveRedirectsTo(path string) error
	HasCircularRedirect(from, to string) bool
	Serialize() []byte
	GetRedirects() map[string]string
}