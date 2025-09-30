
---
description: Convert openEHR templates to FHIR Questionnaires and QuestionnaireResponse to openEHR compositions using Aidbox SDC operations
---

# Generating Questionnaire from openEHR Template API

Aidbox provides specialized operations to bridge [openEHR](https://www.openehr.org/) clinical modeling with [FHIR Questionnaire](https://www.hl7.org/fhir/questionnaire.html) resources. This integration allows you to leverage existing openEHR templates and convert clinical data between the two standards seamlessly.

The integration supports two main workflows:
- Converting openEHR OPT (Operational Template) files into FHIR Questionnaires
- Reconstructing openEHR compositions from FHIR QuestionnaireResponse data

## Converting openEHR Template to Questionnaire

The `$sdc-openehr-convert` operation accepts openEHR OPT templates as input and generates corresponding FHIR Questionnaires.

### Request Format

```http
POST /$sdc-openehr-convert
Content-Type: application/fhir+json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "content",
      "valueString": "<<openehr-template>>"
    },
    {
      "name": "save",
      "valueBoolean": true
    }
  ]
}
```

### Parameters

- `content` (required): The openEHR OPT template content as a string
- `save` (optional): Boolean flag indicating whether to save the generated Questionnaire to the database. Defaults to false.

### Response

The operation returns a FHIR Questionnaire resource that corresponds to the structure defined in the openEHR template. If `save` is set to true, the Questionnaire is also persisted in the Aidbox database.

## Converting QuestionnaireResponse to openEHR Composition

The `$sdc-openehr-reconstruct` operation converts FHIR QuestionnaireResponse data back into openEHR composition format.

### Request Format

```http
POST /$sdc-openehr-reconstruct
Content-Type: application/fhir+json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "questionnaire-response",
      "valueReference": {
        "reference": "QuestionnaireResponse/<<qr-id>>"
      }
    },
    {
      "name": "questionnaire",
      "resource": {
        "resourceType": "Questionnaire",
        // ... Questionnaire resource content
      }
    },
    {
      "name": "source-template",
      "valueString": "<<openEHR-OPT-Template>>"
    }
    {
      "name": "format"
      "valueString": "flat | canonical" // default is "flat"
    }
  ]
}
```

### Parameters

- `questionnaire-response` (required): Reference to the QuestionnaireResponse resource containing the form data
- `questionnaire` (optional): The original Questionnaire resource. If not provided, Aidbox will attempt to retrieve it from the database using the reference in the QuestionnaireResponse
- `source-template` (optional): The original openEHR OPT template. If not provided, Aidbox will use the template stored during the initial conversion
- `format` (optional): Specifies the output format of the openEHR composition. Can be either `flat` or `canonical`. Defaults to `flat`.

### Response

The operation returns an openEHR composition that represents the clinical data from the QuestionnaireResponse in openEHR format, structured according to the original template.

## See also

- [FHIR Questionnaire Resource](https://www.hl7.org/fhir/questionnaire.html) - FHIR specification for Questionnaire
- [FHIR QuestionnaireResponse Resource](https://www.hl7.org/fhir/questionnaireresponse.html) - FHIR specification for QuestionnaireResponse
- [openEHR Foundation](https://www.openehr.org/) - Official openEHR documentation and specifications
