---
description: How to configure AzureAD IdentityProvider with Aidbox
---

# AzureAD

## Register an application in Azure

* Find **App Registration** in search bar

![](<../../../.gitbook/assets/azure7.png>)

* Click **New Registration**

![](<../../../.gitbook/assets/azure0.png>)

* Fill form. Choose `web` as a platform and https://<box-url>/auth/callback/azure

![](<../../../.gitbook/assets/azure4.png>)

* Click **Certificates&secrets > New client secret** and create a new
  secret

![](<../../../.gitbook/assets/azure3.png>)
Save it for next step

## Create IdentityProvider in Aidbox

* Open REST console in AidboxUI and create IdentityProvider resource

```yaml
POST /IdentityProvider

scopes:
  - profile
  - openid
system: azure
userinfo_endpoint: <your userinfo endpoint>
authorize_endpoint: <your authorize endpoint>
token_endpoint: <your token endpoint>
client:
  id: <your application(client) id>
  secret: <your client secret>
resourceType: IdentityProvider
title: Azure AD
active: true
id: azure-ad
```

You can find application (client) id on **App Overview** page

![](<../../../.gitbook/assets/azure5.png>)
and your endpoints by clicking on **Endpoints** button

![](<../../../.gitbook/assets/azure2.png>)

## Login into Aidbox

Go to your Aidbox base URL, you will be redirected to the login page -
you should see **"Log in with Azure AD"** button. Press this button and log in with AzureAD user into aidbox.
This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../../security/) about permissions.
