# Plan API Overview

Plan API enables a health plan organization’s members to consent to have their health data shared with third-party applications. It also allows third-party application owners to connect to provider and pharmacy directories, further referred to as “public non-member specific data”.

Aidbox Plan API:

* Enables SMART app developers to register a beneficiary-facing application.
* Uses the HL7 FHIR standard for beneficiary data and the OAuth 2.0 standard for beneficiary authorization.

## Developer Sandbox

You are a SMART app vendor aiming to deliver value to the members of a health plan organization by integrating your product? We got you covered. Use our Developer Sandbox to provision your applications to the Member Portal.

### Registration

First of all, let’s set you up an account with the Sandbox. To register, go to the member portal and choose _“Vendor Enrollment to Sandbox”_. You will be redirected to an enrollment form, where you will need to provide some information about you and your organization. When you’re done, click on _“Submit”_. You will receive an email with the link to continue your registration. Follow the link and set the account password. After that is done, you're all set to start using the Developer Sandbox.

### Creating an application

Now that you have a Sandbox account, it is time to create an application. Log into your account using the password you set up during the registration. After you have logged in for the first time, you will see an empty list of applications. Click the _“New Application”_ button to create your first app. You will be taken to a form for the app details. Find the descriptions of the form fields below.

**Client ID**

A unique public identifier for your app. This field is automatically filled.

**Client Secret**

A confidential string used to authenticate your app. This field is automatically filled.

**Application Name**

The public name of your app, which the users will see.

**Client OAuth Type**

Plan API uses OAuth 2.0 Authorization Code grant type to authenticate your app with the FHIR server. Two auth options are available to choose from:&#x20;

* _Authorization Code + Secret_&#x20;
* _Authorization Code + PKCE_

First option is used to tell the auth server to utilize the Client Secret to authenticate your app.&#x20;

{% hint style="danger" %}
Only select _Authorization Code + Secret_ if your app can securely store the Client Secret and keep it confidential.
{% endhint %}

For insecure implementations, such as mobile apps, the PKCE (Proof Key for Code Exchange) flow is available. It utilizes a dynamic secret string to authenticate your app, as opposed to a static one that must be secured by the app itself. It is generally recommended to use PKCE flow when available, so if your app supports it, you probably will always want to use it over a static secret.

{% hint style="info" %}
For more information about the OAuth Authorization Code flow, refer to the [page in the docs](../security-and-access-control-1/auth/authorization-code.md) and the [OAuth 2.0 specification](https://datatracker.ietf.org/doc/html/rfc6749).
{% endhint %}

**Redirect URL**

The URL the authorization server will redirect to after having authorized your app.

**Launch URL**

The URL used to launch the client.

**Logo URL**

Link to the logo of your app.

**Organization/Company Name**

Name of your organization.

**Organization/Company Website URL**

Link to the website of your organization.

**Privacy Policy URL**

Link to your organization’s privacy policy document.

**Security Policy URL**

Link to your organization’s security policy document.

**Terms of Service URL**

Link to the terms of service.

**Description**

A clear and concise description of your app that the users will see.

When you are done filling out the app information, click on _“Save”_ button at the bottom of the form to update your app details.

### SMART Auth and FHIR API sandbox

The Developer Sandbox allows you to test your application from a health plan beneficiary perspective by using synthetic clinical resources. This enables you to launch your app as a health plan Patient would within the Sandbox. To use this feature, you must create synthetic patient resources. Go to the list of your apps and click on _“Initialize Patient”_ button and confirm your action. As a result, a bundle of synthetic FHIR resources will be created and attached. You will be able to view and inspect them under _“My Data”_ tab. These resources will be used in an emulated launch of your application.

Now you are able to launch your SMART application as a Patient, perform the Authorization Code Grant flow and make calls to the FHIR API using your app in the Sandbox. A _“Launch App”_ button will appear on the app page and app list. Make sure you have provided all the necessary information, such as Launch and Redirect URLs.

### Publishing your app on the Member Portal

The final step is promoting your app to the production Member Portal, so that health plan members can take advantage of your product. If you are ready to provision your app to the Member Portal, then it's time to send a Production Request.

To send a Production Request, first click on _“Edit”_ button on the app you wish to publish. On the details form, there is a button that says _"Send Request"_.

{% hint style="warning" %}
Before you commit to promoting your app, please make sure you have entered the correct app details that you intend to use on production.&#x20;
{% endhint %}

Requests with pre-production app configurations should not be sent out. However, if you have sent a request with incorrect app information, you can still update the form with the correct details. Saving the form will result in updating the request as well, as long as it is pending review. You can find the current status of your Production Request under the _“App Status”_ section of the form.

{% hint style="info" %}
Consider the following recommendations to minimize the chances of a Production Request being rejected:

* Make sure you have specified all the required app information;
* Provide links to Terms of Service and policies;
* Provide a clear and transparent description of your app and it's purpose.
{% endhint %}

As soon as your Production Request has been reviewed, you will receive an email on the address you used to register the account with the Developer Sandbox.

If your app has been rejected, the rejection reason will be specified in the email. You will be able to edit your app and send another request.

If your app has been approved, congratulations! It is now available on the Member Portal for the health plan beneficiaries.&#x20;

{% hint style="info" %}
The Client ID and the Client Secret used on production will be the same as the ones used in the sandbox.
{% endhint %}

{% hint style="warning" %}
Do not delete the app from your sandbox after it has been approved for production. Doing so will prevent you from accessing it's security credentials (Client ID and Secret) and updating it in the future.
{% endhint %}

Should you need to update the app details used on production after the app has been approved, go to the _"Edit"_ page and update the necessary information fields. You will need to send a new Production Request for your changes to take effect on production.

## API permissions and scopes

Access tokens have scopes, which define permissions and the resources that the token can access. Scopes are primarily utilized to determine the type of data an application is requesting. Scopes should be explicitly declared. In case of using a wildcard, only supported scopes will be provided.

Refer to the [FHIR API docs](../api-1/fhir-api/search-1/) for guidance.

{% hint style="info" %}
Any scope not currently listed is not supported Patient Access scopes
{% endhint %}

```
openid
fhirUser
profile
offline_access
patient/CareTeam.read
patient/Coverage.read
patient/ExplanationOfBenefit.read
patient/Encounter.read
patient/DiagnosticReport.read
patient/MedicationRequest.read
patient/Goal.read
patient/Condition.read
patient/CarePlan.read
patient/Procedure.read
patient/DocumentReference.read
patient/AllergyIntolerance.read
patient/Immunization.read
patient/Provenance.read
patient/Observation.read
patient/Device.read
patient/Practitioner.read
patient/Organization.read
```

Provider Directory Access is publicly available. Here is the list of supported resource types:

```
Organization
Practitioner
Location
PractitionerRole
HealthcareService
OrganizationAffiliation
InsurancePlan
```

