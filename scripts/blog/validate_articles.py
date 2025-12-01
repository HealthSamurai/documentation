#!/usr/bin/env python3
"""
Validate blog articles in docs-new/blog.

Checks:
- Required frontmatter fields (title, published, author, teaser, image)
- Date format (YYYY-MM-DD)
- Reading time format (N min / N minutes)
- Cover image exists
- Images referenced in content exist
- Teaser length (max 300 chars)
- Featured article in blog.yaml exists
- No empty content after frontmatter
"""

import os
import re
import sys
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from lib.output import check_start, check_success, check_error, print_issue, print_detail

try:
    import yaml
    YAML_AVAILABLE = True
except ImportError:
    YAML_AVAILABLE = False

BLOG_DIR = Path(__file__).parent.parent.parent / "docs-new" / "blog"

REQUIRED_FIELDS = ["title", "published", "author", "teaser", "image"]
MAX_TEASER_LENGTH = 300


def parse_frontmatter(content: str) -> tuple[dict, str]:
    """Parse YAML frontmatter from markdown content."""
    if not content.startswith("---"):
        return {}, content

    rest = content[3:]
    end_idx = rest.find("---")
    if end_idx == -1:
        return {}, content

    frontmatter_str = rest[:end_idx]
    body = rest[end_idx + 3:].strip()

    try:
        metadata = yaml.safe_load(frontmatter_str) or {}
    except yaml.YAMLError:
        metadata = {}

    return metadata, body


def extract_markdown_images(content: str) -> list[str]:
    """Extract image paths from markdown content."""
    pattern = r"!\[[^\]]*\]\(([^)]+)\)"
    images = re.findall(pattern, content)
    # Filter out URLs and absolute paths
    return [img for img in images if not img.startswith(("http", "/"))]


def validate_article(article_dir: Path) -> list[str]:
    """Validate a single blog article. Returns list of error messages."""
    errors = []
    index_file = article_dir / "index.md"

    if not index_file.exists():
        return ["No index.md found"]

    content = index_file.read_text(encoding="utf-8")
    metadata, body = parse_frontmatter(content)

    # Check required fields
    for field in REQUIRED_FIELDS:
        value = metadata.get(field)
        if not value or (isinstance(value, str) and not value.strip()):
            errors.append(f"Missing required field: {field}")

    # Check date format
    published = metadata.get("published")
    if published:
        published_str = str(published)
        if not re.match(r"^\d{4}-\d{2}-\d{2}$", published_str):
            errors.append(f"Invalid date format: {published_str} (expected YYYY-MM-DD)")

    # Check reading-time format (accepts "N min", "N min read", "N minutes")
    reading_time = metadata.get("reading-time")
    if reading_time:
        if not re.match(r"^\d+\s+(min|minutes?)(\s+read)?\s*$", str(reading_time)):
            errors.append(f"Invalid reading-time format: {reading_time} (expected 'N min' or 'N minutes')")

    # Check cover image exists
    image = metadata.get("image")
    if image and not str(image).startswith("http"):
        image_path = article_dir / image
        if not image_path.exists():
            errors.append(f"Cover image not found: {image}")

    # Check teaser length
    teaser = metadata.get("teaser")
    if teaser and len(teaser) > MAX_TEASER_LENGTH:
        errors.append(f"Teaser too long: {len(teaser)} chars (max {MAX_TEASER_LENGTH})")

    # Check content images exist
    for img_path in extract_markdown_images(body):
        full_path = article_dir / img_path
        if not full_path.exists():
            errors.append(f"Image not found in content: {img_path}")

    # Check non-empty content
    if not body.strip():
        errors.append("Article has no content after frontmatter")

    return errors


def validate_blog_yaml() -> list[str]:
    """Validate blog.yaml configuration."""
    errors = []
    yaml_file = BLOG_DIR / "blog.yaml"

    if not yaml_file.exists():
        return ["blog.yaml not found"]

    try:
        config = yaml.safe_load(yaml_file.read_text(encoding="utf-8"))
    except yaml.YAMLError as e:
        return [f"Invalid YAML in blog.yaml: {e}"]

    # Check featured article exists
    featured = config.get("featured-article")
    if featured:
        featured_dir = BLOG_DIR / featured
        if not featured_dir.exists() or not (featured_dir / "index.md").exists():
            errors.append(f"Featured article not found: {featured}")

    return errors


def main():
    check_start("Blog Articles")

    if not YAML_AVAILABLE:
        print_detail("Skipped (PyYAML not installed)")
        print_detail("Install: pip install pyyaml | apt install python3-yaml | pacman -S python-yaml")
        return 0

    if not BLOG_DIR.exists():
        check_error(f"Blog directory not found: {BLOG_DIR}")
        return 1

    all_errors = {}

    yaml_errors = validate_blog_yaml()
    if yaml_errors:
        all_errors["blog.yaml"] = yaml_errors

    article_dirs = [
        d for d in BLOG_DIR.iterdir()
        if d.is_dir() and (d / "index.md").exists()
    ]

    for article_dir in sorted(article_dirs):
        errors = validate_article(article_dir)
        if errors:
            all_errors[article_dir.name] = errors

    if all_errors:
        total_errors = sum(len(e) for e in all_errors.values())
        check_error(f"Found {total_errors} errors in {len(all_errors)} item(s):")
        for name, errors in sorted(all_errors.items())[:5]:
            print_issue(f"{name}: {errors[0]}")
        if len(all_errors) > 5:
            print_issue(f"... and {len(all_errors) - 5} more items with errors")
        return 1

    check_success(f"{len(article_dirs)} blog articles valid")
    return 0


if __name__ == "__main__":
    sys.exit(main())
