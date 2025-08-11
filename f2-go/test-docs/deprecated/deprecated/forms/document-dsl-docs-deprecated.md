---
hidden: true
---

# Document DSL (Docs Deprecated)

* Document is schema for Questionnaire with questions and answers and types of values.
* Document is used as source of meta-information by [FormLayout](layout-dsl-docs-deprecated.md)
* Document is used as source of meta-information by [FormFinalize](finalize-dsl-docs-deprecated.md)
* Document resources stored in `SDCDocument` table.
* Document can define computed fields via rules.

## Marking schema as SDC `Document` entity

Document definition should be marked with `aidbox.sdc/doc` tag, and also tagged as `zen/schema` - because it's already schema.

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc}}
```

And after that you can define your custom properties of the document and it's meta information (like source of the document)

Document source defined as fhir `Coding` like structure, with `:system`, `:code` and `:url` properties

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc}
 :source {:code "85353-1" :system "http://loinc.org"}}
```

To define document fields - you should add `:type zen/map` property and put your fields under `:keys` property.

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :type zen/map
 :keys {:loinc-8310-5 {:text "Body temperature"
                       :type zen/map,
                       :confirms #{aidbox.sdc.fhir/quantity},
                       :sdc/boundaries {:imperial {:min 86, :max 105},
                                        :metric {:min 30, :max 42}},
                       :units [{:name "°F"} {:name "°C"}]}}}
```

Every document field should confirms `aidbox.sdc/super-field` schema.

For details see [**Document fields definition**](document-dsl-docs-deprecated.md#document-field-definition) section.

## DB Storage & Common properties

To support document db-saving ability and support common fields for all documents such as:

* `:author` - Reference to user which create document,
* `:encounter` - Reference to encounter,
* `:unit-system` - What unit system was used in this document at launch,
* `:patient` - Reference to patient
* `:status` - Status of the document,
* `:type` - Full name of document profile.
* `:form` - Name/version of form, with which document was captured

You need to add `:confirms #{aidbox.sdc/Document}` property

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc}
 :type zen/map
 :confirms #{aidbox.sdc/Document}}
