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

#### multi: array

If you set multi = 'array' parameters will be coerced as PostgreSQL array

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
# create patient resources

PUT /Patient/my-patient

resourceType: Patient
id: my-patient
identifier:
 - value: id1
 
 
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

