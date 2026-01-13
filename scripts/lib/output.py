"""Shared output utilities for prepush scripts.

Provides consistent output formatting across all validation scripts.

Usage:
    from lib.output import check_start, check_success, check_error, print_issue

    check_start("H1 Headers")
    # ... do validation ...
    if errors:
        check_error(f"Found {len(errors)} issues:")
        for err in errors:
            print_issue(err)
    else:
        check_success("701 files checked, no issues")
"""


def check_start(name: str):
    """Print check start header.

    Output: [check] Check Name
    """
    print(f"\n[check] {name}")


def check_success(message: str):
    """Print success result with checkmark.

    Output:         ✓ Success message
    """
    print(f"        ✓ {message}")


def check_error(message: str):
    """Print error result with X mark.

    Output:         ✗ Error message
    """
    print(f"        ✗ {message}")


def check_warning(message: str):
    """Print warning result with warning sign.

    Output:         ⚠ Warning message
    """
    print(f"        ⚠ {message}")


def print_issue(message: str):
    """Print single issue detail.

    Output:           - issue description
    """
    print(f"          - {message}")


def print_detail(message: str):
    """Print additional detail line (same indent as success/error).

    Output:         detail message
    """
    print(f"        {message}")
