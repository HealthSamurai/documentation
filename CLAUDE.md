## Used technologies
- Clojure
- HTMX
- Tailwind 4 (no .js config, just resources/public/app.css)

## Clojure

1. Always use `clojure-mcp` mcp to write clojure code.
2. When you change .clj files, ask to re-evaluate repl and check.
3. If writing comments in code (source and tests), you MUST use english.
4. Please every step of the way just give me a high level explanation of what changes you made
5. Make every task and code change you do as simple as possible. We want to avoid making any massive or complex changes. Every change should impact as little code as possible. Everything is about simplicity.
6. Prefer small functions.
7. In hiccup, always use :class instead of "[:sometag.someclass-a.someclass-b]"

## Tailwind 4

- Use Tailwind utility classes for consistent styling, with custom CSS only for special cases, do not use app.css if not necessary
- Organize classes logically (layout, spacing, color, typography)
- Use responsive and state variants (for example, sm:, md:, lg:, hover:, focus:, dark:) in markup
- Make sure you understand what tailwind v4 is and all its differences between v3. Read the doc https://tailwindcss.com/docs.
- Embrace Tailwind v4 features like container queries and CSS variables
- Rely on Tailwind classes rather than inline styles or external CSS files for a unified design language
- note that tailwind is compiled by `make tailwind` with watch flag.

## Running tests

Run:
```
make test
```
### Run specific test namespace

```
make test-single TEST=<test-ns>/<deftest-name>
```
For example:
```
make test-single TEST=proto.search.chained-int-test/test-has-with-subselect-optimization
```
You can run all tests in one namespace, for example:
```
make test-single TEST=proto.search.chained-int-test
```

## Development Workflow

- Prefer `rg` for searching over `find` or `grep`
- NEVER write markdown files unless asked!
