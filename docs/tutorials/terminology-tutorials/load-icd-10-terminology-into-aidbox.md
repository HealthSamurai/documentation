---
description: In this post, you will learn how to load ICD-10 terminology into aidbox.
---

# Load ICD-10 Terminology into Aidbox

{% embed url="https://youtu.be/zXzy-is20e8" %}

With a new version of Aidbox we provide you with a set of terminology packages like ICD-10 and LOINC and a special bulk operation to import these code sets.

You can read more about how terminology service is designed in a our CTO blog post - [Two-phase terminology](https://medium.com/@niquola/two-phase-fhir-terminology-e52e1b105f6d).

Aidbox team prepared terminology packages with popular code systems. These packages are essentially ndjson files with a set of concept resources and they are available by public url in our cloud.

Here are a few packages you can start with:

* ICD-10 - [https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz](https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz)
* LOINC - [https://storage.googleapis.com/aidbox-public/loinc/loinc-concepts-2.65.ndjson.gz](https://storage.googleapis.com/aidbox-public/loinc/loinc-concepts-2.65.ndjson.gz)

Rx-Norm, SNOMED-CT and basic FHIR CodeSystems/ValueSets packages are in progress. If you need something specific, ping us in a [Aidbox Community Chat](https://t.me/aidbox).

In this tutorial, we will load ICD-10 deceases codes into Aidbox and see how we can look up codes using Concept resource Search API.

To import these terminology packs, you can use `terminology/$import` operation. Let's load ICD-10 (Classification of Deceases): just copy-paste the following snippet into REST console in Aidbox UI:

{% code title="request.yalm" %}
```yaml
POST /terminology/$import

url: 'https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz'
```
{% endcode %}

You will get a response with numbers of resources loaded:

{% code title="response.yaml" %}
```yaml
status: 200
result: {CodeSystem: 1, ValueSet: 1, Concept: 44487}
```
{% endcode %}

As you can see, a package consists of one CodeSystem, one ValueSet and about 40K concepts with decease codes.

Let's go to REST console and see what we have:

```yaml
GET /CodeSystem

# response 200

resourceType: Bundle
type: searchset
entry:
- resource:
    url: ICD-10
    id: icd-10
    name: ICD-10-CM
    status: active
    content: complete
    version: '2019'
    date: '2019'
    resourceType: CodeSystem
    description: International Classification of Diseases
```

```yaml
GET /ValueSet

# response 200

resourceType: Bundle
type: searchset
entry:
- resource:
    url: icd-10
    id: icd-10
    name: ICD-10
    status: active
    compose:
      include:
      - {system: ICD-10}
    version: 0.0.1
    date: '2019-01-01'
    resourceType: ValueSet
    description: This value set includes all ICD-10 codes.
```

Now we can search diagnoses with, for example, [\_ilike](../../api/rest-api/fhir-search/searchparameter.md#ilike) parameter:

```yaml
GET /Concept?system=ICD-10&_ilike=flue

# response 200
resourceType: Bundle
type: searchset
entry:
- resource:
    code: A08
    system: ICD-10
    display: Viral and other specified intestinal infections
    property:
      icd10:
        excludes1: ['influenza with involvement of gastrointestinal tract (J09.X3, 10.2, J11.2)']
    valueset: [icd-10]
    id: ICD-10-A08
    resourceType: Concept
- resource:
    code: A41.3
    system: ICD-10
    display: Sepsis due to Hemophilus influenzae
    valueset: [icd-10]
    id: ICD-10-A41-3
    resourceType: Concept
...
```

Or inspect concepts in Aidbox Console UI:

![](../../../.gitbook/assets/31b89cd1-c3f2-4a63-8591-2873f7d562f6.png)

### Indexing \_like search

Health Samurai Team is working on declarative search indexing in the future release. For now, let's create a trigram index to make `_ilike` search faster. Go to SQL Console and execute the following statement:

```sql
CREATE INDEX concept_trgm_idx  on concept
USING gin (
  (id || ' ' || resource::text) gin_trgm_ops
);

VACUUM ANALYZE concept;
```

Try `_ilike` search again - is it faster?

### Clean up

To clean up all terminology, you can truncate all related tables from db console:

```sql
truncate concept;
truncate codesystem;
truncate valueset;
```
