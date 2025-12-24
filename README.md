# Aidbox Documentation

The source of https://www.health-samurai.io/docs.

## Setup

Run `make init` to set up the pre-push git hook. This hook will automatically run mandatory checks.

## Run locally

```
git clone git@github.com:HealthSamurai/documentation.git
```

```
cd documentation
```

```
docker pull ghcr.io/healthsamurai/documentation:latest && docker run -p 8081:8081 \
  -e BASE_URL=http://localhost:8081 \
  -e DEV=true \
  -e DOCS_PREFIX=/docs \
	-e DOCS_REPO_PATH=/repo \
	-e DOCS_VOLUME_PATH=/repo/docs-new \
	-e PORT=8081 \
	-e RELOAD_CHECK_INTERVAL=10 \
  -e EXAMPLES_UPDATE_INTERVAL=1 \
  -v $(pwd):/repo \
  --rm ghcr.io/healthsamurai/documentation:latest
```

And go to `http://localhost:8081/docs/aidbox`.

Aidbox docs search:
1. Run meilisearch.
```
docker-compose up -d meilisearch
```
2. Index Aidbox docs from https://www.health-samurai.io/docs/aidbox.
```
make reindex-search
```

## Environment variables

### `PORT`
```
PORT=8081
```
Specifies the port number on which the documentation server will run. Default: `8081`.

### `DEV`
```
DEV=true
```
Enables development mode. When set to `true`, the server reads, parses and renders files on every request, allowing for live updates during development. Default: `false`.

### `BASE_URL`
```
BASE_URL=http://localhost:8081
```
The base URL of the documentation site. This is used in:
- Meta tags for SEO
- Sitemap generation
- robots.txt file
- Absolute URL construction

Default: `http://localhost:8081`.

### `DOCS_PREFIX`
```
DOCS_PREFIX=/aidbox
```
URL path prefix for documentation pages. The full URL of any page is constructed as:
```
FULL_URL = BASE_URL + DOCS_PREFIX + relative-url
```
This allows serving documentation under a subdirectory. Also used in meta tags, sitemap, and robots.txt.

Default: `/aidbox`.

### `VERSION`
```
VERSION=v1.0.0
```
Version identifier for the build. Used during the build process to tag the application version. If not specified, defaults to the short Git commit hash from the current HEAD.

Default: Git short commit hash (for example, `a1b2c3d`).

### `WORKDIR`
```
WORKDIR=/path/to/docs
```
Working directory for the build process. When specified, the build will copy files from this directory instead of the default `docs` directory. This is useful for building documentation from a custom location.

Default: Uses `docs` directory in the project root.

### `MEILISEARCH_URL`
```
MEILISEARCH_URL=http://localhost:7700
```
Meilisearch URL.

Default: "http://localhost:7700"

### `MEILISEARCH_API_KEY`
```
MEILISEARCH_API_KEY=60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM
```
Meilisearch API key.

Default: none

### `GITHUB_TOKEN`
```
GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxx
```
GitHub personal access token. Used for accessing GitHub API to fetch repository data, such as last modification dates for documentation files.

Default: none

### `DOCS_REPO_PATH`
```
DOCS_REPO_PATH=/path/to/repo
```
Path to the documentation repository root. Used to locate the git repository for determining file modification dates and other git-related operations.

Default: `.` (current directory)

### `DOCS_VOLUME_PATH`
```
DOCS_VOLUME_PATH=/repo/docs-new
```
Path to the documentation content directory. Points to the location where product documentation folders are located (as specified in `products.yaml`).

Default: none

### `EXAMPLES_UPDATE_INTERVAL`
```
EXAMPLES_UPDATE_INTERVAL=60
```
Interval in seconds for updating code examples from external sources. Controls how frequently the system checks and updates embedded code examples.

Default: `60` seconds

### `RELOAD_CHECK_INTERVAL`
```
RELOAD_CHECK_INTERVAL=30
```
Interval in seconds for checking file changes in development mode. When `DEV=true`, the system checks for file modifications at this interval to trigger automatic reloads.

Default: `30` seconds

### `POSTHOG_API_KEY`
```
POSTHOG_API_KEY=phc_xxxxxxxxxxxxxxxxxxxxxxxx
```
PostHog API key for analytics integration. Used to track usage analytics and user behavior on the documentation site.

Default: none

### `POSTHOG_HOST`
```
POSTHOG_HOST=https://app.posthog.com
```
PostHog host URL for analytics. Specifies the PostHog instance endpoint for sending analytics data.

Default: none

## Rendering
```
npm install
```
```
make tailwind
```
## REPL
```
git submodule update --init --recursive
```
```
make repl
```
Go to dev/user.clj

## Search 

Runs Meilisearch:
```
make up
```

## Multi-product documentation support

This documentation system supports hosting multiple product documentation under a single deployment using the `products.yaml` configuration file.

### Add new product

How to add new product:
1. Add new product in `docs-new/products.yaml`.
2. Create `docs-new/<product-name>` directory, which contains `.gitbook.yaml` file and `docs/` directory.
3. Set up `.gitbook.yaml`.
3. Set up `docs-new/<product-name>/docs/SUMMARY.md` with table of contents.
4. Add your markdown files to `docs-new/<product-name>/docs/` directory.
5. Commit and push

