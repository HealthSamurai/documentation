# ValueSet

## Overview

The ValueSet resource official FHIR documentation can be found here: [https://www.hl7.org/fhir/valueset.html](https://www.hl7.org/fhir/valueset.html).

All examples can be executed in the REST Console of your Box in Aidbox.Cloud. Just copy/paste a sample into the REST Console and click the EXECUTE button or press Ctrl+Enter.

Also, you can run all examples in [Postman](https://www.getpostman.com/) having Aibox.Dev running locally, or by setting up a box in Aidbox.Cloud. Download the Aidbox collection and open it in Postman. Setup the proper environment value for the `base` variable which should be the base URL of your FHIR server. Setup authorization if you are using Aidbox.Cloud.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/view-collection/63adc3e748810862fddd?referrer=https%3A%2F%2Fapp.getpostman.com%2Frun-collection%2F63adc3e748810862fddd%23%3Fenv[Aidbox.Dev]%3DW3sia2V5IjoiYmFzZSIsInZhbHVlIjoiaHR0cDovL2xvY2FsaG9zdDo4ODg4IiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfV0%3D&_ga=2.64469999.1592054488.1539615553-1595564802.1538573158)

### CRUD

CRUD is fully supported for the ValueSet resource.

{% tabs %}
{% tab title="Create" %}
```yaml
POST /ValueSet

description: The gender of a person used for administrative purposes. 2
compose:
  include:
  - system: http://hl7.org/fhir/administrative-gender
  exclude:
  - system: http://hl7.org/fhir/administrative-gender
    concept:
    - code: other
    - code: unknown
name: AdministrativeGender2
experimental: true
resourceType: ValueSet
status: draft
id: administrative-gender2
url: http://hl7.org/fhir/ValueSet/administrative-gender2
immutable: true
```
{% endtab %}

{% tab title="Response" %}
```yaml
description: The gender of a person used for administrative purposes. 2
compose:
  exclude:
  - system: http://hl7.org/fhir/administrative-gender
    concept:
    - code: other
    - code: unknown
  include:
  - system: http://hl7.org/fhir/administrative-gender
name: AdministrativeGender2
experimental: true
resourceType: ValueSet
status: draft
id: administrative-gender2
url: http://hl7.org/fhir/ValueSet/administrative-gender2
immutable: true
```
{% endtab %}

{% tab title="Update" %}
```yaml
PUT /ValueSet/administrative-gender2

description: The gender of a person used for administrative purposes. 2
compose:
  include:
  - system: http://hl7.org/fhir/administrative-gender
  exclude:
  - system: http://hl7.org/fhir/administrative-gender
    concept:
    - code: other
    - code: unknown
name: AdministrativeGender2
experimental: true
resourceType: ValueSet
status: draft
id: administrative-gender2
url: http://hl7.org/fhir/ValueSet/administrative-gender2
immutable: true
version: 3.3.2
```
{% endtab %}

{% tab title="Response" %}
```yaml
description: The gender of a person used for administrative purposes. 2
compose:
  exclude:
  - system: http://hl7.org/fhir/administrative-gender
    concept:
    - code: other
    - code: unknown
  include:
  - system: http://hl7.org/fhir/administrative-gender
name: AdministrativeGender2
experimental: true
resourceType: ValueSet
status: draft
id: administrative-gender2
url: http://hl7.org/fhir/ValueSet/administrative-gender2
immutable: true
version: 3.3.2
```
{% endtab %}

{% tab title="Read" %}
```javascript
GET /ValueSet/administrative-gender2
```
{% endtab %}

{% tab title="Response" %}
```yaml
description: The gender of a person used for administrative purposes. 2
compose:
  exclude:
  - system: http://hl7.org/fhir/administrative-gender
    concept:
    - code: other
    - code: unknown
  include:
  - system: http://hl7.org/fhir/administrative-gender
name: AdministrativeGender2
experimental: true
resourceType: ValueSet
status: draft
id: administrative-gender2
url: http://hl7.org/fhir/ValueSet/administrative-gender2
immutable: true
version: 3.3.2
```
{% endtab %}

{% tab title="Delete" %}
```javascript
DELETE /ValueSet/administrative-gender2
```
{% endtab %}
{% endtabs %}

## ValueSet Compose Parameters

We will show examples of using the compose element by expanding different value sets.

### include.concept

Create a ValueSet using the `include.concept` element. The result will include only listed concepts: `kg` and `m`.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /ValueSet/$expand

resourceType: Parameters
parameter:
- name: valueSet
  resource:
    resourceType: ValueSet
    id: sample-valueset-include-concept
    status: draft
    compose:
      include:
      - system: http://unitsofmeasure.org
        concept:
        - code: kg
          display: kilogram
        - code: m
          display: meter
```
{% endtab %}

{% tab title="Response" %}
```yaml
resourceType: ValueSet
id: sample-valueset-include-concept
status: draft
compose:
  include:
  - system: http://unitsofmeasure.org
    concept:
    - code: kg
      display: kilogram
    - code: m
      display: meter
expansion:
  timestamp: '2018-10-25T14:40:09Z'
  identifier: 
  contains:
  - code: kg
    system: http://unitsofmeasure.org
    display: kilogram
  - code: m
    system: http://unitsofmeasure.org
    display: meter
```
{% endtab %}
{% endtabs %}

### include.filter

Create a ValueSet using the `include.filter` element.

Include all concepts from the [http://hl7.org/fhir/contact-point-system](http://hl7.org/fhir/contact-point-system) system where `display = "SMS"`.

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
        "id": "sample-valueset-include-filter",
        "url": "http://hl7.org/fhir/ValueSet/sample-valueset-include-filter",
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
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
  "expansion": {
    "timestamp": "2018-10-05T11:56:22Z",
    "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-include-filter",
    "contains": [{
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
{% tab title="Request" %}
```javascript
POST {{base}}/ValueSet/$expand
{
  "resourceType": "Parameters",
  "parameter": [{
      "name": "valueSet",
      "resource": {
        "resourceType": "ValueSet",
        "id": "sample-valueset-exclude-concept",
        "url": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-concept",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/contact-point-system"
            }
          ],
          "exclude": [{
              "concept": [{
                  "code": "pager"
                }, {
                  "code": "other"
                }, {
                  "code": "url"
                }
              ],
              "system": "http://hl7.org/fhir/contact-point-system"
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
    "timestamp": "2018-10-05T12:13:23Z",
    "identifier": "http://hl7.org/fhir/ValueSet/sample-valueset-exclude-concept",
    "contains": [{
        "code": "phone",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/contact-point-system",
        "display": "Phone",
        "definition": "The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
      }, {
        "code": "fax",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/contact-point-system",
        "display": "Fax",
        "definition": "The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
      }, {
        "code": "email",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/contact-point-system",
        "display": "Email",
        "definition": "The value is an email address."
      }, {
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
{% tab title="Request" %}
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
    "contains": [{
        "code": "phone",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/contact-point-system",
        "display": "Phone",
        "definition": "The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required."
      }, {
        "code": "email",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/contact-point-system",
        "display": "Email",
        "definition": "The value is an email address."
      }, {
        "code": "pager",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/contact-point-system",
        "display": "Pager",
        "definition": "The value is a pager number. These may be local pager numbers that are only usable on a particular pager system."
      }, {
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

### include.valueSet

Selects concepts found in this value set. This is an absolute URI that is a reference to `ValueSet.url`.

`concept` and `filter` don't apply to `valueSet`.

N/B:  `ValueSet.compose.include.valueSet` ****should be an array not a string.

Let's include the `administrative-gender` valueset. The result will be 4 concepts: `male`, `female`, `unknown`, and `other`.

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
        "id": "valueset-from-valueset",
        "url": "http://hl7.org/fhir/ValueSet/valueset-from-valueset",
        "status": "draft",
        "compose": {
          "include": [{
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
  "expansion": {
    "timestamp": "2018-10-09T11:34:26Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-from-valueset",
    "contains": [{
        "code": "male",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/administrative-gender",
        "display": "Male",
        "definition": "Male"
      }, {
        "code": "female",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/administrative-gender",
        "display": "Female",
        "definition": "Female"
      }, {
        "code": "other",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/administrative-gender",
        "display": "Other",
        "definition": "Other"
      }, {
        "code": "unknown",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/administrative-gender",
        "display": "Unknown",
        "definition": "Unknown"
      }
    ]
  },
  ...
}
```
{% endtab %}
{% endtabs %}

### exclude.valueSet

Let's exclude administrative-gender2 which consists of `male` and `female` from administrative-gender code system. The result will be 2 values: `other` and `unknown`.

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
        "id": "valueset-exclude-valueset",
        "url": "http://hl7.org/fhir/ValueSet/valueset-exclude-valueset",
        "status": "draft",
        "compose": {
          "include": [{
              "system": "http://hl7.org/fhir/administrative-gender"
            }
          ],
          "exclude": [{
              "valueSet": ["{{base}}/ValueSet/administrative-gender2"]
            }
          ]
        }
      }
    }
  ]
}
```
{% endtab %}

{% tab title="" %}
```javascript
{
  "expansion": {
    "timestamp": "2018-10-09T13:20:38Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-exclude-valueset",
    "contains": [{
        "code": "other",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/administrative-gender",
        "display": "Other",
        "definition": "Other"
      }, {
        "code": "unknown",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/administrative-gender",
        "display": "Unknown",
        "definition": "Unknown"
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

Let's include all codes where the specified property of the code does not have an is-a relationship with the provided value `accepted`. The result will include 4 values whose parent is not `accepted`: `proposed`,  `cancelled`, `rejected`, and `entered-in-error`.

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
```javascript
 {
  "expansion": {
    "timestamp": "2018-10-10T13:44:22Z",
    "identifier": "http://localhost:8888/ValueSet/valueset-filter-is-not-a",
    "contains": [{
        "code": "proposed",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Proposed",
        "definition": "A goal is proposed for this patient"
      }, {
        "code": "cancelled",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Cancelled",
        "definition": "The previously accepted goal is no longer being sought"
      }, {
        "code": "entered-in-error",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Entered In Error",
        "definition": "The goal was entered in error and voided."
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

Let's display concepts where property `hierarchy` exists. The result will include 8 concepts for which `hierarchy` is not an empty array: `achieved`, `ahead-of-target`, `behind-target`, `in-progress`, `on-hold`, `on-target`, `planned`, and `sustaining`.

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

{% tab title="Response" %}
```javascript
{
  "expansion": {
    "timestamp": "2018-10-08T15:14:18Z",
    "identifier": "http://hl7.org/fhir/ValueSet/valueset-filter-exists",
    "contains": [{
        "code": "planned",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "Planned",
        "hierarchy": [
          "accepted"
        ],
        "definition": "A goal is planned for this patient"
      }, {
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
        "code": "on-hold",
        "module": "fhir-3.3.0",
        "system": "http://hl7.org/fhir/goal-status",
        "display": "On Hold",
        "hierarchy": [
          "accepted"
        ],
        "definition": "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time."
      }
    ]
  },
  ...
}

```
{% endtab %}
{% endtabs %}

## Operations

| FHIR specification | Status | Documentation and samples |
| :--- | :--- | :--- |
| [$expand](https://www.hl7.org/fhir/valueset-operations.html#expand) | `supported` | [ValueSet Expansion](value-set-expansion.md) |
| [$validate-code](https://www.hl7.org/fhir/valueset-operations.html#validate-code) | `supported` | [ValueSet Code validation](value-set-validation.md) |

