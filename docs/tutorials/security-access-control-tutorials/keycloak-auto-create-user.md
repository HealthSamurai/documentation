# Auto-create User from foreign token tutorial

This tutorial explains how to use [Auto-create users from foreign tokens](../../reference/settings/all-settings#security.introspection-create-user) setting.
To illustrate this feature, we are using Keycloak in this tutorial.

## Before you begin

- Setup the local Aidbox instance using getting started [guide](../../getting-started/run-aidbox-locally.md)

## Set up Aidbox and Keycloak in docker compose

1. Add to docker-compose.yaml following:

```yaml
volumes:
  keycloak_data: {}
  postgres_data: {}

services:
  keycloak:
    container_name: keycloak
    hostname: keycloak
    image: quay.io/keycloak/keycloak:26.3.5
    ports:
      - 8180:8080
    volumes:
      - keycloak_data:/opt/keycloak/data
    environment:
      KC_BOOTSTRAP_ADMIN_USERNAME: admin
      KC_BOOTSTRAP_ADMIN_PASSWORD: admin
      KC_HTTP_ENABLED: "true"
      KC_HOSTNAME: http://localhost:8180
      KC_HOSTNAME_STRICT: "false"
      KC_HOSTNAME_BACKCHANNEL_DYNAMIC: "true"
    command: start-dev

  aidbox:
    container_name: aidbox
    # ... other aidbox config ...
    extra_hosts:
      - "keycloak:host-gateway" # Maps keycloak hostname to host machine
    environment:
      # ...
      - BOX_SECURITY_INTROSPECTION_CREATE_USER=true
```

## Starting services

```bash
docker-compose up -d
```

Service URLs:

- Keycloak: http://localhost:8180 (admin/admin)
- Aidbox: http://localhost:8080

## Keycloak configuration

### 1. Create Realm

1. Login to Keycloak: http://localhost:8180 (admin/admin)
2. Create new realm: hover over "Master" → "Create Realm"
3. Name: `myrealm`
4. Create

### 2. Create Client

1. Clients → Create client
2. Settings:
   - Client type: `OpenID Connect`
   - Client ID: `my-client`
3. Next → Capability config:
   - Client authentication: `ON`
   - Standard flow: `ON`
   - Direct access grants: `ON`
   - Service accounts roles: `ON` (important for client_credentials)
4. Next, Save
5. After creation, go to Client scopes tab:
   - Add `openid` to Default Client Scopes if not already there
   - This ensures service account gets openid scope

### 3. Get Client secret

1. Open `my-client` → "Credentials" tab
2. Copy the Client secret

### 4. Create User

1. Users → Add user
2. Fill in:
   - Email verified: `ON`
   - Username: `testuser`
   - Email: `testuser@example.com`
3. Create
4. "Credentials" tab → Set password:
   - Password: `testpass123`
   - Temporary: `OFF`

## Aidbox configuration

In Aidbox REST Console (http://localhost:8080) execute:

### 1. Create IdentityProvider

Aidbox needs IdentityProvider to fetch user data after first successful request with foreign token.

```http
PUT /fhir/IdentityProvider/keycloak

id: keycloak
scopes:
  - profile
  - openid
system: keycloak
userinfo_endpoint: http://keycloak:8180/realms/myrealm/protocol/openid-connect/userinfo
userinfo-source: userinfo-endpoint
resourceType: IdentityProvider
title: Keycloak
active: true
```

### 2. Create TokenIntrospector

With TokenIntrospector, we tell Aidbox to validate the JWT from `Authorization` header using `jwks_uri` endpoint and that jwt must have `iss: http://localhost:8180/realms/myrealm` field.

```http
PUT /fhir/TokenIntrospector/keycloak-jwt-introspector

resourceType: TokenIntrospector
type: jwt
jwt:
  iss: http://localhost:8180/realms/myrealm
jwks_uri: http://keycloak:8180/realms/myrealm/protocol/openid-connect/certs
identity_provider:
  reference: IdentityProvider/keycloak
```

### 3. Create AccessPolicy

With this AccessPolicy, we allow every request with `iss: http://localhost:8180/realms/myrealm`.

```yaml
PUT /fhir/AccessPolicy/keycloak-users-policy

resourceType: AccessPolicy
engine: matcho
matcho:
  jwt:
    iss: http://localhost:8180/realms/myrealm
```

## Getting JWT token

### Getting token using Client Credentials flow (service account)

```bash
curl -X POST http://localhost:8180/realms/myrealm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=my-client" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "grant_type=client_credentials" \
  -d "scope=openid profile email"
```

Response contains `access_token` for use with Aidbox.

Copy `access_token`. You can view the issued JWT in https://www.jwt.io/.

<details>
<summary>Example JWT</summary>

```json
{
  "exp": 1758805244,
  "iat": 1758804944,
  "jti": "trrtcc:0380f499-624a-be8d-9e0f-f3c9b47f2074",
  "iss": "http://localhost:8180/realms/myrealm",
  "aud": "account",
  "sub": "74ae9aaf-5681-4231-96d3-eeec940a20b4",
  "typ": "Bearer",
  "azp": "my-client",
  "acr": "1",
  "allowed-origins": [
    "/*"
  ],
  "realm_access": {
    "roles": [
      "default-roles-myrealm",
      "offline_access",
      "uma_authorization"
    ]
  },
  "resource_access": {
    "account": {
      "roles": [
        "manage-account",
        "manage-account-links",
        "view-profile"
      ]
    }
  },
  "scope": "profile email",
  "email_verified": false,
  "clientHost": "192.168.112.1",
  "preferred_username": "service-account-my-client",
  "clientAddress": "192.168.112.1",
  "client_id": "my-client"
}
```

</details>

## Using token with Aidbox

```bash
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  http://localhost:8080/fhir/Patient
```

On first request with a valid token, Aidbox will automatically create a user based on JWT data.

## Verify created User

```yaml
GET /fhir/User
```

User example:

```json
{
  "data": {
    "sub": "74ae9aaf-5681-4231-96d3-eeec940a20b4",
    "email_verified": false,
    "preferred_username": "service-account-my-client"
  },
  "identifier": [
    {
      "value": "74ae9aaf-5681-4231-96d3-eeec940a20b4",
      "system": "keycloak"
    }
  ],
  "id": "13dea80b-f5fa-4ec6-a758-67158db9844f",
  "resourceType": "User",
  "meta": {
    "lastUpdated": "2025-09-25T13:42:28.826592Z",
    "versionId": "30",
    "extension": [
      {
        "url": "https://aidbox.app/ex/createdAt",
        "valueInstant": "2025-09-25T13:42:28.826592Z"
      }
    ]
  }
}
```

## Troubleshooting

### "Account is not fully set up"

- Ensure user has Email verified: ON
- Clear Required User Actions
- Check that Temporary password: OFF

### "Client not enabled to retrieve service account"

- In client settings enable Service accounts roles: ON

### Aidbox cannot connect to Keycloak

- Within Docker use `http://keycloak:8080` instead of `localhost:8180`
- Verify both containers are in the same Docker network
