#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path

# Try to import yaml, but don't fail if it's not available
try:
    import yaml
    YAML_AVAILABLE = True
except ImportError:
    YAML_AVAILABLE = False

print('Checking for absolute references to documentation')
print('  - Checking for https://docs.aidbox.app/ links')
print('  - Checking for https://www.health-samurai.io/docs/ links')

# Create output directory
os.makedirs('out', exist_ok=True)
output_file = 'out/absolute_aidbox_links.txt'

def load_products_config():
    """Load products configuration from docs-new/products.yaml"""
    if not YAML_AVAILABLE:
        return None

    try:
        products_yaml_path = os.path.join('docs-new', 'products.yaml')
        if os.path.exists(products_yaml_path):
            with open(products_yaml_path, 'r', encoding='utf-8') as f:
                return yaml.safe_load(f)
    except Exception as e:
        print(f"Warning: Could not load products.yaml: {str(e)}")
    return None

def load_product_config(config_path):
    """Load product-specific .gitbook.yaml configuration"""
    if not YAML_AVAILABLE:
        return None

    base_path = 'docs-new'
    full_config_path = os.path.join(base_path, config_path)
    full_config_path = os.path.normpath(full_config_path)

    if not os.path.exists(full_config_path):
        return None

    try:
        with open(full_config_path, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)
    except Exception:
        return None

def get_product_docs_path(config_path):
    """Get the actual documentation path for a product by reading its config"""
    product_config = load_product_config(config_path)
    if not product_config:
        return None

    root_path = product_config.get('root', './docs/')
    base_path = 'docs-new'
    full_config_path = os.path.join(base_path, config_path)
    full_config_path = os.path.normpath(full_config_path)
    config_dir = os.path.dirname(full_config_path)
    docs_path = os.path.join(config_dir, root_path)
    docs_path = os.path.normpath(docs_path)
    return docs_path

def check_directory_for_links(directory, prefix=""):
    """Check a directory for absolute documentation links"""
    local_aidbox_results = []
    local_health_samurai_results = []

    for file in Path(directory).rglob('*.md'):
        # Skip excluded paths
        if 'docs/reference' in str(file) or str(file) == 'docs/overview/faq.md':
            continue

        with open(file) as f:
            content = f.read()

            # Check for docs.aidbox.app links
            aidbox_matches = re.finditer(r'https://docs\.aidbox\.app', content)
            if aidbox_matches:
                # Get line numbers for matches
                lines = []
                line_num = 1
                for line in content.splitlines():
                    if 'https://docs.aidbox.app' in line:
                        lines.append(str(line_num))
                    line_num += 1

                count = len(re.findall(r'https://docs\.aidbox\.app', content))
                if count > 0:
                    display_path = f"{prefix}{str(file)}" if prefix else str(file)
                    local_aidbox_results.append({
                        'file': display_path,
                        'count': count,
                        'lines': ','.join(lines)
                    })

            # Check for health-samurai.io/docs links
            health_samurai_matches = re.finditer(r'https://www\.health-samurai\.io/docs', content)
            if health_samurai_matches:
                # Get line numbers for matches
                lines = []
                line_num = 1
                for line in content.splitlines():
                    if 'https://www.health-samurai.io/docs' in line:
                        lines.append(str(line_num))
                    line_num += 1

                count = len(re.findall(r'https://www\.health-samurai\.io/docs', content))
                if count > 0:
                    display_path = f"{prefix}{str(file)}" if prefix else str(file)
                    local_health_samurai_results.append({
                        'file': display_path,
                        'count': count,
                        'lines': ','.join(lines)
                    })

    return local_aidbox_results, local_health_samurai_results

# Initialize results
aidbox_results = []
health_samurai_results = []
checked_dirs = set()  # Track directories we've already checked to avoid duplicates

# Check main docs directory (for backward compatibility)
if os.path.exists('docs'):
    print("\nChecking main docs directory...")
    main_aidbox, main_health = check_directory_for_links('docs')
    aidbox_results.extend(main_aidbox)
    health_samurai_results.extend(main_health)
    checked_dirs.add(os.path.abspath('docs'))

# Check multi-product documentation
if YAML_AVAILABLE:
    products_config = load_products_config()
    if products_config and os.path.exists('docs-new'):
        print("Checking multi-product documentation...")
        for product in products_config.get('products', []):
            product_id = product['id']
            config_path = product.get('config')

            if not config_path:
                continue

            product_docs_dir = get_product_docs_path(config_path)
            if not product_docs_dir or not os.path.exists(product_docs_dir):
                continue

            # Skip if we've already checked this directory (e.g., aidbox docs are in main docs/)
            abs_docs_dir = os.path.abspath(product_docs_dir)
            if abs_docs_dir in checked_dirs:
                print(f"  Skipping product: {product_id} (already checked)")
                continue

            print(f"  Checking product: {product_id}")
            prod_aidbox, prod_health = check_directory_for_links(product_docs_dir, f"[{product_id}] ")
            aidbox_results.extend(prod_aidbox)
            health_samurai_results.extend(prod_health)
            checked_dirs.add(abs_docs_dir)

# Check if any links were found
error_found = False

if aidbox_results:
    print("\n❌ Absolute links to https://docs.aidbox.app found. Please fix them before pushing!")
    for result in aidbox_results:
        print(f"  {result['file']}: {result['count']} link(s) on line(s) {result['lines']}")
    error_found = True

if health_samurai_results:
    print("\n❌ Absolute links to https://www.health-samurai.io/docs found. Please fix them before pushing!")
    print("   These should be replaced with relative links.")
    for result in health_samurai_results:
        print(f"  {result['file']}: {result['count']} link(s) on line(s) {result['lines']}")
    error_found = True

if error_found:
    sys.exit(42)
else:
    print("\n✅ No absolute documentation links found (except /reference directory and FAQ).")
    sys.exit(0)