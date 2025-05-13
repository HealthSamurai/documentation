# mCode

In this tutorial we will guide you how to setup minimal Common Oncology Data Elements (mCODE) FHIR Implementation Guide.&#x20;

## Setup Aidbox with minimal Common Oncology Data Elements (mCODE)  IG version 3.0.0 with prebuilt Aidbox config

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects.&#x20;

There's an [existing guide](broken-reference) for this process. Adhere to this guide, but note a variation when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```
git clone \
  --branch=mcode \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

The git template project contains minimal Common Oncology Data Elements (mCODE)  IG version 3.0.0 FHIR IG preconfigured via .env file.

{% hint style="info" %}
If you already have a running Aidbox instance, please refer to the following guide:
{% endhint %}

{% content-ref url="upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](upload-fhir-implementation-guide/README.md)
{% endcontent-ref %}

## Validate example resources against minimal Common Oncology Data Elements (mCODE)  IG version profiles

<details>

<summary>Valid Patient resource</summary>

{% code lineNumbers="true" %}
```yaml
POST /fhir/Patient

resourceType: Patient
meta:
  profile:
  - http://hl7.org/fhir/us/mcode/StructureDefinition/mcode-cancer-patient
extension:
- extension:
  - url: ombCategory
    valueCoding:
      system: urn:oid:2.16.840.1.113883.6.238
      code: 2028-9
      display: Asian
  - url: text
    valueString: Asian
  url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-race
- extension:
  - url: ombCategory
    valueCoding:
      system: urn:oid:2.16.840.1.113883.6.238
      code: 2186-5
      display: Not Hispanic or Latino
  - url: text
    valueString: Not Hispanic or Latino
  url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex
  valueCode: M
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-sex
  valueCode: '248153007'
identifier:
- use: usual
  type:
    coding:
    - system: http://terminology.hl7.org/CodeSystem/v2-0203
      code: MR
      display: Medical Record Number
    text: Medical Record Number
  system: http://hospital.smarthealthit.org
  value: '1032704'
active: true
name:
- family: Example
  given:
  - Child
telecom:
- system: phone
  value: 555-555-5555
  use: home
gender: male
birthDate: '2016-01-15'
address:
- line:
  - 49 Meadow St
  city: Mounds
  state: OK
  postalCode: '74047'
  country: US
```
{% endcode %}

</details>

{% hint style="info" %}
The sample of the Patient resource is invalid because it does not comply with parental `us-core-patient profile` with the required keys, `identifier` and `gender`. It violates the `us-core-6` FHIRPath constraint due to the absence of the `family` and `given` properties and the `data-absent-reason` extension in the `name` property. Additionally, it violates the terminology binding on the `ombCategory` extension.
{% endhint %}

<details>

<summary>Invalid Patient resource</summary>

{% code lineNumbers="true" %}
```yaml
POST /fhir/Patient

resourceType: Patient
meta:
  profile:
  - http://hl7.org/fhir/us/mcode/StructureDefinition/mcode-cancer-patient
extension:
- extension:
  - url: ombCategory
    valueCoding:
      system: urn:oid:2.16.840.1.113883.6.238
      code: some-nonsense
      display: Asian
  - url: text
    valueString: Asian
  url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-race
- extension:
  - url: ombCategory
    valueCoding:
      system: urn:oid:2.16.840.1.113883.6.238
      code: 2186-5
      display: Not Hispanic or Latino
  - url: text
    valueString: Not Hispanic or Latino
  url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex
  valueCode: M
- url: http://hl7.org/fhir/us/core/StructureDefinition/us-core-sex
  valueCode: '248153007'
active: true
name:
- use: official
telecom:
- system: phone
  value: 555-555-5555
  use: home
birthDate: '2016-01-15'
address:
- line:
  - 49 Meadow St
  city: Mounds
  state: OK
  postalCode: '74047'
  country: US
```
{% endcode %}

</details>
