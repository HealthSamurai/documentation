package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"regexp"
	"strconv"
	"strings"

	"github.com/f2-go/internal/core"
	"github.com/f2-go/internal/utils"
)

// InsertPosition represents a possible position for insertion
type InsertPosition struct {
	Index       int
	Label       string
	Level       int
	IsSection   bool
	IsEndOption bool
}

// filterSameLevelEntries filters entries to show only same-level items as the target section
func filterSameLevelEntries(entries []core.Entry, targetPath string) []InsertPosition {
	// Extract target section from path
	targetSection := ""
	if idx := strings.Index(targetPath, "/"); idx > 0 {
		targetSection = targetPath[:idx]
	}
	
	if targetSection == "" {
		// Root level - show only top-level sections
		var positions []InsertPosition
		for i, entry := range entries {
			if entry.Level == 0 {
				positions = append(positions, InsertPosition{
					Index:     i,
					Label:     entry.Title,
					Level:     entry.Level,
					IsSection: true,
				})
			}
		}
		// Add "at the end" option
		positions = append(positions, InsertPosition{
			Index:       len(entries),
			Label:       "At the end of document",
			Level:       0,
			IsEndOption: true,
		})
		return positions
	}
	
	// Find the target section and its level
	var sectionLevel int  // Level of the section header (e.g., Overview)
	var childLevel int    // Level of direct children in the section
	var sectionStart, sectionEnd int
	found := false
	sectionHeaderIndex := -1
	
	// First, find the section header
	for i, entry := range entries {
		// Check if this is the section header (level 0 with matching path prefix)
		if strings.HasPrefix(entry.Path, targetSection+"/") && entry.Level == 0 {
			sectionHeaderIndex = i
			sectionLevel = entry.Level
			childLevel = sectionLevel + 1
			sectionStart = i
			found = true
			break
		}
	}
	
	// If no level 0 section header found, look for the first entry in the section
	if !found {
		for i, entry := range entries {
			entrySection := ""
			if idx := strings.Index(entry.Path, "/"); idx > 0 {
				entrySection = entry.Path[:idx]
			}
			
			if entrySection == targetSection {
				if !found {
					// No explicit section header, use the first entry's level
					sectionLevel = entry.Level - 1
					if sectionLevel < 0 {
						sectionLevel = 0
					}
					childLevel = entry.Level
					sectionStart = i
					found = true
				}
			}
		}
	}
	
	if !found {
		// Section not found, return empty
		return []InsertPosition{}
	}
	
	// Find the end of the section
	for i := sectionStart; i < len(entries); i++ {
		entry := entries[i]
		entrySection := ""
		if idx := strings.Index(entry.Path, "/"); idx > 0 {
			entrySection = entry.Path[:idx]
		}
		
		// Still in the target section or its children
		if entrySection == targetSection || (sectionHeaderIndex >= 0 && i == sectionHeaderIndex) {
			sectionEnd = i + 1
		} else if entry.Level <= sectionLevel {
			// Reached next section at same or higher level
			break
		} else if entrySection != targetSection {
			// Different section at lower level
			break
		}
	}
	
	// Collect entries at section level and direct child level
	var positions []InsertPosition
	
	// Add section header if it exists
	if sectionHeaderIndex >= 0 {
		positions = append(positions, InsertPosition{
			Index:     sectionHeaderIndex,
			Label:     entries[sectionHeaderIndex].Title,
			Level:     entries[sectionHeaderIndex].Level,
			IsSection: true,
		})
	}
	
	// Add direct children (level 1 entries in the section)
	for i := sectionStart; i < sectionEnd; i++ {
		entry := entries[i]
		entrySection := ""
		if idx := strings.Index(entry.Path, "/"); idx > 0 {
			entrySection = entry.Path[:idx]
		}
		
		// Include entries that are direct children of the section
		if entrySection == targetSection && entry.Level == childLevel && i != sectionHeaderIndex {
			positions = append(positions, InsertPosition{
				Index:     i,
				Label:     entry.Title,
				Level:     entry.Level,
				IsSection: false,
			})
		}
	}
	
	// Add "at the end of section" option
	positions = append(positions, InsertPosition{
		Index:       sectionEnd,
		Label:       "At the end of section",
		Level:       childLevel,
		IsEndOption: true,
	})
	
	return positions
}

