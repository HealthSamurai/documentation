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

