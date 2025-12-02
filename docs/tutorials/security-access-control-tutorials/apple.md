# How to configure sign-in with Apple for access to the Aidbox UI

## Objectives

* Configure integration with the Sign-in with Apple (SSO) to enable secure login to the Aidbox UI

## Before you begin

* Create an account in Apple Developer portal
* Make sure your Aidbox version is newer than 2503
* Set up the local Aidbox instance using the getting started [guide](../../getting-started/run-aidbox-locally.md)

## Managing Admin Access to the Aidbox UI Using Apple SSO

### Create a Client (Application) in Apple

Log in to [https://developer.apple.com/account](https://developer.apple.com/account)

Navigate to **Certificates, Identifiers & Profiles -> Identifiers** section and create a new Identifier for your App.

<figure><img src="../../.gitbook/assets/fe697d52-9b2d-493c-ae46-ef3cc3c2e611.png" alt="Apple Developer Identifiers section"><figcaption></figcaption></figure>

Check **"Sign in with Apple"** on the **Capabilities** tab.

<figure><img src="../../.gitbook/assets/d1c0cb07-11a1-4de3-91b4-ee11801d7f47.png" alt="Sign in with Apple capability checkbox"><figcaption></figcaption></figure>

Follow the process and Register your App.

### Create Service

Navigate to **Certificates, Identifiers & Profiles -> Identifiers** section and create a new Identifier for your Service

<figure><img src="../../.gitbook/assets/779f9f4c-b68d-46d9-b599-126e0f38b620.png" alt="Create new Service Identifier"><figcaption></figcaption></figure>

Drill down in the registered service and enable **"Sign in with Apple"**

<figure><img src="../../.gitbook/assets/c7bfc30e-ab79-4b04-9dda-8918f57284de.png" alt="Enable Sign in with Apple for service"><figcaption></figcaption></figure>

Click **"Configure"** button.

Select App ID you created as a **Primary App ID** and add a new **Website URL**

<figure><img src="../../.gitbook/assets/b92cd421-659f-4bca-967c-3e602e09f09c.png" alt="Configure Website URLs for Apple service"><figcaption></figcaption></figure>

Apple allows HTTPS URLs only. In local development, you can tunnel your local Aidbox URL `http://localhost:8080` to HTTPS using [Ngrok](https://ngrok.com) or a similar tunnelling tool.

Follow the process and Register your Service.

### Create Key

Navigate to **Certificates, Identifiers & Profiles -> Keys** section and create a new Key.

Enable **"Sign in with Apple"** and click the "Configure" button.

<figure><img src="../../.gitbook/assets/e33106c5-46a1-4c20-b4e3-28ba030fa5e7.png" alt="Create Key with Sign in with Apple enabled"><figcaption></figcaption></figure>

Select you App as a **"Primary App Id".**

<figure><img src="../../.gitbook/assets/38224bfc-51af-4d11-93ce-ba7fec8cd994.png" alt="Select Primary App ID for key"><figcaption></figcaption></figure>

Register your Key and download the Key.

<figure><img src="../../.gitbook/assets/64770e19-bdd2-47ea-b973-026b333cae89.png" alt="Download registered Apple Key"><figcaption></figcaption></figure>

### Create an IdentityProvider in Aidbox

Login to Aidbox UI.

Use REST Console to execute the request below.

* `<kid>` should be your Key Id\
  ![Apple Key ID location](../../.gitbook/assets/c0983653-1f5d-49c1-9481-b71546e287a8.png)
* `<apple-service-id>`should be your Apple service id, e.g. `local.aidbox.svc`
* `<apple-private-key>` put your private key, that you've downloaded, here.
*   `<team-id>`should be your Apple team Id\\

    <figure><img src="../../.gitbook/assets/bdaf5f74-c4c0-4a5c-ab80-f6fa9a963439.png" alt="Apple Team ID location"><figcaption></figcaption></figure>
* `<your-https-url>`is your Service Website URL you have created before, e.g. `https://38f9-93-103-225-249.ngrok-free.app/auth/callback/apple`

<pre class="language-json"><code class="lang-json"><strong>PUT /fhir/IdentityProvider/apple
</strong>content-type: application/json
accept: application/json

{
  "scopes" : [ "email", "openid" ],
  "system" : "apple",
  "authorize_endpoint" : "https://appleid.apple.com/auth/authorize",
  "kid" : "&#x3C;kid>",
  "client" : {
    "id" : "&#x3C;apple-service-id>",
    "redirect_uri" : "&#x3C;your-https-url>",
    "certificate" : "-----BEGIN PRIVATE KEY\n-----&#x3C;apple-private-key>-----\nEND PRIVATE KEY-----"
  },
  "type" : "apple",
  "resourceType" : "IdentityProvider",
  "title" : "Apple",
  "active" : true,
  "id" : "apple",
  "team_id" : "&#x3C;your-team-id>",
  "token_endpoint" : "https://appleid.apple.com/auth/token",
  "userinfo-source" : "id-token"
}
</code></pre>

### Login into Aidbox using Apple user

Go to the Aidbox login page. You should see **Sign in with Apple** button.

<img src="../../.gitbook/assets/5d0e43b7-c6c0-48aa-885d-173df7d06d33.png" alt="Aidbox login page with Sign in with Apple button" data-size="original">

Press this button and log in to Aidbox using your Apple account.

Make sure you have selected **"Share my email"** option.

<figure><img src="../../.gitbook/assets/a42e1978-dc26-4d3b-bb1e-5939a248f343.png" alt="Apple Share my email option"><figcaption></figcaption></figure>

Since no access policy has been assigned to your user yet, you won’t see much in Aidbox.

Log in again as an admin, then navigate to **IAM -> User** to check the iser created in Aidbox for your Apple account. Click on the user ID to view details.

<figure><img src="../../.gitbook/assets/44910356-d93a-44cd-b0fd-9a87fbe3769c.png" alt="Apple user in Aidbox IAM Users list"><figcaption></figcaption></figure>

User Resource is Aidbox is updated every time the user logs in using an external Identity Provider. The source of the user information is configured using `userinfo-source` element in the IdentityProvider configuration resource.

### Create the AccessPolicy

Use REST Console to execute the request

```json
PUT /fhir/AccessPolicy/apple-admins-policy
content-type: application/json
accept: application/json

{
 "engine": "matcho",
 "matcho": {
  "user": {
   "email": "aleksandr.kislitsyn@health-samurai.io"
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

Now you can log in again with your Apple user, and the user will be granted admin access.

## What's next

* See more about [Aidbox Security](../../access-control/access-control.md)
