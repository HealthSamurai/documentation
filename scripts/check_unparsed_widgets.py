#!/usr/bin/env python3

import requests
import re
import sys
from bs4 import BeautifulSoup

BASE_URL = "http://localhost:8081"
SITEMAP_INDEX_URL = f"{BASE_URL}/docs/sitemap.xml"

def get_urls_from_sitemap(sitemap_url):
    """Извлекает все <loc> из sitemap XML"""
    response = requests.get(sitemap_url, timeout=30)
    response.raise_for_status()
    urls = re.findall(r'<loc>([^<]+)</loc>', response.text)
    return urls

def find_unparsed_widgets(html):
    """Ищет текст, содержащий {% ... %}"""
    soup = BeautifulSoup(html, 'html.parser')
    widgets = []

    # Проверяем текстовые элементы в p, div, span, li, td
    for tag in soup.find_all(['p', 'div', 'span', 'li', 'td']):
        text = tag.get_text(strip=True)
        # Ищем паттерн {% ... %} внутри текста
        if '{%' in text and '%}' in text:
            widgets.append(text)

    return widgets

def check_page(url):
    """Проверяет страницу на непарсенные виджеты"""
    try:
        response = requests.get(url, timeout=30)
        if response.status_code != 200:
            return None
        widgets = find_unparsed_widgets(response.text)
        if widgets:
            return {'url': url, 'widgets': widgets}
    except Exception:
        pass
    return None

def main():
    try:
        # Получаем sitemap index
        product_sitemaps = get_urls_from_sitemap(SITEMAP_INDEX_URL)
        print(f"Found {len(product_sitemaps)} product sitemaps")

        # Собираем все URLs страниц
        all_urls = []
        for sitemap_url in product_sitemaps:
            urls = get_urls_from_sitemap(sitemap_url)
            all_urls.extend(urls)

        print(f"Total pages to check: {len(all_urls)}")

        # Проверяем каждую страницу
        errors = []
        for i, url in enumerate(all_urls):
            print(f"[{i + 1}/{len(all_urls)}] {url}")
            result = check_page(url)
            if result:
                errors.append(result)

        # Выводим результат
        if errors:
            print(f"\nFound {len(errors)} pages with unparsed GitBook widgets:\n")
            for error in errors:
                print(f"  {error['url']}")
                for widget in error['widgets']:
                    print(f"    -> {widget[:100]}...")
            sys.exit(1)
        else:
            print("\nNo unparsed GitBook widgets found")
            sys.exit(0)

    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
