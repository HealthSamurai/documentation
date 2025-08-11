package ui

import (
	"fmt"
	"path/filepath"

	"github.com/f2-go/internal/core"
)

// Save saves all pending changes to disk
func (m *Model) Save() error {
	if m.unsavedChanges == 0 {
		return nil
	}

	// Use the root directory from the model
	rootDir := m.rootDir

	// Process all operations
	pathChanges := make(map[string]string)
	
	for _, op := range m.operations {
		switch op.Type {
		case core.OpTypeMove:
			// Moving only affects SUMMARY.md structure, no file operations needed
			
		case core.OpTypeRename:
			oldPath := filepath.Join(rootDir, op.OldPath)
			newPath := filepath.Join(rootDir, op.NewPath)
			
			// Track path changes for link updates
			pathChanges[op.OldPath] = op.NewPath
			
			// Move the actual file
			if err := m.fileSystem.MoveFile(oldPath, newPath); err != nil {
				return fmt.Errorf("failed to rename file %s to %s: %w", oldPath, newPath, err)
			}
			
		case core.OpTypeDelete:
			filePath := filepath.Join(rootDir, op.OldPath)
			
			// Delete the file
			if err := m.fileSystem.Remove(filePath); err != nil {
				return fmt.Errorf("failed to delete file %s: %w", filePath, err)
			}
		}
	}
	
	// Update links in all markdown files if there were renames
	if len(pathChanges) > 0 {
		if err := m.updateAllLinks(rootDir, pathChanges); err != nil {
			return fmt.Errorf("failed to update links: %w", err)
		}
	}
	
	// Save updated SUMMARY.md
	summaryPath := filepath.Join(rootDir, "SUMMARY.md")
	summaryContent := m.summaryManager.Serialize()
	if err := m.fileSystem.WriteFile(summaryPath, summaryContent, 0644); err != nil {
		return fmt.Errorf("failed to save SUMMARY.md: %w", err)
	}
	
	// Save redirects if any were added
	// Note: We always save .gitbook.yaml even if empty to maintain consistency
	redirectsPath := filepath.Join(rootDir, ".gitbook.yaml")
	redirectContent := m.redirectManager.Serialize()
	if err := m.fileSystem.WriteFile(redirectsPath, redirectContent, 0644); err != nil {
		return fmt.Errorf("failed to save .gitbook.yaml: %w", err)
	}
	
	// Clear operations and reset unsaved changes
	m.operations = []core.Operation{}
	m.unsavedChanges = 0
	m.changesSummary = []string{}
	m.successMsg = "All changes saved successfully!"
	
	return nil
}

// updateAllLinks updates links in all markdown files
func (m *Model) updateAllLinks(rootDir string, pathChanges map[string]string) error {
	// Walk through all markdown files
	walkFn := func(path string, isDir bool) error {
		if isDir || filepath.Ext(path) != ".md" {
			return nil
		}
		
		// Read file content
		content, err := m.fileSystem.ReadFile(path)
		if err != nil {
			return fmt.Errorf("failed to read %s: %w", path, err)
		}
		
		// Update links
		updated, changed := m.linkUpdater.UpdateLinks(content, pathChanges)
		if changed {
			// Write updated content back
			if err := m.fileSystem.WriteFile(path, updated, 0644); err != nil {
				return fmt.Errorf("failed to write %s: %w", path, err)
			}
		}
		
		return nil
	}
	
	return m.fileSystem.Walk(rootDir, walkFn)
}