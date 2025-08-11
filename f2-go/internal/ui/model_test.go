package ui

import (
	"testing"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/f2-go/internal/core"
	"github.com/f2-go/internal/parser"
	"github.com/f2-go/internal/utils"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestModel(t *testing.T) {
	// Setup test data
	summaryContent := `# Table of contents

* [A](a.md)
  * [A1](a1.md)
* [B](b.md)

## Section

* [C](c.md)`

	// Create managers
	summaryParser := core.NewSummaryParser()
	summary, err := summaryParser.Parse([]byte(summaryContent))
	require.NoError(t, err)

	summaryManager := core.NewSummaryManager(summary)
	redirectManager := core.NewRedirectManager()
	linkUpdater := parser.NewLinkParser()
	fs := utils.NewMemoryFileSystem()

	// Create model
	model := New(summaryManager, redirectManager, linkUpdater, fs, ".")

	t.Run("initial state", func(t *testing.T) {
		assert.Equal(t, ModeNormal, model.mode)
		assert.Equal(t, 0, model.cursor)
		assert.Equal(t, "", model.selectedID)
		assert.Len(t, model.entries, 5) // A, A1, B, Section, C
	})

	t.Run("navigation", func(t *testing.T) {
		// Test down navigation
		newModel, _ := model.Update(tea.KeyMsg{Type: tea.KeyRunes, Runes: []rune{'j'}})
		m := newModel.(Model)
		assert.Equal(t, 1, m.cursor)

		// Test up navigation
		newModel, _ = m.Update(tea.KeyMsg{Type: tea.KeyRunes, Runes: []rune{'k'}})
		m = newModel.(Model)
		assert.Equal(t, 0, m.cursor)
	})

	t.Run("expand/collapse", func(t *testing.T) {
		// Find entry A (which has children)
		var entryA *core.Entry
		for i, e := range model.entries {
			if e.Title == "A" && e.HasChildren {
				entryA = &model.entries[i]
				break
			}
		}
		require.NotNil(t, entryA)

		// Expand
		model.cursor = 0 // Position on A
		newModel, _ := model.Update(tea.KeyMsg{Type: tea.KeyRunes, Runes: []rune{' '}})
		m := newModel.(Model)
		assert.True(t, m.expandedFolders[entryA.ID])

		// Collapse
		newModel, _ = m.Update(tea.KeyMsg{Type: tea.KeyRunes, Runes: []rune{' '}})
		m = newModel.(Model)
		assert.False(t, m.expandedFolders[entryA.ID])
	})

	t.Run("mode transitions", func(t *testing.T) {
		// First, ensure we refresh visible entries and expand all folders
		model.refreshEntries()
		for _, e := range model.entries {
			if e.HasChildren {
				model.expandedFolders[e.ID] = true
			}
		}
		
		// Find B entry's visible position
		visibleIndex := 0
		for _, e := range model.entries {
			if model.shouldShowEntry(e) {
				if e.Title == "B" {
					break
				}
				visibleIndex++
			}
		}
		
		// Enter move mode
		model.cursor = visibleIndex
		newModel, _ := model.Update(tea.KeyMsg{Type: tea.KeyEnter})
		m := newModel.(Model)
		assert.Equal(t, ModeMove, m.mode)
		assert.NotEmpty(t, m.moveSourceID)

		// Cancel move mode
		newModel, _ = m.Update(tea.KeyMsg{Type: tea.KeyEscape})
		m = newModel.(Model)
		assert.Equal(t, ModeNormal, m.mode)
		assert.Empty(t, m.moveSourceID)
	})

	t.Run("render tree", func(t *testing.T) {
		// Test tree rendering
		tree := model.renderTree()
		assert.Contains(t, tree, "A")
		assert.Contains(t, tree, "B")
		assert.Contains(t, tree, "Section")
		assert.Contains(t, tree, "C")

		// With cursor
		assert.Contains(t, tree, ">") // Cursor indicator
	})

	t.Run("entry visibility", func(t *testing.T) {
		// A1 should not be visible when A is collapsed
		model.expandedFolders = make(map[string]bool)
		
		var a1Entry *core.Entry
		for i, e := range model.entries {
			if e.Title == "A1" {
				a1Entry = &model.entries[i]
				break
			}
		}
		require.NotNil(t, a1Entry)

		assert.False(t, model.shouldShowEntry(*a1Entry))

		// Expand A
		for _, e := range model.entries {
			if e.Title == "A" && e.HasChildren {
				model.expandedFolders[e.ID] = true
				break
			}
		}

		assert.True(t, model.shouldShowEntry(*a1Entry))
	})

	t.Run("get entry at cursor", func(t *testing.T) {
		model.cursor = 0
		entry := model.getEntryAtCursor()
		require.NotNil(t, entry)
		assert.Equal(t, "A", entry.Title)

		model.cursor = 2
		entry = model.getEntryAtCursor()
		require.NotNil(t, entry)
		assert.Equal(t, "B", entry.Title)
	})
}

func TestRenderEntry(t *testing.T) {
	model := Model{
		styles:          DefaultStyles(),
		expandedFolders: make(map[string]bool),
	}

	tests := []struct {
		name     string
		entry    core.Entry
		isCursor bool
		expanded bool
		want     []string // strings that should be in the output
	}{
		{
			name: "section entry",
			entry: core.Entry{
				Type:  core.EntryTypeSection,
				Title: "Test Section",
				Level: 0,
			},
			want: []string{"##", "Test Section"},
		},
		{
			name: "regular item",
			entry: core.Entry{
				Type:  core.EntryTypeItem,
				Title: "Regular Item",
				Level: 0,
			},
			want: []string{"•", "Regular Item"},
		},
		{
			name: "item with children collapsed",
			entry: core.Entry{
				ID:          "parent",
				Type:        core.EntryTypeItem,
				Title:       "Parent",
				Level:       0,
				HasChildren: true,
			},
			expanded: false,
			want:     []string{"▶", "Parent"},
		},
		{
			name: "item with children expanded",
			entry: core.Entry{
				ID:          "parent",
				Type:        core.EntryTypeItem,
				Title:       "Parent",
				Level:       0,
				HasChildren: true,
			},
			expanded: true,
			want:     []string{"▼", "Parent"},
		},
		{
			name: "cursor item",
			entry: core.Entry{
				Type:  core.EntryTypeItem,
				Title: "Cursor Item",
				Level: 0,
			},
			isCursor: true,
			want:     []string{">", "Cursor Item"},
		},
		{
			name: "indented item",
			entry: core.Entry{
				Type:  core.EntryTypeItem,
				Title: "Indented",
				Level: 2,
			},
			want: []string{"    ", "Indented"}, // 2 levels = 4 spaces
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.expanded {
				model.expandedFolders[tt.entry.ID] = true
			} else {
				model.expandedFolders[tt.entry.ID] = false
			}

			result := model.renderEntry(tt.entry, tt.isCursor)

			for _, want := range tt.want {
				assert.Contains(t, result, want)
			}
		})
	}
}

