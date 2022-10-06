---
description: Dev endpoint to test matcho engine
---

# $matcho

Macho DSL is used as engine in [AccessPolicy](../security-and-access-control-1/security/access-control.md#matcho-engine) and [Subscription](../api-1/reactive-api-and-subscriptions/subscriptions-1.md#trigger-format)

## Definition

Matcho DSL definition is described [here](../security-and-access-control-1/security/access-control.md#matcho-engine)

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