// selectPositionInteractive allows interactive selection with arrow keys
func (c *CLI) selectPositionInteractive(positions []InsertPosition, targetSection string) (int, error) {
	if len(positions) == 0 {
		return -1, fmt.Errorf("no positions available")
	}
	
	// Use bubbletea for interactive selection
	return RunPositionSelector(positions, targetSection)
}

// selectPositionByNumber fallback to number selection
func (c *CLI) selectPositionByNumber(positions []InsertPosition, targetSection string) (int, error) {
	// Display positions
	if targetSection != "" {
		fmt.Fprintf(c.stdout, "Select position for insertion in '%s' section:\n", targetSection)
	} else {
		fmt.Fprintln(c.stdout, "Select position for insertion:")
	}
	fmt.Fprintln(c.stdout, "----------------------------------------")
	
	for i, pos := range positions {
		if pos.IsEndOption {
			fmt.Fprintf(c.stdout, "%d. [%s]\n", i+1, pos.Label)
		} else {
			indent := strings.Repeat("  ", pos.Level)
			fmt.Fprintf(c.stdout, "%d. %s%s\n", i+1, indent, pos.Label)
		}
	}
	
	fmt.Fprint(c.stdout, "Enter position number: ")
	scanner := bufio.NewScanner(c.stdin)
	if !scanner.Scan() {
		return -1, fmt.Errorf("no position selected")
	}
	
	input := scanner.Text()
	num, err := strconv.Atoi(input)
	if err != nil || num < 1 || num > len(positions) {
		return -1, fmt.Errorf("invalid position number")
	}
	
	return positions[num-1].Index, nil
}

// filterEntriesForSection filters entries to show only relevant section
func filterEntriesForSection(entries []core.Entry, targetPath string) ([]core.Entry, int, int) {
	// Extract target section from path
	targetSection := ""
	if idx := strings.Index(targetPath, "/"); idx > 0 {
		targetSection = targetPath[:idx]
	}
	
	if targetSection == "" {
		return entries, 0, len(entries)
	}
	
	// Find section boundaries
	sectionStart := -1
	sectionEnd := -1
	inTargetSection := false
	
	for i, entry := range entries {
		// Check if entry belongs to target section
		entrySection := ""
		if idx := strings.Index(entry.Path, "/"); idx > 0 {
			entrySection = entry.Path[:idx]
		}
		
		if entrySection == targetSection {
			if sectionStart == -1 {
				sectionStart = i
				// Look backwards for section header
				for j := i - 1; j >= 0; j-- {
					if entries[j].Level < entry.Level {
						sectionStart = j
						break
					}
				}
			}
			inTargetSection = true
			sectionEnd = i + 1
		} else if inTargetSection && entry.Level <= entries[sectionStart].Level {
			// We've reached the next section
			break
		} else if inTargetSection {
			sectionEnd = i + 1
		}
	}
	
	// If section not found, return full list
	if sectionStart == -1 {
		return entries, 0, len(entries)
	}
	
	// Add some context
	contextBefore := 1
	contextAfter := 1
	
	start := sectionStart - contextBefore
	if start < 0 {
		start = 0
	}
	
	end := sectionEnd + contextAfter
	if end > len(entries) {
		end = len(entries)
	}
	
	return entries[start:end], start, sectionEnd
}

// SimpleLinkUpdater implements basic link updating functionality
type SimpleLinkUpdater struct {
	fs       core.FileSystem
	filePath string // Current file being processed
}

