---
description: The API endpoints facilitate interactions between the Aidbox Forms application and the Aidbox server
---

# API Endpoints

The following API endpoints are utilized by the Aidbox Forms application to interact with Aidbox. 
These endpoints are activated by specific actions within the application, such as saving a questionnaire, 
generating a questionnaire response, or assembling a questionnaire. Each endpoint has a descriptive name 
reflecting the action it performs and the timing of its execution.


## assemble-questionnaire
Triggered after saving the current questionnaire, if it includes sub-questionnaires, to assemble the full questionnaire.

#### Request
```http
POST /fhir/Questionnaire/$assemble HTTP/1.1 
Content-Type: application/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "questionnaire",
        "resource": <questionnaire>
    }]
}
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) being assembled.


#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the assembled [questionnaire](https://www.hl7.org/fhir/questionnaire.html).


## assemble-sub-questionnaire-usages
Triggered after saving a sub-questionnaire to update all parent questionnaires that include it.

#### Request
```http
POST /Questionnaire/$assemble-all HTTP/1.1 
Content-Type: application/json

{
    "questionnaires": [
        {
            "id": <questionnaire-1-id>,
            "resourceType": "Questionnaire"
        },
        {
            "id": <questionnaire-2-id>,
            "resourceType": "Questionnaire"
        },
        ...
    ]
}
```

Where `<questionnaire-1-id>`, `<questionnaire-2-id>`, etc., are the IDs of the parent [questionnaires](https://www.hl7.org/fhir/questionnaire.html) that include the sub-questionnaire.

#### Response
```json
[
  <questionnaire-1>, 
  <questionnaire-2>, 
  ...
]
```

Where `<questionnaire-1>`, `<questionnaire-2>`, etc., are the updated [questionnaires](https://www.hl7.org/fhir/questionnaire.html).

## check-sub-questionnaire-usage
Triggered after saving a sub-questionnaire to verify if it is currently used in any parent questionnaires.

#### Request
```http
GET /Questionnaire/<sub-questionnaire-id>/$usage HTTP/1.1 
```

Where `<sub-questionnaire-id>` is the ID of the sub-questionnaire.

#### Response
```json
{
    "questionnaires": [
        {
            "id": <questionnaire-1-id>,
            "resourceType": "Questionnaire"
        },
        {
            "id": <questionnaire-2-id>,
            "resourceType": "Questionnaire"
        },
        ...
    ]
}
```

Where `<questionnaire-1-id>`, `<questionnaire-2-id>`, etc., are the IDs of the parent [questionnaires](https://www.hl7.org/fhir/questionnaire.html) that include the sub-questionnaire.

## delete-assembled-questionnaire
Triggered after saving the current questionnaire, when the saved questionnaire no longer references sub-questionnaires.

#### Request
```http
DELETE /sdc/Questionnaire?url=<questionnaire-url>&version=<questionnaire-version>-assembled HTTP/1.1
```

Where `<questionnaire-url>` is the canonical URL and `<questionnaire-version>` is the version of the questionnaire being deleted.

#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) that was deleted.

## delete-attachment
Triggered when the attachment input field is cleared.

#### Request
```http
DELETE /$sdc-file/<filepath> HTTP/1.1
```

Where `<filepath>` is the path of the attachment being deleted.

#### Response
Response shape is specific to the storage type and is not processed by the frontend.

## extract
Triggered when the "Extract" button in the debug panel is clicked.

#### Request
```http
POST /fhir/QuestionnaireResponse/$extract HTTP/1.1
Content-Type: application/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "questionnaireResponse",
        "resource": <questionnaire-response>
    }, {
        "name": "questionnaire",
        "resource": <questionnaire>
    }]
}
```

Where `<questionnaire-response>` is the questionnaire response being extracted and `<questionnaire>` is the questionnaire it is based on.

#### Response
```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) of resources extracted from the questionnaire response.

## get-assembled-questionnaire
Triggered after retrieving the current questionnaire to obtain its fully assembled version, if it includes sub-questionnaires.

#### Request
```http
GET /sdc/Questionnaire?url=<questionnaire-url>&version=<questionnaire-version>-assembled HTTP/1.1
```

Where `<questionnaire-url>` represents the canonical URL, and `<questionnaire-version>` specifies the version of the assembled questionnaire being requested.

