# Database Overview

TODO: mention that SQL is the main strength.

Aidbox uses PostgreSQL as its database engine, leveraging its robust JSONB capabilities, reliability, and performance for healthcare data storage.

## Storage model

All FHIR data is stored in PostgreSQL JSONb data format. It gives better performance and flexibility.

For each resource type Aidbox creates their own table:

```SQL
select * from Patient limit 1;
```

Result:

TODO: add `type` and `description` columns.

| Column        | Value                                           |
| ------------- | ----------------------------------------------- |
| resource_type | Patient                                         |
| id            | 2193c2e7-4d66-74c6-17c5-6d0c1c094fc2            |
| txid          | 178246                                          |
| ts            | 2025-06-09T08:51:07.533413Z                     |
| cts           | 2025-06-09T08:51:07.533413Z                     |
| status        | created                                         |
| resource      | `{"gender": "male", "birthDate": "2025-01-01"}` |

Resource table contains `resource` column in JSONB format that stores FHIR resource data. And several additional columns for versioning, status, and other metadata. When Aidbox responses, all values from different columns are converted into one object on the fly.

See [Database Schema](./database-schema.md) page for full details about Aidbox database schema.

## CRUD interactions

The table structure explained above allows implementing [CRUD](../api/resp-api/crud/README.md) simply using `resource` column and JSONB functions.

In this section, we'll examine the basic concepts of how Aidbox interacts with the database. We'll present simplified query examples that demonstrate the essence of these operations. For more detailed information, please refer to the [CRUD](./crud-internals.md) documentation.

### Create

To create a resource, you can use a standard `INSERT` statement. When inserting a new resource, you need to specify these four required columns:

- `id` - unique identifier for the resource. This can be automatically generated using the `gen_random_uuid()` function or provided by the client.
- `txid` - version id. This can be generated using `nextval('transaction_id_seq')`, which is a global Aidbox sequence, or it can be provided by the client.
- `status` - the current state of the resource. Can be: `created`, `updated`, or `deleted`.
- `resource` - the actual resource data in JSONB format.

```SQL
insert into Patient (
    id,
    txid,
    status,
    resource
) values (
    gen_random_uuid(),
    nextval('transaction_id_seq'),
    'created',
    '{"gender": "male", "birthDate": "2024-01-01"}'
)
```

### Read

To retrieve FHIR data, you simply need to execute a standard `SELECT` query against the target resource table

```SQL
select * from Patient limit 1;
```

You can query more FHIR like data structure by merging resource data with metadata `id` and `resource_type` columns:

```SQL
select jsonb_build_object( 'id', id, 'resourceType', resource_type ) || resource as resource
  from patient
 limit 1
```

Result:

```json
{
  "id": "ad1eba27-815a-4421-a9fe-89da89b842d0",
  "gender": "male",
  "birthDate": "2024-01-01",
  "resourceType": "Patient"
}
```

### Update

To update a resource, specify the new resource data, increment the transaction ID, update the timestamp, and change the resource status to `updated`:

```SQL
UPDATE patient
   SET resource = '{"birthDate": "2025-02-02", "gender": "female"}',
       status = 'updated',
       ts = NOW(),
       txid = nextval('transaction_id_seq')
 WHERE id = 'ad1eba27-815a-4421-a9fe-89da89b842d0';
```

### Delete

To delete a resource, use the sql DELETE statement and specify the resource id that you want to remove:

```SQL
DELETE FROM patient
 WHERE id = 'ad1eba27-815a-4421-a9fe-89da89b842d0';
```

### History

In the sections above, we intentionally omitted working with [resource history](../api/rest-api/history.md) to keep the examples straightforward. In normal operation, Aidbox creates a new record in the `resource_history` table for every modification and deletion of a resource.

To get full resource history, Aidbox selects resource table and history table by resource id.

```SQL
  SELECT *
    FROM patient
   WHERE id = 'ad1eba27-815a-4421-a9fe-89da89b842d0'
   UNION ALL
  SELECT *
    FROM patient_history
   WHERE id = 'ad1eba27-815a-4421-a9fe-89da89b842d0'
```

### See also

