.PHONY: clean build uberjar

init:
	mkdir -p .git/hooks
	cp .git/hooks/pre-commit.sample .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit

repl:
	cp .gitbook.yaml resources
	DEV=true clj -M:dev:test

tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js --watch

build-tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js

uberjar:
	clojure -T:build uber

docker-build:
	docker buildx create --name gitbok || true
	docker buildx build \
		--platform linux/amd64,linux/arm64 \
		--builder gitbok \
		-t faridco/gitbok \
		--push \
		.

docker-clean:
	docker buildx rm gitbok || true
