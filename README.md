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

Run `make init` to set up the pre-push git hook. This hook will automatically run checks for a minute or so.

## Rendering
```
npm install -D tailwindcss@3
```