// UpdateLinks updates links in the content when files are moved
// Returns the updated content, whether any updates were made, and the count of updates
func (u *SimpleLinkUpdater) UpdateLinks(content []byte, pathChanges map[string]string) ([]byte, bool, int) {
	contentStr := string(content)
	updated := false
	updateCount := 0
	
	// Regular expression to find markdown links
	linkRegex := regexp.MustCompile(`\[([^\]]+)\]\(([^)]+)\)`)
	
	result := linkRegex.ReplaceAllStringFunc(contentStr, func(match string) string {
		submatches := linkRegex.FindStringSubmatch(match)
		if len(submatches) < 3 {
			return match
		}
		
		linkText := submatches[1]
		linkPath := submatches[2]
		
		// Skip absolute URLs and anchors
		if strings.HasPrefix(linkPath, "http://") || 
		   strings.HasPrefix(linkPath, "https://") || 
		   strings.HasPrefix(linkPath, "#") ||
		   strings.HasPrefix(linkPath, "/") {
			return match
		}
		
		// Handle anchors in the path
		anchor := ""
		if idx := strings.Index(linkPath, "#"); idx >= 0 {
			anchor = linkPath[idx:]
			linkPath = linkPath[:idx]
		}
		
		// Check if this link points to any of the moved files
		for oldPath, newPath := range pathChanges {
			// Check direct match
			if linkPath == oldPath {
				updated = true
				updateCount++
				return fmt.Sprintf("[%s](%s%s)", linkText, newPath, anchor)
			}
			
			// Check if it's a relative path to the moved file
			// Calculate what the link resolves to from current file's perspective
			if u.filePath != "" {
				currentDir := filepath.Dir(u.filePath)
				resolvedPath := filepath.Join(currentDir, linkPath)
				resolvedPath = filepath.Clean(resolvedPath)
				// Normalize to forward slashes
				resolvedPath = strings.ReplaceAll(resolvedPath, "\\", "/")
				
				// Check if resolved path matches the old path
				if strings.HasSuffix(resolvedPath, oldPath) || resolvedPath == oldPath {
					// Calculate relative path from current file to new location
					newResolvedPath := strings.Replace(resolvedPath, oldPath, newPath, 1)
					relPath, err := filepath.Rel(currentDir, newResolvedPath)
					if err == nil {
						relPath = strings.ReplaceAll(relPath, "\\", "/")
						updated = true
						updateCount++
						return fmt.Sprintf("[%s](%s%s)", linkText, relPath, anchor)
					}
				}
			}
		}
		
		return match
	})
	
	if updated {
		return []byte(result), true, updateCount
	}
	return content, false, 0
}

// CalculateRelativePath calculates relative path between two paths
func (u *SimpleLinkUpdater) CalculateRelativePath(from, to string) string {
	// Simple implementation - just return the target path
	// A real implementation would calculate the actual relative path
	return to
}

// FindLinks finds all links in the content
func (u *SimpleLinkUpdater) FindLinks(content []byte) []core.Link {
	// Simple implementation - return empty slice
	// A real implementation would parse markdown links
	return []core.Link{}
}

// ResolveReferences resolves reference-style links
func (u *SimpleLinkUpdater) ResolveReferences(content []byte) map[string]string {
	// Simple implementation - return empty map
	// A real implementation would parse reference definitions
	return make(map[string]string)
}

// CLI represents the command-line interface
type CLI struct {
	fs             core.FileSystem
	stdin          io.Reader
	stdout         io.Writer
	stderr         io.Writer
	exitFunc       func(int)
	docsPath       string  // Path to documentation root directory
}

// NewCLI creates a new CLI instance
func NewCLI() *CLI {
	return &CLI{
		fs:       utils.NewOsFileSystem(),
		stdin:    os.Stdin,
		stdout:   os.Stdout,
		stderr:   os.Stderr,
		exitFunc: os.Exit,
		docsPath: ".",  // Default to current directory
	}
}

// NewTestCLI creates a CLI instance for testing
func NewTestCLI(fs core.FileSystem, stdin io.Reader, stdout, stderr io.Writer) *CLI {
	return &CLI{
		fs:       fs,
		stdin:    stdin,
		stdout:   stdout,
		stderr:   stderr,
		exitFunc: func(code int) { panic(fmt.Sprintf("exit %d", code)) },
		docsPath: ".",  // Default to current directory
	}
}

