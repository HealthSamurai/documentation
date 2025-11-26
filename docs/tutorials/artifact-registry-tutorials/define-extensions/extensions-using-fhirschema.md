# Extensions using FHIRSchema

When Aidbox is configured with [FHIRSchema mode](../../../modules/profiling-and-validation/fhir-schema-validator/), it is possible to define Extension in [FHIRSchema format](https://fhir-schema.github.io/fhir-schema/).

See also [Extensions using StructureDefinition](extensions-using-structuredefinition.md).

## Example

To create the extension, use `POST /fhir/FHIRSchema` endpoint.

```
POST /fhir/FHIRSchema

url: http://my-extension/insurance-plan-reference
id: insurance-plan-reference
base: Extension
name: insurance-plan-reference
kind: complex-type
type: Extension
version: 0.0.1
resourceType: FHIRSchema
elements:
  url:
    fixed: http://my-extension/insurance-plan-reference
  value:
    choices:
      - valueReference
  valueReference:
    type: Reference
    refers:
      - InsurancePlan
    choiceOf: value
derivation: constraint
```

When the extension is created, you can inspect it in "FHIR Packages" Aidbox UI page.

<figure><img src="../../../.gitbook/assets/d05df5a8-b47c-474b-bccc-287520d47f1f.png" alt=""><figcaption></figcaption></figure>

Usage of the extension:

<pre class="language-yaml"><code class="lang-yaml"><strong>PUT /fhir/Coverage/my-coverage
</strong>
extension:
- url: http://my-extension/insurance-plan-reference
  valueReference: 
    reference: InsurancePlan/ip1
payor: 
- reference: Patient/pt1
beneficiary:
  reference: Patient/pt1
status: active
</code></pre>
