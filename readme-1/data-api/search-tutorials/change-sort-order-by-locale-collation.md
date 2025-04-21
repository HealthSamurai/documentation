# Change sort order by locale collation

Consider this example:

```sql
select * from (values('Abildlunden'),('Æblerosestien'),('Agern Alle 1')) x(word)
order by word collate "en_GB";
```

PostgreSQL is expanding "Æ" into "AE"- which is a correct rule for English.

```
     word      
---------------
 Abildlunden
 Æblerosestien
 Agern Alle 1
(3 rows)
```

However, in Danish the correct order will be

```sql
select * from (values('Abildlunden'),('Æblerosestien'),('Agern Alle 1')) x(word)
order by word collate "da_DK";
```

```
     word      
---------------
 Abildlunden
 Agern Alle 1
 Æblerosestien
(3 rows)
```

## Change locale collation

By default Aidbox uses en\_US.utf8 locale. **Aidboxdb version 14.7** supports locale collation changes. To change cluster locale to Danish, use [PostgreSQL locale variables](https://www.postgresql.org/docs/current/locale.html) and [Aidboxdb variable](../../../reference/environment-variables/aidboxdb-environment-variables.md#optional-environment-variables): `EXTRA_LOCALES` to make PostgreSQL able to collate with the passed languages:&#x20;

{% code title="docker-compose.yaml" %}
```yaml
  aidbox-db:
    image: "${PGIMAGE}"
    pull_policy: always
    ports:
      - "${PGHOSTPORT}:5432"
    volumes:
      - "./pgdata:/data"
    environment:
      POSTGRES_USER:     "${PGUSER}"
      POSTGRES_PASSWORD: "${PGPASSWORD}"
      POSTGRES_DB:       "${PGDATABASE}"
      LC_COLLATE: "da_DK.UTF-8"
      LC_CTYPE: "da_DK.UTF-8"
      EXTRA_LOCALES: "fr_CA.UTF-8,en_GB.UTF-8"
      ...

```
{% endcode %}

If these variables were set before the first start of aidboxdb (i.e. when the cluster is not initialized yet), then PostgreSQL will set cluster (and database) locales from environment variables.

Hence the SQL request

```sql
select * from (values('Abildlunden'),('Æblerosestien'),('Agern Alle 1')) x(word)
order by word;
```

Will return the right order for Danish locale.

Any [\_sort Search Parameter](broken-reference) will also consider Danish sort order.
