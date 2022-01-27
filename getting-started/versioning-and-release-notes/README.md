---
description: >-
  This page introduces release cycle and versioning approach used in Aidbox
  development
---

# Versioning & Release Notes

### Aidbox release cycle

Aidbox Docker Images are available on [Docker Hub](https://hub.docker.com/u/healthsamurai). The latest stable images are tagged with **stable.** For **** every release, we create an immutable tag **** in the **** format `YYMM` (for example for the 2021 July release it will be **2107**). If you are interested in the next release's features/bug fixes or want to test your project with an upcoming version of Aidbox - you can use tag **edge**. Every commit into Aidbox code base after successful CI is published into **edge** channel.

{% hint style="warning" %}
Note that the edge channel may have issues and/or regressions and is risky to use in production. However, it is highly recommended to use the edge channel in staging and dev environments in order to detect potential issues as soon as possible.
{% endhint %}

We actively encourage developers to use channels either **stable** or **edge** tags in their setup. The **release images** can be used to set a specific Aidbox version if the application development is frozen and the development team does not want to receive updates any longer. Keep in mind that the HealthSamurai team maintains backward compatibility in its releases, however, we can not guarantee a trivial update from old **versions**.

### Aidboxdb release cycle

[aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated) image is a custom build of an open-source PostgreSQL database. It contains a number of extensions that are needed to increase search performance.

aidboxdb image follows PostgreSQL versioning. For instance, if you want to use PostgreSQL 11.11, pull healthsamurai/aidboxdb:11.11

The HealthSamurai team officially supports the latest minor release of each major version of PostgreSQL starting from 11. The currently supported versions of PostgreSQL are 11.11, 12.6, and 13.2.
