package ui

import (
	"fmt"
	"strings"

	"github.com/charmbracelet/bubbles/key"
	"github.com/charmbracelet/bubbles/textinput"
	"github.com/charmbracelet/bubbles/viewport"
	tea "github.com/charmbracelet/bubbletea"
	"github.com/charmbracelet/lipgloss"
	"github.com/f2-go/internal/core"
)

// Mode represents the current mode of the application
type Mode string

const (
	ModeNormal Mode = "normal"
	ModeMove   Mode = "move"
	ModeRename Mode = "rename"
	ModeDelete Mode = "delete"
)

// Model represents the application state
type Model struct {
	// Core components
	summaryManager   core.SummaryManager
	redirectManager  core.RedirectManager
	linkUpdater      core.LinkUpdater
	fileSystem       core.FileSystem
	rootDir          string

	// UI state
	entries          []core.Entry
	cursor           int
	selectedID       string
	expandedFolders  map[string]bool
	mode             Mode
	viewport         viewport.Model
	textInput        textinput.Model
	width            int
	height           int

	// Operation state
	moveSourceID     string
	unsavedChanges   int
	operations       []core.Operation
	changesSummary   []string
	errorMsg         string
	successMsg       string

	// Styling
	styles           Styles
}

// Styles holds all the styling for the UI
type Styles struct {
	Title        lipgloss.Style
	Cursor       lipgloss.Style
	Selected     lipgloss.Style
	Normal       lipgloss.Style
	Section      lipgloss.Style
	StatusBar    lipgloss.Style
	Error        lipgloss.Style
	Success      lipgloss.Style
	TextInput    lipgloss.Style
	MoveMode     lipgloss.Style
}

// KeyMap defines the key bindings
type KeyMap struct {
	Up       key.Binding
	Down     key.Binding
	Left     key.Binding
	Right    key.Binding
	Enter    key.Binding
	Space    key.Binding
	Rename   key.Binding
	Delete   key.Binding
	Save     key.Binding
	Undo     key.Binding
	Help     key.Binding
	Quit     key.Binding
}

// DefaultKeyMap returns the default key bindings
func DefaultKeyMap() KeyMap {
	return KeyMap{
		Up: key.NewBinding(
			key.WithKeys("up", "k"),
			key.WithHelp("↑/k", "up"),
		),
		Down: key.NewBinding(
			key.WithKeys("down", "j"),
			key.WithHelp("↓/j", "down"),
		),
		Left: key.NewBinding(
			key.WithKeys("left", "h"),
			key.WithHelp("←/h", "collapse"),
		),
		Right: key.NewBinding(
			key.WithKeys("right", "l"),
			key.WithHelp("→/l", "expand"),
		),
		Enter: key.NewBinding(
			key.WithKeys("enter"),
			key.WithHelp("enter", "select/move"),
		),
		Space: key.NewBinding(
			key.WithKeys(" "),
			key.WithHelp("space", "toggle expand"),
		),
		Rename: key.NewBinding(
			key.WithKeys("r"),
			key.WithHelp("r", "rename"),
		),
		Delete: key.NewBinding(
			key.WithKeys("d", "delete"),
			key.WithHelp("d", "delete"),
		),
		Save: key.NewBinding(
			key.WithKeys("ctrl+s"),
			key.WithHelp("ctrl+s", "save"),
		),
		Undo: key.NewBinding(
			key.WithKeys("ctrl+z"),
			key.WithHelp("ctrl+z", "undo"),
		),
		Help: key.NewBinding(
			key.WithKeys("?"),
			key.WithHelp("?", "help"),
		),
		Quit: key.NewBinding(
			key.WithKeys("q", "ctrl+c"),
			key.WithHelp("q", "quit"),
		),
	}
}

