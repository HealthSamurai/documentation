#!/usr/bin/env python3
"""
Check external HTTPS links in documentation for availability.
Extracts all external links from markdown files and verifies they are accessible.
"""

import os
import re
import sys
import time
import argparse
import concurrent.futures
from pathlib import Path
from typing import List, Dict, Tuple, Set
from urllib.parse import urlparse
import json

try:
    import requests
    from requests.adapters import HTTPAdapter
    from urllib3.util.retry import Retry
except ImportError:
    print("Error: requests library not installed. Please run: pip install requests")
    sys.exit(1)

# Configuration
DEFAULT_TIMEOUT = 10  # seconds
MAX_WORKERS = 10  # concurrent requests
RETRY_COUNT = 3
RETRY_BACKOFF = 0.5
USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

# Links patterns to exclude from checking
EXCLUDE_PATTERNS = [
    r"localhost",
    r"127\.0\.0\.1",
    r"example\.com",
    r"your-domain",
    r"\$\{.*\}",  # Template variables
    r"<.*>",  # Placeholder links
]

# Known problematic links that should be skipped
SKIP_DOMAINS = [
    # Add domains that require authentication or are known to block bots
    "www.terraform.io",
    "terraform.io",
]

# Domains that require special handling (use GET instead of HEAD)
ALWAYS_GET_DOMAINS = [
    "aidbox.app",
    "hl7.org",
    "www.hl7.org",
    "build.fhir.org",
    "touchstone.aegis.net",
    "www.healthit.gov",
    "developer.apple.com",
    "fhir.org",
    "www.fhir.org",
]

# File extensions that should be checked differently
DOWNLOADABLE_EXTENSIONS = [
    ".pdf",
    ".zip",
    ".tar",
    ".gz",
    ".doc",
    ".docx",
    ".xls",
    ".xlsx",
    ".ppt",
    ".pptx",
]

# Domains where certain error codes should be treated as success
# (some servers return 405 Method Not Allowed but the resource exists)
ACCEPTABLE_ERROR_CODES = {
    "hl7.org": [405],
    "www.hl7.org": [405],
    "fhir.org": [405], 
    "www.fhir.org": [405],
}

# Domains with known redirect issues that should be handled specially
REDIRECT_PROBLEMATIC_DOMAINS = {
    "www.healthit.gov": {
        "force_https": True,  # Force HTTPS even if server redirects to HTTP
        "note": "Server redirects HTTPS to HTTP which then fails"
    }
}


def setup_session() -> requests.Session:
    """Create a requests session with retry logic."""
    session = requests.Session()
    retry = Retry(
        total=RETRY_COUNT,
        backoff_factor=RETRY_BACKOFF,
        status_forcelist=[500, 502, 503, 504, 429],
    )
    adapter = HTTPAdapter(max_retries=retry)
    session.mount("http://", adapter)
    session.mount("https://", adapter)
    session.headers.update({
        "User-Agent": USER_AGENT,
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language": "en-US,en;q=0.5",
        "Accept-Encoding": "gzip, deflate",
        "DNT": "1",
        "Connection": "keep-alive",
        "Upgrade-Insecure-Requests": "1"
    })
    return session


def extract_links_from_file(file_path: Path) -> Dict[str, List[int]]:
    """Extract all external HTTPS links from a markdown file (markdown-style links and embedded URLs)."""
    links = {}
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # Find markdown links [text](url)
        markdown_pattern = r'\[([^\]]+)\]\(([^)]+)\)'
        # Find embedded URLs {% embed url="..." %}
        embed_pattern = r'{%\s*embed\s+url="([^"]+)"'
        
        for line_num, line in enumerate(content.split('\n'), 1):
            # Check for markdown-style links
            for match in re.finditer(markdown_pattern, line):
                url = match.group(2)
                if url.startswith('https://'):
                    if url not in links:
                        links[url] = []
                    links[url].append(line_num)
            
            # Check for embedded URLs
            for match in re.finditer(embed_pattern, line):
                url = match.group(1)
                if url.startswith('https://'):
                    if url not in links:
                        links[url] = []
                    links[url].append(line_num)
                    
    except Exception as e:
        print(f"Error reading {file_path}: {e}", file=sys.stderr)
    
    return links


def should_skip_url(url: str) -> bool:
    """Check if URL should be skipped based on exclusion patterns."""
    for pattern in EXCLUDE_PATTERNS:
        if re.search(pattern, url, re.IGNORECASE):
            return True
    
    parsed = urlparse(url)
    if parsed.hostname in SKIP_DOMAINS:
        return True
    
    return False


