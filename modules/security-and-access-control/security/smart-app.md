# SMART on FHIR

SMART Defines Two Patterns For Client _Authorization_

### [**Authorization via SMART App Launch**](http://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html)

Authorizes a user-facing client application (“App”) to connect to a FHIR Server. This pattern allows for “launch context” such as _currently selected patient_ to be shared with the app, based on a user’s session inside an EHR or other health data software, or based on a user’s selection at launch time. Authorization allows for delegation of a user’s permissions to the app itself.

#### Launch App: Standalone Launch

In SMART’s standalone launch flow, a user selects an app from outside the EHR,

#### Launch App: EHR Launch

In SMART’s EHR launch flow, a user has established an EHR session, and then decides to launch an app. This could be a single-patient app (which runs in the context of a patient record), or a user-level app (like an appointment manager or a population dashboard).

### The top-level steps for Smart App Launch are:

1. [Register App with EHR](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-1-register) (_one-time step_, can be out-of-band)
2. Launch App: [Standalone Launch](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-2-launch-standalone) or [EHR Launch](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-2-launch-ehr)
3. [Retrieve .well-known/smart-configuration](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-3-discovery)
4. [Obtain authorization code](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-4-authorization-code)
5. [Obtain access token](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-5-access-token)
6. [Access FHIR API](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-6-fhir-api)
7. [Refresh access token](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html#step-7-refresh)

{% hint style="info" %}
Check[ SMART App launch tutorial](../how-to-guides/smart-on-fhir/smart-of-fhir.md) to launch Smart App locally
{% endhint %}

{% hint style="info" %}
For Inferno compliance test check [Aidbox sample](https://github.com/Aidbox/aidbox-project-samples)
{% endhint %}

### [**Authorization via SMART Backend Services**](http://build.fhir.org/ig/HL7/smart-app-launch/backend-services.html)

Authorizes a headless or automated client application (“Backend Service”) to connect to a FHIR Server. This pattern allows for backend services to connect and interact with an EHR when there is no user directly involved in the launch process.