// DefaultStyles returns the default styles
func DefaultStyles() Styles {
	return Styles{
		Title: lipgloss.NewStyle().
			Bold(true).
			Foreground(lipgloss.Color("39")).
			Padding(0, 1),
		Cursor: lipgloss.NewStyle().
			Foreground(lipgloss.Color("33")).
			Bold(true),
		Selected: lipgloss.NewStyle().
			Foreground(lipgloss.Color("170")).
			Bold(true),
		Normal: lipgloss.NewStyle(),
		Section: lipgloss.NewStyle().
			Foreground(lipgloss.Color("39")).
			Bold(true),
		StatusBar: lipgloss.NewStyle().
			Foreground(lipgloss.Color("240")),
		Error: lipgloss.NewStyle().
			Foreground(lipgloss.Color("196")),
		Success: lipgloss.NewStyle().
			Foreground(lipgloss.Color("46")),
		TextInput: lipgloss.NewStyle().
			Foreground(lipgloss.Color("39")),
		MoveMode: lipgloss.NewStyle().
			Foreground(lipgloss.Color("214")).
			Bold(true),
	}
}

// New creates a new Model
func New(
	summaryManager core.SummaryManager,
	redirectManager core.RedirectManager,
	linkUpdater core.LinkUpdater,
	fileSystem core.FileSystem,
	rootDir string,
) Model {
	ti := textinput.New()
	ti.Placeholder = "Enter new name..."
	ti.Focus()
	ti.CharLimit = 100
	
	return Model{
		summaryManager:  summaryManager,
		redirectManager: redirectManager,
		linkUpdater:     linkUpdater,
		fileSystem:      fileSystem,
		entries:         summaryManager.GetEntries(),
		expandedFolders: make(map[string]bool),
		mode:            ModeNormal,
		textInput:       ti,
		styles:          DefaultStyles(),
		operations:      []core.Operation{},
		changesSummary:  []string{},
		rootDir:         rootDir,
	}
}

// Init initializes the model
func (m Model) Init() tea.Cmd {
	return nil
}

// Update handles messages
func (m Model) Update(msg tea.Msg) (tea.Model, tea.Cmd) {
	switch msg := msg.(type) {
	case tea.WindowSizeMsg:
		m.width = msg.Width
		m.height = msg.Height
		m.viewport = viewport.New(msg.Width, msg.Height-4) // Leave room for header and status
		m.viewport.SetContent(m.renderTree())
		return m, nil

	case tea.KeyMsg:
		switch m.mode {
		case ModeNormal:
			return m.handleNormalMode(msg)
		case ModeMove:
			return m.handleMoveMode(msg)
		case ModeRename:
			return m.handleRenameMode(msg)
		case ModeDelete:
			return m.handleDeleteMode(msg)
		}
	}

	// Update viewport
	var cmd tea.Cmd
	m.viewport, cmd = m.viewport.Update(msg)
	return m, cmd
}

// View renders the UI
func (m Model) View() string {
	if m.width == 0 {
		return "Initializing..."
	}

	var b strings.Builder

	// Header
	title := m.styles.Title.Render("📚 f2 - GitBook Documentation Manager")
	help := m.styles.Normal.Foreground(lipgloss.Color("240")).Render("[? Help]")
	header := lipgloss.JoinHorizontal(
		lipgloss.Top,
		title,
		lipgloss.PlaceHorizontal(m.width-lipgloss.Width(title)-lipgloss.Width(help), lipgloss.Right, help),
	)
	b.WriteString(header + "\n")

	// Main content area with two panels
	leftPanelWidth := m.width * 2 / 3
	rightPanelWidth := m.width - leftPanelWidth - 3 // -3 for borders and padding
	
	// Left panel: Tree view
	leftContent := m.renderTree()
	leftViewport := viewport.New(leftPanelWidth, m.height-4)
	leftViewport.SetContent(leftContent)
	
	// Right panel: Info/Changes
	rightContent := m.renderInfoPanel()
	rightViewport := viewport.New(rightPanelWidth, m.height-4)
	rightViewport.SetContent(rightContent)
	
	// Join panels horizontally
	leftPanel := lipgloss.NewStyle().
		BorderStyle(lipgloss.RoundedBorder()).
		BorderForeground(lipgloss.Color("240")).
		Width(leftPanelWidth).
		Height(m.height - 4).
		Render(leftViewport.View())
		
	rightPanel := lipgloss.NewStyle().
		BorderStyle(lipgloss.RoundedBorder()).
		BorderForeground(lipgloss.Color("240")).
		Width(rightPanelWidth).
		Height(m.height - 4).
		Render(rightViewport.View())
	
	panels := lipgloss.JoinHorizontal(lipgloss.Top, leftPanel, " ", rightPanel)
	b.WriteString(panels + "\n")
	
	// Mode-specific content
	if m.mode == ModeRename {
		b.WriteString(m.styles.TextInput.Render("Rename: ") + m.textInput.View() + "\n")
	}

	// Status bar
	status := m.renderStatusBar()
	b.WriteString(status)

	return b.String()
}

