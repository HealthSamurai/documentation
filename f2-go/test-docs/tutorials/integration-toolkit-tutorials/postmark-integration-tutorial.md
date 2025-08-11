---
description: How to configure sending email using Postmark
---

# Postmark Integration Tutorial

Postmark is an email delivery service for sending, receiving, and tracking emails. Aidbox offers integrations with Postmark to simplify sending notifications via email.

### Create Postmark account

You can do that account by following [Postmark Documentation](https://postmarkapp.com/manual).

### Add environment variables to the docker-compose.yaml

```yaml
BOX_PROVIDER_POSTMARK__PROVIDER_FROM: Sender's Name <noreply@{{YOUR_DOMAIN}}>
BOX_PROVIDER_POSTMARK__PROVIDER_API__KEY: <api-key>
```

You can find more about Postmark environment variables [here.](../../reference/email-providers-reference/postmark-environment-variables.md)

### Creating `NotificationTemplate` resource

NotificationTemplate is a resource that stores the body of a mail message.

```yaml
PUT /NotificationTemplate/notification-template-1
content-type: text/yaml
accept: text/yaml

template: <b>Hello world! Mustache value from Notification.providerData.payload - {{foo.bar}}</b>
```

### Creating `Notification` resource

```yaml
PUT /Notification/notification-1
content-type: text/yaml
accept: text/yaml

provider: 'postmark-provider'         # Provider id
providerData:
  to: recipient@example.com          # Email of recipient
  subject: My subject of the message # subject of email
  template:                          # Notification template 
    id: notification-template-1
    resourceType: NotificationTemplate
  payload:                           # Data that can be used in the template
    foo:
      bar: zaz
```

### Call notify method

```yaml
POST /Notification/notification-1/$send
```
