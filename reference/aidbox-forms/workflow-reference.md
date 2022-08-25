# Workflow reference

### Workflow definition

```
 DemoWF3
 ;; Tag to mark entity as Workflow
 {:zen/tags #{aidbox.sdc/Workflow} 
  ;; title of the WF
  :title "Demo workflow"
  ;; items consists of sections/forms in any level deep
           ;; section key can be arbitrary keyword
  :items {:section1
                    ;; section definition with 2 forms
                    {:item aidbox.sdc/SectionItem
                     :title "My custom section 1"
                     :items {:phisical-exam 
                                            ;; form item definition.
                                            {:item aidbox.sdc/FormItem
                                             :form VitalsForm}
                             :questionnaire  {:item aidbox.sdc/FormItem
                                              :form PHQ2PHQ9Form}}}}}
```

When WF is started - it will be populated with items `:order` information, launched forms, document's references and state statuses.

```
 DemoWF3
 ;; Tag to mark entity as Workflow
 {:zen/tags #{aidbox.sdc/Workflow} 
  :title "Demo workflow"
  :order [:section1]
  :status "new"
  :items {:section1 {:item aidbox.sdc/SectionItem
                     :title "My custom section 1"
                     :status "new"
                     :order [:phisical-exam :questionnaire]
                     :items {:phisical-exam {:item aidbox.sdc/FormItem
                                             :form myforms/Demographic
                                             :title "Demographic"
                                             :layout {... layout DSL}
                                             :rules {... rules ...}
                                             :document {id: doc-id :resourceType}
                                             :status "new"}
                             :questionnaire  {... launched form ...}}}}}
```

> Also see _Optional features_ section for some workflow payload customization.

#### Section

Section item is item with type aidbox.sdc/SectionItem`It contains`title`and children`items\`. These fields are mandatory

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :section1 {:item aidbox.sdc/SectionItem
                    :title "My custom Section"
                    :items {:form1 {:item aidbox.sdc/FormItem
                                    :form myforms/Demographic}}}}}
```

When workflow is started - all `SectionItems` will be populated with meta information of items order.

> Order generated from `:items` natural placement in ZEN file.

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :order [:form1 :section1]
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :section1 {:item aidbox.sdc/SectionItem
                    :title "My custom Section"
                    :order [:form1]
                    :items {:form1 {:item aidbox.sdc/FormItem
                                    :form myforms/Demographic}}}}}
```

#### Form

FormItem is item with type `aidbox.sdc/FormItem` It contains only reference to existed `form`. This field is mandatory

```
{:item aidbox.sdc/FormItem
 :form myforms/Demographic}
```

> Zen validates reference - it can refer to symbols with `aidbox.sdc/Form` tag only.

When workflow is started - all `FormItems` will be populated with `Form` definition, Launched `Layout`, form+document rules and reference to created `Document`.

```
{:item aidbox.sdc/FormItem
 :form myforms/Demographic
 :title "Demographic"
 :layout {... layout DSL}
 :rules {... rules ...}
 :document {id: doc-id :resourceType}}
```

**Form rule keys**

FormItem also supports special rule based keys: `sdc/inject`, `sdc/enable-when`

`:sdc/inject` used for injecting values to form context.

* This rule is client side rule. You don't have access to DB.
* This rule is invoked on each time when form is focused.

Value of the `:sdc/inject` key should be rules map - where key should match expected key by form's rules, and value is a lisp expression. Lisp expressions are invoked in the context of workflow state.

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :order [:form1 :form2]
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :form2 {:item aidbox.sdc/FormItem
                 :sdc/inject {:vitals/bmi (lisp/get-in [:items :form1 :document :bmi])}
                 :form myforms/Demographic}}}

;; myforms namespace
DemographicDocument
{:zen/tags #{aidbox.sdc/doc}
 :sdc/rules {:external-bmi (lisp/get :vitals/bmi)}
 ...}
 
Demographic
{:zen/tags #{aidbox.sdc/Form}
 :document DemographicDocument 
 ...}
```

`:sdc/enable-when` used in situations when you have optional forms.

* The rule is used on the client to show/hide the form
* The rule is used on the server
  * to launch forms without storing SDCDocuments in DB.
  * to validate form optionality on the complete-workflow/complete-step rpcs

Value of the key `:sdc/enable-when` should be boolean lisp expression. Lisp expression is invoked in the context of workflow state.

```
{:item aidbox.sdc/SectionItem
 :title "My custom Section"
 :order [:form1 :form2]
 :items {:form1 {:item aidbox.sdc/FormItem
                 :form myforms./VitalsForm}
         :form2 {:item aidbox.sdc/FormItem
                 :sdc/enable-when (> (lisp/get-in [:items :form1 :document :bmi]) 5)
                 :form myforms/Demographic}}}
```

