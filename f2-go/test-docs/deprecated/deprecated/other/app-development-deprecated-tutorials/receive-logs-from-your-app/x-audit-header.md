---
description: Add custom fields to logs
---

# X-Audit Header

If you want to add more information to logs, you can use X-Audit header in the format `base64(json)` — all properties from this json will be added to `w/req` and `w/resp` logs.

```yaml
POST /Operation
X-Audit: eyJyb2xlIjogIm51cnNlIn0=
# base64({"role": "nurse"})

...body

# in logs
{ev: 'w/req', ....., role: 'nurse'}
{ev: 'w/resp', ....., role: 'nurse'}
```

