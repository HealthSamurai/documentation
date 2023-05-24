# Task User API

Task User API allows users to manually control Aidbox tasks by [RPC methods](../../../api-1/rpc-api.md).&#x20;

### `awf.task/create-and-execute`&#x20;

Creates an instance of a defined task and makes it ready to be executed immediately or at a specified time.

#### Params:

<table><thead><tr><th width="133">Parameter</th><th width="87">Type</th><th width="111" data-type="checkbox">Required</th><th width="411">Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>false</td><td>Identifier of task (If not provided, will be auto-generated).</td></tr><tr><td>label</td><td>string</td><td>false</td><td>Human- or machine-readable description of task instance.<br><em>Example: <code>Import Patient resources</code></em></td></tr><tr><td>definition</td><td>string</td><td>true</td><td>Definition of <a href="task-user-api.md#aidbox-predefined-tasks">predefined task</a> or <br>custom-defined task.<br><em>Example:</em> <code>aidbox.archive/create-archive</code></td></tr><tr><td>params</td><td>object</td><td>true</td><td>The input parameters described in the task definition.</td></tr><tr><td>executeAt</td><td>string</td><td>false</td><td>Time at which the task will become ready. If not provided - task will become ready immediately. <br><em>Example: <code>2023-05-03T13:30:43</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>Created AidboxTask resources.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/create-and-execute
params:
  definition: aidbox.bulk/import-resource-task
  params:
    type: aidbox
    input:
      url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
      resourceType: Organization
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      type: aidbox
      input:
        url: >-
          https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
    status: ready
    definition: aidbox.bulk/import-resource-task
    id: >-
      61053bc2-aac6-49a1-91e7-7f9fa29afe21
    resourceType: AidboxTask
    meta:
      lastUpdated: '2023-05-03T15:21:19.357771Z'
      createdAt: '2023-05-03T15:21:19.357771Z'
      versionId: '17525'
```
{% endtab %}
{% endtabs %}

### `awf.task/status`

Returns the status of a task instance with the specified id.

#### Params:

<table><thead><tr><th width="183">Parameter</th><th width="128.33333333333331">Type</th><th width="125" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Id of the task whose status will be returned.</td></tr><tr><td>include-settings?</td><td>boolean</td><td>false</td><td>If <code>true</code> , includes setting parameters predefined in definition.<br><em>Default: <code>false</code></em></td></tr><tr><td>include-log?</td><td>boolean</td><td>false</td><td>If <code>true,</code> includes log of task status transitions according to <a data-mention href="task-user-api.md#task-instance-lifecycle">#task-instance-lifecycle</a>.<br><em>Default: <code>false</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="93">Type</th><th width="504">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Created AidboxTask resource.</td></tr><tr><td>settings</td><td>object</td><td>TODO</td></tr><tr><td>log</td><td>object[]</td><td>AidboxTaskLog resources for task with specified id</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/status
params:
  id: c9555892-6221-42b4-9b34-1428a18e893c
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    definition: aidbox.bulk/import-resource-task
    meta:
      lastUpdated: '2023-04-24T09:30:51.562261Z'
      createdAt: '2023-04-24T09:30:50.888731Z'
      versionId: '128371'
    params:
      type: aidbox
      input:
        url: >-
          https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
      contentEncoding: gzip
    retryCount: 1
    outcome: succeeded
    resourceType: AidboxTask
    status: done
    result:
      imported-resources: 100
    execId: ea82769d-e083-461c-8cb0-3427ed466f19
    label: >-
      Organization
      https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
    id: >-
      c9555892-6221-42b4-9b34-1428a18e893c
```
{% endtab %}
{% endtabs %}



### `awf.task/cancel`

Cancels execution of a created task instance.

#### Params:

<table><thead><tr><th width="183">Parameter</th><th width="128.33333333333331">Type</th><th width="125" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Id of the task that will be canceled.</td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="93">Type</th><th width="504">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>Created AidboxTask resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/cancel
params:
  id: 485b9cbd-0a78-4909-9908-0ae2e66a2b12
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      type: aidbox
      input:
        url: >-
          https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
    status: done
    outcome: canceled
    executeAt: '2023-05-04T15:30:43'
    definition: aidbox.bulk/import-resource-task
    id: >-
      485b9cbd-0a78-4909-9908-0ae2e66a2b12
    resourceType: AidboxTask
    meta:
      lastUpdated: '2023-05-04T12:25:38.800464Z'
      createdAt: '2023-05-04T12:25:24.355856Z'
      versionId: '17563'
```
{% endtab %}
{% endtabs %}

### `awf.task/list`

Returns the list of all tasks.

#### Params:

<table><thead><tr><th width="230">Parameter</th><th width="88">Type</th><th width="101" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td><strong>filter</strong></td><td>object</td><td>false</td><td></td></tr><tr><td>filter.includeDefinitions</td><td>string[]</td><td>false</td><td>Array of task definitions to include.</td></tr><tr><td>filter.excludeDefinitions</td><td>string[]</td><td>false</td><td>Array of task definitions to exclude.</td></tr><tr><td>filter.status</td><td>string</td><td>false</td><td>Include task with specified status.<br><em>Possible values: <code>created</code>, <code>waiting</code>, <code>ready</code>, <code>requested</code>, <code>in-progress</code>, <code>done</code></em></td></tr><tr><td>filter.outcome</td><td>string</td><td>false</td><td>Include task with status <em><code>done</code></em> and specified outcome.<br><em>Possible values: <code>succeeded</code>, <code>failed</code>, <code>canceled</code></em></td></tr><tr><td>filter.ilike</td><td>string</td><td>false</td><td>ilike search by resource content</td></tr><tr><td><strong>sort</strong></td><td>object</td><td>false</td><td>May contain either <code>createdAt</code> or <code>lastUpdated</code>.</td></tr><tr><td>sort.createdAt</td><td>string</td><td>false</td><td>Sorts result by <code>createdAt</code> DateTime.<br><em>Possible values: <code>asc</code>, <code>desc</code></em></td></tr><tr><td>sort.lastUpdated</td><td>string</td><td>false</td><td>Sorts result by <code>lastUpdated</code> DateTime.<br><em>Possible values: <code>asc</code>, <code>desc</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="97">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>Created AidboxTask resources.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/list
params:
  filter:
    ilike: import-resource-task
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resources:
    - definition: aidbox.bulk/import-resource-task
      meta:
        lastUpdated: '2023-04-24T09:30:51.562261Z'
        createdAt: '2023-04-24T09:30:50.888731Z'
        versionId: '128371'
      params:
        type: aidbox
        input:
          url: >-
            https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
          resourceType: Organization
        contentEncoding: gzip
      retryCount: 1
      outcome: succeeded
      resourceType: AidboxTask
      requester:
        id: >-
          38239f30-2e67-45fb-8e67-992c5c1b350e
        resourceType: AidboxWorkflow
      status: done
      result:
        imported-resources: 100
      execId: ea82769d-e083-461c-8cb0-3427ed466f19
      label: >-
        Organization
        https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
      id: >-
        c9555892-6221-42b4-9b34-1428a18e893
```
{% endtab %}
{% endtabs %}
