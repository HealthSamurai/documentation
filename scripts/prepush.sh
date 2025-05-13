#!/bin/bash

bash ./scripts/find_absolute_aidbox_links.sh || exit 1
bash ./scripts/extract-broken-links.sh || exit 1
bash ./scripts/extract-nonexistent-links.sh || exit 1
bash ./scripts/check-summary-vs-files.sh
