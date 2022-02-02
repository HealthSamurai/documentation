---
description: >-
  This page introduces release cycle and versioning approach used in Aidbox
  development
---

# Versioning & Release Notes

### Aidbox release cycle

Aidbox Docker Images are available on [Docker Hub](https://hub.docker.com/u/healthsamurai).&#x20;

**:edge**\
****\
****The most recent Aidbox build. **** We highly recommend using the **edge** channel in staging and dev environments to get access to new functionality and detect potential issues as soon as possible. Every commit into Aidbox code base after successful CI is published into **edge** channel. The **edge** channel may have issues and/or regressions.\
\
:**latest**\
****\
**Latest** releases passed all available QA and review process, but needs real-world testing. These releases are updated monthly.\
****\
**:stable**

**Stable** releases are ready for production. Current **stable** is a previous **latest** release. For \*\*\*\* every release, we create an immutable tag \*\*\*\* in the \*\*\*\* format `YYMM` (for example for the 2021 July release it will be **2107**).

#### Long Term Support releases

Starting from January 2022 we introduce Long Term Support releases. Aidbox team will backport security and critical bug fixes to LTS releases throughout 1-year support window.\
\
Aidbox LTS releases are scheduled to be published annually in February and July. If you are not interested in getting new features on a monthly basis but want to get critical bug fixes please consider switching to LTS releases.

### Aidboxdb release cycle

[aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated) image is a custom build of an open-source PostgreSQL database. It contains a number of extensions that are needed to increase search performance.

aidboxdb image follows PostgreSQL versioning. For instance, if you want to use PostgreSQL 11.11, pull healthsamurai/aidboxdb:11.11

The HealthSamurai team officially supports the latest minor release of each major version of PostgreSQL starting from 11. The currently supported versions of PostgreSQL are 11.11, 12.6, and 13.2.
