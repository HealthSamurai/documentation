---
description: Dev endpoint to test matcho engine
---

# $matcho

Macho DSL is used as engine in [AccessPolicy](../security/access-control.md#matcho-engine) and [Subscription](../reactive-api-and-subscriptions/subscriptions-1.md#trigger-format)

### Definition

Matcho DSL definition:

* If **pattern** \(match\) is object, search for inclusion of this object into subject. For example: `{x: 1}` matches `{x: 1, y: 2 ....}`. This algorithm is recursive — `{a: {b: 5}}` matches `{a: {b: 5, ...}...}`
* Objects with one **$enum, $one-of** or **$contains** keys are special cases:
  * **$enum** —  test subject is equal to one of items in the enumeration. `{request-method: {$enum: ['get','post']}}` matches `{request-method: 'post'}`
  * **$contains** — ****if a subject is a collection then search at least one match. `{type: {$contains: {system: 'loinc'}}` matches `{type: [{system: 'snomed'}, {system: 'loinc'}]}`
  * **$one-of —** try to match one of patterns. `{a: {$one-of: [{b: present?}, {c: present?}]} matches {a: {c: 5}}`
* For **array,** match first item in the pattern with first item in the subject. `[1,2]` matches `[1,2,3...]`
* Primitive values \(strings, numbers and booleans\) are compared by value
* If a string starts with `'#'`  — it will be transformed into regex and matched as regex. `{a: '#\\d+'}` matches `{a: '2345'}`
* If a string starts with `'.'` — it's interpreted as a pointer to another path in the subject to compare. For example: `{params: {user_id: '.user.id'}}` matches `{user: {id: 1}, params: {user_id: 1}},` i.e. `user.id == param.user_id`
* There are several special string literals postfixed with the `?`
  * **present?** — matches the subject if it is not null, i.e. `{a: 'present?'}` matches `{a: 5}` or `{a: {b: 6}}`
  * **nil?**  — matches if nil/null — `{a: nil?}` matches `{b: 6}`
  * **not-blank?** — matches not blank string.

### Debug endpoint

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





