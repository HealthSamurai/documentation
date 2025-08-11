# Task Executor API

{% hint style="warning" %}
Workflow engine is configured by zen. We do not support it and do not recommend to use it anymore. Please, use any other workflow engine e.g. [Temporal](https://temporal.io/).

Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

Task Executor API is designed to allow implement task executor in any programming language and use it in Aidbox via REST requests with [RPC calls](../../../../../api/other/rpc-api.md).

### `awf.task/poll`

Fetches a task in the status `ready` from the queue and changes its status to `requested`.

Immediately returns an empty array if there are no tasks in the queue.

Either **taskDefinitions** or **workflowDefinitions** parameter SHOULD be specified.

#### Params:

<table><thead><tr><th width="216">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>taskDefinitions</td><td>string[]</td><td>false</td><td>An array of task definitions to include.<br></td></tr><tr><td>workflowDefinitions</td><td>string[]</td><td>false</td><td>An array of workflow definitions to include. In response, decision tasks will be returned for specified workflow if available.</td></tr><tr><td>maxBatchSize</td><td>integer</td><td>false</td><td>The number of tasks that can be polled from the queue simultaneously.<br><em>Default value: 1</em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>AidboxTask resources with <code>execId</code>.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/poll
params:
    taskDefinitions: [aidbox.bulk/import-resource-task]
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resources:
    - execId: ea5c0022-c1e7-4f3c-b1ed-7e9b2428b7d2
      params:
        type: aidbox
        input:
          url: >-
            https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
          resourceType: Organization
      status: requested
      executeAt: '2023-05-04T15:30:43'
      definition: aidbox.bulk/import-resource-task
      id: >-
        819c2499-1303-4c0e-bf1e-bcca761bfbde
      resourceType: AidboxTask
      meta:
        lastUpdated: '2023-05-04T16:33:14.682354Z'
        createdAt: '2023-05-04T16:33:12.056223Z'
        versionId: '17633'
```
{% endtab %}
{% endtabs %}

### `awf.task/long-poll`

Fetches a task in the status `ready` from the queue and changes its status to `requested`.

Waits for a timeout unless a new task is received. In case of timeout, returns an empty array.

Either **taskDefinitions** or **workflowDefinitions** parameter SHOULD be specified.

#### Params:

<table><thead><tr><th width="198">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>taskDefinitions</td><td>string[]</td><td>false</td><td>An array of task definitions to include.<br></td></tr><tr><td>workflowDefinitions</td><td>string[]</td><td>false</td><td>An array of workflow definitions to include. In response, decision tasks will be returned for specified workflow if available.</td></tr><tr><td>maxBatchSize</td><td>integer</td><td>false</td><td>The number of tasks that can be polled from the queue simultaneously.<br><em>Default value: 1</em></td></tr><tr><td>timeout</td><td>integer</td><td>false</td><td>A period of time in ms, the period of time during which the tasks can be polled.<br><em>Default value: 60000 (equal to 1 minute)</em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>AidboxTask resources with <code>execId</code>.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/long-poll
params:
    timeout: 50000
    taskDefinitions: [aidbox.bulk/import-resource-task]
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resources:
    - execId: ea5c0022-c1e7-4f3c-b1ed-7e9b2428b7d2
      params:
        type: aidbox
        input:
          url: >-
            https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
          resourceType: Organization
      status: requested
      executeAt: '2023-05-04T15:30:43'
      definition: aidbox.bulk/import-resource-task
      id: >-
        819c2499-1303-4c0e-bf1e-bcca761bfbde
      resourceType: AidboxTask
      meta:
        lastUpdated: '2023-05-04T16:33:14.682354Z'
        createdAt: '2023-05-04T16:33:12.056223Z'
        versionId: '17633'
```
{% endtab %}
{% endtabs %}

### `awf.task/start`

Changes the status of a task from `requested` to `in-progress` and start its execution.

The required param of `execId` is supposed to be received from the request`awf.task/poll` or `awf.task/long-poll`.

#### Params:

<table><thead><tr><th width="193">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Started AidboxTask resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/start
params:
    id: c578a224-c1fa-44c1-bc7a-9d2e624b872b
    execId: aac4c869-daa4-45fb-917a-46fe14359ed4
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    definition: aidbox.bulk/import-resource-task
    meta:
      lastUpdated: '2023-05-05T13:40:38.088195Z'
      createdAt: '2023-05-05T09:37:41.786909Z'
      versionId: '128518'
    params:
      type: aidbox
      input:
        url: >-
          https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
    retryCount: 1
    resourceType: AidboxTask
    status: in-progress
    execId: aac4c869-daa4-45fb-917a-46fe14359ed4
    id: >-
      c578a224-c1fa-44c1-bc7a-9d2e624b872b
```
{% endtab %}
{% endtabs %}

### `awf.task/notify`

Notifies Task Service that a task is still alive.

After receiving notification, Task Service creates AidboxTaskLog resource with values of extra params fields below.

#### Notification Types:

<table><thead><tr><th width="178.33333333333331">Notification Type</th><th>Description</th></tr></thead><tbody><tr><td><code>heartbeat</code></td><td>Prolongs the <code>inProgressTimeout</code> property of the task and sends a string message.</td></tr><tr><td><code>progress</code></td><td>Prolongs the <code>inProgressTimeout</code> property of the task and sends information about its progress.</td></tr></tbody></table>

#### Params:

<table><thead><tr><th width="193">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr><tr><td>notification</td><td>string</td><td>true</td><td>The type of notification depends on which extra parameters may be required.<br>Possible values: <a href="task-executor-api.md#heartbeat-extra-params"><code>heartbeat</code></a>, <a href="task-executor-api.md#progress-extra-params"><code>progress</code></a></td></tr></tbody></table>

#### Extra Params

Depending on the [#notification-types](task-executor-api.md#notification-types), the following params are needed.

These parameters with notification type will be recorded in AidboxTaskLog resource after handling API request.

#### _For `heartbeat` Notification_ Type :

<table><thead><tr><th width="193">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>message</td><td>string</td><td>false</td><td>A string message about any additional information.</td></tr></tbody></table>

#### _For `progress` Notification Type :_

<table><thead><tr><th width="193">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>progress-current</td><td>integer</td><td>true</td><td>The current value of progress in an integer<br>(The numerator of <code>current / total</code>).</td></tr><tr><td>progress-total</td><td>integer</td><td>false</td><td>The total value of progress in an integer<br>(The dominator of <code>current / total</code>).</td></tr><tr><td>progress-unit</td><td>string</td><td>false</td><td>The unit for the values of <code>progress-total</code> and <code>progress-current</code>.<br><em>Example: <code>rows</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="208">Parameter</th><th width="92">Type</th><th>Description</th></tr></thead><tbody><tr><td>inProgressTimeoutAt</td><td>integer</td><td>The renewed <code>inProgressTimeout</code> property of the task.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/notify
params:
    id: 3abf82be-ec63-4fb4-908a-21d91b2d4af9
    execId:   292e9f34-c275-4cd3-840f-b4c39d0512e4
    notification: awf.task/heartbeat
    message: "task is alive"
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  inProgressTimeoutAt: '2023-05-05T14:07:27.831585Z'
```
{% endtab %}
{% endtabs %}

### `awf.task/success`

Changes the status of a task from `in-progress` to `done`, setting the outcome to `succeeded`.

#### Params:

<table><thead><tr><th width="193">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr><tr><td>result</td><td>object</td><td>false</td><td>Result with which task was succeeded.</td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Succeeded AidboxTask resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/success
params:
    id: c578a224-c1fa-44c1-bc7a-9d2e624b872b
    execId: aac4c869-daa4-45fb-917a-46fe14359ed4
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    definition: aidbox.bulk/import-resource-task
    pool: awf.executor/aidbox-long-tasks-pool2
    meta:
      lastUpdated: '2023-05-05T13:41:20.628857Z'
      createdAt: '2023-05-05T09:37:41.786909Z'
      versionId: '128521'
    params:
      type: aidbox
      input:
        url: >-
          https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
    retryCount: 1
    outcome: succeeded
    resourceType: AidboxTask
    status: done
    execId: aac4c869-daa4-45fb-917a-46fe14359ed4
    id: >-
      c578a224-c1fa-44c1-bc7a-9d2e624b872b
```
{% endtab %}
{% endtabs %}

### `awf.task/fail`

Changes the status of a task from `in-progress` to `done`, setting the outcome to `failed`.

#### Params:

<table><thead><tr><th width="193">Parameter</th><th width="89">Type</th><th width="100" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr><tr><td>error</td><td>object</td><td>false</td><td>Error with which task was failed.</td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Failed AidboxTask resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/fail
params:
    id: 7572b48b-7196-4887-bab7-a78bf09bdd83
    execId: d9bdbaa7-35ee-4234-a603-990a6a5dd559
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    definition: aidbox.bulk/import-resource-task
    meta:
      lastUpdated: '2023-05-05T14:15:49.658083Z'
      createdAt: '2023-05-05T14:12:52.899952Z'
      versionId: '128620'
    params:
      type: aidbox
      input:
        url: >-
          https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
    retryCount: 2
    outcome: failed
    resourceType: AidboxTask
    status: done
    execId: d9bdbaa7-35ee-4234-a603-990a6a5dd559
    id: >-
      7572b48b-7196-4887-bab7-a78bf09bdd83
    outcomeReason:
      type: awf.task/failed-by-executor
      message: Failed by executor

```
{% endtab %}
{% endtabs %}
