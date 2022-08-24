# Finalize DSL

Finalize DSL is a schema that contains all the necessary information for signing a document:

* more strict validation before signing (Finalize profile)
* extraction of the data into FHIR resources after signing

Example of `Finalize` document:

```
 VitalsFinalize
 {:zen/tags #{aidbox.sdc/Finalize zen/schema}
  :document VitalsDocument
  :profile VitalsFinalizeConstraints
  :export-engine aidbox.sdc/LispExport
  :export [(lisp/for [lisp/i (lisp/get :loinc-8310-5)]
             (lisp/when (lisp/get lisp/i :loinc-8310-5-value)
               {:resourceType "Observation"
                :status "final"
                :subject   (lisp/get :patient)
                :encounter (lisp/get :encounter)
                :effective {:dateTime (lisp/get lisp/i :datetime)}

                :code {:coding [{:code "8310-5"}]}
                :value {:Quantity (lisp/get lisp/i :loinc-8310-5-value)}}))
           (lisp/for [lisp/i (lisp/get :loinc-85354-9)]
             (lisp/when (lisp/and (lisp/get lisp/i :loinc-8480-6)
                                  (lisp/get lisp/i :loinc-8462-4))
               {:resourceType "Observation"
                :status "final"
                :subject   (lisp/get :patient)
                :encounter (lisp/get :encounter)
                :effective {:dateTime (lisp/get lisp/i :datetime)}
                :code {:coding [{:code "85354-9"}]}
                :bodySite {:coding
                           (lisp/remove
                            lisp/nil?
                            [(lisp/when (lisp/get-in lisp/i [:loinc-41904-4 :code])
                               {:display (lisp/get-in lisp/i [:loinc-41904-4 :text])
                                :code (lisp/get-in lisp/i [:loinc-41904-4 :code])
                                :system (lisp/get-in lisp/i [:loinc-41904-4 :system])})
                             (lisp/when (lisp/get-in lisp/i [:loinc-8361-8 :code])
                               {:display (lisp/get-in lisp/i [:loinc-8361-8 :text])
                                :code (lisp/get-in lisp/i [:loinc-8361-8 :code])
                                :system (lisp/get-in lisp/i [:loinc-8361-8 :system])})])}
                :component [{:code {:coding [{:code "8480-6"}]}
                             :value {:Quantity (lisp/get lisp/i :loinc-8480-6)}}
                            {:code {:coding [{:code "8462-4"}]}
                             :value {:Quantity (lisp/get lisp/i :loinc-8462-4)}}]}))
           (lisp/when (lisp/get :loinc-29463-7)
             {:resourceType "Observation"
              :status "final"
              :code {:coding [{:code "29463-7"}]}
              :subject   (lisp/get :patient)
              :encounter (lisp/get :encounter)
              :value {:Quantity (lisp/get :loinc-29463-7)}})

           (lisp/when (lisp/get :loinc-8302-2)
             {:resourceType "Observation"
              :status "final"
              :code {:coding [{:code "8302-2"}]}
              :subject   (lisp/get :patient)
              :encounter (lisp/get :encounter)
              :value {:Quantity (lisp/get :loinc-8302-2)}})
           (lisp/when (lisp/get :loinc-39156-5)
             {:resourceType "Observation"
              :status "final"
              :code {:coding [{:code "39156-5"}]}
              :subject   (lisp/get :patient)
              :encounter (lisp/get :encounter)
              :value {:integer (lisp/get :loinc-39156-5)}})
           (lisp/for
            [lisp/i (lisp/get :loinc-8867-4)]
             (lisp/when
              (lisp/get lisp/i :loinc-8867-4-value)
               {:encounter (lisp/get :encounter),
                :value {:Quantity (lisp/get lisp/i :loinc-8867-4-value)},
                :resourceType "Observation",
                :status "final",
                :effective {:dateTime (lisp/get lisp/i :datetime)},
                :code {:coding [{:code "8867-4"}]},
                :subject (lisp/get :patient)}))
           (lisp/for
            [lisp/i (lisp/get :loinc-9279-1)]
             (lisp/when
              (lisp/get lisp/i :loinc-9279-1-value)
               {:encounter (lisp/get :encounter),
                :value {:Quantity (lisp/get lisp/i :loinc-9279-1-value)},
                :resourceType "Observation",
                :status "final",
                :effective {:dateTime (lisp/get lisp/i :datetime)},
                :code {:coding [{:code "9279-1"}]},
                :subject (lisp/get :patient)}))
           (lisp/for
            [lisp/i (lisp/get :loinc-59408-5)]
             (lisp/when
              (lisp/get lisp/i :loinc-59408-5-value)
               {:encounter (lisp/get :encounter),
                :value {:Quantity (lisp/get lisp/i :loinc-59408-5-value)},
                :resourceType "Observation",
                :status "final",
                :effective {:dateTime (lisp/get lisp/i :datetime)},
                :code {:coding [{:code "59408-5"}]},
                :subject (lisp/get :patient)}))]}
```

Description of top-level parameters:

| Parameter        | Type                         | Description                                                                          |
| ---------------- | ---------------------------- | ------------------------------------------------------------------------------------ |
| `:zen/tags`      | A Set                        | Should always be `#{aidbox.sdc/Finalize zen/schema}`                                 |
| `:document`      | A symbol                     | A reference to a document object, e.g. `VitalsDocument`                              |
| `:profile`       | A symbol                     | An (optional) reference to [FinalizeConstraints ](finalizeconstraints-dsl.md)object  |
| `:export-engine` | A symbol                     | Should always be `aidbox.sdc/LispExport`                                             |
| `:create`        | A Vector of LISP expressions | Please [see below](finalize-dsl.md#export-and-lisp-expressions)                      |
| `:patch`         | A Vector of LISP expressions | Please [see below](finalize-dsl.md#export-and-lisp-expressions)                      |
| `:delet`         | A Vector of LISP expressions | Please [see below](finalize-dsl.md#export-and-lisp-expressions)                      |

### `:export` and LISP expressions

LISP sub-language [(see Lisp reference)](lisp.md) is used to specify expressions which transform data of the document into FHIR resources (e.g. Observations).

Inside of `:create` structure, LISP's `get-in` function is used to access data in the document. Other LISP functions (and `lisp/i`, `lisp/j` expressions) are used to transform the data correspondingly and create a FHIR resource object (see an example above)

```
....
:create [{:resourceType "Observation"
          :subject (lisp/get-in [:patient :id])
		  :code [...]}
	     {:resourceType "Observation"
          :subject (lisp/get-in [:patient :id])
		  :code [...]}]
....
```

This code will create 2 Observations resources with given patient.

Inside of `:patch, :delete` LISP's expressions are used to return list of resources that should be updated or deleted.

Examples:

```
...
:patch [{:id (lisp/get-in [:patient :id])
         :resourceType "Patient"
		 :gender (lisp/get [:gender])}]
:delete [(lisp/sql ["select jsonb_build_object('id', id, 'resourceType', resource_type) from Observation
                    where resource#>>'{subject,id}' = ?", (lisp/get-in [:patient :id])])]
...
```

In `:patch` section, patient's gender will be updated to the value from document In `:delete` section, we query all Observations related to this patient that should be deleted

\
