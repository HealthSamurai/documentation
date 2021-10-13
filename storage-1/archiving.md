---
description: Wal_g and pg_dump approaches
---

# Archiving

## wal_g

While you can do restoration and archiving with **`pg_dump`**,** `wal_g` **is even more efficient because you can restore any past point of your state. **`Wal_g`** also can do:

1. archive WAL files into cloud storage
2. create physical backups
3. recover a database up to any chosen point in time
4. make delta backups
5. recover after a crash from cloud archive

## pg_dump 

**`pg_dump`** is a utility for backing up a PostgreSQL database.

It makes consistent backups even if the database is being used concurrently. **`pg_dump`** only dumps a single database. To back up an entire cluster, or to back up global objects that are common to all databases in a cluster, use **`pg_dumpall`**

Dumps can be output in script or archive file formats. Script dumps are plain-text files containing the SQL commands required to reconstruct the database to the state it was in at the time pg_dump was called.

Script files can be used to reconstruct the database even on other machines and other architectures; with some modifications, even on other SQL database products. To restore a plain-text file [feed it to psql](https://www.postgresql.org/docs/12/app-psql.html).

The alternative archive file formats[ must be used with **pg_restore**](https://www.postgresql.org/docs/12/app-pgrestore.html)** **to rebuild the database. **`pg_restore`** can be used to examine the archive and/or select which parts of the database are to be restored.

Suitable for small databases (< 5 GB) This approach assumes downtime
