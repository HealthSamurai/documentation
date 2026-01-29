---
description: SMART on FHIR Publish Terms of Use Link Onto the Documentation Page implementation in Aidbox for healthcare app integration.
---

# Publish Terms of Use Link Onto the Documentation Page

## How to add Terms of Use link with API

To set up the link use the following request.

```http
PATCH /AidboxConfig/smartbox
content-type: text/yaml
accept: text/yaml

smartbox:
  terms-of-use-url: https://example.com/terms-of-use
```

{% hint style="info" %}
`terms-of-use-url` should be a link to a publicly accessible terms of use file
{% endhint %}
