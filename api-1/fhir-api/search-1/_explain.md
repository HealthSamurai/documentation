---
description: Debug & Optimise your SQL queries for Search
---

# \_explain

With the nonstandard `_explain` parameter you can inspect the search query execution plan.

```yaml
GET /Encounter?subject:Patient._ilike=john&_explain=analyze

-- resp
query: ['SELECT * FROM (SELECT DISTINCT ON ("encounter".id) "encounter".* FROM "encounter" INNER JOIN patient subject_patient ON ("encounter".resource @> ($JSON${"subject":{"id":"$JSON$ || subject_patient.id || $JSON$","resourceType":"Patient"}}$JSON$)::jsonb) WHERE ((("subject_patient".id || '' '' ||  "subject_patient".resource::text) ilike ?)) ) "encounter" LIMIT ? OFFSET ?', '%john%', 100, 0]
plan: |-
  Limit  (cost=94.22..94.25 rows=2 width=116) (actual time=2.188..2.196 rows=0 loops=1)
    ->  Unique  (cost=94.22..94.23 rows=2 width=116) (actual time=2.169..2.178 rows=0 loops=1)
          ->  Sort  (cost=94.22..94.23 rows=2 width=116) (actual time=2.152..2.161 rows=0 loops=1)
                Sort Key: encounter.id
                Sort Method: quicksort  Memory: 25kB
                ->  Nested Loop  (cost=0.00..94.21 rows=2 width=116) (actual time=2.107..2.116 rows=0 loops=1)
                      Join Filter: (encounter.resource @> ((('{"subject":{"id":"'::text || subject_patient.id) || '","resourceType":"Patient"}}'::text))::jsonb)
                      ->  Seq Scan on encounter  (cost=0.00..15.60 rows=560 width=116) (actual time=2.087..2.109 rows=0 loops=1)
                      ->  Materialize  (cost=0.00..22.62 rows=4 width=32) (never executed)
                            ->  Seq Scan on patient subject_patient  (cost=0.00..22.60 rows=4 width=32) (never executed)
                                  Filter: (((id || ' '::text) || (resource)::text) ~~* '%john%'::text)
  Planning Time: 6.040 ms
  Execution Time: 2.522 ms
```

If your query is slow and you see `Seq Scans` , it's time to build indexes. Do not forget to run `vacuum analyze` on tables involved in query. Read more about [PostgreSQL Explain](https://www.postgresql.org/docs/11/using-explain.html).