// Execute runs the CLI with the given arguments
func (c *CLI) Execute(args []string) error {
	// Parse flags first
	var commandArgs []string
	for i := 1; i < len(args); i++ {
		if args[i] == "--path" || args[i] == "-p" {
			if i+1 < len(args) {
				c.docsPath = args[i+1]
				i++ // Skip the path value
			} else {
				return fmt.Errorf("--path flag requires a value")
			}
		} else {
			commandArgs = append(commandArgs, args[i])
		}
	}

	// If no command provided, run TUI
	if len(commandArgs) == 0 {
		return c.runTUI("")
	}

	command := commandArgs[0]
	switch command {
	case "rename":
		if len(commandArgs) != 3 {
			return fmt.Errorf("Usage: %s [--path <docs-path>] rename <path-to-file> <new-name>", args[0])
		}
		return c.runRename(commandArgs[1], commandArgs[2])
	case "move":
		if len(commandArgs) != 3 {
			return fmt.Errorf("Usage: %s [--path <docs-path>] move <path-to-file> <new-path-to-file>", args[0])
		}
		return c.runMove(commandArgs[1], commandArgs[2])
	case "delete":
		if len(commandArgs) != 2 {
			return fmt.Errorf("Usage: %s [--path <docs-path>] delete <path-to-file>", args[0])
		}
		return c.runDelete(commandArgs[1])
	default:
		// If first argument looks like a path, run TUI with that path
		if _, err := os.Stat(command); err == nil {
			return c.runTUI(command)
		}
		return fmt.Errorf("Unknown command: %s\nAvailable commands: rename, move, delete", command)
	}
}

// runRename handles the rename command
func (c *CLI) runRename(filePath, newName string) error {
	// Initialize managers
	summary, summaryManager, redirectManager, linkUpdater, err := c.initializeManagers()
	if err != nil {
		return err
	}

	// Find entry in SUMMARY.md
	var entry *core.Entry
	for _, e := range summary.Entries {
		if e.Path == filePath {
			entry = &e
			break
		}
	}

	if entry == nil {
		return fmt.Errorf("File '%s' not found in SUMMARY.md", filePath)
	}

	fmt.Fprintf(c.stdout, "Renaming '%s' to '%s'\n", entry.Title, newName)

	// Generate new filename
	newPath := generateFilename(newName, filepath.Dir(filePath))
	fmt.Fprintf(c.stdout, "File will be renamed: %s -> %s\n", filePath, newPath)

	// Check for file collision
	oldFullPath := filepath.Join(c.docsPath, "docs", filePath)
	newFullPath := filepath.Join(c.docsPath, "docs", newPath)

	// Check if target file already exists (and it's not the same file)
	if oldFullPath != newFullPath && c.fs.Exists(newFullPath) {
		return fmt.Errorf("Target file '%s' already exists. Please choose a different name", newPath)
	}

	// Update entry in SUMMARY.md
	entry.Title = newName
	entry.Path = newPath
	err = summaryManager.Rename(entry.ID, newName, newPath)
	if err != nil {
		return fmt.Errorf("Error updating SUMMARY.md: %v", err)
	}

	// Save SUMMARY.md
	summaryPath := filepath.Join(c.docsPath, "docs", "SUMMARY.md")
	err = c.fs.WriteFile(summaryPath, summaryManager.Serialize(), 0644)
	if err != nil {
		return fmt.Errorf("Error saving SUMMARY.md: %v", err)
	}

	// Rename physical file
	if c.fs.Exists(oldFullPath) {
		// Read file content to update H1
		content, err := c.fs.ReadFile(oldFullPath)
		if err != nil {
			return fmt.Errorf("Error reading file: %v", err)
		}

		// Update H1 header
		updatedContent := updateH1Header(content, newName)

		// Write to new location
		err = c.fs.WriteFile(newFullPath, updatedContent, 0644)
		if err != nil {
			return fmt.Errorf("Error writing file: %v", err)
		}

		// Remove old file
		if newFullPath != oldFullPath {
			err = c.fs.Remove(oldFullPath)
			if err != nil {
				return fmt.Errorf("Error removing old file: %v", err)
			}
		}
	}

	// Add redirect if URL changed
	oldURL := pathToURL(filePath)
	newURL := pathToURL(newPath)

	if oldURL != newURL {
		err = redirectManager.AddRedirect(oldURL, newPath)
		if err != nil {
			return fmt.Errorf("Error adding redirect: %v", err)
		}

		// Save redirects
		gitbookPath := ".gitbook.yaml"
		err = c.fs.WriteFile(gitbookPath, redirectManager.Serialize(), 0644)
		if err != nil {
			return fmt.Errorf("Error saving redirects: %v", err)
		}

		fmt.Fprintf(c.stdout, "Added redirect: %s -> %s\n", oldURL, newPath)
	}

	// Update links in other files
	pathChanges := map[string]string{filePath: newPath}
	updatedFiles := c.updateLinksInFiles(linkUpdater, pathChanges)
	
	if len(updatedFiles) > 0 {
		fmt.Fprintln(c.stdout, "\nUpdated links in:")
		for file, count := range updatedFiles {
			fmt.Fprintf(c.stdout, "  • %s (%d link%s)\n", file, count, pluralize(count))
		}
	}

	fmt.Fprintln(c.stdout, "✓ Rename completed successfully")
	return nil
}

