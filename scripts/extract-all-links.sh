#!/bin/bash

mkdir -p out

declare -A file_links

declare -a files

while IFS= read -r -d '' file; do
  links=$(grep -oP '\[[^\]]+\]\([^\)]+\)' "$file" | sort | uniq)
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
