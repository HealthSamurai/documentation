# Metadata

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

### Example

For example, lets try to define two custom resources: **City** and **Country**. Country will consists of name, population and flag. City consists of name, phoneCode, reference to country and date of foundation. Also we want that Country.name, City.name and City.country attributes should be required. And we are want to search Country by name and population, City by name and country in which it is located

{% tabs %}
{% tab title="Request" %}
**POST:**  \[base\]/$metadata

```yaml
definitions:
  Country:
    properties:
      name: {"type": "string"}
      population: {"type": "number"}
      flag: {"type": "Attachment"}
    required: ["name"]
    search:
      name:
        expression: [["name"]]
        type: "string"
      population:
        expression: [["name"]]
        type: "number"
  City:
    properties:
      name: {"type": "string"}
      phoneCode: {"type": "string"}
      country: {"type": "Reference"}
      foundation: {"type": "date"}
    required: ["name", "country"]
    search:
      name:
        expression: [["name"]]
        type: "string"
      country:
        expression: [["country"]]
        type: "reference"
```
{% endtab %}

{% tab title="Response" %}
**Status:** 200

```yaml
Entity:
  Country:
    type: resource
  City:
    type: resource
Attribute:
  Country.name:
    type:
      id: string
      resourceType: Entity
    resource:
      id: Country
      resourceType: Entity
    path:
    - name
    isRequired: true
  Country.population:
    type:
      id: number
      resourceType: Entity
    resource:
      id: Country
      resourceType: Entity
    path:
    - population
  Country.flag:
    type:
      id: Attachment
      resourceType: Entity
    resource:
      id: Country
      resourceType: Entity
    path:
    - flag
  City.name:
    type:
      id: string
      resourceType: Entity
    resource:
      id: City
      resourceType: Entity
    path:
    - name
    isRequired: true
  City.phoneCode:
    type:
      id: string
      resourceType: Entity
    resource:
      id: City
      resourceType: Entity
    path:
    - phoneCode
  City.country:
    type:
      id: Reference
      resourceType: Entity
    resource:
      id: City
      resourceType: Entity
    path:
    - country
    isRequired: true
  City.foundation:
    type:
      id: date
      resourceType: Entity
    resource:
      id: City
      resourceType: Entity
    path:
    - foundation
SearchParameter:
  Country.name:
    expression:
    - - name
    type: string
    name: name
    resource:
      id: Country
      resourceType: Entity
  Country.population:
    expression:
    - - name
    type: number
    name: population
    resource:
      id: Country
      resourceType: Entity
  City.name:
    expression:
    - - name
    type: string
    name: name
    resource:
      id: City
      resourceType: Entity
  City.country:
    expression:
    - - country
    type: reference
    name: country
    resource:
      id: City
      resourceType: Entity
```
{% endtab %}
{% endtabs %}

