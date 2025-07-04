#!/usr/bin/env python3

import requests
import re
import sys

SITEMAP_URL = "https://docs.aidbox.app/sitemap-pages.xml"
BASE_URL = "http://localhost:8081"

def main():
    try:
        # Fetch sitemap
        response = requests.get(SITEMAP_URL)
        response.raise_for_status()
        
        # Extract URLs using regex
        url_pattern = r'https://docs\.aidbox\.app[^<\s]+'
        urls = re.findall(url_pattern, response.text)
        
        # Remove duplicates and sort
        unique_urls = sorted(list(set(urls)))
        
        not_found_count = 0
        
        for url in unique_urls:
            try:
                path = url.split("aidbox.app", 1)[1]
                if not path.startswith("/"):
                    path = "/" + path
            except IndexError:
                continue

            local_url = BASE_URL + path
            try:
                resp = requests.get(local_url, timeout=10)
                if resp.status_code == 404:
                    print(f"404: {local_url}")
                    not_found_count += 1
            except Exception:
                # Ignore connection errors
                pass
        
        if not_found_count > 0:
            print(f"\nTotal 404 responses: {not_found_count}")
            sys.exit(1)
        else:
            print("No 404 responses found")
            sys.exit(0)
            
    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main() 