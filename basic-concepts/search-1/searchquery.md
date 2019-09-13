---
description: Control query SQL for Search API
---

# SearchQuery

With **SearchQuery** resource you can define "managed"  SQL for Search API with parameters, paging, sorting and includes.

Let's start from simple example - you want so search old patients by partial match of family name with filter by gender:

```yaml
PUT /SearchQuery/q-1

# attach this query to Patient resource type
resource: {id: 'Patient', resourceType: 'Entity'}
# give alias to patient table
as: pt
# enable total query
total: true 
# basic query
query:
  where: "(pt.resource->>'birthDate')::date < '1980'"
  order-by: pt.id desc
params:
   gender:
     type: string
     where: "pt.resource->>'gender' = {{params.gender}}"
   family:
     type: string
     format: '% ?%'
     where: |
       aidbox_text_search(knife_extract_text(pt.resource, $$[["name","family"]]$$)) 
       ilike {{params.family}}
```

Now we can call this query with `/alpha/<resourceType>?query=<query-name>&params....`:

```yaml
GET /alpha/Patient?query=q-1&_page=2&_count=3&_total=none

# 200
resourceType: Bundle
type: searchset
entry: [...]
query-sql: |
  SELECT *
  FROM "patient" pt
  WHERE /* query */ (pt.resource->>'birthDate')::date < '1980-01-01'
  ORDER BY pt.id desc
  LIMIT 100
query-timeout: 60000
```

You can use **count** and **page** parameters for paging and control total query \(if enabled\) with **total** parameter. Use **\_timeout** parameter to set query timeout.

Or with parameter:

```yaml
GET /alpha/Patient?query=q-1&family=joh

# 200

resourceType: Bundle
type: searchset
entry: []
query-sql:
- | 
  SELECT *
  FROM \"patient\" pt
  WHERE /* query */ (pt.resource->>'birthDate')::date < '1980-01-01'
    AND /* family */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
    ilike ?\nORDER BY pt.id desc
    LIMIT 100"
- '% joh%'
query-timeout: 60000
```

### Add JOIN

You parameters and basic query can use join attribute to join related resources for search:

```yaml
PUT /SearchQuery/q-2

resource: {id: 'Encounter', resourceType: 'Entity'}
as: enc
query:
  order-by: pt.id desc
params:
   pt:
     type: string
     format: '% ?%'
     join:
       pt: 
         table: patient
         by: "enc.resource#>>'{subject,id}' = pt.id"
     where: |
        aidbox_text_search(knife_extract_text(pt.resource, $$[["name","family"]]$$)) 
        ilike {{params.pt}}
```

```yaml
GET /alpha/Encounter?query=q-2&pt=joh

# 200
resourceType: Bundle
type: searchset
entry: [...]
query-sql:
-  |
  SELECT *
  FROM \"encounter\" enc
  JOIN \"patient\" pt
    ON enc.resource#>>'{subject,id}' = pt.id
  WHERE /* pt */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
   ilike ?
  ORDER BY pt.id desc\nLIMIT 100"
- '% joh%'
query-timeout: 60000
```

### EXPLAIN ANALYZE

With parameter `_explain=analyze` you can inspect execution plan of search query:

```yaml
GET /alpha/Encounter?query=q-2&pt=joh&_explain=analyze

# 200

query: |-
  EXPLAIN ANALYZE SELECT *FROM \"encounter\" enc
  JOIN \"patient\" pt
    ON enc.resource#>>'{subject,id}' = pt.id
  WHERE /* pt */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
    ilike ?
    ORDER BY pt.id desc
    LIMIT 100"
params: ['% joh%']
explain: |-
  Limit  (cost=1382.90..1382.97 rows=28 width=882) (actual time=4.274..4.274 rows=0 loops=1)
    ->  Sort  (cost=1382.90..1382.97 rows=28 width=882) (actual time=4.272..4.272 rows=0 loops=1)
          Sort Key: pt.id DESC
          Sort Method: quicksort  Memory: 25kB
          ->  Hash Join  (cost=951.07..1382.23 rows=28 width=882) (actual time=4.247..4.248 rows=0 loops=1)
                Hash Cond: ((enc.resource #>> '{subject,id}'::text[]) = pt.id)
                ->  Seq Scan on encounter enc  (cost=0.00..421.60 rows=3460 width=839) (actual time=0.779..1.544 rows=3460 loops=1)
                ->  Hash  (cost=950.95..950.95 rows=10 width=38) (actual time=1.375..1.375 rows=1 loops=1)
                      Buckets: 1024  Batches: 1  Memory Usage: 9kB
                      ->  Seq Scan on patient pt  (cost=0.00..950.95 rows=10 width=38) (actual time=1.370..1.371 rows=1 loops=1)
                            Filter: (immutable_wrap_ws(immutable_unaccent(immutable_array_to_string(knife_extract_text(resource, '[["name", "family"]]'::jsonb), ' '::text))) ~~* '% joh%'::text)
                            Rows Removed by Filter: 1
  Planning Time: 9.345 ms
  Execution Time: 4.564 ms
total-query: "EXPLAIN ANALYZE SELECT count(*)\nFROM \"encounter\" enc\nJOIN \"patient\" pt\n  ON enc.resource#>>'{subject,id}' = pt.id\nWHERE /* pt */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) \nilike ?"
total-explain: |-
  Aggregate  (cost=1382.30..1382.31 rows=1 width=8) (actual time=3.257..3.257 rows=1 loops=1)
    ->  Hash Join  (cost=951.07..1382.23 rows=28 width=0) (actual time=3.254..3.254 rows=0 loops=1)
          Hash Cond: ((enc.resource #>> '{subject,id}'::text[]) = pt.id)
          ->  Seq Scan on encounter enc  (cost=0.00..421.60 rows=3460 width=772) (actual time=0.286..0.910 rows=3460 loops=1)
          ->  Hash  (cost=950.95..950.95 rows=10 width=5) (actual time=1.198..1.199 rows=1 loops=1)
                Buckets: 1024  Batches: 1  Memory Usage: 9kB
                ->  Seq Scan on patient pt  (cost=0.00..950.95 rows=10 width=5) (actual time=1.195..1.195 rows=1 loops=1)
                      Filter: (immutable_wrap_ws(immutable_unaccent(immutable_array_to_string(knife_extract_text(resource, '[["name", "family"]]'::jsonb), ' '::text))) ~~* '% joh%'::text)
                      Rows Removed by Filter: 1
  Planning Time: 6.716 ms
  Execution Time: 3.543 ms
```

