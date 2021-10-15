# $metadata

[Aidbox](https://www.health-samurai.io/aidbox) is a metadata driven platform, therefore when you want to define new ResourceType, or Attribute you need to create a couple of resources, such as Entity and Attribute etc. But sometimes this way is not convenient. For this reason, we support the special endpoint that provides an easier way to define custom Metadata resources.

{% swagger baseUrl="[base]" path="/$metadata" method="post" summary="Metadata" %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="body" name="definitions" type="object" %}
Metadata object
{% endswagger-parameter %}

{% swagger-response status="200" description="Result of metadata expand" %}
```
```
{% endswagger-response %}

{% swagger-response status="422" description="Explain of errors" %}
```
```
{% endswagger-response %}
{% endswagger %}
