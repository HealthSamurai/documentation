package core

import "time"

// Entry represents a single entry in SUMMARY.md
type Entry struct {
	ID          string
	Type        EntryType // "entry" or "section"
	Title       string
	Path        string // relative to docs/
	Level       int    // indentation level (0-5)
	Index       int    // position in the list
	HasChildren bool
	Section     string // parent section title
	Line        int    // line number in SUMMARY.md
}

// EntryType defines the type of entry
type EntryType string

const (
	EntryTypeItem    EntryType = "entry"
	EntryTypeSection EntryType = "section"
)

// Summary represents parsed SUMMARY.md
type Summary struct {
	Entries      []Entry
	RawContent   string
	LastModified time.Time
}

// Link represents a markdown link
type Link struct {
	Text     string
	URL      string
	Type     LinkType
	Line     int
	Column   int
	IsAnchor bool
	Anchor   string
}

// LinkType defines different link types
type LinkType string

const (
	LinkTypeMarkdown  LinkType = "markdown"  // [text](url)
	LinkTypeReference LinkType = "reference" // [text][ref]
	LinkTypeImage     LinkType = "image"     // ![alt](url)
	LinkTypeHTML      LinkType = "html"      // <a href="url">
)

// Operation represents a modification operation
type Operation struct {
	Type      OperationType
	Timestamp time.Time
	EntryID   string
	
	// For move operations
	OldPosition int
	NewPosition int
	OldLevel    int
	NewLevel    int
	
	// For rename operations
	OldTitle string
	NewTitle string
	OldPath  string
	NewPath  string
	
	// Side effects
	RedirectsAdded []Redirect
	FilesModified  []string
	LinksUpdated   int
}

// OperationType defines operation types
type OperationType string

const (
	OpTypeMove   OperationType = "move"
	OpTypeRename OperationType = "rename"
	OpTypeDelete OperationType = "delete"
)

// Redirect represents a redirect entry
type Redirect struct {
	From string
	To   string
}

// RedirectConfig represents .gitbook.yaml redirects section
type RedirectConfig struct {
	Redirects map[string]string `yaml:"redirects"`
}