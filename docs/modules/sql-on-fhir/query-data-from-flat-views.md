---
description: SQL on FHIR Query data from flat views for querying and analyzing FHIR data with SQL.
---

# Query data from flat views

## Using flat views

Once you've saved your View Definition, the corresponding flat view will be created in a database in `sof` schema. For example, to select all the rows from a view named `patient_view` you'll need a query like this:

```sql
select * from sof.patient_view
```

View Definitions can be materialized as SQL views, tables, or materialized views. See [`$materialize` operation](operation-materialize.md) for details.

From here, you can use your flat views however you like. Popular use cases include building complex queries for data analysis and using BI-tools to build dashboards.

## Real-world examples

### Get all patients diagnosed with COVID after a specified date

ViewDefinitions do not directly implement [joins](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/StructureDefinition-ViewDefinition.html#joins-with-resource-and-reference-keys) across resources. Therefore, to find all patients who was born after or in 1970 and who were diagnosed with COVID after or in 2021, you'll need to define 2 views.

{% tabs %}
{% tab title="Patient View" %}
```json
{
  "name": "patient_view",
  "resource": "Patient",
  "status": "active",
  "description": "Patient flat view",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        },
        {
          "name": "name",
          "path": "name[0].given.join(' ')"
        },
        {
          "name": "birthDate",
          "path": "birthDate"
        }
      ]
    }
  ]
}

```
{% endtab %}

{% tab title="Condition View" %}
```json
{
  "name": "condition_view",
  "resource": "Condition",
  "status": "active",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        },
        {
          "name": "pid",
          "path": "subject.getReferenceKey(Patient)"
        },
        {
          "name": "date",
          "path": "recordedDate"
        }
      ]
    },
    {
      "forEach": "code.coding",
      "column": [
        {
          "name": "code",
          "path": "code"
        },
        {
          "name": "system",
          "path": "system"
        }
      ]
    }
  ]
}

```
{% endtab %}
{% endtabs %}

With these views defined, you can query the information you need with the following query.

{% tabs %}
{% tab title="SQL Query" %}
```sql
select pt.name,
       pt."birthDate",
       cond.date
from sof.patient_view pt
join sof.condition_view cond on cond.pid = pt.id
where pt."birthDate" > '1970-01-01'
  and cond.code = '840539006'
  and cond.system = 'http://snomed.info/sct'
  and cond.date > '2021-01-01'
limit 100;
```
{% endtab %}

{% tab title="Possible output" %}
We use synthetic data for the purpose of this demonstration.

| name      | birthdate  | date                |
| --------- | ---------- | ------------------- |
| Rae Lucie | 2021-02-11 | 2021-03-31T12:50:15 |
{% endtab %}
{% endtabs %}

### All patients who had an encounter in a specified period in a given location

To find all times patients had an encounter in 2020 or later in a location managed by a given organization you'll need to define 3 views.

{% tabs %}
{% tab title="Patient view" %}
```json
{
  "name": "patient_view",
  "resource": "Patient",
  "description": "Patient flat view",
  "status": "active",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        },
        {
          "name": "name",
          "path": "name[0].given.join(' ')"
        }
      ]
    }
  ]
}

```
{% endtab %}

{% tab title="Location view" %}
```json
{
  "name": "location_view",
  "resource": "Location",
  "status": "active",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        },
        {
          "name": "name",
          "path": "name"
        },
        {
          "name": "org_id",
          "path": "managingOrganization.getReferenceKey(Organization)"
        }
      ]
    }
  ]
}

```
{% endtab %}

{% tab title="Encounter view" %}
```json
{
  "name": "encounter_view",
  "resource": "Encounter",
  "status": "active",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "id"
        },
        {
          "name": "subject_id",
          "path": "subject.getReferenceKey(Patient)"
        },
        {
          "name": "location_id",
          "path": "location[0].location.getReferenceKey(Location)"
        },
        {
          "name": "start_time",
          "path": "period.start"
        }
      ]
    }
  ]
}

```
{% endtab %}
{% endtabs %}

Then, your query will look like this.

{% tabs %}
{% tab title="SQL query" %}
```sql
select pt.name,
       loc.name location,
       enc.start_time
from sof.patient_view pt
join sof.encounter_view enc on enc.subject_id = pt.id
join sof.location_view loc on enc.location_id = loc.id
where enc.start_time > '2020-01-01'
  and loc.org_id = '74ab949d-17ac-3309-83a0-13b4405c66aa'
limit 100;
```
{% endtab %}

{% tab title="Possible output" %}
We use synthetic data for the purpose of this demonstration.

| name             | location                    | start\_time         |
| ---------------- | --------------------------- | ------------------- |
| Holli Casey      | Fitchburg Outpatient Clinic | 2021-12-04T16:58:15 |
| Holli Casey      | Fitchburg Outpatient Clinic | 2021-12-04T17:28:14 |
| Lonnie Kristofer | Fitchburg Outpatient Clinic | 2020-12-24T19:54:09 |
{% endtab %}
{% endtabs %}
