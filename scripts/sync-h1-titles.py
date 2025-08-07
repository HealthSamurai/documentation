#!/usr/bin/env python3
"""
Sync h1 headers in markdown files with titles from SUMMARY.md
"""

import os
import re
import sys

def parse_summary():
    """Parse SUMMARY.md and extract titles with their file paths."""
    summary_entries = {}
    
    summary_files = [
        'docs/SUMMARY.md',
        'docs-new/forms/docs/SUMMARY.md'
    ]
    
    for summary_file in summary_files:
        if not os.path.exists(summary_file):
            continue
            
        with open(summary_file, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        base_dir = os.path.dirname(summary_file)
        
        for line in lines:
            # Match lines with titles and file paths
            match = re.match(r'\s*\*\s*\[(.*?)\]\((.*?)\)', line)
            if match:
                title = match.group(1)
                file_path = match.group(2)
                
                # Skip external links and anchors
                if file_path.startswith('http') or file_path.startswith('#'):
                    continue
                
                # Convert relative path to absolute
                full_path = os.path.join(base_dir, file_path)
                full_path = os.path.normpath(full_path)
                
                summary_entries[full_path] = title
    
    return summary_entries

def get_h1_header(file_path):
    """Extract the h1 header from a markdown file."""
    if not os.path.exists(file_path):
        return None
    
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    for line in lines:
        # Match h1 header (single #)
        match = re.match(r'^#\s+(.+)$', line.strip())
        if match:
            return match.group(1).strip()
    
    return None

def update_h1_header(file_path, new_title):
    """Update the h1 header in a markdown file."""
    if not os.path.exists(file_path):
        return False
    
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    updated = False
    for i, line in enumerate(lines):
        # Match h1 header (single #)
        match = re.match(r'^#\s+(.+)$', line.strip())
        if match:
            old_title = match.group(1).strip()
            if old_title != new_title:
                # Preserve the original line's indentation and format
                lines[i] = f"# {new_title}\n"
                updated = True
                print(f"Updated: {file_path}")
                print(f"  Old: {old_title}")
                print(f"  New: {new_title}")
                break
    
    if updated:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(lines)
    
    return updated

def main():
    """Main function to sync all h1 headers."""
    print("Syncing h1 headers with SUMMARY.md titles...")
    print()
    
    # Parse SUMMARY.md files
    summary_entries = parse_summary()
    
    if not summary_entries:
        print("ERROR: No entries found in SUMMARY.md files")
        return 1
    
    print(f"Found {len(summary_entries)} entries in SUMMARY.md files")
    print()
    
    # Check and update h1 headers
    mismatches = 0
    fixed = 0
    
    for file_path, summary_title in summary_entries.items():
        h1_header = get_h1_header(file_path)
        
        if h1_header is None:
            # File doesn't exist or has no h1 header
            continue
        
        if h1_header != summary_title:
            mismatches += 1
            if update_h1_header(file_path, summary_title):
                fixed += 1
    
    print()
    print(f"Summary:")
    print(f"  Total mismatches found: {mismatches}")
    print(f"  Successfully fixed: {fixed}")
    
    if mismatches == fixed:
        print("\nSUCCESS: All mismatches have been fixed!")
        return 0
    elif fixed > 0:
        print(f"\nPARTIAL SUCCESS: Fixed {fixed} out of {mismatches} mismatches")
        return 0
    else:
        print(f"\nWARNING: Found {mismatches} mismatches but none were fixed")
        return 1

if __name__ == "__main__":
    sys.exit(main())