---
description: This guide explains how to set up EHR-level customization
---

# Set up EHR-level customization

## How to set up EHR logo

To set up logo use the following request.

```http
PUT /AidboxConfig/smartbox
content-type: text/yaml

smartbox:
  logoUrl: https://example.com/ehr-logo.png
```

{% hint style="info" %}
`logoUrl` should be a link to a publicly accessible image file
{% endhint %}

## Smartbox (portal) templates customization

### Reset user password

```http
PUT /NotificationTemplate/reset-user-password
content-type: text/yaml

subject: Reset your password
template: |-
  <p>Dear {{user.name.givenName}},<br />
     To reset your password click this </p>
  <a href={{confirm-href}}>link</a>
  <p>Best wishes,<br />
     Acme Inc</p>
```

#### Template parameters

* `user`
  * `email` address of the developer
  * `name`
    * `givenName`  of the developer
    * `familyName` of the developer
* `confirm-href` is the link users open to change their password

## Sandbox email templates customization

### Confirm email address

```http
PUT /NotificationTemplate/developer-confirm-email
content-type: text/yaml

subject: Confirm email address
template: |-
  <p>Dear {{user.name.givenName}},<br />
     Please, verify your email address using this </p>
  <a href={{confirm-address}}>link</a>
  <p>Best wishes,<br />
     Acme Inc</p>
```

#### Template parameters

* `user`
  * `email` address of the developer
  * `name`
    * `givenName`  of the developer
    * `familyName` of the developer
* `confirm-address` is the link new user open to confirm the email exist

### Application rejected

```http
PUT /NotificationTemplate/review-request-rejected
content-type: text/yaml

subject: Review request rejected
template: |-
  <p>Your application was rejected</p>
  <p>Best wishes,<br />
     Acme Inc</p>
```

#### Template parameters

* `user`
  * `email` address of the developer
  * `name`
    * `givenName`  of the developer
    * `familyName` of the developer
* `client`
  * `id` of the application
  * `name` of the application
  * `description` of the application

### &#x20;Application approved

```http
PUT /NotificationTemplate/review-request-approved
content-type: text/yaml

subject: Review request approved
template: |-
  <p>Your application was approved</p>
  <p>Best wishes,<br />
     Acme Inc</p>
```

#### Template parameters

* `user`
  * `email` address of the developer
  * `name`
    * `givenName`  of the developer
    * `familyName` of the developer
* `client`
  * `id` of the application
  * `name` of the application
  * `description` of the application

### Application suspended

```http
PUT /NotificationTemplate/suspend-deployed-application
content-type: text/yaml

subject: Your application has been suspended
template: |-
  <p>Your deployed application was suspended</p>
  <p>Best wishes,<br />
     Acme Inc</p>
```

#### Template parameters

* `user`
  * `email` address of the developer
  * `name`
    * `givenName`  of the developer
    * `familyName` of the developer
* `client`
  * `id` of the application
  * `name` of the application
  * `description` of the application

### Application suspended

```http
PUT /NotificationTemplate/approve-deployed-application
content-type: text/yaml

subject: Your application access has been approved
template: |-
  <p>Your suspended application was deployed</p>
  <p>Best wishes,<br />
     Acme Inc</p>
```

#### Template parameters

* `user`
  * `email` address of the developer
  * `name`
    * `givenName`  of the developer
    * `familyName` of the developer
* `client`
  * `id` of the application
  * `name` of the application
  * `description` of the application
