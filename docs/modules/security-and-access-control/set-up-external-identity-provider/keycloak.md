---
description: This guide shows how to set-up Keycloak identity provider with Aidbox
---

# Keycloak

## Create a realm

*   Click on the dropdown in the top-left corner where it says `Master`, then click on `Create Realm`\\

    <figure><img src="../../../../.gitbook/assets/96bb00e6-761a-43e2-abce-e2646287b090.png" alt=""><figcaption></figcaption></figure>
*   Pul the name of your realm to the `Realm name` input

    <figure><img src="../../../../.gitbook/assets/36fcf8ca-147d-477f-ba78-a85ab767678a.png" alt=""><figcaption></figcaption></figure>

## Create a client

* Click the `Clients` menu item in the left sidebar

<figure><img src="../../../../.gitbook/assets/d24631bd-25d1-42a2-aa41-c2de4af6f2fa.png" alt=""><figcaption></figcaption></figure>

*   Then click the `Create client` button\\

    <figure><img src="../../../../.gitbook/assets/72a9f144-400a-43d8-9b5d-d42e2d8d8cd8.png" alt=""><figcaption></figcaption></figure>
*   Enter the client data\\

    <figure><img src="../../../../.gitbook/assets/6115af56-fae4-4ea8-8adc-75115e4bdcfb.png" alt=""><figcaption></figcaption></figure>
*   Check the `Client authentication` checkbox\\

    <figure><img src="../../../../.gitbook/assets/8d6d9db9-28e6-4c0a-abc7-c00afa3c71e9.png" alt=""><figcaption></figcaption></figure>
*   Add `<aidbox-url>/auth/callback/keycloak` to `Valid redirect URIs` field.\\

    <figure><img src="../../../../.gitbook/assets/637fe35f-ee1a-4d12-9780-18de4463b67e.png" alt=""><figcaption></figcaption></figure>

## Create IdentityProvider in Aidbox

* Open REST console in AidboxUI and create IdentityProvider resource

```yaml
POST /IdentityProvider

scopes:
  - profile
  - openid
system: keycloak
authorize_endpoint: <keycloak-url>/realms/<your-realm>/protocol/openid-connect/auth
token_endpoint: <keycloak-url>/realms/<your-realm>/protocol/openid-connect/token
userinfo_endpoint: <keycloak-url>/realms/<your-realm>/protocol/openid-connect/userinfo
userinfo-source: id-token | userinfo-endpoint
client:
  id: <your client id>
  secret: <your client secret>
resourceType: IdentityProvider
title: Keycloak
active: true
id: keycloak
```

You can find client secret on `Clients details` page under `Credentials` tab

<figure><img src="../../../../.gitbook/assets/722a0e3d-f7ba-41d9-bd52-3cc732108b10.png" alt=""><figcaption></figcaption></figure>

## Login into Aidbox

Go to your Aidbox base URL, you will be redirected to the login page - you should see **"Log in with Keycloak"** button. Press this button and log in with Keycloak user into aidbox. This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../security/README.md) about permissions.
