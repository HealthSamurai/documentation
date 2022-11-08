---
description: >-
  This guide explains how to pass the Inferno Visual Inspection and Attestation
  sequence
---

# Pass Inferno Visual Inspection and Attestation

{% hint style="warning" %}
Currently this guide is in progress. It is going to be updated with all the attestations
{% endhint %}

## 9.10.01 Health IT Module demonstrated support for application registration for single patients

Provided by certification buddy `Tests steps` require demonstrating the process of application registration for single patients. You should switch to the `Yes` option.

{% hint style="info" %}
Smartbox supports [several ways](../background-information/adding-clients-for-inferno-tests.md) to register SMART applications
{% endhint %}

## 9.10.02 Health IT Module demonstrated support for application registration for multiple patients

Provided by certification buddy `Tests steps` require demonstrating the process of application registration for multiple patients. You should switch to the `Yes` option.

{% hint style="info" %}
Smartbox supports [several ways](../background-information/adding-clients-for-inferno-tests.md) to register SMART applications
{% endhint %}

## 9.10.03 Health IT Module demonstrated a graphical user interface for user to authorize FHIR resources

During the test sessions, Smartbox shows users the `Consent screen`. The `Consent screen` is the graphical user interface.

## 9.10.04 Health IT Module informed patient when "offline\_access" scope is being granted during authorization

During the test sessions, Smartbox shows users the `Consent screen`. There is the `Offline access` option on the `Consent screen`.

## 9.10.05 Health IT Module attested that it is capable of issuing refresh tokens that are valid for a period of no shorter than three months

Default `refresh_token` lifespan is unlimited.

