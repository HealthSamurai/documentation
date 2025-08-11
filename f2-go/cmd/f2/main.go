package main

import (
	"fmt"
	"os"
)

// osExit is a variable to allow mocking in tests
var osExit = os.Exit

func main() {
	cli := NewCLI()
	
	// Handle TUI mode separately
	if len(os.Args) < 2 {
		runTUI()
		return
	}
	
	// Check if first arg is a path (for TUI with custom path)
	if _, err := os.Stat(os.Args[1]); err == nil && len(os.Args) == 2 {
		runTUIWithPath(os.Args[1])
		return
	}
	
	// Execute CLI command
	if err := cli.Execute(os.Args); err != nil {
		fmt.Fprintf(os.Stderr, "Error: %v\n", err)
		osExit(1)
	}
}

func runTUI() {
	runTUIWithPath("docs")
}

func runTUIWithPath(docsPath string) {
	// For TUI mode, we need to initialize the model differently
	// This is a simplified version - the actual implementation would need more setup
	fmt.Println("TUI mode is not available in this refactored version")
	osExit(1)
}