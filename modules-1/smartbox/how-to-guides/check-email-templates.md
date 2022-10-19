---
description: This guide explains how you can check all your email templates
---

# Check email templates

### Send populated templates to your email

```http
POST /rpc
content-type: text/yaml

method: smartbox.portal.rpc/send-email-templates
params:
  email: 'email@example.com'
```

{% hint style="info" %}
1. By default, the RPC method is not publicly accessible. Use Aidbox Rest Console to make the RPC call
2. By default `admin` user doesn't have `name.givenName and name.familyName properties. If your template expects such properties, you should add it`
{% endhint %}
