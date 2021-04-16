---
description: The topic explains configuration options of Devbox and Aidbox distributions
---

# Configure Devbox/Aidbox distribution

### Introduction

\*\*\*\*[**Devbox**](https://hub.docker.com/r/healthsamurai/devbox) ****is a lightweight version of Aidbox. It is a free version aimed at local development and can not be used to store real healthcare data or PHI.  

[**Aidbox**](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [**Multibox**](https://hub.docker.com/r/healthsamurai/multibox) distribution.

All distributions are intended to be used with [healthsamurai/aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb) postgres distribution. If you want to use DBaaS solution for storage, contact your support team to check if it is implemented.

All distributions require license details to be provided. Alongside with online license server, we offer a special license server distribution for the deployment environments that do not have access to the internet. Contact your support team for the details.

### Required environment variables

| Env variable name | Meaning |
| :--- | :--- |
| AIDBOX\_PORT | A port that webserver will listen to |
| AIDBOX\_FHIR\_VERSION | Version of FHIR - 1.0.2, 1.4.0, 1.8.0, 3.0.1, 3.2.0, 3.3.0, 4.0.0 |
| PGHOST | Aidboxdb host address |
| PGPORT | Aidboxdb port |
| PGDATABASE | Name of the database that is used to store resources |
| PGUSER | Database role name to use |
| PGPASSWORD |  A password of that role |
| AIDBOX\_CLIENT\_ID | Root Client resource id to create on startup |
| AIDBOX\_CLIENT\_SECRET | A secret for that Client |
| AIDBOX\_ADMIN\_ID | Root User resource id to create on startup |
| AIDBOX\_ADMIN\_PASSWORD | A password for that User |
| AIDBOX\_LICENSE\_ID | License id obtained from the license server |
| AIDBOX\_LICENSE\_KEY | Key for that license |

### Optional environment variables

| Env variable name | Meaning |
| :--- | :--- |
| AIDBOX\_BASE\_URL | URL to use in links between resources. Defaults to http://localhost:\[AIDBOX\_PORT\] |
| AIDBOX\_OFFLINE\_LICENSE | If set to anything license is validated in offline mode. |
| AIDBOX\_LICENSE\_SERVER\_URL | URL of the offline license server. |
| AIDBOX\_ES\_URL | If provided, enables mode to push logs to ElasticSearch. |
| AIDBOX\_ES\_AUTH | Basic auth credentials for ElasticSearch in the format of "user:password". |
| AIDBOX\_ES\_BATCH\_SIZE | Size of log batch, used to optimize performance of log shipping. The default value is 200. |
| AIDBOX\_ES\_BATCH\_TIMEOUT | Timeout to post a batch to ElasticSearch if there are not enough records to reach full batch size. |
| AIDBOX\_ES\_INDEX\_PAT | Custom index format string. The default value is 'aidbox-logs'-yyyy-MM-dd. By changing this setting you can control how often new indices should be created. |
| AIDBOX\_LOGS | If provided, enables mode to pipe logs as json into the file by specified path. If ElasticSearch URL is provided then the file is used as a fallback in case if ElasticSearch is not available. |
| AIDBOX\_LOGS\_MAX\_LINES | Sets the limit of log records to push into the file. When the limit is reached, the current log file is renamed with ".old" postfix and a new log file is created. The default value is "10000". |
| AIDBOX\_STDOUT\_JSON | If provided, enables mode to write logs as json into stdout. |
| AIDBOX\_DEVLOGS | If provided, pushes logs into \_logs table of aidboxdb. Can be useful for testing and debugging. |