```

## Rules

Rules used for computed properties. Properties schema and compute formula - defined separately.

To support rules definition you need to add tag `aidbox.sdc/rules`

```
MyDocument
{:zen/tags #{aidbox.sdc/doc zen/schema aidbox.sdc/rules}}
```

and after that you can define caclulated properties and their formulas under `:sdc/rules` property

```
MyDocument
{:zen/tags #{aidbox.sdc/dok zen/schema aidbox.sdc/rules},
 :type zen/map
 :sdc/rules {:loinc-39156-5 (when (and (get-in [:loinc-29463-7])
                                       (get-in [:loinc-8302-2]))
                             (* (divide (* (get-in [:loinc-29463-7]) 703)
                                        (* (get-in [:loinc-8302-2])
                                           (get-in [:loinc-8302-2])))
                                1))},
 :keys {:loinc-39156-5 {:type zen/number,
                        :text "BMI"}}}
```

* name of the rule (`:loinc-39156-5`) should be matched with name of the field.
* If sdc-type inference can't recognize field as calculated - you can mark it manually - add `:sdc-type aidbox.sdc/calculated` to it

{% hint style="info" %}
When form launched - Rules from `Document` will be merged with rules from `Layout` and placed under `:rules` key in response.
{% endhint %}

{% hint style="danger" %}
Don't use same keys in Document rules and Layout rules
{% endhint %}

Rules logic defined via Embeddel lisp. See details in Lisp reference.

{% hint style="info" %}
Aidbox Form UI has embedded lisp engine. If you use your custom ui - you should implement your lisp-engine, based on lisp reference.
{% endhint %}

## Document Field definition

SDCDocument has several embedded field-types:

* `aidbox.sdc/text` - fields with multiline text
* `aidbox.sdc/string` - fields with oneline text
* `aidbox.sdc/number` - fields with number
* `aidbox.sdc/quantity` - fields with measurement units
* `aidbox.sdc/choice` - fields with answers, located in valuesets
* `aidbox.sdc/reference` - fields with reference to arbitrary resources
* `aidbox.sdc/calculated` - fields, which need to be calculated via rules engine
* `aidbox.sdc/attachment` - fields, that can be used to attach a file

This field types inferred from Document field schemas. But you can overwrite that by specifying it directly via `:sdc-type` key

### super-field schema

Field-type definition.

Schema contains several aspects of Document (Field) lifecycle

* how it stored in DB
* how it validates
* how it appeares in UI layout
* what the possible options
* what the source of the field

Can contain sub-fields or behaves like an array. Also has links to original sources of questions.

super-field schema has next keys:

| property               | description                                                           | filed-type | type                                     | required? |
| ---------------------- | --------------------------------------------------------------------- | ---------- | ---------------------------------------- | --------- |
| `text`                 | Question text                                                         | all        | string                                   | yes       |
| `help`                 | Question explanation or some hint - how to interpret it               | all        | string                                   | no        |
| `type`                 | zen/type of the value, which will be stored in DB                     | all        | symbol                                   | no        |
| `sdc-type`             | field meta-type - used for determine UI input type                    | all        | symbol                                   | no        |
| `confirms`             | additional schemas for stored value confirmation                      | all        | set of symbols                           | no        |
| `sdc/default-value`    | Default field value (also for repeated fields)                        | all        | zen/any                                  | no        |
| `enum`                 | List of available values                                              | choice     | zen/keyword                              | no        |
| `sdc/options`          | Alternative options source                                            | choice     | zen/keyword                              | no        |
| `units`                | List of units                                                         | quantity   | vector of {:name string}                 | no        |
| `keys`                 | When field is complex object(map) - used for defining nested fields   | group      | map of super-fields                      | no        |
| `every`                | When field is coll (zen/vector) - used for defining coll-item schema  | repeated   | super-field                              | no        |
| `sdc/boundaries`       | Min/max values for quantity fields (for different measurement units)  | quantity   | {:imperial MinMaxMap, :metric MinMaxMap} | no        |
| `last-change-datetime` | Meta field - contains datetime of last changes                        | all        | any                                      | no        |
| `code`                 | CodeSystem of the question (used in extractions)                      | all        | Coding                                   | no        |
| `linkId`               | Link to original questing (in questionnaire, used in converter to QR) | all        | string                                   | no        |
| `resourceType`         | resource table for quering data                                       | reference  | string                                   | no        |

### quantity field-type

Quantity contains units of measurement

quantity schema has next additional keys:

| property | description   | type                     | required? |
| -------- | ------------- | ------------------------ | --------- |
| `units`  | List of units | vector of {:name string} | no        |

Example:

```
MyDocument
{:zen/tags #{aidbox.sdc/doc zen/schema}
 :keys {:temp {:text "Body temperature"
               :type zen/map,
               :confirms #{aidbox.sdc.fhir/quantity},
               :units [{:name "°F"} {:name "°C"}]}}}
```

### choice field-type

Choise field has predefined set of answers.

Choice fields use these properties:

| property       | description                | type                              | required? |
| -------------- | -------------------------- | --------------------------------- | --------- |
| `:enum`        | List of available values   | zen/vector of values              | no        |
| `:sdc/options` | Alternative options source | aidbox.sdc.options/{valueset,rpc} | no        |

By default choice field uses `:enum` keyword for options definition.

> if you don't use :enum - you should confirm `#{aidbox.sdc.fhir/coding}` schema.

Example:

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :keys {:bp-measurement {:text "BP measurement site"
                         :confirms #{aidbox.sdc.fhir/coding}    ;; <-- if you use :enum to enumerate possible values - this confirms is optional
                         :enum [{:value {:text "Biceps left",
                                         :code "LA11158-5",
                                         :system "http://loinc.org"}}
                                {:value {:text "Biceps right",
                                         :code "LA11159-3",
                                         :system "http://loinc.org"}}]}}}

```

For simple cases you can freely use `:enum` keyword with options - but for complex one (big datasets/more flexibility) you also have 2 alternative sources for retreiving them:

* `:aidbox.sdc.options/valueset` use valuset stored in aidbox db
* `:aidbox.sdc.options/rpc` - use custom rpc

> It's better to use valuesets instead of `:enum`, they are more flexible for a long run. (ex: You will not have validation errors if change display of some value) you can add valuesets by using [FTR](https://github.com/Aidbox/documentation/blob/master/reference/aidbox-forms/terminology/fhir-terminology-repository/ftr-specification.md)

If you specify `:sdc/options :aidbox.sdc.options/valueset`, then you also must specify `:valueset` property.

| property   | description                        | type      | required? |
| ---------- | ---------------------------------- | --------- | --------- |
| `valueset` | lisp expr that returns ValueSet ID | lisp-expr | yes       |

Example:

Static valueset

```
:sdc/options :aidbox.sdc.options/valueset
:valueset "my-food-allergy-valueset"
```

Dynamic valueset

```
:sdc/options :aidbox.sdc.options/valueset
:valueset (if (= (get :type) "food")
            "my-food-allergy-valueset"
            "my-substance-allergy-valueset")
