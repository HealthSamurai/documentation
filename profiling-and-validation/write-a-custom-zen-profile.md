---
description: Create a simple profile with zen
---

# Write a custom zen profile

{% hint style="info" %}
This article is work-in-progress. Please [contact us](../contact-us.md) if you want to get details on how to create a custom zen profile.
{% endhint %}

Zen-lang profile schema must be tagged with `zen.fhir/profile-schema` , describe data structure in the [Aidbox format](../modules-1/fhir-resources/aidbox-and-fhir-formats.md) and conform to the following schema:

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
| zen.fhir/reference.refers   | optional | set of symbols referring to other `zen.fhir/profile-schemas` or `zen.fhir/base-schemas`, used on reference validation                |
| zen.fhir/value-set.symbol   | optional | symbol referring to `zen.fhir/value-set` schema, used on validation to check data against a valueSet                                 |
| zen.fhir/value-set.strength | optional | keyword specifying strength of binding                                                                                               |

{% hint style="info" %}
To validate data against custom zen profiles make sure make sure namespace with profile zen-lang schemas is imported in `AIDBOX_ZEN_ENTRYPOINT` ns
{% endhint %}
