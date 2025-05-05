#!/bin/bash

DIR="${1:-.}"

# crud-1 -> crud
find "$DIR" -depth -name '*-1' | while read -r item; do
    new_name="$(echo "$item" | sed 's/-1$//')"
    if [ "$item" != "$new_name" ]; then
        mv -v "$item" "$new_name"
    fi
done
