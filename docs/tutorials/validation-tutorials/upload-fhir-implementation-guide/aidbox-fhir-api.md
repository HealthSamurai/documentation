---
description: How to load FHIR Canonical resources via Aidbox FHIR API
---

# Aidbox FHIR API

Aidbox provides a FHIR CRUD API over canonical resources such as StructureDefinition, SearchParameter, and ValueSet.

### 1. Run Aidbox with FHIR Schema Validation Engine

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## 2. Creating conformance resources

### StructureDefinition

This allows you to create profiles on resources in Aidbox instance at runtime

Send a PUT request to create a profile called "**patient-profile**" and specify that the "**gender**" key must be present.

```yaml
PUT /fhir/StructureDefinition/patient-profile
content-type: text/yaml
accept: text/yaml

resourceType: StructureDefinition
url: http://example.org/fhir/StructureDefinition/patient-profile
name: patient-profile
derivation: constraint
type: Patient
status: active
kind: resource
abstract: false
differential:
  element:
    - id: Patient.gender
      path: Patient.gender
      max: "1"
      min: 1
```

Check that the profile was successfully uploaded by creating a Patient resource with the URL of the created profile specified.

```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

resourceType: Patient
meta:
  profile: 
    - http://example.org/fhir/StructureDefinition/patient-profile
```

In response, you will receive an error because the profile specifies that the "gender" key is required.

```yaml
resourceType: OperationOutcome
text:
  status: generated
  div: Invalid resource
issue:
  - severity: fatal
    code: invalid
    expression:
      - Patient.gender
    diagnostics:
      type: required-key
      path: gender
```

### Creating a SearchParameter

You can create SearchParameters to search for resources.

Send a PUT request to create a FHIR search parameter called "**patient-profile**"

```yaml
PUT /fhir/SearchParameter/new-gender
content-type: text/yaml
accept: text/yaml

resourceType: SearchParameter
url: http://example.org/fhir/SearchParameter/new-gender
base:
- Patient
expression: Patient.gender
status: draft
type: token
version: 4.0.1
name: new-gender
code: new-gender
```

Check that the search parameter was successfully uploaded by creating a Patient resource and search them using "**new-gender**" query parameter.

```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

gender: unknown
```

```
GET /fhir/Patient?new-gender=unknown
```

### Creating a ValueSet

Send a PUT request to create a ValueSet called "**my-gender-identity**"

```yaml
PUT /fhir/ValueSet/my-gender-identity
content-type: text/yaml
accept: text/yaml

resourceType: ValueSet
url: http://hl7.org/fhir/ValueSet/my-gender-identity
status: draft
name: my-gender-identity
compose:
  include:
    - system: http://hl7.org/fhir/gender-identity
      concept:
        - code: male
          display: male
        - code: female
          display: female
        - code: non-binary
          display: non-binary
        - code: transgender-male
          display: transgender male
        - code: transgender-female
          display: transgender female
        - code: other
          display: other
        - code: non-disclose
          display: does not wish to disclose
```

Learn about other methods for loading IGs here:

{% content-ref url="./" %}
[.](./)
{% endcontent-ref %}
