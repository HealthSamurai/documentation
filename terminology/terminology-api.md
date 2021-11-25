---
description: Use a pre-packages terminology bundles to import codes into Aidbox
---

# Import external (not-present) terminologies

### /terminology/$import

Terminology import operation allows you to load prepared terminology concept packages into you server.

```yaml
POST /terminology/$import

url: <terminology-package-url>
```

Here is a list of available packages:

* ICD-10 - [https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz](https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz)
* LOINC - [https://storage.googleapis.com/aidbox-public/loinc/loinc-2.65.ndjson.gz](https://storage.googleapis.com/aidbox-public/loinc/loinc-2.65.ndjson.gz)
* FHIR 4.0.0 - [https://storage.googleapis.com/aidbox-public/fhir-terminology/fhir-4.0.0-concepts.ndjson.gz](https://storage.googleapis.com/aidbox-public/fhir-terminology/fhir-4.0.0-concepts.ndjson.gz)
* RxNorm 10072019 - [https://storage.googleapis.com/aidbox-public/fhir-terminology/rxnorm-10072019.ndjson.gz](https://storage.googleapis.com/aidbox-public/fhir-terminology/rxnorm-10072019.ndjson.gz)
* SNOMED CT US Edition 20190901 - [https://storage.googleapis.com/aidbox-public/fhir-terminology/snomedct-20190901.ndjson.gz](https://storage.googleapis.com/aidbox-public/fhir-terminology/snomedct-20190901.ndjson.gz)

### RxNorm

To import RxNorm concepts, execute the following request:

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

#### How to search RxNorm concepts by RxCUI

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Concept/?system=http://www.nlm.nih.gov/research/umls/rxnorm&code=636671
```
{% endtab %}

{% tab title="Response" %}
```
resourceType: Bundle
type: searchset
entry:
- resource:
    code: '636671'
    system: http://www.nlm.nih.gov/research/umls/rxnorm
    display: varenicline 0.5 MG Oral Tablet
    valueset: [rxnorm]
    id: rxnorm-636671
    resourceType: Concept
    meta: {lastUpdated: '2019-12-18T21:18:10.885605Z', createdAt: '2019-12-18T21:18:10.885605Z', versionId: '199'}
  fullUrl: https://testrevinclude.edge.aidbox.app/Concept/rxnorm-636671
total: 1
link:
- {relation: first, url: '/Concept?system=http://www.nlm.nih.gov/research/umls/rxnorm&code=636671&page=1'}
- {relation: self, url: '/Concept?system=http://www.nlm.nih.gov/research/umls/rxnorm&code=636671&page=1'}
query-sql: ['SELECT "concept".* FROM "concept" WHERE (aidbox_text_search(knife_extract_text("concept".resource, $JSON$[["system"]]$JSON$)) ilike unaccent(?) AND aidbox_text_search(knife_extract_text("concept".resource, $JSON$[["code"]]$JSON$)) ilike unaccent(?)) LIMIT ? OFFSET ? ', '% http://www.nlm.nih.gov/research/umls/rxnorm%', '% 636671%', 100, 0]
query-timeout: 60000
query-time: 1259
```
{% endtab %}
{% endtabs %}

### SNOMEDCT

```yaml
POST /
Content-Type: text/yaml

resourceType: Bundle
type: transaction
entry:
  - resource:
      resourceType: CodeSystem
      id: snomedct
      url: http://snomed.info/sct
      date: '2019-09-01'
      description: SNOMED CT is a standardized, multilingual vocabulary of clinical terminology
        that is used by physicians and other health care providers for the electronic exchange
        of clinical health information
      content: complete
      status: active
      version: snomed-version
    request:
      url: /CodeSystem/snomedct
      method: PUT

  - resource:
      resourceType: ValueSet
      id: snomedct
      description: This value set includes all RxNorm codes.
      version: snomed-version
      compose:
        include:
        - system: http://snomed.info/sct
      url: snomedct
      status: active
    request:
      url: /ValueSet/snomedct
      method: PUT

  - resource:
      id: snomedct
      inputFormat: application/fhir+ndjson
      contentEncoding: gzip
      mode: bulk
      inputs:
        - resourceType: Concept
          url: https://storage.googleapis.com/aidbox-public/fhir-terminology/snomedct-20190901.ndjson.gz
    request:
      url: /$import
      method: POST
```