#### Response
```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) containing zero or one [questionnaire](https://www.hl7.org/fhir/questionnaire.html) resource.

## get-config
Triggered during the initialization of the builder or renderer to fetch configuration details.

#### Request
```http
POST /$sdc-config HTTP/1.1
Content-Type: application/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "id",
        "valueString": "<config-id>"
    }]
}
```

Where `<config-id>` refers to the ID of the SDCConfig resource included in the JWT token payload of the current authentication session. This parameter is omitted if no configuration is specified.

#### Response
```json
<config>
```

Where `<config>` is the [SDCConfig](./configuration.md) resource containing the configuration details.

## get-questionnaire
Triggered during the initialization of the builder or renderer to fetch the current questionnaire.

#### Request
```http
GET /sdc/Questionnaire/<questionnaire-id> HTTP/1.1
```

Where `<questionnaire-id>` is the ID of the questionnaire being requested.

#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) being requested.


## get-questionnaire-response
Triggered during the initialization of the renderer to fetch the current user response.

#### Request
```http
GET /sdc/QuestionnaireResponse/<questionnaire-response-id> HTTP/1.1
```

Where `<questionnaire-response-id>` is the ID of the questionnaire response being requested.

#### Response
```json
<questionnaire-response>
```

Where `<questionnaire-response>` is the [questionnaire response](https://www.hl7.org/fhir/questionnaireresponse.html) being requested.

## get-fhir-metadata
Triggered during the initialization of the builder to fetch metadata for FHIR resources, which aids in autocompletion.

#### Request
```http
GET /fhir/metadata HTTP/1.1
```

#### Response
```json
<fhir-metadata>
```

Where `<fhir-metadata>` is the [metadata](https://www.hl7.org/fhir/capabilitystatement.html) for the FHIR server.

## get-fhir-schemas
Triggered during the initialization of the builder to fetch schemas for FHIR resource elements, aiding in autocompletion.

#### Request
```http
GET /static/fhir_schemas.json HTTP/1.1
```

#### Response
```json
<fhir-schemas>
```

Where `<fhir-schemas>` is the JSON object containing the [schemas](https://fhir-schema.github.io/fhir-schema/) for the FHIR resource elements.

## get-questionnaire-by-id
Triggered before saving the current questionnaire to check for conflicts with existing questionnaires.

#### Request
```http
GET /sdc/Questionnaire/<questionnaire-id> HTTP/1.1
```

Where `<questionnaire-id>` is the ID of the questionnaire being checked.

#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) being checked.

## get-theme
Triggered during the initialization of the web component if a theme is referenced in the configuration.

#### Request
```http
GET /QuestionnaireTheme/<theme-id> HTTP/1.1
```

Where `<theme-id>` is the ID of the theme being requested.

#### Response
```json
<theme>
```

Where `<theme>` is the [theme](./configuration.md#theme) being requested.

## get-themes
Triggered during the initialization of the builder web component and after saving a theme to list available themes in the theme selector.

#### Request
```http
GET /QuestionnaireTheme?_sort=.theme-name HTTP/1.1
```

#### Response
```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) of [themes](./configuration.md#theme).

## import-questionnaire
Triggered when the "Import" button is clicked in the questionnaire importer.

#### Request
```http
POST /sdc/Questionnaire HTTP/1.1
Content-Type: application/json

<questionnaire>
```

Where `<questionnaire>` is the questionnaire being imported.


#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) that was imported.

## populate
Triggered when the "Populate" button is clicked in the debug panel.

#### Request
```http
POST /fhir/Questionnaire/$populate HTTP/1.1
Content-Type: application/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "questionnaire",
        "resource": <questionnaire>
    }, {
        "name": "subject",
        "valueReference": <patient-id>
    }, {
        "name": "local",
        "valueBoolean": true
    }, {
        "name": "context",
        "part": [{
            "name": "name",
            "valueString": "encounter"
        }, {
            "name": "content",
            "valueReference": <encounter-id>
        }, {
            "name": "name",
            "valueString": "author"
        }, {
            "name": "content",
            "valueReference": <patient-id>
        }]
    }]
}
```

Where `<questionnaire>` is the questionnaire being populated, `<patient-id>` is the ID of the patient, and `<encounter-id>` is the ID of the encounter selected in the debug panel. (todo: add explanation)

#### Response
```json
<parameters>
```

Where `<parameters>` is a [parameters](https://www.hl7.org/fhir/parameters.html) resource containing the populated questionnaire response under the `response` name.

## save-response
Triggered by the auto-save mechanism shortly after the user makes changes in the form.

#### Request
```http
POST /fhir/QuestionnaireResponse/$save HTTP/1.1
Content-Type: appliation/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "response",
        "resource": <questionnaire-response>
    }]
}
```

Where `<response>` is the response being submitted. 

{% hint style=“info” %}

## Status Value

Since auto-save is only enabled for non-completed forms, the `response` parameter always has the status `in-progress`.
{% endhint %}

#### Response
```json
<parameters>
```

Where `<parameters>` is a [parameters](https://www.hl7.org/fhir/parameters.html) that includes:
- `response`: the saved questionnaire response.
- `issues`: any validation or processing issues, if available.

## submit-response
Triggered when the user submits a response by clicking the submit button.
The submit button is displayed when the questionnaire response is either in progress (`in-progress`) or when the user is amending a completed response.

#### Request
```http
POST /fhir/QuestionnaireResponse/$submit HTTP/1.1
Content-Type: appliation/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "response",
        "resource": <questionnaire-response>
    }]
}
```

Where `<response>` is the response being submitted. 

{% hint style=“info” %}
Status Value
The `response` parameter contains the current status, and the Aidbox backend is responsible for transitioning it to the appropriate new state.
Therefore, if you need to intercept an amending submission, you can check for the condition `response.status = 'completed'`.
{% endhint %}

#### Response
```json
<parameters>
```

Where `<parameters>` is a [parameters](https://www.hl7.org/fhir/parameters.html) that includes:
- `response`: the processed questionnaire response.
- `issues`: any validation or processing issues, if available.
- `outcome`: a list of extracted resources when the questionnaire has extraction rules.

## repopulate
Triggered when the "Repopulate" button is clicked by the user.

#### Request
```http
POST /fhir/Questionnaire/$populate HTTP/1.1
Content-Type: application/json

{
    "resourceType": "Parameters",
    "parameter": [{
        "name": "questionnaire",
        "resource": <questionnaire>
    }, {
        "name": "response",
        "resource": <questionnaire-response>
    }, {
        "name": "subject",
        "valueReference": <patient-id>
    }, {
        "name": "local",
        "valueBoolean": true
    }, {
        "name": "context",
        "part": [{
            "name": "name",
            "valueString": "encounter"
        }, {
            "name": "content",
            "valueReference": <encounter-id>
        }]
    }]
}
```

Where `<questionnaire>`, `<questionnaire-response>`, `<patient-id>`, and `<encounter-id>` are as described in the [populate](#populate) request.

#### Response
```json
<parameters>
```

Where `<parameters>` is a [parameters](https://www.hl7.org/fhir/parameters.html) resource containing the repopulated questionnaire response under the `response` name.

## save-assembled-questionnaire
Triggered after saving the current questionnaire, if it includes sub-questionnaires, to save the assembled version.

#### Request
```http
PUT /sdc/Questionnaire?url=<questionnaire-url>&version=<questionnaire-version>-assembled HTTP/1.1
Content-Type: application/json

<assembled-questionnaire>
```

Where `<questionnaire-url>` and `<questionnaire-version>` are as described in the [get-assembled-questionnaire](#get-assembled-questionnaire) request, and `<assembled-questionnaire>` is the assembled questionnaire being saved.

#### Response

```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) that was saved.

## validated-extracted-bundle
Triggered upon completion of an extract operation initiated by the "Extract" button in the debug panel.

#### Request
```http
POST / HTTP/1.1
Content-Type: application/json

<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) containing resources obtained from [extract](#extract).

#### Response

```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) of saved resources.

## create-questionnaire
Triggered when the "Save" button is clicked in the builder for a new questionnaire.

#### Request
```http
POST /sdc/Questionnaire HTTP/1.1
Content-Type: application/json

<questionnaire>
```

Where `<questionnaire>` is the new questionnaire being created.

#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) that was created.

## save-questionnaire
Triggered when the "Save" button is clicked in the builder.

#### Request
```http
PUT /sdc/Questionnaire/<questionnaire-id> HTTP/1.1
Content-Type: application/json

<questionnaire>
```

Where `<questionnaire>` is the questionnaire being saved.

#### Response
```json
<questionnaire>
```

Where `<questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) that was saved.

## create-sub-questionnaire
Triggered when an outline item is saved as a sub-questionnaire.

#### Request
```http
POST /sdc/Questionnaire?url=<sub-questionnaire-url>&version=1 HTTP/1.1
Content-Type: application/json

<sub-questionnaire>
```

Where `<sub-questionnaire-url>` is the canonical URL of the sub-questionnaire and `<sub-questionnaire>` is the sub-questionnaire being saved.

#### Response
```json
<sub-questionnaire>
```

Where `<sub-questionnaire>` is the [questionnaire](https://www.hl7.org/fhir/questionnaire.html) that was created.

## create-theme
Triggered when the "Save" button is clicked in the theme editor for a new theme.

#### Request
```http
POST /QuestionnaireTheme HTTP/1.1
Content-Type: application/json

<theme>
```

Where `<theme>` is the new theme being created.

#### Response
```json
<theme>
```

Where `<theme>` is the [theme](./configuration.md#theme) being created.

## save-theme
Triggered when the "Save" button is clicked in the theme editor.

#### Request
```http
PUT /QuestionnaireTheme/<theme-id> HTTP/1.1
Content-Type: application/json

<theme>
```

Where `<theme>` is the theme being saved.

#### Response
```json
<theme>
```

Where `<theme>` is the [theme](./configuration.md#theme) being saved.

## search-choice-options
Triggered when dropdown options are requested for a choice item.

#### Request
```http
GET <url> HTTP/1.1
```

Where `<url>` is the URL of the value set or resources associated with the choice item.

#### Response
Response be a plain list or [bundle](https://www.hl7.org/fhir/bundle.html) of resources (e.g. [ValueSet](https://build.fhir.org/valueset.html)) depending on the source of the options.

## search-questionnaires-by-url
Triggered before saving the current questionnaire to check for conflicts with existing questionnaires based on their URL.

#### Request
```http
GET /sdc/Questionnaire?url=<questionnaire-url> HTTP/1.1
```

Where `<questionnaire-url>` is the canonical URL of the questionnaire being checked.

#### Response
```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) of questionnaires with the same URL.

## search-sub-questionnaires
Triggered when the user searches for sub-questionnaires in the builder.

#### Request
```http
GET /sdc/Questionnaire?.extension$contains=%-child&.title$contains=<search-term> HTTP/1.1
```

Where `<search-term>` is the term being searched for.

#### Response
```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) of sub-questionnaires.

## get-sub-questionnaire
Triggered to retrieve a sub-questionnaire when it is referenced in a parent questionnaire.

#### Request
```http
GET /sdc/Questionnaire?url=<sub-questionnaire-url>&version=<sub-questionnaire-version> HTTP/1.1
```

Where `<sub-questionnaire-url>` is the canonical URL of the sub-questionnaire being requested. Parameter `version` is omitted if no version is specified in the reference.

#### Response
```json
<bundle>
```

Where `<bundle>` is the [bundle](https://www.hl7.org/fhir/bundle.html) containing zero or one [questionnaire](https://www.hl7.org/fhir/questionnaire.html) resource.

## upload-attachment
Triggered when a file is selected in the attachment input field.

#### Request
```http
POST /$sdc-file HTTP/1.1 
Content-Type: multipart/form-data; boundary=------------------------boundary12345

--------------------------boundary12345
Content-Disposition: form-data; name="file"; filename="example.txt"
Content-Type: text/plain

<file-content>
--------------------------boundary12345
Content-Disposition: form-data; name="filename"

<file-name>
--------------------------boundary12345--
```

Where `<file-content>` is the content of the file being uploaded and `<file-name>` is the path and name of the file.

#### Response
```json
{
  "url": "<file-url>"
}
```

Where `<file-url>` is the URL of the uploaded file which will further be used to either download or [delete](#delete-attachment) the file.

## validate-questionnaire
Triggered when the "Validate Questionnaire" button is clicked in the debug panel.

#### Request
```http
POST /Questionnaire/$validate HTTP/1.1
Content-Type: application/json

<questionnaire>
```

Where `<questionnaire>` is the questionnaire being validated.

#### Response
```json
<operation-outcome>
```

Where `<operation-outcome>` is the [operation outcome](https://www.hl7.org/fhir/operationoutcome.html) of the validation.

## validate-response
Triggered when the "Validate Questionnaire Response" button is clicked in the debug panel.

#### Request
```http
POST /QuestionnaireResponse/$validate HTTP/1.1
Content-Type: application/json

<questionnaire-response>
```

Where `<questionnaire-response>` is the response being validated.

#### Response
```json
<operation-outcome>
```

Where `<operation-outcome>` is the [operation outcome](https://www.hl7.org/fhir/operationoutcome.html) of the validation.
