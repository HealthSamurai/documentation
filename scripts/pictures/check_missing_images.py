#!/usr/bin/env python3

"""
This script checks for missing image files referenced in documentation.
It scans all markdown files for image references and verifies that the referenced files exist.
Supports multi-product documentation structure with docs-new/ directory.
"""

import os
import re
import urllib.parse
from pathlib import Path
import sys
import yaml

# Root directories for the docs
DOCS_DIR = "docs"
DOCS_NEW_DIR = "docs-new"
ASSETS_DIR = ".gitbook/assets"

def load_products_config():
    """Load products configuration from docs-new/products.yaml"""
    products_yaml_path = os.path.join(DOCS_NEW_DIR, "products.yaml")
    if not os.path.exists(products_yaml_path):
        return None
    
    try:
        with open(products_yaml_path, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)
    except Exception as e:
        print(f"Warning: Could not load products.yaml: {str(e)}")
        return None

def find_actual_file(base_path, image_path, product_path=None):
    """
    Find the actual image file, handling various formats:
    - Regular paths
    - URL-encoded paths
    - Paths with angle brackets
    - Different timestamp formats (dot vs hyphen)
    - Multi-product structure (checks both root and product-specific assets)
    """
    # Extract path from angle brackets if present
    if "<" in image_path and ">" in image_path:
        image_path = re.search(r"<([^>]+)>", image_path).group(1)
    
    # List of possible asset directories to check
    asset_dirs = [ASSETS_DIR]
    if product_path:
        # For products in docs-new, also check product-specific assets
        product_assets = os.path.join(product_path, ".gitbook/assets")
        if os.path.exists(product_assets):
            asset_dirs.append(product_assets)
    
    # Try direct path - using normpath to handle "../" references properly
    direct_path = os.path.join(os.path.dirname(base_path), image_path)
    if os.path.exists(direct_path):
        return direct_path
    
    # For paths with multiple "../" references, try to resolve them absolutely
    if image_path.startswith("../"):
        # Get the image filename
        img_filename = os.path.basename(image_path)
        # Check in all possible asset directories
        for assets_dir in asset_dirs:
            assets_path = os.path.join(assets_dir, img_filename)
            if os.path.exists(assets_path):
                return assets_path
    
    # Try decoding URL-encoded path
    decoded_path = urllib.parse.unquote(image_path)
    direct_decoded_path = os.path.join(os.path.dirname(base_path), decoded_path)
    if os.path.exists(direct_decoded_path):
        return direct_decoded_path
    
    # For decoded paths with "../" references
    if decoded_path.startswith("../"):
        # Get the image filename
        img_filename = os.path.basename(decoded_path)
        # Check in all possible asset directories
        for assets_dir in asset_dirs:
            assets_path = os.path.join(assets_dir, img_filename)
            if os.path.exists(assets_path):
                return assets_path
    
    # Try encoding spaces in path
    encoded_path = image_path.replace(" ", "%20")
    direct_encoded_path = os.path.join(os.path.dirname(base_path), encoded_path)
    if os.path.exists(direct_encoded_path):
        return direct_encoded_path
    
    # Final attempt - check if the image exists directly in any assets dir
    # Extract just the filename and check in the assets directories
    img_filename = os.path.basename(image_path)
    for assets_dir in asset_dirs:
        assets_path = os.path.join(assets_dir, img_filename)
        if os.path.exists(assets_path):
            return assets_path
        
        # Also try decoded filename
        decoded_filename = urllib.parse.unquote(img_filename)
        assets_decoded_path = os.path.join(assets_dir, decoded_filename)
        if os.path.exists(assets_decoded_path):
            return assets_decoded_path
    
    # Handle timestamp format differences (dots vs hyphens)
    # For example: screen-shot-2021-08-06-at-04-24-22.png vs screen-shot-2021-08-06-at-04.24.22.png
    if "screen-shot" in img_filename and "at" in img_filename:
        for assets_dir in asset_dirs:
            # Try converting dots to hyphens in timestamps
            hyphen_version = re.sub(r'(\d+)\.(\d+)\.(\d+)', r'\1-\2-\3', img_filename)
            hyphen_path = os.path.join(assets_dir, hyphen_version)
            if os.path.exists(hyphen_path):
                return hyphen_path
                
            # Try converting hyphens to dots in timestamps
            dots_version = re.sub(r'at-(\d+)-(\d+)-(\d+)', r'at-\1.\2.\3', img_filename)
            dots_path = os.path.join(assets_dir, dots_version)
            if os.path.exists(dots_path):
                return dots_path
    
    # Try a fuzzy search among all files in the assets directories
    for assets_dir in asset_dirs:
        if os.path.exists(assets_dir):
            # Get all files in assets directory
            all_assets = [f for f in os.listdir(assets_dir) if os.path.isfile(os.path.join(assets_dir, f))]
            
            # Remove extension from the filename we're looking for
            base_name, ext = os.path.splitext(img_filename)
            
            # Look for similar filenames
            for asset in all_assets:
                asset_base, asset_ext = os.path.splitext(asset)
                
                # If the extensions match and the base part is similar enough
                if asset_ext.lower() == ext.lower() and (
                    asset_base.lower() == base_name.lower() or
                    asset_base.replace("-", ".").lower() == base_name.replace("-", ".").lower() or
                    asset_base.replace(".", "-").lower() == base_name.replace(".", "-").lower()
                ):
                    return os.path.join(assets_dir, asset)
    
    return None

