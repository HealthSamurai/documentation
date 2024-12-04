# SMART Scopes for Limiting Access

{% hint style="danger" %}
WORK IN PROGRESS
{% endhint %}

{% hint style="info" %}
This functionality is available starting from version 2411.\
Aidbox should be in [FHIR Schema mode](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine).
{% endhint %}

Aidbox supports both [version 1 ](https://www.hl7.org/fhir/smart-app-launch/1.0.0/scopes-and-launch-context/index.html)and [version 2](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html) of SMART on FHIR scopes. If a requested operation is not permitted by the scopes, Aidbox will deny access. If access is granted, Aidbox will retrieve and return only the data allowed by the specified scopes and context.

## Access Token

To enable scope checking in the Access Control layer, the JWT access token must contain the following claims:

| Claim name            | Value type  | Description                                                 |
| --------------------- | ----------- | ----------------------------------------------------------- |
| `atv` \*              | fixed value | <p>Access Token Version<br>Fixed value - <code>2</code></p> |
| `scope`  \*           | valueString | String with scopes separated by space.                      |
| `context.patient`  \* | valueString | Patient ID.                                                 |

&#x20;\* - required claim

For scope checking, Aidbox accepts any [valid JWT tokens](../how-to-guides/token-introspection.md) issued by external servers if they contain the specified scopes and Aidbox can issue its own JWT tokens with all the required claims.

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

Patient-level access control in Aidbox enables restricting data access to resources associated with a specific patient. When users interact with the FHIR API, they can access only the resources that belong to that patient.

To achieve this behavior, the request must include:

* A valid [JWT access token.](smart-scopes-for-limiting-access.md#access-token)
* Only patient-level scopes ( `patient/...`).
* The patient ID in the `context.patient` claim.

&#x20;Aidbox will limit access and filter retrieved data based on [FHIR Patient CompartmentDefinition](https://hl7.org/fhir/r4/compartmentdefinition-patient.html).
