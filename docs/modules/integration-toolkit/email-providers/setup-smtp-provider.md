---
description: Set up SMTP email provider configuration for Aidbox notifications and messaging.
---

# Setup SMTP provider

Aidbox allows you to configure SMTP email provider to manage your email communications.

{% hint style="info" %}
To enable SMTP please specify optional environment variable [**BOX_WEB_BASE_URLL**](../../../reference/all-settings.md#web.base-url)**.**
{% endhint %}

## Configuring Mailgun

See [Configuring Mailgun Integration Tutorial](../../../tutorials/integration-toolkit-tutorials/mailgun-integration-tutorial.md).

## Configuring Postmark

See [Configuring Postmark Tutorial](../../../tutorials/integration-toolkit-tutorials/postmark-integration-tutorial.md).

## Configuring other SMTP providers

Firstly, get your credentials from your provider:

* host
* port
* from
* username
* password

Then, configure your SMTP provider using Aidbox settings/environment variables:

| Setting | Environment Variable | Description |
|---------|---------------------|-------------|
| [Default provider type](../../../reference/all-settings.md#provider.default.type) | `BOX_MODULE_PROVIDER_DEFAULT_TYPE` | Set to `smtp` for SMTP providers |
| [Default provider host](../../../reference/all-settings.md#provider.default.host) | `BOX_MODULE_PROVIDER_DEFAULT_HOST` | SMTP server hostname (e.g., `smtp.example.com`) |
| [Default provider port](../../../reference/all-settings.md#provider.default.port) | `BOX_MODULE_PROVIDER_DEFAULT_PORT` | SMTP server port (e.g., `465` or `587`) |
| [Default provider from address](../../../reference/all-settings.md#provider.default.from) | `BOX_MODULE_PROVIDER_DEFAULT_FROM` | Sender email address |
| [Default provider username](../../../reference/all-settings.md#provider.default.username) | `BOX_MODULE_PROVIDER_DEFAULT_USERNAME` | SMTP authentication username |
| [Default provider password](../../../reference/all-settings.md#provider.default.password) | `BOX_MODULE_PROVIDER_DEFAULT_PASSWORD` | SMTP authentication password |
| [Default provider SSL](../../../reference/all-settings.md#provider.default.ssl) | `BOX_MODULE_PROVIDER_DEFAULT_SSL` | Enable SSL connection (`true` / `false`) |
| [Default provider TLS](../../../reference/all-settings.md#provider.default.tls) | `BOX_MODULE_PROVIDER_DEFAULT_TLS` | Enable TLS connection (`true` / `false`) |

### Example configuration

```yaml
BOX_MODULE_PROVIDER_DEFAULT_TYPE: smtp
BOX_MODULE_PROVIDER_DEFAULT_HOST: smtp.example.com
BOX_MODULE_PROVIDER_DEFAULT_PORT: 465
BOX_MODULE_PROVIDER_DEFAULT_FROM: user@example.com
BOX_MODULE_PROVIDER_DEFAULT_USERNAME: user@example.com
BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: password
BOX_MODULE_PROVIDER_DEFAULT_TLS: true
```

### Sending notifications

To send an email notification, create a Notification resource:

```yaml
PUT /Notification/notification-1

provider: smtp
providerData:
  to: recipient@example.com
  subject: My subject of the message
  template:
    id: notificationTemplateId
    resourceType: NotificationTemplate
  payload:
    foo:
      bar: zaz
```
