---
description: Endpoint to run sql
---

# SQL endpoints

{% api-method method="get" host="<base-url>/$sql" path="" %}
{% api-method-summary %}
$sql
{% endapi-method-summary %}

{% api-method-description %}
Execute SQL in aidbox
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="body" type="string" required=false %}
JSON: SQL string or jdbc freendly array  \[SQL, param, param\]
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
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

{% api-method method="get" host="<base-url>/$psql" path="" %}
{% api-method-summary %}
$psql
{% endapi-method-summary %}

{% api-method-description %}
TBD
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="" type="string" required=false %}

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

