# Aidbox

We built Aidbox with the [FHIR](http://www.hl7.org/fhir/) standard. FHIR \(Fast Healthcare Interoperability Resources\) is an open HL7 standard and specification for storing and exchanging healthcare data by leveraging modern web technologies.

The FHIR standard currently describes about 116 models for storing health data, and these units are called [Resources](http://www.hl7.org/fhir/resourcelist.html) \(such as Patient, Encounter, Observation etc.\). FHIR uses a [REST API](https://hello.aidbox.io/docs/rest-api) to access and manipulate the data \(Create, Update, Search, etc\).

Aidbox is a FHIR backend as a service with useful additions.

With Aidbox, you can focus on your business ideas validation, and leave the backend technical details to us.

Aidbox provides you with everything you need to start building your healthcare application:

* A scalable and powerful PostgreSQL database \([fhirbase](http://fhirbase.github.io/)\) to store and query your data
* [FHIR](http://www.hl7.org/fhir/) compliant REST API to access and manipulate this data
* OAuth 2.0 and flexible security rules to control access to application data
* API to manage application users, with optional registration module
* Terminology server with popular medical coding systems \(LOINC, SNOMED, ICD10, RxNorm\) and custom dictionaries \(ValueSets\)
* Hosting for Single Page Applications written in HTML and JavaScript
* Extension and customization aidbox with Aidbox SDK

With Aidbox, you can easily develop Mobile, Single Page, and classic Web Applications.

