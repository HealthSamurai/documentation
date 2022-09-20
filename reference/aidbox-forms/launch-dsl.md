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

