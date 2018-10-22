---
description: In this tutorial we will show you how to use REST Api with Aidbox.Dev
---

# How to use REST Api in Aidbox.dev

## Basic Authorization

After installation `Aidbox.Dev` create one default [`Client`](../oauth-2.0/users-and-clients.md#client) resource. This resource used in OAuth 2.0 functionality. Also this resource may be used for `basic authorization`. In this tutorial we will use this [`Client`](../oauth-2.0/users-and-clients.md#client) for basic authorization in to Aidbox.Dev .

Default `Client` will create  with `password` described in `DEVBOX_PASSWORD` environment variable sent to the container in the `docker-compose.yaml` . If you do not explicitly specify `DEVBOX_PASSWORD` by default if was set as `secret`. We are strongly recommending to change default password in your installation. [`Client.id`](../oauth-2.0/users-and-clients.md#client) of this client is constantly `root`.

{% code-tabs %}
{% code-tabs-item title="docker-compose.yaml" %}
```yaml
version: '3.1'
services:
  devbox:
    environment:
      DEVBOX_PASSOWRD: "${DEVBOX_PASSOWRD:-secret}"
...
```
{% endcode-tabs-item %}
{% endcode-tabs %}



BUILD  base64 authorization header

## What next?

Learn how to start up with simple Single Page Application with Patient list and Patient CRUD in our tutorial.

{% page-ref page="run-local-demo.md" %}









