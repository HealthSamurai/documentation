---
description: FHIR profiles, terminology bindings, binding strengths, and how validation engines use terminology servers
---

# Profiles, Binding, and Validation

FHIR Profiles define implementation-specific constraints on base FHIR resources, specifying exactly what data elements are required, optional, or prohibited for particular use cases. When a healthcare organization implements FHIR, they create profiles that reflect their specific workflows, data requirements, and regulatory constraints.

Terminology bindings are a key component of profiles, connecting data elements to specific ValueSets to ensure consistent coded data. A profile might specify that `Observation.code` must use codes from a particular laboratory test ValueSet, or that `Patient.maritalStatus` should prefer standard FHIR codes but allow local extensions when needed.

FHIR validation engines use these profiles to verify that incoming data meets the specified constraints. The terminology server plays a crucial role by validating that coded values conform to the bound ValueSets, ensuring data quality and interoperability across systems.

TODO: add

--- brought from previous:

## Binding Concept

To specify what codes can be assigned to specific elements, FHIR uses the concept of **binding** - linking element definitions to ValueSets. A binding defines which ValueSet constrains the possible values for a coded element.

Bindings can have different strengths:
- **Required** - The element must have a value from the specified ValueSet
- **Extensible** - The element should have a value from the specified ValueSet, but other codes are allowed if the ValueSet doesn't contain an appropriate code
- **Preferred** - The element should have a value from the specified ValueSet if possible
- **Example** - The ValueSet provides examples of codes that might be used

For example, the `Observation.code` element is bound to a ValueSet that includes LOINC codes, ensuring that laboratory observations use standardized terminology for test identification.

#TODO: add a screenshot of FHIR Specification with binding for Observation.code

