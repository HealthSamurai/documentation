# Plan API Overview

Plan API enables healthplan's members to consent to have their health data shared with third-party applications. It also allows third-party application owners to connect to provider and pharmacy directories, further referred to as “public non-member specific data.”&#x20;

Aidbox Plan API:

* Enables a developer to register a beneficiary-facing application.
* Uses the HL7 FHIR standard for beneficiary data and the OAuth 2.0 standard for beneficiary authorization.

## Authorization

To use the Plan API OAuth 2.0 a developer has to register an application. An organization has to register as a user by creating a Smart App, validating it in Sandbox and send Production Request for review. A registered application is given a client ID and a client secret. The secret should only be used if it can be kept confidential, such as communication between your server and the respective Plan API.

For insecure implementations, such as mobile apps, PKCE (Proof Key for Code Exchange) is coming soon.

## API permissions and scopes

Access tokens have scopes, which define permissions and the resources that the token can access. Scopes are primarily utilized to determine the type of data an application is requesting. Scopes should be explicitly declared. In case of using wildcard, only supported will be provided.

Note: Any Scope not currently listed is not supported Patient Access scopes:

```
patient/CareTeam.read
patient/Coverage.read
patient/ExplanationOfBenefit.read
patient/Encounter.read
patient/DiagnosticReport.read
patient/MedicationRequest.read
patient/Goal.read
patient/Condition.read
patient/CarePlan.read
```

Provider Directory Access is publicly availiable. Here is the list of supported resource types:

```
Organization
Practitioner
Location
PractitionerRole
HealthcareService
Organization
Affiliation
InsurancePlan
```
