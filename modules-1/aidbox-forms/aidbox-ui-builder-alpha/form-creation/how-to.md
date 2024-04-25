# How Tos

## How to populate form with patient demographic data: patient name, DOB, MRN, address, phone

To populate a form we should: 

1. setup a form to be able to get patient's data and prefill form items with it
2. provide partient's reference to population operation. 

### Form Setup 

Assume that we already have:

- form with 5 items(data: patient name, DOB, MRN, address, phone)
- Patient resource in the Aidbox DB

We should setup items with `populate` expressions.

> How to find population expression:
> 1. select widget in the outline
> 2  click on `populate` checkbox in the widget settings panel
> 2. in opened section select `Expression` tab

For this example we will use:
- `%subject` parameter, which will contain `Patient` resource 
- `FHIRPath` expressions to retrieve data.

> `%subject` parameter will be filled with data in population operation


#### Expressions:

Patient name 

```fhirpath
%subject.name.where(use='official').family + ' ' + %subject.name.where(use='official').given.first()
```

DOB (Date widget)

```fhirpath
%subject.birthDate
```

MRN (Text widget)

```fhirpath
%subject.identifier.where(type.coding.system='http://terminology.hl7.org/CodeSystem/v2-0203', type.coding.code='MR').value
```

address (Text widget)

```fhirpath
%subject.address.where(use='home').text
```

phone (Text widget)

```fhirpath
%subject.telecom.where(system='phone', use='home').value
```

### Populate Parameters

To make `%subject` resource available we should call `$populate` operation with specific parameters

- `subject = <reference>` (reference to patient)
- `local = true` (says that we should search for subject in DB and load resource)

Operation call example:

```yaml
POST /fhir/Questionnaire/<qid>/$populate

resourceType: Parameters
parameter:
- name: subject
  valueReference:
    id: example
    resourceType: Patient
- name: local
  valueBoolean: true
```

## How to populate form with patient weight, height

To populate a form we should: 

1. setup a form to be able to get patient's observations
2. provide partient's reference to population operation. 

### Form Setup 

Assume that we have:
- Form with `body weight` and `body height` fields
- Patient in DB
- Patient's `body weight` and `body height` `Observations` in DB

Stored `Observations` should be linked to a patient and should be coded with right terminology code (LOINC in our example)

LOINC coding for body measurements:

- Body Height: `{"system" : "http://loinc.org", "code" : "8302-2"}`
- Body Weight: `{"system" : "http://loinc.org", "code" : "29463-7"}`


Observation examples

Body Weight

```json
{
  "resourceType": "Observation",
  "subject": {"id": "example", "resourceType": "Patient"},
  "status": "final",
  "code": {"coding": [{"code": "29463-7", "system": "http://loinc.org"}]},
  "value": {"Quantity": {"unit": "kg", "value": 80}}
}
```

Body Height

```json
{
  "resourceType": "Observation",
  "subject": {"id": "example", "resourceType": "Patient"},
  "status": "final",
  "code": {"coding": [{"code": "8302-2", "system": "http://loinc.org"}]},
  "value": {"Quantity": {"unit": "cm", "value": 180}}
}
```

We should configure items with Observation based population

1. Select item in outline
2. Press `include code?` section and type corresponding code/system (from Observations)
3. Enable `Populate` section (`Observation` population should be opened by default) and choose period to search for Observations. (For example `1 Month`)

### Populate parameters

To pass Patient's reference we use  `subject` parameter to `$populate` opearation

Operation call example:

```yaml
POST /fhir/Questionnaire/<qid>/$populate

resourceType: Parameters
parameter:
- name: subject
  valueReference:
    id: example
    resourceType: Patient
```

## How to populate form with patient allergies



## How to populate form with data from another form during the visit

