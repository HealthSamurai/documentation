# View Definition



### Create View Definition with Aidbox

#### Aidbox UI

1. Go to Database > View Definition
2. Describe view using FHIRPath expressions

{% tabs %}
{% tab title="View Definiton" %}
```json
{
  "name": "patient_view",
  "resource": "Patient",
  "desc": "Patient flat view",
  "select": [
    {
      "name": "id",
      "expr": "id"
    },
    {
      "name": "bod",
      "expr": "birthDate"
    },
    {
      "name": "gender",
      "expr": "gender"
    }
  ]
}
```
{% endtab %}

{% tab title="Flat view" %}
|   |   |   |
| - | - | - |
|   |   |   |
|   |   |   |
|   |   |   |
{% endtab %}
{% endtabs %}

#### REST API

You can create View Definitions using REST API

{% tabs %}
{% tab title="Request" %}

{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

### Materialize&#x20;

Once you create a view definition you can materialize this view. There're several options:&#x20;

* PostgreSQL view
* Materilze into a table

#### Plain View

#### Table
