---
description: This section aggregates vision and approaches on multitenancy issue
---

# Multitenancy

There are many ways you can [build multitenancy on top of Aidbox](../security/multitenancy.md). Current section is going to focus on an upcoming into Aidbox first-class multitenancy, which will provide you a guided framework to built your own multitenant application on top of Aidbox.

## First-class multitenancy

Key ideas of multitenancy in Aidbox:

* tenant is represented as Organization resource,
* every resource belongs to a tenant,
* tenant may be nested into another tenant.
