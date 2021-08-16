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

#### Prepare data

To illustrate let's create some invalid data in Aidbox:

```yaml
POST /Patient
content-type: text/yaml

id: 'pt1'
birthDate: '1980-03-05'
```

Break data from DB Console:

```sql
update patient 
set resource = resource || '{"ups": "extra"}'
where id = 'pt1' 
returning *
```

#### aidbox.validation/batch-validation

You can validate your existing data with our new rpc `aidbox.validation/batch-validation`:

```yaml
POST /rpc
content-type: text/yaml

method: aidbox.validation/batch-validation
params:
  # resourceType to validate
  resource: Patient
  id: pt-validation-run-1
  # you can limit number of resources to validate
  limit: 100 
  # you can stop process on specific number of invalid resources
  errorsThreshold: 10 
  # where section of resources query
  filter: "resource#>>'{birthDate}' is not null"
  ## run validation asynchronously
  # async: true
  ## specify profiles to validate
  # profiles: ['profile-url-1', 'profile-url-2']
  ## specify zen schemas to validation
  #schemas: ['myapp/Patient', 'us-core.patient/Patient']
  
  
  
# response
result:
  id: pt-validation-run-2
  valid: 0
  invalid: 1
  duration: 15
  problems:
    - resource:
        id: pt1
        ups: extra
        meta:
          createdAt: '2021-08-05T16:36:37.723008+03:00'
          versionId: '1224'
          lastUpdated: '2021-08-05T16:36:37.723008+03:00'
        birthDate: '1980-03-05'
        resourceType: Patient
      errors:
        - path:
            - ups
          message: extra property
```

#### aidbox.validation/batch-validation-result

If you run validation in async mode, it will respond instantly and run validation in the background. You can get validation results with  RPC `aidbox.validation/batch-validation-result`

```yaml
POST /rpc?_format=yaml
content-type: text/yaml

method: aidbox.validation/batch-validation-results
params:
  id: pt-validation-run-1
   
# response
status: 200
result:
  valid: 1543
  invalid: 2
  duration: 3293
  problems:
    - resource: {....}
      errors: [{...}, {...}]
```

#### aidbox.validation/clear-batch-validation

When you do not need results of this validation you can clean up resources with:

```yaml
POST /rpc?_format=yaml
content-type: text/yaml

method: aidbox.validation/clear-batch-validation
params:
  id: pt-validation-run-1
```

#### BatchValidationRun & BatchValidationError Resources

When you run validation operation aidbox internally creates resource BatchValidationRun and put errors of validation in BatchValidationError. You can access these resources through standard CRUD/Search API

```yaml
GET /BatchValidationError?.run.id=pt-validation-run-2&_format=yaml&_result=array

# response
- run:
    id: pt-validation-run-2
    resourceType: BatchValidationRun
  errors:
    - path:
        - ups
      message: extra property
  resource:
    id: pt1
    resourceType: Patient
  id: pt-validation-run-2-Patient-pt1
```