{% hint style="info" %}
To reduce the refresh\_token lifespan use [refresh\_token\_expiration documentation](../../../security-and-access-control-1/auth/authorization-code.md#configure-client)
{% endhint %}

## 9.10.07 Tester verifies that all information is accurate and without omission

Switch to the `Yes` option. If something is lost, the tester says it.

## 9.10.09 Health IT developer demonstrated the documentation is available at a publicly accessible URL

Smartbox has a documentation page. The address of the page is `https://example.com/documentation`

{% hint style="info" %}
Here `https://example.com` is the Smartbox domain
{% endhint %}

## 9.10.10 Health IT developer confirms the Health IT module does not cache the JWK Set received via a TLS-protected URL for longer than the cache-control header received by an application indicates

It is an attestation. You should state Smartbox never caches `JWK` sets it receives during the token validations.

## 9.10.11 Health IT developer demonstrates support for the Patient Demographics Suffix USCDI v1 element

To demonstrate supporting of the `Suffix`

1. Open the result of the `1.7.02 Access to Patient resources granted` test case
2. Press the `Details` button
3. Scroll down to the `name` array of the fetched `Patient` resource
4. See the `Suffix` property inside one `name` element

## 9.10.12 Health IT developer demonstrates support for the Patient Demographics Previous Name USCDI v1 element

To demonstrate supporting of the `Previous Name`

1. Open the result of the `1.7.02 Access to Patient resources granted` test case
2. Press the `Details` button
3. Scroll down to the `name` property of the fetched `Patient` resource
4. There are two items in the `name` array
   1. First `name` has `period.end` property. It means that `name` is the `previous` one
   2. Second `name` has no `period.end` property. That `name` is `current` one

## 9.10.13 Health IT developer demonstrates support for issuing refresh tokens to native applications

Native applications can register `custom URL schemas`. Instead of the ordinary `http://` prefix the URL of the native application can start with anything else. For example, `my-awesome-smart-app://` is a legal custom URL schema.

In terms of `SMART App launch` supporting native applications stands for allowing custom schemas in the `redirect_uri` property.

**To demonstrate native applications support**

1. Register an application with a custom URL schema
2. Build the authorization request URL
3. Open the URL in the browser
4. Authenticate in the Smartbox (enter login and password)
5. Authorize the launch (allow on the Consent screen)
6. Receive the \`code\` from Smartbox
7. Get Postman collection installed
8. Exchange the \`code\` to the \`access\_token\` & \`refresh\_token\`
9. Use \`access\_token\` to fetch resources from Smartbox
10. Use \`refresh\_token\` to get a new \`access\_token\`
11. Use updated \`access\_token\` to fetch resources from Smartbox

### Register an application with a custom URL schema

<pre class="language-yaml"><code class="lang-yaml">PUT /Client/for-refresh-token
content-type: text/yaml

id: for-refresh-token
type: patient-facing-smart-app
grant_types:
  - authorization_code
  - basic
resourceType: Client
auth:
  authorization_code:
    pkce: false
<strong>    redirect_uri: custom://redirect     # custom schema is defined
</strong>    refresh_token: true
    secret_required: true
    access_token_expiration: 300
secret: secret
active: true
smart:
  launch_uri: https://inferno.healthit.gov/suites/custom/smart/launch</code></pre>

### Build the authorization request URL

The link should look like this `https://example.com/tenant/my-clinic/patient/auth/authorize?client_id=for-refresh-token&scope=launch/patient%20patient/Patient.read%20patient/Condition.read%20offline_access&state=my-state&response_type=code&redirect_uri=custom://redirect&aud=https://example.com/tenant/my-clinic/patient/smart-api`

Instead of `https://example.com` use your Smartbox base url.

### Open the URL in the browser

In your web browser:

1. Open a new private tab in the browser
2. Open developer console to be able to see all the http requests the browser does
3. Put the link to the URL bar and press enter

### Authenticate in the Smartbox (enter login and password)

Still on the browser enter you login and password credentials

### Authorize the launch (allow on the Consent screen)

Still on the browser press the `Allow` button

### Receive the \`code\` from Smartbox

After press the `Allow` button Smartbox redirects user back to the `Consent screen`.

In the http requests list find the latest one `GET` request with the code `302`. The `location` header should start with the `custom://redirect?` string. \
\
Fetch the `code` parameter from the location. Mind there are other parameters in that link. You do not need them.

### Get the Postman collection installed

1. Open Postman
2. Import the [collection](https://www.getpostman.com/collections/c3f1546d1df29d3df725)
3. Update the `host` variable with your Smarbox base url

### Exchange the \`code\` to the \`access\_token\` & \`refresh\_token\`

In the Postman

1. Open `Exchange code for access_token` request
2. Put the `code` to the `code` parameter
3. Press the `Send` button

The result of the request is a JSON-object containing `access_token` and `refresh_token` properties.

### Use \`access\_token\` to fetch resources from Smartbox

Still in postman:

1. Open the `Get Patient Resource` request
2. Copy the `access_token` to the `token` input
3. Press the `Send` button

The result of the request is a JSON-object of the  `Patient` resource. Access token works.

### Use \`refresh\_token\` to get a new \`access\_token\`

Still in postman:

1. Open the `Refresh access token` request
2. Copy the `refresh_token` to the `refresh_token` parameter
3. Press the `Send` button

The result of the request is a JSON-object containing the `access_token`.

### Use updated \`access\_token\` to fetch resources from Smartbox

Still in postman:

1. Open the `Get Patient Resource` request
2. Copy the freshly issued `access_token` to the `token` input&#x20;
3. Press the `Send` button

The result of the request is the JSON-object of the  `Patient` resource. Updated `access_token`works

## 9.10.14 Health IT developer demonstrates the public location of its base URLs

Smartbox generates the list of the base URLs. The address of the list is `https://example.com/service-base-urls`

{% hint style="info" %}
Here `https://example.com` is the Smartbox domain
{% endhint %}

## 9.10.15 TLS version 1.2 or above must be enforced

That behavior is not related to the Smartbox (Aidbox) settings. Setting the TLS versions and enforcement to the wanted versions is out of the Smartbox setup scope
