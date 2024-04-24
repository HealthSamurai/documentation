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
POST /fhir/Questionnaire/$populate

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
## How to populate form with patient allergies
## How to populate form with data from another form during the visit


