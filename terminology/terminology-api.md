---
description: Terminology API
---

# Terminology API

### /terminology/$import

Terminology import operation allows you to load prepared terminology concept packages into you server.

```yaml
POST /terminology/$import

url: <terminology-package-url>
```

Here is list of available packages:

* ICD-10 - [https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz](https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz)
* LOINC - [https://storage.googleapis.com/aidbox-public/loinc/loinc-concepts-2.65.ndjson.gz](https://storage.googleapis.com/aidbox-public/loinc/loinc-concepts-2.65.ndjson.gz)
* FHIR 4.0.0 - [https://storage.googleapis.com/aidbox-public/fhir-terminology/fhir-4.0.0-concepts.ndjson.gz](https://storage.googleapis.com/aidbox-public/fhir-terminology/fhir-4.0.0-concepts.ndjson.gz)

SNOMED & RxNorm are coming! Contact us in a [community chat](https://community.aidbox.app).

