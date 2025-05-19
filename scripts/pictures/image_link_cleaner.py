#!/usr/bin/env python3

"""
Image link cleaner for documentation:
Cleans image links in documentation by removing angle brackets
and other "bad" characters that can cause issues.

Usage:
- python image_link_cleaner.py - clean all links
- python image_link_cleaner.py --dry-run - show what would be changed without making changes
"""

import os
import re
import argparse

# Root directory for the docs
DOCS_DIR = "docs"

# Bad characters that shouldn't be in paths
BAD_CHARS = ['<', '>', '|', '*', ':', ';', '"', "'", '(', ')', '[', ']', '{', '}']

def clean_file_links(file_path, dry_run=False):
    """
    Clean image links in a file from angle brackets and other "bad" characters
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        fixed_content = content
        
        # 1. Fix HTML attributes src= and href=
        
        # Pattern for src=<path> and href=<path>
        angle_bracket_pattern = r'((?:src|href)=)<([^>]+)>'
        fixed_content = re.sub(angle_bracket_pattern, r'\1"\2"', fixed_content)
        
        # 2. Fix standard image links in markdown format with angle brackets
        # Pattern for ![alt](<path>)
        md_angle_bracket_pattern = r'(!\[.*?\]\()<([^>]+)>(.*?\))'
        fixed_content = re.sub(md_angle_bracket_pattern, r'\1\2\3', fixed_content)
        
        # Write the fixed content back to the file
        if fixed_content != original_content:
            if not dry_run:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(fixed_content)
                print(f"✅ Fixed links in file {file_path}")
            else:
                print(f"[DRY RUN] Would fix links in file {file_path}")
            return True
        
        return False
    
    except Exception as e:
        print(f"❌ Error processing file {file_path}: {str(e)}")
        return False

def clean_all_links(dry_run=False):
    """
    Clean all image links in the documentation from angle brackets and other "bad" characters
    """
    fixed_files = 0
    
    # Walk through all markdown files
    for root, _, files in os.walk(DOCS_DIR):
        for file in files:
            if not file.endswith('.md'):
                continue
                
            file_path = os.path.join(root, file)
            if clean_file_links(file_path, dry_run):
                fixed_files += 1
    
    # Print summary
    print("\n=== LINK CLEANING SUMMARY ===")
    mode = "[DRY RUN] Would fix" if dry_run else "Fixed"
    print(f"{mode} {fixed_files} files")
    
    return fixed_files > 0

def main():
    """
    Main function to run the cleaner
    """
    parser = argparse.ArgumentParser(description="Clean image links in documentation")
    parser.add_argument('--dry-run', action='store_true', help='Run without making changes')
    args = parser.parse_args()
    
    # Output run mode information
    if args.dry_run:
        print("🔍 DRY RUN MODE: No changes will be made\n")
    
    print("=== CLEANING LINKS ===")
    result = clean_all_links(dry_run=args.dry_run)
    
    # Print overall result
    print("\n=== OVERALL RESULT ===")
    if result:
        print("✅ Link cleaning completed successfully!")
    else:
        print("⚠️ No links needed cleaning.")

if __name__ == "__main__":
    main() 