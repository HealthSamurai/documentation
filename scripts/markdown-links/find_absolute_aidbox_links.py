#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path

print('Checking that we do not use absolute reference https://docs.aidbox.app/ in docs/ directory')

# Create output directory
os.makedirs('out', exist_ok=True)
output_file = 'out/absolute_aidbox_links.txt'

# Find markdown files with absolute links
results = []
for file in Path('docs').rglob('*.md'):
    # Skip excluded paths
    if 'docs/reference' in str(file) or str(file) == 'docs/overview/faq.md':
        continue

    with open(file) as f:
        content = f.read()
        matches = re.finditer(r'https://docs\.aidbox\.app', content)
        if matches:
            # Get line numbers for matches
            lines = []
            line_num = 1
            for line in content.splitlines():
                if 'https://docs.aidbox.app' in line:
                    lines.append(str(line_num))
                line_num += 1

            count = len(re.findall(r'https://docs\.aidbox\.app', content))
            if count > 0:
                results.append({
                    'file': str(file),
                    'count': count,
                    'lines': ','.join(lines)
                })

# Check if any links were found
if results:
    print("\nAbsolute links to https://docs.aidbox.app found. Please fix them before pushing!")
    print(results)
    sys.exit(42)
else:
    print("\nNo absolute links found (except /reference directory and FAQ).")
    sys.exit(0)
