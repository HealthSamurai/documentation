package core

import (
	"bufio"
	"bytes"
	"fmt"
	"regexp"
	"strings"
)

var (
	// Regex patterns for parsing
	sectionRegex = regexp.MustCompile(`^##\s+(.+)$`)
	entryRegex   = regexp.MustCompile(`^(\s*)\*\s+\[([^\]]+)\]\(([^)]+)\)`)
	bulletRegex  = regexp.MustCompile(`^(\s*)\*\s+`)
)

// SummaryParser handles parsing of SUMMARY.md files
type SummaryParser struct{}

// NewSummaryParser creates a new parser instance
func NewSummaryParser() *SummaryParser {
	return &SummaryParser{}
}

// Parse parses SUMMARY.md content into structured data
func (p *SummaryParser) Parse(content []byte) (*Summary, error) {
	scanner := bufio.NewScanner(bytes.NewReader(content))
	
	summary := &Summary{
		RawContent: string(content),
		Entries:    []Entry{},
	}
	
	var currentSection string
	lineNum := 0
	index := 0
	
	for scanner.Scan() {
		lineNum++
		line := scanner.Text()
		
		// Skip empty lines
		if strings.TrimSpace(line) == "" {
			continue
		}
		
		// Check for section header
		if matches := sectionRegex.FindStringSubmatch(line); matches != nil {
			section := strings.TrimSpace(matches[1])
			summary.Entries = append(summary.Entries, Entry{
				ID:      fmt.Sprintf("section-%d", index),
				Type:    EntryTypeSection,
				Title:   section,
				Level:   0,
				Index:   index,
				Line:    lineNum,
			})
			currentSection = section
			index++
			continue
		}
		
		// Check for entry
		if matches := entryRegex.FindStringSubmatch(line); matches != nil {
			indent := matches[1]
			title := strings.TrimSpace(matches[2])
			path := strings.TrimSpace(matches[3])
			
			// Calculate level based on indentation
			level := p.calculateLevel(indent)
			
			entry := Entry{
				ID:      fmt.Sprintf("entry-%d", index),
				Type:    EntryTypeItem,
				Title:   title,
				Path:    path,
				Level:   level,
				Index:   index,
				Section: currentSection,
				Line:    lineNum,
			}
			
			summary.Entries = append(summary.Entries, entry)
			index++
		}
	}
	
	// Mark entries with children
	p.markParents(summary.Entries)
	
	return summary, scanner.Err()
}

// calculateLevel determines indentation level
func (p *SummaryParser) calculateLevel(indent string) int {
	// Count spaces or tabs
	// 2 spaces or 1 tab = 1 level
	spaces := 0
	tabs := 0
	
	for _, ch := range indent {
		if ch == ' ' {
			spaces++
		} else if ch == '\t' {
			tabs++
		}
	}
	
	// Convert to levels (2 spaces = 1 level, 1 tab = 1 level)
	level := tabs + (spaces / 2)
	return level
}

// markParents marks entries that have children
func (p *SummaryParser) markParents(entries []Entry) {
	for i := 0; i < len(entries)-1; i++ {
		if entries[i].Type != EntryTypeItem {
			continue
		}
		
		// Check if next entry is a child (higher level)
		for j := i + 1; j < len(entries); j++ {
			if entries[j].Type != EntryTypeItem {
				continue
			}
			
			if entries[j].Level > entries[i].Level {
				entries[i].HasChildren = true
				break
			} else if entries[j].Level <= entries[i].Level {
				// Found entry at same or lower level, no children
				break
			}
		}
	}
}

// summaryManager implements SummaryManager interface
type summaryManager struct {
	summary *Summary
}

// NewSummaryManager creates a new summary manager
func NewSummaryManager(summary *Summary) SummaryManager {
	return &summaryManager{summary: summary}
}

// FindEntry finds an entry by ID
func (m *summaryManager) FindEntry(id string) *Entry {
	for i := range m.summary.Entries {
		if m.summary.Entries[i].ID == id {
			return &m.summary.Entries[i]
		}
	}
	return nil
}

