---
description: This article outlines operations from the FHIR SDC Implementation Guide.
---

> Implementation corresponds to  FHIR SDC - **STU 3 Release (STU)**

# FHIR SDC API

Aidbox Forms module supports FHIR SDC operations:

* [$populate](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populate.html)  - filling out a form with existing data
* [$extract](https://hl7.org/fhir/uv/sdc/OperationDefinition-QuestionnaireResponse-extract.html) - extract data from QuestionnaireResponse to other FHIR resources
* [$expand](https://www.hl7.org/fhir/valueset-operation-expand.html) - create a simple collection of codes suitable for use for data entry or validation.


## Populate Questionnaire - $populate

The [populate](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populate.html) operation generates a [QuestionnaireResponse](https://www.hl7.org/fhir/questionnaireresponse.html) based on a specific [Questionnaire](https://www.hl7.org/fhir/questionnaire.html), 
filling in answers to questions where possible based on information provided as part of the operation or already known by the server about the subject of the Questionnaire.

> This implementation allows the [Observation based](https://hl7.org/fhir/uv/sdc/populate.html#observation-based-population)
and [Expression based](https://hl7.org/fhir/uv/sdc/populate.html#expression-based-population) populations.


### URLs

Populate Questionnaire provided via parameters

```
POST [base]/Questionnaire/$populate
```

Populate Questionnaire referenced via id

```
POST [base]/Questionnaire/[id]/$populate
```

### Parameters:

| Parameter        | Cardinality | Type                                                             | Status          | Example                                              |
|------------------|-------------|------------------------------------------------------------------|-----------------|------------------------------------------------------|
| identifier       | 0..1        | [Identifier](http://hl7.org/fhir/R4/datatypes.html#Identifier)   | `supported`     | [identifier](fhir-sdc-api.md#identifier)             |
| canonical        | 0..1        | [uri](http://hl7.org/fhir/R4/datatypes.html#uri)                 | `supported`     | [canonical](fhir-sdc-api.md#canonical)               |
| questionnaire    | 0..1        | [Questionnaire](http://hl7.org/fhir/R4/questionnaire.html)       | `supported`     | [questionnaire](fhir-sdc-api.md#questionnaire)       |
| questionnaireRef | 0..1        | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     | [questionnaireRef](fhir-sdc-api.md#questionnaireRef) |
| subject          | 1..1        | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     | [subject](fhir-sdc-api.md#valueset)                  |
| context          | 0..*        | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     | [context](fhir-sdc-api.md#context)                   |
| context.name     | 0..*        | [string](https://www.hl7.org/fhir/datatypes.html#string)         | `supported`     | [context](fhir-sdc-api.md#context.name)              |
| context.content  | 0..*        | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     | [context](fhir-sdc-api.md#context.content)           |
| local            | 0..1        | [boolean](http://hl7.org/fhir/R4/datatypes.html#boolean)         | `supported`     | [local](fhir-sdc-api.md#local)                       |
| launchContext    | 0..1        | [Extension](http://hl7.org/fhir/R4/extensibility.html#Extension) | `not supported` | [launchContext](fhir-sdc-api.md#launchContext)       |


> Parameters are specified via FHIR [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) type.

Example

```
resourceType: Parameters
parameter:
- name: subject
  valueReference:
    id: pt-1
    resourceType: Patient
```

#### identifier

A logical questionnaire identifier (i.e. Questionnaire.identifier). The server must know the questionnaire or be able to retrieve it from other known repositories.

```yaml
name: identifier
valueIdentifier:
  system: 'forms.aidbox'
  value: 'vitals'
```

#### canonical

The canonical identifier for the questionnaire (optionally version-specific).


```yaml
name: canonical
valueCanonical: http://forms.aidbox.io/Questionnaire/vitals
```

#### questionnaire

The Questionnaire is provided directly as part of the request. Servers may choose not to accept questionnaires in this fashion

```yaml
name: questionnaire
resource:
  resourceType: Questionnaire
  id: new-form
  title: "Vitals"
  ...
```


#### questionnaireRef

The Questionnaire is provided as a resource reference. Servers may choose not to accept questionnaires in this fashion or may fail if they cannot resolve or access the referenced questionnaire.

```yaml
name: questionnaireRef
valueReference:
  resourceType: Questionnaire
  id: new-form
```

#### subject

The resource that is to be the QuestionnaireResponse.subject. The QuestionnaireResponse instance will reference the provided subject. In addition, if the local parameter is set to true, server information about the specified subject will be used to populate the instance.


```yaml
name: subject
valueReference:
  id: pt-1
  resourceType: Patient
```

#### local
If specified and set to true (and the server is capable), the server should use what resources and other knowledge it has about the referenced subject when pre-populating answers to questions.

```
name: local
valueBoolean: true
```

#### context

Resources containing information to be used to help populate the QuestionnaireResponse. These will typically be FHIR resources.

Context paramertes are presented as pairs of `name` and `content` parameters that are placed under the  `part` key.

They should correspond launchContext parameter definitions.


```yaml
name: context
part:
- name: name
  valueString: encounter
- name: content
  valueReference:
    resourceType: Encounter
    id: enc1
```

FHIR SDC launchContext extension [enumerates](http://hl7.org/fhir/uv/sdc/STU3/CodeSystem-launchContext.html) possible context variables, they are:


| Code      | Definition                                                                                                                         |
|-----------|------------------------------------------------------------------------------------------------------------------------------------|
| patient   | Patient Patient in context at launch time (FHIR Patient resource)                                                                  |
| encounter | Encounter Encounter context at launch time (FHIR Encounter resource)                                                               |
| location  | Location Location context at launch time (FHIR Location resource)                                                                  |
| user      | User User in context at launch time (FHIR Device, PractitionerRole, Practitioner, RelatedPerson, Organization or Patient resource) |
| study     | ResearchStudy in context at launch time (FHIR ResearchStudy resource)                                                              |

Additionnaly Aidbox expands this list with parameters that mirrors [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html) root properties, 
they are:

> WARN: This is non FHIR SDC compliant behavior!


| Code       | Type                                                                                                                                        |
|------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| identifier | [Identifier](http://hl7.org/fhir/R4/datatypes.html#Identifier)                                                                              |
| basedOn    | [Reference<CarePlan, ServiceRequest>](http://hl7.org/fhir/R4/references.html#Reference)                                                     |
| partOf     | [Reference<Observation, Procedure>](http://hl7.org/fhir/R4/references.html#Reference)                                                       |
| encounter  | [Reference<CarePlan/ServiceRequest>](http://hl7.org/fhir/R4/references.html#Reference)                                                      |
| author     | [Reference<Device, Practitioner, PractitionerRole, Patient, RelatedPerson, Organization>](http://hl7.org/fhir/R4/references.html#Reference) |
| source     | [Reference<Device, Organization, Patient, Practitioner, PractitionerRole, RelatedPerson>](http://hl7.org/fhir/R4/references.html#Reference) |

There paramerters passed directly to [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html) root properties and should not be used in populate expressions.


```yaml
name: name
valueReference: 
    id: sr1
    resourceType: ServiceRequest
```


#### context.name

The name of the launchContext or root Questionnaire variable the passed content should be used as for population purposes. The name SHALL correspond to a launchContext or variable delared at the root of the Questionnaire.

```yaml
name: name
valueString: encounter
```

#### context.content

The actual resource (or resources) to use as the value of the launchContext or variable.

```yaml 
name: content
valueReference:
  resourceType: Encounter
  id: enc1
```

Or 

```yaml 
name: content
resource:
  ...
  resourceType: Encounter
  id: enc1
  ...
```

#### launchContext

Resources that provide context for form processing logic (pre-population) when creating/displaying/editing a QuestionnaireResponse.


### Response

- in failure case - response specified as [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html) object.
- in success case - response is specified via [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) object.

Sucess response 


| Parameter | Cardinality | Type                                                                        | Description                                                                                 |
|-----------|-------------|-----------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| response  | 1..1        | [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html) | The partially (or fully)-populated set of answers for the specified Questionnaire           |
| issues    | 0..1        | [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html)           | A list of hints and warnings about problems encountered while populating the questionnaire. |

### Request Example


{% tabs %}
{% tab title="Request" %}
```http
POST [base]/Questionnaire/vitals/$populate
content-type: application/json

resourceType: Parameters
parameter:
- name: subject
  valueReference:
    id: pt-1
    resourceType: Patient
```
{% endtab %}

{% tab title="Success Response" %}

HTTP status: 200

```yaml
resourceType: Parameters
parameter:
- name: response
  resource:
    resourceType: QuestionnaireResponse
    questionnaire: http://aidbox.app/forms/new-form
    status: in-progress
    basedOn:
    - id: sr1
      resourceType: ServiceRequest
    encounter:
      id: enc1
      resourceType: Encounter
    item:
    - linkId: name
      text: Name
    - linkId: weight
```
{% endtab %}

{% tab title="Failure Response" %}

HTTP status: 422

```yaml
resourceType: OperationOutcome
text:
  status: generated
  div: Parameters are invalid
issue:
- severity: error
  code: invalid
  expression:
  - parameter.0.resource
  diagnostics: unknown key :resource

```
{% endtab %}

{% endtabs %}


## Questionnaire response extract to resources - $extract <a href="#root" id="root"></a>

The  `extract` operation takes a completed QuestionnaireResponse and converts it to a FHIR resource or Bundle of resources by using metadata embedded in the Questionnaire the QuestionnaireResponse is based on. The extracted resources might include Observations, MedicationStatements and other standard FHIR resources which can then be shared and manipulated.&#x20;

{% hint style="warning" %}
When invoking the $extract operation, care should be taken that the submitted QuestionnaireResponse is itself valid. If not, the extract operation could fail (with appropriate OperationOutcomes) or, more problematic, might succeed but provide incorrect output.
{% endhint %}

This implementation allows the [Observation based](https://hl7.org/fhir/uv/sdc/extraction.html#observation-based-extraction) extraction.



## ValueSet Expansion - $expand

Value Sets are used to define possible coded answer choices in a questionnaire.

The use of standardized codes is useful when data needs to be populated into the questionnaire or extracted from the questionnaire for other uses.

The `expand` operation expand given ValueSet in to set of concepts.

This operation is described in detail [here](../../terminology/valueset/value-set-expansion.md).
