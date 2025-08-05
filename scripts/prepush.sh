#!/bin/bash

# This script runs various validation checks before code is pushed, ensuring:
# - No absolute Aidbox links are present
# - No broken links with "broken-reference" are present
# - All relative links point to existing files
# - Summary files are in sync with actual files
# - Titles in SUMMARY.md match h1 headers in files
# - No titles in SUMMARY.md contain " & " (should use " and " instead)

python3 ./scripts/markdown-links/find_absolute_aidbox_links.py || exit 1
python3 ./scripts/markdown-links/extract-nonexistent-links.py || exit 1
python3 ./scripts/summary/check-summary-vs-files.py || exit 1
python3 ./scripts/check-title-mismatch.py || exit 1
python3 ./scripts/check-ampersand-in-summary.py || exit 1

# Check for broken-reference links in markdown files
echo "Checking for broken-reference links..."
if grep -rn "\[.*\](broken-reference" docs/ --include="*.md" --exclude-dir="deprecated"; then
    echo "ERROR: Found markdown links starting with 'broken-reference'"
    echo "Please fix these broken references before pushing"
    exit 1
fi

python3 ./scripts/redirects.py || exit 1

# Check title case (warning only, doesn't block)
echo "Checking title case in SUMMARY.md files..."
python3 ./scripts/check-title-case-in-summary.py || true

# maybe later...
# python ./scripts/pictures/image_analyzer.py || exit 1
