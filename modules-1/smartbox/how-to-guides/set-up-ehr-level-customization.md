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

## Sandbox customization

### Confirm address email template

```http
PUT /NotificationTemplate/developer-confirm-email
content-type: text/yaml

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

### Application rejected email template

```
PUT /NotificationTemplate/review-request-rejected
content-type: text/yaml

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

### &#x20;Application approved email template

```
PUT /NotificationTemplate/review-request-approved
content-type: text/yaml

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

### Application suspended email template

```
PUT /NotificationTemplate/suspend-deployed-application
content-type: text/yaml

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

### Application suspended email template

```
PUT /NotificationTemplate/approve-deployed-application
content-type: text/yaml

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
