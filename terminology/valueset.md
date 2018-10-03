# ValueSet

## Overview

[https://www.hl7.org/fhir/valueset.html](https://www.hl7.org/fhir/valueset.html)

CRUD is fully supported

ValueSet compose parameters

concept  - sample

filter - sample

.....

### Samples

We will show examples of compose by  expanding value set

#### concept

A concept defined in the system

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
        "url": "http://custom/gender",
        "compose": {
           "include": [
              {
                "concept": [{"code": "male"}]
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
```text
RESPONSE
```
{% endtab %}
{% endtabs %}