func TestWindowResize(t *testing.T) {
	model := Model{
		styles: DefaultStyles(),
	}

	// Test window resize
	newModel, _ := model.Update(tea.WindowSizeMsg{
		Width:  80,
		Height: 24,
	})
	m := newModel.(Model)

	assert.Equal(t, 80, m.width)
	assert.Equal(t, 24, m.height)
	assert.Equal(t, 80, m.viewport.Width)
	assert.Equal(t, 20, m.viewport.Height) // 24 - 4 (header + status)
}

func TestStatusBar(t *testing.T) {
	model := Model{
		mode:           ModeNormal,
		unsavedChanges: 3,
		width:          80,
		styles:         DefaultStyles(),
	}

	status := model.renderStatusBar()
	
	assert.Contains(t, status, "[Navigate]")
	assert.Contains(t, status, "3 unsaved changes")
	assert.Contains(t, status, "[↑↓ Navigate]")
	assert.Contains(t, status, "[q Quit]")

	// Test different modes
	model.mode = ModeMove
	status = model.renderStatusBar()
	assert.Contains(t, status, "[Move]")

	model.mode = ModeRename
	status = model.renderStatusBar()
	assert.Contains(t, status, "[Rename]")

	model.mode = ModeDelete
	status = model.renderStatusBar()
	assert.Contains(t, status, "[Delete]")
}