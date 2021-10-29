# Installation & Configuration

Basic Aidbox installation consists of two components: the backend and the database. Both are released as docker images and can be pulled from HealthSamurai [docker hub](https://hub.docker.com/u/healthsamurai). For each type of Aidbox license an individual backend image is available - either [Devbox](https://hub.docker.com/r/healthsamurai/devbox), [Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) or [Multibox](https://hub.docker.com/r/healthsamurai/multibox).&#x20;

[The database image](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated) is the custom build of the open source PostgreSQL database. It contains a number of extensions that are primarily used for search performance optimization. aidboxdb officially supports the latest minor releases of all major PosgreSQL versions starting from 11. Note that the database image is the same for all Aidbox backend license types.

Refer to the following pages for the specific image description and the list of available configuration options.

{% content-ref url="aidboxdb-image.md" %}
[aidboxdb-image.md](aidboxdb-image.md)
{% endcontent-ref %}

Check out the tutorials if you are looking for a step-by-step guide on how to setup and run an image of your choice.

{% content-ref url="setup-aidbox.dev.md" %}
[setup-aidbox.dev.md](setup-aidbox.dev.md)
{% endcontent-ref %}

If you are looking for the latest versions of the docker images or general release cycle explanation go to the Versioning & Release Notes page.

{% content-ref url="../versioning-and-release-notes/" %}
[versioning-and-release-notes](../versioning-and-release-notes/)
{% endcontent-ref %}
