#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue, print_detail

def extract_summary_entries(summary_path):
    """Extract file paths and titles from SUMMARY.md"""
    entries = []
    
    with open(summary_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Pattern to match markdown links: [title](path)
    pattern = r'\* \[([^\]]+)\]\(([^)]+)\)'
    matches = re.findall(pattern, content)
    
    for title, path in matches:
        if path.endswith('.md'):
            entries.append((title.strip(), path))
    
    return entries

def extract_h1_header(file_path):
    """Extract the first h1 header from a markdown file"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Pattern to match h1 headers: # Title
        pattern = r'^#\s+(.+)$'
        match = re.search(pattern, content, re.MULTILINE)
        
        if match:
            return match.group(1).strip()
        return None
    except FileNotFoundError:
        return None
    except Exception as e:
        print(f"Error reading {file_path}: {e}")
        return None

def normalize_title(title):
    """Normalize title for comparison by removing special characters and extra spaces"""
    # Remove markdown formatting
    title = re.sub(r'\*\*(.*?)\*\*', r'\1', title)
    title = re.sub(r'\*(.*?)\*', r'\1', title)
    title = re.sub(r'`(.*?)`', r'\1', title)
    
    # Remove extra whitespace and normalize
    title = re.sub(r'\s+', ' ', title).strip()
    
    return title

def main():
    check_start("Title Mismatch")

    docs_dir = Path("docs")
    summary_path = docs_dir / "SUMMARY.md"

    if not summary_path.exists():
        check_error("SUMMARY.md not found in docs directory")
        return 1

    entries = extract_summary_entries(summary_path)
    mismatches = []
    warnings = []

    for title, file_path in entries:
        full_path = docs_dir / file_path

        if not full_path.exists():
            continue

        h1_header = extract_h1_header(full_path)

        if h1_header is None:
            warnings.append(file_path)
            continue

        normalized_title = normalize_title(title)
        normalized_h1 = normalize_title(h1_header)

        if normalized_title != normalized_h1:
            mismatches.append(file_path)

    for w in warnings:
        print_detail(f"WARNING: No h1 header in {w}")

    if mismatches:
        check_error(f"Found {len(mismatches)} title mismatches:")
        for m in mismatches[:10]:
            print_issue(m)
        if len(mismatches) > 10:
            print_issue(f"... and {len(mismatches) - 10} more")
        return 1

    check_success(f"{len(entries)} entries checked, all titles match")
    return 0


if __name__ == "__main__":
    sys.exit(main()) 