---
description: How to load FHIR Canonical resources via Aidbox FHIR API
---

# Aidbox FHIR API

Aidbox provides a FHIR CRUD API over canonical resources such as StructureDefinition, SearchParameter, and ValueSet.

This enables you to create profiles on resources dynamically on an Aidbox instance already in operation. Additionally, you can create Search Parameters to search for resources.

## Example of creating a StructureDefinition:

Send a PUT request to create a profile called "patient-profile" and specify that the "gender" key must be present.

```yaml
PUT /fhir/StructureDefinition/patient-profile

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

