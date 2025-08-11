package main

import (
	"bufio"
	"fmt"
	"strconv"
	"strings"
)

// Simple position selector that works reliably without terminal magic
func (c *CLI) selectPositionSimple(positions []InsertPosition, targetSection string) (int, error) {
	if len(positions) == 0 {
		return -1, fmt.Errorf("no positions available")
	}
	
	// Display header
	fmt.Fprintln(c.stdout)
	if targetSection != "" {
		fmt.Fprintf(c.stdout, "Select position for insertion in '%s' section:\n", targetSection)
	} else {
		fmt.Fprintln(c.stdout, "Select position for insertion:")
	}
	fmt.Fprintln(c.stdout, "----------------------------------------")
	
	// Display positions with clear numbering
	for i, pos := range positions {
		num := fmt.Sprintf("%2d", i+1)
		
		if pos.IsEndOption {
			fmt.Fprintf(c.stdout, "%s. → [%s]\n", num, pos.Label)
		} else {
			// Show with arrow to indicate insertion point
			fmt.Fprintf(c.stdout, "%s. → %s\n", num, pos.Label)
		}
	}
	
	fmt.Fprintln(c.stdout, "----------------------------------------")
	fmt.Fprint(c.stdout, "Enter position number (or 'q' to cancel): ")
	
	// Read user input
	scanner := bufio.NewScanner(c.stdin)
	if !scanner.Scan() {
		return -1, fmt.Errorf("no position selected")
	}
	
	input := strings.TrimSpace(scanner.Text())
	if input == "q" || input == "Q" {
		return -1, fmt.Errorf("selection cancelled")
	}
	
	num, err := strconv.Atoi(input)
	if err != nil || num < 1 || num > len(positions) {
		return -1, fmt.Errorf("invalid position number")
	}
	
	return positions[num-1].Index, nil
}