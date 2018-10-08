# ValueSet

## Overview

The ValueSet resource documentation can be found here: [https://www.hl7.org/fhir/valueset.html](https://www.hl7.org/fhir/valueset.html).

CRUD is fully supported for the ValueSet resource.

{% tabs %}
{% tab title="Create/Update" %}
```javascript
PUT {{base}}/ValueSet/administrative-gender2
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
```javascript
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

{% tab title="Read" %}
```javascript
GET {{base}}/ValueSet/administrative-gender2
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "description": "The gender of a person used for administrative purposes. 2",
    "compose": {
        "include": [
            {
                "filter": [
                    {
                        "op": "=",
                        "value": "female",
                        "property": "code"
                    }
                ],
                "system": "http://hl7.org/fhir/administrative-gender"
            }
        ]
    },
    "date": "2018-04-03T12:05:46+10:00",
    "meta": {
        "lastUpdated": "2018-10-05T08:09:30.427Z",
        "versionId": "24",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "updated"
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

{% tab title="Delete" %}
```javascript
DELETE {{base}}/ValueSet/administrative-gender2
```
{% endtab %}
{% endtabs %}

## ValueSet Compose Parameters

We will show examples of using the compose element by expanding different value sets.

### include.concept

Create a ValueSet using the `include.concept` element.

{% tabs %}
{% tab title="Create/Update" %}
```javascript
PUT {{base}}/ValueSet/sample-valueset-include-concept
{
  "resourceType": "ValueSet",
  "id": "sample-valueset-include-concept",
  "status": "draft",
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
    ]
  }
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "status": "draft",
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
        ]
    },
    "id": "sample-valueset-include-concept",
    "resourceType": "ValueSet",
    "meta": {
        "lastUpdated": "2018-10-05T08:56:57.235Z",
        "versionId": "26",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "created"
            }
        ]
    }
}
```
{% endtab %}

{% tab title="Expand" %}
```javascript
GET {{base}}/ValueSet/sample-valueset-include-concept/$expand
```
{% endtab %}

{% tab title="Expansion" %}
```javascript
{
  "expansion": {
        "timestamp": "2018-10-05T09:07:06Z",
        "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-include-concept",
        "contains": [
            {
                "code": "kg",
                "system": "http://unitsofmeasure.org",
                "display": "kilogram"
            },
            {
                "code": "m",
                "system": "http://unitsofmeasure.org",
                "display": "meter"
            }
        ]
    },
 ...
}
```
{% endtab %}
{% endtabs %}

### include.filter

Create a ValueSet using the `include.filter` element.

Include all concepts from the [http://hl7.org/fhir/contact-point-system](http://hl7.org/fhir/contact-point-system) system where `display = "SMS"`.

{% tabs %}
{% tab title="Create/Update" %}
```javascript
PUT {{base}}/ValueSet/sample-valueset-include-filter
{
  "resourceType": "ValueSet",
  "id": "sample-valueset-include-filter",
  "url":"http://hl7.org/fhir/ValueSet/sample-valueset-include-filter",
  "status": "draft",
  "compose": {
    "include": [{
        "system": "http://hl7.org/fhir/contact-point-system",
        "filter": [{
            "property": "display",
            "op": "=",
            "value": "SMS"
          }
        ]
      }
    ]
  }
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "url": "http://hl7.org/fhir/ValueSet/sample-valueset-include-filter",
    "status": "draft",
    "compose": {
        "include": [
            {
                "filter": [
                    {
                        "op": "=",
                        "value": "SMS",
                        "property": "display"
                    }
                ],
                "system": "http://hl7.org/fhir/contact-point-system"
            }
        ]
    },
    "id": "sample-valueset-include-filter",
    "resourceType": "ValueSet",
    "meta": {
        "lastUpdated": "2018-10-05T11:55:44.303Z",
        "versionId": "42",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "updated"
            }
        ]
    }
}
```
{% endtab %}

{% tab title="Expand" %}
```javascript
GET {{base}}/ValueSet/sample-valueset-include-filter/$expand
```
{% endtab %}

{% tab title="Expansion" %}
```javascript
{
  "expansion": {
        "timestamp": "2018-10-05T11:56:22Z",
        "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-include-filter",
        "contains": [
            {
                "code": "sms",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "SMS",
                "definition": "A contact that can be used for sending an sms message (e.g. mobile phones, some landlines)"
            }
        ]
    },
 ...
}
```
{% endtab %}
{% endtabs %}

### exclude.concept

Create a ValueSet using the `exclude.concept` element.

Include all concepts from the [http://hl7.org/fhir/contact-point-system](http://hl7.org/fhir/contact-point-system) system and exclude concepts pager, url, and other. Now, the value set should include only values phone, fax, email, and sms.

{% tabs %}
{% tab title="Create/Update" %}
```javascript
PUT {{base}}/ValueSet/sample-valueset-exclude-concept
{
  "resourceType": "ValueSet",
  "id": "sample-valueset-exclude-concept",
  "url":"http://hl7.org/fhir/ValueSet/sample-valueset-exclude-concept",
  "status": "draft",
  "compose": {
    "include": [{
        "system": "http://hl7.org/fhir/contact-point-system"        
      }
    ],
    "exclude": [{
    	"concept": [{
    		"code": "pager"
    	},
    	{
    		"code": "other"
    	},
    	{
    		"code": "url"
    	}],
    	"system": "http://hl7.org/fhir/contact-point-system"
    }]
  }
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "url": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-concept",
    "status": "draft",
    "compose": {
        "exclude": [
            {
                "system": "http://hl7.org/fhir/contact-point-system",
                "concept": [
                    {
                        "code": "pager"
                    },
                    {
                        "code": "other"
                    },
                    {
                        "code": "url"
                    }
                ]
            }
        ],
        "include": [
            {
                "system": "http://hl7.org/fhir/contact-point-system"
            }
        ]
    },
    "id": "sample-valueset-exclude-concept",
    "resourceType": "ValueSet",
    "meta": {
        "lastUpdated": "2018-10-05T12:12:09.387Z",
        "versionId": "46",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "updated"
            }
        ]
    }
}
```
{% endtab %}

{% tab title="Expand" %}
```javascript
GET {{base}}/ValueSet/sample-valueset-exclude-concept/$expand
```
{% endtab %}

{% tab title="Expansion" %}
```javascript
{
  "expansion": {
        "timestamp": "2018-10-05T12:13:23Z",
        "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-concept",
        "contains": [
            {
                "code": "phone",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Phone",
                "definition": "The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
            },
            {
                "code": "fax",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Fax",
                "definition": "The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
            },
            {
                "code": "email",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Email",
                "definition": "The value is an email address."
            },
            {
                "code": "sms",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "SMS",
                "definition": "A contact that can be used for sending an sms message (e.g. mobile phones, some landlines)"
            }
        ]
    },
 ...
}        
```
{% endtab %}
{% endtabs %}

### exclude.filter

Create a ValueSet using the `exclude.filter` element.

Include all concepts from the [http://hl7.org/fhir/contact-point-system](http://hl7.org/fhir/contact-point-system) code system and exclude concepts by the filter: exclude all concepts with length of code = 3 \(sms, fax, url\). So that the value set should include only values: phone, email, pager, and other.

{% tabs %}
{% tab title="Create/Update" %}
```javascript
PUT {{base}}/ValueSet/sample-valueset-exclude-filter
{
  "resourceType": "ValueSet",
  "id": "sample-valueset-exclude-filter",
  "url":"http://hl7.org/fhir/ValueSet/sample-valueset-exclude-filter",
  "status": "draft",
  "compose": {
    "include": [{
        "system": "http://hl7.org/fhir/contact-point-system"
        
      }
    ],
    "exclude": [{
    	"system": "http://hl7.org/fhir/contact-point-system",
    	"filter": [
          {
            "property": "code",
            "op": "regex",
            "value": "\\w{3}"
          }
        ]
    }]
  }
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "url": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-filter",
    "status": "draft",
    "compose": {
        "exclude": [
            {
                "filter": [
                    {
                        "op": "regex",
                        "value": "\\w{3}",
                        "property": "code"
                    }
                ],
                "system": "http://hl7.org/fhir/contact-point-system"
            }
        ],
        "include": [
            {
                "system": "http://hl7.org/fhir/contact-point-system"
            }
        ]
    },
    "id": "sample-valueset-exclude-filter",
    "resourceType": "ValueSet",
    "meta": {
        "lastUpdated": "2018-10-05T14:52:54.974Z",
        "versionId": "74",
        "tag": [
            {
                "system": "https://aidbox.io",
                "code": "updated"
            }
        ]
    }
}
```
{% endtab %}

{% tab title="Expand" %}
```javascript
GET {{base}}/ValueSet/sample-valueset-exclude-filter/$expand
```
{% endtab %}

{% tab title="Expansion" %}
```javascript
{
  "expansion": {
        "timestamp": "2018-10-05T14:53:13Z",
        "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-filter",
        "contains": [
            {
                "code": "phone",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Phone",
                "definition": "The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
            },
            {
                "code": "email",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Email",
                "definition": "The value is an email address."
            },
            {
                "code": "pager",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Pager",
                "definition": "The value is a pager number. These may be local pager numbers that are only usable on a particular pager system."
            },
            {
                "code": "other",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Other",
                "definition": "A contact that is not a phone, fax, page or email address and is not expressible as a URL.  E.g. Internal mail address.  This SHOULD NOT be used for contacts that are expressible as a URL (e.g. Skype, Twitter, Facebook, etc.)  Extensions may be used to distinguish \"other\" contact types."
            }
        ]
    },
 ...
}
```
{% endtab %}

{% tab title="Create & Expand" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "sample-valueset-exclude-filter",
        "url": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-filter",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/contact-point-system"
            }
          ],
          "exclude": [{
              "system": "http://hl7.org/fhir/contact-point-system",
              "filter": [{
                  "property": "code",
                  "op": "=",
                  "value": "\\w{3}"
                }
              ]
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
  "expansion": {
        "timestamp": "2018-10-05T14:59:15Z",
        "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-filter",
        "contains": [
            {
                "code": "phone",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Phone",
                "definition": "The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
            },
            {
                "code": "email",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Email",
                "definition": "The value is an email address."
            },
            {
                "code": "pager",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Pager",
                "definition": "The value is a pager number. These may be local pager numbers that are only usable on a particular pager system."
            },
            {
                "code": "other",
                "module": "fhir-3.3.0",
                "system": "http://hl7.org/fhir/contact-point-system",
                "display": "Other",
                "definition": "A contact that is not a phone, fax, page or email address and is not expressible as a URL.  E.g. Internal mail address.  This SHOULD NOT be used for contacts that are expressible as a URL (e.g. Skype, Twitter, Facebook, etc.)  Extensions may be used to distinguish \"other\" contact types."
            }
        ]
    },
 ...
}
```
{% endtab %}
{% endtabs %}

