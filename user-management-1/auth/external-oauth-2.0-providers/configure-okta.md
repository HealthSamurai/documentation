---
description: How to configure Okta IdentityProvider with Aidbox
---

# Configure Okta

## Create an Account in Okta

If you do not have an Okta account, create it to get your authorization server. Find your **okta domain**. You can do it in **Security &gt; API**

![](../../../.gitbook/assets/image%20%2847%29.png)

## Create a Client \(Application\) in Okta

Go to **Application &gt; Application** in Okta and create a new one.

* Check **Authorization Code** Grant Type
* Set Sign-in url to _**&lt;box-url&gt;**/auth/callback/**&lt;identity-provider-id&gt;**_

![](../../../.gitbook/assets/image%20%2843%29.png)

Checkout **Client ID** and **Client secret**

![](../../../.gitbook/assets/image%20%2844%29.png)

## Create an IdentityProvider in Aidbox

Using REST Console create an IdentityProvider config. Replace **&lt;okta-domain&gt;** with your okta domain.

* `client.redirect_uri` should be _**&lt;box-url&gt;**/auth/callback/**&lt;identity-provider-fdid&gt;**_
* set `client.client_id` and `client.client_secret` to Okta's credentials
* replace **&lt;box-url&gt;** with your box url \(like http://localhost:8080\)
* set scopes to `['profile', 'openid']`

```text
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

Go to your Aidbox base URL, you will be redirected to the login page - you should see **"Log in with &lt;provider.title or .type&gt;"** button**.** Press this button and log in with Okta user into aidbox.

![](../../../.gitbook/assets/image%20%2845%29.png)

This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../../../security-and-access-control-1/security/) about permissions.

