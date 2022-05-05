---
description: Multibox box life cycle RPC methods
---

# Multibox box manager API

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](../contact-us.md) us if you have question, feedback or suggestions.
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

{% tab title="Response" %}
`description` - box description \
`meta` - meta info about the box resource \
`fhirVersion` \
`box-url` \
`access-url` - link to get admin access \
`participant`- collection of user resources \
`resourceType` \
`env` - object with environment variables in lower-kebab-case (not in UPPER\_SNAKE\_CASE) \
`access-token` \
`id`
{% endtab %}

{% tab title="Errors" %}
`message` - "Can not create box"\
`message` - "Box already exists"\
`message` - `FHIR OperationOutcome`
{% endtab %}
{% endtabs %}

### `multibox/list-boxes`

List boxes available for the current user.

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Response" %}
Collection of objects with the following structure:

`id` - box id

`cluster`&#x20;

`fhirVersion` - fhir version
{% endtab %}

{% tab title="Error" %}
`message -` "No user session"
{% endtab %}
{% endtabs %}

### `multibox/get-box`

Get box information.

{% tabs %}
{% tab title="Parameters" %}
`id` (required): id of the box
{% endtab %}

{% tab title="Response" %}
`description` - box description \
`meta` - meta info about the box resource \
`fhirVersion` \
`box-url` \
`access-url` - link to get admin access \
`participant`- collection of user resources \
`resourceType` \
`env` - object with environment variables in lower-kebab-case (not in UPPER\_SNAKE\_CASE) \
`access-token` \
`id`
{% endtab %}

{% tab title="Error" %}
`message`- "You do not have access to this box"\
`message` - "No box with id - "\
`message` - "No user session"
{% endtab %}
{% endtabs %}

### `multibox/delete-box`

{% hint style="danger" %}
This operation will drop the box database. Can not be undone!
{% endhint %}

Delete a box

{% tabs %}
{% tab title="Parameters" %}
`id` (required): id of the box
{% endtab %}

{% tab title="Response" %}
`description` - box description \
`meta` - meta info about the box resource \
`fhirVersion` \
`box-url` \
`access-url` - link to get admin access \
`participant`- collection of user resources \
`resourceType` \
`env` - object with environment variables in lower-kebab-case (not in UPPER\_SNAKE\_CASE) \
`access-token` \
`id`
{% endtab %}

{% tab title="Error" %}
`message` - "Only owner can delete box."\
`message` - "Cannot delete box: \n \<details>"\
`message` - `FHIR OperationOutcome`
{% endtab %}
{% endtabs %}

### `multibox/drop-box-caches`

Drop cache in every box

{% tabs %}
{% tab title="Parameters" %}
_expects no parameters._
{% endtab %}

{% tab title="Response" %}
String: "ok".
{% endtab %}
{% endtabs %}
