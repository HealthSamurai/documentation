# Migrate custom resources defined with Zen to FHIR Schema

## Prerequisites

The materials in this section describe what to do next with the resulting FHIR Schema resource or set of resources. They explain how to load them into Aidbox, either one by one or via IG. Additionally, they outline the requirements for FHIR Schema to define a Custom Resource and provide a detailed FHIR Schema specification.

{% content-ref url="../../../modules-1/profiling-and-validation/fhir-schema-validator.md" %}
[fhir-schema-validator.md](../../../modules-1/profiling-and-validation/fhir-schema-validator.md)
{% endcontent-ref %}

{% content-ref url="../custom-resources-using-fhirschema.md" %}
[custom-resources-using-fhirschema.md](../custom-resources-using-fhirschema.md)
{% endcontent-ref %}

{% content-ref url="../../../modules-1/profiling-and-validation/fhir-schema-validator/tutorials/how-to-create-fhir-npm-package.md" %}
[how-to-create-fhir-npm-package.md](../../../modules-1/profiling-and-validation/fhir-schema-validator/tutorials/how-to-create-fhir-npm-package.md)
{% endcontent-ref %}

{% content-ref url="../../../modules-1/profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../../../modules-1/profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/)
{% endcontent-ref %}

## Obtain FHIRSchema

In the following sections, we will describe how to obtain the FHIR Schema from a Zen resource definition.&#x20;

## Semi-Automated approach

You can use a special REST API endpoint to migrate specific Zen definitions to the FHIR schema. The endpoint requires a `Zen namespace` and `symbol` under which the custom resource definition is described. Internally, it attempts to compile the entity to the FHIR schema.

```http
GET /ZenSchema/<zen-namespace>/<zen-definition-symbol>/$dump-as-fhir-schema
```

If the compilation succeeds, the endpoint returns the resulting FHIR Schema. If it fails, it provides a list of errors detailing what went wrong. Most errors occur because you've used keys on your Zen definition that are not recognized by our compiler. In this case, you can [contact us](../../../contact-us.md) to potentially extend the compiler. Alternatively, you can manually rewrite your custom resource using FHIR Schema.

{% hint style="danger" %}
Please double-check the compilation results, as they may contain transformation errors. This tool is intended to reduce manual effort during the migration process and is not meant for fully automated resource migration.
{% endhint %}

#### Example: EmailSchedule custom resource

Here's an example of migrating a custom resource `EmailSchedule`that describes the schedule for email notifications about appointments. Let's imagine it is described in `main` namespace of our [Aidbox configuration project](../custom-resources-using-aidbox-project.md).

```javascript
{ns main
    import #{aidbox
             zenbox
             zen.fhir}

    EmailSchedule
    {:zen/tags #{zen/schema zenbox/persistent zen.fhir/base-schema}
     :zen.fhir/type "EmailSchedule"
     :zen.fhir/version "0.5.3"
     :resourceType "EmailSchedule"
     :type zen/map
     :confirms #{zenbox/Resource}
     :require  #{:reports}
     :keys {:name {:type zen/string}
            :days {:type zen/vector :every {:type zen/string
                                            :enum [{:value "mon"}
                                                   {:value "tue"}
                                                   {:value "wed"}
                                                   {:value "thu"}
                                                   {:value "fri"}
                                                   {:value "sat"}
                                                   {:value "sun"}]}}
            :parameters {:type zen/any}
            :time {:type zen/string}
            :timezone {:type zen/string}
            :reports {:type zen/vector
                      :minItems 1
                      :every {:type zen/map
                              :require #{:enabled :fileFormat :campaignTypes :reportType}
                              :keys {:enabled {:type zen/boolean}
                                     :reportType {:type zen/string :enum [{:value "campaign"} {:value "summary"} {:value "combined-campaign"}]}
                                     :fileFormat {:type zen/map
                                                  :keys {:csvPartial {:type zen/boolean}
                                                         :pdfPartial {:type zen/boolean}
                                                         :csvFull {:type zen/boolean}
                                                         :csv {:type zen/boolean}
                                                         :pdf {:type zen/boolean}}}
                                     :campaignTypes {:type zen/vector
                                                     :minItems 1
                                                     :every {:type zen/string
                                                             :enum [{:value "appointment-reminder"}
                                                                    {:value "appointment-notification"}
                                                                    {:value "appointment-noshow"}
                                                                    {:value "appointment-alert"}
                                                                    {:value "recall"}
                                                                    {:value "broadcast"}]}}}}}
            :recipients {:type zen/vector :every {:type zen/string}}}}

    box
    {:zen/tags #{aidbox/system}}}
```

Convert this `Zen definition` to `FHIRSchema` using API:

{% tabs %}
{% tab title="Request" %}
```yaml
GET /ZenSchema/main/EmailSchedule/$dump-as-fhir-schema
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```json
{
  "derivation": "specialization",
  "id": "EmailSchedule",
  "kind": "resource",
  "datatype": "EmailSchedule",
  "url": "EmailSchedule",
  "elements": {
    "timezone": {
      "type": "string",
      "datatype": "string"
    },
    "reports": {
      "array": true,
      "min": 1,
      "elements": {
        "enabled": {
          "type": "boolean",
          "datatype": "boolean"
        },
        "reportType": {
          "type": "string",
          "datatype": "string",
          "constraints": {
            "enum-8": {
              "expression": "%context.subsetOf('campaign' | 'summary' | 'combined-campaign')",
              "severity": "error"
            }
          }
        },
        "fileFormat": {
          "elements": {
            "csvPartial": {
              "type": "boolean",
              "datatype": "boolean"
            },
            "pdfPartial": {
              "type": "boolean",
              "datatype": "boolean"
            },
            "csvFull": {
              "type": "boolean",
              "datatype": "boolean"
            },
            "csv": {
              "type": "boolean",
              "datatype": "boolean"
            },
            "pdf": {
              "type": "boolean",
              "datatype": "boolean"
            }
          }
        },
        "campaignTypes": {
          "array": true,
          "min": 1,
          "type": "string",
          "datatype": "string",
          "constraints": {
            "enum-9": {
              "expression": "%context.subsetOf('appointment-reminder' | 'appointment-notification' | 'appointment-noshow' | 'appointment-alert' | 'recall' | 'broadcast')",
              "severity": "error"
            }
          }
        }
      },
      "required": [
        "fileFormat",
        "reportType",
        "campaignTypes",
        "enabled"
      ]
    },
    "id": {
      "type": "string",
      "datatype": "string"
    },
    "name": {
      "type": "string",
      "datatype": "string"
    },
    "recipients": {
      "array": true,
      "min": 1,
      "type": "string",
      "datatype": "string"
    },
    "parameters": {
       "any": true
    },
    "time": {
      "type": "string",
      "datatype": "string"
    },
    "days": {
      "array": true,
      "type": "string",
      "datatype": "string",
      "constraints": {
        "enum-7": {
          "expression": "%context.subsetOf('mon' | 'tue' | 'wed' | 'thu' | 'fri' | 'sat' | 'sun')",
          "severity": "error"
        }
      }
    },
    "meta": {
      "additionalProperties": {
        "any": true
      }
    },
    "resourceType": {
      "type": "string",
      "datatype": "string"
    }
  },
  "required": [
    "reports"
  ],
  "type": "EmailSchedule"
}
```
{% endcode %}


{% endtab %}
{% endtabs %}

## Manual Approach

If the compiler doesn't support certain instructions for your Zen definitions, if you want full control during the migration process, or if you want to extend your custom resources using unique FHIR Schema features, you can manually rewrite your custom resource definitions from scratch using the FHIR Schema.

In the following steps, we will use the same custom resource example, `EmailSchedule`. Although it is just an example, it covers most aspects of resource definition with Zen. If this guide misses any features of Zen resource definition, please [contact us.](https://docs.aidbox.app/overview/contact-us)

**Get Zen definition**

Open your [Aidbox configuration project](../../../aidbox-configuration/aidbox-zen-lang-project/) and find custom resource definitions. It should look similar to `EmailSchedule` definition example.&#x20;

### Translate zen definition `:keys` to FHIRSchema element entry

The FHIRSchema `elements` is similar to `:keys` field in Zen definition, serving the same purpose: defining properties for a resource.&#x20;

For example, we have `name` key in `EmailSchedule` custom resource:

```yaml
{...
:keys {:name {:type zen/string}}
...}
```

It states that the `name` property must be a `string`. This property is located on the top level, indicating it is the root field in the resource.&#x20;

To describe it as a FHIRSchema element, do the following:

```json
{"elements": {"name": {"type": "string"}}}
```

For nested keys like `reports.enabled` in the `EmailSchedule` resource, nest the "elements" statements accordingly. Note that "elements" repeats each time you nest a property:

```json
{"elements": {"name": {"type": "string"}
              "reports": {"elements": {"enabled": {"type": "boolean"}},
                          "array": true}}}
```

**Handling Enums**

Enums (`:enum`) were a handy tool for limiting possible values directly within the Zen definition model. However, this approach conflicts with FHIR, which has its own well-defined methods for limiting value sets.

_**Using FHIRPath Constraints**_

Enums can be described using FHIRPath constraints. Here's an example of `days` property in `EmailSchedule` resource:

```json
{"elements":
 "days": {
    "array": true,
    "type": "string",
    "constraints": {
    "<your-constraint-id>": {
      "expression": "%context.subsetOf('mon' | 'tue' | 'wed' | 'thu' | 'fri' | 'sat' | 'sun')",
      "severity": "error"
    }}}}
```

Constraint id must be unique across one resource definition. While this method allows for defining possible values in place, it has downsides. It makes it difficult to reuse or implement lookups for these values. Here is the FHIRSchema reference specification describing the [constraints](https://fhir-schema.github.io/fhir-schema/reference/constraint.html) property.

_**Using ValueSets**_

Alternatively, you can create a ValueSet and bind it to the coded value. FHIR resources extensively utilize this approach. This involves creating a ValueSet as a resource and delivering it to an external terminology server. However, this is beyond the scope of this guide.

```json
{"elements":
 {"days":
  {"type": "string",
   "binding": {"valueSet": "<your-vs-url>",
               "strength": "required"}}}}
```

In FHIR, terminology bindings occur in four gradations: required, extensible, preferred, and example. The FHIRSchema validator checks bindings only with the strength of "required"; all other binding strengths are ignored.

This approach leverages FHIR's capabilities for managing value sets, ensuring consistency and reusability across different resources.

#### **Handling** `zen/any`

The `zen/any` type for a Zen definition indicates that the value is arbitrary and may be of any type.

**Example:** `parameters` property in `EmailSchedule` resource

```yaml
{...
:keys {:parameters {:type zen/any}}
...}
```

**FHIRSchema Equivalent**

To describe `zen/any` use `additionalProperties` with `any`

```json
{"elements":
 {"parameters": {"any": true}}}
```

For more details about these FHIRSchema instructions, please refer to the reference specification [section](https://fhir-schema.github.io/fhir-schema/reference/extensions.html).

#### **Handling `:require`**   &#x20;

The `:require` instruction indicates that the described property is required and must be present in a resource instance.

**Example:** `reports` property in `EmailSchedule` resource

```yaml
{...
:require  #{:reports}
:keys {:reports {:type zen/vector
                ...}}
...}
```

**FHIRSchema Equivalent**

To express `:require` in FHIRSchema, use the `required` property at the same level as `elements`. It is an array of fields that must be present in the data instance.

```json
{"required": ["reports"],
 "elements":
 {"reports": {"array": true,
              ...}}
```

For more information about this instruction, please refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#requires-and-exclusions) in the FHIRSchema specification.

#### Handling `zen/vector`

The `zen/vector` type indicates that the described property represents a collection of items.

**Example:** `recipients` property in `EmailSchedule` resource

```yaml
{...
:keys {:recipients {:type zen/vector :every {:type zen/string}}}
...}
```

This definition specifies that `recipients` is a property within the `EmailSchedule` resource, where each item in the collection is of type string.

**FHIRSchema Equivalent**

To express `zen/vector` in FHIRSchema, use `array: true`.

```json
{"elements":
 {"recipients": {"array": true, "type": "string"}}
```

For more information about this instruction, please refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#shape) in the FHIRSchema specification.

### Resulting FHIRSchema

```json
{ "derivation": "specialization",
  "id": "EmailSchedule",
  "kind": "resource",
  "datatype": "EmailSchedule",
  "url": "EmailSchedule",
  "required": [
    "reports"
  ],
  "type": "EmailSchedule",
  "elements": {
    "name": {
      "type": "string"
    },
    "parameters": {"any": true},
    "recipients": {"array": true, "type": "string"},
    "days": {
      "array": true,
      "type": "string",
      "constraints": {
        "enum-7": {
          "expression": "%context.subsetOf('mon' | 'tue' | 'wed' | 'thu' | 'fri' | 'sat' | 'sun')",
          "severity": "error"
        }
      }
    }
  }
}
```
