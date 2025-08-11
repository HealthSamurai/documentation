#!/bin/bash

# Script to reset documentation to git state
# Removes untracked files and restores modified files

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Find git root directory
GIT_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
if [ -z "$GIT_ROOT" ]; then
    echo -e "${RED}Error: Not in a git repository${NC}"
    exit 1
fi

# Change to git root directory
cd "$GIT_ROOT"

echo -e "${YELLOW}Resetting documentation to git state in $GIT_ROOT...${NC}"

# Function to reset a directory
reset_directory() {
    local dir=$1
    
    if [ -d "$dir" ]; then
        echo -e "${GREEN}Resetting $dir...${NC}"
        
        # Remove untracked files in the directory
        echo "  Removing untracked files..."
        git clean -fd "$dir" 2>/dev/null || true
        
        # Restore modified files
        echo "  Restoring modified files..."
        git checkout -- "$dir" 2>/dev/null || true
    else
        echo -e "${YELLOW}Directory $dir does not exist, skipping...${NC}"
    fi
}

# Function to reset a file
reset_file() {
    local file=$1
    
    if [ -f "$file" ]; then
        echo -e "${GREEN}Resetting $file...${NC}"
        git checkout -- "$file" 2>/dev/null || true
    elif git ls-files --error-unmatch "$file" > /dev/null 2>&1; then
        # File exists in git but not on disk, restore it
        echo -e "${GREEN}Restoring deleted file $file...${NC}"
        git checkout -- "$file" 2>/dev/null || true
    fi
}

# Reset docs directory
reset_directory "docs"

# Reset test-docs if it exists (remove it as it's untracked)
if [ -d "test-docs" ]; then
    echo -e "${YELLOW}Removing untracked test-docs directory...${NC}"
    rm -rf test-docs
fi

# Reset .gitbook.yaml with correct structure
echo -e "${GREEN}Restoring .gitbook.yaml...${NC}"
if git ls-files --error-unmatch ".gitbook.yaml" > /dev/null 2>&1; then
    # File exists in git, restore it
    git checkout -- .gitbook.yaml
else
    # File doesn't exist in git, create default one
    cat > .gitbook.yaml << 'EOF'
root: ./docs/
structure:
  readme: readme/README.md
  summary: SUMMARY.md
redirects: {}
EOF
    echo -e "${YELLOW}Created default .gitbook.yaml${NC}"
fi

# Remove any f2 binary if it exists
if [ -f "f2" ]; then
    echo -e "${YELLOW}Removing f2 binary...${NC}"
    rm -f f2
fi

# Show git status
echo -e "\n${GREEN}Current git status:${NC}"
git status --short

echo -e "\n${GREEN}✓ Documentation reset complete!${NC}"

# Optionally show what was changed
if [ "$1" == "--verbose" ] || [ "$1" == "-v" ]; then
    echo -e "\n${YELLOW}Detailed changes:${NC}"
    git diff --stat
fi