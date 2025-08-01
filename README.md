# Aidbox Documentation
The source of https://docs.aidbox.app.

## Setup

Run `make init` to set up the pre-push git hook. This hook will automatically run mandatory checks.

## Run locally
```
docker pull ghcr.io/healthsamurai/documentation:latest && docker run -p 8081:8081 --rm ghcr.io/healthsamurai/documentation:latest
```
And go to `http:localhost:8081`.

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
make repl
```
