#!/usr/bin/env python3
"""
Check that no titles in SUMMARY.md contain " & " and suggest using " and " instead.
"""

import os
import sys
import re

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue


def main():
    check_start("Ampersand in Summary")

    summary_files = [
        'docs/SUMMARY.md',
        'docs-new/forms/docs/SUMMARY.md'
    ]

    all_errors = []
    titles_checked = 0

    for summary_file in summary_files:
        if not os.path.exists(summary_file):
            continue

        with open(summary_file, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        for line_num, line in enumerate(lines, 1):
            if re.search(r'\[.*?\]', line):
                titles_checked += 1
                if ' & ' in line:
                    title_match = re.search(r'\[(.*?)\]', line)
                    if title_match:
                        all_errors.append(f"{summary_file}:{line_num}")

    if all_errors:
        check_error(f"Found {len(all_errors)} titles with ' & ':")
        for err in all_errors[:10]:
            print_issue(err)
        if len(all_errors) > 10:
            print_issue(f"... and {len(all_errors) - 10} more")
        return 1

    check_success(f"{titles_checked} titles checked, no ' & ' found")
    return 0


if __name__ == "__main__":
    sys.exit(main())