---
description: >-
  This page describes how to populate C-CDA documents from FHIR data stored in
  Aidbox.
---

# Producing C-CDA documents

{% hint style="info" %}
C-CDA / FHIR Converter provides bidirectional mapping for all data elements from the [USCDI v1](https://www.healthit.gov/isa/sites/isa/files/2020-10/USCDI-Version-1-July-2020-Errata-Final\_0.pdf) list.
{% endhint %}

To generate a C-CDA document from FHIR data, it is necessary to create a [FHIR Document](https://hl7.org/fhir/R4/documents.html) bundle containing a [Composition](https://hl7.org/fhir/R4/composition.html) resource that specifies the top-level document attributes, including the title, document type, author, subject (patient), and a list of document sections. Each section must be described by type, title and the FHIR resources to be included. Once the FHIR Document bundle is composed, it can be submitted to the /ccda/v2/to-ccda endpoint for conversion to a C-CDA document.

To simplify the creation of Document bundles, Aidbox offers a feature called Document Definition, which enables the description of document contents using the FHIR Search API. The example below illustrates how to define a Document Definition:

```clojure
{:type {:code  "18842-5",
        :display "Discharge Summary",
        :system "http://loinc.org"}

 :date "2020-02-02"
 :title "Discharge summary example"
 :subject {:method "GET"
           :url "/Patient/{{pid}}"}

 :custodian {:method "GET"
             :url "/Organization/42"}

 :author {:method "GET"
          :url "/Practitioner/42"}

 :section
 [{:title "Allergies and Intolerances"
   :code {:code "48765-2"
          :display "Allergies"
          :system "http://loinc.org"}
   :entry
   {:method "GET"
    :url "/AllergyIntolerance?patient=Patient/{{pid}}"}}

  {:title "Results"
   :code {:code "30954-2"
          :display "Results"
          :system "http://loinc.org"}
   :entry
   {:method "GET"
    :url "/Observation?patient=Patient/{{pid}}&category=laboratory&_assoc=hasMember"}}

  {:title "Social History"
   :code {:code "29762-2"
          :display "Social History"
          :system "http://loinc.org"}
   :entry
   {:method "GET"
    :url "/Observation?patient=Patient/{{pid}}&category=social-history&_assoc=hasMember"}}

  {:title "Problems"
   :code {:code "11450-4"
          :display "Problems"
          :system "http://loinc.org"}
   :entry
   {:method "GET"
    :url "/Condition?patient=Patient/{{pid}}&category=problem-list-item"}}

  {:title "Vital Signs"
   :code {:code "8716-3"
          :display "Vitals"
          :system "http://loinc.org"}
   :entry
   {:method "GET"
    :url "/Observation?category=vital-signs&patient=Patient/{{pid}}"}}]}
```

Each resource attribute, such as `:subject`, `:author`, or `:section/:entry`, is specified as a HTTP request that returns a single FHIR resource or multiple FHIR resources. The full power of the FHIR Search API can be used to retrieve resources that meet specific criteria. For example, in the last section of the sample document:

```clojure
{:method "GET"
 :url "/Observation?category=vital-signs&patient=Patient/{{pid}}"}
```

only observations for a specific patient and category `vital-signs` are retrieved.&#x20;

Additional criteria, such as time span, can be easily added:

```clojure
{:method "GET"
 :url "/Observation?category=vital-signs&patient=Patient/{{pid}}&date={{start-date}}&date={{end-date}}"}
```

In the above example, the Patient ID is not hardcoded but specified as `{{pid}}` parameter, so the same Document Definition can be used to populate documents for different patients.

### Predefined Document Definitions

C-CDA / FHIR module provides ready-to-use Document Definitions for most frequently used document types.

| docdef-id          | Description | Supported Parameters                                                                     |
| ------------------ | ----------- | ---------------------------------------------------------------------------------------- |
| continuity-of-care | CCD         | <p><code>pid</code> - Patient ID<br><code>start-date</code><br><code>end-date</code></p> |

Additionally to this list, you can put your own predefined Document Definitions via [Aidbox Configuration Project](../../../aidbox-configuration/aidbox-zen-lang-project/).

### /ccda/prepare-doc endpoint

This endpoint accepts Document Definition in request body or `docdef-id` and converts it into the FHIR Document bundle without applying C-CDA transformation. Resulting FHIR Document is returned. This endpoint is useful to debug Document Definition and check if all needed data is in place.

Endpoint uses query string to get values for Document Definition parameters. In the example below `{{pid}}` param will be replaced with `42` from the query string.

```http
POST /ccda/prepare-doc?pid=42
Content-Type: application/json

{
  "type": {
    "code": "18842-5",
    "display": "Discharge Summary",
    "system": "http://loinc.org"
  },
  "date": "2020-02-02",
  "title": "Discharge summary example",
  "subject": {
    "method": "GET",
    "url": "/Patient/{{pid}}"
  },
  "custodian": {
    "method": "GET",
    "url": "/Organization/42"
  },
  "author": {
    "method": "GET",
    "url": "/Practitioner/42"
  },
  "section": [
    {
      "title": "Vital Signs",
      "code": {
        "code": "8716-3",
        "display": "Vitals",
        "system": "http://loinc.org"
      },
      "template": "Vitals",
      "entry": {
        "method": "GET",
        "url": "/Observation?category=vital-signs&patient=Patient/{{pid}}"
      }
    }
  ]
}
```

You can pass predefined Document Definition ID in`docdef-id` query-string parameter:

```http
GET /ccda/prepare-doc?docdef-id=continuity-of-care&pid=42&start-date=2023-01-01&end-date=2023-02-01
```

Endpoint returns a FHIR Document or OperationOutcome resource in case of error.

```json
// GET /ccda/prepare-doc?docdef-id=continuity-of-care
// HTTP/1.1 200 OK

{
  "resourceType": "Bundle",
  "type": "document",
  "entry": [...]
}
```

### /ccda/make-doc

Behaves the same way as `/ccda/prepare-doc`, but additionally applies C-CDA transformation, so you'll get C-CDA XML document in response.

```http
POST /ccda/make-doc?pid=42
Content-Type: application/json

{
  "type": {
    "code": "18842-5",
    "display": "Discharge Summary",
    "system": "http://loinc.org"
  },
  "date": "2020-02-02",
  "title": "Discharge summary example",
  "subject": {
    "method": "GET",
    "url": "/Patient/{{pid}}"
  },
  "custodian": {
    "method": "GET",
    "url": "/Organization/42"
  },
  "author": {
    "method": "GET",
    "url": "/Practitioner/42"
  },
  "section": [
    {
      "title": "Vital Signs",
      "code": {
        "code": "8716-3",
        "display": "Vitals",
        "system": "http://loinc.org"
      },
      "template": "Vitals",
      "entry": {
        "method": "GET",
        "url": "/Observation?category=vital-signs&patient=Patient/{{pid}}"
      }
    }
  ]
}
```

Response:

```http
200 OK
Content-Type: application/cda+xml

<?xml version="1.0" encoding="UTF-8"?>
<ClinicalDocument 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
  xmlns="urn:hl7-org:v3"
  xmlns:voc="urn:hl7-org:v3/voc"
  xmlns:sdtc="urn:hl7-org:sdtc">
  
...
```

Predefined Document Definition example:

```http
GET /ccda/prepare-doc?docdef-id=continuity-of-care&pid=42&start-date=2023-01-01&end-date=2023-02-01
```

{% hint style="info" %}
These [Terms](https://storage.googleapis.com/aidbox-public/smartbox/certification/terms-of-use/Terms%20of%20use.pdf) govern your access to and use of the Aidbox FHIR API module. By accessing or using the Aidbox FHIR API, the App Developer (“You”), agrees to be bound by [these Terms](https://storage.googleapis.com/aidbox-public/smartbox/certification/terms-of-use/Terms%20of%20use.pdf).
{% endhint %}
