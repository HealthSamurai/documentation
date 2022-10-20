---
description: This guide explains how you can check all your email templates
---

# Check email templates

### Send all populated templates to your email

```http
POST /rpc
content-type: text/yaml

method: smartbox.portal.rpc/send-email-templates
params:
  email: 'email@example.com' # emails are sent to this email address
```

### Send a certain populated template

```http
POST /rpc
content-type: text/yaml

method: smartbox.portal.rpc/send-email-templates
params:
  email: 'email@example.com'
  template_id: 'reset-user-password' # the only template being sent
```

The list of available email templates can be found [here](set-up-ehr-level-customization.md).

{% hint style="info" %}
1. By default, the RPC method `smartbox.portal.rpc/send-email-templates` is not publicly accessible. Use Aidbox Rest Console to send the RPC requests
2. By default,`admin` user doesn't have `name.givenName and name.familyName properties. If your template expects such properties, you should add it`
{% endhint %}
