package main

import (
	"testing"
	"github.com/f2-go/internal/core"
	"github.com/stretchr/testify/assert"
)

func TestFilterSameLevelEntries(t *testing.T) {
	tests := []struct {
		name           string
		entries        []core.Entry
		targetPath     string
		expectedCount  int
		expectedLabels []string
		description    string
	}{
		{
			name: "Filter overview section - same level entries",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "2", Title: "Licensing and Support", Path: "overview/licensing.md", Level: 1},
				{ID: "3", Title: "Aidbox User Portal", Path: "overview/portal.md", Level: 1},
				{ID: "4", Title: "Projects", Path: "overview/projects.md", Level: 2}, // Should be excluded (level 2)
				{ID: "5", Title: "Licenses", Path: "overview/licenses.md", Level: 2}, // Should be excluded (level 2)
				{ID: "6", Title: "Aidbox UI", Path: "overview/ui.md", Level: 1},
				{ID: "7", Title: "Versioning", Path: "overview/versioning.md", Level: 1},
				{ID: "8", Title: "Configuration", Path: "configuration/index.md", Level: 0},
			},
			targetPath:    "overview/architecture.md",
			expectedCount: 6, // Overview + 4 level-1 items + "At the end of section"
			expectedLabels: []string{
				"Overview",
				"Licensing and Support", 
				"Aidbox User Portal",
				"Aidbox UI",
				"Versioning",
				"At the end of section",
			},
			description: "Should filter to show only level 0 and level 1 entries in overview section",
		},
		{
			name: "Root level entries only",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "2", Title: "Configuration", Path: "configuration/index.md", Level: 0},
				{ID: "3", Title: "API", Path: "api/index.md", Level: 0},
				{ID: "4", Title: "Sub Item", Path: "overview/sub.md", Level: 1}, // Should be excluded
			},
			targetPath:    "readme.md",
			expectedCount: 4, // 3 root entries + "At the end of document"
			expectedLabels: []string{
				"Overview",
				"Configuration", 
				"API",
				"At the end of document",
			},
			description: "Should show only root level (0) entries when target is at root",
		},
		{
			name: "Non-existent section returns empty",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "2", Title: "Configuration", Path: "configuration/index.md", Level: 0},
			},
			targetPath:     "nonexistent/file.md",
			expectedCount:  0,
			expectedLabels: []string{},
			description:    "Should return empty list for non-existent section",
		},
		{
			name: "Complex nested structure",
			entries: []core.Entry{
				{ID: "1", Title: "API", Path: "api/index.md", Level: 0},
				{ID: "2", Title: "REST", Path: "api/rest.md", Level: 1},
				{ID: "3", Title: "Authentication", Path: "api/auth.md", Level: 2},
				{ID: "4", Title: "OAuth", Path: "api/oauth.md", Level: 3}, 
				{ID: "5", Title: "GraphQL", Path: "api/graphql.md", Level: 1},
				{ID: "6", Title: "Webhooks", Path: "api/webhooks.md", Level: 1},
			},
			targetPath:    "api/new-endpoint.md",
			expectedCount: 5, // API + REST + GraphQL + Webhooks + "At the end"
			expectedLabels: []string{
				"API",
				"REST",
				"GraphQL",
				"Webhooks",
				"At the end of section",
			},
			description: "Should handle complex nested structure and filter correctly",
		},
		{
			name: "Empty entries list",
			entries: []core.Entry{},
			targetPath:    "overview/test.md",
			expectedCount: 0,
			expectedLabels: []string{},
			description: "Should handle empty entries gracefully",
		},
		{
			name: "Single entry section",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
			},
			targetPath:    "overview/test.md",
			expectedCount: 2, // Overview + "At the end of section"
			expectedLabels: []string{
				"Overview",
				"At the end of section",
			},
			description: "Should handle single entry section",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			positions := filterSameLevelEntries(tt.entries, tt.targetPath)
			
			// Check count
			assert.Equal(t, tt.expectedCount, len(positions), 
				"Test: %s\nDescription: %s\nExpected %d positions, got %d", 
				tt.name, tt.description, tt.expectedCount, len(positions))
			
			// Check labels match
			for i, expectedLabel := range tt.expectedLabels {
				if i >= len(positions) {
					t.Errorf("Missing position %d with label '%s'", i, expectedLabel)
					continue
				}
				assert.Equal(t, expectedLabel, positions[i].Label,
					"Position %d: expected label '%s', got '%s'", 
					i, expectedLabel, positions[i].Label)
			}
			
			// Additional checks for specific properties
			if len(positions) > 0 {
				// Check that last item is "end of section/document" option if not empty
				if tt.expectedCount > 0 {
					lastPos := positions[len(positions)-1]
					assert.True(t, lastPos.IsEndOption, 
						"Last position should be an end option")
				}
				
				// Check that levels are set correctly
				for _, pos := range positions {
					if !pos.IsEndOption {
						// Find corresponding entry and verify level
						for _, entry := range tt.entries {
							if entry.Title == pos.Label {
								assert.Equal(t, entry.Level, pos.Level,
									"Level mismatch for '%s': expected %d, got %d",
									pos.Label, entry.Level, pos.Level)
								break
							}
						}
					}
				}
			}
		})
	}
}

