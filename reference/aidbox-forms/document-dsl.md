# Document DSL



* Document is schema for Questionnaire with questions and answers and types of values.
* Document is used as source of meta-information by [FormLayout](layout-dsl.md)
* Document resources stored in `SDCDocument` table.
* Document can define computed fields via rules.



## Marking schema as SDC `Document` entity

Document definition should be marked with `aidbox.sdc/doc` tag, and also tagged as `zen/schema` - because it's already schema.

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc}}
```

And after that you can define your custom properties of the document and it's meta information (like source of the document)

Document source defined as fhir `Coding` like structure, with `:system` and `:code` properties

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
 :keys {:loinc-8310-5 {:questionCodeSystem "http://loinc.org",
                       :linkId "/8310-5",
                       :confirms #{hl7-fhir-r4-core.Quantity/schema},
                       :boundaries {:imperial {:min 86, :max 105},
                                    :metric {:min 30, :max 42}},
                       :type zen/map,
                       :zen/desc "Body temperature",
                       :keys {:value {:type zen/number}},
                       :units [{:name "°F"} {:name "°C"}],
                       :sdc-type aidbox.sdc/quantity,
                       :text "Body temperature"}}}
```

Every document field should confirms `aidbox.sdc/super-field` schema.

For details see [**Document fields definition**](document-dsl.md#document-field-definition) section.

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
 :sdc/rules {:loinc-39156-5 (when (and
                                          (get-in [:loinc-29463-7])
                                          (get-in [:loinc-8302-2]))
                             (* (divide
                                        (* (get-in [:loinc-29463-7]) 703)
                                        (* (get-in [:loinc-8302-2])
                                                (get-in [:loinc-8302-2])))
                                     1))},
 :keys {:loinc-39156-5 {:questionCodeSystem "http://loinc.org",
                        :linkId "/39156-5",
                        :type zen/number,
                        :sdc-type aidbox.sdc/calculated,
                        :text "BMI",
                        :zen/desc "BMI"}}}
```

* name of the rule (`:loinc-39156-5`) should be matched with name of the field.
* Also to mark field as calculated you should add `:sdc-type aidbox.sdc/calculated` to it

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

* `aidbox.sdc/quantity` - fields with measurement units
* `aidbox.sdc/choice` - fields with answers, located in valuesets
* `aidbox.sdc/calculated` - fields, which need to be calculated via rules engine

{% hint style="info" %}
You can Create your own field-types, if necessary. But in that way you are obligated to add support for them in the Layout engine
{% endhint %}

Every field-type satisfies `aidbox.sdc/super-field` tag schema. Its in the core of every field-type.

### super-field schema

Field-type definition.

Schema contains several aspects of Document (Field) lifecycle

* how it stored in DB
* how it validates
* how it appeares in UI layout
* what the source of the field

Can contain sub-fields or behaves like an array. Extendable via `:sdc-type` subtyping. Also has links to original sources of questions.

super-field schema has next keys:

| property               | description                                                          | type                | required? |
| ---------------------- | -------------------------------------------------------------------- | ------------------- | --------- |
| `type`                 | zen/type of the value, which will be store in DB                     | symbol              | yes       |
| `confirms`             | additional schemas for stored value confirmation                     | set of symbols      | no        |
| `keys`                 | When field is complex object(map) - used for defining nested fields  | map of super-fields | no        |
| `every`                | When field is coll (zen/vector) - used for defining coll-item schema | super-field         | no        |
| `text`                 | Question text                                                        | string              | no        |
| `help`                 | Question explanation or some hint - how to interpret it              | string              | no        |
| `linkId`               | Link to original questing (in questionnaire or different resource)   | string              | no        |
| `questionCodeSystem`   | CodeSystem of the question                                           | string              | no        |
| `sdc/display-when`     | Rule for Layout - when need to display current field                 | lisp/expr           | no        |
| `sdc/disable-when`     | Rule for Layout - when disable edition of current field              | lisp/expr           | no        |
| `last-change-datetime` | Meta field - contains datetime of last changes                       | any                 | no        |
| `sdc-type`             | field sub-type - used for defining additional fields/constrains      | symbol              | no        |

{% hint style="danger" %}
This schema may change in the future - there are work on simplification.
{% endhint %}

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
 :keys {:temp {:type zen/map,
               :sdc-type aidbox.sdc/quantity,
               :confirms #{hl7-fhir-r4-core.Quantity/schema},
               :questionCodeSystem "http://loinc.org",
               :linkId "/8310-5",
               :keys {:value {:type zen/number}},
               :units [{:name "°F"} {:name "°C"}],
               :text "Body temperature"}}}
```

### choice field-type

Choise field - references valueset with answers

Choice schema has next keys:

| property       | description                | type        | required? |
| -------------- | -------------------------- | ----------- | --------- |
| `:enum`        | List of available values   | zen/keyword | no        |
| `:sdc/options` | Alternative options source | zen/keyword | no        |

By default choice field uses `:enum` keyword for options definition.

Example:

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :keys {:bp-measurement {:type zen/map,
                         :sdc-type aidbox.sdc/choice,
                         :questionCodeSystem "http://loinc.org",
                         :linkId "/41904-4",
                         :keys {:text {:type zen/string},
                                :code {:type zen/string},
                                :system {:type zen/string}},
                         :enum [{:value {:text "Biceps left",
                                         :code "LA11158-5",
                                         :system "http://loinc.org"}}
                                {:value {:text "Biceps right",
                                         :code "LA11159-3",
                                         :system "http://loinc.org"}}],
                         :text "BP measurement site"}}}

```

If by some reason you can't use zen-defined `:enum` keyword with options - you also have 2 alternative sources for retreiving them:

* `:aidbox.sdc.options/valueset` use valuset stored in aidbox db
* `:aidbox.sdc.options/rpc` - use custom rpc

If you specify `:sdc/options :aidbox.sdc.options/valueset`, then you also must specify `:valueset` property.

| property   | description                      | type       | required? |
| ---------- | -------------------------------- | ---------- | --------- |
| `valueset` | ValueSet ID with list of choices | zen/string | yes       |

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

###

\


### calculated field-type

Calculated field. Rule with same name should be used to calculate the result.

Calculated fields don't provide additional keys. Instead it depends on `:sdc/rules`

```
MyDocument
{:zen/tags #{zen/schema aidbox.sdc/doc},
 :keys {:height  {...}
        :weigth {...}
        :bmi {:sdc-type aidbox.sdc/calculated,
              :type zen/number,
              :questionCodeSystem "http://loinc.org",
              :linkId "/39156-5",
              :text "BMI",
              :zen/desc "BMI"}},
 :sdc/rules {:bmi (* (divide (* (get-in [:weigth]) 703)
                             (* (get-in [:height]) (get-in [:height])))
                     1)}}
```
