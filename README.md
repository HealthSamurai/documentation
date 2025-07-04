# Aidbox Documentation
The source of https://docs.aidbox.app.

## Setup

Run `make init` to set up the pre-push git hook. This hook will automatically run mandatory checks.

## Run locally
```
docker pull ghcr.io/healthsamurai/documentation:latest && docker run -p 8081:8081 --rm ghcr.io/healthsamurai/documentation:latest
```
And go to `http:localhost:8081`.

## Envs

```
PORT=8081
```
Specifies port.

```
DEV=true
```
Reads, parses and renders file on every requests.

```
BASE_URL=http://localhost:8081
```
Used in meta tags, sitemap, robots.txt.
```
DOCS_PREFIX=/aidbox
```
FULL_URL = BASE_URL + DOCS_PREFIX + relative-url
Also used in meta tags, sitemap, robots.txt.

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
