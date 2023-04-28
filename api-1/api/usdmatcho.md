---
description: Dev endpoint to test matcho engine
---

# $matcho

Matcho DSL is used to define rules for [AccessPolicy](../../modules-1/security-and-access-control/security/access-control.md) and [Subscription](../reactive-api-and-subscriptions/subscriptions-1.md#trigger-format) resources.

## Definition

Matcho DSL syntax is described [here](../../modules-1/security-and-access-control/security/evaluation-engines.md#matcho).

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
