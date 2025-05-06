# Workflow User API

{% hint style="warning" %}
Workflow engine is configured by zen. We do not support it and do not recommend to use it anymore. Please, use any other workflow engine e.g. [Temporal](https://temporal.io/).

Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](https://docs.aidbox.app/modules-1/profiling-and-validation/fhir-schema-validator/setup)
{% endhint %}

Workflow User API allows users to manually control Aidbox workflows by [RPC methods](../../../../../api/other/rpc-api.md).

### `awf.workflow/create-and-execute`

Creates an instance of a defined workflow and makes it ready to be executed immediately or at a specified time.

#### Params:

<table><thead><tr><th width="133">Parameter</th><th width="87">Type</th><th width="111" data-type="checkbox">Required</th><th width="411">Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>false</td><td>Identifier of workflow (If not provided, will be auto-generated).</td></tr><tr><td>label</td><td>string</td><td>false</td><td>Human- or machine-readable description of workflow instance.<br><em>Example: <code>Import Patient resources</code></em></td></tr><tr><td>definition</td><td>string</td><td>true</td><td>Definition of <a href="https://github.com/Aidbox/documentation/blob/master/modules-1/workflow-engine/workflow/broken-reference/README.md">predefined workflow</a> or<br>custom-defined workflow.<br><em>Example:</em> <code>aidbox.bulk/import-resources-workflow</code></td></tr><tr><td>params</td><td>object</td><td>true</td><td>The input parameters described in the workflow definition.</td></tr><tr><td>executeAt</td><td>string</td><td>false</td><td>Time at which the workflow will become ready. If not provided - workflow will become ready immediately.<br><em>Example: <code>2023-05-03T13:30:43</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="92">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>Created AidboxWorkflow resources.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.workflow/create-and-execute
params:
  definition: aidbox.bulk/import-resources-workflow
  params:
    type: aidbox
    inputs:
      - url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
        resourceType: Organization
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      type: aidbox
      inputs:
        - url: >-
            https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
          resourceType: Organization
    status: in-progress
    definition: aidbox.bulk/import-resources-workflow
    id: >-
      abbc317d-f9b0-415e-b5b5-df059e000060
    resourceType: AidboxWorkflow
    meta:
      lastUpdated: '2023-05-22T11:55:06.570348Z'
      createdAt: '2023-05-22T11:55:06.570348Z'
      versionId: '18039'
```
{% endtab %}
{% endtabs %}

### `awf.workflow/cancel`

Cancels a workflow that is **not** in status _`done`_, canceling recursively all activities started by that workflow.

<table><thead><tr><th width="190">Parameter</th><th width="128.33333333333331">Type</th><th width="125" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Id of the workflow to be canceled.</td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="93">Type</th><th width="504">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>The canceled AidboxWorkflow resource.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.workflow/cancel
params:
  id: af10a9cf-3313-45f0-bbf4-7d3bf3a4da37
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      type: aidbox
      inputs:
        - url: https://synthea-public.s3.amazonaws.com/2/Claim.ndjson.gz
          resourceType: Claim
      contentEncoding: gzip
    status: done
    outcome: canceled
    definition: aidbox.bulk/import-resources-workflow
    id: >-
      af10a9cf-3313-45f0-bbf4-7d3bf3a4da37
    resourceType: AidboxWorkflow
    meta:
      lastUpdated: '2023-06-15T12:58:31.362126Z'
      createdAt: '2023-06-15T12:58:22.604066Z'
      versionId: '712'
```
{% endtab %}

{% tab title="Response (already done)" %}
```yaml
status: 422

error:
  type: workflow-has-been-done
  message: >-
    Workflow id:'af10a9cf-3313-45f0-bbf4-7d3bf3a4da37' has already been done.
    Couldn't cancel it.
```
{% endtab %}
{% endtabs %}

### `awf.workflow/status`

Returns the status of a workflow instance with the specified id.

#### Params:

<table><thead><tr><th width="190">Parameter</th><th width="128.33333333333331">Type</th><th width="125" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>true</td><td>Id of the workflow whose status will be returned.</td></tr><tr><td>include-activities?</td><td>boolean</td><td>false</td><td>If <code>true</code> , includes tasks and another workflows started by workflow.<br><em>Default: <code>false</code></em></td></tr><tr><td>include-decisions?</td><td>boolean</td><td>false</td><td>If <code>true,</code> includes all workflow's <a href="../task/aidbox-predefined-tasks.md#decision-task">decision tasks</a>.<br><em>Default: <code>false</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="93">Type</th><th width="504">Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>AidboxWorkflow resource.</td></tr><tr><td>activities</td><td>object[]</td><td>Activities started by workflow.</td></tr><tr><td>decisions</td><td>object[]</td><td>Decision task resources of workflow.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.workflow/status
params:
  id: abbc317d-f9b0-415e-b5b5-df059e000060
  include-activities?: true
  include-decisions?: true
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      type: aidbox
      inputs:
        - url: >-
            https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
          resourceType: Organization
    result:
      message: All input files imported, 0 new resources loaded
      total-files: 1
      total-imported-resources: 0
    status: done
    outcome: succeeded
    definition: aidbox.bulk/import-resources-workflow
    id: >-
      abbc317d-f9b0-415e-b5b5-df059e000060
    resourceType: AidboxWorkflow
    meta:
      lastUpdated: '2023-05-22T11:55:07.427230Z'
      createdAt: '2023-05-22T11:55:06.570348Z'
      versionId: '18073'
  activities:
    - definition: aidbox.bulk/import-resource-task
      meta:
        lastUpdated: '2023-05-22T11:55:07.379041Z'
        createdAt: '2023-05-22T11:55:06.673539Z'
        versionId: '18061'
      params:
        type: aidbox
        input:
          url: >-
            https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
          resourceType: Organization
      retryCount: 1
      outcome: succeeded
      resourceType: AidboxTask
      requester:
        id: >-
          abbc317d-f9b0-415e-b5b5-df059e000060
        resourceType: AidboxWorkflow
      status: done
      result:
        imported-resources: 0
      execId: f5fd6d24-b240-4225-87c7-c57538bf083c
      label: >-
        Organization
        https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
      id: >-
        6e718350-0fc1-4bdd-b8e2-d73890f4fff7
  decisions:
    - definition: awf.workflow/decision-task
      meta:
        lastUpdated: '2023-05-22T11:55:06.673539Z'
        createdAt: '2023-05-22T11:55:06.570348Z'
        versionId: '18049'
      params:
        event: awf.workflow.event/workflow-init
      retryCount: 1
      outcome: succeeded
      workflow-definition: aidbox.bulk/import-resources-workflow
      resourceType: AidboxTask
      requester:
        id: >-
          abbc317d-f9b0-415e-b5b5-df059e000060
        resourceType: AidboxWorkflow
      status: done
      result:
        - action: awf.workflow.action/schedule-task
          task-request:
            label: >-
              Organization
              https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
            params:
              type: aidbox
              input:
                url: >-
                  https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
                resourceType: Organization
            definition: aidbox.bulk/import-resource-task
      execId: 7d8d6ad5-fbd8-4bce-b7a0-daaf8cd91f13
      id: >-
        97c7b246-de63-4302-927d-6dc160d332af
    - definition: awf.workflow/decision-task
      meta:
        lastUpdated: '2023-05-22T11:55:07.427230Z'
        createdAt: '2023-05-22T11:55:07.379041Z'
        versionId: '18071'
      params:
        event: awf.workflow.event/task-completed
        task-id: 6e718350-0fc1-4bdd-b8e2-d73890f4fff7
      retryCount: 1
      outcome: succeeded
      workflow-definition: aidbox.bulk/import-resources-workflow
      resourceType: AidboxTask
      requester:
        id: >-
          abbc317d-f9b0-415e-b5b5-df059e000060
        resourceType: AidboxWorkflow
      status: done
      result:
        - action: awf.workflow.action/complete-workflow
          result:
            message: All input files imported, 0 new resources loaded
            total-files: 1
            total-imported-resources: 0
      execId: e7820ed2-92e7-4635-846d-35331051b466
      id: >-
        439dd4a4-7d70-4cf2-85ce-244878a2c910
```
{% endtab %}
{% endtabs %}

### `awf.workflow/list`

Returns the list of all workflows.

#### Params:

<table><thead><tr><th width="230">Parameter</th><th width="88">Type</th><th width="101" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td><strong>filter</strong></td><td>object</td><td>false</td><td></td></tr><tr><td>filter.status</td><td>string</td><td>false</td><td>Include workflow with specified status.<br><em>Possible values: <code>created</code>, <code>in-progress</code>, <code>done</code></em></td></tr><tr><td>filter.outcome</td><td>string</td><td>false</td><td>Include workflow with status <em><code>done</code></em> and specified outcome.<br><em>Possible values: <code>succeeded</code>, <code>failed</code></em></td></tr><tr><td>filter.ilike</td><td>string</td><td>false</td><td>ilike search by resource content</td></tr><tr><td><strong>sort</strong></td><td>object</td><td>false</td><td>May contain either <code>createdAt</code> or <code>lastUpdated</code>.</td></tr><tr><td>sort.createdAt</td><td>string</td><td>false</td><td>Sorts result by <code>createdAt</code> DateTime.<br><em>Possible values: <code>asc</code>, <code>desc</code></em></td></tr><tr><td>sort.lastUpdated</td><td>string</td><td>false</td><td>Sorts result by <code>lastUpdated</code> DateTime.<br><em>Possible values: <code>asc</code>, <code>desc</code></em></td></tr></tbody></table>

#### Result:

<table><thead><tr><th width="153">Parameter</th><th width="97">Type</th><th width="503">Description</th></tr></thead><tbody><tr><td>resources</td><td>object[]</td><td>AidboxWorkflow resources.</td></tr></tbody></table>

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.workflow/list
params:
  filter: 
    ilike: import
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
        c9555892-6221-42b4-9b34-1428a18e893result:
  resources:
    - params:
        type: aidbox
        inputs:
          - url: >-
              https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
            resourceType: Organization
      result:
        message: All input files imported, 0 new resources loaded
        total-files: 1
        total-imported-resources: 0
      status: done
      outcome: succeeded
      definition: aidbox.bulk/import-resources-workflow
      id: >-
        abbc317d-f9b0-415e-b5b5-df059e000060
      resourceType: AidboxWorkflow
      meta:
        lastUpdated: '2023-05-22T11:55:07.427230Z'
        createdAt: '2023-05-22T11:55:06.570348Z'
        versionId: '18073'
```
{% endtab %}
{% endtabs %}
