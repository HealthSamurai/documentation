---
description: This article outlines operations from the FHIR SDC Implementation Guide.
---

# FHIR SDC API

> Implementation corresponds to [SDC FHIR IG - 3.0.version 0](http://hl7.org/fhir/uv/sdc/)

## FHIR SDC API

Aidbox Forms module supports FHIR SDC operations:

* [$populate](fhir-sdc-api.md#populate-questionnaire-usdpopulate) - filling out a form with existing data ([FHIR](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populate.html))
* [$populatelink](fhir-sdc-api.md#populate-questionnaire-and-generate-a-link-usdpopulatelink) - filling out a form with existing data, and return signed link to it ([FHIR](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populatelink.html))
* [$extract](fhir-sdc-api.md#questionnaire-response-extract-to-resources-usdextract) - extract data from QuestionnaireResponse to other FHIR resources ([FHIR](https://hl7.org/fhir/uv/sdc/OperationDefinition-QuestionnaireResponse-extract.html))
* [$expand](fhir-sdc-api.md#valueset-expansion-usdexpand) - create a simple collection of codes suitable for use for data entry or validation. ([FHIR](https://www.hl7.org/fhir/valueset-operation-expand.html))

## Populate Questionnaire - $populate

The [populate](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populate.html) operation generates a [QuestionnaireResponse](https://www.hl7.org/fhir/questionnaireresponse.html) based on a specific [Questionnaire](https://www.hl7.org/fhir/questionnaire.html), filling in answers to questions where possible based on information provided as part of the operation or already known by the server about the subject of the Questionnaire.

> This implementation supports the [Observation based](https://hl7.org/fhir/uv/sdc/populate.html#observation-based-population) and [Expression based](https://hl7.org/fhir/uv/sdc/populate.html#expression-based-population) population methods.

### URLs

```
POST [base]/Questionnaire/$populate
```

```
POST [base]/Questionnaire/[id]/$populate
```

### Parameters

| Parameter                                            | Cardinality | Type                                                             | Status          |
| ---------------------------------------------------- | ----------- | ---------------------------------------------------------------- | --------------- |
| [identifier](fhir-sdc-api.md#identifier)             | 0..1        | [Identifier](http://hl7.org/fhir/R4/datatypes.html#Identifier)   | `supported`     |
| [canonical](fhir-sdc-api.md#canonical)               | 0..1        | [uri](http://hl7.org/fhir/R4/datatypes.html#uri)                 | `supported`     |
| [questionnaire](fhir-sdc-api.md#questionnaire)       | 0..1        | [Questionnaire](http://hl7.org/fhir/R4/questionnaire.html)       | `supported`     |
| [questionnaireRef](fhir-sdc-api.md#questionnaireref) | 0..1        | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     |
| [subject](fhir-sdc-api.md#subject)                   | 1..1        | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     |
| [context](fhir-sdc-api.md#context)                   | 0..\*       | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     |
| [context.name](fhir-sdc-api.md#context.name)         | 1..1        | [string](https://www.hl7.org/fhir/datatypes.html#string)         | `supported`     |
| [context.content](fhir-sdc-api.md#context.content)   | 1..\*       | [Reference](http://hl7.org/fhir/R4/references.html#Reference)    | `supported`     |
| [local](fhir-sdc-api.md#local)                       | 0..1        | [boolean](http://hl7.org/fhir/R4/datatypes.html#boolean)         | `supported`     |
| [launchContext](fhir-sdc-api.md#launchcontext)       | 0..1        | [Extension](http://hl7.org/fhir/R4/extensibility.html#Extension) | `not supported` |

> Parameters are specified via FHIR [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) type.

Example

```
resourceType: Parameters
parameter:
- name: subject
  value:
    Reference:
      id: pt-1
      resourceType: Patient
```

#### identifier

A logical questionnaire identifier (i.e. Questionnaire.identifier). The server must know the questionnaire or be able to retrieve it from other known repositories.

```yaml
name: identifier
value:
  Identifier:
    system: 'forms.aidbox'
    value: 'vitals'
```

#### canonical

The canonical identifier for the questionnaire (optionally version-specific).

```yaml
name: canonical
value:
  Canonical: http://forms.aidbox.io/Questionnaire/vitals
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
value:
  Reference:
    resourceType: Questionnaire
    id: new-form
```

#### subject

The resource that is to be the QuestionnaireResponse.subject. The QuestionnaireResponse instance will reference the provided subject. In addition, if the local parameter is set to true, server information about the specified subject will be used to populate the instance.

```yaml
name: subject
value:
  Reference:
    id: pt-1
    resourceType: Patient
```

#### local

If specified and set to true (and the server is capable), the server should use what resources and other knowledge it has about the referenced subject when pre-populating answers to questions.

```
name: local
value:
  Boolean: true
```

#### context

Resources containing information to be used to help populate the QuestionnaireResponse. These will typically be FHIR resources.

Context paramertes are presented as pairs of `name` and `content` parameters that are placed under the `part` key.

They should correspond launchContext parameter definitions.

```yaml
name: context
part:
- name: name
  value:
    String: encounter
- name: content
  value:
    Reference:
      resourceType: Encounter
      id: enc1
```

FHIR SDC launchContext extension [enumerates](http://hl7.org/fhir/uv/sdc/STU3/CodeSystem-launchContext.html) possible context variables, they are:

| Code      | Type                                                                                                                                                  | Definition                                |
| --------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------- |
| patient   | [Reference](http://hl7.org/fhir/R4/references.html#Reference)/resource                                                                                | Patient Patient in context at launch time |
| encounter | [Reference](http://hl7.org/fhir/R4/references.html#Reference)/resource                                                                                | Encounter context at launch time          |
| location  | [Reference](http://hl7.org/fhir/R4/references.html#Reference)/resource                                                                                | Location context at launch time           |
| user      | [Reference\<Patient, Device, PractitionerRole, Practitioner, RelatedPerson, Organization>](http://hl7.org/fhir/R4/references.html#Reference)/resource | User in context at launch time            |
| study     | [Reference](http://hl7.org/fhir/R4/references.html#Reference)/resource                                                                                | ResearchStudy in context at launch time   |

Additionnaly Aidbox expands this list with parameters that mirrors [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html) root properties.

{% hint style="warning" %}
WARN: This is non FHIR SDC compliant behavior!

Paramerters passed directly to [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html) root properties and should not be used in populate expressions.
{% endhint %}

| Code       | Type                                                                                                                                         |
| ---------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| identifier | [Identifier](http://hl7.org/fhir/R4/datatypes.html#Identifier)                                                                               |
| basedOn    | [Reference\<CarePlan, ServiceRequest>](http://hl7.org/fhir/R4/references.html#Reference)                                                     |
| partOf     | [Reference\<Observation, Procedure>](http://hl7.org/fhir/R4/references.html#Reference)                                                       |
| encounter  | [Reference\<CarePlan/ServiceRequest>](http://hl7.org/fhir/R4/references.html#Reference)                                                      |
| author     | [Reference\<Device, Practitioner, PractitionerRole, Patient, RelatedPerson, Organization>](http://hl7.org/fhir/R4/references.html#Reference) |
| source     | [Reference\<Device, Organization, Patient, Practitioner, PractitionerRole, RelatedPerson>](http://hl7.org/fhir/R4/references.html#Reference) |

Example:

```yaml
name: name
value:
  Reference: 
    id: sr1
    resourceType: ServiceRequest
```

#### context.name

The name of the launchContext or root Questionnaire variable the passed content should be used as for population purposes. The name SHALL correspond to a launchContext or variable delared at the root of the Questionnaire.

```yaml
name: name
value:
  String: encounter
```

#### context.content

The actual resource (or resources) to use as the value of the launchContext or variable.

```yaml
name: content
value:
  Reference:
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
  subject: 
    id: pt-1
    resourceType: Patient
  ...
```

#### launchContext

Resources that provide context for form processing logic (pre-population) when creating/displaying/editing a QuestionnaireResponse.

### Response

* in failure case - response is specified as [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html) object.
* in success case - response is specified as [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) object.

Success response shape

| Parameter | Cardinality | Type                                                                        | Description                                                                                 |
| --------- | ----------- | --------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| response  | 1..1        | [QuestionnaireResponse](https://hl7.org/fhir/R4/questionnaireresponse.html) | The partially (or fully)-populated set of answers for the specified Questionnaire           |
| issues    | 0..1        | [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html)           | A list of hints and warnings about problems encountered while populating the questionnaire. |

### Usage Example

{% tabs %}
{% tab title="Request" %}
```http
POST [base]/Questionnaire/vitals/$populate
content-type: text/yaml

resourceType: Parameters
parameter:
- name: subject
  value:
    Reference:
      id: pt-1
      resourceType: Patient
- name: context
  part:
  - name: name
    value:
      String: encounter
  - name: content
    value:
      Reference:
        resourceType: Encounter
        id: enc1
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

## Populate Questionnaire and generate a link - $populatelink

The [populatelink](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populatelink.html) operation generates a link to a web page to be used to answer a specified [Questionnaire](https://www.hl7.org/fhir/questionnaire.html). The form at the specified location will be pre-filled with answers to questions where possible based on information provided as part of the operation or already known by the server about the subject of the Questionnaire.

### URLs

```
POST [base]/Questionnaire/$populatelink
```

```
POST [base]/Questionnaire/[id]/$populatelink
```

### Parameters

The base set of parameters is the same as for [$populate](fhir-sdc-api.md#parameters). On top of that, a few extra parameters are supported by Aidbox Forms.

{% hint style="warning" %}
WARN: The following parameters are not FHIR SDC compliant. This is an extension of the specification.
{% endhint %}

{% hint style="warning" %}
NOTE: Don't forget to wrap parameters in `Parameters object`


```yaml
resourceType: Parameters
parameter:
- name:  [var-name]
  value: [varType: var-value]
```
{% endhint %}


| Parameter                                                | Cardinality | Type                                                     |
|----------------------------------------------------------|-------------|----------------------------------------------------------|
| [allow-amend](fhir-sdc-api.md#allow-amend)               | 0..1        | [Boolean](http://hl7.org/fhir/R4/datatypes.html#boolean) |
| [redirect-on-submit](fhir-sdc-api.md#redirect-on-submit) | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)   |
| [redirect-on-submit](fhir-sdc-api.md#redirect-on-submit) | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)   |
| [expiration](fhir-sdc-api.md#link-expiration-time)       | 0..1        | [Integer](http://hl7.org/fhir/R4/datatypes.html#integer) |
| [theme](fhir-sdc-api.md#theme)                           | 0..1        | [String](http://hl7.org/fhir/R4/datatypes.html#string)   |
| [read-only](fhir-sdc-api.md#read-only)                   | 0..1        | [Boolean](http://hl7.org/fhir/R4/datatypes.html#boolean) |

#### allow-amend

Whether the generated link will allow amending and re-submitting the form.

```
name: allow-amend
value:
  Boolean: true
```

#### redirect-on-submit

A URL where the user will be redirected to after successfully submitting the form.

```yaml
name: redirect-on-submit
value:
  String: https://example.com/submit-hook?questionnaire=123
```

#### redirect-on-save

A URL where the user will be redirected to after hitting Save button.

> By default `Save button is not visible` - form autosaved after every keystroke. But sometimes it's usefull to close form in a partially-filled state

```yaml
name: redirect-on-save
value:
  String: https://example.com/submit-hook?questionnaire=123
```

#### link expiration time

Link expiration period (days)

```yaml
name: expiration
value:
  Integer: 30
```

> By default thir parameter = 7 days

#### theme

Form theme.

```yaml
name: theme
value:
  String: hs-theme
```

#### read-only

Open form in read-only mode.

```yaml
name: read-only
value:
  Boolean: true
```


### Response

* in failure case - response is specified as [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html) object containing a link.
* in success case - response is specified as [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) object containing a list of issues.

| Parameter | Cardinality | Type                                                              | Description                                                                                |
| --------- | ----------- | ----------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| link      | 1..1        | [uri](http://hl7.org/fhir/R4/datatypes.html#uri)                  | The URL for the web form that supports capturing the information defined by questionnaire  |
| issues    | 0..1        | [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html) | A list of hints and warnings about problems encountered while populating the questionnaire |

### Usage Example

{% tabs %}
{% tab title="Request" %}
```http
POST [base]/Questionnaire/vitals/$populatelink
content-type: text/yaml

resourceType: Parameters
parameter:
- name: subject
  value:
    Reference:
      id: pt-1
      resourceType: Patient
- name: context
  part:
  - name: name
    value:
      String: encounter
  - name: content
    resource:
      resourceType: Encounter
      id: enc-1
      subject:
        id: pt-1
        resourceType: Patient
```
{% endtab %}

{% tab title="Success Response" %}
HTTP status: 200

```yaml
resourceType: Parameters
parameter:
- name: link
  value:
    Uri: http://forms.aidbox.io/ui/sdc#/questionnaire-response/12c1178c-70a9-4e02-a53d-65b13373926e?token=eyJhbGciOiJIUzI

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

## Questionnaire response extract to resources - $extract

The [extract](http://hl7.org/fhir/uv/sdc/OperationDefinition/QuestionnaireResponse-extract) operation takes a completed `QuestionnaireResponse` and extracts it's data to `Bundle` of resources by using metadata embedded in the `Questionnaire` the `QuestionnaireResponse` is based on. The extracted resources might include `Observations`, MedicationStatements and other standard FHIR resources which can then be shared and manipulated.

Aidbox supports only the [Observation based](https://hl7.org/fhir/uv/sdc/extraction.html#observation-based-extraction) and [Definition based](https://hl7.org/fhir/uv/sdc/extraction.html#definition-based-extraction) extraction methods.

### URLs

```http
URL: [base]/QuestionnaireResponse/$extract
```

```
URL: [base]/QuestionnaireResponse/[id]/$extract
```

### Parameters

| Parameter              | Cardinality | Type                                             | Description                                     |
| ---------------------- | ----------- | ------------------------------------------------ | ----------------------------------------------- |
| questionnaire-response | 0..1        | [Resourse](http://hl7.org/fhir/R4/resource.html) | The QuestionnaireResponse to extract data from. |

> Parameters are specified via FHIR [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) type.

Example

```
resourceType: Parameters
parameter:
- name: questionnaire-response
  resource:
    id: qr-1
    resourceType: QuestionnaireResponse
    ...
```

### Response

* in failure case - response is specified as [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html) object.
* in success case - response is specified as [Parameters](https://www.hl7.org/fhir/parameters.html#parameters) object.

| Parameter | Cardinality | Type                                                              | Description                                                                       |
| --------- | ----------- | ----------------------------------------------------------------- | --------------------------------------------------------------------------------- |
| return    | 1..1        | [Resourse](http://hl7.org/fhir/R4/resource.html)                  | FHIR Bundle with extracted resources                                              |
| issues    | 0..1        | [OperationOutcome](https://hl7.org/fhir/R4/operationoutcome.html) | A list of hints and warnings about problems encountered while extracting the data |

### Usage Example

{% tabs %}
{% tab title="Request" %}
```http
POST [base]/QuestionnaireResponse/$extract
content-type: text/yaml

resourceType: Parameters
parameter:
- name: questionnaire-response
  resource:
    id: pt-1
    resourceType: QuestionnaireResponse
    questionnaire: https://forms.aidbox.io/vitals
    item:
      - linkId: temperature
        value:
          Decimal: 36.6
    ...
```
{% endtab %}

{% tab title="Success Response" %}
HTTP status: 200

```yaml
return:
  resourceType: Bundle
  type: transaction
  entry:
  - resource:
      resourceType: Observation
      status: final
      code:
        coding:
        - code: body-temperature
          system: loinc
      subject:
        id: pt-1
        resourceType: Patient
      value:
        Quantity:
          unit: [c]
          value: 36.6
    request: {method: POST,  url: /Observation}

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
  - parameter.0.resource.given
  diagnostics: unknown key :given

```
{% endtab %}
{% endtabs %}

## ValueSet Expansion - $expand

Value Sets are used to define possible coded answer choices in a questionnaire.

The use of standardized codes is useful when data needs to be populated into the questionnaire or extracted from the questionnaire for other uses.

The `expand` operation expand given ValueSet in to set of concepts.

This operation is described in detail [here](../../terminology/valueset/value-set-expansion.md).
