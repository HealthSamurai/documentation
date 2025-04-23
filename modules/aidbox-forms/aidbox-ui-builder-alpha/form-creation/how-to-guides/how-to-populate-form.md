# How to: populate forms with data

## Warn

### You should have items with types - that corresponds populate values types.

In all populate items configuration you should use proper item(widget) for populate data

| Item               | data type     |
| ------------------ | ------------- |
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

* form with 5 items(data: patient name, DOB, MRN, address, phone)
* Patient resource in the Aidbox DB

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

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to-populate-form.md#warn))

We should setup items with `populate` expressions.

> How to find population expression:
>
> 1. select widget in the outline 2 click on `populate` checkbox in the widget settings panel
> 2. in opened section select `Expression` tab

For this example we will use:

* `%subject` parameter, which will contain `Patient` resource
* `FHIRPath` expressions to retrieve data.

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

* `subject = <reference>` (reference to patient)
* `local = true` (says that we should search for subject in DB and load resource)

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
2. provide patient's reference to population operation. (usage time)

### Form Setup (design time)

Assume that we have:

* Form with `body weight` and `body height` items
* Patient in DB
* Patient's `body weight` and `body height` `Observations` in DB

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to-populate-form.md#warn))

Stored `Observations` should be linked to a patient and should be coded with right terminology code (LOINC in our example)

LOINC coding for body measurements:

* Body Height: `{ system: http://loinc.org, code: 8302-2 }`
* Body Weight: `{ system: http://loinc.org, code: 29463-7 }`

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

To populate a form with data from allergies we should

1. setup a form to be able to find allergies for a patient and populate them in a list (design time)
   * Create a `group`/`group table` with columns of proper types
   * Set `named expression` for created `group` to search for allergies
   * Set columns `populate expressions` to extract data from found `AllergyIntolerance` resources
2. provide `Patient` reference in input parameters of populate operation (usage time)

### Form Setup (design time)

Assume that we already have:

* Form for Allergies
* Several `AllergyIntolerance` resources in DB

#### `AllergyIntolerance` resource examples:

Food allergy

```yaml
resourceType: AllergyIntolerance
id: example
type: allergy
patient:
  reference: Patient/pt-1
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
  reference: Patient/pt-1
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

Since there can be several allergies - we should use **Group Table** (or **Group**) for them. It will allow us to grow a form with new elements.

Every table row should have next columns:

* category (text input)
* allergy code (text item)
* reaction (Choice input)
* criticality (Choice input)

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to-populate-form.md#warn))

Reaction and allergy code should be taken from _Terminology_ server, in demo purposes we just set answerOptions with predefined values.

#### Create a table with columns

1. Press `+ Add widget` button in the outline
2. Select `Group Table` in a opened widget panel.

You will get a group table with 2 items in it.

Now we must setup our inputs

1. Remove predefined `Group table's` items
   * hover items with mouse and click on trash icon. (outline)
