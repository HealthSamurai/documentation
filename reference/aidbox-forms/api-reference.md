# Form API

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
* `add-note` - add note as addendum to the given document







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

Creates [policy token](../../security-and-access-control-1/security/access-policy.md#signed-rpc-policy-token) to get access to SDCDocument/SDCWorkflow

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

With this token you can get access to the document via RPC (read-document, save, sign, amend) without authorization in Aidbox

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

### add-note

Add Addendum Note to the given Document. This is the preferred way to add Notes to Documents.

{% hint style="warning" %}
Use this API (`aidbox.sdc/add-note`) instead of the low-level [Addendum API (`aidbox.sdc.addendum/add-note`)](addendum-api.md#add-note)
{% endhint %}

Params:

| Param | Description                       | Type             | required? |
| ----- | --------------------------------- | ---------------- | --------- |
| id    | Document id                       | zen/string       | yes       |
| user  | Reference to user which adds note | zenbox/Reference | yes       |
| text  | Addendum note text                | zen/string       | yes       |

Request:

```
POST /rpc?

method: aidbox.sdc/add-note
params:
  id: doc-1
  user:
    id: user-1
    resourceType: User
  text: "Temperature measurements are not correct. Should be 100"
```

Result:

> Success

```
result:
  date: '2022-11-11T11:11:10.111Z'
  text: Temperature measurements are not correct. Should be 100
  type: aidbox.sdc.addendum/Note
  user:
    id: user-1
    resourceType: User
  target:
    id: doc-1
    resourceType: SDCDocument
  id: f3989be1-9e6d-4e9e-a9bf-7b52956ea432
  resourceType: SDCAddendum
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong document id is provided.

```
POST /rpc?

method: aidbox.sdc/add-note
params:
  id: some-unknown-document-id
  user:
    id: user-1
    resourceType: User
  text: "Temperature measurements are not correct. Should be 100"
```

Result:

> Error

```
error:
  message: Resource not found

```
