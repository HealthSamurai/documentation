package main

import (
	"bufio"
	"fmt"
	"strconv"
	"strings"

	"github.com/f2-go/internal/core"
)

// runMoveImproved handles the move command with smart position selection
func (c *CLI) runMoveImproved(srcPath, dstPath string) error {
	// Initialize managers
	summary, summaryManager, redirectManager, linkUpdater, err := c.initializeManagers()
	if err != nil {
		return err
	}

	// Find source entry
	var srcEntry *core.Entry
	for _, entry := range summary.Entries {
		if entry.Path == srcPath {
			srcEntry = &entry
			break
		}
	}

	if srcEntry == nil {
		return fmt.Errorf("File '%s' not found in SUMMARY.md", srcPath)
	}

	// Extract target section from destination path
	targetSection := ""
	if idx := strings.Index(dstPath, "/"); idx > 0 {
		targetSection = dstPath[:idx]
	}

	// Filter entries based on target section
	entries := summaryManager.GetEntries()
	var filteredEntries []core.Entry
	var sectionStart, sectionEnd int = -1, -1
	
	if targetSection != "" {
		// Find the section boundaries
		for i, entry := range entries {
			// Check if this is a section header or belongs to the target section
			if strings.HasPrefix(entry.Path, targetSection+"/") || 
			   strings.HasPrefix(entry.Path, targetSection+".md") ||
			   (entry.Path == targetSection && strings.HasSuffix(entry.Path, "/README.md")) {
				if sectionStart == -1 {
					// Find the section header (level 0 or 1 entry before this)
					sectionStart = i
					for j := i - 1; j >= 0; j-- {
						if entries[j].Level < entry.Level {
							sectionStart = j
							break
						}
					}
				}
				sectionEnd = i + 1
			} else if sectionStart != -1 && entry.Level <= entries[sectionStart].Level {
				// We've reached the next section
				break
			}
		}

		// If section found, show only relevant entries
		if sectionStart != -1 {
			// Include some context before and after
			contextBefore := 2
			contextAfter := 2
			
			start := sectionStart - contextBefore
			if start < 0 {
				start = 0
			}
			
			end := sectionEnd + contextAfter
			if end > len(entries) {
				end = len(entries)
			}
			
			filteredEntries = entries[start:end]
			
			// Show filtered entries
			fmt.Fprintf(c.stdout, "Select position for insertion (showing %s section):\n", targetSection)
			fmt.Fprintln(c.stdout, "----------------------------------------")
			
			for i, entry := range filteredEntries {
				indent := strings.Repeat("  ", entry.Level)
				globalIndex := start + i + 1
				
				// Highlight section boundaries
				prefix := ""
				if entry.Path == targetSection || strings.HasPrefix(entry.Path, targetSection+"/") {
					prefix = "→ "
				}
				
				fmt.Fprintf(c.stdout, "%d. %s%s%s\n", globalIndex, prefix, indent, entry.Title)
			}
			
			fmt.Fprintln(c.stdout, "----------------------------------------")
			fmt.Fprintf(c.stdout, "Or enter %d to insert at the end of this section\n", sectionEnd+1)
			fmt.Fprintf(c.stdout, "Or enter 0 to see all entries\n")
		} else {
			// Section not found, suggest creating it
			fmt.Fprintf(c.stdout, "Section '%s' not found. Options:\n", targetSection)
			fmt.Fprintln(c.stdout, "1. Create new section")
			fmt.Fprintln(c.stdout, "2. Show all entries")
			fmt.Fprint(c.stdout, "Enter choice (1 or 2): ")
			
			scanner := bufio.NewScanner(c.stdin)
			if scanner.Scan() {
				choice := scanner.Text()
				if choice == "1" {
					// Find appropriate position for new section
					// This would need additional logic to determine where to insert
					return c.createNewSectionAndMove(srcEntry, dstPath, targetSection, summaryManager, redirectManager, linkUpdater)
				}
				// Fall through to show all entries
			}
			filteredEntries = entries
		}
	} else {
		// No specific section, but still limit display
		fmt.Fprintln(c.stdout, "Select position for insertion:")
		filteredEntries = entries
	}

	// If we're showing all entries but there are too many, paginate
	if len(filteredEntries) > 50 {
		return c.showPaginatedEntries(srcEntry, dstPath, filteredEntries, summaryManager, redirectManager, linkUpdater)
	}

	// Show all filtered entries if not already shown
	if targetSection == "" || sectionStart == -1 {
		for i, entry := range filteredEntries {
			indent := strings.Repeat("  ", entry.Level)
			fmt.Fprintf(c.stdout, "%d. %s%s\n", i+1, indent, entry.Title)
		}
		fmt.Fprintf(c.stdout, "%d. At the end\n", len(filteredEntries)+1)
	}

	// Get user selection
	var position int
	fmt.Fprint(c.stdout, "Enter position number: ")
	scanner := bufio.NewScanner(c.stdin)
	if scanner.Scan() {
		input := scanner.Text()
		if input == "0" && targetSection != "" {
			// User wants to see all entries
			return c.runMove(srcPath, dstPath)
		}
		
		position, err = strconv.Atoi(input)
		if err != nil || position < 1 || position > len(entries)+1 {
			return fmt.Errorf("Invalid position")
		}
	} else {
		return fmt.Errorf("No position selected")
	}

	// Continue with the rest of the move logic...
	// (This would include the actual move operation, file operations, redirects, etc.)
	
	return nil
}

