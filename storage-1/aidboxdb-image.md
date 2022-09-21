---
description: This topic explains the configuration and internals of aidboxdb image
---

# Overview

### Introduction

aidboxdb image is a custom build of open source PostgreSQL database. Aidbox uses it as data storage. The image can be pulled from [HealthSamurai dockerhub](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).&#x20;

The aidboxdb image use cases are:

* To initialize and run a master database for Aidbox to work with
* To initialize and run a streaming replica of the master database

An open source tool [wal-g](https://github.com/wal-g/wal-g) is used by aidboxdb for continuous archival, backups, and restoration.

aidboxdb image is tagged by PostgreSQL version from which it is built. For example, if you want to use 11.11 PostgreSQL version you should pull healthsamurai/aidboxdb:11.11 image.

Actual supported PostgreSQL versions: [14.5](https://hub.docker.com/layers/healthsamurai/aidboxdb/14.5/images/sha256-8330a20d8ac5220ee40466781d51872ee3f50a7925f47d8da6f19c2fc6d44172?context=explore), [13.8](https://hub.docker.com/layers/healthsamurai/aidboxdb/13.8/images/sha256-f48c57079af5e940be9c18dab81cf780687cb24b0a778c6a2c9d3f808c705919?context=repo)

Available versions: [12.12](https://hub.docker.com/layers/healthsamurai/aidboxdb/12.12/images/sha256-8a898079a8dc3f9a46a652632450738cd1e88f340838fef8f2bc7101d1ab3e00?context=repo), [11.11](https://hub.docker.com/layers/aidboxdb/healthsamurai/aidboxdb/11.11/images/sha256-9e767a6f1a0d21faf8542edcdc9f11ba8e836889f6a05d38e29003297037d136?context=explore)

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

