#!/bin/bash

REPORT_FILE="check_summary_report.txt"
TMP_FILE=$(mktemp)

SUMMARY_CONTENT=$(<SUMMARY.md)

find . -type f ! -path '*/.*/*' ! -name '.*' ! -name "$REPORT_FILE" ! -name 'SUMMARY.md' | while read -r filepath; do
    rel_path="${filepath#./}"
    if echo "$SUMMARY_CONTENT" | grep -qF "$rel_path"; then
        echo "OK: $rel_path"
    else
        echo "MISSING: $rel_path"
    fi
done > "$TMP_FILE"

grep '^MISSING' "$TMP_FILE" > "$REPORT_FILE"
grep '^OK' "$TMP_FILE" >> "$REPORT_FILE"

rm "$TMP_FILE"
echo 'see check_summary_report.txt'
