#!/bin/bash

# This script identifies links containing "broken-reference" in the documentation.
# It first extracts all links using extract-all-links.sh, then uses awk to filter out
# broken references and outputs them to out/all_broken_links_by_file.txt.

mkdir -p out

bash ./scripts/markdown-links/extract-all-links.sh || exit 1
echo -e '\nGenerating out/all_broken_links_by_file.txt'
awk '/^[^ ].*links\):$/ { if (block && found) print block; block = $0 ORS; found = 0; next } /^  \[.*broken-reference.*\)/ || /^  src=".*broken-reference.*"/ { block = block $0 ORS; found = 1 } END { if (block && found) print block }' out/all_links_by_file.txt > out/all_broken_links_by_file.txt
echo 'Generated out/all_broken_links_by_file.txt'
