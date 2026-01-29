---
description: >-
  This page describes C-CDA built-in module which provides API to convert C-CDA
  documents to FHIR documents and backward.
coverY: 0
---

# C-CDA / FHIR Converter

## Introduction

C-CDA (Consolidated Clinical Document Architecture) is a standard that has been widely adopted in the healthcare industry for exchanging clinical documents. Based on the HL7 Clinical Document Architecture (CDA) standard, it provides a structured approach to capturing and sharing patient information in a consistent manner. However, with the healthcare industry moving towards the more modern FHIR standard, there is now a growing need to convert data between C-CDA and FHIR. Furthermore, the § 170.315(g)(9) ONC criterion mandates that EHR systems must import and export patient data in C-CDA format.

To meet these data exchange requirements, Aidbox offers a module that performs C-CDA to FHIR and FHIR to C-CDA conversions. This module applies a set of rules consistently in both directions, ensuring a robust and reliable conversion process. In addition to data conversion, the module also provides endpoints for validating C-CDA documents and for persisting FHIR data extracted from C-CDA documents.

{% hint style="info" %}
C-CDA / FHIR Converter provides bidirectional mapping for all data elements from the [USCDI v1](https://www.healthit.gov/isa/sites/isa/files/2020-10/USCDI-Version-1-July-2020-Errata-Final_0.pdf) list.
{% endhint %}

{% hint style="info" %}
You can quickly evaluate C-CDA to FHIR Converter on our [C-CDA to FHIR Demo page](https://ccda.aidbox.app/v2/ccda-fhir).
{% endhint %}

### List of supported sections

The list of supported sections can be found [on the separate page](sections/). Section aliases are used to configure C-CDA endpoints to specify sections to process.

### Converting a C-CDA document to FHIR

To perform C-CDA to FHIR conversion without storing FHIR resources in the Aidbox database, use the `/ccda/v2/to-fhir` endpoint. Simply `POST` C-CDA XML document in request body as in the following example:

```http
POST /ccda/v2/to-fhir
Authorization: .....
Content-Type: application/cda+xml

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns="urn:hl7-org:v3" ... >
  ....
</ClinicalDocument>
```

{% hint style="warning" %}
Please note the`Content-Type: application/cda+xml` header. Any other MIME type will be rejected by this endpoint.
{% endhint %}

If conversion was successful, you'll get a [FHIR Document](https://www.hl7.org/fhir/documents.html) as a result. Accordingly to the FHIR Documents specification, the first resource in the resulting bundle is [Composition resource](https://www.hl7.org/fhir/composition.html) containing document header information as well as the list of document sections.

```json
// POST /ccda/to-fhir
// HTTP/1.1 200 OK

{
  "resourceType" : "Bundle",
  "type" : "document",
  "entry" : [ {
    "resource" : {
      "resourceType": "Composition",
      "date" : "2015-06-22",
      "section" : [ {
        "code" : {
          "coding" : [ {
            "code" : "48765-2",
            "display" : "48765-2",
            "system" : "http://loinc.org"
          } ]
        },
        "title" : "ALLERGIES AND ADVERSE REACTIONS",
        "author" : [ {
          "uri" : "urn:uuid:f7a93b9c-f490-dee8-a1ce-e3bf5f0360cc"
        } ],
        "entry" : [ {
          "uri" : "urn:uuid:468e2476-2095-240f-84b6-8f05b0d97637"
        }, {
          "uri" : "urn:uuid:5082dbe5-1c5a-326a-a72b-a0d73216a679"
        } ],
        "text" : {
          "div" : "<table width='100%' border='1'>\n<thead>\n<tr>\n<th>\nSubstance\n</th>\n<th>\nReaction\n</th>\n<th>\nSeverity\n</th>\n<th>\nStatus\n</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td>\n<content ID='product1'>\nPenicillin G\n</content>\n</td>\n<td>\n<content ID='reaction1'>\nHives\n</content>\n</td>\n<td>\n<content ID='severity1'>\nModerate\n</content>\n</td>\n<td>\nActive\n</td>\n</tr>\n<tr>\n<td>\n<content ID='product2'>\nAmpicillin\n</content>\n</td>\n<td>\n<content ID='reaction2'>\nHives\n</content>\n</td>\n<td>\n<content ID='severity2'>\nModerate\n</content>\n</td>\n<td>\nActive\n</td>\n</tr>\n</tbody>\n</table>",
          "status" : "generated"
        }
      }, {
        "code" : {
          "coding" : [ {
            "code" : "10160-0",
            "display" : "HISTORY OF MEDICATION USE",
            "system" : "http://loinc.org"
          } ]
        },
        "title" : "MEDICATIONS",
        "entry" : [ {
          "uri" : "urn:uuid:4ddb05da-b1e6-685a-5c08-f28210d31d5d"
        }, {
          "uri" : "urn:uuid:6f87fc48-b173-97c0-a5fd-1b7e483d5c67"
        }, {
          "uri" : "urn:uuid:b8b29b52-ecb1-7e69-bee9-52af94d0c174"
        } ]
        
        ....
```

In case of error, OperationOutcome resource will be returned:

```json
// POST /ccda/v2/to-fhir
// HTTP/1.1 422 Unprocessable Entity

{
  "resourceType": "OperationOutcome",
  "id": "invalid",
  "text": {
    "status": "generated",
    "div": "...."
  },
  "issue": [
    {
      "severity": "fatal",
      "code": "invalid",
      "diagnostics": "..."
    }
  ]
}
```

{% hint style="warning" %}
Please note that this endpoint doesn't persist any populated FHIR data to Aidbox database. This endpoint is read-only and it performs a stateless conversion of the document from one format to another. To persist FHIR data extracted from a C-DDA document proceed to the [Persisting a result of C-CDA to FHIR conversion](./#persisting-a-result-of-c-cda-to-fhir-conversion) section.
{% endhint %}

#### Endpoint options

There are several options you may pass to the `/ccda/v2/to-fhir` endpoint. Options are passed as query string parameters, i.e. `/ccda/v2/to-fhir?option1=value1&option2=value2`.

| Option             | Values                                                                                                                               | Description                                                                                |
| ------------------ | ------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------ |
| `format`           | `aidbox` `fhir`                                                                                                                      | Default: `aidbox`                                                                          |
| `reference-format` | `resource-id`                                                                                                                        | Sets output reference format to ResourceType/id                                            |
| `transaction`      | `true`                                                                                                                               | Output FHIR bundle type. By default FHIR document bundle will be returned                  |
| `sections`         | <p>Proceed to the <a href="./#list-of-supported-sections">Section Aliases</a> table to find all possible values.<br>Default: all</p> | Comma-separated list of section aliases to process. By default all sections are processed. |
| `patient-id`       | `id` that will be inserted and propagated as `uri` , `id` or `fullURl` in resulting FHIR Bundle.                                     | Arbitrary string that corresponds to id.                                                   |
| `post-process`     | Different useful utils that are applied after conversion                                                                             | `single-entry-organizer` - will remove organizers with single Observation entry            |

Example

```http
POST /ccda/v2/to-fhir?transaction=true
Authorization: .....
Content-Type: application/cda+xml

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns="urn:hl7-org:v3" ...>
  ....
</ClinicalDocument>
```

### Converting a FHIR Document to C-CDA

The `/ccda/v2/to-ccda` endpoint is used to convert FHIR document to C-CDA document. It accepts a [FHIR Document](https://build.fhir.org/documents.html), which is a specialized FHIR Bundle with [Composition](https://www.hl7.org/fhir/composition.html) resource as its first element. It is the user's responsibility to compose this bundle. The resulting C-CDA document will contain all sections from the provided Composition.

{% hint style="warning" %}
Because of technical limitation you have to make sure that provided FHIR Document doesn't contain circular references between resources.

Circular references occur when two or more resources in the bundle reference each other in a loop, such as resource A references resource B, which in turn references resource C, which then references resource A again.
{% endhint %}

#### Validation of FHIR Bundle for Conversion Purposes

Incoming FHIR Bundle can be validated for the conversion purposes via using `/ccda/fhir-validate` endpoint. Validator will check if it is possible to create valid C-CDA document. If something is wrong with dataset - all potential errors will be aggregated and explained:

```http
POST /ccda/v2/to-ccda
Content-Type: application/json
Authorization: ...

{
    "resourceType": "Bundle",
    "type": "document",
    "entry": [...]
}

```

```json
{
  "status": 200,
  "body": {
    "errors": [
      {
        "47420-5": {
          "error": "Missing data for 'entries required' section: FunctionalStatusSectionV2",
          "section-loinc": "47420-5"
        }
      }
    ]
  }
}
```

{% hint style="info" %}
There's a mechanism to streamline FHIR Document generation to use with this endpoint, please check [Producing C-CDA documents](producing-c-cda-documents.md) page.
{% endhint %}

To convert a FHIR document to C-CDA, make a POST request to the `/ccda/v2/to-ccda` endpoint with the FHIR document in the request body. The endpoint will return the C-CDA document in XML format.

```http
POST /ccda/v2/to-ccda
Content-Type: application/json
Authorization: ...

{
    "resourceType": "Bundle",
    "type": "document",
    "entry": [...]
}
```

```xml
// POST /ccda/v2/to-ccda
// HTTP/1.1 200 OK

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns="urn:hl7-org:v3" ...>
  ....
</ClinicalDocument>
```

The source FHIR Document for this endpoint can be in either [FHIR or Aidbox format](../../../api/rest-api/other/aidbox-and-fhir-formats.md). Endpoint uses `Content-Type` header to understand which format is used. `Content-Type: application/json` means Aidbox format and `Content-Type: application/fhir+json` means FHIR format. So in the example above Aidbox format is used.

If FHIR Document format and `Content-Type` header are mismached then 422 error will be returned:

```http
POST /ccda/v2/to-ccda
Content-Type: application/json
Authorization: ...

{
    "resourceType": "Bundle",
    "type": "document",
    // Reference is in FHIR format
    "subject": {"reference": "urn:uuid:xxxxxxxxxxxxxxx"} 
}
```

```json
// POST /ccda/v2/to-ccda
// HTTP/1.1 422 Invalid Entity

{
  "resourceType": "OperationOutcome",
  "id": "invalid",
  "text": {
    "status": "generated",
    "div": "Provided FHIR Document is in FHIR format, but expected to be in AIDBOX format. Make sure that you're using the right 'content-type' header."
  },
  "issue": [
    {
      "severity": "fatal",
      "code": "invalid",
      "diagnostics": "Provided FHIR Document is in FHIR format, but expected to be in AIDBOX format. Make sure that you're using the right 'content-type' header."
    }
  ]
}
```

### Persisting result of C-CDA to FHIR conversion

To store C-CDA to FHIR conversion results in the Aidbox database, you can use the `/ccda/v2/persist` endpoint. This endpoint converts the C-CDA document to a FHIR document and creates or updates resources using a FHIR Transaction bundle. As this process is transactional, any failure in the bundle will cause the entire transaction to be rolled back, and nothing will be saved. Here is an example of how to use the `/ccda/v2/persist` endpoint:

```http
POST /ccda/v2/persist
Authorization: .....
Content-Type: application/cda+xml

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns="urn:hl7-org:v3" ...>
  ....
</ClinicalDocument>
```

{% hint style="info" %}
Persisting is heavily based on C-CDA's `<id root="...">` element. If some entity in C-CDA document contains this element (identifier), this identifier will be used (in hashed form) as an ID for resulting FHIR resource. This means that resubmitting the same document several times will not produce duplicate resources in the database for C-CDA entities which contains `<id>` elements.
{% endhint %}

In addition to the FHIR resources that are converted from C-CDA data, the `/ccda/v2/persist` endpoint also creates a [Provenance](https://build.fhir.org/provenance.html) resource that contains references to all of the resources that are created or updated during a single call. This list is useful for identifying all FHIR resources that are related to a specific C-CDA document, such as for deleting them from the database.

If the `create-docref` option is provided, this endpoint will also create a [DocumentReference](https://build.fhir.org/documentreference.html) resource that contains a base64-encoded copy of the original C-CDA XML document in its `content` attribute. This feature is useful if you want to store the original document in the Aidbox database for rare use cases. By default, the `create-docref` option is set to false, so no DocumentReference resource is created.

#### Endpoint Options

Options are passed as query-string parameters, i.e. `/ccda/v2/persist?create-docref=true&option2=value2`. `sections` are passed separated by commas, i.e. `/ccda/v2/persist?section=dicom,goals,findings`.

| Option          | Values                                                                                                                               | Description                                                                                                     |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------- |
| `create-docref` | `true`                                                                                                                               | <p>false<br>Default: <code>false</code></p>                                                                     |
| `tenant-id`     | <p>ID of Tenant resource<br>Default: none</p>                                                                                        | For [Smartbox](../../../deprecated/deprecated/smartbox/) users only. Assigns Tenant to all populated resources. |
| `sections`      | <p>Proceed to the <a href="./#list-of-supported-sections">Section Aliases</a> table to find all possible values.<br>Default: all</p> | Comma-separated list of section aliases to process. By default all sections are processed.                      |
| `patient-id`    | `id` that will be inserted and propagated as `uri` , `id` or `fullURl` in persisted FHIR Bundle.                                     | Arbitrary string that corresponds to id.                                                                        |
| `post-process`  | Different useful utils that are applied after conversion                                                                             | `single-entry-organizer` - will remove organizers with single Observation entry                                 |

### Validating a C-CDA document

Aidbox provides an endpoint to check correctness of C-CDA document. Despite the fact that C-CDA to FHIR conversion can be performed even on invalid C-CDA document (with undefined result), there are some use cases when you want to validate C-CDA document first. For instance, you can use this endpoint to check user-submitted document and reject if it's not valid.

There are two stages of C-CDA validation:

* Structural validation (XSD) which checks that XML tree is properly formed
* Schematron validation that checks the presence of data elements required by C-CDA specification

By default both stages are performed.

{% hint style="info" %}
On [C-CDA Converter Demo page](https://ccda.aidbox.app) only XSD validation is performed. Uploaded XML file will be highlighted with green color if the document is valid, red otherwise.
{% endhint %}

The following example demonstrates how to use validation endpoint:

```http
POST /ccda/validate
Authorization: .....
Content-Type: application/cda+xml

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns="urn:hl7-org:v3" ...>
  ....
</ClinicalDocument>
```

If validation was successful, you will get the following response:

```json
// POST /ccda/validate
// HTTP/1.1 200 OK

{
  "result": true
}
```

In case of failed validation endpoint will return a list of errors and warnings:

```json
// POST /ccda/validate
// HTTP/1.1 200 OK

{
  "result": false,
  "errors": [
    {
      "pattern": {
        "id": "p-urn-hl7ii-2.16.840.1.113883.10.20.22.4.78-2014-06-09-errors"
      },
      "rule": {
        "context": "//cda:observation[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.78' and @extension='2014-06-09']]",
        "id": "r-urn-hl7ii-2.16.840.1.113883.10.20.22.4.78-2014-06-09-errors"
      },
      "failed-assert": {
        "location": "/ClinicalDocument/component/structuredBody/component[5]/section/entry[0]/observation",
        "schematron": "count(cda:statusCode)=1",
        "explanation": [
          "SHALL contain exactly one [1..1] statusCode (CONF:1098-14809)."
        ],
        "id": "a-1098-14809"
      }
    }
  ],
  "warnings": [
    {
      "pattern": {
        "id": "p-urn-oid-2.16.840.1.113883.10.20.22.4.64-warnings"
      },
      "rule": {
        "context": "//cda:act[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.64']]",
        "id": "r-urn-oid-2.16.840.1.113883.10.20.22.4.64-warnings"
      },
      "failed-assert": {
        "location": "/ClinicalDocument/component/structuredBody/component[1]/section/entry/substanceAdministration/entryRelationship[1]/act",
        "schematron": "count(cda:author[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.119']])=1",
        "explanation": [
          "SHOULD contain zero or one [0..1] Author Participation (identifier: urn:oid:2.16.840.1.113883.10.20.22.4.119) (CONF:81-9433)."
        ],
        "id": "a-81-9433"
      }
    }
  ],
  "no-of-fails": {
    "errors": 1,
    "warnings": 1
  }
}
```

#### Endpoint options

Options are passed as query-string parameters, i.e. `/ccda/validate?option1=value1&option2=value2`.

| Option   | Values | Description |
| -------- | ------ | ----------- |
| `method` | `xsd`  | schematron  |

## Deployment

* [How to deploy the service](how-to-deploy-the-service.md)
