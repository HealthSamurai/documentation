---
description: this topic explains the configuration and internals of aidboxdb image
---

# Use aidboxdb image

### Introduction

aidboxdb image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated). 

The aidboxdb image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica of the master database

An open source tool [wal-g](https://github.com/wal-g/wal-g) is used by aidboxdb for continuous archival, backups, and restoration.

aidboxdb image is tagged by PostgreSQL version from which it is built. For example, if you want to use 11.11 PostgreSQL version you should pull healthsamurai/aidboxdb:11.11 image.

The image is configured by supplying environment variables and command line arguments on startup. Keep in mind that you should not change the environment variables once you have initialised the database. The image expects them to be immutable.

### Required environment variables

| Env variable name | Meaning |
| :--- | :--- |
| `POSTGRES_USER` | Name of the user that will be created during db initialization |
| `POSTGRES_PASSWORD` | Password for that user |
| `POSTGRES_DB` | Name of the database to be created on startup |
| `WALG_` variables | Credentials for storage and bucket name for wal-g to use. Refer to the [official docs](https://github.com/wal-g/wal-g#configuration) for the details. |

### Optional environment variables

| Env variable name | Default | Meaning |
| :--- | :--- | :--- |
| `PGDATA` |  | Path to the postgresql cluster directory in the filesystem. /data by default. |
| `PG_ROLE` |  | When set to "replica" image proceeds to the streaming replica mode |
| `PG_REPLICA` |  | Name of the replication slot to be created in master database. Should only contain lower case letters, numbers, and the underscore character. |
| `PG_MASTER_HOST` |  | Master database host for streaming replica |
| `ENABLE_PGAGENT` |  | When present and `PG_ROLE` is not set to "replica" starts `pgagent` daemon on `aidboxdb` start. |
| `PGAGENT_DB` | _Value of`POSTGRES_DB` variable_ | Database where `pgagent` data is stored. If value is set, then **database must exist** on container start up. |
| `PGAGENT_LOG_FILE_PATH` | _`"/tmp/pgagent.logs"`_ | Path to file where `pgagent` messages are logged |
| `PGAGENT_LOG_LEVEL` | _`0`_ | `0` error, `1` warning, `2` debug. |



