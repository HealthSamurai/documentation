# Documentation as Volume - Implementation Plan

## Overview
This document outlines the plan to separate documentation from the Docker image, allowing documentation updates without rebuilding the application container. The documentation will be mounted as a volume, and the application will automatically detect and reload changes.

## Current Architecture

### File Structure
```
/home/svt/dev/hs/documentation/
├── docs/                  # Aidbox documentation
├── docs-new/              # Multi-product documentation structure
│   ├── aidbox/           # Symlinks to docs/ and .gitbook.yaml
│   │   ├── docs -> ../../docs
│   │   └── .gitbook.yaml -> ../../.gitbook.yaml
│   ├── fhirbase/         # Fhirbase documentation
│   └── products.yaml     # Products configuration
├── .gitbook.yaml         # Aidbox config
└── src/                  # Application source
```

### Current Build Process
1. `make uberjar` runs with `WORKDIR=docs-new`
2. Build copies all documentation into JAR file
3. Docker image includes JAR with embedded documentation
4. Every commit triggers new Docker build
5. Keel automatically deploys new versions

### Current Runtime Behavior
- **DEV mode** (`DEV=true`): Reads and renders markdown on each request
- **Production mode**: Pre-renders all pages into memory at startup for performance
- Files loaded via `utils/slurp-resource` which uses `io/resource` (classpath)

## Implementation Plan

### Phase 1: Support Volume-Based File Reading

#### 1.1 Update File Reading Logic

**File: `src/gitbok/utils.clj`**

```clojure
;; Cache environment variable at startup
(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

(defn slurp-resource [path]
  (println "Reading" path "from" (if volume-path "volume" "classpath"))
  (if volume-path
    (let [file-path (str volume-path "/" path)
          file (io/file file-path)]
      (println "  Volume path:" file-path "exists:" (.exists file))
      (if (.exists file)
        (slurp file)
        ;; Fallback to classpath for non-documentation resources
        (do
          (println "  Not in volume, trying classpath")
          (if-let [r (io/resource path)]
            (slurp r)
            (throw (Exception. (str "Cannot find " path " in volume or classpath")))))))
    ;; Original classpath logic for backward compatibility
    (if-let [r (io/resource path)]
      (slurp r)
      (throw (Exception. (str "Cannot find " path))))))
```

**File: `src/gitbok/products.clj`**

Update `load-products-config` to support volume:

```clojure
(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

(defn load-products-config []
  (try
    (let [config-str (if volume-path
                       ;; Read from volume
                       (let [file (io/file volume-path "products.yaml")]
                         (println "Loading products.yaml from volume:" (.getPath file))
                         (slurp file))
                       ;; Read from classpath
                       (utils/slurp-resource "products.yaml"))
          config (yaml/parse-string config-str)
          products (mapv
                    #(merge % (read-product-config-file (:config %)))
                    (:products config))]
      {:products products
       :root-redirect (:root-redirect config)})
    (catch Exception e
      (println "ERROR loading products.yaml:" (.getMessage e))
      (println "Falling back to default-aidbox")
      {:products default-aidbox})))
```

### Phase 2: Automatic Cache Reload

#### 2.1 Create Reload Module

**File: `src/gitbok/reload.clj`** (new)

