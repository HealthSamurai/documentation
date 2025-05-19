#!/usr/bin/env python3

"""
This script identifies unused image assets in the documentation.
It compiles a list of all image files in the .gitbook/assets directory,
then scans all markdown files to find which images are referenced.
Any images not referenced in the documentation are reported as unused.

The script also detects duplicate images (same content but different names)
to help identify images created by the process_image_links.py script.
"""

import os
import re
import urllib.parse
from pathlib import Path
import sys
import hashlib
import argparse
from collections import defaultdict

# Root directory for the docs
DOCS_DIR = "docs"
ASSETS_DIR = ".gitbook/assets"

def get_file_hash(file_path):
    """
    Calculate SHA256 hash of a file to identify duplicates with different names
    """
    try:
        with open(file_path, 'rb') as f:
            file_hash = hashlib.sha256()
            # Read in 64kb chunks to handle large files efficiently
            for chunk in iter(lambda: f.read(65536), b''):
                file_hash.update(chunk)
            return file_hash.hexdigest()
    except Exception as e:
        print(f"Error hashing file {file_path}: {str(e)}")
        return None

def collect_all_image_references():
    """
    Collect all image references from markdown files
    """
    image_references = set()
    processed_files = 0
    
    # Walk through all markdown files
    for root, _, files in os.walk(DOCS_DIR):
        for file in files:
            if not file.endswith('.md'):
                continue
                
            file_path = os.path.join(root, file)
            processed_files += 1
            
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                # Find all image references using different patterns
                
                # 1. Pattern for src="path" or src=<path> format
                src_pattern = r'src=(?:"([^"]+)"|<([^>]+)>)'
                src_matches = re.finditer(src_pattern, content)
                
                for match in src_matches:
                    # Get the image path (from either group 1 or 2)
                    image_path = match.group(1) if match.group(1) else match.group(2)
                    
                    # Skip if not referencing assets
                    if '.gitbook/assets' not in image_path:
                        continue
                    
                    # Extract the filename from the path
                    filename = os.path.basename(image_path)
                    
                    # Handle URL-encoded filenames
                    decoded_filename = urllib.parse.unquote(filename)
                    
                    # Add both encoded and decoded versions
                    image_references.add(filename)
                    image_references.add(decoded_filename)
                    
                    # Handle paths with angle brackets
                    if "<" in filename and ">" in filename:
                        clean_filename = re.search(r"<([^>]+)>", filename).group(1)
                        image_references.add(clean_filename)
                        image_references.add(urllib.parse.unquote(clean_filename))
                
                # 2. Pattern for standard markdown image format: ![alt text](path)
                md_pattern = r'!\[.*?\]\(([^)]+)\)'
                md_matches = re.finditer(md_pattern, content)
                
                for match in md_matches:
                    image_path = match.group(1)
                    
                    # Skip if not referencing assets
                    if '.gitbook/assets' not in image_path:
                        continue
                    
                    # Extract the filename from the path
                    filename = os.path.basename(image_path)
                    
                    # Handle URL-encoded filenames
                    decoded_filename = urllib.parse.unquote(filename)
                    
                    # Add both encoded and decoded versions
                    image_references.add(filename)
                    image_references.add(decoded_filename)
                
                # 3. Pattern for HTML href attributes that point to image files
                # This will catch ANY href that points to an image, regardless of HTML structure
                href_pattern = r'href=(?:"([^"]+)"|\'([^\']+)\')'
                href_matches = re.finditer(href_pattern, content)
                
                for match in href_matches:
                    href_path = match.group(1) if match.group(1) else match.group(2)
                    
                    # Skip if not referencing assets
                    if '.gitbook/assets' not in href_path:
                        continue
                    
                    # Check if it's an image file by extension
                    _, ext = os.path.splitext(href_path.lower())
                    if ext in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                        # Extract the filename from the path
                        filename = os.path.basename(href_path)
                        
                        # Handle URL-encoded filenames
                        decoded_filename = urllib.parse.unquote(filename)
                        
                        # Add both encoded and decoded versions
                        image_references.add(filename)
                        image_references.add(decoded_filename)
            
            except Exception as e:
                print(f"Error processing file {file_path}: {str(e)}")
    
    print(f"Processed {processed_files} markdown files")
    return image_references

