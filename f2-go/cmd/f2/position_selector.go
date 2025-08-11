package main

import (
	"fmt"
	"strings"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/lipgloss"
	"github.com/f2-go/internal/core"
)

// Model for position selector
type selectorModel struct {
	positions     []InsertPosition
	cursor        int
	selected      int
	targetSection string
	cancelled     bool
}

func newSelectorModel(positions []InsertPosition, targetSection string) selectorModel {
	return selectorModel{
		positions:     positions,
		cursor:        0,
		selected:      -1,
		targetSection: targetSection,
	}
}

func (m selectorModel) Init() tea.Cmd {
	return nil
}

func (m selectorModel) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.KeyMsg:
		switch msg.String() {
		case "up", "k":
			if m.cursor > 0 {
				m.cursor--
			}
		case "down", "j":
			if m.cursor < len(m.positions)-1 {
				m.cursor++
			}
		case "enter":
			m.selected = m.cursor
			return m, tea.Quit
		case "q", "esc", "ctrl+c":
			m.cancelled = true
			return m, tea.Quit
		}
	}
	return m, nil
}

func (m selectorModel) View() string {
	var s strings.Builder
	
	// Header
	s.WriteString("\n")
	if m.targetSection != "" {
		s.WriteString(fmt.Sprintf("Select position for insertion in '%s' section:\n", m.targetSection))
	} else {
		s.WriteString("Select position for insertion:\n")
	}
	s.WriteString("Use ↑/↓ to move, Enter to select, q to cancel\n")
	s.WriteString("----------------------------------------\n")
	
	// Style definitions
	cursorStyle := lipgloss.NewStyle().Foreground(lipgloss.Color("39")).Bold(true)
	normalStyle := lipgloss.NewStyle()
	
	// Positions
	for i, pos := range m.positions {
		cursor := "  "
		if m.cursor == i {
			cursor = "→ "
		}
		
		line := cursor
		if pos.IsEndOption {
			line += fmt.Sprintf("[%s]", pos.Label)
		} else {
			indent := strings.Repeat("  ", pos.Level)
			line += fmt.Sprintf("%s%s", indent, pos.Label)
		}
		
		if m.cursor == i {
			s.WriteString(cursorStyle.Render(line))
		} else {
			s.WriteString(normalStyle.Render(line))
		}
		s.WriteString("\n")
	}
	
	s.WriteString("----------------------------------------\n")
	
	return s.String()
}

// RunPositionSelector runs the interactive position selector with bubbletea
func RunPositionSelector(positions []InsertPosition, targetSection string) (int, error) {
	m := newSelectorModel(positions, targetSection)
	
	p := tea.NewProgram(m)
	finalModel, err := p.Run()
	if err != nil {
		return -1, err
	}
	
	final := finalModel.(selectorModel)
	if final.cancelled {
		return -1, fmt.Errorf("selection cancelled")
	}
	
	if final.selected >= 0 && final.selected < len(positions) {
		return positions[final.selected].Index, nil
	}
	
	return -1, fmt.Errorf("no position selected")
}

// FilterEntriesForSection filters entries to show only relevant section
func FilterEntriesForSection(entries []core.Entry, targetPath string) ([]core.Entry, int, int) {
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
				// Find the section header
				sectionStart = i
				// Look backwards for section header (lower level)
				for j := i - 1; j >= 0; j-- {
					if entries[j].Level < entry.Level {
						// Check if this is our section header
						if strings.HasPrefix(entries[j].Path, targetSection) ||
						   entries[j].Title == formatSectionTitle(targetSection) {
							sectionStart = j
							break
						}
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

// formatSectionTitle converts section path to expected title format
func formatSectionTitle(section string) string {
	// Convert kebab-case to Title Case
	words := strings.Split(section, "-")
	for i, word := range words {
		if len(word) > 0 {
			words[i] = strings.ToUpper(word[:1]) + word[1:]
		}
	}
	return strings.Join(words, " ")
}