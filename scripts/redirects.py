#!/usr/bin/env python3

import re
import sys
from pathlib import Path
from typing import Set

def validate_redirects(file_content) -> Set[str]:
    errors = set()
    for match in re.finditer(r':[ \t]+(.*).md', file_content):
        filepath = "docs/" + str(match.group(1)) + ".md"
        if not Path(filepath).exists():
            errors.add(filepath)
    return errors

def main():
    print("Check that every file in redirect exists.")
    redirects_path = Path('.gitbook.yaml')
    if not redirects_path.exists():
        print("Error: .gitbook.yaml not found")
        sys.exit(1)

    with open(redirects_path) as f:
        content = f.read()
        errors = validate_redirects(content)
        if errors:
            print("Files from .gitbook.yaml not found:")
            for e in errors:
                print(e)
            sys.exit(1)
    print("ok")


if __name__ == '__main__':
    main()
