# How to create a form

To create new form you need to define 5 resources in zen (schemas from `adibox.sdc`) :

* [SDCDocument](how-to-create-a-form.md#sdcdocument) - contains questions with answers and other meta data
* [Form Layout](how-to-create-a-form.md#form-layout) - binds to SDCDocument and its fields.
* [Form Launch](how-to-create-a-form.md#form-launch) - define form init parameters and prepopulate logic.
* [Form Finalize](how-to-create-a-form.md#form-finalize) - define additional validations and extractions for form `sign`
* [Form](how-to-create-a-form.md#form) - bind all in one place

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
 :sdc/rules {:bmi (lisp/when
                   (lisp/and (lisp/get :loinc-29463-7)
                    (lisp/get :loinc-8302-2))
                   (lisp/*
                    (lisp/divide
                     (lisp/get :loinc-29463-7)
                     (lisp/get :loinc-8302-2)
                     (lisp/get :loinc-8302-2))
                    10000))},
;; all fields should be defined under :keys
;; fields are defined with zen types from: zen, aidbox.sdc, fhir namespaces
 :keys {:loinc-29463-7 {:sdc-type aidbox.sdc/quantity,
                        :confirms #{fhir/Quantity},
                        :questionCodeSystem "http://loinc.org",
                        :linkId "/29463-7",
                        :units [{:name "kg"}],
                        :text "Weight",
                        :zen/desc "Weight"},
        :loinc-8302-2 {:questionCodeSystem "http://loinc.org",
                       :linkId "/8302-2",
                       :units [{:name "cm"}],
                       :text "Body height",
                       :sdc-type aidbox.sdc/quantity,
                       :confirms #{fhir/Quantity},
                       :zen/desc "Body height"},
        :loinc-39156-5 {:questionCodeSystem "http://loinc.org",
                        :linkId "/39156-5",
                        :text "BMI",
                        :type zen/number,
                        :sdc-type aidbox.sdc/calculated,
                        :zen/desc "BMI"}}}
```

**SDC Rules** is just are [lisp expressions](https://github.com/getheal/zen-sdc/blob/main/docs/lisp.md) that can be used for two things:

* store temporary value
* calculate document field

To store rule value in SDCDocument give it the same name as the field:

```
DepressionDocument
{...
 :sdc/rules {:phq2-score (lisp/+
                            (lisp/get-in [...]
                          ...)
 :type zen/map
 :keys {:phq2-score {:type zen/integer}} ;; phq2-score will be calculated on-fly and stored in the document
 ...
 }
```

More on SDCDocument:[ Document DSL](../../reference/aidbox-forms/document-dsl.md) Reference

### Form Layout

Form layout binds to specific `SDCDocument` and layout-engine and defines layout via specific DSL

Default Layout DSL has shape of nested objects `{:type component-type :children []}`

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

More on form layout: [Layout DSL](../../reference/aidbox-forms/layout-dsl.md) Reference

### Form Launch

Form `Launch` binds to `SDCDocument` and `Form`. And optionally can specify fields populate logic via `populate-engine`

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
  :populate {:author    (lisp/get-in [:ctx :user])
             :encounter {:id (lisp/get-in [:params :encounter-id])
                         :resourceType "Encounter"}
             :patient   (lisp/sql {:select [:#> :resource [:subject]]
                                   :from :Encounter
                                   :where [:= :id (lisp/get-in [:params :encounter-id])]})}}
```

More on launch: [Launch DSL](../../reference/aidbox-forms/launch-dsl.md) Reference

### Form Finalize

Form `Finalize` binds to document, specifies custom validations and set `export-engine` with its DSL to define extractions that should be done after document `sign`.

{% hint style="info" %}
Default export-engine - `aidbox.sdc/LispExport` (see available commands: [LISP](../../reference/aidbox-forms/lisp.md) Reference)
{% endhint %}

```
 VitalsFinalize
 {:zen/tags #{aidbox.sdc/Finalize zen/schema}

  ;; bind to document
  :document VitalsDocument

  ;;
  :profile {:require #{:loinc-8302-2 :loinc-8867-4}}

  ;; define cusot export-engine
  :export-engine aidbox.sdc/LispExport
  :export [

        ;; if field is existed create observation resource
        (lisp/when (lisp/get :loinc-8302-2)
          {:resourceType "Observation"
          :subject   (lisp/get :patient)
          :encounter (lisp/get :encounter)
          :status    "final"
          :code     {:coding [{:code "8302-2"}]}
          :value {:Quantity {:value (lisp/get :loinc-8302-2)
                             :unit "cm"}}})

         (lisp/when (lisp/get :loinc-29463-7)
          {:resourceType "Observation"
            :subject   (lisp/get :patient)
            :encounter (lisp/get :encounter)
            :status    "final"
            :code     {:coding [{:code "29463-7"}]}
            :value {:Quantity {:value (lisp/get :loinc-29463-7)
                              :unit "kg"}}})]}
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

For now you can alredy try to use created document via `aidbox.sdc` [API](../../reference/aidbox-forms/api-reference.md)