// runMove handles the move command
func (c *CLI) runMove(srcPath, dstPath string) error {
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

	// Get all entries
	entries := summaryManager.GetEntries()
	
	// Get same-level positions for the target section
	positions := filterSameLevelEntries(entries, dstPath)
	
	if len(positions) == 0 {
		return fmt.Errorf("No valid positions found for destination '%s'", dstPath)
	}
	
	// Extract target section for display
	targetSection := ""
	if idx := strings.Index(dstPath, "/"); idx > 0 {
		targetSection = dstPath[:idx]
	}
	
	// Get user selection using interactive selector
	// Try interactive selector first, fall back to number selection if TTY not available
	position, err := c.selectPositionInteractive(positions, targetSection)
	if err != nil {
		// If TTY error, fall back to simple number selection
		if strings.Contains(err.Error(), "TTY") || strings.Contains(err.Error(), "tty") {
			position, err = c.selectPositionByNumber(positions, targetSection)
			if err != nil {
				return err
			}
		} else {
			return err
		}
	}

	// Determine level based on position
	level := 0
	if position < len(entries) {
		// Find the level of the selected position
		for _, pos := range positions {
			if pos.Index == position {
				level = pos.Level
				break
			}
		}
	}

	// Check if moving to the same position
	currentIndex := -1
	for i, entry := range entries {
		if entry.ID == srcEntry.ID {
			currentIndex = i
			break
		}
	}

	// The position is already the actual index in entries
	targetPosition := position
	if currentIndex != -1 && targetPosition == currentIndex {
		fmt.Fprintln(c.stdout, "File is already at this position. No changes made.")
		return nil
	}

	// Find human-readable position description
	positionLabel := ""
	for i, pos := range positions {
		if pos.Index == position {
			if pos.IsEndOption {
				positionLabel = pos.Label
			} else {
				positionLabel = fmt.Sprintf("after '%s'", pos.Label)
				// If it's the first position, say "beginning"
				if i == 0 && pos.Level == 0 {
					positionLabel = "beginning of section"
				}
			}
			break
		}
	}
	fmt.Fprintf(c.stdout, "Moving '%s' to %s\n", srcEntry.Title, positionLabel)

	// Perform move in SUMMARY.md
	err = summaryManager.Move(srcEntry.ID, targetPosition, level)
	if err != nil {
		return fmt.Errorf("Error moving in SUMMARY.md: %v", err)
	}

	// Save SUMMARY.md
	summaryPath := filepath.Join(c.docsPath, "docs", "SUMMARY.md")
	err = c.fs.WriteFile(summaryPath, summaryManager.Serialize(), 0644)
	if err != nil {
		return fmt.Errorf("Error saving SUMMARY.md: %v", err)
	}

	// Move physical file if path changed
	oldFullPath := filepath.Join(c.docsPath, "docs", srcPath)
	newFullPath := filepath.Join(c.docsPath, "docs", dstPath)

	// Check if target file already exists
	if oldFullPath != newFullPath && c.fs.Exists(newFullPath) {
		return fmt.Errorf("Target file '%s' already exists. Please choose a different destination", dstPath)
	}

	if oldFullPath != newFullPath && c.fs.Exists(oldFullPath) {
		// Ensure destination directory exists
		destDir := filepath.Dir(newFullPath)
		err = c.fs.MkdirAll(destDir, 0755)
		if err != nil {
			return fmt.Errorf("Error creating directory: %v", err)
		}

		// Move file
		err = c.fs.MoveFile(oldFullPath, newFullPath)
		if err != nil {
			return fmt.Errorf("Error moving file: %v", err)
		}
		fmt.Fprintf(c.stdout, "Moved file: %s -> %s\n", oldFullPath, newFullPath)
	}

	// Add redirect if URL changed
	oldURL := pathToURL(srcPath)
	newURL := pathToURL(dstPath)

	if oldURL != newURL {
		// Update existing redirects that point to the old file
		redirectManager.UpdateRedirectsTo(srcPath, dstPath)
		
		// Add new redirect from old URL to new location
		err = redirectManager.AddRedirect(oldURL, dstPath)
		if err != nil {
			return fmt.Errorf("Error adding redirect: %v", err)
		}

		// Save redirects
		gitbookPath := ".gitbook.yaml"
		err = c.fs.WriteFile(gitbookPath, redirectManager.Serialize(), 0644)
		if err != nil {
			return fmt.Errorf("Error saving redirects: %v", err)
		}

		fmt.Fprintf(c.stdout, "Added redirect: %s -> %s\n", oldURL, dstPath)
	}

	// Update internal links in the moved file
	if c.fs.Exists(newFullPath) {
		content, err := c.fs.ReadFile(newFullPath)
		if err != nil {
			return fmt.Errorf("Error reading moved file: %v", err)
		}

		// Adjust relative links based on path change
		adjustedContent := adjustRelativeLinks(content, srcPath, dstPath)

		err = c.fs.WriteFile(newFullPath, adjustedContent, 0644)
		if err != nil {
			return fmt.Errorf("Error updating moved file: %v", err)
		}
	}

	// Update links in other files
	pathChanges := map[string]string{srcPath: dstPath}
	updatedFiles := c.updateLinksInFiles(linkUpdater, pathChanges)

	if len(updatedFiles) > 0 {
		fmt.Fprintln(c.stdout, "\nUpdated links in:")
		for file, count := range updatedFiles {
			fmt.Fprintf(c.stdout, "  • %s (%d link%s)\n", file, count, pluralize(count))
		}
	}

	fmt.Fprintln(c.stdout, "✓ Move completed successfully")
	return nil
}

