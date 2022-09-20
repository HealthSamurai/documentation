# API reference

* ``[`get-forms`](api-reference.md#get-forms) - get existed forms
* ``[`get-form`](api-reference.md#get-form) - get form definition for given form name
* ``[`launch`](api-reference.md#launch) - launch new form with given params
* ``[`read-document`](api-reference.md#read-document) - get form with saved document
* ``[`save`](api-reference.md#save) - save document
* ``[`sign`](api-reference.md#sign) - finalize document, run extracts

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

\


\


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

Save document draft without any validations

| Param    | Description       | Type        | required? |
| -------- | ----------------- | ----------- | --------- |
| document | document resource | SDCDocument | yes       |

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

Result

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

\


### sign

Validates document and run extractions on it. Mark document as completed

| Param    | Description                                 | Type        | required? |
| -------- | ------------------------------------------- | ----------- | --------- |
| document | document resource                           | SDCDocument | yes       |
| dry-run  | Run without saving document and extractions | boolean     | no        |

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

##

\


##

\
