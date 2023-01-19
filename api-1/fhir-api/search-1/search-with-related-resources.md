# Search with related resources

A client can add related resources to a search result using [**(rev)include**](https://www.hl7.org/fhir/search.html#revinclude) FHIR parameters and **with** Aidbox parameter. In ORM frameworks, such feature is sometimes called an "associations eager loading". This technique can save extra roundtrips from the client to the server and potential N+1 problem.

### Example

This example demonstrates how [\_include, \_revinclude, and \_with](\_include-and-\_revinclude.md) search parameters work_._ You may want to get encounters with patients (each encounter refers to patient):

#### Create Patient&#x20;

```
PUT /Patient

resourceType: Patient
id: pat-234
name:
  - family: Smith
```

#### Create Encounter

```
PUT /Encounter

resourceType: Encounter
id: enc-234
subject: 
  resourceType: Patient
  id: pat-234
class: {code: 'IMP', 
  system: 'http://terminology.hl7.org/CodeSystem/v3-ActCode', 
  display: 'inpatient encounter'}
status: finished
```

#### Search with \_include

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Encounter?_include=Encounter:subject:Patient
```
{% endtab %}

{% tab title="Response" %}
```yaml
type: searchset
resourceType: Bundle
total: 1
link:
  - relation: first
    url: /Encounter?_include=Encounter:subject:Patient&page=1
  - relation: self
    url: /Encounter?_include=Encounter:subject:Patient&page=1
entry:
  - resource:
      class:
        code: IMP
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: inpatient encounter
      status: finished
      subject:
        id: >-
          pat-234
        resourceType: Patient
      id: >-
        enc-234
      resourceType: Encounter
    search:
      mode: match
    fullUrl: [base]/Encounter/enc-234
    link:
      - relation: self
        url: [base]/Encounter/enc-234
  - resource:
      name:
        - family: Smith
      id: >-
        pat-234
      resourceType: Patient
    search:
      mode: include
    fullUrl: [base]/Patient/pat-234
    link:
      - relation: self
        url: [base]/Patient/pat-234
query-sql:
  - 'SELECT "encounter".* FROM "encounter" LIMIT ? OFFSET ? '
  - 100
  - 0
include-queries:
  - - SELECT * FROM "patient" WHERE (id in (?)) LIMIT ?
    - pat-234
    - 5000
```
{% endtab %}
{% endtabs %}

#### Search with \_with

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Encounter?_with=subject{Patient}
```
{% endtab %}

{% tab title="Response" %}
```yaml
type: searchset
resourceType: Bundle
total: 1
link:
  - relation: first
    url: /Encounter?_with=subject{Patient}&page=1
  - relation: self
    url: /Encounter?_with=subject{Patient}&page=1
entry:
  - resource:
      class:
        code: IMP
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: inpatient encounter
      status: finished
      subject:
        id: >-
          pat-234
        resourceType: Patient
      id: >-
        enc-234
      resourceType: Encounter
    search:
      mode: match
    fullUrl: [base]/Encounter/enc-234
    link:
      - relation: self
        url: [base]/Encounter/enc-234
  - resource:
      name:
        - family: Smith
      id: >-
        pat-234
      resourceType: Patient
    search:
      mode: include
    fullUrl: [base]/Patient/pat-234
    link:
      - relation: self
        url: [base]/Patient/pat-234
query-sql:
  - 'SELECT "encounter".* FROM "encounter" LIMIT ? OFFSET ? '
  - 100
  - 0
include-queries:
  - - SELECT * FROM "patient" WHERE (id in (?)) LIMIT ?
    - pat-234
    - 5000
```
{% endtab %}
{% endtabs %}



Or you can request patients and return all Encounter resources that refer to them (by a reverse reference):

```yaml
GET /Patient?_revinclude=Encounter:subject:Patient
```

Aidbox can do the same in a compact way:

```yaml
GET /Patient?_with=Encounter.subject
```

### Distinguish between matched and related resources

An **entry.search.mode** field has a value `match` if the resource is in the search set because it matched the search criteria and has a value `include` if  another resource refers to it.