- [CRUD internals](./crud-internals.md)
- [How to delete-data](../tutorials/crud-search-tutorials/delete-data.md) tutorial
- [Set up uniqueness in resource](../tutorials/crud-search-tutorials/set-up-uniqueness-in-resource.md) tutorial

## Transactional bulk & migrations

?

## Transactional isolation levels & conditional operations

By default, all requests in Aidbox are ran in `serializable` [isolation level](https://www.postgresql.org/docs/current/transaction-iso.html).
It can be changed using [`x-max-isolation-level` header](../api/rest-api/crud/update#isolation-levels).

some explanation ...?

## FHIR search SQL explained

Aidbox uses [PostgreSQL jsonb functions](https://www.postgresql.org/docs/current/functions-json.html) to retrieve fields to search for.
Depending on a type of [SearchParameter](../api/rest-api/fhir-search/README.md) (e.g. reference, token, string), the SQL differs.

Lets take a look how to several search types works in Aidbox.

### Reference search

On the rest `GET /Observation?subject=Patient/123`

On the SQL

```SQL
select * from observation where jsonb_path_query_first(data, '$.subject.reference') = 'Patient/123'
```

### Token search

On the rest `GET /Patient?identifier=ssn|123`

On the SQL

```SQL
select * from patient
where resource @> '{"identifier": [{"system": "ssn", "value": "123"}]}'
```

Aidbox uses `@>` operator to search for resources by token.

### String search

Strings are more difficult. To search properly, by FHIR rules, we need to cast jsonb to text.

Lets see `GET /Patient?name=Smith`.

```sql
SELECT "patient".*
FROM "patient"
WHERE jsonb_path_query_array(
          "patient".resource,
          CAST('($.\"name\"[*]).** ? (@.type() == "string")' AS jsonpath))::text
      ) ILIKE unaccent('%"Smith%')
LIMIT 100
OFFSET 0;
```

See also:

- [FHIR Search](../api/rest-api/fhir-search/README.md)
- How to inspect SQL of the query using [\_explain](../api/rest-api/aidbox-search#explain) search parameter.

## Advanced search

FHIR search cannot express everything.
Sometimes SQL is the only option to achieve complex performant enough search.
See [Aidbox Search](../api/rest-api/aidbox-search.md) to discover how Aidbox extends FHIR Search.

## Performance considerations

Performance can be improved by:

- Creating indexes
- Tuning PostgreSQL (workmem, shared buffers...)
- ...
  toasts?

See more in the [Performance Considerations page](../deployment-and-maintenance/performance-considerations.md).

## Plain SQL access

It is absolutely fine to run plain SQL requests instead of using endpoints,
especially when FHIR does not provide a solution to the problem.
For example, updating some field in whole `patient` table using usual UPDATE.

- transactional
- this is faster

TODO: improve update example

```SQL
UPDATE patient SET ...
```

TODO: bulk delete all inactive patients
TODO: truncate history table example
TODO: new tutorial how to work raw SQL
TODO: join example
TODO: db migrations

You can send plain SQL request in:

- [Aidbox UI](../overview/aidbox-ui/README.md)
- [psql](https://www.postgresql.org/docs/current/app-psql.html)
- any other SQL tools (Datagrip, PgAdmin)
- [$sql endpoint](../api/rest-api/other/sql-endpoints.md)

## SQL on FHIR

Most analytic and machine learning use cases require the preparation of FHIR data using
transformations and tabular projections from its original form.
Aidbox supports [SQL on FHIR specifiction](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/index.html).
Using ViewDefinition SQL on FHIR resource, you can flatten FHIR data and store as PostgreSQL view, materialized view or table.

TODO: viewdef json request example
TODO: eval
TODO: sof.schema
TODO: example on view sql request
Copy it from readme.
TODO: https://www.health-samurai.io/articles/sql-on-fhir-an-inside-look

See: [SQL on FHIR](../modules/sql-on-fhir/README.md).

## Requirements

See [Requirements](./requirements.md)

## Monitoring

some explanation?
See more in [Deployment and Maintenance section](../deployment-and-maintenance/deploy-aidbox/README.md).

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
- FAQ?
- AidboxDB
  - Releases/versions
  - PostgreSQL Extensions
  - HA AidboxDB (CloudNativePGOperator/Crunchy)

reference:

- Aidbox's functions
