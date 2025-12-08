#!/usr/bin/env python3
"""Check that YAML frontmatter in markdown files is valid."""
import os
import sys
import re
from pathlib import Path

import yaml

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue


def extract_frontmatter(content):
    """Extract YAML frontmatter from markdown content.

    Returns tuple (frontmatter_string, line_offset) or (None, 0) if no frontmatter.
    """
    if not content.startswith('---'):
        return None, 0

    # Find the closing ---
    match = re.match(r'^---\n(.*?)\n---', content, re.DOTALL)
    if match:
        return match.group(1), 2  # offset for the opening ---
    return None, 0


def check_frontmatter(file_path):
    """Check if frontmatter YAML is valid.

    Returns None if valid, or error message if invalid.
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    frontmatter, line_offset = extract_frontmatter(content)
    if frontmatter is None:
        return None  # No frontmatter is OK

    try:
        yaml.safe_load(frontmatter)
        return None
    except yaml.YAMLError as e:
        if hasattr(e, 'problem_mark'):
            line = e.problem_mark.line + line_offset
            return f"line {line}: {e.problem}"
        return str(e)


def main():
    docs_dir = Path('docs')

    check_start("Frontmatter YAML")

    if not docs_dir.exists():
        check_error(f"Directory '{docs_dir}' not found")
        return 1

    errors = []
    total_files = 0

    for md_file in docs_dir.rglob('*.md'):
        total_files += 1
        error = check_frontmatter(md_file)

        if error:
            relative_path = md_file.relative_to('.')
            errors.append({
                'file': str(relative_path),
                'error': error
            })

    if errors:
        check_error(f"Found {len(errors)} file(s) with invalid YAML frontmatter:")
        for error in errors:
            print_issue(f"{error['file']}: {error['error']}")
        return 1
    else:
        check_success(f"{total_files} files checked, no issues")
        return 0


if __name__ == '__main__':
    sys.exit(main())
