# Carin BB

## Setup Aidbox with CARIN Consumer Directed Payer Data Exchange (CARIN IG for Blue Button®) version 2.0.0

Follow [getting started guide](../../getting-started/run-aidbox-locally.md). Add the dependency:
```
AIDBOX_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1:hl7.fhir.us.carin-bb#2.0.0
```

## Validate example resources against Carin BB profiles

<details>

<summary>Valid Patient resource</summary>

{% code lineNumbers="true" %}
```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

resourceType: Patient
meta:
  lastUpdated: '2020-07-07T13:26:22.0314215+00:00'
  profile:
  - http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Patient|2.0.0
language: en-US
identifier:
- type:
    coding:
    - system: http://terminology.hl7.org/CodeSystem/v2-0203
      code: MB
  system: https://www.xxxhealthplan.com/fhir/memberidentifier
  value: 1234-234-1243-12345678901
- type:
    coding:
    - system: http://hl7.org/fhir/us/carin-bb/CodeSystem/C4BBIdentifierType
      code: um
  system: https://www.xxxhealthplan.com/fhir/iniquememberidentifier
  value: 1234-234-1243-12345678901u
active: true
name:
- family: Example1
  given:
  - Johnny
telecom:
- system: phone
  value: "(301)666-1212"
  rank: 2
gender: male
birthDate: '1986-01-01'
address:
- type: physical
  line:
  - 123 Main Street
  city: Pittsburgh
  state: PA
  postalCode: '12519'
maritalStatus:
  coding:
  - system: http://terminology.hl7.org/CodeSystem/v3-NullFlavor
    code: UNK

```
{% endcode %}



</details>

{% hint style="info" %}
The sample of the Patient resource is invalid because it does not include the required key, `meta.lastUpdated`. The sample violates the `patient-meta-profile-version` FHIRPath constraint due to the absence of the version in the profile canonical URL. Additionally, it violates the required slicing on the `identifier` property due to the absence of a slice identifier with the system `http://terminology.hl7.org/CodeSystem/v2-0203` and the value `MB`.
{% endhint %}

<details>

<summary>Invalid Patient resource</summary>

{% code lineNumbers="true" %}
```yaml
POST /fhir/Patient
content-type: text/yaml
accept: text/yaml

resourceType: Patient
meta:
  profile:
  - http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Patient
language: en-US
identifier:
- type:
    coding:
    - system: http://hl7.org/fhir/us/carin-bb/CodeSystem/C4BBIdentifierType
      code: um
  system: https://www.xxxhealthplan.com/fhir/iniquememberidentifier
  value: 1234-234-1243-12345678901u
active: true
name:
- family: Example1
  given:
  - Johnny
telecom:
- system: phone
  value: "(301)666-1212"
  rank: 2
gender: male
birthDate: '1986-01-01'
address:
- type: physical
  line:
  - 123 Main Street
  city: Pittsburgh
  state: PA
  postalCode: '12519'
maritalStatus:
  coding:
  - system: http://terminology.hl7.org/CodeSystem/v3-NullFlavor
    code: UNK

```
{% endcode %}



</details>
