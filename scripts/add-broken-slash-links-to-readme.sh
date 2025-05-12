#!/bin/bash

find docs/ -type f -name '*.md' | while read file; do
  sed -i -E 's#(\]\((\./|\.\./|/)[^)]*/)\)#\1README.md)#g' "$file"
  sed -i 's/(.\/\#/(\#/g' "$file"
done

echo "All relative links ending with / now have README.md appended, and (./#...) links replaced with (#...) in all markdown files."
