---
description: This guide shows how to set-up Keycloak identity provider with Aidbox
---

# Keycloak

## Create a realm

*   Click on the dropdown in the top-left corner where it says `Master`, then click on `Create Realm`\\

    <figure><img src="../../../../.gitbook/assets/471ad5aa-e363-4749-b5b5-d0139aaeef3c.png" alt=""><figcaption></figcaption></figure>
*   Pul the name of your realm to the `Realm name` input

    <figure><img src="../../../../.gitbook/assets/b0a9f216-c2eb-4c35-b6c4-c8d5abba3615.png" alt=""><figcaption></figcaption></figure>

## Create a client

* Click the `Clients` menu item in the left sidebar

<figure><img src="../../../../.gitbook/assets/339fa2f3-93eb-4fc0-8543-9a768cdabade.png" alt=""><figcaption></figcaption></figure>

*   Then click the `Create client` button\\

    <figure><img src="../../../../.gitbook/assets/3efd7a64-6f24-45a9-94e5-6cfe6ec972dc.png" alt=""><figcaption></figcaption></figure>
*   Enter the client data\\

    <figure><img src="../../../../.gitbook/assets/3616ac7b-66ae-407e-99d7-7e937092c969.png" alt=""><figcaption></figcaption></figure>
*   Check the `Client authentication` checkbox\\

    <figure><img src="../../../../.gitbook/assets/71339728-d3cb-4d86-b8b8-c4c7e015b9a9.png" alt=""><figcaption></figcaption></figure>
*   Add `<aidbox-url>/auth/callback/keycloak` to `Valid redirect URIs` field.\\

    <figure><img src="../../../../.gitbook/assets/92666ab7-fd0e-4549-816c-9418e0865c2a.png" alt=""><figcaption></figcaption></figure>

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

<figure><img src="../../../../.gitbook/assets/3344a568-66c1-4a2f-8c75-8b9869bfd85d.png" alt=""><figcaption></figcaption></figure>

## Login into Aidbox

Go to your Aidbox base URL, you will be redirected to the login page - you should see **"Log in with Keycloak"** button. Press this button and log in with Keycloak user into aidbox. This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../security/README.md) about permissions.
