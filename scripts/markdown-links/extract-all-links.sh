#!/bin/bash

# This script extracts all links from Markdown files in the docs directory.
# It finds both Markdown links [text](link) and HTML img src attributes.
# Results are sorted by number of links per file and output to out/all_links_by_file.txt.
# Release notes and deprecated files are put at the end of the output.

mkdir -p out

declare -A file_links

declare -a files

while IFS= read -r -d '' file; do
  # Combine markdown links and HTML src attributes, sort and remove duplicates
  links=$(
    {
      grep -oP '\[[^\]]+\]\([^\)]+\)' "$file"
      grep -oP 'src="[^"]+"' "$file" | sed 's/src="\([^"]*\)".*/  src="\1"/'
    } | sort | uniq
  )
  count=$(echo "$links" | grep -c .)
  if [ "$count" -gt 0 ]; then
    file_links["$file"]="$links"
    files+=("$file:$count")
  fi
done < <(find docs -type f -name '*.md' -print0)

sorted_files=$(printf '%s\n' "${files[@]}" | sort -t: -k2,2nr)

last_files=()
main_files=()
for entry in $sorted_files; do
  file="${entry%%:*}"
  count="${entry##*:}"
  if [[ "$file" == "docs/overview/release-notes.md" ]] || \
     [[ "$file" == docs/deprecated/* ]]; then
    last_files+=("$file:$count")
  else
    main_files+=("$file:$count")
  fi
done

{
  for entry in "${main_files[@]}" "${last_files[@]}"; do
    file="${entry%%:*}"
    count="${entry##*:}"
    [ -z "$file" ] && continue
    echo "$file ($count links):"
    echo "${file_links[$file]}" | sed 's/^/  /'
    echo
  done
} > out/all_links_by_file.txt

echo 'Generated out/all_links_by_file.txt'
