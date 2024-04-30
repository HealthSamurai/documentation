# How Tos

## Warn 

### You should have items with types - that corresponds populate values types.

In all populate items configuration you should use proper item(widget) for populate data 

| Item               | data type     |
|--------------------|---------------|
| String Input       | string        |
| Textarea           | string        |
| URL                | url           |
| Integer Input      | integer       |
| Decimal Input      | decimal       |
| Quantity Input     | quantity      |
| Date               | date          |
| Time               | time          |
| Datetime           | dateTime      |
| Choice Input       | coding        |
| Open Choice Input  | coding/string |
| Radio Button       | coding        |
| Boolean input      | boolean       |
| File               | attachment    |
| Author's Signature | attachment    |
| Reference          | reference     |

## How to populate form with patient demographic data: patient name, DOB, MRN, address, phone

To populate a form we should: 

1. setup a form to be able to get patient's data and prefill form items with it (design time)
2. provide patient's reference to population operation. (usage time)

### Form Setup (design time)

Assume that we already have:

- form with 5 items(data: patient name, DOB, MRN, address, phone)
- Patient resource in the Aidbox DB

Patient resource example:

```yaml
resourceType: Patient
id: example
gender: male
name:
- family: Chalmers
  given:
  - Peter
  - James
address:
- city: PleasantVille
  district: Rainbow
  postalCode: '3999'
  text: 534 Erewhon St PeasantVille, Rainbow, Vic  3999
  line:
  - 534 Erewhon St
  state: Vic
identifier:
- use: usual
  type:
    coding:
    - system: http://terminology.hl7.org/CodeSystem/v2-0203
      code: MR
  system: urn:oid:1.2.36.146.595.217.0.1
  value: '12345'
birthDate: '1974-12-25'
telecom:
- system: phone
  value: (03) 5555 6473

```

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to.md#warn))

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
%subject.name.family + ' ' + %subject.name.given.first()
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
%subject.address.text
```

phone (Text widget)

```fhirpath
%subject.telecom.value
```

### Populate Parameters (usage time)

To make `%subject` resource available we should call `$populate` operation with specific parameters

- `subject = <reference>` (reference to patient)
- `local = true` (says that we should search for subject in DB and load resource)

Operation call example:

```yaml
POST /fhir/Questionnaire/<qid>/$populatelink

resourceType: Parameters
parameter:
- name: subject
  valueReference:
    reference: Patient/example
- name: local
  valueBoolean: true