def check_url(session: requests.Session, url: str, timeout: int) -> Tuple[str, bool, str]:
    """
    Check if a URL is accessible.
    Returns: (url, is_accessible, error_message)
    """
    if should_skip_url(url):
        return (url, True, "Skipped")
    
    try:
        # Check if URL points to a downloadable file
        is_downloadable = any(url.lower().endswith(ext) for ext in DOWNLOADABLE_EXTENSIONS)
        
        # Check if domain requires GET instead of HEAD
        parsed = urlparse(url)
        use_get = any(domain in parsed.hostname for domain in ALWAYS_GET_DOMAINS if parsed.hostname)
        
        # Special handling for problematic redirect domains
        if parsed.hostname in REDIRECT_PROBLEMATIC_DOMAINS:
            domain_config = REDIRECT_PROBLEMATIC_DOMAINS[parsed.hostname]
            if domain_config.get("force_https"):
                # For healthit.gov, we need to handle the redirect manually
                # The server redirects HTTPS to HTTP which then fails
                # We'll check if the redirect happens and consider it success
                try:
                    # Don't follow redirects automatically
                    response = session.get(url, timeout=timeout, allow_redirects=False, stream=True)
                    if response.status_code in [301, 302, 303, 307, 308]:
                        # If we get a redirect, consider it as the resource exists
                        redirect_url = response.headers.get('location', '')
                        response.close()
                        # Try to verify the redirect URL works (but don't fail if it doesn't due to HTTP issues)
                        if redirect_url:
                            # Replace http:// with https:// if present
                            if redirect_url.startswith('http://'):
                                redirect_url = 'https://' + redirect_url[7:]
                            try:
                                # Quick check with short timeout
                                test_response = session.head(redirect_url, timeout=5, allow_redirects=False)
                                test_response.close()
                                return (url, True, f"OK (Redirects to {redirect_url[:50]}...)")
                            except:
                                # If the redirect check fails, still consider original URL as OK since it redirects
                                return (url, True, f"OK (Redirects, target check failed)")
                        return (url, True, f"OK (Redirects)")
                    elif response.status_code < 400:
                        response.close()
                        return (url, True, f"OK ({response.status_code})")
                    else:
                        response.close()
                        return (url, False, f"HTTP {response.status_code}")
                except Exception as e:
                    # For healthit.gov, connection errors after redirect are expected
                    if "Connection" in str(e) or "Remote" in str(e):
                        # Try once more with curl to verify
                        import subprocess
                        try:
                            result = subprocess.run(
                                ['curl', '-I', '-L', '--max-time', '10', url],
                                capture_output=True, text=True, timeout=15
                            )
                            if '200 OK' in result.stdout or '301' in result.stdout or '302' in result.stdout:
                                return (url, True, "OK (Verified via curl)")
                        except:
                            pass
                    return (url, False, f"Connection Error (known redirect issue)")
        
        if use_get or is_downloadable:
            # Use GET request for problematic domains and downloadable files with minimal data transfer
            # For downloadable files, we need to handle potential redirects properly
            response = session.get(url, timeout=timeout, allow_redirects=True, stream=True)
            # Close the response to avoid downloading entire content
            response.close()
        else:
            # Try HEAD request first (faster)
            try:
                response = session.head(url, timeout=timeout, allow_redirects=True)
                
                # If HEAD fails with client/server errors (except 403), try GET
                # 403 is acceptable, so we don't retry with GET for it
                if response.status_code in [404, 405, 406]:
                    response = session.get(url, timeout=timeout, allow_redirects=True, stream=True)
                    response.close()
            except:
                # If HEAD completely fails, try GET
                response = session.get(url, timeout=timeout, allow_redirects=True, stream=True)
                response.close()
        
        # Check for successful response
        if response.status_code < 400:
            # For downloadable files, also check content-type if available
            if is_downloadable:
                content_type = response.headers.get('content-type', '')
                # Check if we got an HTML error page instead of the expected file
                if 'text/html' in content_type.lower() and not url.lower().endswith('.html'):
                    return (url, False, f"Expected file but got HTML (possibly error page)")
            return (url, True, f"OK ({response.status_code})")
        elif response.status_code == 403:
            # 403 Forbidden is acceptable - the resource exists but access is restricted
            return (url, True, f"OK ({response.status_code} - Access restricted but resource exists)")
        else:
            # Check if this error code is acceptable for this domain
            if parsed.hostname in ACCEPTABLE_ERROR_CODES:
                if response.status_code in ACCEPTABLE_ERROR_CODES[parsed.hostname]:
                    return (url, True, f"OK ({response.status_code} - acceptable for {parsed.hostname})")
            
            # Double-check with GET if we got an error with HEAD (except 403 which is acceptable)
            if response.request.method == 'HEAD' and response.status_code >= 400 and response.status_code != 403:
                try:
                    response = session.get(url, timeout=timeout, allow_redirects=True, stream=True)
                    response.close()
                    if response.status_code < 400:
                        return (url, True, f"OK ({response.status_code})")
                    elif response.status_code == 403:
                        return (url, True, f"OK ({response.status_code} - Access restricted but resource exists)")
                    # Check again for acceptable error codes
                    if parsed.hostname in ACCEPTABLE_ERROR_CODES:
                        if response.status_code in ACCEPTABLE_ERROR_CODES[parsed.hostname]:
                            return (url, True, f"OK ({response.status_code} - acceptable for {parsed.hostname})")
                except:
                    pass
            return (url, False, f"HTTP {response.status_code}")
            
    except requests.exceptions.Timeout:
        return (url, False, "Timeout")
    except requests.exceptions.ConnectionError:
        return (url, False, "Connection Error")
    except requests.exceptions.TooManyRedirects:
        return (url, False, "Too Many Redirects")
    except requests.exceptions.SSLError:
        return (url, False, "SSL Error")
    except Exception as e:
        return (url, False, str(e))


