#!/usr/bin/env python3
import os
import sys
import re
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue

def check_h1_headers(file_path):
    """Check if a markdown file has more than one H1 header."""
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    h1_headers = []
    in_code_block = False
    in_html_block = False
    
    for line in lines:
        # Check if we're entering or exiting a code block
        if line.strip().startswith('```'):
            in_code_block = not in_code_block
            continue
        
        # Check for HTML code blocks
        if '<pre' in line or '<code' in line:
            in_html_block = True
        if '</pre>' in line or '</code>' in line:
            in_html_block = False
            continue
            
        # Skip lines inside code blocks or HTML blocks
        if in_code_block or in_html_block:
            continue
            
        # Check for H1 headers: lines starting with exactly one # followed by space
        # Not ##, ###, etc.
        if re.match(r'^#\s+.*$', line) and not re.match(r'^##', line):
            h1_headers.append(line.strip())
    
    return h1_headers

def main():
    docs_dir = Path('docs')

    check_start("H1 Headers")

    if not docs_dir.exists():
        check_error(f"Directory '{docs_dir}' not found")
        return 1

    errors = []
    total_files = 0

    for md_file in docs_dir.rglob('*.md'):
        total_files += 1
        h1_headers = check_h1_headers(md_file)

        if len(h1_headers) > 1:
            relative_path = md_file.relative_to('.')
            errors.append({
                'file': str(relative_path),
                'count': len(h1_headers),
                'headers': h1_headers
            })

    if errors:
        check_error(f"Found {len(errors)} file(s) with multiple H1 headers:")
        for error in errors:
            print_issue(f"{error['file']} ({error['count']} headers)")
        return 1
    else:
        check_success(f"{total_files} files checked, no issues")
        return 0

if __name__ == '__main__':
    sys.exit(main())