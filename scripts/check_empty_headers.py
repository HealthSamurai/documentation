#!/usr/bin/env python3
"""
Check for empty headers in markdown files.
Empty headers like "## ", "### ", etc. should not be allowed.
"""

import os
import re
import sys

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
    errors_found = False
    docs_dir = 'docs'

    if not os.path.exists(docs_dir):
        print(f"ERROR: Directory '{docs_dir}' not found")
        return 1

    for root, dirs, files in os.walk(docs_dir):
        # Skip deprecated directories
        if 'deprecated' in root.split(os.sep):
            continue

        for file in files:
            if file.endswith('.md'):
                file_path = os.path.join(root, file)
                empty_headers = check_empty_headers(file_path)

                if empty_headers:
                    if not errors_found:
                        print("ERROR: Found empty headers in markdown files:")
                        errors_found = True

                    print(f"\n{file_path}:")
                    for line_num, header in empty_headers:
                        print(f"  Line {line_num}: {header}")

    if errors_found:
        print("\nPlease remove empty headers before pushing")
        return 1

    print("✓ No empty headers found")
    return 0

if __name__ == '__main__':
    sys.exit(main())
