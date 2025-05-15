#!/bin/bash

grep -rhoP "\.gitbook/assets/[^>]+?\.(png|jpg|jpeg|gif|svg|webp|bmp|tiff|ico|pdf|sql)" docs/ > out/all_gitbook_asset_links.txt

find .gitbook/assets -type f | while read asset; do
  filename=$(basename "$asset")
  grep -Fq "$filename" out/all_gitbook_asset_links.txt || echo "$asset"
done > out/unused_assets.txt

if [ -s out/unused_assets.txt ]; then
  echo "Unused assets in .gitbook/assets (safe to delete):"
  cat out/unused_assets.txt
else
  echo "All assets in .gitbook/assets are used in documentation."
fi