### Workflow RPC-API

> Workflow API wraps Form API and when you use WF you don't need to use Form API directly .

* [get-workflows](workflow-reference.md#get-workflows) - get existed workflows
* [get-workflow](workflow-reference.md#get-workflow) - get workflow definition
* [start-workflow](workflow-reference.md#start-workflow) - start WF and launch forms
* [save-step](workflow-reference.md#save-step) - save document through WF, mark step as in-progress
* [skip-step](workflow-reference.md#skip-step) - skip WF step
* [complete-step](workflow-reference.md#complete-step) - try complete WF step with document, call sign on it.
* [complete-workflow](workflow-reference.md#complete-workflow) - complete WF
* [cancel-workflow](workflow-reference.md#cancel-workflow) - try cancel WF

#### get-workflows

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

#### get-workflow

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

#### start-workflow

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

#### save-step

Save document draft without any validations. Mark step and it's parent sections as 'in-progress'

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
    updates:
    - [[phisical-exam] {status: "in-progress"}]
```

#### complete-step

Try complete step with given document. Call sign on that document and extracts data. If everything is ok - mark step as completed. If one of the validations fails - returns error

| Param    | Description                                 | Type        | required? |
| -------- | ------------------------------------------- | ----------- | --------- |
| id       | workflow id                                 | string      | yes       |
| step     | path to nested item                         | array       | yes       |
| document | document resource                           | SDCDocument | yes       |
| dry-run  | Run without saving document and extractions | boolean     | no        |

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
  message: Can't update step in <(completed/skipped)> status
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

#### skip-step

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
  message: No such step <[path to step]> in wf <wf-1>
  message: Can't skip completed step: <[path to step]>
  message: Can't skip stateless form step
  message: Something wrong: Can't find document with id : <doc1>
```

> Success

```
result:
    updates:
    - [[phisical-exam] {status: "completed"}]
```

#### complete-workflow

Complete WF and it's items.

* When all items are already completed/skipped - just complete WF.
* When some items are not completed - try to sign docs for them and complete.
  * By default take docments from DB
  * Also can take documents for signing from parameters - (suitable for Offline mode).

> Finalized step is step in `skipped` or `completed` status

| Param   | Description                       | Type    | required? |
| ------- | --------------------------------- | ------- | --------- |
| id      | workflow id                       | string  | yes       |
| items   | documents in items structure      | map     | no        |
| dry-run | Run without saving document in db | boolean | no        |

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

#### cancel-workflow

Tries skip all non-finalized steps and mark WF as canceled.

> Finalized step is step in `skipped` or `completed` status

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

### Basic WF usage scenarios

Assume we have such WF with one section and two forms

```
 DemoWF
 {:zen/tags #{aidbox.sdc/Workflow}
  :title "Demo workflow"
  :version "0.0.1"
  :items {:section1 {:item aidbox.sdc/SectionItem
                     :title "My custom section 1"
                     :items {:phisical-exam {:item aidbox.sdc/FormItem
                                             :form VitalsForm}
                             :questionnaire  {:item aidbox.sdc/FormItem
                                             :form PHQ2PHQ9Form}}}}}
```

From the start (after WF start) we are working with form-items directly via step functions (step-save/step-complete/step-skip) When form-items pushed to some final state (completed/skipped) - sections will complete/skip automatically. And as final action - you need to complete/cancel workflow.

### Fast forward scenario

[![Fast forward scenario](https://camo.githubusercontent.com/2b274346d1eeafa418f5a32a1b9f57486ee13e33a3880bd34ca762ec8f61755d/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6e5044445169443033384e7446654d775173627a574c31414c6876316f5351524f496e78316b5064363664677a6c51444b624347586a30694964516f7a467055766d532d34666c41424f526b574872365845304e6e7169724e7a71784f4f4e2d334b78333049776756667378716f3268385454755736315169677756706d2d6f562d73745651624f536364314334766d6b4f674c74434e6768356d7a6b787341544142766d39754f3270704132354573424d425939754d574a576759624b635356752d4165485973336162595855547666724f654469534961377a6d54426e56716a5f594e794f342d35626f624d4c356f67725166456a474f786835506256536c6e477266646a726a5271593851506e5a65517a314b42424659565153634c4d5f3356736678765764517a4b7148716e4a56425256304330)](https://camo.githubusercontent.com/2b274346d1eeafa418f5a32a1b9f57486ee13e33a3880bd34ca762ec8f61755d/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6e5044445169443033384e7446654d775173627a574c31414c6876316f5351524f496e78316b5064363664677a6c51444b624347586a30694964516f7a467055766d532d34666c41424f526b574872365845304e6e7169724e7a71784f4f4e2d334b78333049776756667378716f3268385454755736315169677756706d2d6f562d73745651624f536364314334766d6b4f674c74434e6768356d7a6b787341544142766d39754f3270704132354573424d425939754d574a576759624b635356752d4165485973336162595855547666724f654469534961377a6d54426e56716a5f594e794f342d35626f624d4c356f67725166456a474f786835506256536c6e477266646a726a5271593851506e5a65517a314b42424659565153634c4d5f3356736678765764517a4b7148716e4a56425256304330)

### Fast skip scenario

[![Fast skip scenario](https://camo.githubusercontent.com/e19ba76d9bd76df635d12beb32a839021ece740e022c94b17ad4a1e52a678702/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6850354452694b5733384a746443387a30315475676767746f5773676a74523132495a794d363234556c6b36664167514c6a637a394a455a2d4752764e574d426a315a4b597059413143346c4a665132642d677a4b57377a31655f6631646a417a72617a665935446b534752313670394533726c5a77365f77632d5432743763356233386d6b7745676b3646675974506c525a5f674e656368667672354d4230347048594d533563716f386230516e57433161415034794a565741444247684c6c6d694f6e4a59773864757957705a435775544453614a6e5061556d4a6d6a4b436b39706b6a6872557a50317170734e544777764e5f74614a6963454377656c)](https://camo.githubusercontent.com/e19ba76d9bd76df635d12beb32a839021ece740e022c94b17ad4a1e52a678702/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f706e672f6850354452694b5733384a746443387a30315475676767746f5773676a74523132495a794d363234556c6b36664167514c6a637a394a455a2d4752764e574d426a315a4b597059413143346c4a665132642d677a4b57377a31655f6631646a417a72617a665935446b534752313670394533726c5a77365f77632d5432743763356233386d6b7745676b3646675974506c525a5f674e656368667672354d4230347048594d533563716f386230516e57433161415034794a565741444247684c6c6d694f6e4a59773864757957705a435775544453614a6e5061556d4a6d6a4b436b39706b6a6872557a50317170734e544777764e5f74614a6963454377656c)

### Optional features

Workflow support 2 additional features

* versioning
* section id generation

These features you can configure via api-construtor in zen-project ([https://docs.aidbox.app/aidbox-configuration/aidbox-api-constructor](https://docs.aidbox.app/aidbox-configuration/aidbox-api-constructor))

You need to configure your `aidbox/system` with `sdc-service` and it's configuration

> Your zen-project entrypoint namespace

`aidbox/system` with `sdc-service`

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:sdc sdc-service}}
```

`sdc-service` configuration

```
 sdc-service
 ;; bind your sdc/service with engine `aidbox.sdc/service`
 {:zen/tags         #{aidbox/service}
  :engine           aidbox.sdc/service
  ;; Enable Form/Workflow versioning
  :versioning       {:enabled false}
  ;; Enable WF items id generation
  :wf-items-ids-gen {:enabled true}}
```

### Versioning

When `:versioning` feature is enabled - all workflows and forms definitions will be snapshotted to DB.

Version is positive natural number.

> Started from version=1

Versioning is automatic and based on hashing essential fields of definitions. If some essential field of form/wf is changed - created a new version and snapshotted to DB.

* Workflow essential fields are:
  * title
  * order
  * items,
  * form reference.
* Form essential fields are:
  * title
  * inlined document definition
  * inlined layout definition with resolved sub-forms
  * inlined finalize definition.
  * inlined finalize-profile definition.

WF versions just store workflow contents and no information of form istelf, but form fererence (zen symbol).

WF and Form snapshots weakly coupled. When WF is started and forms are launched - **documents** will be created and they contains **reference to particular form-version**.

> Weakly coupling of form<->wf versions adds ability to avoid version generation chain reaction.

### Section id generation

When `:wf-items-ids-gen` feature is enabled - each section will receive `:id` property. That id returned on `start-workflow`, `read-workflow` rpc payloads, and don't affect WF storage.

Id is generated from item path (path from WF root to item itself).

Example:

This WF definition

```
 {:zen/tags #{aidbox.sdc/Workflow} 
  :title "My Workflow"
  :items {:section1 {:item aidbox.sdc/SectionItem
                     :items {:phisical-exam {:item aidbox.sdc/FormItem
                                             :form VitalsForm}
                             :questionnaire  {:item aidbox.sdc/FormItem
                                              :form PHQ2PHQ9Form}}}}}
```

on `start-workflow`/`read-workflow` rpcs call - adds items :id information to payload

```
 {:title "My Workflow"
  :items {:section1 {:id "section1"
                     :items {:phisical-exam {... :id "section1.phisical-exam" ...}
                             :questionnaire {... :id "section1.questionnaire" ...}}
                     ...}}}
```

##

\
