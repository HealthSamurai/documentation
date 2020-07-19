---
description: New Search resource provides fine-grained control over search parameters
---

# Search Resource

You can define search parameters or override the existing one with Search meta-resource. Search resource takes precedence over [SearchParameter](searchparameter.md). This may be useful for performance optimization of built-in FHIR SearchParameters or for the implementation of complicated custom searches.

```yaml
resourceType: Search
id: Patient.name # id of resource
name: name # name of search parameter
resource: {id: Patient, resourceType: Entity} # link to resource type
where: {{table}}.resource->'name' ilike {{param}} # sql string for search
order-by: {{table}}.resoource#>>'{name,0,family}' # sql string for search
format: '%?%' # format parameter value
# multi: array # coerce params to array
```

#### SQL Templating

In "where" and "order-by" expressions you can use `{{table}}` for table name and `{{param}}` for  parameter.

#### format

You can provide format string for value where `?` will be replaced with value of parameter. This feature may be useful for `ilike` expressions 

#### multi: array

If you set multi = 'array' parameters will be coerced as PostgreSQL array

#### Examples

```yaml
resourceType: Search
id: Patient.name # id of resource
resource: {id: Patient, resourceType: Entity}
where: {{table}}.resource->'name' ilike {{param}}
order-by: {{table}}.resoource#>>'{name,0,family}'
format: '%?%'

GET /Patient?name=joh
=> select * from patient where patient.resource->'name' ilike '%joh%'

GET /Patient?_sort=name
=> select * from patient where patient.resource->'name' ilike '%joh%'
```

```yaml
resourceType: Search
id: Patient.identifier # id of resource
resource: {id: Patient, resourceType: Entity}
where: knife_extract_text({{table}}.resource, '[["identifier","value"]]') && {{param}}
multi: array

GET /Patient?identifier=id1,id2,id3

=> select * from patient 
  where knife_extract_text({{table}}.resource, '[["identifier","value"]]') 
      && ARRAY['id1', 'id2', 'id3']
```

