echo 'generating out/all_broken_links_by_file.txt'
./scripts/extract-all-links.sh && ./scripts/extract-broken-links.sh
echo 'done'
echo 'checking if summary.md file and filetree are synced'
./scripts/check-summary-vs-files.sh
