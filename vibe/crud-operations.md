<!-- Merge into: database.md -->

# How data inserted, updated and deleted?

Managing healthcare data requires careful tracking of every change. 
The [FHIR HTTP API](https://www.hl7.org/fhir/http.html) defines standard operations for creating, reading, updating, and deleting resources, along with versioning and history management. 
Aidbox implements these operations efficiently while maintaining complete resource history for clinical and regulatory compliance.

This section covers how Aidbox performs CRUD (Create, Read, Update, Delete) operations, manages resource history, handles bulk operations, and enables advanced transactional processing. 
Beyond the standard FHIR API, Aidbox provides direct SQL access for advanced queries and bulk operations - see the [Query section](#how-to-query-data) for details.

## CRUD Operations with Built-in History

Every modification in Aidbox creates an history record following FHIR versioning principles. 
When you update a Patient resource, the previous version moves to the history table while the main table gets the new version. 
This happens atomically within a single [PostgreSQL transaction](https://www.postgresql.org/docs/current/tutorial-transactions.html).

Examples shown in this section are simplified for clarity. For complete implementation details, see [CRUD internals](./crud-internals.md).

### Resource Lifecycle

Each resource in Aidbox has a lifecycle tracked through the `status` column:
- `created` - Initial resource creation
- `updated` - Resource has been modified
- `deleted` - Resource has been soft-deleted (only appears in history)

See also: [Database Schema](./database-schema.md)

### Creating Resources

Creating a resource requires four essential columns:

```sql
INSERT INTO patient (id, txid, status, resource) 
VALUES (
  gen_random_uuid(),              -- Auto-generated ID
  nextval('transaction_id_seq'),  -- Global version sequence
  'created',                      -- Initial status
  '{"gender": "male", "birthDate": "1990-01-15"}'::jsonb
);
```

The Aidbox supports both auto-generated UUIDs via `gen_random_uuid()` and client-provided IDs for migrations or deterministic scenarios.

The `txid` (transaction ID) uses a global sequence that provides cross-resource transaction ordering, reliable change tracking across the entire database, and serves as the foundation for change feeds and synchronization.

### Reading Resources

Basic read operations query the main resource table:

```sql
-- Get a specific patient
SELECT * FROM patient WHERE id = 'patient-123';

-- Reconstruct full FHIR format
SELECT jsonb_build_object(
  'id', id,
  'resourceType', resource_type,
  'meta', jsonb_build_object(
    'versionId', txid::text,
    'lastUpdated', ts
  )
) || resource as fhir_resource
FROM patient
WHERE id = 'patient-123';
```

For versioned reads, you need to check both current and history tables:

```sql
-- Read specific version
SELECT * FROM (
  SELECT * FROM patient WHERE id = 'patient-123'
  UNION ALL
  SELECT * FROM patient_history WHERE id = 'patient-123'
) t
WHERE txid = 456789
LIMIT 1;
```

### Updating Resources

Updates in Aidbox follow a two-step process:
1. Archive the current version to history table
2. Update the main table with new data

```sql
-- Step 1: Copy current to history (Aidbox does this automatically)
INSERT INTO patient_history 
SELECT * FROM patient WHERE id = 'patient-123';

-- Step 2: Update main table
UPDATE patient
SET resource = '{"gender": "female", "birthDate": "1990-01-15"}'::jsonb,
    status = 'updated',
    ts = CURRENT_TIMESTAMP,
    txid = nextval('transaction_id_seq')
WHERE id = 'patient-123';
```


### Deleting Resources

Deletion is a soft operation that preserves data for history purposes:

```sql
-- Step 1: Archive current version
INSERT INTO patient_history 
SELECT * FROM patient WHERE id = 'patient-123';

-- Step 2: Create deletion marker in history
INSERT INTO patient_history (id, txid, ts, status, resource)
SELECT id, 
       nextval('transaction_id_seq'),
       CURRENT_TIMESTAMP,
       'deleted',
       resource
FROM patient WHERE id = 'patient-123';

-- Step 3: Remove from main table
DELETE FROM patient WHERE id = 'patient-123';
```

After deletion:
- The resource no longer appears in the main table
- Two records exist in history: the last version and a deletion marker
- The resource can still be retrieved for history purposes

## History Management

Every resource type has a corresponding `_history` table with identical structure. This design enables:

### Complete Version History

```sql
-- Get full history of a resource
SELECT id, txid, ts, status, resource->>'gender' as gender
FROM (
  SELECT * FROM patient WHERE id = 'patient-123'
  UNION ALL
  SELECT * FROM patient_history WHERE id = 'patient-123'
) t
ORDER BY txid DESC;
```

### Point-in-Time Queries

```sql
-- What did the resource look like on January 1st?
SELECT * FROM patient_history 
WHERE id = 'patient-123' 
  AND ts <= '2024-01-01'::timestamptz
ORDER BY txid DESC
LIMIT 1;
```

### Change Detection

```sql
-- Find all changes in the last hour
SELECT id, txid, ts, status
FROM patient_history
WHERE ts > CURRENT_TIMESTAMP - INTERVAL '1 hour'
ORDER BY txid DESC;
```

## Transactions and Batch Operations

Aidbox uses PostgreSQL's [ACID transactions](https://www.postgresql.org/docs/current/acid.html) to ensure data consistency across multiple operations. The system uses [SERIALIZABLE isolation level](https://www.postgresql.org/docs/current/transaction-iso.html) by default, which prevents all serialization anomalies but may cause transaction rejection under high concurrency.

You can adjust the isolation level using the `x-max-isolation-level` header in API requests. Lower isolation levels reduce transaction rejection but may allow serialization anomalies. See [batch-transaction documentation](../api/batch-transaction.md) for details.

### FHIR Transaction Bundles

FHIR [transaction bundles](https://www.hl7.org/fhir/http.html#transaction) allow multiple operations to be submitted as a single atomic unit. Aidbox processes these bundles by:

1. Executing all operations within a single PostgreSQL transaction
2. Resolving internal references between resources before processing
3. Rolling back all changes if any operation fails
4. Returning a transaction-response bundle with results

```json
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "fullUrl": "urn:uuid:patient-temp",
      "request": {"method": "POST", "url": "Patient"},
      "resource": {"resourceType": "Patient", "name": [{"family": "Smith"}]}
    },
    {
      "request": {"method": "POST", "url": "Observation"},
      "resource": {
        "resourceType": "Observation",
        "subject": {"reference": "urn:uuid:patient-temp"}
      }
    }
  ]
}
```

The temporary reference `urn:uuid:patient-temp` gets resolved to the actual Patient ID during processing, allowing you to create related resources in a single transaction.

See also: [Batch Transaction](../api/batch-transaction.md)


### Conditional Operations

[Conditional operations](https://www.hl7.org/fhir/http.html#cond) in FHIR allow creating, updating, or deleting resources based on search criteria rather than specific IDs. These operations enable stateless clients to manage resources without needing to track exact resource identifiers.

Aidbox supports conditional operations with some differences from the FHIR specification:
- Uses query parameters instead of `If-None-Exist` headers for consistency
- Conditional create returns existing resource if match found
- Conditional operations use the same search parameters as regular queries

```sql
-- Example: Conditional update using SQL with CTEs
WITH check_update AS (
  SELECT id FROM patient 
  WHERE resource->'identifier' @> '[{"system": "ssn", "value": "123-45-6789"}]'
)
UPDATE patient p
SET resource = jsonb_set(resource, '{active}', 'false'),
    txid = nextval('transaction_id_seq'),
    ts = CURRENT_TIMESTAMP
FROM check_update c
WHERE p.id = c.id;
```

See [conditional create documentation](../api/rest-api/crud/create.md) for complete API details.

## Bulk Operations

### Aidbox Bulk API

Aidbox provides several bulk operation APIs for efficient data processing:

- [`$import` and `$load`](../api/bulk-api.md) - Asynchronous bulk import from external sources
- [`$export` and `$dump`](../api/bulk-api.md) - Memory-efficient streaming export 
- [`$dump-sql`](../api/bulk-api.md) - Export SQL query results
- [Transaction bundles](../api/batch-transaction.md) - Multiple operations in single HTTP request
- [Batch upsert](../api/bulk-api.md) - Lightweight collection upserting

### SQL Bulk Operations

SQL bulk operations are the underlying implementation of Aidbox's bulk APIs. When you use `$import` or `$load`, Aidbox translates these into optimized PostgreSQL operations. For direct database access, you can use these same SQL capabilities.

#### Bulk Inserts with COPY

For high-performance data import, use PostgreSQL's [COPY command](https://www.postgresql.org/docs/current/sql-copy.html):

```sql
-- Prepare CSV data
COPY patient (id, txid, status, resource) FROM STDIN WITH (FORMAT csv);
patient-1,100001,created,"{""name"":[{""family"":""Johnson""}]}"
patient-2,100002,created,"{""name"":[{""family"":""Williams""}]}"
\.

-- Or from file
COPY patient (id, txid, status, resource) 
FROM '/data/patients.csv' WITH (FORMAT csv, HEADER true);
```

#### Bulk Updates

```sql
-- Update all patients in a city
UPDATE patient
SET resource = jsonb_set(resource, '{address,0,city}', '"New York"'),
    txid = nextval('transaction_id_seq'),
    ts = CURRENT_TIMESTAMP
WHERE resource->'address'->0->>'city' = 'NY';
```

#### Bulk Deletes

```sql
-- Archive and delete inactive patients
WITH to_delete AS (
  SELECT id FROM patient 
  WHERE resource->>'active' = 'false'
)
-- First archive to history
INSERT INTO patient_history
SELECT p.* FROM patient p
JOIN to_delete d ON p.id = d.id;

-- Then delete
DELETE FROM patient
WHERE id IN (SELECT id FROM to_delete);
```

See also: [Bulk API](../api/bulk-api.md)

