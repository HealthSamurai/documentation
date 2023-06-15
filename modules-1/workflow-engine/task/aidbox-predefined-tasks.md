# Aidbox Predefined Tasks

## awf.task/subscribe

Task that will be in progress until a resource with the specified attributes would be persisted in the database. It is used for some workflow cases when the workflow should wait for resource creation.

### Params

<table data-full-width="false"><thead><tr><th width="141.33333333333331">Parameter</th><th width="77">Type</th><th width="104" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>select-by</td><td>string</td><td>true</td><td>Rule written on <a href="../services.md#subscription-trigger-rule-dsl">subscription trigger rule DSL</a>.</td></tr></tbody></table>

### Result

<table data-full-width="false"><thead><tr><th width="141.33333333333331">Parameter</th><th width="84">Type</th><th>Description</th></tr></thead><tbody><tr><td>resource</td><td>object</td><td>The resource that triggered the task completion.</td></tr></tbody></table>

### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/create-and-execute
params:
  definition: awf.task/subscribe
  params:
    select-by: 
      - get-in: 
        - resourceType
        comparator: eq
        value: Patient
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      select-by:
        - value: Patient
          get-in:
            - resourceType
          comparator: eq
    status: in-progress
    definition: awf.task/subscribe
    retryCount: 1
    id: >-
      ce39265f-3ab0-40c4-b407-b75a45dc2f26
    resourceType: AidboxTask
    meta:
      lastUpdated: '2023-05-24T13:54:04.444841Z'
      createdAt: '2023-05-24T13:54:04.444841Z'
      versionId: '38277'
```
{% endtab %}

{% tab title="Status response after Patient created" %}
```yaml
result:
  resource:
    definition: awf.task/subscribe
    meta:
      lastUpdated: '2023-05-24T13:55:21.134679Z'
      createdAt: '2023-05-24T13:54:04.444841Z'
      versionId: '38406'
    params:
      select-by:
        - value: Patient
          get-in:
            - resourceType
          comparator: eq
    retryCount: 1
    outcome: succeeded
    resourceType: AidboxTask
    status: done
    result:
      resource:
        id: >-
          asdasd
        meta:
          createdAt: '2023-05-24T13:55:21.131777Z'
          versionId: '38405'
          lastUpdated: '2023-05-24T13:55:21.131777Z'
        resourceType: Patient
    id: >-
      ce39265f-3ab0-40c4-b407-b75a45dc2f26
```
{% endtab %}
{% endtabs %}

## awf.task/wait

Task that will wait for the indicated duration or until the indicated datetime. Used in workflow when need to be paused for an external signal to continue.

Either **duration** or **until** parameter SHOULD be specified.

### Params

<table data-full-width="false"><thead><tr><th width="180.33333333333331">Parameter</th><th width="128">Type</th><th width="109" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td><strong>duration</strong></td><td>object</td><td>false</td><td>The task will wait for this duration</td></tr><tr><td>duration.hours</td><td>integer</td><td>false</td><td>duration value in hours</td></tr><tr><td>duration.minutes</td><td>integer</td><td>false</td><td>duration value in minutes</td></tr><tr><td>duration.seconds</td><td>integer</td><td>false</td><td>duration value in seconds</td></tr><tr><td><strong>until</strong></td><td>datetime</td><td>false</td><td><p>The task will wait for until this datetime. Need to indicate in string of valid  FHIR datetime -<br><code>YYYY-MM-DD</code> or </p><p><code>YYYY-MM-DDThh:mm:ss+zz:zz</code><br></p><p><em>Example:</em> "1905-08-23", "2015-02-07T13:28:17-05:00" or "2017-01-01T00:00:00.000Z"</p></td></tr></tbody></table>

### Result

No result

### Example

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/create-and-execute
params:
  definition: awf.task/wait
  params:
    duration:
      hours: 4
      minutes: 30
```
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      duration:
        hours: 4
        minutes: 30
    status: in-progress
    definition: awf.task/wait
    retryCount: 1
    id: >-
      41ab6e83-fa01-4a25-be56-beafd3ee5744
    resourceType: AidboxTask
    meta:
      lastUpdated: '2023-06-15T12:44:02.740779Z'
      createdAt: '2023-06-15T12:44:02.740779Z'
      versionId: '400'
