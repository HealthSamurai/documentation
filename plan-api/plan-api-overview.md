# Plan API Overview

Plan API enables healthplan's members to consent to have their health data shared with third-party applications. It also allows third-party application owners to connect to provider and pharmacy directories, further referred to as “public non-member specific data.”&#x20;

Aidbox Plan API:

* Enables a developer to register a beneficiary-facing application.
* Uses the HL7 FHIR standard for beneficiary data and the OAuth 2.0 standard for beneficiary authorization.

## Developer Sandbox

As a SMART app developer, in order to make your SMART application available to healthplan's members, you need to register with the Developer Sandbox. After logging into the Sandbox, create a new application. It will be given a client ID and a client secret. The secret should only be used if it can be kept confidential, such as communication between your server and the respective Plan API.

For insecure implementations, such as mobile apps, PKCE (Proof Key for Code Exchange) is available. To enable PKCE, change the OAuth Type setting when editing your Sandbox SMART app.

Sandbox allows you to test your application from a healthplan beneficiary perspective by using generated clinical resources that are linked to your account. This enables you to launch your app as a patient would, all within the Sandbox. To use this feature, initialize a new synthetic patient from the page where all your apps are. You will see the synthetic resources appear under the 'My Data' tab. When this step is complete, you can launch your SMART application as a healthplan member and perform the OAuth 2.0 Authorization Code Grant flow. In certain scenarios you will be asked to log in, use the credentials from your Sandbox user.

If you believe your app is ready for production, 'Edit' your app and click the 'Send request' button to send your app for review.

## API permissions and scopes

Access tokens have scopes, which define permissions and the resources that the token can access. Scopes are primarily utilized to determine the type of data an application is requesting. Scopes should be explicitly declared. In case of using wildcard, only supported scopes will be provided.

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

Provider Directory Access is publicly available. Here is the list of supported resource types:

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
