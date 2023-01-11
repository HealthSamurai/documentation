---
description: >-
  This page describes CCDA built-in module which provides API to convert CCDA
  documents to FHIR documents and backward.
---

# CCDA

## Introduction

The HL7 **Clinical Document Architecture** (**CDA**) is an XML-based markup standard intended to specify the encoding, structure and semantics of clinical documents for exchange. It’s the most widely used format for health information exchange in the US today. To help developers to comply with § 170.315(g)(9) ONC criterion Aidbox provides endpoint to convert CCDA documents to FHIR format.

Aidbox CCDA converter aims to handle all data elements included into [USCDI v1](https://www.healthit.gov/isa/united-states-core-data-interoperability-uscdi#uscdi-v1), more recent USCDI versions will be supported later.

{% hint style="info" %}
You can quickly evaluate CCDA to FHIR Converter on our [CCDA to FHIR Demo page](https://ccda.aidbox.app/).
{% endhint %}

{% hint style="warning" %}
CCDA to FHIR mapping is still in development stage. You can check the current state of the mapping on [this spreadsheet](https://docs.google.com/spreadsheets/d/1rrjciXnSkAv7Le-\_wTQzjWNHzJhTKtV-UqzNBpza02c/edit?usp=sharing).
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

{% hint style="warning" %}
Please note the`Content-Type: application/cda+xml` header. Any other MIME type will be rejected by this endpoint.
{% endhint %}

If conversion was successful, you'll get a [FHIR Document](https://www.hl7.org/fhir/documents.html) as a result:

<pre class="language-json"><code class="lang-json">// POST /ccda/to-fhir
// HTTP/1.1 200 OK
<strong>{
</strong>  "resourceType" : "Bundle",
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
}</code></pre>

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
Please note that this endpoint doesn't persist any populated FHIR data to Aidbox database. This endpoint is read-only and it performs a stateless conversion of the document from one format to another. To persist FHIR data extracted from a CDDA document you need to setup a simple pipeline as described in this tutorial (TODO).
{% endhint %}

#### Options for `/ccda/to-fhir` endpoint: 

You may specify format of resulting FHIR document by passing `format` query param with `aidbox` or `fhir` value:

* Return resulting FHIR document in [Aidbox Format](https://docs.aidbox.app/modules-1/fhir-resources/aidbox-and-fhir-formats):

```http 
POST /ccda/to-fhir?format=aidbox

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  ...
</ClinicalDocument>
```

* Return resulting FHIR document in [FHIR Format](https://docs.aidbox.app/modules-1/fhir-resources/aidbox-and-fhir-formats):

```http
POST /ccda/to-fhir?format=fhir 

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  ...
</ClinicalDocument>
```

{% hint style="warning" %}
By default - resulting document will be in FHIR format.
{% endhint %}


### Validating a CCDA document

Aidbox CCDA module supports validation of CCDA documents. Basically, there are two types of validation:

* against XSD schema
* using Schematron

By default both types of validation are used.

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

You may specify type of validation  by passing `method` query param with `xsd` or `schematron` value:

```http
POST /ccda/validate?method=xsd
Authorization: .....
Content-Type: application/cda+xml
```

```http
POST /ccda/validate?method=schematron
Authorization: .....
Content-Type: application/cda+xml
```

{% hint style="info" %}
On [CCDA Converter Demo page](https://ccda.aidbox.app) only XSD validation is implemented. Uploaded XML file will be highlighted with green color if the document passed validation.
{% endhint %}


### Persisting a result of CCDA to FHIR conversion 

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

Aidbox provides a function of saving result of successful CCDA->FHIR conversion. 
When CCDA document is converted - transactional Bundle resource is created with all resources that were created during the conversion.
Transactional Bundle is executed and resources are persisted in DB. According to its transactional nature  - any single failure will rollback a whole transaction and nothing will be saved. 

- If the same document is uploaded - resources in DB will remain unchanged. 
- If uploaded document contain changes to resources with `id` that are already stored in DB - such documents will be updated (patched). 
- References of created resources during the transaction will be stored in Provenance resource. 

If you pass `create-docref` parameter then initial CCDA document will be saved as DocumentReference FHIR resource in Base64 format:

```http
POST /ccda/persist?create-docref=true
Authorization: .....
Content-Type: application/cda+xml
```