def find_unused_images(show_duplicates=True):
    """
    Find images in the assets directory that aren't referenced in docs
    Also identifies duplicate images (same content but different names)
    """
    # Collect all image references
    referenced_images = collect_all_image_references()
    print(f"Found {len(referenced_images)} unique image references in documentation")
    
    # Get all image files in the assets directory
    all_images = []
    image_count_by_ext = {}
    hash_to_files = defaultdict(list)  # Maps content hash to list of files with that hash
    
    if os.path.exists(ASSETS_DIR) and os.path.isdir(ASSETS_DIR):
        for filename in os.listdir(ASSETS_DIR):
            file_path = os.path.join(ASSETS_DIR, filename)
            if os.path.isfile(file_path):
                _, ext = os.path.splitext(filename.lower())
                if ext in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                    all_images.append(filename)
                    # Track count by extension
                    image_count_by_ext[ext] = image_count_by_ext.get(ext, 0) + 1
                    
                    # Calculate content hash for duplicate detection
                    if show_duplicates:
                        file_hash = get_file_hash(file_path)
                        if file_hash:
                            hash_to_files[file_hash].append(filename)
    
    print(f"Found {len(all_images)} images in {ASSETS_DIR}")
    
    # Print breakdown by extension
    print("\nImages by extension:")
    for ext, count in sorted(image_count_by_ext.items(), key=lambda x: x[1], reverse=True):
        print(f"  {ext}: {count}")
    
    # Find duplicate images (same content hash but different names)
    duplicate_groups = []
    if show_duplicates:
        for file_hash, filenames in hash_to_files.items():
            if len(filenames) > 1:
                duplicate_groups.append(filenames)
        
        # Print duplicate stats
        total_duplicates = sum(len(group) for group in duplicate_groups) - len(duplicate_groups)
        if duplicate_groups:
            print(f"\nFound {len(duplicate_groups)} groups of duplicate images ({total_duplicates} duplicate files)")
            if len(duplicate_groups) > 5:
                print("Showing first 5 groups:")
                for i, group in enumerate(sorted(duplicate_groups, key=len, reverse=True)[:5]):
                    if len(group) > 5:
                        print(f"  Group {i+1}: {len(group)} files with same content - {', '.join(group[:3])}... and {len(group)-3} more")
                    else:
                        print(f"  Group {i+1}: {', '.join(group)}")
            else:
                for i, group in enumerate(duplicate_groups):
                    print(f"  Group {i+1}: {', '.join(group)}")
    
    # Find unused images
    unused_images = []
    uuid_pattern = re.compile(r'^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\.')
    
    for image in all_images:
        if image not in referenced_images:
            # Double check a decoded version isn't referenced
            decoded = urllib.parse.unquote(image)
            if decoded not in referenced_images:
                unused_images.append(image)
    
    # Separate UUID-named files (likely duplicates from process_image_links.py)
    uuid_files = [f for f in unused_images if uuid_pattern.match(f)]
    regular_files = [f for f in unused_images if not uuid_pattern.match(f)]
    
    # Print results
    if unused_images:
        print(f"\nFound {len(unused_images)} unused images ({len(unused_images)/len(all_images):.1%} of total):")
        print(f"  - {len(uuid_files)} have UUID names (likely created by process_image_links.py)")
        print(f"  - {len(regular_files)} have regular names")
        
        if regular_files:
            print("\nSample of unused files with regular names:")
            for image in sorted(regular_files)[:10]:
                print(f"  {os.path.join(ASSETS_DIR, image)}")
            if len(regular_files) > 10:
                print(f"  ... and {len(regular_files) - 10} more")
        
        if uuid_files and len(uuid_files) <= 10:
            print("\nUnused files with UUID names (likely duplicates):")
            for image in sorted(uuid_files):
                print(f"  {os.path.join(ASSETS_DIR, image)}")
        elif uuid_files:
            print(f"\nThere are {len(uuid_files)} unused files with UUID names (likely duplicates)")
            
        return unused_images
    else:
        print("\nAll images in the assets directory are referenced in the documentation.")
        return []