```
{% endtab %}
{% endtabs %}

##

## awf.workflow/decision-task

**Decision tasks** - special tasks that form the body of the workflow and partially execute it in certain iterations. Decision tasks are created when a workflow is started or as a response on [another event](aidbox-predefined-tasks.md#event-types) to decide what action should be taken. Actions are predefined in task execution operations that are used as a response to a completed decision task. Actions are used in [certain cases](aidbox-predefined-tasks.md#action-types), such as starting a new task.

Decision tasks should also include an event that should represent the purpose of that task as a parameter.

### Params

<table data-full-width="true"><thead><tr><th width="141.33333333333331">Parameter</th><th width="77">Type</th><th width="104" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>event</td><td>string</td><td>true</td><td><a href="aidbox-predefined-tasks.md#event-types">Type of event.</a><br><em>Example:</em> <em><code>awf.workflow.event/workflow-init</code></em></td></tr><tr><td>workflow-id</td><td>string</td><td>false</td><td>Id of completed workflow instance. <strong>Required only if event type is</strong> <em><code>awf.workflow.event/workflow-completed</code></em></td></tr><tr><td>task-id</td><td>string</td><td>false</td><td>Id of completed task instance. <strong>Required only if event type is</strong> <em><code>awf.workflow.event/task-completed</code></em></td></tr></tbody></table>

### Event types

<table data-full-width="true"><thead><tr><th width="415">Event type</th><th>Description</th></tr></thead><tbody><tr><td><em><code>awf.workflow.event/workflow-init</code></em></td><td>First task execution in the context of the current workflow instance. The decision task contains this event if it was started using the <code>awf.workflow/create-and-execute</code> method.</td></tr><tr><td><em><code>awf.workflow.event/task-completed</code></em></td><td>Reaction to a completed task started by the current workflow. Includes the ID of the completed task instance in the decision task parameters.</td></tr><tr><td><em><code>awf.workflow.event/workflow-completed</code></em></td><td>Reaction to a completed workflow started by the current workflow. Includes the ID of the completed workflow instance in the decision task parameters.</td></tr></tbody></table>

### Result

<table data-full-width="true"><thead><tr><th width="155.33333333333331">Parameter</th><th width="81">Type</th><th width="103" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>action</td><td>string</td><td>true</td><td><a href="aidbox-predefined-tasks.md#action-types">Type of action.</a><br><em>Example:</em> <em><code>awf.workflow.action/schedule-task</code></em></td></tr><tr><td><strong>task-request</strong></td><td>object</td><td>false</td><td><p>Object with task execution request. <strong>Required</strong> <strong>only if action type is</strong> <em><code>awf.workflow.action/schedule-task</code> or</em> </p><p><em><code>awf.workflow.action/schedule-workflow</code></em></p></td></tr><tr><td><p><strong>task-request</strong>.</p><p>definition</p></td><td>string</td><td>false</td><td>Definition of <a href="aidbox-predefined-tasks.md#aidbox-predefined-tasks">predefined task</a> or <br>custom-defined task.<br><em>Example:</em> <code>aidbox.archive/create-archive</code></td></tr><tr><td><strong>task-request</strong>.<br>label</td><td>string</td><td>false</td><td>Human- or machine-readable description of task instance. Should be  unique in the context of single workflow.<br><em>Example: <code>Import Patient resources</code></em></td></tr><tr><td><strong>task-request</strong>.<br>params</td><td>object</td><td>false</td><td>The input parameters described in the task or workflow definition.</td></tr></tbody></table>

### Action types

<table data-full-width="true"><thead><tr><th width="415">Action type</th><th>Description</th></tr></thead><tbody><tr><td><em><code>awf.workflow.action/schedule-task</code></em></td><td>Used to schedule the execution of a new task from current workflow. Requires task request object in the response.</td></tr><tr><td><em><code>awf.workflow.action/schedule-workflow</code></em></td><td>Used to schedule the execution of a new workflow from current workflow. Requires workflow request object in the response.</td></tr><tr><td><em><code>awf.workflow.action/do-nothing</code></em></td><td>Used to wait until the next event occurs when multiple tasks have been started in parallel, but when some tasks are completed, other tasks are still in progress.</td></tr><tr><td><em><code>awf.workflow.action/complete-workflow</code></em></td><td>Used when all tasks have been completed to change the <a href="../workflow/#workflow-statuses-and-outcomes">status of the workflow</a> instance to <code>done</code> with outcome <code>succeeded</code>.</td></tr><tr><td><em><code>awf.workflow.action/fail</code></em></td><td>Used when some tasks have failed to change the status of the workflow instance to <code>done</code> with outcome <code>failed</code> with handled error.</td></tr><tr><td><em><code>awf.workflow.action/unknown-error</code></em></td><td>Used when some tasks have failed to change the status of the workflow instance to <code>done</code> with outcome <code>failed</code> with unknown error.</td></tr></tbody></table>

##

## Import resource task

Loading resource in specified format (.gzip or .ndjson) from specified url and persists it into Aidbox database.

### Params

<table data-full-width="true"><thead><tr><th width="216">Parameter</th><th width="86">Type</th><th width="102" data-type="checkbox">Required</th><th width="596">Description</th></tr></thead><tbody><tr><td>id</td><td>string</td><td>false</td><td>Identifier of the import.<br>If you don't provide this, the id will be auto-generated. You can check it on <code>Content-Location</code> header in the response</td></tr><tr><td>contentEncoding</td><td>string</td><td>false</td><td>Supports <code>gzip</code> or <code>plain</code> (non-gzipped .ndjson files)</td></tr><tr><td><strong>input</strong></td><td>object</td><td>true</td><td>Resource to import </td></tr><tr><td><strong>input</strong>.url</td><td>string</td><td>true</td><td>URL from which load resources</td></tr><tr><td><strong>input</strong>.resourceType</td><td>string</td><td>true</td><td>Resource type to be loaded</td></tr><tr><td>type</td><td>string</td><td>false</td><td>Type of persisted resource<br><em>Possible values: <code>fhir</code>, <code>aidbox</code></em></td></tr></tbody></table>

### Example

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
      102fe9a4-386e-4b66-a01a-7f6c2661e9bd
    resourceType: AidboxTask
    meta:
      lastUpdated: '2023-05-24T13:37:49.615506Z'
      createdAt: '2023-05-24T13:37:49.615506Z'
      versionId: '37424'
```
{% endtab %}

