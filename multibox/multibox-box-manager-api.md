---
description: Multibox box life cycle RPC methods
---

# Multibox box manager API

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../contact-us.md) us if you have a question, feedback or suggestions.
{% endhint %}

Multibox box manager API uses Aidbox JSON [RPC API](../api-1/rpc-api.md).

{% hint style="info" %}
Multibox API is accessible on the box manager URL. Please use an external REST client to access your Multibox server.
{% endhint %}

## RPC methods available

### `multibox/fhir-versions`

List FHIR versions supported by the server. One of these values must be used in `multibox/create-box`.

### `multibox/create-box`

Create a new box for the current user.

{% tabs %}
{% tab title="Parameters" %}
* `id` _(required)_: id of the box to create. Must match `/[a-z][a-z0-9]{4,}/`
* `fhirVersion` _(required)_: FHIR version. Value must be from the `multibox/versions` response.
* `description`: description of the box to create.
* `env`: object with environment variables in `lower-kebab-case` (not in `UPPER_SNAKE_CASE`).
{% endtab %}
{% endtabs %}

### `multibox/list-boxes`

List boxes available for the current user.

### `multibox/get-box`

Get box information.

{% tabs %}
{% tab title="Parameters" %}
`id` (required): id of the box
{% endtab %}
{% endtabs %}

### `multibox/delete-box`

Delete a box. <mark style="color:red;">**This operation will drop the box database. Can not be undone!**</mark>

{% tabs %}
{% tab title="Parameters" %}
`id` (required): id of the box
{% endtab %}
{% endtabs %}
