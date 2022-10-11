---
description: This guide explains how to set up EHR-level customization
---

# How to set up EHR-level customization

### How to set up EHR logo

To set up logo use the following request.

```http
POST /AidboxConfig
content-type: text/yaml

smartbox:
  logoUrl: https://example.com/ehr-logo.png
id: smartbox
resourceType: AidboxConfig
```

{% hint style="info" %}
logoUrl `should be provided under` smartbox `property`
{% endhint %}

{% hint style="success" %}
`logoUrl` should be a link to a publicly accessible image file
{% endhint %}