def check_files():
    """
    Main function to check all markdown files and their image references
    """
    missing_images = []
    processed_files = 0
    total_images = 0
    
    # Check main docs directory
    print("Checking main docs directory...")
    if os.path.exists(DOCS_DIR):
        for root, _, files in os.walk(DOCS_DIR):
            for file in files:
                if not file.endswith('.md'):
                    continue
                    
                file_path = os.path.join(root, file)
                images_found, images_missing = process_file(file_path, missing_images)
                total_images += images_found
                processed_files += 1
    
    # Check multi-product docs-new directory
    products_config = load_products_config()
    if products_config and os.path.exists(DOCS_NEW_DIR):
        print("\nChecking multi-product documentation...")
        for product in products_config.get('products', []):
            product_id = product['id']
            product_path = os.path.join(DOCS_NEW_DIR, product_id)
            
            if not os.path.exists(product_path):
                print(f"Warning: Product directory not found: {product_path}")
                continue
            
            print(f"  Checking product: {product_id}")
            product_docs_dir = os.path.join(product_path, 'docs')
            
            if os.path.exists(product_docs_dir):
                for root, _, files in os.walk(product_docs_dir):
                    for file in files:
                        if not file.endswith('.md'):
                            continue
                        
                        file_path = os.path.join(root, file)
                        images_found, images_missing = process_file(
                            file_path, missing_images, product_path=product_path
                        )
                        total_images += images_found
                        processed_files += 1
    
    # Print summary
    print("\n=== SUMMARY ===")
    print(f"Files processed: {processed_files}")
    print(f"Total images referenced: {total_images}")
    print(f"Missing images found: {len(missing_images)}")
    
    if missing_images:
        print("\nMissing images:")
        for entry in missing_images:
            print(f"  {entry['image']} referenced in {entry['file']}")
        return False
    
    print("\nAll images found successfully!")
    return True

def process_file(file_path, missing_images, product_path=None):
    """
    Process a single markdown file, checking for missing image references
    Returns (total_images_found, total_images_missing)
    """
    total_found = 0
    total_missing = 0
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Skip checking for external images (URLs starting with http)
        def is_external_url(path):
            return path.startswith('http://') or path.startswith('https://')
        
        # Process all image reference types
        
        # 1. Pattern for src="path" or src=<path> format (HTML style)
        src_pattern = r'src=(?:"([^"]+)"|<([^>]+)>)'
        src_matches = re.finditer(src_pattern, content)
        
        for match in src_matches:
            # Get the image path (from either group 1 or 2)
            image_path = match.group(1) if match.group(1) else match.group(2)
            
            # Skip external URLs
            if is_external_url(image_path):
                continue
            
            # Skip if not an image or not referencing assets
            if '.gitbook/assets' not in image_path:
                # Check if it might be an image by extension
                _, ext = os.path.splitext(image_path.lower())
                if ext not in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                    continue
            
            total_found += 1
            
            # Find the actual file
            actual_file = find_actual_file(file_path, image_path, product_path)
            if not actual_file:
                missing_images.append({
                    'file': file_path,
                    'image': image_path
                })
                total_missing += 1
        
        # 2. Pattern for standard markdown image format: ![alt text](path)
        md_pattern = r'!\[.*?\]\(([^)]+)\)'
        md_matches = re.finditer(md_pattern, content)
        
        for match in md_matches:
            image_path = match.group(1)
            
            # Skip external URLs
            if is_external_url(image_path):
                continue
            
            # Skip if not referencing assets (but still check for common image extensions)
            if '.gitbook/assets' not in image_path:
                # Check if it might be an image by extension
                _, ext = os.path.splitext(image_path.lower())
                if ext not in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                    continue
            
            total_found += 1
            
            # Find the actual file
            actual_file = find_actual_file(file_path, image_path, product_path)
            if not actual_file:
                missing_images.append({
                    'file': file_path,
                    'image': image_path
                })
                total_missing += 1
        
        # 3. Pattern for HTML href that references images
        href_pattern = r'href=(?:"([^"]+)"|\'([^\']+)\')'
        href_matches = re.finditer(href_pattern, content)
        
        for match in href_matches:
            href_path = match.group(1) if match.group(1) else match.group(2)
            
            # Skip external URLs
            if is_external_url(href_path):
                continue
            
            # Skip if not referencing assets
            if '.gitbook/assets' not in href_path:
                continue
            
            # Check if it's an image file by extension
            _, ext = os.path.splitext(href_path.lower())
            if ext in ['.png', '.jpg', '.jpeg', '.gif', '.svg', '.webp', '.bmp', '.tiff']:
                total_found += 1
                
                # Find the actual file
                actual_file = find_actual_file(file_path, href_path, product_path)
                if not actual_file:
                    missing_images.append({
                        'file': file_path,
                        'image': href_path
                    })
                    total_missing += 1
    
    except Exception as e:
        print(f"Error processing {file_path}: {str(e)}")
    
    return total_found, total_missing

if __name__ == "__main__":
    # Make script executable from any directory
    os.chdir(os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__)))))
    
    success = check_files()
    if not success:
        sys.exit(1)