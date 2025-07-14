.PHONY: clean build uberjar test

init-test:
	cp .gitbook.yaml resources
	echo 'dev' > resources/version
	mkdir -p .git/hooks

init:
	rm .git/hooks/pre-push || true
	cp scripts/prepush.sh .git/hooks/pre-push
	chmod +x .git/hooks/pre-push

repl: init-test
	DEV=true BASE_URL=http://localhost:8081 DOCS_PREFIX=/ clj -M:dev:nrepl:test:build

mcp:
	clojure -X:mcp :port 7888

tailwind:
	npx @tailwindcss/cli -i ./resources/public/app.css -o ./resources/public/app.min.css --minify --watch

build-tailwind-min:
	npx @tailwindcss/cli -i ./resources/public/app.css -o ./resources/public/app.min.css --minify

uberjar:
	clojure -M:build -m build

docker-clean:
	docker buildx rm gitbok || true

test: init-test
	clojure -M:test:kaocha

minify-js:
	find resources/public/ -type f -name "*.js" ! -name "*.min.js" -exec sh -c 'npx terser "$$0" -c -m -o "$$0.tmp" && mv "$$0.tmp" "$$0"' {} \;
