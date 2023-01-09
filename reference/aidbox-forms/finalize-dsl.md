# Finalize DSL

Finalize definition describes document validation and postprocessing logic that invoked on sign:

Features:

* more strict validation before signing (Finalize profile)
* extraction of the data into FHIR resources after signing

Description of top-level parameters:

| Parameter        | Type                         | Description                                                       |
| ---------------- | ---------------------------- | ----------------------------------------------------------------- |
| `:zen/tags`      | A Set                        | Should always be `#{aidbox.sdc/Finalize zen/schema}`              |
| `:document`      | A symbol                     | A reference to a document object, e.g. `VitalsDocument`           |
| `:profile`       | A symbol                     | An (optional) reference to FinalizeConstraints object (see below) |
| `:export-engine` | A symbol                     | Should always be `aidbox.sdc/LispExport`                          |
| `:create`        | A Vector of LISP expressions | Please see below                                                  |
| `:patch`         | A Vector of LISP expressions | Please see below                                                  |
| `:delete`        | A Vector of LISP expressions | Please see belowExample of `Finalize` document:                   |

Example of `Finalize` document:

```
 VitalsFinalize
 {:zen/tags #{aidbox.sdc/Finalize zen/schema}
  :document VitalsDocument
  :profile VitalsFinalizeConstraints
  :export-engine aidbox.sdc/LispExport
  :delete [
    ;; resource reference construction for deletion 
    {:resourceType "Observation" :id (get-in )}
   ]
  :patch [
    ;; resource patch construction.
    (when (= "mogan" (get [:patient :id]))
      {:resourceType "Observation" :id (get [:patient :id])})
  ]
  :create [

    ;; lisp templates to generate observation only by specifying path of question
    {:template aidbox.sdc/gen-observation-template
     :params {:path [:loinc-8310-5]} }

    ;; lisp logic to traverse repeated document values with lisp.
    (for [i (get :loinc-8310-5)]
        ;; condition for resource creation
        (when (get i :loinc-8310-5-value)
        ;; resource construction from document values
        {:resourceType "Observation"
            :status "final"
            :subject   (get :patient)
            :encounter (get :encounter)
            :effective {:dateTime (get i :datetime)}
            :code {:coding [{:code "8310-5"}]}
            :value {:Quantity (get i :loinc-8310-5-value)}}))

    ;; lisp expression which can retun nil. if nil - nothing is created
    (when (get :loinc-29463-7)
        ;; resource construction from document values
        {:resourceType "Observation"
        :status "final"
        :code {:coding [{:code "29463-7"}]}
        :subject   (get :patient)
        :encounter (get :encounter)
        :value {:Quantity (get :loinc-29463-7)}})
  ]}
```

### `:create/:delete/:patch` and LISP expressions

[LISP sub-language](lisp.md) is used to specify expressions which transform data of the document into FHIR resources (e.g. Observations).

Inside of `:create` structure, LISP's `get-in` function is used to access data in the document. Other LISP functions are used to transform the data correspondingly and create a FHIR resource object (see an example above)
Also in `:create` structure you can use lisp templates to avoid repetitions for extracting common resources (e.g. Observations)

```
....
:create [
          ;; direct resource mapping
          {:resourceType "Observation"
                    :subject (get-in [:patient :id])
                    :code [...]}

          ;; with lisp expr
          (when (get-in [:loinc-8310-2])
            {:resourceType "Observation"
             :subject (get-in [:patient :id])
             :code [...]})

         ;; wiht lisp templates
         {:template aidbox.sdc/gen-observation-template
          :params {:path [:loinc-8310-5]} }
          ]
....
```

This code will create 3 Observations resources with given patient.

We have only 2 lisp templates 

- `aidbox.sdc/gen-observation-template` - generate observations resource
- `aidbox.sdc/gen-repeated-observations-template` - generated observations for repeated values

> lisp templates can be manually created by user.

Inside of `:patch, :delete` LISP's expressions are used to return list of resources that should be updated or deleted.

Examples:

```
...
:patch [{:id (get-in [:patient :id])
         :resourceType "Patient"
         :gender (get [:gender])}]
:delete [(sql ["select jsonb_build_object('id', id, 'resourceType', resource_type) from Observation
                where resource#>>'{subject,id}' = ?", (get-in [:patient :id])])]
...
```

In `:patch` section, patient's gender will be updated to the value from document In `:delete` section, we query all Observations related to this patient that should be deleted
