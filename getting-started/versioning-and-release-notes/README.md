---
description: >-
  This page introduces the release cycle and versioning approach used in Aidbox
  development
---

# Versioning

### Aidbox release cycle

Aidbox docker images are available on [Docker Hub](https://hub.docker.com/u/healthsamurai).&#x20;

**:edge**\
****\
****The most recent Aidbox build. **** We highly recommend using the **edge** channel in staging and dev environments to get access to new functionality and detect potential issues as soon as possible. Every commit into the Aidbox code base after successful CI is published to the **edge** channel. The **edge** channel may have issues and/or regressions.

:**latest**\
****\
**Latest** releases contain new functionality, bugfixes, and optimizations and passed all available QA and review processes by the Health Samurai team. If you're actively developing we recommend using **latest**. These releases are updated monthly.

**:stable**

**Stable** releases are ready for production. The current **stable** is the previous **latest** release. These releases are updated monthly.&#x20;

**Tagged releases**\
\
For every `:stable` release, we also create a tag in the format YYMM (for example it will be **2107** for the 2021 July release) if you want to check out to a specific Aidbox version.

#### Long-term support releases

Starting from January 2022 we are introducing long-term support releases. The Aidbox team will backport security and critical bug fixes to LTS releases throughout a one-year support window.

Aidbox LTS releases are scheduled to be published twice a year. If you are not interested in getting new features on a monthly basis but want to get critical bug fixes, please consider switching to LTS releases.

| Aidbox version                                                                                                                                                                      | Availability starts | End of life |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------- | ----------- |
| [2106-lts](https://hub.docker.com/layers/healthsamurai/aidboxone/2106-lts/images/sha256-4e4566ee3f472cbc4c26ddf253c1bb3c0894cc6a0e307ef7a6b9a15fafba510c?context=explore)           | 2021-06             | 2022-06     |
| [2202-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-997c1e28b8ea2274979caf3fe2e6e620fa5eacd18f49195c293b7c74f85ec23d?context=explore) | 2022-04             | 2023-04     |

### Aidboxdb release cycle

The [aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated) image is a custom build of an open-source PostgreSQL database. It contains a number of extensions that are needed to increase search performance.

The aidboxdb image follows PostgreSQL versioning. For instance, if you want to use PostgreSQL 11.11, pull healthsamurai/aidboxdb:11.11

The HealthSamurai team officially supports the latest minor release of each major version of PostgreSQL starting from 11. The currently supported versions of PostgreSQL are 11.11, 12.6, and 13.2.
