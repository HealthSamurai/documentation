#!/bin/bash

# Pre-push validation script
# Runs all documentation checks before pushing

# Show hint on error
trap 'echo ""; echo "Tip: Run \"make init\" locally to install pre-push hook with these checks."' ERR

echo "=== Pre-push Validation ==="

python3 ./scripts/check_frontmatter_yaml.py || exit 1
python3 ./scripts/check_h1_headers.py || exit 1
python3 ./scripts/check_empty_headers.py || exit 1
python3 ./scripts/check_image_alt.py || exit 1
python3 ./scripts/markdown-links/find_absolute_aidbox_links.py || exit 1
python3 ./scripts/markdown-links/extract-nonexistent-links.py || exit 1
python3 ./scripts/summary/check-summary-vs-files.py || exit 1
python3 ./scripts/check-title-mismatch.py || exit 1
python3 ./scripts/check-ampersand-in-summary.py || exit 1
python3 ./scripts/check_deprecated_links.py || exit 1

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

python3 ./scripts/redirects.py || exit 1
python3 ./scripts/pictures/check_missing_images.py || exit 1
python3 ./scripts/blog/validate_articles.py || exit 1

echo ""
echo "=== All checks passed ==="
