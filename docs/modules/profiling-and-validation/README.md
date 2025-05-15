# Profiling and validation

## What is _Profiling_?

The core FHIR specification outlines essential resources, frameworks, and APIs widely used across diverse healthcare settings. Due to significant variations in practices, regulations, and requirements between regions and within the healthcare industry, the FHIR specification serves as a "platform specification."

However, FHIR resources are generic and have minimal mandatory requirements. For example, all elements in the Patient resource are optional, allowing for the creation of a Patient resource with no data, which is generally impractical. Therefore, this specification often needs to be constrained to suit specific use cases.

The process of creating these constraints is known as _profiling_. These constraints can be expressed in various ways, for which FHIR defines a set of artefacts:

<table><thead><tr><th width="166">Artefact</th><th width="425">Description</th><th>Example</th></tr></thead><tbody><tr><td>Implementation Guide (IG)</td><td>A well-defined and bounded set of adaptations, published together as a single entity. Typically consists of profiles, terminology resources, operation definitions, search parameters, and data examples.</td><td>US Core IG<br><br>mCODE</td></tr><tr><td>Conformance Resource</td><td><p>A single resource in a package that makes rules about how an implementation works, for example:</p><p></p><ul><li>The <code>ValueSet</code> resource defines an inclusion of codes from various <code>CodeSystems</code> or other <code>ValueSets</code>. This resource can be used as a <code>Terminology Binding</code> in a <code>StructureDefinition</code> to constrain the possible set of values for a property (e.g., Patient.gender) or to validate a specific code by checking its presence in the <code>ValueSet</code> via <code>/ValueSet/$validate-code</code>.</li><li>The <code>CodeSystem</code> resource defines a source of codes that can be used for lookups or included in a ValueSet resource.</li><li>The <code>OperationDefinition</code>  defines a FHIR Server operation behaviour and input/output interfaces.</li></ul></td><td>US Core Condition Codes Value Set<br><br>AdministrativeGender CodeSystem<br><br>ValueSet $expand OperationDefinition</td></tr><tr><td>Profile</td><td>A set of constraints on a resource is represented as a <code>StructureDefinition</code> with derivation set to <code>constraint</code>. <br><br><code>StructureDefinition</code> is also used for defining resources, not just specifying constraints; all FHIR Core resources are defined using <code>StructureDefinition</code>. <br><br>While <code>StructureDefinition</code> is indeed a Conformance Resource, we emphasize it here as a <code>Profile</code> because <code>Implementation Guides (IGs)</code> typically consist mostly of these specific resources.</td><td>US Core Medication Request</td></tr></tbody></table>

## What Aidbox provides for _profiling_?

Though Aidbox does not emphasize the process of profiling, it provides a rich experience for introspection of IGs loaded into Aidbox.

{% hint style="info" %}
Introspection views are available only in the New UI. To enable it, go to `"Settings" > "New UI."`
{% endhint %}

* To view the content of an IG in the Aidbox UI, navigate to `"FHIR Packages" > "<IG name>"`
* To view the rendered StructureDefinition differential/snapshot, go to `"FHIR Packages" > "<IG name>" > "Profiles tab" > "<profile>" > "Differential" or "Snapshot"`
* To expand ValueSets from an IG, go to `"FHIR Packages" > "<IG name>" > "ValueSets" > "<ValueSet>" > "Expansion tab"`

Additionally, Aidbox offers a developer-oriented approach for creating and testing profiles. You can simply POST a `StructureDefinition` to a running Aidbox instance and validate a resource against it. Your `StructureDefinitions` and other Conformance Resources posted via the FHIR CRUD API will later appear in the aidbox.main package. To view its content, navigate to `"FHIR Packages" > "app.aidbox.main" > "Profiles tab"`

The FHIR ecosystem offers a diverse array of tools for profiling, with the most notable being the [FHIR Shorthand](https://build.fhir.org/ig/HL7/fhir-shorthand/)/[SUSHI](https://github.com/FHIR/sushi) project and the [FHIR IG Publisher](https://confluence.hl7.org/display/FHIR/IG+Publisher+Documentation).

## What is _Validation_?

Specifying constraints and refinements is only half the job; your FHIR server must also ensure that incoming data conforms to the selected IG for its operation. The process of verifying that a data instance is valid against a predefined set of constraints is known as _validation_.

During validation, various constraints are checked, including data shape, required properties, FHIRPath invariants, slicings, reference checks, and terminology bindings. You can read about the specific constraints supported by the Aidbox validator engine here:

* [FHIR Schema Validator](fhir-schema-validator/)

## What Aidbox provides for _validation_?

Aidbox allows you to upload Implementation Guides. Once an IG is loaded, data instances marked with profile canonical URLs from the loaded IGs will be automatically validated against it without any additional setup. For tutorials on loading IGs into Aidbox, please refer to this page:

* [Upload FHIR Implementation Guide](../../tutorials/validation-tutorials/upload-fhir-implementation-guide/)

Additionally, you can easily create a `StructureDefinition` in a running Aidbox instance and validate a resource against it. Simply mark your data instance with the profile's canonical URL that was posted earlier, and then create your data. The validator engine will check it against the previously created profile. For tutorials on creating StructureDefinitions in Aidbox, please refer to this guide:

* [Aidbox FHIR API](../../tutorials/validation-tutorials/upload-fhir-implementation-guide/aidbox-fhir-api.md)

## Aidbox validation engines

* [FHIR Schema Validator](fhir-schema-validator/)
