---
description: Configure Aidbox as an External OAuth Provider
---

# 🎓 Aidbox

To set up one Aidbox instance to authenticate users in another Aidbox instance follow these steps:

1. Set up Aidbox as OAuth Provider instance
2. Set up client Aidbox instances that use the Provider&#x20;

### Set up Provider Aidbox

To set up the Provider Aidbox create a `Client` resource to be used by Client Aidbox

_In this guide Provider Aidbox URL is `https://provider.example.com`_

#### Create `Client` resource

Client Aidbox instance will use these Client to access Provider instance when authenticating users

```http
PUT /Client/local-client
Content-Type: text/yaml
Accept: text/yaml

resourceType: Client
id: local-client
secret: local-secret
auth:
  authorization_code:
    redirect_uri: https://client.example.com/auth/callback/global-provider
    refresh_token: true
    secret_required: true
    access_token_expiration: 36000
grant_types:
  - authorization_code
```

### Set up Client Aidbox

Create an `IdentityProvider` resource to set up a Client Aidbox instance.

_In this guide Client Aidbox URL is `https://client.example.com`_

#### Create `IdentityProvider` resource

`IdentityProvider` resource defies external authentication server

```http
PUT /IdentityProvider/global-provider
Accept: text/yaml
Content-Type: text/yaml

resourceType: IdentityProvider
title: AidboxGlobal
system: https://provider.example.com
active: true
id: global-provider
authorize_endpoint: https://provider.example.com/auth/authorize
token_endpoint: https://provider.example.com/auth/token
userinfo_endpoint: https://provider.example.com/auth/userinfo
scopes:
 - user
 - read:org
client:
 id: local-client
 secret: local-secret
 redirect_uri: https://client.example.com/auth/callback/global-provider
```

### Usage

If the Provider and the Client Aidbox instances are configured correctly, users registered and authenticated in the Provider Aidbox can get access to the Client Aidbox.

#### User signs in the Client Aidbox using  Provider Aidbox

1. Press "Sign in with `<Provider Aidbox title>`" button
2. Enter email and password of some User from the Provider Aidbox
3. Allow requested scopes in the Grant screen

Finally the user is authenticated and redirected to the Client Aidbox.

{% hint style="info" %}
You can create `Access Policy` for a User in the Client Aidbox
{% endhint %}
