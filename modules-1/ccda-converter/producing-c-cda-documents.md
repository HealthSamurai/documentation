---
description: >-
  This page describes how to populate C-CDA documents from FHIR data stored in
  Aidbox.
---

# Producing C-CDA documents

To populate a C-CDA document from FHIR data, it is necessary to compose a [FHIR Document bundle](https://build.fhir.org/documents.html) first. The FHIR Document bundle should contain a [Composition](https://www.hl7.org/fhir/composition.html) resource, which outlines the top-level attributes of the document, including the title, document type, author, subject (patient), and most importantly, the list of document sections. Each document section is specified by type, title, narrative, and the FHIR resources that will be included in that section. Once such bundle is composed, it can be posted to the [/ccda/v2/to-ccda](./#converting-a-fhir-document-to-c-cda) endpoint to convert it to the C-CDA document.

To simplify composing of such Document bundles, Aidbox provides a way to describe document contents in terms of the [FHIR Search API](https://hl7.org/fhir/search.html) using so-called Document Definiton. Document Definition includes attributes such as the type of document to be generated (specified by a LOINC code), the patient for whom the document is being generated, the date of the document, the title of the document, the organization listed as the custodian of the document, the author of the document, and the sections of the document (each with its own LOINC code and narrative template).

Consider the following Document Definition example:

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
   :template "Allergies"
   :entry
   {:method "GET"
    :url "/AllergyIntolerance?patient=Patient/{{pid}}"}}

  {:title "Results"
   :code {:code "30954-2"
          :display "Results"
          :system "http://loinc.org"}
   :template "Results html"
   :entry
   {:method "GET"
    :url "/Observation?patient=Patient/{{pid}}&category=laboratory&_assoc=hasMember"}}

  {:title "Social History"
   :code {:code "29762-2"
          :display "Social History"
          :system "http://loinc.org"}
   :template "Social History"
   :entry
   {:method "GET"
    :url "/Observation?patient=Patient/{{pid}}&category=social-history&_assoc=hasMember"}}

  {:title "Problems"
   :code {:code "11450-4"
          :display "Problems"
          :system "http://loinc.org"}
   :template "Problems"
   :entry
   {:method "GET"
    :url "/Condition?patient=Patient/{{pid}}&category=problem-list-item"}}

  {:title "Vital Signs"
   :code {:code "8716-3"
          :display "Vitals"
          :system "http://loinc.org"}
   :template "Vitals"
   :entry
   {:method "GET"
    :url "/Observation?category=vital-signs&patient=Patient/{{pid}}"}}]}
```

Each resource attribute such as `:subject`, `:author` or `:section / :entry` is specified as FHIR transaction request which returns single resource or multiple resources. Therefore the full power of FHIR Search API can be used here to retrieve resources which meets certain criteria. For example, in the last section of this sample document, only Observations for specific patient and  category = vital-signs are retrieved. Additional criteria such as time span can be added easily.

Simple parameters interpolation is also supported. In example above, Patient ID is not hardcoded but specified as `{{pid}}` parameter, so the same Document Definition can be used to populate documents for different patients.

### /ccda/prepare-doc endpoint

This endpoint accepts Document Definition in request body and converts it into the FHIR Document bundle without applying C-CDA transformation. This endpoint is useful to debug Document Definition and check if all needed data is in place.

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

Endpoint returns a FHIR Document or OperationOutcome resource in case of error.

### /ccda/make-doc

Behaves the same way as `/ccda/prepare-doc`, but additionally applies C-CDA transformation, so you'll get C-CDA XML document in response.

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