// GetEntries returns all entries
func (m *summaryManager) GetEntries() []Entry {
	return m.summary.Entries
}

// Move moves an entry to a new position
func (m *summaryManager) Move(entryID string, targetPosition int, newLevel int) error {
	// Find the entry to move
	entryIdx := -1
	for i, e := range m.summary.Entries {
		if e.ID == entryID {
			entryIdx = i
			break
		}
	}
	
	if entryIdx == -1 {
		return fmt.Errorf("entry not found: %s", entryID)
	}
	
	entry := m.summary.Entries[entryIdx]
	
	// Check for circular dependency BEFORE collecting children
	if err := m.checkCircularDependency(entryIdx, targetPosition, newLevel); err != nil {
		return err
	}
	
	// Collect entry with its children
	entriesToMove := m.collectEntryWithChildren(entryIdx)
	
	// Remove entries from original position
	remaining := make([]Entry, 0, len(m.summary.Entries))
	moveIdx := 0
	for i, e := range m.summary.Entries {
		found := false
		for _, moveEntry := range entriesToMove {
			if e.ID == moveEntry.ID {
				found = true
				break
			}
		}
		if !found {
			remaining = append(remaining, e)
		} else if i < targetPosition {
			moveIdx++
		}
	}
	
	// Adjust target position based on removed entries
	adjustedTarget := targetPosition - moveIdx
	if adjustedTarget < 0 {
		adjustedTarget = 0
	}
	if adjustedTarget > len(remaining) {
		adjustedTarget = len(remaining)
	}
	
	// Update levels for moved entries
	levelDiff := newLevel - entry.Level
	for i := range entriesToMove {
		entriesToMove[i].Level += levelDiff
	}
	
	// Insert at new position
	result := make([]Entry, 0, len(m.summary.Entries))
	result = append(result, remaining[:adjustedTarget]...)
	result = append(result, entriesToMove...)
	result = append(result, remaining[adjustedTarget:]...)
	
	// Update indices
	for i := range result {
		result[i].Index = i
	}
	
	// Update parent/child relationships
	p := &SummaryParser{}
	p.markParents(result)
	
	m.summary.Entries = result
	return nil
}

// collectEntryWithChildren collects an entry and all its children
func (m *summaryManager) collectEntryWithChildren(startIdx int) []Entry {
	result := []Entry{m.summary.Entries[startIdx]}
	startLevel := m.summary.Entries[startIdx].Level
	
	for i := startIdx + 1; i < len(m.summary.Entries); i++ {
		entry := m.summary.Entries[i]
		if entry.Type == EntryTypeItem && entry.Level > startLevel {
			result = append(result, entry)
		} else if entry.Type == EntryTypeItem && entry.Level <= startLevel {
			break
		}
	}
	
	return result
}

