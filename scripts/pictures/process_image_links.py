#!/usr/bin/env python3

"""
This script identifies and fixes problematic image filenames in the documentation.
It performs the following operations:
1. Scans all markdown files for image references
2. Verifies each referenced image exists
3. Identifies "bad" filenames (with spaces, special chars, dots in names, etc.)
4. Renames images with UUID-based names or converts dots to hyphens
5. Updates all references in the documents to match the new filenames
"""

import os
import re
import uuid
import urllib.parse
import shutil
import sys
import argparse

# Root directory for the docs
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
ROOT_DIR = os.path.dirname(os.path.dirname(SCRIPT_DIR))  # Go up two levels to reach the root
DOCS_DIR = os.path.join(ROOT_DIR, "docs")
ASSETS_DIR = os.path.join(ROOT_DIR, ".gitbook/assets")

# Characters that make a filename "bad"
BAD_CHARS = [' ', '(', ')', '[', ']', '{', '}', '%', '+', '&', '#', '@', '!', '$', '*', ';', ':', ',', '?', '.']

def is_bad_filename(filename):
    """
    Check if a filename is considered "bad" based on our criteria:
    - Contains spaces or %20
    - Contains parentheses, brackets, or other special characters
    - Has uppercase extensions
    - Contains dots in the name (not the extension)
    """
    decoded_name = urllib.parse.unquote(filename)
    # name_lower = decoded_name.lower()

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

def convert_dots_to_hyphens(filename):
    """
    Convert all dots in the filename (except for the extension dot) to hyphens
    """
    # Split into name and extension
    name, ext = os.path.splitext(filename)

    # Replace dots with hyphens in the name part
    name = name.replace('.', '-')

    # Rejoin with the original extension
    return name + ext

def find_actual_file(base_path, image_path):
    """
    Find the actual image file, handling various path formats and encodings.
    Returns the absolute path to the file if found, None otherwise.
    """
    # Extract path from angle brackets if present
    if image_path.startswith('<') and image_path.endswith('>'):
        image_path = image_path[1:-1]

    # Handle escaped characters (like \(11\))
    unescaped_path = image_path.replace('\\(', '(').replace('\\)', ')').replace('\\[', '[').replace('\\]', ']')

    def try_path_variants(path):
        """Helper function to try different variants of a path"""
        variants = set()

        # Original path
        variants.add(path)

        # URL-decoded path
        decoded = urllib.parse.unquote(path)
        if decoded != path:
            variants.add(decoded)

        # Try both dot and hyphen variants
        name, ext = os.path.splitext(os.path.basename(path))
        if '.' in name:
            # If path has dots, try with hyphens
            variants.add(os.path.join(os.path.dirname(path), name.replace('.', '-') + ext))
        if '-' in name:
            # If path has hyphens, try with dots
            variants.add(os.path.join(os.path.dirname(path), name.replace('-', '.') + ext))

        # Try with spaces encoded/decoded
        if ' ' in path:
            variants.add(path.replace(' ', '%20'))
        if '%20' in path:
            variants.add(path.replace('%20', ' '))

        # Try with parentheses encoded/decoded
        if '(' in path or ')' in path:
            variants.add(path.replace('(', '%28').replace(')', '%29'))
        if '%28' in path or '%29' in path:
            variants.add(path.replace('%28', '(').replace('%29', ')'))

        return variants

    def try_path(path):
        """Helper function to try a single path variant"""
        # Try direct path
        direct_path = os.path.join(os.path.dirname(base_path), path)
        if os.path.exists(direct_path):
            return direct_path

        # Try in assets directory
        if '.gitbook/assets' in path:
            # Extract just the filename from the path
            filename = os.path.basename(path)
            assets_path = os.path.join(ASSETS_DIR, filename)
            if os.path.exists(assets_path):
                return assets_path

        # For paths with relative references "../"
        if path.startswith('../'):
            levels_up = path.count('../')
            base_dir = os.path.dirname(base_path)
            for _ in range(levels_up):
                base_dir = os.path.dirname(base_dir)
            resolved_path = os.path.join(base_dir, path.replace('../' * levels_up, ''))
            if os.path.exists(resolved_path):
                return resolved_path

        return None

    # First try the original path and its URL-decoded variant
    for variant in [image_path, urllib.parse.unquote(image_path)]:
        result = try_path(variant)
        if result:
            return result

    # Then try the unescaped path and its URL-decoded variant
    if unescaped_path != image_path:
        for variant in [unescaped_path, urllib.parse.unquote(unescaped_path)]:
            result = try_path(variant)
            if result:
                return result

    # Finally try all other variants (dot/hyphen replacements, space encoding, etc.)
    for variant in try_path_variants(image_path):
        if variant not in [image_path, urllib.parse.unquote(image_path)]:
            result = try_path(variant)
            if result:
                return result

    if unescaped_path != image_path:
        for variant in try_path_variants(unescaped_path):
            if variant not in [unescaped_path, urllib.parse.unquote(unescaped_path)]:
                result = try_path(variant)
                if result:
                    return result

    # Always try the decoded, unescaped filename directly in the assets directory
    decoded_unescaped_filename = os.path.basename(unescaped_path)
    decoded_unescaped_filename = urllib.parse.unquote(decoded_unescaped_filename)
    assets_path = os.path.join(ASSETS_DIR, decoded_unescaped_filename)
    if os.path.exists(assets_path):
        return assets_path

    # If we reach here, the file wasn't found
    return None

