---
description: >-
  This page describes how to populate C-CDA documents from FHIR data stored in
  Aidbox.
---

# Producing C-CDA documents

To generate a C-CDA document from FHIR data, it is necessary to create a FHIR Document bundle containing a Composition resource that specifies the top-level document attributes, including the title, document type, author, subject (patient), and a list of document sections. Each section must be described by type, title, narrative, and the FHIR resources to be included. Once the FHIR Document bundle is composed, it can be submitted to the /ccda/v2/to-ccda endpoint for conversion to a C-CDA document.

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

Each resource attribute, such as `:subject`, `:author`, or `:section/:entry`, is specified as a FHIR transaction request that returns a single resource or multiple resources. The full power of the FHIR Search API can be used to retrieve resources that meet specific criteria. For example, in the last section of the sample document, only observations for a specific patient and category = vital-signs are retrieved. Additional criteria, such as time span, can be easily added. Simple parameter interpolation is also supported. In the above example, the Patient ID is not hardcoded but specified as `{{pid}}` parameter, so the same Document Definition can be used to populate documents for different patients.

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
