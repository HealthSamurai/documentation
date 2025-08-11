package main

import (
	"bytes"
	"strings"
	"testing"

	"github.com/f2-go/internal/core"
	"github.com/stretchr/testify/assert"
)

// TestMoveCommandIntegration tests the move command with arrow navigation
func TestMoveCommandIntegration(t *testing.T) {
	tests := []struct {
		name          string
		srcPath       string
		dstPath       string
		entries       []core.Entry
		userInput     string // simulated position selection
		expectSuccess bool
		expectOutput  []string
		description   string
	}{
		{
			name:    "Move within overview section - select from filtered positions",
			srcPath: "overview/licensing.md",  // Use an existing entry
			dstPath: "overview/new-location.md",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "2", Title: "Licensing and Support", Path: "overview/licensing.md", Level: 1},
				{ID: "3", Title: "Aidbox User Portal", Path: "overview/portal.md", Level: 1},
				{ID: "4", Title: "Projects", Path: "overview/projects.md", Level: 2}, // Should be excluded
				{ID: "5", Title: "Aidbox UI", Path: "overview/ui.md", Level: 1},
				{ID: "6", Title: "Versioning", Path: "overview/versioning.md", Level: 1},
				{ID: "7", Title: "Configuration", Path: "configuration/index.md", Level: 0},
			},
			userInput:     "3\n", // Select position after "Aidbox User Portal"
			expectSuccess: true,
			expectOutput: []string{
				"Select position for insertion in 'overview' section:",
				"Overview",
				"Licensing and Support",
				"Aidbox User Portal",
				"Aidbox UI",
				"Versioning",
				"At the end of section",
				"✓ Move completed successfully",
			},
			description: "Should show only level 0 and level 1 entries from overview section",
		},
		{
			name:    "Move to root level - only shows top-level sections",
			srcPath: "overview/licensing.md",
			dstPath: "readme.md",
			entries: []core.Entry{
				{ID: "1", Title: "Getting Started", Path: "getting-started/index.md", Level: 0},
				{ID: "2", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "3", Title: "Licensing", Path: "overview/licensing.md", Level: 1}, // Should be excluded
				{ID: "4", Title: "Configuration", Path: "configuration/index.md", Level: 0},
				{ID: "5", Title: "API", Path: "api/index.md", Level: 0},
			},
			userInput:     "2\n", // Select after Overview
			expectSuccess: true,
			expectOutput: []string{
				"Select position for insertion:",
				"Getting Started",
				"Overview",
				"Configuration",
				"API",
				"At the end of document",
			},
			description: "Should show only level 0 entries when moving to root",
		},
		{
			name:    "Move to non-existent section",
			srcPath: "overview/test.md",
			dstPath: "nonexistent/test.md",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "2", Title: "Test", Path: "overview/test.md", Level: 1},
			},
			userInput:     "",
			expectSuccess: false,
			expectOutput: []string{
				"No valid positions found for destination",
			},
			description: "Should fail gracefully when target section doesn't exist",
		},
		{
			name:    "Cancel selection",
			srcPath: "overview/test.md",
			dstPath: "overview/new.md",
			entries: []core.Entry{
				{ID: "1", Title: "Overview", Path: "overview/index.md", Level: 0},
				{ID: "2", Title: "Test", Path: "overview/test.md", Level: 1},
			},
			userInput:     "q\n", // Cancel selection
			expectSuccess: false,
			expectOutput: []string{
				"invalid position", // selectPositionByNumber returns this for non-numeric input
			},
			description: "Should handle cancelled selection",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Setup mock filesystem
			fs := NewMockFileSystem()
			
			// Create SUMMARY.md with test entries
			summaryContent := generateSummaryContent(tt.entries)
			fs.files["docs/SUMMARY.md"] = []byte(summaryContent)
			
			// Create .gitbook.yaml
			fs.files[".gitbook.yaml"] = []byte("redirects:\n")
			
			// Create source file
			srcFullPath := "docs/" + tt.srcPath
			fs.files[srcFullPath] = []byte("# Test File\n\nContent")
			
			// Setup CLI with mock stdin/stdout
			stdin := strings.NewReader(tt.userInput)
			stdout := &bytes.Buffer{}
			stderr := &bytes.Buffer{}
			
			cli := NewTestCLI(fs, stdin, stdout, stderr)
			
			// Run the move command
			err := cli.runMove(tt.srcPath, tt.dstPath)
			
			// Check success/failure
			if tt.expectSuccess {
				assert.NoError(t, err, tt.description)
			} else {
				assert.Error(t, err, tt.description)
			}
			
			// Check output contains expected strings
			output := stdout.String()
			// For error cases, also check stderr
			if !tt.expectSuccess && err != nil {
				output = output + err.Error()
			}
			for _, expected := range tt.expectOutput {
				assert.Contains(t, output, expected, 
					"Expected output to contain '%s'\nDescription: %s", 
					expected, tt.description)
			}
			
			// If successful, verify the positions shown were correctly filtered
			if tt.expectSuccess && strings.Contains(tt.dstPath, "/") {
				// Extract section from path
				targetSection := strings.Split(tt.dstPath, "/")[0]
				
				// Get filtered positions
				positions := filterSameLevelEntries(tt.entries, tt.dstPath)
				
				// Verify no deep-level entries are included
				for _, pos := range positions {
					if !pos.IsEndOption {
						// Find the entry
						for _, entry := range tt.entries {
							if entry.Title == pos.Label {
								// For overview section, should only have level 0 or 1
								if targetSection == "overview" {
									assert.True(t, entry.Level <= 1,
										"Entry '%s' with level %d should not be included for section '%s'",
										entry.Title, entry.Level, targetSection)
								}
								break
							}
						}
					}
				}
			}
		})
	}
}

