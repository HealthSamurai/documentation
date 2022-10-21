# API reference

* [`get-forms`](api-reference.md#get-forms) - get existed forms
* [`get-form`](api-reference.md#get-form) - get form definition for given form name
* [`launch`](api-reference.md#launch) - launch new form with given params
* [`read-document`](api-reference.md#read-document) - get form with saved document
* [`save`](api-reference.md#save) - save document
* [`sign`](api-reference.md#sign) - finalize document, run extracts
* [`aidbox.sdc/convert-document`](api-reference.md#aidbox.sdc-convert-document) - converts SDCDocument to FHIR QuestionnaireResponse
* [`aidbox.sdc/convert-questionnaire`](api-reference.md#aidbox.sdc-convert-questionnaire)- converts FHIR Questionnaire to Aidbox SDC Form&#x20;
* [`aidbox.sdc/get-form-access-jwt`](api-reference.md#aidbox.sdc-get-form-access-jwt)- creates policy token for form
* ``[`amend`](api-reference.md#amend) - put document to in-amendment state. Used for corrections
* [create-addendum](api-reference.md#create-addendum-wip) - creates custom addendum resource for given source (SDCDocument/SDCWorkflow)
* [add-note](api-reference.md#add-note) - creates addendum Note for SDCDocument/SDCWorkflow&#x20;
* [add-to-history](api-reference.md#add-to-history) - add history addendum for resource for given status. When resource in status `completed/amended` - snapshot can be saved (if resource is differs)
* [create-amendment](api-reference.md#create-amendment) - create addendum resource with type amendment which contains difference between last two signs
*   [add-comment](api-reference.md#add-comment) - add comment for document/workflow with optional path to commented value in resource



### get-forms

Get forms definitions

> Response can be narrowed with document Symbol name substring

params:

| Param | Description                     | Type   | required? |
| ----- | ------------------------------- | ------ | --------- |
| q     | substring of Form symbolic name | String | yes       |

```
POST /rpc?

method: aidbox.sdc.get-forms
params:
    q: 'Vitals'
```

### get-form

Get form form for given document definition

params:

| Param    | Description      | Type   | required? |
| -------- | ---------------- | ------ | --------- |
| document | Form Symbol name | String | yes       |

```
POST /rpc?

method: aidbox.sdc.get-form
params:
    form : aidbox.sdc.VitalsForm
```

### launch

Launch form with given launch, prepoluate data, and return enriched with metadata layout.

| Param         | Description                               | Type    | required? |
| ------------- | ----------------------------------------- | ------- | --------- |
| form          | Form Symbol name                          | String  | yes       |
| dry-run       | Run without saving document in db         | boolean | no        |
| unit-system   | Preffered unit system (default: imperial) | String  | no        |
| rules-in-lisp | Return rules as Lisp or AST               | boolean | no        |
| params        | Params to launch-context                  | map     | no        |

```
POST /rpc?

method: aidbox.sdc.launch
params:
   form: box.sdc.sdc-example/VitalsForm
   dry-run: true
   params:
      encounter-id: enc-1
```

Result:

> Success

```
result:
   form: 'box.sdc.sdc-example/VitalsForm' ;; Form name
   title: "MyForm"                        ;; Form title
   version: 1                             ;; Internal form version.
   document: {}                           ;; SDCDocument resource
   document-def: {}                       ;; Document zen definition (can be used for UI validations)
   layout: {}                             ;; Enriched layout with document metadata and rules. Used for rendering
   layout-def: {}                         ;; Layout zen definition. (can be used for retrieving additional info from layout engine)
   rules: {}                              ;; merged (and optionally transformed) :sdc/rules from document and layout definitions
   rules-order: {}                        ;; Rules keys in topological sort.
   finalize-profile: {}                   ;; Zen Schema for Finalize Constraints
```

Error:

```
error:
    message:  "Wrong population logic for resource defined"
    errors:   [{message: "..."}] ;; schema validation errors  (zen-style errors)
    warnings:                    ;; failed population logic expressions
     - message: "..."
       launch-ctx {...}          ;; context data of the launch process
       localion:
         expr: <lisp/expr>       ;; failed lisp/expr
```

####



### read-document

Get form for saved document.

| Param         | Description                 | Type    | required? |
| ------------- | --------------------------- | ------- | --------- |
| id            | Document id                 | String  | yes       |
| rules-in-lisp | Return rules as Lisp or AST | boolean | no        |

```
POST /rpc?

method: aidbox.sdc.read-document
params:
   id: doc-1
```

Result:

> Success

```
result:
   form: 'box.sdc.sdc-example/VitalsForm' ;; Form name
   title: "MyForm"                        ;; Form title
   version: 1                             ;; Internal form version.
   document: {}                           ;; SDCDocument resource
   document-def: {}                       ;; Document zen definition (can be used for UI validations)
   layout: {}                             ;; Enriched layout with document metadata and rules. Used for rendering
   layout-def: {}                         ;; Layout zen definition. (can be used for retrieving additional info from layout engine)
   rules: {}                              ;; merged (and optionally transformed) :sdc/rules from document and layout definitions
   rules-order: {}                        ;; Rules keys in topological sort.
   finalize-profile: {}                   ;; Zen Schema for Finalize Constraints
```

Error:

```
error:
    :message  "Can't find document"
```

### save

Save document draft without Finalize Profile validations. Can be used with documents in `draft`, `in-progress`, `in-amendment` statuses

| Param    | Description       | Type        | required? |
| -------- | ----------------- | ----------- | --------- |
| document | document resource | SDCDocument | yes       |

If document in `draft` status - set `in-progress` status to it.

Request:

```
POST /rpc?

method: 'aidbox.sdc/save,
params:
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
  message: Can't be saved in final status: <(completed/canceled/amended)>
```

> Success

```
result:
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

####

### sign

Validates document and run extractions on it. Mark document as completed.

Can be used wiht Documents in `draft`, `in-progress`, `in-amendment` statuses.

| Param    | Description                                 | Type        | required? |
| -------- | ------------------------------------------- | ----------- | --------- |
| document | document resource                           | SDCDocument | yes       |
| dry-run  | Run without saving document and extractions | boolean     | no        |



Apply `created`/`patched`/`deleted` extractions. Create `Provenance` resource with links to `SDCDocument` and created resources(`created` extractions).

> Additionally will create History SDCAddendum resource with timestamp user and document snapshot.

> Additionally when called on document with`in-amendment` status it will
>
> * delete previously created extractions, Provenance resource, and recreate them with new data.
> * create Amendment SDCAddendum resource with timestamp, user, and diff between last two snapshots from History addendums.

```
POST /rpc?

method: 'aidbox.sdc/sign,
params:
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

Response:

> Success

```
result
   document: {...}   ;; signed document
   exported:         ;; exported resources (optional)
     to-create: [...]
     to-update: [...]
     to-delete: [...]
```

> Error

```
error:
  message: Document didn't pass sign validations
  type: :sdc.error/validation
  errors:
    - message: More detailsed validation message
  ...
  message: Some of the exported documents are invalid
  type: :sdc.error/export
  errors:
    to-create:
      - resource: {...} ;; generated resource by export
        export/idx: 0   ;; export position in export list
        errors: {...}   ;; zen-style  validation errors
    to-delete:
      - resource: {...} ;; generated resource by export
        export/idx: 0   ;; export position in export list
        errors: {...}   ;; zen-style  validation errors
    to-update:
      - resource: {...} ;; generated resource by export
        export/idx: 0   ;; export position in export list
        errors: {...}   ;; zen-style  validation errors
```

### aidbox.sdc/convert-questionnaire

Converts Questionnaire to Aidbox SDC Form (Document + Form + Launch + (Finalize))

params:

| Param                   | Description                                | Type     | required? |
| ----------------------- | ------------------------------------------ | -------- | --------- |
| url                     | Link to Questinnaire on public FHIR server | String   | no        |
| resource                | Questionnaire resource body                | Resource | no        |
| options                 | Additional options                         | Map      | no        |
| optinos.gen-extractions | Generate basic extractions for fields      | Boolean  | no        |

Request:

```
POST /rpc

method: aidbox.sdc/convert-questionnaire
params:
  options:
    gen-extractions: true
  resource:
    resourceType: Questionnaire
    id: 90854-1
    url: http://loinc.org/q/90854-1
    name: Duke_Anxiety_Depression_Scale
    title: Duke Anxiety Depression Scale
    status: draft
    code:
    - system: http://loinc.org
      code: 90854-1
      display: Duke Anxiety Depression Scale
    item:
    - linkId: '107153'
      text: Final score
      type: decimal
```

Result:

> Success

```
result:
  ns: duke-anxiety-depression-scale
  filename: duke-anxiety-depression-scale.edn
  code: |
    {ns duke-anxiety-depression-scale,
     import #{aidbox.sdc zenbox fhir hl7-fhir-r4-core.Coding},
     DukeAnxietyDepressionScaleDocument
     {:zen/tags #{zen/schema aidbox.sdc/doc aidbox.sdc/rules},
      :zen/desc "Duke Anxiety Depression Scale",
      :type zen/map,
      :confirms #{aidbox.sdc/Document},
      :keys
      {:loinc-107153
       {:linkId "/107153",
        :text "Final score",
        :zen/desc "Final score",
        :type zen/number,
        :sdc-type aidbox.sdc/decimal}},
      :source {:code "90854-1", :system "http://loinc.org"}},
     DukeAnxietyDepressionScaleLayout
     {:zen/tags #{aidbox.sdc/Layout aidbox.sdc/rules},
      :document DukeAnxietyDepressionScaleDocument,
      :engine aidbox.sdc/Hiccup,
      :layout {:type aidbox.sdc/col, :children [{:bind [:loinc-107153]}]}},
     DukeAnxietyDepressionScaleLaunch
     {:zen/tags #{aidbox.sdc/Launch},
      :document DukeAnxietyDepressionScaleDocument,
      :params {:encounter-id {:type zen/string}},
      :populate-engine aidbox.sdc/LispPopulate,
      :populate
      {:author (lisp/get-in [:ctx :user]),
       :encounter
       {:id (lisp/get-in [:params :encounter-id]),
        :resourceType "Encounter"},
       :patient
       (lisp/sql
        {:select [:#> :resource [:subject]],
         :from :Encounter,
         :where [:= :id (lisp/get-in [:params :encounter-id])]})}},
     DukeAnxietyDepressionScaleFinalizeConstraints
     {:type zen/map, :zen/tags #{zen/schema}},
     DukeAnxietyDepressionScaleFinalize
     {:zen/tags #{aidbox.sdc/Finalize zen/schema},
      :document DukeAnxietyDepressionScaleDocument,
      :profile DukeAnxietyDepressionScaleFinalizeConstraints,
      :export-engine aidbox.sdc/LispExport,
      :create
      [(lisp/when
        (lisp/get :loinc-107153)
        {:resourceType "Observation",
         :subject (lisp/get :patient),
         :encounter (lisp/get :encounter),
         :status "final",
         :code {:coding [{:code "107153"}]},
         :value {:integer (lisp/get :loinc-107153)}})]},
     DukeAnxietyDepressionScaleForm
     {:zen/tags #{aidbox.sdc/Form},
      :title "Duke Anxiety Depression Scale",
      :document DukeAnxietyDepressionScaleDocument,
      :layout DukeAnxietyDepressionScaleLayout,
      :launch DukeAnxietyDepressionScaleLaunch,
      :finalize DukeAnxietyDepressionScaleFinalize}}
```

### aidbox.sdc/convert-document

Converts SDCDocument to QuestionnaireResponse

Also you may choose the output format:

* aidbox - to store inside Aidbox DB
* fhir - to exchange with other FHIR systems

params:

| Param    | Description               | Type              | required? |
| -------- | ------------------------- | ----------------- | --------- |
| id       | id of SDCDocument in DB   | String            | no        |
| document | SDCDocument resource body | Resource          | no        |
| format   | Output format             | "fhir" / "aidbox" | yes       |

Request:

> Example with document stored in DB

```
POST /rpc

method: aidbox.sdc/convert-document
params:
  id: doc-1
  format: fhir
```

> Example with SDCDocument resource body

```
POST /rpc

method: aidbox.sdc/convert-document
params:
  document:
    patient:
      id: pt-1
      resourceType: Patient
    encounter:
      id: enc-1
      resourceType: Encounter
    type: box.sdc.sdc-example/VitalsDocument
    resourceType: SDCDocument
    author:
      id: u-1
      resourceType: User
    status: in-progress
    id: doc-1
    blood-pressure:
      - numbers:
          loinc-8480-6:
            value: 100
          loinc-8462-4:
            value: 130
        loinc-41904-4:
          text: Biceps left
          code: LA11158-5
          system: 'http://loinc.org'
      - numbers:
          loinc-8480-6:
            value: 110
          loinc-8462-4:
            value: 130
        loinc-41904-4:
          text: Biceps right
          code: LA11159-3
          system: 'http://loinc.org'
    form:
      form: box.sdc.sdc-example/VitalsForm
    loinc-8867-4:
      value: 75
      unit: kg
  format: fhir
```

Result:

> Success

```
result:
  resourceType: QuestionnaireResponse
  status: in-progress
  questionnaire: http://loinc.org/85353-1
  encounter:
    id: enc-1
    resourceType: Encounter
  item:
  - linkId: "/blood-pressure"
    text: Blood pressure
    item:
    - linkId: "/numbers"
      text: Numbers
      item:
      - linkId: "/8480-6"
        text: BP sys
        answer:
        - valueQuantity:
            value: 100
      - linkId: "/8462-4"
        text: BP dias
        answer:
        - valueQuantity:
            value: 130
    - linkId: "/41904-4"
      text: BP measurement site
      answer:
      - valueCoding:
          system: http://loinc.org
          code: LA11158-5
  - linkId: "/blood-pressure"
    item:
    - item:
      - answer:
        - valueQuantity:
            value: 110
      - answer:
        - valueQuantity:
            value: 130
    - answer:
      - valueCoding:
          code: LA11159-3
  - linkId: "/8867-4"
    answer:
    - valueQuantity:
        value: 75
        unit: kg
```

### aidbox.sdc/get-form-access-jwt

Creates policy token to get access to SDCDocument/SDCWorkflow

params:

| Param             | Description                | Type                    | required? |
| ----------------- | -------------------------- | ----------------------- | --------- |
| form              | link to the form in DB     | Map                     | yes       |
| form.id           | SDCDocument/SDCWorkflow id | String                  | yes       |
| form.resourceType |                            | SDCDocument/SDCWorkflow | yes       |

Request:

> Example with document

```javascript
POST {{base}}/rpc

method: aidbox.sdc/get-form-access-jwt
params:
  form:
    id: doc-1
	resourceType: SDCDocument
```

Result:

```javascript
result:
  token: <jwt policy token>
```

Usage:

With this token you can get access to the document via RPC without authorization in Aidbox

```javascript
POST {{base}}/rpc

method: aidbox.sdc/read-document
params:
  id: doc-1

policy: <jwt policy token>
```

### amend

Set `in-amendment` status for Document. Used for document corrections. Should be signed after correction

Can be used only for documents in `completed`, `amended` statuses.

| Param   | Description                                 | Type    | required? |
| ------- | ------------------------------------------- | ------- | --------- |
| id      | document id                                 | string  | yes       |
| dry-run | Run without saving document and extractions | boolean | no        |

> Additionally will create History SDCAddendum resource with timestamp and user.

Request:

```
POST /rpc?

method: 'aidbox.sdc/amend,
params:
  id: doc-1,
```

Response:

> Success

```
result: {...} ;; updated document (the same if it is already in in-amendment status)
```

> Error

```
error:
  message: Resource is not found
  message: Can't be amended not in final status (completed/amended): <cur-status>
  ...
```

### create-addendum (WIP)

Creates custom addendum resource for given source (SDCDocument/SDCWorkflow)

> Extensible via ZEN

Params:

| Param    | Description       | Type                         | required? |
| -------- | ----------------- | ---------------------------- | --------- |
| addendum | Addendum resource | SDCAddendum (without 'date') | yes       |

Should be used only for custom created(by user) SDCAddendum resources

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/create-addendum
params:
  addendum:
    type: aidbox.sdc.addendum/Note
    target: 
      id: doc-1
      resourceType: SDCDocument
    user:
      id: user-1
      resourceType: User
    appointment: "2022-10-10T10:10:10.000Z"
    text: "Temperature measuarements are not correct. Should be 100"
```

Response:

```
result:
  type: aidbox.sdc.addendum/Note
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  appointment: "2022-10-10T10:10:10.000Z"
  text: "Temperature measuarements are not correct. Should be 100"
```

### add-note

Creates addendum Note for SDCDocument/SDCWorkflow

Params:

| Param  | Description                       | Type             | required? |
| ------ | --------------------------------- | ---------------- | --------- |
| target | reference to target resource      | zenbox/Reference | yes       |
| user   | reference to user which adds note | zenbox/Reference | yes       |
| text   | reference to target resource      | zen/string       | yes       |

Should be used for creating Note addendum for SDCDocument/SDCWorkflow

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/add-note
params:
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  text: "Temperature measuarements are not correct. Should be 100"
```

Response:

```
result:
  type: aidbox.sdc.addendum/Note
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  appointment: "2022-10-10T10:10:10.000Z"
  text: "Temperature measuarements are not correct. Should be 100"
```

### add-to-history

Add history addendum for resource for given status. When resource in status `completed/amended`- snapshot can be saved (if resource is differs)

Params:

| Param    | Description                       | Type                    | required? |
| -------- | --------------------------------- | ----------------------- | --------- |
| resource | resource body                     | SDCDocument/SDCWorkflow | yes       |
| user     | reference to user which adds note | zenbox/Reference        | yes       |

Should be used for storing status changes for SDCDocument/SDCWorkflow.

> When status is `completed`/`amended` - try find latest `complete`/`amended` History resource with snapshot and checks is resource different - if so - ads snapshot field to new History addendum

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/add-to-history
params:
  resource: 
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
  user:
    id: user-1
    resourceType: User
```

Response:

```
result:
  type: aidbox.sdc.addendum/History
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  changed: true
  snapshot: 
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
```

### create-amendment

Finds History addendums for given target and try generate Amendment report - which contains difference between 2 latest snapshots of that resource.

Params:

| Param  | Description                                  | Type                    | required? |
| ------ | -------------------------------------------- | ----------------------- | --------- |
| target | reference to resource with History addendums | SDCDocument/SDCWorkflow | yes       |
| user   | reference to user which adds note            | zenbox/Reference        | yes       |

Should be used for compute diff for two latest History snapshots of SDCDocument/SDCWorkflow and created Addendum resource for it.

> If there are less than 2 History addendums with `completed`/`ammended` status and with snapshot - Amendment should not be created.

Difference is array of diff objects

Diff object is a map of keys

| key  | type                  |                          |
| ---- | --------------------- | ------------------------ |
| type | change operation type | "remove"/"replace"/"add" |
| path | path of change        | vector of strings        |
| old  | old value (if exists) | zen/string? (optional)   |
| new  | new value (if exitts) | zen/string (optional)    |

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/create-amendment
params:
  target: 
     id: doc-1,
     resourceType: SDCDocument,
  user:
    id: user-1
    resourceType: User
```

Response:

```
result:
  type: aidbox.sdc.addendum/Amendment
  id: a-1
  date: "2022-12-12T10:10:10.000Z"
  user:
    id: user-1
    resourceType: User
  target: 
    id: doc-1
    resourceType: SDCDocument
  old-revision:
    id: "a-1"
    resourceType: "SDCAddendum",
  new-revision: 
    id "a-2"
    resourceType: "SDCAddendum",
  diff: 
    - type: remove
      path: ["loinc-8867-4" "value"]
      old 72
    - type: add
      path: ["loinc-59408-5" "value"]
      new: 41
    - type: replace
      path: ["loinc-8310-5" "value"]
      old: 90
      new: 97
```

### add-comment

Add comment for document/workflow with optional path to commented value in resource

Params:

| Param  | Description                         | Type              | required? |
| ------ | ----------------------------------- | ----------------- | --------- |
| target | reference to target resource        | zenbox/Reference  | yes       |
| user   | reference to user which adds note   | zenbox/Reference  | yes       |
| path   | path to commented value in resource | vector of strings | no        |
| text   | reference to target resource        | zen/string        | yes       |

Should be used for creating Comment addendum for SDCDocument/SDCWorkflow

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/add-comment
params:
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  path: ["loinc-59408-5" "value"]
  text: "Temperature measuarements are not correct. Should be 100"
```

Response:

```
result:
  type: aidbox.sdc.addendum/Comment
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  path: ["loinc-59408-5" "value"]
  text: "Temperature measuarements are not correct. Should be 100"
```

> It's looks like `Comment` similar to `Note` - but semantically they are different. Comment should not releated to status of `SDCDocument`/`SDCWorkflow` and used for informal conversations, which doesn't have any legal force. `Note` should be used for `SDCDocument`/`SDCWorkflow` in `completed`/`amended` statuses.

## Workflow RPC-API

> Workflow API wraps Form API and when you use WF you don't need to use Form API directly .

* [get-workflows](api-reference.md#get-workflows) - get existed workflows
* [get-workflow](api-reference.md#get-workflow) - get workflow definition
* [start-workflow](api-reference.md#start-workflow) - start WF and launch forms
* [save-step](api-reference.md#save-step) - save document through WF, mark step as in-progress
* [skip-step](api-reference.md#skip-step) - skip WF step
* [amend-step](api-reference.md#undefined-1) - amend WF step
* [complete-step](api-reference.md#complete-step) - try complete WF step with document, call sign on it.
* [complete-workflow](api-reference.md#complete-workflow) - complete WF
* [cancel-workflow](api-reference.md#cancel-workflow) - try cancel WF
* [amend-workflow](api-reference.md#amend-workflow) - amend completed WF

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
