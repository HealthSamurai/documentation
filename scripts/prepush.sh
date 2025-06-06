#!/bin/bash

# This script runs various validation checks before code is pushed, ensuring:
# - No absolute Aidbox links are present
# - No broken links with "broken-reference" are present
# - All relative links point to existing files
# - Summary files are in sync with actual files

python ./scripts/markdown-links/find_absolute_aidbox_links.py || exit 1
# bash ./scripts/markdown-links/extract-broken-links.sh || exit 1
# bash ./scripts/markdown-links/extract-nonexistent-links.sh || exit 1
python ./scripts/markdown-links/extract-nonexistent-links.py || exit 1
bash ./scripts/summary/check-summary-vs-files.sh
python ./scripts/pictures/image_analyzer.py || exit 1
