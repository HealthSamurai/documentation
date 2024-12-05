# SMART App Launch

{% hint style="danger" %}
WORK IN PROGRESS
{% endhint %}

{% hint style="info" %}
This functionality is available starting from version 2411.\
Aidbox should be in [FHIR Schema mode](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine).
{% endhint %}

&#x20;Performing [SMART App Launch](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html) with Aidbox requires:

## 1. Client for SMART Application

It shall be [Authorization Code Grant](../auth/authorization-code.md) Client with several required values:

<table><thead><tr><th width="405">Client resource field</th><th>Description</th></tr></thead><tbody><tr><td><code>auth.authorization_code.token_format</code></td><td>Fixed value - <code>jwt</code></td></tr><tr><td><code>smart.launch_uri</code></td><td>SMART Application launch endpoint</td></tr><tr><td><code>type</code></td><td>Fixed value - <code>smart-app</code></td></tr></tbody></table>

### Example

```http
PUT /Client/my-smart-app-client-id
content-type: application/json
accept: application/json

{
  "resourceType": "Client",
  "id": "my-smart-app-client-id",
  "auth": {
    "authorization_code": {
      "redirect_uri": "http://smart-app-uri.com/",
      "refresh_token": true,
      // always shall be jwt
      "token_format": "jwt",
      "access_token_expiration": 3600000
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

## 2. Launch URI

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

## External Identity Provider

Aidbox supports SMART App Launch with external identity provider authorization. In this case, an [Identity Provider](../set-up-external-identity-provider/) needs to be set up in Aidbox then you will be able to Sign In with the external identity provider during the SMART App Launch flow.

Try SMART App Launch using Aidbox and an external identity provider (Keycloak) running our pre-configured demo:

{% content-ref url="example-smart-app-launch-using-aidbox-and-keycloak.md" %}
[example-smart-app-launch-using-aidbox-and-keycloak.md](example-smart-app-launch-using-aidbox-and-keycloak.md)
{% endcontent-ref %}
