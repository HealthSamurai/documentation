# Sequence API

Aidbox provides access to[ PostgreSQL sequences](https://postgrespro.ru/docs/postgrespro/11/sql-createsequence) through REST API.

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

## Get next value

Now you can transactionally move sequence forward:

```yaml
POST /PGSequence/pt_seq

# response

id: pt_seq
value: 1
```

Each call will increment sequence

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

