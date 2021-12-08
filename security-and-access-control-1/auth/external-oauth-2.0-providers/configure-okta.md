---
description: How to configure Okta IdentityProvider with Aidbox
---

# 🎓 Okta

## Create an Account in Okta

If you do not have an Okta account, create it to get your authorization server. Find your **okta domain**. You can do it in **Security > API**

![](<../../../.gitbook/assets/image (40).png>)

## Create a Client (Application) in Okta

Go to **Application > Application** in Okta and create a new one.

* Check **Authorization Code** Grant Type
* Set Sign-in url to _**\<box-url>**/auth/callback/**\<identity-provider-id>**_

![](<../../../.gitbook/assets/image (41).png>)

Checkout **Client ID** and **Client secret**

![](<../../../.gitbook/assets/image (43).png>)

## Create an IdentityProvider in Aidbox

Using REST Console create an IdentityProvider config. Replace **\<okta-domain>** with your okta domain.

* `client.redirect_uri` should be _**\<box-url>**/auth/callback/**\<identity-provider-fdid>**_
* set `client.client_id` and `client.client_secret` to Okta's credentials
* replace **\<box-url>** with your box url (like http://localhost:8080)
* set scopes to `['profile', 'openid']`

```
PUT /IdentityProvider/octa?_format=yaml&_pretty=true
content-type: text/yaml

type: OAuth
title: MyOcta # this element present only in 202106 version
active: true
system: 'okta'
scopes:
  - profile
  - openid
userinfo_endpoint: '<okta-domain>/oauth2/v1//userinfo'
authorize_endpoint: '<okta-domain>/oauth2/v1/authorize'
client:
  id: <client-id>
  secret: <client-secret>
  redirect_uri: '<box-url>/auth/callback/okta'

token_endpoint: '<okta-domain>/oauth2/v1/token'
```

## Login into Aidbox with Okta

Go to your Aidbox base URL, you will be redirected to the login page - you should see **"Log in with \<provider.title or .type>"** button**.** Press this button and log in with Okta user into aidbox.

![](<../../../.gitbook/assets/image (44).png>)

This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../../security/) about permissions.
