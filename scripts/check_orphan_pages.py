#!/usr/bin/env python3
"""Check for orphan pages - pages with no incoming links from other docs."""

import os
import re
import sys
from pathlib import Path
from collections import defaultdict

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue


def normalize_link(source_file: str, link: str) -> str:
    """Resolve relative link to absolute path from docs root."""
    # Remove anchor
    link = link.split('#')[0]
    if not link:
        return ''

    # Skip external links
    if link.startswith(('http://', 'https://', 'mailto:')):
        return ''

    source_dir = os.path.dirname(source_file)

    # Handle relative paths
    if link.startswith('../'):
        resolved = os.path.normpath(os.path.join(source_dir, link))
    elif link.startswith('./'):
        resolved = os.path.normpath(os.path.join(source_dir, link[2:]))
    elif not link.startswith('/'):
        resolved = os.path.normpath(os.path.join(source_dir, link))
    else:
        resolved = link.lstrip('/')

    # Handle directory links -> README.md
    if resolved.endswith('/'):
        resolved = resolved + 'README.md'
    elif not resolved.endswith('.md'):
        # Could be a directory
        if os.path.isdir(os.path.join('docs', resolved)):
            resolved = resolved + '/README.md'

    return resolved


def build_link_graph(docs_dir: str) -> tuple[set, dict]:
    """Build graph of all pages and incoming links."""
    all_files = set()
    incoming_links = defaultdict(set)  # target -> set of sources

    for path in Path(docs_dir).rglob('*.md'):
        rel_path = str(path.relative_to(docs_dir))

        # Skip deprecated
        if rel_path.startswith('deprecated/'):
            continue

        # Skip aidbox-forms (auto-generated)
        if rel_path.startswith('modules/aidbox-forms/'):
            continue

        # Skip reference (auto-generated)
        if rel_path.startswith('reference/'):
            continue

        # Skip SUMMARY.md from file list (it's navigation, not content)
        if rel_path == 'SUMMARY.md':
            continue

        all_files.add(rel_path)

        # Extract links from this file
        with open(path, 'r') as f:
            content = f.read()

        links = re.findall(r'\]\(([^)]+)\)', content)
        for link in links:
            target = normalize_link(rel_path, link)
            if target and target.endswith('.md'):
                incoming_links[target].add(rel_path)

    return all_files, incoming_links


def main():
    check_start("Orphan Pages")

    docs_dir = 'docs'
    all_files, incoming_links = build_link_graph(docs_dir)

    # Entry points that don't need incoming links
    entry_points = {'SUMMARY.md', 'README.md', 'getting-started/README.md'}

    orphans = []
    for file in all_files:
        if file in entry_points:
            continue
        # Skip README.md files - they are auto-generated index pages
        if file.endswith('README.md'):
            continue
        # Skip hidden/deprecated pages
        try:
            with open(f'{docs_dir}/{file}', 'r') as f:
                header = f.read(500)
                if 'hidden: true' in header:
                    continue
        except:
            pass
        if file not in incoming_links:
            orphans.append(file)

    if orphans:
        check_error(f"Found {len(orphans)} orphan page(s) with no incoming links:")
        for orphan in sorted(orphans)[:10]:
            print_issue(f"docs/{orphan}")
        if len(orphans) > 10:
            print_issue(f"... and {len(orphans) - 10} more")
        sys.exit(1)

    check_success(f"{len(all_files)} pages checked, all have incoming links")


if __name__ == '__main__':
    main()
