#!/usr/bin/env python3
"""Check for dead-end pages - pages with no outgoing links to other docs."""

import os
import re
import sys
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue


def has_internal_links(file_path: str) -> bool:
    """Check if file has any internal documentation links."""
    with open(file_path, 'r') as f:
        content = f.read()

    # Find markdown links: [text](path)
    links = re.findall(r'\]\(([^)]+)\)', content)

    for link in links:
        # Skip external links, anchors, images
        if link.startswith(('http://', 'https://', '#', 'mailto:')):
            continue
        # Skip image files
        if re.search(r'\.(png|jpg|jpeg|gif|svg|webp)$', link, re.IGNORECASE):
            continue
        # Has internal link to another doc
        if link.endswith('.md') or link.endswith('/') or '/' in link:
            return True

    return False


def main():
    check_start("Dead-end Pages")

    docs_dir = 'docs'
    dead_ends = []
    total = 0

    for path in Path(docs_dir).rglob('*.md'):
        rel_path = str(path.relative_to(docs_dir))

        # Skip SUMMARY.md and deprecated
        if rel_path == 'SUMMARY.md' or rel_path.startswith('deprecated/'):
            continue

        # Skip README files (they are index pages, links come TO them)
        if rel_path.endswith('README.md'):
            continue

        total += 1

        if not has_internal_links(str(path)):
            dead_ends.append(rel_path)

    if dead_ends:
        check_error(f"Found {len(dead_ends)} dead-end page(s) with no internal links:")
        for page in sorted(dead_ends)[:10]:
            print_issue(f"docs/{page}")
        if len(dead_ends) > 10:
            print_issue(f"... and {len(dead_ends) - 10} more")
        sys.exit(1)

    check_success(f"{total} pages checked, all have internal links")


if __name__ == '__main__':
    main()
