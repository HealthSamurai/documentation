# Value Set Expansion

Aidbox use de-normalized approach to ValueSets. That means we pre-calculate valuesets in design time and store valueset id's into _Concept.valuset_ element \(see /Concept article\). That's why ValueSet expansion in aidbox is just a special case of Concept Search:

```http
GET [base]/fhir/ValueSet/23/$expand?filter=abdo
```

means

```http
GET [base]/Concept?valueset=23&filter=abdo
```

## Api

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

### url

A canonical url for a value set.

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

### valueSet

The value set is provided directly as part of the request.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/ValueSet/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "valueSet",
      "resource" : {
        "resourceType": "ValueSet",
        "url": "http://custom/administrative-gender",
        "compose": {
           "include": [
              {
                "valueSet": ["http://hl7.org/fhir/ValueSet/administrative-gender"]
              }
          ]
        } 
       }
     }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "ValueSet",
    "url": "http://custom/administrative-gender",
    "compose": {
        "include": [
            {
                "valueSet": [
                    "http://hl7.org/fhir/ValueSet/administrative-gender"
                ]
            }
        ]
    },
    "expansion": {
        "timestamp": "2018-09-26T08:51:30Z",
        "identifier": "http://custom/administrative-gender",
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
    }
}
```
{% endtab %}
{% endtabs %}

### filter

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/ValueSet/administrative-gender/$expand?filter=male
```

Or

```javascript
POST [base]/ValueSet/administrative-gender/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "filter",
      "valueString" : "male"
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
            }
        ]
    },
    ......
}
```
{% endtab %}
{% endtabs %}

### offset

Paging support - where to start if a subset is desired \(default = 0\).

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/ValueSet/administrative-gender/$expand?offset=2
```

Or

```javascript
POST [base]/ValueSet/administrative-gender/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "offset",
      "valueInteger" : 2
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

### count

Paging support - how many codes should be provided in a partial page view.

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/ValueSet/administrative-gender/$expand?count=1
```

Or

```javascript
POST [base]/ValueSet/administrative-gender/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "count",
      "valueInteger" : 1
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
            }
        ]
    },
    ......
}
```
{% endtab %}
{% endtabs %}

### activeOnly

Controls whether inactive concepts are included or excluded in value set expansions.

For example we are create testing ValueSet with one cuurent active concept and one deprecated concept provided directly as part of the request.  


Get all concepts

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/ValueSet/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
       {
           "name" : "activeOnly",
           "valueBoolean": false
       },
     {
      "name" : "valueSet",
      "resource" : {
        "resourceType": "ValueSet",
        "url": "http://custom/testing",
        "compose": {
           "include": [
              {"system": "http://testing",
               "concept": [{"code": "active"},
                           {"code": "inactive",
                            "deprecated": true}]
              }
            ]
          } 
       }
     }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "ValueSet",
    "url": "http://custom/testing",
    "compose": {
        "include": [
            {
                "system": "http://testing",
                "concept": [
                    {
                        "code": "active"
                    },
                    {
                        "code": "inactive",
                        "deprecated": true
                    }
                ]
            }
        ]
    },
    "expansion": {
        "timestamp": "2018-09-26T09:19:03Z",
        "identifier": "http://custom/testing",
        "contains": [
            {
                "code": "active",
                "system": "http://testing"
            },
            {
                "code": "inactive",
                "system": "http://testing",
                "deprecated": true
            }
        ]
    }
}
```
{% endtab %}
{% endtabs %}

  
Get only active concepts

{% tabs %}
{% tab title="Request" %}
Get only active concepts

```javascript
POST [base]/ValueSet/$expand
{ 
  "resourceType" : "Parameters",
  "parameter" : [
       {
           "name" : "activeOnly",
           "valueBoolean": true
       },
     {
      "name" : "valueSet",
      "resource" : {
        "resourceType": "ValueSet",
        "url": "http://custom/testing",
        "compose": {
           "include": [
              {"system": "http://testing",
               "concept": [{"code": "active"},
                           {"code": "inactive",
                            "deprecated": true}]
              }
            ]
          } 
        }
     }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "ValueSet",
    "url": "http://custom/testing",
    "compose": {
        "include": [
            {
                "system": "http://testing",
                "concept": [
                    {
                        "code": "active"
                    },
                    {
                        "code": "inactive",
                        "deprecated": true
                    }
                ]
            }
        ]
    },
    "expansion": {
        "timestamp": "2018-09-26T09:19:03Z",
        "identifier": "http://custom/testing",
        "contains": [
            {
                "code": "active",
                "system": "http://testing"
            }
        ]
    }
}
```
{% endtab %}
{% endtabs %}

