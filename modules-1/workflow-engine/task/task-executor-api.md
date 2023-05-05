# Task Executor API

Task Executor API is designed to allow implement task executor in any programming language and use it in Aidbox via REST requests with [RPC calls](../../../api-1/rpc-api.md).&#x20;

### `awf.task/poll`

Fetches a new task from the queue and moves its status from `ready` to `requested`, immediately returns an empty array if there are no tasks in the queue.

#### Params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>pool</td><td>string</td><td>true</td><td>???</td></tr><tr><td>maxBatchSize</td><td>integer</td><td>false</td><td>The number of tasks that can be polled from the queue simultaneously.<br><em>Default value: 1</em></td></tr><tr><td>includeDefinitions</td><td>string[]</td><td>false</td><td>An array of task definitions to include.<br><em>Exclusive with <code>excludeDefinitions</code></em></td></tr><tr><td>excludeDefinitions</td><td>string[]</td><td>false</td><td>An array of task definitions to exclude.<br><em>Exclusive with <code>includeDefinitions</code></em></td></tr></tbody></table>

#### Result:

| Parameter | Type      | Description                                 |
| --------- | --------- | ------------------------------------------- |
| resources | object\[] | Created AidboxTask resources with `execId`. |

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/poll
params:
    pool: awf.executor/aidbox-long-tasks-pool
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

Fetches a new task from the queue and moves its status from `ready` to `requested`. Waits until timeout unless there will be a new task, and after that returns an empty array.&#x20;

#### Params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>pool</td><td>string</td><td>true</td><td>???</td></tr><tr><td>maxBatchSize</td><td>integer</td><td>false</td><td>The number of tasks that can be polled from the queue simultaneously.<br><em>Default value: 1</em></td></tr><tr><td>includeDefinitions</td><td>string[]</td><td>false</td><td>An array of task definitions to include.<br><em>Exclusive with <code>excludeDefinitions</code></em></td></tr><tr><td>excludeDefinitions</td><td>string[]</td><td>false</td><td>An array of task definitions to exclude.<br><em>Exclusive with <code>includeDefinitions</code></em></td></tr><tr><td>timeout</td><td>integer</td><td>false</td><td>A period of time in ms, the period of time during which the tasks can be polled. <br><em>Default value: 60000 (equal to 1 minute)</em></td></tr></tbody></table>

#### Result:

| Parameter | Type      | Description                                 |
| --------- | --------- | ------------------------------------------- |
| resources | object\[] | Created AidboxTask resources with `execId`. |

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/long-poll
params:
    timeout: 50000
    pool: awf.executor/aidbox-long-tasks-pool
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

Moves a task status from `requested` to `in-progress` and start its execution. Requires to use `execId` received from `awf.task/poll` or `awf.task/long-poll`.

#### Params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr></tbody></table>

#### Result:

| Parameter | Type   | Description                  |
| --------- | ------ | ---------------------------- |
| resource  | object | Started AidboxTask resource. |

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
    pool: awf.executor/aidbox-long-tasks-pool
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

Notifies task service that a task is still alive and creates AidboxTaskLog resource depending on the [notification type](task-executor-api.md#notification-types).

#### Notification types:

| Notification type | Description                                                     |
| ----------------- | --------------------------------------------------------------- |
| `heartbeat`       | Used to renew the `inProgressTimeout` property of the task.     |
| `progress`        | Used to periodically pass current progress to the task service. |

#### Params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr><tr><td>notification</td><td>string</td><td>true</td><td>The type of notification depends on which extra parameters may be required.<br>Possible values: <a href="task-executor-api.md#heartbeat-extra-params"><code>heartbeat</code></a>, <a href="task-executor-api.md#progress-extra-params"><code>progress</code></a></td></tr></tbody></table>

#### Heartbeat extra params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>message</td><td>string</td><td>false</td><td>Message field for additional information.</td></tr></tbody></table>

#### Progress extra params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>progress-current</td><td>integer</td><td>true</td><td>Current progress.</td></tr><tr><td>progress-total</td><td>integer</td><td>false</td><td>Total progress.</td></tr><tr><td>progress-unit</td><td>string</td><td>false</td><td>Unit is supposed to be used with <code>progress-total</code> and <code>progress-current</code>.<br><em>Example: <code>rows</code></em></td></tr></tbody></table>

#### Result:

| Parameter           | Type    | Description |
| ------------------- | ------- | ----------- |
| inProgressTimeoutAt | integer |             |

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

Move a task status from `in-progress`  to `done` with outcome `succeeded`.

#### Params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr><tr><td>result</td><td>object</td><td>false</td><td>Result with which task was succeeded.</td></tr></tbody></table>

#### Result:

| Parameter | Type   | Description                    |
| --------- | ------ | ------------------------------ |
| resource  | object | Succeeded AidboxTask resource. |

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

Moves a task status from `in-progress` to `done` with outcome `failed`.

#### Params:

<table><thead><tr><th>Parameter</th><th>Type</th><th data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Identifier of the Task resource.</td></tr><tr><td>execId</td><td>string</td><td>true</td><td>Execution id of the task. Used to avoid duplicate task executions.</td></tr><tr><td>error</td><td>object</td><td>false</td><td>Error with which task was failed.</td></tr></tbody></table>

#### Result:

| Parameter | Type   | Description                 |
| --------- | ------ | --------------------------- |
| resource  | object | Failed AidboxTask resource. |

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
    pool: awf.executor/aidbox-long-tasks-pool2
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
