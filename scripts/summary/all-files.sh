#!/bin/bash

# This script lists all files in the docs directory.
# It changes to the docs directory, finds all files recursively,
# removes the leading './' from each path, and outputs a sorted list.
# Used in conjunction with all-files-from-summary.sh to verify documentation completeness.

cd docs && find . -type f | sed 's|^./||' | sort
