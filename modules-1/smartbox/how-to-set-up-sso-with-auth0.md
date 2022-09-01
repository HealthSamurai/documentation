---
description: This guide explains how to set up single sign-on features (SSO) with Auth0
---

# How to set up SSO with Auth0

This guide expands [Set up SMARTbox](how-to-set-up-sso-with-auth0.md#set-up-smartbox).

### Set up ngrok

Use [Ngrok](https://ngrok.com) to expose the 8888 port.

<pre class="language-bash"><code class="lang-bash"><strong>ngrok http 8888</strong></code></pre>

The output should contain the forwarding details like this.

```bash
Forwarding https://f81a-5-161-99-84.ngrok.io -> http://localhost:8888
```

### Set `AIDBOX_BASE_URL` in the docker-compose.yaml&#x20;

#### Follow the instructions according to the [Set up SMARTbox](how-to-set-up-sso-with-auth0.md#set-up-smartbox) guide till the 'Create docker-compose.yaml'.

{% hint style="warning" %}
`AIDBOX_BASE_URL` parameter on the `portal section` should have value Ngrok provided.

In the current configuration setup, it is the `https://f81a-5-161-99-84.ngrok.io`
{% endhint %}

## Create an application in Auth0

&#x20;To create an application:

* Login to the Auth0
* Open the 'Applications' page&#x20;
* Press the 'Create Application' button
* Give the name of the application
* Choose the type of the application. Consider, Native type
* Press the 'Create' button

After the app is created add the details:

* Application login URL: `https://f81a-5-161-99-84.ngrok.io/auth/authorize`
* Application callback URLs: `https://f81a-5-161-99-84.ngrok.io/auth/callback/auth0?`
* Press the 'Save Changes' button

Open the 'Advanced settings' of the application and copy:

* `OAuth Authorization URL`
* `OAuth Token URL`
* `OAuth User Info URL`

They will be used when create IdentityProvider in SMARTbox.

## Create a user in Auth0

To create a new user:

* Open the 'Users' page
* Press the 'Create User' button
* Provie email and password
* Press the 'Create' button

Copy the 'user\_id'. It looks like `auth0|6310e2d143b66b669906d775`

## Create `IdentityProvider` in Aidbox Portal

As the administrator of the Portal create an `IdentityProvider` resource

<pre class="language-yaml"><code class="lang-yaml">id: auth0
scopes:
  - user
  - read:org
  - openid
  - profile
<strong>system: auth0
</strong>userinfo_endpoint: https://dev-nei1uq73.us.auth0.com/userinfo
authorize_endpoint: https://dev-nei1uq73.us.auth0.com/authorize
token_endpoint: https://dev-nei1uq73.us.auth0.com/oauth/token
client:
  id: ziW2tl6z5nmk2F9h7hAFqVRROOjO8lv0
  secret: 7HoQ_ERhXWDbg6D1jx-mnHnN7-JvTXkYAzQv1y2H7zl_qRlkbHcZ-qFMb7sXsgvb
  redirect_uri: https://f81a-5-161-99-84.ngrok.io/auth/callback/auth0
resourceType: IdentityProvider
title: Auth0
active: true</code></pre>

{% hint style="info" %}
The URLs copied from the Auth0 are used here:

* `OAuth Authorization URL as authorize_endpoint`&#x20;
* `OAuth Token URL as token_endpoint`
* `OAuth User Info URL as userinfo_endpoint`
{% endhint %}

{% hint style="warning" %}
It's important to provide the `system` attribute
{% endhint %}

## Create a User in Aidbox Portal

The user should be linked to a Patient to be able to launch SMART Apps. And the the same time it should be related to the Auth0 user.

```yaml
id: test-user-1
resourceType: User
identifier:
  - value: auth0|6310e2d143b66b669906d775
    system: auth0
fhirUser:
  id: test-pt-1
  resourceType: Patient
roles:
  - type: patient
active: true
```

{% hint style="warning" %}
1. `identifier` contains the link to the Auth0 user
2. `fhirUser` links the user to the Patient
{% endhint %}

## Checking it works together

* Open the link `https://f81a-5-161-99-84.ngrok.io` in the Invisible (Stealth) mode
* Press the 'Sign in with Auth0' button
* Enter `email` and `password` of the user we created in the Auth0
* Press the 'Login' button

### Launch the Growth Chart

* On the SMARTbox page 'Applications' press the 'Launch' button against the Growth Chart application
* Press the 'Allow' button on the consent screen
* Get the Growth Chart works

