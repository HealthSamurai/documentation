# Installation & Configuration

Basic Aidbox installation consists of two components - the backend and the database. Both are released as docker images and can be pulled from HealthSamurai [docker hub](https://hub.docker.com/u/healthsamurai). For each type of Aidbox license an individual backend image is available - either [devbox](https://hub.docker.com/r/healthsamurai/devbox), [aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) or [multibox](https://hub.docker.com/r/healthsamurai/multibox). 

[The database image](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated) is the custom build of the open source PostgreSQL database. It contains a number of extensions that are primarily used for search performance optimization. aidboxdb officially supports the latest minor releases of all major PosgreSQL versions starting from 11. Note that the database image is the same for all Aidbox backend license types.

Refer to the following pages for the specific image description and the list of available configuration options.

{% page-ref page="aidboxdb-image.md" %}

Check out the tutorials if you are looking for a step-by-step guide on how to setup and run an image of your choice.

{% page-ref page="setup-aidbox.dev.md" %}

If you are looking for the latest versions of the docker images or general release cycle explanation go to the Versioning & Release Notes page.

{% page-ref page="../versioning-and-release-notes/" %}

