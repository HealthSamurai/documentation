# PostgreSQL Extensions

Aidbox requires specific PostgreSQL extensions to function properly.  There are **required** and **optional** PostgreSQL extensions:

* If any **required** extensions are missing, Aidbox won’t start.&#x20;
* **Optional** extensions enhance functionality but are not critical. Missing optional extensions will trigger a warning at startup.

## Required PostgreSQL extensions

* [unaccent](https://www.postgresql.org/docs/current/unaccent.html) — A text search dictionary that removes diacritics. Used in some searches.
* [pg\_trgm](https://www.postgresql.org/docs/current/pgtrgm.html) —  Support for similarity of text using trigram matching. Useful for some indexes.
* [fuzzystrmatch](https://www.postgresql.org/docs/current/fuzzystrmatch.html) —  String similarities functions. Used by [MDM module](../modules/other-modules/mdm/) only.
* [pgcrypto](https://www.postgresql.org/docs/current/pgcrypto.html)

## Optional PostgreSQL extensions

* [jsonknife](https://github.com/niquola/jsonknife) — Useful functions for jsonb. It was the only way to create complex search queries in jsonb before PostgreSQL 12 ([jsonpath support](https://www.postgresql.org/docs/current/functions-json.html)).
* [pg\_similarity](https://github.com/eulerto/pg_similarity) — Support similarity queries. Used by [MDM module](../modules/other-modules/mdm/) only.
* [pgagent](https://github.com/pgadmin-org/pgagent) — A PostgreSQL job scheduler.
* [pg\_stat\_statements](https://www.postgresql.org/docs/current/pgstatstatements.html) — Track statistics.
* [postgis](https://github.com/postgis/postgis) — Support storing, indexing, and querying geographic data. Used by `Location.near` search parameter only.
