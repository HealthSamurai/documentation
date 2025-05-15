---
description: This guide shows how to set-up Keycloak identity provider with Aidbox
---

# Keycloak

## Create a realm

*   Click on the dropdown in the top-left corner where it says `Master`, then click on `Create Realm`\\

    <figure><img src="../../../../.gitbook/assets/0895cd422e9446439758e43097a8ba70.png" alt=""><figcaption></figcaption></figure>
*   Pul the name of your realm to the `Realm name` input

    <figure><img src="../../../../.gitbook/assets/b5f099e5083141f2b798ede54611f0a0.png" alt=""><figcaption></figcaption></figure>

## Create a client

* Click the `Clients` menu item in the left sidebar

<figure><img src="../../../../.gitbook/assets/2976828803a74882b47b05e1e26029da.png" alt=""><figcaption></figcaption></figure>

*   Then click the `Create client` button\\

    <figure><img src="../../../../.gitbook/assets/cdd23f5baf87458693e76f84d8ab7b6b.png" alt=""><figcaption></figcaption></figure>
*   Enter the client data\\

    <figure><img src="../../../../.gitbook/assets/b9c9df17f2a9420099dbbd55bfb4757b.png" alt=""><figcaption></figcaption></figure>
*   Check the `Client authentication` checkbox\\

    <figure><img src="../../../../.gitbook/assets/046837dd88b8431fa213f196f5a8cc06.png" alt=""><figcaption></figcaption></figure>
*   Add `<aidbox-url>/auth/callback/keycloak` to `Valid redirect URIs` field.\\

    <figure><img src="../../../../.gitbook/assets/a957e0754c514f4bba2282b5ce2710ce.png" alt=""><figcaption></figcaption></figure>

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

<figure><img src="../../../../.gitbook/assets/f54a91a08e0540aeb3bd3d7ebb14e44e.png" alt=""><figcaption></figcaption></figure>

## Login into Aidbox

Go to your Aidbox base URL, you will be redirected to the login page - you should see **"Log in with Keycloak"** button. Press this button and log in with Keycloak user into aidbox. This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../security/README.md) about permissions.
