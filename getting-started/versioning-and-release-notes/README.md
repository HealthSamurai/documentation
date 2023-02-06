---
description: >-
  This page introduces the release cycle and versioning approach used in Aidbox
  development
---

# Versioning

## Aidbox release cycle

Aidbox docker images are available on [Docker Hub](https://hub.docker.com/u/healthsamurai).&#x20;

### **:edge**

The most recent Aidbox build. **** We highly recommend using the **edge** channel in staging and dev environments to get access to new functionality and detect potential issues as soon as possible. Every commit into the Aidbox code base after successful CI is published to the **edge** channel. The **edge** channel may have issues and/or regressions.

### :**latest**

**Latest** releases contain new functionality, bugfixes, and optimizations and passed all available QA and review processes by the Health Samurai team. If you're actively developing we recommend using **latest**. These releases are updated monthly.

### **:stable**

**Stable** releases are ready for production. The current **stable** is the previous **latest** release. These releases are updated monthly.&#x20;

### **Tagged releases**

For every `:stable` release, we also create a tag in the format YYMM (for example it will be **2107** for the 2021 July release) if you want to check out to a specific Aidbox version.

### Long-term support releases

Starting in January 2022 we are introducing long-term support releases. The Aidbox team will backport security and critical bug fixes to LTS releases throughout a one-year support window.

Aidbox LTS releases are scheduled to be published twice a year. If you are not interested in getting new features on a monthly basis but want to get critical bug fixes, please consider switching to LTS releases.

| Aidbox version                                                                                                                                                                      | Availability starts | End of life |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------- | ----------- |
| [2202-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore) | 2022-04             | 2023-04     |
| [2206-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore) | 2022-08             | 2023-08     |

#### LTS changelog

| Aidbox version                                                                                                                                                                                                                                                                                                                                                           | Date       | Reason                                                                                |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ---------- | ------------------------------------------------------------------------------------- |
| [2202-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore), [2206-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore) | 2022-11-03 | Fixed security issues in Search API.                                                  |
| [2202-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore), [2206-lts](https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore) | 2022-10-10 | Fixed critical issue on subscriptions in DB transaction that can break DB connection. |
| 2202-lts,2206-lts                                                                                                                                                                                                                                                                                                                                                        | 2023-02-03 | Fix vulnerabilities                                                                   |



