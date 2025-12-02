# SMART App Launch

{% hint style="info" %}
This functionality is available starting from version 2411.\
The [FHIR Schema Validator Engine](../../../../modules/profiling-and-validation/fhir-schema-validator/) should be enabled.
{% endhint %}

Performing [SMART App Launch](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html) with Aidbox requires:

## Register SMART Client

SMART defines two client (application) types: `public` and `confidential`. To determine the correct client type for you, ask: "Can my app protect a secret?"

If the answer is "**Yes,"** use a confidential client.

* Example: The app operates on a trusted server, ensuring that the secret is only accessed server-side.
* Example: The app is a native application that employs additional technologies (such as dynamic client registration and universal redirect\_uris) to secure the secret.

If the answer is "**No**," use a public client.

* Example: The app is an HTML5 or JavaScript application running in a browser (including single-page applications), where the secret would be exposed in user space.
* Example: The app is a native application that can only distribute a secret in a fixed, unprotected manner.

Client shall be [Authorization Code Grant](../../../authentication/oauth-2-0.md) Client with several required values:

<table><thead><tr><th width="445">Client resource field</th><th>Description</th></tr></thead><tbody><tr><td><code>auth.authorization_code.token_format</code></td><td>Fixed value - <code>jwt</code></td></tr><tr><td><code>auth.authorization_code.secret_required</code></td><td><code>true</code> - for confidential Client<br><code>false</code> - for public Client</td></tr><tr><td><code>smart.launch_uri</code></td><td>SMART Application launch endpoint</td></tr><tr><td><code>type</code></td><td>Fixed value - <code>smart-app</code></td></tr><tr><td><code>secret</code></td><td>Only for confidential Client</td></tr><tr><td><code>scope</code></td><td>List of scopes this client is authorized to request.</td></tr></tbody></table>

### Example

{% tabs %}
{% tab title="Public" %}
```json
PUT /Client/my-public-smart-app-client-id
content-type: application/json
accept: application/json

{
  "resourceType": "Client",
  "id": "my-public-smart-app-client-id",
  "active": true,
  "auth": {
    "authorization_code": {
      "redirect_uri": "http://smart-app-uri.com/redirect",
      "refresh_token": true,
      // always shall be jwt
      "token_format": "jwt",
      "access_token_expiration": 3600000,
      "secret_required": false,
      "pkce": true
    }
  },
  // always shall have smart.launch_uri value
  "smart": {
    "launch_uri": "http://smart-app-launch-endpoint/launch.html"
  },
  // always shall be "smart-app" type
  "type": "smart-app",
  "grant_types": [
    "code"
  ]
}
```
{% endtab %}

{% tab title="Confidential" %}
```json
PUT /Client/my-confidential-smart-app-client-id
content-type: application/json
accept: application/json

{
  "resourceType": "Client",
  "id": "my-confidential-smart-app-client-id",
  "active": true,
  "auth": {
    "authorization_code": {
      "redirect_uri": "http://smart-app-uri.com/redirect",
      "refresh_token": true,
      // always shall be jwt
      "token_format": "jwt",
      "access_token_expiration": 3600000,
      "secret_required": true,
      // only if you want to use PKCE challenge
      "pkce": true
    }
  },
  "secret": "quOfCRS7ty1RMUQq",
  // always shall have smart.launch_uri value
  "smart": {
    "launch_uri": "http://smart-app-launch-endpoint/launch.html"
  },
  // always shall be "smart-app" type
  "type": "smart-app",
  "grant_types": [
    "code"
  ]
}
```
{% endtab %}
{% endtabs %}

## Create AccessPolicy

```json
PUT /AccessPolicy/my-confidential-smart-app-client-id
content-type: application/json
accept: application/json

{
  "resourceType": "AccessPolicy",
  "id": "my-smart-app-client-id-allow",
  "engine": "allow",
  "link": [{
    "id": "my-public-smart-app-client-id",
    "resourceType": "Client"
  }]
}
```

## Launch App

SMART Launch Interaction Diagram:

<figure><img src="../../../../.gitbook/assets/svgviewer-output.svg" alt="SMART App Launch flow sequence diagram showing interactions between user, client, authorization server, and resource server"><figcaption><p>SMART App Lunch Ffow</p></figcaption></figure>