```clojure
(ns gitbok.reload
  (:require
   [clojure.java.io :as io]
   [gitbok.core :as core]
   [gitbok.products :as products]
   [gitbok.constants :as const]
   [system]))

(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

;; Configuration from environment
(def reload-check-interval-ms 
  (or (some-> (System/getenv "RELOAD_CHECK_INTERVAL_SEC")
              Integer/parseInt
              (* 1000))
      60000)) ;; Default: check every 60 seconds

(defn calculate-docs-checksum
  "Calculate checksum based on file modification times and sizes.
   This avoids reading file contents for performance."
  []
  (when volume-path
    (try
      (let [docs-dir (io/file volume-path)
            ;; Get all relevant files
            files (filter #(let [name (.getName %)]
                            (or (.endsWith name ".md")
                                (.endsWith name ".yaml")
                                (.endsWith name ".yml")
                                (.endsWith name ".edn")))
                         (file-seq docs-dir))
            ;; Create checksum from file stats
            file-stats (map (fn [f]
                             (str (.getPath f) 
                                  "-" (.lastModified f)
                                  "-" (.length f)))
                           files)]
        ;; Return hash of all file stats
        (str (hash (sort file-stats))))
      (catch Exception e
        (println "Error calculating checksum:" (.getMessage e))
        nil))))

;; State management functions using context
(defn get-current-checksum [context]
  (system/get-system-state context [::reload :checksum]))

(defn set-current-checksum [context checksum]
  (system/set-system-state context [::reload :checksum] checksum))

(defn is-reloading? [context]
  (system/get-system-state context [::reload :in-progress] false))

(defn set-reloading [context value]
  (system/set-system-state context [::reload :in-progress] value))

(defn rebuild-all-caches
  "Build complete new cache for all products.
   This reuses existing initialization logic."
  [context]
  (let [products-config (products/load-products-config)]
    ;; Update products configuration
    (products/set-products-config context (:products products-config))
    (products/set-full-config context products-config)
    
    ;; Rebuild cache for each product
    (doseq [product (:products products-config)]
      (println "Rebuilding cache for product:" (:name product))
      ;; This will:
      ;; 1. Read SUMMARY.md
      ;; 2. Build URI mappings
      ;; 3. Load all markdown files
      ;; 4. Parse markdown
      ;; 5. Build search index
      ;; 6. Pre-render pages
      ;; 7. Generate sitemap
      (core/init-product-indices context product))
    
    true))

(defn check-and-reload!
  "Check if documentation changed and reload if needed.
   This ensures only one reload happens at a time."
  [context]
  (when (and volume-path 
             (not (is-reloading? context)))
    (let [new-checksum (calculate-docs-checksum)
          current-checksum (get-current-checksum context)]
      (when (and new-checksum 
                 (not= new-checksum current-checksum))
        (println "Documentation changed")
        (println "  Old checksum:" current-checksum)
        (println "  New checksum:" new-checksum)
        
        (set-reloading context true)
        (try
          (let [start-time (System/currentTimeMillis)]
            (println "Starting documentation reload...")
            
            ;; Build new caches
            (rebuild-all-caches context)
            
            ;; Update checksum only after successful reload
            (set-current-checksum context new-checksum)
            
            (let [duration (- (System/currentTimeMillis) start-time)]
              (println (str "Documentation reloaded successfully in " duration "ms"))))
          (catch Exception e
            (println "ERROR: Documentation reload failed")
            (println "  Error:" (.getMessage e))
            (.printStackTrace e)
            ;; Keep old cache on error
            )
          (finally
            (set-reloading context false)))))))

(defn start-reload-watcher
  "Start background thread that checks for documentation changes"
  [context]
  (when volume-path
    (println "Starting documentation reload watcher")
    (println "  Volume path:" volume-path)
    (println "  Check interval:" reload-check-interval-ms "ms")
    
    ;; Set initial checksum
    (let [initial-checksum (calculate-docs-checksum)]
      (set-current-checksum context initial-checksum)
      (println "  Initial checksum:" initial-checksum))
    
    ;; Start background thread
    (future
      (loop []
        (try
          (Thread/sleep reload-check-interval-ms)
          (check-and-reload! context)
          (catch Exception e
            (println "Error in reload watcher:" (.getMessage e))))
        (recur)))
    
    (println "Reload watcher started successfully")))
```

#### 2.2 Integrate Reload Module

**File: `src/gitbok/core.clj`**

Add to requires:
```clojure
(ns gitbok.core
  (:require
   ;; ... existing requires ...
   [gitbok.reload :as reload]  ;; Add this
   ;; ... rest ...
   ))
```

Update `-main` function:
```clojure
(defn -main [& _args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(println "Got SIGTERM.")))
  (println "Server started")
  (println "port " port)
  
  ;; Start system
  (let [started-system (system/start-system default-config)
        context (:app/context started-system)]
    
    ;; Start reload watcher if in volume mode
    (when (System/getenv "DOCS_VOLUME_PATH")
      (reload/start-reload-watcher context))
    
    started-system))
```

### Phase 3: Docker Configuration

#### 3.1 Update Dockerfile

**File: `Dockerfile`**

```dockerfile
FROM clojure:tools-deps AS builder
WORKDIR /srv
COPY . /srv/

RUN --mount=target=/root/.m2,type=cache,sharing=locked \
    make uberjar

FROM bellsoft/liberica-openjre-alpine-musl:24 AS final
COPY --from=builder /srv/target/gitbok.jar /gitbok.jar

# Declare volume mount point for documentation
VOLUME ["/docs-volume"]

RUN adduser -u 1000 -D user
USER 1000

ENTRYPOINT ["java", "-jar", "/gitbok.jar"]
```

### Phase 4: Kubernetes Deployment

#### 4.1 Create Deployment with Git-Sync

**File: `k8s/deployment.yaml`**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gitbok
  namespace: default
