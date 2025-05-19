#!/bin/bash

# This script extracts all file paths referenced in the SUMMARY.md file.
# It uses grep to find all markdown links in the format [text](path),
# extracts just the paths, excludes external URLs (starting with http),
# and outputs a sorted, unique list of all internal file references.

grep -oP '\]\(\K[^)]+' ./docs/SUMMARY.md | grep -v '^http' | sort | uniq
