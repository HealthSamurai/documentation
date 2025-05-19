#!/bin/bash

# This script checks for broken relative links in the documentation.
# It parses the output from extract-all-links.sh and validates if each relative link
# points to an existing file. Links to external sites, anchors, and reference docs are skipped.
# The script reports errors only for broken links in non-deprecated files.
# Output is saved to out/all_nonexistent_links_by_file.txt.

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
  elif echo "$line" | grep -qE '^  \[.*\]\([^)]+\)' || echo "$line" | grep -qE '^  <img.*src="[^"]+' || echo "$line" | grep -qE '^  src="[^"]+'; then
    # Extract link from markdown or HTML src attribute
    if echo "$line" | grep -qE '^  \[.*\]\([^)]+\)'; then
      link=$(echo "$line" | sed -n 's/.*](\([^)]*\)).*/\1/p')
    elif echo "$line" | grep -qE '^  src="[^"]+'; then
      link=$(echo "$line" | sed -n 's/.*src="\([^"]*\)".*/\1/p')
    else
      link=$(echo "$line" | sed -n 's/.*src="\([^"]*\)".*/\1/p')
    fi
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
      # Check if there's a README.md in the directory for directory-style links
      if [[ -d "$full_path" && -f "$full_path/README.md" ]]; then
        continue
      fi
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

if [[ -s out/all_nonexistent_links_by_file.txt ]]; then
  # Only consider file header lines (ending with 'links):')
  files=$(grep 'links):$' out/all_nonexistent_links_by_file.txt | sed 's/ (.*//')
  
  # Check for non-deprecated files with broken links
  non_deprecated_files=$(echo "$files" | grep -v '^docs/deprecated/')
  
  if [[ -n "$non_deprecated_files" ]]; then
    echo -e '\nERROR: Broken relative links found outside docs/deprecated directory:'
    echo "$non_deprecated_files" | head -5
    if [[ $(echo "$non_deprecated_files" | wc -l) -gt 5 ]]; then
      echo "... and more (total $(echo "$non_deprecated_files" | wc -l) files)"
    fi
    echo "See full details in out/all_nonexistent_links_by_file.txt"
    
    # Extract a few examples of broken links for quick reference
    echo -e "\nExamples of broken links:"
    grep -v "^docs/deprecated/" out/all_nonexistent_links_by_file.txt | grep -A 1 "links):" | grep -v "^--$" | head -10
    
    exit 1
  else
    echo -e '\nOnly deprecated files have broken relative links. Skipping error.'
  fi
fi
