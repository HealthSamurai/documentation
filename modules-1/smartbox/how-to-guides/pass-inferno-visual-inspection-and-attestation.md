---
description: >-
  This guide explains how to pass the Inferno Visual Inspection and Attestation
  sequence
---

# Pass Inferno Visual Inspection and Attestation

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
To reduce the refresh\_token lifespan use [refresh\_token\_expiration documentation](../../security-and-access-control/auth/authorization-code.md#configure-client)
{% endhint %}

## 9.10.06 Health IT developer demonstrated the ability of the Health IT Module / authorization server to validate token it has issued

1. Follow the guide of the [9.10.13 Health IT developer demonstrates support for issuing refresh tokens to native applications](pass-inferno-visual-inspection-and-attestation.md#9.10.13-health-it-developer-demonstrates-support-for-issuing-refresh-tokens-to-native-applications) guide till the end

Smartbox allowed to fetch `Patient` resource with the `access_token` it issued.

## 9.10.07 Tester verifies that all information is accurate and without omission

Switch to the `Yes` option. If something is lost, the tester says it.

## 9.10.08 Information returned no greater than scopes pre-authorized for multi-patient queries

**To demonstrate that behavior**

* Add a Client with narrow pre-authorized `scope`
* Launch the `Multi-Patient Authorization and API` sequence with wide scope

### Add a Client with the narrow pre-authorized scope

Mind the `scope` property. It holds the `system/Patient.read` value only. Access to the other resources is forbidden.

<pre class="language-yaml"><code class="lang-yaml">PUT /Client/inferno-my-clinic-bulk-client
Content-Type: text/yaml

type: bulk-api-client
active: true
auth:
  client_credentials:
    client_assertion_types: ['urn:ietf:params:oauth:client-assertion-type:jwt-bearer']
    access_token_expiration: 300
<strong>scope: [system/Patient.read]
</strong>jwks_uri: https://inferno.healthit.gov/suites/custom/g10_certification/.well-known/jwks.json
grant_types:
- client_credentials
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
</code></pre>

### Launch the Multi-Patient Authorization and API sequence with wide scope

1. Start new Inferno session (it's important)
2. Switch to the `Multi-Patient Authorization and API` sequence
3. Press the `Run tests` button
4. Set up the test:
   * Bulk Data FHIR URL: `[aidbox-url]/tenant/my-clinic/bulk-api`
   * Backend Services Token Endpoint: `[aidbox-url]/auth/token`
   * Bulk Data Client ID: `inferno-my-clinic-bulk-client`
   * Bulk Data Scopes: `system/Patient.read`
   * Encryption method: `RS384`
   * Group ID: `test-group-1`
   * Patient IDs in exported Group: `test-pt-1,test-pt-2`
5. Press the `Submit` button

You should receive a lot of errors as Smartbox doesn't issue the access token.

## 9.10.09 Health IT developer demonstrated the documentation is available at a publicly accessible URL

Smartbox has a documentation page. The address of the page is `https://example.com/documentation`

{% hint style="info" %}
Here `https://example.com` is the Smartbox domain
{% endhint %}

## 9.10.10 Health IT developer confirms the Health IT module does not cache the JWK Set received via a TLS-protected URL for longer than the cache-control header received by an application indicates

It is an attestation. You should state Smartbox (Aidbox) caches `JWK` sets it receives during the token validations. Cached JWK lives for 5 minutes then it is deleted.

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

{% hint style="warning" %}
It is a good idea to perform this flow in the `Incognito mode` (Chrome) or `Private Window` (Firefox)
{% endhint %}

1. Register an application with a custom URL schema
2. Build the authorization request URL
3. Open the URL in the browser
4. Authenticate in the Smartbox (enter login and password)
5. Authorize the launch (allow on the Consent screen)
6. Receive the `code` from Smartbox
7. Get Postman collection installed
8. Exchange the `code` to the `access_token` and `refresh_token`
9. Use `access_token` to fetch resources from Smartbox
10. Use `refresh_token` to get a new `access_token`
11. Use updated `access_token` to fetch resources from Smartbox

### 1. Register an application with a custom URL schema

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
  launch_uri: https://inferno.healthit.gov/suites/custom/smart/launch
</code></pre>

### 2. Build the authorization request URL

The link should look like this `https://example.com/tenant/my-clinic/patient/auth/authorize?client_id=for-refresh-token&scope=launch/patient%20patient/Patient.read%20patient/Condition.read%20offline_access&state=my-state&response_type=code&redirect_uri=custom://redirect&aud=https://example.com/tenant/my-clinic/patient/smart-api`

Instead of `https://example.com` use your Smartbox base url.

### 3. Open the URL in the browser

In your web browser:

1. Open a new private tab in the browser
2. Open developer console to be able to see all the http requests the browser does
3. Put the link to the URL bar and press enter

### 4. Authenticate in the Smartbox (enter login and password)

Still on the browser enter your pateint login and password credentials

### 5. Authorize the launch (allow on the Consent screen)

Still on the browser press the `Allow` button

### 6. Receive the \`code\` from Smartbox

After press the `Allow` button Smartbox redirects user back to the `Consent screen`.

In the list of the https requests find the latest one `GET` request with the code `302`. The `location` header should start with the `custom://redirect?` string.\
\
Fetch the `code` parameter from the location. Mind there are other parameters in that link. You do not need them.

### 7. Get the Postman collection installed

1. Open Postman
2. Import the [collection](https://www.getpostman.com/collections/c3f1546d1df29d3df725)
3. Update the `host` variable with your Smartbox base url

### 8. Exchange the \`code\` to the \`access\_token\` & \`refresh\_token\`

In the Postman

1. Open `Exchange code for access_token` request
2. Put the `code` to the `code` parameter
3. Press the `Send` button

The result of the request is a JSON-object containing `access_token` and `refresh_token` properties.

{% hint style="warning" %}
Smartbox allows to exchange `code` to `token` in no more than 5 minutes after the `code` was issued
{% endhint %}

### 9. Use \`access\_token\` to fetch resources from Smartbox

Still in postman:

1. Open the `Get Patient Resource` request
2. Copy the `access_token` to the `token` input
3. Press the `Send` button

The result of the request is a JSON-object of the `Patient` resource. Access token works.

### 10. Use \`refresh\_token\` to get a new \`access\_token\`

Still in postman:

1. Open the `Refresh access token` request
2. Copy the `refresh_token` to the `refresh_token` parameter
3. Press the `Send` button

The result of the request is a JSON-object containing the `access_token`.

### 11. Use updated \`access\_token\` to fetch resources from Smartbox

Still in postman:

1. Open the `Get Patient Resource` request
2. Copy the freshly issued `access_token` to the `token` input
3. Press the `Send` button

The result of the request is the JSON-object of the `Patient` resource. Updated `access_token` works

## 9.10.14 Health IT developer demonstrates the public location of its base URLs

Smartbox generates the list of the base URLs. The address of the list is `https://example.com/service-base-urls`

{% hint style="info" %}
Here `https://example.com` is the Smartbox domain
{% endhint %}

## 9.10.15 TLS version 1.2 or above must be enforced

That behavior is not related to the Smartbox settings. Setting the TLS versions and enforcement to the wanted versions is out of the Smartbox setup scope

## 9.10.16 Health IT developer attested that the Health IT Module is capable of issuing refresh tokens valid for a new period of no shorter than three months without requiring re-authentication and re-authorization when a valid refresh token is supplied by the application

It is an attestation. You should state Smartbox is capable of issuing refresh tokens valid for a new period of no shorter than three months.