// renderTree renders the document tree
func (m Model) renderTree() string {
	var b strings.Builder
	visibleIndex := 0
	showInsertAfterIndex := -1
	insertLevel := 0

	// First pass: determine where to show the insert indicator
	if m.mode == ModeMove && m.moveSourceID != "" {
		cursorEntry := m.getEntryAtCursor()
		if cursorEntry != nil && cursorEntry.ID != m.moveSourceID {
			_, targetLevel := m.calculateMoveTarget()
			insertLevel = targetLevel
			
			if cursorEntry.HasChildren && m.expandedFolders[cursorEntry.ID] {
				// Insert as first child - show after parent
				showInsertAfterIndex = m.cursor
			} else {
				// Insert after cursor entry (and its children if any)
				showInsertAfterIndex = m.cursor
				// Skip children to find actual insert position
				visIdx := 0
				for i, e := range m.entries {
					if m.shouldShowEntry(e) {
						if visIdx == m.cursor {
							// Found cursor entry, now skip its children
							for j := i + 1; j < len(m.entries); j++ {
								if m.entries[j].Level <= cursorEntry.Level {
									break
								}
								if m.shouldShowEntry(m.entries[j]) {
									showInsertAfterIndex++
								}
							}
							break
						}
						visIdx++
					}
				}
			}
		}
	}

	// Second pass: render entries and insert indicator
	visibleIndex = 0
	for _, entry := range m.entries {
		if m.shouldShowEntry(entry) {
			// Render the entry
			line := m.renderEntry(entry, visibleIndex == m.cursor)
			b.WriteString(line + "\n")
			
			// Show insert indicator after this entry if needed
			if visibleIndex == showInsertAfterIndex {
				indent := strings.Repeat("  ", insertLevel)
				b.WriteString(m.styles.MoveMode.Render(fmt.Sprintf("%s↳ [Insert here]\n", indent)))
			}
			
			visibleIndex++
		}
	}
	
	// Show insert at end if cursor is at the last position
	if m.mode == ModeMove && m.moveSourceID != "" && m.cursor >= visibleIndex-1 && showInsertAfterIndex == -1 {
		cursorEntry := m.getEntryAtCursor()
		if cursorEntry != nil && cursorEntry.ID != m.moveSourceID {
			indent := strings.Repeat("  ", insertLevel)
			b.WriteString(m.styles.MoveMode.Render(fmt.Sprintf("%s↳ [Insert here]\n", indent)))
		}
	}

	return b.String()
}

