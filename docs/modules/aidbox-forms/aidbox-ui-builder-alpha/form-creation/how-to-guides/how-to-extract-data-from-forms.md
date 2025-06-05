---
description: >-
  Aidbox forms supports Observation-based extraction, Definition-based
  extraction and Template-base extraction
---

# How to extract data from forms

## Observation-based extraction

Observation-based extraction are widely used extraction method and the simpliest one.&#x20;

To extract data from item to Observation, you just need to add code to item and enable Observation extraction.&#x20;

### Form setup:

1. Fill item code (this code will go into Observation code)
2. Enable Observation-extraction by clicking "Data extraction" -> "Observation"

Minimal Questionnaire that uses that approach:

```yaml
title: New form
status: draft
url: http://forms.aidbox.io/questionnaire/new-form
resourceType: Questionnaire
item:
- type: quantity
  text: Weight
  extension:
  - url: http://hl7.org/fhir/StructureDefinition/questionnaire-unitOption
    valueCoding:
      display: F
  - url: http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-observationExtract
    valueBoolean: true
  linkId: weight
  code:
  - code: 8302-2
    system: http://loinc.org
    display: Body height
```

### Usage

To get some data from you need to call `$extract` operation. Also, you can use Debug panel in builder, choose Extraction and you will see result of calling this operation:

```yaml
POST /fhir/QuestionnaireResponse/$extract
content-type: application/json

resourceType: Parameters
parameter:
- name: questionnaire-response
  resource:
    resourceType: QuestionnaireResponse
    subject:
      id: morgan
      resourceType: Patient
    author:
      id: morgan
      resourceType: Patient
    status: in-progress
    item:
    - linkId: weight
      text: Weight
      answer:
      - valueQuantity:
          value: 123123
          unit: F
    questionnaire: http://forms.aidbox.io/questionnaire/new-form
```

In response you will get resource Bundle with needed Observation:

```yaml
resourceType: Observation
status: final
code:
  coding:
  - code: 8302-2
    system: http://loinc.org
    display: Body height
subject:
  reference: Patient/morgan
valueQuantity:
  value: 123123
  unit: F
effectiveDateTime: '2024-08-13T10:56:51.101406969Z'
id: c83afc80-b274-4a75-8034-c54ee3110e67
```

## Definition-based extraction

Definiton-based extraction can be used to extract any type of resources. In this guide we will create form that updates Patient info.

### Form setup

Let's create a form that updates Patient resource. Any item in Questionnaire that you want to extract using this approach must be placed inside a group with [`itemExtractionContext`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-itemExtractionContext.html)

Create in builder group called Patient and three items, so your form in item tree should look like this:

```
Patient [group]
  - family name [string]
  - given name [string]
  - birth date [date]
```

#### 1. Setting group extraction context

1. Select group in outline
2. Expand `Data extraction` section
3. Check `Extract`&#x20;
4. Select `Definition`
5. Fill `Group extraction context` with **`Patient/{{%resource.subject.id}}`**

When aidbox mets this group during the extraction process, it will fetch Patient resource with id = subject.id of QuestionnaireResponse.&#x20;

#### 2. Setting item definitions

Once we have item extraction context (in our case it is a Patient resource), we can set where answers from items in this group should go.&#x20;

For each item in "Patient" group enable data-extraction in builder and choose "Definition" and fill it with these values:

* `Patient.name.family` for family name item
* `Patient.name.given` for given name item
* `Patient.birthDate` for birth date item

In the end your Questionnaire should look like this:

```yaml
title: Patient info
id: fc83becc-44b1-4d51-8d49-70e869bd271c
status: draft
url: http://forms.aidbox.io/questionnaire/patient-info-q
meta:
  extension:
  - url: https://fhir.aidbox.app/fhir/StructureDefinition/created-at
    valueInstant: '2024-08-13T10:43:11.588138Z'
  - url: ex:createdAt
    valueInstant: '2024-08-13T11:30:29.542773Z'
  lastUpdated: '2024-08-13T11:30:44.897987Z'
  versionId: '7181'
resourceType: Questionnaire
item:
- item:
  - text: Family
    type: string
    linkId: yJbcSamj
    definition: Patient.name.family
  - text: Given
    type: string
    linkId: s3mUJBd0
    definition: Patient.name.given
  - text: Birthdate
    type: date
    linkId: mMkI03mi
    definition: Patient.birthDate
  text: Patient info
  type: group
  linkId: J7OYIFMA
  extension:
  - url: http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-itemExtractionContext
    valueExpression:
      language: application/x-fhir-query
      expression: Patient/{%resource.subject.id}
```

