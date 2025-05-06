# Setup SMTP provider

Aidbox allows you to configure SMTP email provider to manage your email communications.

{% hint style="info" %}
To enable SMTP please specify optional environment variable [**AIDBOX\_BASE\_URL**](../../../reference/environment-variables/optional-environment-variables.md#aidbox_base_url)**.**
{% endhint %}

## Configuring Mailgun

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## Configuring Postmark

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

## Configuring other SMTP providers

Firstly, get your credentials from your provider:

* host
* port
* from
* username
* password

Then, add your provider using `AidboxConfig`resource:&#x20;

```
PUT /AidboxConfig/config
content-type: application/json
accept: application/json

{
    "provider": {
        "yourProviderId": {
            "type": "smtp",
            "host": "smtp.example.com",
            "port": 465,
            // "ssl": true,
            "tls": true,
            "from": "user@example.com",
            "username": "user@example.com",
            "password": "password"
        }
    }
}
```

Note that you should reference to \<yourProviderId> in the Notification resource:

```
PUT /Notification/notification-1

provider: 'yourProviderId'
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