## Standalone launch

In SMART’s standalone launch flow, a user selects an app from outside the EHR (for example, by tapping an app icon on a mobile phone home screen).

The application should start launch by performing [authorization code grant flow](smart-app-launch.md#obtain-authorization-code).

## EHR launch

The EHR initiates a “launch sequence” by opening a new browser instance (or iframe) pointing to the app’s registered launch URL and passing some context.

### Launch URL structure

```
http://smart-app-launch-endpoint?iss=<fhir-endpoint>&launch=<jwt-identifier>
```

Where:

1. `smart-app-launch-endpoint` - SMART Application launch endpoint. It shall be equal to `Client.smart.launch_uri`.
2. `iss` - Identifies the EHR's FHIR endpoint.
3. `launch` - JWT identifier for this specific launch and context associated with it. It shall be signed with an [Aidbox private key](../../../../reference/all-settings.md).

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
    "client": "my-public-smart-app-client-id",
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

<table><thead><tr><th width="272">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>response_type</code> *</td><td>Fixed value - <code>code</code></td></tr><tr><td><code>client_id</code> *</td><td>Client resource ID.</td></tr><tr><td><code>redirect_uri</code> *</td><td>Client's pre-registered redirect URIs. Must match the <code>Client.auth.authorization_code.redirect_uri</code> value.</td></tr><tr><td><code>scope</code> *</td><td>String with scopes separated by space. Must describe the access that the app needs.</td></tr><tr><td><code>state</code> *</td><td>An opaque value used by the client to maintain state between the request and callback. The authorization server includes this value when redirecting the user-agent back to the client. The parameter SHALL be used for preventing cross-site request forgery or session fixation attacks. The app SHALL use an unpredictable value for the state parameter with at least 122 bits of entropy (e.g., a properly configured random uuid is suitable).</td></tr><tr><td><code>aud</code> *</td><td>URL of the EHR resource server from which the app wishes to retrieve FHIR data. Usually, it is <code>&#x3C;AIDBOX_BASE_URL>/fhir</code>.</td></tr><tr><td><code>code_challenge</code></td><td>This parameter is generated by the app and used for the code challenge, as specified by <a href="../../../../modules/security-and-access-control/auth/authorization-code/#get-code">PKCE</a>.</td></tr><tr><td><code>code_challenge_method</code></td><td>Fixed value - <code>S256</code></td></tr><tr><td><code>launch</code></td><td>When using the <a href="smart-app-launch.md#ehr-launch">EHR Launch flow</a>, this must match the launch value received from the EHR (Aidbox). Omitted when using the <a href="smart-app-launch.md#standalone-launch">Standalone Launch</a>.</td></tr></tbody></table>

\*- required parameter

### Example

{% tabs %}
{% tab title="Request" %}
```http
Location: https://<AIDBOX_BASE_URL>/auth/authorize?
            response_type=code&
            client_id=my-public-smart-app-client-id&
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

<table><thead><tr><th width="222">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>grant_type</code> *</td><td>Fixed value - <code>authorization_code</code></td></tr><tr><td><code>code</code> *</td><td>Code that the app received from Aidbox on the previous step.</td></tr><tr><td><code>redirect_uri</code> *</td><td>Client's pre-registered redirect URIs. Must match the <code>Client.auth.authorization_code.redirect_uri</code> value.</td></tr><tr><td><code>code_verifier</code></td><td>This parameter is used to verify against the <code>code_challenge</code> parameter previously provided in the authorize request.</td></tr><tr><td><code>client_id</code></td><td>Required for <code>public apps</code>. Omit for <code>confidential apps</code>.</td></tr></tbody></table>

\*- required parameter

### Example

{% tabs %}
{% tab title="Request" %}
```json
POST /auth/token
content-type: application/json
accept: application/json

{
  "client_id": "my-public-smart-app-client-id",
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
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6InRlc3QtdXNlciIsImlzcyI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAiLCJleHAiOjE3MzQwMzQ3MTYsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvQWxsZXJneUludG9sZXJhbmNlLnJlYWQgcGF0aWVudC9DYXJlUGxhbi5yZWFkIHBhdGllbnQvQ2FyZVRlYW0ucmVhZCBwYXRpZW50L0NvbmRpdGlvbi5yZWFkIHBhdGllbnQvRGV2aWNlLnJlYWQgcGF0aWVudC9EaWFnbm9zdGljUmVwb3J0LnJlYWQgcGF0aWVudC9Eb2N1bWVudFJlZmVyZW5jZS5yZWFkIHBhdGllbnQvR29hbC5yZWFkIHBhdGllbnQvRW5jb3VudGVyLnJlYWQgcGF0aWVudC9JbW11bml6YXRpb24ucmVhZCBwYXRpZW50L01lZGljYXRpb25SZXF1ZXN0LnJlYWQgcGF0aWVudC9PYnNlcnZhdGlvbi5yZWFkIHBhdGllbnQvUHJvY2VkdXJlLnJlYWQgcGF0aWVudC9Qcm92ZW5hbmNlLnJlYWQgcGF0aWVudC9QcmFjdGl0aW9uZXIucmVhZCBwYXRpZW50L09yZ2FuaXphdGlvbi5yZWFkIHBhdGllbnQvTG9jYXRpb24ucmVhZCIsImp0aSI6IjlmMjUxODU1LWQ1MTAtNDY2Mi1iZTg2LTE5ZTljZDYzN2Y2OCIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzQwMzQ0MTZ9.EC815f80x6QJebLLWmS0E9XjzPPee5QpIMYz0Oos9ocR_3b4FOsQalModuG4YMGkyZXJOwE29WUjv0fVVXGovdfb0a1hR3iK9_p28qUUb_OmGHo22Upt6K-smHkV8krGM5xNm6g_YPSFT1u9T4qlWoNMoCpti5UdKlmBjHdcIwXoeLb5yC9BynwkJBUpt5PTtE-_gpC_VIg6WkC1hwe2RrDwJl8qvaFl2VZEPhdLU2it3WnX1R-JR_tkXbmY8pv6UfeuPGABleR1sPweyQ-pz3coK4KmkY0tm_7OsBQGKgX9s7RIBP3ab3-dnx8XBJ_s9lw_zefIcYjCuBqZbZU12w",
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1wYXRpZW50LXNtYXJ0LWFwcCIsImp0aSI6IjlmMjUxODU1LWQ1MTAtNDY2Mi1iZTg2LTE5ZTljZDYzN2Y2OCIsInR5cCI6InJlZnJlc2gifQ.iKXaJjfAL5dRqfiduLuCgEJhWu0CIzi_2KPS6d80OEp24LB61M4PWx1_TUUS5qaedzrKUBhkE7-x07fI-6f5FdiBMGxq_aKbfGxTAUJJzh-ki-N20IOSolKFNSqyKILhwIP4V221H0YQZFles5ghXBGxK_O5TW-l9w3QDbcsLXBbhH1fOqetsiKdVac8iy2H278iMVnWq3eD8I_-O3yAuISxh_nOI4ENGnX8Z1KKcdrMDmwN7HNsTxmSLM5zkikPZlqIp02JijcV4y8z3XfVZhR2jaXmegTfz_qEWyVrgPYX1-oQ06MZFkgjnlYCZMswvz_wEPuE0zDPJMgGbiUwjg",
  "smart_style_url": "https://g10test.edge.aidbox.app/fhir/style-v1.json",
  "scope": "launch/patient openid fhirUser offline_access patient/Patient.read patient/AllergyIntolerance.read patient/CarePlan.read patient/CareTeam.read patient/Condition.read patient/Device.read patient/DiagnosticReport.read patient/DocumentReference.read patient/Goal.read patient/Encounter.read patient/Immunization.read patient/MedicationRequest.read patient/Observation.read patient/Procedure.read patient/Provenance.read patient/Practitioner.read patient/Organization.read patient/Location.read",
  "token_type": "Bearer",
  "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwianRpIjoiOWYyNTE4NTUtZDUxMC00NjYyLWJlODYtMTllOWNkNjM3ZjY4IiwiaWF0IjoxNzM0MDM0NDE2LCJleHAiOjE3MzQwMzQ3MTYsImZoaXJVc2VyIjoiaHR0cHM6Ly9nMTB0ZXN0LmVkZ2UuYWlkYm94LmFwcC9maGlyL1BhdGllbnQvdGVzdC1wdC0xIiwiZW1haWwiOiJleGFtcGxlQG1haWwuY29tIiwiYXVkIjoiaW5mZXJuby1wYXRpZW50LXNtYXJ0LWFwcCJ9.GL7VUZ7eUhDshfV6qbtFDuuMgbOeJwX8UpVJZPEwoJrv6Uy_2Dzvp2R5v3JrsX0HCI-6uyDl40J0SOnGtxjh1jaemzwR9nAAJ7lcxGoldzB53e5LYM_2tI0OMS12aCJCGCNWTLt4VgN9UcmC1PUzyXT8q8Uoqewj00zFu1wU_Mxe3Il3Kc6WQJV25LUVuYAC0UgZrTbPsWvGsWu6XaF-2RrCPqy5Lyc-z5gYMlOcJbFnedL8yKZrJXtIV8TAmBHOq4nr4KAXsv6NehB3Xo_Pn55IplOMWwil9F6_Q7rrzZSBnfyxgxGznups3YYZpC4FJHMuxIFEmsmQf96I5KSKbw",
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

Use `access_token` received in the previous step to access Aidbox FHIR API:

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/Observation?code=4548-4&_count=2
content-type: application/json
accept: application/json
authorization: "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdHYiOjIsImF1ZCI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAvZmhpciIsInN1YiI6InRlc3QtdXNlciIsImlzcyI6Imh0dHBzOi8vZzEwdGVzdC5lZGdlLmFpZGJveC5hcHAiLCJleHAiOjE3MzQwMzQ3MTYsInNjb3BlIjoibGF1bmNoL3BhdGllbnQgb3BlbmlkIGZoaXJVc2VyIG9mZmxpbmVfYWNjZXNzIHBhdGllbnQvUGF0aWVudC5yZWFkIHBhdGllbnQvQWxsZXJneUludG9sZXJhbmNlLnJlYWQgcGF0aWVudC9DYXJlUGxhbi5yZWFkIHBhdGllbnQvQ2FyZVRlYW0ucmVhZCBwYXRpZW50L0NvbmRpdGlvbi5yZWFkIHBhdGllbnQvRGV2aWNlLnJlYWQgcGF0aWVudC9EaWFnbm9zdGljUmVwb3J0LnJlYWQgcGF0aWVudC9Eb2N1bWVudFJlZmVyZW5jZS5yZWFkIHBhdGllbnQvR29hbC5yZWFkIHBhdGllbnQvRW5jb3VudGVyLnJlYWQgcGF0aWVudC9JbW11bml6YXRpb24ucmVhZCBwYXRpZW50L01lZGljYXRpb25SZXF1ZXN0LnJlYWQgcGF0aWVudC9PYnNlcnZhdGlvbi5yZWFkIHBhdGllbnQvUHJvY2VkdXJlLnJlYWQgcGF0aWVudC9Qcm92ZW5hbmNlLnJlYWQgcGF0aWVudC9QcmFjdGl0aW9uZXIucmVhZCBwYXRpZW50L09yZ2FuaXphdGlvbi5yZWFkIHBhdGllbnQvTG9jYXRpb24ucmVhZCIsImp0aSI6IjlmMjUxODU1LWQ1MTAtNDY2Mi1iZTg2LTE5ZTljZDYzN2Y2OCIsImNvbnRleHQiOnsicGF0aWVudCI6InRlc3QtcHQtMSJ9LCJpYXQiOjE3MzQwMzQ0MTZ9.EC815f80x6QJebLLWmS0E9XjzPPee5QpIMYz0Oos9ocR_3b4FOsQalModuG4YMGkyZXJOwE29WUjv0fVVXGovdfb0a1hR3iK9_p28qUUb_OmGHo22Upt6K-smHkV8krGM5xNm6g_YPSFT1u9T4qlWoNMoCpti5UdKlmBjHdcIwXoeLb5yC9BynwkJBUpt5PTtE-_gpC_VIg6WkC1hwe2RrDwJl8qvaFl2VZEPhdLU2it3WnX1R-JR_tkXbmY8pv6UfeuPGABleR1sPweyQ-pz3coK4KmkY0tm_7OsBQGKgX9s7RIBP3ab3-dnx8XBJ_s9lw_zefIcYjCuBqZbZU12w"
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK

{
 "resourceType": "Bundle",
 "type": "searchset",
 "entry": [
  {
   "resource": {
    "category": [
     {
      "coding": [
       {
        "code": "laboratory",
        "system": "http://terminology.hl7.org/CodeSystem/observation-category",
        "display": "laboratory"
       }
      ]
     }
    ],
    "meta": {
     "lastUpdated": "2024-08-29T15:51:05.117806Z",
     "versionId": "74",
     "extension": [
      {
       "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
       "valueInstant": "2024-08-29T15:51:05.117806Z"
      }
     ]
    },
    "encounter": {
     "reference": "Encounter/67b8fa04-6e1b-4074-8b8c-3ec44bfec48f"
    },
    "valueQuantity": {
     "code": "%",
     "unit": "%",
     "value": 2.856519918445372,
     "system": "http://unitsofmeasure.org"
    },
    "resourceType": "Observation",
    "effectiveDateTime": "2014-05-11T12:39:55+04:00",
    "status": "final",
    "id": "00592410-ec4a-4d64-a674-f0bfb244a978",
    "code": {
     "text": "Hemoglobin A1c/Hemoglobin.total in Blood",
     "coding": [
      {
       "code": "4548-4",
       "system": "http://loinc.org",
       "display": "Hemoglobin A1c/Hemoglobin.total in Blood"
      }
     ]
    },
    "issued": "2014-05-11T12:39:55.513+04:00",
    "subject": {
     "reference": "Patient/test-pt-1"
    }
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "https://releasetest.edge.aidbox.app/Observation/00592410-ec4a-4d64-a674-f0bfb244a978",
   "link": [
    {
     "relation": "self",
     "url": "https://releasetest.edge.aidbox.app/Observation/00592410-ec4a-4d64-a674-f0bfb244a978"
    }
   ]
  },
  {
   "resource": {
    "category": [
     {
      "coding": [
       {
        "code": "laboratory",
        "system": "http://terminology.hl7.org/CodeSystem/observation-category",
        "display": "laboratory"
       }
      ]
     }
    ],
    "meta": {
     "lastUpdated": "2024-08-29T15:51:05.117806Z",
     "versionId": "74",
     "extension": [
      {
       "url": "https://fhir.aidbox.app/fhir/StructureDefinition/created-at",
       "valueInstant": "2024-08-29T15:51:05.117806Z"
      }
     ]
    },
    "encounter": {
     "reference": "Encounter/f1c8a70d-0dfa-47a6-b940-d441fdfd1323"
    },
    "valueQuantity": {
     "code": "%",
     "unit": "%",
     "value": 3.1257055258079536,
     "system": "http://unitsofmeasure.org"
    },
    "resourceType": "Observation",
    "effectiveDateTime": "2018-01-14T11:39:55+03:00",
    "status": "final",
    "id": "01e57d19-35b7-47d0-9c3b-29d14d16d3f5",
    "code": {
     "text": "Hemoglobin A1c/Hemoglobin.total in Blood",
     "coding": [
      {
       "code": "4548-4",
       "system": "http://loinc.org",
       "display": "Hemoglobin A1c/Hemoglobin.total in Blood"
      }
     ]
    },
    "issued": "2018-01-14T11:39:55.513+03:00",
    "subject": {
     "reference": "Patient/test-pt-1"
    }
   },
   "search": {
    "mode": "match"
   },
   "fullUrl": "https://releasetest.edge.aidbox.app/Observation/01e57d19-35b7-47d0-9c3b-29d14d16d3f5",
   "link": [
    {
     "relation": "self",
     "url": "https://releasetest.edge.aidbox.app/Observation/01e57d19-35b7-47d0-9c3b-29d14d16d3f5"
    }
   ]
  }
 ]
}
```
{% endtab %}
{% endtabs %}

## Refresh access token

To refresh the `access_token` send request to Aidbox's `/auth/token` endpoint with following parameters:

<table><thead><tr><th width="270">Parameter</th><th>Description</th></tr></thead><tbody><tr><td><code>grant_type</code> *</td><td>Fixed value - <code>refresh_token</code></td></tr><tr><td><code>refresh_token</code> *</td><td>The refresh token from the <a href="smart-app-launch.md#obtain-access-token">authorization response</a>.</td></tr><tr><td><code>client_id</code></td><td>Client resource ID.</td></tr><tr><td><code>scope</code></td><td>String with scopes separated by space. Must describe the access that the app needs.</td></tr></tbody></table>

\*- required parameter

### Example

{% tabs %}
{% tab title="Request" %}
```json
POST /auth/token
content-type: application/json
accept: application/json

{
  "grant_type": "refresh_token",
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1wYXRpZW50LXNtYXJ0LWFwcCIsImp0aSI6IjlmMjUxODU1LWQ1MTAtNDY2Mi1iZTg2LTE5ZTljZDYzN2Y2OCIsInR5cCI6InJlZnJlc2gifQ.iKXaJjfAL5dRqfiduLuCgEJhWu0CIzi_2KPS6d80OEp24LB61M4PWx1_TUUS5qaedzrKUBhkE7-x07fI-6f5FdiBMGxq_aKbfGxTAUJJzh-ki-N20IOSolKFNSqyKILhwIP4V221H0YQZFles5ghXBGxK_O5TW-l9w3QDbcsLXBbhH1fOqetsiKdVac8iy2H278iMVnWq3eD8I_-O3yAuISxh_nOI4ENGnX8Z1KKcdrMDmwN7HNsTxmSLM5zkikPZlqIp02JijcV4y8z3XfVZhR2jaXmegTfz_qEWyVrgPYX1-oQ06MZFkgjnlYCZMswvz_wEPuE0zDPJMgGbiUwjg",
  "client_id": "my-public-smart-app-client-id"
}
```
{% endtab %}

{% tab title="Response" %}
```json
// 200 OK

{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2cxMHRlc3QuZWRnZS5haWRib3guYXBwIiwic3ViIjoiaW5mZXJuby1wYXRpZW50LXNtYXJ0LWFwcCIsImlhdCI6MTczNDAzNDQyMSwianRpIjoiOWYyNTE4NTUtZDUxMC00NjYyLWJlODYtMTllOWNkNjM3ZjY4IiwiYXVkIjoiaHR0cHM6Ly9nMTB0ZXN0LmVkZ2UuYWlkYm94LmFwcC9maGlyIiwiZXhwIjoxNzM0MDM0NzIxfQ.Y9Ghv8HqOjDZWowpsZU0ZR2Yreaot_EqcQiiOX39Ihxx5IplitYylzTJkE8bMBAZaZrRfHM4djEviC8xfkOcFKrOlwABZsAf5GYk7kSpmodNE-e_X4AkJ5MgxLChFJNFhxWuVvwT9jbMC2tLv7ycN4ZGMr1hGLr1hkg1jF43OE5VX-OCY6i6tIS50r4iBgKjTunQPpx0boG9skAAWhpKOFgj_QT5Mieq5UQRjV6z-B2B0ckQOZCByxlXBMyUAbiY8s-XHGxz1OPlqlllzy13205A4NpiWeJ5BrURtLorbJRqBr5Ij2CbDJm4ey81gq1XEY8TdirkWux5oVA5zbKWZQ",
  "token_type": "Bearer",
  "patient": "test-pt-1",
  "scope": "launch/patient openid fhirUser offline_access patient/Patient.read patient/AllergyIntolerance.read patient/CarePlan.read patient/CareTeam.read patient/Condition.read patient/Device.read patient/DiagnosticReport.read patient/DocumentReference.read patient/Goal.read patient/Encounter.read patient/Immunization.read patient/MedicationRequest.read patient/Observation.read patient/Procedure.read patient/Provenance.read patient/Practitioner.read patient/Organization.read patient/Location.read",
  "expires_in": 300
}
```
{% endtab %}
{% endtabs %}

## SMART App Launch using an External Identity Provider

Aidbox enables SMART App Launch with external identity providers. Set up an [IdentityProvider](../../../authentication/sso-with-external-identity-provider.md) in Aidbox to sign in via the provider during the launch. Try it with our pre-configured demo (e.g., Keycloak):
