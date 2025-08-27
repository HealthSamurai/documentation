#!/bin/bash

# Script to check external links in documentation locally
# This is a wrapper around the Python script with user-friendly features

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
PYTHON_SCRIPT="$SCRIPT_DIR/check-external-links.py"

# Default values
DOCS_DIR="$PROJECT_ROOT/docs"
OUTPUT_FILE=""
VERBOSE=false
JSON_OUTPUT=false
WORKERS=10
TIMEOUT=10

# Function to print colored output
print_color() {
    local color=$1
    shift
    echo -e "${color}$*${NC}"
}

# Function to check if Python is installed
check_python() {
    if ! command -v python3 &> /dev/null; then
        print_color "$RED" "Error: Python 3 is not installed"
        print_color "$YELLOW" "Please install Python 3 to use this script"
        exit 1
    fi
}

# Function to check if requests library is installed
check_requests() {
    if ! python3 -c "import requests" 2>/dev/null; then
        print_color "$YELLOW" "The 'requests' library is not installed."
        read -p "Would you like to install it now? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            print_color "$BLUE" "Installing requests..."
            pip3 install --user requests || {
                print_color "$RED" "Failed to install requests. Please install manually: pip3 install requests"
                exit 1
            }
            print_color "$GREEN" "✓ requests library installed successfully"
        else
            print_color "$RED" "Cannot proceed without the requests library"
            exit 1
        fi
    fi
}

# Function to show usage
usage() {
    cat << EOF
Usage: $(basename "$0") [OPTIONS]

Check external HTTPS links in documentation for availability.

OPTIONS:
    -d, --docs-dir DIR     Directory containing markdown files (default: docs)
    -o, --output FILE      Save output to file
    -v, --verbose          Show all checked links, not just broken ones
    -j, --json             Output in JSON format
    -w, --workers NUM      Maximum concurrent requests (default: 10)
    -t, --timeout SEC      Request timeout in seconds (default: 10)
    -h, --help             Show this help message

EXAMPLES:
    # Basic check
    $(basename "$0")
    
    # Check with verbose output and save to file
    $(basename "$0") -v -o broken-links.txt
    
    # Check with more concurrent workers and longer timeout
    $(basename "$0") -w 20 -t 15
    
    # Output as JSON for processing by other tools
    $(basename "$0") --json -o links-report.json

EOF
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -d|--docs-dir)
            DOCS_DIR="$2"
            shift 2
            ;;
        -o|--output)
            OUTPUT_FILE="$2"
            shift 2
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        -j|--json)
            JSON_OUTPUT=true
            shift
            ;;
        -w|--workers)
            WORKERS="$2"
            shift 2
            ;;
        -t|--timeout)
            TIMEOUT="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            print_color "$RED" "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

# Main execution
print_color "$BLUE" "=========================================="
print_color "$BLUE" "  External Link Checker for Documentation"
print_color "$BLUE" "=========================================="
echo

# Check prerequisites
check_python
check_requests

# Check if docs directory exists
if [ ! -d "$DOCS_DIR" ]; then
    print_color "$RED" "Error: Documentation directory '$DOCS_DIR' does not exist"
    exit 1
fi

# Check if Python script exists
if [ ! -f "$PYTHON_SCRIPT" ]; then
    print_color "$RED" "Error: Python script not found at $PYTHON_SCRIPT"
    exit 1
fi

# Build the command
CMD="python3 $PYTHON_SCRIPT --docs-dir \"$DOCS_DIR\" --workers $WORKERS --timeout $TIMEOUT"

if [ "$VERBOSE" = true ]; then
    CMD="$CMD --verbose"
fi

if [ "$JSON_OUTPUT" = true ]; then
    CMD="$CMD --output json"
fi

# Run the link checker
print_color "$BLUE" "Starting link check..."
print_color "$BLUE" "Configuration:"
echo "  - Docs directory: $DOCS_DIR"
echo "  - Workers: $WORKERS"
echo "  - Timeout: ${TIMEOUT}s"
if [ "$VERBOSE" = true ]; then
    echo "  - Mode: Verbose"
fi
if [ "$JSON_OUTPUT" = true ]; then
    echo "  - Format: JSON"
fi
echo

# Execute the command
if [ -n "$OUTPUT_FILE" ]; then
    print_color "$BLUE" "Running link checker (output will be saved to $OUTPUT_FILE)..."
    eval $CMD > "$OUTPUT_FILE" 2>&1
    EXIT_CODE=$?
    
    # Also display to console if not JSON
    if [ "$JSON_OUTPUT" = false ]; then
        cat "$OUTPUT_FILE"
    fi
    
    if [ $EXIT_CODE -eq 0 ]; then
        print_color "$GREEN" "✓ Link check completed successfully!"
        print_color "$GREEN" "  Results saved to: $OUTPUT_FILE"
    else
        print_color "$RED" "✗ Broken links found!"
        print_color "$RED" "  Details saved to: $OUTPUT_FILE"
    fi
else
    eval $CMD
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ]; then
        print_color "$GREEN" "✓ Link check completed successfully!"
    else
        print_color "$RED" "✗ Broken links found!"
    fi
fi

exit $EXIT_CODE