#!/bin/bash

# This script checks for missing image asset files referenced in documentation.
# It searches for all asset references in image src attributes across all markdown files,
# skipping the deprecated directory, and verifies that the referenced files exist.
# Missing images are reported with their full path and the file that references them.

# Find all links to assets in all files except the deprecated folder
grep -r -o "src=\"[^\"]*assets[^\"]*\"" docs/ | grep -v "docs/deprecated" | sed 's/.*src="\([^"]*\)".*/\1/' | while read -r line; do
  # Get the file with the link and the link itself
  file=$(echo "$line" | cut -d: -f1)
  link=$(echo "$line" | cut -d: -f2-)
  
  # Get the directory of the file
  dir=$(dirname "$file")
  
  # Build the full path
  full_path="$dir/$link"
  
  # Check if the file exists
  if [[ ! -e "$full_path" ]]; then
    # Also try to resolve the path through realpath
    resolved_path=$(realpath --no-symlinks --relative-to=. "$full_path" 2>/dev/null || echo "INVALID_PATH")
    if [[ "$resolved_path" == "INVALID_PATH" || ! -e "$resolved_path" ]]; then
      echo "ERROR: Missing file: $full_path"
      echo "  Referenced in: $file"
      echo "  Link: $link"
      echo "---"
    fi
  fi
done 