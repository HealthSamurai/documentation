---
description: Dev endpoint to test matcho engine
---

# $matcho

Matcho DSL is used to define rules for [AccessPolicy](broken-reference) and [Subscription](broken-reference) resources.

## Definition

Matcho DSL syntax is described [here](../../../modules/security-and-access-control/security/evaluation-engines.md#matcho).

## Debug endpoint

`POST /$matcho` is a REST endpoint to test matcho engine, which is used in AccessPolicy and Subscriptions.

```yaml
POST /$matcho

matcho: {'a': 1}
resource: {'a': 2}
context: { user: 'u-1' }

-- 

matcho: {a: 1}
resource: {a: 2}
result:
- expected: 1
  but: 2
  path: [a]
```
