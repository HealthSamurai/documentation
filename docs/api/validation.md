---
description: FHIR resource validation using FHIR Schema Validation Engine with support for profiles, extensions, and asynchronous batch validation
---

Todo redirects, summary.md


# Validation

At its core, validation compares a resource instance against its schema. The schema is a [`StructureDefinition`](https://www.hl7.org/fhir/structuredefinition.html) — a formal description of what elements a resource can contain, what types they have, and what constraints they must satisfy.

Consider a simple Patient resource instance:

```json
{
  "resourceType": "Patient",
  "birthDate": "2024-13-45",
  "gender": "M",
  "contact": [{}]
}
```

Validation catches several issues here by checking the instance against the Patient StructureDefinition:

* **Element names** — verifying that `birthDate` and `gender` are actually defined elements in the Patient schema
* **Element types** — confirming `birthDate` is a valid FHIR `date` type (it's not — "2024-13-45" isn't a real date)
* **Cardinality** — ensuring required elements are present and maximum cardinality isn't exceeded
* **Terminology bindings** — checking that `gender` uses a code from the bound value set (`"M"` is invalid — should be `"male"`)
* **Constraints** — evaluating [FHIRPath](https://www.hl7.org/fhir/fhirpath.html) invariants like "A contact detail must have at least a name, telecom, or address"
* **References** — verifying that referenced resources (like `Patient.generalPractitioner`) actually exist
* **Extensions** — confirming extension URLs are known and extension values match their definitions
* **Slicing** — when elements are sliced, ensuring instances follow slice discriminator rules and cardinality

## The complexity of StructureDefinition schemas

Implementing FHIR validation isn't straightforward. StructureDefinitions themselves form a recursive, interconnected graph of schemas that validators must traverse to build a complete picture of what's valid.

### Recursive type definitions

Consider what happens when validating a Patient resource. The Patient StructureDefinition defines its top-level structure, but many of its elements have complex types — `HumanName`, `Address`, `ContactPoint`, `Identifier`. Each of these complex types has its own StructureDefinition that defines nested elements. For instance, `HumanName` contains `family`, `given`, `prefix`, and `suffix` elements, along with constraints about their use. The validator must recursively resolve and apply these nested StructureDefinitions.

### Inheritance chains

FHIR's inheritance hierarchy adds another layer. Resources inherit from base types: `Patient` inherits from [`DomainResource`](https://www.hl7.org/fhir/domainresource.html), which inherits from [`Resource`](https://www.hl7.org/fhir/resource.html). Complex types inherit from [`Element`](https://www.hl7.org/fhir/element.html) or [`BackboneElement`](https://www.hl7.org/fhir/backboneelement.html). Each level in this hierarchy can define constraints and elements that child types inherit. A validator must walk this inheritance chain to understand the complete set of rules that apply to a given element.

### Layered profile constraints

[Profiling](https://build.fhir.org/profiling.html) multiplies this complexity. Profiles don't just add constraints — they can extend resources through [slicing](https://www.hl7.org/fhir/profiling.html#slicing) (subdividing repeating elements into specific categories) and by defining new extensions. When a profile constrains a resource, it doesn't replace the base StructureDefinition — it overlays additional restrictions.

Consider the [US Core Patient profile](https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html):

```json
{
  "resourceType": "Patient",
  "meta": {
    "profile": ["http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient"]
  },
  "identifier": [
    {
      "system": "http://hospital.example.org",
      "value": "12345"
    }
  ],
  "extension": [
    {
      "url": "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
      "extension": [
        {
          "url": "ombCategory",
          "valueCoding": {
            "system": "urn:oid:2.16.840.1.113883.6.238",
            "code": "2106-3"
          }
        }
      ]
    }
  ],
  "name": [{"family": "Smith", "given": ["John"]}],
  "gender": "male"
}
```

The US Core profile requires at least one `name` and one `gender` (both optional in base FHIR). It slices the `identifier` array to require at least one identifier. It defines extensions like `us-core-race` and `us-core-ethnicity` that capture US-specific demographic data not present in base FHIR. The validator must check the instance against both the base Patient StructureDefinition and these additional profile constraints.

If that profile is itself profiled (re-profiled), you now have three layers: base resource, intermediate profile, and specialized profile. The validator must compute the effective schema by merging constraints from all layers, respecting rules about which constraints can be tightened and which cannot.

Extensions introduce their own schemas into this mix. Each extension URL points to a StructureDefinition that defines what data the extension can contain. Extensions can themselves contain extensions, creating nested schema resolution.

## FHIR Schema Validation Engine

The complexity described above explains why relatively few FHIR validation implementations exist. StructureDefinitions require esoteric knowledge to parse correctly, lack comprehensive unit tests in the specification, and force implementers to handle intricate edge cases. Most implementers end up doing similar transformations: converting StructureDefinitions into nested data structures, explicitly marking arrays (elements with `max: "*"`), resolving references, and working around the fact that snapshots — an implementation detail — leak into the standard.

[FHIR Schema](https://fhir-schema.github.io/fhir-schema/) addresses these challenges by providing a format designed to simplify FHIR validation implementation. It's heavily inspired by [JSON Schema](https://json-schema.org/) and transforms StructureDefinitions into a more developer-friendly representation.

### Key features

FHIR Schema provides several advantages over working directly with StructureDefinitions:

* **Simple and intuitive structure** — resources and elements are represented as nested properties with types specified directly, similar to how data structures are defined in programming languages
* **Explicit arrays** — pre-calculates which elements are arrays versus singular values, matching how most non-XML implementations work
* **Clear operational semantics** — offers unambiguous rules for implementing validation, making it easier to create robust validators
* **Human- and machine-readable** — format works for both code generation and manual inspection
* **Unified logical models** — treats logical models the same as regular resources, simplifying implementation

FHIR Schema also solves a performance challenge. StructureDefinitions store element definitions in arrays (`snapshot.element[]`), requiring linear searches to find rules for each element. Validating `Patient.name.given` means iterating through the entire array looking for path matches.

FHIR Schema transforms this into nested maps for direct property lookups:

```javascript
// StructureDefinition approach (simplified)
snapshot.element.find(el => el.path === "Patient.name.given")  // O(n) search

// FHIR Schema approach (simplified)
schema["Patient"]["name"]["given"]  // O(1) lookup
```

The compilation process resolves all StructureDefinition references, walks inheritance chains, merges layered profile constraints, handles recursive type definitions, and computes the effective schema with all constraints at each element level.

### Validation features

Aidbox's FHIR Schema Validation Engine automatically compiles StructureDefinitions into this optimized format when loading Implementation Guides. The engine supports comprehensive validation features:

| Feature |
|---------|
| FHIRPath invariants |
| Required elements |
| Forbidden elements (max cardinality 0) |
| Constants |
| Union types |
| Empty values check |
| Primitive types |
| Slicing |
| Ordered slicing |
| Default slice |
| Type slicing |
| Binding slicing |
| Target slicing |
| Re-slicing |
| Terminology bindings |
| Extensions validation |
| Underscore properties (`_propertyName` for primitive extensions) |
| References |
| Recursive schemas |
| Error on `null` values |

Starting from the 2405 release, the FHIR Schema validation engine throws errors when resources contain `null` values. This behavior is FHIR-compliant — FHIR resources should omit absent values rather than representing them as `null`.

The engine validates resources during CRUD operations (automatic validation) and provides the [`$validate`](rest-api/other/validate.md) operation for explicit validation without persisting data.

## Asynchronous batch validation

Validation typically happens synchronously during CRUD operations — you POST a Patient resource, Aidbox validates it, and either accepts or rejects it immediately. But what happens when you've already loaded data into your database and later update your profiles? Or when you need to validate thousands of existing resources without impacting production traffic?

Aidbox provides RPC methods for asynchronous batch validation. These methods let you validate existing data in the background, inspect results at your convenience, and clean up when done.

### Running batch validation

The `aidbox.validation/batch-validation` RPC method validates resources of a specific type:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox.validation/batch-validation
params:
  resource: Patient          # Resource type to validate
  id: patient-check-2024     # Identifier for this validation run
  limit: 1000                # Optional: limit number of resources
  errorsThreshold: 50        # Optional: stop after N invalid resources
  filter: "resource#>>'{birthDate}' is not null"  # Optional: SQL where clause
  async: true                # Optional: run in background
  profiles: ['http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient']  # Optional: specific profiles
```

The method returns immediately when `async: true`, creating a `BatchValidationRun` resource to track progress. Without the `async` flag, it runs synchronously and returns results directly.

For validating multiple resource types, use `aidbox.validation/resources-batch-validation-task`, which creates an [Aidbox Workflow](../deprecated/deprecated/zen-related/workflow-engine/workflow/README.md) task:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox.validation/resources-batch-validation-task
params:
  include: ['patient', 'observation']  # Resource types to validate
  error-threshold: 10000
```

Specify either `include` (only these types) or `exclude` (all types except these), but not both.

### Checking results

For async validation, retrieve results with `aidbox.validation/batch-validation-result`:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox.validation/batch-validation-result
params:
  id: patient-check-2024

# Response
result:
  valid: 1543
  invalid: 2
  duration: 3293
  problems:
    - resource: {...}
      errors: [{...}]
```

For workflow-based validation, pass `resourceType: AidboxWorkflow` to the same method.

### Accessing validation results

Aidbox stores validation results in `BatchValidationRun` and `BatchValidationError` resources, accessible through standard CRUD and Search APIs:

```yaml
GET /BatchValidationError?.run.id=patient-check-2024&_format=yaml&_result=array

# Response
- run:
    id: patient-check-2024
    resourceType: BatchValidationRun
  errors:
    - path: [gender]
      message: "Invalid code: 'M'"
  resource:
    id: pt-123
    resourceType: Patient
    gender: M
  id: patient-check-2024-Patient-pt-123
```

### Cleanup

When you no longer need validation results:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox.validation/clear-batch-validation
params:
  id: patient-check-2024
```

Validation results don't persist across Aidbox restarts — if you restart, you'll need to run validation again.

## Skip reference validation

FHIR validation includes reference checking — ensuring that when a resource references another resource (like `Observation.subject` pointing to a Patient), the target resource actually exists. This maintains referential integrity but creates challenges for certain workflows.

Say you're loading data in stages: first loading Patients, then Observations that reference those Patients. If you load the Observations before their referenced Patients exist, validation fails. Or consider systems with eventual consistency, where referenced resources might not be immediately available.

The `aidbox-validation-skip` header lets you bypass reference validation for specific requests:

```yaml
PUT /fhir/Observation/obs-001
content-type: text/yaml
aidbox-validation-skip: reference

resourceType: Observation
id: obs-001
subject:
  reference: Patient/pt-999  # Patient doesn't exist yet
status: final
code:
  coding:
    - system: http://loinc.org
      code: 15074-8
```

Without the header, this request would fail with a 422 error about the missing Patient. With the header, Aidbox accepts the resource despite the dangling reference.

This feature must be explicitly enabled via environment variable:

```bash
BOX_FHIR_VALIDATION_SKIP_REFERENCE=true
```

Use this carefully. Skipping reference validation compromises referential integrity and can lead to data quality issues. It's appropriate for staged data loading or integration with eventually-consistent systems, but generally not recommended for production use where data integrity is critical.

See also: [Environment variable reference](../reference/all-settings.md#fhir.validation.skip-reference)

## Setup and configuration

To enable FHIR Schema validation in Aidbox, set the following environment variables:

```bash
# Enable FHIR Schema Validation Engine
AIDBOX_FHIR_SCHEMA_VALIDATION=true

# Load Implementation Guides at startup
# Separate multiple packages with colons
AIDBOX_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0

# Configure external terminology service for terminology bindings
AIDBOX_TERMINOLOGY_SERVICE_BASE_URL=https://tx.health-samurai.io/fhir
```

Without `AIDBOX_TERMINOLOGY_SERVICE_BASE_URL`, terminology binding validation is skipped. The external terminology server is used exclusively for validating coded values against ValueSets.

Additional validation settings control strictness:

```bash
# Require all extensions to be known (raise error for unknown extensions)
AIDBOX_VALIDATOR_STRICT_EXTENSION_RESOLUTION_ENABLED=true

# Require all profiles to be known (raise error for unknown profiles in meta.profile)
AIDBOX_VALIDATOR_STRICT_PROFILE_RESOLUTION_ENABLED=true
```

For detailed setup instructions, including how to load Implementation Guides through the UI or API, see the [setup guide](../modules/profiling-and-validation/fhir-schema-validator/setup-aidbox-with-fhir-schema-validation-engine.md).

See also:
* [Artifact Registry overview](../artifact-registry/artifact-registry-overview.md)
* [Upload FHIR Implementation Guide tutorial](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/)
* [FHIR profiling documentation](https://build.fhir.org/profiling.html)
* [FHIR Schema specification](https://fhir-schema.github.io/fhir-schema/)
* [`$validate` operation](rest-api/other/validate.md)
