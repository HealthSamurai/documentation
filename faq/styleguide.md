# StyleGuide

### Headers

Don't put `.` at the end of the header:

```yaml
# good
Header

# bad
Header.
```

### Abbreviations and product names

Keep original case of abbreviations:

```yaml
# good
FHIR, API, JSON, SaaS

# bad
fhir, Api, json, saas
```

Use capitalized and consistent names for products:

```yaml
# good
Aidbox, Aidbox.Dev, Aidbox.Cloud, Aidbox.One, Fhirbase
​
# bad
aidbox, devbox, cloud version of aidbox, enterprise aidbox, fhirebase
```

### Requests

* `[ ]` some name, which should be substituted with something meaningful
* `{ }` mean optional

{% api-method method="get" host="\[base\]" path="/\[type\]/\[id\]" %}
{% api-method-summary %}
Example request
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="\[base\]" type="string" required=true %}

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

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

resourceType: Patient
name:
- given: [Max]
  family: Turikov
gender: male
birthDate: '1990-10-10'
address:
- line:
  - 123 Oxygen St
  city: Hello
  district: World
  state: NY
  postalCode: '3212'
telecom:
- use: home
- system: phone
  value: "(32) 8934 1234"
  use: work
  rank: 1
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

`200 OK` inside text

* **`200`** **OK** - as options for responses



'Button' names should be single-quoted.



Placeholders `<YOUR-BOX>` should be in SCREAMING-CAMEL-CASE.



 

