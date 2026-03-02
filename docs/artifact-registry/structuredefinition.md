---
description: FHIR StructureDefinition resources for creating profiles, extensions, and custom resources in Aidbox Artifact Registry
---

# StructureDefinition

Modern healthcare systems require flexibility to adapt FHIR resources to specific organizational needs while maintaining interoperability. Without proper structure definitions, implementations end up with incompatible data models, failed validations, and integration headaches. **StructureDefinition** resources solve this problem by providing a standardized way to define profiles, extensions, and custom resource structures within the FHIR ecosystem.

[StructureDefinition](https://www.hl7.org/fhir/structuredefinition.html) is a fundamental [FHIR canonical resource](https://build.fhir.org/canonicalresource.html) that describes the structure and constraints of FHIR resources, data types, and extensions. It serves as a blueprint that defines what elements are allowed, which are required, and how they should be constrained for specific use cases.

## What You Can Define with StructureDefinition

StructureDefinition enables customization patterns that address different implementation needs: resource profiles for adding constraints, extensions for adding new data elements, and in Aidbox, custom resources for entirely new resource types. While profiles and extensions are standard FHIR features, defining custom resources is not FHIR compliant and is an Aidbox-specific extension.

### Resource Profiles

Profiles allow you to constrain existing FHIR resources to meet specific implementation requirements. Think of a profile as a specialized version of a base resource with additional rules about which elements are required, forbidden, or constrained to specific values.

For example, a [US Core Patient profile](https://hl7.org/fhir/us/core/STU8/StructureDefinition-us-core-patient.html) might require specific elements like identifier that aren't mandatory in the base Patient resource:

```json
{
  "resourceType": "StructureDefinition",
  "url": "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient",
  "name": "USCorePatientProfile",
  "title": "US Core Patient Profile",
  "status": "active",
  "kind": "resource",
  "abstract": false,
  "type": "Patient",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Patient",
  "derivation": "constraint"
  "differential": {
  ...
    "id" : "Patient.identifier",
    "path" : "Patient.identifier",
    "short" : "An identifier for this patient",
    "definition" : "An identifier for this patient.",
    "requirements" : "Patients are almost always assigned specific numerical identifiers.",
    "min" : 1,
  ...
  }
}
```


### Extensions

Extensions add new data elements to existing FHIR resources without breaking compatibility. When base resources don't include fields you need, extensions provide a standardized mechanism to augment the structure.

A birth place extension might add location information to a Patient resource:

```json
{
  "resourceType": "StructureDefinition",
  "id": "patient-birthPlace",
  "url": "http://hl7.org/fhir/StructureDefinition/patient-birthPlace",
  "version": "4.0.1",
  "name": "PatientBirthPlace",
  "status": "active",
  "date": "2016-09-01",
  "publisher": "HL7 International - Patient Care WG",
  "description": "An extension to record the place of birth for a patient",
  "fhirVersion": "4.0.1",
  "kind": "complex-type",
  "abstract": false,
  "type": "Extension",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Extension",
  "derivation": "specialization",
  "context": [
    {
      "type": "element",
      "expression": "Patient"
    }
  ],
  "snapshot": {
    "element": [
      {
        "id": "Extension",
        "path": "Extension"
      },
      {
        "id": "Extension.url",
        "path": "Extension.url",
        "fixedUri": "http://hl7.org/fhir/StructureDefinition/patient-birthPlace"
      },
      {
        "id": "Extension.valueAddress",
        "path": "Extension.valueAddress",
        "min": 0,
        "max": "1",
        "type": [
          {
            "code": "Address"
          }
        ],
        "short": "The place of birth",
        "definition": "The place where the patient was born."
      }
    ]
  }
}
```

Extensions maintain interoperability because systems that don't understand specific extensions can safely ignore them while preserving the core resource data.

See also:
- [Extension Definition Tutorial](../tutorials/artifact-registry-tutorials/define-extensions/)

### Custom Resources

Aidbox supports custom resources as an extension to standard FHIR capabilities. While FHIR provides the standard [mechanism](https://hl7.org/fhir/R4/structuredefinition-definitions.html#StructureDefinition.derivation) for defining resource types through StructureDefinition, Aidbox leverages this same approach to enable you to define entirely new resource types for use cases not covered by standard FHIR resources.

This Aidbox-specific feature uses the standard FHIR way of defining resource types, ensuring compatibility with FHIR tooling while extending functionality. Custom resources should be used sparingly since they reduce interoperability, but they're valuable for organization-specific workflows or emerging healthcare domains.

A custom TelemedicineSession resource might capture video consultation data:

```json
{
  "resourceType": "StructureDefinition",
  "url": "http://example.org/fhir/StructureDefinition/TelemedicineSession",
  "name": "TelemedicineSession",
  "title": "Telemedicine Session",
  "status": "active",
  "kind": "resource",
  "abstract": false,
  "type": "TelemedicineSession",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/DomainResource",
  "derivation": "specialization"
  ...
}
```

Custom resources give you complete control over the data structure but require careful consideration of long-term maintenance and interoperability implications.

See also:

{% content-ref url="../tutorials/artifact-registry-tutorials/custom-resources/" %}
[Custom Resource Tutorials](../tutorials/artifact-registry-tutorials/custom-resources/)
{% endcontent-ref %}

{% content-ref url="../modules/profiling-and-validation/" %}
[FHIR Profiling and Validation](../modules/profiling-and-validation/)
{% endcontent-ref %}

## Implementation in Aidbox

Aidbox stores StructureDefinition resources in the Artifact Registry, providing automatic validation and resolution capabilities. When you import FHIR packages or create custom structure definitions, they become available for validation operations and resource profiling.

### Automatic Validation

Once a StructureDefinition is loaded into the Artifact Registry, Aidbox can automatically validate resources against the defined profiles. The validation engine checks cardinality constraints, data type requirements, and value restrictions defined in your structure definitions.

### Profile Resolution

Aidbox resolves profile references using the canonical URLs defined in StructureDefinition resources. When a resource declares conformance to a profile using the `meta.profile` element, the validation system automatically applies the corresponding constraints.