// runDelete handles the delete command
func (c *CLI) runDelete(filePath string) error {
	// Initialize managers
	summary, summaryManager, redirectManager, _, err := c.initializeManagers()
	if err != nil {
		return err
	}

	// Find entry in SUMMARY.md
	var entry *core.Entry
	for _, e := range summary.Entries {
		if e.Path == filePath {
			entry = &e
			break
		}
	}

	if entry == nil {
		return fmt.Errorf("File '%s' not found in SUMMARY.md", filePath)
	}

	// Ask for confirmation
	fmt.Fprintf(c.stdout, "Are you sure you want to delete '%s'? [y/N]: ", entry.Title)
	scanner := bufio.NewScanner(c.stdin)
	if !scanner.Scan() {
		fmt.Fprintln(c.stdout, "Deletion cancelled")
		return nil
	}

	response := strings.ToLower(strings.TrimSpace(scanner.Text()))
	if response != "y" && response != "yes" {
		fmt.Fprintln(c.stdout, "Deletion cancelled")
		return nil
	}

	// Remove from SUMMARY.md
	err = summaryManager.Delete(entry.ID)
	if err != nil {
		return fmt.Errorf("Error removing from SUMMARY.md: %v", err)
	}

	// Save SUMMARY.md
	summaryPath := filepath.Join(c.docsPath, "docs", "SUMMARY.md")
	err = c.fs.WriteFile(summaryPath, summaryManager.Serialize(), 0644)
	if err != nil {
		return fmt.Errorf("Error saving SUMMARY.md: %v", err)
	}

	// Delete physical file
	fullPath := filepath.Join(c.docsPath, "docs", filePath)
	if c.fs.Exists(fullPath) {
		err = c.fs.Remove(fullPath)
		if err != nil {
			return fmt.Errorf("Error deleting file: %v", err)
		}
		fmt.Fprintf(c.stdout, "Deleted file: %s\n", fullPath)
	}

	// Remove from redirects if it was a redirect target
	url := pathToURL(filePath)
	redirectManager.RemoveRedirectsTo(url)

	// Save redirects
	gitbookPath := filepath.Join(c.docsPath, ".gitbook.yaml")
	err = c.fs.WriteFile(gitbookPath, redirectManager.Serialize(), 0644)
	if err != nil {
		return fmt.Errorf("Error saving redirects: %v", err)
	}

	fmt.Fprintln(c.stdout, "✓ Delete completed successfully")
	fmt.Fprintln(c.stdout, "Warning: Other files may contain links to the deleted file")
	return nil
}

