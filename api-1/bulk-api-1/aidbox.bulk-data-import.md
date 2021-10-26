---
description: aidbox.bulk/* RPC operations
---

# aidbox.bulk data import

{% hint style="warning" %}
This page is a work in progress
{% endhint %}

## Overview

aidbox.bulk API uses Aidbox JSON [RPC API](../rpc-api.md)

Features of the aidbox.bulk API:

* Asynchronous validation of uploaded resources & references
* Human readable validation errors
* Can process Aidbox & FHIR format

## RPC methods

### `aidbox.bulk/import-start`

Starts `aidbox.bulk` import

{% tabs %}
{% tab title="Parameters" %}
Object with following structure:

* `format `input resources format, <mark style="color:orange;">`fhir`</mark> <mark style="color:red;"></mark>or <mark style="color:orange;">`aidbox`</mark> (default: <mark style="color:orange;">`fhir`</mark>)&#x20;
* `meta `meta data that will be attached to each of the imported resources
* `on-conflict` action to resolve id uniqueness constraint violation, <mark style="color:orange;">`update`</mark> or <mark style="color:orange;">`override`</mark>
* `input` array of input sources urls
  * `url`
{% endtab %}

{% tab title="Result" %}
Returns input params object with following attributes added:

* `status` <mark style="color:orange;">`in-progress`</mark>, <mark style="color:orange;">`finished`</mark>, <mark style="color:orange;">`failed`</mark>
* `count` overall number of imported resources
* `time` import execution time
* `input` the import status of each specific input
  * `status` <mark style="color:orange;">`loaded`</mark>, <mark style="color:orange;">`validated`</mark>, <mark style="color:orange;">`failed`</mark>
  * `count` number of loaded resources
  * `errors` number of validation errors
  * `time` input execution time
{% endtab %}

{% tab title="Error" %}
* `message` _There is running import - wait until it finish or cancel it with aidbox.bulk/import-cance_l
* `import` object identical to result object
{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox.bulk/import-start
params:
  on-conflict: update
  id_prefix: app1
  format: fhir
  meta: {source: app1}
  input:
  - {url: 'https://storage.googleapis.com/aidbox-public/synthea/100/corrupted-patient.ndjson.gz'}
```
{% endtab %}

{% tab title="Response" %}
```
// Some code
```
{% endtab %}
{% endtabs %}

### `aidbox.bulk/import-status`

Returns latest `aidbox.bulk` import info

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
Same as `aidbox.bulk/import-start` result
{% endtab %}

{% tab title="Error" %}

{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```
// Some code
```
{% endtab %}

{% tab title="Response" %}
```
// Some code
```
{% endtab %}
{% endtabs %}

### `aidbox.bulk/import-errors`

Returns latest `aidbox.bulk` import detailed errors list

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
Result is an array of objects with following structure:

* `input_no`
* `line_no`
* `type`
* `id`
* `resource`
* `errors`
{% endtab %}

{% tab title="Error" %}

{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```
// Some code
```
{% endtab %}

{% tab title="Response" %}
```
// Some code
```
{% endtab %}
{% endtabs %}

### `aidbox.bulk/import-cancel`

Cancels latest `aidbox.bulk` import

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Result" %}
Returns object with following structure:

* `message`
* `import`
{% endtab %}

{% tab title="Error" %}

{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```
// Some code
```
{% endtab %}

{% tab title="Response" %}
```
// Some code
```
{% endtab %}
{% endtabs %}