def remove_duplicates():
    """
    Find and remove duplicate image files based on content hash
    Keep one file from each duplicate group and delete the rest
    """
    hash_to_files = defaultdict(list)
    
    # Calculate hashes for all image files
    print(f"Scanning {ASSETS_DIR} for duplicate images...")
    for filename in os.listdir(ASSETS_DIR):
        file_path = os.path.join(ASSETS_DIR, filename)
        if os.path.isfile(file_path):
            _, ext = os.path.splitext(filename.lower())
            if ext in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                file_hash = get_file_hash(file_path)
                if file_hash:
                    hash_to_files[file_hash].append(file_path)
    
    # Find duplicate groups
    duplicate_groups = [files for files in hash_to_files.values() if len(files) > 1]
    
    if not duplicate_groups:
        print("No duplicate images found.")
        return
    
    # Count total duplicates
    total_duplicates = sum(len(group) - 1 for group in duplicate_groups)
    print(f"Found {len(duplicate_groups)} groups with {total_duplicates} duplicate files")
    
    # Regular expression to identify UUID-named files
    uuid_pattern = re.compile(r'[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\.')
    
    files_to_delete = []
    
    # Process each duplicate group
    for group in duplicate_groups:
        # First, look for files that are referenced in docs
        referenced_files = []
        for file_path in group:
            filename = os.path.basename(file_path)
            # Check if file is referenced (would need to call collect_all_image_references)
            # For now, prioritize keeping non-UUID files
            if not uuid_pattern.search(filename):
                referenced_files.append(file_path)
        
        # If we found referenced files, keep those and delete the rest
        # Otherwise, keep the shortest named file (likely the original)
        if referenced_files:
            keeper = referenced_files[0]  # Keep the first referenced file
        else:
            keeper = min(group, key=lambda x: len(os.path.basename(x)))  # Keep shortest name
        
        # Mark all other files in this group for deletion
        for file_path in group:
            if file_path != keeper:
                files_to_delete.append(file_path)
    
    # Report on what would be deleted
    print(f"\nWould delete {len(files_to_delete)} duplicate files, for example:")
    for file_path in files_to_delete[:5]:
        print(f"  {file_path}")
    if len(files_to_delete) > 5:
        print(f"  ... and {len(files_to_delete) - 5} more")
    
    # Since this is destructive, require explicit confirmation before proceeding
    print("\n⚠️ This operation would permanently delete files.")
    print("Use --cleanup-duplicates to run the actual deletion.")
    
    return files_to_delete

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Find unused and duplicate images in the documentation")
    parser.add_argument("--no-duplicates", action="store_true", help="Skip duplicate detection (faster)")
    parser.add_argument("--cleanup-duplicates", action="store_true", help="Remove duplicate images (DESTRUCTIVE)")
    args = parser.parse_args()
    
    if args.cleanup_duplicates:
        remove_duplicates()
    else:
        unused_images = find_unused_images(show_duplicates=not args.no_duplicates)
        
        # Write results to file
        output_dir = "out"
        if not os.path.exists(output_dir):
            os.makedirs(output_dir)
            
        with open(os.path.join(output_dir, "unused_images.txt"), "w") as f:
            for image in sorted(unused_images):
                f.write(f"{os.path.join(ASSETS_DIR, image)}\n")
        
        if unused_images:
            print(f"\nList of unused images saved to out/unused_images.txt")
            print("These images can be safely deleted if they're not needed elsewhere.")
            print("\nTIP: Run with --cleanup-duplicates to remove duplicate images") 