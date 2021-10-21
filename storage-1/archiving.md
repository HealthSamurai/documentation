---
description: Wal_g and pg_dump approaches
---

# Archiving

## wal\_g

[**WAL-G**](https://github.com/wal-g/wal-g)** is an archival restoration tool for PostgreSQL (aidboxdb). **Postgres documentation explains that continuous integration and PITR can be implemented in postgres and explains how to set it up. But it doesn’t mention one important aspect -- where to store backups and how to rotate them. Wal-g is a collection of command line utilities **that make it easy to work with basic backups** (basebackup) pg. Pg\_basebakup utility also makes a basic backup, but we are using wal-g.

WAL-G is a simple and efficient PostgreSQL cloud backup tool.

Wal-g simplifies [Continuous Archiving and Point-in-Time Recovery (PITR)](https://postgrespro.ru/docs/postgresql/13/continuous-archiving?lang=en) and it has seamless integration with s3-storages (aws s3, google storage, azure storage)

In order to set up continuous backup, you need to do the following:&#x20;

1. Configure `wal-g` access to external storage
2. Enable archiving of logs in pg and write the archive command archive\_command = '`wal-g wal-push% p`'&#x20;
3. Make a basic backup `wal-g` `backup-push` `$PGDATA`. From this moment on, you can always restore the PG if it is destroyed.
4. Add the previous command to the crowns to your liking (for example, daily launch). Then the restoration to the current state will be much faster
5. Add another command `wal-g` `delete` `retain FULL 30` to confirm the crown. This command will delete the last 30 base backups and the actual logs for them. Everything else will be deleted. If daily backup is configured, then

**PG recovery:**

1. Configure `wal-g` access to external storage
2. Download backup `wal-g` `backup-push` `$PGDATA`
3. Configure the` wal-g wal-fetch` restaurant command&#x20;
4. Start pg
5. After starting the pg, it will automatically pump out the missing logs, read them and be ready for work, i.e. accept read / write requests. If you enable the replica mode, then after reading all the logs, the pg will switch to the “read-only” mode and it will also continue to download the incoming logs. the replica will work lagging behind the master by at least one file shaft (+ network delay for the transfer of the remaining shafts).

If the creation of a basic backup takes a long time, you can configure incremental backups of one env variable `WALG_DELTA_MAX_STEPS`. So the backup will be faster, but the recovery will take longer.

While you can do restoration and archiving with **`pg_dump`**,** `wal_g` **is even more efficient because you can restore any past point of your state. **`Wal_g`** also can do:

1. archive WAL files into cloud storage
2. create physical backups
3. recover a database up to any chosen point in time
4. make delta backups
5. recover after a crash from cloud archive

## pg\_dump&#x20;

**`pg_dump`** is a utility for backing up a PostgreSQL database.

It makes consistent backups even if the database is being used concurrently. **`pg_dump`** only dumps a single database. To back up an entire cluster, or to back up global objects that are common to all databases in a cluster, use **`pg_dumpall`**

Dumps can be output in script or archive file formats. Script dumps are plain-text files containing the SQL commands required to reconstruct the database to the state it was in at the time pg\_dump was called.

Script files can be used to reconstruct the database even on other machines and other architectures; with some modifications, even on other SQL database products. To restore a plain-text file [feed it to psql](https://www.postgresql.org/docs/12/app-psql.html).

The alternative archive file formats[ must be used with **pg\_restore**](https://www.postgresql.org/docs/12/app-pgrestore.html)** **to rebuild the database. **`pg_restore`** can be used to examine the archive and/or select which parts of the database are to be restored.

Suitable for small databases (< 5 GB) This approach assumes downtime
