# `$materialize` operation

{% hint style="info" %}
This functionality is available in Aidbox versions 2508 and later.
{% endhint %}

SQL on FHIR provides the `$materialize` operation to materialize a given ViewDefinition that can either create a table, view, or materialized view.

{% hint style="warning" %}
If Aidbox doesn't run in FHIRSchema mode, the input parameters are not validated.
{% endhint %}

## General syntax

```
POST /fhir/ViewDefinition/<resource-id>/$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": parameter name,
      "resource": resource value
    }
  ]
}
```

## Parameters

- **type**: materialization type.

  This parameter is required.

  Example:

  ```json
  {
    "name": "type",
    "valueCode": "table"
  }
  ```

  Supported materialization types:

  - `"table"` - materialization of ViewDefinition as a table;
  - `"view"` - materialization of ViewDefinition as a view;
  - `"materialized-view"` - materialization of ViewDefinition as a materialized view.

## Schema configuration

By default, Aidbox creates all views using the `sof` schema.
The schema can be changed in Aidbox settings, using the [`db.view-definition-schema`](../../reference/settings/database.md#db.view-definition-schema) setting.

Changing this setting does not affect already materialized views; it applies only to new ones.
It is recommended to use a dedicated schema for ViewDefinition to avoid potential collisions.

## Examples

For example, with the given saved ViewDefinition:

```json
{
  "resourceType": "ViewDefinition",
  "id": "0448a9a8-6114-4a19-aa8e-fc5f60c4d714",
  "name": "patient_view",
  "status": "draft",
  "resource": "Patient",
  "description": "Patient flat view",
  "select": [{
    "column": [{
      "name": "id",
      "path": "id",
      "type": "id"
    }, {
      "name": "bod",
      "path": "birthDate",
      "type": "date"
    }, {
      "name": "gender",
      "path": "gender",
      "type": "code"
    }]
  }]
}
```

Materialization into a table can be performed in the REST console with the following request:

```
POST /fhir/ViewDefinition/0448a9a8-6114-4a19-aa8e-fc5f60c4d714/$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "type",
      "valueCode": "table"
    }
  ]
}
```

Alternatively, it can be materialized as a view:

```
POST /fhir/ViewDefinition/0448a9a8-6114-4a19-aa8e-fc5f60c4d714/$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "type",
      "valueCode": "view"
    }
  ]
}
```

Or a materialized view:

```
POST /fhir/ViewDefinition/0448a9a8-6114-4a19-aa8e-fc5f60c4d714/$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "type",
      "valueCode": "materialized-view"
    }
  ]
}
```

### Using a Bundle

ViewDefinition creation and materialization can be performed with a Bundle:

```
POST /fhir/
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Bundle",
  "type": "transaction",
  "id": "bundle-transaction",
  "entry": [{
    "request": {
      "method": "POST",
      "url": "ViewDefinition"
    },
    "resource": {
      "id": "custom_schema_view_id_bundle",
      "resourceType": "ViewDefinition",
      "resource": "Patient",
      "name": "custom_schema_view_name",
      "status": "draft",
      "select": [{
        "column": [{
          "name": "id",
          "path": "id"
        }, {
          "name": "gender",
          "path": "gender"
        }, {
          "name": "birthdate",
          "path": "birthDate"
        }]
      }]
    }
  }, {
    "request": {
      "method": "POST",
      "url": "ViewDefinition/custom_schema_view_id_bundle/$materialize"
    },
    "resource": {
      "resourceType": "Parameters",
      "parameter": [{
        "name": "type",
        "valueCode": "view"
      }]
    }
  }]
}
```

### Output examples

Successful materialization:

```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "viewName",
      "valueString": "sof.patient_view"
    },
    {
      "name": "viewType",
      "valueString": "table"
    },
    {
      "name": "viewSchema",
      "valueString": "sof"
    }
  ]
}
```

Unsuccessful materialization due to incorrect materialization type:

```json
{
  "resourceType": "OperationOutcome",
  "id": "value",
  "text": {
    "status": "generated",
    "div": "Invalid materialization type"
  },
  "issue": [
    {
      "severity": "fatal",
      "code": "value",
      "diagnostics": "Invalid materialization type",
      "type": "materialized-table",
      "schema": "sof"
    }
  ]
}
```

## Checking view in DB Console

The view created with either of the above examples can be checked in the DB Console with the following query:

```sql
SELECT * FROM sof.patient_view;
```

## Managing created views

Aidbox only creates or updates existing materialized ViewDefinition - refreshing and deletion should be handled manually via PostgreSQL.
Editing and deleting the original ViewDefinition doesn't affect any created views.