## Filter Operations

The `filter.op` element defines the kind of operation to perform as a part of the filter criteria. It can take one of the following values:

| Code | Display | Support |
| :--- | :--- | :--- |
| = |  Equals | `Supported` |
|  is-a |  Is A \(by subsumption\) | `Supported` |
|  descendent-of |  Descendent Of \(by subsumption\) | `Supported` |
|  is-not-a |  Not \(Is A\) \(by subsumption\) | `Supported` |
|  regex |  Regular Expression | `Supported` |
|  in |  In Set | `Supported` |
|  not-in |  Not in Set | `Supported` |
|  generalizes |  Generalizes \(by Subsumption\) | `Not supported` |
|  exists |  Exists | `Supported` |

In the examples below, we will use the [goal-status](https://www.hl7.org/fhir/codesystem-goal-status.html) code system which consists of :

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

### Equals

Let's include only concepts with the specified property `code` equals the provided value `cancelled`. The result will include only one concept: `cancelled`.

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-equals",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-equals",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "=",
                  "value": "cancelled"
                }
              ]
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
  "expansion": {
    "timestamp": "2018-10-08T12:26:56Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-equals",
    "contains": [{
        "code": "cancelled",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Cancelled",
        "definition": "The previously accepted goal is no longer being sought"
      }
    ]
  },
  ...
}
```
{% endtab %}
{% endtabs %}

### Is A \(by subsumption\)

Let's include all concepts that have a transitive is-a relationship with the concept code `in-progress` provided as the value including the provided concept itself \(i.e. include self and child codes\). The result will include 5 values: `in-progress`, `on-target`, `ahead-of-target`, `behind-target`, and `sustaining`.

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-is-a",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-is-a",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "is-a",
                  "value": "in-progress"
                }
              ]
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
  "expansion": {
    "timestamp": "2018-10-08T12:56:36Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-is-a",
    "contains": [{
        "code": "in-progress",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "In Progress",
        "hierarchy": [
          "accepted"
        ],
        "definition": "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)"
      }, {
        "code": "on-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "On Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is on schedule for the planned timelines"
      }, {
        "code": "ahead-of-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Ahead of Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is ahead of the planned timelines"
      }, {
        "code": "behind-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Behind Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is behind the planned timelines"
      }, {
        "code": "sustaining",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Sustaining",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal has been met, but ongoing activity is needed to sustain the goal objective"
      }
    ]
  },
  ...
}

```
{% endtab %}
{% endtabs %}