2. Create `Category` column and set answer options [Category item FHIR Spec](http://hl7.org/fhir/r4/valueset-allergy-intolerance-category.html)
   * Hover `Group Table` item in the outline and click on `+` sign - this will open an items list panel for choosing item.
   * Select `Choice Input` item type in a items list panel
   * Type it's title = `Category` in `text` input of item's settings panel
   * Find `Options` section in `Attributes` segment of `item's settings` panel
   * Fill out options with next value [Category item FHIR Spec](http://hl7.org/fhir/r4/valueset-allergy-intolerance-category.html)
     * `code` = `food` , `display` = `Food`
     * `code` = `medication` , `display` = `Medication`
     * `code` = `environment` , `display` = `Environment`
     * `code` = `biologic` , `display` = `Biologic`
3. Create `Allergy Code` column
   * Repeat actions from 2nd step with next values
   * `text` = `Allergy code`
   * `Options` are
     * `system` = `http://snomed.info/sct`, `code` = `409137002`, `display` = `No Known Drug Allergy (situation)`
     * `system` = `http://snomed.info/sct`, `code` = `227493005`, `display` = `Cashew nuts` (in production we should use ValuseSet here, but for demo purposes we just fill our value from existed `AllergyIntolerance` resource)
4. Create `Reaction` column and set answer options
   * Repeat actions from 2nd step with next values
   * `text` = `Reaction`
   * `Options` are:
     * `code` = `1160593`, `display` = `cashew nut allergenic extract Injectable Product` (in production we should use ValuseSet here, but for demo purposes we just fill our value from existed `AllergyIntolerance` resource)
5. Create `Criticality` column and set answer options
   * Repeat actions from 2nd step with next values
   * Find `Options` section in `Attributes` segment of `item's settings` panel
   * text = `Criticality`
   * `Options` are: [Criticality item FHIR Spec](http://hl7.org/fhir/r4/valueset-allergy-intolerance-criticality.html)
     * `code` = `low` , `display` = `Low Risk`
     * `code` = `high` , `display` = `High Risk`
     * `code` = `unable-to-assess` , `display` = `Unable to Assess Risk`

#### Set table `population expression`

At first we should design and debug a FHIR Query to find `AllergyIntolerance` resources

For searching `AllergyIntolerance` we need only `Patient` reference that we get as `%subject` Input Parameter see Input Parameters section

Complete FHIR Search Query looks like this:

```
GET /AllergyIntolerance?patient=pt-1
```

We should specify form's `named expression` with this query, but with small modifications:

* remove http method (`GET`)
* replace `patient` parameter value (= `pt-1`) with embedded `FHIRPath` expression `{{%subject.id}}`

> ```
> /AllergyIntolerance?patient={{%subject.id}}
> ```

> Embedded FHIRPath expression `{{%subject.id}}` consists of:
>
> * `{{}}` - FHIRPath expression embedding point
> * `%subject` - populate input parameter. (all parameters start with `%` sign)
> * `%subject.id` - `FHIRPath` expression that extracts `id` from `Patient` reference

To specify named expression we should:

1. Click on `group table` item in the outline panel
2. Enable `Populate` section in item's settings panel (`Observation` population should be opened by default)
3. Select `Expression` tab in `Populate` section.
4. enter `expression name` = `allergy` (we will use expression by name in next section)
5. set `expression language` = `FHIRQuery`
6. Copy `FHIRQuery` that we get in last step

#### Set columns `population expressions`

For every column we should set populate expression which extracts data from `%allergy` named expression.

1. Select column item in the outline
2. Enable `Populate` section (`Observation` population should be opened by default)
3. Select `Expression` tab
4. Enter `FHIRPath` expression that extracts needed value.

Category (Choice input)

```fhirpath
%qitem.answerOption.valueCoding.where(code = %allergy.category.first())
```

> There can be several catigories but for demo purpores we just use first of them

Allergy code (Choice item)

```
%allergy.reaction.substance.coding.first()
```

Reaction (Choice input)

```fhirpath
%allergy.reaction.substance.coding.first()
```

> There can be several reactoins but for demo purpores we just use first of them

Criticality (Choice input)

```fhirpath
%qitem.answerOption.valueCoding.where(code = %allergy.criticality)
```

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

## How to populate form with data from another form during the visit

To populate a form with data from another form we should:

1. setup a form to be able to find another form's response and get information from it (design time)
   * Enable input parameter, that is common for both forms. (`Encounter` in our case)
   * Set form's named expression with `FHIRQuery` to search for response in DB
   * Set item's populate expression to extract data from found `QuestionnaireResponse`
2. provide `Encounter` reference in input parameters of populate operation (usage time)

### Form Setup (design time)

Assume that we have:

* 1st Form and it's response with captured data in DB, which will be used as data source
* 2nd Form, that should be pre-populated

> WARN: You should have items with types - that corresponds populate values types. (see [WARN section](how-to-populate-form.md#warn))

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

* `encounter reference` - will be common with our form
* `questionnaire` - unique `Questionnaire's` identifier
* item's `linkId` - will be used to extract an answer in following section.

We need to build `FHIR Search Query` to find this response.

It's better to design and debug query in **Aidbox REST Console**

Complete FHIR Search Query looks like this:

```
GET /QuestionnaireResponse?status=completed&questionnaire=http://aidbox.io/forms/patient-name|1.0.0&encounter=enc-1
```

It uses several filter criteria:

* `status = completed` - we need only submitted forms
* `questionnaire` - canonical URL of response's questionnaire
* `encounter` - our common reference, for example `enc-1`

We should specify form's `named expression` with this query, but with small modifications:

* remove http method (`GET`)
* replace `encounter` parameter value (= `enc-1`) with embedded `FHIRPath` expression `{{%encounter.id}}`

> ```
> /QuestionnaireResponse?status=completed&questionnaire=http://aidbox.io/forms/patient-name|1.0.0&encounter={{%encounter.id}}
> ```

> Embedded FHIRPath expression `{{%encounter.id}}` consists of:
>
> * `{{}}` - FHIRPath expression embedding point
> * `%encounter` - populate input parameter. (all parameters start with `%` sign)
> * `%encounter.id` - `FHIRPath` expression that extracts `id` from `Encounter` reference

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
POST /fhir/Questionnaire/<qid>/$populatelink

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
