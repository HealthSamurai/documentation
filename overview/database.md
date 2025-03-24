# FHIR Database

Aidbox database is compatible with our FHIRBase database schema.
Aidbox uses only PostgreSQL as a FHIR database, but we exploit the most PostgreSQL features.
Aidbox can work with different versions of PostgreSQL, including Cloud managed PostgreSQL (GCP, AWS, Azure).

We leverage PostgreSQL JSONB type to store FHIR resources 
almost as is in JSONB column (read more about [Storage Format](/overview/storage-format.md)).

For performant searches Aidbox requires JSON Path functions.

## Schema

Resources are stored in tables named after the resource type.
All tables have same structure.

```sql
CREATE TABLE patient (
    id text PRIMARY KEY DEFAULT gen_random_uuid(),
    -- transaction id, and logical version of the resource
    txid bigint NOT NULL,
    -- creation timestamp of the resource
    cts timestamp NOT NULL DEFAULT current_timestamp,
    -- last update timestamp of the resource
    ts timestamptz NOT NULL DEFAULT current_timestamp,
    -- resource type
    resource_type text NOT NULL,
    resource jsonb NOT NULL
);
```

Transactional operations use cross-table sequence to generate txid.
Every resource in this transaction will have the same txid. This is essentially logical timestamp.

## Toasts

Big json can lead to performance issues, related to PostgreSQL toast.
PostgreSQL page size is 8KB, so big json will lead to multiple pages.
Such resources can be efficiently accessed by id, but complex search queries
can be slow. We recommend to use smaller json objects.



## History

For each resource type the history table is created with name `<resource_type>_history`.
Aidbox automatically copy old versions of resources to history tables. Multiple updates
may lead to bloat of history tables. It's up to user to clean them up.

```sql
CREATE TABLE patient_history (
    id text PRIMARY KEY DEFAULT gen_random_uuid(),
    txid bigint NOT NULL,
    -- creation timestamp of the resource
    cts timestamp NOT NULL DEFAULT current_timestamp,
    -- last update timestamp of the resource
    ts timestamptz NOT NULL DEFAULT current_timestamp,
    -- created, updated, deleted
    status text,
    resource jsonb NOT NULL
);
```

## Import & Export

We use PostgreSQL `COPY` command to efficiently import and export data.

[See Bulk API](/api/bulk-api.md) for more details.


## Search & Indexes

We use more traditional approach to work with data in database.
We do not create materialized search parameters tables,
but generate sophisticated SQL queries from FHIR Search Parameters.


```sql
-- GET /Patient?name=Smith
SELECT * FROM patient 
WHERE resource->>'status' = 'active';
```

For performance you can create indexes for search parameters.
But because it is a trade-off between write and read performance,
it's up to you to decide whether to create them or not.

## Indexes




## SQL API

Aidbox does not hide database from you.
You can connect directly to database and execute SQL queries. 

```sql
-- get all resources
SELECT * FROM patient;

-- get all resources by status
SELECT * FROM patient WHERE resource->>'status' = 'active';
```

Use PostgreSQL jsonb functions to access resource elements.


## Transactional SQL and Migrations

You can write classical SQL to change resources in database.

```sql
UPDATE patient 
SET resource = resource || '{"status": "active"}'
WHERE resource->>'status' is null;
```

When changing resources in database, Aidbox will not check if resource is valid.
Use async validation to validate resources.

TBD: vacuum problems

## SQL on FHIR ViewDefinition

Aidbox supports SQL on FHIR ViewDefinition specification, implementing
`in-database` view runner. We translate view definitions into SQL query
and create a view on top of it.



