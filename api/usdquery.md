# $query

With AidboxQuery resource you can turn your SQL query into REST Endpoint.

For example lets create simple aggregation report for encounters parametrised by date. We can create AidboxQuery resource:

{% code-tabs %}
{% code-tabs-item title="request" %}
```yaml
POST /AidboxQuery

resourceType: AidboxQuery
id: daily-report
params:
  date:
     type: date
     isRequired: true
query: |
  SELECT 
     resource->>'class' as class, 
     count(*) as count
  FROM encounter 
  WHERE resource#>>'{period,start}'::date = {{params.date}}
  GROUP BY resource->>'class'
```
{% endcode-tabs-item %}
{% endcode-tabs %}

When you created AidboxQuery, you can use it:

{% code-tabs %}
{% code-tabs-item title="request" %}
```yaml
GET /$query/daily-report?date=today&_format=yaml
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="response" %}
```yaml
query: "SELECT ...."
duration: 2
data:
  - class: { .... }
    count: 100
  - class: { .... }
    count: 5    
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Parameters in Query

### Extend FHIR search with AidboxQuery \(\_query\)

