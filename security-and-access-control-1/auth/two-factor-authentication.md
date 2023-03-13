---
description: This article explains 2FA implementation in Aidbox
---

# Two Factor Authentication

{% hint style="info" %}
Two Factor Authentication is not supported for external OAuth 2.0 providers
{% endhint %}

Aidbox supports Two Factor Authentication with TOTP (time-based one-time passwords). This article explains how to enable 2FA for a user, login with one-time password, and get an access token for your application. Familiarity with [OAuth 2.0](https://tools.ietf.org/html/rfc6749) and [TOTP](https://tools.ietf.org/html/rfc6238) is suggested.

## Try demo app with 2FA implementation

{% embed url="https://youtu.be/N_ZkebvqM24" %}

We've prepared the demo Python/TypeScript app with Devbox, so you can run everything in your local environment. The implemented scenario includes signup and login user flows.

Clone the open-source application [repository](https://github.com/Aidbox/two-factor-auth-template). Follow the instructions to see how does 2FA work.

First of all, you need to define 3 resources in the [manifest.py file](https://github.com/Aidbox/two-factor-auth-template/blob/58a951dd21778488ec00eb7b6ca085f40bd829d6/backend/app/manifest.py)

### 1. AuthConfig

`AuthConfig` resource is required for 2FA process. In the demo app, we've generated the following `AuthConfig` with the name `app`.

{% tabs %}
{% tab title="AuthConfig app" %}
```yaml
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

| AuthConfig attribute             | meaning                                                                                                                                                                                                       |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `twoFactor.issuerName`           | Name of the TOTP token issuer that is shown in authenticator                                                                                                                                                  |
| `twoFactor.validPastTokensCount` | Number of previous tokens that are considered valid. Used to improve user experience if standard 30 seconds token lifetime is not enough.                                                                     |
| `twoFactor.webhook.endpoint`     | Endpoint to send the TOTP token to during login. Used to support scenarios when it's not possible to use the mobile authenticator. For instance, a service integrated with twilio may listen on this address. |
| `twoFactor.webhook.timeout`      | Timeout for webhook in milliseconds                                                                                                                                                                           |
| `twoFactor.webhook.headers`      | Key-value headers for webhook                                                                                                                                                                                 |
| `theme.styleUrl`                 | URL to external stylesheet to customise how the authentication form looks like                                                                                                                                |
| `theme.title`                    | Title to use on the authentication form                                                                                                                                                                       |
| `theme.brand`                    | Application name to display on the authentication page                                                                                                                                                        |

### 2. Client

`Client` resource is required for 2FA process. In the demo app we've generated the following Client resource with the name "web"ya

```yaml
auth:
  implicit:
    redirect_uri: 'http://localhost:3000/auth'
first_party: true
grant_types:
  - implicit
id: web
resourceType: Client
```

Read more about Client resource configuration [here](https://app.gitbook.com/@aidbox/s/project/\~/drafts/-MVyOIaYZI6lD2jaf35C/auth/implicit)

### 3. Provider

Provider resource belongs to the `AidboxConfig`. It defines the transport for sending email notifications, which is part of the signup flow. Since we do not want to send real emails in the demo app, we'll send notifications to the Console output stream. You can find the generated Provider resource below

```yaml
provider:
  console:
    type: console
  default: console
id: provider
resourceType: AidboxConfig
```

![](../../.gitbook/assets/group-4-1-.png)

![](../../.gitbook/assets/group-6.png)

![2FA Form](../../.gitbook/assets/2fa-form.png)

When the user scans the QR code and enters the token, he is redirected to the 2FA settings page. Aidbox saves that 2FA is enabled for this user into the User.twoFactor attribute.

Next time when the user logs into the system, the TOTP authentication page will be shown. Using the mobile authenticator (or any other transport) the user enters the code and gets redirected to the application. You can configure which OAuth 2.0 flow by changing Client configuration and login endpoint query parameters.

![](../../.gitbook/assets/group-7.png)

### Disable 2FA

To disable 2FA for a particular user, redirect the user to the following URL. When the user enters a token, they get redirected to the 2FA settings page. Aidbox sets `User.twoFactor.enabled` to `false`.

```
GET /auth/two-factor/disable
```
