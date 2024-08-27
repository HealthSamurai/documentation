# Setup SMTP provider

Aidbox allows you to configure SMTP email provider to manage your email communications.

{% hint style="info" %}
To enable SMTP please specify optional environment variable[`AIDBOX_BASE_URL`](../reference/configuration/environment-variables/optional-environment-variables.md)
{% endhint %}

## Add SMTP provider

Update your `AidboxConfig` resource with provider credentials to add a new SMTP provider

```json
PUT /AidboxConfig/provider

{
    "provider": {
        "smtp-provider": {
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

You can set either `"ssl"` or `"tls"` depending what your SMTP supports.
