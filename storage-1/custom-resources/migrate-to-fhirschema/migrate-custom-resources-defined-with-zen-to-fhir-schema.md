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



## Semi-Automated approach

You can use a special REST API endpoint to migrate specific Zen definitions to the FHIR schema. The endpoint requires a `Zen namespace` and `symbol` under which the custom resource definition is described. Internally, it attempts to compile the entity to the FHIR schema.

```http
GET /ZenSchema/<zen-namespace>/<zen-definition-symbol>/$dump-as-fhir-schema
```

If the compilation succeeds, the endpoint returns the resulting FHIR Schema. If it fails, it provides a list of errors detailing what went wrong. Most errors occur because you've used keys on your Zen definition that are not recognized by our compiler. In this case, you can [contact us](../../../contact-us.md) to potentially extend the compiler. Alternatively, you can manually rewrite your custom resource using FHIR Schema.

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
            :recipients {:type zen/vector :minItems 1 :every {:type zen/string}}}}

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

Let's take the same EmailSchedule custom resource and try to convert it to FHIRSchema

### Translate zen definition `:keys` to FHIRSchema element entry

For example, we have `name` key in `EmailSchedule` custom resource:

```yaml
{...
:keys {:name {:type zen/string}}
...}
```

To describe it as a FHIRSchema element, do the following:

```json
{"elements": {"name": {"type": "string"}}}
```

For nested keys simply nest "elements" statements like this:

```json
{"elements": {"name": {"type": "string"}
              "reports": {"elements": {"enabled": {"type": "boolean"}},
                          "array": true}}}
```

#### What to do with `:enum`

Enums can be described using FHIRPath constraints, here's an example:

```json
{"elements":
 "days": {
    "array": true,
    "type": "string",
    "datatype": "string",
    "constraints": {
    "<your-constraint-id>": {
      "expression": "%context.subsetOf('mon' | 'tue' | 'wed' | 'thu' | 'fri' | 'sat' | 'sun')",
      "severity": "error"
    }}}}
```

Alternatively, you can create a ValueSet and bind it to the coded value.

```json
{"elements":
 {"days":
  {"type": "string",
   "binding": {"valueSet": "<your-vs-url>",
               "strength": "required"}}}}
```

#### What to do with `zen/any`

```yaml
{...
:keys {:parameters {:type zen/any}}
...}
```

To describe `zen/any` use `additionalProperties` with `any`

```json
{"elements":
 {"parameters": {"additionalProperties": {"any": true}}}}
```

#### What to do with `:require`

```yaml
{...
:require  #{:reports}
:keys {:reports {:type zen/vector
                ...}}
...}
```

To describe `:require` use `required` that sits on the same level with `elements`:

```json
{"required": ["reports"],
 "elements":
 {"reports": {"array": true,
              ...}}
```

#### What to do with `zen/vector`

```yaml
{...
:keys {:days {:type zen/vector :every {:type zen/string}}}
...}
```

To describe `isCollection` use `array: true`

```json
{"elements":
 {"days": {"array": true, "type": "string"}}
```

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
    "parameters": {"additionalProperties": {"any": true}},
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
    }
  }
}
```
