# Database schema

The database schema consists of many tables and schemas, organized into two groups:

* FHIR resource and history tables
* System schemas and tables

## FHIR resource and history tables

Aidbox stores all FHIR resources in the `public` schema by default. 
You can [set different schema name](../reference/environment-variables/optional-environment-variables.md#use-different-postgresql-schema). 
Aidbox creates a table for each FHIR resource type with the same name as the resource type in lowercase (e.g., `patient` for the `Patient` resource type). 
All resource tables have the same structure:

* `id`: Resource ID (primary key, text)
* `txid`:  Version ID (bigint)
* `cts`: Creation timestamp (timestamp with time zone)
* `ts`: Last update timestamp (timestamp with time zone)
* `resource_type`: The resource type (text)
* `status`: Resource status (enum)
* `resource`: The actual resource data (jsonb)

## More details

* **`txid`**: Aidbox tracks version IDs using PostgreSQL sequence `transaction_id_seq`. This sequence is shared between all resource tables, functioning as a global counter incremented for each new resource version.
*   **`status`**: Aidbox uses PostgreSQL enum type `resource_status` for the status column to describe the lifecycle of a resource:\


    ```sql
    CREATE TYPE resource_status AS ENUM (
      'created', 
      'updated', 
      'deleted', 
      'recreated'
    );    
    ```
* **`resource`**: FHIR resources are stored as JSONB documents, which enables efficient storage and querying using PostgreSQL's JSON functions.

## History tables

For each resource table, Aidbox creates a matching history table by adding `_history` to the table name (e.g., `patient_history`). These history tables track all previous versions of resources. When you update or delete a resource, Aidbox moves the existing version to the history table before saving the new version.

Both resource and history tables share the same structure and columns, but only history tables have a composite primary key: `(id, txid)`.

For example, the `Patient` resource table and its history table are defined as follows:

```sql
create table "patient" (
  id text primary key,
  txid bigint not null,
  ts timestamptz default now(),
  cts timestamptz default now(),     
  resource_type text,                
  status resource_status not null,  
  resource jsonb not null            
);

create table "patient_history" (
  id text,               
  txid bigint not null,              
  ts timestamptz default now(),      
  cts timestamptz default now(),     
  resource_type text,                
  status resource_status not null,  
  resource jsonb not null,
  primary key (id, txid)
);
```

## System schemas

### SOF (SQL On FHIR)

The `sof` schema contains views and tables related to SQL on FHIR functionality. All ViewDefinition-defined views and tables are created in this schema, providing simplified SQL-based access to FHIR data.&#x20;

### FAR Schema (FHIR Artifact Registry)

The `far` schema contains tables related to FHIR artifact management and registry functionality. It's primarily used for internal Aidbox operations and typically not accessed directly by users:

* `atlas`: Stores Atlas configurations and metadata
* `canonicalresource`: Manages canonical FHIR resources
* `package`: Stores FHIR packages
* `packagename`: Manages package names and metadata
* `resource`: Stores general FHIR resources

### TDS Schema (Topic Destination Storage)

The `tds` schema contains tables related to event and message handling:

* `event_storage`: Stores events and their destinations
