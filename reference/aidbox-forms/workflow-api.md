# Workflow API

{% hint style="warning" %}
Workflow API wraps Form API and when you use WF you don't need to use Form API directly
{% endhint %}

* [get-workflow](workflow-api.md#get-workflow) - get workflow definition
* [get-workflows](workflow-api.md#get-workflows) - return existed workflows
* [start-workflow](workflow-api.md#start-workflow) - start WF and launch forms
* [save-step](workflow-api.md#save-step) - save document through WF, mark step as in-progress
* [skip-step](workflow-api.md#skip-step) - skip WF step
* [amend-step](workflow-api.md#amend-step) - amend WF step
* [complete-step](workflow-api.md#complete-step) - try complete WF step with document, call sign on it.
* [complete-workflow](workflow-api.md#complete-workflow) - complete WF
* [cancel-workflow](workflow-api.md#cancel-workflow) - try cancel WF
* [amend-workflow](workflow-api.md#amend-workflow) - amend completed WF
* [add-workflow-note](workflow-api.md#add-workflow-note) - add addendum note to the given WF
* [get-workflow-addendums](workflow-api.md#get-workflow-addendums) - get list of addendums for the given WF

### get-workflows

Return existed Workflows

> Response can be narrowed with Workflow Symbol name substring

params:

| Param | Description                         | Type   | required? |
| ----- | ----------------------------------- | ------ | --------- |
| q     | substring of Workflow symbolic name | String | yes       |

```
POST /rpc?

method: aidbox.sdc/get-workflows
params:
    q: 'Demo'
```

Response:

```
result:
 - workflow: 'box.sdc.sdc-example/DemoWF
   title:Demo workflow,
 - workflow:'box.sdc.sdc-example/DemoWF2
 - workflow:'box.sdc.sdc-example/DemoWF3
```

### get-workflow

Get workflow definition by name

| Param    | Description          | Type   | required? |
| -------- | -------------------- | ------ | --------- |
| workflow | Workflow Symbol name | String | yes       |

```
POST /rpc?

method: aidbox.sdc/get-workflow
params:
    workflow : aidbox.sdc.DemoVisitWorkflow
```

Responses:

> Error

```
error:
  message:  Can't find random-wf workflow
```

> Success

```
result:
  title: Demo workflow
  version: 0.0.1
  items:
     phisical-exam:
       item: aidbox.sdc/FormItem
       form: box.sdc.sdc-example/VitalsForm
     questionnaire:
       item: aidbox.sdc/FormItem
       form: box.sdc.sdc-example/PHQ2PHQ9Form
```

### start-workflow

Launch all forms of the WF and create `SDCWorkflow` resource.

| Param    | Description                                 | Type          | required? |
| -------- | ------------------------------------------- | ------------- | --------- |
| workflow | workflow symbolic name                      | String        | yes       |
| params   | Parameters wich will be propagated to forms | Map\<Any,Any> | yes       |
| dry-run  | Run without saving document and extractions | boolean       | no        |

```
POST /rpc?

method: 'aidbox.sdc/start-workflow,
params:
  workflow : aidbox.sdc.DemoVisitWorkflow
  params:
    encounter-id: enc-1
    patient-id:   pt-1
```

Result:

```
---
result:
  title: Demo workflow
  version: 0.0.1
  status: new
  items:
    phisical-exam:
      layout:
        type: "'aidbox.sdc/col"
      item: "'aidbox.sdc/FormItem"
      document-def:
        keys: {...}
        sdc/rules: {...}
      rules: {...}
      title: Vitals Signs
      document: {...}
      status: new
      form: "'box.sdc.sdc-example/VitalsForm"
      version: 1.0.0
    questionnaire:
      layout: {...}
      item: "'aidbox.sdc/FormItem"
      document-def:
        sdc/rules: {...}
        keys: {...}
      rules: {...}
      title: PHQ2/PHQ9 Depresssion
      document:
        form:
          form: box.sdc.sdc-example/PHQ2PHQ9Form
          version: 1.0.0
      status: new
      form: "'box.sdc.sdc-example/PHQ2PHQ9Form"
      version: 1.0.0
```

### save-step

Save document draft without any validations. Mark step and it's parent sections as 'in-progress'

Only steps in `new`/`in-progress`/`in-amendment` statues can be saved through this RPC

| Param    | Description         | Type        | required? |
| -------- | ------------------- | ----------- | --------- |
| id       | workflow id         | string      | yes       |
| step     | path to nested item | array       | yes       |
| document | document resource   | SDCDocument | yes       |

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/save-step,
params:
  id: wf-1
  step: [phisical-exam]
  document:
    id: doc-1,
    patient: {id: pt-1, resourceType: Patient},
    encounter: {id: enc-1, resourceType: Encounter},
    type: box.sdc.sdc-example/VitalsDocument,
    form: 
      form: box.sdc.sdc-example/VitalsForm,
      version: 1.0.0
    resourceType: SDCDocument,
    loinc-59408-5: {value: 97},
    author: {id: doc-1, resourceType: User},
    loinc-8310-5: {value: 36.6, unit: C},
    loinc-8867-4: {value: 72}
```

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: No such step <[pato to step]> in wf <wf-1>
  message: Forms does not match in the step and given document
  message: Can't update steps of wf in <(completes/canceled)> statuses
  message: Can't update step in <(completed/skipped)> status
  message: Given document has different id than document in step: <doc1> != <doc2>
```

> Success

```
result:
  document:
    id: doc-1,
    patient: {id: pt-1, resourceType: Patient},
    encounter: {id: enc-1, resourceType: Encounter},
    type: box.sdc.sdc-example/VitalsDocument,
    form:
      form: box.sdc.sdc-example/VitalsForm,
      version: 1.0.0
    resourceType: SDCDocument,
    loinc-59408-5: {value: 97},
    author: {id: doc-1, resourceType: User},
    loinc-8310-5: {value: 36.6, unit: C},
    loinc-8867-4: {value: 72}
  updates:
    - [[phisical-exam] {status: "in-progress"}]
```

### complete-step

Try complete step with given document. Call sign on that document and extract data. If everything is ok - mark step as completed. If one of the validations fails - returns error

| Param    | Description                                 | Type        | required? |
| -------- | ------------------------------------------- | ----------- | --------- |
| id       | workflow id                                 | string      | yes       |
| step     | path to nested item                         | array       | yes       |
| document | document resource                           | SDCDocument | yes       |
| dry-run  | Run without saving document and extractions | boolean     | no        |



If workflow step was:

* in `in-progress` status - set `completed` status
* in `in-amendment` status - set `amended` status

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/complete-step,
params:
  id: wf-1
  step: [phisical-exam]
  document:
    id: doc-1,
    patient: {id: pt-1, resourceType: Patient},
    encounter: {id: enc-1, resourceType: Encounter},
    type: box.sdc.sdc-example/VitalsDocument,
    resourceType: SDCDocument,
    loinc-59408-5: {value: 97},
    author: {id: doc-1, resourceType: User},
    loinc-8310-5: {value: 36.6, unit: C},
    loinc-8867-4: {value: 72}
```

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: No such step <[path to step]> in wf <wf-1>
  message: Forms does not match in the step and given document
  message: Can't update steps of wf in <(completes/canceled)> statuses
  message: Can't update step in <(completed/skipped/amended)> status
  message: Given document has different id than document in step: <doc1> != <doc2>
  message: Can't complete step %s in wf %s, due to valdation errors
  message: Can't complete this step because :sdc/enable-when rule failed: ...
  message: Can't complete this step because :sdc/enable-when rule is false
```

```
error:
  message: Can't complete step <[path to step]> in wf <wf-idk>, due to valdation errors
  errors: [{:resourceType "Observation", :export/idx 0}                  ;; failed export resource (with its index in Finalize)
           [{:path [], :message "Property code is required"}             ;; errors with it's location
            {:path [], :message "Property status is required"}]]
```

> Success

```
result:
    updates:
    - [[phisical-exam] {status: "completed"}]
    document: 
      id: doc-1,
      patient: {id: pt-1, resourceType: Patient},
      encounter: {id: enc-1, resourceType: Encounter},
      type: box.sdc.sdc-example/VitalsDocument,
      resourceType: SDCDocument,
      status: "completed"
      loinc-59408-5: {value: 97},
      author: {id: doc-1, resourceType: User},
      loinc-8310-5: {value: 36.6, unit: C},
      loinc-8867-4: {value: 72}
    export: [array of exported objects]
```

### skip-step

Skip step in WF.

| Param  | Description         | Type   | required? |
| ------ | ------------------- | ------ | --------- |
| id     | workflow id         | string | yes       |
| step   | path to nested item | array  | yes       |
| reason | skip reason         | string | yes       |

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/skip-step,
params:
  id: wf-1
  step: [phisical-exam]
  reason: Some reason
```

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: Can't skip step in workflow with status: '<(new/in-progress)>' 
  message: No such step <[path to step]> in wf <wf-1>
  message: Can't skip stateless form step
  message: Can't skip step in status '<(completed/amended/in-amendment)>': <[path to step]>
  message: Something wrong: Can't find document with id : <doc1>
```

> Success

```
result:
    document: {... status: "skipped"}
    updates:
    - [[phisical-exam] {status: "skipped"}]
```

### amend-step

Set `in-amendment` status for step and try to amend document.

| Param   | Description                          | Type   | required? |
| ------- | ------------------------------------ | ------ | --------- |
| id      | workflow id                          | string | yes       |
| step    | path to nested item                  | array  | yes       |
| dry-run | Try without creating resources in DB | string | no        |

> Additionally if workflow was in `completed` status - change it status to `in-amendment` and creates History addendum with timestamp and user

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/amend-step,
params:
  id: wf-1
  step: [phisical-exam]
```

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: Can't amend step in workflow with '<(new/in-progress/canceled)>' status
  message: No such step <[path to step]> in wf <wf-1>
  message: Can't amend stateless form step
  message: Something wrong: Can't find document with id : <doc1>
  message: Can't amend this step because :sdc/enable-when rule failed
  message: Can't amend this step because :sdc/enable-when rule is false
  message: Can't amend step %s in wf %s, due to document amending errors
```

> Success

```
result:
    document: {... status: "in-amendment"}
    updates:
    - [[phisical-exam] {status: "in-amendment"}]
```

### complete-workflow

Complete WF and it's items.

* When all items are already completed/skipped - just complete WF.
* When some items are not completed - try to sign docs for them and complete.
  * By default take docments from DB
  * Also can take documents for signing from parameters - (suitable for Offline mode).

> Finalized step is step in `skipped`, `completed` or `amended` status

| Param   | Description                       | Type    | required? |
| ------- | --------------------------------- | ------- | --------- |
| id      | workflow id                       | string  | yes       |
| items   | documents in items structure      | map     | no        |
| dry-run | Run without saving document in db | boolean | no        |



If workflow was:

* in `in-progress` status - set `completed` status
* in `in-amendment` status - set `amended` status

Additionally creates History addendum for WF with timestamp and user

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/complete-workflow,
params:
  id: wf-1
```

Params with direct document placement

```
POST /rpc?

method: 'aidbox.sdc/complete-workflow,
params:
  id: wf-1
  items: 
    section1:
       phisical-exam: 
           document: {... valid document ...}
           questionnaire: {... valid document ...}
```

> If you supply document through parameters - you need to specify them for the whole WF

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: Can't complete workflow in status: <(completed/canceled)>
  message: Can't complete workflow due to sign errors
```

```
error:
  message: Can't complete workflow due to sign errors,
  errors: [{:message "..." :errors [...]}]
```

> Success

```
result:
    updates:
    - [[] {status: "completed"}]
```

### cancel-workflow

Tries skip all non-finalized steps and mark WF as canceled.

> Finalized step is step in `skipped`, `completed` or `amended` status

| Param   | Description                      | Type    | required? |
| ------- | -------------------------------- | ------- | --------- |
| id      | workflow id                      | string  | yes       |
| reason  | cancel reason                    | string  | yes       |
| dry-run | Run without saving updates in db | boolean | no        |

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/cancel-workflow,
params:
  id: wf-1
  reason: My private reason
```

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: Can't cancel workflow in status: <(completed/canceled)>
  message: Can't skip workflow due to skip-document errors
```

> Success

```
result:
    updates:
    - [[section1 questionnaire] {status: "skipped", skip-reason "My private reason"}]
    - [[section1] {status: "skipped", skip-reason "My private reason"}]
    - [[] {status: "canceled", cancel-reason "My private reason"}]
```

### amend-workflow



Set `in-amendment` status for WF and all `completed`/`amended` steps.

| Param   | Description                      | Type    | required? |
| ------- | -------------------------------- | ------- | --------- |
| id      | workflow id                      | string  | yes       |
| dry-run | Run without saving updates in db | boolean | no        |

> Additionally creates History Addendum with user and timestamp

Returns patches for workflow or error

* patch - pair of `[step, patch-obj]`
* step - path to item in WF (array)
  * \[] - path for wf root
  * \[section1] - path for section in 1st level
  * \[section1 form1] - path for form, located in 2st level (real path in WF object = `{items: {section1: {items: {form1: {.. form ...}}}}}`)
* patch-obj - item object with changed fields (example: `{status: "completed"}`)

Request:

```
POST /rpc?

method: 'aidbox.sdc/amend-workflow,
params:
  id: wf-1
```

Result:

> Error

```
error:
  message: Resource SDCWorkflow/id not found
  message: Can't amend workflow in status: <(new/in-progress/in-amendment/canceled)>
  message: Can't amend workflow due to amend-document errors
    errors:  [...]
```

> Success

```
result:
    updates:
    - [[section1 questionnaire] {status: "in-amendment"}]
    - [[section1] {status: "in-amendment"}]
    - [[] {status: "in-amendment"}]
```

###

### add-workflow-note

Add Addendum Note to the given Workflow. This is the preferred way to add Notes to Workflows.

{% hint style="info" %}
Use this API (`aidbox.sdc/add-workflow-note`) instead of the low-level [Addendum API (`aidbox.sdc.addendum/add-note`).](addendum-api.md#add-note)
{% endhint %}

Params:

| Param | Description                       | Type             | required? |
| ----- | --------------------------------- | ---------------- | --------- |
| id    | workflow id                       | zen/string       | yes       |
| user  | Reference to user which adds note | zenbox/Reference | yes       |
| text  | Addendum note text                | zen/string       | yes       |

Request:

```
POST /rpc?
content-type: text/yaml

method: aidbox.sdc/add-workflow-note
params:
  id: wf-1
  user:
    id: user-1
    resourceType: User
  text: "Some important note on this workflow"
```

Result:

```
result:
  date: '2022-11-11T11:11:10.111Z'
  text: Some important note on this workflow
  type: aidbox.sdc.addendum/Note
  user:
    id: user-1
    resourceType: User
  target:
    id: wf-1
    resourceType: SDCWorkflow
  id: 168d0d5f-d496-46fa-8bac-ccd9f9e335cf
  resourceType: SDCAddendum
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong workflow id is provided.

Request:

```
POST /rpc?

method: aidbox.sdc/add-workflow-note
params:
  id: some-unknown-wf-id
  user:
    id: user-1
    resourceType: User
  text: "Some important note on this workflow"
```

Result:

> Error

```
error:
  message: Resource not found
```



### get-workflow-addendums

Returns collection of addendums for the given workflow.

> This is the preferred way to retrieve Addendums related to a Workflow.

> Use this API (`aidbox.sdc/get-workflow-addendums`) instead of the low-level Addendum API (`aidbox.sdc.addendum/get-addendums`).

Params:

| Param | Description | Type          | required? |
| ----- | ----------- | ------------- | --------- |
| id    | workflow id | zenbox/string | yes       |

Request:

```
POST /rpc?

method: aidbox.sdc/get-workflow-addendums
params:
    id: wf-1
```

Response:

```
result:
  - date: '2022-10-01T12:00:00.000Z'
    meta: ...
    type: aidbox.sdc.addendum/History
    resourceType: SDCAddendum
    snapshot: ...
    status: completed
    id: 039455f7-ed08-4462-90a6-14b5b679d728
    changed: true
    target:
      id: doc-1
      resourceType: SDCDocument
    user: ...
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong id is provided.

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/get-addendums
params:
    id: some-unknown-doc-id
```

Result:

> Error

```
error:
  message: Resource not found
```
