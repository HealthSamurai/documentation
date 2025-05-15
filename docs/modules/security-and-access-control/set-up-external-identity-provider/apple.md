# Apple

## Objectives

* Configure integration with the Sign-in with Apple (SSO) to enable secure login to the Aidbox UI

## Before you begin

* Create an account in Apple Developer portal
* Make sure your Aidbox version is newer than 2503
* Set up the local Aidbox instance using the getting started [guide](../../../getting-started/run-aidbox-locally.md)

## Managing Admin Access to the Aidbox UI Using Apple SSO

### Create a Client (Application) in Apple

Log in to [https://developer.apple.com/account](https://developer.apple.com/account)

Navigate to **Certificates, Identifiers & Profiles -> Identifiers** section and create a new Identifier for your App.

<figure><img src="../../../../.gitbook/assets/b07fae938c044413b036e0f4348c89a7.png" alt=""><figcaption></figcaption></figure>

Check **"Sign in with Apple"** on the **Capabilities** tab.

<figure><img src="../../../../.gitbook/assets/e17daacbd4c045819324389731dd8956.png" alt=""><figcaption></figcaption></figure>

Follow the process and Register your App.

### Create Service

Navigate to **Certificates, Identifiers & Profiles -> Identifiers** section and create a new Identifier for your Service

<figure><img src="../../../../.gitbook/assets/25bd5910ffee4ce0b7eecfc3cb50569c.png" alt=""><figcaption></figcaption></figure>

Drill down in the registered service and enable **"Sign in with Apple"**

<figure><img src="../../../../.gitbook/assets/7c4fb3d5b4364b5aa4776b702bb09b78.png" alt=""><figcaption></figcaption></figure>

Click **"Configure"** button.

Select App ID you created as a **Primary App ID** and add a new **Website URL**

<figure><img src="../../../../.gitbook/assets/540bd8c78b834574b00e6f880450efff.png" alt=""><figcaption></figcaption></figure>

Apple allows  HTTPS URLs only. In local development, you can tunnel your local Aidbox URL `http://localhost:8080` to HTTPS using [Ngrok](https://ngrok.com) or a similar tunnelling tool.

Follow the process and Register your Service.

### Create Key

Navigate to **Certificates, Identifiers & Profiles -> Keys** section and create a new Key.

Enable **"Sign in with Apple"** and click the "Configure" button.

&#x20;

<figure><img src="../../../../.gitbook/assets/8db804353ab64b51b2e8bf800632fde2.png" alt=""><figcaption></figcaption></figure>

Select you App as a **"Primary App Id".**

<figure><img src="../../../../.gitbook/assets/6742c5cc9da14520b49a92e682fca9bb.png" alt=""><figcaption></figcaption></figure>

Register your Key and download the Key.

<figure><img src="../../../../.gitbook/assets/8a8140d4a9e64b0bb7afd7ff930d19ff.png" alt=""><figcaption></figcaption></figure>

### Create an IdentityProvider in Aidbox

Login to Aidbox UI.&#x20;

Use REST Console to execute the request below.

* `<kid>` should be your Key Id\
  ![](<../../../../.gitbook/assets/8fd48c04a6a744b4951733acb3f2fe7c.png>)
* `<apple-service-id>`should be your Apple service id, e.g. `local.aidbox.svc`
* `<apple-private-key>` put your private key, that you've downloaded, here.
*   `<team-id>`should be your Apple team Id\


    <figure><img src="../../../../.gitbook/assets/821ea63a85354acca87ef51b6585acf5.png" alt=""><figcaption></figcaption></figure>
* `<your-https-url>`is your Service Website URL you have created before, e.g.  `https://38f9-93-103-225-249.ngrok-free.app/auth/callback/apple`

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

Go to the Aidbox login page. You should see **Sign in with Apple** button.&#x20;



<img src="../../../../.gitbook/assets/a5b1530c4ea3478a866956c0f04e75d4.png" alt="" data-size="original">



Press this button and log in to Aidbox using your Apple account.

Make sure you have selected **"Share my email"** option.

<figure><img src="../../../../.gitbook/assets/aa97375c950d4d8fbe1724b7568402b4.png" alt=""><figcaption></figcaption></figure>

Since no access policy has been assigned to your user yet, you won’t see much in Aidbox.

Log in again as an admin, then navigate to **IAM -> User** to check the iser created in Aidbox for your Apple account. Click on the user ID to view details.

<figure><img src="../../../../.gitbook/assets/8497790e2d1b435282070acbc1e7f6ed.png" alt=""><figcaption></figcaption></figure>

User Resource is Aidbox is updated every time the user logs in using an external Identity Provider. The source of the user information is configured using `userinfo-source` element in the IdentityProvider configuration resource.&#x20;

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

* See more about Aidbox Security [..](../README.md)
