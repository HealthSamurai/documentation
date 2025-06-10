.PHONY: clean build uberjar

init:
	mkdir -p .git/hooks
	cp .git/hooks/pre-commit.sample .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit

repl:
	# clj -M:dev:test:build
	clj -M:dev:test

tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js --watch

build-tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js

uberjar:
	clojure -T:build uber
