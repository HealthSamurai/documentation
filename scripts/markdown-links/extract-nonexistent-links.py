#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path
from typing import List, Tuple, Dict, Optional
from collections import defaultdict
import urllib.parse

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from lib.output import check_start, check_success, check_error, print_issue

def extract_links_from_line(line: str) -> Optional[str]:
    # Markdown link:   [text](<path>) или   [text](path)
    if re.match(r'^  \[.*\]\([^)]+', line):
        start = line.find('](')
        if start != -1:
            start += 2
            link = line[start:].strip()
            
            # Skip HTTP/HTTPS links immediately
            if link.startswith(('http://', 'https://')):
                return ''
                
            if link.startswith('<'):
                # Find the closing bracket, but also check for incomplete brackets
                close = link.find('>')
                if close != -1:
                    # Extract everything between < and >, including the extension
                    return link[1:close].strip(' "\'')
                # If no closing bracket found, try to extract until the end of the line
                # This handles cases where the closing bracket is missing
                return link[1:].strip(' "\'')
            else:
                depth = 0
                for i, c in enumerate(link):
                    if c == '(': depth += 1
                    if c == ')':
                        if depth == 0:
                            return link[:i].strip(' "\'')
                        depth -= 1
                return None
    elif re.match(r'^  <img.*src="[^"]+', line) or re.match(r'^  src="[^"]+', line):
        match = re.search(r'src="([^"]*)"', line)
        if match:
            link = match.group(1).strip()
            if link.startswith('<') and link.endswith('>'):
                link = link[1:-1].strip()
            return link.strip('"\'')
    return ''

def should_skip_link(link: str) -> bool:
    if not link:
        return True

    # First normalize the link by removing any escaped characters
    normalized_link = link.replace('\\', '')
    
    # Check if it starts with http/https before any other processing
    if re.match(r'^(http|https)://', normalized_link):
        return True

    try:
        # Try to parse as URL to handle escaped characters in query parameters
        parsed = urllib.parse.urlparse(normalized_link)
        if parsed.scheme in ['http', 'https', 'ftp', 'mailto']:
            return True
    except:
        pass

    external_patterns = [
        r'^(http|https|ftp|mailto|#)',
        r'^broken-reference'
    ]

    return any(re.match(pattern, normalized_link) for pattern in external_patterns)

def normalize_path(path: str) -> str:
    # First handle escaped characters
    path = path.replace('\\(', '(').replace('\\)', ')').replace('\\[', '[').replace('\\]', ']')
    # Then URL decode
    path = urllib.parse.unquote(path)
    
    # Handle parentheses in filenames
    if '(' in path and ')' in path:
        # Extract the base path and the part with parentheses
        base = path[:path.find('(')].strip()
        if base:
            # Try to find the file with the exact name first
            if os.path.exists(path):
                return path
            # Then try with common extensions
            for ext in ['.svg', '.png', '.jpg', '.jpeg', '.gif']:
                if os.path.exists(base + ext):
                    return base + ext
            # Finally try just the base path
            if os.path.exists(base):
                return base
    return path

def resolve_relative_path(base_path: str, rel_path: str) -> str:
    if not rel_path.startswith('../'):
        return os.path.join(os.path.dirname(base_path), rel_path)

    # Count how many levels up we need to go
    levels_up = rel_path.count('../')
    base_dir = os.path.dirname(base_path)

    # Go up the required number of levels
    for _ in range(levels_up):
        base_dir = os.path.dirname(base_dir)

    # Join with the remaining path
    remaining_path = rel_path.replace('../' * levels_up, '')
    return os.path.join(base_dir, remaining_path)

def validate_link_path(current_file: str, link: str, original_line: str) -> Tuple[bool, str]:
    file_path = link.split('#')[0]
    if not file_path:
        return True, ''

    normalized_path = normalize_path(file_path)
    dir_path = os.path.dirname(current_file)

    def try_path(path: str) -> Optional[str]:
        # Handle relative paths with multiple ../
        full_path = resolve_relative_path(current_file, path)

        # Try the exact path first
        try:
            resolved_path = os.path.realpath(full_path)
            if os.path.exists(resolved_path):
                return resolved_path
        except Exception:
            pass

        # Try with and without extensions
        root, ext = os.path.splitext(full_path)
        paths_to_try = [full_path]
        
        # If no extension, try common image extensions
        if not ext:
            for img_ext in ['.svg', '.png', '.jpg', '.jpeg', '.gif']:
                paths_to_try.append(f"{full_path}{img_ext}")
            paths_to_try.append(f"{full_path}.md")

        for try_path in paths_to_try:
            try:
                resolved_path = os.path.realpath(try_path)
                if os.path.exists(resolved_path):
                    return resolved_path

                # Check for directory with README.md
                if os.path.isdir(try_path) and os.path.exists(os.path.join(try_path, 'README.md')):
                    return os.path.join(try_path, 'README.md')
            except Exception:
                continue
        return None

    # Try different path variants
    variants = [
        file_path,  # Original path
        normalized_path,  # Normalized path
        urllib.parse.quote(file_path),  # URL-encoded path
        file_path.replace(' ', '%20'),  # Space encoded
        file_path.replace('%20', ' '),  # Space decoded
    ]

    # Handle "page-name/" format - could be either dir/README.md or page-name.md
    if file_path.endswith('/'):
        # Try as file.md (without trailing slash)
        base_without_slash = file_path.rstrip('/')
        variants.append(f"{base_without_slash}.md")

    # Add variants with parentheses handled
    if '(' in file_path and ')' in file_path:
        base = file_path[:file_path.find('(')].strip()
        if base:
            variants.extend([
                base,  # Path without parentheses
                urllib.parse.quote(base),  # URL-encoded without parentheses
                base.replace(' ', '%20'),  # Space encoded without parentheses
                base.replace('%20', ' '),  # Space decoded without parentheses
            ])

    for variant in variants:
        resolved_path = try_path(variant)
        if resolved_path:
            return True, ''

    return False, f"{original_line} [BROKEN: target does not exist or path is invalid]"

