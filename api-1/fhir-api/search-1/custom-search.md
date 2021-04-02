---
description: Turn SQL into REST endpoint
---

# AidboxQuery

With the `AidboxQuery` resource, you can turn your SQL query into REST Endpoint.

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
query: 'SELECT * from patient where id ilike = {{params.filter}} limit {{params.count}}
# if count-query is present - it will be evaluated for total property in response
count-query: 'SELECT count(*) from patient where id ilike = {{params.filter}}
```

Here is a self-debugging AidboxQuery to start with:

```yaml
PUT /AidboxQuery/debug

params:
  filter:
    isRequired: true
    type: string
    format: '%% %s%%'
  count:
    type: integer
    default: 10
  data:
    type: object
    default: {resourceType: 'Nop'}
  flag:
    default: true
    type: boolean
query: |
  SELECT 
   {{params.filter}}::text as filter,
   {{params.flag}} as flag,
   {{params.data}}::jsonb as data,
   {{params}}::jsonb as params,
   {{params.count}} as count,
   {{}} as ctx
count-query: |
  SELECT {{params.count}}

GET /$query/debug?filter=ups&data=%7B%22a%22%3A%201%7D
                            ^ url encoded {"a": 1}
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

Let's upload some sample data using [Bulk Upsert](../../transaction.md#bulk-upsert):

```yaml
PUT /

- status: draft
  class: {code: IMP}
  period: {start: "2013-06-08T10:57:34", end: "2013-06-08T12:00:00"}
  resourceType: Encounter
  id: enc-1

- status: draft
  class: {code: IMP}
  period: {start: "2013-06-08T11:00:05", end: "2013-06-08T11:30:00"}
  resourceType: Encounter
  id: enc-2

- status: draft
  class: {code: AMB}
  period: {start: "2013-06-08T10:21:01", end: "2013-06-08T11:42:11"}
  resourceType: Encounter
  id: enc-3

- status: draft
  class: {code: IMP}
  period: {start: "2013-06-07T09:02:01", end: "2013-06-07T15:10:09"}
  resourceType: Encounter
  id: enc-3
```

After you created AidboxQuery, you can use it:

```yaml
GET /$query/daily-report?date=2013-06-08

# Status: 200

data:
- {class: '{"code": "IMP"}', count: 2}
- {class: '{"code": "AMB"}', count: 1}
query: [...]
```

{% hint style="info" %}
PostgreSQL supports Special Date/Time inputs like **now**, **today**, **tomorrow** etc.
{% endhint %}

### Design AidboxQuery 

To design the aidbox query, you can use `POST /$query/$debug`  endpoint without the need to create an AidboxQuery resource:

```yaml
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

```yaml
GET /$query/daily-report?date=2013-06-08&_explain=true

# Status: 200

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

### Parameters in Query

Query can be parameterised by the special template language `{{path.to.parameter}}`

All parameters passed in query string will be available under `{{params.PARAMETER-NAME}}`

Also, `{{user.id}}` will be available, for example `user-info` custom query can be implemented like this:

{% code title="request" %}
```yaml
POST /AidboxQuery

query: 'select * from public.User where id = {{user.id}}'
id: user-info
resourceType: AidboxQuery
```
{% endcode %}

Sample query will be:

{% tabs %}
{% tab title="Request" %}
```http
GET /$query/user-info HTTP/1.1
Host: <YOUR-BOX>.aidbox.app
Authorization: Bearer <YOUR-ACCESS-TOKEN>
Accept: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 200

data:
- id: testuser
  txid: 198
  ts: '2018-10-16T13:30:03.036Z'
  resource_type: User
  status: updated
  resource: {email: testmail@mail.com, password: $s0$f0801$72nz8sgiT91maOn8zzOppA==$PtBarKD+2TafNX+k7sBeejnvfl+N5o2VhAGA7y+JIRA=}
query: ['select * from public.User where id = ?', testuser]

```
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
It's not possible to call such AidboxQuery from REST Console, because in REST console there are no user claims. It can be done only by request with the access token provided. Check [OAuth2.0]() doc for additional information.
{% endhint %}

### \_query

There is another option for calling `AidboxQuery`:

```
GET /Patient?_query=get-by-id&rid=patient1

#or 

GET /fhir/Patient?_query=get-by-id&rid=patient1
```

The result will be represented as the Search Bundle. If you call it from `fhir/` base-url, resulting resources will be transformed to the FHIR compliant representation.



The main difference is that such a query can use an additional variable available in context of `{{resourceType}}`.

```yaml
POST /AidboxQuery

resourceType: AidboxQuery
query: 'select * from {{resourceType}} where id = {{params.rid}}'
params:
  rid: {isRequired: true}
id: get-by-id

# resp

query: select * from {{resourceType}} where id = {{params.rid}}
params:
  rid: {isRequired: true}
id: get-by-id
resourceType: AidboxQuery
meta:
  lastUpdated: '2018-11-28T15:33:03.073Z'
  versionId: '11'
  tag:
  - {system: 'https://aidbox.app', code: created}
```

Example usage:

```yaml
GET /Attribute?_query=get-by-id&rid=Encounter.status

# resp

data:
- id: Encounter.status
  txid: 0
  ts: '2018-11-07T10:10:41.051Z'
  resource_type: Attribute
  status: updated
  resource:
    resource: {id: Encounter, resourceType: Entity}
    valueSet: {id: encounter-status, resourceType: ValueSet}
    path: [status]
    module: fhir-3.0.1
    order: 10
    source: code
    type: {id: code, resourceType: Entity}
    isSummary: true
    resourceType: Attribute
    description: planned | arrived | triaged | in-progress | onleave | finished | cancelled +
    isModifier: true
    isRequired: true
query: ['select * from Attribute where id = ?', Encounter.status]
```

{% tabs %}
{% tab title="Request" %}
```text
GET /Attribute?_query=get-by-id&rid=Encounter.status
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Pay attention that only`{{resourceType}}`can be used in place of table name, because this variable is secure and will be inserted directly into the query. Other variables will be escaped and can't be used in such parts of a query.
{% endhint %}

See another tutorial:

{% page-ref page="../../../app-development-guides/tutorials/custom-search.md" %}

