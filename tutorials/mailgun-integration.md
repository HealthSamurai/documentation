---
description: How to configure sending email using Mailgun
---

# Mailgun integration

Mailgun is an email delivery service for sending, receiving, and tracking emails. Aidbox offers integrations with Mailgun to simplify sending notifications via email.&#x20;

### Create Mailgun account

You can do that account by following [Mailgun Documentation](https://documentation.mailgun.com/en/latest/).

### Add environment variables to the docker-compose.yaml

```yaml
BOX_PROVIDER_MAILGUN__PROVIDER_URL: https://api.mailgun.net/v3/{{YOUR_DOMAIN}}/messages
BOX_PROVIDER_MAILGUN__PROVIDER_TYPE: mailgun
BOX_PROVIDER_MAILGUN__PROVIDER_FROM: Sender's Name <noreply@{{YOUR_DOMAIN}}>
BOX_PROVIDER_MAILGUN__PROVIDER_USERNAME: api
BOX_PROVIDER_MAILGUN__PROVIDER_PASSWORD: <password>
```

You can find more about Mailgun environment variables [here.](../reference/configuration/environment-variables/mailgun-environment-variables.md)

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

provider: 'mailgun-provider'         # Provider id
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

### Example of email sent from the template above

![image](https://user-images.githubusercontent.com/43318093/186183697-aa0a23d9-0c0e-43a9-9980-a0dd6c170625.png)
