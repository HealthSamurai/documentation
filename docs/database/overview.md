# Database Overview
Aidbox uses PostgreSQL as its database engine, leveraging its robust JSONB capabilities, reliability, and performance for healthcare data storage.

## Storage model
All FHIR data stored in PostgreSQL JSONb data format.
It gives better performance and flexibility. For each resource their own table

```SQL
select * from Patient;
```

See:
- [Database Schema & Storage Format](./database-schema-storage-format.md)

## CRUD interactions
You can manually create/update/delete resources using SQL queries.

### Create

Just insert into resource table
```SQL
insert into Patient (id, ts, resources,  ...... )
values (...., '{}', '{"gender": "male", "birthDate": "2025-01-01"}')
```

Versioning - global sequence ... `next_val()` ...

### Read

Plain row data
```SQL
select * from Patient;
```

Make as FHIR resource (TODO: row-to-resource helper)
```SQL
  select jsonb_build_object('resourceType', 'Patient', 'id', id, 'meta', meta, 'ts', ts, 'resources', resources) ....
    from Patient;

```

### Update

On update, move previous version to history table

```SQL
update patient set resources = jsonb_set(resources, '{gender}', '"female"') where id = 'ID';
insert into patient_history select * from patient where id = 'ID';
```


What about history????

### Delete

On delete move current version of resource into history table and add new row with deleted flag

```SQL
delete from patient where id = 'ID';
```

### History

To get full resource history select resource table and history table by id.

```SQL
select * from patient  where id = 'ID'
union all
select * from patient_history where id = 'ID'
```

```
PUT /fhir/Patient/pt1

gender: male
```

```SQL
WITH
-- inserted - result of insert into main resource table
-- if resource already exists in main table, aidbox
-- save json and change status to updated
inserted AS (
  INSERT INTO "patient" AS t (id, txid, status, resource)
  SELECT
    'pt1',
    nextval('transaction_id_seq'::text),
    'created',
    '{"gender":"male"}'
  ON CONFLICT (id) DO UPDATE
    SET
      txid = EXCLUDED.txid,
      status = 'updated',
      ts = current_timestamp,
      resource = EXCLUDED.resource
  WHERE (t.resource <> EXCLUDED.resource)
  RETURNING *
),

-- archived - populated history record (if needed)
archived AS (
  INSERT INTO "patient_history" (id, txid, cts, ts, status, resource)
  SELECT id, txid, cts, ts, status, resource
  FROM "patient"
  WHERE  id = 'pt1' AND ('updated' IN (SELECT status FROM inserted))
  ON CONFLICT (id, txid) DO UPDATE
    SET
      status = EXCLUDED.status,
      ts = EXCLUDED.ts,
      cts = EXCLUDED.cts,
      resource = EXCLUDED.resource
  RETURNING *
)

SELECT * FROM (
    SELECT id, txid, ts, resource_type, status::text AS status, resource, cts
    FROM inserted
    LIMIT 1
  UNION ALL
    SELECT id, txid, ts, resource_type, 'unchanged' AS status, resource, cts
    FROM "patient" AS t
    WHERE id = 'pt1'
    LIMIT 1

) _
LIMIT 2;
```


## FHIR search SQL explained
Aidbox uses [PostgreSQL jsonb functions](https://www.postgresql.org/docs/current/functions-json.html) to retrieve fields to search for.
Depending on a type of SearchParameter (e.g. reference, token, string), the SQL differs.

Lets take a look how to several search types works in Aidbox.

### Reference
On the rest `GET /Observation?subject=Patient/123`

On the SQL

```SQL
select * from observation where jsonb_path_query_first(data, '$.subject.reference') = 'Patient/123'
```

### Token

On the rest `GET /Patient?identifier=ssn|123`

On the SQL

```SQL
select * from patient
where resource @> '{"identifier": [{"system": "ssn", "value": "123"}]}'
```

Aidbox uses `@>` operator to search for resources by token

### String

Strings are more difficult. Need cast jsonb to text by fhir rules
We have a several functions to do that

Lets see `GET /Patient?name=Smith`

```SQL
select * from patient
where aidbox_text_search(resouce, '["name"]') ilike '%smith'
```


## Advanced search
FHIR search cannot express everything.
Sometimes SQL is the only option.
See [Aidbox Search](../api/rest-api/aidbox-search.md)

## Plains SQL access
Aidbox offers plain SQL.

```SQL
UPDATE patient SET ...
```

It is possible to run custom SQL using $sql endpoint.

### $sql endpoint
See [SQL endpoints](https://docs.aidbox.app/api/rest-api/other/sql-endpoints) page.

## Performance considerations
Performance: create Performance considerations in the maintenance page, reference here
  toasts
  Indexes
  PostgreSQL tuning (workmen, shared buffers...)

## SQL on FHIR
Most analytic and machine learning use cases require the preparation of FHIR data using
transformations and tabular projections from its original form.
Aidbox supports [SQL on FHIR specifiction](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/index.html).
Using ViewDefinition SQL on FHIR resource, you can flatten FHIR data and store as PostgreSQL view, materialized view or table.
See: [SQL on FHIR](../modules/todo-sql-on-fhir.md).

## Transactional bulk & migrations
?

## Transactional isolation levels & conditional operations
By default, all requests in Aidbox are ran in serializable isolation level.

## Requirements
See [Requirements](./requirements.md)

## Monitoring
Links to Maintenance page

## PostgreSQL Aidbox functions
See: [PostgreSQL functions](../../reference/todo-functions-page)


STRUCTURE
Database
  - Requirements
  - Database Schema & Storage Format
  - Query Resources & Search API SQL
  - Indexes & Performance
  - Transactional bulk & migrations
  - Transactional isolation levels & conditional operations
  - Database migrations: Which PostgreSQL user permissions is needed for migrations?
  - AidboxDB
    - Releases/versions
    - PostgreSQL Extensions
    - HA AidboxDB (CloudNativePGOperator/Crunchy)

reference:
  - Aidbox's functions
