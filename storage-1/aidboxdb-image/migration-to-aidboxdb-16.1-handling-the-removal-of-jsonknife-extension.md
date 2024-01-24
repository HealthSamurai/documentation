# Migration to Aidboxdb 16.1: Handling the Removal of jsonknife Extension

AidboxDB 16.1 has discontinued the `jsonknife` extension that was available in previous versions like AidboxDB 15.3. This documentation provides a guide for migrating to AidboxDB 16.1, specifically addressing the absence of the `jsonknife` extension.

### Pre-Migration Assessment

Before migrating to AidboxDB 16.1, it's crucial to determine whether your current database utilizes the `jsonknife` extension, particularly in indexes. The following SQL query can be used to identify the presence of `jsonknife` in index definitions:

```sql
SELECT indexname, indexdef 
 FROM pg_indexes 
WHERE indexdef ILIKE '%knife%';
```

* **No Action Required:** If the query returns an empty result set, your database does not use the `jsonknife` extension in indexes and no further action is required regarding this extension.
* **Action Required:** If the query results are not empty, indicating the usage of `jsonknife`, you must follow the steps outlined below to ensure a smooth transition.

### Migration Steps for `jsonknife` Dependency

1. **Database Backup:**\
   Begin by creating a complete backup of your database. This can be achieved using the `pg_dump` command:&#x20;

```sql
pg_dump -U [username] [dbname] > [backupfile].sql
```

2.  **Replicating `jsonknife` Functions:**

    Run [this SQL](https://gist.github.com/Rost-is-love/6e3fa654871a7dfa8a7001acd648c899) to implement functions that replicate the functionality previously provided by the `jsonknife` extension.&#x20;
3. **Database Restoration:**\
   Once the new functions are in place and tested, proceed with restoring the database using `pg_restore`. This will apply your backup along with the newly created functions to AidboxDB 16.1:

```sql
pg_restore -U [username] -d [dbname] [backupfile].sql
```