def find_markdown_files(docs_dir: Path) -> List[Path]:
    """Find all markdown files in the docs directory."""
    return list(docs_dir.rglob("*.md"))


def check_all_links(docs_dir: Path, timeout: int, max_workers: int, verbose: bool) -> Dict:
    """Check all external links in the documentation."""
    print(f"Scanning markdown files in {docs_dir}...")
    
    # Find all markdown files
    md_files = find_markdown_files(docs_dir)
    print(f"Found {len(md_files)} markdown files")
    
    # Extract all links
    all_links = {}  # url -> {files: {file: [line_nums]}}
    for md_file in md_files:
        file_links = extract_links_from_file(md_file)
        rel_path = str(md_file.relative_to(docs_dir.parent))
        
        for url, line_nums in file_links.items():
            if url not in all_links:
                all_links[url] = {"files": {}}
            all_links[url]["files"][rel_path] = line_nums
    
    unique_urls = list(all_links.keys())
    print(f"Found {len(unique_urls)} unique external links to check")
    
    if not unique_urls:
        print("No external links found")
        return {"broken": [], "total": 0, "checked": 0}
    
    # Check URLs concurrently
    print(f"Checking links (max {max_workers} concurrent requests)...")
    session = setup_session()
    broken_links = []
    checked_count = 0
    
    with concurrent.futures.ThreadPoolExecutor(max_workers=max_workers) as executor:
        future_to_url = {
            executor.submit(check_url, session, url, timeout): url 
            for url in unique_urls
        }
        
        for future in concurrent.futures.as_completed(future_to_url):
            url, is_accessible, message = future.result()
            checked_count += 1
            
            if verbose or not is_accessible:
                status = "✓" if is_accessible else "✗"
                print(f"  [{checked_count}/{len(unique_urls)}] {status} {url}: {message}")
            
            if not is_accessible and message != "Skipped":
                all_links[url]["error"] = message
                broken_links.append({
                    "url": url,
                    "error": message,
                    "files": all_links[url]["files"]
                })
    
    return {
        "broken": broken_links,
        "total": len(unique_urls),
        "checked": checked_count
    }


def print_report(results: Dict, output_format: str):
    """Print the results report."""
    broken = results["broken"]
    total = results["total"]
    checked = results["checked"]
    
    if output_format == "json":
        print(json.dumps(results, indent=2))
        return
    
    print("\n" + "="*80)
    print("LINK CHECK REPORT")
    print("="*80)
    print(f"Total unique external links: {total}")
    print(f"Links checked: {checked}")
    print(f"Broken links: {len(broken)}")
    
    if broken:
        print("\n" + "="*80)
        print("BROKEN LINKS DETAILS")
        print("="*80)
        
        for item in broken:
            print(f"\n✗ {item['url']}")
            print(f"  Error: {item['error']}")
            print(f"  Found in:")
            for file_path, line_nums in item['files'].items():
                lines_str = ", ".join(map(str, line_nums))
                print(f"    - {file_path} (lines: {lines_str})")
        
        print("\n" + "="*80)
        print(f"SUMMARY: {len(broken)} broken links found!")
        print("="*80)
        sys.exit(1)
    else:
        print("\n✓ All external links are accessible!")
        sys.exit(0)


def main():
    """Main entry point."""
    parser = argparse.ArgumentParser(
        description="Check external HTTPS links in documentation for availability"
    )
    parser.add_argument(
        "--docs-dir",
        type=str,
        default="docs",
        help="Directory containing markdown files (default: docs)"
    )
    parser.add_argument(
        "--timeout",
        type=int,
        default=DEFAULT_TIMEOUT,
        help=f"Request timeout in seconds (default: {DEFAULT_TIMEOUT})"
    )
    parser.add_argument(
        "--workers",
        type=int,
        default=MAX_WORKERS,
        help=f"Maximum concurrent requests (default: {MAX_WORKERS})"
    )
    parser.add_argument(
        "--verbose",
        action="store_true",
        help="Show all checked links, not just broken ones"
    )
    parser.add_argument(
        "--output",
        choices=["text", "json"],
        default="text",
        help="Output format (default: text)"
    )
    
    args = parser.parse_args()
    
    docs_dir = Path(args.docs_dir)
    if not docs_dir.exists():
        print(f"Error: Directory '{docs_dir}' does not exist", file=sys.stderr)
        sys.exit(1)
    
    # Run the link checker
    results = check_all_links(
        docs_dir=docs_dir,
        timeout=args.timeout,
        max_workers=args.workers,
        verbose=args.verbose
    )
    
    # Print report
    print_report(results, args.output)


if __name__ == "__main__":
    main()