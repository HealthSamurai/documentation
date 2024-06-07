# Custom resources on FHIR logical model

A logical model is a representation of data structures, that are not implemented in a FHIR specification. A FHIR logical data model - or simply logical model - is an expression of a data structure captured using FHIR. Logical models expressed via FHIR StructureDefinition resource,  with `kind` property set to `logical`. For example:

```
 "kind": "logical",
 "abstract": false,
 "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Element",
 "derivation": "specialization",
```

## Configure Aidbox

To begin using FHIR logical models, enable the FHIR Schema validator engine in Aidbox.

{% content-ref url="../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md" %}
[setup.md](../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md)
{% endcontent-ref %}

## Create StructureDefinition for custom resource

Let's define a custom resource called CustomUser. This resource contains two properties:

1. `name`: This property represents the user's name and is of the FHIR `HumanName` type.
2. `field`: This property is for arbitrary text and is of the FHIR `string` data type.

{% code lineNumbers="true" %}
```json
POST /fhir/StructureDefinition
content-type: application/json
accept: application/json

{
    "derivation": "specialization",
    "name": "CustomUser",
    "abstract": "false",
    "type": "CustomUser",
    "resourceType": "StructureDefinition",
    "status": "active",
    "id": "CustomUser",
    "kind": "logical",
    "url": "http://example.org/StructureDefinition/CustomUser",
    "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Element",
    "differential": {
        "element": [
            {
                "id": "CustomUser",
                "path": "CustomUser",
                "min": 0,
                "max": "*"
            },
            {
                "id": "CustomUser.name",
                "path": "CustomUser.name",
                "min": 0,
                "max": "*",
                "type": [
                    {
                        "code": "HumanName"
                    }
                ]
            },
            {
                "id": "CustomUser.field",
                "path": "CustomUser.field",
                "min": 1,
                "max": "1",
                "type": [
                    {
                        "code": "string"
                    }
                ]
            }
        ]
    }
}
```
{% endcode %}

Response:

{% code title="Status: 200" lineNumbers="true" %}
```json
{
 "derivation": "specialization",
 "name": "CustomUser",
 "abstract": "false",
 "type": "CustomUser",
 "resourceType": "StructureDefinition",
 "status": "active",
 "id": "CustomUser",
 "kind": "logical",
 "url": "http://example.org/StructureDefinition/CustomUser",
 "differential": {
  "element": [
   {
    "id": "CustomUser",
    "path": "CustomUser",
    "min": 0,
    "max": "*"
   },
   {
    "id": "CustomUser.name",
    "path": "CustomUser.name",
    "min": 0,
    "max": "*",
    "type": [
     {
      "code": "HumanName"
     }
    ]
   },
   {
    "id": "CustomUser.field",
    "path": "CustomUser.field",
    "min": 1,
    "max": "1",
    "type": [
     {
      "code": "string"
     }
    ]
   }
  ]
 },
 "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Element"
}
```
{% endcode %}

## Interact with a resource

Let's create an instance of `CustomUser` resource

{% tabs %}
{% tab title="Request" %}
{% code lineNumbers="true" %}
```json
POST /fhir/CustomUser
content-type: application/json
accept: application/json

{
  "resourceType": "CustomUser",
  "name": [{"use": "nickname", "text": "test-user"}],
  "field": "some-field"
}
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" lineNumbers="true" %}
```json
{
 "name": [
  {
   "use": "nickname",
   "text": "test-user"
  }
 ],
 "field": "some-field",
 "id": "d406fd3b-67db-4385-a88e-7f702027a267",
 "resourceType": "CustomUser",
 "meta": {
  "lastUpdated": "2024-06-07T12:59:06.570826Z",
  "createdAt": "2024-06-07T12:59:06.570826Z",
  "versionId": "15"
 }
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

Let's try to create invalid `CustomUser` resource

{% tabs %}
{% tab title="Request" %}
```json
POST /fhir/CustomUser
content-type: application/json
accept: application/json

{
  "resourceType": "CustomUser",
  "name": [{"use": "wrong-code", "text": "test-user"}]
}
```
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 422" lineNumbers="true" %}
```json
{
 "resourceType": "OperationOutcome",
 "text": {
  "status": "generated",
  "div": "Invalid resource"
 },
 "issue": [
  {
   "severity": "fatal",
   "code": "invalid",
   "expression": [
    "CustomUser.name[0].use"
   ],
   "diagnostics": {
    "schema-id": "HumanName",
    "type": "terminology-binding-error",
    "context": {
     "valueset": "http://hl7.org/fhir/ValueSet/name-use",
     "value": "wrong-code"
    },
    "path": "name[0].use"
   }
  },
  {
   "severity": "fatal",
   "code": "invalid",
   "expression": [
    "CustomUser.field"
   ],
   "diagnostics": {
    "type": "required-key",
    "path": "field"
   }
  }
 ]
}
```
{% endcode %}
{% endtab %}
{% endtabs %}

## Convenience

Manually writing StructureDefinitions can be overwhelming. Fortunately, there is an alternative: FSH/SUSHI allows you to generate a FHIR NPM package with your custom resources, which you can load into Aidbox and use.

{% content-ref url="../../modules-1/profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../../modules-1/profiling-and-validation/fhir-schema-validator/upload-fhir-implementation-guide/)
{% endcontent-ref %}