### Usage

Now let's call `$extract` operation again or click "Extract" in Debug panel in Extraction tab:

```yaml
POST /fhir/QuestionnaireResponse/$extract

resourceType: Parameters
parameter:
- name: questionnaire-response
  resource:
    author:
      reference: Patient/morgan
    id: 0935069e-2396-41d6-9f27-eafbd812cc98
    encounter:
      reference: Encounter/enc-1
    status: in-progress
    item:
    - item:
      - text: Family
        answer:
        - valueString: John
        linkId: yJbcSamj
      - text: Given
        answer:
        - valueString: Doe
        linkId: s3mUJBd0
      - text: Birthdate
        answer:
        - valueDate: '2024-08-17'
        linkId: mMkI03mi
      text: Patient info
      linkId: J7OYIFMA
    subject:
      reference: Patient/morgan
    questionnaire: http://forms.aidbox.io/questionnaire/patient-info-q
    meta:
      lastUpdated: '2024-08-13T11:32:02.855923Z'
      versionId: '7186'
      extension:
      - url: ex:createdAt
        valueInstant: '2024-08-13T11:30:59.052413Z'
    resourceType: QuestionnaireResponse
```

In response you will find two entries in Bundle resource: one updating Patient resource and one for creating Provenance resource.

```yaml
resourceType: Bundle
type: transaction
entry:
- resource:
    address:
    - city: Historic City
      line:
      - 534 Erewhon St
      state: DE
    name:
    - family: John
      given:
      - Doe
    birthDate: '2024-08-17'
    resourceType: Patient
    id: morgan
    identifier:
    - value: ''
      system: ''
    gender: male
    maritalStatus:
      coding:
      - code: M
        system: http://terminology.hl7.org/CodeSystem/v3-MaritalStatus
        display: Married
  request:
    method: PUT
    url: /Patient/morgan
- resource:
    resourceType: Provenance
    recorded: '2024-08-13T11:32:02.855923Z'
    target:
    - reference: Patient/morgan
    agent:
    - who:
        reference: Patient/morgan
    entity:
    - what:
        reference: QuestionnaireResponse/0935069e-2396-41d6-9f27-eafbd812cc98
      role: source
  request:
    method: POST
    url: /Provenance
```

### Extraction complicated resource types

