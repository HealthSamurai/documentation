---
description: this topic explains the configuration and internals of aidboxdb image
---

# aidboxdb image

### Introduction

aidboxdb image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).&#x20;

The aidboxdb image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica of the master database

An open source tool [wal-g](https://github.com/wal-g/wal-g) is used by aidboxdb for continuous archival, backups, and restoration.

aidboxdb image is tagged by PostgreSQL version from which it is built. For example, if you want to use 11.11 PostgreSQL version you should pull healthsamurai/aidboxdb:11.11 image.

Supported PostgreSQL versions: [14.2](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/14.2/images/sha256-5bdc4e259785be6c9741bd6faab8d37a8737154062fab8a84a7d68c7d81a5f6f), [13.6](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/13.6/images/sha256-49097e7fb0d60798dbdfe4a3ba31dc324abe232e399a78a487ab91dbd892e2c1?context=explore), [13.2](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/13.2/images/sha256-31294389f0339edeff3926ce0f27c856194f6e934ac744af5aa776b1f675dfe1?context=explore), [12.6](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/12.6/images/sha256-2a4fc68fc80c0f6e48ddd06b4dcd8a1cab72f2ab13968cc37b06fd2a53e85070?context=explore), [11.11](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/11.11/images/sha256-9e767a6f1a0d21faf8542edcdc9f11ba8e836889f6a05d38e29003297037d136?context=explore)

The image is configured by supplying environment variables and command line arguments on startup. Keep in mind that you should not change the environment variables once you have initialised the database. The image expects them to be immutable.

### Required environment variables

| Env variable name   | Meaning                                                                                               |
| ------------------- | ----------------------------------------------------------------------------------------------------- |
| `POSTGRES_USER`     | Name of the user that will be created during db initialization                                        |
| `POSTGRES_PASSWORD` | Password for that user                                                                                |
| `POSTGRES_DB`       | Name of the database to be created on startup                                                         |
| `WALG_` variables   | Credentials for storage and bucket name for wal-g to use. Refer to the official docs for the details. |

### Optional environment variables

| Env variable name       | Default                                 | Meaning                                                                                                                                                                                                                                                  |
| ----------------------- | --------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `PGDATA`                |                                         | Path to the postgresql cluster directory in the filesystem. /data by default.                                                                                                                                                                            |
| `PG_ROLE`               |                                         | When set to "replica" image proceeds to the streaming replica mode                                                                                                                                                                                       |
| `PG_REPLICA`            |                                         | Name of the replication slot to be created in master database. Should only contain lower case letters, numbers, and the underscore character.                                                                                                            |
| `PG_MASTER_HOST`        |                                         | Master database host for streaming replica                                                                                                                                                                                                               |
| `PGAGENT_ENABLED`       |                                         | <p>When present and <code>PG_ROLE</code> is not set to <code>"replica"</code> starts <code>pgagent</code> daemon on <code>aidboxdb</code> start.</p><p><a href="../../app-development-guides/tutorials/working-with-pgagent.md">pgAgent tutorial</a></p> |
| `PGAGENT_DB`            | _Value of`POSTGRES_DB` variable_        | Database where `pgagent` data is stored. If value is set, then **database must exist** on container start up.                                                                                                                                            |
| `PGAGENT_LOG_FILE_PATH` | _`"/tmp/pgagent.logs"`_                 | Path to file where `pgagent` messages are logged                                                                                                                                                                                                         |
| `PGAGENT_LOG_LEVEL`     | _`0`_                                   | `0` error, `1` warning, `2` debug.                                                                                                                                                                                                                       |
| `PGAGENT_USER`          | postgres                                | If you want to use custom user  for `pgagent` you can specify in this variable.                                                                                                                                                                          |
| `PGAGENT_PASSWORD`      | _Value of `POSTGRES_PASSWORD` variable_ | Password for **custom** `pgagent` user.                                                                                                                                                                                                                  |

