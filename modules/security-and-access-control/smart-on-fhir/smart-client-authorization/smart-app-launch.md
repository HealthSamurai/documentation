# SMART App Launch

{% hint style="info" %}
This functionality is available starting from version 2411.\
The [FHIR Schema Validator Engine](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine) should be enabled.
{% endhint %}

&#x20;Performing [SMART App Launch](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html) with Aidbox requires:

## Register SMART Client&#x20;

It shall be [Authorization Code Grant](../../auth/authorization-code.md) Client with several required values:

<table><thead><tr><th width="405">Client resource field</th><th>Description</th></tr></thead><tbody><tr><td><code>auth.authorization_code.token_format</code></td><td>Fixed value - <code>jwt</code></td></tr><tr><td><code>smart.launch_uri</code></td><td>SMART Application launch endpoint</td></tr><tr><td><code>type</code></td><td>Fixed value - <code>smart-app</code></td></tr></tbody></table>

### Example

```http
PUT /Client/my-smart-app-client-id
content-type: application/json
accept: application/json

{
  "resourceType": "Client",
  "id": "my-smart-app-client-id",
  "active": true,
  "auth": {
    "authorization_code": {
      "redirect_uri": "http://smart-app-uri.com/redirect",
      "refresh_token": true,
      // always shall be jwt
      "token_format": "jwt",
      "access_token_expiration": 3600000,
      // only if you want to use PKCE challenge
      "pkce": true
    }
  },
  // always shall have smart.launch_uri value
  "smart": {
    "launch_uri": "http://smart-app-launch-endpoint/launch.html"
  },
  // always shall be "smart-app" type
  "type": "smart-app",
  "secret": "quOfCRS7ty1RMUQq",
  "grant_types": [
    "code"
  ]
}
```

## Create AccessPolicy

```json
PUT /AccessPolicy/my-smart-app-client-id-allow
content-type: application/json
accept: application/json

{
  "resourceType": "AccessPolicy",
  "id": "my-smart-app-client-id-allow",
  "engine": "allow",
  "link": {
    "id": "my-smart-app-client-id",
    "resourceType": "Client"
  }
}
```



## Launch App

### Standalone launch

### EHR launch





Launch URI structure:

```
http://smart-app-launch-endpoint?iss=<fhir-endpoint>&launch=<opaque-identifier>
```

Where:

