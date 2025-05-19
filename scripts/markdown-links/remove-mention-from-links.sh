#!/bin/bash

# This script removes all 'mention' attributes from links in markdown files.
# It processes every .md file in the docs directory and removes the ' "mention"' text.

find docs/ -type f -name '*.md' | while read file; do
  sed -i -E 's/ "mention"//g' "$file"
done

echo "All 'mention' attributes removed from links in docs/"