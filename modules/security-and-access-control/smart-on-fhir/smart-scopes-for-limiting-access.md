# SMART Scopes for Limiting Access

{% hint style="danger" %}
WORK IN PROGRESS
{% endhint %}

{% hint style="info" %}
This functionality is available starting from version 2411 and only in&#x20;
{% endhint %}

Aidbox supports SMART on FHIR scopes [version 1 ](https://www.hl7.org/fhir/smart-app-launch/1.0.0/scopes-and-launch-context/index.html)and [version 2](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html). To activate checking scopes in the Access Control layer, JWT access token has to contain the following claims:

| Claim name            | Value type  | Description                                                 |
| --------------------- | ----------- | ----------------------------------------------------------- |
| `atv` \*              | fixed value | <p>Access Token Version<br>Fixed value - <code>2</code></p> |
| `scope`  \*           | valueString | String with scopes separated by space.                      |
| `context.patient`  \* | valueString | Patient ID.                                                 |

&#x20;\* - required claim

### Example&#x20;

Parsed valid JWT access token

```json
{
  "atv": 2,
  "aud": "https://example.edge.aidbox.app/fhir",
  "sub": "3d0efb80-9019-47a1-b361-e04538d871fe",
  "iss": "https://example.edge.aidbox.app",
  "exp": 1733234948,
  "scope": "launch/patient openid fhirUser offline_access patient/Condition.read patient/Observation.read patient/Patient.read",
  "jti": "53ed516a-3c81-4dcd-9551-7e953a93fc0e",
  "context": {
    "patient": "my-patient-id"
  },
  "iat": 1733234648
}
```

## Patient data access API

For requests with patient scopes (`patient/...`) Aidbox limits access and filters retrieved data based on [FHIR Patient CompartmentDefinition](https://hl7.org/fhir/r4/compartmentdefinition-patient.html).
