#!/usr/bin/env python3

"""
Comprehensive image analysis tool for documentation:
1. Check for missing images
2. Find unused images
3. Detect "bad" characters in image paths
4. Find duplicate images
5. Check for bad characters in image references

Usage:
- --check-missing: check for missing images
- --find-unused: find unused images
- --show-duplicates: show duplicate images
- --dry-run: run without making changes
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
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
ROOT_DIR = os.path.dirname(os.path.dirname(SCRIPT_DIR))  # Go up two levels to reach the root
DOCS_DIR = os.path.join(ROOT_DIR, "docs")
ASSETS_DIR = os.path.join(ROOT_DIR, ".gitbook/assets")

# Bad characters that shouldn't be in file paths
BAD_CHARS = [' ', '(', ')', '[', ']', '{', '}', '%', '+', '&', '#', '@', '!', '$', '*', ';', ':', ',', '?', '.']

def normalize_path(path):
    """
    Normalize path: remove escaped characters, decode URL
    """
    # Remove escape characters
    unescaped = path.replace('\\(', '(').replace('\\)', ')').replace('\\[', '[').replace('\\]', ']')
    # Decode URL-encoded characters
    decoded = urllib.parse.unquote(unescaped)
    return decoded

def get_file_hash(file_path):
    """
    Calculate file hash for detecting duplicates
    """
    try:
        with open(file_path, 'rb') as f:
            file_hash = hashlib.sha256()
            # Read in 64kb chunks for efficient processing of large files
            for chunk in iter(lambda: f.read(65536), b''):
                file_hash.update(chunk)
            return file_hash.hexdigest()
    except Exception as e:
        print(f"Error calculating hash for {file_path}: {str(e)}")
        return None

def find_actual_file(base_path, image_path):
    """
    Find the actual image file, handling various path formats
    """
    # Extract path from angle brackets, if present
    if "<" in image_path and ">" in image_path:
        image_path = re.search(r"<([^>]+)>", image_path).group(1)
    
    # Normalize path
    normalized_path = normalize_path(image_path)
    
    # Try direct access to the file
    direct_path = os.path.join(os.path.dirname(base_path), image_path)
    if os.path.exists(direct_path):
        return direct_path
    
    # Try with normalized path
    normalized_direct_path = os.path.join(os.path.dirname(base_path), normalized_path)
    if os.path.exists(normalized_direct_path):
        return normalized_direct_path
    
    # For paths with relative references "../"
    if image_path.startswith("../"):
        # Get filename
        img_filename = os.path.basename(image_path)
        norm_img_filename = os.path.basename(normalized_path)
        
        # Check if file exists directly in assets directory
        assets_path = os.path.join(ASSETS_DIR, img_filename)
        if os.path.exists(assets_path):
            return assets_path
            
        norm_assets_path = os.path.join(ASSETS_DIR, norm_img_filename)
        if os.path.exists(norm_assets_path):
            return norm_assets_path
    
    # Try encoding spaces
    encoded_path = image_path.replace(" ", "%20")
    direct_encoded_path = os.path.join(os.path.dirname(base_path), encoded_path)
    if os.path.exists(direct_encoded_path):
        return direct_encoded_path
    
    # Extract only the filename and check if it exists in assets directory
    img_filename = os.path.basename(image_path)
    norm_img_filename = os.path.basename(normalized_path)
    
    # Check in assets directory
    assets_path = os.path.join(ASSETS_DIR, img_filename)
    if os.path.exists(assets_path):
        return assets_path
    
    norm_assets_path = os.path.join(ASSETS_DIR, norm_img_filename)
    if os.path.exists(norm_assets_path):
        return norm_assets_path
    
    # For filenames with dates and times (screenshots)
    if ("screen-shot" in norm_img_filename.lower() or "screenshot" in norm_img_filename.lower()) and "at" in norm_img_filename.lower():
        # Convert dots to hyphens in timestamps
        hyphen_version = re.sub(r'(\d+)\.(\d+)\.(\d+)', r'\1-\2-\3', norm_img_filename)
        hyphen_path = os.path.join(ASSETS_DIR, hyphen_version)
        if os.path.exists(hyphen_path):
            return hyphen_path
    
    # Special handling for complex filenames like "image (11) (2) (1).png"
    if "image" in norm_img_filename.lower() and "(" in norm_img_filename:
        # Normalize format, remove extra spaces
        clean_name = re.sub(r'\s+', ' ', norm_img_filename)
        clean_path = os.path.join(ASSETS_DIR, clean_name)
        if os.path.exists(clean_path):
            return clean_path
            
        # If filename contains multiple bracket sets
        if norm_img_filename.count('(') > 1:
            # Try to find by primary number (first bracket set)
            primary_match = re.search(r'image\s*\((\d+)\)', norm_img_filename)
            if primary_match:
                primary_num = primary_match.group(1)
                # Search for files matching "image (NUM)*"
                for f in os.listdir(ASSETS_DIR):
                    if f.startswith(f'image ({primary_num})') or f.startswith(f'image({primary_num})'):
                        return os.path.join(ASSETS_DIR, f)
    
    # Fuzzy search among all files in assets directory
    if os.path.exists(ASSETS_DIR):
        # Get all files
        all_assets = [f for f in os.listdir(ASSETS_DIR) if os.path.isfile(os.path.join(ASSETS_DIR, f))]
        
        # Remove extension from search filename
        base_name, ext = os.path.splitext(norm_img_filename)
        
        # For complex filenames like "image (N)", extract base and number
        image_num_match = re.search(r'image\s*\((\d+)\)', base_name)
        if image_num_match:
            image_num = image_num_match.group(1)
            for asset in all_assets:
                # If file contains both "image" and the specific number, it's likely a match
                if "image" in asset.lower() and f"({image_num})" in asset:
                    return os.path.join(ASSETS_DIR, asset)
        
        # Search for similar filenames for other file types
        for asset in all_assets:
            asset_base, asset_ext = os.path.splitext(asset)
            
            # If extensions match and base part is exactly the same
            if asset_ext.lower() == ext.lower() and asset_base.lower() == base_name.lower():
                return os.path.join(ASSETS_DIR, asset)
    
    # If all attempts failed
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
                
                # 1. Pattern for HTML attributes src= and href=
                html_pattern = r'(?:src|href)=(?:"([^"]+)"|\'([^\']+)\'|<([^>]+)>)'
                html_matches = re.finditer(html_pattern, content)
                
                for match in html_matches:
                    # Get image path (from group 1, 2 or 3)
                    image_path = match.group(1) or match.group(2) or match.group(3)
                    
                    # Skip if not referencing assets
                    if not image_path or '.gitbook/assets' not in image_path:
                        continue
                    
                    # Check if it's an image by extension
                    _, ext = os.path.splitext(image_path.lower())
                    if ext in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                        # Extract filename
                        filename = os.path.basename(image_path)
                        
                        # Handle URL-encoded filenames
                        decoded_filename = urllib.parse.unquote(filename)
                        
                        # Add both versions
                        image_references.add(filename)
                        image_references.add(decoded_filename)
                
                # 2. Process standard image links in markdown format
                # Need line-by-line approach for correct handling of escaped characters
                content_lines = content.split('\n')
                for line in content_lines:
                    # Skip lines that don't contain potential image references
                    if '![' not in line or '.gitbook/assets' not in line:
                        continue
                        
                    # Use simplified pattern to extract path between () symbols
                    img_match = re.search(r'!\[.*?\]\((.*?\.gitbook/assets/.*?)\)', line)
                    if img_match:
                        image_path = img_match.group(1)
                        
                        # Extract filename
                        filename = os.path.basename(image_path)
                        
                        # Handle URL-encoded and escaped filenames
                        clean_filename = normalize_path(filename)
                        
                        # Add both versions
                        image_references.add(filename)
                        image_references.add(clean_filename)
            
            except Exception as e:
                print(f"Error processing file {file_path}: {str(e)}")
    
    print(f"Processed {processed_files} markdown files")
    return image_references

def check_missing_images():
    """
    Check for missing images in documentation
    """
    missing_images = []
    processed_files = 0
    total_images = 0
    
    # Walk through all markdown files in the documentation directory
    for root, _, files in os.walk(DOCS_DIR):
        for file in files:
            if not file.endswith('.md'):
                continue
                
            file_path = os.path.join(root, file)
            images_found, images_missing = process_file(file_path, missing_images)
                
            total_images += images_found
            processed_files += 1
    
    # Print summary
    print("\n=== SUMMARY ===")
    print(f"Processed files: {processed_files}")
    print(f"Total image references: {total_images}")
    print(f"Missing images found: {len(missing_images)}")
    
    if missing_images:
        print("\nMissing images:")
        for entry in missing_images:
            print(f"  {entry['image']} in file {entry['file']}")
        return False, missing_images
    
    print("\nAll images found successfully!")
    return True, []

def process_file(file_path, missing_images):
    """
    Process a single markdown file, check for missing image references
    Returns (total_images_found, total_missing)
    """
    total_found = 0
    total_missing = 0
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Process all types of image references
        
        # 1. Pattern for HTML attributes src= and href=
        html_pattern = r'(?:src|href)=(?:"([^"]+)"|\'([^\']+)\'|<([^>]+)>)'
        html_matches = re.finditer(html_pattern, content)
        
        for match in html_matches:
            # Get image path (from group 1, 2 or 3)
            image_path = match.group(1) or match.group(2) or match.group(3)
            
            # Skip if not referencing assets or not an image
            if not image_path or '.gitbook/assets' not in image_path:
                continue
            
            # Check if it's an image by extension
            _, ext = os.path.splitext(image_path.lower())
            if ext not in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                continue
                
            total_found += 1
            
            # Find the actual file
            actual_file = find_actual_file(file_path, image_path)
            if not actual_file:
                missing_images.append({
                    'file': file_path,
                    'image': image_path
                })
                total_missing += 1
        
        # 2. Standard image links in markdown format
        # Need line-by-line approach for correct handling of escaped characters
        content_lines = content.split('\n')
        for line in content_lines:
            # Skip lines that don't contain potential image references
            if '![' not in line or '.gitbook/assets' not in line:
                continue
                
            # Use simplified pattern to extract path between () symbols
            img_match = re.search(r'!\[.*?\]\((.*?\.gitbook/assets/.*?)\)', line)
            if img_match:
                image_path = img_match.group(1)
                
                total_found += 1
                
                # Find the actual file
                actual_file = find_actual_file(file_path, image_path)
                if not actual_file:
                    missing_images.append({
                        'file': file_path,
                        'image': image_path
                    })
                    total_missing += 1
    
    except Exception as e:
        print(f"Error processing file {file_path}: {str(e)}")
    
    return total_found, total_missing

def find_unused_images(show_duplicates=True):
    """
    Find unused images in the assets directory
    """
    # Collect all image references
    referenced_images = collect_all_image_references()
    print(f"Found {len(referenced_images)} unique image references in documentation")
    
    # Get all image files in assets directory
    all_images = []
    image_count_by_ext = {}
    hash_to_files = defaultdict(list)  # Map of content hashes to list of files with that hash
    
    if os.path.exists(ASSETS_DIR) and os.path.isdir(ASSETS_DIR):
        for filename in os.listdir(ASSETS_DIR):
            file_path = os.path.join(ASSETS_DIR, filename)
            if os.path.isfile(file_path):
                _, ext = os.path.splitext(filename.lower())
                if ext in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                    all_images.append(filename)
                    # Count by extension
                    image_count_by_ext[ext] = image_count_by_ext.get(ext, 0) + 1
                    
                    # Calculate content hash for duplicate detection
                    if show_duplicates:
                        file_hash = get_file_hash(file_path)
                        if file_hash:
                            hash_to_files[file_hash].append(file_path)
    
    print(f"Found {len(all_images)} images in {ASSETS_DIR}")
    
    # Print statistics by extension
    print("\nImages by extension:")
    for ext, count in sorted(image_count_by_ext.items(), key=lambda x: x[1], reverse=True):
        print(f"  {ext}: {count}")
    
    # Find duplicate images (same content but different names)
    duplicate_groups = []
    if show_duplicates:
        for file_hash, filenames in hash_to_files.items():
            if len(filenames) > 1:
                duplicate_groups.append(filenames)
        
        # Print duplicate statistics
        total_duplicates = sum(len(group) for group in duplicate_groups) - len(duplicate_groups)
        if duplicate_groups:
            print(f"\nFound {len(duplicate_groups)} duplicate groups ({total_duplicates} duplicate files)")
            if len(duplicate_groups) > 5:
                print("Showing first 5 groups:")
                for i, group in enumerate(sorted(duplicate_groups, key=len, reverse=True)[:5]):
                    if len(group) > 5:
                        print(f"  Group {i+1}: {len(group)} files with identical content - {', '.join(os.path.basename(f) for f in group[:3])}... and {len(group)-3} more")
                    else:
                        print(f"  Group {i+1}: {', '.join(os.path.basename(f) for f in group)}")
            else:
                for i, group in enumerate(duplicate_groups):
                    print(f"  Group {i+1}: {', '.join(os.path.basename(f) for f in group)}")
    
    # Find unused images
    unused_images = []
    uuid_pattern = re.compile(r'^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\.')
    
    for image in all_images:
        if image not in referenced_images:
            # Additional check for decoded version
            decoded = urllib.parse.unquote(image)
            if decoded not in referenced_images:
                unused_images.append(image)
    
    # Separate UUID-named files (likely duplicates from process_image_links.py)
    uuid_files = [f for f in unused_images if uuid_pattern.match(f)]
    regular_files = [f for f in unused_images if not uuid_pattern.match(f)]
    
    # Print results
    if unused_images:
        print(f"\nFound {len(unused_images)} unused images ({len(unused_images)/len(all_images):.1%} of total):")
        print(f"  - {len(uuid_files)} have UUID format names (likely created by process_image_links.py)")
        print(f"  - {len(regular_files)} have regular names")
        
        if regular_files:
            print("\nExamples of unused files with regular names:")
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
    else:
        print("\nAll images in the assets directory are used in documentation.")
        
    return unused_images

def is_bad_filename(filename):
    """
    Check if a filename is considered "bad" based on our criteria:
    - Contains spaces or %20
    - Contains parentheses, brackets, or other special characters
    - Has uppercase extensions
    - Contains dots in the name (not the extension)
    """
    decoded_name = urllib.parse.unquote(filename)
    name_lower = decoded_name.lower()
    
    # Check for uppercase extensions
    name, ext = os.path.splitext(decoded_name)
    if ext != ext.lower():
        return True
    
    # Check specifically for dots in the name (not the extension)
    if '.' in name:
        return True
    
    # Check for bad characters
    for char in BAD_CHARS:
        if char in name:  # Only check the name part, not the extension
            return True
            
    # Check for URL-encoded spaces
    if "%20" in filename:
        return True
        
    return False

def check_bad_references():
    """
    Check for any image references that contain bad characters
    Returns list of tuples (file_path, line_number, reference)
    """
    bad_references = []
    
    # Walk through all markdown files
    for root, _, files in os.walk(DOCS_DIR):
        for file in files:
            if not file.endswith('.md'):
                continue
                
            file_path = os.path.join(root, file)
            
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    content_lines = f.readlines()
                
                for line_num, line in enumerate(content_lines, 1):
                    # Skip lines that don't contain potential image references
                    if '.gitbook/assets' not in line:
                        continue
                    
                    # Check HTML-style references
                    html_pattern = r'(?:src|href)=(?:"([^"]+)"|\'([^\']+)\'|<([^>]+)>)'
                    for match in re.finditer(html_pattern, line):
                        # Get image path (from group 1, 2 or 3)
                        image_path = match.group(1) or match.group(2) or match.group(3)
                        
                        # Skip if not referencing assets
                        if not image_path or '.gitbook/assets' not in image_path:
                            continue
                        
                        # Extract filename
                        filename = os.path.basename(image_path)
                        
                        # Check if filename is bad
                        if is_bad_filename(filename):
                            bad_references.append((file_path, line_num, line.strip()))
                    
                    # Check markdown-style references
                    if '![' in line:
                        # Try to extract path between () symbols
                        img_match = re.search(r'!\[.*?\]\((.*?\.gitbook/assets/.*?)\)', line)
                        if img_match:
                            image_path = img_match.group(1)
                            filename = os.path.basename(image_path)
                            
                            # Check if filename is bad
                            if is_bad_filename(filename):
                                bad_references.append((file_path, line_num, line.strip()))
            
            except Exception as e:
                print(f"Error processing file {file_path}: {str(e)}")
    
    return bad_references

def main():
    """
    Main function to run all checks
    """
    print("=== CHECKING FOR MISSING IMAGES ===")
    missing_ok, missing_images = check_missing_images()
    print()
    
    print("=== FINDING UNUSED IMAGES ===")
    unused = find_unused_images(show_duplicates=True)
    unused_ok = len(unused) == 0
    print()
    
    print("=== CHECKING FOR BAD REFERENCES ===")
    bad_refs = check_bad_references()
    bad_refs_ok = len(bad_refs) == 0
    
    if bad_refs:
        print(f"\nFound {len(bad_refs)} references with bad characters:")
        for file_path, line_num, line in bad_refs:
            print(f"\nFile: {file_path}:{line_num}")
            print(f"Line: {line}")
    else:
        print("\nNo references with bad characters found.")
    print()
    
    # Print overall result
    print("\n=== OVERALL RESULT ===")
    all_successful = all([missing_ok, unused_ok, bad_refs_ok])
    
    if all_successful:
        print("✅ All checks passed successfully!")
    else:
        print("⚠️ Some checks found issues.")
        if not missing_ok:
            print("  ❌ Missing images found")
        if not unused_ok:
            print("  ❌ Unused images found")
        if not bad_refs_ok:
            print("  ❌ References with bad characters found")
        
        sys.exit(1)

if __name__ == "__main__":
    main() 