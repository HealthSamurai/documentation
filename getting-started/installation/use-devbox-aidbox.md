---
description: The topic explains configuration options of Devbox and Aidbox distributions
---

# Configure Devbox/Aidbox distribution

## Introduction

[Devbox](https://hub.docker.com/r/healthsamurai/devbox) is a lightweight version of Aidbox. It is a free version aimed at local development and can not be used to store real healthcare data or PHI.

[Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [Multibox ](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox)distribution.

All distributions are intended to be used with [healthsamurai/aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb) postgres distribution. If you want to use DBaaS solution for storage, contact your support team to check if it is implemented.

All distributions require license details to be provided. Alongside with online license server, we offer a special license server distribution for the deployment environments that do not have access to the internet. Contact your support team for the details.

## Required environment variables

| Env variable name | Meaning |
| :--- | :--- |
| `AIDBOX_PORT` | A port that webserver will listen to |
| `AIDBOX_FHIR_VERSION` | Version of FHIR - 1.0.2, 1.4.0, 1.8.0, 3.0.1, 3.2.0, 3.3.0, 4.0.0 |
| `PGHOST` | Aidboxdb host address |
| `PGPORT` | Aidboxdb port |
| `PGDATABASE` | Name of the database that is used to store resources |
| `PGUSER` | Database role name to use |
| `PGPASSWORD` | A password of that role |
| `AIDBOX_CLIENT_ID` | Root Client resource id to create on startup |
| `AIDBOX_CLIENT_SECRET` | A secret for that Client |
| `AIDBOX_ADMIN_ID` | Root User resource id to create on startup |
| `AIDBOX_ADMIN_PASSWORD` | A password for that User |
| `AIDBOX_LICENSE_ID` | License id obtained from the license server |
| `AIDBOX_LICENSE_KEY` | Key for that license |

## Optional environment variables

<table>
  <thead>
    <tr>
      <th style="text-align:left">Env variable name</th>
      <th style="text-align:left">Meaning</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left"><code>AIDBOX_BASE_URL</code>
      </td>
      <td style="text-align:left">
        <p>URL to use in links between resources.</p>
        <p>Defaults to http://localhost:[AIDBOX_PORT]</p>
      </td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_DB_PARAM_*</code>
      </td>
      <td style="text-align:left">Parameters prefixed with <code>AIDBOX_DB_PARAM_</code> will be passed to
        <a
        href="https://jdbc.postgresql.org/documentation/80/connect.html">JDBC PostgreSQL connection string</a>. For example, parameters: <code>AIDBOX_DB_PARAM_SSL=true</code>  <code>AIDBOX_DB_PARAM_SSLMODE=verify-ca</code> will
          add <code>ssl=true&amp;sslmode=verify-ca</code> params to connection string</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_ES_URL</code>
      </td>
      <td style="text-align:left">If provided, enables mode to push logs to ElasticSearch.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_ES_AUTH</code>
      </td>
      <td style="text-align:left">Basic auth credentials for ElasticSearch in the format of &quot;user:password&quot;.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_ES_BATCH_SIZE</code>
      </td>
      <td style="text-align:left">Size of log batch, used to optimize performance of log shipping. The default
        value is 200.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_ES_BATCH_TIMEOUT</code>
      </td>
      <td style="text-align:left">Timeout to post a batch to ElasticSearch if there are not enough records
        to reach full batch size.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_ES_INDEX_PAT</code>
      </td>
      <td style="text-align:left">Custom index format string. The default value is &apos;aidbox-logs&apos;-yyyy-MM-dd.
        By changing this setting you can control how often new indices should be
        created.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_LOGS</code>
      </td>
      <td style="text-align:left">If provided, enables mode to pipe logs as json into the file by specified
        path. If ElasticSearch URL is provided then the file is used as a fallback
        in case if ElasticSearch is not available.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_LOGS_MAX_LINES</code>
      </td>
      <td style="text-align:left">Sets the limit of log records to push into the file. When the limit is
        reached, the current log file is renamed with &quot;.old&quot; postfix
        and a new log file is created. The default value is &quot;10000&quot;.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_STDOUT_JSON</code>
      </td>
      <td style="text-align:left">If provided, enables mode to write logs as json into stdout.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_STDOUT_PRETTY</code>
      </td>
      <td style="text-align:left">If provided, enables mode to write logs in prettified format into stdout</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_DEVLOGS</code>
      </td>
      <td style="text-align:left">If provided, pushes logs into _logs table of aidboxdb. Can be useful for
        testing and debugging.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_DD_API_KEY</code>
      </td>
      <td style="text-align:left">If provided, enables mode to push logs to DataDog</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_DD_BATCH_SIZE</code>
      </td>
      <td style="text-align:left">Size of log batch, used to optimize performance of log shipping. The default
        value is 200.</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_DD_BATCH_TIMEOUT</code>
      </td>
      <td style="text-align:left">Timeout (in ms) to post a batch to DataDog if there are not enough records
        to reach full batch size. Default value: 3600000 (1 hour)</td>
    </tr>
    <tr>
      <td style="text-align:left"><code>AIDBOX_DD_LOGS</code>
      </td>
      <td style="text-align:left">Fallback file to write logs in if uploading to DataDog fails</td>
    </tr>
  </tbody>
</table>

### Configuring SSL connection with PostgreSQL

Parameters prefixed with AIDBOX\_DB\_PARAM is passed to JDBC PostgreSQL connection string.

For an instance:

AIDBOX\_DB\_PARAM\_SSL=true  
AIDBOX\_DB\_PARAM\_SSLMODE=verify-ca

will add ssl=true&sslmode=verify-ca params to connection string.

These parameters will enable SSL connection from Aidbox to postgresql Docs on JDBC PostgreSQL connection string are here: [https://jdbc.postgresql.org/documentation/80/connect.html](https://jdbc.postgresql.org/documentation/80/connect.html) [https://jdbc.postgresql.org/documentation/head/ssl-client.html](https://jdbc.postgresql.org/documentation/head/ssl-client.html)

The next step is to configure your database to accept SSL connections. You can do that by passing your own postgresql.conf with argument -c config\_file passed into the db containter and probably you want to set up postgres to receive only SSL connections, you can do that by passing your own pg\_hba.conf file with -c hba\_file

### Usage on Apple M1 \(ARM architecture\)

We are aware of performance issues with the aidbox and aidboxdb images on the recently released M1 processors. As a workaround we offer you to use static builds of aidbox and aidboxdb precompiled specifically for the ARM architecture. 

Please check the status updates [here](https://github.com/Aidbox/Issues/issues/393).

