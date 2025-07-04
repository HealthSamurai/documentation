.PHONY: clean build uberjar test

init-test:
	cp .gitbook.yaml resources
	echo 'dev' > resources/version
	mkdir -p .git/hooks

init:
	cp .git/hooks/pre-commit.sample .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit

repl: init-test
	DEV=true BASE_URL=http://localhost:8081 DOCS_PREFIX=/ clj -M:dev:test:build

tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.min.css --config ./tailwind.config.js --minify --watch

build-tailwind-min:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.min.css --config ./tailwind.config.js --minify

uberjar:
	clojure -M:build -m build

docker-clean:
	docker buildx rm gitbok || true

test: init-test
	clojure -M:test:kaocha

minify-js:
	find resources/public/ -type f -name "*.js" ! -name "*.min.js" -exec sh -c 'npx terser "$$0" -c -m -o "$$0.tmp" && mv "$$0.tmp" "$$0"' {} \;
