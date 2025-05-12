#!/bin/bash

# Recursively remove all 'usd' from any part of markdown links in all .md files in the docs directory
find docs/ -type f -name '*.md' | while read file; do
  # Use perl for in-place regex replacement only inside markdown links
  perl -pi -e 's/(\[[^\]]*\]\([^\)]*)usd([\/]?)/$1$2/g' "$file"
done

echo "All 'usd' removed from any part of markdown links in all markdown files in docs/"
