---
description: Aidbox database schema
---

# Database

All resource types in Aidbox are stored in different tables, named with a lowercased resource type name. All these tables have a similar schema:

```text
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

### Custom Queries

You can define and expose over REST API sophisticated queries in SQL on FHIR data using [Custom Queries](../api-1/fhir-api/search-1/custom-search.md). 

### Backup and restore

To restore Aidbox it is enough to restore Postgres from backup. There are several options:

* For small databases _\(&lt; 5Gb\)_ you can use Postgres built-in function `pgdump` [https://www.postgresql.org/docs/9.3/app-pgdump.html](https://www.postgresql.org/docs/9.3/app-pgdump.html). After running the command you get the file from which you can restore aidboxdb using command `pgrestore` [https://www.postgresql.org/docs/9.2/app-pgrestore.html](https://www.postgresql.org/docs/9.2/app-pgrestore.html).    This approach assumes downtime

### Tutorials

Check out our video tutorial about SQL on FHIR in PostgreSQL:

{% embed url="https://www.youtube.com/watch?v=zgU5c3RwjD4" %}



