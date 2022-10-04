---
description: Search resource provides fine-grained control over search parameters
---

# Search Resource

Search meta-resource can be used to define search parameters or override the existing one. Search resources take precedence over [SearchParameters](searchparameter.md). This may be useful for performance optimization of built-in FHIR SearchParameters or for the implementation of complicated custom searches.

```yaml
PUT /Search/Patient.name
content-type: text/yaml
accept: text/yaml

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

Use `{{table}}` for table name and `{{param}}` for parameter in "where" and "order-by" expressions.\
Provided `format` parameter will be passed to `{{param}}` (`?` will be replaced with the value of parameter). This feature may be useful writing `ilike` expressions.

### **Token search**

You can define Search resources for different token syntax forms and `:text` modifier.\
To refer to system and code in SQL query use `{{param.system}}` and `{{param.code}}` accordingly.\
To refer to value of param with `:text` modifier use `{{param.text}}`\
When using the `:text` modifier you also need to specify `"text-format"`, refer to `{{param.text}}` with `?`.\
`"text-format"` is a format string which will be applied to`{{param.text}}` before inserting into SQL query. It is useful for wrapping text with `%` for `like` or `ilike.` For example `text-format: '%?%'`

```yaml
PUT /Search/<resourceType>.<parameter>
content-type: text/yaml
accept: text/yaml

resourceType: Search
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

### **Reference search**

