.PHONY: clean build uberjar test

init-test:
	echo 'dev' > resources/version
	mkdir -p .git/hooks

init:
	rm .git/hooks/pre-push || true
	cp scripts/prepush.sh .git/hooks/pre-push
	chmod +x .git/hooks/pre-push

repl: init-test
	DOCS_VOLUME_PATH=$$(pwd)/docs-new \
	RELOAD_CHECK_INTERVAL_SEC=10 \
	ENABLE_LASTMOD_UPDATER=true \
	LASTMOD_UPDATE_INTERVAL_MINUTES=1 \
	DEV=true \
	BASE_URL=http://localhost:8081 \
	DOCS_PREFIX=/docs \
	MEILISEARCH_URL=http://localhost:7700 \
	MEILISEARCH_API_KEY=60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM \
	PORT=8081 \
	clj -M:dev:nrepl:test:build

repl-classpath: init-test
	DEV=true BASE_URL=http://localhost:8081 DOCS_PREFIX=/docs MEILISEARCH_URL=http://localhost:7700 MEILISEARCH_API_KEY=60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM clj -M:dev:nrepl:test:build

mcp:
	clojure -X:mcp :port 7888

tailwind:
	npx @tailwindcss/cli -i ./resources/public/app.css -o ./resources/public/app.min.css --minify --watch

build-tailwind-min:
	npx @tailwindcss/cli -i ./resources/public/app.css -o ./resources/public/app.min.css --minify

uberjar:
	WORKDIR=docs-new clojure -M:build -m build

uberjar-legacy:
	clojure -M:build -m build


test: init-test
	clojure -M:test:kaocha

minify-js:
	find resources/public/ -type f -name "*.js" ! -name "*.min.js" -exec sh -c 'npx terser "$$0" -c -m -o "$$0.tmp" && mv "$$0.tmp" "$$0"' {} \;

# Meilisearch targets
up:
	docker-compose up -d meilisearch

down:
	docker-compose down

# Run the docs scraper to reindex content in Meilisearch
reindex-search:
	@echo "Starting Meilisearch reindexing..."
	docker-compose run --rm docs-scraper

# Reindex after build (can be called after uberjar/build)
post-build-reindex: reindex-search
	@echo "Documentation has been reindexed in Meilisearch"


# Test reload by touching files
test-reload:
	@echo "Touching documentation files..."
	@touch docs/*.md docs-new/**/*.yaml || true
	@echo "Files touched. Watch logs for reload activity."
	@echo "Use 'make watch-reload' in another terminal"

# Build Docker image for testing
docker-build:
	docker build -t gitbok:test .
	@echo "Docker image built: gitbok:test"

# Run full stack with docker-compose (meilisearch + gitbok with volume)
docker-up:
	docker-compose --profile full up -d --build
	@echo "Services started:"
	@echo "  - Gitbok: http://localhost:8081/docs"
	@echo "  - Meilisearch: http://localhost:7700"
	@echo "Logs: docker-compose logs -f gitbok"

# Stop docker-compose stack
docker-down:
	docker-compose --profile full down

# Rebuild and restart gitbok container
docker-restart:
	docker-compose --profile full stop gitbok
	docker-compose --profile full build gitbok
	docker-compose --profile full up -d gitbok
	@echo "Gitbok restarted with new image"

# Watch gitbok logs
docker-logs:
	docker-compose logs -f gitbok

# Test reload in docker by touching files
docker-test-reload:
	@echo "Touching documentation files..."
	@touch docs-new/products.yaml docs-new/aidbox/docs/*.md
	@echo "Files touched. Watch logs with: make docker-logs"

# Clean up everything
docker-clean-all: docker-down
	docker rmi documentation-gitbok || true
	docker rmi gitbok:test || true
	@echo "Cleaned up docker images and containers"

