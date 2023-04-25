# pg\_dump

[**`pg_dump`**](https://www.postgresql.org/docs/15/app-pgdump.html) is a standard utility for backing up a PostgreSQL database.

It makes consistent backups even if the database is being used concurrently. **`pg_dump`** only dumps a single database. To back up an entire cluster, or to back up global objects that are common to all databases in a cluster, use **`pg_dumpall`**

Dumps can be output in SQL script or archive file formats. Script dumps are plain-text files containing SQL commands required to reconstruct the database to the state it was in at the time `pg_dump` was called.

```bash
psql -d aidbox -f aidbox_dump.sql
```

You can compress your dump using pipe gzip

```bash
pg_dump aidbox | gzip > aidbox_dump.sql.gz
```

To restore a database feed the dump file[ to psql](https://www.postgresql.org/docs/12/app-psql.html).

```
cat aidbox_dump.sql | psql aidbox
```

Or, if you use a compressed dump

```bash
gunzip -c aidbox_dump.sql.gz | psql aidbox
```

The alternative archive file formats[ must be used with **pg\_restore**](https://www.postgresql.org/docs/12/app-pgrestore.html) to restore the database.

{% hint style="info" %}
`pg_dump` works best for small databases (< 10 GB) and assumes downtime.
{% endhint %}
