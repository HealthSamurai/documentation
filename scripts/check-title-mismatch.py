#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path

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
    docs_dir = Path("docs")
    summary_path = docs_dir / "SUMMARY.md"
    
    if not summary_path.exists():
        print("ERROR: SUMMARY.md not found in docs directory")
        sys.exit(1)
    
    entries = extract_summary_entries(summary_path)
    mismatches = []
    
    for title, file_path in entries:
        full_path = docs_dir / file_path
        
        if not full_path.exists():
            print(f"WARNING: File not found: {file_path}")
            continue
        
        h1_header = extract_h1_header(full_path)
        
        if h1_header is None:
            print(f"WARNING: No h1 header found in {file_path}")
            continue
        
        normalized_title = normalize_title(title)
        normalized_h1 = normalize_title(h1_header)
        
        if normalized_title != normalized_h1:
            mismatches.append({
                'file': file_path,
                'summary_title': title,
                'h1_header': h1_header
            })
    
    if mismatches:
        print("WARNING: Found title mismatches between SUMMARY.md and h1 headers:")
        print()
        for mismatch in mismatches:
            print(f"File: {mismatch['file']}")
            print(f"  Summary title: {mismatch['summary_title']}")
            print(f"  H1 header:     {mismatch['h1_header']}")
            print()
        
        print(f"Total mismatches found: {len(mismatches)}")
        return False
    else:
        print("✓ All titles in SUMMARY.md match their corresponding h1 headers")
        return True

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1) 