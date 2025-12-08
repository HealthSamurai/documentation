---
description: >-
  This article explains how to enable and configure Two Factor Authentication in
  Aidbox
---

# Two Factor Authentication

Aidbox supports Two Factor Authentication (2FA) with TOTP (time-based one-time passwords). 2FA adds an extra layer of security by requiring users to provide a second form of verification in addition to their password.

{% hint style="info" %}
Two Factor Authentication is not supported for external OAuth 2.0 providers
{% endhint %}

## How to enable 2FA for specific user

{% stepper %}
{% step %}
#### Login

Request `GET /auth/login`. User enters credentials and logs in.
{% endstep %}

{% step %}
#### Enable 2FA

Request `GET /auth/two-factor`. User receives a form with an "Enable" button. After clicking the button, they receive the OTPAuth URL encoded as a QR code.
{% endstep %}

{% step %}
#### Setting up authenticator app

Encoded OTPAuth URL (QR) has the following structure:

```
otpauth://totp/<LABEL>?secret=<SECRET>&issuer=<ISSUER>
```

* LABEL - consists of two parts `<Issuer>:<UserEmailOrId>`. Issuer in label is the same as in `issuer`.
* SECRET - random byte sequence encoded in Base32. Secret is stored in the `User` resource under the `User.twoFactor.secret` attribute.
* ISSUER - same as in `LABEL`. Default value is `Aidbox`, but can be configured via `AuthConfig.twoFactor.issuerName`.

<br>

User scans this QR code with their authenticator app. After scanning, the app generates a confirmation token that the user enters. If successful, 2FA is enabled for this user and the User resource stores `User.twoFactor.enabled = true`.

```json
{
 "password": "<password>",
 "twoFactor": {
  "enabled": true,
  "secretKey": "<secret-key>"
 },
 "id": "<user-id>",
 "resourceType": "User"
}
```
{% endstep %}
{% endstepper %}

On the next login attempt, the user will be prompted to enter a TOTP code from their authenticator app.

## How to disable 2FA for specific user

{% stepper %}
{% step %}
#### Login

Request `GET /auth/login`. User enters credentials and logs in.
{% endstep %}

{% step %}
#### Disable 2FA

Request `GET /auth/two-factor`. User receives a form with a "Disable" button. After clicking the button, they receive a confirmation form.
{% endstep %}

{% step %}
#### Confirm disabling 2FA

User enters a TOTP code from their authenticator app to confirm disabling. If successful, 2FA is disabled for this user and the User resource stores `User.twoFactor.enabled = false`.

```json
{
 "password": "<password>",
 "twoFactor": {
  "enabled": false,
  "secretKey": "<secret-key>"
 },
 "id": "<user-id>",
 "resourceType": "User"
}
```
{% endstep %}
{% endstepper %}

## Configuration

2FA may be configured via `AuthConfig` resource.

| AuthConfig attribute             | meaning                                                                                                                                   |
| -------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| `twoFactor.issuerName`           | Name of the TOTP token issuer that is shown in authenticator                                                                              |
| `twoFactor.validPastTokensCount` | Number of previous tokens that are considered valid. Used to improve user experience if standard 30 seconds token lifetime is not enough. |

## See also

{% content-ref url="../../reference/system-resources-reference/iam-module-resources.md#authconfig" %}
[#authconfig](../../reference/system-resources-reference/iam-module-resources.md#authconfig)
{% endcontent-ref %}
