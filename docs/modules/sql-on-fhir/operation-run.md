# $run operation

{% hint style="info" %}
This functionality is available in Aidbox versions 2507 and later.
{% endhint %}

SQL on FHIR provides the `$run` operation to run a given ViewDefinition.
It can evaluate the provided or stored ViewDefinition on provided or stored resources.

SQL on FHIR specification [defines $run operation](https://sql-on-fhir.org/ig/latest/operations-run.html).

## General syntax
```
POST /fhir/ViewDefinition/$run
Content-Type: application/json
Accept: preferred format

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": parameter name,
      "resource": resource value
    },
    {
      "name": parameter name,
      "valueReference": FHIR reference
    },
    ...
  ]
```

## Parameters

- **viewReference**: reference to the ViewDefinition resource.
  
  This parameter is exclusive with the `viewResource` parameter.
  
  Example:
  ```json
  {
    "name": "viewReference",
    "valueReference": "ViewDefinition/patient-view"
  }
  ```

- **viewResource**: provided viewDefinition resource.
  
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

- **resource**: provided resources to run ViewDefinition on.

  If this parameter is specified, the server runs ViewDefinition
  only on this resource, and not on a stored resources.
  Conversely, if this parameter is omitted, the server runs ViewDefinition
  on stored resources.

  Example:
  ```json
  {
    "name": "resource",
    "resource": {
      "resourceType": "Patient",
      "active": true
    }
  }
  ```

  To provide multiple resources, provide this parameter multiple times.

- **group**: use only resources which are members of the specified group

  Filters all input resources, and run ViewDefinition only on those resources,
  which are the members of the specified group.

  Example:
  ```json
  {
    "name": "group",
    "valueReference": {
      "reference": "Group/my-group"
    }
  }
  ```

- **patient**: use only resources which are related to the specified patient.

  Filter all input resources and run ViewDefinition only on those resources,
  which are members of the patient compartment for the specified patient.

  Example:
  ```json
  {
    "name": "patient",
    "valueReference": {
      "reference": "Patient/patient-1"
    }
  }
  ```

- **_since**: use only resources updated later than the specified date

  Filter all input resources and run ViewDefinition only on those resources,
  which were modified (meta.lastModified) since the specified date

  Example:
  ```json
  {
    "name": "_since",
    "valueInstant": "2020-02-25T17:59:02.812Z"
  }
  ```

- **_format**: output format (json/ndjson/csv)

  Return response in the given format.
  Allowed values: `json`, `ndjson`, `csv`.
  
  Example:
  ```json
  {
    "name": "_format",
    "valueCode": "json"
  }
  ```

- **_limit**: Maximum number of returned rows

  Limit the number of the returned rows to the given value.
  Note that it limits the number of the returned rows,
  not the number of input resources.

  Example:
  ```json
  {
    "name": "_limit",
    "valueInteger": 1
  }
  ```

## Output format (content negotiation)
Depending on combination of the `Accept` header and `_format` parameter,
the returned type is different

By default the response is wrapped in the Binary resource. But if Accept
specifies the media type specified by format, the server doesn't wrap
response in Binary.

For example, if you specify `Accept: application/fhir+json` and `_format`: JSON,
the server will return Binary resource containing the returned payload.

If you specify `Accept: application/json` and `_format`: JSON, the server
will return the processed data (without wrapping inside Binary resource).

## Examples

Provided resources and ViewDefinition:
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "_format",
      "valueCode": "json"
    },
    {
      "name": "viewResource",
      "resource": {
        "id": "view-1",
        "resourceType": "ViewDefinition",
        "status   ": "draft",
        "resource ": "Patient",
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
    },
    {
      "name": "resource",
      "resource": {
        "resourceType": "Patient",
        "id": "source-1"
      }
    },
    {
      "name": "resource",
      "resource": {
        "resourceType": "Patient",
        "id": "source-2"
      }
    }
  ]
}
```

Provided resources, stored ViewDefinition:
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "_format",
      "valueCode": "json"
    },
    {
      "name": "viewReference",
      "viewReference": {
        "reference": "ViewDefinition/patient"
      }
    },
    {
      "name": "resource",
      "resource": {
        "resourceType": "Patient",
        "id": "source-1"
      }
    },
    {
      "name": "resource",
      "resource": {
        "resourceType": "Patient",
        "id": "source-2"
      }
    }
  ]
}
```

Stored resources, stored ViewDefinition:
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "_format",
      "valueCode": "json"
    },
    {
      "name": "viewReference",
      "viewReference": {
        "reference": "ViewDefinition/patient"
      }
    }
  ]
}
```

## Output examples
With `Accept: application/json` and `_format`: JSON

```json
[
  {
    "patient_id": "source-1"
  },
  {
    "patient_id": "source-2"
  }
]

```

With `Accept: application/fhir+json` and `_format`: JSON
```json
{
  "resourceType": "Binary",
  "contentType": "application/json",
  "data": "WwogIHsKICAgICJwYXRpZW50X2lkIjogInNvdXJjZS0xIgogIH0sCiAgewogICAgInBhdGllbnRfaWQiOiAic291cmNlLTIiCiAgfQpdCg=="
}
```
