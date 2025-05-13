#!/bin/bash

mkdir -p out

output="out/all_nonexistent_links_by_file.txt"
> "$output"

echo "[extract-nonexistent-links] Checking all relative links..."

current_file=""
while IFS= read -r line; do
  if echo "$line" | grep -qE '^[^ ].*links\):$'; then
    current_file=$(echo "$line" | sed 's/ (.*//;s/ links.*//')
    file_header="$line"
    broken_links=()
  elif echo "$line" | grep -qE '^  \[.*\]\([^)]+\)'; then
    link=$(echo "$line" | sed -n 's/.*](\([^)]*\)).*/\1/p')
    # Skip external and anchor links
    if [[ "$link" =~ ^(http|https|ftp|mailto|#) ]]; then
      continue
    fi
    # Skip absolute links to docs.aidbox.app/reference
    if [[ "$link" =~ ^https://docs\.aidbox\.app/reference ]]; then
      continue
    fi
    # Skip links that point to reference/ anywhere in the path
    if [[ "$link" =~ (^|/)reference([/.]|$) ]]; then
      continue
    fi
    # Remove possible anchors/fragments
    file_path="${link%%#*}"
    # If file_path is empty, it's an anchor in the same file, skip
    if [[ -z "$file_path" ]]; then
      continue
    fi
    dir=$(dirname "$current_file")
    full_path="$dir/$file_path"
    # If no extension, try .md
    if [[ ! "$file_path" =~ \.[a-zA-Z0-9]+$ ]]; then
      full_path_md="$full_path.md"
    else
      full_path_md="$full_path"
    fi
    # Try to resolve realpath (diagnostic)
    resolved_path=$(realpath --no-symlinks --relative-to=. "$full_path_md" 2>/dev/null || echo "INVALID_PATH")
    if [[ "$resolved_path" == "INVALID_PATH" || ! -e "$full_path_md" ]]; then
      broken_links+=("$line [BROKEN: target does not exist or path is invalid]")
    fi
  elif [[ -z "$line" ]]; then
    if [[ ${#broken_links[@]} -gt 0 ]]; then
      echo "$file_header" >> "$output"
      printf "%s\n" "${broken_links[@]}" >> "$output"
      echo >> "$output"
    fi
    broken_links=()
  fi
done < out/all_links_by_file.txt

echo "Generated $output"