```




## How to populate form with patient weight, height

To populate a form we should: 

1. setup a form to be able to get patient's observations (design time)
2. provide patient's reference to population operation.  (usage time)

### Form Setup (design time)

Assume that we have:
- Form with `body weight` and `body height` items
- Patient in DB
- Patient's `body weight` and `body height` `Observations` in DB

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to.md#warn))

Stored `Observations` should be linked to a patient and should be coded with right terminology code (LOINC in our example)

LOINC coding for body measurements:

- Body Height: `{ system: http://loinc.org, code: 8302-2 }`
- Body Weight: `{ system: http://loinc.org, code: 29463-7 }`


Observation examples

Body Weight

```yaml
resourceType: Observation
subject:
  reference: Patient/example
status: final
code:
  coding:
  - code: 29463-7
    system: http://loinc.org
valueQuantity:
  unit: kg
  value: 80
```

Body Height

```yaml
resourceType: Observation
subject:
  reference: Patient/example
status: final
code:
  coding:
  - code: 8302-2
    system: http://loinc.org
valueQuantity:
  unit: cm
  value: 180

```

We should configure items with Observation based population

1. Select item in outline
2. Press `include code?` section and type corresponding code/system (from Observations)
3. Enable `Populate` section (`Observation` population should be opened by default) and choose period to search for Observations. (For example `1 Month`)

### Populate parameters (usage time)

To pass Patient's reference we use `subject` parameter to `$populatelink`/`$populate` operation.

Operation call example:

```yaml
POST /fhir/Questionnaire/<qid>/$populatelink

resourceType: Parameters
parameter:
- name: subject
  valueReference:
    reference: Patient/example
```


## How to populate form with patient allergies

(**WIP: not completed**)

To populate a form with data from allergies we should

1. setup a form to be able to find allergies for a patient and populate them in a list (design time)
    - Create a `group` item with `named expression` to search for allergies
    - Set items populate expressions to extract data from found `AllergyIntolerance`
2. provide `Patient` reference in input parameters of populate operation  (usage time)

### Form Setup (design time)

Assume that we already have:

- Form for Allergies
- Several `AllergyIntolerance` resources in DB

`AllergyIntolerance` resource examples:

Food allergy

```yaml
resourceType: AllergyIntolerance
id: example
type: allergy
patient:
  reference: Patient/example
category:
- food
criticality: high
recordedDate: '2014-10-09T14:58:00+11:00'
onsetDateTime: '2004'
clinicalStatus:
  coding:
  - system: http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical
    code: active
    display: Active
lastOccurrence: 2012-06
reaction:
- substance:
    coding:
    - system: http://www.nlm.nih.gov/research/umls/rxnorm
      code: '1160593'
      display: cashew nut allergenic extract Injectable Product
  manifestation:
  - coding:
    - system: http://snomed.info/sct
      code: '39579001'
      display: Anaphylactic reaction
  description: Challenge Protocol. Severe reaction to subcutaneous cashew extract. Epinephrine administered
  onset: '2012-06-12'
  severity: severe
code:
  coding:
  - system: http://snomed.info/sct
    code: '227493005'
    display: Cashew nuts
```

No Known Drug Allergy

```yaml
resourceType: AllergyIntolerance
id: nkda
patient:
  reference: Patient/example
recordedDate: '2015-08-06T15:37:31-06:00'
clinicalStatus:
  coding:
  - system: http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical
    code: active
    display: Active
code:
  coding:
  - system: http://snomed.info/sct
    code: '409137002'
    display: No Known Drug Allergy (situation)
  text: NKDA
```

Since there can be several allergies - we should use **Group Table** (or **Group**) for them.
It will allow us to grow a form with new elements.

Every row of a table should have next items:

- category (text input)
- allergy code (text item)
- reaction (Choice input)
- criticality (Choice input)

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to.md#warn))

reaction and allergy code should be taken from _Terminology_ server, in demo purposes we just set answerOptions with predefined values.

Create a table

1. Press `+ Add widget` button in the outline
2. Select `Group Table` in a opened widget panel. 

You will get a group table with 2 items in it.

Now we must setup our inputs 

1. Remove predefined `Group table's` items 
    - hover items with mouse and click on trash icon. (outline)
2. Create new `Group table` column
    - Hover `Group Table` item in the outline and click on `+` sign - this will open an items list panel for choosing item.
    - Select needed item type in a items list panel (for first column it should be `Text Input`)
3. Enter text for created column 
    - Select item (in the outline) and type it's title in `text` input of item's settings panel (for first column it should be `Category`)
4. Repeat 2-3 steps for other columns
    - `Text input` with text `Allergy code`
    - `Choice input` with text `Reaction`
    - `Choice input` with text `Criticality` 


TBD

### Populate parameters (usage time)

TBD

## How to populate form with data from another form during the visit

To populate a form with data from another form we should: 

1. setup a form to be able to find another form's response and get information from it (design time)
    - Enable input parameter, that is common for both forms. (`Encounter` in our case)
    - Set form's named expression with `FHIRQuery` to search for response in DB
    - Set item's populate expression to extract data from found `QuestionnaireResponse`
2. provide `Encounter` reference in input parameters of populate operation (usage time)


### Form Setup (design time)

Assume that we have:
- 1st Form and it's response with captured data in DB, which will be used as data source
- 2nd Form, that should be pre-populated

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to.md#warn))

> We are working only with 2nd form in this demo

#### Enable input parameter

To use `%encounter` input parameter - we should enable it.

1. Click on form's name in the outline panel (top left corner of the Form Builder)
2. In `Populate section` of form's settings panel click on `Encounter` checkbox.

#### Setup query to find a resource

Response of 1st form should be stored in DB and looks like this:

```yaml
resourceType: QuestionnaireResponse
status: completed
questionnaire: http://aidbox.io/forms/patient-name|1.0.0
encounter:
  reference: Encounter/enc-1
item:
- linkId: patient-name
  text: Patient Name
  answer:
  - valueString: John Smith
```

We are interested in following values from it.

- `encounter reference` - will be common with our form
- `questionnaire` - unique `Questionnaire's` identifier 
- item's `linkId` - will be used to extract an answer in following section.

We need to build `FHIR Search Query` to find this response.

It's better to design and debug query in **Aidbox REST Console**

Complete FHIR Search Query looks like this:

```
GET /QuestionnaireResponse?status=completed&questionnaire=http://aidbox.io/forms/patient-name|1.0.0&encounter=enc-1
```

It uses several filter criteria:

- `status = completed` - we need only submitted forms
- `questionnaire`  - canonical URL of response's questionnaire
- `encounter` - our common reference, for example `enc-1`

We should specify form's `named expression` with this query, but with small modifications:

- remove http method (`GET`) 
- replace `encounter` parameter value (= `enc-1`) with embedded `FHIRPath` expression  `{{%encounter.id}}`

> ```
> /QuestionnaireResponse?status=completed&questionnaire=http://aidbox.io/forms/patient-name|1.0.0&encounter={{%encounter.id}}
> ```

> Embedded FHIRPath expression `{{%encounter.id}}`  consists of:
> - `{{}}` -  FHIRPath expression embedding point
> - `%encounter` - populate input parameter. (all parameters start with `%` sign)
> - `%encounter.id` - `FHIRPath` expression that extracts id from `Encounter` reference

To specify named expression we should:

1. Click on form's name in the outline panel (top left corner of the Form Builder)
2. In form's settings panel click button `+ Add Expression` (`Named Expressions` section).
3. Select created empty line
4. enter `expression name` = `response` (we will use expression by name in next section)
5. set `expression language` = `FHIRQuery`
6. Copy `FHIRQuery` that we get in last step
7. Click `close` button in the `named expression` form
  
#### Set item's populate expression

We should use created `named expression` (`%response`) to extract a value and fill out our item.

1. Select the item in outline 
2. Enable `Populate` section (`Observation` population should be opened by default)
3. Select `Expression` tab 
4. Enter `FHIRPath` expression that extracts needed value.

```fhirpath
%response.repeat(item).where(linkId='patient-name').answer.value
```

### Populate parameters (usage time)

To pass Encounter's reference we use `context` parameter - `encounter` to `$populatelink`/`$populate` operation

Operation call example:

```yaml
POST /fhir/Questionnaire/<qid>/$populate

resourceType: Parameters
parameter:
- name: context
  part: 
    - name: name
      valueString: Encounter
    - name: value
      valueReference:
        reference: Encounter/enc-1
```
