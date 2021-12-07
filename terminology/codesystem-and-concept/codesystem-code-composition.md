# CodeSystem Code Composition

## Overview

Given a set of properties, return one or more possible matching codes. For more details, see the official FHIR terminology documentation [CodeSystem Code Composition](https://www.hl7.org/fhir/codesystem-operations.html#compose)

$compose may return  3 possible types of match

* complete match - a code that represents all the provided properties correctly
* partial match - a code that represents some of the provided properties correctly and not others
* possible match - code that may represent the provided properties closely

When send `exact` parameter is `true` - $compose operation only returns complete and partial matches. When exact is `false` - $compose include possible matches to a response. Default value is true; that means that by default returning only complete and partial matches.

## &#x20;Api

```
GET/POST URL: [base]/ValueSet/$compose
```

```
GET/POST URL: [base]/ValueSet/[id]/$compose
```

## Parameters

| Parameter                  | Type                                                                | Status          | Example                                             |
| -------------------------- | ------------------------------------------------------------------- | --------------- | --------------------------------------------------- |
| system                     | [uri](https://www.hl7.org/fhir/datatypes.html#uri)                  | `supported`     | [property](codesystem-code-composition.md#property) |
| version                    | [string](https://www.hl7.org/fhir/datatypes.html#string)            | `not supported` |                                                     |
| exact                      | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)          | `supported`     | [exact](codesystem-code-composition.md#exact)       |
| property                   |                                                                     | `supported`     | [property](codesystem-code-composition.md#property) |
| property.code              | [code](https://www.hl7.org/fhir/datatypes.html#code)                | `supported`     |                                                     |
| property.value             | <p>code | Coding | string | </p><p>integer | boolean | dateTime</p> | `supported`     |                                                     |
| property.subproperty       |                                                                     | `not supported` |                                                     |
| property.subproperty.code  | [code](https://www.hl7.org/fhir/datatypes.html#code)                | `not supported` |                                                     |
| property.subproperty.value | <p>code | Coding | string | </p><p>integer | boolean | dateTime</p> | `not supported` |                                                     |
| compositional              | [boolean](https://www.hl7.org/fhir/datatypes.html#boolean)          | `not supported` |                                                     |

### property

One or more properties that contain information to be composed into the code.

**Example:** Compose from system `http://hl7.org/fhir/goal-status`  where `code` is `proposed`

{% tabs %}
{% tab title="Request" %}
```javascript
POST [BASE]/CodeSystem/$compose
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
    },
    {
      "name" : "exact",
      "valueUri" : true
    },
    {
     	"name": "property",
     	"part": [
     		{
     			"name": "code",
     		    "valueCode": "code"
            },
            {
            	"name": "value",
            	"valueString": "proposed"
            }]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "match",
            "part": [
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "proposed",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "Proposed",
                        "definition": "A goal is proposed for this patient"
                    }
                }
            ]
        }
    ]
}
```
{% endtab %}
{% endtabs %}

**Example:** Compose from system `http://hl7.org/fhir/goal-status`  where `code` is `proposed` or `accepted`&#x20;

{% tabs %}
{% tab title="Request" %}
```javascript
POST [BASE]/CodeSystem/$compose
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
    },
    {
      "name" : "exact",
      "valueUri" : true
    },
    {
     	"name": "property",
     	"part": [
     		{
     			"name": "code",
     		    "valueCode": "code"
            },
            {
            	"name": "value",
            	"valueString": "proposed"
            },
            {
            	"name": "value",
            	"valueString": "accepted"
            }]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "match",
            "part": [
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "accepted",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "Accepted",
                        "definition": "A proposed goal was accepted or acknowledged"
                    }
                },
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "proposed",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "Proposed",
                        "definition": "A goal is proposed for this patient"
                    }
                }
            ]
        }
    ]
}
```
{% endtab %}
{% endtabs %}

**Example:** Compose from system `http://hl7.org/fhir/goal-status`  where `code` is `proposed` or `accepted` and/or where `definition` is `Planned`&#x20;

{% tabs %}
{% tab title="Request" %}
```javascript
POST [BASE]/CodeSystem/$compose
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
    },
    {
      "name" : "exact",
      "valueUri" : true
    },
    {
     	"name": "property",
     	"part": [
     		{
     			"name": "code",
     		    "valueCode": "code"
            },
            {
            	"name": "value",
            	"valueString": "proposed"
            },
            {
            	"name": "value",
            	"valueString": "accepted"
            }]
    },
    {
     	"name": "property",
     	"part": [
     		{
     			"name": "code",
     		    "valueCode": "display"
            },
            {
            	"name": "value",
            	"valueString": "Planned"
            }]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "match",
            "part": [
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "accepted",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "Accepted",
                        "definition": "A proposed goal was accepted or acknowledged"
                    }
                },
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "proposed",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "Proposed",
                        "definition": "A goal is proposed for this patient"
                    }
                },
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "planned",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "Planned",
                        "hierarchy": [
                            "accepted"
                        ],
                        "definition": "A goal is planned for this patient"
                    }
                }
            ]
        }
    ]
}
```
{% endtab %}
{% endtabs %}

### exact

Whether the operation is being used by a human, or a machine. When `false` include to response possible matches.

**Example:** lets try to compose code system from concepts where the code contains `on-`

{% tabs %}
{% tab title="Request" %}
```javascript
POST [BASE]/CodeSystem/$compose
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
    },
    {
      "name" : "exact",
      "valueUri" : false
    },
    {
     	"name": "property",
     	"part": [
     		{
     			"name": "code",
     		    "valueCode": "code"
            },
            {
            	"name": "value",
            	"valueString": "on-"
            }]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "match",
            "part": [
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "on-target",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "On Target",
                        "hierarchy": [
                            "accepted",
                            "in-progress"
                        ],
                        "definition": "The goal is on schedule for the planned timelines"
                    }
                },
                {
                    "name": "code",
                    "valueCoding": {
                        "code": "on-hold",
                        "module": "fhir-3.3.0",
                        "system": "http://hl7.org/fhir/goal-status",
                        "display": "On Hold",
                        "hierarchy": [
                            "accepted"
                        ],
                        "definition": "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time."
                    }
                }
            ]
        }
    ]
}
```
{% endtab %}
{% endtabs %}

