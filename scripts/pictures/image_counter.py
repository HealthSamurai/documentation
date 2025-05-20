#!/usr/bin/env python3

import os
import re
from collections import defaultdict
from urllib.parse import urljoin

# Base URL for documentation
BASE_URL = "https://docs.aidbox.app"

# Root directory for the docs
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
ROOT_DIR = os.path.dirname(os.path.dirname(SCRIPT_DIR))  # Go up two levels to reach the root
DOCS_DIR = os.path.join(ROOT_DIR, "docs")

def count_images_in_file(file_path):
    """
    Count image references in a file
    Returns tuple (count, list of image paths)
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # Find all image references
        image_patterns = [
            # Match src="..." or href="..." with any image extension
            r'(?:src|href)=["\']([^"\']*\.(?:png|jpg|jpeg|gif|svg|webp|bmp|tiff))["\']',
            # Match src='...' or href='...' with any image extension
            r'(?:src|href)=\'([^\']*\.(?:png|jpg|jpeg|gif|svg|webp|bmp|tiff))\'',
            # Match src=<...> or href=<...> with any image extension
            r'(?:src|href)=<([^>]*\.(?:png|jpg|jpeg|gif|svg|webp|bmp|tiff))>',
            # Match markdown image links with any image extension
            r'!\[.*?\]\(([^)]*\.(?:png|jpg|jpeg|gif|svg|webp|bmp|tiff))\)'
        ]

        images = []
        for pattern in image_patterns:
            matches = re.finditer(pattern, content, re.DOTALL | re.IGNORECASE)
            for match in matches:
                image_path = match.group(1)
                if image_path:
                    # Skip if path contains .gitbook/assets
                    if '.gitbook/assets' not in image_path:
                        images.append(image_path)

        return len(images), images

    except Exception as e:
        print(f"Error processing file {file_path}: {str(e)}")
        return 0, []

def get_doc_url(file_path):
    """
    Convert file path to documentation URL
    """
    # Get relative path from docs directory
    rel_path = os.path.relpath(file_path, DOCS_DIR)
    # Convert to URL path
    url_path = rel_path.replace('\\', '/')
    # Remove .md extension
    url_path = os.path.splitext(url_path)[0]
    # Join with base URL
    return urljoin(BASE_URL, url_path)

def main():
    # Dictionary to store file info: {url: (count, [images])}
    file_info = {}

    # Walk through all markdown files
    for root, _, files in os.walk(DOCS_DIR):
        for file in files:
            if not file.endswith('.md'):
                continue

            file_path = os.path.join(root, file)
            count, images = count_images_in_file(file_path)
            
            if count > 0:
                url = get_doc_url(file_path)
                file_info[url] = (count, images)

    # Sort files by image count (descending)
    sorted_files = sorted(file_info.items(), key=lambda x: x[1][0], reverse=True)

    # Print results
    print(f"\nFound {len(sorted_files)} files with non-.gitbook/assets images")
    print("\nFiles sorted by number of images (descending):")
    print("-" * 80)
    
    for url, (count, images) in sorted_files:
        print(f"\n{url}")
        print(f"Images: {count}")
        if count > 0:
            print("Image paths:")
            for img in sorted(set(images)):  # Remove duplicates and sort
                print(f"  - {img}")

if __name__ == "__main__":
    main() 