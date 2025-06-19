# Database Overview

* How data are stored in Aidbox?
* How data inserted and updated
  * crud &  history
  * transaction (tx, batch vs transaction)
  * bulk inserts, updates and deletes
  * migrations
* How to query data?
  * using FHIR search
  * using SQL
  * SQL on FHIR (using ViewDefinitions)

* Which PostgreSQL can be used with Aidbox?
  * which extensions are used
* How to optimize performance?
  * indexes & monitoring
  * performance tuning
  * materialized views
  * postgres configuration

## How data are stored in Aidbox?

Aidbox exclusively uses PostgreSQL as its database.
FHIR resources are stored almost in their original form within [JSONB columns](https://www.postgresql.org/docs/current/datatype-json.html), 
which provide efficient binary storage and indexing capabilities. 


Since FHIR resources are inherently nested documents, JSONB offers the ideal balance of flexibility and performance for storing them in a relational database. This approach enables you to leverage the full power of PostgreSQL features including SQL queries, transactions, and advanced indexing while maintaining the hierarchical structure of FHIR data.

JSONB's schema-less nature allows for seamless evolution of your data model - you can add new elements, extensions, or migrate existing data without requiring database schema changes.

For each resource type, Aidbox creates two tables:
- A main table with the resource type name in lowercase to store the current versions of resources
- A history table with the `_history` postfix to store the complete version history of resources, which Aidbox updates transactionally.

Aidbox uses normalized approach (one source of truth) and does not use additional columns or tables 
for search and metadata. Aidbox generates sophisticated SQL queries to implement FHIR Searches and other operations.
As well this allows you to query and modify data directly, if you follow basic storage principles.

For example, for the `Patient` resource type, Aidbox creates `patient` and `patient_history` tables.

```SQL
select * from patient limit 1;
```

| Column        | Type         | Description                                    | Value                                           |
| ------------- | ------------ | ---------------------------------------------- | ----------------------------------------------- |
| resource_type | text         | Resource type                                  | Patient                                         |
| id            | text         | Resource ID                                    | 2193c2e7-4d66-74c6-17c5-6d0c1c094fc2            |
| txid          | bigint       | Version ID                                     | 178246                                          |
| ts            | timestamptz  | Updated at                                     | 2025-06-09T08:51:07.533413Z                     |
| cts           | timestamptz  | Created at                                     | 2025-06-09T08:51:07.533413Z                     |
| status        | text         | Status (created, updated, deleted)             | created                                         |
| resource      | jsonb        | Resource data in JSONB format                  | `{"gender": "male", "birthDate": "2025-01-01"}` |

The main resource table contains a `resource` column in JSONB format that stores the FHIR resource data minus id, resourceType, meta.lastUpdated and meta.versionId, which are stored in separate columns.

See [Database Schema](./database-schema.md) for more details about database schema.


##  How data inserted, updated and deleted?

* FHIR CRUD with history
  * id management
  * status
  * tx (versionid)
  * ts and cts
  * history tables...
* Bulk inserts
* Bulk updates and deletes

This is a list of rules how to update data directly!!!!

For create operation Aidbox generates insert statement with all required columns. 
id is text. User can control id by providing it or aidbox generates uuid
we assign global versionid for traceablity - txid (see transactions).
we set cts to creation time.

We do not validate data in database, this is done in Aidbox.

for update we move previous resource to history table and insert new one and change status.
duplicate optimization ... Update ts (updated at time)

for deletes we create two records in history table - one current one and another with status deleted and ts set to deletion time.

for bulk import we use copy command from postgresql for efficient data import (what about history? what about duplicates?)

you can write transactional updates and deletes using sql:
* update example
* delete/truncate example

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
