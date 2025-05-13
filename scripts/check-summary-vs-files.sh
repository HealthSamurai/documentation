#!/usr/bin/env bash
set -e
echo -e '\nChecking if summary.md file and filetree are synced'

ALL_FROM_SUMMARY=$(./scripts/all-files-from-summary.sh)
ALL_ON_DISK=$(./scripts/all-files.sh | grep -v '^SUMMARY.md$' | grep '.md$')

TMP_SUMMARY=$(mktemp)
TMP_DISK=$(mktemp)
echo "$ALL_FROM_SUMMARY" | sort > "$TMP_SUMMARY"
echo "$ALL_ON_DISK" | sort > "$TMP_DISK"

files_not_in_summary=$(comm -23 "$TMP_DISK" "$TMP_SUMMARY")
paths_not_on_disk=$(comm -13 "$TMP_DISK" "$TMP_SUMMARY")

if [[ -z "$files_not_in_summary" && -z "$paths_not_on_disk" ]]; then
  echo 'SUMMARY.md and docs/ are in sync.'
else
  if [[ -n "$files_not_in_summary" ]]; then
    echo 'Files in docs/ not referenced in SUMMARY.md:'
    echo "$files_not_in_summary"
  fi
  if [[ -n "$paths_not_on_disk" ]]; then
    echo 'Paths in SUMMARY.md that do not exist on disk:'
    echo "$paths_not_on_disk"
  fi
  echo -e "\nERROR: docs/SUMMARY.md and docs/ are out of sync. Fix the issues above before pushing."
  exit 1
fi

rm -f "$TMP_SUMMARY" "$TMP_DISK"
