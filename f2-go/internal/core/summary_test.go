package core

import (
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestParseSummary(t *testing.T) {
	tests := []struct {
		name    string
		content string
		want    []Entry
		wantErr bool
	}{
		{
			name: "simple flat list",
			content: `# Table of contents

* [Page A](a.md)
* [Page B](b.md)
* [Page C](c.md)`,
			want: []Entry{
				{Type: EntryTypeItem, Title: "Page A", Path: "a.md", Level: 0, Index: 0, Line: 3},
				{Type: EntryTypeItem, Title: "Page B", Path: "b.md", Level: 0, Index: 1, Line: 4},
				{Type: EntryTypeItem, Title: "Page C", Path: "c.md", Level: 0, Index: 2, Line: 5},
			},
		},
		{
			name: "nested entries",
			content: `# Table of contents

* [Getting Started](getting-started/README.md)
  * [Installation](getting-started/install.md)
  * [Configuration](getting-started/config.md)
* [Guides](guides/README.md)`,
			want: []Entry{
				{Type: EntryTypeItem, Title: "Getting Started", Path: "getting-started/README.md", Level: 0, Index: 0, HasChildren: true, Line: 3},
				{Type: EntryTypeItem, Title: "Installation", Path: "getting-started/install.md", Level: 1, Index: 1, Line: 4},
				{Type: EntryTypeItem, Title: "Configuration", Path: "getting-started/config.md", Level: 1, Index: 2, Line: 5},
				{Type: EntryTypeItem, Title: "Guides", Path: "guides/README.md", Level: 0, Index: 3, Line: 6},
			},
		},
		{
			name: "with sections",
			content: `# Table of contents

* [Introduction](intro.md)

## Getting Started

* [Installation](install.md)
* [Setup](setup.md)

## Reference

* [API](api.md)`,
			want: []Entry{
				{Type: EntryTypeItem, Title: "Introduction", Path: "intro.md", Level: 0, Index: 0, Line: 3},
				{Type: EntryTypeSection, Title: "Getting Started", Level: 0, Index: 1, Line: 5},
				{Type: EntryTypeItem, Title: "Installation", Path: "install.md", Level: 0, Index: 2, Section: "Getting Started", Line: 7},
				{Type: EntryTypeItem, Title: "Setup", Path: "setup.md", Level: 0, Index: 3, Section: "Getting Started", Line: 8},
				{Type: EntryTypeSection, Title: "Reference", Level: 0, Index: 4, Line: 10},
				{Type: EntryTypeItem, Title: "API", Path: "api.md", Level: 0, Index: 5, Section: "Reference", Line: 12},
			},
		},
		{
			name: "deep nesting",
			content: `# Table of contents

* [Root](root.md)
  * [Level 1](level1.md)
    * [Level 2](level2.md)
      * [Level 3](level3.md)`,
			want: []Entry{
				{Type: EntryTypeItem, Title: "Root", Path: "root.md", Level: 0, Index: 0, HasChildren: true, Line: 3},
				{Type: EntryTypeItem, Title: "Level 1", Path: "level1.md", Level: 1, Index: 1, HasChildren: true, Line: 4},
				{Type: EntryTypeItem, Title: "Level 2", Path: "level2.md", Level: 2, Index: 2, HasChildren: true, Line: 5},
				{Type: EntryTypeItem, Title: "Level 3", Path: "level3.md", Level: 3, Index: 3, Line: 6},
			},
		},
		{
			name: "mixed indentation types",
			content: `# Table of contents

* [Tab indent](tab.md)
	* [With tab](with-tab.md)
  * [With spaces](with-spaces.md)`,
			want: []Entry{
				{Type: EntryTypeItem, Title: "Tab indent", Path: "tab.md", Level: 0, Index: 0, HasChildren: true, Line: 3},
				{Type: EntryTypeItem, Title: "With tab", Path: "with-tab.md", Level: 1, Index: 1, Line: 4},
				{Type: EntryTypeItem, Title: "With spaces", Path: "with-spaces.md", Level: 1, Index: 2, Line: 5},
			},
		},
		{
			name:    "empty content",
			content: "",
			want:    []Entry{},
		},
		{
			name: "only sections no entries",
			content: `# Table of contents

## Section 1

## Section 2`,
			want: []Entry{
				{Type: EntryTypeSection, Title: "Section 1", Level: 0, Index: 0, Line: 3},
				{Type: EntryTypeSection, Title: "Section 2", Level: 0, Index: 1, Line: 5},
			},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewSummaryParser()
			summary, err := parser.Parse([]byte(tt.content))
			
			if tt.wantErr {
				assert.Error(t, err)
				return
			}
			
			require.NoError(t, err)
			require.NotNil(t, summary)
			
			// Compare entries
			assert.Equal(t, len(tt.want), len(summary.Entries), "number of entries mismatch")
			
			for i, want := range tt.want {
				if i >= len(summary.Entries) {
					break
				}
				got := summary.Entries[i]
				
				assert.Equal(t, want.Type, got.Type, "entry %d type", i)
				assert.Equal(t, want.Title, got.Title, "entry %d title", i)
				assert.Equal(t, want.Path, got.Path, "entry %d path", i)
				assert.Equal(t, want.Level, got.Level, "entry %d level", i)
				assert.Equal(t, want.Index, got.Index, "entry %d index", i)
				assert.Equal(t, want.HasChildren, got.HasChildren, "entry %d hasChildren", i)
				assert.Equal(t, want.Section, got.Section, "entry %d section", i)
				assert.Equal(t, want.Line, got.Line, "entry %d line", i)
			}
		})
	}
}

