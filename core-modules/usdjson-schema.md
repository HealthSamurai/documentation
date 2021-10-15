# $json-schema

[Aidbox](https://www.health-samurai.io/aidbox) does validation using JSON-schema, which is generated automatically from Entity & Attribute meta-resources. Operation $json-schema provides access to this JSON-schema to debug and inspect:

{% swagger baseUrl="" path="" method="get" summary="/$json-schema" %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="path" type="string" %}
Dot separated specific path in json schema (because it's huge)
{% endswagger-parameter %}

{% swagger-response status="200" description="" %}
```
```
{% endswagger-response %}
{% endswagger %}