func TestFilterSameLevelEntriesIndexMapping(t *testing.T) {
	// Test that the Index field correctly maps to the original entries array
	entries := []core.Entry{
		{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
		{ID: "2", Title: "Licensing", Path: "overview/licensing.md", Level: 1},
		{ID: "3", Title: "Portal", Path: "overview/portal.md", Level: 1},
		{ID: "4", Title: "Config", Path: "config/index.md", Level: 0},
	}
	
	positions := filterSameLevelEntries(entries, "overview/new.md")
	
	// Verify that Index points to correct entries
	for _, pos := range positions {
		if !pos.IsEndOption {
			assert.True(t, pos.Index >= 0 && pos.Index < len(entries),
				"Index %d should be within entries bounds", pos.Index)
			
			// For non-end options, the label should match the entry title
			if pos.Index < len(entries) {
				expectedTitle := entries[pos.Index].Title
				assert.Equal(t, expectedTitle, pos.Label,
					"Position label should match entry title at index %d", pos.Index)
			}
		}
	}
	
	// Check that the "end of section" index is correct
	lastPos := positions[len(positions)-1]
	assert.True(t, lastPos.IsEndOption, "Last position should be end option")
	// The index for "end of section" should be after the last entry in that section
	assert.True(t, lastPos.Index <= len(entries), 
		"End of section index should be valid")
}

func TestFilterSameLevelEntriesRealWorldScenario(t *testing.T) {
	// Simulate the actual documentation structure from the user's example
	entries := []core.Entry{
		// Overview section
		{ID: "100", Title: "Overview", Path: "overview/index.md", Level: 0},
		{ID: "101", Title: "Licensing and Support", Path: "overview/licensing.md", Level: 1},
		{ID: "102", Title: "Aidbox User Portal", Path: "overview/portal.md", Level: 1},
		{ID: "103", Title: "Projects", Path: "overview/projects.md", Level: 2},
		{ID: "104", Title: "Licenses", Path: "overview/licenses.md", Level: 2},
		{ID: "105", Title: "Members", Path: "overview/members.md", Level: 2},
		{ID: "106", Title: "Aidbox UI", Path: "overview/ui.md", Level: 1},
		{ID: "107", Title: "Aidbox Notebooks", Path: "overview/notebooks.md", Level: 2},
		{ID: "108", Title: "REST Console", Path: "overview/rest-console.md", Level: 2},
		{ID: "109", Title: "Database Console", Path: "overview/db-console.md", Level: 2},
		{ID: "110", Title: "Attrs Stats", Path: "overview/attrs.md", Level: 2},
		{ID: "111", Title: "DB Tables", Path: "overview/tables.md", Level: 2},
		{ID: "112", Title: "DB Queries", Path: "overview/queries.md", Level: 2},
		{ID: "113", Title: "Versioning", Path: "overview/versioning.md", Level: 1},
		{ID: "114", Title: "Release Notes", Path: "overview/release.md", Level: 1},
		{ID: "115", Title: "FAQ", Path: "overview/faq.md", Level: 1},
		{ID: "116", Title: "Contact Us", Path: "overview/contact.md", Level: 1},
		// Configuration section
		{ID: "117", Title: "Configuration", Path: "configuration/index.md", Level: 0},
	}
	
	positions := filterSameLevelEntries(entries, "overview/architecture.md")
	
	// Should only show level 0 (Overview) and level 1 items within overview section
	expectedTitles := []string{
		"Overview",
		"Licensing and Support",
		"Aidbox User Portal", 
		"Aidbox UI",
		"Versioning",
		"Release Notes",
		"FAQ",
		"Contact Us",
		"At the end of section",
	}
	
	assert.Equal(t, len(expectedTitles), len(positions),
		"Should have exactly %d positions", len(expectedTitles))
	
	for i, expectedTitle := range expectedTitles {
		assert.Equal(t, expectedTitle, positions[i].Label,
			"Position %d should be '%s'", i, expectedTitle)
	}
	
	// Verify no level 2 items are included
	for _, pos := range positions {
		if !pos.IsEndOption {
			assert.True(t, pos.Level <= 1,
				"Should not include items deeper than level 1, got level %d for '%s'",
				pos.Level, pos.Label)
		}
	}
}