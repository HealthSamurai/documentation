#!/bin/bash

mkdir -p out

scripts/all-files.sh > out/all_files.txt
scripts/all-files-from-summary.sh > out/summary_files.txt

comm -23 out/all_files.txt out/summary_files.txt > out/not_in_summary.txt
comm -13 out/all_files.txt out/summary_files.txt | grep -v '^README.md$' > out/not_on_disk.txt

echo 'Files in docs/ not referenced in SUMMARY.md:'
if [ -s out/not_in_summary.txt ]; then
  cat out/not_in_summary.txt
else
  echo 'none, OK'
fi

echo 'Paths in SUMMARY.md that do not exist on disk:'
if [ -s out/not_on_disk.txt ]; then
  cat out/not_on_disk.txt
else
  echo 'none, OK'
fi

# Exit with error if any of the result files are not empty
if [[ -s out/not_in_summary.txt || -s out/not_on_disk.txt ]]; then
  exit 1
else
  exit 0
fi