// runTUI launches the terminal user interface
func (c *CLI) runTUI(docsPath string) error {
	// TUI implementation would go here
	// For now, return an error to indicate not implemented
	return fmt.Errorf("TUI mode not implemented in refactored version")
}

// initializeManagers initializes all required managers
func (c *CLI) initializeManagers() (*core.Summary, core.SummaryManager, core.RedirectManager, core.LinkUpdater, error) {
	// Load SUMMARY.md
	summaryPath := filepath.Join(c.docsPath, "docs", "SUMMARY.md")
	if !c.fs.Exists(summaryPath) {
		return nil, nil, nil, nil, fmt.Errorf("SUMMARY.md not found in '%s/docs'", c.docsPath)
	}

	summaryContent, err := c.fs.ReadFile(summaryPath)
	if err != nil {
		return nil, nil, nil, nil, fmt.Errorf("Error reading SUMMARY.md: %v", err)
	}

	parser := core.NewSummaryParser()
	summary, err := parser.Parse(summaryContent)
	if err != nil {
		return nil, nil, nil, nil, fmt.Errorf("Error parsing SUMMARY.md: %v", err)
	}

	summaryManager := core.NewSummaryManager(summary)

	// Load .gitbook.yaml for redirects
	redirectManager := core.NewRedirectManager()
	gitbookPath := filepath.Join(c.docsPath, ".gitbook.yaml")
	if c.fs.Exists(gitbookPath) {
		content, err := c.fs.ReadFile(gitbookPath)
		if err != nil {
			return nil, nil, nil, nil, fmt.Errorf("Error reading .gitbook.yaml: %v", err)
		}
		_, err = redirectManager.LoadRedirects(content)
		if err != nil {
			return nil, nil, nil, nil, fmt.Errorf("Error loading redirects: %v", err)
		}
	}

	// Create link updater
	linkUpdater := &SimpleLinkUpdater{fs: c.fs}

	return summary, summaryManager, redirectManager, linkUpdater, nil
}

// updateLinksInFiles updates links in all documentation files
// Returns a map of file paths to the number of links updated in each file
func (c *CLI) updateLinksInFiles(linkUpdater core.LinkUpdater, pathChanges map[string]string) map[string]int {
	updatedFiles := make(map[string]int)

	// Get all markdown files
	docsDir := filepath.Join(c.docsPath, "docs")
	files := c.getMarkdownFiles(docsDir)

	for _, file := range files {
		content, err := c.fs.ReadFile(file)
		if err != nil {
			continue
		}

		// Set the current file path for relative link resolution
		if simpleUpdater, ok := linkUpdater.(*SimpleLinkUpdater); ok {
			// Remove the docs path prefix to get relative path
			relFile := strings.TrimPrefix(file, filepath.Join(c.docsPath, "docs")+"/")
			simpleUpdater.filePath = relFile
		}

		newContent, updated, count := linkUpdater.UpdateLinks(content, pathChanges)

		if updated {
			c.fs.WriteFile(file, newContent, 0644)
			// Store relative path and count
			relFile := strings.TrimPrefix(file, filepath.Join(c.docsPath, "")+"/")
			updatedFiles[relFile] = count
		}
	}

	return updatedFiles
}

// getMarkdownFiles recursively finds all markdown files
func (c *CLI) getMarkdownFiles(dir string) []string {
	var files []string

	entries, err := c.fs.ReadDir(dir)
	if err != nil {
		return files
	}

	for _, entry := range entries {
		path := filepath.Join(dir, entry.Name())
		if entry.IsDir() {
			files = append(files, c.getMarkdownFiles(path)...)
		} else if strings.HasSuffix(entry.Name(), ".md") {
			files = append(files, path)
		}
	}

	return files
}

// Helper functions (kept as package-level for compatibility)

