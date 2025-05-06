---
description: Separate data by tenant
---

# Multitenancy

## Overview

Multitenancy in Aidbox can be achieved with Multibox, API Constructor or AccessPolicies.

### Multibox

Multibox is a type of Aidbox distribution that isolates data to separate databases in the same PostgreSQL cluster and provides separate base URLs for each of them. Separate APIs with different base URLs are used for different tenants. Multibox allows hosting hundreds and thousands of Aidbox services in a single runtime.

{% hint style="success" %}
* Data isolation is guaranteed by the separation of databases and APIs.
* Each tenant has its own unique base URL.
* Each tenant can have a different configuration, e.g. different FHIR versions.
* Query performance of a tenant database is not affected by the data size of other tenants.
{% endhint %}

{% hint style="warning" %}
* Due to the database separation, it may be difficult to perform analytics, migrations, etc. across multiple tenants.
* Data can't be easily shared between tenants, e.g. terminologies, Practitioner, Organization resources etc.
{% endhint %}

### Aidbox API Constructor

Aidbox API Constructor is a tool to define your own REST API routes with middlewares, access checks, etc.\
\
In terms of multitenancy API Constructor allows you to:

* configure API with tenant filters forced on data access;
* create separate APIs for different tenants.

You can fine-tune access to every operation or resource type, for example:

* Restrict access to resources like Patient, Encounter access by tenants, i.e. each tenant can only read/change their own resources.
* Allow `Location` resources read access across all tenants.
* Allow creating `Location` resources only for some specific client.

{% hint style="success" %}
- Flexible configuration at a resource type or operations level.
- Dynamically create new tenants.
- Share common data across multiple tenants, e.g. terminologies, Practitioner, Organization resources etc.
- Data is stored in a single database making it easier to perform analytics, migrations, etc. across all tenants.
{% endhint %}

{% hint style="warning" %}
* Different tenants can't have different configurations, e.g. the same FHIR version across all tenants.
* Multitenancy without isolation requires precise configuration.
{% endhint %}

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

{% embed url="https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples/multitenancy" %}
Aidbox project with API constructor multitenancy example
{% endembed %}

### Aidbox AccessPolicy

Aidbox AccessPolicy can be used to add requirements to requests.\
\
Aidbox FHIR API can have filters to ensure that a client can access only certain data. To separate search request results by a tenant you can add a filter by the tenant identifier. AccessPolicy forces using the tenant id to get access to the tenant data.

{% hint style="success" %}
* Configuration is simpler than with Aidbox API Constructor.
* Dynamically create new tenants.
* Data is stored in a single database making it easier to perform analytics, migrations, etc. across all tenants.
{% endhint %}

{% hint style="warning" %}
* Multitenancy is forced but not automated.
* Different tenants can't have different configurations, e.g. the same FHIR version across all tenants.
{% endhint %}

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}