### Descendent Of \(by subsumption\)

Let's include all concepts that have a transitive is-a relationship with the concept code `in-progress` provided as the value excluding the provided concept itself \(i.e. include child codes\). The result will include 4 values:  `on-target`, `ahead-of-target`, `behind-target`, and `sustaining`.

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-descendent-of",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-descendent-of",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "descendent-of",
                  "value": "in-progress"
                }
              ]
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
  "expansion": {
    "timestamp": "2018-10-08T13:02:33Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-descendent-of",
    "contains": [{
        "code": "on-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "On Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is on schedule for the planned timelines"
      }, {
        "code": "ahead-of-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Ahead of Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is ahead of the planned timelines"
      }, {
        "code": "behind-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Behind Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is behind the planned timelines"
      }, {
        "code": "sustaining",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Sustaining",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal has been met, but ongoing activity is needed to sustain the goal objective"
      }
    ]
  },
  ...
}

```
{% endtab %}
{% endtabs %}

### Not \(Is A\) \(by subsumption\)

Let's include all codes where the specified property of the code does not have an is-a relationship with the provided value `accepted`. The result will include 5 values whose parent is not `accepted`: `proposed`, `accepted`, `cancelled`, `rejected`, and `entered-in-error`.

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-is-not-a",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-is-not-a",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "is-not-a",
                  "value": "accepted"
                }
              ]
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
TODO: add response after fix.
{% endtab %}
{% endtabs %}

### Regular Expression

Aidbox supports filter opearion `regex` and implements Postgresql regular expressions.  See the documentation here: [https://www.postgresql.org/docs/9.3/static/functions-matching.html\#FUNCTIONS-SIMILARTO-REGEXP](https://www.postgresql.org/docs/9.3/static/functions-matching.html#FUNCTIONS-SIMILARTO-REGEXP).

Please notice that regular expressions require additional character escaping in JSON.

Let's include codes where the specified property of the code matches the regex specified in the provided value `\\w{8}`. The result will include 4 values which length is 8 symbols: `proposed`, `accepted`, `achieved`, and `rejected`. 

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-equals",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-equals",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "regex",
                  "value": "\\w{8}"
                }
              ]
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
  "expansion": {
    "timestamp": "2018-10-08T13:32:27Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-equals",
    "contains": [{
        "code": "proposed",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Proposed",
        "definition": "A goal is proposed for this patient"
      }, {
        "code": "accepted",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Accepted",
        "definition": "A proposed goal was accepted or acknowledged"
      }, {
        "code": "achieved",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Achieved",
        "hierarchy": [
          "accepted"
        ],
        "definition": "The goal has been met and no further action is needed"
      }, {
        "code": "rejected",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Rejected",
        "definition": "A proposed goal was rejected"
      }
    ]
  },
  ...
}
```
{% endtab %}
{% endtabs %}