spec:
  replicas: 2
  selector:
    matchLabels:
      app: gitbok
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
  template:
    metadata:
      labels:
        app: gitbok
    spec:
      containers:
      # Main application container
      - name: gitbok
        image: gitbok:stable-v1  # Use stable tag, not latest
        ports:
        - containerPort: 8080
        env:
        - name: DOCS_VOLUME_PATH
          value: "/docs-volume/repo"  # Git-sync puts repo in 'repo' subdirectory
        - name: RELOAD_CHECK_INTERVAL_SEC
          value: "60"  # Check for changes every minute
        - name: BASE_URL
          value: "https://docs.example.com"
        - name: DOCS_PREFIX
          value: "/docs"
        - name: MEILISEARCH_URL
          valueFrom:
            secretKeyRef:
              name: gitbok-secrets
              key: meilisearch-url
        - name: MEILISEARCH_API_KEY
          valueFrom:
            secretKeyRef:
              name: gitbok-secrets
              key: meilisearch-api-key
        volumeMounts:
        - name: docs
          mountPath: /docs-volume
          readOnly: true
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
      
      # Git-sync sidecar to keep docs updated
      - name: git-sync
        image: k8s.gcr.io/git-sync/git-sync:v3.6.1
        volumeMounts:
        - name: docs
          mountPath: /tmp/git
        env:
        - name: GIT_SYNC_REPO
          value: https://github.com/your-org/documentation.git
        - name: GIT_SYNC_BRANCH
          value: master
        - name: GIT_SYNC_ROOT
          value: /tmp/git
        - name: GIT_SYNC_DEST
          value: repo
        - name: GIT_SYNC_PERIOD
          value: 30s  # Pull from git every 30 seconds
        - name: GIT_SYNC_ONE_TIME
          value: "false"
        resources:
          requests:
            memory: "50Mi"
            cpu: "10m"
          limits:
            memory: "100Mi"
            cpu: "50m"
      
      volumes:
      - name: docs
        emptyDir: {}  # Shared between containers
```

### Phase 5: Build Process

No changes needed to `build/build.clj`. Continue copying docs to JAR for backward compatibility. When `DOCS_VOLUME_PATH` is set, volume takes precedence over classpath resources.

### Phase 6: Testing

#### 6.1 Local Testing

**File: `Makefile`** additions:

```makefile
# Run with volume locally (simulates production)
run-volume:
	DOCS_VOLUME_PATH=$(PWD) \
	RELOAD_CHECK_INTERVAL_SEC=10 \
	DEV=false \
	BASE_URL=http://localhost:8081 \
	DOCS_PREFIX=/docs \
	clojure -M:dev -m gitbok.core

# Run with volume in dev mode (for debugging)
run-volume-dev:
	DOCS_VOLUME_PATH=$(PWD) \
	RELOAD_CHECK_INTERVAL_SEC=10 \
	DEV=true \
	BASE_URL=http://localhost:8081 \
	DOCS_PREFIX=/docs \
	clojure -M:dev:nrepl:test:build

# Watch logs for reload activity
watch-reload:
	tail -f logs | grep -E "(checksum|reload|Reading|Volume)"

# Test reload by touching files
test-reload:
	@echo "Touching documentation files..."
	@touch docs/*.md docs-new/**/*.yaml
	@echo "Files touched. Watch logs for reload activity."
	@echo "Use 'make watch-reload' in another terminal"

# Docker test with volume
docker-test-volume:
	docker run -d \
		--name gitbok-test \
		-v $(PWD):/docs-volume:ro \
		-e DOCS_VOLUME_PATH=/docs-volume \
		-e RELOAD_CHECK_INTERVAL_SEC=10 \
		-p 8080:8080 \
		gitbok:latest
	@echo "Container started. Check logs with: docker logs -f gitbok-test"

# Clean up test container
docker-test-clean:
	docker stop gitbok-test || true
	docker rm gitbok-test || true
