# Migrate Custom Resources Defined with Entity & Attributes to FHIR Schema

## Prerequisites

The materials in this section describe what to do next with the resulting FHIR Schema resource or set of resources. They explain how to load them into Aidbox, either one by one or via IG. Additionally, they outline the requirements for FHIR Schema to define a Custom Resource and provide a detailed FHIR Schema specification.

{% content-ref url="../../../modules/profiling-and-validation/fhir-schema-validator/" %}
[fhir-schema-validator](../../../modules/profiling-and-validation/fhir-schema-validator/)
{% endcontent-ref %}

{% content-ref url="../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md" %}
[how-to-create-fhir-npm-package.md](../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md)
{% endcontent-ref %}

{% content-ref url="../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/)
{% endcontent-ref %}

## Obtain FHIRSchema

In the following sections, we will describe how to obtain the FHIR Schema from an Entity/Attribute resource definition.

### Semi-Automated approach

You can use a special REST API endpoint to migrate specific Entities and their corresponding Attributes to FHIR Schema. The endpoint requires an `entity ID` to process it. Internally, it attempts to compile the entity to FHIR Schema.

```http
GET /Entity/<entity-id>/$dump-as-fhir-schema
```

If the compilation succeeds, the endpoint returns the resulting FHIR Schema. If it fails, it provides a list of errors detailing what went wrong. Most errors occur because you've used keys on your Attributes/Entities that are not recognized by our compiler. In this case, you can [contact us](../../../overview/contact-us.md) to potentially extend the compiler. Alternatively, you can manually rewrite your custom resource using FHIR Schema.

{% hint style="danger" %}
The compiler inlines types from Entity/Attributes as they are. At Aidbox runtime, the validator tries to interpret them as FHIR types.

If your resource relies on another custom resource type that clashes with a FHIR type, migrate your other custom resource first. Give it a unique name that does not clash with FHIR types.
{% endhint %}

{% hint style="danger" %}
Please double-check the compilation results, as they may contain transformation errors. This tool is intended to reduce manual effort during the migration process and is not meant for fully automated resource migration.
{% endhint %}

#### Example: Concept entity

Here's an example of migrating a custom Aidbox resource, [Concept](../../../modules/terminology/concept/). This resource mirrors FHIR's CodeSystem `concept` property and extends it in various ways.

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

### Manual Approach

If the compiler doesn't support certain instructions for your Entity/Attributes, if you want full control during the migration process, or if you want to extend your custom resources using unique FHIR Schema features, you can manually rewrite your custom resource definitions from scratch using the FHIR Schema.

In the following steps, we will use the example resource, `UserSetting`. Although it is just an example, it covers most aspects of resource definition with the Entity/Attribute model. If this guide misses any features of Entity/Attribute resource definition, please [contact us.](../../../overview/contact-us.md)

#### Get Attributes related to one Entity

First, we need to obtain the Attributes. Attributes define a property for your entity and are connected to a single Entity, allowing us to easily obtain all Attributes related to that Entity.

```sql
SELECT * FROM Attribute WHERE resource#>>'{resource, id}' = <your-entity-id>
```

For the `UserSetting` example, we will perform the following request:

```sql
SELECT * FROM Attribute WHERE resource#>>'{resource, id}' = 'UserSetting'
```

#### Translate your Attribute to FHIRSchema element entry

The FHIRSchema `elements` is similar to an Attribute collection, serving the same purpose: defining a properties for an Entity. For example, we have the `theme` Attribute related to the `UserSetting` Entity:

```yaml
{
  "id": "UserSetting.theme",
  "path": [
    "theme"
  ],
  "type": {
    "id": "string",
    "resourceType": "Entity"
  },
  "enum": [
    "dark",
    "white"
  ],
  "resource": {
    "id": "UserSetting",
    "resourceType": "Entity"
  }
}
```

It states that the theme property must be a `string`. This property has the path `['theme']`, indicating it is at the root of the entity type. It is limited to two possible values: `dark` and `white`, defined via an `enum` property. Additionally, it includes a reference to the Entity to which this attribute belongs: `UserSetting`.\
\
To describe it as a FHIRSchema element, do the following:

```json
{"elements": {"theme": {"type": "string"}}}
```

For paths longer than one element (e.g., \["composite", "field"]), nest the "elements" statements accordingly. Note that "elements" repeats each time you nest a property:

```json
{"elements": {"theme": {"type": "string"},
              "composite": {"elements": {"field": {"type": "string}}}}}
```

#### Handling Enums

Enums were a handy tool for limiting possible values directly within the Entity/Attribute model. However, this approach conflicts with FHIR, which has its own well-defined methods for limiting value sets.

_**Using FHIRPath Constraints**_

Enums can be described using FHIRPath constraints. Here's an example:

```json
{
  "elements": {
    "theme": {
      "type": "string",
      "constraints": {
        "<your-constraint-id>": {
          "expression": "%context.subsetOf('dark' | 'white')",
          "severity": "error"
        }
      }
    }
  }
}
```

Constraint id must be unique across one resource definition. While this method allows for defining possible values in place, it has downsides. It makes it difficult to reuse or implement lookups for these values. Here is the FHIRSchema reference specification describing the [constraints](https://fhir-schema.github.io/fhir-schema/reference/constraint.html) property.

_**Using ValueSets**_

Alternatively, you can create a ValueSet and bind it to the coded value. FHIR resources extensively utilize this approach. This involves creating a ValueSet as a resource and delivering it to an external terminology server. However, this is beyond the scope of this guide.

```json
{
  "elements": {
    "theme": {
      "type": "string",
      "binding": {
        "valueSet": "<your-vs-url>",
        "strength": "required"
      }
    }
  }
}
```

In FHIR, terminology bindings occur in four gradations: required, extensible, preferred, and example. The FHIRSchema validator checks bindings only with the strength of "required"; all other binding strengths are ignored.

This approach leverages FHIR's capabilities for managing value sets, ensuring consistency and reusability across different resources.

#### Handling `isOpen`

The `isOpen` instruction for an Attribute indicates that the next node in the data will be an arbitrary map with arbitrary key/value pairs.

**Example:**

```json
{
  "id": "UserSetting.someContainer",
  "path": ["someContainer"],
  "isOpen": true,
  "resource": { "id": "UserSetting", "resourceType": "Entity" }
}
```

**FHIRSchema Equivalent**

With FHIRSchema, you can express identical semantics using `additionalProperties` and `any: true`. However, note that resource definitions using these instructions are not compatible with FHIR. This means you cannot convert FHIRSchema with these instructions back to StructureDefinition, as FHIR doesn't naturally describe arbitrary key/value nodes.

To express `isOpen` in FHIRSchema:

```json
{
  "elements": {
    "someContainer": {
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
{
  "elements": {
    "someContainer": {
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

#### Handling `isRequired`

The `isRequired` instruction indicates that the property described by this Attribute is required and must be present in a resource instance.

**Example:**

```json
{
  "id": "UserSetting.requiredField",
  "path": ["requiredField"],
  "type": { "id": "string", "resourceType": "Entity" },
  "isRequired": true,
  "resource": { "id": "UserSetting", "resourceType": "Entity" }
}
```

**FHIRSchema Equivalent**

To express `isRequired` in FHIRSchema, use the `required` property at the same level as `elements`. It is an array of fields that must be present in the data instance.

```json
{
  "required": ["requiredField"],
  "elements": {
    "requiredField": {
      "type": "string"
    }
  }
}
```

For more information about this instruction, please refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#requires-and-exclusions) in the FHIRSchema specification.

#### Handling `isCollection`

The `isCollection` instruction indicates that the property described by this Attribute represents a collection of items.

**Example:**

```json
{
  "id": "UserSetting.someCollection",
  "path": ["someCollection"],
  "type": { "id": "string", "resourceType": "Entity" },
  "resource": { "id": "UserSetting", "resourceType": "Entity" }
}
```

This definition specifies that `someCollection` is a property within the `UserSetting` Entity, where each item in the collection is of type string.

**FHIRSchema Equivalent**

To express `isCollection` in FHIRSchema, use `array: true`.

```json
{
  "elements": {
    "someCollection": {
      "array": true,
      "type": "string"
    }
  }
}
```

For more information about this instruction, please refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#shape) in the FHIRSchema specification.

#### Handling `refers`

The `refers` instruction indicates that a node will contain a reference to another resource and limits the possible target resource types to a predefined list.

**Example:**

```json
{
  "id": "UserSetting.refToSmth",
  "path": ["refToSmth"],
  "type": { "id": "string", "resourceType": "Entity" },
  "refers": ["SomeType"],
  "resource": { "id": "UserSetting", "resourceType": "Entity" }
}
```

In this example, the `refToSmth` property refers to a resource of type `SomeType`.

**FHIRSchema Equivalent**

To express this in FHIRSchema, use the `refers` instruction and specify the "Reference" type. This will be handled as a regular FHIR reference. Additionally, in the `refers` property, you can reference not only resource types but also profiles on some resources.

```json
{
  "elements": {
    "refToSmth": {
      "type": "Reference",
      "refers": ["SomeType"]
    }
  }
}
```

In this schema, `refToSmth` is defined as a reference to resources of type `SomeType`.

For more information about this instruction, refer to the relevant [section](https://fhir-schema.github.io/fhir-schema/reference/element.html#reference-target) of the FHIR Schema reference specification.

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
 {"someCollection": {"array": true, "type": "string"},
  "refToSmth": {"type": "Reference", "refers": ["SomeType"]}
  "requiredField": {"type": "string"},
  "someContainer": {"additionalProperties": {"any": true}},
  "theme":
  {"type": "string",
   "constraints":
   {"<your-constraint-id>": {"expression": "%context.subsetOf('dark' | 'white')",
                             "severity":   "error"}}}}}
```

#### Handling resulting FHIR Schemas

To deliver the FHIR Schema(s) and related Entities you authored\
to Aidbox, follow these steps.\
Ensure that your Aidbox is configured to run with the\
FHIRSchema validation engine. Here's[a guide describing how to achieve that](../../../modules/profiling-and-validation/fhir-schema-validator/).

**Single FHIRSchema Delivery**

If you have only one FHIRSchema that replaces your custom-defined Entity/Attributes, follow [this guide](../custom-resources-using-fhir-schema.md) to deliver a single FHIRSchema to Aidbox.

**Multiple Schemas as a Package**

If you have multiple schemas replacing a set of resources and want to work with this set of entities as a package (ImplementationGuide), refer to this guide on how to create your own FHIR NPM package with ImplementationGuide entities.

{% content-ref url="../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md" %}
[how-to-create-fhir-npm-package.md](../../../tutorials/other-tutorials/how-to-create-fhir-npm-package.md)
{% endcontent-ref %}

**Loading the FHIR NPM Package**

{% content-ref url="../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../../../tutorials/validation-tutorials/upload-fhir-implementation-guide/)
{% endcontent-ref %}

#### **Important Notes:**

{% hint style="warning" %}
**Schema Precedence:** Uploaded FHIRSchemas with your resource definitions have higher precedence than Entity/Attribute definitions, so validation will be performed using the FHIRSchema. You can delete your Entity/Attribute resources after creating the FHIRSchema with resource definitions.
{% endhint %}

{% hint style="danger" %}
**SearchParameters:** SearchParameters described for custom resources won't work in FHIRSchema validation mode. You need to redefine them as regular FHIR SearchParameters, not Aidbox Search Parameters. See the [migration guide](../../../tutorials/crud-search-tutorials/search-tutorials/migrate-from-aidbox-searchparameter-to-fhir-searchparameter.md).
{% endhint %}
