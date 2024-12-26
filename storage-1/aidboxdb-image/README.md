---
description: This topic explains the configuration and internals of aidboxdb image
---

# AidboxDB

### Introduction

AidboxDB image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last_updated).

```bash
docker run \
       -v "$(pwd)"/pgdata:/data/pg \
       -e POSTGRES_USER='postgres' \
       -e POSTGRES_PASSWORD='pass' \
       -e POSTGRES_DB='aidbox' \
       -e PGDATA="/data/pg" \
       healthsamurai/aidboxdb:16.1
```

The AidboxDB image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica
* Optimized FHIR search queries
* Backup and maintenance extensions

### Versioning

AidboxDB image is tagged by PostgreSQL version from which it is built. For example, if you want to use 14.5 PostgreSQL version you should pull [healthsamurai/aidboxdb:14.5](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.5/images/sha256-24accc760960f6abed0f9b2d2382712e5b98aa382403887e24408f0f0fdcf58d?context=repo) image.

Supported PostgreSQL versions: 16.6, 15.10, 14.12.

See available versions on [Docker Hub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags).

### Extensions

List of additional availiable extensions:

* [pgagent](https://github.com/pgadmin-org/pgagent) — A PostgreSQL job scheduler
* [pg\_repack](https://github.com/reorg/pg_repack) — Reorganize tables in PostgreSQL databases with minimal locks
* [wal2json](https://github.com/eulerto/wal2json) — Is an output plugin for logical decoding
* [wal-g](https://github.com/wal-g/wal-g) — Archival and Restoration for databases in the Cloud
* [PostGIS](https://github.com/postgis/postgis) — Support storing, indexing and querying geographic data

{% hint style="warning" %}
Extensions **unavailable** since AidboxDB version 16:

* [jsonknife](https://github.com/niquola/jsonknife) — Jsonb extraction tool for optimizing FHIR search
* [jsquery](https://github.com/postgrespro/jsquery) — JSON query language with GIN indexing support
{% endhint %}

### Configuration

The image is configured by supplying environment variables and command line arguments on startup. Keep in mind that you should not change the environment variables once you have initialized the database. The image expects them to be immutable.

{% content-ref url="../../reference/configuration/environment-variables/aidboxdb-environment-variables.md" %}
[aidboxdb-environment-variables.md](../../reference/configuration/environment-variables/aidboxdb-environment-variables.md)
{% endcontent-ref %}

### Archiving

An open-source tool [wal-g](https://github.com/wal-g/wal-g) is used by AidboxDB for continuous archival, backups, and restoration.

{% content-ref url="../backup-and-restore/archiving.md" %}
[archiving.md](../backup-and-restore/archiving.md)
{% endcontent-ref %}
