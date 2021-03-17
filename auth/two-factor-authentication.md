---
description: This article explains 2FA implementation in Aidbox
---

# Two Factor Authentication

{% hint style="warning" %}
This feature is in beta right now. If you have any feedback or comments, reach out to us!
{% endhint %}

{% hint style="info" %}
Two Factor Authentication is not supported for external OAuth 2.0 providers
{% endhint %}

Aidbox supports Two Factor Authentication with TOTP \(time-based one-time passwords\). This article explains how to enable 2FA for a user, login with one-time password, and get an access token for your application. Familiarity with [OAuth 2.0](https://tools.ietf.org/html/rfc6749) and [TOTP](https://tools.ietf.org/html/rfc6238) is suggested. 

### Try demo app with 2FA implementation

{% embed url="https://youtu.be/iJTKeKlJm7g" %}

We've prepared the demo Python/JS/TypeScript app with Aidbox.Dev, so you can run everything on your local environment.

Clone the open-source application [repository](https://github.com/Aidbox/two-factor-auth-template). Follow the instructions to see how does 2FA works

The implemented scenario includes signup and login user flows.

#### Signup

1. The Client sends /auth/signup?client\_id=\[client name\]&response\_type=code to Aidbox
2. The signup form is displayed, the only email field is required for signup.
3. When the form with the email data is submitted, Aidbox generates a registration request, which needs to be confirmed by using a specific confirmation link. 
4. Aidbox generates the link and sends it with a Registration Confirmation email via the defined smtp provider. For demo purposes, the email is sent to the Console output instead of sending a real email.
5. The generated link leads the user to the Signup Set Password form.
6. When the form is submitted with the new password, the user registration is completed. Aidbox responds with generated /auth/token and authorizes the user in the system.

#### Login

The user logs into the system. 2FA is not enabled.

1. The Client sends the following request to Aidbox

```text
GET /auth/login?client_id=web&response_type=token
```

    2. The login form is displayed

    3. The user enters the email/password used in signup flow

    4. Aidbox creates a session for the user. The is logged into Aidbox.

    5. If  2FA is not enabled, the user is redirected to the following URL to establish TOTP authentication. 

```text
GET /auth/two-factor/enable
```

   6. The user clicks on "Enable using Authentificator app" button

   7. The Client sends the following request to Aidbox 

```text
GET /app/auth/two-factor/request
```

   8. Aidbox responds with 2FA form

![](../.gitbook/assets/2fa-form.png)

   9. When the user scans the QR code and enters the token, he is redirected to the 2FA settings page. Aidbox saves that 2FA is enabled for this user into the User.twoFactor attribute.

   10. Next time when the user logs into the system, the TOTP authentication page will be shown. Using the mobile authenticator \(or any other transport\) the user enters the code and gets redirected to the application. You can configure which OAuth 2.0 flow by changing Client configuration and login endpoint query parameters. 

#### Disable 2FA

To disable 2FA for a particular user, redirect the user to the following URL. When the user enters a token, they get redirected to the 2FA settings page. Aidbox sets User.twoFactor.enabled to false.

```text
GET /auth/two-factor/disable
```

#### 

#### Configuration

In the demo app, we defined all configurable resources in the /backend/app/manifest.py file.

#### Client resource

Client resource is required for 2FA process. In our demo app we've generated the following Client resource with the name "app"

```text
auth:
  implicit:
    redirect_uri: 'http://localhost:3000/auth'
first_party: true
grant_types:
  - implicit
id: web
resourceType: Client
```

Read more about Client resource configuration [here](https://app.gitbook.com/@aidbox/s/project/~/drafts/-MVyOIaYZI6lD2jaf35C/auth/implicit)

#### AuthConfig resource

AuthConfig resource is required for 2FA process. In our demo app we've generated the following AuthConfig with the name "app"

{% tabs %}
{% tab title="AuthConfig app" %}
```
twoFactor:
  webhook:
    headers:
      Authorization: Basic dHdvLWZhY3Rvci13ZWJob29rOnR3by1mYWN0b3Itd2ViaG9vaw==
    endpoint: 'http://devbox:8080/webhook/two-factor-confirmation'
  issuerName: Demo
  validPastTokensCount: 5
id: app
resourceType: AuthConfig
```
{% endtab %}
{% endtabs %}

| AuthConfig attribute | meaning |
| :--- | :--- |
| twoFactor.issuerName | Name of the TOTP token issuer that is shown in authenticator |
| twoFactor.validPastTokensCount | Number of previous tokens that are considered valid. Used to improve user experience if standard 30 seconds token lifetime is not enough. |
| twoFactor.webhook.endpoint | Endpoint to send the TOTP token to during login. Used to support scenarios when it's not possible to use the mobile authenticator. For instance, a service integrated with twilio may listen on this address.  |
| twoFactor.webhook.timeout | Timeout for webhook in milliseconds |
| twoFactor.webhook.headers | Key-value headers for webhook |
| theme.styleUrl | URL to external stylesheet to customise how the authentication form looks like |
| theme.title | Title to use on the authentication form |
| theme.brand | Application name to display on the authentication page |



