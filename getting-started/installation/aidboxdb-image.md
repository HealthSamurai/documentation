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
| `WALG_` variables | Credentials for storage and bucket name for wal-g to use. Refer to the official docs for the details. |

### Optional environment variables

<table>
  <thead>
    <tr>
      <th style="text-align:left">Env variable name</th>
      <th style="text-align:left">Default</th>
      <th style="text-align:left">Meaning</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left"><code>PGDATA</code>
      </td>
      <td style="text-align:left"></td>
      <td style="text-align:left">Path to the postgresql cluster directory in the filesystem. /data by default.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PG_ROLE</code>
      </td>
      <td style="text-align:left"></td>
      <td style="text-align:left">When set to &quot;replica&quot; image proceeds to the streaming replica
        mode</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PG_REPLICA</code>
      </td>
      <td style="text-align:left"></td>
      <td style="text-align:left">Name of the replication slot to be created in master database. Should
        only contain lower case letters, numbers, and the underscore character.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PG_MASTER_HOST</code>
      </td>
      <td style="text-align:left"></td>
      <td style="text-align:left">Master database host for streaming replica</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PGAGENT_ENABLED</code>
      </td>
      <td style="text-align:left"></td>
      <td style="text-align:left">
        <p>When present and <code>PG_ROLE</code> is not set to <code>&quot;replica&quot;</code> starts <code>pgagent</code> daemon
          on <code>aidboxdb</code> start.</p>
        <p><a href="../../app-development-guides/tutorials/working-with-pgagent.md">pgAgent tutorial</a>
        </p>
      </td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PGAGENT_DB</code>
      </td>
      <td style="text-align:left"><em>Value of<code>POSTGRES_DB </code>variable</em>
      </td>
      <td style="text-align:left">Database where <code>pgagent</code> data is stored. If value is set, then <b>database must exist</b> on
        container start up.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PGAGENT_LOG_FILE_PATH</code>
      </td>
      <td style="text-align:left"><em><code>&quot;/tmp/pgagent.logs&quot;</code></em>
      </td>
      <td style="text-align:left">Path to file where <code>pgagent</code> messages are logged</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PGAGENT_LOG_LEVEL</code>
      </td>
      <td style="text-align:left"><em><code>0</code></em>
      </td>
      <td style="text-align:left"><code>0</code> error, <code>1</code> warning, <code>2</code> debug.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PGAGENT_USER</code>
      </td>
      <td style="text-align:left">postgres</td>
      <td style="text-align:left">If you want to use custom user for <code>pgagent</code> you can specify
        in this variable.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>PGAGENT_PASSWORD</code>
      </td>
      <td style="text-align:left"><em>Value of <code>POSTGRES_PASSWORD</code> variable</em>
      </td>
      <td style="text-align:left">Password for <b>custom</b>  <code>pgagent</code> user.</td>
    </tr>
  </tbody>
</table>