def rename_image_file(file_path, dry_run=False):
    """
    Rename an image file based on its issues:
    - If the file has bad characters (spaces, dots, etc.), rename to a UUID-based name
    """
    filename = os.path.basename(file_path)
    _, ext = os.path.splitext(filename)
    ext = ext.lower()  # Ensure lowercase extension

    # Always use UUID for bad filenames
    new_filename = f"{str(uuid.uuid4())}{ext}"

    # Get the directory of the original file
    dir_path = os.path.dirname(file_path)

    # Create the full path for the new file
    new_file_path = os.path.join(dir_path, new_filename)

    if dry_run:
        print(f"[DRY RUN] Would rename {os.path.basename(file_path)} to {new_filename}")
        # Return what the new path would be
        return new_file_path

    # SAFETY CHANGE: In the actual renaming, we ONLY create the new file
    # We never delete the original to prevent data loss
    try:
        # Copy the file to new name
        shutil.copy2(file_path, new_file_path)

        # Verify the copy worked
        if os.path.exists(new_file_path) and os.path.getsize(new_file_path) == os.path.getsize(file_path):
            print(f"✅ Created copy: {os.path.basename(file_path)} → {new_filename}")
            print(f"⚠️ Original file preserved at: {file_path}")
        else:
            print(f"❌ Error copying file: {file_path}")
            # If the copy failed, return the original path
            if os.path.exists(new_file_path):
                os.remove(new_file_path)  # Clean up partial copy
            return file_path
    except Exception as e:
        print(f"❌ Error creating new file {new_file_path}: {str(e)}")
        return file_path

    return new_file_path

def process_files(dry_run=False):
    """
    Main function to process all markdown files and their image references
    """
    renamed_files = {}  # Keep track of renamed files to avoid duplicates
    errors = []  # Keep track of errors
    not_found_images = set()  # Track missing images without duplicates

    if dry_run:
        print("🔍 DRY RUN MODE: No files will be modified")

    # Walk through all markdown files in the docs directory
    for root, _, files in os.walk(DOCS_DIR):
        for file in files:
            if not file.endswith('.md'):
                continue

            file_path = os.path.join(root, file)
            process_file(file_path, renamed_files, errors, not_found_images, dry_run)

    # Print summary
    print("\n=== SUMMARY ===")
    print(f"Total files that would be renamed: {len(renamed_files)}")

    if renamed_files:
        print("\nFiles to rename:")
        for old_path, new_path in renamed_files.items():
            print(f"  {os.path.basename(old_path)} → {os.path.basename(new_path)}")

    if not_found_images:
        print("\nMissing images:")
        for img in sorted(not_found_images):
            print(f"  {img}")
        return False

    if errors:
        print("\nErrors encountered:")
        for error in errors:
            print(f"  {error}")
        return False

    return True

