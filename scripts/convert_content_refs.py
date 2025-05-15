#!/usr/bin/env python3
import os
import re

def extract_title_from_content(content):
    # Skip YAML frontmatter if present
    if content.startswith('---'):
        parts = content.split('---', 2)
        if len(parts) >= 3:
            content = parts[2].lstrip()
    
    # Try to find the first markdown header
    header_match = re.search(r'^#\s+(.+)$', content, re.MULTILINE)
    if header_match:
        return header_match.group(1).strip()
    
    # If no header found, try to find the first non-empty line
    lines = content.split('\n')
    for line in lines:
        line = line.strip()
        if line and not line.startswith('---'):
            return line
    
    return "Untitled"

def get_title_from_file(file_path):
    try:
        # Check if path is a directory
        if os.path.isdir(file_path):
            # Try to read README.md in the directory
            readme_path = os.path.join(file_path, 'README.md')
            if os.path.isfile(readme_path):
                with open(readme_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                return extract_title_from_content(content)
            return os.path.basename(file_path).replace('-', ' ').title()
        
        # Regular file
        if os.path.isfile(file_path):
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            return extract_title_from_content(content)
        
        # File doesn't exist
        return os.path.basename(file_path).replace('-', ' ').title()
    
    except Exception as e:
        print(f"Warning: Could not read title from {file_path}: {e}")
        return os.path.basename(file_path).replace('-', ' ').title()

def convert_content_ref_to_link(match, base_path):
    # Extract URL from the content-ref block
    url = re.search(r'url="([^"]+)"', match.group(0)).group(1)
    
    # Construct absolute path to the referenced file
    ref_path = os.path.normpath(os.path.join(os.path.dirname(base_path), url))
    
    # Get title from the referenced file
    title = get_title_from_file(ref_path)
    
    # Create markdown list item with link
    return f'* [{title}]({url})'

def process_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Pattern to match content-ref blocks
    pattern = r'{% content-ref url="[^"]+?" %}\s*\[[^\]]+?\]\([^)]+?\)\s*{% endcontent-ref %}'

    # Replace all matches with markdown list links
    # Using lambda to pass the current file path to convert_content_ref_to_link
    new_content = re.sub(pattern, lambda m: convert_content_ref_to_link(m, file_path), content)

    # Only write if changes were made
    if new_content != content:
        print(f"Processing: {file_path}")
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    # Start from docs directory
    docs_dir = "docs"
    files_processed = 0
    files_modified = 0

    # Walk through all markdown files
    for root, _, files in os.walk(docs_dir):
        for file in files:
            if file.endswith('.md'):
                files_processed += 1
                file_path = os.path.join(root, file)
                if process_file(file_path):
                    files_modified += 1

    print(f"\nSummary:")
    print(f"Files processed: {files_processed}")
    print(f"Files modified: {files_modified}")

if __name__ == "__main__":
    main() 