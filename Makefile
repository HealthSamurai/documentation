init:
	mkdir -p .git/hooks
	ln -sf ../../scripts/prepush.sh .git/hooks/pre-push
	npm install
	git submodule update --init --recursive

repl:
	clj -M:dev:test

tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js --watch

build-tailwind:
	npx tailwindcss -i ./resources/public/app.css -o ./resources/public/app.build.css --config ./tailwind.config.js
