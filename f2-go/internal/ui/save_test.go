package ui

import (
	"testing"

	"github.com/f2-go/internal/core"
	"github.com/f2-go/internal/parser"
	"github.com/f2-go/internal/utils"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestSave(t *testing.T) {
	// Setup test data
	summaryContent := `# Table of contents

* [Introduction](README.md)
* [Chapter 1](chapter1.md)
  * [Section 1.1](chapter1/section1.md)
* [Chapter 2](chapter2.md)`

	// Create managers
	summaryParser := core.NewSummaryParser()
	summary, err := summaryParser.Parse([]byte(summaryContent))
	require.NoError(t, err)

	summaryManager := core.NewSummaryManager(summary)
	redirectManager := core.NewRedirectManager()
	linkUpdater := parser.NewLinkParser()
	fs := utils.NewMemoryFileSystem()

	// Create test files
	require.NoError(t, fs.WriteFile("README.md", []byte("# Introduction\n\nWelcome!"), 0644))
	require.NoError(t, fs.WriteFile("chapter1.md", []byte("# Chapter 1\n\nContent"), 0644))
	require.NoError(t, fs.WriteFile("chapter1/section1.md", []byte("# Section 1.1\n\nMore content"), 0644))
	require.NoError(t, fs.WriteFile("chapter2.md", []byte("# Chapter 2\n\nFinal content"), 0644))
	require.NoError(t, fs.WriteFile("SUMMARY.md", []byte(summaryContent), 0644))

	// Create model
	model := &Model{
		summaryManager:  summaryManager,
		redirectManager: redirectManager,
		linkUpdater:     linkUpdater,
		fileSystem:      fs,
		operations:      []core.Operation{},
		unsavedChanges:  0,
		rootDir:         ".",
		changesSummary:  []string{},
	}

	t.Run("save without changes", func(t *testing.T) {
		err := model.Save()
		assert.NoError(t, err)
		assert.Equal(t, 0, model.unsavedChanges)
	})

	t.Run("save rename operation", func(t *testing.T) {
		// Debug: print all entries
		entries := summaryManager.GetEntries()
		var chapter1ID string
		for _, e := range entries {
			if e.Title == "Chapter 1" {
				chapter1ID = e.ID
				break
			}
		}
		require.NotEmpty(t, chapter1ID, "Chapter 1 entry not found")
		
		// Perform rename
		entry := summaryManager.FindEntry(chapter1ID)
		require.NotNil(t, entry)
		
		err := summaryManager.Rename(chapter1ID, "Chapter One", "chapter-one.md")
		require.NoError(t, err)
		
		// Track operation
		model.operations = append(model.operations, core.Operation{
			Type:     core.OpTypeRename,
			EntryID:  chapter1ID,
			OldTitle: "Chapter 1",
			NewTitle: "Chapter One",
			OldPath:  "chapter1.md",
			NewPath:  "chapter-one.md",
		})
		model.unsavedChanges = 1

		// Save
		err = model.Save()
		assert.NoError(t, err)
		assert.Equal(t, 0, model.unsavedChanges)
		assert.Empty(t, model.operations)

		// Verify file was renamed
		assert.False(t, fs.Exists("chapter1.md"))
		assert.True(t, fs.Exists("chapter-one.md"))

		// Verify SUMMARY.md was updated
		summaryData, err := fs.ReadFile("SUMMARY.md")
		require.NoError(t, err)
		assert.Contains(t, string(summaryData), "Chapter One")
		assert.Contains(t, string(summaryData), "chapter-one.md")
	})

	t.Run("save delete operation", func(t *testing.T) {
		// Reset
		model.operations = []core.Operation{}
		
		// Find Chapter 2 ID
		entries := summaryManager.GetEntries()
		var chapter2ID string
		for _, e := range entries {
			if e.Title == "Chapter 2" {
				chapter2ID = e.ID
				break
			}
		}
		require.NotEmpty(t, chapter2ID, "Chapter 2 entry not found")
		
		// Perform delete
		err := summaryManager.Delete(chapter2ID)
		require.NoError(t, err)
		
		// Track operation
		model.operations = append(model.operations, core.Operation{
			Type:    core.OpTypeDelete,
			EntryID: chapter2ID,
			OldPath: "chapter2.md",
		})
		model.unsavedChanges = 1

		// Save
		err = model.Save()
		assert.NoError(t, err)
		assert.Equal(t, 0, model.unsavedChanges)

		// Verify file was deleted
		assert.False(t, fs.Exists("chapter2.md"))

		// Verify SUMMARY.md was updated
		summaryData, err := fs.ReadFile("SUMMARY.md")
		require.NoError(t, err)
		assert.NotContains(t, string(summaryData), "Chapter 2")
	})

	t.Run("save with link updates", func(t *testing.T) {
		// Create a file with links
		linkedContent := `# Linked Document

See [Chapter 1](chapter1.md) and [Section 1.1](chapter1/section1.md).`
		
		require.NoError(t, fs.WriteFile("linked.md", []byte(linkedContent), 0644))
		
		// Find Section 1.1 ID
		entries := summaryManager.GetEntries()
		var section11ID string
		for _, e := range entries {
			if e.Title == "Section 1.1" {
				section11ID = e.ID
				break
			}
		}
		require.NotEmpty(t, section11ID, "Section 1.1 entry not found")
		
		// Rename with links that need updating
		err := summaryManager.Rename(section11ID, "Section One", "chapter1/section-one.md")
		require.NoError(t, err)
		
		model.operations = []core.Operation{{
			Type:     core.OpTypeRename,
			EntryID:  section11ID,
			OldPath:  "chapter1/section1.md",
			NewPath:  "chapter1/section-one.md",
		}}
		model.unsavedChanges = 1

		// Save
		err = model.Save()
		assert.NoError(t, err)

		// Verify links were updated
		linkedData, err := fs.ReadFile("linked.md")
		require.NoError(t, err)
		assert.Contains(t, string(linkedData), "chapter1/section-one.md")
		assert.NotContains(t, string(linkedData), "chapter1/section1.md")
	})

	t.Run("save creates redirects", func(t *testing.T) {
		// Reset operations
		model.operations = []core.Operation{}
		model.unsavedChanges = 0
		
		// Add a redirect
		err := redirectManager.AddRedirect("old-path.md", "new-path.md")
		require.NoError(t, err)

		// Make a change to trigger save
		model.unsavedChanges = 1

		// Save
		err = model.Save()
		assert.NoError(t, err)

		// Verify .gitbook.yaml was created
		assert.True(t, fs.Exists(".gitbook.yaml"))
		
		yamlData, err := fs.ReadFile(".gitbook.yaml")
		require.NoError(t, err)
		assert.Contains(t, string(yamlData), "old-path.md: new-path.md")
	})
}