# Custom Search

With `AidboxQuery` resource you can turn your SQL query into REST Endpoint.

For example lets create a simple aggregation report for encounters parameterized by date. Create an `AidboxQuery` resource:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /AidboxQuery

resourceType: AidboxQuery
id: daily-report
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
{% endtab %}

{% tab title="Response" %}
```yaml
query: "SELECT \n   resource->>'class' as class, \n   count(*) as count\nFROM encounter\
  \ \nWHERE {{params.date}}\nBETWEEN (resource#>>'{period,start}')::date \nAND (resource#>>'{period,end}')::date\n\
  GROUP BY resource->>'class'"
params:
  date: {isRequired: true}
id: daily-report
resourceType: AidboxQuery
meta:
  lastUpdated: '2018-11-29T09:51:55.166Z'
  versionId: '2'
  tag:
  - {system: 'https://aidbox.app', code: created}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}
```yaml
POST /

type: transaction
entry:
- resource:
    status: draft
    class: {code: IMP}
    period: {start: "2013-06-08T10:57:34", end: "2013-06-08T12:00:00"}
  request:
    method: POST
    url: "/Encounter"

- resource:
    status: draft
    class: {code: IMP}
    period: {start: "2013-06-08T11:00:05", end: "2013-06-08T11:30:00"}
  request:
    method: POST
    url: "/Encounter"

- resource:
    status: draft
    class: {code: AMB}
    period: {start: "2013-06-08T10:21:01", end: "2013-06-08T11:42:11"}
  request:
    method: POST
    url: "/Encounter"

- resource:
    status: draft
    class: {code: IMP}
    period: {start: "2013-06-07T09:02:01", end: "2013-06-07T15:10:09"}
  request:
    method: POST
    url: "/Encounter"
```
{% endtab %}

{% tab title="Response" %}
```yaml
id: '3'
type: transaction-response
resourceType: Bundle
entry:
- resource:
    class: {code: IMP}
    period: {end: '2013-06-08T12:00:00', start: '2013-06-08T10:57:34'}
    status: draft
    id: e38c8817-4009-45c6-9490-f7c4af900756
    resourceType: Encounter
  status: 201
- resource:
    class: {code: IMP}
    period: {end: '2013-06-08T11:30:00', start: '2013-06-08T11:00:05'}
    status: draft
    id: 996627c0-3b98-4c12-8b4c-651f714977a9
    resourceType: Encounter
  status: 201
- resource:
    class: {code: AMB}
    period: {end: '2013-06-08T11:42:11', start: '2013-06-08T10:21:01'}
    status: draft
    id: 8ba15fdb-3885-4bd5-99dc-79c142e5daf0
    resourceType: Encounter
  status: 201
- resource:
    class: {code: IMP}
    period: {end: '2013-06-07T15:10:09', start: '2013-06-07T09:02:01'}
    status: draft
    id: 68dc1ca4-f4f8-4961-8aff-c19b09ba712a
    resourceType: Encounter
  status: 201
```
{% endtab %}
{% endtabs %}

When you created AidboxQuery, you can use it:

{% tabs %}
{% tab title="Request" %}
```
GET /$query/daily-report?date=2013-06-08
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 200

data:
- {class: '{"code": "IMP"}', count: 2}
- {class: '{"code": "AMB"}', count: 1}
query: ["SELECT \n   resource->>'class' as class, \n   count(*) as count\nFROM encounter\
    \ \nWHERE ?\nBETWEEN (resource#>>'{period,start}')::date \nAND (resource#>>'{period,end}')::date\n\
    GROUP BY resource->>'class'", '2013-06-08']
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
PostgreSQL supports Special Date/Time Inputs like **now**, **today**, **tomorrow** etc
{% endhint %}

### Parameters in Query

Query can be parameterized by special template language `{{path.to.parameter}}`

All parameters passed in query string will be available under `{{params.PARAMETER-NAME}}`

Also `{{user.id}}` will be available, for example `user-info` custom query can be implemented like this:

{% code-tabs %}
{% code-tabs-item title="request" %}
```yaml
POST /AidboxQuery

query: 'select * from public.User where id = {{user.id}}'
id: user-info
resourceType: AidboxQuery
```
{% endcode-tabs-item %}
{% endcode-tabs %}

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
It's not possible to call such AidboxQuery from REST Console, because in REST console there are no user claims. It can be done only by request with access token provided. Check [OAuth2.0](../security/oauth-2.0/) doc for additional information.
{% endhint %}

### \_query

There are another option for calling `AidboxQuery`:

```
GET /Patient?_query=get-by-id&rid=patient1
```

Main difference is that such query can use additional variable available in context `{{resourceType}}`.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /AidboxQuery

resourceType: AidboxQuery
query: 'select * from {{resourceType}} where id = {{params.rid}}'
params:
  rid: {isRequired: true}
id: get-by-id
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 201

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
{% endtab %}
{% endtabs %}

Example usage:

{% tabs %}
{% tab title="Request" %}
```text
GET /Attribute?_query=get-by-id&rid=Encounter.status
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 200

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
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Pay attention that only `{{resourceType}}` can be used in place of table name, because this variable is secure and will be inserted directly into query. Other variables will be escaped and can't be used in such parts of query.
{% endhint %}

See also our tutorial:

{% page-ref page="../tutorials/custom-search.md" %}

