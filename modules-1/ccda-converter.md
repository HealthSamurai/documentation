---
description: >-
  This page describes CCDA built-in module which provides API to convert CCDA
  documents to FHIR documents and backward.
---

# CCDA

## Introduction

The HL7 **Clinical Document Architecture** (**CDA**) is an XML-based markup standard intended to specify the encoding, structure and semantics of clinical documents for exchange. To help developers to comply with § 170.315(g)(9) ONC criterion Aidbox provides endpoint to convert CCDA documents to FHIR format.

{% hint style="info" %}
You can quickly evaluate CCDA to FHIR Converter on our [CCDA to FHIR Demo page](https://ccda.aidbox.app/).
{% endhint %}

### Converting a CCDA document to FHIR

To perform CCDA to FHIR conversion, use the `/ccda/to-fhir` endpoint:

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

If conversion was successful, you'll get a [FHIR Document](https://www.hl7.org/fhir/documents.html) as a result:

```json
{
  "resourceType" : "Bundle",
  "type" : "document",
  "entry" : [{
    "resource" : {
      "address" : [ {
        "line" : [ "1007 Amber Dr" ],
        "city" : "Beaverton",
        "state" : "OR",
        "postalCode" : "97266",
        "country" : "US"
      } ],
      "name" : [ {
        "given" : [ "Rabecca", "Jones" ],
        "family" : "Angeles"
      }, {
        "given" : [ "Becky", "Jones" ],
        "family" : "Angeles"
      } ],
      "birthDate" : "1970-07-01",
      "resourceType" : "Patient",
      "extension" : [ {
        "url" : "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race",
        "valueCoding" : {
          "system" : "urn:oid:2.16.840.1.113883.6.238",
          "code" : "2106-3",
          "display" : "White"
        }
      } ],
      "communication" : [ {
        "language" : {
          "coding" : [ {
            "code" : "en-US",
            "system" : "urn:ietf:bcp:47"
          } ]
        },
        "preferred" : true
      } ],
      "id" : "patient",
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(555)-225-1234",
        "use" : "MC"
      }, {
        "system" : "phone",
        "value" : "+1(555)-226-1544",
        "use" : "HP"
      } ],
      "gender" : "female",
      "maritalStatus" : {
        "coding" : [ {
          "code" : "M",
          "display" : "Married"
        } ]
      }
    },
    "fullUrl" : "urn:uuid:patient-patient"
  }, {
    "resource" : {
      "resourceType" : "Practitioner",
      "id" : "2.16.840.1.113883.4.6",
      "name" : [ {
        "given" : [ "Henry" ],
        "family" : "Seven"
      } ],
      "address" : [ {
        "line" : [ "1002 Healthcare Dr" ],
        "city" : "Beaverton",
        "state" : "OR",
        "postalCode" : "97266",
        "country" : "US"
      } ],
      "telecom" : [ {
        "system" : "phone",
        "value" : "+1(555)-555-1002",
        "use" : "WP"
      } ]
    },
    "fullUrl" : "urn:uuid:practitioner-2.16.840.1.113883.4.6"
  }]
}
// POST /ccda/to-fhir
// HTTP/1.1 200 OK
```

In case of error, OperationOutcome resource will be returned:

```json
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
// POST /ccda/to-fhir
// HTTP/1.1 422 Unprocessable Entity
```

{% hint style="warning" %}
Please note that this endpoint doesn't persist any populated FHIR data to Aidbox database. This endpoint is read-only and it performs a stateless conversion of the document from one format to another. To persist FHIR data extracted from a CDDA document you need to setup a simple pipeline as described in this tutorial (TODO).
{% endhint %}
