#!/usr/bin/env python3
"""
Check for links pointing to deprecated pages.
Usage: python scripts/check_deprecated_links.py
"""

import os
import sys
import re
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue

DOCS_ROOT = Path(__file__).parent.parent / "docs"
LINK_PATTERN = re.compile(r'\[([^\]]*)\]\(([^)]+)\)')


def check_file(file_path: Path) -> list[tuple[str, str, int]]:
    """Check file for links to deprecated. Returns list of (link_text, href, line_num)."""
    issues = []
    try:
        lines = file_path.read_text(encoding='utf-8').splitlines()
    except Exception:
        return issues

    for line_num, line in enumerate(lines, 1):
        for match in LINK_PATTERN.finditer(line):
            text, href = match.groups()
            if 'deprecated' in href.lower():
                issues.append((text, href, line_num))

    return issues


def main():
    check_start("Deprecated Links")

    # Get all md files except excluded paths
    all_files = [f for f in DOCS_ROOT.rglob("*.md")
                 if "deprecated" not in str(f).lower()
                 and f.name != "SUMMARY.md"
                 and "release-notes" not in f.name
                 and "aidbox-forms" not in str(f)]

    all_issues = []

    for file_path in sorted(all_files):
        issues = check_file(file_path)
        if issues:
            rel_path = file_path.relative_to(DOCS_ROOT)
            for text, href, line_num in issues:
                all_issues.append(f"{rel_path}:{line_num}: [{text}]({href})")

    if all_issues:
        check_error(f"Found {len(all_issues)} links to deprecated pages")
        for issue in all_issues[:10]:
            print_issue(issue)
        if len(all_issues) > 10:
            print_issue(f"... and {len(all_issues) - 10} more")
        return 1
    else:
        check_success(f"{len(all_files)} files checked, no deprecated links")
        return 0


if __name__ == '__main__':
    sys.exit(main())
