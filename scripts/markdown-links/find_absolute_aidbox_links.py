#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from lib.output import check_start, check_success, check_error, print_issue

try:
    import yaml
    YAML_AVAILABLE = True
except ImportError:
    YAML_AVAILABLE = False


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
    """Check a directory for absolute documentation links. Returns (aidbox_results, health_samurai_results, files_checked)"""
    local_aidbox_results = []
    local_health_samurai_results = []
    files_checked = 0

    for file in Path(directory).rglob('*.md'):
        # Skip excluded paths
        if 'docs/reference' in str(file) or str(file) == 'docs/overview/faq.md':
            continue

        files_checked += 1
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

    return local_aidbox_results, local_health_samurai_results, files_checked

def main():
    check_start("Absolute Links")

    aidbox_results = []
    health_samurai_results = []
    checked_dirs = set()
    total_files_checked = 0

    if os.path.exists('docs'):
        main_aidbox, main_health, files_checked = check_directory_for_links('docs')
        aidbox_results.extend(main_aidbox)
        health_samurai_results.extend(main_health)
        total_files_checked += files_checked
        checked_dirs.add(os.path.abspath('docs'))

    if YAML_AVAILABLE:
        products_config = load_products_config()
        if products_config and os.path.exists('docs-new'):
            for product in products_config.get('products', []):
                product_id = product['id']
                config_path = product.get('config')

                if not config_path:
                    continue

                product_docs_dir = get_product_docs_path(config_path)
                if not product_docs_dir or not os.path.exists(product_docs_dir):
                    continue

                abs_docs_dir = os.path.abspath(product_docs_dir)
                if abs_docs_dir in checked_dirs:
                    continue

                prod_aidbox, prod_health, files_checked = check_directory_for_links(product_docs_dir, f"[{product_id}] ")
                aidbox_results.extend(prod_aidbox)
                health_samurai_results.extend(prod_health)
                total_files_checked += files_checked
                checked_dirs.add(abs_docs_dir)

    all_results = aidbox_results + health_samurai_results

    if all_results:
        check_error(f"Found {len(all_results)} file(s) with absolute links:")
        for result in all_results:
            print_issue(f"{result['file']}:{result['lines']}")
        return 1

    check_success(f"{total_files_checked} files checked, no absolute links")
    return 0


if __name__ == '__main__':
    sys.exit(main())