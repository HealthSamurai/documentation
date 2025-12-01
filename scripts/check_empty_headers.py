#!/usr/bin/env python3
"""
Check for empty headers in markdown files.
Empty headers like "## ", "### ", etc. should not be allowed.
"""

import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue

def check_empty_headers(file_path):
    """Check if file contains empty headers."""
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    empty_headers = []
    for line_num, line in enumerate(lines, 1):
        # Match headers (##, ###, ####, #####, ######) followed by only whitespace
        if re.match(r'^#{2,6}\s*$', line):
            empty_headers.append((line_num, line.strip()))

    return empty_headers

def main():
    """Main function to check all markdown files."""
    docs_dir = 'docs'

    check_start("Empty Headers")

    if not os.path.exists(docs_dir):
        check_error(f"Directory '{docs_dir}' not found")
        return 1

    all_errors = []

    for root, dirs, files in os.walk(docs_dir):
        if 'deprecated' in root.split(os.sep):
            continue

        for file in files:
            if file.endswith('.md'):
                file_path = os.path.join(root, file)
                empty_headers = check_empty_headers(file_path)

                if empty_headers:
                    for line_num, header in empty_headers:
                        all_errors.append(f"{file_path}:{line_num}")

    if all_errors:
        check_error(f"Found {len(all_errors)} empty headers:")
        for err in all_errors:
            print_issue(err)
        return 1

    check_success("No empty headers found")
    return 0

if __name__ == '__main__':
    sys.exit(main())
