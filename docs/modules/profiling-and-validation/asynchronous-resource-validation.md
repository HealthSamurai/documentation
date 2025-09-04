---
description: Use RPC to run a validation operation to check a resource conformance
---

# Asynchronous resource validation

### Asynchronous Batch Validation `draft`

{% hint style="info" %}
This is a draft API. We appreciate your feedback and ideas in [this discussion](https://github.com/Aidbox/Issues/discussions/409)
{% endhint %}

It may happen that you updated your profiles when data is already in your database or you want to do efficiently load a batch of data and validate it later. API consists of 4 procedures and a couple of resources:

* [aidbox.validation/batch-validation](asynchronous-resource-validation.md#aidbox-validation-batch-validation) **-** run validation
* [aidbox.validation/resources-batch-validation-task](asynchronous-resource-validation.md#aidbox.validation-batch-validation-result) - run validation with [Aidbox Workflow](../../deprecated/deprecated/zen-related/workflow-engine/workflow/README.md)
* [aidbox.validation/batch-validation-result](asynchronous-resource-validation.md#aidbox-validation-batch-validation-result) - inspect results (useful for async mode)
* [aidbox.validation/clear-batch-validation](asynchronous-resource-validation.md#aidbox-validation-clear-batch-validation) - clear validation results

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

#### aidbox.validation/resources-batch-validation-task

You can run validation workflow with rpc method, which creates task for every resource provided in rpc's params fields `include` or `exclude`:

<pre class="language-yaml"><code class="lang-yaml"><strong>POST /rpc
</strong>accept: text/yaml
content-type: text/yaml


method: aidbox.validation/resources-batch-validation-task
params:
  include: ['patient', 'observation']
  
  
# response
params:
  tables:
    - patient
    - observation
status: in-progress
definition: aidbox.validation/resource-types-batch-validation-workflow
id: >-
  7addda33-003e-4892-a1d9-0faffbedf86d
resourceType: AidboxWorkflow
</code></pre>

{% hint style="info" %}
If you specify `include` param, only types you passed will be validated.

If you specify `exclude` param, all types will be validated except the ones you passed.

`include` and `exclude` params cannot be used together.
{% endhint %}

You can check a progress of workflow in Aidbox UI or by rpc method:

```yaml
POST /rpc
accept: text/yaml
content-type: text/yaml


method: awf.workflow/status
params:
  id: 7addda33-003e-4892-a1d9-0faffbedf86d

#response
result:
  resource:
    params:
      tables:
        - patient
        - observation
    result: Finished
    status: done
    outcome: succeeded
    definition: aidbox.validation/resource-types-batch-validation-workflow
    id: >-
      7addda33-003e-4892-a1d9-0faffbedf86d
    resourceType: AidboxWorkflow
```

#### aidbox.validation/batch-validation-result

If you run validation in async mode or aidbox.validation/resources-batch-validation-task, it will respond instantly and run validation in the background. You can get validation results with RPC `aidbox.validation/batch-validation-result`

```yaml
POST /rpc?_format=yaml
content-type: text/yaml

method: aidbox.validation/batch-validation-result
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

{% hint style="info" %}
`aidbox.validation/batch-validation-result` method requires `resourceType` param, which has a default value `BatchValidationRun`. 
So, if you want to get the result from aidbox.validation/resources-batch-validation-task you need pass "AidboxWorkflow" to `resourceType` param. 
{% endhint %}

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

{% hint style="info" %}
If you restart Aidbox you have to start validation over
{% endhint %}
