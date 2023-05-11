# Set up uniqueness in Resource

Suppose we want to store Patient resource only if patient with such email does not already exist in the database. Hence we need to make `Patient.telecom.value` unique where `Patient.telecom.system = 'email'`.

## Create Unique Index

To solve the problem we can use PostgreSQL [unique indexes](https://www.postgresql.org/docs/current/indexes-unique.html).&#x20;

```sql
CREATE UNIQUE INDEX patient_email_unique_idx
ON patient ((jsonb_path_query_first(resource, '$.telecom[*] ? (@.system == "email").value')) #>> '{}');
```

In this example [jsonb\_path\_query\_first](https://www.postgresql.org/docs/15/functions-json.html) PostgreSQL function will check first email in Patient.telecom array. This works if the Patient resource can have only one email.&#x20;

If it can have two or more emails, the only solution we see is to create new `patient_email` table, store emails into it via [PostgreSQL triggers ](https://www.postgresql.org/docs/current/sql-createtrigger.html)when inserting/updating in `patient` table and make Unique index for `patient_email` as in the example above.&#x20;
