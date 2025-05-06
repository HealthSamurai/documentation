---
description: This guide explains how to set up single sign-on features (SSO) with Auth0
---

# Set up SSO with Auth0

This guide expands [Set up SMARTbox](../get-started/set-up-smartbox.md).

## Create an application in Auth0

&#x20;To create an application:

* Login to the Auth0
* Open the `Applications` page&#x20;
* Press the `Create Application` button
* Give the name of the application
* Choose the type of application. Consider, Native type
* Press the `Create` button

After the app is created add the details:

* Application callback URLs: `http://localhost:8888/tenant/my-clinic/patient/auth/callback/?`
* Press the `Save Changes` button

Open the `Advanced settings` of the application and copy:

* `OAuth Authorization URL`
* `OAuth Token URL`
* `OAuth User Info URL`

## Create a user in Auth0

To create a new user:

* Open the `Users` page
* Press the `Create User` button
* Provide email and password
* Press the `Create` button

Copy the `user_id`. It looks like `auth0|6310e2d143b66b669906d775`

## Create a Tenant in Aidbox Portal

As the administrator of the Portal create an `Tenant` resource. Please, see [What is Tenant](../background-information/what-is-tenant.md) for more details.

<pre class="language-yaml"><code class="lang-yaml">id: my-clinic
resourceType: Tenant
name: My Clinic Name
logoUrl: https://example.com/my-clinic-logo.png
identityProvider:
  scopes:
    - user
<strong>    - read:org
</strong>    - openid
    - profile
<strong>  system: aidbox:tenant:my-clinic
</strong>  userinfo_endpoint: https://dev-nei1uq73.us.auth0.com/userinfo
  authorize_endpoint: https://dev-nei1uq73.us.auth0.com/authorize
  token_endpoint: https://dev-nei1uq73.us.auth0.com/oauth/token
  client:
    id: ziW...lv0
    secret: 7HoQ...gvb</code></pre>

{% hint style="info" %}
The URLs copied from the Auth0 are used here:

* `OAuth Authorization URL as authorize_endpoint`&#x20;
* `OAuth Token URL as token_endpoint`
* `OAuth User Info URL as userinfo_endpoint`
{% endhint %}

{% hint style="warning" %}
It's important to provide the `system` attribute
{% endhint %}

## Create a user in Aidbox Portal

The user should be linked to a Patient to be able to launch SMART Apps. And at the same time, it should be related to the Auth0 user. Please, see [What is Tenant](../background-information/what-is-tenant.md) for more details.

```yaml
id: test-user-1
resourceType: User
identifier:
  - value: auth0|6310e2d143b66b669906d775
    system: aidbox:tenant:my-clinic
fhirUser:
  id: test-pt-1
  resourceType: Patient
roles:
  - type: patient
active: true
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

{% hint style="warning" %}
1. `identifier` contains the link to the Auth0 user
2. `fhirUser` links the user to the Patient
{% endhint %}

## Checking it works together

* Open the link `https://f81a-5-161-99-84.ngrok.io/tenant/my-clinic/patient/portal` in the Invisible (Stealth) mode
* Click the `Sign In` link
* Enter `email` and `password` of the user, we created in the Auth0
* Press the `Login` button

### Launch the Growth Chart

* On the SMARTbox page `Applications`
* Press the `Launch` button against the Growth Chart application
* Press the `Allow` button on the consent screen
* Get the Growth Chart works
