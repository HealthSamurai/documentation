# Migrate custom resources defined with Entity & Attributes to FHIR Schema

## Pre requisites

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

You can use a special REST API endpoint to migrate specific Entities and their corresponding Attributes to FHIR Schema. The endpoint requires an `entity ID` to process it. Internally, it attempts to compile the entity to FHIR Schema.

```http
GET /Entity/<entity-id>/$dump-as-fhir-schema
```

If the compilation succeeds, the endpoint returns the resulting FHIR Schema. If it fails, it provides a list of errors detailing what went wrong. Most errors occur because you've used keys on your Attributes/Entities that are not recognized by our compiler. In this case, you can [contact us](../../../contact-us.md) to potentially extend the compiler. Alternatively, you can manually rewrite your custom resource using FHIR Schema.

{% hint style="danger" %}
The compiler inlines types from Entity/Attributes as they are. At Aidbox runtime, the validator tries to interpret them as FHIR types.

If your resource relies on another custom resource type that clashes with a FHIR type, migrate your other custom resource first. Give it a unique name that does not clash with FHIR types.
{% endhint %}

#### Example: Concept entity

Here's an example of migrating a custom Aidbox resource, [Concept](../../../modules-1/terminology/concept/). This resource mirrors FHIR's CodeSystem `concept` property and extends it in various ways.

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Entity/Concept/$dump-as-fhir-schema
content-type: application/json
accept: application/json
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```json
{
  "derivation": "specialization",
  "id": "Concept",
  "kind": "resource",
  "datatype": "Concept",
  "url": "Concept",
  "elements": {
    "designation": {
      "scalar": true,
      "elements": {
        "definition": {
          "scalar": true,
          "additionalProperties": {
            "any": true
          }
        },
        "display": {
          "scalar": true,
          "additionalProperties": {
            "any": true
          }
        }
      },
      "additionalProperties": {
        "any": true
      }
    },
    "ancestors": {
      "scalar": true,
      "additionalProperties": {
        "any": true
      }
    },
    "valueset": {
      "type": "string",
      "datatype": "string",
      "array": true
    },
    "id": {
      "type": "string",
      "datatype": "string",
      "scalar": true
    },
    "hierarchy": {
      "type": "string",
      "datatype": "string",
      "array": true
    },
    "_source": {
      "type": "string",
      "datatype": "string"
    },
    "deprecated": {
      "type": "boolean",
      "datatype": "boolean",
      "scalar": true
    },
    "property": {
      "scalar": true,
      "additionalProperties": {
        "any": true
      }
    },
    "definition": {
      "type": "string",
      "datatype": "string",
      "scalar": true
    },
    "meta": {
      "type": "Meta",
      "datatype": "Meta"
    },
    "display": {
      "type": "string",
      "datatype": "string",
      "scalar": true
    },
    "system": {
      "type": "string",
      "datatype": "string",
      "scalar": true
    },
    "resourceType": {
      "type": "string",
      "datatype": "string"
    },
    "code": {
      "type": "string",
      "datatype": "string",
      "scalar": true
    }
  },
  "required": [
    "system",
    "code"
  ],
  "additionalProperties": {
    "any": true
  }
}
```
{% endcode %}


{% endtab %}
{% endtabs %}

## Manual Approach

### Get Attributes related to one Entity

```sql
SELECT * FROM Attribute WHERE resource#>>'{resource, id}' = <your-entity-id>
```

### Translate your Attribute to FHIRSchema element entry

For example, we have `theme` Attribute related to `UserSetting` Entity:

```yaml
id: UserSetting.theme
path: ['theme']
type: {id: string, resourceType: Entity}
enum: ['dark', 'white']
resource: {id: UserSetting, resourceType: Entity}
```

To describe it as a FHIRSchema element, do the following:

```json
{"elements": {"theme": {"type": "string"}}}
```

For paths with length greater than 1 (e.g `["composite", "field"]`), simply nest "elements" statements like this:

```json
{"elements": {"theme": {"type": "string"}
              "composite": {"elements": {"field": {"type": "string}}}}}
```

#### What to do with enums?

Enums can be described using FHIRPath constraints, here's an example:

```json
{"elements":
 {"theme":
  {"type": "string",
   "constraints":
   {"<your-constraint-id>": {"expression": "%context.subsetOf('dark' | 'white')",
                             "severity":   "error"}}}}}
```

Alternatively, you can create a ValueSet and bind it to the coded value.

```json
{"elements":
 {"theme":
  {"type": "string",
   "binding": {"valueSet": "<your-vs-url>",
               "strength": "required"}}}}
```

#### What to do with `isOpen`

```yaml
id: UserSetting.someContainer
path: ['someContainer']
isOpen: true
resource: {id: UserSetting, resourceType: Entity}
```

To describe `isOpen` use `additionalProperties`:

```json
{"elements":
 {"someContainer": {"additionalProperties": {"any": true}}}}
```

#### What to do with `isRequired`

```yaml
id: UserSetting.requiredField
path: ['requiredField']
type: {id: string, resourceType: Entity}
isRequired: true
resource: {id: UserSetting, resourceType: Entity}
```

To describe `isRequired` use `required` that sits on the same level with `elements`:

```json
{"required": ["requiredField"],
 "elements":
 {"requiredField": {"type": "string"}}
```

#### What to do with `isCollection`

```yaml
id: UserSetting.someCollection
path: ['someCollection']
type: {id: string, resourceType: Entity}
resource: {id: UserSetting, resourceType: Entity}
```

To describe `isCollection` use `array: true`

```json
{"elements":
 {"someCollection": {"array": true, "type": "string"}}
```

### Resulting FHIRSchema

```json
{"id": "UserSetting",
 "name": "UserSetting",
 "type": "UserSetting",
 "kind": "resource",
 "derivation": "specialization",
 "url": "https://example.com/FHIRSchema/UserSetting",
 "required": ["requiredField"],
 "elements":
 {"someCollection": {"array": true, "type": "string"}
  "requiredField": {"type": "string"},
  "someContainer": {"additionalProperties": {"any": true}},
  "theme":
  {"type": "string",
   "constraints":
   {"<your-constraint-id>": {"expression": "%context.subsetOf('dark' | 'white')",
                             "severity":   "error"}}}}}
```
