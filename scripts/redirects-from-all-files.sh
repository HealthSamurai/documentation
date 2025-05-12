#!/usr/bin/env bash

segments=("api" "bulk-api" "readme" "modules" "crud" "plan-api" "rest-api" )

declare -A redirects

while IFS= read -r path; do
  rel_path="${path#docs/}"
  value="$rel_path"
  # Split the path into parts
  IFS='/' read -ra parts <<< "$rel_path"
  n=${#parts[@]}
  # For each combination of segments, add -1
  for ((mask=1; mask < (1 << n); mask++)); do
    new_parts=("${parts[@]}")
    changed=false
    for ((i=0; i<n; i++)); do
      for seg in "${segments[@]}"; do
        if [[ "${parts[$i]}" == "$seg" && $(( (mask >> i) & 1 )) -eq 1 ]]; then
          new_parts[$i]="${parts[$i]}-1"
          changed=true
        fi
      done
    done
    if $changed; then
      key_no_md="$(IFS=/; echo "${new_parts[*]}")"
      key_no_md="${key_no_md%.md}"
      redirects["$key_no_md"]="$value"
    fi
  done

done < <(./scripts/all-files.sh)

echo "redirects:" > out/redirects.yaml
for k in $(printf "%s\n" "${!redirects[@]}" | sort); do
  echo "    $k: ${redirects[$k]}" >> out/redirects.yaml
done
