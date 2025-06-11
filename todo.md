# Todo

## Most important

- [ ] deploy
- [ ] ci
- [ ] design (Kirill)
- [ ] new UI to move pages (handles references and creates redirect)
- [ ] tracking
- [ ] 404 analytics?

## CI

- [ ] check links
  - [ ] verify summary & files
  - [ ] grep `broken-reference` does not exists
  - [ ] make sure every link is valid = check that file exists (clojure tests)
  - [ ] check absolute references
- [ ] check redirects, some are 404 already

# bugs/techdebt

- [ ] do not read on refresh
- [ ] cache html
- [ ] rm huge image history from repo
- [ ] redirects with sections should work api-1/api/search-1/search-parameters-list/\_explain: api/rest-api/aidbox-search.md#aidbox-special-search-parameters
- [ ] table and cards http://localhost:8081/api-1/api/search-1/search-parameters-list/_filter

## UX

- [ ] move page with references and save redirect
- [ ] create page
- [ ] write page in ui?

## UI

- [ ] pretty design
- [ ] mobile view
- [ ] check that everything nice in zoom level

## Tests

- [ ] test page-link->uri function
- [ ] using .. in url should be not possible

## Features

- [ ] rag
- [ ] tracking
- [ ] next/prev pages
- [ ] pretty render system resource reference
- [ ] feedback buttons?
