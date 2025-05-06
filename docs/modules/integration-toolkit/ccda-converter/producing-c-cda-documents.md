---
description: >-
  This page describes how to populate C-CDA documents from FHIR data stored in
  Aidbox FHIR Server.
---

# Producing C-CDA documents

{% hint style="info" %}
C-CDA / FHIR Converter provides bidirectional mapping for all data elements from the [USCDI v1](https://www.healthit.gov/isa/sites/isa/files/2020-10/USCDI-Version-1-July-2020-Errata-Final_0.pdf) list. [Detailed list of supported C-CDA sections](sections/) is also available.
{% endhint %}

In certain cases, such as regulatory requirements, legacy systems integration, or the need to support a specific workflow, it may be necessary to convert FHIR data to C-CDA format to seamlessly integrate systems.

A typical FHIR to C-CDA conversion process is usually divided into three main steps:

* **Gather required data in FHIR format:** In this step, a number of GET requests are performed to retrieve the required data from the FHIR server or source systems.
* **Generate FHIR 'document' type Bundle:** The gathered data is organized into a FHIR Document Bundle, with the FHIR Composition resource serving as the main document container.
* **Convert FHIR Document to C-CDA Document:** In this step, the FHIR data is mapped and transformed into C-CDA structures, and divided into entries and sections according to the C-CDA document template requirements.

Aidbox simplifies this process by incorporating features such as document definitions, section narrative generators, and pseudo-code scripts to preprocess the document before the conversion.

In technical terms, to generate a C-CDA document from FHIR data, it is necessary to create a [FHIR Document](https://hl7.org/fhir/R4/documents.html) bundle containing a [Composition](https://hl7.org/fhir/R4/composition.html) resource that specifies the top-level document attributes, including the title, document type, author, subject (patient), and a list of document sections. Each section must be described by type, title and the FHIR resources to be included. Once the FHIR Document bundle is composed, it can be submitted to the /ccda/v2/to-ccda endpoint for conversion to a C-CDA document.

Below are some details about each of these features.

### Document Definitions

To simplify the creation of Document bundles, Aidbox offers a feature called Document Definition, which enables the description of document contents using the FHIR Search API. The example below illustrates how to define a Document Definition:

```clojure
{:type {:code "34133-9" 
        :display "Summarization of Episode Note"
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
   :entriesRequired true
   :entry
   {:method "GET"
    :url "/AllergyIntolerance?patient=Patient/{{pid}}"}}

  {:title "Results"
   :code {:code "30954-2"
          :display "Results"
          :system "http://loinc.org"}
   :entriesRequired true
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
   :entriesRequired true
   :entry
   {:method "GET"
    :url "/Condition?patient=Patient/{{pid}}&category=problem-list-item"}}
    
  {:title "Hospital Course"
    :code {:code "8648-8" :display "Hospital course Narrative" :system "http://loinc.org"}
    :text {:method "GET" :url "/Observation?subject=Patient/{{pid}}&code=8648-8"}}

  {:title "Vital Signs"
   :code {:code "8716-3"
          :display "Vitals"
          :system "http://loinc.org"}
   :entriesRequired true
   :entry
   {:method "GET"
    :url "/Observation?category=vital-signs&patient=Patient/{{pid}}"}}]}
```

Each resource attribute, such as `:subject`, `:author`, or `:section/:entry`, is specified as a HTTP request that returns a single FHIR resource or multiple FHIR resources. The full power of the [FHIR Search API](https://www.hl7.org/fhir/search.html) can be used to retrieve resources that meet specific criteria.

Parameters interpolation is also supported. For example, in the Vitals Signs section of the sample above:

```clojure
{:method "GET"
 :url "/Observation?category=vital-signs&patient=Patient/{{pid}}"}
```

`{{pid}}` will be replaced with the value passed in the query-string parameter `pid` and this way vitals observations are scoped to the specific patient.

Another frequent use-case is date filtering. It can be easily added with two parameters: `start-date` and `end-date:`

```clojure
{:method "GET"
 :url "/Observation?category=vital-signs&patient=Patient/{{pid}}&date={{start-date}}&date={{end-date}}"}
```

All the filtering logic is strictly described by the [FHIR Search specification](https://www.hl7.org/fhir/search.html), so the date filtering in the example above [is covered by `date` search param type](https://www.hl7.org/fhir/search.html#date).

Multiple FHIR searches per section is also possible:

```clojure
{:title "Procedures"
 :code {:code "47519-4" :display "History of Procedures Document" :system "http://loinc.org"}
 :entry
 [{:method "GET" :url "/Procedure?subject=Patient/{{pid}}&category:not=225299006&status=completed&date=ge{{start-date}}&date=le{{end-date}}&_sort=date"}
  {:method "GET" :url "/Procedure?subject=Patient/{{pid}}&status=completed&category=225299006"}]}
```

### Section templates and LOINC codes

To pick the right `templateId` for a section, converter uses LOINC to OID mapping table which can be found on the [List of supported sections page](sections/). "Entries Required" / "Entries Optional" variation can be specified via FHIR extension. In the example below document contains two sections: [Social History Section (V3)](./sections/social-history-section-docs-v3.md) and [Allergies and Intolerances Section (entries required) (V3)](./sections/allergies-and-intolerances-section-docs-entries-optional-docs-v3.md).

```json
{
  "resourceType": "Composition",
  ...
  "section": [
    {
      "title": "Social History",
      "code" : {
        "text" : "Social History",
        "coding" : [ {
          "code" : "29762-2",
          "display" : "Social History",
          "system" : "http://loinc.org"
        } ]
      },
      "entry": [ ... ]
    }, {
      "title": "Allergies and Intolerances",
      "extension" : [ {
        "value" : {
          "boolean" : true
        },
        "url" : "entries-required"
      } ],
      "code" : {
        "coding" : [ {
          "code" : "48765-2",
          "display" : "48765-2",
          "system" : "http://loinc.org"
        } ]
      },
      "entry": [ ... ]
    }
  ]
}
```

### Section Narratives

Along with structured entries, CDA section contains human-readable narrative describing section data. This narrative can be automatically generated from entries ([if specific section supports it](sections/)) or it can be retrieved from Observation resource. To retrieve narrative from Observation, provide corresponding request under the `:text` key:

```clojure
{:title "Discharge Instructions"
 :code {:code "8653-8"
        :display "Discharge instructions"
        :system "http://loinc.org"}
 :text {:method "GET" :url "/Observation?subject=Patient/{{pid}}&code=8653-8"}}
```

The best practice is to have Observation.code to be equal to section LOINC code for Observations containing section narratives. Narrative itself can be stored in either `Observation.note[0].text` or `Observation.value.string`.

### Making modifications to the source FHIR Document

Quite often it's needed to make ad-hoc changes here and there in the CDA document to meet specific requirements. For example, one may want to generate section narrative from section entries or discard all patient identifiers except for SSN. To make this possible, you can get FHIR Document populated by Document Definition via `/ccda/prepare-doc` endpoint and then make modifications to it. When all modifications are in place, you can submit result to the `/ccda/v2/to-cda` endpoint to generate the final CDA document. Consider the following pseudo-code which removes all patient idenfitiers from the Patient resource except for SSN:

```javascript
var docdef = { ... };
var bundle = aidbox.post('/ccda/prepare-doc', docdef);

var composition = bundle.entry[0].resource;
var patient = bundle.findByRef(composition.subject);

for (i = 0; i < patient.identifier.length; i++) {
  var ident = patient.identifier[i];
  if (ident.system != 'http://hl7.org/fhir/sid/us-ssn') {
    patient.identifier[i] = null;
  }
}

var cda = aidbox.post('/ccda/v2/to-cсda', bundle);
```

Another pseudo-code example on how to populate section narrative from section entries:

```javascript
function generateVitalSignsNarrative(section, bundle) {
  var result = '';
  
  for (i = 0; i < section.entry.length; i++) {
    var vs = bundle.findByRef(section.entry[i]);
    
    result += '<paragraph>' + 
      formatCode(vs.code) + " " +
      formatDate(vs.effective) + " " +
      formatValue(vs.value) + '</paragraph>';
  }
  
  if (section.entry.length == 0) {
    result = 'No vital signs available';
  }
  
  return result;
}

var docdef = { ... };
var bundle = aidbox.post('/ccda/prepare-doc', docdef);

var composition = bundle.entry[0].resource;

for (i = 0; i < composition.section.length; i++) {
  var section = composition.section[i];
  
  if (section.code.coding[0].code == '8716-3') {
    section.text = {
      // this function receives all the data it needs to populate narrative
      // it returns stringified HTML
      div: generateVitalSignsNarrative(section, bundle),
      status: 'generated'
    };
  }
}

var cda = aidbox.post('/ccda/v2/to-cсda', bundle);
```

### Predefined Document Definitions

C-CDA / FHIR module provides ready-to-use Document Definitions for most frequently used document types.

| docdef-id          | Description | Supported Parameters                                                                     |
| ------------------ | ----------- | ---------------------------------------------------------------------------------------- |
| continuity-of-care | CCD         | <p><code>pid</code> - Patient ID<br><code>start-date</code><br><code>end-date</code></p> |

Additionally to this list, you can put your own predefined Document Definitions via [Aidbox Configuration Project](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/).

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
// GET /ccda/prepare-doc?docdef-id=continuity-of-care?pid=xxx
// HTTP/1.1 200 OK

{
  "resourceType": "Bundle",
  "type": "document",
  "entry": [...]
}
```

```json
// GET /ccda/prepare-doc?docdef-id=continuity-of-care?pid=xxx
// HTTP/1.1 404 Not found

{
  "resourceType": "OperationOutcome",
  "id": "processing",
  "text": {
    "status": "generated",
    "div": "Transaction failed at entry[0]. Response status is 404. Response body is {\"resourceType\":\"OperationOutcome\",\"id\":\"not-found\",\"text\":{\"status\":\"generated\",\"div\":\"Resource Organization/42 not found\"},\"issue\":[{\"severity\":\"fatal\",\"code\":\"not-found\",\"diagnostics\":\"Resource Organization/42 not found\"}]}."
  },

  ....
```

You may set the mode of resolving references in`mode` query-string parameter, if you pass `resolve-all`all unresolved references will be resolved independently of FHIR Search query un Document Definition.

```http
GET /ccda/prepare-doc?mode=resolve-all&docdef-id=continuity-of-care&pid=42&start-date=2023-01-01&end-date=2023-02-01
```

You may also pass `:resolve` instruction inside Document Definition. It will resolve references by path even if FHIR Search does not have such reference in search-params.

```http
{
  "method": "GET",
  "url": "/MedicationStatement?subject=Patient/{{pid}}",
  "resolve": [
    "MedicationStatement.informationSource"
  ]
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

```json
// GET /ccda/make-doc?docdef-id=continuity-of-care?pid=xxx
// HTTP/1.1 404 Not found

{
  "resourceType": "OperationOutcome",
  "id": "processing",
  "text": {
    "status": "generated",
    "div": "Transaction failed at entry[0]. Response status is 404. Response body is {\"resourceType\":\"OperationOutcome\",\"id\":\"not-found\",\"text\":{\"status\":\"generated\",\"div\":\"Resource Organization/42 not found\"},\"issue\":[{\"severity\":\"fatal\",\"code\":\"not-found\",\"diagnostics\":\"Resource Organization/42 not found\"}]}."
  },

  ....
```

Predefined Document Definition example:

```http
GET /ccda/prepare-doc?docdef-id=continuity-of-care&pid=42&start-date=2023-01-01&end-date=2023-02-01
```

{% hint style="info" %}
These [Terms](https://storage.googleapis.com/aidbox-public/smartbox/certification/terms-of-use/Terms%20of%20use.pdf) govern your access to and use of the Aidbox FHIR API module. By accessing or using the Aidbox FHIR API, the App Developer (“You”), agrees to be bound by [these Terms](https://storage.googleapis.com/aidbox-public/smartbox/certification/terms-of-use/Terms%20of%20use.pdf).
{% endhint %}
