#!/bin/bash

find docs/ -type f -name '*.md' | while read file; do
  sed -i -E 's#(\]\((\./|\.\./|/)[^)]*/)\)#\1README.md)#g' "$file"
done

echo "All relative links ending with / now have README.md appended in all markdown files."