// renderEntry renders a single entry
func (m Model) renderEntry(entry core.Entry, isCursor bool) string {
	indent := strings.Repeat("  ", entry.Level)
	
	var icon string
	var style lipgloss.Style

	switch entry.Type {
	case core.EntryTypeSection:
		icon = "##"
		style = m.styles.Section
	case core.EntryTypeItem:
		if entry.HasChildren {
			if m.expandedFolders[entry.ID] {
				icon = "▼"
			} else {
				icon = "▶"
			}
		} else {
			icon = "•"
		}
		style = m.styles.Normal
	}

	text := fmt.Sprintf("%s%s %s", indent, icon, entry.Title)

	// Add visual indicators
	if m.mode == ModeMove && entry.ID == m.moveSourceID {
		// Show the item being moved
		style = m.styles.Selected
		text = "[✂] " + text
	} else if isCursor {
		style = m.styles.Cursor
		text = "> " + text
	} else {
		text = "  " + text
	}

	return style.Render(text)
}

// renderStatusBar renders the status bar
func (m Model) renderStatusBar() string {
	var modeStr string
	switch m.mode {
	case ModeNormal:
		modeStr = "[Navigate]"
	case ModeMove:
		modeStr = "[Move]"
	case ModeRename:
		modeStr = "[Rename]"
	case ModeDelete:
		modeStr = "[Delete]"
	}

	left := fmt.Sprintf("%s %d unsaved changes", modeStr, m.unsavedChanges)
	
	var right string
	switch m.mode {
	case ModeMove:
		right = "[↑↓ Navigate] [→ Expand] [⏎ Confirm Move] [Esc Cancel]"
	case ModeRename:
		right = "[Type new name] [⏎ Confirm] [Esc Cancel]"
	case ModeDelete:
		right = "[y Confirm Delete] [n/Esc Cancel]"
	default:
		right = "[↑↓ Navigate] [Space Toggle] [⏎ Move] [r Rename] [d Delete] [^S Save] [q Quit]"
	}

	status := lipgloss.JoinHorizontal(
		lipgloss.Top,
		m.styles.StatusBar.Render(left),
		lipgloss.PlaceHorizontal(m.width-lipgloss.Width(left)-lipgloss.Width(right), lipgloss.Right, 
			m.styles.StatusBar.Render(right)),
	)

	return m.styles.StatusBar.Width(m.width).Render(status)
}

// shouldShowEntry determines if an entry should be visible
func (m Model) shouldShowEntry(entry core.Entry) bool {
	// Always show sections
	if entry.Type == core.EntryTypeSection {
		return true
	}

	// Always show top-level items
	if entry.Level == 0 {
		return true
	}

	// Check if parent is expanded
	// This is simplified - in real implementation, we'd need to check all parents
	for _, parent := range m.entries {
		if parent.HasChildren && parent.Level == entry.Level-1 {
			if parent.Index < entry.Index {
				return m.expandedFolders[parent.ID]
			}
		}
	}

	return true
}

