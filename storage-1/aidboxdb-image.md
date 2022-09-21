---
description: This topic explains the configuration and internals of aidboxdb image
---

# AiboxDB

### Introduction

AidboxDB image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).&#x20;

```bash
docker run \
       -v "$(pwd)"/pgdata:/data/pg \
       -e POSTGRES_USER='user' \
       -e POSTGRES_PASSWORD='pass' \
       -e POSTGRES_DB='aidbox' \
       -e PGDATA="/data/pg" \
       healthsamurai/aidboxdb:14.5
```

The AidboxDB image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica
* Optimized FHIR search queries
* Backup and maintenance extensions

### Versioning

AidboxDB image is tagged by PostgreSQL version from which it is built. For example, if you want to use 14.5 PostgreSQL version you should pull [healthsamurai/aidboxdb:14.5](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.5/images/sha256-24accc760960f6abed0f9b2d2382712e5b98aa382403887e24408f0f0fdcf58d?context=repo) image.

Supported PostgreSQL versions: [15 (beta)](https://hub.docker.com/layers/healthsamurai/aidboxdb/15-beta/images/sha256-7a4254f58f823303b1164755b2e5cc068d4cf66adc21a6b8b8569ecec8bde985?context=explore), [14.5](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.5/images/sha256-24accc760960f6abed0f9b2d2382712e5b98aa382403887e24408f0f0fdcf58d?context=explore), [13.8](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.8/images/sha256-5634c7d61ef9c975014fd87435759d1d5d7fd9ba08692aad18b13e1ff6013eed)

Available versions: [14.2](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.2/images/sha256-770eb8be3d51fa7c9829ab40c51588833ab91b613894cf80dc7b633f934a890e?context=repo), [13.6](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.6/images/sha256-49097e7fb0d60798dbdfe4a3ba31dc324abe232e399a78a487ab91dbd892e2c1?context=repo), [13.2](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.2/images/sha256-31294389f0339edeff3926ce0f27c856194f6e934ac744af5aa776b1f675dfe1?context=repo), [12.12](https://hub.docker.com/layers/healthsamurai/aidboxdb/12.12/images/sha256-8a898079a8dc3f9a46a652632450738cd1e88f340838fef8f2bc7101d1ab3e00?context=repo), [12.6](https://hub.docker.com/layers/healthsamurai/aidboxdb/12.6/images/sha256-2a4fc68fc80c0f6e48ddd06b4dcd8a1cab72f2ab13968cc37b06fd2a53e85070?context=repo), [11.17](https://hub.docker.com/layers/healthsamurai/aidboxdb/11.17/images/sha256-fbf8c08c00d4be9993ef61429272078242b3d808e98ea30e9f45d1f1b0b0a93d?context=repo), [11.11](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/11.11/images/sha256-9e767a6f1a0d21faf8542edcdc9f11ba8e836889f6a05d38e29003297037d136?context=explore)

### Extensions

List of additional availiable extensions:

* [jsonknife](https://github.com/niquola/jsonknife) — Jsonb extraction tool for optimizing FHIR search
* [pgagent](https://github.com/pgadmin-org/pgagent) — A PostgreSQL job scheduler &#x20;
* [pg\_repack](https://github.com/reorg/pg\_repack) — Reorganize tables in PostgreSQL databases with minimal locks&#x20;
* [jsquery](https://github.com/postgrespro/jsquery) — JSON query language with GIN indexing support
* [wal2json](https://github.com/eulerto/wal2json) — Is an output plugin for logical decoding
* [wal-g](https://github.com/wal-g/wal-g) — Archival and Restoration for databases in the Cloud

### Configuration

The image is configured by supplying environment variables and command line arguments on startup. Keep in mind that you should not change the environment variables once you have initialized the database. The image expects them to be immutable.

{% content-ref url="../reference/configuration/environment-variables/aidboxdb-environment-variables.md" %}
[aidboxdb-environment-variables.md](../reference/configuration/environment-variables/aidboxdb-environment-variables.md)
{% endcontent-ref %}

### Archiving

An open-source tool [wal-g](https://github.com/wal-g/wal-g) is used by AidboxDB for continuous archival, backups, and restoration.

{% content-ref url="archiving.md" %}
[archiving.md](archiving.md)
{% endcontent-ref %}
