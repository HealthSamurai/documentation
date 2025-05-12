# Database schema

The database schema consists of multiple tables and schemas that can be categorized into several groups:

- FHIR resource and history tables
- System schemas and tables

## FHIR resource and history tables

All FHIR resources are stored in the `public` schema. For each FHIR resource type Aidbox creates a table with the same name as the resource type. For example for resource type `Patient` Aidbox will create table `patient`. Structure of the resource table is the same for all resource types.

- `id`: Resource ID  (primary key, text)
- `txid`: Version ID (bigint)
- `cts`: Creation timestamp (timestamp with time zone)
- `ts`: Last update timestamp (timestamp with time zone)
- `resource_type`: The type of resource (text)
- `status`: Resource status (enum)
- `resource`: The actual resource data (jsonb)

`txid` - Aidbox tracks version IDs using PostgreSQL sequence `transaction_id_seq`. This sequence is shared between all resource tables. You can imagine it as a global counter that is incremented for each new resource version.

`status` - Aidbox uses PostgreSQL enum type `resource_status` for status column to describe the lifecycle of a resource. This type is defined as follows:

```sql
CREATE TYPE resource_status AS ENUM (
  'created', 
  'updated', 
  'deleted', 
  'recreated'
);    
```

For each resource table, Aidbox creates a matching history table by adding `_history` to the table name. This history table keeps track of all previous versions of resources. For example, a `Patient` resource have both a `patient` table for the current version and a `patient_history` table for older versions. When you update or delete a resource, Aidbox moves the existing version to the history table before saving the new version. Both tables share the same structure and columns. But only  history table has primary key: `(id, txid)`.

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

## System Tables

- `_aidbox_license`
- `_awf_action_queue`
- `_awf_task_queue`
- `_box`
- `_logs`
- `_schema_version`


## Another Schemas

### SOF (Sql On FHIR)

The `sof` schema contains views and tables related to Sql On FHIR functionality. All viewdefinition view and tables will be created in this schema.

### FAR Schema (FHIR Artifact Registry)

The `far` schema contains tables related to FHIR artifact management and registry functionality. 
Used for internal Aidbox functionality. Usually not used by users.

- `atlas` Stores Atlas configurations and metadata.
- `canonicalresource` Manages canonical FHIR resources.
- `package` Stores FHIR packages.
- `packagename` Manages package names and metadata.
- `resource` Stores general FHIR resources.


### TDS Schema (Topic Destination Storage)

The `tds` schema contains tables related to event and message handling.

- `event_storage` - stores events and their destinations.