func generateFilename(title, dir string) string {
	// Convert to lowercase
	filename := strings.ToLower(title)

	// Replace spaces and special characters with hyphens
	filename = strings.ReplaceAll(filename, " ", "-")
	filename = strings.ReplaceAll(filename, ".", "")
	filename = strings.ReplaceAll(filename, "/", "")
	filename = strings.ReplaceAll(filename, "\\", "")
	filename = strings.ReplaceAll(filename, ":", "")
	filename = strings.ReplaceAll(filename, "*", "")
	filename = strings.ReplaceAll(filename, "?", "")
	filename = strings.ReplaceAll(filename, "\"", "")
	filename = strings.ReplaceAll(filename, "<", "")
	filename = strings.ReplaceAll(filename, ">", "")
	filename = strings.ReplaceAll(filename, "|", "")
	filename = strings.ReplaceAll(filename, "!", "")
	filename = strings.ReplaceAll(filename, "@", "")
	filename = strings.ReplaceAll(filename, "#", "")
	filename = strings.ReplaceAll(filename, "$", "")
	filename = strings.ReplaceAll(filename, "%", "")
	filename = strings.ReplaceAll(filename, "^", "")
	filename = strings.ReplaceAll(filename, "&", "")
	filename = strings.ReplaceAll(filename, "(", "")
	filename = strings.ReplaceAll(filename, ")", "")

	// Remove multiple hyphens
	for strings.Contains(filename, "--") {
		filename = strings.ReplaceAll(filename, "--", "-")
	}
	
	// Trim leading and trailing hyphens
	filename = strings.Trim(filename, "-")

	// Add .md extension
	filename = filename + ".md"

	// Combine with directory
	if dir != "" {
		return filepath.Join(dir, filename)
	}
	return filename
}

func pathToURL(path string) string {
	// Remove .md extension
	url := strings.TrimSuffix(path, ".md")

	// Handle README.md specially - it becomes the directory index
	if strings.HasSuffix(url, "/README") {
		url = strings.TrimSuffix(url, "/README")
	} else if url == "README" {
		url = ""
	}

	// Add leading slash
	if url != "" && !strings.HasPrefix(url, "/") {
		url = "/" + url
	}

	return url
}

func updateH1Header(content []byte, newTitle string) []byte {
	lines := strings.Split(string(content), "\n")
	for i, line := range lines {
		if strings.HasPrefix(strings.TrimSpace(line), "# ") {
			lines[i] = "# " + newTitle
			break
		}
	}
	return []byte(strings.Join(lines, "\n"))
}

// adjustRelativeLinks updates relative links when a file moves to a different location
func adjustRelativeLinks(content []byte, oldPath, newPath string) []byte {
	// Parse old and new directories
	oldDir := filepath.Dir(oldPath)
	newDir := filepath.Dir(newPath)
	
	// If in the same directory, no changes needed
	if oldDir == newDir {
		return content
	}
	
	contentStr := string(content)
	
	// Regular expression to find markdown links
	// Matches [text](link) pattern
	linkRegex := regexp.MustCompile(`\[([^\]]+)\]\(([^)]+)\)`)
	
	result := linkRegex.ReplaceAllStringFunc(contentStr, func(match string) string {
		submatches := linkRegex.FindStringSubmatch(match)
		if len(submatches) < 3 {
			return match
		}
		
		linkText := submatches[1]
		linkPath := submatches[2]
		
		// Skip absolute URLs and anchors
		if strings.HasPrefix(linkPath, "http://") || 
		   strings.HasPrefix(linkPath, "https://") || 
		   strings.HasPrefix(linkPath, "#") ||
		   strings.HasPrefix(linkPath, "/") {
			return match
		}
		
		// Handle anchors in the path
		anchor := ""
		if idx := strings.Index(linkPath, "#"); idx >= 0 {
			anchor = linkPath[idx:]
			linkPath = linkPath[:idx]
		}
		
		// Calculate the absolute path of the link from the old location
		var absolutePath string
		if filepath.IsAbs(linkPath) {
			absolutePath = linkPath
		} else {
			// Resolve the link relative to the old location
			absolutePath = filepath.Join(oldDir, linkPath)
			absolutePath = filepath.Clean(absolutePath)
		}
		
		// Calculate the new relative path from the new location
		relPath, err := filepath.Rel(newDir, absolutePath)
		if err != nil {
			// If we can't calculate relative path, keep original
			return match
		}
		
		// Ensure forward slashes for markdown links
		relPath = strings.ReplaceAll(relPath, "\\", "/")
		
		// Add anchor back if there was one
		if anchor != "" {
			relPath = relPath + anchor
		}
		
		return fmt.Sprintf("[%s](%s)", linkText, relPath)
	})
	
	return []byte(result)
}

// pluralize returns "s" if count is not 1
func pluralize(count int) string {
	if count == 1 {
		return ""
	}
	return "s"
}