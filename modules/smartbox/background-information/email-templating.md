---
description: The article explains, how email templating works
---

# Email templating

## `NotificationTemplate` resource

Templates are defined with `NotificationTemplate` resources. The shape of the resource looks like.

```yaml
subject: An interesting subject {{confirm-address}}
template: |-
  <span>Do it!!! </span>
        <a href={{confirm-address}}>{{ confirm-address}}</b>
id: developer-confirm-email
resourceType: NotificationTemplate
```

* `subject` of the email
* `template` is the body of the email
* `id` of the template
* `resourceType` is always `NotificationTemplate`

## How to create a `NotificationTemplate`

To create a `NotificatinTemplate` send the following request in the REST console

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

## Inlining dynamic values in templates

Every template has access to the dynamic values. For example, `reset-user-password` template has access to `user` and `confirm-href` values. To inline a value use `{{dynamic-value-name}}` syntax.

The following example shows usage of the inlining in the `subject` and in the `template` properties.

```http
PUT /NotificationTemplate/reset-user-password
content-type: text/yaml

subject: Reset your password {{user.name.givenName}}
template: |-
  <p>Dear {{user.name.givenName}},<br />
     To reset your password click this </p>
  <a href={{confirm-href}}>link</a>
  <p>Best wishes,<br />
     Acme Inc</p>
```

The full list of available templates and their dynamic values can be found [here](../how-to-guides/set-up-ehr-level-customization.md).
