---
description: Aidbox database schema
---

# Database

All resource types in Aidbox are stored in different tables, named with a lowercased resource type name. All these tables have a similar schema:

```
CREATE TABLE "patient" (
  id text PRIMARY KEY,               // id of resource
  txid bigint not null,              // version id and logical transaction id
  ts timestamptz DEFAULT NOW(),      // last updated time
  resource_type text,                // resource type
  status resource_status not null,   // resource status
  resource jsonb not null            // resource body
);
```

You use the DB Console to explore the database:

```sql
select * from "entity" limit 10
```

As you can see, resources are stored as JSONB documents in the **resource** column.

{% hint style="info" %}
Resources are stored in [Aidbox JSON format](../modules-1/fhir-resources/aidbox-and-fhir-formats.md), which is more friendly for storage, and converted into FHIR in REST API on the fly!
{% endhint %}

You can access attributes of resources using [PostgreSQL JSON functions](https://www.postgresql.org/docs/11/functions-json.html):

```sql
SELECT
   resource#>>'{name,0,famly}' as last_name,
   resource#>>'{name,0,given,0}' as first_name
FROM "patient"
LIMIT 10
```

## Custom Queries

You can define and expose over REST API sophisticated queries in SQL on FHIR data using [Custom Queries](../api-1/fhir-api/search-1/custom-search.md).

## **Archiving**

**Wal_g and pg_dump approaches**

### wal_g

While you can do restoration and archiving with pg_dump, wal_g is even more efficient because you can restore any past point of your state. Wal_g also can do:

1. archive WAL files into cloud storage
2. create physical backups
3. recover a database up to any chosen point in time
4. make delta backups
5. recover after a crash from cloud archive

### pg_dump

 pg_dump is a utility for backing up a PostgreSQL database.

It makes consistent backups even if the database is being used concurrently. pg_dump only dumps a single database. To back up an entire cluster, or to back up global objects that are common to all databases in a cluster, use pg_dumpall

Dumps can be output in script or archive file formats. Script dumps are plain-text files containing the SQL commands required to reconstruct the database to the state it was in at the time pg_dump was called.

Script files can be used to reconstruct the database even on other machines and other architectures; with some modifications, even on other SQL database products. To restore a plain-text file feed it to psql.

The alternative archive file formats must be used with pg_restore to rebuild the database. pg_restore can be used to examine the archive and/or select which parts of the database are to be restored.

Suitable for small databases (< 5 GB) This approach assumes downtime



## Tutorials

Check out our video tutorial about SQL on FHIR in PostgreSQL:

{% embed url="https://www.youtube.com/watch?v=zgU5c3RwjD4" %}
