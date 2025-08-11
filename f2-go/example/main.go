package main

import (
	"fmt"
	"log"
	"os"

	tea "github.com/charmbracelet/bubbletea"
	"github.com/f2-go/internal/core"
	"github.com/f2-go/internal/parser"
	"github.com/f2-go/internal/ui"
	"github.com/f2-go/internal/utils"
)

func main() {
	// Check if a directory was provided
	var dir string
	if len(os.Args) > 1 {
		dir = os.Args[1]
	} else {
		dir = "."
	}

	// Create file system
	fs := utils.NewOsFileSystem()

	// Read SUMMARY.md
	summaryPath := dir + "/SUMMARY.md"
	summaryContent, err := fs.ReadFile(summaryPath)
	if err != nil {
		log.Fatal("Failed to read SUMMARY.md:", err)
	}

	// Parse SUMMARY.md
	summaryParser := core.NewSummaryParser()
	summary, err := summaryParser.Parse(summaryContent)
	if err != nil {
		log.Fatal("Failed to parse SUMMARY.md:", err)
	}

	// Create managers
	summaryManager := core.NewSummaryManager(summary)
	redirectManager := core.NewRedirectManager()
	linkUpdater := parser.NewLinkParser()

	// Load existing redirects if available
	gitbookPath := dir + "/.gitbook.yaml"
	if gitbookContent, err := fs.ReadFile(gitbookPath); err == nil {
		_, err = redirectManager.LoadRedirects(gitbookContent)
		if err != nil {
			log.Printf("Warning: Failed to load redirects: %v", err)
		}
	}

	// Create UI model
	model := ui.New(summaryManager, redirectManager, linkUpdater, fs, dir)

	// Run the TUI
	p := tea.NewProgram(model, tea.WithAltScreen())
	if _, err := p.Run(); err != nil {
		fmt.Printf("Error running program: %v", err)
		os.Exit(1)
	}
}