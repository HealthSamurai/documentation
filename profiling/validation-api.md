---
description: API to validate resources
---

# Validation API

### Batch Validation `draft`

{% hint style="info" %}
This is a draft API. We appreciate your feedback and ideas in [this discussion](https://github.com/Aidbox/Issues/discussions/409)
{% endhint %}

It may happen that you updated your profiles when data is already in your database or you want to do efficiently load a batch of data and validate it later.  API consists of 3 procedures and a couple of resources:

* [aidbox.validation/batch-validation](validation-api.md#aidbox-validation-batch-validation) **-** run validation
* [aidbox.validation/batch-validation-result](validation-api.md#aidbox-validation-batch-validation-result) - inspect results \(useful for async mode\)
* [aidbox.validation/clear-batch-validation](validation-api.md#aidbox-validation-clear-batch-validation) - clear validation results

#### aidbox.validation/batch-validation

You can validate your existing data with our new rpc `aidbox/batch-validation`:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox/batch-validation
params:
  resource: Patient
  id: pt-validation-run-1
  # you can limit number of resources to validate
  limit: 100 
  # you can stop process on specific number of invalid resources
  errorsThreshold: 10 
  # run validation asynchronously
  async: true
  # specify profiles to validate
  profiles: ['profile-url-1', 'profile-url-2']
  # specify zen schemas to validation
  schemas: ['myapp/Patient', 'us-core.patient/Patient']
  
  
# response
status: 200
result:
  valid: 1543
  invalid: 2
  duration: 3293
  resource: Patient
  id: s2
  problems:
    - resource: {....}
      errors: [{...}, {...}]
```

#### aidbox.validation/batch-validation-result

If you provided 

#### aidbox.validation/clear-batch-validation

#### BatchValidationRun & BatchValidationError Resources

