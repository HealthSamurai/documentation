---
description: This guide explains how you can set up email provider
---

# Setup email provider

Aidbox supports different [email API services](../../../integrations/email-providers.md) and [SMTP](../../../aidbox-configuration/setup-smtp-provider.md) protocol.

### How to set up `mailgun`

Add the lines below to the `.env` file.

```
# Mailgun
BOX_PROVIDER_DEFAULT_TYPE=mailgun
BOX_PROVIDER_DEFAULT_FROM=<YOUR_MAILGUN_FROM_EMAIL>
BOX_PROVIDER_DEFAULT_USERNAME=<YOUR_MAILGUN_USERNAME>
BOX_PROVIDER_DEFAULT_PASSWORD=<YOUR_MAILGUN_PASSWORD>
BOX_PROVIDER_DEFAULT_URL=<YOUR_MAILGUN_URL>
```

### How to set up `Postmark`

Add the lines below to the `.env` file.

```
# Postmark
BOX_PROVIDER_DEFAULT_TYPE=postmark
BOX_PROVIDER_DEFAULT_FROM=<YOUR_POSTMARK_FROM>
BOX_PROVIDER_DEFAULT_API__KEY=<YOUR_POSTMARK_API_KEY>
```

### How to set up `SMTP`

Add the lines below to the `.env` file.

```
# SMTP
BOX_PROVIDER_DEFAULT_TYPE=smtp
BOX_PROVIDER_DEFAULT_SSL=true
BOX_PROVIDER_DEFAULT_HOST=<YOUR_SMTP_HOST>
BOX_PROVIDER_DEFAULT_PORT=<YOUR_SMTP_PORT>
BOX_PROVIDER_DEFAULT_FROM=<YOUR_SMTP_FROM_EMAIL>
BOX_PROVIDER_DEFAULT_USERNAME=<YOUR_SMTP_USERNAME>
BOX_PROVIDER_DEFAULT_PASSWORD=<YOUR_SMTP_PASSWORD>
```
