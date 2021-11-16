---
description: Using WAL-G and pg_dump for archiving and restoration
---

# Archiving

## WAL-G

[**WAL-G**](https://github.com/wal-g/wal-g)** **is a simple and efficient archival restoration tool for PostgreSQL that simplifies [Continuous Archiving and Point-in-Time Recovery (PITR)](https://www.postgresql.org/docs/9.1/continuous-archiving.html) and can store backups in S3, Google Cloud Storage, Azure, Swift, remote host (via SSH) or local file system.

{% hint style="info" %}
`pg_basebackup` can also be used to take base backups of a running PostgreSQL database cluster, but we are using WAL-G.
{% endhint %}

In order to set up continuous backups, you need to do the following:&#x20;

1. Configure `wal-g` access to external storage.
2.  Enable archiving of logs in Postgres and write the archive command:

    `archive_command = 'wal-g wal-push% p' `
3.  Make a base backup&#x20;

    `wal-g backup-push $PGDATA`

    You can always restore the database from base backup if your database is corrupted.
4. Use [cron jobs](https://en.wikipedia.org/wiki/Cron) to schedule backup process (daily, weekly, etc.).
5.  To start rotating logs schedule this command using cron job:

    &#x20;`wal-g` `delete` `retain FULL 30`&#x20;

    This will delete all but 30 latest backups.

### Backups integrity:

WAL-G includes "wal-verify" command that checks backup integrity.&#x20;

You can use cron, Cronjob to schedule "wal-verify"  execution.&#x20;

Pick the JSON output of the command. The format is explained [here](archiving.md#wal-g).

### ** Aidboxdb recovery:**

1. Configure WAL-G access to external storage
2. Download backup `wal-g` `backup-pull` `$PGDATA`
3. Configure the` wal-g wal-fetch` restore command&#x20;
4. Start Postgres

Postgres downloads all the missing logs and reads them on start.&#x20;

{% hint style="info" %}
In replica mode Postgres operates in "read-only" mode, continues to receive WAL logs and lags minimum one WAL file behind main instance.
{% endhint %}

### Incremental backups

You can configure incremental backups with _env_ variable `WALG_DELTA_MAX_STEPS`.&#x20;

So the backup will be faster, but the recovery process will take longer.

### What WAL-G doesn't work for?

* WAL-G is not a replacement of `pg_dump`. `pg_dump` is used to create a logical dump of one or several DBs in cluster.&#x20;
* WAL-G doesn't schedule backup automatically.&#x20;

## pg\_dump&#x20;

**`pg_dump`** is a utility for backing up a PostgreSQL database.

It makes consistent backups even if the database is being used concurrently. **`pg_dump`** only dumps a single database. To back up an entire cluster, or to back up global objects that are common to all databases in a cluster, use **`pg_dumpall`**

Dumps can be output in SQL script or archive file formats. Script dumps are plain-text files containing SQL commands required to reconstruct the database to the state it was in at the time `pg_dump` was called.

To restore a database feed the dump file[ to psql](https://www.postgresql.org/docs/12/app-psql.html).

The alternative archive file formats[ must be used with **pg\_restore**](https://www.postgresql.org/docs/12/app-pgrestore.html)** **to restore the database.&#x20;

{% hint style="info" %}
`pg_dump` works best for small databases (< 5 GB) and assumes downtime.
{% endhint %}
