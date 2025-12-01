#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path
from typing import Set, List

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from lib.output import check_start, check_success, check_error, print_issue

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
    check_start("Summary vs Files")

    files_from_summary = get_files_from_summary()
    files_on_disk = get_files_on_disk()

    files_not_in_summary = files_on_disk - files_from_summary
    paths_not_on_disk = files_from_summary - files_on_disk

    total_issues = len(files_not_in_summary) + len(paths_not_on_disk)

    if total_issues == 0:
        check_success("SUMMARY.md and docs/ are in sync")
        return 0

    check_error(f"Found {total_issues} sync issues:")
    for file in sorted(files_not_in_summary)[:5]:
        print_issue(f"Not in SUMMARY: {file}")
    for path in sorted(paths_not_on_disk)[:5]:
        print_issue(f"Missing on disk: {path}")
    if total_issues > 10:
        print_issue(f"... and {total_issues - 10} more")

    return 1

if __name__ == '__main__':
    sys.exit(main())