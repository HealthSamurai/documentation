---
description: >-
  This page describes the first version of C-CDA to FHIR converter. This version
  of converter is deprecated in favour of the new bidirectional converter.
---

# 🔒 v1 Documentation

## Introduction

The HL7 **Clinical Document Architecture** (**CDA**) is an XML-based markup standard intended to specify the encoding, structure and semantics of clinical documents for exchange. It’s the most widely used format for health information exchange in the US today. To help developers to comply with § 170.315(g)(9) ONC criterion Aidbox provides endpoint to convert CCDA documents to FHIR format.

Aidbox C-CDA converter aims to handle all data elements included into [USCDI v1](https://www.healthit.gov/isa/united-states-core-data-interoperability-uscdi#uscdi-v1), more recent USCDI versions will be supported later.

{% hint style="info" %}
You can quickly evaluate C-CDA to FHIR Converter on our [C-CDA to FHIR Demo page](https://ccda.aidbox.app/).
{% endhint %}

### Converting a C-CDA document to FHIR

To perform C-CDA to FHIR conversion without storing FHIR resources in the Aidbox database, use the `/ccda/to-fhir` endpoint. Simply `POST` C-CDA XML document in request body as in the following example:

```http
POST /ccda/to-fhir
Authorization: .....
Content-Type: application/cda+xml

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xmlns="urn:hl7-org:v3" ...>
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
        }, {
          "uri" : "urn:uuid:cb89a5f1-994b-1660-584d-4e6c4b4cdc33"
        }, {
          "uri" : "urn:uuid:453f59c9-b057-44a3-e3be-56e660ea3abd"
        }, {
          "uri" : "urn:uuid:ea565d25-d34c-7dbe-819b-f92c9a26792f"
        }, {
          "uri" : "urn:uuid:5bda5eea-744c-a6e9-7c65-e9b676577a13"
        }, {
          "uri" : "urn:uuid:7154267b-efe9-c424-504b-a885ea734fdd"
        }, {
          "uri" : "urn:uuid:2608dce8-3663-d3e7-ad9e-fe8fef7748ad"
        }, {
          "uri" : "urn:uuid:e6941356-69b2-c13c-408a-51918e5b9674"
        }, {
          "uri" : "urn:uuid:fc569f5d-e27a-51ed-6289-ea6013b8225c"
        }, {
          "uri" : "urn:uuid:21781c99-5265-7693-5c3c-7b6081d87b0d"
        } ]
        
        ....
```

In case of error, OperationOutcome resource will be returned:

```json
// POST /ccda/to-fhir
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
Please note that this endpoint doesn't persist any populated FHIR data to Aidbox database. This endpoint is read-only and it performs a stateless conversion of the document from one format to another. To persist FHIR data extracted from a C-DDA document proceed to the [Persisting a result of C-CDA to FHIR conversion](ccda-converter.md#persisting-a-result-of-c-cda-to-fhir-conversion) section.
{% endhint %}

#### Endpoint options

There are several options you may pass to the `/ccda/to-fhir` endpoint. Options are passed as query string parameters, i.e. `/ccda/to-fhir?option1=value1&option2=value2`.

| Option     | Values                                                                                                                                    | Description                                                                                              |
| ---------- | ----------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- |
| `format`   | <p><code>aidbox | fhir</code><br>Default: <code>fhir</code></p>                                                                           | [Format](../fhir-resources/aidbox-and-fhir-formats.md) of resulting FHIR document. It's FHIR by default. |
| `sections` | <p>Proceed to the <a href="ccda-converter.md#section-aliases">Section Aliases</a> table to find all possible values. <br>Default: all</p> | Comma-separated list of section aliases to process. By default all sections are processed.               |

### Persisting result of C-CDA to FHIR conversion

If you wish to store C-CDA to FHIR conversion results in the Aidbox database, use `/ccda/persist` endpoint. After converting C-CDA document to FHIR document, this endpoint produces [FHIR Transaction bundle](https://www.hl7.org/fhir/bundle.html#transaction) to create or update existing resources. According to its transactional nature, any single failure in this transaction bundle will rollback a whole transaction and nothing will be saved. The following example demonstrates how to use `/ccda/persist` endpoint.

```http
POST /ccda/persist
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

Additionally to the FHIR resources converted from C-CDA data, this endpoint will create [Provenance](https://build.fhir.org/provenance.html) resource which will contain references to all resources created or updated during single `/ccda/persist` call. It's useful to have such list to get all FHIR resources related to the specific C-CDA document. For instance, to wipe them out from the database.

Also, if `create-docref` option is provided, this endpoint will create a [DocumentReference](https://build.fhir.org/documentreference.html) resource which will contain a base64-encoded copy of the original C-CDA XML document in it's `content` attribute. It's useful when you want to store original document in the Aidbox database for various rare use-cases. By default, `create-docref` option is set to false, so no DocumentReference resource is created.

#### Endpoint Options

Options are passed as query-string parameters, i.e. `/ccda/persist?create-docref=true&option2=value2`. `sections` are passed separated by commas, i.e. `/ccda/persist?section=dicom,goals,findings`.

| Option          | Values                                                                                                                                    | Description                                                                                |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| `create-docref` | <p><code>true | false</code><br>Default: <code>false</code></p>                                                                           | Specifies if a DocumentReference resource is needed to store original XML document.        |
| `tenant-id`     | <p>ID of Tenant resource<br>Default: none</p>                                                                                             | For [Smartbox](../smartbox/) users only. Assigns Tenant to all populated resources.        |
| `sections`      | <p>Proceed to the <a href="ccda-converter.md#section-aliases">Section Aliases</a> table to find all possible values. <br>Default: all</p> | Comma-separated list of section aliases to process. By default all sections are processed. |

#### Section Aliases

Below is the list of section aliases and their corresponding OIDs. Section aliases are used to configure C-CDA endpoints to specify a list of sections to process.

| Value               | Section OID(s)                                                                                                     |
| ------------------- | ------------------------------------------------------------------------------------------------------------------ |
| `dicom`             | 2.16.840.1.113883.10.20.6.1.1                                                                                      |
| `findings`          | 2.16.840.1.113883.10.20.6.1.2                                                                                      |
| `assesment`         | 2.16.840.1.113883.10.20.22.2.8                                                                                     |
| `goals`             | 2.16.840.1.113883.10.20.22.2.60                                                                                    |
| `problems`          | 2.16.840.1.113883.10.20.22.2.5.1                                                                                   |
| `health-concerns`   | 2.16.840.1.113883.10.20.22.2.58                                                                                    |
| `medications`       | 2.16.840.1.113883.10.20.22.2.1.1                                                                                   |
| `medical-equipment` | 2.16.840.1.113883.10.20.22.2.23                                                                                    |
| `immunizations`     | <p>2.16.840.1.113883.10.20.22.2.2</p><p>2.16.840.1.113883.10.20.22.2.2.1</p><p>2.16.840.1.113883.10.20.22.4.52</p> |
| `payers`            | 2.16.840.1.113883.10.20.22.2.18                                                                                    |
| `vital-signs`       | 2.16.840.1.113883.10.20.22.2.4.1                                                                                   |
| `social-history`    | 2.16.840.1.113883.10.20.22.2.17                                                                                    |
| `procedures`        | 2.16.840.1.113883.10.20.22.2.7.1                                                                                   |
| `encounters`        | 2.16.840.1.113883.10.20.22.2.22.1                                                                                  |
| `notes`             | <p>2.16.840.1.113883.10.20.22.2.65</p><p>1.3.6.1.4.1.19376.1.5.3.1.3.5</p>                                         |
| `plan-of-treatment` | 2.16.840.1.113883.10.20.22.2.10                                                                                    |
| `careteam`          | 2.16.840.1.113883.10.20.22.2.500                                                                                   |
| `allergies`         | 2.16.840.1.113883.10.20.22.2.6.1                                                                                   |
| `results`           | 2.16.840.1.113883.10.20.22.2.3.1                                                                                   |

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

| Option   | Values                                                                    | Description                    |
| -------- | ------------------------------------------------------------------------- | ------------------------------ |
| `method` | <p><code>xsd | schematron | both</code><br>Default: <code>both</code></p> | Type of validation to perform. |
