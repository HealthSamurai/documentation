#!/bin/bash

# Pre-push validation script
# Runs all documentation checks before pushing

# Show hint on error
trap 'echo ""; echo "Tip: Run \"make init\" locally to install pre-push hook with these checks."' ERR

# Use Python from venv if available, otherwise use system Python
if [ -f "scripts/venv/bin/python3" ]; then
    PYTHON="scripts/venv/bin/python3"
else
    PYTHON="python3"
    # Check if required packages are available
    if ! $PYTHON -c "import yaml" 2>/dev/null; then
        echo "❌ Error: Python package 'pyyaml' not found"
        echo ""
        echo "To fix this, run one of:"
        echo "  1. make install-deps       (creates local venv in scripts/venv/)"
        echo "  2. pip install pyyaml      (if you have permissions)"
        echo "  3. Use asdf/pyenv/homebrew to install non-system Python"
        echo ""
        exit 1
    fi
fi

echo "=== Pre-push Validation ==="

$PYTHON ./scripts/check_frontmatter_yaml.py || exit 1
$PYTHON ./scripts/check_h1_headers.py || exit 1
$PYTHON ./scripts/check_empty_headers.py || exit 1
$PYTHON ./scripts/check_image_alt.py || exit 1
$PYTHON ./scripts/markdown-links/find_absolute_aidbox_links.py || exit 1
$PYTHON ./scripts/markdown-links/extract-nonexistent-links.py || exit 1
$PYTHON ./scripts/summary/check-summary-vs-files.py || exit 1
$PYTHON ./scripts/check-title-mismatch.py || exit 1
$PYTHON ./scripts/check-ampersand-in-summary.py || exit 1
$PYTHON ./scripts/check_deprecated_links.py || exit 1
$PYTHON ./scripts/check_orphan_pages.py || exit 1

# Check for broken-reference links (inline check)
echo ""
echo "[check] Broken References"
FILES_COUNT=$(find docs/ -name "*.md" -not -path "*/deprecated/*" | wc -l | tr -d ' ')
if grep -rn "\[.*\](broken-reference" docs/ --include="*.md" --exclude-dir="deprecated" > /dev/null 2>&1; then
    echo "        ✗ Found broken-reference links"
    grep -rn "\[.*\](broken-reference" docs/ --include="*.md" --exclude-dir="deprecated" | head -5 | while read line; do
        echo "          - $line"
    done
    exit 1
fi
echo "        ✓ ${FILES_COUNT} files checked, no broken-reference links"

$PYTHON ./scripts/redirects.py || exit 1
$PYTHON ./scripts/pictures/check_missing_images.py || exit 1
$PYTHON ./scripts/blog/validate_articles.py || exit 1

echo ""
echo "=== All checks passed ==="
