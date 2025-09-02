#!/usr/bin/env python3
import os
import sys
import re
import subprocess
import yaml
from pathlib import Path
from typing import Optional, Tuple, Dict

def find_title_in_markdown(file_path: Path) -> Optional[str]:
    """Extract the first H1 title from a markdown file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            for line in f:
                if line.startswith('# '):
                    return line[2:].strip()
    except Exception as e:
        print(f"Error reading {file_path}: {e}")
    return None

def check_imagemagick() -> Tuple[bool, str]:
    """Check if ImageMagick is available and return the command."""
    try:
        subprocess.run(['magick', '--version'], capture_output=True, check=True)
        return True, 'magick'
    except:
        try:
            subprocess.run(['convert', '--version'], capture_output=True, check=True)
            return True, 'convert'
        except:
            return False, ''

def generate_og_image(
    title: str,
    product_name: str,
    logo_path: Path,
    output_path: Path,
    magick_cmd: str,
    force_regenerate: bool = False
) -> bool:
    """Generate an OG preview image using ImageMagick."""
    output_path.parent.mkdir(parents=True, exist_ok=True)
    
    # Skip if image already exists and force_regenerate is False
    if not force_regenerate and output_path.exists():
        return True
    
    # Use default logo if the specified one doesn't exist
    if not logo_path.exists():
        default_logo = Path('.gitbook/assets/aidbox-logo.svg')
        if default_logo.exists():
            logo_path = default_logo
        else:
            print(f"Warning: Logo not found: {logo_path}")
            return False
    
    cmd = [
        magick_cmd,
        '-size', '1200x630', 'xc:white',
        '(', str(logo_path), '-resize', '150x150', ')',
        '-gravity', 'northeast', '-geometry', '+30+30', '-composite',
        '-gravity', 'center', '-pointsize', '40',
        '-font', 'scripts/fonts/Roboto-Regular.ttf',
        '-fill', '#555555', '-annotate', '+0+100', product_name,
        '-gravity', 'center', '-pointsize', '80',
        '-font', 'scripts/fonts/Roboto-Regular.ttf',
        '-fill', '#222222', '-size', '1000x200',
        '-background', 'none', f'caption:{title}',
        '-gravity', 'center', '-geometry', '+0-40', '-composite',
        '-strip',  # Remove all metadata (timestamps, etc.) for deterministic output
        '-define', 'png:exclude-chunks=all',  # Exclude all PNG chunks except required ones
        str(output_path)
    ]
    
    try:
        result = subprocess.run(cmd, capture_output=True, text=True)
        if result.returncode != 0:
            print(f"Error generating image: {result.stderr}")
            return False
        return True
    except Exception as e:
        print(f"Error running ImageMagick: {e}")
        return False

def generate_for_product(
    product_id: str,
    product_name: str,
    docs_root: Path,
    logo_path: Path,
    magick_cmd: str,
    force_regenerate: bool = False
) -> int:
    """Generate OG preview images for all markdown files in a product's docs."""
    print(f"Generating OG preview images for product: {product_id}")
    
    # Resolve symlinks to get the actual path
    docs_root = docs_root.resolve()
    
    if not docs_root.exists():
        print(f"Error: Documentation directory not found: {docs_root}")
        return 1
    
    output_dir = Path(f"resources/public/og-preview/{product_id}")
    output_dir.mkdir(parents=True, exist_ok=True)
    
    processed = 0
    errors = 0
    
    # Find all markdown files
    md_files = list(docs_root.rglob('*.md'))
    
    if not md_files:
        print(f"No markdown files found in {docs_root}")
        return 0
    
    print(f"Found {len(md_files)} markdown files")
    
    for md_file in md_files:
        title = find_title_in_markdown(md_file)
        if not title:
            continue
        
        # Calculate relative path and output path
        rel_path = md_file.relative_to(docs_root)
        png_path = output_dir / rel_path.with_suffix('.png')
        
        # Skip if file already exists in non-force mode
        if not force_regenerate and png_path.exists():
            print(f"Skipping existing preview: {png_path}")
            processed += 1
            continue
        
        print(f"Creating preview: {png_path} ← \"{title}\"")
        
        if generate_og_image(title, product_name, logo_path, png_path, magick_cmd, force_regenerate):
            print("  ✓ done")
            processed += 1
        else:
            print("  ✗ failed")
            errors += 1
    
    print(f"\nProcessed: {processed} files, Errors: {errors}")
    return 0 if errors == 0 else 1

