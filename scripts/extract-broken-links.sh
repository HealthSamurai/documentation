#!/bin/bash

mkdir -p out

bash ./scripts/extract-all-links.sh || exit 1
echo -e '\nGenerating out/all_broken_links_by_file.txt'
awk '/^[^ ].*links\):$/ { if (block && found) print block; block = $0 ORS; found = 0; next } /^  \[.*broken-reference.*\)/ { block = block $0 ORS; found = 1 } END { if (block && found) print block }' out/all_links_by_file.txt > out/all_broken_links_by_file.txt
echo 'Generated out/all_broken_links_by_file.txt'