Once the new version of gitbok is built and deployed by FluxCD, go to `<domain>/<DOCS_PREFIX>/<product-name>`.

When the product is deployed, we can [set up search](#set-up-search)

### products.yaml structure

The `products.yaml` file should be placed in the root of your documentation directory (for example, `docs-new/products.yaml`). Here's the structure:

```yaml
root-redirect: "/aidbox"

products:
  - id: aidbox
    name: "Aidbox User Docs"
    # url <BASE_URL><DOCS_PREFIX>/aidbox
    path: /aidbox
    config: aidbox/.gitbook.yaml
    logo: .gitbook/assets/aidbox_logo.jpg
    favicon: .gitbook/assets/favicon.ico
    og-preview-text: "Aidbox User Docs"  # Optional: custom text for OG preview images
    meilisearch-index: "docs" # Meilisearch index name to search. See how to set up search below.
    links:
      - text: "Run Aidbox locally"
        href: "/getting-started/run-aidbox-locally"
      - text: "Run Aidbox in Sandbox"
        href: "/getting-started/run-aidbox-in-sandbox"

  - id: fhirbase
    name: "fhirbase"
    ...
```

### Expected folder structure

Each product should have its own folder with the following structure:

```
docs-new/
├── products.yaml             # Main products configuration
├── aidbox/                   # Product folder (matches product.id)
│   ├── .gitbook.yaml         # GitBook configuration
│   └── docs/                 # Documentation content
│       ├── SUMMARY.md        # Table of contents
│       └── ...               # Other documentation files
└── fhirbase/                    # Another product
    ├── .gitbook.yaml
    └── docs/
        └── ...
```

### GitBook configuration (.gitbook.yaml)

Each product needs a `.gitbook.yaml` file with the following structure:

```yaml
root: ./docs/              # Root directory for documentation files
structure:
  readme: README.md        # Path to main README relative to root
  summary: SUMMARY.md      # Path to SUMMARY.md relative to root

redirects:                 # Optional: URL redirects
  access-control/authentication/access-to-aidboxui: access-control/authentication/README.md
```

### SUMMARY.md structure

SUMMARY.md structure example:

```
# Table of contents

* [Overview](README.md)
* [Integrations](integrations/README.md)
  * [Python](integrations/python.md)
  * [.NET](integrations/net.md)

## Overview
* [Licensing and Support](overview/licensing-and-support.md)

...
```

### How it works

1. The system reads `products.yaml` to discover available products.
2. Each product configuration is loaded from its respective `.gitbook.yaml`. SUMMARY.md is loaded from the path provided by `.gitbook.yaml`.
3. URLs are constructed as: `BASE_URL + DOCS_PREFIX + product.path + page-path`.
4. The `root-redirect` option (if specified) redirects the root URL to a specific product.

### Testing if everything is right

Go to [run locally](#run-locally) section, run locally as described, then go to `localhost:8081/docs/<product-id>`.

## Set up search

We use [Meilisearch](https://www.meilisearch.com/) as a search engine.
It is deployed in k8s (see **k8s/meilisearch** directory).

Every product has its own [meilisearch **index**](https://www.meilisearch.com/docs/learn/getting_started/indexes).
To index the product documentation, we use [meilisearch-docs-scraper](https://github.com/HealthSamurai/meilisearch-docs-scraper).

Every 2 hours we scrape the HTML from `<domain>/<DOCS_PREFIX>/<product>/sitemap.xml` and recreate all indexes.
See **k8s/meilisearch/cronjob-reindex-v2.yaml** — a single CronJob that indexes all products.

### How to set up search for a new product

You need a deployed documentation website to set up a search, because we create search index through parsing the HTML of a product documentation.

1. Add scraper config:
```
cp k8s/meilisearch/config.json k8s/meilisearch/config-<your-product>.json
```

2. Edit `config-<your-product>.json`:
   - Set `index_uid` to match `meilisearch-index` from **products.yaml**
   - Change `start_urls` and `sitemap_urls`

3. Add config to `k8s/meilisearch/kustomization.yaml`:
```yaml
configMapGenerator:
  - name: meilisearch-scraper-config-<your-product>
    files:
      - config.json=./config-<your-product>.json
```

4. Add config to `k8s/meilisearch/cronjob-reindex-v2.yaml`:
```yaml
args:
  - /configs/docs.json
  - /configs/fhirbase.json
  - /configs/auditbox.json
  - /configs/<your-product>.json  # Add this
volumeMounts:
  - name: config-<your-product>
    mountPath: /configs/<your-product>.json
    subPath: config.json
volumes:
  - name: config-<your-product>
    configMap:
      name: meilisearch-scraper-config-<your-product>
```

5. Commit and push, wait until FluxCD applies it

### Test locally

1. Run meilisearch:
```
make up
```

2. Run scraper:
```
docker run --rm \
  -e MEILISEARCH_HOST_URL=http://host.docker.internal:7700 \
  -e MEILISEARCH_API_KEY=-60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM \
  -v $(pwd)/k8s/meilisearch/config-<your-product>.json:/app/config.json \
  ghcr.io/healthsamurai/meilisearch-docs-scraper:latest
```

3. Verify index:
```
curl http://localhost:7700/indexes/<index-name> \
  -H "Authorization: Bearer -60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM"
```
