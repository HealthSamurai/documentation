# Aidbox Documentation
The source of https://docs.aidbox.app.


## Current state
We want to migrate the docs from gitbook to our new engine https://github.com/context-clj/gitbok.

Gitbook cons:
- cannot insert custom js scripts
- price
- file tree was a mess (e.g. api-1, rest-api-1)
- pictures are stored in one place
- bugs
- hard to write own widget (e.g. FHIR resource structure)

Gitbok pros:
- wysiwyg editor
    - easy to change tree structure (handling redirects)
    - paste images
    - easy to change urls of pages
    - PRs in editor
    - pair writing
- insights
    - user metrics
    - user feedback 
    - broken urls
- widgets support: we use hints/warnings, tabs, cards, collapse, "big" links to pages
- easy to setup redirects using .gitbook.yaml
- broken links from one page to another are checked
- github sync: we can use ui or just push to the repo

## Setup

Run `make init` to set up the pre-push git hook. This hook will automatically run checks for a minute or so:

## Scripts

### Pre-push checks (run automatically before each push)

- `find_absolute_aidbox_links.sh` — Fails if any markdown file in `docs/` (except `docs/reference/`) contains absolute links to `https://docs.aidbox.app`. Only relative links are allowed outside reference.
- `extract-all-links.sh` — Extracts all markdown links from documentation files and groups them by file.
- `extract-broken-links.sh` — Finds links that point to `broken-reference` (placeholders for missing or intentionally broken links).
- `extract-nonexistent-links.sh` — Checks that all relative links point to existing files. Reports missing targets. **Broken links in `docs/deprecated/` are allowed and do not block push.**
- `check-summary-vs-files.sh` — Ensures that all files referenced in `SUMMARY.md` exist on disk and vice versa.

### Other scripts

- `all-files.sh`, `all-files-from-summary.sh` — Helpers to list all markdown files on disk or from SUMMARY.md.
- `replace_absolute_aidbox_links.sh` — Replaces absolute links to `https://docs.aidbox.app` with correct relative links (if the target file exists).
- `redirects-from-all-files.sh` — Generates a YAML file with possible redirects for legacy paths (used for migration/redirects).
- `change_names.sh` — Batch renaming utility for files. api-1.md -> api.md
- `find-unused-assets.sh` — Finds images in `.gitbook/assets` that are not referenced in any markdown file. The list of unused files is saved to `out/unused_assets.txt` — you can safely delete them with `xargs -d '\n' rm -- < out/unused_assets.txt`.

You can run any script from the `scripts/` directory manually if needed.