// TestFilterSameLevelEntriesEdgeCases tests edge cases for the filtering function
func TestFilterSameLevelEntriesEdgeCases(t *testing.T) {
	tests := []struct {
		name          string
		entries       []core.Entry
		targetPath    string
		expectCount   int
		expectLabels  []string
	}{
		{
			name: "Section with only deep nested items",
			entries: []core.Entry{
				{ID: "1", Title: "API", Path: "api/index.md", Level: 0},
				{ID: "2", Title: "Deep1", Path: "api/deep1.md", Level: 2},
				{ID: "3", Title: "Deep2", Path: "api/deep2.md", Level: 3},
				{ID: "4", Title: "Deep3", Path: "api/deep3.md", Level: 2},
			},
			targetPath:   "api/new.md",
			expectCount:  2, // API header + "At the end of section"
			expectLabels: []string{"API", "At the end of section"},
		},
		{
			name: "Multiple sections with same prefix",
			entries: []core.Entry{
				{ID: "1", Title: "Auth", Path: "auth/index.md", Level: 0},
				{ID: "2", Title: "Login", Path: "auth/login.md", Level: 1},
				{ID: "3", Title: "Auth API", Path: "auth-api/index.md", Level: 0},
				{ID: "4", Title: "OAuth", Path: "auth-api/oauth.md", Level: 1},
			},
			targetPath:   "auth/new.md",
			expectCount:  3, // Auth + Login + "At the end"
			expectLabels: []string{"Auth", "Login", "At the end of section"},
		},
		{
			name: "Section without explicit header",
			entries: []core.Entry{
				{ID: "1", Title: "First", Path: "section/first.md", Level: 1},
				{ID: "2", Title: "Second", Path: "section/second.md", Level: 1},
				{ID: "3", Title: "Nested", Path: "section/nested.md", Level: 2},
				{ID: "4", Title: "Third", Path: "section/third.md", Level: 1},
			},
			targetPath:   "section/new.md",
			expectCount:  4, // All level 1 entries + "At the end"
			expectLabels: []string{"First", "Second", "Third", "At the end of section"},
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			positions := filterSameLevelEntries(tt.entries, tt.targetPath)
			
			assert.Equal(t, tt.expectCount, len(positions),
				"Expected %d positions, got %d", tt.expectCount, len(positions))
			
			for i, expectedLabel := range tt.expectLabels {
				if i < len(positions) {
					assert.Equal(t, expectedLabel, positions[i].Label,
						"Position %d: expected '%s', got '%s'",
						i, expectedLabel, positions[i].Label)
				} else {
					t.Errorf("Missing position %d with label '%s'", i, expectedLabel)
				}
			}
		})
	}
}

// Helper function to generate SUMMARY.md content from entries
func generateSummaryContent(entries []core.Entry) string {
	var lines []string
	lines = append(lines, "# Table of contents\n")
	
	for _, entry := range entries {
		indent := strings.Repeat("  ", entry.Level)
		lines = append(lines, indent+"* ["+entry.Title+"]("+entry.Path+")")
	}
	
	return strings.Join(lines, "\n")
}