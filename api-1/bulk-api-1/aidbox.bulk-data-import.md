---
description: aidbox.bulk/* RPC operations
---

# aidbox.bulk data import

## Overview

aidbox.bulk API uses Aidbox JSON [RPC API](../rpc-api.md)

Features of the aidbox.bulk API:

* Asynchronous validation of uploaded resources & references
* Human readable validation errors
* Can process Aidbox & FHIR format

## RPC methods

### `aidbox.bulk/import-start`

Starts `aidbox.bulk` import.

{% tabs %}
{% tab title="Parameters" %}
Object with the following structure:

* `format` input resources format, <mark style="color:orange;">`fhir`</mark> or <mark style="color:orange;">`aidbox`</mark> (default: <mark style="color:orange;">`fhir`</mark>)
* `meta` meta data that will be attached to each of the imported resources
* `on-conflict` action to resolve id uniqueness constraint violation, <mark style="color:orange;">`update`</mark> or <mark style="color:orange;">`override`</mark>
* `input` array of objects
  * `url` string with input source url
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
* `message` _"There is running import - wait until it finish or cancel it with aidbox.bulk/import-cancel"_
* `import` object identical to result object
{% endtab %}
{% endtabs %}

#### Resource requirements for `aidbox.bulk/import-start`:

| Operation                  | id       | resourceType |
| -------------------------- | -------- | ------------ |
| `aidbox.bulk/import-start` | Required | Required     |

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
{% code title="status: 200" %}
```yaml
result:
  on-conflict: update
  id_prefix: app1
  format: fhir
  meta: {source: app1}
  input:
  - {url: 'https://storage.googleapis.com/aidbox-public/synthea/100/corrupted-patient.ndjson.gz', status: loaded, count: 124, errors: 1, time: 157}
  status: failed
  errors: 1
  time: 241
```
{% endcode %}
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
`message` _"No active imports"_
{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox.bulk/import-status
params: {}
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```yaml
result:
  on-conflict: update
  id_prefix: app1
  format: fhir
  meta:
    source: app1
  input:
    - url: >-
        https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
      status: loaded
      count: 124
      errors: 0
      time: 1205
  status: finished
  count: 0
  time: 1452
```
{% endcode %}
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
`message` _"No active imports"_
{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox.bulk/import-errors
params:
  omit-resources?: true
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```yaml
result:
  - input_no: 0
    line_no: 1
    type: Patient
    id: e9adac47-eb98-4fce-b871-512226086c97
    errors:
      - path:
          - name
          - 0
          - given
        message: expected array
  - input_no: 0
    line_no: 20
    type: Patient
    id: b2d58f0f-4499-4392-ad3a-1c2141c8a6c1
    errors:
      - path:
          - address
          - 0
          - line
        message: expected array
```
{% endcode %}
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

* `message` _"Import canceled"_
* `import` Same as `aidbox.bulk/import-start` result
{% endtab %}

{% tab title="Error" %}
`message` _"No active imports"_
{% endtab %}
{% endtabs %}

#### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: aidbox.bulk/import-cancel
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```
result:
  message: "Import Canceled"
  import: <. . .>
```
{% endcode %}
{% endtab %}
{% endtabs %}

``
