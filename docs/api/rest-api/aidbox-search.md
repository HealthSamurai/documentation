# Aidbox Search

Sometimes, FHIR search is not enough. Aidbox provides more ways to search:

* [Search resource](aidbox-search.md#search-resource) - similar to SearchParameter, but allows defining SQL. It is composable with other SearchParameters and Search resources.
* [AidboxQuery](aidbox-search.md#aidboxquery) - a more general way to search using SQL. DSL to build complex queries using the new endpoint. It is not composable with other SearchParameters and Search resources.
* [Dot expressions](aidbox-search.md#dot-expressions) - search without SearchParameters.
* [$lookup](aidbox-search.md#usdlookup) - efficient lookup for resources by key attributes.

Also see [special search parameters defined by Aidbox](aidbox-search.md#aidbox-special-search-parameters), e.g. `_explain` and `_timeout`.

## Search resource

Aidbox Search resource defines a search parameter or overrides the existing one. Search resources take precedence over FHIR [SearchParameters](fhir-search/searchparameter.md). This may be useful for the performance optimization of built-in FHIR SearchParameters or for the implementation of complicated custom searches.

### Example

```
PUT /Search/Patient.name
content-type: text/yaml
accept: text/yaml

name: name
resource:
  id: Patient
  resourceType: Entity
where: "{{table}}.resource->>'name' ilike {{param}}" # sql for search
format: "%?%" # parameter format for ilike 
order-by: "{{table}}.resource#>>'{name,0,family}'" # sql for ordering (using _sort)
```

### Search resource structure <a href="#search-resource-structure" id="search-resource-structure"></a>

See [Search resource structure](../../reference/system-resources-reference/base-module-resources.md#search).

### Value parsing

In case of reference or identifier search, the value must be parsed:

* `resourceType` and `id` or `url` in case of reference search
* `code` and `system` in case of identifier search

#### Reference search

Allows the use of different reference types in the "where" expression. Reference can be defined [in several ways](http://www.hl7.org/fhir/search.html#reference):

* `{{param.resourceType}}` for `ResourceType` and `{{param.id}}` for resource `id`
* `{{param.id}}` for resource `id`
* `{{param.url}}` for resource `url`

```
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

#### Token search

To refer to the system and code in the SQL query, use `{{param.system}}` and `{{param.code}}` accordingly.&#x20;

```
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

To refer to the value of the parameter with `:text` modifier use `{{param.text}}`&#x20;

When using the `:text` modifier, you also need to specify `"text-format"`, refer to `{{param.text}}` with `?`.`"text-format"` is a format string that will be applied to`{{param.text}}` before inserting it into the SQL query. It is useful for wrapping text with `%` for `like` or `ilike.` For example `text-format: '%?%'`

See tutorial:

{% content-ref url="../../tutorials/crud-search-tutorials/search-tutorials/create-custom-aidbox-search-resource.md" %}
[create-custom-aidbox-search-resource.md](../../tutorials/crud-search-tutorials/search-tutorials/create-custom-aidbox-search-resource.md)
{% endcontent-ref %}

## AidboxQuery

With the AidboxQuery resource, you can turn your SQL query into a REST endpoint.

```yaml
PUT /AidboxQuery/<query-name>

params:
  # define filter parameter
  filter:
    # make it required
    isRequired: true
    # it's type is string (can be integer, number, object, boolean)
    type: string
    # format is java format string, which will be applied to value
    # this useful to get for example ilike query  expr ilike '% value%'
    # do not forget to escape % with one more %
    format: '%% %s%%'
    # you can set default value
    default: 'ups'
  count:
    type: integer
    default: 10
# sql query with parameters {{path.to.ctx.elements}}
query: 'SELECT * from patient where id ilike = {{params.filter}} limit {{params.count}}'
# if count-query is present - it will be evaluated for total property in response
count-query: 'SELECT count(*) from patient where id ilike = {{params.filter}}'
# not required. enable links in response, see the section below
enable-links: false
# not required. omit sql in query response
omit-sql: false
# not required. `query` or `execute`. see below
type: query
```

### Example

For example, let's create a simple aggregation report for encounters parameterised by date. Create an `AidboxQuery` resource:

```yaml
PUT /AidboxQuery/daily-report

params:
  date:
     isRequired: true
query: |
  SELECT 
     resource->>'class' as class, 
     count(*) as count
  FROM encounter 
  WHERE {{params.date}}
  BETWEEN (resource#>>'{period,start}')::date 
  AND (resource#>>'{period,end}')::date
  GROUP BY resource->>'class'
```

Call it like this (data and query keys):

```
GET /$query/daily-report?date=2013-06-08
```

Or like this (Search Bundle):

```
GET /fhir/Patient?_query=get-by-id&rid=patient1
```

The main difference is that such a query can use an additional variable available in the context of `{{resourceType}}`.

### Query types <a href="#query-types" id="query-types"></a>

AidboxQuery has `type` field, which can be either `query` or `execute`. Default type is query. This means that _SELECT_ statement in query parameter is expected. If you want to make SQL query with execute statements e.g. _TRUNCATE_, use `execute` type.

```
PUT /AidboxQuery/truncate

query: 'TRUNCATE {{resourceType}}; TRUNCATE {{resourceType}}_history'
type: execute
```

### Return links <a href="#return-links" id="return-links"></a>

You can use `enable-links` parameter to include [links](https://www.hl7.org/fhir/http.html#paging) in the response. Here is simple example how to use paging with AidboxQuery and include links.

```
PUT /AidboxQuery/q1

query: |
  SELECT 
   sr.*
  FROM ServiceRequest sr
  WHERE sr.Resource #>> '{subject,id}' = {{params.patient}}
  LIMIT {{params._count}} OFFSET {{params._page}}
count-query: |
  SELECT count(*) FROM ServiceRequest sr 
  WHERE sr.Resource #>> '{subject,id}' = {{params.patient}}

enable-links: true

params:
  patient: 
    type: string
    isRequired: true
    default: "pt1"
  _count:
    type: integer
    default: 100
  _page:
    type: integer
    default: 1
```

AidboxQuery expects that parameters `_count` and `_page` (exactly such names) are defined, otherwise links won't be attached.

Use like this:&#x20;

```
GET /$query/q1?patient=pt1&_count=1&_page=2
```

### Design AidboxQuery

To design the aidbox query, you can use `POST /$query/$debug` endpoint without the need to create an AidboxQuery resource:

```
POST /$query/$debug

query:
  # AidboxQuery resource content
  query: 'SELECT {{params.id}} as params_id'
  params:
    id: {isRequired: true}
# test params
params: 
  id: 'ups'
  
---
# actual result
data:
- {params_id: ups}
# sql query
query: ['SELECT ? as params_id', ups]
# execution plan
plan: |-
  Result  (cost=0.00..0.01 rows=1 width=32) (actual time=0.009..0.022 rows=1 loops=1)
  Planning Time: 0.025 ms
  Execution Time: 0.067 ms
# templating context
ctx:
  remote-addr: 0:0:0:0:0:0:0:1
  client: { ... }
  params: {id: ups}
  headers: {...}
  uri: /$query/$debug
  user: {...}
  scheme: http
  request-method: post
```

### Debug AidboxQuery

You can debug AidboxQuery with `_explain=true` parameter:

```
GET /$query/daily-report?date=2013-06-08&_explain=true

plan: |-
  HashAggregate  (cost=27.27..27.97 rows=56 width=40) (actual time=0.443..0.459 rows=1 loops=1)
    Group Key: (resource ->> 'class'::text)
    ->  Seq Scan on encounter  (cost=0.00..26.96 rows=62 width=32) (actual time=0.398..0.420 rows=2 loops=1)
          Filter: (('2013-06-08'::date >= ((resource #>> '{period,start}'::text[]))::date) AND ('2013-06-08'::date <= ((resource #>> '{period,end}'::text[]))::date))
          Rows Removed by Filter: 1
  Planning Time: 3.222 ms
  Execution Time: 0.600 ms
ctx:
  params: {date: '2013-06-08', _explain: 'true'}
  resourceType: null
  safe-paths:
  - [resourceType]
query:
- "SELECT \n   resource->>'class' as class, \n   count(*) as count\nFROM encounter \nWHERE ?\nBETWEEN (resource#>>'{period,start}')::date \nAND (resource#>>'{period,end}')::date\nGROUP BY resource->>'class'"
- '2013-06-08'
```

## Dot expressions

With parameters started with `.`, you can provide the exact path for the element, optionally provide coercing after `::` using PostgreSQL types and the operator after `$`.

### Example

```
GET /Patient?.name.0.family=Johnson
=> WHERE resource#>>'{name,0,family}' = 'Jonhnson'

GET /Patient?.name.0.family$contains=Joh
=> WHERE resource#>>'{name,0,family}' ilike '%John%'

GET /Encounter?.start::timestamptz$gt=2015-01-01
=> WHERE (resource#>>'{start}')::timestamptz > '2015-01-01'

GET /Patient?.contact$isnull=true
=> WHERE resource#>>'{contact}' IS NULL
```

Note: expressions with typecast require user input to be correct by PostgreSQL syntax. For example, for `timestamptz` values you must use `2015-01-01T00:00:00Z` format.

## $lookup

There are scenarios when you want to quickly look up patients or practitioners with the prefix search by multiple key elements like a family name, date of birth, and identifier. Prefix search means you want to say in the query string `jo do 79` and find `John Doe with 1979 birthdate`. Sometimes, there are millions of patients in your database, and you want to do it efficiently to show type-ahead dropdown choices in your UI.

$lookup operations are especially designed to be an efficient implementation for this case.

There is no way to implement an efficient multidimensional prefix search with ranking and sorting in a relational database. $lookup based on specific assumptions to find the right trade-off: if the search returns more than count (by default 50) results, we consider that the search failed and results can have some anomalies, for example, not complete sorting.

Here is how it works.

First of all, you have to describe priority groups of attributes with **by** parameter. Groups are separated by `;` and inside group, you specify the list of paths separated by `,`. Each path expression consists of dot separated elements and indexes and should end with primitive type (examples: `name.given` or `identifier.value`).

The result will be sorted according to an order of priority groups. For example, if you want to rate first matches of name, identifier, and birth of data, and second matches in address and telecom, you will use the following expression:`name.family,name.given,identifier.value,birthDate;address.state,address.city,address.line,telecom.value`.

Let's say you are searching `joh 1979 ny` Aidbox will initially search in first priority group by expression like this:

```
expr = extract_space_separated(resource, paths) 
where expr ilike ' % joh' AND  expr ilike '% 1979'
limit 50
```

If this query returns 50 records, Aidbox will respond with these records.

```
GET /Patient/$lookup?\
  by=name.family,name.given,birthDate,identifier.value;address.city,address.line&\
  sort=name.family,name.given&\
  q=Joh+Do+1980&\
  count=50&\
  limit=200
```

### Parameters

Note: Each path expression should point to a primitive element!

* `by`: `;`-separated list of priority groups. Each group is `,`-separated list of path expressions.
* `sort`: `,`-separated list of path expressions to sort by
* `q`: is `+` or space separated term (prefixes) to search
* `limit`: is internal search limit (default 200)
* `count`: number results to return (default 50)
* `mode`: if mode = `index` Aidbox returns index DDL for a specific search

## Aidbox special search parameters

### \_explain

Use `_explain` parameter to inspect the search query execution plan.

```
GET /fhir/Encounter?subject:Patient._ilike=john&_explain=analyze
```

Response:

```
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

If your query is slow and you see Seq Scans , it's time to build indexes. Do not forget to run a vacuum analyze on the tables involved in query. Read more about [PostgreSQL Explain](https://www.postgresql.org/docs/current/using-explain.html).

This parameter can be used for debugging too. If an SQL error happens, `_explain` will show the original query:

```
GET /fhir/Patient?error-demo=1&_explain=0

exception: |-
  ERROR: division by zero
    Where: SQL function "divide" during inlining
query:
  - 'SELECT "patient".* FROM "patient" WHERE (divide(1, ?) = 2) LIMIT ? OFFSET ? '
  - '0'
  - 100
  - 0
```

### \_timeout

With `_timeout` parameter, you can control the search query timeout in seconds. If the query takes more than the timeout value, it will be cancelled. The default timeout value is 60 seconds.

See also [Default timeout ](../../reference/settings/fhir.md#fhir.search.default-params.timeout)setting.

### \_createdAt

Search by the creation time of the resource `meta.createdAt` (`cts` column in the database table).

```javascript
GET /fhir/Patient?_createdAt=2019-01-01
```

You can use operators `lt,le,gt,ge` like in other date search parameters.

### \_result

By default, the search result is returned as a FHIR Bundle. You can change this behavior by setting `_result=array` and your search result will be returned as JSON array with resources, without the Bundle envelope:

```
GET /fhir/Patient?_result=array
# 200
- id: pt1
  resourceType: Patient
  name:
  - given: [Adam]
    family: Smith
- name:
  - given: [Andrew]
    family: John
  id: pt-1
  resourceType: Patient
```

### \_search-language

`_search-language` is experimental SearchParameter. It can be used to search for a specified language.

```
GET /fhir/<resource>?_search-language=<locale>&<string-param>=<value>
```

Any [string search parameters](https://www.hl7.org/fhir/search.html#string) (e.g. name) will search in the desired language if  `_search-language` is specified in the query. Specifying only `_search-language` without any other string search parameters won't affect anything (except `_sort`).

You may use locales that are [specified](https://hl7.org/fhir/valueset-languages.html) in FHIR.

#### Resources with Translation Extension

Aidbox searches for [Translation Extension](https://build.fhir.org/ig/HL7/fhir-extensions/StructureDefinition-translation.html) and if the resource contains it and the language is correct, then searches by the content of this translation.

Structure of a resource containing translation extension:

```yaml
resourceType: <resourceType>
id: <id>
name: <name>
_name:
  extension:
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: <locale1>
        - url: content
          valueString: <translation in locale1> 
    - url: http://hl7.org/fhir/StructureDefinition/translation
      extension:
        - url: lang
          valueCode: <locale2>
        - url: content
          valueString: <translation in locale2>
```

See also:

{% content-ref url="broken-reference" %}
[Broken link](broken-reference)
{% endcontent-ref %}
