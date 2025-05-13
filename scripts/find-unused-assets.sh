#!/bin/bash

# Find all references to assets in markdown files
used_assets=$(grep -rhoP ".gitbook/assets/[^)\"'\\s]+" docs/ | sort | uniq)

unused=()
while IFS= read -r -d '' asset; do
  rel_asset=${asset#./}
  echo "Checking: $rel_asset"
  if ! echo "$used_assets" | grep -Fxq "$rel_asset"; then
    unused+=("$rel_asset")
  fi
done < <(find .gitbook/assets -type f -print0)

echo
out_file="out/unused_assets.txt"
printf '%s\n' "${unused[@]}" > "$out_file"

if [ ${#unused[@]} -eq 0 ]; then
  echo "All assets in .gitbook/assets  are used in documentation."
else
  echo "Unused assets in .gitbook/assets (safe to delete):"
  cat "$out_file"
  echo "List saved to $out_file"
fi