Allows use different reference types in "where" expression. Reference can be defined [in several ways](http://www.hl7.org/fhir/search.html#reference):

* `{{param.resourceType}}` for `ResourceType` and `{{param.id}}` for resource `id`
* `{{param.id}}` for resource `id`
* `{{param.url}}` for resource `url`

```yaml
PUT /Search/<resourceType>.<parameter>
content-type: text/yaml
accept: text/yaml

resourceType: Search
name: <parameter>
resource: {id: <resourceType>, resourceType: Entity}
param-parser: reference
```

### Multi: array parameter

If you set multi = 'array', parameters will be coerced as PostgreSQL array.

{% tabs %}
{% tab title="PUT /Patient" %}
```yaml
# create patient resources
PUT /Patient/my-patient
content-type: text/yaml
accept: text/yaml

resourceType: Patient
id: my-patient
identifier:
 - value: id1
```
{% endtab %}

{% tab title="PUT /Patient" %}
```yaml
PUT /Patient/my-patient-1
content-type: text/yaml
accept: text/yaml

resourceType: Patient
id: my-patient-1
identifier:
 - value: id2
```
{% endtab %}

{% tab title="PUT /Search" %}
```yaml
# create search resource 
PUT /Search/Patient.identifier
content-type: text/yaml
accept: text/yaml

resourceType: Search
id: Patient.identifier 
resource: 
 resourceType: Entity
 id: Patient
where: knife_extract_text({{table}}.resource, '[["identifier","value"]]') && {{param}}
multi: array
```
{% endtab %}

{% tab title="GET /Patient" %}
```yaml
# execute searches and retrieve two patients
# check query-sql field in response bundle
GET /Patient?identifier=id1,id2,id3
content-type: text/yaml
accept: text/yaml
```
{% endtab %}
{% endtabs %}

### Examples

#### Search patient name with SQL ilike

{% tabs %}
{% tab title="PUT /Patient" %}
```yaml
# create patient resource
PUT /Patient/my-patient
content-type: text/yaml
accept: text/yaml

resourceType: Patient
id: my-patient
name:
 - family: johnson
```
{% endtab %}

{% tab title="PUT /Search" %}
```yaml
# create search resource
PUT /Search/Patient.name
content-type: text/yaml
accept: text/yaml

resourceType: Search
id: Patient.name 
name: name 
resource: 
  id: Patient
  resourceType: Entity
where: "{{table}}.resource->>'name' ilike {{param}}" 
format: "%?%" 
order-by: "{{table}}.resource#>>'{name,0,family}'" 
```
{% endtab %}

{% tab title="GET /Patient" %}
```yaml
# execute search for new parameter
# check query-sql field in response bundle
GET /Patient?name=john
content-type: text/yaml
accept: text/yaml

GET /Patient?_sort=name
content-type: text/yaml
accept: text/yaml
```
{% endtab %}
{% endtabs %}

#### Search if patient is [deceased](https://www.hl7.org/fhir/patient.html#search)

```yaml
PUT /Search/Patient.name
content-type: text/yaml
accept: text/yaml

resourceType: Search
id: Patient.deceased
name: deceased
resource:
  id: Patient
  resourceType: Entity
where: "coalesce((resource#>>'{deceased,boolean}')::boolean, resource ?? 'deceased', false) = {{param}}"
```

#### Token search

{% tabs %}
{% tab title="PUT /Search" %}
```yaml
PUT /Search/ServiceRequest.identifier
content-type: text/yaml
accept: text/yaml

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
```
{% endtab %}

{% tab title="GET /ServiceRequest" %}
```yaml
GET /ServiceRequest?identifier=foo
content-type: text/yaml
accept: text/yaml
# will result in querying with knife_extract(...) && ARRAY['foo']
```
{% endtab %}

{% tab title="GET /ServiceRequest" %}
```yaml
GET /ServiceRequest?identifier:text=foo
content-type: text/yaml
accept: text/yaml
# will result in querying with array_to_string(knife_extract (...)) ilike '%foo%'
```
{% endtab %}

{% tab title="GET /ServiceRequest" %}
```yaml
GET /ServiceRequest?identifier:not=foo
content-type: text/yaml
accept: text/yaml
# will result fallback to default implementation NOT resource @> '{"identifier": [{"value": "foo"}]}'
```
{% endtab %}
{% endtabs %}

#### Reference search by type/id

{% tabs %}
{% tab title="PUT /Search" %}
```yaml
PUT /Search/Patient.generalPractitioner
content-type: text/yaml
accept: text/yaml

resourceType: Search
id: Patient.generalPractitioner
name: generalPractitioner
param-parser: reference
where: '{{table}}.resource->'generalPractitioner' @>  jsonb_build_array(jsonb_build_object('id', {{param.id}}::text, 'resourceType', {{param.resourceType}}::text)) '
resource: {id: Patient, resourceType: Entity}
```
{% endtab %}

{% tab title="GET /Patient" %}
```yaml
# search Patient by Practitioner reference
GET /Patient?generalPractitioner=Practitioner/pract-1
content-type: text/yaml
accept: text/yaml
```
{% endtab %}
{% endtabs %}

#### Reference search by id

{% tabs %}
{% tab title="PUT /Search" %}
```yaml
PUT /Search/Patient.generalPractitioner
content-type: text/yaml
accept: text/yaml

resourceType: Search
id: Patient.generalPractitioner
name: generalPractitioner
param-parser: reference
where: '{{table}}.resource->'generalPractitioner' @>  jsonb_build_array(jsonb_build_object('id', {{param.id}}::text))  '
resource: {id: Patient, resourceType: Entity}
```
{% endtab %}

{% tab title="GET /Patient" %}
```yaml
# search Patient by Pratitoner reference
GET /Patient?generalPractitioner=pract-1
content-type: text/yaml
accept: text/yaml
```
{% endtab %}
{% endtabs %}

#### Reference search by url

{% tabs %}
{% tab title="PUT /Search" %}
```yaml
PUT /Search/Patient.myProfile
content-type: text/yaml
accept: text/yaml

resourceType: Search
id: Patient.myProfile
name: myProfile
param-parser: reference
where: '{{table}}.resource#>'{meta,profile}' @>  jsonb_build_array({{param.url}}::text) '
resource: {id: Patient, resourceType: Entity}
```
{% endtab %}

{% tab title="GET /Patient" %}
```yaml
# search Patient by profile
GET /Patient?myProfile=http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient
content-type: text/yaml
accept: text/yaml
```
{% endtab %}
{% endtabs %}
