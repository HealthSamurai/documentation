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

* `format`
* `meta`
* `on-conflict`
* `input`
  * `url`
{% endtab %}

{% tab title="Not implemented parameters" %}
* `error-threshold`
* `id_prefix`
* `mode`
* `skip-validation`
{% endtab %}

{% tab title="Result" %}
Returns input params object with following attributes added:

* `status`
* `count`
* `time`
* `input`
  * `status`
  * `count`
  * `errors`
  * `time`
{% endtab %}

{% tab title="Error" %}

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

