# Managing Admin Access to the Aidbox UI Using Okta Groups

## Objectives

* Configure integration with the Okta Identity Provider to enable secure login to the Aidbox UI
* Manage administrative access to the Aidbox UI through Okta by assigning users to specific groups

## Before you begin

* Create an account in [Okta development portal](https://developer.okta.com/)
* Make sure your Aidbox version is newer than 2107
* Setup the local Aidbox instance using getting started [guide](https://docs.aidbox.app/getting-started/run-aidbox-locally-with-docker/run-aidbox-locally)

## Managing Admin Access to the Aidbox UI Using Okta Groups

### Create a Client (Application) in Okta

Go to **Applications -> Applications** in Okta portal and click "**Create App Integration"** button.

* Sign-in method: OIDC - OpenID Connect
* Application type: Web Application

<figure><img src="../../.gitbook/assets/image (113).png" alt=""><figcaption></figcaption></figure>

Enter Application details:

* App integration name: Aidbox
* Grant type:&#x20;
  * Authorization Code
  * Refresh Token
  * Implicit (hybrid)

<figure><img src="../../.gitbook/assets/image (116).png" alt=""><figcaption></figcaption></figure>

* Sign-in redirect URIs: `http://localhost:8080/auth/callback/okta-identity-provider`
* Controlled access: Skip group assignment for now

<figure><img src="../../.gitbook/assets/image (115).png" alt=""><figcaption></figcaption></figure>

Assign the application to your personal account in Okta:

<figure><img src="../../.gitbook/assets/image (118).png" alt=""><figcaption></figcaption></figure>

Checkout **Client ID** and **Client secret**:

<figure><img src="../../.gitbook/assets/image (117).png" alt=""><figcaption></figcaption></figure>

### Create an IdentityProvider in Aidbox

Login to Aidbox UI.&#x20;

Use REST Console to execute the request below.

* `<okta-domain>` should be your Okta domain, e.g. `dev-43727041.okta.com`
* replace `<client-id>` and `<client-secret>` with the actual values

<pre class="language-json"><code class="lang-json"><strong>PUT /fhir/IdentityProvider/okta-identity-provider
</strong>content-type: application/json
accept: application/json

{
 "scopes": [
  "profile",
  "openid"
 ],
 "system": "okta",
 "authorize_endpoint": "https://&#x3C;okta-domain>/oauth2/default/v1/authorize",
 "token_endpoint": "https://&#x3C;okta-domain>/oauth2/default/v1/token",
 "userinfo-source": "id-token",
 "client": {
  "id": "&#x3C;client-id>",
  "secret": "&#x3C;client-secret>",
  "redirect_uri": "http://localhost:8080/auth/callback/okta-identity-provider"
 },
 "type": "okta",
 "resourceType": "IdentityProvider",
 "title": "MyOkta",
 "active": true
}
</code></pre>

### Login into Aidbox using Okta user

Go to the Aidbox login page. You should see **Sign in with MyOkta** button.&#x20;

<figure><img src="../../.gitbook/assets/image (119).png" alt="" width="375"><figcaption></figcaption></figure>

Press this button and log in with Okta user into Aidbox.

You should not be able to see much in the Aidbox because there's no access policy for your user yet.

Relogin with admin and check the user created in Aidbox for your Okta user. Go to **IAM -> User** and click on the user ID.

<figure><img src="../../.gitbook/assets/image (120).png" alt=""><figcaption></figcaption></figure>

User Resource is Aidbox is updated every time the user logs in using external Identity Provider. Source of the user information is configured using `userinfo-source` element in IdentityProvider configuration resource.&#x20;

### Create the Aidbox-Admins group in Okta

In Okta go to **Directory -> Groups** and create group `Aidbox-Admins`

<figure><img src="../../.gitbook/assets/image (121).png" alt=""><figcaption></figcaption></figure>

Add your user to the group

<figure><img src="../../.gitbook/assets/image (124).png" alt=""><figcaption></figcaption></figure>

### Customize ID token in Okta to include groups

In Okta go to **Security -> API** drill down to the `default` authorization server go to **Claims** tab. Click **Add Claim** button.

* name: groups
* Include in token type: ID Token
* Value type: Groups
* Filter: Starts with: Aidbox-Admins
* Include in: any scope

<figure><img src="../../.gitbook/assets/image (127).png" alt=""><figcaption></figcaption></figure>

### Make sure the group info from Okta is making into Aidbox

Relogin into Aidbox using **Sign in with MyOkta** button. The user is updated with Okta groups data. Relogin with admin and check.

<figure><img src="../../.gitbook/assets/image (126).png" alt=""><figcaption></figcaption></figure>

### Create the AccessPolicy

Use REST Console to execute the request

```json
PUT /fhir/AccessPolicy/okta-admins-policy
content-type: application/json
accept: application/json

{
 "engine": "matcho",
 "matcho": {
  "user": {
   "data": {
    "groups": {
     "$contains": "Aidbox-Admins"
    }
   }
  },
  "request-method": {
   "$enum": [
    "get",
    "post",
    "put",
    "delete"
   ]
  }
 },
 "resourceType": "AccessPolicy"
}
```

Now you can relogin with your Okta user and the user will be granted admin access.

## What's next

* See more about Aidbox Security [security-and-access-control](../../modules/security-and-access-control/ "mention")
