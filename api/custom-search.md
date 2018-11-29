# Custom Search

With `AidboxQuery` resource you can turn your SQL query into REST Endpoint.

For example lets create a simple aggregation report for encounters parameterized by date. Create an `AidboxQuery` resource:

{% code-tabs %}
{% code-tabs-item title="request" %}
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
  WHERE (resource#>>'{period,start}')::date = {{params.date}}
  GROUP BY resource->>'class'
```
{% endcode-tabs-item %}
{% endcode-tabs %}

When you created AidboxQuery, you can use it:

{% tabs %}
{% tab title="request" %}
```
GET /$query/daily-report?date=today
```
{% endtab %}

{% tab title="response" %}
```yaml
# Status: 200

query: "SELECT ...."
duration: 2
data:
  - class: { .... }
    count: 100
  - class: { .... }
    count: 5
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
{% tab title="request" %}
```http
GET /$query/user-info HTTP/1.1
Host: <YOUR-BOX>.aidbox.app
Authorization: Bearer <YOUR-ACCESS-TOKEN>
Accept: text/yaml
```
{% endtab %}

{% tab title="response" %}
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

There are another option for calling AidboxQuery:

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