{% tab title="Status response" %}
```yaml
result:
  resource:
    definition: aidbox.bulk/import-resource-task
    meta:
      lastUpdated: '2023-05-24T13:37:49.828022Z'
      createdAt: '2023-05-24T13:37:49.615506Z'
      versionId: '37433'
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
    result:
      imported-resources: 0
    execId: 2748c559-cd78-4005-abc8-b85e5e94b3a4
    id: >-
      102fe9a4-386e-4b66-a01a-7f6c2661e9bd
```
{% endtab %}
{% endtabs %}

##

## ingestion/map-to-fhir-bundle-task

Task that applies [lisp/mapping](../../../tools/mappings/mappings-with-lisp-mapping.md) to the given context and applies the result to the database as a bundle in either fhir or aidbox formats.

### Params

<table data-full-width="false"><thead><tr><th width="141.33333333333331">Parameter</th><th width="85">Type</th><th width="104" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>mapping</td><td>string</td><td>true</td><td>Reference to a defined <a href="../../../tools/mappings/mappings-with-lisp-mapping.md">lisp/mapping</a>.</td></tr><tr><td>context</td><td>object</td><td>true</td><td>Initial data.</td></tr><tr><td>format</td><td>string</td><td>true</td><td>Bundle format.<br><em>Possible values: <code>fhir</code>, <code>aidbox</code></em></td></tr></tbody></table>

### Example

