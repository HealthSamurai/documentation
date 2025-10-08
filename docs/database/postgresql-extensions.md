# PostgreSQL Extensions

Aidbox requires specific PostgreSQL extensions to function properly.

## Required PostgreSQL extensions

If any **required** extensions are missing, Aidbox won’t start.

* [unaccent](https://www.postgresql.org/docs/current/unaccent.html) — A text search dictionary that removes diacritics. Used in some searches.
* [pg\_trgm](https://www.postgresql.org/docs/current/pgtrgm.html) — Provides trigram matching support for text similarity.  Useful for some indexes.
* [fuzzystrmatch](https://www.postgresql.org/docs/current/fuzzystrmatch.html) — Implements string similarity functions such as Levenshtein. Used by the [MPI module](../modules/mpi/) only.
* [pgcrypto](https://www.postgresql.org/docs/current/pgcrypto.html) — Provides cryptographic functions for data encryption and hashing

## Optional PostgreSQL extensions

**Optional** extensions enhance functionality but are not critical. Missing optional extensions will trigger a warning at startup.

* [jsonknife](https://github.com/niquola/jsonknife) — Provides useful functions for jsonb operations. Particularly important for complex search queries in older PostgreSQL versions (pre-12) without native [jsonpath support](https://www.postgresql.org/docs/current/functions-json.html).
* [pg\_similarity](https://github.com/eulerto/pg_similarity) — Support similarity queries. Used by the [MPI module](../modules/mpi/) only.
* [pgagent](https://github.com/pgadmin-org/pgagent) — A job scheduling system that allows automating database maintenance tasks
* [pg\_stat\_statements](https://www.postgresql.org/docs/current/pgstatstatements.html) — Tracks execution statistics of SQL statements, useful for performance monitoring and troubleshooting
* [postgis](https://github.com/postgis/postgis) — Adds support for storing, indexing, and querying geographic data, required only when using the `Location.near` search parameter

## Installation Notes

Most of these extensions are included in standard PostgreSQL distributions. AidboxDB distribution comes with all required extensions pre-installed. \
\
For standard PostgreSQL installations, you may need to install some extensions manually using the `CREATE EXTENSION` command or through your package manager.\
\
For cloud-managed PostgreSQL services, consult your provider's documentation about supported extensions and how to enable them.
