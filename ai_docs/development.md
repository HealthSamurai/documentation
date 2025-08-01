Used technologies:
- Clojure
- HTMX
- Tailwind 4 (no .js config, just resources/public/app.css)

Development rules:
0. Always use `clojure-mcp` and `sequentialthinking` mcps.
1. When you change .clj files, ask to re-evaluate repl and check.
2. First think through the problem, read the codebase for relevant files, and write a plan
3. The plan should have a list of todo items that you can check off as you complete them
4. Before you begin working, check in with me and I will verify the plan.
5. Then, begin working on the todo items, marking them as complete as you go.
6. Please every step of the way just give me a high level explanation of what changes you made
7. Make every task and code change you do as simple as possible. We want to avoid making any massive or complex changes. Every change should impact as little code as possible. Everything is about simplicity.
8. When use styles, always prefer using tailwind classes and tailwind theme, do not use app.css if not necessary
9. Make sure you understand what tailwind v4 is and all its differences between v3. Read the doc https://tailwindcss.com/docs.
10. Use kaocha to run tests.

## Running Tests

The project uses Kaocha test runner. To run tests:

### Run all tests
```bash
clojure -M:test:kaocha
```

### Run specific test namespace
```bash
clojure -M:test:kaocha --focus namespace-name
# Example: clojure -M:test:kaocha --focus gitbok.ui.breadcrumb-test
```

### Run with additional options
```bash
# Watch mode (re-run tests on file changes)
clojure -M:test:kaocha --watch

# Fail fast (stop on first failure)
clojure -M:test:kaocha --fail-fast

# Print test names as they run
clojure -M:test:kaocha --reporter documentation
```

The test configuration is defined in `deps.edn` under the `:test` and `:kaocha` aliases.
