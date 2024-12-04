# SMART Scopes for Limiting Access

{% hint style="info" %}
This functionality is available starting from version 2411.\
Aidbox should be in [FHIR Schema mode](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine).
{% endhint %}

Aidbox fully supports [version 1 ](https://www.hl7.org/fhir/smart-app-launch/1.0.0/scopes-and-launch-context/index.html)of SMART on FHIR scopes:

<figure><img src="../../../.gitbook/assets/smart-scopes-v1.png" alt=""><figcaption><p>SMART scopes V1</p></figcaption></figure>

&#x20;And support everything except search parameters in [version 2](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html) of SMART on FHIR scopes:

<figure><img src="../../../.gitbook/assets/smart-scope-v2.png" alt=""><figcaption><p>SMART scopes V2</p></figcaption></figure>

If a requested operation is not permitted by the scopes, Aidbox will deny access. If access is granted, Aidbox will retrieve and return only the data allowed by the specified scopes and context.

## Access Token

To enable scope checking in the Access Control layer, the JWT access token must contain the following claims:

| Claim name            | Value type  | Description                                                 |
| --------------------- | ----------- | ----------------------------------------------------------- |
| `atv` \*              | fixed value | <p>Access Token Version<br>Fixed value - <code>2</code></p> |
| `scope`  \*           | valueString | String with scopes separated by space.                      |
| `context.patient`  \* | valueString | Patient ID.                                                 |

&#x20;\* - required claim

For scope checking, Aidbox accepts any valid JWT tokens issued by [external servers](../how-to-guides/token-introspection.md) if they contain the specified scopes and Aidbox can issue its own JWT tokens with all the required claims.

### Example&#x20;

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
GET /fhir/Observation/my-observation
content-type: application/json
accept: application/json
// Token with "patient/Patient.read patient/Appointment.read" scopes
Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6IjNkMGVmYjgwLTkwMTktNDdhMS1iMzYxLWUwNDUzOGQ4NzFmZSIsImlzcyI6Imh0dHBzOi8vYXV0aC5leGFtcGxlLmNvbSIsImV4cCI6MTczMzIzNDk0ODQsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvQXBwb2ludG1lbnQucmVhZCIsImp0aSI6IjUzZWQ1MTZhLTNjODEtNGRjZC05NTUxLTdlOTUzYTkzZmMwZSIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzMyMzQ2NDh9.xE022h0Qi1UaeGO22tiV3Opxg3FqnlauyCXEifaYe4g"
```
{% endtab %}

{% tab title="Response" %}
```json
// Forbidden because the token doesn't have Observation/read scope
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
// Token with "patient/Patient.read patient/Appointment.read" scopes
Authorization: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6IjNkMGVmYjgwLTkwMTktNDdhMS1iMzYxLWUwNDUzOGQ4NzFmZSIsImlzcyI6Imh0dHBzOi8vYXV0aC5leGFtcGxlLmNvbSIsImV4cCI6MTczMzIzNDk0ODQsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvQXBwb2ludG1lbnQucmVhZCIsImp0aSI6IjUzZWQ1MTZhLTNjODEtNGRjZC05NTUxLTdlOTUzYTkzZmMwZSIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzMyMzQ2NDh9.xE022h0Qi1UaeGO22tiV3Opxg3FqnlauyCXEifaYe4g"
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



## Patient data access API

Patient-level access control in Aidbox enables restricting data access to resources associated with a specific patient. When users interact with the FHIR API, they can access only the resources that belong to that patient.

To achieve this behavior, the request must include:

* A valid [JWT access token.](smart-scopes-for-limiting-access.md#access-token)
* Only patient-level scopes ( `patient/...`).
* The patient ID in the `context.patient` claim.

&#x20;Aidbox will limit access and filter retrieved data based on [FHIR Patient CompartmentDefinition](https://hl7.org/fhir/r4/compartmentdefinition-patient.html).