func TestSummaryMove(t *testing.T) {
	tests := []struct {
		name        string
		summary     string
		moveTitle   string
		targetIndex int
		newLevel    int
		wantOrder   []string // expected title order after move
		wantLevels  []int    // expected levels after move
		wantErr     bool
	}{
		{
			name: "move within same level",
			summary: `# Table of contents

* [A](a.md)
* [B](b.md)
* [C](c.md)`,
			moveTitle:   "C",
			targetIndex: 1,
			newLevel:    0,
			wantOrder:   []string{"A", "C", "B"},
			wantLevels:  []int{0, 0, 0},
		},
		{
			name: "move to become child",
			summary: `# Table of contents

* [A](a.md)
* [B](b.md)
* [C](c.md)`,
			moveTitle:   "C",
			targetIndex: 1,
			newLevel:    1,
			wantOrder:   []string{"A", "C", "B"},
			wantLevels:  []int{0, 1, 0},
		},
		{
			name: "move child to top level",
			summary: `# Table of contents

* [A](a.md)
  * [B](b.md)
* [C](c.md)`,
			moveTitle:   "B",
			targetIndex: 2,
			newLevel:    0,
			wantOrder:   []string{"A", "B", "C"},
			wantLevels:  []int{0, 0, 0},
		},
		{
			name: "move with children preserves hierarchy",
			summary: `# Table of contents

* [A](a.md)
* [B](b.md)
  * [B1](b1.md)
  * [B2](b2.md)
* [C](c.md)`,
			moveTitle:   "B",
			targetIndex: 4,
			newLevel:    0,
			wantOrder:   []string{"A", "B", "B1", "B2", "C"},
			wantLevels:  []int{0, 0, 1, 1, 0},
		},
		// TODO: Fix circular dependency detection
		// {
		// 	name: "cannot move parent under its child",
		// 	summary: `# Table of contents

		// * [Root](root.md)
		//   * [A](a.md)
		//     * [B](b.md)
		//   * [C](c.md)`,
		// 	moveTitle:   "A",
		// 	targetIndex: 3,  // Try to move A after B
		// 	newLevel:    2,  // With level 2, this would make A a child of B
		// 	wantErr:     true,
		// },
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewSummaryParser()
			summary, err := parser.Parse([]byte(tt.summary))
			require.NoError(t, err)

			// Create manager
			manager := NewSummaryManager(summary)
			
			// Find entry to move
			var entryID string
			for _, e := range summary.Entries {
				if e.Title == tt.moveTitle {
					entryID = e.ID
					break
				}
			}
			require.NotEmpty(t, entryID, "entry to move not found")

			// Perform move
			err = manager.Move(entryID, tt.targetIndex, tt.newLevel)
			
			if tt.wantErr {
				assert.Error(t, err)
				return
			}
			
			require.NoError(t, err)
			
			// Check result
			entries := manager.GetEntries()
			var items []Entry
			for _, e := range entries {
				if e.Type == EntryTypeItem {
					items = append(items, e)
				}
			}
			
			// Verify order
			require.Equal(t, len(tt.wantOrder), len(items), "number of items mismatch")
			for i, title := range tt.wantOrder {
				assert.Equal(t, title, items[i].Title, "item %d title", i)
				assert.Equal(t, tt.wantLevels[i], items[i].Level, "item %d level", i)
			}
		})
	}
}

