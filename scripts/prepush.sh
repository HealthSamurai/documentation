./scripts/extract-all-links.sh && ./scripts/extract-broken-links.sh
echo 'broken links: see out/all_broken_links_by_file.txt'
./scripts/check-summary-vs-files.sh
