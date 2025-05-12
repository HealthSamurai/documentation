#!/bin/bash

mkdir -p out
OUTPUT="out/absolute_aidbox_links.txt"

{
  echo "File | Count | Lines with links"
  echo "-----|-------|-----------------"
  grep -r --include='*.md' 'https://docs.aidbox.app' docs/ \
    | awk -F: '{print $1}' \
    | sort \
    | uniq -c \
    | sort -rn \
    | while read count file; do
        echo -n "$file | $count | "
        grep -n --color=never 'https://docs.aidbox.app' "$file" | awk -F: '{print $1}' | paste -sd, -
      done
} > "$OUTPUT"

cat "$OUTPUT"

count=$(grep -c -v -e '^File ' -e '^-----' "$OUTPUT")
if [ "$count" -gt 0 ]; then
  echo -e "\nAbsolute links to https://docs.aidbox.app found. Please fix them before pushing!\nUse `bash scripts/replace_absolute_aidbox_links.sh`"
  exit 42
else
  echo -e "\nNo absolute links found."
  exit 0
fi
