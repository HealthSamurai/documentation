# Custom resources using FHIRSchema

{% hint style="warning" %}
Custom resources are not interoperable with FHIR.
{% endhint %}

FHIRSchema is a project that aims to simplify the implementation and validation of FHIR (Fast Healthcare Interoperability Resources) resources across different programming languages. It also provides the ability to define custom resources. You can read more about the syntax and validation algorithms in the [FHIRSchema documentation](https://fhir-schema.github.io/fhir-schema/).

## Configure Aidbox

To begin using custom FHIR resources, enable the FHIRSchema validator engine in Aidbox.

{% content-ref url="../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md" %}
[setup.md](../../modules-1/profiling-and-validation/fhir-schema-validator/setup.md)
{% endcontent-ref %}

## Create FHIRSchema for custom resource

Let's define a custom resource called CustomUser. This resource contains two properties:

1. `name`: This property represents the user's name and is of the FHIR `HumanName` type.
2. `field`: This property is for arbitrary text and is of the FHIR `string` data type.

{% code lineNumbers="true" %}
```json
POST /FHIRSchema
content-type: application/json
accept: application/json

{
    "derivation": "specialization",
    "name": "CustomUser",
    "type": "CustomUser",
    "resourceType": "FHIRSchema",
    "kind": "resource",
    "url": "http://example.org/StructureDefinition/CustomUser",
    "base": "http://hl7.org/fhir/StructureDefinition/Element",
    "elements": {
           "name": {
              "type": "HumanName"
           },
           "field": {
              "min": 1,
              "max": 1,
              "type": "string"
           }
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
 "type": "CustomUser",
 "resourceType": "FHIRSchema",
 "kind": "resource",
 "url": "http://example.org/StructureDefinition/CustomUser",
 "base": "http://hl7.org/fhir/StructureDefinition/Element",
 "elements": {
  "name": {
   "type": "HumanName"
  },
  "field": {
   "min": 1,
   "max": 1,
   "type": "string"
  }
 }
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
