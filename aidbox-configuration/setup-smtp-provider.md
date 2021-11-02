# Setup SMTP-provider

Aidbox allows you to configure SMTP email provider to manage your email communications.

## Adding SMTP-provider

To add new SMTP-provider update your `AidboxConfig` resource with provider credentials.

```json
PUT /AidboxConfig/provider
 
{
    "provider": {
      "smtp-provider": {
            "type": "smtp",
            "host": "mail.host.ru",
            "port": 465,
            "ssl": true,
            "from": "user@host.ru",
            "username": "user@host.ru",
            "password": "password"
      }
    }
}
```