def extract_all_links() -> tuple[Dict[str, List[str]], int, int]:
    """Returns (file_links, total_files, total_links)"""
    file_links = defaultdict(list)
    files_with_counts = []
    total_files = 0
    total_links = 0

    for file_path in Path('docs').rglob('*.md'):
        total_files += 1
        file_str = str(file_path)
        links = []

        with open(file_path, 'r') as f:
            content = f.read()

            markdown_links = re.finditer(r'\[[^\]]+\]\([^\)]+\)', content)
            html_src = re.finditer(r'src="[^"]+"', content)

            for match in markdown_links:
                links.append(match.group(0))
            for match in html_src:
                links.append(f'  src="{match.group(0)[5:-1]}"')

        links = sorted(set(links))
        count = len(links)
        total_links += count
        if count > 0:
            file_links[file_str] = links
            files_with_counts.append((file_str, count))

    sorted_files = sorted(files_with_counts, key=lambda x: x[1], reverse=True)

    main_files = []
    last_files = []
    for file_str, count in sorted_files:
        if file_str == 'docs/overview/release-notes.md' or file_str.startswith('docs/deprecated/'):
            last_files.append((file_str, count))
        else:
            main_files.append((file_str, count))

    output_dir = Path('out')
    output_dir.mkdir(exist_ok=True)
    output_file = output_dir / 'all_links_by_file.txt'

    with open(output_file, 'w') as f:
        for file_str, count in main_files + last_files:
            f.write(f"{file_str} ({count} links):\n")
            for link in file_links[file_str]:
                f.write(f"  {link}\n")
            f.write("\n")

    return file_links, total_files, total_links

def main():
    check_start("Broken Links")

    _, total_files, total_links = extract_all_links()

    output_dir = Path('out')
    output_file = output_dir / 'all_nonexistent_links_by_file.txt'

    current_file = ""
    broken_links: List[str] = []
    file_header = ""

    with open(output_dir / 'all_links_by_file.txt', 'r') as f, \
         open(output_file, 'w') as out:

        for line in f:
            line = line.rstrip()

            if re.match(r'^[^ ].*links\):$', line):
                if broken_links:
                    out.write(f"{file_header}\n")
                    out.write('\n'.join(broken_links) + '\n\n')

                current_file = re.sub(r' \(.*', '', re.sub(r' links.*', '', line))
                file_header = line
                broken_links = []

            elif re.match(r'^  \[.*\]\([^)]+\)', line) or \
                 re.match(r'^  <img.*src="[^"]+', line) or \
                 re.match(r'^  src="[^"]+', line):

                link = extract_links_from_line(line)
                if link is None:
                    broken_links.append(f"{line} [BROKEN: malformed link syntax]")
                    continue
                if should_skip_link(link):
                    continue
                is_valid, error_msg = validate_link_path(current_file, link, line)
                if not is_valid:
                    broken_links.append(error_msg)

            elif not line and broken_links:
                out.write(f"{file_header}\n")
                out.write('\n'.join(broken_links) + '\n\n')
                broken_links = []

    if output_file.stat().st_size > 0:
        with open(output_file, 'r') as f:
            content = f.read()

        # Parse broken links by file
        broken_by_file = {}
        current_file = None
        for line in content.split('\n'):
            if re.match(r'^[^ ].*links\):$', line):
                current_file = re.sub(r' \(.*', '', line)
            elif line.startswith('  ') and current_file:
                if current_file not in broken_by_file:
                    broken_by_file[current_file] = []
                broken_by_file[current_file].append(line.strip())

        non_deprecated = {f: links for f, links in broken_by_file.items()
                         if not f.startswith('docs/deprecated/')}

        if non_deprecated:
            total_broken = sum(len(links) for links in non_deprecated.values())
            check_error(f"Found {total_broken} broken link(s) in {len(non_deprecated)} file(s):")
            for file, links in list(non_deprecated.items())[:5]:
                print_issue(file)
                for link in links[:3]:
                    # Extract just the link part for cleaner output
                    match = re.search(r'\[([^\]]*)\]\(([^)]*)\)', link)
                    if match:
                        print(f"              → [{match.group(1)[:30]}...]({match.group(2)[:50]})")
                    else:
                        print(f"              → {link[:80]}")
                if len(links) > 3:
                    print(f"              ... and {len(links) - 3} more in this file")
            if len(non_deprecated) > 5:
                print_issue(f"... and {len(non_deprecated) - 5} more file(s)")
            sys.exit(1)

    check_success(f"{total_links} links in {total_files} files checked, no broken links")

if __name__ == '__main__':
    main()
