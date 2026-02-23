---
description: Permanently delete a Patient and all resources in their compartment using the $purge operation.
---

# $purge

The `$purge` operation permanently deletes a Patient resource and all resources in that patient's compartment, including all historical versions. 

This operation implements the [FHIR Patient Purge](https://build.fhir.org/patient-operation-purge.html) specification. By default, this operation uses the system default compartment for Patient, or a custom compartment can be passed as a parameter.

{% hint style="info" %}
**Idempotent**: Purging a non-existent patient returns 200 (sync) or 202 (async) without error. This allows re-running `$purge` after a partial failure.
{% endhint %}

{% hint style="warning" %}
**Scoped references**: Only resources that reference the patient with the full reference format (e.g., `Patient/<id>`) are deleted. Resources referencing a different resource type with the same ID are not affected.
{% endhint %}

{% hint style="info" %}
**Audit logging**: A successful purge creates an AuditEvent with `action: "E"` (Execute) and `subtype: "$purge"`. See [How to configure audit log](../../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md) for setup instructions.
{% endhint %}

## Endpoint

```
POST /fhir/Patient/<patient-id>/$purge
```

## Parameters

The request body is optional. When provided, it must be a `Parameters` resource with the following optional parameter:

| Parameter                | Type                   | Description                                                                                                                                                                   |
| ------------------------ | ---------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `compartmentDefinition`  | resource (CompartmentDefinition)  | A contained `CompartmentDefinition` resource that defines which resource types and search parameters to use for identifying compartment resources. If omitted, the standard server Patient CompartmentDefinition is used. |


### Async mode

To run the purge asynchronously, include the `Prefer: respond-async` header. In async mode, the Patient is deleted immediately, while compartment resources are deleted in background tasks. The response includes a `Content-Location` header with a URL to check the operation status.

{% hint style="info" %}
The number of concurrent async worker threads is controlled by the [`BOX_SCHEDULER_EXECUTORS`](../../reference/all-settings.md#scheduler-executors) setting (default: 4).
{% endhint %}

## Basic usage

Send a POST request to purge a patient and all associated resources:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Patient/pt-1/$purge
Content-Type: application/json
```
{% endtab %}

{% tab title="Response" %}
**Status**

200 OK

**Body**

```json
{
  "resourceType": "OperationOutcome",
  "id": "informational",
  "issue": [
    {
      "severity": "fatal",
      "code": "informational",
      "diagnostics": "All resources for Patient/pt-1 were purged"
    }
  ]
}
```
{% endtab %}
{% endtabs %}

After the operation completes, the Patient and all resources referencing it (Observations, Conditions, Encounters, etc.) are permanently removed, including their history.

## Async mode

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Patient/pt-1/$purge
Content-Type: application/json
Prefer: respond-async
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted

**Headers**

* `Content-Location` — URL to check purge status (e.g. `/fhir/$async/<operation-id>`)

**Body**

```json
{
  "resourceType": "OperationOutcome",
  "id": "informational",
  "issue": [
    {
      "severity": "fatal",
      "code": "informational",
      "diagnostics": "Purge for Patient/pt-1 accepted for async processing"
    }
  ]
}
```
{% endtab %}
{% endtabs %}

### Check async status

Query the status URL from the `Content-Location` header:

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/$async/<operation-id>
```
{% endtab %}

{% tab title="Response (in progress)" %}
**Status**

202 Accepted
{% endtab %}

{% tab title="Response (completed)" %}
**Status**

200 OK

**Body**

```json
{
  "resourceType": "Bundle",
  "type": "batch-response",
  "entry": [
    {
      "response": {
        "status": "200 OK",
        "outcome": {
          "resourceType": "OperationOutcome",
          "id": "informational",
          "issue": [
            {
              "severity": "fatal",
              "code": "informational",
              "diagnostics": "Operation completed successfully"
            }
          ]
        }
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

### Cancel async operation

To cancel an active async purge, send a DELETE request to the status URL:

{% tabs %}
{% tab title="Request" %}
```http
DELETE /fhir/$async/<operation-id>
```
{% endtab %}

{% tab title="Response" %}
**Status**

202 Accepted
{% endtab %}
{% endtabs %}

## Custom compartment definition

By default, `$purge` uses the standard server Patient CompartmentDefinition. You can provide a custom compartment definition to control exactly which resource types are purged.

For example, to purge only Observations associated with the patient:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Patient/pt-1/$purge
Content-Type: application/json
```

```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "compartmentDefinition",
      "resource": {
        "resourceType": "CompartmentDefinition",
        "url": "http://example.com/custom-compartment",
        "name": "CustomPatientCompartment",
        "code": "Patient",
        "status": "active",
        "search": true,
        "resource": [
          {
            "code": "Observation",
            "param": ["subject"]
          }
        ]
      }
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
**Status**

200 OK

**Body**

```json
{
  "resourceType": "OperationOutcome",
  "id": "informational",
  "issue": [
    {
      "severity": "fatal",
      "code": "informational",
      "diagnostics": "All resources for Patient/pt-1 were purged"
    }
  ]
}
```

Only Observation resources referencing this patient are deleted. Other resource types (Conditions, Encounters, etc.) are left intact.
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
The custom `CompartmentDefinition` must satisfy:

* `code` must be `Patient`
* Must have at least one resource entry with a non-empty `param` list
{% endhint %}