// Helper function to show paginated entries
func (c *CLI) showPaginatedEntries(srcEntry *core.Entry, dstPath string, entries []core.Entry, 
	summaryManager core.SummaryManager, redirectManager core.RedirectManager, linkUpdater core.LinkUpdater) error {
	
	pageSize := 30
	currentPage := 0
	totalPages := (len(entries) + pageSize - 1) / pageSize
	
	for {
		start := currentPage * pageSize
		end := start + pageSize
		if end > len(entries) {
			end = len(entries)
		}
		
		fmt.Fprintf(c.stdout, "\n=== Page %d of %d ===\n", currentPage+1, totalPages)
		
		for i := start; i < end; i++ {
			entry := entries[i]
			indent := strings.Repeat("  ", entry.Level)
			fmt.Fprintf(c.stdout, "%d. %s%s\n", i+1, indent, entry.Title)
		}
		
		fmt.Fprintln(c.stdout, "\nOptions:")
		fmt.Fprintln(c.stdout, "- Enter a number to select position")
		if currentPage > 0 {
			fmt.Fprintln(c.stdout, "- Enter 'p' for previous page")
		}
		if currentPage < totalPages-1 {
			fmt.Fprintln(c.stdout, "- Enter 'n' for next page")
		}
		fmt.Fprintln(c.stdout, "- Enter 'q' to quit")
		fmt.Fprint(c.stdout, "Choice: ")
		
		scanner := bufio.NewScanner(c.stdin)
		if !scanner.Scan() {
			return fmt.Errorf("No input provided")
		}
		
		input := strings.TrimSpace(scanner.Text())
		
		switch input {
		case "n":
			if currentPage < totalPages-1 {
				currentPage++
			}
		case "p":
			if currentPage > 0 {
				currentPage--
			}
		case "q":
			return fmt.Errorf("Move operation cancelled")
		default:
			position, err := strconv.Atoi(input)
			if err == nil && position >= 1 && position <= len(entries) {
				// Valid position selected, continue with move
				// This would need to be integrated with the actual move logic
				return nil
			}
			fmt.Fprintln(c.stderr, "Invalid input. Please try again.")
		}
	}
}

// Helper function to create new section and move entry
func (c *CLI) createNewSectionAndMove(srcEntry *core.Entry, dstPath, sectionName string, 
	summaryManager core.SummaryManager, redirectManager core.RedirectManager, linkUpdater core.LinkUpdater) error {
	
	fmt.Fprintf(c.stdout, "Creating new section '%s' and moving entry...\n", sectionName)
	
	// Find appropriate position for new section (e.g., after "Getting Started", before "Tutorials")
	// This is a simplified version - real implementation would be more sophisticated
	
	entries := summaryManager.GetEntries()
	insertPosition := -1
	
	// Look for common section ordering
	sectionOrder := []string{"getting-started", "tutorials", "overview", "configuration", "api", "modules"}
	targetIndex := -1
	for i, sec := range sectionOrder {
		if sec == sectionName {
			targetIndex = i
			break
		}
	}
	
	if targetIndex != -1 {
		// Find where to insert based on section ordering
		for i, entry := range entries {
			if entry.Level == 0 { // Top-level section
				entrySection := strings.Split(entry.Path, "/")[0]
				for j, sec := range sectionOrder {
					if sec == entrySection && j > targetIndex {
						insertPosition = i
						break
					}
				}
				if insertPosition != -1 {
					break
				}
			}
		}
	}
	
	if insertPosition == -1 {
		// Default: insert before deprecated section or at the end
		for i, entry := range entries {
			if strings.Contains(strings.ToLower(entry.Title), "deprecated") {
				insertPosition = i
				break
			}
		}
		if insertPosition == -1 {
			insertPosition = len(entries)
		}
	}
	
	// Create section header and move entry
	// This would need actual implementation
	fmt.Fprintf(c.stdout, "Would insert new section at position %d\n", insertPosition)
	
	return nil
}