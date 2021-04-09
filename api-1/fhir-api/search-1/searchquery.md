---
description: Managed SQL for Search API
---

# SearchQuery

With **SearchQuery** resource, you can define "managed"  SQL for Search API with parameters, paging, sorting and includes.

Let's start from a simple example. You want to search old patients by the partial match of the family name with filter by gender:

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
  where: "(pt.resource->>'birthDate')::date < '1980-01-01'"
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

If parameter is provided, another query will be generated on the fly:

```yaml
GET /alpha/Patient?query=q-1&family=joh

# 200

resourceType: Bundle
type: searchset
entry: [...]
query-sql:
- | 
  SELECT *
  FROM \"patient\" pt
  WHERE /* query */ (pt.resource->>'birthDate')::date < '1980-01-01'
    AND /* family */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
    ilike ?\nORDER BY pt.id desc
    LIMIT 100"
- '% joh%'
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
```

### Add order-by into parameters

Both `query` and `params` support `order-by`. `order-by` in query has the least precedence. `order-by` in params are added in top-down order. e.g. `order-by` in first search parameter has the most precedence.

Example: create search query

```yaml
PUT /SearchQuery/sq

as: ap
query:
  order-by: "ap.resource->>'start' ASC"
resource:
  id: 'Appointment'
  resourceType: 'Entity'
params:
  ord-dir:
    type: string
    format: '?'
    order-by: |
      CASE WHEN {{params.ord-dir}} = 'asc' THEN ap.resource->>'start' END ASC,
      CASE WHEN {{params.ord-dir}} = 'desc' THEN ap.resource->>'start' END DESC
```

Example: use this search query

```yaml
# GET /alpha/Appointment?query=sq&ord-dir=desc

resourceType: Bundle
type: searchset
entry:
  - resource:
      start: '2021-04-02T16:02:50.996+03:00'
      # omitted
  - resource:
      start: '2021-02-02T16:02:50.997+03:00'
      # omitted
  - resource:
      start: '2020-02-02T16:02:50.997+03:00'
      # omitted
# omitted
```

### Include related resources

You can predefine included resources for SearchQuery with **includes** property:

```yaml
resourceType: SearchQuery
resource: {id: Encounter, resourceType: Entity}
as: enc
total: true
includes:
  # name for include
  subject:
    # path to reference
    path: [subject]
    # ref to resource
    resource: {id: Patient, resourceType: Entity}
    # nested includes
    includes:
      organization:
        path: [managingOrganization]
        resource: {id: Organization, resourceType: Entity}
query: {order-by: enc.id}
limit: 40
```

#### Reverse includes

To include resources that refer resources from your query, you can add **reverse**: true attribute:

```yaml
resourceType: SearchQuery
resource: {id: Patient, resourceType: Entity}
as: pt
total: true
includes:
  encounters:
    # means reference going from Encounter to patient
    reverse: true
    path: [subject]
    resource: {id: Encounter, resourceType: Entity}
    where: "resource->>'status' = 'finished'"
limit: 40
```

#### Path in includes

Path expression in includes is `json_knife` extension path, it consists of strings, integers, and objects. If the item is path string, it means get key in object \(arrays are implicitly flattened\). If key is integer, it is interpreted as index in array. If key is object, it is pattern to filter values in array with inclusion semantic \(like PostgreSQL JSONB operator `@>`\). 

Here is an example of how to extract a patient \(code: PART\) from the appointment:

`["participant", {"type": [{"coding": [{"code": "PART"}]}, "actor"] => pt-2`

```yaml
resourceType: Appointment
status: active
participant:
- type:
  - text: Patient
    coding:
    - {code: PART}
  actor: {id: pt-2, resourceType: Patient}
  status: active
- type:
  - text: Admit
    coding:
    - {code: ADM}
   actor: {id: pr-2, resourceType: Practitioner}
   status: active

```

#### Parametrised includes

Include query can be parametrised if you define include inside params. You can use `where` key to add additional filter on included resources.

