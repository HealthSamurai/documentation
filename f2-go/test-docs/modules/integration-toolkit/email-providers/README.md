# Email Providers Integration

Aidbox supports sending emails through using `Notification`, `NotificationTemplate`, and, optionally, `AidboxConfig` resources.

It is possible to setup any SMTP provider using `AidboxConfig` resource, also there's [Postmark](../../../tutorials/integration-toolkit-tutorials/postmark-integration-tutorial.md)\
and [Mailgun](../../../tutorials/integration-toolkit-tutorials/postmark-integration-tutorial.md) configuration via environment variables.

## Sending email

The email can be sent using `POST /Notification/<notification-id>/$send`request.

## Setup SMTP provider

Explained in the following page:

{% content-ref url="setup-smtp-provider.md" %}
[setup-smtp-provider.md](setup-smtp-provider.md)
{% endcontent-ref %}

## Notification resource

The Notification resource is used to send notifications using a specified provider. Simplest notification can be created like this:

```
PUT /Notification/notification-1

provider: '<yourProviderId>'
providerData:
  to: recipient@example.com
  subject: my subject
  body: hello
```

It is also possible to create a predefined notification template. The resource supports dynamic content by including a payload that is merged into the template.

```
PUT /Notification/notification-1

provider: '<yourProviderId>'
providerData:
  to: recipient@example.com
  subject: My subject of the message
  template:
    id: notification-template-1
    resourceType: NotificationTemplate
  payload:
    foo:
      bar: zaz
```

## NotificationTemplate resource

The NotificationTemplate resource is used to store and manage the body of email messages. This allows for templated messages to be dynamically generated using placeholders that can be replaced with actual values at runtime.

NotificationTemplate example:

```
PUT /NotificationTemplate/notification-template-1

# foo.bar value is placed in the Notification.providerData.payload object
template: <b>Hello world! {{foo.bar}}</b>
```

## Sending the email

Use `$send` endpoint:

```
POST /Notification/notification-1/$send
```

## See also

{% content-ref url="../../../reference/email-providers-reference/" %}
[email-providers-reference](../../../reference/email-providers-reference/)
{% endcontent-ref %}
