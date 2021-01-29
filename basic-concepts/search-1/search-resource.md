---
description: New Search resource provides fine-grained control over search parameters
---

# Search Resource

You can define search parameters or override the existing one with Search meta-resource. Search resource takes precedence over [SearchParameter](searchparameter.md). This may be useful for performance optimization of built-in FHIR SearchParameters or for the implementation of complicated custom searches.

```yaml
PUT /Search/Patient.name

resourceType: Search
id: Patient.name # id of Search resource
name: name # name of the new search parameter
resource: # link to the Patient resource
  id: Patient
  resourceType: Entity
where: "{{table}}.resource->>'name' ilike {{param}}" # sql for search
format: "%?%" # parameter format for ilike 
order-by: "{{table}}.resource#>>'{name,0,family}'" # sql for ordering
```

#### SQL Templating

In "where" and "order-by" expressions you can use `{{table}}` for table name and `{{param}}` for  parameter.

#### format

You can provide format string for value where `?` will be replaced with value of parameter. This feature may be useful for `ilike` expressions 

**token search**

You can define search parameters for different token syntax forms and `:text` modifier \(other modifiers may be implemented in future\).   
To refer to system and code in SQL query use `{{param.system}}` and `{{param.code}}` accordingly.  
To refer to value of param with `:text` modifier use `{{param.text}}`   
For `:text` modifier you also need to specify `"text-format"`, refer to `{{param.text}}` with `?`. `"text-format"` is a format string which will be applied to`{{param.text}}`  before inserting into SQL query. It is useful for wrapping text with `%` for `like` or `ilike.` For example `text-format: '%?%'`

```yaml
PUT /Search/<resourceType>.<parameter>

resourceType: Serach
name: <parameter>
resource: {id: <resourceType>, resourceType: Entity}
param-parser: token
token:
  only-code: <SQL query for parameter={{param.code}}>
  no-system: <SQL query for parameter=|{{param.code}}>
  only-system: <SQL query for parameter={{param.system}}|>
  both: <SQL query for parameter={{param.system}}|{{param.code}}>
  text: <SQL query for parameter:text={{param.text}}>
  text-format: <format string {{param.text}}>
```

#### multi: array

If you set multi = 'array' parameters will be coerced as PostgreSQL array  
Currently it `does not` support `param-parser: token`

#### Examples \(executable in REST console\)

Search patient name with SQL ilike:

```yaml
# create patient resource

PUT /Patient/my-patient

resourceType: Patient
id: my-patient
name:
 - family: johnson
 
 
# create search resource

PUT /Search/Patient.name

resourceType: Search
id: Patient.name 
name: name 
resource: 
  id: Patient
  resourceType: Entity
where: "{{table}}.resource->>'name' ilike {{param}}" 
format: "%?%" 
order-by: "{{table}}.resource#>>'{name,0,family}'" 


# execute search for new parameter
# check query-sql field in response bundle

GET /Patient?name=john

GET /Patient?_sort=name
```

Search patient identifiers with array search parameter:

```yaml
# create patient resources (one query at a time)

PUT /Patient/my-patient

resourceType: Patient
id: my-patient
identifier:
 - value: id1
 
###

PUT /Patient/my-patient-1

resourceType: Patient
id: my-patient-1
identifier:
 - value: id2
 
 
# create search resource 

PUT /Search/Patient.identifier

resourceType: Search
id: Patient.identifier 
resource: 
 resourceType: Entity
 id: Patient
where: knife_extract_text({{table}}.resource, '[["identifier","value"]]') && {{param}}
multi: array


# execute searches and retrieve two patients
# check query-sql field in response bundle

GET /Patient?identifier=id1,id2,id3
```

token search:

```yaml
PUT /
ServiceRequest.identifier


resourceType: Search
id: ServiceRequest.identifier
name: identifier
param-parser: token
token-sql:
  only-code: 'knife_extract_text({{table}}.resource, ''[["identifier","value"]]'') && ARRAY[{{param.code}}]'
  only-system: 'knife_extract_text({{table}}.resource, ''[["identifier",  "system"]]'') && ARRAY[{{param.system}}]'
  no-system: 'knife_extract_text({{table}}.resource, ''[["identifier","value"]]'') && ARRAY[{{param.code}}]'
  both: '(knife_extract_text({{table}}.resource, ''[["identifier","value"]]'') && ARRAY[{{param.code}}]) AND ({{table}}.resource->''identifier'' @> jsonb_build_array(jsonb_build_object(''system'', {{param.system}}::text, ''value'', {{param.code}}::text)))'
  text: 'array_to_string(knife_extract({{table}}.resource, ''[["identifier"]]''), '''') ilike {{param.text}}'
  text-format: '%?%'
where: '(knife_extract_text({{table}}.resource, ''[["identifier","value"]]'') && ARRAY[{{param.code}}]) AND ({{table}}.resource->''identifier'' @> jsonb_build_array(jsonb_build_object(''system'', {{param.system}}::text, ''value'', {{param.code}}::text)))'
resource: {id: ServiceRequest, resourceType: Entity}


GET /ServiceRequest?identifier=foo
# will result in querying with knife_extract(...) && ARRAY['foo']

GET /ServiceRequest?identifier:text=foo
# will result in querying with array_to_string(knife_extract (...)) ilike '%foo%'

GET /ServiceRequest?identifier:not=foo
# will result fallback to default implementation NOT resource @> '{"identifier": [{"value": "foo"}]}'
```

