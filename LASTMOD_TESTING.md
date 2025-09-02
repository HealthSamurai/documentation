# Testing Lastmod Generation

The lastmod generation feature automatically generates last modification timestamps for documentation files using Git history. It includes caching based on Git HEAD to avoid redundant computations.

## Method 1: Testing via REPL

### Quick Test
```bash
# Start REPL with environment variables
make repl

# In REPL, load and run tests
(load-file "test-lastmod-repl.clj")
```

### Manual Testing in REPL
```clojure
;; Start REPL
make repl

;; Test basic generation
(require '[gitbok.lastmod.generator :as gen])
(def ctx {:system (atom {})})
(def data (gen/generate-lastmod-data "docs-new/aidbox" "."))
(count data)  ; Should show number of .md files

;; Test caching
(def d1 (gen/generate-or-get-cached-lastmod ctx "aidbox" "docs-new/aidbox" "."))
(def d2 (gen/generate-or-get-cached-lastmod ctx "aidbox" "docs-new/aidbox" "."))
(= d1 d2)  ; Should be true (cache hit)

;; Check cache state
(gen/get-lastmod-cache ctx)
```

### Monitor REPL Logs
```bash
# In another terminal
make monitor-lastmod-repl
```

## Method 2: Testing via Docker Compose

### Start Services
```bash
# Build and start all services
make docker-up

# Or manually:
docker-compose --profile full up -d --build
```

### Monitor Logs
```bash
# Watch lastmod-specific logs
make monitor-lastmod-docker

# Or watch all gitbok logs
docker-compose logs -f gitbok
```

### Trigger Lastmod Generation

1. **Initial generation** - happens on startup
2. **Periodic updates** - every 30 minutes (configurable)
3. **On reload** - when files change:
```bash
# Touch files to trigger reload
make docker-test-reload
```

## What to Look For

### Successful Generation
```
:gitbok.lastmod.generator/lastmod-generated {:dir "docs-new/aidbox" :entries 150}
```

### Cache Hit
```
:gitbok.lastmod.generator/using-cached-lastmod {:product "aidbox"}
```

### Periodic Update
```
:gitbok.reload/updating-lastmod {}
```

### Updater Started
```
:gitbok.reload/starting-lastmod-updater {:interval-minutes 30}
```

## Configuration

### Environment Variables

- `ENABLE_LASTMOD_UPDATER` - Enable periodic updates (true/false)
- `LASTMOD_UPDATE_INTERVAL_MINUTES` - Update interval (default: 30)

### REPL (Makefile)
```makefile
ENABLE_LASTMOD_UPDATER=true
LASTMOD_UPDATE_INTERVAL_MINUTES=5  # Faster for testing
```

### Docker (docker-compose.yml)
```yaml
environment:
  - ENABLE_LASTMOD_UPDATER=true
  - LASTMOD_UPDATE_INTERVAL_MINUTES=30
```

## Troubleshooting

### No Git History
If files don't have git history:
- Check that `.git` directory exists
- Ensure files are committed
- Verify git is installed in container

### Cache Not Working
Check system state:
```clojure
(require '[system])
(system/get-system-state ctx [:gitbok.lastmod.generator/lastmod-cache])
```

### Updater Not Running
Check environment:
```bash
# In container
docker exec gitbok env | grep LASTMOD

# In REPL
(System/getenv "ENABLE_LASTMOD_UPDATER")
```

## Quick Commands Reference

```bash
# REPL testing
make repl                      # Start REPL
make test-lastmod-repl         # Show instructions
make monitor-lastmod-repl      # Monitor logs

# Docker testing  
make docker-up                 # Start services
make docker-logs               # Watch all logs
make monitor-lastmod-docker    # Watch lastmod logs
make docker-test-reload        # Trigger reload
make docker-down               # Stop services
```