def process_file(file_path, renamed_files, errors, not_found_images, dry_run=False):
    """
    Process a single markdown file, finding and fixing image references
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        new_content = content
        file_renamed_images = []

        # Process both types of image references

        # 1. Process HTML-style references with src attribute
        new_content = process_src_references(file_path, new_content, renamed_files,
                              errors, not_found_images, file_renamed_images, dry_run)

        # 2. Process standard markdown image references
        new_content = process_markdown_references(file_path, new_content, renamed_files,
                                               errors, not_found_images, file_renamed_images, dry_run)

        # Write the modified content back to the file
        if new_content != content and not dry_run:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"📄 Updated references in {file_path}:")
            for rename in file_renamed_images:
                print(f"  {rename}")
        elif new_content != content:
            print(f"[DRY RUN] Would update references in {file_path}:")
            for rename in file_renamed_images:
                print(f"  {rename}")

    except Exception as e:
        errors.append(f"Error processing {file_path}: {str(e)}")

def generate_all_path_variants(path):
    """
    Generate all possible variants of a path for matching/replacing:
    - original
    - url-decoded
    - url-encoded
    - spaces <-> %20
    - with/without escaped parentheses
    - with/without escaped brackets
    - all combinations
    """
    import itertools
    variants = set()
    # Start with original and url-decoded
    base_variants = {path, urllib.parse.unquote(path)}
    for base in base_variants:
        # Spaces <-> %20
        space_variants = {base, base.replace(' ', '%20'), base.replace('%20', ' ')}
        for sv in space_variants:
            # Escaped parentheses
            paren_variants = {sv,
                sv.replace('(', '\\(').replace(')', '\\)'),
                sv.replace('\\(', '(').replace('\\)', ')')
            }
            # Escaped brackets
            bracket_variants = set()
            for pv in paren_variants:
                bracket_variants |= {pv, pv.replace('[', '\\[').replace(']', '\\]'), pv.replace('\\[', '[').replace('\\]', ']')}
            # URL-encode the whole thing
            for bv in bracket_variants:
                variants.add(bv)
                variants.add(urllib.parse.quote(bv))
    # Remove empty and duplicates
    return set(filter(None, variants))

def process_src_references(file_path, content, renamed_files,
                          errors, not_found_images, file_renamed_images, dry_run=False):
    """
    Process HTML-style image references with src or href attributes, regardless of the HTML tag.
    Handles all encoding/escaping variants.
    """
    pattern = r'<[^>]*(?:src|href)\s*=\s*(?:"([^"]+)"|\'([^\']+)\'|<([^>]+)>)[^>]*>'
    matches = list(re.finditer(pattern, content))
    new_content = content

    for match in matches:
        orig_image_path = match.group(1) or match.group(2) or match.group(3)
        if not orig_image_path:
            continue

        # Generate all possible variants
        candidates = generate_all_path_variants(orig_image_path)
        for image_path in candidates:
            if '.gitbook/assets' not in image_path:
                continue
            # Normalize relative paths
            if image_path.startswith('../'):
                levels_up = image_path.count('../')
                base_dir = os.path.dirname(file_path)
                for _ in range(levels_up):
                    base_dir = os.path.dirname(base_dir)
                image_path = os.path.join(base_dir, image_path.replace('../' * levels_up, ''))
                image_path = os.path.relpath(image_path, os.path.dirname(file_path))
            actual_file = find_actual_file(file_path, image_path)
            if not actual_file:
                decoded_path = urllib.parse.unquote(image_path)
                actual_file = find_actual_file(file_path, decoded_path)
            if not actual_file:
                not_found_images.add(f"{image_path} referenced in {file_path}")
                continue
            filename = os.path.basename(actual_file)
            if is_bad_filename(filename):
                old_filename = filename
                if actual_file in renamed_files:
                    new_file_path = renamed_files[actual_file]
                else:
                    new_file_path = rename_image_file(actual_file, dry_run)
                    if new_file_path == actual_file:
                        errors.append(f"Failed to rename {filename} in {file_path}")
                        continue
                    renamed_files[actual_file] = new_file_path
                new_filename = os.path.basename(new_file_path)
                file_renamed_images.append(f"{old_filename} → {new_filename}")
                # Replace all variants in content
                for variant in generate_all_path_variants(orig_image_path) | generate_all_path_variants(old_filename):
                    tag_pattern = f'<[^>]*(?:src|href)\\s*=\\s*(?:".*{re.escape(variant)}.*"|\'.*{re.escape(variant)}.*\'|<.*{re.escape(variant)}.*>)[^>]*>'
                    for tag_match in re.finditer(tag_pattern, new_content):
                        old_tag = tag_match.group(0)
                        new_tag = old_tag.replace(variant, new_filename)
                        new_content = new_content.replace(old_tag, new_tag)
    return new_content

def process_markdown_references(file_path, content, renamed_files,
                               errors, not_found_images, file_renamed_images, dry_run=False):
    """
    Process standard markdown image references in the format ![alt text](path)
    Handles all encoding/escaping variants.
    """
    new_content = content
    # First pattern: Standard markdown images with angle brackets
    pattern1 = r'!\[(.*?)\]\(<([^>]+)>\)'
    matches = list(re.finditer(pattern1, content))
    for match in matches:
        alt_text = match.group(1)
        orig_image_path = match.group(2)
        candidates = generate_all_path_variants(orig_image_path)
        for image_path in candidates:
            if '.gitbook/assets' not in image_path:
                continue
            actual_file = find_actual_file(file_path, image_path)
            if not actual_file:
                decoded_path = urllib.parse.unquote(image_path)
                actual_file = find_actual_file(file_path, decoded_path)
            if not actual_file:
                not_found_images.add(f"<{image_path}> referenced in {file_path}")
                continue
            filename = os.path.basename(actual_file)
            if is_bad_filename(filename):
                old_filename = filename
                if actual_file in renamed_files:
                    new_file_path = renamed_files[actual_file]
                else:
                    new_file_path = rename_image_file(actual_file, dry_run)
                    if new_file_path == actual_file:
                        errors.append(f"Failed to rename {filename} in {file_path}")
                        continue
                    renamed_files[actual_file] = new_file_path
                new_filename = os.path.basename(new_file_path)
                file_renamed_images.append(f"{old_filename} → {new_filename}")
                for variant in generate_all_path_variants(orig_image_path) | generate_all_path_variants(old_filename):
                    old_ref = f'![{alt_text}](<{variant}>)'
                    new_ref = f'![{alt_text}](<{new_filename}>)'
                    new_content = new_content.replace(old_ref, new_ref)
    # For standard markdown images without angle brackets, process line-by-line
    content_lines = new_content.split('\n')
    for i, line in enumerate(content_lines):
        if '![' not in line or '.gitbook/assets' not in line or '<' in line:
            continue
        try:
            start_idx = line.find('![')
            if start_idx == -1:
                continue
            alt_close_idx = -1
            bracket_level = 0
            for j in range(start_idx + 2, len(line)):
                if line[j] == '[':
                    bracket_level += 1
                elif line[j] == ']':
                    if bracket_level == 0:
                        alt_close_idx = j
                        break
                    bracket_level -= 1
            if alt_close_idx == -1:
                continue
            alt_text = line[start_idx + 2:alt_close_idx]
            path_start_idx = line.find('(', alt_close_idx)
            if path_start_idx == -1:
                continue
            path_end_idx = -1
            paren_level = 0
            for j in range(path_start_idx + 1, len(line)):
                if j > 0 and line[j-1] == '\\':
                    continue
                if line[j] == '(': paren_level += 1
                elif line[j] == ')':
                    if paren_level == 0:
                        path_end_idx = j
                        break
                    paren_level -= 1
            if path_end_idx == -1:
                continue
            raw_path = line[path_start_idx + 1:path_end_idx]
            if '.gitbook/assets' not in raw_path:
                continue
            # Normalize relative paths
            if raw_path.startswith('../'):
                levels_up = raw_path.count('../')
                base_dir = os.path.dirname(file_path)
                for _ in range(levels_up):
                    base_dir = os.path.dirname(base_dir)
                normalized_path = os.path.join(base_dir, raw_path.replace('../' * levels_up, ''))
                normalized_path = os.path.relpath(normalized_path, os.path.dirname(file_path))
            else:
                normalized_path = raw_path
            candidates = generate_all_path_variants(normalized_path)
            for image_path in candidates:
                unescaped_path = image_path.replace('\\(', '(').replace('\\)', ')')
                actual_file = find_actual_file(file_path, unescaped_path)
                if not actual_file:
                    decoded_path = urllib.parse.unquote(unescaped_path)
                    actual_file = find_actual_file(file_path, decoded_path)
                if not actual_file:
                    not_found_images.add(f"{raw_path} referenced in {file_path}")
                    continue
                filename = os.path.basename(actual_file)
                if is_bad_filename(filename):
                    old_filename = filename
                    if actual_file in renamed_files:
                        new_file_path = renamed_files[actual_file]
                    else:
                        new_file_path = rename_image_file(actual_file, dry_run)
                        if new_file_path == actual_file:
                            errors.append(f"Failed to rename {filename} in {file_path}")
                            continue
                        renamed_files[actual_file] = new_file_path
                    new_filename = os.path.basename(new_file_path)
                    file_renamed_images.append(f"{old_filename} → {new_filename}")
                    for variant in generate_all_path_variants(raw_path) | generate_all_path_variants(old_filename):
                        old_ref = f'![{alt_text}]({variant})'
                        new_ref = f'![{alt_text}]({new_filename})'
                        content_lines[i] = content_lines[i].replace(old_ref, new_ref)
                    break  # Stop after first successful replacement
        except Exception as e:
            errors.append(f"Error processing image in {file_path}, line {i+1}: {str(e)}")
    new_content = '\n'.join(content_lines)
    return new_content

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Process image links in documentation')
    parser.add_argument('--dry-run', action='store_true', help='Dry run mode - no changes will be made')
    args = parser.parse_args()

    success = process_files(dry_run=args.dry_run)

    if not success:
        print("\n⚠️ Some errors were encountered during processing")
        print("✅ Original files have been preserved")
        if not args.dry_run:
            print("\nTIP: Run with --dry-run to see what would be changed without modifying files")
        sys.exit(1)
