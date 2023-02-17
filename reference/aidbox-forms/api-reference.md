# Form API

## Form API

* [`aidbox.sdc/convert-questionnaire`](api-reference.md#convert-questionnaire)- converts FHIR Questionnaire to Aidbox SDC Form
* [`aidbox.sdc/convert-form`](api-reference.md#convert-form) - converts Aidbox Form to FHIR Questionnaire
* [`aidbox.sdc/convert-forms`](api-reference.md#convert-forms) - converts Aidbox Forms to FHIR Questionnaire resources
* [`aidbox.sdc/generate-form-template`](api-reference.md#generate-form-template) - genereate form from scratch 
* [`aidbox.sdc/generate-form-layout`](api-reference.md#generate-form-layout) - generate form layout
* [`aidbox.sdc/generate-form-constraints`](api-reference.md#generate-form-constraints) - generate constraints schema
* [`aidbox.sdc/generate-form-finalize`](api-reference.md#generate-form-finalize) - generate finalize with extractions
* [`aidbox.sdc/get-forms`](api-reference.md#get-forms) - get existed forms
* [`aidbox.sdc/get-form`](api-reference.md#get-form) - get form definition for given form name
* [`aidbox.sdc/launch`](api-reference.md#launch) - launch new form with given params

## Document API

* [`aidbox.sdc/read-document`](api-reference.md#read-document) - get form with saved document
* [`aidbox.sdc/save`](api-reference.md#save) - save document
* [`aidbox.sdc/sign`](api-reference.md#sign) - finalize document, run extracts
* [`aidbox.sdc/convert-document`](api-reference.md#aidbox.sdc-convert-document) - converts SDCDocument to FHIR QuestionnaireResponse
* [`aidbox.sdc/get-form-access-jwt`](api-reference.md#aidbox.sdc-get-form-access-jwt)- creates policy token for form
* [`aidbox.sdc/generate-form-link`](api-reference.md#aidbox.sdc-generate-form-link) - creates shared form link
* [`aidbox.sdc/amend`](api-reference.md#amend) - put document to in-amendment state. Used for corrections
* [`aidbox.sdc/add-note`](api-reference.md#add-note) - add note as addendum to the given document

###

### convert-questionnaire

Converts Questionnaire to Aidbox SDC Form (Document + Form + Launch + (Finalize))

params:

| Param                   | Description                                             | Type                                         | required? |
|-------------------------|---------------------------------------------------------|----------------------------------------------|-----------|
| url                     | Link to Questinnaire on public FHIR server              | zen/string                                   | no        |
| resource                | Questionnaire resource body                             | zenbox/resource                              | no        |
| options                 | Additional options                                      | map                                          | no        |
| optinos.gen-extractions | Generate basic extractions for fields                   | boolean                                      | no        |
| optinos.keygen-strategy | Strategy for document keys generation                   | "text"/"numbers"/"link-id"/"code-or-link-id" | no        |
| optinos.base-name       | Name prefix for all generated zen-symbols and namespace | string                                       | no        |
| options.key-prefix      | Prefix for document keys                                | zen/string                                   | no        |
  

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

```
result:
  ns: duke-anxiety-depression-scale
  filename: duke-anxiety-depression-scale.edn
  code: |
    {ns duke-anxiety-depression-scale,
     import #{aidbox.sdc zenbox fhir hl7-fhir-r4-core.Coding},
     DukeAnxietyDepressionScaleDocument
     {:zen/tags #{zen/schema aidbox.sdc/doc aidbox.sdc/rules},
      :source {:code "90854-1", :system "http://loinc.org"}
      :zen/desc "Duke Anxiety Depression Scale",
      :type zen/map,
      :confirms #{aidbox.sdc/Document},
      :keys {:final-score {:linkId "/107153",
                           :text "Final score",
                           :type zen/number,
                           :sdc-type aidbox.sdc/decimal}}},
     DukeAnxietyDepressionScaleLayout
     {:zen/tags #{aidbox.sdc/Layout aidbox.sdc/rules},
      :document DukeAnxietyDepressionScaleDocument,
      :engine aidbox.sdc/Hiccup,
      :layout {:type aidbox.sdc/col, :children [{:bind [:final-score]}]}},
     DukeAnxietyDepressionScaleLaunch
     {:zen/tags #{aidbox.sdc/Launch},
      :document DukeAnxietyDepressionScaleDocument,
      :params {:encounter-id {:type zen/string}},
      :populate-engine aidbox.sdc/LispPopulate,
      :populate {:author (get-in [:ctx :user]),
                 :encounter
                 {:id (get-in [:params :encounter-id]),
                   :resourceType "Encounter"},
                   :patient (sql {:select [:#> :resource [:subject]],
                                  :from :Encounter,
                                  :where [:= :id (get-in [:params :encounter-id])]})}},
     DukeAnxietyDepressionScaleFinalizeConstraints
     {:type zen/map, :zen/tags #{zen/schema}},
     DukeAnxietyDepressionScaleFinalize
     {:zen/tags #{aidbox.sdc/Finalize zen/schema},
      :document DukeAnxietyDepressionScaleDocument,
      :profile DukeAnxietyDepressionScaleFinalizeConstraints,
      :export-engine aidbox.sdc/LispExport,
      :create
      [{:template aidbox.sdc/gen-observation-template
        :params {:path [:final-score]}}]}
     DukeAnxietyDepressionScaleForm
     {:zen/tags #{aidbox.sdc/Form},
      :title "Duke Anxiety Depression Scale",
      :document DukeAnxietyDepressionScaleDocument,
      :layout DukeAnxietyDepressionScaleLayout,
      :launch DukeAnxietyDepressionScaleLaunch,
      :finalize DukeAnxietyDepressionScaleFinalize}}
```

-----------------------------------------------------------------

### convert-form

Converts Aidbox Form to FHIR Questionnaire, optionaly can save it to Questinnaire resource in Aidbox.

As Aidbox Forms is able to express more stuff than Questionnaire can handle - only subset of Forms functionality supported for convertion.

Basically only `Document` and `FinalizeConstraints` DSLs are supported

What is supported:

- `Document` structure with questions
  - nested questions
  - repeated questions
- Questions ordering based on natural question order in `Document DSL`
- required fields based on `Document DSL` and `FinalizeConstraints DSL`
- Questionnaire root properties via `Forms DSL` properties


params:

| Param            | Description                    | Type    | required? |
|------------------|--------------------------------|---------|-----------|
| form             | Form symbolic name             | string  | yes       |
| format           | Format of QR. Aidbox or FHIR   | string  | yes       |
| save-to-resource | Save Questionnaire to resource | boolean | no        |

- To save Questionnaire to resource you should specify `:fhir/id` property in the [`Form DSL`](form-dsl.md#properties-for-conversion)
- To specify additional fields that are represented in Questionnaire but not in the Form DSL - you can use predefined form properties for that [`Form DSL`](form-dsl.md#properties-for-conversion)

Request:

```
POST /rpc

method: aidbox.sdc/convert-form
params:
  form: 'box.sdc.sdc-example/PHQ2PHQ9Form
  format: "aidbox"
  save-to-resource: true
```

Result:

```yaml
result:
  resourceType: Questionnaire
  title: PHQ2/PHQ9 Depresssion
  name: box.sdc.sdc-example/PHQ2PHQ9Form
  status: draft
  lastReviewDate: '2022-10-10'
  contact:
    - name: cont1
  telecom:
    - system: tel
      value: 300-00-00
  effectivePeriod:
    start: '2022-10-10T10:10:10Z'
    end: '2022-10-10T10:10:10Z'
  description: Base schema for questionnaire(document) definition. Also a resource in DB - SDCDocument
  approvalDate: '2022-10-10'
  item:
  - linkId: misc-yes-no-button
    text: 'PHQ-9 Questionnaire: For positive depression screen or follow up'
    type: boolean
  - linkId: 44258-2
    text: Feeling bad about yourself-or that you are a failure or have let yourself or your family down
    type: choice
    code:
      - code: 44258-2
        system: http://loinc.org
        answerOption:
      - value:
          Coding:
            code: LA6568-5
            system: http://loinc.org
            display: Not at all
      - value:
          Coding:
            code: LA6569-3
            system: http://loinc.org
            display: Several days
      - value:
          Coding:
            code: LA6570-1
            system: http://loinc.org
            display: More than half the days
      - value:
          Coding:
            code: LA6571-9
            system: http://loinc.org
            display: Nearly every day
  - linkId: loinc-LL382-3
    text: PHQ-9 Interp-score
    type: decimal
    code:
      - code: LL382-3
        system: http://loinc.org
```

Error:

```yaml
error:
    message: Can't save form to resource: you need specify :fhir/id property on the form

```

### convert-forms

Convert all SDCForms with :fhir/id property to FHIR questionnaire and store them in DB.

This RPC don't have any paramerters.

Returns list of pairs [form-name, convertions-result]
where convertion result is one of:
 - Ok
 - {messge: "error message"}


Request:

```
POST /rpc

method: aidbox.sdc/convert-forms
```

Result:

```yaml
result:
  - - box.sdc.sdc-example/PHQ2PHQ9Form
    - ok
  - - box.sdc.sdc-example/PHQ2PHQ9Form
    - message: '...Something wrong with conversion...'

```

-----------------------------------------------------------------
### generate-form-template 

Generate ZEN namespace with empty Form definition and all relevant layers.
You can use this as starting point when you need create custom form from scratch.

> Note: rpc doesn't make any changes in filesystem - it just returns template. You then should save it to file manually


params:

| Param | Description       | Type   | required? |
|-------|-------------------|--------|-----------|
| ns    | Namespace of form | String | no        |
| title | Form Title        | String | no        |


```yaml
POST /rpc?

method: aidbox.sdc/generate-form-template
params:
    ns: 'my.company.forms.my-vitals'
    title: 'My first form'
```

Result:

```
result:
  form-content: |
    {ns my.company.forms.my-vitals,
     import #{aidbox.sdc zenbox aidbox.sdc.fhir},

     MyVitalsDocument
     {:zen/tags #{zen/schema aidbox.sdc/doc aidbox.sdc/rules},
      :zen/desc "My first form",
      :type zen/map,
      :confirms #{aidbox.sdc/Document},
      :keys {}},

     MyVitalsLayout
     {:zen/tags #{aidbox.sdc/Layout aidbox.sdc/rules},
      :document MyVitalsDocument,
      :engine aidbox.sdc/Hiccup,
      :layout {:type aidbox.sdc/col, :children []}},

     MyVitalsLaunch
     {:zen/tags #{aidbox.sdc/Launch},
      :document MyVitalsDocument,
      :params {:encounter-id {:type zen/string}},
      :populate-engine aidbox.sdc/LispPopulate,
      :populate
      {:author (get-in [:ctx :user]),
       :encounter
       {:id (get-in [:params :encounter-id]), :resourceType "Encounter"},
       :patient (sql {:select [:#> :resource [:subject]],
                      :from :Encounter,
                      :where [:= :id (get-in [:params :encounter-id])]})}},

     MyVitalsFinalizeConstraints
     {:type zen/map, :zen/tags #{zen/schema}},

     MyVitalsFinalize
     {:zen/tags #{aidbox.sdc/Finalize zen/schema},
      :document MyVitalsDocument,
      :profile MyVitalsFinalizeConstraints,
      :export-engine aidbox.sdc/LispExport,
      :create []},

     MyVitalsForm
     {:zen/tags #{aidbox.sdc/Form},
      :title "My first form",
      :document MyVitalsDocument,
      :layout MyVitalsLayout,
      :launch MyVitalsLaunch,
      :finalize MyVitalsFinalize}}
```

### generate-form-layout

Generate Form Layout Definition, based on Document schema.

> Note: rpc doesn't make any changes in filesystem - it just returns template. You then should save it to file manually


params:

| Param     | Description              | Type              | required? |
|-----------|--------------------------|-------------------|-----------|
| document  | SDCDocument schema name  | symbol            | yes       |
| show-keys | fields to show in layout | vector of strings | no        |

if `show-keys` is not given - generate layout for all fields in the document.

> NOTE: layout fields order will be the same as natural keys order in SDCDocument

```
POST /rpc?

method: aidbox.sdc/generate-form-layout
params:
    document: my.company.forms.my-vitals/MyVitals
    show-keys: 
      - weight
      - height
```

Result: 

```
result: |
  {:zen/tags #{aidbox.sdc/Layout aidbox.sdc/rules},
   :document my.company.forms.my-vitals/MyVitals,
   :engine aidbox.sdc/Hiccup,
   :layout
   {:type aidbox.sdc/col,
    :children
    [{:bind [:height]}
     {:bind [:weight]}]}}

```

Error 

```
error:
  message: Invalid params
  method: aidbox.sdc/generate-form-layout
  errors:
  - message: No symbol 'my.forms/MyForm found
    type: symbol
```

### generate-form-constraints

Generate Form FinalizeConstrains Definition, based on Document schema

> Note: rpc doesn't make any changes in filesystem - it just returns template. You then should save it to file manually


params:

| Param         | Description             | Type              | required? |
|---------------|-------------------------|-------------------|-----------|
| document      | SDCDocument schema name | symbol            | yes       |
| required-keys | Required fields         | vector of strings | no        |

if `required-keys` is not given - all fields are required by default.

```
POST /rpc?

method: aidbox.sdc/generate-form-constraints
params:
    document: my.company.forms.my-vitals/MyVitals
    required-keys: 
      - weight
      - height
```

Result: 


```
result: |
  {:zen/tags #{zen/schema},
   :type zen/map,
   :require #{:height :weight}}
```

Error 

```
error:
  message: Invalid params
  method: aidbox.sdc/generate-form-layout
  errors:
  - message: No symbol 'my.forms/MyForm found
    type: symbol
```

### generate-form-finalize


Generate Form Finalize Definition with extractions, based on Document schema.

> Note: rpc doesn't make any changes in filesystem - it just returns template. You then should save it to file manually

params:

| Param        | Description                          | Type              | required? |
|--------------|--------------------------------------|-------------------|-----------|
| document     | SDCDocument schema name              | symbol            | yes       |
| extract-keys | Keys for extraction                  | vector of strings | no        |
| profile      | name of Finalize Constraint profile. | string/symbol     | no        |


if `extract-keys` is not given - generate layout for all fields in the document.

```
POST /rpc?

method: aidbox.sdc/generate-form-finalize
params:
    document: my.company.forms.my-vitals/MyVitals
    profile: 
    extract-keys: 
      - weight
      - height
```

Result: 

```
result: |
  {:zen/tags #{aidbox.sdc/Finalize zen/schema},
   :document box.sdc.sdc-example/VitalsDocument,
   :export-engine aidbox.sdc/LispExport,
   :create
   [{:template aidbox.sdc/gen-observation-template,
     :params {:path [:weight]}}
    {:template aidbox.sdc/gen-observation-template,
     :params {:path [:height]}}]}
```

Error 

```
error:
  message: Invalid params
  method: aidbox.sdc/generate-form-layout
  errors:
  - message: No symbol 'my.forms/MyForm found
    type: symbol
```


### get-forms

Get forms definitions

{% hint style="info" %}
Response can be narrowed with document Symbol name substring
{% endhint %}

params:

| Param              | Description                         | Type   | required? |
|--------------------|-------------------------------------|--------|-----------|
| q                  | substring of Form symbolic name     | String | no        |
| include.properties | include-filter for properties match | map    | no        |
| exclude.properties | exclude-filter for properties match | map    | no        |

#### include/exclude filter

- include/exclude properties should be in that shape as you except them in Form definition.
- if property value is a set - then filter specifies elements subset with AND logic
- if property value is a coll - then filter specifies collection matching with strict order and elements counts
- if property value is a map - then filter specifies subset of map structure with searched leaves values
- if property value is a keyword/string/symbol - then filter specifies equal match by stringified property value. 



Request: 

```
POST /rpc?

method: aidbox.sdc/get-forms
params:
    q: 'Vitals'
    include:
        properties:
            teams: [physician]
            in-dev: true
```


Result 

```
result:
  entries:
  - form: box.sdc.sdc-example/VitalsForm
    title: Vitals Signs
  - form: box.sdc.sdc-example/VitalsFormWithoutExtractions
    title: Vitals Signs
```

### get-form

Get form for given document definition

params:

| Param | Description      | Type   | required? |
| ----- | ---------------- | ------ | --------- |
| form  | Form Symbol name | String | yes       |

```
POST /rpc?

method: aidbox.sdc/get-form
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

method: aidbox.sdc/launch
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

method: aidbox.sdc/read-document
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

method: aidbox.sdc/save,
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

method: aidbox.sdc/sign,
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


### aidbox.sdc/convert-document

Converts SDCDocument to QuestionnaireResponse

> If you need to have original Questionnaire `linkId`-s  - you should specify `:linkId` property of the SDCDocument fields.
> By default field keys are used as `linkId`. 

Also you may choose the output format:

* aidbox - to store inside Aidbox DB
* fhir - to exchange with other FHIR systems

Optionaly you can save converted `QuestionnaireResponse` to its resource table via `save-to-resource` flag

> NOTE: If you use `save-to-resource = true` you should specify `format = 'aidbox'` only

Params:

| Param            | Description                                 | Type              | required? |
|------------------|---------------------------------------------|-------------------|-----------|
| id               | id of SDCDocument in DB                     | String            | no        |
| document         | SDCDocument resource body                   | Resource          | no        |
| format           | Output format                               | "fhir" / "aidbox" | yes       |
| save-to-resource | If true - converts and saves QR to resource | boolean           | no        |


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
  - linkId: "blood-pressure"
    text: Blood pressure
    item:
    - linkId: "numbers"
      text: Numbers
      item:
      - linkId: "loinc-8480-6"
        text: BP sys
        answer:
        - valueQuantity:
            value: 100
      - linkId: "loinc-8462-4"
        text: BP dias
        answer:
        - valueQuantity:
            value: 130
    - linkId: "loinc-41904-4"
      text: BP measurement site
      answer:
      - valueCoding:
          system: http://loinc.org
          code: LA11158-5
  - linkId: "blood-pressure"
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
  - linkId: "loinc-8867-4"
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

### aidbox.sdc/generate-form-link

Creates [policy token](../../security-and-access-control-1/security/access-policy.md#signed-rpc-policy-token) to get access to SDCDocument/SDCWorkflow

params:

| Param             | Description                                            | Type                    | required? |
|-------------------|--------------------------------------------------------|-------------------------|-----------|
| form              | link to the form in DB                                 | Map                     | yes       |
| form.id           | SDCDocument/SDCWorkflow id                             | String                  | yes       |
| form.resourceType |                                                        | SDCDocument/SDCWorkflow | yes       |
| redirect-on-sign  | URI that used after form sign/amend event for redirect | URI                     | no        |

Request:

> Example with document

```javascript
POST {{base}}/rpc

method: aidbox.sdc/generate-form-link
params:
  form:
    id: doc-1
    resourceType: SDCDocument
  redirect-on-sign: http://my.portal.com?param1=p1
```

Result:

```javascript
result:
  link: <shared form link>
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

method: aidbox.sdc/amend,
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

{% hint style="info" %}
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
