# Profiling

## Overview

For custom profiling, Aidbox provides additional resource `AidboxProfile`. This resource specifies resource type and [JSON Schema](https://json-schema.org/) which will validate the specified resource type.

## AidboxProfile resource structure

### bind 

Element is of type [Reference](https://www.hl7.org/fhir/references.html). Specifies resource type for which the profile will be applied. 

**Example:** Binding to a `Practitioner` resource.

```javascript
{
  "bind": {
    "id": "Practitioner",    // Target resource type "Practitoner"
    "resourceType": "Entity"
  }
}
```

### schema

Plain [JSON Schema ](https://json-schema.org/)object which will validate a resource.

**Example:** Require name attribute

```javascript
{
  "schema": {
    "type": "object",
    "required": ["name"]
  }
}
```

## **Examples**

### **Require properties**

Let's validate newly created `Patient` resources by specifying that `name` and `gender` properties are required. First, we need to create the appropriate `AidboxProfile` resource.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/AidboxProfile

{
	"resourceType": "AidboxProfile",
	"id": "custom-patient-constraint",
	"bind": {
		"id": "Patient",
		"resourceType": "Entity"
	},
	"schema": {
		"type": "object",
		"required": [
			"name",
			"gender"
		]
	}
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201
{
  "bind": {
    "id": "Patient",
    "resourceType": "Entity"
  },
  "schema": {
    "required": [
      "name",
      "gender"
    ]
  },
  "id": "custom-patient-constraint",
  "resourceType": "AidboxProfile",
  "meta": {
    "lastUpdated": "2018-10-10T14:45:43.801Z",
    "versionId": "2",
    "tag": [{
        "system": "https://aidbox.io",
        "code": "created"
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
If you are using Aidbox.Dev below 0.3.1 version - after creating an AidboxProfile resource you need to restart your Aidbox.Dev server.

`$ docker-compose down && docker-compose up -d`
{% endhint %}

Now, let's try to create a Patient resource without `name` and/or `gender` . You will receive the error.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/Patient

{
 "resourceType": "Patient",
 "birthDate": "1985-01-11"
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 422 

{
  "resourceType": "OperationOutcome",
  "errors": [{
      "path": [],
      "message": "Property name is required",
      "profile": {
        "id": "custom-patient-constraint",
        "resourceType": "AidboxProfile"
      }
    }, {
      "path": [],
      "message": "Property gender is required",
      "profile": {
        "id": "custom-patient-constraint",
        "resourceType": "AidboxProfile"
      }
    }
  ],
  "warnings": []
}
```
{% endtab %}
{% endtabs %}

###  **Require given and family in name**

In this case, we are expecting that `name` attribute of the type [`HumanName`](https://www.hl7.org/fhir/datatypes.html#HumanName) will contain elements `given` and `family`. Let's create the `AidboxProfile` resource with the code below. Then you will need to restart server if you're on Aidbox.Dev. Now, on Patient resource creation we will be receiving the validation error.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/AidboxProfile

{
	"resourceType": "AidboxProfile",
	"id": "custom-patient-constraint",
	"bind": {
		"id": "Patient",
		"resourceType": "Entity"
	},
	"schema": {
		"type": "object",
		"required": [
			"name"
		],
		"properties": {
			"name": {
				"type": "array",
				"minItems": 1,
				"items": {
					"type": "object",
					"required": [
						"given",
						"family"
					],
					"properties": {
						"given": {
							"type": "array",
							"minItems": 1,
							"items": {
								"type": "string"
							}
						},
						"family": {
							"type": "string"
						}
					}
				}
			}
		}
	}
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201

{
  "bind": {
    "id": "Patient",
    "resourceType": "Entity"
  },
  "schema": {
    "type": "object",
    "required": [
      "name"
    ],
    "properties": {
      "name": {
        "type": "array",
        "items": {
          "type": "object",
          "required": [
            "given",
            "family"
          ],
          "properties": {
            "given": {
              "type": "array",
              "items": {
                "type": "string"
              },
              "minItems": 1
            },
            "family": {
              "type": "string"
            }
          }
        },
        "minItems": 1
      }
    }
  },
  "id": "custom-patient-constraint",
  "resourceType": "AidboxProfile",
  "meta": {
    "lastUpdated": "2018-10-11T09:47:18.147Z",
    "versionId": "12",
    "tag": [{
        "system": "https://aidbox.io",
        "code": "created"
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

Now, let's try to create a Patient resource without `name` and/or `gender` . You will receive the error.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/Patient

{
 "name": [{"text": "John Malcovich"}]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 422

{
  "resourceType": "OperationOutcome",
  "errors": [{
      "path": [
        "name",
        0
      ],
      "message": "Property given is required",
      "profile": {
        "id": "custom-patient-constraint",
        "resourceType": "AidboxProfile"
      }
    }, {
      "path": [
        "name",
        0
      ],
      "message": "Property family is required",
      "profile": {
        "id": "custom-patient-constraint",
        "resourceType": "AidboxProfile"
      }
    }
  ],
  "warnings": []
}
```
{% endtab %}
{% endtabs %}



