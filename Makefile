.PHONY: clean build uberjar test

init:
	mkdir -p .git/hooks
	cp .git/hooks/pre-commit.sample .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit

repl:
	cp .gitbook.yaml resources
	echo 'dev' > resources/version
	DEV=true BASE_URL=http://localhost:8081 clj -M:dev:test:build

tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js --watch

build-tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js

build-tailwind-min:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.min.css --config ./tailwind.config.js --minify

uberjar:
	clojure -M:build -m build

docker-clean:
	docker buildx rm gitbok || true

test:
	clojure -M:test:kaocha
