package main

import (
	"bytes"
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
)

// Test the main function
func TestMain(t *testing.T) {
	// Test that main delegates to CLI.Execute
	// The actual CLI logic is tested elsewhere
	t.Run("main delegates to CLI", func(t *testing.T) {
		// This is a simple smoke test
		// The real testing happens in TestCLI_Execute
		assert.NotNil(t, NewCLI())
	})
}

// Test runTUI function
func TestRunTUI(t *testing.T) {
	// Save and mock stdout
	oldStdout := os.Stdout
	r, w, _ := os.Pipe()
	os.Stdout = w
	
	// Save and mock os.Exit
	oldExit := osExit
	var exitCode int
	osExit = func(code int) {
		exitCode = code
		panic("os.Exit called")
	}
	
	defer func() {
		os.Stdout = oldStdout
		osExit = oldExit
	}()

	// Test should panic with os.Exit
	assert.Panics(t, func() {
		runTUI()
	})

	// Close writer and read output
	w.Close()
	buf := new(bytes.Buffer)
	buf.ReadFrom(r)
	output := buf.String()

	assert.Contains(t, output, "TUI mode is not available")
	assert.Equal(t, 1, exitCode)
}

// Test runTUIWithPath function
func TestRunTUIWithPath(t *testing.T) {
	// Save and mock stdout
	oldStdout := os.Stdout
	r, w, _ := os.Pipe()
	os.Stdout = w
	
	// Save and mock os.Exit
	oldExit := osExit
	var exitCode int
	osExit = func(code int) {
		exitCode = code
		panic("os.Exit called")
	}
	
	defer func() {
		os.Stdout = oldStdout
		osExit = oldExit
	}()

	// Test should panic with os.Exit
	assert.Panics(t, func() {
		runTUIWithPath("custom/path")
	})

	// Close writer and read output
	w.Close()
	buf := new(bytes.Buffer)
	buf.ReadFrom(r)
	output := buf.String()

	assert.Contains(t, output, "TUI mode is not available")
	assert.Equal(t, 1, exitCode)
}

// Helper functions for setting up test environment
func setupTestEnvironment() {
	// Create test docs directory
	os.MkdirAll("docs/getting-started", 0755)
	
	// Create SUMMARY.md
	summaryContent := `# Table of contents

* [Getting Started](getting-started/README.md)
  * [Quick Start](getting-started/quick-start.md)
`
	os.WriteFile("docs/SUMMARY.md", []byte(summaryContent), 0644)
	
	// Create test file
	os.WriteFile("docs/getting-started/quick-start.md", []byte("# Quick Start\n"), 0644)
	
	// Create .gitbook.yaml
	os.WriteFile(".gitbook.yaml", []byte("redirects:\n"), 0644)
}

func cleanupTestEnvironment() {
	os.RemoveAll("docs")
	os.Remove(".gitbook.yaml")
}