# Aidbox Search Parameters

{% hint style="warning" %}
Aidbox Search Parameters will be ignored if Aidbox operates with the [FHIR Schema validator engine](../../../../modules-1/profiling-and-validation/fhir-schema-validator.md). Please contact [us](../../../../contact-us.md) if you need assistance migrating to FHIR Search Parameters.

If you want to migrate from Aidbox SearchParameter to FHIR SearchParameter see [this guide.](../../../../storage-1/custom-resources/migrate-to-fhirschema/migrate-custom-resources-defined-with-entity-and-attributes-to-fhir-schema.md)
{% endhint %}

You can define custom search params by just creating SearchParameter resource. Let's say you want to search patient by city:

```yaml
PUT /SearchParameter/Patient.city
​
name: city
type: token
resource: {id: Patient, resourceType: Entity}
expression: [[address, city]]
```

Now let's test new search parameter

```yaml
GET /Patient?city=New-York
​
# resourceType: Bundle
type: searchset
entry: [...]
total: 10
query-sql: 
- | 
  SELECT "patient".* FROM "patient" 
  WHERE ("patient".resource @> '{"address":[{"city":"NY"}]}')
   LIMIT 100 OFFSET 0
```

#### Aidbox format example

{% hint style="warning" %}
Be aware of the [Aidbox format](../../../../modules-1/fhir-resources/aidbox-and-fhir-formats.md) in "expression".&#x20;
{% endhint %}

In this example, the `["value", "string"]` expression is used to look into FHIR `valueString` field:

```
PUT /fhir/SearchParameter/Observation.value-string
content-type: application/json
accept: application/json

{
  "name": "value-string",
  "type": "string",
  "resource": {
    "id": "Observation",
    "resourceType": "Entity"
  },
  "expression": [
    [
     "value", "string" 
    ]
  ],
  "id": "Observation.value-string",
  "resourceType": "SearchParameter"
}
```

#### Define custom SearchParameter with extension

If you have defined [first-class extension](../../../../storage-1/first-class-extensions.md), you have to use Aidbox format for the SearchParameter expression. If you use FHIR format, you don't need to create Attribute and the `expression` path should be in FHIR format.

{% tabs %}
{% tab title="First-class extension" %}
```yaml
PUT /Attribute/ServiceRequest.precondition

resourceType: Attribute
description: "The condition or state of the patient, prior or during the diagnostic procedure or test, for example, fasting, at-rest, or post-operative. This captures circumstances that may influence the measured value and have bearing on the interpretation of the result."
resource: {id: ServiceRequest, resourceType: Entity}
path: [precondition]
id: ServiceRequest.precondition
type: {id: CodeableConcept, resourceType: Entity}
isCollection: true
extensionUrl: "http://hl7.org/fhir/StructureDefinition/servicerequest-precondition"
```
{% endtab %}

{% tab title="SearchParameter (First-class extension)" %}
```yaml
PUT /SearchParameter/ServiceRequest.precondition

name: precondition
type: token
resource: {id: ServiceRequest, resourceType: Entity}
expression: [[precondition, coding]]
```
{% endtab %}

{% tab title="SearchParameter (FHIR extension)" %}
```yaml
PUT /SearchParameter/ServiceRequest.precondition

name: precondition
type: token
resource: {id: ServiceRequest, resourceType: Entity}
expression:
- [extension, {url: 'http://hl7.org/fhir/StructureDefinition/servicerequest-precondition'}, 
value, CodeableConcept, coding, code]

```
{% endtab %}
{% endtabs %}

If you use [Zen IG](../../../../aidbox-configuration/zen-configuration.md) then first-class extensions are generated from zen-schemas. You have to use Aidbox format for the custom SearchParameter `expression` (check tab #3 in the example above).
