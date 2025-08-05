---
description: This guide explains how you can set up email provider
---

# Setup Email Provider

Aidbox supports different email API services and SMTP protocol.

## Smartbox administrator UI

Use Smartbox administrator UI to set up email provider.

### Setup provider details

To set up email provider details:

1. Click the `Settings` link in the header
2. Click the `Email provider` tab
3. Choose the provider type (`SMTP` or `mailgun`)
4. Populate provider details
5. Press the `Update email provider` button

### Check if provider works

To check provider works:

1. Press the `Send test email` button
2. Enter your email address
3. Press the `Ok` button

{% hint style="info" %}
If you receive the test email message, the provider works
{% endhint %}

## How to set up `mailgun`

Add the lines below to the `.env` file.

```
# Mailgun
BOX_PROVIDER_DEFAULT_TYPE=mailgun
BOX_PROVIDER_DEFAULT_FROM=<YOUR_MAILGUN_FROM_EMAIL>
BOX_PROVIDER_DEFAULT_USERNAME=<YOUR_MAILGUN_USERNAME>
BOX_PROVIDER_DEFAULT_PASSWORD=<YOUR_MAILGUN_PASSWORD>
BOX_PROVIDER_DEFAULT_URL=<YOUR_MAILGUN_URL>
```

## How to set up `Postmark`

Add the lines below to the `.env` file.

```
# Postmark
BOX_PROVIDER_DEFAULT_TYPE=postmark
BOX_PROVIDER_DEFAULT_FROM=<YOUR_POSTMARK_FROM>
BOX_PROVIDER_DEFAULT_API__KEY=<YOUR_POSTMARK_API_KEY>
```

## How to set up `SMTP`

Add the lines below to the `.env` file.

```
# SMTP
BOX_PROVIDER_DEFAULT_TYPE=smtp
BOX_PROVIDER_DEFAULT_SSL=true             # if your SMTP supports SSL
BOX_PROVIDER_DEFAULT_TLS=true             # if your SMTP supports TLS
BOX_PROVIDER_DEFAULT_HOST=<YOUR_SMTP_HOST>
BOX_PROVIDER_DEFAULT_PORT=<YOUR_SMTP_PORT>
BOX_PROVIDER_DEFAULT_FROM=<YOUR_SMTP_FROM_EMAIL>
BOX_PROVIDER_DEFAULT_USERNAME=<YOUR_SMTP_USERNAME>
BOX_PROVIDER_DEFAULT_PASSWORD=<YOUR_SMTP_PASSWORD>
```
