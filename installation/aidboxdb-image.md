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

The image is configured by supplying environment variables and command line arguments on startup.

### Required environment variables

| Env variable name | Meaning |
| :--- | :--- |
| POSTGRES\_USER | Name of the user that will be created during db initialization |
| POSTGRES\_PASSWORD | Password for that user |
| POSTGRES\_DB | Name of the database to be created on startup |
| WALG\_ variables | Credentials for storage and bucket name for wal-g to use. Refer to the [official docs](https://github.com/wal-g/wal-g#configuration) for the details. |

### Optional environment variables

| Env variable name | Meaning |
| :--- | :--- |
| PGDATA | Path to the postgresql cluster directory in the filesystem. /data by default. |
| PG\_ROLE | When set to "replica" image proceeds to the streaming replica mode |
| NODE\_NAME | Name of the replication slot to be created in master database |
| PG\_MASTER\_HOST | Master database host for streaming replica |
| NUM\_ATTEMPTS | Number of attempts to connect to the master before the image exits with error |

