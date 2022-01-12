---
description: Create a simple profile with zen
---

# Write a custom zen profile

{% hint style="info" %}
This article is work-in-progress. Please [contact us](../../contact-us.md) if you want to get details on how to create a custom zen profile.
{% endhint %}

Zen-lang profile schema must be tagged with `zen.fhir/profile-schema` , describe data structure in the [Aidbox format](../../modules-1/fhir-resources/aidbox-and-fhir-formats.md) and conform to the following schema:

{% code title="zen.fhir/profile-schema" %}
```
{:zen/tags #{zen/tag zen/schema}
 :type     zen/map
 :confirms #{zen.fhir/structure-schema}
 :keys     {:zen.fhir/type       {:type zen/string}
            :zen.fhir/profileUri {:type zen/string}}}
```
{% endcode %}

`zen.fhir/profile-schema` and every nested schema must conform to the following schema:

{% code title="zen.fhir/nested-schema" %}
```
{:zen/tags #{zen/schema}
 :type zen/map
 :keys {:fhir/flags {:type zen/set}
        :fhir/extensionUri {:type zen/string}
        :fhir/polymorphic {:type zen/boolean}
        :zen.fhir/reference {:type zen/map
                             :keys {:refers {:type zen/set
                                             :every {:type zen/symbol}}}}
        :zen.fhir/value-set {:type zen/map
                             :keys {:symbol {:type zen/symbol}
                                    :strength {:type zen/keyword
                                               :enum [{:value :required}
                                                      {:value :extensible}
                                                      {:value :preferred}
                                                      {:value :example}]}}}
        :keys {:type zen/map
               :values {:confirms #{nested-schema}}}
        :every {:confirms #{nested-schema}}}}
```
{% endcode %}

Description of used schema keys:

| key                         |          | description                                                                                                                          |
| --------------------------- | -------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| zen.fhir/type               | required | Used to find schema by matching against `resourceType` of incoming data                                                              |
| zen.fhir/profileUri         | required | Used to find schema by matching against `meta.profile` of incoming data                                                              |
| fhir/flags                  | optional | A set of keywords derived from FHIR ElementDefinition boolean attributes: isModifier (`:?!`), isSummary (`:SU`), mustSupport (`:MS`) |
| fhir/extensionUri           | optional | Used in Aidbox->FHIR format transformations to create `extension` element with `url` described in this key                           |
| fhir/polymorphic            | optional | Used in Aidbox->FHIR format transformations to detect whether the element is a choice type                                           |
| zen.fhir/reference          | optional | Used to specify resource types that can be referenced                                                                                |
| zen.fhir/reference.refers   | optional | set of symbols referring to other `zen.fhir/profile-schemas` or `zen.fhir/base-schemas`, used on reference validation                |
| zen.fhir/value-set.symbol   | optional | symbol referring to `zen.fhir/value-set` schema, used on validation to check data against a valueSet                                 |
| zen.fhir/value-set.strength | optional | keyword specifying strength of binding                                                                                               |

{% hint style="info" %}
To validate data against custom zen profiles make sure make sure namespace with profile zen-lang schemas is imported in `AIDBOX_ZEN_ENTRYPOINT` ns
{% endhint %}

### Examples

#### `:zen.fhir/reference`&#x20;

`Patient.managingOrganization` is a reference to an `Organiazation` resource:

{% code title="hl7-fhir-r4-core.Patient" %}
```clojure
 schema
 {#_...
  :keys
  {#_...
   :managingOrganization
   {:confirms #{hl7-fhir-r4-core.Reference/schema zen.fhir/Reference}
    :zen.fhir/reference {:refers #{hl7-fhir-r4-core.Organization/schema}}}}}
```
{% endcode %}

#### `zen.fhir/value-set`

To enable value-set validation in zen [concept resources](../../terminology/concept.md) with `valueset` attribute populated must be loaded into Aidbox.

`:zen.fhir/value-set` key must be a symbol tagged with `zen.fhir/value-set`&#x20;

{% code title="hl7-fhir-r4-core.Patient" %}
```clojure
 schema
 {#_...
  :keys
  {#_...
   :gender
   {#_...
    :zen.fhir/value-set
    {:symbol hl7-fhir-r4-core.value-set.administrative-gender/value-set
     :strength :required}}}}
```
{% endcode %}

`zen.fhir/value-set` symbol definition must contain `:uri` and  `:fhir/code-systems` attributes

`:uri` is a `ValueSet.url`, which must be mentioned in a `Concept.valueset.*`&#x20;

`:fhir/code-systems`\
&#x20; `:fhir/url` is a `CodeSystem.url`, which must be a value of `Concept.system`\
&#x20; `:zen.fhir/content` can have a value `:bundled` , this means that a `CodeSystem` content is accessible as Aidbox `Concept` resources. Other option is `:not-present`, Aidbox will skip validation of such concept assuming that the `CodeSystem` content is not loaded into Aidbox

{% code title="hl7-fhir-r4-core.value-set.administrative-gender" %}
```clojure
  value-set
  {:zen/tags #{zen.fhir/value-set},
   #_...
   :uri "http://hl7.org/fhir/ValueSet/administrative-gender",
   :fhir/code-systems #{{:fhir/url "http://hl7.org/fhir/administrative-gender",
                         :zen.fhir/content :bundled}}}
```
{% endcode %}

#### `:slicing`

`:slicing` is a zen-lang instruction which allows to validate a part of some array with a zen-schema. Such part is called a slice.

Before applying `:schema` to a slice zen needs to determine elements of the array this slice consists of, this logic is described using `:filter` key.\
\
`:filter` must specify an `:engine`. Currently there're two engines available `:zen` and `:match`

`:zen` engine matches elements against specified zen schema

`:match` engine is a pattern matching DSL:

* primitive (e.g. string or a number): constant, i.e. literal match of a value&#x20;
* `{}` : objects validated with this match must contain and conform to every key from this pattern
* `#{}` : at least one element of an array must conform each element of this pattern

From example below: `:match {:code "29463-7", :system "http://loinc.org"}` means that elements with specified code and systems will be considered as a part of the slice.

`:schema` is a zen schema which will be applied to a slice.

Following example describes that in the `code.coding` array there is a slice called `BodyWeightCode` consisting of exactly one element with `code`=`"29463-7"` and `system`=`"http://loinc.org"`:

{% code title="hl7-fhir-r4-core.bodyweight" %}
```clojure
 schema
 {#_...
  :keys
  {#_...
   :code
   {:confirms #{hl7-fhir-r4-core.CodeableConcept/schema}
    :type zen/map
    :keys
    {:coding
     {:type zen/vector
      :every {:confirms #{hl7-fhir-r4-core.Coding/schema}}
      :slicing
      {:slices
       {"BodyWeightCode"
        {:filter
         {:engine :match
          :match {:code "29463-7", :system "http://loinc.org"}}
         :schema
         {:type zen/vector
          :minItems 1
          :maxItems 1
          :every
          {:type zen/map
           :require #{:system :code}}}}}}}}}}}
```
{% endcode %}