```

If you specify `:sdc/options :aidbox.sdc.options/rpc`, then you also must specify `:rpc` property.

| property   | description                       | type       | required? |
| ---------- | --------------------------------- | ---------- | --------- |
| `valueset` | ValueSet ID with list of choices  | zen/string | yes       |
| `rpc`      | rpc method invocation description | zen/map    | yes       |

RPC method invocations have next structure

| property   | description                      | type       | required? |
| ---------- | -------------------------------- | ---------- | --------- |
| `valueset` | ValueSet ID with list of choices | zen/string | yes       |
| `method`   | rpc method name                  | zen/symbol | yes       |
| `params`   | parameters map                   | zen/map    | yes       |

Each value of parameters-map should be valid lisp expression.

Example:

```
:sdc/options :aidbox.sdc.options/rpc
:rpc {:method my-ns/my-rpc
      :params {:p1 (+ 1 2)
               :p2 20}}
```

### reference field-type

Reference field-type contains aidbox reference to resource (id + resourceType + display)

Also field has search semantics based on full resource text search.

> To store reference - you should confirm `aidbox.sdc.fhir/reference` or `zenbox/Reference` schema.

reference fields should have these properties:

| property       | description                 | type   | required? |
| -------------- | --------------------------- | ------ | --------- |
| `resourceType` | name of table to query data | string | no        |

Example:

```
MyDocument
{:zen/tags #{aidbox.sdc/doc zen/schema}
 :keys {:patient {:text "Patient"
                  :type zen/map,
                  :confirms #{aidbox.sdc.fhir/reference},
                  :resourceType "Patient"}}}
```

### calculated field-type

Calculated field. Rule with same name should be used to calculate the result.

Calculated fields don't provide additional keys. Instead it depends on `:sdc/rules`

> NOTE: we have one restriction - calculated fields can't be in nested fields.

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc aidbox.sdc/rules},
 :keys {:height  {...}
        :weigth  {...}
        :bmi     {:text "BMI",
                  :type zen/number}}
 :sdc/rules {:bmi (divide (get :weigth)
                          (* (get :height)
                             (get :height)))}}
```

### attachment field-type

Attachment field can be used to attach a file to document. File can be stored in DB or in cloud bucket (currently AWS S3 and GCP buckets are supported)

> NOTE: We recommend storing files in the cloud, since storing files in the database takes too much space For cloud storage configuration [read here](document-dsl-docs-deprecated.md#store-attachments-in-cloud-storage)

Example:

```clojure
MyDocument
{:zen/tags #{aidbox.sdc/doc zen/schema}
 :keys {:pulse-image {:confirms #{aidbox.sdc.fhir/attachment}
                      :sdc-type aidbox.sdc/attachment}}}
```

You can apply some constraints such as file type, max size:

```clojure
MyDocument
{:zen/tags #{aidbox.sdc/doc zen/schema}
 :keys {:pulse-image {:confirms #{aidbox.sdc.fhir/attachment}
                      :sdc-type aidbox.sdc/attachment
					  :type zen/map
					  :keys {:contentType {:type zen/string
                                           :regex "image/*"}
                             :size {:type zen/integer
                                    :max 100000}}}}}
```

### QuestionnaireResponse convertions

If you need to convert SDCDocument to QuestionnaireResponse you need to specify additional field property

* `:linkId` - linkId of original Questionnaire (if not given key of the question will be taken)

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :keys {:bmi {:text "BMI",
              :type zen/number,
              :linkId "/39156-5"}},
 }
```

### SDCDocument extraction

If you would like to extract some values to Observation you should specify additional field property:

* `:code` - code/system of original Questionnaire question

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :keys {:bmi {:text "BMI",
              :type zen/number,
              :code [{:system "http://loinc.org" :code "39156-5"}]}},
 }
```

## Optional features

### Store attachments in cloud storage

Aidbox Forms support storing attachments in the cloud.

Supported cloud storages:

* AWS S3
* GCP Cloud Storage

> This feature can be configured via [api-constructor](../zen-related/api-constructor-docs-beta.md) in zen-project.

1. Configure your `aidbox/system` with `sdc-service` and it's configuration.

Example:

> Your zen-project entrypoint namespace (box.edn for example)

```
 box
 {:zen/tags #{aidbox/system}
  :zen/desc "test server"
  :services {:sdc sdc-service}}
```

2. Create [`AwsAccount`](../../../storage/aws-s3.md) or [`GcpServiceAccount`](../../../storage/gcp-cloud-storage.md)
3. Configure sdc-service with created account resource and specify a bucket name

Example:

```clojure
sdc-service
{:zen/tags   #{aidbox/service}
 :engine     aidbox.sdc/service
 :attachment {:bucket {:name "sdcattachments_bucket"
		       :account {:id "aws-account"
				 :resourceType "AwsAccount"}}}}
```