// handleNormalMode handles key presses in normal mode
func (m Model) handleNormalMode(msg tea.KeyMsg) (tea.Model, tea.Cmd) {
	keys := DefaultKeyMap()

	switch {
	case key.Matches(msg, keys.Up):
		if m.cursor > 0 {
			m.cursor--
		}

	case key.Matches(msg, keys.Down):
		// Count visible entries
		visibleCount := 0
		for _, entry := range m.entries {
			if m.shouldShowEntry(entry) {
				visibleCount++
			}
		}
		if m.cursor < visibleCount-1 {
			m.cursor++
		}

	case key.Matches(msg, keys.Space):
		// Toggle expand/collapse
		entry := m.getEntryAtCursor()
		if entry != nil {
			if entry.HasChildren {
				m.expandedFolders[entry.ID] = !m.expandedFolders[entry.ID]
				m.successMsg = fmt.Sprintf("Toggled folder: %s (expanded: %v)", entry.Title, m.expandedFolders[entry.ID])
				// Force viewport update
				m.viewport.SetContent(m.renderTree())
			} else {
				m.successMsg = fmt.Sprintf("%s is not a folder", entry.Title)
			}
		} else {
			m.errorMsg = "No entry at cursor"
		}
	
	case key.Matches(msg, keys.Right):
		// Expand folder
		entry := m.getEntryAtCursor()
		if entry != nil && entry.HasChildren {
			m.expandedFolders[entry.ID] = true
			// Force viewport update
			m.viewport.SetContent(m.renderTree())
		}

	case key.Matches(msg, keys.Left):
		// Collapse
		entry := m.getEntryAtCursor()
		if entry != nil && entry.HasChildren {
			m.expandedFolders[entry.ID] = false
			// Force viewport update
			m.viewport.SetContent(m.renderTree())
		}

	case key.Matches(msg, keys.Enter):
		// Start move mode
		entry := m.getEntryAtCursor()
		if entry != nil && entry.Type == core.EntryTypeItem {
			m.mode = ModeMove
			m.moveSourceID = entry.ID
			m.selectedID = entry.ID
		}

	case key.Matches(msg, keys.Rename):
		// Start rename mode
		entry := m.getEntryAtCursor()
		if entry != nil && entry.Type == core.EntryTypeItem {
			m.mode = ModeRename
			m.selectedID = entry.ID
			m.textInput.SetValue(entry.Title)
			m.textInput.Focus()
			m.textInput.CursorEnd()
		}

	case key.Matches(msg, keys.Delete):
		// Start delete mode
		entry := m.getEntryAtCursor()
		if entry != nil && entry.Type == core.EntryTypeItem {
			m.mode = ModeDelete
			m.selectedID = entry.ID
		}

	case key.Matches(msg, keys.Save):
		// Save changes
		err := m.Save()
		if err != nil {
			m.errorMsg = err.Error()
		} else {
			m.successMsg = "Changes saved!"
			m.changesSummary = []string{}
		}

	case key.Matches(msg, keys.Help):
		// Toggle help
		// For now, just show a message
		m.successMsg = "Help: Use arrows to navigate, Space to expand/collapse, Enter to move, r to rename, d to delete, Ctrl+S to save, q to quit"

	case key.Matches(msg, keys.Quit):
		if m.unsavedChanges > 0 {
			// TODO: Confirm quit with unsaved changes
		}
		return m, tea.Quit
	
	case msg.String() == "?":
		// Alternative help handler for "?" key
		m.successMsg = "Help: Use arrows to navigate, Space to expand/collapse, Enter to move, r to rename, d to delete, Ctrl+S to save, q to quit"
	}

	return m, nil
}

// handleMoveMode handles key presses in move mode
func (m Model) handleMoveMode(msg tea.KeyMsg) (tea.Model, tea.Cmd) {
	switch msg.String() {
	case "esc":
		m.mode = ModeNormal
		m.selectedID = ""
		m.moveSourceID = ""

	case "enter":
		// Perform move
		sourceEntry := m.summaryManager.FindEntry(m.moveSourceID)
		if sourceEntry != nil {
			// Calculate target position and level
			targetPos, targetLevel := m.calculateMoveTarget()
			
			err := m.summaryManager.Move(m.moveSourceID, targetPos, targetLevel)
			if err != nil {
				m.errorMsg = err.Error()
			} else {
				// Track operation
				m.operations = append(m.operations, core.Operation{
					Type:        core.OpTypeMove,
					EntryID:     m.moveSourceID,
					OldPosition: sourceEntry.Index,
					NewPosition: targetPos,
					OldLevel:    sourceEntry.Level,
					NewLevel:    targetLevel,
				})
				m.entries = m.summaryManager.GetEntries()
				m.unsavedChanges++
				m.changesSummary = append(m.changesSummary, fmt.Sprintf("Moved '%s'", sourceEntry.Title))
				m.successMsg = "Entry moved!"
			}
		}
		m.mode = ModeNormal
		m.selectedID = ""
		m.moveSourceID = ""

	default:
		// Allow navigation in move mode
		return m.handleNormalMode(msg)
	}

	return m, nil
}