def load_products_config() -> Dict:
    """Load product configurations from products.yaml."""
    products_file = Path('docs-new/products.yaml')
    if not products_file.exists():
        print(f"Error: products.yaml not found at {products_file}")
        return {}
    
    try:
        with open(products_file, 'r') as f:
            data = yaml.safe_load(f)
            return data.get('products', [])
    except Exception as e:
        print(f"Error parsing products.yaml: {e}")
        return []

def get_docs_root_from_config(config_path: Path) -> Optional[Path]:
    """Extract the docs root path from a .gitbook.yaml configuration."""
    try:
        with open(config_path, 'r') as f:
            gitbook_config = yaml.safe_load(f)
            root = gitbook_config.get('root', './')
            # Return the absolute path relative to the config file location
            return (config_path.parent / root).resolve()
    except Exception as e:
        print(f"Error reading gitbook config {config_path}: {e}")
        return None

def main():
    """Main entry point."""
    # Check ImageMagick availability
    has_magick, magick_cmd = check_imagemagick()
    if not has_magick:
        print("Error: ImageMagick not found. Please install ImageMagick (magick or convert command)")
        return 1
    
    print(f"Using ImageMagick command: {magick_cmd}")
    
    # Check fonts
    font_path = Path('scripts/fonts/Roboto-Regular.ttf')
    if not font_path.exists():
        print(f"Error: Font not found: {font_path}")
        return 1
    
    # Parse command line arguments
    args = sys.argv[1:]
    force_regenerate = '--force' in args
    if force_regenerate:
        args.remove('--force')
        print("Force regeneration mode enabled")
    
    product_id = args[0] if args else 'all'
    
    # Load product configurations from products.yaml
    products_list = load_products_config()
    if not products_list:
        print("Error: No products found in products.yaml")
        return 1
    
    # Convert to dictionary format for compatibility
    products = {}
    for product in products_list:
        pid = product.get('id')
        if not pid:
            continue
        
        # Resolve the config path relative to docs-new
        config_path = Path('docs-new') / product.get('config', '')
        if not config_path.exists():
            print(f"Warning: Config file not found for {pid}: {config_path}")
            continue
        
        # Get the actual docs root from the config file
        docs_root = get_docs_root_from_config(config_path)
        if not docs_root:
            print(f"Warning: Could not determine docs root for {pid}")
            continue
        
        # Get logo path
        logo_path = Path(product.get('logo', '.gitbook/assets/aidbox_logo.jpg'))
        
        products[pid] = {
            'name': product.get('name', pid),
            'docs_root': docs_root,
            'logo': logo_path
        }
    
    if product_id == 'all':
        # Generate for all products
        exit_code = 0
        for pid, config in products.items():
            code = generate_for_product(
                pid,
                config['name'],
                config['docs_root'],
                config['logo'],
                magick_cmd,
                force_regenerate
            )
            if code != 0:
                exit_code = code
        return exit_code
    elif product_id in products:
        # Generate for specific product
        config = products[product_id]
        return generate_for_product(
            product_id,
            config['name'],
            config['docs_root'],
            config['logo'],
            magick_cmd,
            force_regenerate
        )
    else:
        print(f"Unknown product: {product_id}")
        print(f"Usage: {sys.argv[0]} [--force] [all|{'|'.join(products.keys())}]")
        print("  --force: Regenerate all images even if they already exist")
        return 1

if __name__ == '__main__':
    sys.exit(main())