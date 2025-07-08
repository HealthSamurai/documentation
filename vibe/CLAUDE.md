# What to do

You are the best technical writer and aidbox engineer, who writes a documentation about Aidbox for developers and architects, with basic technical knowledge and minimal knowledge of Aidbox.
You have to write a overview of "Database" section of Aidbox documentation.

## Structure of desired overview page

* How data are stored in Aidbox?
* How data inserted, updated and deleted?
  * crud &  history
  * transaction (tx, batch vs transaction)
  * bulk inserts, updates and deletes
  * migrations
* How to query data?
  * using FHIR search
  * using SQL
  * SQL on FHIR (using ViewDefinitions)
* Which PostgreSQL can be used with Aidbox?
  * which extensions are used
* How to optimize performance?
  * indexes & monitoring
  * performance tuning
  * materialized views
  * postgres configuration

Feel free to rewrite names of headers, but only them. Do not add/remove any headers.

## Sources
You have: 
* source code examples in ./aidbox-sources/*.clj
* legacy documentation in ../docs/database. in ../docs/database/overview, we think that `How data are stored in Aidbox?` section is written nicely. You can query any data in ../docs, there are about 600 pages there.
* for postgres jsonb reference, use https://www.postgresql.org/docs/17/datatype-json.html and https://www.postgresql.org/docs/17/functions-json.html
* Extra information about JSONB (why jsonb is good) - https://www.architecture-weekly.com/p/postgresql-jsonb-powerful-storage?hide_intro_popup=true
* official FHIR specification about storage - https://build.fhir.org/storage.html
* notes on TOASTS for JSONB (advanced topic) - https://pganalyze.com/blog/5mins-postgres-jsonb-toast
* for transaction isolation - https://www.postgresql.org/docs/current/transaction-iso.html
* SQL ON FHIR:
    * current aidbox docs - ../docs/modules/sql-on-fhir 
    * specification - https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/
* fhir http api (history, crud) https://www.hl7.org/fhir/http.html

Feel free to use web search on topics you want to clarify.

## How to write

1. We need book like database overview - so it's interesting to read for developers.
2. Write with simple and concise language, but be careful with technical details. Use styleguide: ./styleguide.md
3. Overview should provide a top level picture of how Aidbox works with database (postgresql).
4. At the end of the section, add references to related articles/pages in markdown format. The markdown link should reference the desired file relatively to docs/database/overview.md.
5. Move by section, write a good section first (create `<section-name>.md` file), then ask for feedback in editor notes (see editor-notes.md). After the iteration, write the summary into editor-notes.md. Name the iteration, wait for notes in this file. Add "EDITOR NOTES:" line. When section is done, "merge" it into ./database.md. If the file does not exist, create it.
6. When postgres feature is mentioned, add markdown reference to it
7. Do not reference related resources in own header, after each section (after the text, but before new header) add something like "see also: <list of references>".
8. When section is written in file, first line must mention which file should this section be merged to.
9. You write overview, not a tutorial. You should write the state of the Aidbox, not trying to "sell" the feature.
10. Do not use " **Something**: some text " template.
11. When adding a list, try to find a related link and use it in markdown way.
12. When a new name of something is encountered first, add reference to it in text.
13. When you merge sections into page, make sure you have only one h1 header (the title) and all sections start with h2 header (you can also use h3 and h4 headers).
14. After all sections are done, suggest to update the text after the h1 header, but before first h2 header. It is "overview" of a page. It should explain, what is in this page.
15. After all sections are done, add description at the start of the file in format "---\ndescription: <description>\n---". This description will be available in UI and in meta tags for SEO. Make sure it is one-liner.


Plan what are you doing, put the plan into ./log.md file.
Use ./log.md file as your memory. 
Write a documentation and update ./log.md file.
