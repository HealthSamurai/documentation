---
description: ilike search by resource content
---

# \_ilike

With **\_ilike** search parameter you search for terms inclusion as substring in text representation of FHIR resource. Interesting feature of this parameter, that it can provide quick feedback to user about matches without forcing to print the whole word (as with full text search). For example `jo` will find Johns and Jolie or `asp` will match Aspirin.

That's why it is default search in Aidbox Console user interface.

```
GET /Patient?_ilike=joh+smit,jes+park
```

With **\_ilike** parameter you term separated with space combined with `AND` and separated by comma `,` with `OR`. Example above is translated into SQL query like this:

```sql
SELECT * FROM patient
WHERE
resource::text ilike '%joh%' AND ... ilike '%smit%'
OR
resource::text ilike '%jes%' AND ... '%park%'
```

ILIKE search can be efficiently indexed with trigram PostgreSQL extension and GIN Index, providing response in tens of miliseconds responses on milions of records.

```sql
CREATE INDEX patient_trgm_idx  on patient
USING gin (
  (id || ' ' || resource::text) gin_trgm_ops
);

VACUUM ANALYZE patient;
```
