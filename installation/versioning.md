# Versioning

Aidbox release cycle is about 1-2 weeks.

Aidbox is distributed by two channels:

* **stable** - default cloud version, `devbox:latest` and `aidboxone:latest` at docker registry
* **edge** - current development version

Latest stable version of Aidbox is always deployed into Aidbox.Cloud stable clusters and represented as `max(version)` or `latest` in docker registry - [https://hub.docker.com/u/healthsamurai](https://hub.docker.com/u/healthsamurai).

We only have one development version - it's name is edge - ie `devbox:edge` and `aidboxone:edge`.

In pre-release stabilisation we publish  RC versions with `VERSION-RC[i]` like `0.4.0-RC1`, so you can test upcoming releases. After release all RC versions are cleared.

