---
description: >-
  The topic explains configuration options of Devbox, Aidbox and Multibox
  distributions
---

# Configure Devbox/Aidbox/Multibox

## Introduction

[Devbox](https://hub.docker.com/r/healthsamurai/devbox) is a lightweight version of Aidbox. It is a free version aimed at local development and can not be used to store real healthcare data or PHI.

[Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [Multibox ](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox)distribution.

All distributions can be used both on [healthsamurai/aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb) PostgreSQL distribution or managed PostgreSQLs.

All distributions require license details to be provided. Alongside with online license server, we offer a special license server distribution for the deployment environments that do not have access to the internet. Contact your support team for the details.

For more information take a look at the environment variables page.

{% content-ref url="../reference/configuration/environment-variables/" %}
[environment-variables](../reference/configuration/environment-variables/)
{% endcontent-ref %}
