# CRUD internals


They all are unlogged queries.

## Create

```http
POST /fhir/Patient

gender: male
```

```sql
WITH _txid AS (
  SELECT NULL
), inserted AS (
  INSERT INTO "patient" AS t (
    id, txid, status, resource
  )
  SELECT
    gen_random_uuid(),
    nextval('transaction_id_seq'::text),
    'created',
    '{"gender":"male"}'
  RETURNING *
)
SELECT *
FROM (
  SELECT
    id,
    txid,
    ts,
    resource_type,
    status::text AS status,
    resource,
    cts
  FROM inserted
  LIMIT 1
) _
LIMIT 2;
```

## Read
```http
GET /fhir/Patient/pt1
```

```sql
SELECT *
FROM (
  (
    SELECT
      id,
      txid,
      ts,
      resource_type,
      status::text AS status,
      resource,
      cts
    FROM "patient" AS t
    WHERE (t.id = 'pt1')
  )
  UNION
  (
    SELECT
      id,
      txid,
      ts,
      resource_type,
      status::text AS status,
      resource,
      cts
    FROM "patient_history" AS t
    WHERE (
      t.id = 'pt1'
      AND t.status = 'deleted'
    )
  )
) _
ORDER BY ts DESC, txid DESC
LIMIT 1;
```

## Update

```http
PUT /fhir/Patient/83286b83-72c8-4956-9a67-e7f770762a19

gender: male
```

```sql
WITH inserted AS (
  INSERT INTO "patient" AS t (
    id, txid, status, resource
  )
  SELECT
    '83286b83-72c8-4956-9a67-e7f770762a19',
    nextval('transaction_id_seq'::text),
    'created',
    '{}'
  ON CONFLICT (id) DO UPDATE
    SET
      txid = EXCLUDED.txid,
      status = 'updated',
      ts = current_timestamp,
      resource = EXCLUDED.resource
  WHERE (t.resource <> EXCLUDED.resource)
  RETURNING *
),
archived AS (
  INSERT INTO "patient_history" (
    id, txid, cts, ts, status, resource
  )
  SELECT
    id,
    txid,
    cts,
    ts,
    status,
    resource
  FROM "patient"
  WHERE (
    id = '83286b83-72c8-4956-9a67-e7f770762a19'
    AND ('updated' IN (SELECT status FROM inserted))
  )
  ON CONFLICT (id, txid) DO UPDATE
    SET
      status = EXCLUDED.status,
      ts = EXCLUDED.ts,
      cts = EXCLUDED.cts,
      resource = EXCLUDED.resource
  RETURNING *
)
SELECT *
FROM (
  (
    SELECT
      id,
      txid,
      ts,
      resource_type,
      status::text AS status,
      resource,
      cts
    FROM inserted
    LIMIT 1
  )
  UNION ALL
  (
    SELECT
      id,
      txid,
      ts,
      resource_type,
      'unchanged' AS status,
      resource,
      cts
    FROM "patient" AS t
    WHERE (id = '83286b83-72c8-4956-9a67-e7f770762a19')
    LIMIT 1
  )
) _
LIMIT 2;
```


## Delete
```http
DELETE /fhir/Patient/83286b83-72c8-4956-9a67-e7f770762a19
```

```sql
WITH deleted AS (
  DELETE FROM "patient" AS t
  WHERE (t.id = '83286b83-72c8-4956-9a67-e7f770762a19')
  RETURNING *
),
archived AS (
  INSERT INTO "patient_history" (
    id, txid, cts, ts, status, resource
  )
  SELECT
    id,
    txid,
    cts,
    ts,
    status,
    resource
  FROM "patient"
  WHERE (
    id = '83286b83-72c8-4956-9a67-e7f770762a19'
    AND (
      ('created' IN (SELECT status FROM deleted)) OR
      ('updated' IN (SELECT status FROM deleted))
    )
  )
  ON CONFLICT (id, txid) DO UPDATE
    SET
      status = EXCLUDED.status,
      ts = EXCLUDED.ts,
      cts = EXCLUDED.cts,
      resource = EXCLUDED.resource
  RETURNING *
),
archived_deleted AS (
  INSERT INTO "patient_history" (
    id, txid, ts, status, resource
  )
  SELECT
    id,
    nextval('transaction_id_seq'::text),
    current_timestamp,
    'deleted',
    resource
  FROM "patient"
  WHERE (
    (
      ('created' IN (SELECT status FROM deleted)) OR
      ('updated' IN (SELECT status FROM deleted))
    )
    AND id = '83286b83-72c8-4956-9a67-e7f770762a19'
  )
  ON CONFLICT (id, txid) DO NOTHING
  RETURNING *
)
SELECT * FROM archived_deleted;
```

## See also
- [How to delete-data](../tutorials/crud-search-tutorials/delete-data.md) tutorial
- [Set up uniqueness in resource](/tutorials/crud-search-tutorials/set-up-uniqueness-in-resource.md) tutorial
