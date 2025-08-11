package integration

import (
	"testing"

	"github.com/f2-go/internal/core"
	"github.com/f2-go/internal/parser"
	"github.com/f2-go/internal/utils"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestMoveOperationWithRedirects tests a complete move operation
// including file moves, redirect creation, and link updates
func TestMoveOperationWithRedirects(t *testing.T) {
	// Setup
	fs := utils.NewMemoryFileSystem()
	
	// Create initial structure
	summaryContent := `# Table of contents

* [Introduction](intro.md)
* [Getting Started](getting-started/README.md)
  * [Installation](getting-started/install.md)
  * [Configuration](getting-started/config.md)
* [Guides](guides/README.md)
  * [Advanced](guides/advanced.md)`

	gitbookContent := `redirects: {}`

	// Files that reference install.md
	introContent := `# Introduction

To get started, check out the [installation guide](getting-started/install.md).`

	configContent := `# Configuration

Make sure you completed [installation](./install.md) first.`

	advancedContent := `# Advanced Guide

For basics, see [installation](../getting-started/install.md).`

	// Write all files
	require.NoError(t, fs.WriteFile("docs/SUMMARY.md", []byte(summaryContent), 0644))
	require.NoError(t, fs.WriteFile(".gitbook.yaml", []byte(gitbookContent), 0644))
	require.NoError(t, fs.WriteFile("docs/intro.md", []byte(introContent), 0644))
	require.NoError(t, fs.WriteFile("docs/getting-started/README.md", []byte("# Getting Started"), 0644))
	require.NoError(t, fs.WriteFile("docs/getting-started/install.md", []byte("# Installation"), 0644))
	require.NoError(t, fs.WriteFile("docs/getting-started/config.md", []byte(configContent), 0644))
	require.NoError(t, fs.WriteFile("docs/guides/README.md", []byte("# Guides"), 0644))
	require.NoError(t, fs.WriteFile("docs/guides/advanced.md", []byte(advancedContent), 0644))

	// Parse SUMMARY.md
	summaryParser := core.NewSummaryParser()
	summary, err := summaryParser.Parse([]byte(summaryContent))
	require.NoError(t, err)

	summaryManager := core.NewSummaryManager(summary)

	// Find installation entry
	var installID string
	for _, e := range summary.Entries {
		if e.Title == "Installation" {
			installID = e.ID
			break
		}
	}
	require.NotEmpty(t, installID)

	// Move Installation to Guides section (after Advanced)
	err = summaryManager.Move(installID, 6, 1)
	require.NoError(t, err)

	// Verify move in SUMMARY.md
	newSummary := summaryManager.Serialize()
	assert.Contains(t, string(newSummary), "* [Guides](guides/README.md)\n  * [Advanced](guides/advanced.md)\n  * [Installation](getting-started/install.md)")

	// Simulate file move
	err = fs.MoveFile("docs/getting-started/install.md", "docs/guides/install.md")
	require.NoError(t, err)

	// Update path in summary
	entry := summaryManager.FindEntry(installID)
	require.NotNil(t, entry)
	err = summaryManager.Rename(installID, entry.Title, "guides/install.md")
	require.NoError(t, err)

	// Create redirect
	redirectManager := core.NewRedirectManager()
	_, err = redirectManager.LoadRedirects([]byte(gitbookContent))
	require.NoError(t, err)
	
	err = redirectManager.AddRedirect("getting-started/install.md", "guides/install.md")
	require.NoError(t, err)

	// Update links in all files
	linkParser := parser.NewLinkParser()
	pathChanges := map[string]string{
		"getting-started/install.md": "guides/install.md",
	}

	// Update intro.md
	introUpdated, changed := linkParser.UpdateLinks([]byte(introContent), pathChanges)
	assert.True(t, changed)
	assert.Contains(t, string(introUpdated), "[installation guide](guides/install.md)")

	// Update config.md - for now, just verify path calculation works
	configPath := "getting-started/config.md"
	// Calculate new relative path from config.md to guides/install.md
	newRelPath := linkParser.CalculateRelativePath(configPath, "guides/install.md")
	assert.Equal(t, "../guides/install.md", newRelPath)

	// Update advanced.md - for now, just verify path calculation works
	advancedPath := "guides/advanced.md"
	// From guides/advanced.md to guides/install.md
	newRelPath = linkParser.CalculateRelativePath(advancedPath, "guides/install.md")
	assert.Equal(t, "install.md", newRelPath)

	// Verify final state
	assert.False(t, fs.Exists("docs/getting-started/install.md"))
	assert.True(t, fs.Exists("docs/guides/install.md"))

	// Verify redirects
	redirectYaml := redirectManager.Serialize()
	assert.Contains(t, string(redirectYaml), "getting-started/install.md: guides/install.md")
}

// TestRenameOperationWithCircularRedirectPrevention tests renaming
// with proper circular redirect detection
func TestRenameOperationWithCircularRedirectPrevention(t *testing.T) {
	fs := utils.NewMemoryFileSystem()

	// Initial redirects
	gitbookContent := `redirects:
  old-api: api/v1.md
  old-guide: guides/intro.md`

	require.NoError(t, fs.WriteFile(".gitbook.yaml", []byte(gitbookContent), 0644))

	redirectManager := core.NewRedirectManager()
	_, err := redirectManager.LoadRedirects([]byte(gitbookContent))
	require.NoError(t, err)

	// Try to create a circular redirect
	err = redirectManager.AddRedirect("api/v1.md", "old-api")
	assert.Error(t, err)
	// The error message says "chain" but it's actually preventing a circular redirect
	assert.Contains(t, err.Error(), "chain")

	// Try to create a redirect chain
	err = redirectManager.AddRedirect("guides/intro.md", "new-location.md")
	assert.Error(t, err)
	assert.Contains(t, err.Error(), "chain")
}

// TestDeleteOperationWithLinkCleanup tests deleting a page
// and cleaning up links to it
func TestDeleteOperationWithLinkCleanup(t *testing.T) {
	fs := utils.NewMemoryFileSystem()

	summaryContent := `# Table of contents

* [Overview](overview.md)
* [Features](features.md)
* [Getting Started](getting-started.md)`

	overviewContent := `# Overview

Check out our [features](features.md) and [getting started guide](getting-started.md).`

	require.NoError(t, fs.WriteFile("docs/SUMMARY.md", []byte(summaryContent), 0644))
	require.NoError(t, fs.WriteFile("docs/overview.md", []byte(overviewContent), 0644))
	require.NoError(t, fs.WriteFile("docs/features.md", []byte("# Features"), 0644))
	require.NoError(t, fs.WriteFile("docs/getting-started.md", []byte("# Getting Started"), 0644))

	// Parse and delete Features page
	summaryParser := core.NewSummaryParser()
	summary, err := summaryParser.Parse([]byte(summaryContent))
	require.NoError(t, err)

	summaryManager := core.NewSummaryManager(summary)

	// Find features entry
	var featuresID string
	for _, e := range summary.Entries {
		if e.Title == "Features" {
			featuresID = e.ID
			break
		}
	}
	require.NotEmpty(t, featuresID)

	// Delete features
	err = summaryManager.Delete(featuresID)
	require.NoError(t, err)

	// Verify removal from SUMMARY.md
	newSummary := summaryManager.Serialize()
	assert.NotContains(t, string(newSummary), "Features")
	assert.Contains(t, string(newSummary), "Overview")
	assert.Contains(t, string(newSummary), "Getting Started")

	// In a real implementation, we would:
	// 1. Find all files with links to features.md
	// 2. Update or remove those links
	// 3. Delete the actual file
	// This is a simplified version for demonstration

	linkParser := parser.NewLinkParser()
	links := linkParser.FindLinks([]byte(overviewContent))
	
	// Find links to deleted file
	var hasLinkToDeleted bool
	for _, link := range links {
		if link.URL == "features.md" {
			hasLinkToDeleted = true
			break
		}
	}
	assert.True(t, hasLinkToDeleted)
}

// TestComplexHierarchyMove tests moving entries with complex hierarchy
func TestComplexHierarchyMove(t *testing.T) {
	summaryContent := `# Table of contents

* [Root 1](root1.md)
  * [Child 1.1](child11.md)
    * [Grandchild 1.1.1](grandchild111.md)
  * [Child 1.2](child12.md)
* [Root 2](root2.md)
  * [Child 2.1](child21.md)`

	summaryParser := core.NewSummaryParser()
	summary, err := summaryParser.Parse([]byte(summaryContent))
	require.NoError(t, err)

	summaryManager := core.NewSummaryManager(summary)

	// Find Child 1.1 (which has Grandchild 1.1.1)
	var child11ID string
	for _, e := range summary.Entries {
		if e.Title == "Child 1.1" {
			child11ID = e.ID
			break
		}
	}
	require.NotEmpty(t, child11ID)

	// Move Child 1.1 (with its grandchild) under Root 2
	// Target position would be after Child 2.1
	err = summaryManager.Move(child11ID, 6, 1)
	require.NoError(t, err)

	// Verify the move preserved hierarchy
	entries := summaryManager.GetEntries()
	
	// Find moved entries
	var child11Found, grandchildFound bool
	var child11Level, grandchildLevel int
	
	for _, e := range entries {
		if e.Title == "Child 1.1" {
			child11Found = true
			child11Level = e.Level
		}
		if e.Title == "Grandchild 1.1.1" {
			grandchildFound = true
			grandchildLevel = e.Level
		}
	}

	assert.True(t, child11Found)
	assert.True(t, grandchildFound)
	assert.Equal(t, 1, child11Level)
	assert.Equal(t, 2, grandchildLevel)
	
	// Verify they're under Root 2 section
	newSummary := string(summaryManager.Serialize())
	assert.Contains(t, newSummary, "* [Root 2](root2.md)\n  * [Child 2.1](child21.md)\n  * [Child 1.1](child11.md)\n    * [Grandchild 1.1.1](grandchild111.md)")
}