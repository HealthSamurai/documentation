# Davinci Pdex

## Setup Aidbox with Da Vinci Payer Data Exchange version 2.0.0 with prebuilt Aidbox config

To correctly set up Aidbox, we'll utilize the Aidbox configuration projects.&#x20;

There's an [existing guide](https://docs.aidbox.app/getting-started/run-aidbox-locally-with-docker) for this process. Adhere to this guide, but note a variation when you reach the `Configure the Aidbox` step: instead of using the recommended configuration projects (R4,R4B,R5,etc.) — clone this specific project:

```
git clone \
  --branch=carin-bb \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

The git template project contains Da Vinci Payer Data Exchange version 2.0.0 FHIR IG preconfigured via .env file.

{% hint style="info" %}
If you already have a running Aidbox instance, please refer to the following guide:
{% endhint %}

{% content-ref url="../upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../upload-fhir-implementation-guide/)
{% endcontent-ref %}

## Validate example resources against Davinci PDEX profiles

<details>

<summary>Valid Organization resource</summary>

{% code lineNumbers="true" %}
```yaml
POST /fhir/Organization
content-type: text/yaml
accept: text/yaml

resourceType: Organization
meta:
  profile:
  - http://hl7.org/fhir/us/davinci-pdex/StructureDefinition/mtls-organization
language: en-US
identifier:
- system: http://hl7.org/fhir/sid/us-npi
  value: '1356362586'
active: true
type:
- coding:
  - system: http://hl7.org/fhir/us/davinci-pdex/CodeSystem/OrgTypeCS
    code: payer
    display: Payer
name: Acme of CT
telecom:
- extension:
  - extension:
    - url: daysOfWeek
      valueCode: mon
    - url: daysOfWeek
      valueCode: tue
    - url: daysOfWeek
      valueCode: wed
    - url: daysOfWeek
      valueCode: thu
    - url: daysOfWeek
      valueCode: fri
    - url: availableStartTime
      valueTime: '08:00:00'
    - url: availableEndTime
      valueTime: '17:00:00'
    url: http://hl7.org/fhir/us/davinci-pdex/StructureDefinition/base-ext-contactpoint-availabletime
  system: url
address:
- line:
  - 456 Main Street
  city: Norwalk
  state: CT
  postalCode: 00014-1234

```
{% endcode %}



</details>

{% hint style="info" %}
The sample of the Patient resource is invalid because it does not include the required key, `telecom`.
{% endhint %}

<details>

<summary>Invalid Organization resource</summary>

{% code lineNumbers="true" %}
```yaml
POST /fhir/Organization
content-type: text/yaml
accept: text/yaml

resourceType: Organization
meta:
  profile:
  - http://hl7.org/fhir/us/davinci-pdex/StructureDefinition/mtls-organization
language: en-US
identifier:
- system: http://hl7.org/fhir/sid/us-npi
  value: '1356362586'
active: true
type:
- coding:
  - system: http://hl7.org/fhir/us/davinci-pdex/CodeSystem/OrgTypeCS
    code: payer
    display: Payer
name: Acme of CT
address:
- line:
  - 456 Main Street
  city: Norwalk
  state: CT
  postalCode: 00014-1234

```
{% endcode %}



</details>
