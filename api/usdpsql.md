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

Example request:

{% code-tabs %}
{% code-tabs-item title="request" %}
```yaml
POST /$sql?_format=yaml

SELECT count(*) FROM patient
```
{% endcode-tabs-item %}
{% endcode-tabs %}

{% code-tabs %}
{% code-tabs-item title="response" %}
```yaml
- {count: 7}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

