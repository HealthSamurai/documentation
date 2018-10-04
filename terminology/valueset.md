# ValueSet

## Overview

[https://www.hl7.org/fhir/valueset.html](https://www.hl7.org/fhir/valueset.html)

CRUD is fully supported

Create/update ValueSet

{% tabs %}
{% tab title="Request" %}
```javascript
PUT [base]/ValueSet/administrative-gender2
{
  "description": "The gender of a person used for administrative purposes. 2",
  "compose": {
    "include": [{
        "system": "http://hl7.org/fhir/administrative-gender"
      }
    ],
    "exclude": [{
        "system": "http://hl7.org/fhir/administrative-gender",
        "concept":[{
        "code":"other"},
        {
        "code":"unknown"}]
      }
    ]
  },
  "date": "2018-04-03T12:05:46+10:00",
  "meta": {
    "lastUpdated": "2018-10-03T13:19:57.503Z",
    "versionId": "0",
    "tag": [{
        "system": "https://aidbox.io",
        "code": "created"
      }
    ]
  },
  "publisher": "HL7 (FHIR Project)",
  "name": "AdministrativeGender2",
  "experimental": true,
  "resourceType": "ValueSet",
  
  "status": "draft",
  "id": "administrative-gender2",
  "url": "http://hl7.org/fhir/ValueSet/administrative-gender2",
  "identifier": [{
      "value": "urn:oid:2.16.840.1.113883.4.642.3.1",
      "system": "urn:ietf:rfc:3986"
    }
  ],
  "immutable": true,
  "version": "3.3.2",
  "contact": [{
      "telecom": [{
          "value": "http://hl7.org/fhir",
          "system": "url"
        }, {
          "value": "fhir@lists.hl7.org",
          "system": "email"
        }
      ]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```text
{
    "description": "The gender of a person used for administrative purposes. 2",
    "compose": {
        "exclude": [
            {
                "system": "http://hl7.org/fhir/administrative-gender",
                "concept": [
                    {
                        "code": "other"
                    },
                    {
                        "code": "unknown"
                    }
                ]
            }
        ],
        "include": [
            {
                "system": "http://hl7.org/fhir/administrative-gender"
            }
        ]
    },
    "date": "2018-04-03T12:05:46+10:00",
    "meta": {
        "lastUpdated": "2018-10-04T14:23:23.548Z",
        "versionId": "15",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "created"
            }
        ]
    },
    "publisher": "HL7 (FHIR Project)",
    "name": "AdministrativeGender2",
    "experimental": true,
    "resourceType": "ValueSet",
    "status": "draft",
    "id": "administrative-gender2",
    "url": "http://hl7.org/fhir/ValueSet/administrative-gender2",
    "identifier": [
        {
            "value": "urn:oid:2.16.840.1.113883.4.642.3.1",
            "system": "urn:ietf:rfc:3986"
        }
    ],
    "immutable": true,
    "version": "3.3.2",
    "contact": [
        {
            "telecom": [
                {
                    "value": "http://hl7.org/fhir",
                    "system": "url"
                },
                {
                    "value": "fhir@lists.hl7.org",
                    "system": "email"
                }
            ]
        }
    ]
}
```
{% endtab %}
{% endtabs %}

Delete ValueSet

{% tabs %}
{% tab title="Request" %}
```javascript
DELETE [base]/ValueSet/administrative-gender2
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

ValueSet compose parameters

Create a ValueSet using the `compose.include.concept` element

{% tabs %}
{% tab title="Request" %}
```javascript
PUT [base]/ValueSet/sample-valueset-xxx
{
    "description": "This is a sample ValueSet resource. Common UCUM units.",
    "compose": {
        "include": [
            {
                "system": "http://unitsofmeasure.org",
                "concept": [
                    {
                        "code": "kg",
                        "display": "kilogram"
                    },
                    {
                        "code": "m",
                        "display": "meter"
                    }
                ]
            }
        ],
        "inactive": false,
        "lockedDate": "2018-10-04"
    },
    "date": "2018-10-04 15:53",
    "meta": {
        "lastUpdated": "2018-10-04T14:33:27.689Z",
        "versionId": "21",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "created"
            }
        ]
    },
    "publisher": "HS",
    "extensible": true,
    "purpose": "Why this value set is defined",
    "name": "sample-valueset-xxx",
    "copyright": "Use and/or publishing restrictions",
    "experimental": true,
    "resourceType": "ValueSet",
    "title": "Common UCUM units",
    "status": "draft",
    "id": "sample-valueset-xxx",
    "url": "http://hl7.org/fhir/ValueSet/sample-valueset-xxx",
    "immutable": false,
    "version": "2018.10.04",
    "contact": [
        {
            "name": "Hello",
            "telecom": [
                {
                    "use": "work",
                    "rank": 1,
                    "value": "hello@health-samurai.io",
                    "system": "email"
                }
            ]
        }
    ],
    "text": {
        "div": "<div>xxx</div>",
        "status": "generated"
    }
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
  "description": "This is a sample ValueSet resource. Common UCUM units.",
  "compose": {
    "include": [{
        "system": "http://unitsofmeasure.org",
        "concept": [{
            "code": "kg",
            "display": "kilogram"
          }, {
            "code": "m",
            "display": "meter"
          }
        ]
      }
    ],
    "inactive": false,
    "lockedDate": "2018-10-04"
  },
  "date": "2018-10-04 15:53",
  "meta": {
    "lastUpdated": "2018-10-04T14:36:15.477Z",
    "versionId": "22",
    "tag": [{
        "system": "https://aidbox.io",
        "code": "updated"
      }
    ]
  },
  "publisher": "HS",
  "extensible": true,
  "purpose": "Why this value set is defined",
  "name": "sample-valueset-xxx",
  "copyright": "Use and/or publishing restrictions",
  "experimental": true,
  "resourceType": "ValueSet",
  "title": "Common UCUM units",
  "status": "draft",
  "id": "sample-valueset-xxx",
  "url": "http://hl7.org/fhir/ValueSet/sample-valueset-xxx",
  "immutable": false,
  "version": "2018.10.04",
  "contact": [{
      "name": "Hello",
      "telecom": [{
          "use": "work",
          "rank": 1,
          "value": "hello@health-samurai.io",
          "system": "email"
        }
      ]
    }
  ],
  "text": {
    "div": "<div>xxx</div>",
    "status": "generated"
  }
}

```
{% endtab %}
{% endtabs %}

  
Create a ValueSet using the `compose.include.filter` element

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



