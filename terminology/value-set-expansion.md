# Value Set Expansion

Aidbox use de-normalized  approach to ValueSets. That means we pre-calculate valuesets in design time and store valueset id's into _Concept.valuset_ element \(see /Concept article\). That's why ValueSet expansion in aidbox is just a special case of Concept Search:

```http
GET [base]/fhir/ValueSet/23/$expand?filter=abdo
```

means

```http
GET [base]/Concept?valueset=23&filter=abdo
```

### Api

Official documentation [FHIR Terminology ValueSet Expansion](https://www.hl7.org/fhir/valueset-operations.html#expand)

```text
GET/POST URL: [base]/ValueSet/$expand
```

```text
GET/POST URL: [base]/ValueSet/[id]/$expand
```

Example for expand default AdministrativeGender ValueSet. [List of default ValueSets.](https://www.hl7.org/fhir/terminologies-valuesets.html)

```text
GET [base]/ValueSet/administrative-gender/$expand
```

Parameters

| Parameter | Type | Status | Example |
| :--- | :--- | :--- | :--- |
| url | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` | [url](value-set-expansion.md#url) |
| valueSet | [ValueSet](https://www.hl7.org/fhir/valueset.html) | `supported` | [valueSet](value-set-expansion.md#valueset) |
| context | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `not supported` | - |
| filter | [string](https://www.hl7.org/fhir/datatypes.html#string) | `supported` | [filter](value-set-expansion.md#filter) |
| profile | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `not supported` | - |
| date | [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime) | `not supported` | - |
| offset | [integer](https://www.hl7.org/fhir/datatypes.html#integer) | `supported` | [offset](value-set-expansion.md#offset) |
| count | [integer](https://www.hl7.org/fhir/datatypes.html#integer) | `supported` | [count](value-set-expansion.md#count) |
| includeDesignations | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` | - |
| includeDefinition | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` | - |
| activeOnly | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `supported` | [activeOnly](value-set-expansion.md#activeonly) |
| excludeNested | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` | - |
| excludeNotForUI | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` | - |
| excludePostCoordinated | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` | - |
| displayLanguage | [code](https://www.hl7.org/fhir/datatypes.html#code) | `not supported` | - |
| limitedExpansion | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean) | `not supported` | - |

#### url

{% tabs %}
{% tab title="Request" %}
```http
GET [base]/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender
```

Or

```javascript
POST [base]/ValueSet/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "url",
      "valueUri" : "http://hl7.org/fhir/ValueSet/administrative-gender"
     }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "id": "administrative-gender",
    "resourceType": "ValueSet",
    "url": "http://hl7.org/fhir/ValueSet/administrative-gender",
    "description": "The gender of a person used for administrative purposes.",
    "compose": {
        "include": [
            {
                "system": "http://hl7.org/fhir/administrative-gender"
            }
        ]
    },
    "name": "AdministrativeGender",
    "expansion": {
        "timestamp": "2018-09-25T16:24:55Z",
        "identifier": "http://hl7.org/fhir/ValueSet/administrative-gender",
        "contains": [
            {
                "code": "male",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/administrative-gender",
                "display": "Male",
                "definition": "Male"
            },
            {
                "code": "female",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/administrative-gender",
                "display": "Female",
                "definition": "Female"
            },
            {
                "code": "other",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/administrative-gender",
                "display": "Other",
                "definition": "Other"
            },
            {
                "code": "unknown",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/administrative-gender",
                "display": "Unknown",
                "definition": "Unknown"
            }
        ]
    },
    ......
}
```
{% endtab %}
{% endtabs %}

#### valueSet

{% tabs %}
{% tab title="Request" %}
```text
GET [base]/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender
```

Or

```javascript
POST [base]/ValueSet/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "url",
      "valueUri" : "http://hl7.org/fhir/ValueSet/administrative-gender"
     }
  ]
}
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

#### filter

{% tabs %}
{% tab title="First Tab" %}

{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

#### offset

{% tabs %}
{% tab title="First Tab" %}

{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

#### count

{% tabs %}
{% tab title="First Tab" %}

{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

#### activeOnly

{% tabs %}
{% tab title="First Tab" %}

{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

