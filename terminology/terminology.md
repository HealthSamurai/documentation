# Aidbox terminology module overview

FHIR defines [terminology service](https://www.hl7.org/fhir/terminology-service.html#4.6) to simplify usage of terminologies.

> A terminology service is a service that lets healthcare applications make use of codes and value sets without having to become experts in the fine details of code system, value set, and concept map resources, and the underlying code systems and terminological principles.&#x20;

**Code System** is a set of codes with meanings (also known as enumeration, dictionary, terminology, classification, and/or ontology; you probably know ICD-10, RxNorm, SNOMED, LOINC).

In FHIR, many resource elements are represented as coded values. For example, to encode Lab Tests in Observation resource you may want to use LOINC, or RxNorm for medication prescriptions. FHIR has special data types for coded values:

* [**code**](https://www.hl7.org/fhir/datatypes.html#code) — code as string strictly bound to predefined ValueSet (usually used for fixed codes defined by standard such as Encounter.status or Patient.gender)
* [**Coding**](https://www.hl7.org/fhir/datatypes.html#Coding) — complex type with system & code attributes; system refers to specific Code System
* [**CodeableConcept**](https://www.hl7.org/fhir/datatypes.html#codeableconcept) — complex type which may contain many Codings, so it can be coded using multiple Code Systems (for example, Lab Tests can be coded by proprietary internal code system and by LOINC).

To specify what codes can be assigned to specific element, FHIR defines **binding** of element definition to **ValueSet**. **ValueSet** selects a set of codes from those defined by one or more code systems.&#x20;

In Aidbox, all terminology services are built around non-FHIR **Concept** resource type. Concept resource behaves like other FHIR resources: you can CRUD & Search it. Concept structure essentially follows the structure of Coding data type with some additional attributes.

### Two-phase terminology

#### Design/Management phase

{% content-ref url="create-a-valueset.md" %}
[create-a-valueset.md](create-a-valueset.md)
{% endcontent-ref %}

{% content-ref url="terminology-api/" %}
[terminology-api](terminology-api/)
{% endcontent-ref %}

#### Usage phase

FHIR spec offers FHIR terminology API for validation and lookups

#### CodeSystem

| FHIR specification                                                        | Status      | Documentation and samples                                                            |
| ------------------------------------------------------------------------- | ----------- | ------------------------------------------------------------------------------------ |
| [$lookup](https://www.hl7.org/fhir/codesystem-operations.html#lookup)     | `supported` | [CodeSystem Concept Lookup](codesystem-and-concept/concept-lookup.md)                |
| [$subsumes](https://www.hl7.org/fhir/codesystem-operations.html#subsumes) | `supported` | [CodeSystem Subsumption testing](codesystem-and-concept/subsumption-testing.md)      |
| [$compose](https://www.hl7.org/fhir/codesystem-operations.html#compose)   | `supported` | [CodeSystem Code composition](codesystem-and-concept/codesystem-code-composition.md) |

#### ValueSet

| FHIR specification                                                                | Status      | Documentation and samples                                    |
| --------------------------------------------------------------------------------- | ----------- | ------------------------------------------------------------ |
| [$expand](https://www.hl7.org/fhir/valueset-operations.html#expand)               | `supported` | [ValueSet Expansion](valueset/value-set-expansion.md)        |
| [$validate-code](https://www.hl7.org/fhir/valueset-operations.html#validate-code) | `supported` | [ValueSet Code validation](valueset/value-set-validation.md) |

#### ConceptMap

| FHIR specification                                                          | Status          | Documentation and samples                               |
| --------------------------------------------------------------------------- | --------------- | ------------------------------------------------------- |
| [$translate](https://www.hl7.org/fhir/conceptmap-operations.html#translate) | `supported`     | [ConceptMap Translation](usdtranslate-on-conceptmap.md) |
| [$closure](https://www.hl7.org/fhir/conceptmap-operations.html#closure)     | `not supported` |                                                         |

