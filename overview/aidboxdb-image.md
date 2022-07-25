---
description: This topic explains the configuration and internals of aidboxdb image
---

# aidboxdb

### Introduction

aidboxdb image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).&#x20;

The aidboxdb image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica of the master database

An open source tool [wal-g](https://github.com/wal-g/wal-g) is used by aidboxdb for continuous archival, backups, and restoration.

aidboxdb image is tagged by PostgreSQL version from which it is built. For example, if you want to use 11.11 PostgreSQL version you should pull healthsamurai/aidboxdb:11.11 image.

Actual supported PostgreSQL versions: [14.2](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/14.2/images/sha256-5bdc4e259785be6c9741bd6faab8d37a8737154062fab8a84a7d68c7d81a5f6f), [13.6](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/13.6/images/sha256-49097e7fb0d60798dbdfe4a3ba31dc324abe232e399a78a487ab91dbd892e2c1?context=explore)

Available versions: [13.2](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/13.2/images/sha256-31294389f0339edeff3926ce0f27c856194f6e934ac744af5aa776b1f675dfe1?context=explore), [12.6](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/12.6/images/sha256-2a4fc68fc80c0f6e48ddd06b4dcd8a1cab72f2ab13968cc37b06fd2a53e85070?context=explore), [11.11](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/11.11/images/sha256-9e767a6f1a0d21faf8542edcdc9f11ba8e836889f6a05d38e29003297037d136?context=explore)

List of additional installed extensions:

* pgagent - A PostgreSQL job scheduler &#x20;
* pg\_repack - Reorganize tables in PostgreSQL databases with minimal locks&#x20;
* jsonknife - Jsonb extraction tool
* jsquery - Data type for jsonb inspection

The image is configured by supplying environment variables and command line arguments on startup. Keep in mind that you should not change the environment variables once you have initialized the database. The image expects them to be immutable.

Check aidboxdb environment variables

{% content-ref url="../reference/configuration/environment-variables/aidboxdb-environment-variables.md" %}
[aidboxdb-environment-variables.md](../reference/configuration/environment-variables/aidboxdb-environment-variables.md)
{% endcontent-ref %}