// checkCircularDependency checks if move would create circular dependency
func (m *summaryManager) checkCircularDependency(entryIdx, targetPos, newLevel int) error {
	if newLevel == 0 {
		// Top level entries can't create circular dependencies
		return nil
	}
	
	entry := m.summary.Entries[entryIdx]
	
	// Collect all descendants (children, grandchildren, etc.) of the moving entry
	descendantIDs := make(map[string]bool)
	var collectDescendants func(idx int, parentLevel int)
	collectDescendants = func(idx int, parentLevel int) {
		for i := idx + 1; i < len(m.summary.Entries); i++ {
			if m.summary.Entries[i].Type != EntryTypeItem {
				continue
			}
			
			if m.summary.Entries[i].Level <= parentLevel {
				break // Not a descendant anymore
			}
			
			// This is a descendant
			descendantIDs[m.summary.Entries[i].ID] = true
		}
	}
	collectDescendants(entryIdx, entry.Level)
	
	// If no descendants, no circular dependency possible
	if len(descendantIDs) == 0 {
		return nil
	}
	
	// Build list of what remains after removal
	var remainingEntries []Entry
	descendantCount := 0
	for i, e := range m.summary.Entries {
		if i == entryIdx {
			continue // Skip the moving entry
		}
		if i > entryIdx && descendantIDs[e.ID] {
			descendantCount++
			continue // Skip descendants
		}
		remainingEntries = append(remainingEntries, e)
	}
	
	// Adjust target position based on removed entries before it
	adjustedTarget := targetPos
	if targetPos > entryIdx {
		// Subtract the number of entries removed before the target
		adjustedTarget = targetPos - 1 - descendantCount
	}
	
	// Ensure adjusted target is within bounds
	if adjustedTarget < 0 {
		adjustedTarget = 0
	}
	if adjustedTarget > len(remainingEntries) {
		adjustedTarget = len(remainingEntries)
	}
	
	// Find what would be the parent at the target position with the new level
	for i := adjustedTarget - 1; i >= 0; i-- {
		e := remainingEntries[i]
		if e.Type != EntryTypeItem {
			continue
		}
		
		// Found a potential parent (has lower level than our new level)
		if e.Level < newLevel {
			// Check if this potential parent is one of our descendants
			if descendantIDs[e.ID] {
				return fmt.Errorf("cannot move parent under its own child")
			}
			// Found valid parent, no circular dependency
			return nil
		}
	}
	
	return nil
}

// Rename renames an entry
func (m *summaryManager) Rename(entryID string, newTitle, newPath string) error {
	if newTitle == "" {
		return fmt.Errorf("title cannot be empty")
	}
	
	entry := m.FindEntry(entryID)
	if entry == nil {
		return fmt.Errorf("entry not found: %s", entryID)
	}
	
	entry.Title = newTitle
	if newPath != "" {
		entry.Path = newPath
	}
	
	return nil
}

// Delete deletes an entry
func (m *summaryManager) Delete(entryID string) error {
	// Find entry
	entryIdx := -1
	for i, e := range m.summary.Entries {
		if e.ID == entryID {
			entryIdx = i
			break
		}
	}
	
	if entryIdx == -1 {
		return fmt.Errorf("entry not found: %s", entryID)
	}
	
	entry := m.summary.Entries[entryIdx]
	
	// Check if entry has children
	if entry.HasChildren {
		return fmt.Errorf("cannot delete entry that has children")
	}
	
	// Remove entry
	m.summary.Entries = append(m.summary.Entries[:entryIdx], m.summary.Entries[entryIdx+1:]...)
	
	// Update indices
	for i := entryIdx; i < len(m.summary.Entries); i++ {
		m.summary.Entries[i].Index = i
	}
	
	// Update parent/child relationships
	p := &SummaryParser{}
	p.markParents(m.summary.Entries)
	
	return nil
}

// Serialize converts the summary back to markdown
func (m *summaryManager) Serialize() []byte {
	var buf bytes.Buffer
	
	// Write header
	buf.WriteString("# Table of contents\n")
	
	// Add blank line after header if there are entries
	if len(m.summary.Entries) > 0 && m.summary.Entries[0].Type == EntryTypeItem {
		buf.WriteString("\n")
	}
	
	lastWasSection := false
	
	for _, entry := range m.summary.Entries {
		switch entry.Type {
		case EntryTypeSection:
			// Add blank line before section
			buf.WriteString("\n")
			buf.WriteString(fmt.Sprintf("## %s\n", entry.Title))
			lastWasSection = true
			
		case EntryTypeItem:
			// Add blank line after section header
			if lastWasSection {
				buf.WriteString("\n")
				lastWasSection = false
			}
			
			// Write indentation
			indent := strings.Repeat("  ", entry.Level)
			buf.WriteString(fmt.Sprintf("%s* [%s](%s)\n", indent, entry.Title, entry.Path))
		}
	}
	
	// Ensure file ends with a newline
	result := buf.Bytes()
	if len(result) > 0 && result[len(result)-1] != '\n' {
		result = append(result, '\n')
	}
	
	return result
}