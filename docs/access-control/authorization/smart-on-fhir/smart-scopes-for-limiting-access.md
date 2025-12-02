# SMART: Scopes for Limiting Access

{% hint style="info" %}
This functionality is available starting from version 2411.\
The [FHIR Schema Validator Engine](../../../modules/profiling-and-validation/fhir-schema-validator/) must be enabled.
{% endhint %}

Aidbox fully supports [version 1 ](https://www.hl7.org/fhir/smart-app-launch/1.0.0/scopes-and-launch-context/index.html)of SMART on FHIR scopes:

<figure><img src="../../../.gitbook/assets/smart-scopes-v1.png" alt="SMART scopes version 1 syntax showing scope patterns for user, patient, and system contexts"><figcaption><p>SMART scopes V1</p></figcaption></figure>

And [version 2](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html) of SMART on FHIR scopes with partial support of [search parameters](smart-scopes-for-limiting-access.md#scopes-with-search-parameters) in scopes:

<figure><img src="../../../.gitbook/assets/scope_v2.jpg" alt="SMART scopes version 2 syntax showing enhanced scope patterns with granular permissions"><figcaption></figcaption></figure>

If a requested operation is not permitted by the scopes, Aidbox will deny access. If access is granted, Aidbox will retrieve and return only the data allowed by the specified scopes and context.

## Scopes with search parameters

{% hint style="info" %}
This functionality is available starting from version 2509.
{% endhint %}

SMART on FHIR v2 supports [finer-grained access control](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html#finer-grained-resource-constraints-using-search-parameters) by allowing FHIR search parameters to be embedded in scopes. In Aidbox, you can append a query string to a scope to restrict what a client can read/search.

### Example

`patient/Observation.rs?status=final` - Grants read & search access only to `Observation` resources whose `status` is `final`. Any request such as `GET /fhir/Observation` (or other Observation searches) will be automatically filtered to include only `status=final` results.

You can combine as many search parameters and scopes as you want using FHIR search syntax, except for complex search parameters like `_include`, `_revinclude`, `_has`, `_assoc`, `_with`.

Also, Aidbox **forbids** using search parameters in scopes with `create/update/delete` (cud) permissions.

## Access Token

To enable scope checking in the Access Control layer, the JWT access token must contain the following claims:

| Claim name        | Value type  | Description                                                 |
| ----------------- | ----------- | ----------------------------------------------------------- |
| `atv` \*          | fixed value | <p>Access Token Version<br>Fixed value - <code>2</code></p> |
| `scope` \*        | valueString | String with scopes separated by space.                      |
| `context.patient` | valueString | Patient ID.                                                 |

\* - required claim

For scope checking, Aidbox accepts any valid JWT tokens issued by [external servers](../../../tutorials/security-access-control-tutorials/set-up-token-introspection.md) if they contain the specified scopes and Aidbox can issue its own JWT tokens with all the required claims.

### Example

Parsed valid JWT access token:

```json
{
  "atv": 2,
  "aud": "https://example.edge.aidbox.app/fhir",
  "sub": "3d0efb80-9019-47a1-b361-e04538d871fe",
  "iss": "https://example.edge.aidbox.app",
  "exp": 1733234948,
  "scope": "launch/patient openid fhirUser offline_access patient/Patient.read patient/Appointment.read",
  "jti": "53ed516a-3c81-4dcd-9551-7e953a93fc0e",
  "context": {
    "patient": "my-patient-id"
  },
  "iat": 1733234648
}
```

Denied request based on allowed scopes:

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/Appointment/my-appointment
content-type: application/json
accept: application/json
// Token with "patient/Patient.read patient/Observation.read" scopes
Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6IjNkMGVmYjgwLTkwMTktNDdhMS1iMzYxLWUwNDUzOGQ4NzFmZSIsImlzcyI6Imh0dHBzOi8vYXV0aC5leGFtcGxlLmNvbSIsImV4cCI6MTczMzIzNDk0ODQsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvT2JzZXJ2YXRpb24ucmVhZCIsImp0aSI6IjUzZWQ1MTZhLTNjODEtNGRjZC05NTUxLTdlOTUzYTkzZmMwZSIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzMyMzQ2NDh9.PsYalqkaN-6V0tBqLn_9pkDrR0cLmEg237W8xz5Ymdo"
```
{% endtab %}

{% tab title="Response" %}
```json
// Forbidden because the token doesn't have Appointment/read scope
{
  "resourceType": "OperationOutcome",
  "id": "forbidden",
  "text": {
    "status": "generated",
    "div": "Forbidden"
  },
  "issue": [
    {
      "severity": "fatal",
      "code": "forbidden",
      "diagnostics": "Forbidden"
    }
  ]
}
```
{% endtab %}
{% endtabs %}

Permitted request based on allowed scopes:

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/Patient/test-pt-1
content-type: application/json
accept: application/json
// Token with "patient/Patient.read patient/Observation.read" scopes
Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6IjNkMGVmYjgwLTkwMTktNDdhMS1iMzYxLWUwNDUzOGQ4NzFmZSIsImlzcyI6Imh0dHBzOi8vYXV0aC5leGFtcGxlLmNvbSIsImV4cCI6MTczMzIzNDk0ODQsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvT2JzZXJ2YXRpb24ucmVhZCIsImp0aSI6IjUzZWQ1MTZhLTNjODEtNGRjZC05NTUxLTdlOTUzYTkzZmMwZSIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzMyMzQ2NDh9.PsYalqkaN-6V0tBqLn_9pkDrR0cLmEg237W8xz5Ymdo"
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK because the token has Patient/read scope
{
  "name": [
    {
      "given": [
        "Amy",
        "V."
      ],
      "family": "Shaw",
      "period": {
        "end": "2020-07-22",
        "start": "2016-12-06"
      }
    }
  ],
  "birthDate": "1987-02-20",
  "resourceType": "Patient",
  "active": true,
  "id": "test-pt-1",
  "gender": "female",
  "birthsex": "F"
}
```
{% endtab %}
{% endtabs %}

## Bundle

SMART does not define specific scopes for [batch or transaction](https://hl7.org/fhir/smart-app-launch/scopes-and-launch-context.html#batches-and-transactions) interactions. Aidbox allows Bundle requests regardless of scopes and applies Access Control restrictions to each element within `Bundle.entry`. This means that while the Bundle as a whole is accepted, Aidbox enforces scope Access Control restrictions on each entry in the Bundle.

### Example

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir
content-type: application/json
accept: application/json
// Token with "patient/Patient.read patient/Observation.read" scopes
Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6IjNkMGVmYjgwLTkwMTktNDdhMS1iMzYxLWUwNDUzOGQ4NzFmZSIsImlzcyI6Imh0dHBzOi8vYXV0aC5leGFtcGxlLmNvbSIsImV4cCI6MTczMzIzNDk0ODQsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvT2JzZXJ2YXRpb24ucmVhZCIsImp0aSI6IjUzZWQ1MTZhLTNjODEtNGRjZC05NTUxLTdlOTUzYTkzZmMwZSIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzMyMzQ2NDh9.PsYalqkaN-6V0tBqLn_9pkDrR0cLmEg237W8xz5Ymdo"

{
  "resoruceType": "Bundle",
  "type": "batch",
  "entry": [
    {
      "request": {
        "method": "GET",
        "url": "Encounter"
      }
    },
    {
      "request": {
        "method": "GET",
        "url": "Patient/test-pt-1"
      }
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK
{
  "type": "batch-response",
  "resourceType": "Bundle",
  "entry": [
    // first entry is Forbidden because token doesn't have
    // patient.Encounter/read scope
    {
      "resource": {
        "resourceType": "OperationOutcome",
        "id": "forbidden",
        "text": {
          "status": "generated",
          "div": "Forbidden"
        },
        "issue": [
          {
            "severity": "fatal",
            "code": "forbidden",
            "diagnostics": "Forbidden"
          }
        ]
      },
      "response": {
        "status": "403"
      }
    },
    // second entry is allowed because 
    // token has patient/Patient.read scope
    {
      "resource": {
        "name": [
          {
            "given": [
              "Amy",
              "V."
            ],
            "family": "Shaw",
            "period": {
              "end": "2020-07-22",
              "start": "2016-12-06"
            }
          }
        ],
        "birthDate": "1987-02-20",
        "resourceType": "Patient",
        "active": true,
        "id": "test-pt-1",
        "gender": "female",
        "birthsex": "F"
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

## Patient data access API

Patient-level access control in Aidbox enables restricting data access to resources associated with a specific patient. When users interact with the FHIR API, they can access only the resources that belong to that patient.

To achieve this behavior, the request must include:

* A valid [JWT access token.](smart-scopes-for-limiting-access.md#access-token)
* Only patient-level scopes ( `patient/...`).
* The patient ID in the `context.patient` claim.

Aidbox will limit access and filter retrieved data based on [FHIR Patient CompartmentDefinition](https://hl7.org/fhir/r4/compartmentdefinition-patient.html).

### Example

{% tabs %}
{% tab title="Request" %}
```json
// Search over all Observations
GET /fhir/Observation
content-type: application/json
accept: application/json
// Token with "patient/Observation.read" scope and "contest.patient" = "test-pt-1"
Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6IjNkMGVmYjgwLTkwMTktNDdhMS1iMzYxLWUwNDUzOGQ4NzFmZSIsImlzcyI6Imh0dHBzOi8vYXV0aC5leGFtcGxlLmNvbSIsImV4cCI6MTczMzIzNDk0ODQsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvT2JzZXJ2YXRpb24ucmVhZCIsImp0aSI6IjUzZWQ1MTZhLTNjODEtNGRjZC05NTUxLTdlOTUzYTkzZmMwZSIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzMyMzQ2NDh9.PsYalqkaN-6V0tBqLn_9pkDrR0cLmEg237W8xz5Ymdo"
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK Return Observation only 
// with reference to "test-pt-1" Patient (from "contest.patient" claim)

{
  "resourceType": "Bundle",
  "type": "searchset",
  "total": 3,
  "entry": [
    {
      "resource": {
        "resourceType": "Observation",
        "id": "test-blood-pressure",
        "status": "final",
        "code": {
          "text": "Blood pressure systolic and diastolic",
          "coding": [
            {
              "code": "85354-9",
              "system": "http://loinc.org",
              "display": "Blood pressure panel with all children optional"
            }
          ]
        },
        "subject": {
          "reference": "Patient/test-pt-1"
        }
      }
    },
    {
      "resource": {
        "resourceType": "Observation",
        "id": "test-heart-rate",
        "status": "final",
        "code": {
          "text": "heart_rate",
          "coding": [
            {
              "code": "8867-4",
              "system": "http://loinc.org",
              "display": "Heart Rate"
            }
          ]
        },
        "subject": {
          "reference": "Patient/test-pt-1"
        }
      }
    },
    {
      "resource": {
        "resourceType": "Observation",
        "id": "test-height",
        "status": "final",
        "code": {
          "text": "height",
          "coding": [
            {
              "code": "8302-2",
              "system": "http://loinc.org",
              "display": "Body height"
            }
          ]
        },
        "subject": {
          "reference": "Patient/test-pt-1"
        }
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}
