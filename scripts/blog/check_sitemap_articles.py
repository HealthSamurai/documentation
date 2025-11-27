#!/usr/bin/env python3
"""
Check that all articles from health-samurai.io sitemap exist locally.
Compares sitemap URLs against local blog folders and/or localhost server.
"""

import requests
import xml.etree.ElementTree as ET
from pathlib import Path
import sys
import argparse

SITEMAP_URL = "https://www.health-samurai.io/sitemap.xml"
LOCAL_SERVER = "http://localhost:8081"
BLOG_DIR = Path(__file__).parent.parent / "blog"


def fetch_sitemap_articles():
    """Fetch sitemap and extract article slugs."""
    response = requests.get(SITEMAP_URL, timeout=30)
    response.raise_for_status()

    root = ET.fromstring(response.content)
    # Handle XML namespace
    ns = {"sm": "http://www.sitemaps.org/schemas/sitemap/0.9"}

    articles = []
    for url in root.findall(".//sm:url/sm:loc", ns):
        loc = url.text
        if "/articles/" in loc:
            # Extract slug from URL like https://www.health-samurai.io/articles/slug
            slug = loc.split("/articles/")[-1].rstrip("/")
            if slug:
                articles.append(slug)

    return articles


def check_local_folders(slugs):
    """Check which slugs have local blog folders."""
    missing = []
    found = []

    for slug in slugs:
        folder = BLOG_DIR / slug
        if folder.exists() and (folder / "index.md").exists():
            found.append(slug)
        else:
            missing.append(slug)

    return found, missing


def check_local_server(slugs):
    """Check which slugs are accessible on local server."""
    missing = []
    found = []
    errors = []

    for slug in slugs:
        url = f"{LOCAL_SERVER}/articles/{slug}"
        try:
            response = requests.get(url, timeout=5)
            if response.status_code == 200:
                found.append(slug)
            else:
                missing.append((slug, response.status_code))
        except requests.RequestException as e:
            errors.append((slug, str(e)))

    return found, missing, errors


def main():
    parser = argparse.ArgumentParser(description="Check sitemap articles against local server")
    parser.add_argument("--server", action="store_true", help="Check against localhost server instead of folders")
    parser.add_argument("--verbose", "-v", action="store_true", help="Show all found articles")
    args = parser.parse_args()

    print(f"Fetching sitemap from {SITEMAP_URL}...")
    slugs = fetch_sitemap_articles()
    print(f"Found {len(slugs)} articles in sitemap\n")

    if args.server:
        print(f"Checking against {LOCAL_SERVER}...")
        found, missing, errors = check_local_server(slugs)

        if args.verbose and found:
            print(f"\n✓ Found ({len(found)}):")
            for slug in found:
                print(f"  - {slug}")

        if missing:
            print(f"\n✗ Missing ({len(missing)}):")
            for slug, status in missing:
                print(f"  - {slug} (HTTP {status})")

        if errors:
            print(f"\n⚠ Errors ({len(errors)}):")
            for slug, error in errors:
                print(f"  - {slug}: {error}")

        print(f"\nSummary: {len(found)}/{len(slugs)} articles found")
        if missing or errors:
            sys.exit(1)
    else:
        print(f"Checking against local folders in {BLOG_DIR}...")
        found, missing = check_local_folders(slugs)

        if args.verbose and found:
            print(f"\n✓ Found ({len(found)}):")
            for slug in found:
                print(f"  - {slug}")

        if missing:
            print(f"\n✗ Missing ({len(missing)}):")
            for slug in missing:
                print(f"  - {slug}")

        print(f"\nSummary: {len(found)}/{len(slugs)} articles found locally")
        if missing:
            sys.exit(1)

    print("\n✓ All articles present!")


if __name__ == "__main__":
    main()
