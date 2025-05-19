#!/bin/bash

# This script removes '-1' suffixes from filenames and directories.
# It recursively traverses the specified directory (or current directory if none provided)
# and renames any items ending with '-1' by removing this suffix.
# Example: 'crud-1' becomes 'crud'

DIR="${1:-.}"

# crud-1 -> crud
find "$DIR" -depth -name '*-1' | while read -r item; do
    new_name="$(echo "$item" | sed 's/-1$//')"
    if [ "$item" != "$new_name" ]; then
        mv -v "$item" "$new_name"
    fi
done
