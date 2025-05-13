#!/bin/bash

echo 'Checking that we do not use absolute reference https://docs.aidbox.app/ in docs/ directory'
mkdir -p out
OUTPUT="out/absolute_aidbox_links.txt"

# Only search for absolute links in markdown files outside docs/reference
find docs/ -type f -name '*.md' ! -path 'docs/reference/*' | xargs grep -l 'https://docs.aidbox.app' | while read file; do
  count=$(grep -c 'https://docs.aidbox.app' "$file")
  lines=$(grep -n --color=never 'https://docs.aidbox.app' "$file" | awk -F: '{print $1}' | paste -sd, -)
  echo "$file | $count | $lines"
done | (
  echo "File | Count | Lines with links"
  echo "-----|-------|-----------------"
  cat
) > "$OUTPUT"

cat "$OUTPUT"

count=$(grep -c -v -e '^File ' -e '^-----' "$OUTPUT")
if [ "$count" -gt 0 ]; then
  echo -e "\nAbsolute links to https://docs.aidbox.app found. Please fix them before pushing!\nUse bash scripts/replace_absolute_aidbox_links.sh"
  exit 42
else
  echo -e "\nNo absolute links found (except /reference directory)."
  exit 0
fi
