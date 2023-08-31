---
description: WORK IN PROGRESS
---

# View Definition

### Creating flat views

To create a flat view of the resource, you have to define it with a special resource called [view-definition.md](view-definition.md "mention"). You can do it with a View Definitions editor in Aidbox UI.

<figure><img src="../../.gitbook/assets/image (97).png" alt=""><figcaption><p>View Definitions editor</p></figcaption></figure>

View definitions you've created can be viewed in a menu in the right part of the screen. There also are several samples to get you started. Note that samples are presented as View Definitions only and have no corresponding views in the database by default.

You can use the _Run_ button or `Ctrl+Enter` to preview your view. To save a View Definition and materialize it as a view in the database, press _Save_. _Delete_ button deletes both a View Definition and the corresponding view in the database.

You can learn more about View Definition syntax and ways to create View Definitons with REST API on the corresponding page.

### Using flat views

Once you've saved your View Definition, the corresponding flat view will be created in a database in `sof` schema. For example, to select all the rows from a view named `patient_view` you'll need a query like this:

```sql
select * from sof.patient_view
```

Note that in the current version of Aidbox, View Definitions can be materialized as SQL views only.

From here, you can use your flat views however you like. Popular use cases include building complex queries for data analysis and using BI-tools to build dashboards.

### Create View Definition with Aidbox

#### Aidbox UI

1. Go to Database > View Definition
2. Describe view using FHIRPath expressions

{% tabs %}
{% tab title="View Definiton" %}
```json
{
  "name": "patient_view",
  "resource": "Patient",
  "desc": "Patient flat view",
  "select": [
    {
      "name": "id",
      "expr": "id"
    },
    {
      "name": "bod",
      "expr": "birthDate"
    },
    {
      "name": "gender",
      "expr": "gender"
    }
  ]
}
```
{% endtab %}

{% tab title="Flat view" %}
|   |   |   |
| - | - | - |
|   |   |   |
|   |   |   |
|   |   |   |
{% endtab %}
{% endtabs %}

#### REST API

You can create View Definitions using REST API

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /ViewDefinition/patient_view

name: patient_view
resource: '{"name":"patient_view","resource":"Patient","desc":"Patient flat view","select":[{"name":"id","expr":"id"},{"name":"bod","expr":"birthDate"},{"name":"gender","expr":"gender"}]}'
```
{% endtab %}

{% tab title="Response" %}
```yaml
name: patient_view
resource: >-
  {"name":"patient_view","resource":"Patient","desc":"Patient flat
  view","select":[{"name":"id","expr":"id"},{"name":"bod","expr":"birthDate"},{"name":"gender","expr":"gender"}]}
id: >-
  patient_view
resourceType: ViewDefinition
```
{% endtab %}
{% endtabs %}

### Materialize&#x20;

Once you create a view definition you can materialize this view. There're several options:&#x20;

* PostgreSQL view
* Materialize into a table

#### Plain View

#### Aidbox UI

* Click on "Save" button to materialize view definition.

#### REST API

You can materialize a view definition using REST API.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc

method: sof/materialize-view
params:
  view:
    name: patient_view
    resource: Patient
    desc: Patient flat view
    select:
    - name: id
      expr: id
    - name: name
      expr: name
    - name: bod
      expr: birthDate
    - name: gender
      expr: gender
    limit: 100
```
{% endtab %}

{% tab title="Response" %}
```
result:
  status: ok
  sql: >-
    SELECT id as id , jsonb_path_query_first( r.resource , '$  . name' ) #>>
    '{}' as name , jsonb_path_query_first( r.resource , '$  . birthDate' ) #>>
    '{}' as bod , jsonb_path_query_first( r.resource , '$  . gender' ) #>> '{}'
    as gender FROM "patient" as r LIMIT 100
  data:
    - id: pt1
      name: '[{"given": ["John"], "family": "Smith"}]'
      bod: '1976-12-12'
      gender: male
    - id: pt2
      name: '[{"given": ["Ella El"], "family": "Smith"}]'
      bod: '1998-11-22'
      gender: female
    - id: pt3
      name: '[{"given": ["Frodo"], "family": "Baggins"}]'
      bod: '1954-09-22'
      gender: male
    - id: pt4
      name: '[{"given": ["Kot"], "family": "Terex"}]'
      bod: '1978-02-29'
      gender: male
```
{% endtab %}
{% endtabs %}

#### Table
