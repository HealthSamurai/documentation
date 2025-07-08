# How data are stored in Aidbox?

Storing FHIR resources in traditional relational databases requires complex schemas with dozens of tables and joins. The deeply nested, polymorphic nature of FHIR data makes this approach difficult to maintain and query efficiently.

Aidbox takes a different approach by leveraging PostgreSQL's JSONB columns to store FHIR resources in their native hierarchical format. 
This design provides both the flexibility of document storage and the reliability of a relational database.

## The PostgreSQL + JSONB Approach

Aidbox exclusively uses PostgreSQL as its database. 
Instead of spreading your Patient resource across multiple columns, or even multiple tables with complex joins, it stores the entire resource in a single JSONB column.

[JSONB](https://www.postgresql.org/docs/current/datatype-json.html) is PostgreSQL's binary JSON storage format. 
The binary representation enables efficient indexing, querying, and storage optimization. 
This provides the flexibility of document storage with the full power of PostgreSQL's relational features.

```sql
-- A Patient resource lives in one row, one column
SELECT resource FROM patient WHERE id = 'patient-123';

-- Returns something like:
{
  "name": [{"given": ["John"], "family": "Doe"}],
  "gender": "male",
  "birthDate": "1980-01-15",
  "address": [{
    "line": ["123 Main St"],
    "city": "Boston",
    "state": "MA"
  }]
}
```

## Two Tables for Every Resource

For each FHIR resource type, Aidbox creates exactly two tables:

1. **Main table** (e.g., `patient`) - stores the current version of each resource
2. **History table** (e.g., `patient_history`) - stores every version ever created

This dual-table approach means you always have fast access to current data while maintaining a complete audit trail. No complex versioning schemes, no separate audit databases - just two tables per resource type.

## The Anatomy of a Resource Row

Let's peek inside the `patient` table to see what's actually stored:

```sql
SELECT * FROM patient LIMIT 1;
```

| Column | Type | Description | Example |
|--------|------|-------------|---------|
| **id** | text | Resource ID | `e7c3b-4d66-74c6` |
| **txid** | bigint | Version ID (global sequence) | `178246` |
| **ts** | timestamptz | Last updated timestamp | `2025-01-09T10:30:00Z` |
| **cts** | timestamptz | Created timestamp | `2025-01-09T10:00:00Z` |
| **status** | text | Resource status | `created`, `updated`, `deleted` |
| **resource** | jsonb | The actual FHIR resource | `{"name": [...], "gender": "male"}` |

Notice what's *not* in the JSONB? The `id`, `resourceType`, `meta.lastUpdated`, and `meta.versionId` live as separate columns. This normalized approach gives you the best of both worlds:

- Fast queries on common fields (WHERE id = ?)
- Full FHIR resource in one place
- No data duplication

## Why This Design Wins

**Schema flexibility without chaos.** Need to add a new extension to all your Patient resources? Just update the JSONB - no ALTER TABLE nightmares. Your database schema stays stable while your data model evolves.

**One source of truth.** Unlike some systems that maintain separate search tables or denormalized views, Aidbox queries directly against the JSONB data. What you store is what you search.

**PostgreSQL superpowers at your fingertips.** Want to run a complex analytical query mixing FHIR searches with custom SQL? Need ACID transactions across multiple resources? Want to use PostGIS for geographical queries? It's all there because it's just PostgreSQL.


## Direct SQL Access

Since it's just PostgreSQL, you can query your data directly when needed:

```sql
-- Find all male patients born after 1990
SELECT id, resource->>'birthDate' as birth_date
FROM patient
WHERE resource->>'gender' = 'male'
  AND (resource->>'birthDate')::date > '1990-01-01';

-- Extract nested data with PostgreSQL's JSON operators
SELECT 
  id,
  resource->'name'->0->>'family' as last_name,
  resource->'address'->0->>'city' as city
FROM patient
WHERE resource->'address'->0->>'state' = 'MA';
```

The beauty is that these aren't special Aidbox queries - they're standard PostgreSQL JSON operations that any PostgreSQL client (psql, pgAdmin, DBeaver, etc.) can run.

See also:
- [Database Schema](./database-schema.md) - Detailed table structures and column definitions
- [CRUD Operations](./crud-operations.md) - How data gets in and out
- [PostgreSQL Requirements](./postgresql-requirements.md) - Supported versions and extensions
