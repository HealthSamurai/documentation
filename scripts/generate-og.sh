#!/bin/bash
set -e

# Usage: ./generate-og.sh [product-id]
# If no product-id is provided, generates for all products

PRODUCT_ID="${1:-all}"

generate_for_product() {
  local product_id=$1
  local product_name=$2
  local docs_root=$3
  local logo_path=$4
  
  echo "Generating OG preview images for product: $product_id"
  
  mkdir -p "resources/public/og-preview/$product_id"
  
  find "$docs_root" -name '*.md' | while read -r mdfile; do
    title=$(grep -m 1 '^# ' "$mdfile" | sed 's/^# //')
    [ -z "$title" ] && continue
    
    relpath="${mdfile#$docs_root/}"
    pngpath="resources/public/og-preview/$product_id/${relpath%.md}.png"
    
    mkdir -p "$(dirname "$pngpath")"
    
    echo "Creating preview: $pngpath ← \"$title\""
    
    # Use product logo if available, fallback to default
    if [ -f "$logo_path" ]; then
      logo="$logo_path"
    else
      logo=".gitbook/assets/aidbox-logo.svg"
    fi
    
    if command -v magick >/dev/null 2>&1; then
      magick -size 1200x630 xc:white \
        \( "$logo" -resize 150x150 \) -gravity northeast -geometry +30+30 -composite \
        -gravity center -pointsize 40 -font fonts/Roboto-Regular.ttf -fill "#555555" -annotate +0+100 "$product_name" \
        -gravity center -pointsize 80 -font fonts/Roboto-Regular.ttf -fill "#222222" -size 1000x200 -background none caption:"$title" \
        -gravity center -geometry +0-40 -composite \
        "$pngpath"
    else
      convert -size 1200x630 xc:white \
        \( "$logo" -resize 150x150 \) -gravity northeast -geometry +30+30 -composite \
        -gravity center -pointsize 40 -font fonts/Roboto-Regular.ttf -fill "#555555" -annotate +0+100 "$product_name" \
        -gravity center -pointsize 80 -font fonts/Roboto-Regular.ttf -fill "#222222" -size 1000x200 -background none caption:"$title" \
        -gravity center -geometry +0-40 -composite \
        "$pngpath"
    fi
  done
}

# For now, hardcoded product configurations
# TODO: Parse from products.yaml
if [ "$PRODUCT_ID" = "all" ]; then
  generate_for_product "aidbox" "Aidbox User Docs" "docs-new/aidbox/docs" "resources/.gitbook/assets/aidbox_logo.jpg"
  generate_for_product "forms" "Forms Documentation" "docs-new/forms/docs" "resources/.gitbook/assets/aidbox_logo.jpg"
elif [ "$PRODUCT_ID" = "aidbox" ]; then
  generate_for_product "aidbox" "Aidbox User Docs" "docs-new/aidbox/docs" "resources/.gitbook/assets/aidbox_logo.jpg"
elif [ "$PRODUCT_ID" = "forms" ]; then
  generate_for_product "forms" "Forms Documentation" "docs-new/forms/docs" "resources/.gitbook/assets/aidbox_logo.jpg"
else
  echo "Unknown product: $PRODUCT_ID"
  echo "Usage: $0 [all|aidbox|forms]"
  exit 1
fi
