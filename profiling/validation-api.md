---
description: API to validate resources
---

# Validation API

### Asynchronous Batch Validation `draft`

It may happen that you updated your profiles when data is already in your database or you want to do efficiently load a batch of data and validate it later. You can validate your existing data with our new rpc `aidbox/batch-validation`:

```text
{:method 'aidbox/batch-validation
 :params {
    :resourceType "Patient"
    
 }}
```

