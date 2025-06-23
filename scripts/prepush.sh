#!/bin/bash

# This script runs various validation checks before code is pushed, ensuring:
# - No absolute Aidbox links are present
# - No broken links with "broken-reference" are present
# - All relative links point to existing files
# - Summary files are in sync with actual files

python ./scripts/markdown-links/find_absolute_aidbox_links.py || exit 1
python ./scripts/markdown-links/extract-nonexistent-links.py || exit 1
python ./scripts/summary/check-summary-vs-files.py || exit 1

# Check for broken-reference links in markdown files
echo "Checking for broken-reference links..."
if grep -rn "\[.*\](broken-reference" docs/ --include="*.md" --exclude-dir="deprecated"; then
    echo "ERROR: Found markdown links starting with 'broken-reference'"
    echo "Please fix these broken references before pushing"
    exit 1
fi

python ./scripts/redirects.py || exit 1
# maybe later...
# python ./scripts/pictures/image_analyzer.py || exit 1
