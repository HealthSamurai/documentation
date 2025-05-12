#!/usr/bin/env bash
set -e

ALL_FROM_SUMMARY=$(./scripts/all-files-from-summary.sh)
ALL_ON_DISK=$(./scripts/all-files.sh | grep -v '^SUMMARY.md$' | grep '.md$')

TMP_SUMMARY=$(mktemp)
TMP_DISK=$(mktemp)
echo "$ALL_FROM_SUMMARY" | sort > "$TMP_SUMMARY"
echo "$ALL_ON_DISK" | sort > "$TMP_DISK"

echo "Files in docs/ not referenced in SUMMARY.md:"
comm -23 "$TMP_DISK" "$TMP_SUMMARY"

echo "Paths in SUMMARY.md that do not exist on disk:"
comm -13 "$TMP_DISK" "$TMP_SUMMARY"

if [[ -s "$TMP_DISK" && -s "$TMP_SUMMARY" ]]; then
    if [[ -n $(comm -23 "$TMP_DISK" "$TMP_SUMMARY") || -n $(comm -13 "$TMP_DISK" "$TMP_SUMMARY") ]]; then
        echo -e "\n[pre-push] ERROR: docs/SUMMARY.md and docs/ are out of sync. Fix the issues above before pushing."
        exit 1
    fi
fi

echo "SUMMARY.md and docs/ are in sync."

rm -f "$TMP_SUMMARY" "$TMP_DISK"
