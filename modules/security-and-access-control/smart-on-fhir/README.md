# SMART on FHIR

{% hint style="info" %}
This functionality is available starting from version 2411.\
The [FHIR Schema Validator Engine](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine) should be enabled.
{% endhint %}

[SMART on FHIR](https://build.fhir.org/ig/HL7/smart-app-launch/) is a framework that describes a set of foundational patterns based on OAuth 2.0 for client applications to authorize, authenticate, and integrate with FHIR-based data systems.&#x20;

Aidbox provides many SMART on FHIR capabilities:

* Limiting access with SMART scopes version 1 and version 2;
* `EHR` and `Standalone` SMART App Launch sequences;
* Support for `public` and `confidential` apps;
* `Asymmetric` (private key JWT) and `symmetric` (client secret) authentication;
* Support `Proof Key for Code Exchange` (PKCE);
* Authorization via SMART Backend Services;

**Read the following articles if you need:**

1. Use SMART on FHIR scopes in Aidbox access control and limit access based on scopes.

{% content-ref url="smart-scopes-for-limiting-access.md" %}
[smart-scopes-for-limiting-access.md](smart-scopes-for-limiting-access.md)
{% endcontent-ref %}

2. Launch SMART apps and grant access to users with SMART on FHIR.

{% content-ref url="smart-app-launch.md" %}
[smart-app-launch.md](smart-app-launch.md)
{% endcontent-ref %}

3. Launch SMART App using external Identity Provider

{% content-ref url="example-smart-app-launch-using-aidbox-and-keycloak.md" %}
[example-smart-app-launch-using-aidbox-and-keycloak.md](example-smart-app-launch-using-aidbox-and-keycloak.md)
{% endcontent-ref %}

3. Pass Inferno SMART App Launch Test Kit with Aidbox.

{% content-ref url="pass-inferno-tests-with-aidbox.md" %}
[pass-inferno-tests-with-aidbox.md](pass-inferno-tests-with-aidbox.md)
{% endcontent-ref %}

