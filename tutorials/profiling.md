# Profiling and Validation

## Overview

FHIR resources are very loose in requirements which gives FHIR its flexibility. For example, all elements are optional in the Patient resource, and it's possible to create a Patient resource without any data which does not make much sense. So, sometimes there is a need to constraint resources. In FHIR, you need to create a StructureDefinition resource and describe the requirements for a resource you want to restrict. And it is definitely not an easy task. There are special tools developed specifically for this. And there is an alternative — custom profiling in [Aidbox](https://www.health-samurai.io/aidbox) which is in fact a well-known JSON Schema.

{% tabs %}
{% tab title="Request" %}
```javascript
POST /Patient
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 201

id: 6c2b9ea9-ea57-4f9a-9c9a-6c46cbad43da
resourceType: Patient
meta:
  ...
```
{% endtab %}
{% endtabs %}

For custom profiling, Aidbox provides additional resource `AidboxProfile`. This resource specifies resource type and [JSON Schema](https://json-schema.org/) which will validate the specified resource type.

## AidboxProfile Resource Structure

### bind 

The `bind` element is of type [Reference](https://www.hl7.org/fhir/references.html). It specifies resource type which the profile will be applied to. 

**Example:** Binding to `Practitioner` resource.

{% tabs %}
{% tab title="YAML" %}
```yaml
bind:
  id: Practitioner # Target resource type "Practitoner"
  resourceType: Entity
```
{% endtab %}

{% tab title="JSON" %}
```javascript
{
  "bind": {
    "id": "Practitioner",  
    "resourceType": "Entity"
  }
}
```
{% endtab %}
{% endtabs %}

### schema

It's a plain [JSON Schema ](https://json-schema.org/)object which will validate a resource.

**Example:** Require the `name` attribute

{% tabs %}
{% tab title="YAML" %}
```yaml
schema:
  type: object
  required:
  - name
```
{% endtab %}

{% tab title="JSON" %}
```javascript
{
  "schema": {
    "type": "object",
    "required": ["name"]
  }
}
```
{% endtab %}
{% endtabs %}

## **Examples**

### **Require Properties**

Let's validate newly created `Patient` resources by specifying that `name` and `gender` properties are required. First, we need to create the appropriate `AidboxProfile` resource.

{% tabs %}
{% tab title="Request YAML" %}
```yaml
POST /AidboxProfile

resourceType: AidboxProfile
id: custom-patient-constraint
bind:
  id: Patient
  resourceType: Entity
schema:
  type: object
  required:
  - name
  - gender
```
{% endtab %}

{% tab title="Request JSON" %}
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

{% hint style="warning" %}
If you are using Aidbox.Dev below 0.3.1 version, then after creating an AidboxProfile resource you will need to restart your Aidbox.Dev server.

`$ docker-compose down && docker-compose up -d`
{% endhint %}

Now, let's try to create a Patient resource without `name` and/or `gender` . You will receive the error.

{% tabs %}
{% tab title="Request YAML" %}
```yaml
POST /Patient

resourceType: Patient
birthDate: '1985-01-11'
```
{% endtab %}

{% tab title="Request JSON" %}
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

###  **Require Nested Properties**

Let's require `given` and `family` elements of the `name` property. In this case, we are expecting that `name` attribute of the type [`HumanName`](https://www.hl7.org/fhir/datatypes.html#HumanName) will contain elements `given` and `family`. Let's create the `AidboxProfile` resource with the code below. Then you will need to restart server if you're on Aidbox.Dev. 

{% tabs %}
{% tab title="Request YAML" %}
```yaml
POST /AidboxProfile

resourceType: AidboxProfile
id: custom-patient-constraint
bind:
  id: Patient
  resourceType: Entity
schema:
  type: object
  required:
  - name
  properties:
    name:
      type: array
      minItems: 1
      items:
        type: object
        required:
        - given
        - family
        properties:
          given:
            type: array
            minItems: 1
            items:
              type: string
          family:
            type: string
```
{% endtab %}

{% tab title="Request JSON" %}
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

Now, on Patient resource creation we will be receiving the validation error. Let's try to create a Patient resource without a `family` name. You will receive the error.

{% tabs %}
{% tab title="Request YAML" %}
```yaml
POST /Patient

name:
- text: John Malcovich
  given:
  - John
```
{% endtab %}

{% tab title="Request JSON" %}
```javascript
POST [base]/Patient

{
  "name": [
    {
      "text": "John Malcovich",
      "given": [
        "John"
      ]
    }
  ]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 422

{
    "resourceType": "OperationOutcome",
    "errors": [
        {
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



