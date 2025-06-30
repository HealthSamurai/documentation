#!/bin/bash
set -e

mkdir -p resources/public/og-preview

find docs -name '*.md' | while read -r mdfile; do
  title=$(grep -m 1 '^# ' "$mdfile" | sed 's/^# //')
  [ -z "$title" ] && continue

  relpath="${mdfile#docs/}"
  pngpath="resources/public/og-preview/${relpath%.md}.png"

  mkdir -p "$(dirname "$pngpath")"

  echo "Creating preview: $pngpath ← \"$title\""

if command -v magick >/dev/null 2>&1; then
  magick -size 1200x630 xc:white \
    \( .gitbook/assets/aidbox-logo.svg -resize 150x150 \) -gravity northeast -geometry +30+30 -composite \
    -gravity center -pointsize 40 -font fonts/Roboto-Regular.ttf -fill "#555555" -annotate +0+100 "Aidbox User Docs" \
    -gravity center -pointsize 80 -font fonts/Roboto-Regular.ttf -fill "#222222" -size 1000x200 -background none caption:"$title" \
    -gravity center -geometry +0-40 -composite \
    "$pngpath"
else
  convert -size 1200x630 xc:white \
    \( .gitbook/assets/aidbox-logo.svg -resize 150x150 \) -gravity northeast -geometry +30+30 -composite \
    -gravity center -pointsize 40 -font fonts/Roboto-Regular.ttf -fill "#555555" -annotate +0+100 "Aidbox User Docs" \
    -gravity center -pointsize 80 -font fonts/Roboto-Regular.ttf -fill "#222222" -size 1000x200 -background none caption:"$title" \
    -gravity center -geometry +0-40 -composite \
    "$pngpath"
fi

done
