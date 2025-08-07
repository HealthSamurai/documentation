# Aidbox Documentation
The source of https://docs.aidbox.app.

## Setup

Run `make init` to set up the pre-push git hook. This hook will automatically run mandatory checks.

## Run locally
```
docker pull ghcr.io/healthsamurai/documentation:latest && docker run -p 8081:8081 -e DOCS_PREFIX=/docs --rm ghcr.io/healthsamurai/documentation:latest
```
And go to `http://localhost:8081`.

## Environment Variables

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
URL path prefix for documentation pages. The full URL for any page is constructed as:
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

Default: Git short commit hash (e.g., `a1b2c3d`).

### `WORKDIR`
```
WORKDIR=/path/to/docs
```
Working directory for the build process. When specified, the build will copy files from this directory instead of the default `docs` directory. This is useful for building documentation from a custom location.

Default: Uses `docs` directory in the project root.

## Rendering
```
npm install -D tailwindcss@3
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

## Multi-Product Documentation Support

This documentation system supports hosting multiple product documentation under a single deployment using the `products.yaml` configuration file.

### products.yaml Structure

The `products.yaml` file should be placed in the root of your documentation directory (e.g., `docs-new/products.yaml`). Here's the structure:

```yaml
root-redirect: "/aidbox"  # Optional: redirect from "/" to a specific product

products:
  - id: aidbox             # Unique identifier for the product
    name: "Aidbox Docs"    # Display name shown in the UI
    path: /aidbox          # URL path prefix for this product
    config: ./aidbox/.gitbook.yaml  # Path to GitBook config
    logo: .gitbook/assets/logo.jpg  # Optional: product logo
    links:                 # Optional: navigation links for this product
      - text: "Getting Started"
        href: "/getting-started"
      - text: "External Link"
        href: "https://example.com"
        target: "_blank"
        
  - id: forms
    name: "Forms Documentation"
    path: /forms
    config: ./forms/.gitbook.yaml
    # ... additional configuration
```

### Expected Folder Structure

Each product should have its own folder with the following structure:

```
docs-new/
├── products.yaml              # Main products configuration
├── aidbox/                    # Product folder (matches product.id)
│   ├── .gitbook.yaml         # GitBook configuration
│   └── docs/                 # Documentation content
│       ├── readme/           # README location (defined in .gitbook.yaml)
│       │   └── README.md
│       ├── SUMMARY.md        # Table of contents
│       └── ...               # Other documentation files
└── forms/                    # Another product
    ├── .gitbook.yaml
    └── docs/
        └── ...
```

### GitBook Configuration (.gitbook.yaml)

Each product needs a `.gitbook.yaml` file with the following structure:

```yaml
root: ./docs/              # Root directory for documentation files
structure:
  readme: readme/README.md # Path to main README relative to root
  summary: SUMMARY.md      # Path to SUMMARY.md relative to root

redirects:                 # Optional: URL redirects
```

### How It Works

1. The system reads `products.yaml` to discover available products
2. Each product configuration is loaded from its respective `.gitbook.yaml`
3. URLs are constructed as: `BASE_URL + DOCS_PREFIX + product.path + page-path`
4. The `root-redirect` option (if specified) redirects the root URL to a specific product

### Environment Variables

When using multiple products, the `DOCS_PREFIX` environment variable affects all product URLs. For example:
- With `DOCS_PREFIX=/docs` and product path `/aidbox`
- The full product URL becomes: `https://example.com/docs/aidbox`
