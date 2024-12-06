# SMART on FHIR

{% hint style="info" %}
This functionality is available starting from version 2411.\
The [FHIR Schema Validator Engine](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine) should be enabled.
{% endhint %}

[SMART on FHIR](https://build.fhir.org/ig/HL7/smart-app-launch/) is a framework that describes a set of foundational patterns based on [OAuth 2.0](https://datatracker.ietf.org/doc/html/rfc6749) for client applications to authorize, authenticate, and integrate with FHIR-based data systems.&#x20;

Aidbox provides many SMART on FHIR capabilities, including access control based on SMART on FHIR scopes version 1 and version 2, authorization via SMART App Launch, asymmetric (“private key JWT”) and symmetric (“client secret”) authentication.

**Read the following articles if you need:**

1. Use SMART on FHIR scopes in Aidbox access control and limit access based on scopes.

{% content-ref url="smart-scopes-for-limiting-access.md" %}
[smart-scopes-for-limiting-access.md](smart-scopes-for-limiting-access.md)
{% endcontent-ref %}

2. Launch SMART apps and grant access to users with SMART on FHIR.

{% content-ref url="smart-app-launch.md" %}
[smart-app-launch.md](smart-app-launch.md)
{% endcontent-ref %}

3. Pass Inferno SMART App Launch Test Kit with Aidbox.

{% content-ref url="pass-inferno-tests-with-aidbox.md" %}
[pass-inferno-tests-with-aidbox.md](pass-inferno-tests-with-aidbox.md)
{% endcontent-ref %}