func TestSummaryRename(t *testing.T) {
	tests := []struct {
		name      string
		summary   string
		oldTitle  string
		newTitle  string
		newPath   string
		wantTitle string
		wantPath  string
		wantErr   bool
	}{
		{
			name: "rename title only",
			summary: `# Table of contents

* [Quick Start](quick-start.md)`,
			oldTitle:  "Quick Start",
			newTitle:  "Getting Started",
			newPath:   "", // keep same path
			wantTitle: "Getting Started",
			wantPath:  "quick-start.md",
		},
		{
			name: "rename title and path",
			summary: `# Table of contents

* [Quick Start](quick-start.md)`,
			oldTitle:  "Quick Start",
			newTitle:  "Getting Started",
			newPath:   "getting-started.md",
			wantTitle: "Getting Started",
			wantPath:  "getting-started.md",
		},
		{
			name: "rename nested entry",
			summary: `# Table of contents

* [Guides](guides/README.md)
  * [Quick Start](guides/quick-start.md)`,
			oldTitle:  "Quick Start",
			newTitle:  "Installation Guide",
			newPath:   "guides/installation.md",
			wantTitle: "Installation Guide",
			wantPath:  "guides/installation.md",
		},
		{
			name: "invalid - empty title",
			summary: `# Table of contents

* [Test](test.md)`,
			oldTitle: "Test",
			newTitle: "",
			wantErr:  true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewSummaryParser()
			summary, err := parser.Parse([]byte(tt.summary))
			require.NoError(t, err)

			manager := NewSummaryManager(summary)
			
			// Find entry
			var entryID string
			for _, e := range summary.Entries {
				if e.Title == tt.oldTitle {
					entryID = e.ID
					break
				}
			}
			require.NotEmpty(t, entryID)

			// Perform rename
			err = manager.Rename(entryID, tt.newTitle, tt.newPath)
			
			if tt.wantErr {
				assert.Error(t, err)
				return
			}
			
			require.NoError(t, err)
			
			// Check result
			entry := manager.FindEntry(entryID)
			require.NotNil(t, entry)
			assert.Equal(t, tt.wantTitle, entry.Title)
			if tt.wantPath != "" {
				assert.Equal(t, tt.wantPath, entry.Path)
			}
		})
	}
}

func TestSummaryDelete(t *testing.T) {
	tests := []struct {
		name         string
		summary      string
		deleteTitle  string
		wantTitles   []string
		wantErr      bool
		errContains  string
	}{
		{
			name: "delete leaf entry",
			summary: `# Table of contents

* [A](a.md)
* [B](b.md)
* [C](c.md)`,
			deleteTitle: "B",
			wantTitles:  []string{"A", "C"},
		},
		{
			name: "cannot delete entry with children",
			summary: `# Table of contents

* [A](a.md)
  * [A1](a1.md)
  * [A2](a2.md)
* [B](b.md)`,
			deleteTitle: "A",
			wantErr:     true,
			errContains: "has children",
		},
		{
			name: "delete last child",
			summary: `# Table of contents

* [A](a.md)
  * [A1](a1.md)`,
			deleteTitle: "A1",
			wantTitles:  []string{"A"},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewSummaryParser()
			summary, err := parser.Parse([]byte(tt.summary))
			require.NoError(t, err)

			manager := NewSummaryManager(summary)
			
			// Find entry
			var entryID string
			for _, e := range summary.Entries {
				if e.Title == tt.deleteTitle {
					entryID = e.ID
					break
				}
			}
			require.NotEmpty(t, entryID)

			// Perform delete
			err = manager.Delete(entryID)
			
			if tt.wantErr {
				assert.Error(t, err)
				if tt.errContains != "" {
					assert.Contains(t, err.Error(), tt.errContains)
				}
				return
			}
			
			require.NoError(t, err)
			
			// Check result
			entries := manager.GetEntries()
			var titles []string
			for _, e := range entries {
				if e.Type == EntryTypeItem {
					titles = append(titles, e.Title)
				}
			}
			assert.Equal(t, tt.wantTitles, titles)
		})
	}
}

func TestSummarySerialize(t *testing.T) {
	tests := []struct {
		name    string
		summary string
		ops     func(SummaryManager)
		want    string
	}{
		{
			name: "preserve formatting after move",
			summary: `# Table of contents

* [A](a.md)
* [B](b.md)
* [C](c.md)`,
			ops: func(m SummaryManager) {
				// Move C after A
				entry := findByTitle(m, "C")
				m.Move(entry.ID, 1, 0)
			},
			want: `# Table of contents

* [A](a.md)
* [C](c.md)
* [B](b.md)
`,
		},
		{
			name: "preserve sections",
			summary: `# Table of contents

## Section 1

* [A](a.md)
* [B](b.md)

## Section 2

* [C](c.md)`,
			ops: func(m SummaryManager) {
				// Move B to Section 2
				entry := findByTitle(m, "B")
				m.Move(entry.ID, 5, 0) // after C
			},
			want: `# Table of contents

## Section 1

* [A](a.md)

## Section 2

* [C](c.md)
* [B](b.md)
`,
		},
		{
			name: "handle indentation",
			summary: `# Table of contents

* [A](a.md)
* [B](b.md)`,
			ops: func(m SummaryManager) {
				// Make B a child of A
				entry := findByTitle(m, "B")
				m.Move(entry.ID, 1, 1)
			},
			want: `# Table of contents

* [A](a.md)
  * [B](b.md)
`,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			parser := NewSummaryParser()
			summary, err := parser.Parse([]byte(tt.summary))
			require.NoError(t, err)

			manager := NewSummaryManager(summary)
			
			// Apply operations
			if tt.ops != nil {
				tt.ops(manager)
			}
			
			// Serialize
			result := manager.Serialize()
			assert.Equal(t, tt.want, string(result))
		})
	}
}

// Helper function
func findByTitle(m SummaryManager, title string) *Entry {
	for _, e := range m.GetEntries() {
		if e.Title == title {
			return &e
		}
	}
	return nil
}