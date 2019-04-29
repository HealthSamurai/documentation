---
description: Aidbox provides access to PostgreSQL sequences through REST API.
---

# Sequence API

In some situations you want to enumerate your resources with increasing counter or use global sequences for your app needs. Aidbox provides handy access to [PostgreSQL sequences](https://www.postgresql.org/docs/current/sql-createsequence.html) through REST API.

## Create Sequence

You can create named sequence by posting PGSequence resource. We use explicit naming for sequences, so **id** element is required!

{% code-tabs %}
{% code-tabs-item title="create-sequence.yaml" %}
```yaml
POST /PGSequence

id: pt_seq
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Other PGSequence attributes you can specify:

| attr | type | desc |
| :--- | :--- | :--- |
| **start** | integer | initial sequence value \(default 1\) |
| **cycle** | boolean | cycle after reaching sequence max/min value |
| **increment** | integer | sequence step \(default 1\) |
| **maxval** | integer | max value to cycle |
| **minval** | integer | min value to cycle |

## Get next value

Now you can transactionally move sequence forward and reserve next value:

```yaml
POST /PGSequence/pt_seq

# response

id: pt_seq
value: 1
```

Each call will increment sequence.

You can get range of values by providing **count** parameter in body.

```yaml
POST /PGSequence/pt_seq

count: 5

# response 200

id: pt_seq
values: [2,3,4,5,6]
```

## Read current value

You can read current state of sequence without incrementing it with `GET /PGSequence/[id]`

```yaml
GET /PGSequence/pg_seq

# response

id: pt_seq
value: 1
```

## Destroy Sequence

You can drop sequence with `DELETE /PGSequence/[id]`

```yaml
DELETE /PGSequence/pt_seq

# response 200

id: pt_seq
value: 1
```

