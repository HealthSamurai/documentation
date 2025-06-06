#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path
from typing import Set, List

def get_files_from_summary() -> Set[str]:
    """Extract all file paths referenced in SUMMARY.md."""
    summary_path = Path('docs/SUMMARY.md')
    if not summary_path.exists():
        print("Error: docs/SUMMARY.md not found")
        sys.exit(1)

    with open(summary_path) as f:
        content = f.read()
    
    # Find all markdown links and extract paths
    paths = set()
    for match in re.finditer(r'\]\(([^)]+)\)', content):
        path = match.group(1)
        # Skip external URLs
        if not path.startswith(('http://', 'https://')):
            paths.add(path)
    
    return paths

def get_files_on_disk() -> Set[str]:
    """Get all .md files in the docs directory."""
    paths = set()
    for path in Path('docs').rglob('*.md'):
        # Convert to relative path and skip SUMMARY.md
        rel_path = str(path.relative_to('docs'))
        if rel_path != 'SUMMARY.md':
            paths.add(rel_path)
    return paths

def main():
    print('\nChecking if summary.md file and filetree are synced')

    # Get files from both sources
    files_from_summary = get_files_from_summary()
    files_on_disk = get_files_on_disk()

    # Find discrepancies
    files_not_in_summary = files_on_disk - files_from_summary
    paths_not_on_disk = files_from_summary - files_on_disk

    if not files_not_in_summary and not paths_not_on_disk:
        print('SUMMARY.md and docs/ are in sync.')
        return

    # Report discrepancies
    if files_not_in_summary:
        print('Files in docs/ not referenced in SUMMARY.md:')
        for file in sorted(files_not_in_summary):
            print(file)

    if paths_not_on_disk:
        print('Paths in SUMMARY.md that do not exist on disk:')
        for path in sorted(paths_not_on_disk):
            print(path)

    print("\nERROR: docs/SUMMARY.md and docs/ are out of sync. Fix the issues above before pushing.")
    sys.exit(1)

if __name__ == '__main__':
    main() 