// handleRenameMode handles key presses in rename mode
func (m Model) handleRenameMode(msg tea.KeyMsg) (tea.Model, tea.Cmd) {
	var cmd tea.Cmd
	
	switch msg.Type {
	case tea.KeyEscape:
		m.mode = ModeNormal
		m.selectedID = ""
		m.textInput.Blur()
		return m, nil
		
	case tea.KeyEnter:
		newTitle := m.textInput.Value()
		if newTitle != "" && m.selectedID != "" {
			entry := m.summaryManager.FindEntry(m.selectedID)
			if entry != nil {
				// Generate new path from title
				newPath := m.generatePathFromTitle(newTitle, entry.Path)
				
				err := m.summaryManager.Rename(m.selectedID, newTitle, newPath)
				if err != nil {
					m.errorMsg = err.Error()
				} else {
					// Track operation
					m.operations = append(m.operations, core.Operation{
						Type:      core.OpTypeRename,
						EntryID:   m.selectedID,
						OldTitle:  entry.Title,
						NewTitle:  newTitle,
						OldPath:   entry.Path,
						NewPath:   newPath,
					})
					m.entries = m.summaryManager.GetEntries()
					m.unsavedChanges++
					m.changesSummary = append(m.changesSummary, fmt.Sprintf("Renamed '%s' to '%s'", entry.Title, newTitle))
					m.successMsg = "Entry renamed!"
				}
			}
		}
		m.mode = ModeNormal
		m.selectedID = ""
		m.textInput.Blur()
		return m, nil
	}
	
	// Update text input
	m.textInput, cmd = m.textInput.Update(msg)
	return m, cmd
}

// handleDeleteMode handles key presses in delete mode
func (m Model) handleDeleteMode(msg tea.KeyMsg) (tea.Model, tea.Cmd) {
	switch msg.String() {
	case "y":
		// Confirm delete
		entry := m.summaryManager.FindEntry(m.selectedID)
		if entry != nil {
			err := m.summaryManager.Delete(m.selectedID)
			if err != nil {
				m.errorMsg = err.Error()
			} else {
				// Track operation
				m.operations = append(m.operations, core.Operation{
					Type:    core.OpTypeDelete,
					EntryID: m.selectedID,
					OldPath: entry.Path,
				})
				m.entries = m.summaryManager.GetEntries()
				m.unsavedChanges++
				m.changesSummary = append(m.changesSummary, fmt.Sprintf("Deleted '%s'", entry.Title))
				m.successMsg = "Entry deleted!"
			}
		}
		m.mode = ModeNormal
		m.selectedID = ""

	case "n", "esc":
		// Cancel delete
		m.mode = ModeNormal
		m.selectedID = ""
	}
	return m, nil
}

// getEntryAtCursor returns the entry at the current cursor position
func (m *Model) getEntryAtCursor() *core.Entry {
	visibleIndex := 0
	for i, entry := range m.entries {
		if m.shouldShowEntry(entry) {
			if visibleIndex == m.cursor {
				return &m.entries[i]
			}
			visibleIndex++
		}
	}
	return nil
}

// generatePathFromTitle generates a file path from a title
func (m Model) generatePathFromTitle(title, oldPath string) string {
	// Simple implementation: lowercase, replace spaces with hyphens
	baseName := strings.ToLower(title)
	baseName = strings.ReplaceAll(baseName, " ", "-")
	baseName = strings.ReplaceAll(baseName, "/", "-")
	
	// Preserve directory structure
	dir := ""
	if idx := strings.LastIndex(oldPath, "/"); idx >= 0 {
		dir = oldPath[:idx+1]
	}
	
	// Preserve extension or add .md
	ext := ".md"
	if strings.HasSuffix(oldPath, ".md") {
		ext = ".md"
	} else if idx := strings.LastIndex(oldPath, "."); idx >= 0 {
		ext = oldPath[idx:]
	}
	
	return dir + baseName + ext
}

