# Custom Search

## Intro

[FHIR search](https://www.hl7.org/fhir/search.html) has a lot of capabilities, but in some cases, there aren't enough. For example, we want to get a patient resource with an additional field 'encounters' that contains all encounters of that patient, but it's not possible to implement it with FHIR search API. [Aidbox](https://www.health-samurai.io/aidbox) has a solution for such complex tasks, and this tutorial is about how to solve this kind of problem.

## Prepare Data

We need some sample data to see results of our queries. Let's create it using [Batch/Transaction](https://docs.aidbox.app/api-1/transaction).\
Copy the following snippet to the Aidbox.Cloud `REST Console`.

{% tabs %}
{% tab title="Request" %}
````yaml
POST /
Accept: text/yaml
Content-Type: text/yaml

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
    class:
      code: enc1
  request:
    method: POST
    url: "/Encounter"

- resource:
    id: enc2
    status: draft
    subject:
      resourceType: Patient
      id: patient1
    class:
      code: enc2
  request:
    method: POST
    url: "/Encounter"

- resource:
    id: enc3
    status: draft
    subject:
      resourceType: Patient
      id: patient2
    class:
      code: enc3
  request:
    method: POST
    url: "/Encounter"```

</div>

<div data-gb-custom-block data-tag="tab" data-title='Response'>

```yaml
# Status: 200

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
````
{% endtab %}
{% endtabs %}

We created 2 patients and 3 encounters that are linked to those patients.

## SQL

Aidbox uses PostgreSQL (super advanced open-source DBMS), which allows expressing very complex queries. Let's try to implement our task in SQL queries.

First of all, let's try to obtain a list of patients. Access the `DB Console` of our box and run the following code snippets:

![DB Console](<../.gitbook/assets/screenshot-2018-11-27-19.41.13 (1).png>)

{% code title="patients.sql" %}
```sql
SELECT
id, resource_type, resource
FROM 
patient;
```
{% endcode %}

| id         | resource\_type | resource                                           |
| ---------- | -------------- | -------------------------------------------------- |
| `patient1` | `Patient`      | `{"name":[{"given":["Max"],"family":"Turikov"}]}`  |
| `patient2` | `Patient`      | `{"name":[{"given":["Alex"],"family":"Antonov"}]}` |

Next, we want to add the patient **id** and **resource\_type** into **resource**.

{% code title="patients.sql" %}
```sql
SELECT
id, resource_type, 
    jsonb_set( 
      jsonb_set(resource, '{id}', to_jsonb(id)),
      '{resourceType}', 
      to_jsonb(resource_type))
FROM 
patient;
```
{% endcode %}

| id         | resource\_type | resource                                                                                     |
| ---------- | -------------- | -------------------------------------------------------------------------------------------- |
| `patient1` | `Patient`      | `{"id":"patient1","name":[{"given":["Max"],"family":"Turikov"}],"resource_type":"Patient"}`  |
| `patient2` | `Patient`      | `{"id":"patient2","name":[{"given":["Alex"],"family":"Antonov"}],"resource_type":"Patient"}` |

{% hint style="info" %}
Curious how to work with JSON in PostgreSQL? Join our community [chat](https://t.me/aidbox).
{% endhint %}

Also, we can obtain a list of encounters for each patient:

{% code title="patients-encounters.sql" %}
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
{% endcode %}

| patient\_id | encounter\_id | encounter                                                                 |
| ----------- | ------------- | ------------------------------------------------------------------------- |
| `patient1`  | `enc1`        | `{"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}` |
| `patient1`  | `enc2`        | `{"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}` |
| `patient2`  | `enc3`        | `{"status":"draft","subject":{"id":"patient2","resourceType":"Patient"}}` |

Our next step is obtaining aggregated data by patients.

{% code title="patient-encounters.sql" %}
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
{% endcode %}

| patient\_id | encounters                                                                                                                                           |
| ----------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| `patient1`  | `[{"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}, {"status":"draft","subject":{"id":"patient1","resourceType":"Patient"}}]` |
| `patient2`  | `[{"status":"draft","subject":{"id":"patient2","resourceType":"Patient"}}]`                                                                          |

Looks good but there's no information about the encounter id.

{% code title="patients-encounters-with-ids.sql" %}
```sql
SELECT
p.id AS patient_id,
json_agg(e.resource::jsonb) AS encounters
FROM (SELECT id, 
      resource_type,
      jsonb_set(
        jsonb_set(resource, '{id}', to_jsonb(id)),
        '{resourceType}', 
        to_jsonb(resource_type)) 
      AS resource 
      FROM patient) AS p
JOIN (SELECT id, 
      resource_type, 
      jsonb_set(
        jsonb_set(resource, '{id}', to_jsonb(id)), 
        '{resourceType}', 
        to_jsonb(resource_type)) 
      AS resource 
      FROM encounter) AS e
ON p.id = e.resource->'subject'->>'id'
GROUP BY p.id;
```
{% endcode %}

| patient\_id | encounters                                                                                                                                                                                                                          |
| ----------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `patient1`  | `[{"id":"enc1","status":"draft","subject":{"id":"patient1","resourceType":"Patient"},"resource_type":"Encounter"},{"id":"enc2","status":"draft","subject":{"id":"patient1","resourceType":"Patient"},"resource_type":"Encounter"}]` |
| `patient2`  | `[{"id":"enc3","status":"draft","subject":{"id":"patient2","resourceType":"Patient"},"resource_type":"Encounter"}]`                                                                                                                 |

Additionally, we added resourceType and id to the patient resource but didn't use it yet. Let's put encounters to the patient resource and take only one patient by the specified id.

{% code title="patients-with-encounters-and-ids.sql" %}
```sql
SELECT
p.id AS id,
jsonb_set(p.resource, '{encounters}', json_agg(e.resource::jsonb)::jsonb) AS resource
FROM (SELECT id, 
      resource_type,
      jsonb_set(
        jsonb_set(resource, '{id}', to_jsonb(id)),
        '{resourceType}', 
        to_jsonb(resource_type)) 
      AS resource 
      FROM patient) AS p
JOIN (SELECT id, 
      resource_type, 
      jsonb_set(
        jsonb_set(resource, '{id}', to_jsonb(id)), 
        '{resourceType}', 
        to_jsonb(resource_type)) 
      AS resource 
      FROM encounter) AS e
ON p.id = e.resource->'subject'->>'id'
GROUP BY p.id, p.resource
HAVING p.id = 'patient1';
```
{% endcode %}

The result should look like the following table (but without pretty printing):

| id         | resource                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `patient1` | <p><code>{"id":"patient1",</code></p><p><code>"name":[{"given":["Max"],"family":"Turikov"}],</code></p><p><code>"encounters":[</code></p><p><code>{"id":"enc1",</code><br><code>"status":"draft",</code><br><code>"subject":{"id":"patient1","resourceType":"Patient"},</code><br><code>"resourceType":"Encounter"},</code><br><code>{"id":"enc2",</code><br><code>"status":"draft",</code><br><code>"subject":{"id":"patient1","resourceType":"Patient"},</code><br><code>"resourceType":"Encounter"}],</code><br><code>"resourceType":"Patient"}</code></p> |

Now let's make the results of this query accessible via REST API. To do that, we need to create the [`AidboxQuery`](../api-1/fhir-api/search-1/custom-search.md) resource:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /AidboxQuery
Accept: text/yaml
Content-Type: text/yaml

id: patient-with-encounters
params:
  patient-id: {isRequired: true}
query: |
  SELECT
  jsonb_set(p.resource, '{encounters}', json_agg(e.resource::jsonb)::jsonb) AS resource
  FROM (SELECT id, 
        resource_type,
        jsonb_set(
          jsonb_set(resource, '{id}', to_jsonb(id)),
          '{resourceType}', 
          to_jsonb(resource_type)) 
        AS resource 
        FROM patient) AS p
  JOIN (SELECT id, 
        resource_type, 
        jsonb_set(
          jsonb_set(resource, '{id}', to_jsonb(id)), 
          '{resourceType}', 
          to_jsonb(resource_type)) 
        AS resource 
        FROM encounter) AS e
  ON p.id = e.resource->'subject'->>'id'
  GROUP BY p.id, p.resource
  HAVING p.id = {{params.patient-id}};
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 201

query: >-
  SELECT

  jsonb_set(p.resource, '{encounters}', json_agg(e.resource::jsonb)::jsonb) AS
  resource

  FROM (SELECT id, 
        resource_type,
        jsonb_set(
          jsonb_set(resource, '{id}', to_jsonb(id)),
          '{resourceType}', 
          to_jsonb(resource_type)) 
        AS resource 
        FROM patient) AS p
  JOIN (SELECT id, 
        resource_type, 
        jsonb_set(
          jsonb_set(resource, '{id}', to_jsonb(id)), 
          '{resourceType}', 
          to_jsonb(resource_type)) 
        AS resource 
        FROM encounter) AS e
  ON p.id = e.resource->'subject'->>'id'

  GROUP BY p.id, p.resource

  HAVING p.id = {{params.patient-id}};
params:
  patient-id:
    isRequired: true
id: >-
  patient-with-encounters
resourceType: AidboxQuery
meta:
  lastUpdated: '2022-07-08T12:15:55.520339Z'
  createdAt: '2022-07-08T12:15:55.520339Z'
  versionId: '21'
```
{% endtab %}
{% endtabs %}

Pay attention to the end of the query: we used `{{params.patient-id}}` which takes the value from the request and passes it to the query securely (using PostgreSQL `PREPARE` statement). This means that the user of our custom search can change some parameters of the query and get different results.

Let's try it in action!

{% tabs %}
{% tab title="Request" %}
```
GET /$query/patient-with-encounters?patient-id=patient1
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 200

data:
- resource:
    id: patient1
    name:
    - given: [Max]
      family: Turikov
    encounters:
    - id: enc1
      status: draft
      subject: {id: patient1, resourceType: Patient}
      resourceType: Encounter
    - id: enc2
      status: draft
      subject: {id: patient1, resourceType: Patient}
      resourceType: Encounter
    resourceType: Patient
# ...
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}
```
GET /$query/patient-with-encounters?patient-id=patient2
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 200

data:
- resource:
    id: patient2
    name:
    - given: [Alex]
      family: Antonov
    encounters:
    - id: enc3
      status: draft
      subject: {id: patient2, resourceType: Patient}
      resourceType: Encounter
    resourceType: Patient
# ...
```
{% endtab %}
{% endtabs %}

We got all the needed data in the exact shape we wanted. Additional information about custom queries can be found in REST API [$query](../api-1/fhir-api/search-1/custom-search.md) documentation.

{% hint style="info" %}
Want to know more about Aidbox, FHIR, and custom search? Join our community [chat](https://t.me/aidbox).
{% endhint %}
