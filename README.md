# Aidbox Documentation
The source of https://docs.aidbox.app.

## Setup

Run `make init` to set up the pre-push git hook. This hook will automatically run mandatory checks.

## Run locally
```
docker run -p 8081:8081 --rm ghcr.io/healthsamurai/documentation:latest
```
And go to `http:localhost:8081`.

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

