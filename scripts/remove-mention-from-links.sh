#!/bin/bash

find docs/ -type f -name '*.md' | while read file; do
  sed -i -E 's/ "mention"//g' "$file"
done

echo "All 'mention' attributes removed from links in docs/"