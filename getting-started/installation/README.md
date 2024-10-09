# Configure Aidbox and Multibox

[Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [Multibox](https://hub.docker.com/r/healthsamurai/multibox) distribution.

All distributions can be used both on [healthsamurai/aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb) PostgreSQL distribution or managed PostgreSQLs.\
\
Basic Aidbox installation consists of two components: the backend and the database. Both are released as docker images and can be pulled from HealthSamurai [docker hub](https://hub.docker.com/u/healthsamurai). For each type of Aidbox license an individual backend image is available — either [Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) or [Multibox](https://hub.docker.com/r/healthsamurai/multibox).

[The database image](https://hub.docker.com/r/healthsamurai/aidboxdb) is the custom build of the open source PostgreSQL database. It contains a number of extensions that are primarily used for search performance optimization. `aidboxdb` officially supports the latest minor releases of all major PostgreSQL versions starting from 11. Note that the database image is the same for all Aidbox backend license types.

Refer to the following pages for the specific image description and the list of available configuration options.

{% content-ref url="../../storage-1/aidboxdb-image/" %}
[aidboxdb-image](../../storage-1/aidboxdb-image/)
{% endcontent-ref %}

{% content-ref url="../../reference/configuration/environment-variables/" %}
[environment-variables](../../reference/configuration/environment-variables/)
{% endcontent-ref %}

{% content-ref url="../../aidbox-configuration/zen-configuration.md" %}
[zen-configuration.md](../../aidbox-configuration/zen-configuration.md)
{% endcontent-ref %}

Check out the tutorials if you are looking for a step-by-step guide on how to setup and run an image of your choice.

{% content-ref url="../run-aidbox-in-aidbox-sandbox.md" %}
[run-aidbox-in-aidbox-sandbox.md](../run-aidbox-in-aidbox-sandbox.md)
{% endcontent-ref %}

{% content-ref url="../run-aidbox-locally-with-docker/" %}
[run-aidbox-locally-with-docker](../run-aidbox-locally-with-docker/)
{% endcontent-ref %}

If you are looking for the latest versions of the docker images or general release cycle explanation go to the Versioning page.

{% content-ref url="../../overview/versioning.md" %}
[versioning.md](../../overview/versioning.md)
{% endcontent-ref %}
