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
* LOINC - [https://storage.googleapis.com/aidbox-public/loinc/loinc-2.65.ndjson.gz](https://storage.googleapis.com/aidbox-public/loinc/loinc-2.65.ndjson.gz)
* FHIR 4.0.0 - [https://storage.googleapis.com/aidbox-public/fhir-terminology/fhir-4.0.0-concepts.ndjson.gz](https://storage.googleapis.com/aidbox-public/fhir-terminology/fhir-4.0.0-concepts.ndjson.gz)

### RxNorm

To import RxNorm concepts execute following request:

```yaml
POST /
Content-Type: text/yaml

resourceType: Bundle
type: transaction
entry:
  - resource:
      resourceType: CodeSystem
      id: rxnorm
      url: http://www.nlm.nih.gov/research/umls/rxnorm
      date: '2019'
      description: RxNorm is just an RxNorm
      content: complete
      status: active
      version: '10072019'
    request:
      url: /CodeSystem/rxnorm
      method: PUT

  - resource:
      resourceType: ValueSet
      id: rxnorm
      status: active
      description: This value set includes all RxNorm codes.
      version: '10072019'
      compose:
        include:
          - system: http://www.nlm.nih.gov/research/umls/rxnorm
    request:
      url: /ValueSet/rxnorm
      method: PUT

  - resource:
      id: rxnorm
      inputFormat: application/fhir+ndjson
      contentEncoding: gzip
      mode: bulk
      inputs:
        - resourceType: Concept
          url: https://storage.googleapis.com/aidbox-public/fhir-terminology/rxnorm-10072019.ndjson.gz
    request:
      url: /$import
      method: POST
```

