# SendGrid Environment Variables

### BOX\_MODULE\_PROVIDER\_SENDGRID\_API\_KEY

```
BOX_MODULE_PROVIDER_SENDGRID_API_KEY=<api_key>
```

`<api_key>` is your SendGrid API key.

### BOX\_MODULE\_PROVIDER\_SENDGRID\_FROM

```
BOX_MODULE_PROVIDER_SENDGRID_FROM=<string>
```

`<string>` would go into _from_ field. Usually looks like: `example@example.com`, `John Doe <noreply@example.com>`.

### BOX\_MODULE\_PROVIDER\_SENDGRID\_DATARESIDENCY

```
BOX_MODULE_PROVIDER_SENDGRID_DATARESIDENCY=<region>
```

`<region>` specifies the data residency region for the SendGrid API. Set to `eu` to use the European region. When not set, the default region is used.
