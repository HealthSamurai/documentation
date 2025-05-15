# Email Providers integration

Aidbox supports sending emails through using `Notification`, `NotificationTemplate`, and, optionally, `AidboxConfig` resources.&#x20;

It is possible to setup any SMTP provider using `AidboxConfig` resource, also there's [Postmark](../../../tutorials/integration-toolkit-tutorials/postmark-integration-tutorial.md) 
and [Mailgun](../../../tutorials/integration-toolkit-tutorials/postmark-integration-tutorial.md) configuration via environment variables.

## Sending email

The email can be sent using `POST /Notification/<notification-id>/$send`request.

## Setup SMTP provider

Explained in the following page:

* [Setup SMTP provider](setup-smtp-provider.md)

## Notification resource

The Notification resource is used to send notifications using a specified provider. Simplest notification can be created like this:&#x20;

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

## See also

* [Email Providers reference](../../../reference/email-providers-reference/)
