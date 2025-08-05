#!/usr/bin/env python3
"""
Check that no titles in SUMMARY.md contain " & " and suggest using " and " instead.
"""

import os
import sys
import re

def check_ampersand_in_summary():
    """Check SUMMARY.md files for titles containing ' & ' pattern."""
    summary_files = [
        'docs/SUMMARY.md',
        'docs-new/forms/docs/SUMMARY.md'
    ]
    
    errors_found = False
    
    for summary_file in summary_files:
        if not os.path.exists(summary_file):
            continue
            
        print(f"Checking {summary_file}...")
        
        with open(summary_file, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        for line_num, line in enumerate(lines, 1):
            # Check if line contains a title (markdown link)
            if re.search(r'\[.*?\]', line):
                # Check if the title contains " & "
                if ' & ' in line:
                    errors_found = True
                    # Extract the title from the markdown link
                    title_match = re.search(r'\[(.*?)\]', line)
                    if title_match:
                        title = title_match.group(1)
                        suggested_title = title.replace(' & ', ' and ')
                        print(f"ERROR: Line {line_num}: Title contains ' & '")
                        print(f"  Found: {title}")
                        print(f"  Suggested: {suggested_title}")
                        print(f"  Full line: {line.strip()}")
                        print()
    
    if errors_found:
        print("ERROR: Found titles with ' & ' in SUMMARY.md files.")
        print("Please replace ' & ' with ' and ' in all titles.")
        return 1
    else:
        print("SUCCESS: No titles with ' & ' found in SUMMARY.md files.")
        return 0

if __name__ == "__main__":
    sys.exit(check_ampersand_in_summary())