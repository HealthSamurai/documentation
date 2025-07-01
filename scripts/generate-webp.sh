#!/bin/bash

find ./.gitbook/assets -type f \( -iname "*.jpg" -o -iname "*.png" \) -exec bash -c '
  for img; do
    cwebp -q 80 "$img" -o "${img%.*}.webp"
  done
' bash {} +
