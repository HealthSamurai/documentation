# How to Set Up Keycloak as IDP

[Keycloak](https://www.keycloak.org/) provides Open Source Identity
and Access Management. It is the default identity provider used by
Auditbox and is most well-supported.

## Deploy and set up keycloak

Follow the official
[guide](https://www.keycloak.org/getting-started/getting-started-kube)
to install keycloak into your cluster.

You also need to set up a realm with a client and user on it.
Below is an example realm config for a local system that you can use as
a template. To use it, inject appropriate variables (replace `${...}`
with your values), and when you log into master realm,
[import](https://www.keycloak.org/server/importExport) the config.

```json
{
  "id": "${id}",
  "realm": "${id}",
  "enabled": true,
  "clients": [
    {
      "clientId": "${id}",
      "secret": "${clientSecret}",
      "name": "${name}",
      "description": "",
      "rootUrl": "",
      "adminUrl": "",
      "baseUrl": "",
      "surrogateAuthRequired": false,
      "enabled": true,
      "alwaysDisplayInConsole": false,
      "clientAuthenticatorType": "client-secret",
      "redirectUris": [
        "http://localhost:3000/auth/callback/keycloak"
      ],
      "webOrigins": [
        "http://localhost:3000"
      ],
      "notBefore": 0,
      "bearerOnly": false,
      "consentRequired": false,
      "standardFlowEnabled": true,
      "implicitFlowEnabled": false,
      "directAccessGrantsEnabled": true,
      "serviceAccountsEnabled": false,
      "publicClient": false,
      "frontchannelLogout": true,
      "protocol": "openid-connect",
      "attributes": {
        "realm_client": "false",
        "oidc.ciba.grant.enabled": "false",
        "client.secret.creation.time": "1732708036",
        "backchannel.logout.session.required": "true",
        "oauth2.device.authorization.grant.enabled": "false",
        "backchannel.logout.revoke.offline.tokens": "false"
      },
      "authenticationFlowBindingOverrides": {},
      "fullScopeAllowed": true,
      "nodeReRegistrationTimeout": -1,
      "defaultClientScopes": [
        "web-origins",
        "acr",
        "profile",
        "roles",
        "basic",
        "email"
      ],
      "optionalClientScopes": [
        "address",
        "phone",
        "offline_access",
        "organization",
        "microprofile-jwt"
      ],
      "access": {
        "view": true,
        "configure": true,
        "manage": true
      }
    }
  ],
  "users": [
    {
      "username": "${username}",
      "email": "${username}@${domain}",
      "enabled": true,
      "emailVerified": true,
      "firstName": "${firstName}",
      "lastName": "${lastName}",
      "credentials": [
        {
          "type": "password",
          "value": "${password}",
          "temporary": false
        }
      ]
    }
  ]
}
```

Important points:
- Redirect URI should point to path **/auth/callback/keycloak**.
- Section "users" includes user credentials - **do not store in git**.
  If you do decide to store it - remove the users section.

## Configure Auditbox

```yaml
# Both of the fields are configured in the clients section of
# the template, but may be configured manually vai UI.
IDP_CLIENT_ID: <generated>
IDP_CLIENT_SECRET: <generated>

IDP_AUTHORIZE_ENDPOINT: ${auditbox_url}/realms/${id}/protocol/openid-connect/auth
IDP_TOKEN_ENDPOINT: ${auditbox_url}/realms/${id}/protocol/openid-connect/token
IDP_JWKS_URI: ${auditbox_url}/realms/${id}/protocol/openid-connect/certs
```
