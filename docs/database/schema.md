# Database schema

All resource types in Aidbox are stored in different tables, named with a lowercased resource type name. All these tables have a similar schema:

```sql
CREATE TABLE "patient" (
  id text PRIMARY KEY,               -- id of resource
  txid bigint not null,              -- version id and logical transaction id
  ts timestamptz DEFAULT NOW(),      -- last updated time
  cts timestamptz DEFAULT NOW(),     -- created time
  resource_type text,                -- resource type
  status resource_status not null,   -- resource status
  resource jsonb not null            -- resource body
);
```

As you can see, resources are stored as JSONB documents in the **resource** column.

{% hint style="info" %}
Resources are stored in [Aidbox format](../api/rest-api/other/aidbox-and-fhir-formats.md), which is more friendly for storage, and converted into FHIR in REST API on the fly!
{% endhint %}

You can access attributes of resources using [PostgreSQL JSON functions](https://www.postgresql.org/docs/11/functions-json.html):

```sql
SELECT
   resource#>>'{name,0,famly}' as last_name,
   resource#>>'{name,0,given,0}' as first_name
FROM "patient"
LIMIT 10
```

## Tutorials

Check out our video tutorial about JSON queries in PostgreSQL:

{% embed url="https://www.youtube.com/watch?v=zgU5c3RwjD4" %}
