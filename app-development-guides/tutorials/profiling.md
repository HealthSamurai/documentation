# Profiling and Validation

## Overview

FHIR resources are very loose in requirements which gives FHIR its flexibility. For example, all elements are optional in the Patient resource, and it's possible to create a Patient resource without any data which does not make much sense. So, sometimes there is a need to constraint resources. In FHIR, you need to create a StructureDefinition resource and describe the requirements for a resource you want to restrict. And it is definitely not an easy task. There are special tools developed specifically for this. And there is an alternative — custom profiling in [Aidbox](https://www.health-samurai.io/aidbox) which is, in fact, a well-known JSON Schema.

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

For custom profiling, Aidbox provides additional resource `AidboxProfile`. This resource specifies resource type and [JSON Schema](https://json-schema.org/) which validates the specified resource type.

## AidboxProfile Resource Structure

### bind 

The `bind` element is of the type [Reference](https://www.hl7.org/fhir/references.html). It specifies the resource type which the profile will be applied to. 

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

It's a plain [JSON Schema ](https://json-schema.org/)object which validates a resource.

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
If you are using Aidbox.Dev below 0.3.1 version, then after creating an AidboxProfile resource, you will need to restart your Aidbox.Dev server.

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

Now, on the Patient resource creation we will be receiving the validation error. Let's try to create a Patient resource without a `family` name. You will receive the error.

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

## Validation with zen

{% hint style="info" %}
Zen validation is currently available in the EDGE version of Aidbox
{% endhint %}

Aidbox supports an alternative yet very powerful profile validation mechanism powered by  [Zen language](https://github.com/zen-lang/zen).  You can just define a set \(or multiple sets\) of validation profiles in [EDN](https://github.com/edn-format/edn) format and let your Aidbox server know its location.

### Configuration

To enable zen powered validation, you need to specify zen libraries in the `AIDBOX_ZEN_DEPS` environment variable.  
Aidbox validates resources of every resourceType with `aidbox/profile` tagged zen schema.

The value of the `AIDBOX_ZEN_DEPS` variable must be a comma-separated list of @-separated pairs of core-zen-project-ns@zen-project-zip-archive-url.  
For example: `AIDBOX_ZEN_DEPS=foo@https://.../foo.zip,bar@https://.../bar.zip`.

`core-zen-project-ns` should include a zen schema with an `aidbox/profile` tag specified. Schemas tagged with `aidbox/profile` should conform this schema:

```text
{:zen/tags #{zen/tag zen/schema}
 :type     zen/map
 :require  #{:resourceType :severity}
 :keys     {:resourceType       {:type zen/string}
            :profile-definition {:type zen/string}
            :severity           {:type zen/string
                                 :enum [{:value "required"}
                                        {:value "supported"}]}}}
```

| Keyword | Explanation |
| :--- | :--- |
| **:type** | The full list of zen supported types is [here](https://zen-lang.github.io/tags/zen/schema.html) |
| **resourceType** | The profile is applied for resources of this type |
| **severity** | Is related to [FHIR profile usage](http://hl7.org/fhir/profiling.html#profile-uses) |
| **:severity "required"** | The profile is applied to validate all resources of such type |
| **:severity "supported"** | The profile is applied only when referenced in `Resource.meta.profile[]` |
| **profile-definition** | Is the string which should be referenced in the [`Resource.meta.profile[]`](https://www.hl7.org/fhir/resource.html#Meta) for `supported` profiles validation |
| **:validation-type :open** | Optional.  |

### API

| Method | Description |
| :--- | :--- |
| `GET /$zen-ctx` | Returns zen ctx. Useful for debug |
| `GET /$zen-errors` | Returns :errors key of zen ctx |
| `GET /$reload-zen-deps` | Reloads deps specified in `AIDBOX_ZEN_DEPS` variable |

### A basic step by step guide

Let's define your own basic profile for the Devbox.

For an instance, for any Patient in the system, the gender property is allowed to be male, female, other or unknown. All other values are not valid.

* Create a file called zen-test-ns.edn with the following content

```typescript
{ns zen-test-ns
 import #{aidbox}

 patient
 {:zen/tags        #{zen/schema aidbox/profile}
  :type            zen/map
  :resourceType    "Patient"
  :severity        "required"
  :validation-type :open
  :keys            {:gender {:type zen/string
                             :enum [{:value "male"}
                                    {:value "female"}
                                    {:value "other"}
                                    {:value "unknown"}]}}}}
```

* Create a zip archive with that file, with the name zen-test-ns.zip
* Make the file available for downloading by a public URL \(without some authentication   required\). For example, upload it to some cloud storage. 
* Update your .env with `AIDBOX_ZEN_DEPS`
* If you have already running Devbox container, you can just reload Aidbox deps by executing a request `GET /$reload-zen-deps` in the Aidbox Rest Console. Otherwise, just run your Aidbox container as usual.

Let's see how does the defined profile work.

Open the Aidbox Rest Console. Try to create a patient with gender "foo".

{% tabs %}
{% tab title="Request" %}
```javascript
POST /Patient

gender: foo
meta: 
  profile: ["myprofile-definition-url"]
```
{% endtab %}

{% tab title="Response" %}
```javascript
#Status: 422

resourceType: OperationOutcome
text:
  status: generated
  div: Invalid resource
issue:
  - severity: fatal
    code: invalid
    expression:
      - Patient.gender
    diagnostics: "Expected 'foo' in #{\"male\" \"female\" \"unknown\" \"other\"}"
```
{% endtab %}
{% endtabs %}

As you can see, the patient is not created and there is an explanation, that the expected value should be among the defined list of values.

Now try to create a patient with gender "male".

{% tabs %}
{% tab title="Request" %}
```javascript
POST /Patient

gender: male
meta: 
  profile: ["myprofile-definition-url"]
```
{% endtab %}

{% tab title="Response" %}
```javascript
#Status: 201

meta:
  profile:
    - myprofile-definition-url
  lastUpdated: '2021-04-22T09:31:33.483398Z'
  createdAt: '2021-04-22T09:31:33.483398Z'
  versionId: '286'
gender: male
id: bdaa680f-2a07-49dd-9131-0882c753bd16
resourceType: Patient
```
{% endtab %}
{% endtabs %}

Finally, the validation passed and the patient is created.



### 

