---
description: This guide explains how to set up EHR-level customization
---

# Set up EHR-level customization

### How to set up EHR logo

To set up logo use the following request.

```http
PUT /AidboxConfig/smartbox
content-type: text/yaml

smartbox:
  logoUrl: https://example.com/ehr-logo.png
```

{% hint style="info" %}
`logoUrl` should be a link to a publicly accessible image file
{% endhint %}
