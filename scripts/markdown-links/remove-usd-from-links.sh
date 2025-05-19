#!/bin/bash

# This script removes 'usd' segments from all markdown links in documentation files.
# It processes every .md file in the docs directory and uses perl regex to remove 'usd'
# only from inside markdown link syntax ([text](url)) while preserving the rest of the URL.

# Recursively remove all 'usd' from any part of markdown links in all .md files in the docs directory
find docs/ -type f -name '*.md' | while read file; do
  # Use perl for in-place regex replacement only inside markdown links
  perl -pi -e 's/(\[[^\]]*\]\([^\)]*)usd([\/]?)/$1$2/g' "$file"
done

echo "All 'usd' removed from any part of markdown links in all markdown files in docs/"