### In Set

Let's include concepts where the specified property of the code is in the set of codes specified in the provided value `on-target,ahead-of-target,behind-target` \(comma separated list\). The result will be 3 values: `on-target`, `ahead-of-target`, and `behind-target`.

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-in",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-in",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "in",
                  "value": "on-target,ahead-of-target,behind-target"
                }
              ]
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
  "expansion": {
    "timestamp": "2018-10-08T13:49:22Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-in",
    "contains": [{
        "code": "on-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "On Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is on schedule for the planned timelines"
      }, {
        "code": "ahead-of-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Ahead of Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is ahead of the planned timelines"
      }, {
        "code": "behind-target",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Behind Target",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal is behind the planned timelines"
      }
    ]
  },
  ...
}
```
{% endtab %}
{% endtabs %}

### Not in Set

Let's include concepts where the specified property of the code is not in the set of codes specified in the provided value `accepted,achieved,ahead-of-target,behind-target,cancelled,entered-in-error,in-progress,on-hold,on-target,planned` \(comma separated list\). The result will include 3 values: `proposed`, `rejected`, and `sustaining`.

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-not-in",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-not-in",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "code",
                  "op": "not-in",
                  "value": "accepted,achieved,ahead-of-target,behind-target,cancelled,entered-in-error,in-progress,on-hold,on-target,planned"
                }
              ]
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
  "expansion": {
    "timestamp": "2018-10-08T14:10:21Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-not-in",
    "contains": [{
        "code": "proposed",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Proposed",
        "definition": "A goal is proposed for this patient"
      }, {
        "code": "sustaining",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Sustaining",
        "hierarchy": [
          "accepted",
          "in-progress"
        ],
        "definition": "The goal has been met, but ongoing activity is needed to sustain the goal objective"
      }, {
        "code": "rejected",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Rejected",
        "definition": "A proposed goal was rejected"
      }
    ]
  },
  ...
}

```
{% endtab %}
{% endtabs %}

### Generalizes \(by Subsumption\)

This parameter is not supported in Aidbox.

### Exists

The specified property of the code has at least one value \(if the specified value is true; if the specified value is false, then matches when the specified property of the code has no values\).

{% tabs %}
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "valueset-filter-exists",
        "url": "http://hl7.org/fhir/ValueSet/valueset-filter-exists",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/goal-status",
              "filter": [{
                  "property": "hierarchy",
                  "op": "exists",
                  "value": true
                }
              ]
            }
          ]          
        }
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

