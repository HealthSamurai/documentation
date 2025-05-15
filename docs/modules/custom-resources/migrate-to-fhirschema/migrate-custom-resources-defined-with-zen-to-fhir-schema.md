# Migrate custom resources defined with Zen to FHIR Schema

## Prerequisites

The materials in this section describe what to do next with the resulting FHIR Schema resource or set of resources. They explain how to load them into Aidbox, either one by one or via IG. Additionally, they outline the requirements for FHIR Schema to define a Custom Resource and provide a detailed FHIR Schema specification.

{% content-ref url="../../profiling-and-validation/fhir-schema-validator/" %}
[fhir-schema-validator](../../profiling-and-validation/fhir-schema-validator/README.md)
{% endcontent-ref %}

{% content-ref url="../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md" %}
[how-to-create-fhir-npm-package.md](../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md)
{% endcontent-ref %}

{% content-ref url="../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/README.md" %}
[upload-fhir-implementation-guide](../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/README.md)
{% endcontent-ref %}

## Obtain FHIRSchema

In the following sections, we will describe how to obtain the FHIR Schema from a Zen resource definition.&#x20;

## Semi-Automated approach

You can use a special REST API endpoint to migrate specific Zen definitions to the FHIR schema. The endpoint requires a `Zen namespace` and `symbol` under which the custom resource definition is described. Internally, it attempts to compile the Zen definition to the FHIR schema.

```http
GET /ZenSchema/<zen-namespace>/<zen-definition-symbol>/$dump-as-fhir-schema
```

If the compilation succeeds, the endpoint returns the resulting FHIR Schema. If it fails, it provides a list of errors detailing what went wrong. Most errors occur because you've used keys on your Zen definition that are not recognized by our compiler. In this case, you can [contact us](../../../overview/contact-us.md) to potentially extend the compiler. Alternatively, you can manually rewrite your custom resource using FHIR Schema.

{% hint style="danger" %}
Please double-check the compilation results, as they may contain transformation errors. This tool is intended to reduce manual effort during the migration process and is not meant for fully automated resource migration.
{% endhint %}

#### Example: EmailSchedule custom resource

