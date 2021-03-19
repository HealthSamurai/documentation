---
description: >-
  This page introduces release cycle and versioning approach used in Aidbox
  development
---

# Versioning & Release Notes

### aidbox release cycle

For each license type of Aidbox an individual image of the backend is available in the [HealthSamurai docker hub](https://hub.docker.com/u/healthsamurai). All these images are labeled with **stable** and **edge** tags, so called channels. HealthSamurai release cycle is the following: 

* during the development sprint new features become available on the **edge** channel
* when the release is made the current **edge** image becomes new **stable** image 

{% hint style="warning" %}
Note that edge channel may have issues in newly added features and regressions and is not suitable for production environment.
{% endhint %}

For every release we also publish a **timestamped image.** Timestamp has the format of "DDMMYYYY", so for the release that is made on 25th of January, 2021 it is "25012021". **Timestamped image** can only be updated before the next release to apply a hotfix. After the next release the previous release **timestamped image** becomes immutable.

We actively encourage developers to use images with either **stable** or **edge** tags in their applications. **Timestamped image** can be used to set a specific Aidbox version if application development is frozen and development team does not want to receive updates any longer. Keep in mind that the HealthSamurai team maintains backwards compatibility in its releases, however, we can not guarantee a trivial update from an old **timestamped image**.

You can see the next planned release date and release status in [Aidbox/Issues ](https://github.com/Aidbox/Issues/projects)github repository.

### aidboxdb release cycle

[aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated) image is a custom build of open source PostgreSQL database. It contains a number of extensions that are needed to increase search performance.

aidboxdb image follows PostgreSQL versioning. For instance if you want to use PostgreSQL 11.11 pull healthsamurai/aidboxdb:11.11

HealthSamurai team officially supports latest minor releases of every major version of PosgreSQL starting from 11. Currently supported versions of PostgreSQL are 11.11, 12.6 and 13.2.

