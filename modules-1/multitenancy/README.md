---
description: This section aggregates vision and approaches on multitenancy issue
---

# Multitenancy

There are many ways you can [build multitenancy on top of Aidbox](../../security-and-access-control-1/background-information/multitenancy.md). Current section is going to focus on an upcoming into Aidbox first-class multitenancy, which will provide you a guided framework to built your own multitenant application on top of Aidbox.

{% hint style="info" %}
Aidbox first-class multitenancy module and approach is in the active process of crystallising into a final solution. And you may influence on that stage, in order to the module better meet your requirements and expectations by sharing your use case with Health Samurai team.
{% endhint %}

## First-class multitenancy

Key ideas of multitenancy in Aidbox:

* tenant is represented as Tenant resource,
* every resource belongs to a tenant,
* tenant may be nested into another tenant.
