---
description: Integrate SendGrid email service with Aidbox for sending notifications using NotificationTemplates and environment config.
---

# SendGrid integration tutorial

SendGrid (Twilio) is an email delivery service for sending, receiving, and tracking emails. Aidbox offers integrations with SendGrid to simplify sending notifications via email.

### Create SendGrid account

You can create an account by following [SendGrid Documentation](https://docs.sendgrid.com/).

### Add environment variables to the docker-compose.yaml

```yaml
BOX_MODULE_PROVIDER_SENDGRID_API_KEY: <your_api_key>
BOX_MODULE_PROVIDER_SENDGRID_FROM: Sender's Name <noreply@{{YOUR_DOMAIN}}>
BOX_MODULE_PROVIDER_SENDGRID_DATARESIDENCY: eu
```

`BOX_MODULE_PROVIDER_SENDGRID_DATARESIDENCY` specifies the data residency region for the SendGrid API. Set to `eu` to use the European region. When not set, the global (US) region is used.

You can find more about SendGrid environment variables [here.](../../reference/email-providers-reference/sendgrid-environment-variables.md)

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

provider: 'sendgrid'                    # Provider id
providerData:
  to: recipient@example.com             # Email of recipient
  subject: My subject of the message    # subject of email
  template:                             # Notification template
    id: notification-template-1
    resourceType: NotificationTemplate
  payload:                              # Data that can be used in the template
    foo:
      bar: zaz
```

### Call notify method

```yaml
POST /Notification/notification-1/$send
```

### Example of email sent from the template above

The recipient will receive an email with the subject "My subject of the message" and the body: **Hello world! Mustache value from Notification.providerData.payload - zaz**
