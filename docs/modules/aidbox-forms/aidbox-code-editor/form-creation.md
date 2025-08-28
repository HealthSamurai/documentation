# Form creation

To create new form you need to define Form-assembly and at least 1 layer (schemas from `aidbox.sdc`) :

* [Form](form-creation.md#form) - bind all layers in one place
* [SDCDocument](form-creation.md#sdcdocument) - contains questions with answers and other meta data

Other layers you could add as you need some special behavior:

* [Form Layout](form-creation.md#form-layout) - define fields layout and display rules
* [Form Launch](form-creation.md#form-launch) - define form init parameters and prepopulate logic.
* [Form Finalize](form-creation.md#form-finalize) - define additional validations and extractions for form `sign`

### SDCDocument

Lets define some subset of the Vitals questionaire.

```
 VitalsDocument
 {:zen/tags #{zen/schema aidbox.sdc/doc aidbox.sdc/rules},
  :type zen/map,

 ;; define source of original questionaire (optional)
 :source {:code "85353-1", :system "http://loinc.org"},

 ;; title of your document
 :zen/desc "Basic Vitals Document",

 ;; to validate against common document fields you need to confirms sdc/Document
 ;; document also has common fields: author, patient, encounter
 :confirms #{aidbox.sdc/Document},

;; you also can define some rules for your document and also computed fields.
;; (lisp reference see bellow)
 :sdc/rules {:bmi (when (and (get :weight) (get :height))
                   (* (divide (get :weight)
                              (get :height))
                      10000))},
;; all fields should be defined under :keys
;; fields are defined with zen types and confirms.
 :keys {:weight {:text "Weight",
                 :confirms #{aidbox.sdc.fhir/quantity},
                 :units [{:name "kg"}]},
        :height {:text "Body height",
                 :confirms #{aidbox.sdc.fhir/quantity}
                 :units [{:name "cm"}]},
        :bmi {:text "BMI",
              :type zen/number}}}
```

**SDC Rules** is just are lisp expressions that can be used for two things:

* store temporary value
* calculate document field

To store rule value in SDCDocument give it the same name as the field:

```
DepressionDocument
{...
 :sdc/rules {:phq2-score (+ (get-in [...] ...)
 :type zen/map
 :keys {:phq2-score {:type zen/integer}} ;; phq2-score will be calculated on-fly and stored in the document
 ...
 }
```

More on SDCDocument:[ Document DSL](../../../deprecated/deprecated/forms/document-dsl-docs-deprecated.md) Reference

### Form Layout

Form layout defines fields layout for specific `SDCDocument` and layout-engine

Default Layout engine is `aidbox.sdc/Hiccup` uses DSL in shape of nested objects `{:type component-type :children []}`

```
 VitalsLayout
 {:zen/tags #{aidbox.sdc/Layout}

  ;; bind form to document
  :document VitalsDocument

  ;; set layout engine (pluggable, default: aidbox.sdc/Hiccup)
  :engine aidbox.sdc/Hiccup

  ;; layout specified according to layout engine
  :layout
  {:type aidbox.sdc/col,
   :children

   ;; {:bind [:field-name]} used to bind widget to specific field of the document
   [{:bind [:loinc-29463-7]}
    {:bind [:loinc-8302-2]}
    {:bind [:loinc-39156-5]}]}}
```

More on form layout: [Layout DSL](../../../deprecated/deprecated/forms/layout-dsl-docs-deprecated.md) Reference

### Form Launch

Form `Launch` defines parameters schema for `aidbox.sdc/launch` rpc and populate logic for `SDCDocument`.

If you don't specify this Layer your parameters (for `aidbox.sdc/launch` rpc) will be passed **as-is** to `SDCDocument`.

{% hint style="info" %}
Default populate-engine - `aidbox.sdc/LispPopulate`
{% endhint %}

For populate you can specify what fields should be populated.

For that you can use:

* basic edn values
* lisp expressions
* you need to follow SDCDocument fields structure.

```
 VitalsLaunch
 {:zen/tags #{aidbox.sdc/Launch}

  ;; bind to document
  :document VitalsDocument

  ;; specify parameters for launch
  :params {:encounter-id {:type zen/string}
           :tenant-id {:type zen/string}}

  ;; set populate engine
  :populate-engine aidbox.sdc/LispPopulate

  ;; populate logic. Define fields in the shape of the document.
  :populate {:author    (get-in [:ctx :user])
             :encounter {:id (get-in [:params :encounter-id])
                         :resourceType "Encounter"}
             :patient   (sql {:select [:#> :resource [:subject]]
                              :from :Encounter
                              :where [:= :id (get-in [:params :encounter-id])]})}}
```

More on launch: [Launch DSL](../../../deprecated/deprecated/forms/launch-dsl-docs-deprecated.md) Reference

### Form Finalize

Form `Finalize` defines extractions that should be done after document `sign` and optionally binds to custom constraint schema for validations.

{% hint style="info" %}
Default export-engine - `aidbox.sdc/LispExport` (see available commands: [LISP](../../../deprecated/deprecated/forms/lisp-docs-deprecated.md) Reference)
{% endhint %}

```clojure
VitalsFinalize
{:zen/tags #{aidbox.sdc/Finalize zen/schema}

;; bind to document
:document VitalsDocument

;; possible to define custom export engine
:export-engine aidbox.sdc/LispExport

;; describe which resources should be created based on form data
:create [
         ;; use lisp template to generate observation if field is exists
         {:template aidbox.sdc/gen-observation-template
          :params {:path [:bmi]} }

         ;; create observation resource if the field exists
         (when (get :height)
             {:resourceType "Observation"
             :status       "final"
             :code         {:coding [{:code "29463-7"}]}
             :subject      (get :patient)
             :encounter    (get :encounter)
             :value        {:Quantity (get :height)}})

;; decribe other resources to create

 ,,,]}
```

### Finalize Constraints

The Form gets validated on `sign` operation using the document schema. An additional validation profile can be defined within the Finalize layer.

```clojure
VitalsFinalize
{:zen/tags #{aidbox.sdc/Finalize zen/schema}

 ;; bind to document
 :document VitalsDocument

 ;; additional validation profile
 :profile VitalsFinalizeConstraints

,,,
}
```

This profile can e.g. declare mandatory fields or set some limitations like min/max for numeric fields etc. The profile is defined via zen schema.

```clojure
VitalsFinalizeConstraints
{:zen/tags #{zen/schema}
 :type zen/map

 ;; list of mandatory fields
 :require #{:bmi :weight :height}

 :keys
 {;; limit field value to the specified range
  :bmi {:type zen/number :min 20, :max 220}

  ;; denote this field value is required
  :weight {:type zen/map :require #{:value}}

  ;; more validation constraints
  ,,,
  }}}

```

### Form

Form used just to bind all DSLs to one item.

```
 VitalsForm
 {:zen/tags #{aidbox.sdc/Form}
  ;; form title
  :title    "Vitals Signs"
  ;; bind to Document
  :document VitalsDocument
  ;; bind to Layout
  :layout   VitalsLayout
  ;; bind to Launch
  :launch   VitalsLaunch
  ;; bind to Finalize
  :finalize VitalsFinalize}

```

For now you can already try to use created document via `aidbox.sdc` [API](../../../deprecated/deprecated/forms/form-api-docs-deprecated.md)
