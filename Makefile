.PHONY: clean build uberjar test

init-test:
	echo 'dev' > resources/version
	mkdir -p .git/hooks

init:
	rm .git/hooks/pre-push || true
	cp scripts/prepush.sh .git/hooks/pre-push
	chmod +x .git/hooks/pre-push

repl: init-test
	DEV=true BASE_URL=http://localhost:8081 DOCS_PREFIX=/docs MEILISEARCH_URL=http://localhost:7700 MEILISEARCH_API_KEY=60DBZGy6zoDL6Q--s1-dHBWptiVKvK-XRsaacdvkOSM clj -M:dev:nrepl:test:build

repl-legacy: init-test
	DEV=true BASE_URL=http://localhost:8081 DOCS_PREFIX=/ clj -M:dev:nrepl:test:build

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

docker-clean:
	docker buildx rm gitbok || true

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