```

## Deployment Strategy

### Initial Deployment

1. **Build and tag stable image**:
   ```bash
   make uberjar
   docker build -t gitbok:stable-v1 .
   docker tag gitbok:stable-v1 your-registry/gitbok:stable-v1
   docker push your-registry/gitbok:stable-v1
   ```

2. **Deploy to Kubernetes**:
   ```bash
   kubectl apply -f k8s/deployment.yaml
   kubectl apply -f k8s/service.yaml
   ```

3. **Configure Keel** (if using):
   - Remove gitbok from Keel's watch list
   - Or configure to only update on manual approval

### Documentation Update Flow

1. **Developer workflow**:
   - Developer edits markdown files
   - Commits and pushes to GitHub master branch
   - GitHub Actions runs validation (optional)

2. **Automatic deployment**:
   - Git-sync sidecar detects changes (30s interval)
   - Pulls latest documentation to volume
   - Application detects checksum change (60s interval)
   - Application rebuilds cache in background
   - New requests served with updated documentation

3. **No downtime**:
   - Old cache continues serving during rebuild
   - Atomic switch to new cache after rebuild
   - Multiple pods reload independently

### Application Code Updates

When application code (not documentation) needs updating:

1. **Build new version**:
   ```bash
   make uberjar
   docker build -t gitbok:stable-v2 .
   docker push your-registry/gitbok:stable-v2
   ```

2. **Update deployment**:
   ```bash
   kubectl set image deployment/gitbok gitbok=your-registry/gitbok:stable-v2
   ```

3. **Rolling update**:
   - Kubernetes performs rolling update
   - Each new pod starts with fresh cache
   - No service interruption

## Benefits

### Performance
- **Fast documentation updates**: 1-2 minutes from commit to production
- **No Docker rebuild**: Saves ~5 minutes per documentation update
- **Cached rendering**: Production performance maintained
- **Zero downtime**: Updates happen in background

### Reliability
- **Separation of concerns**: Documentation changes can't break application
- **Rollback capability**: Can revert docs via Git without touching application
- **Error resilience**: Failed reload keeps old cache working
- **Independent scaling**: Can scale docs storage separately

### Development
- **Backward compatibility**: Existing deployments work unchanged
- **Local testing**: Easy to test with local volumes
- **Flexible deployment**: Can choose embedded or volume mode
- **Clean architecture**: Reuses existing initialization code

## Monitoring

### Metrics to Track
- Documentation reload frequency
- Reload duration
- Checksum calculation time
- Git-sync pull frequency
- Volume disk usage

### Log Patterns
```
# Successful reload
Documentation changed
  Old checksum: abc123
  New checksum: def456
Starting documentation reload...
Rebuilding cache for product: Aidbox
Rebuilding cache for product: fhirbase
Documentation reloaded successfully in 3500ms

# Volume reading
Reading products.yaml from volume
  Volume path: /docs-volume/repo/products.yaml exists: true

# Watcher startup
Starting documentation reload watcher
  Volume path: /docs-volume/repo
  Check interval: 60000 ms
  Initial checksum: abc123
Reload watcher started successfully
```

## Troubleshooting

### Documentation not updating
1. Check git-sync logs: `kubectl logs deployment/gitbok -c git-sync`
2. Check application logs: `kubectl logs deployment/gitbok -c gitbok`
3. Verify volume mount: `kubectl exec deployment/gitbok -- ls -la /docs-volume`
4. Check checksum: Look for "Documentation changed" in logs

### Reload failures
1. Check error logs for "ERROR: Documentation reload failed"
2. Verify file permissions in volume
3. Check memory usage during reload
4. Ensure all required files present in volume

### Performance issues
1. Increase `RELOAD_CHECK_INTERVAL_SEC` to reduce check frequency
2. Monitor reload duration in logs
3. Check if multiple reloads happening simultaneously
4. Verify git-sync not pulling too frequently

## Future Enhancements

### Possible Improvements
1. **Webhook trigger**: GitHub webhook to trigger immediate reload
2. **Partial reload**: Reload only changed products
3. **Cache versioning**: Keep multiple cache versions for instant rollback
4. **Metrics endpoint**: Expose reload statistics via `/metrics`
5. **Health check**: Include reload status in health endpoint
6. **Blue-green cache**: Prepare new cache in separate process

### Not Implemented (by design)
- **Admin reload endpoint**: Avoided for security
- **File watching (inotify)**: Doesn't work well with Kubernetes ConfigMaps/Volumes
- **Database storage**: Adds complexity without clear benefit
- **Redis cache**: Unnecessary external dependency

## Migration Checklist

### Pre-deployment
- [ ] Test with local volume using `make run-volume`
- [ ] Verify all products load correctly
- [ ] Test reload mechanism with `make test-reload`
- [ ] Build and tag stable Docker image
- [ ] Update Kubernetes manifests with correct image tag

### Deployment
- [ingham documentation repo accessible
- [ ] Deploy to staging environment first
- [ ] Monitor logs during first reload cycle
- [ ] Verify documentation accessible via web
- [ ] Test documentation update flow

### Post-deployment
- [ ] Remove/disable Keel auto-update for gitbok
- [ ] Document new deployment process for team
- [ ] Set up monitoring alerts for reload failures
- [ ] Plan schedule for application updates (quarterly?)
- [ ] Archive old Docker images after successful migration