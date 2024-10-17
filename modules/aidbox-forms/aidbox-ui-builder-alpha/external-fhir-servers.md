---
description: This article outlines how to use Aidbox Forms with external FHIR servers.
---

# Overview

Aidbox Forms module uses Aidbox's FHIR API capabilities to store all it's data. 
But sometimes it's appropriate to use other FHIR backends. 
Aidbox Forms provides a simple and granular way to specify external FHIR servers
for data management.

# Data Domains

Data used by `Aidbox Forms` has different nature and can have different storage requirements and strategies.

We split data in 4 domains, and 3 of them can have it's own FHIR backend:

- `System resources` - non-FHIR resources, used by `Aidbox Forms` for extended functionality.
- `Content resources` - `Questionnaire` resource, the main focus and result of `Forms Builder`.
- `Terminology resources` - `ValueSets` and `Concepts`.
- `Production resources` - `QuestionnaireResponse` resource and other FHIR resources that contain PHI/Production data.

> Because `system resources` are non FHIR resources - there is no FHIR backend configuration for them.

# Storage Defaults

By default all data is stored in Aidbox and there is no need for any configuration.

# Storage Configuration

Storage configuration lives in **default** `SDCConfig` resource.

> IMPORTANT: config should have `default: true` property set
> use REST API to manage `SDCConfig` config
> Read more about [SDCConfig](../../modules/aidbox-forms/aidbox-ui-builder-alpha/configuration.md)

## Storage Configuration structure

For every domain we have separate key:

- `form-store` - content resources domain
- `data-store` - production resources domain
- `term-server` - terminology resources domain

To enable external FHIR Backend we should specify next fields for it:

- `endpoint` - URI to FHIR API
- `headers`(optional) - Additional headers (Authorization for example)

Example: 

```yaml
[store-key]:
    endpoint: 'http://my-ehr.com/fhir'
    headers:
        Authorization: 'Basic cm9vdDpzZWNyZXQ='
```

### Form Storage 

> `Questionnaire` resource only

```yaml
form-store:
    endpoint: "https://fhir-server.com/fhir"
```

> WARN: assembled forms and components are not yet supported with external storage.

### Data Storage

> `QuestionnaireResponse` and all FHIR Resources except `Questionnaire`.

This store will be used as source in `$populate` operations, and as target in `$extract`+`$process-response` operations

```yaml
data-store:
    endpoint: "https://fhir-server.com/fhir"
```

### Terminology service

> `ValueSet` search and `ValueSet/$expand` operation

```yaml
term-service:
    endpoint: "https://fhir-server.com/fhir"
```

## Full config example:

```yaml
POST /SDCConfig
content-type: text/yaml

id: cfg-1
resourceType: SDCConfig
name: Config with external fhir servers
default: true
form-store: 
    endpoint: 'http://forms-repo.com'
data-store: 
    endpoint: 'http://my-ehr.com/fhir'
    headers:
        Authorization: 'Basic cm9vdDpzZWNyZXQ='
term-server:
    endpoint: 'http://tx.com/fhir'
```


## Multitenancy Support

- Default system `SDCCOnfig` (for `root` Organization) does not take effect on tenants
- Every tenant have their own default config and thus separate FHIR backends setup


<!-- ## Proxied API -->

<!-- ``` -->
<!-- CRUD /sdc/[rt] -->
<!-- ``` -->

<!-- ``` -->
<!-- CRUD /sdc/[rt]/[id] -->
<!-- ``` -->

<!-- ``` -->
<!-- GET /sdc/ValueSet/$expand -->
<!-- ``` -->


