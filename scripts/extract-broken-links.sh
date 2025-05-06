#!/bin/bash

mkdir -p out

awk '/^[^ ].*links\):$/ { if (block && found) print block; block = $0 ORS; found = 0; next } /^  \[.*broken-reference.*\)/ { block = block $0 ORS; found = 1 } END { if (block && found) print block }' out/all_links_by_file.txt > out/all_broken_links_by_file.txt
