---
description: This topic explains the configuration and internals of aidboxdb image
---

# AidboxDB

### Introduction

AidboxDB image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).&#x20;

```bash
docker run \
       -v "$(pwd)"/pgdata:/data/pg \
       -e POSTGRES_USER='postgres' \
       -e POSTGRES_PASSWORD='pass' \
       -e POSTGRES_DB='aidbox' \
       -e PGDATA="/data/pg" \
       healthsamurai/aidboxdb:15.2
```

The AidboxDB image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica
* Optimized FHIR search queries
* Backup and maintenance extensions

### Versioning

AidboxDB image is tagged by PostgreSQL version from which it is built. For example, if you want to use 14.5 PostgreSQL version you should pull [healthsamurai/aidboxdb:14.5](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.5/images/sha256-24accc760960f6abed0f9b2d2382712e5b98aa382403887e24408f0f0fdcf58d?context=repo) image.

Supported PostgreSQL versions: [15.3](https://hub.docker.com/layers/healthsamurai/aidboxdb/15.3/images/sha256-68f08757002725ee9ede9177e496fb76a0edcec127e59e122b2372894ee3dc1a?context=explore), [14.8](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.8/images/sha256-5eac0729f6f303294d97e386d19590096539669b0824bc7d8e29c346d0519e85?context=explore), [13.11](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.11/images/sha256-a81b8fedeef96d231704b6574414b9a04adf37f91064cf614188c39e15818e0e?context=explore)

Available versions: [15.2](https://hub.docker.com/layers/healthsamurai/aidboxdb/15.2/images/sha256-0cacd32c5a8137bc5f0b216b1cc525c5aae7e79b8adf347a5d462961fb25bd2b?context=repo), [14.7](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.7/images/sha256-7af93eca31ee9f059f4083210c272d412c5379c8b3f9fdc070f5b4907049ddd8?context=repo), [14.5](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.5/images/sha256-d0e8d9a51028e54fc72d4ea848c972a66d6d30cb04538fe05bee32a0b7c0b486?context=repo), [14.2](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.2/images/sha256-770eb8be3d51fa7c9829ab40c51588833ab91b613894cf80dc7b633f934a890e?context=repo), [13.10](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.10/images/sha256-b6f88a0a75f619222a62945eda150099a80ec725a01336a87e90e9445156a23c?context=repo), [13.6](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.6/images/sha256-49097e7fb0d60798dbdfe4a3ba31dc324abe232e399a78a487ab91dbd892e2c1?context=repo), [13.2](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.2/images/sha256-31294389f0339edeff3926ce0f27c856194f6e934ac744af5aa776b1f675dfe1?context=repo), [12.12](https://hub.docker.com/layers/healthsamurai/aidboxdb/12.12/images/sha256-8a898079a8dc3f9a46a652632450738cd1e88f340838fef8f2bc7101d1ab3e00?context=repo), [12.6](https://hub.docker.com/layers/healthsamurai/aidboxdb/12.6/images/sha256-2a4fc68fc80c0f6e48ddd06b4dcd8a1cab72f2ab13968cc37b06fd2a53e85070?context=repo)

### Extensions

List of additional availiable extensions:

* [jsonknife](https://github.com/niquola/jsonknife) — Jsonb extraction tool for optimizing FHIR search
* [pgagent](https://github.com/pgadmin-org/pgagent) — A PostgreSQL job scheduler &#x20;
* [pg\_repack](https://github.com/reorg/pg\_repack) — Reorganize tables in PostgreSQL databases with minimal locks
* [jsquery](https://github.com/postgrespro/jsquery) — JSON query language with GIN indexing support
* [wal2json](https://github.com/eulerto/wal2json) — Is an output plugin for logical decoding
* [wal-g](https://github.com/wal-g/wal-g) — Archival and Restoration for databases in the Cloud
* [PostGIS](https://github.com/postgis/postgis) — Support storing, indexing and querying geographic data

### Configuration

The image is configured by supplying environment variables and command line arguments on startup. Keep in mind that you should not change the environment variables once you have initialized the database. The image expects them to be immutable.

{% content-ref url="../reference/configuration/environment-variables/aidboxdb-environment-variables.md" %}
[aidboxdb-environment-variables.md](../reference/configuration/environment-variables/aidboxdb-environment-variables.md)
{% endcontent-ref %}

### Archiving

An open-source tool [wal-g](https://github.com/wal-g/wal-g) is used by AidboxDB for continuous archival, backups, and restoration.

{% content-ref url="backup-and-restore/archiving.md" %}
[archiving.md](backup-and-restore/archiving.md)
{% endcontent-ref %}
