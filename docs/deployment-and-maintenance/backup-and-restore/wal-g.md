---
description: WAL-G PostgreSQL backup tool for continuous archiving and PITR. Supports S3, GCS, Azure cloud storage with incremental backups.
---

# WAL-G

[**WAL-G**](https://github.com/wal-g/wal-g) is a simple and efficient archival restoration tool for PostgreSQL that simplifies [Continuous Archiving and Point-in-Time Recovery (PITR)](https://www.postgresql.org/docs/9.1/continuous-archiving.html) and can store backups in S3, Google Cloud Storage, Azure, Swift, remote host (via SSH), or local file system.

In order to set up continuous backups, you need to do the following:

#### Configure `wal-g` access to [external storage](https://wal-g.readthedocs.io/STORAGES/)

For example. If you use GCP GCS you should configure ENV Variables

```bash
WALG_GS_PREFIX=gs://my-bucket/walg-folder
GOOGLE_APPLICATION_CREDENTIALS=/path/to/service/account.json
```

#### Enable archiving of logs in Postgres and specify [archive command](https://www.postgresql.org/docs/current/runtime-config-wal.html#RUNTIME-CONFIG-WAL-ARCHIVING) and [restore\_command](https://www.postgresql.org/docs/current/runtime-config-wal.html#RUNTIME-CONFIG-WAL-ARCHIVE-RECOVERY)

{% code title="postgresql.conf" %}
```ini
archive_command = 'wal-g wal-push %p'
restore_command = 'wal-g wal-fetch %f %p'
```
{% endcode %}

#### Make a base backup

For taking a backup you should run `wal-g backup-push` inside the AidboxDb container

```bash
wal-g backup-push $PGDATA
wal-g backup-list
```

You can always restore the database from base backup if your database is corrupted.

#### To start rotating wal logs and backups schedule you can schedule retain cron job

```bash
wal-g delete retain FULL 30 --confirm
```

This will delete all but 30 latest backups.

#### Backups integrity

WAL-G includes [`wal-g wal-verify`](https://wal-g.readthedocs.io/PostgreSQL/#wal-verify) a command that checks backup integrity.

```bash
wal-g wal-verify integrity timeline # perform integrity and timeline checks
wal-g wal-verify integrity # perform only integrity check
```

### **Aidboxdb recovery:**

1. Configure WAL-G access to external storage
2. Download backup `wal-g` `backup-pull` `$PGDATA`
3. Configure the `wal-g wal-fetch` restore command
4. Start Postgres

Postgres download all the missing logs and read them on start.

{% hint style="info" %}
In replica mode Postgres operates in "read-only" mode, continues to receive WAL logs and lags minimum one WAL file behind main instance.
{% endhint %}

### Incremental backups

You can configure incremental backups with _env_ variable `WALG_DELTA_MAX_STEPS`.

So the backup will be faster, but the recovery process will take longer.

### What WAL-G doesn't work for?

* WAL-G is not a replacement of `pg_dump`. `pg_dump` is used to create a logical dump of one or several DBs in cluster.
* WAL-G doesn't schedule backup automatically.
