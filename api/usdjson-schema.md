# $json-schema

Aidbox does validation using JSON-schema, which is generated automatically from Entity & Attribute meta-resources. Operation $json-schema provides access to this JSON-schema to debug and inspect:

{% api-method method="get" host="" path="" %}
{% api-method-summary %}
/$json-schema
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="path" type="string" required=false %}
Dot separated specific path in json schema \(because it's huge\)
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