Some resource types have rules for extraction conditions. For example, extracting Patient phone or another contact information needs to be created[ ContactPoint](https://hl7.org/fhir/R4/datatypes.html#contactpoint) data type.

In our example ContactPoint has constraints which means:&#x20;

> A system field is required if a value is provided.

We need setup another item which will be extract's need `system` field.

#### Form setup

Let's create a form that updates Patient phone resource. Create in builder group called Patient and two items into it , so your form in item tree should look like this:

```
Patient [group]
  - phone [integer]
    - system [string]
```

#### 1. Setting group extraction context

1. Select group in outline
2. Expand `Data extraction` section
3. Check `Extract`&#x20;
4. Select `Definition`
5. Fill `Group extraction context` with **`Patient/{{%resource.subject.id}}`**

When aidbox mets this group during the extraction process, it will fetch Patient resource with id = subject.id of QuestionnaireResponse.&#x20;

#### 2. Setting item definitions

Once we have item extraction context (in our case it is a Patient resource), we can set where answers from items in this group should go.&#x20;

For each item in "Patient" group enable data-extraction in builder and choose "Definition" and fill it with these values:

* `Patient.telecom.value` for phone name item
* `Patient.telecom.system` for system name item

#### 3. Setting widgets

1. Select `system` widget in outline
2. Select `Hidden` and `Readonly` properties
3. Expand `Rules` section&#x20;
4. Fill `Calculated Expression` with **`'phone'`**

Now `system` item will extract  **`'system' = 'phone'`** into Patient telecom resource.

In the end your Questionnaire should look like this:

```yaml
url: http://forms.aidbox.io/questionnaire/extraction-definition
item:
- item:
  - item:
    - text: system
      type: string
      linkId: system
      readOnly: true
      extension:
      - {url: 'http://hl7.org/fhir/StructureDefinition/questionnaire-hidden', valueBoolean: true}
      - url: http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression
        valueExpression: {language: text/fhirpath, expression: '''phone'''}
      definition: Patient.contact.telecom.system
    text: Phone
    type: integer
    linkId: phone
    definition: Patient.contact.telecom.value
  text: Patient
  type: group
  linkId: patient
  repeats: false
  extension:
  - url: http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-itemExtractionContext
    valueExpression: {language: application/x-fhir-query, expression: 'Patient/{{%resource.subject.id}}'}
title: 'Extraction definition '
status: draft
id: eb58940b-6839-45c5-8fd7-8cf641a5e305
resourceType: Questionnaire
meta:
  lastUpdated: '2000-00-00T00:00:00.00Z'
  versionId: '6487'
  extension:
  - {url: 'https://fhir.aidbox.app/fhir/StructureDefinition/created-at', valueInstant: '2000-00-00T00:00:00.00Z'}
```

#### Testing

&#x20;Call `$extract` operation again or click "Extract" in Debug panel in Extraction tab:

```yaml
resourceType: Parameters
parameter:
- name: return
  resource:
    resourceType: Bundle
    type: transaction
    entry:
    - resource:
        meta:
          lastUpdated: '2000-00-00T00:00:00.00Z'
          versionId: '4491'
          extension:
          - {url: 'https://fhir.aidbox.app/fhir/StructureDefinition/created-at', valueInstant: '2000-00-00T00:00:00.00Z'}
        name:
        - given: [John, Doe]
          family: Andrews
        resourceType: Patient
        id: id2
        identifier:
        - use: usual
          type:
            coding:
            - {code: MR, system: 'http://terminology.hl7.org/CodeSystem/v2-0203'}
          value: '12345'
          system: urn:oid:1.2.36.146.595.217.0.1
        telecom:
        - {system: phone, value: '+1222333444'}
      request: {method: PUT, url: /Patient/id2}
```

In response you will find entry which updates Patient `id2`  with filled `telecom` . In our example it is `phone` type with `+1222333444` numbers.

## Template-based extraction

Template-based extraction is a new way to extract data with user-friendly UI and simple settings. To extract data from form just create template with visual or code editor.

In this guide we will create form with template that updates Patient information.

### Form setup

Let's create a form that updates Patient resource. Any item in Questionnaire that you want to extract using this approach must be placed inside a group.

Create in builder group called Patient and four items, so your form in item tree should look like this:

```
Patient [group]
  - family name [string]
  - given name [string]
  - birth date [date]
  - patient-id [string]
```

{% hint style="info" %}
`patient-id` needed for updating Patient resource and store ID. If you want create new resource do not create this field
{% endhint %}

#### 1. Setting group extraction template

1. Select group in outline
2. Expand `Data extraction` section
3. Check `Extract`&#x20;
4. Select `Template`
5. Click `Add template` and select `New resource`&#x20;

After this steps will opens Resource Template Editor for your template. Now let's create extraction context.

1. Fill your `Template name`&#x20;
2. Select `Patient` into Resource Type&#x20;
3. Select `Existing` into New or existing resource&#x20;
4. Select local items answers `patient-id`  into pick resource id

When aidbox mets this group during the extraction process, it will fetch Patient resource with id = `patient-id` of QuestionnaireResponse.&#x20;

Now assemble template what data need to extract

1. Find `Name` and click `add value`&#x20;
2. Into created bundle `0` find `family` and again press `add value`&#x20;
3. Pick a value into local items answers `family name`&#x20;
4. Repeat this steps for other items `given name` and `birth date`&#x20;

Your template is ready for extraction.

#### 2. Setting widgets

This steps needs only for updating Patient resource. If you want create new resource skip this section.

1. Select `patient-id` widget in outline
2. Select `Hidden`  property
3. Expand `Population` section&#x20;
4. Select `Expression` and fill it with `%subject.id`

Now `patient-id` item will populate id from Patient resource and will store it for our extraction which one we need to update.&#x20;

### Usage

Now let's call `$extract` operation again or click "Extract" in Debug panel in Extraction tab:

```
resourceType: Bundle
type: transaction
entry:
- resource:
    name:
    - family: John
      given:
      - Doe
    birthDate: '2024-08-17'
    resourceType: Patient
    id: id1
    identifier:
    - value: ''
      system: ''
  request:
    method: PUT
    url: /Patient/id1
- resource:
    resourceType: Provenance
    recorded: '2024-08-13T11:32:02.855923Z'
    target:
    - reference: Patient/id1
    agent:
    - who:
        reference: Patient/id1
    entity:
    - what:
        reference: QuestionnaireResponse/0935069e-2396-41d6-9f27-eafbd812cc98
      role: source
  request:
    method: POST
    url: /Provenance
```

In response you will find entry which updates Patient `id1`  with filled `family name` , `given name` and `birthdate`.
