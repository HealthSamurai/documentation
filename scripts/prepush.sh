echo 'Checking that we do not use absolute reference https://docs.aidbox.app/ in docs/ directory'
bash scripts/find_absolute_aidbox_links.sh || exit 1

echo -e '\nGenerating out/all_broken_links_by_file.txt'
bash ./scripts/extract-all-links.sh || exit 1
bash ./scripts/extract-broken-links.sh || exit 1
bash ./scripts/extract-nonexistent-links.sh || exit 1
echo -e 'Done'

if [[ -s out/all_nonexistent_links_by_file.txt ]]; then
  if grep -q '^docs/deprecated/' out/all_nonexistent_links_by_file.txt && ! grep -vq '^docs/deprecated/' out/all_nonexistent_links_by_file.txt; then
    echo '\nOnly deprecated files have broken relative links. Skipping error.'
  else
    echo -e '\nERROR: Broken relative links found outside docs/deprecated directory. See details in out/all_nonexistent_links_by_file.txt. Fix them before pushing.'
    exit 1
  fi
fi

echo -e '\nChecking if summary.md file and filetree are synced'
bash ./scripts/check-summary-vs-files.sh