Here's an example of migrating a custom resource `EmailSchedule`that describes the schedule for email notifications about appointments. Let's imagine it is described in `main` namespace of our [Aidbox configuration project](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/custom-resources-using-aidbox-project.md).

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
            :data {:type zen/any}
            :parameters {:type zen/map :validation-type :open}
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
  "id": "",
  "name": "",
  "kind": "resource",
  "url": "EmailSchedule",
  "base": "Resource",
  "elements": {
    "timezone": {
      "type": "string"
    },
    "reports": {
      "array": true,
      "min": 1,
      "elements": {
        "enabled": {
          "type": "boolean"
        },
        "reportType": {
          "type": "string",
          "constraints": {
            "enum-5": {
              "expression": "%context.subsetOf('campaign' | 'summary' | 'combined-campaign')",
              "severity": "error"
            }
          }
        },
        "fileFormat": {
          "elements": {
            "csvPartial": {
              "type": "boolean"
            },
            "pdfPartial": {
              "type": "boolean"
            },
            "csvFull": {
              "type": "boolean"
            },
            "csv": {
              "type": "boolean"
            },
            "pdf": {
              "type": "boolean"
            }
          }
        },
        "campaignTypes": {
          "array": true,
          "min": 1,
          "type": "string",
          "constraints": {
            "enum-6": {
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
      "type": "string"
    },
    "name": {
      "type": "string"
    },
    "recipients": {
      "array": true,
      "min": 1,
      "type": "string"
    },
    "time": {
      "type": "string"
    },
    "days": {
      "array": true,
      "type": "string",
      "constraints": {
        "enum-4": {
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
      "type": "string"
    },
    "parameters": {
      "additionalProperties": {
        "any": true
      }
    },
    "data": {
      "any": true
    }
  },
  "required": [
    "reports"
  ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

## Manual Approach

If the compiler doesn't support certain instructions for your Zen definitions, if you want full control during the migration process, or if you want to extend your custom resources using unique FHIR Schema features, you can manually rewrite your custom resource definitions from scratch using the FHIR Schema.

In the following steps, we will use the same custom resource example, `EmailSchedule`. Although it is just an example, it covers most aspects of resource definition with Zen. If this guide misses any features of Zen resource definition, please [contact us.](../../../overview/contact-us.md)

**Get Zen definition**

Open your [Aidbox configuration project](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/README.md) and find custom resource definitions. It should look similar to `EmailSchedule` definition example.&#x20;

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

#### **Handling** `:validation-type :open`

The `:validation-type :open` with `:type zen/map` instruction for a Zen definition indicates that value is an arbitrary map with arbitrary key/value pairs.

**Example:** `parameters` property in `EmailSchedule` resource

```yaml
{...
:keys {:parameters {:type zen/map :validation-type :open}}
...}
```

**FHIRSchema Equivalent**

With FHIRSchema, you can express identical semantics using `additionalProperties` and `any: true`. However, note that resource definitions using these instructions are not compatible with FHIR. This means you cannot convert FHIRSchema with these instructions back to StructureDefinition, as FHIR doesn't naturally describe arbitrary key/value nodes.

To express `:validation-type :open` with `:type zen/map` in FHIRSchema:

```json
{
  "elements": {
    "parameters": {
      "additionalProperties": {
        "any": true
      }
    }
  }
}
```

* `additionalProperties` indicates that any property not described in `elements` will be validated against the schema provided in `additionalProperties`.
* `any: true` indicates that the value is arbitrary and may be of any type.

You can also include more complex schemas within `additionalProperties`. For example:

```json
{"elements": {
    "parameters": {
      "elements": {
        "someField": {
          "type": "integer"
        }
      },
      "additionalProperties": {
        "type": "string",
        "constraints": {
          // Define constraints here
        }
      }
    }
  }
}
```

This means any keys not described in `elements` must have a string value and pass the defined constraints. For more details about these FHIRSchema instructions, please refer to the reference specification [section](https://fhir-schema.github.io/fhir-schema/reference/extensions.html).

#### **Handling** `zen/any`

The `zen/any` type for a Zen definition indicates that the value is arbitrary and may be of any type.

**Example:** `data` property in `EmailSchedule` resource

```yaml
{...
:keys {:data {:type zen/any}}
...}
```

**FHIRSchema Equivalent**

To describe `zen/any` use `any` without `additionalProperties`.&#x20;

```json
{"elements":
 {"data": {"any": true}}}
```

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

**Handling `:refers`**

The `:refers` instruction indicates that a node will contain a reference to another resource and limits the possible target resource types to a predefined list.

**Example:**

```clojure
{...
:keys {:organization {:confirms #{zenbox/Reference}
                      :zen.fhir/reference {:refers #{my-resources.organization/Organization}}}}
...}

```

In this example, the `organization` property refers to a resource of type `Organization`.

**FHIRSchema Equivalent**

To express this in FHIRSchema, use the `refers` instruction and specify the "Reference" type. This will be handled as a regular FHIR reference. Additionally, in the `refers` property, you can reference not only resource types but also profiles on some resources.

```
{
  "elements": {
    "organization": {
      "type": "Reference",
      "refers": ["Organization"]
    }
  }
}
```

In this schema, `organization` is defined as a reference to resources of type `Organization`.

For more information about this instruction, refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#reference-target) of the FHIR Schema reference specification.

**Handling `:fhir/polymorphic`**

The `:fhir/polymorphic` instruction indicates a choice of datatypes that allows values of different types for specific fields.

**Example:**

```clojure
{...
 :keys {:effective
         {:type zen/map
          :fhir/polymorphic true
          :exclusive-keys #{#{:dateTime :Period}}
          :keys {:dateTime {:confirms #{my-types/DateTime}}
                 :period {:confirms #{my-types/Period}}}}}
...}

```

In this example, the `effective` property can be either `effectiveDateTime` or `effectivePeriod`.

**FHIRSchema Equivalent**

To express this in FHIRSchema, use the `choiceOf` instruction:

```
{
  "elements": {
    "effectiveDateTime": {
      "choiceOf": "effective",
      "type": "DateTime"
    },
    "effectivePeriod": {
      "type": "Period",
      "choiceOf": "effective"
    }
  }
}
```

For more information about this instruction, refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#choice-type) of the FHIR Schema reference specification.

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
    "data": {"any": true},
    "parameters": {
      "additionalProperties": {
        "any": true
      }
    },
    "organization": {
      "type": "Reference",
      "refers": ["Organization"]
    },
    "effectiveDateTime": {
      "choiceOf": "effective",
      "type": "DateTime"
    },
    "effectivePeriod": {
      "type": "Period",
      "choiceOf": "effective"
    },
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

**Handling resulting FHIR Schemas**

To deliver the FHIR Schema(s) and related Entities you authored to Aidbox, follow these steps. 
Ensure that your Aidbox is configured to run with the FHIRSchema validation engine. 
Here's [a guide describing how to achieve that](../../profiling-and-validation/fhir-schema-validator/README.md).

**Single FHIRSchema Delivery**

If you have only one FHIRSchema that replaces your custom-defined Zen definition, 
follow [this guide](../custom-resources-using-fhir-schema.md) to deliver a single FHIRSchema to Aidbox.

**Multiple Schemas as a Package**

If you have multiple schemas replacing a set of resources and want to work with this set of entities as a package (ImplementationGuide), refer to this guide on how to create your own FHIR NPM package with ImplementationGuide entities.

{% content-ref url="../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md" %}
[how-to-create-fhir-npm-package.md](../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md)
{% endcontent-ref %}

**Loading the FHIR NPM Package**

{% content-ref url="../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/README.md)
{% endcontent-ref %}

**Important Notes:**

{% hint style="warning" %}
**Schema Precedence:** Uploaded FHIRSchemas with your resource definitions have higher precedence than Zen definitions, so validation will be performed using the FHIRSchema. You can delete your Zen definition after creating the FHIRSchema with resource definitions.
{% endhint %}

{% hint style="danger" %}
**SearchParameters:** SearchParameters described for custom resources won't work in FHIRSchema validation mode. You need to redefine them as regular FHIR SearchParameters, not Aidbox Search Parameters. See the [migration guide](../../../tutorials/crud-search-tutorials/search-tutorials/migrate-from-aidbox-searchparameter-to-fhir-searchparameter.md).
{% endhint %}
