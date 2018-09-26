---
description: >-
  Test the subsumption relationship between code/Coding A and code/Coding B
  given the semantics of subsumption in the underlying code system.
---

# Subsumption testing

### Request structure

`URL: [base]/CodeSystem/$subsumes`

or:

`URL: [base]/CodeSystem/[id]/$subsumes`

### For example

We will use [goal-status CodeSystem](https://www.hl7.org/fhir/codesystem-goal-status.html), which consists of :

| Code | Parent codes |
| :--- | :--- |
| accepted | `-` |
| achieved | `["accepted"]` |
| ahead-of-target | `["accepted", "in-progress"]` |
| behind-target | `["accepted", "in-progress"]` |
| cancelled | `-` |
| entered-in-error | `-` |
| in-progress | `["accepted"]` |
| on-hold | `["accepted"]` |
| on-target | `["accepted", "in-progress"]` |
| planned | `["accepted"]` |
| proposed | `-` |
| rejected | `-` |
| sustaining | `["accepted", "in-progress"]` |

### Result of subsumption

The subsumption relationship between code/Coding "A" and code/Coding "B". There are 4 possible codes to be returned: `equivalent`, `subsumes`, `subsumed-by,` and `not-subsumed`. If the server is unable to determine the relationship between the codes/Codings, then it returns an error. 

### Requests examples

#### With code

{% hint style="info" %}
`equivalent`
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem/$subsumes?system=http://hl7.org/fhir/goal-status&codeA=accepted&codeB=accepted
```

`or:`

```javascript
POST [base]/CodeSystem/$subsumes
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
     },
     {
     	"name": "codeA",
     	"valueCode": "accepted"
     },
     {
     	"name": "codeB",
     	"valueCode": "accepted"
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
            "name": "outcome",
            "value": {
                "code": "equivalent"
            }
        }
    ]
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
`subsumes`
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem/$subsumes?system=http://hl7.org/fhir/goal-status&codeA=accepted&codeB=achieved
```

`or:`

```javascript
POST [base]/CodeSystem/$subsumes
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
     },
     {
     	"name": "codeA",
     	"valueCode": "accepted"
     },
     {
     	"name": "codeB",
     	"valueCode": "achieved"
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
            "name": "outcome",
            "value": {
                "code": "subsumed"
            }
        }
    ]
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
`subsumed-by`
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem/$subsumes?system=http://hl7.org/fhir/goal-status&codeA=achieved&codeB=accepted
```

`or:`

```javascript
POST [base]/CodeSystem/$subsumes
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
     },
     {
     	"name": "codeA",
     	"valueCode": "achieved"
     },
     {
     	"name": "codeB",
     	"valueCode": "accepted"
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
            "name": "outcome",
            "value": {
                "code": "subsumed-by"
            }
        }
    ]
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
`not-subsumed`
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem/$subsumes?system=http://hl7.org/fhir/goal-status&codeA=cancelled&codeB=proposed
```

`or:`

```javascript
POST [base]/CodeSystem/$subsumes
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
     },
     {
     	"name": "codeA",
     	"valueCode": "cancelled"
     },
     {
     	"name": "codeB",
     	"valueCode": "proposed"
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
            "name": "outcome",
            "value": {
                "code": "not-subsumed"
            }
        }
    ]
}
```
{% endtab %}
{% endtabs %}

#### With Coding

You will receive same subsumption results

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem/$subsumes
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
     },
     {
     	"name": "codingA",
     	"valueCoding": {
     		"code": "accepted"
     	}
     },
     {
     	"name": "codingB",
     	"valueCoding": {
     		"code": "achieved"
     	}
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
            "name": "outcome",
            "value": {
                "code": "subsumed"
            }
        }
    ]
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
You can also combine code and Coding
{% endhint %}

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem/$subsumes
{ 
  "resourceType" : "Parameters",
  "parameter" : [
     {
      "name" : "system",
      "valueUri" : "http://hl7.org/fhir/goal-status"
     },
     {
     	"name": "codingA",
     	"valueCoding": {
     		"code": "accepted"
     	}
     },
     {
     	"name": "codeB",
     	"valueCode": "achieved"
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
            "name": "outcome",
            "value": {
                "code": "subsumed"
            }
        }
    ]
}
```
{% endtab %}
{% endtabs %}



