# AidboxDB environment variables

### Required environment variables

| Env variable name   | Meaning                                                                                               |
| ------------------- | ----------------------------------------------------------------------------------------------------- |
| `POSTGRES_USER`     | Name of the user that will be created during db initialization                                        |
| `POSTGRES_PASSWORD` | Password for that user                                                                                |
| `POSTGRES_DB`       | Name of the database to be created on startup                                                         |
| `WALG_` variables   | Credentials for storage and bucket name for wal-g to use. Refer to the official docs for the details. |

### Optional environment variables

| Env variable name       | Default                                 | Meaning                                                                                                                                                                                                                                 |
| ----------------------- | --------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `PGDATA`                |                                         | Path to the postgresql cluster directory in the filesystem. /data by default.                                                                                                                                                           |
| `PG_ROLE`               |                                         | When set to "replica" image proceeds to the streaming replica mode                                                                                                                                                                      |
| `PG_REPLICA`            |                                         | Name of the replication slot to be created in master database. Should only contain lower case letters, numbers, and the underscore character.                                                                                           |
| `PG_MASTER_HOST`        |                                         | Master database host for streaming replica                                                                                                                                                                                              |
| `PGAGENT_ENABLED`       |                                         | <p>When present and <code>PG_ROLE</code> is not set to <code>"replica"</code> starts <code>pgagent</code> daemon on <code>aidboxdb</code> start.</p><p><a href="../../storage-1/other/working-with-pgagent.md">pgAgent tutorial</a></p> |
| `PGAGENT_DB`            | _Value of`POSTGRES_DB` variable_        | Database where `pgagent` data is stored. If value is set, then **database must exist** on container start up.                                                                                                                           |
| `PGAGENT_LOG_FILE_PATH` | _`"/tmp/pgagent.logs"`_                 | Path to file where `pgagent` messages are logged                                                                                                                                                                                        |
| `PGAGENT_LOG_LEVEL`     | _`0`_                                   | `0` error, `1` warning, `2` debug.                                                                                                                                                                                                      |
| `PGAGENT_USER`          | postgres                                | If you want to use custom user  for `pgagent` you can specify in this variable.                                                                                                                                                         |
| `PGAGENT_PASSWORD`      | _Value of `POSTGRES_PASSWORD` variable_ | Password for **custom** `pgagent` user.                                                                                                                                                                                                 |
| `EXTRA_LOCALES`         |                                         | Adds locales for PostgreSQL to work with. May pass many locales, must be separated by comma.                                                                                                                                            |