// refreshEntries updates the entries list from the summary manager
func (m *Model) refreshEntries() {
	m.entries = m.summaryManager.GetEntries()
}

// calculateMoveTarget calculates the target position and level for move operation
func (m Model) calculateMoveTarget() (int, int) {
	// Get the entry at cursor position
	cursorEntry := m.getEntryAtCursor()
	if cursorEntry == nil {
		// If no entry at cursor, insert at the end
		return len(m.entries), 0
	}
	
	// Default to same level as cursor entry
	targetLevel := cursorEntry.Level
	targetPos := cursorEntry.Index
	
	// If cursor is on a folder that's expanded, insert as first child
	if cursorEntry.HasChildren && m.expandedFolders[cursorEntry.ID] {
		// Insert as first child of the folder
		targetPos = cursorEntry.Index + 1
		targetLevel = cursorEntry.Level + 1
	} else {
		// Insert after the cursor entry at the same level
		targetPos = cursorEntry.Index + 1
		
		// If cursor entry has children, skip all children
		if cursorEntry.HasChildren {
			// Find the next entry at the same or lower level
			for i := cursorEntry.Index + 1; i < len(m.entries); i++ {
				if m.entries[i].Level <= cursorEntry.Level {
					targetPos = i
					break
				}
				targetPos = i + 1
			}
		}
	}
	
	return targetPos, targetLevel
}

// renderInfoPanel renders the right panel with info and changes
func (m Model) renderInfoPanel() string {
	var b strings.Builder
	
	// Current entry info
	if entry := m.getEntryAtCursor(); entry != nil {
		b.WriteString(m.styles.Section.Render("Current Entry") + "\n\n")
		b.WriteString(fmt.Sprintf("Title: %s\n", entry.Title))
		if entry.Path != "" {
			b.WriteString(fmt.Sprintf("Path: %s\n", entry.Path))
		}
		b.WriteString(fmt.Sprintf("Level: %d\n", entry.Level))
		if entry.HasChildren {
			b.WriteString("Type: Folder\n")
		} else {
			b.WriteString("Type: Page\n")
		}
		b.WriteString("\n")
	}
	
	// Mode info
	if m.mode == ModeMove && m.moveSourceID != "" {
		b.WriteString(m.styles.Section.Render("Move Operation") + "\n\n")
		if sourceEntry := m.summaryManager.FindEntry(m.moveSourceID); sourceEntry != nil {
			b.WriteString(fmt.Sprintf("Moving: %s\n", sourceEntry.Title))
			b.WriteString("\n")
			b.WriteString("Target position:\n")
			if entry := m.getEntryAtCursor(); entry != nil {
				if entry.HasChildren && m.expandedFolders[entry.ID] {
					b.WriteString(fmt.Sprintf("  → Inside '%s' (as first child)\n", entry.Title))
				} else {
					b.WriteString(fmt.Sprintf("  → After '%s'\n", entry.Title))
				}
			}
		}
		b.WriteString("\n")
	}
	
	// Unsaved changes
	if m.unsavedChanges > 0 {
		b.WriteString(m.styles.Section.Render(fmt.Sprintf("Unsaved Changes (%d)", m.unsavedChanges)) + "\n\n")
		for i := len(m.changesSummary) - 1; i >= 0 && i >= len(m.changesSummary)-5; i-- {
			b.WriteString("• " + m.changesSummary[i] + "\n")
		}
		if len(m.changesSummary) > 5 {
			b.WriteString(fmt.Sprintf("  ... and %d more\n", len(m.changesSummary)-5))
		}
	}
	
	// Error/Success messages
	if m.errorMsg != "" {
		b.WriteString("\n" + m.styles.Error.Render("Error: "+m.errorMsg) + "\n")
	}
	if m.successMsg != "" {
		b.WriteString("\n" + m.styles.Success.Render(m.successMsg) + "\n")
	}
	
	return b.String()
}