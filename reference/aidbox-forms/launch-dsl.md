# Launch DSL

**Form Launch** binds to [`Document`](document-dsl.md) and `Form`. And optionally can specify fields populate logic via `populate-engine`

Default populate-engine - `aidbox.sdc/LispPopulate`

For populate you can specify what fields should be populated.

**For that you can use:**

* basic edn values
* [lisp expressions](lisp.md)
* you need to follow [Document](document-dsl.md) fields structure.



```
 VitalsLaunch
 {:zen/tags #{aidbox.sdc/Launch}

  ;; bind to document
  :document VitalsDocument

  ;; specify parameters for launch
  :params {:encounter-id {:type zen/string}}

  ;; set populate engine
  :populate-engine aidbox.sdc/LispPopulate

  ;; populate logic. Define fields in the shape of the document.
  :populate {:author    (lisp/get-in [:ctx :user])
             :encounter {:id (lisp/get-in [:params :encounter-id])
                         :resourceType "Encounter"}
             :patient   (lisp/sql {:select [:#> :resource [:subject]]
                                   :from :Encounter
                                   :where [:= :id (lisp/get-in [:params :encounter-id])]})}}
```

Launch form with given launch, prepopuluate data, and return enriched with metadata layout.

| Param         | Description                               | Type    | required? |
| ------------- | ----------------------------------------- | ------- | --------- |
| form          | Form Symbol name                          | String  | yes       |
| dry-run       | Run without saving document in db         | boolean | no        |
| unit-system   | Preffered unit system (default: imperial) | String  | no        |
| rules-in-lisp | Return rules as Lisp or AST               | boolean | no        |
| params        | Params to launch-context                  | map     | no        |

Request:

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
