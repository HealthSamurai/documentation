# Mappings with lisp/mapping

Aidbox provides the ability to **convert data dynamically** using `lisp/mapping` in your configuration. This is used in the [HL7 v2 pipeline](hl7-v2-integration-with-aidbox-project.md) and some [API constructor](broken-reference) operations.&#x20;

In order to create **mapping**, you need to declare a definition with `lisp/mapping` tag and write a structure with `:mapping` key, using [lisp expressions](broken-reference) in places where data will be calculated dynamically from the passed structure. It also allow to define an optional parameter `:data-schema`, which should be tagged as zen/schema and allows to validate the input data.&#x20;

```clojure
 my-data-schema
 {:zen/tags #{zen/schema}
  :type zen/map
  :values {:type zen/any}}
 
 breathing-rate-mapping
 {:zen/tags #{lisp/mapping}
  :mapping  {:resourceType "Bundle"
             :type "transaction"
             :entry [{:resource {:resourceType "Patient"}
                      :request {:method "PUT"
                                :url "/Patient/zero"}}

                     {:resource {:resourceType "Observation"
                                 :status "final"
                                 :code {:coding [{:system "http://loinc.org"
                                                  :code "8867-4"
                                                  :display "Respiratory rate"}]
                                        :text "Breathing Rate"}
                                 :subject {:reference "Patient/zero"}
                                 :effectiveDateTime (get-in [:br 0 :dateTime]),
                                 :valueQuantity   {
                                                   :value (get-in [:br 0 :value :breathingRate]),
                                                   :unit "breaths/minute",
                                                   :system "http://unitsofmeasure.org",
                                                   :code "/min"}}
                      :request {:method "POST"
                                :url "/Observation"}}]}
    :data-schema my-data-schema ;; Optional
    }
                                

```

### Example

#### Passed structure

```clojure
{:br [{:value {:breathingRate 18.1}
       :dateTime "2022-11-15"}]}
```

#### Result

```clojure
{:type "transaction-response"
 :entry
 [{:resource
   {:id "zero"
    :resourceType "Patient"}}
  {:resource
   {:valueQuantity
    {:code "/min",
     :unit "breaths/minute",
     :value 17.8,
     :system "http://unitsofmeasure.org"},
    :resourceType "Observation",
    :effectiveDateTime "2021-10-25",
    :status "final",
    :code
    {:text "Breathing Rate",
     :coding
     [{:code "8867-4",
       :system "http://loinc.org",
       :display "Respiratory rate"}]},
    :subject {:reference "Patient/zero"}},}]}
```

In the provided example, **mapping** is used to create a Bundle with dynamically calculated _Observation.valueQuantity.value_ and _Obsevation.effectiveDateTime_ FHIR properties.
