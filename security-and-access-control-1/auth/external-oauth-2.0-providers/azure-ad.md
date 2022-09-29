---
description: How to configure Azure AD IdentityProvider with Aidbox
---

# 🎓 Azure AD

## Register an application in Azure

* Find **App Registration** in search bar

![](../../../.gitbook/assets/azure7.png)

* Click **New Registration**

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-83834d5ed70499fcfd4585a9a56febbebaf46af8_azure0.png" alt=""><figcaption></figcaption></figure>

* Fill form. For Redirect URI choose `web` as a platform and input `https://<box-url>/auth/callback/azure`

![](../../../.gitbook/assets/azure4.png)

* Click **Certificates & secrets > New client secret** and create a new secret. Save `Value` for next step

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-c80aad22e2b9fa1bee113d9ff9c19e226be35e8a_azure3.png" alt=""><figcaption></figcaption></figure>

## Create IdentityProvider in Aidbox

* Open REST console in Aidbox UI and create `IdentityProvider`

```yaml
POST /IdentityProvider
content-type: text/yaml
accept: text/yaml

scopes:
  - profile
  - openid
system: azure
userinfo_endpoint: <your userinfo endpoint>
authorize_endpoint: <your authorization endpoint>
token_endpoint: <your token endpoint>
client:
  id: <your application (client) id>
  secret: <your secret value from previous step>
resourceType: IdentityProvider
title: Azure AD
active: true
id: azure
```

You can find application (client) id on **App Overview** page

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-4833cc9444c2a38a76f8edc0759fb36622368c90_azure5.png" alt=""><figcaption></figcaption></figure>

and your endpoints by clicking on **Endpoints** and visiting `OpenID Connect metadata document`

<figure><img src="../../../.gitbook/assets/spaces_-LHqtKiuedlcKJLm337__uploads_git-blob-8f857e9e29de04af413036e10747e7eadf94bcad_azure2.png" alt=""><figcaption></figcaption></figure>

## Log in to Aidbox

Go to your Aidbox base URL. You will be redirected to the log in page where you should now see `Sign in with Azure AD` button. Press this button and log in with Azure AD user. This user will be logged in to Aidbox Console but he won’t have any permissions. Read more in [Access Control Section](../../security/) about permissions.
