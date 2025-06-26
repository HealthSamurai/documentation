.PHONY: clean build uberjar minify build-production build-seo check-optimization test

init:
	mkdir -p .git/hooks
	cp .git/hooks/pre-commit.sample .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit

repl:
	cp .gitbook.yaml resources
	echo 'dev' > resources/version
	DEV=true BASE_URL=http://localhost:8081 clj -M:dev:test:build

build-tailwind-min:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.min.css --config ./tailwind.config.js --minify

tailwind-min:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.min.css --config ./tailwind.config.js --minify --watch

minify:
	npm run minify-css
	npm run minify-js

build-production:
	npm run build-production

build-seo:
	npm run build-seo

check-optimization:
	npm run check-optimization

uberjar:
	clojure -M:build -m build

docker-clean:
	docker buildx rm gitbok || true

test:
	clojure -M:test:kaocha
