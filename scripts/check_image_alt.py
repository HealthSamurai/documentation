#!/usr/bin/env python3
"""
Check for images without alt text in markdown files.
- Markdown images: ![](path) with empty alt
- HTML img tags: <img ... alt="" ...> or missing alt attribute
"""

import os
import re
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue


def check_image_alt(file_path):
    """Check for images without alt text."""
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    issues = []
    in_code_block = False

    for line_num, line in enumerate(lines, 1):
        if line.strip().startswith('```'):
            in_code_block = not in_code_block
            continue
        if in_code_block:
            continue

        # Markdown: ![](...) - empty alt
        if re.search(r'!\[\]\([^)]+\)', line):
            issues.append((line_num, "markdown image without alt"))

        # HTML: <img ... alt="" ...> or alt='' or just alt without value
        # Skip if <img is inside inline code (backticks)
        if '<img' in line.lower():
            # Remove inline code spans before checking
            line_without_code = re.sub(r'`[^`]+`', '', line)
            if '<img' in line_without_code.lower():
                if re.search(r'alt\s*=\s*["\'][\s]*["\']', line_without_code, re.IGNORECASE):
                    issues.append((line_num, "img tag with empty alt"))
                elif not re.search(r'alt\s*=', line_without_code, re.IGNORECASE):
                    issues.append((line_num, "img tag without alt attribute"))

    return issues


def main():
    docs_dir = 'docs'
    check_start("Image Alt Text")

    if not os.path.exists(docs_dir):
        check_error(f"Directory '{docs_dir}' not found")
        return 1

    all_errors = []
    files_checked = 0

    for root, dirs, files in os.walk(docs_dir):
        if 'deprecated' in root.split(os.sep):
            continue
        for file in files:
            if file.endswith('.md'):
                file_path = os.path.join(root, file)
                files_checked += 1
                issues = check_image_alt(file_path)
                for line_num, issue_type in issues:
                    all_errors.append(f"{file_path}:{line_num} ({issue_type})")

    if all_errors:
        print(f"        ⚠ Found {len(all_errors)} images without alt text (warning):")
        for err in all_errors[:10]:
            print_issue(err)
        if len(all_errors) > 10:
            print_issue(f"... and {len(all_errors) - 10} more")
        return 0

    check_success(f"{files_checked} files checked, all images have alt text")
    return 0


if __name__ == '__main__':
    sys.exit(main())
