# GraphQL API

Aidbox supports default GraphQL implementation without any extensions \(spec located [here](http://spec.graphql.org/June2018/)\)  
Queries are supported, but mutations are not \(yet\)

In Aidbox UI we use GraphiQL interface so you can try your queries there. GraphQL console sends all your requests to `$graphql` endpoint which you can use from your application too

{% api-method method="post" host="" path="/$graphql" %}
{% api-method-summary %}
GraphQL endpoint
{% endapi-method-summary %}

{% api-method-description %}
This endpoint allows you to execute GraphQL queries .
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="query" type="string" required=true %}
GraphQL query
{% endapi-method-parameter %}

{% api-method-parameter name="variables" type="object" required=false %}
JSON object with variables
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Query successfully executed.
{% endapi-method-response-example-description %}

```javascript
{"data": {<query exectuion result>}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}



