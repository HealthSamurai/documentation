#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path
from typing import Set

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from lib.output import check_start, check_success, check_error, print_issue


def validate_redirects(file_content) -> tuple[Set[str], int]:
    """Returns (errors, total_redirects)"""
    errors = set()
    total = 0
    for match in re.finditer(r':[ \t]+(.*).md', file_content):
        total += 1
        filepath = "docs/" + str(match.group(1)) + ".md"
        if not Path(filepath).exists():
            errors.add(filepath)
    return errors, total


def main():
    check_start("Redirects")

    redirects_path = Path('.gitbook.yaml')
    if not redirects_path.exists():
        check_error(".gitbook.yaml not found")
        return 1

    with open(redirects_path) as f:
        content = f.read()
        errors, total = validate_redirects(content)

        if errors:
            check_error(f"Found {len(errors)} missing redirect targets")
            print("\n⚠️  WHAT TO FIX:")
            print(f"   Open '.gitbook.yaml' and find the redirects for these missing files:")
            print(f"   Each redirect line looks like:  'old/path: new/path.md'\n")
            print("   WHY: Redirects point to pages that don't exist.")
            print("   FIX: Update the redirect target to an existing page or remove the redirect.\n")
            print("Missing files:")
            for e in errors:
                print_issue(e)
            return 1

    check_success(f"{total} redirects checked, all targets exist")
    return 0


if __name__ == '__main__':
    sys.exit(main())
