# Mailgun environment variables

### BOX\_PROVIDER\_MAILGUN\_\_PROVIDER\_URL

```
BOX_PROVIDER_MAILGUN__PROVIDER_URL=https://api.mailgun.net/v3/<your_domain>/messages
```

Mailgun API url.

### BOX\_PROVIDER\_MAILGUN\_\_PROVIDER\_FROM

```
BOX_PROVIDER_MAILGUN__PROVIDER_FROM=<string>
```

`<string>` would go into _from_ field. Usually looks like in these: `example@example.com`,  `John Doe <noreply@example.com>`.

### BOX\_PROVIDER\_MAILGUN\_\_PROVIDER\_PASSWORD

```
BOX_PROVIDER_MAILGUN__PROVIDER_PASSWORD=<api_secret>
```

`<api_secret>` is your Mailgun API secret.
