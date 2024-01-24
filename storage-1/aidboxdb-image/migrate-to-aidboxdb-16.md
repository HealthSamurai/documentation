---
description: >-
  This guide explains migrating to AidboxDB 16, specifically addressing the
  absence of the jsonknife extension
---

# Migrate to AidboxDB 16

Since AidboxDB 16.1 does not have the `jsonknife` extension on board as it was available in the previous versions there is the guide explaining how to migrate to it.

## Check if extra steps needed

Before migrating to AidboxDB 16, it's crucial to determine whether you have any indexes using that extension. To get list of indexes utilizing `jsonknife` extension, run following SQL query:

```sql
SELECT indexname, indexdef 
 FROM pg_indexes 
WHERE indexdef ILIKE '%knife%';
```

* **No Action Required:** If the query returns an empty result set, no further actions are required regarding this extension.
* **Action Required:** If the query results are not empty, you must follow the following steps.

## Migration Steps

### **Backup database**

Begin by creating a complete backup of your database. For example, this can be achieved using the `pg_dump` command

```sql
pg_dump -U [username] [dbname] > [backupfile].sql
```

### Provision necessary functions in the new PostgreSQL cluster&#x20;

To create necessary `jsonknife` functions run the following SQL with-in your PG cluster.

{% file src="../../.gitbook/assets/jsonknife-function-shims.sql" %}
jsonknife functions shims
{% endfile %}

### Restore database in the new cluster

Once the new functions are defined in the new cluster, restore database using, for example, `pg_restore`.

```sql
pg_restore -U [username] -d [dbname] [backupfile].sql
```

