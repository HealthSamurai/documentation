.PHONY: clean build uberjar

init:
	mkdir -p .git/hooks
	cp .git/hooks/pre-commit.sample .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit

repl:
	cp .gitbook.yaml resources
	echo 'dev' > resources/version
	DEV=true clj -M:dev:test:build

tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js --watch

build-tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js

uberjar:
	clojure -M:build -m build

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
