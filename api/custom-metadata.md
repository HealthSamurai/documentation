# $metadata

Aidbox is a metadata driven platform, therefore when you want to define new ResourceType, or Attribute you need to create a couple of resources, such as Entity and Attribute etc. And sometimes this way is not convenient. For this reason we support special endpoint that provide to you more simply way to define custom Metadata resources.

{% api-method method="post" host="\[base\]" path="/$metadata" %}
{% api-method-summary %}
Metadata
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="definitions" type="object" required=true %}
Metadata object
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Result of metadata expand
{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=422 %}
{% api-method-response-example-description %}
Explain of errors
{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