<pre class="language-clojure"><code class="lang-clojure">{ns     my-mappings
 import #{lisp}
<strong> 
</strong><strong> my-mapping
</strong> {:zen/tags #{lisp/mapping}
  :mapping  {:resourceType "Bundle"
             :type "transaction"
             :entry [{:resource {:resourceType "Patient"}
                      :request {:method "PUT"
                                :url "/Patient/zero"}}

                     {:resource {:resourceType "Observation"
                                 :status (get :status)
                                 :code {:coding [{:system "http://loinc.org"
                                                  :code "8867-4"
                                                  :display "Respiratory rate"}]
                                        :text "Breathing Rate"}
                                 :subject {:reference "Patient/zero"}
                                 :effectiveDateTime (get-in [:br 0 :dateTime]),
                                 :valueQuantity   {
                                                   :value (get-in [:br 0 :value :breathingRate]),
                                                   :unit "breaths/minute",
                                                   :system "http://unitsofmeasure.org",
                                                   :code "/min"}}
                      :request {:method "POST"
                                :url "/Observation"}}]}}}
</code></pre>

{% tabs %}
{% tab title="Request" %}
{% code fullWidth="false" %}
```yaml
POST /rpc
content-type: text/yaml
accept: text/yaml

method: awf.task/create-and-execute
params:
  definition: ingestion.core/map-to-fhir-bundle-task
  params:
    format: fhir
    mapping: my-mappings/my-mapping
    context: 
      br: 
        - value: 
            breathingRate: 17.8
        - dateTime: 2021-10-25
      status: final
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
```yaml
result:
  resource:
    params:
      format: fhir
      context:
        br:
          - value:
              breathingRate: 17.8
          - dateTime: '2021-10-25T00:00:00Z'
        status: final
      mapping: aidbox-with-task/my-mapping
    status: ready
    definition: ingestion.core/map-to-fhir-bundle-task
    id: >-
      fd9808c9-29c5-409b-a10f-96d3e28d0039
    resourceType: AidboxTask
    meta:
      lastUpdated: '2023-05-24T12:17:32.528315Z'
      createdAt: '2023-05-24T12:17:32.528315Z'
      versionId: '33349'
```
{% endtab %}

{% tab title="Status response" %}
```yaml
result:
  resource:
    definition: ingestion.core/map-to-fhir-bundle-task
    meta:
      lastUpdated: '2023-05-24T12:20:45.742574Z'
      createdAt: '2023-05-24T12:20:45.072825Z'
      versionId: '33546'
    params:
      format: fhir
      context:
        br:
          - value:
              breathingRate: 17.8
          - dateTime: '2021-10-25T00:00:00Z'
        status: final
      mapping: my-mappings/my-mapping
    retryCount: 1
    outcome: succeeded
    resourceType: AidboxTask
    status: done
    result:
      id: >-
        33545
      type: transaction-response
      entry:
        - resource:
            id: >-
              zero
            meta:
              extension:
                - url: http://example.com/createdat
                  valueInstant: '2023-05-24T12:20:45.110149Z'
              versionId: '33542'
              lastUpdated: '2023-05-24T12:20:45.110149Z'
            resourceType: Patient
          response:
            etag: '33542'
            status: '201'
            location: /Patient/zero/_history/33542
            lastModified: '2023-05-24T12:20:45.110149Z'
        - resource:
            id: >-
              8f21674f-e03c-47b4-b952-1e237db99b04
            code:
              text: Breathing Rate
              coding:
                - code: 8867-4
                  system: http://loinc.org
                  display: Respiratory rate
            meta:
              extension:
                - url: http://example.com/createdat
                  valueInstant: '2023-05-24T12:20:45.110149Z'
              versionId: '33544'
              lastUpdated: '2023-05-24T12:20:45.110149Z'
            status: final
            subject:
              reference: Patient/zero
            resourceType: Observation
            valueQuantity:
              code: /min
              unit: breaths/minute
              value: 17.8
              system: http://unitsofmeasure.org
          response:
            etag: '33544'
            status: '201'
            location: /Observation/8f21674f-e03c-47b4-b952-1e237db99b04/_history/33544
            lastModified: '2023-05-24T12:20:45.110149Z'
      resourceType: Bundle
    execId: ae747dd9-6cda-477f-8a83-5f6b18cee94e
    id: >-
      fd9808c9-29c5-409b-a10f-96d3e28d0039
```
{% endtab %}
{% endtabs %}
