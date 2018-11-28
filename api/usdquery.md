# $query

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
Status: 200

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
```text
Status: 200

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
It's not possible to do such request from REST Console, because in REST console there are no user credentials. It can be done only by request with access token provided. Check [OAuth2.0](../security/oauth-2.0/) doc for additional information.
{% endhint %}

See also our tutorial:

{% page-ref page="../tutorials/custom-search.md" %}

