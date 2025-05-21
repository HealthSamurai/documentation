---
description: >-
  This guide shows how to set-up Azure AD identity provider in Aidbox with
  asymmetric authentication
---

# How to configure Azure AD SSO with certificate authentication for access to the Aidbox UI

{% hint style="info" %}
This guide explains how to set-up Azure AD identity provider in Aidbox with an asymmetric authentication mechanism. If you are looking for symmetric (secret-based one), please, follow [Azure AD guide](azure-ad.md).
{% endhint %}

## Register an application in Azure

* Find **App Registration** in search bar

![](../../../.gitbook/assets/azure7.png)

* Click **New Registration**

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-83834d5ed70499fcfd4585a9a56febbebaf46af8_azure0.png" alt=""><figcaption></figcaption></figure>

* Fill form. For Redirect URI choose `web` as a platform and input `https://<box-url>/auth/callback/azure`

![](../../../.gitbook/assets/azure4.png)

## Create IdentityProvider in Aidbox

Open REST console in Aidbox UI and create `IdentityProvider`

```yaml
POST /IdentityProvider
content-type: text/yaml
accept: text/yaml

id: azure
title: Azure AD
active: true
scopes:
  - profile
  - openid
system: azure
userinfo-source: id-token
authorize_endpoint: <your authorization endpoint>
token_endpoint: <your token endpoint>
client:
  id: <your application (client) id>
```

You can find application (client) id on **App Overview** page

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-4833cc9444c2a38a76f8edc0759fb36622368c90_azure5.png" alt=""><figcaption></figcaption></figure>

and your endpoints by clicking on **Endpoints** and visiting `OpenID Connect metadata document`

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-8f857e9e29de04af413036e10747e7eadf94bcad_azure2.png" alt=""><figcaption></figcaption></figure>

## Issue certificate

Once IdentityProvider resource is created in Aidbox, you can generate private key & certificate:

```yaml
POST /IdentityProvider/azure/$rotate-credentials
content-type: text/yaml
accept: text/yaml

auth-method: asymmetric
confirm: true

# response 200 OK
# Private key & certificate are generated and saved in the IdentityProvider
```

{% hint style="info" %}
Aidbox generates certificate for 365 days.
{% endhint %}

Than you may download the certificate in order to upload it to Azure AD by following the link

```
<AIDBOX_BASE_URL>/IdentityProvider/azure/$download-certificate
```

Upload the certificate into Azure AD

<figure><img src="../../../.gitbook/assets/19b0358b-f8d7-48d3-a710-1e1fa54ade08.png" alt=""><figcaption></figcaption></figure>

It may take few minutes when Azure starts processing the uploaded certificate.

## Log in to Aidbox

Go to your Aidbox base URL. You will be redirected to the log in page where you should now see `Sign in with Azure AD` button. Press this button and log in with Azure AD user. This user will be logged in to Aidbox Console but he won’t have any permissions. Read more in [Access Control Section](../../modules/security-and-access-control/security/) about permissions.
