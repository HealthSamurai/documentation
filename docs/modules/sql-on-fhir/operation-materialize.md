---
description: Materializing SQL-on-FHIR ViewDefinitions as database tables, views, or materialized views
---
# `$materialize` operation

{% hint style="warning" %}
`$materialize` is not part of the official FHIR or SQL on FHIR specifications. This is a custom operation implemented by Aidbox, and its API may be subject to changes in future versions.
{% endhint %}

{% hint style="warning" %}
When running Aidbox not in FHIRSchema mode, please be aware that input parameters for the `$materialize` operation are not validated against FHIR specifications.
{% endhint %}

{% hint style="info" %}
This functionality is available in Aidbox versions 2508 and later.
{% endhint %}

SQL on FHIR provides the `$materialize` operation to transform a ViewDefinition into a concrete database object. This operation can create a table, view, or materialized view in the database based on the ViewDefinition structure.

## General syntax

To call the `$materialize` operation, use the following request format:

```
POST /fhir/ViewDefinition/[<resource-id>/]$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": parameter name,
      "resource": resource value
    },
    ...
  ]
}
```

The `ViewDefinition` resource should be specified by:

- URL (overrid parameter),
- `viewResource` or `viewReference` parameter (mutually exclusive).

## Parameters

* **viewReference**: reference to the ViewDefinition resource.

    This parameter is exclusive with the `viewResource` parameter.

    Example:

    ```json
    {
      "name": "viewReference",
      "valueReference": "ViewDefinition/patient-view"
    }
    ```

* **viewResource**: provided viewDefinition resource.

    This parameter is exclusive with the `viewReference` parameter.

    Example:

    ```json
    {
      "name": "viewResource",
      "resource": {
        "id": "vd-1",
        "resourceType": "ViewDefinition",
        "status": "draft",
        "resource": "Patient",
        "select": [
          {
            "column": [
              {
                "name": "patient_id",
                "path": "getResourceKey()"
              }
            ]
          }
        ]
      }
    }
    ```

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

  - `"view"` - materialization of ViewDefinition as a view
  - `"materialized-view"` - materialization of ViewDefinition as a materialized view
  - `"table"` - materialization of ViewDefinition as a table.

## Schema configuration

By default, Aidbox creates all materialized ViewDefinitions in the `sof` schema.
This schema can be configured in Aidbox settings through the [`db.view-definition-schema`](../../reference/settings/database.md#db.view-definition-schema) setting.

Please note that changing this setting only affects new materializations and does not retroactively change already materialized database objects.
We recommend using a dedicated schema for ViewDefinition materializations to prevent naming conflicts with other database objects.

## Examples

For example, with the given saved ViewDefinition:

```json
{
  "resourceType": "ViewDefinition",
  "id": "0448a9a8-6114-4a19-aa8e-fc5f60c4d714",
  "name": "patient_view",
  "resource": "Patient",
  "select": [{
    "column": [{
      "name": "id",
      "path": "id",
      "type": "id"
    }, {
      "name": "bod",
      "path": "birthDate",
      "type": "date"
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
POST /fhir/ViewDefinition/$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "viewReference",
      "valueReference": {
        "reference": "ViewDefinition/0448a9a8-6114-4a19-aa8e-fc5f60c4d714"
      }
    },
    {
      "name": "type",
      "valueCode": "view"
    }
  ]
}
```

Or a materialized view:

```
POST /fhir/ViewDefinition/$materialize
Content-Type: application/json
Accept: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "viewResource",
      "resource": {
        "resourceType": "ViewDefinition",
        "id": "0448a9a8-6114-4a19-aa8e-fc5f60c4d714",
        "name": "patient_view",
        "resource": "Patient",
        "select": [{
          "column": [{
            "name": "id",
            "path": "id",
            "type": "id"
          }, {
            "name": "bod",
            "path": "birthDate",
            "type": "date"
          }]
        }]
      }
    },
    {
      "name": "type",
      "valueCode": "materialized-view"
    }
  ]
}
```

### Using a Bundle

ViewDefinition creation and materialization can be performed with a Bundle, making it ideal for inclusion in an InitBundle for automated setup during Aidbox initialization:

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

## Checking Materialized ViewDefinition in the DB Console

After materializing a ViewDefinition using any of the methods described above, you can verify its creation and query its data using the DB Console with the following SQL query:

```sql
SELECT * FROM sof.patient_view;
```

This query retrieves all records from the materialized view, table, or database view that was created in the 'sof' schema (or your configured schema).

## Managing created views

Aidbox only creates or updates existing materialized ViewDefinition objects in the database. The refreshing of materialized views and deletion of any created database objects (tables, views, or materialized views) must be handled manually using PostgreSQL commands. Note that editing or deleting the original ViewDefinition resource in Aidbox does not automatically affect any database objects that were previously created from that definition.