```yaml
PUT /SearchQuery/cond-incl

resource: {id: 'Patient', resourceType: 'Entity'}
as: pt
query:
  order-by: pt.id desc
params:
   obs-cat:
     type: string
     includes: 
        obs:
          reverse: true
          path: ["patient"]
          resource: {id: 'Observation', resourceType: 'Entity'}
          where: "resource#>>'{category,0,coding,0,code}' = {{params.category}}"
          
---

GET /alpha/Patient?query=cond-incl&category=labs
# will add filtered include

GET /alpha/Patient?query=cond-incl
# will skip include

```

If you want to provide default include, define include with the same key on query level and in parameter. Parameter include will override default in case parameter is provided in request.

```yaml
PUT /SearchQuery/cond-incl

resource: {id: 'Patient', resourceType: 'Entity'}
as: pt
query:
  order-by: pt.id desc
includes:
  # default include with filter
  obs:
    reverse: true
    path: ["patient"]
    resource: {id: 'Observation', resourceType: 'Entity'}
    where: "resource#>>'{category,0,coding,0,code}' = 'default"

params:
   obs-cat:
     type: string
     # override default include
     includes: 
        obs:
          where: "resource#>>'{category,0,coding,0,code}' = {{params.category}}"
          
```

### EXPLAIN ANALYZE

With the parameter `_explain=analyze` , you can inspect the execution plan of a search query:

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

### Debug SearchQuery

You can debug SearchQuery with multiple parameters combinations without saving resource by `POST /SearchQuery/$debug`. You can simulate requests with different parameters by  **tests** attribute. Aidbox will return results and explanation for each test:

```yaml
POST /SearchQuery/$debug

# explain all queries
explain: true
# timeout for query in ms
timeout: 2000
# test with requests
tests: 
  # name of request
  only-pid:
    # params for request
    params: {pid: 'pt-1'}
  only-ts:
    params: {ts: '2019-01-01'}
  both:
    params: {pid: 'pt-1', ts: 'ups'}
# SearchQuery defnition
query:
  resource: {id: Patient, resourceType: Entity}
  as: pt
  params:
    pid: {type: string, isRequired: true, where: 'pt.id = {{params.pid}}'}
    ts: {type: date, where: 'pt.tis >= {{params.date}}'}
  query: {order-by: pt.ts desc}
  limit: 40
  
  
  # 200
  
only-pid:
  params: {pid: pt-1, _timeout: 2000}
  result:
    resourceType: Bundle
    type: searchset
    entry:
    - resource:
        name:
        - given: [Andrew]
          family: John
        id: pt-1
        resourceType: Patient
        meta: {lastUpdated: '2019-09-10T11:24:00.481090Z', versionId: '1494'}
  explain:
    query: |-
      EXPLAIN ANALYZE SELECT * FROM "patient" pt
      WHERE /* pid */ pt.id = ?
      ORDER BY pt.ts desc
      LIMIT 40
    params: [pt-1]
    explain: |-
      Limit  (cost=8.18..8.19 rows=1 width=38) (actual time=0.032..0.033 rows=1 loops=1)
        ->  Sort  (cost=8.18..8.19 rows=1 width=38) (actual time=0.032..0.032 rows=1 loops=1)
              Sort Key: ts DESC
              Sort Method: quicksort  Memory: 25kB
              ->  Index Scan using patient_pkey on patient pt  (cost=0.15..8.17 rows=1 width=38) (actual time=0.024..0.025 rows=1 loops=1)
                    Index Cond: (id = 'pt-1'::text)
      Planning Time: 1.556 ms
      Execution Time: 0.057 ms
only-ts:
  status: error
  params: {ts: '2019-01-01', _timeout: 2000}
  errors:
  - {details: Parameter pid is required}
both:
  params: {pid: pt-1, ts: ups, _timeout: 2000}
  result:
    status: error
    query:
    - |-
      SELECT pt.*
      FROM "patient" pt
      WHERE /* pid */ pt.id = ?
        AND /* ts */ pt.tis >= ?
      ORDER BY pt.ts desc
      LIMIT 40
    - pt-1
    - null
    error: |-
      ERROR: column pt.tis does not exist
        Hint: Perhaps you meant to reference the column "pt.ts".
        Position: 73
```

