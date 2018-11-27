# Custom search

### Intro

FHIR search has a lot of capabilities but some cases couldn't be express in terms of it. For example we want to get a patient resource with additional field encounters, which contains all encounters of that patient, but it's not possible to implement this using FHIR search. Aidbox has a solution for such complex task and this tutorial how to solve this problem.

### Prepare data

We need some sample data to see results of our queries. Let's create it. Copy following snippet to REST Console.

{% hint style="info" %}
Use Copy button near the top right corner of snippet to avoid copying trailing spaces
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```yaml
POST /

type: transaction
entry:
- resource:
    id: patient1
    name:
    - given: [Max]
      family: Turikov
  request:
    method: POST
    url: "/Patient"

- resource:
    id: patient2
    name:
    - given: [Alex]
      family: Antonov
  request:
    method: POST
    url: "/Patient"

- resource:
    id: enc1
    status: draft
    subject:
      resourceType: Patient
      id: patient1
  request:
    method: POST
    url: "/Encounter"
    
- resource:
    id: enc2
    status: draft
    subject:
      resourceType: Patient
      id: patient1
  request:
    method: POST
    url: "/Encounter"

- resource:
    id: enc3
    status: draft
    subject:
      resourceType: Patient
      id: patient2
  request:
    method: POST
    url: "/Encounter"
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 200

id: '281'
type: transaction-response
resourceType: Bundle
entry:
- resource:
    name:
    - given: [Max]
      family: Turikov
    id: patient1
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T09:47:27.412Z'
      versionId: '281'
      tag:
      - {system: 'https://aidbox.app', code: created}
  status: 201
- resource:
    name:
    - given: [Alex]
      family: Antonov
    id: patient2
    resourceType: Patient
    meta:
      lastUpdated: '2018-11-27T09:47:27.412Z'
      versionId: '281'
      tag:
      - {system: 'https://aidbox.app', code: created}
  status: 201
- resource:
    status: draft
    subject: {id: patient1, resourceType: Patient}
    id: enc1
    resourceType: Encounter
    meta:
      lastUpdated: '2018-11-27T09:47:27.412Z'
      versionId: '281'
      tag:
      - {system: 'https://aidbox.app', code: created}
  status: 201
- resource:
    status: draft
    subject: {id: patient1, resourceType: Patient}
    id: enc2
    resourceType: Encounter
    meta:
      lastUpdated: '2018-11-27T09:47:27.412Z'
      versionId: '281'
      tag:
      - {system: 'https://aidbox.app', code: created}
  status: 201
- resource:
    status: draft
    subject: {id: patient2, resourceType: Patient}
    id: enc3
    resourceType: Encounter
    meta:
      lastUpdated: '2018-11-27T09:47:27.412Z'
      versionId: '281'
      tag:
      - {system: 'https://aidbox.app', code: created}
  status: 201
```
{% endtab %}
{% endtabs %}

We created 2 patients and 3 encounters, which linked to those patients.

### SQL

Aidbox uses PostgreSQL \(super advanced open-source DBMS\), which allows to express very complex queries. Let's try to implement our task in SQL queries.

First of all let's try to obtain a list of patients.

{% code-tabs %}
{% code-tabs-item title="patients.sql" %}
```sql
SELECT
id, resource_type, resource
FROM 
patient;
```
{% endcode-tabs-item %}
{% endcode-tabs %}

| id | resource\_type | resource |
| :--- | :--- | :--- |
| `patient1` | `Patient` | `{"name":[{"given":["Max"],"family":"Turikov"}]}` |
| `patient2` | `Patient` | `{"name":[{"given":["Alex"],"family":"Antonov"}]}` |

Next, we want add patient **id** and **resource\_type** into **resource**

{% code-tabs %}
{% code-tabs-item title="patients.sql" %}
```sql
SELECT
id, resource_type, 
	jsonb_set( 
      jsonb_set(resource, '{id}', to_jsonb(id)),
      '{resource_type}', 
      to_jsonb(resource_type))
FROM 
patient;
```
{% endcode-tabs-item %}
{% endcode-tabs %}

| id | resource\_type | resource |
| :--- | :--- | :--- |
| `patient1` | `Patient` | `{"id":"patient1","name":[{"given":["Max"],"family":"Turikov"}],"resource_type":"Patient"}` |
| `patient2` | `Patient` | `{"id":"patient2","name":[{"given":["Alex"],"family":"Antonov"}],"resource_type":"Patient"}` |

Also we can obtain list of encounters for each patient

{% code-tabs %}
{% code-tabs-item title="patients-encounters.sql" %}
```sql
SELECT
p.id AS patient_id,
e.id AS encounter_id,
e.resource AS encounter    
FROM 
patient AS p
JOIN encounter AS e
ON p.id = e.resource->'subject'->>'id';
```
{% endcode-tabs-item %}
{% endcode-tabs %}

| patient\_id | encounter\_id | encounter |
| :--- | :--- | :--- |
| `patient1` | `enc1` | `{"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}` |
| `patient1` | `enc2` | `{"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}` |
| `patient2` | `enc3` | `{"status":"draft","subject":{"id":"patient2","resourceType":"Patient"}}` |

Our next step is obtaining aggregated data by patients

{% code-tabs %}
{% code-tabs-item title="patient-encounters.sql" %}
```sql
SELECT
p.id AS patient_id,
json_agg(e.resource::jsonb) AS encounters 
FROM 
patient AS p
JOIN encounter AS e
ON p.id = e.resource->'subject'->>'id'
GROUP BY p.id;
```
{% endcode-tabs-item %}
{% endcode-tabs %}

| patient\_id | encounters |
| :--- | :--- |
| `patient1` | `[{"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}, {"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}]` |
| `patient2` | `[{"status":"draft","subject":{"id":"patient2","resourceType":"Patient"}}]` |

Looks good, but didn't see information about encounters id

{% code-tabs %}
{% code-tabs-item title="patients-encounters.sql" %}
```sql
SELECT
p.id AS patient_id,
json_agg(e.resource::jsonb) AS encounters
FROM (SELECT id, 
      resource_type,
      jsonb_set(
        jsonb_set(resource, '{id}', to_jsonb(id)),
        '{resource_type}', 
        to_jsonb(resource_type)) 
      AS resource 
      FROM patient) AS p
JOIN (SELECT id, 
      resource_type, 
      jsonb_set(
        jsonb_set(resource, '{id}', to_jsonb(id)), 
        '{resource_type}', 
        to_jsonb(resource_type)) 
      AS resource 
      FROM encounter) AS e
ON p.id = e.resource->'subject'->>'id'
GROUP BY p.id;
```
{% endcode-tabs-item %}
{% endcode-tabs %}

| patient\_id | encounters |
| :--- | :--- |
| `patient1` | `[{"id":"enc1","status":"draft","subject":{"id":"patient1","resourceType":"Patient"},"resource_type":"Encounter"},{"id":"enc2","status":"draft","subject":{"id":"patient1","resourceType":"Patient"},"resource_type":"Encounter"}]` |
| `patient2` | `[{"id":"enc3","status":"draft","subject":{"id":"patient2","resourceType":"Patient"},"resource_type":"Encounter"}]` |

### $query

profit!!!