1. `smart-app-launch-endpoint` - SMART Application launch endpoint. It shall be equal to `Client.smart.launch_uri`.
2. `iss` - Identifies the EHR's FHIR endpoint.
3. `launch` - JWT identifier for this specific launch and context associated with it. It shall be signed with an [Aidbox private key](https://docs.aidbox.app/reference/configuration/environment-variables/optional-environment-variables#set-up-rsa-private-public-keys-and-secret).

`launch` parameter JWT shall contain the following claims:

| Claim name      | Value type   | Description                                                                 |
| --------------- | ------------ | --------------------------------------------------------------------------- |
| `client` \*     | valueString  | Aidbox [client](smart-app-launch.md#id-1.-client-for-smart-application) ID. |
| `user`\*        | valueString  | Aidbox user ID.                                                             |
| `exp`\*         | valueInteger | Experation time in seconds                                                  |
| `ctx.patient`\* | valueString  | Patient ID                                                                  |

\* - required claim

### Get launch URI endpoint

Aidbox provides an endpoint to build the correct launch URI:

{% tabs %}
{% tab title="Request" %}
```http
POST /rpc
content-type: application/json
accept: application/json

{
  "method": "aidbox.smart/get-launch-uri",
  "params": {
    "user": "my-aidbox-user-id",
    "iss": "https://example.edge.aidbox.app/fhir",
    "client": "my-smart-app-client-id",
    "ctx": {
      "patient": "my-patient-id"
    }
  }
}
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK

{
 "result": {
  // Run this uri to perform SMART App Launch
  "uri": "http://smart-app-launch-endpoint/launch.html?iss=https://example.edge.aidbox.app/fhir&launch=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnQiOiJteS1zbWFydC1hcHAtY2xpZW50LWlkIiwidXNlciI6Im15LWFpZGJveC11c2VyLWlkIiwiZXhwIjoxNzMzMzk5Nzk3LCJjdHgiOnsicGF0aWVudCI6Im15LXBhdGllbnQtaWQifX0.wn78VQrDN8xmS_wowQ-a3MRPuOEhFZ-PyTMn5BHe5No",
  "launch": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnQiOiJteS1zbWFydC1hcHAtY2xpZW50LWlkIiwidXNlciI6Im15LWFpZGJveC11c2VyLWlkIiwiZXhwIjoxNzMzMzk5Nzk3LCJjdHgiOnsicGF0aWVudCI6Im15LXBhdGllbnQtaWQifX0.wn78VQrDN8xmS_wowQ-a3MRPuOEhFZ-PyTMn5BHe5No",
  "iss": "https://example.edge.aidbox.app/fhir",
  "launch-uri": "http://smart-app-launch-endpoint/launch.html"
 }
}
```


{% endtab %}
{% endtabs %}

## Obtain authorization code

To obtain a token redirect the user to Aidbox `/auth/authorize` with following parameters:

<table><thead><tr><th width="272">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>response_type</code> *</td><td>Fixed value - <code>code</code></td></tr><tr><td><code>client_id</code> * </td><td>Client resource ID.</td></tr><tr><td><code>redirect_uri</code> * </td><td>Client's pre-registered redirect URIs. Must match the <code>Client.auth.authorization_code.redirect_uri</code> value.</td></tr><tr><td><code>scope</code> *</td><td>String with <a href="../smart-scopes.md">scopes</a> separated by space. Must describe the access that the app needs.</td></tr><tr><td><code>state</code> *</td><td>An opaque value used by the client to maintain state between the request and callback. The authorization server includes this value when redirecting the user-agent back to the client. The parameter SHALL be used for preventing cross-site request forgery or session fixation attacks. The app SHALL use an unpredictable value for the state parameter with at least 122 bits of entropy (e.g., a properly configured random uuid is suitable).</td></tr><tr><td><code>aud</code> *</td><td>URL of the EHR resource server from which the app wishes to retrieve FHIR data. Usually, it is <code>&#x3C;AIDBOX_BASE_URL>/fhir</code>.</td></tr><tr><td><code>code_challenge</code></td><td>This parameter is generated by the app and used for the code challenge, as specified by <a href="https://docs.aidbox.app/modules/security-and-access-control/auth/authorization-code#get-code">PKCE</a>. </td></tr><tr><td><code>code_challenge_method</code></td><td>Fixed value - <code>S256</code></td></tr><tr><td><code>launch</code></td><td>When using the <a href="smart-app-launch.md#ehr-launch">EHR Launch flow</a>, this must match the launch value received from the EHR (Aidbox). Omitted when using the <a href="smart-app-launch.md#standalone-launch">Standalone Launch</a>.</td></tr></tbody></table>

\*- required parameter

### Example

{% tabs %}
{% tab title="Request" %}
```http
Location: https://<AIDBOX_BASE_URL>/auth/authorize?
            response_type=code&
            client_id=my-smart-app-client-id&
            redirect_uri=http://smart-app-uri.com/redirect&
            scope=launch%2Fpatient+openid+fhirUser+offline_access+patient%2F*.read&
            state=863c2f71-11e3-4598-913b-930a6aa1593c&
            aud=https%3A%2F%2Fg10test.edge.aidbox.app%2Ffhir&
            code_challenge=E3VWZsn5u_Tiw6HEz8bleXP27hr8TG-Zjpx5CP0lZxA&
            code_challenge_method=S256
```
{% endtab %}

{% tab title="Response" %}
```http
// Aidbox redirect back with code
http://smart-app-uri.com/redirect?
    code=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJleHAiOjE3MzQwMzMxMzksInNjb3BlIjpbImxhdW5jaC9wYXRpZW50Iiwib3BlbmlkIiwiZmhpclVzZXIiLCJvZmZsaW5lX2FjY2VzcyIsInBhdGllbnQvUGF0aWVudC5yZWFkIiwicGF0aWVudC9BbGxlcmd5SW50b2xlcmFuY2UucmVhZCIsInBhdGllbnQvQ2FyZVBsYW4ucmVhZCIsInBhdGllbnQvQ2FyZVRlYW0ucmVhZCIsInBhdGllbnQvQ29uZGl0aW9uLnJlYWQiLCJwYXRpZW50L0RldmljZS5yZWFkIiwicGF0aWVudC9EaWFnbm9zdGljUmVwb3J0LnJlYWQiLCJwYXRpZW50L0RvY3VtZW50UmVmZXJlbmNlLnJlYWQiLCJwYXRpZW50L0dvYWwucmVhZCIsInBhdGllbnQvRW5jb3VudGVyLnJlYWQiLCJwYXRpZW50L0ltbXVuaXphdGlvbi5yZWFkIiwicGF0aWVudC9NZWRpY2F0aW9uUmVxdWVzdC5yZWFkIiwicGF0aWVudC9PYnNlcnZhdGlvbi5yZWFkIiwicGF0aWVudC9Qcm9jZWR1cmUucmVhZCIsInBhdGllbnQvUHJvdmVuYW5jZS5yZWFkIiwicGF0aWVudC9QcmFjdGl0aW9uZXIucmVhZCIsInBhdGllbnQvT3JnYW5pemF0aW9uLnJlYWQiLCJwYXRpZW50L0xvY2F0aW9uLnJlYWQiXSwiYmFzZS11cmwiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwL2ZoaXIiLCJqdGkiOiIyWTNFbmxkSjV0M3lpbjN2TGxXTiIsImN0eCI6eyJwYXRpZW50IjoidGVzdC1wdC0xIn0sImNsaSI6ImluZmVybm8tcGF0aWVudC1zbWFydC1hcHAiLCJjb2RlX2NoYWxsZW5nZSI6IlZoNUlEXzdkdEEtTVlRTUtEdmNyZFJZN3dOSU9DWkdTSElUdlpCcU5yeDAiLCJvbi1iZWhhbGYiOnRydWUsImNvZGVfY2hhbGxlbmdlX21ldGhvZCI6IlMyNTYifQ.kyjOQ16BB_gieSKDcUjm9WpuzYtI1xRmVAMottiOOEw&
    state=07864ab3-206a-495b-b8e9-2c66b7db6fc2
```
{% endtab %}
{% endtabs %}

## Obtain access token

After obtaining an authorization code, the app exchange the code for an access token. To do it send requiest to AIdbox's `/auth/token` with following parameters:

<table><thead><tr><th width="259">Parametr</th><th>Description</th></tr></thead><tbody><tr><td><code>grant_type</code> * </td><td>Fixed value - <code>authorization_cod</code>e</td></tr><tr><td><code>code</code> *</td><td>Code that the app received from Aidbox on the previous step.</td></tr><tr><td><code>redirect_uri</code> * </td><td>Client's pre-registered redirect URIs. Must match the <code>Client.auth.authorization_code.redirect_uri</code> value.</td></tr><tr><td><code>code_verifier</code></td><td>This parameter is used to verify against the <code>code_challenge</code> parameter previously provided in the authorize request.</td></tr><tr><td><code>client_id</code></td><td>Required for <code>public apps</code>. Omit for <code>confidential apps</code>.</td></tr></tbody></table>

\*- required parameter

### Example

{% tabs %}
{% tab title="Request" %}
```json
POST /auth/token
content-type: application/json
accept: application/json

{
  "client_id": "my-smart-app-client-id",
  "code": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJleHAiOjE3MzQwMzMxMzksInNjb3BlIjpbImxhdW5jaC9wYXRpZW50Iiwib3BlbmlkIiwiZmhpclVzZXIiLCJvZmZsaW5lX2FjY2VzcyIsInBhdGllbnQvUGF0aWVudC5yZWFkIiwicGF0aWVudC9BbGxlcmd5SW50b2xlcmFuY2UucmVhZCIsInBhdGllbnQvQ2FyZVBsYW4ucmVhZCIsInBhdGllbnQvQ2FyZVRlYW0ucmVhZCIsInBhdGllbnQvQ29uZGl0aW9uLnJlYWQiLCJwYXRpZW50L0RldmljZS5yZWFkIiwicGF0aWVudC9EaWFnbm9zdGljUmVwb3J0LnJlYWQiLCJwYXRpZW50L0RvY3VtZW50UmVmZXJlbmNlLnJlYWQiLCJwYXRpZW50L0dvYWwucmVhZCIsInBhdGllbnQvRW5jb3VudGVyLnJlYWQiLCJwYXRpZW50L0ltbXVuaXphdGlvbi5yZWFkIiwicGF0aWVudC9NZWRpY2F0aW9uUmVxdWVzdC5yZWFkIiwicGF0aWVudC9PYnNlcnZhdGlvbi5yZWFkIiwicGF0aWVudC9Qcm9jZWR1cmUucmVhZCIsInBhdGllbnQvUHJvdmVuYW5jZS5yZWFkIiwicGF0aWVudC9QcmFjdGl0aW9uZXIucmVhZCIsInBhdGllbnQvT3JnYW5pemF0aW9uLnJlYWQiLCJwYXRpZW50L0xvY2F0aW9uLnJlYWQiXSwiYmFzZS11cmwiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwL2ZoaXIiLCJqdGkiOiIyWTNFbmxkSjV0M3lpbjN2TGxXTiIsImN0eCI6eyJwYXRpZW50IjoidGVzdC1wdC0xIn0sImNsaSI6ImluZmVybm8tcGF0aWVudC1zbWFydC1hcHAiLCJjb2RlX2NoYWxsZW5nZSI6IlZoNUlEXzdkdEEtTVlRTUtEdmNyZFJZN3dOSU9DWkdTSElUdlpCcU5yeDAiLCJvbi1iZWhhbGYiOnRydWUsImNvZGVfY2hhbGxlbmdlX21ldGhvZCI6IlMyNTYifQ.kyjOQ16BB_gieSKDcUjm9WpuzYtI1xRmVAMottiOOEw",
  "code_verifier": "2ef1d0dc-7659-41f9-abbe-bdec3441bca9-81867d53-46a5-47a6-89df-03872627287f",
  "grant_type": "authorization_code",
  "redirect_uri": "http://smart-app-uri.com/redirect"
} 
```
{% endtab %}

{% tab title="Response" %}
```json
{
  "access_token": "611ZFIZiaR62OISylNhS9FMz7RLiPTVRn6sgbxz5uzOwB1TxOI8koxT2elL3fCMH",
  "refresh_token": "N9wSbVxNxme3f2lh31drixdxf4ZWcBpatOlubVRopQTdwKAcMH7CmmkGS21SX0l8",
  "smart_style_url": "https://<AIDBOX_BASE_URL>p/fhir/style-v1.json",
  "scope": "launch/patient openid fhirUser offline_access patient/Patient.read patient/AllergyIntolerance.read patient/CarePlan.read patient/CareTeam.read patient/Condition.read patient/Device.read patient/DiagnosticReport.read patient/DocumentReference.read patient/Goal.read patient/Encounter.read patient/Immunization.read patient/MedicationRequest.read patient/Observation.read patient/Procedure.read patient/Provenance.read patient/Practitioner.read patient/Organization.read patient/Location.read",
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwianRpIjoiODY3ZjJhNWUtYzc3MS00NDgwLTlhYTMtNzQyNzVjYWZkNTgxIiwiaWF0IjoxNzM0MDMyODM5LCJleHAiOjE3MzQwMzMxMzksImZoaXJVc2VyIjoiaHR0cHM6Ly9nMTB0ZXN0LmVkZ2UuYWlkYm94LmFwcC9maGlyL1BhdGllbnQvdGVzdC1wdC0xIiwiZW1haWwiOiJleGFtcGxlQG1haWwuY29tIiwiYXVkIjoiaW5mZXJuby1wYXRpZW50LXNtYXJ0LWFwcCJ9.MJXA_yc1M5O3LUFU5DhvUMx8c1Zp5u6n85mLbJXeh4JuPsmETCz3YxV6VS6JidUSPm6BgbpY9ZqOJP32cxNoSkO0ATvzrocdfN71rUqTsThgPFNJND20drob-XnTODJgpN_JuC_00YyXw5j0Irr9NZLFzORClkzsuBM1jdxiVta04VrDsd3FizzC_p7x3UpmsmGQAO_qta6aG7xZN2BdCGHBAtEVC0uC69unDboo7w9UTB-YASW0t7sEbnKqaDT8OSbHiLP1OleShykCQRisFzo7JlMucotec31-M0XaEPJH94W9xFvxghzhULRMuojP6v0XmAqi2nhQCrwywfcWew",
  "expires_in": 300,
  "need_patient_banner": true,
  "patient": "test-pt-1",
  "userinfo": {
    "email": "example@mail.com",
    "meta": {
      "lastUpdated": "2024-12-12T19:05:17.754510Z",
      "createdAt": "2024-12-06T15:13:59.452154Z",
      "versionId": "1307"
    },
    "sub": "test-user",
    "name": {
      "givenName": "Amy",
      "familyName": "Shaw"
    },
    "fhirUser": {
      "id": "test-pt-1",
      "resourceType": "Patient"
    },
    "resourceType": "User",
    "active": true,
    "id": "test-user"
  }
} 

```
{% endtab %}
{% endtabs %}

## Access FHIR API



## Refresh access token



## SMART App Launch using an External Identity Provider

Aidbox enables SMART App Launch with external identity providers. Set up an [Identity Provider](../../set-up-external-identity-provider/) in Aidbox to sign in via the provider during the launch. Try it with our pre-configured demo (e.g., Keycloak):

{% content-ref url="../example-smart-app-launch-using-aidbox-and-keycloak.md" %}
[example-smart-app-launch-using-aidbox-and-keycloak.md](../example-smart-app-launch-using-aidbox-and-keycloak.md)
{% endcontent-ref %}
