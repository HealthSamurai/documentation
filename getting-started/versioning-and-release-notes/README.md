---
description: >-
  This page introduces release cycle and versioning approach used in Aidbox
  development
---

# Versioning & Release Notes

### Aidbox release cycle

For each license type of Aidbox an individual backend image is available on the [HealthSamurai docker hub](https://hub.docker.com/u/healthsamurai). The images are labeled with **stable** and **edge** tags, also called channels. Aidbox release cycle is the following: 

* during the development sprint, new features become available on the **edge** channel
* when the release is made, the current **edge** image becomes a new **stable** image 

{% hint style="warning" %}
Note that the edge channel may have issues in newly added features and regressions and is not suitable for the production environment.
{% endhint %}

For every release, we also publish a **timestamped image.**  The timestamp has the format of "YYMM", so for the release that is made on the 25th of January, 2021 is "2101". The **timestamped image** can only be updated before the next release. After the next release, the previous release **timestamped image** becomes immutable.

We actively encourage developers to use images with either **stable** or **edge** tags in their applications. The **timestamped image** can be used to set a specific Aidbox version if the application development is frozen and the development team does not want to receive updates any longer. Keep in mind that the HealthSamurai team maintains backward compatibility in its releases, however, we can not guarantee a trivial update from an old **timestamped image**.

You can see the next planned release date and the release status in the [Aidbox/Issues ](https://github.com/Aidbox/Issues/projects)Github repository.

### Aidboxdb release cycle

[aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated) image is a custom build of an open-source PostgreSQL database. It contains a number of extensions that are needed to increase search performance.

aidboxdb image follows PostgreSQL versioning. For an instance, if you want to use PostgreSQL 11.11, pull healthsamurai/aidboxdb:11.11

HealthSamurai team officially supports the latest minor releases of every major version of PosgreSQL starting from 11. Currently supported versions of PostgreSQL are 11.11, 12.6, and 13.2.

