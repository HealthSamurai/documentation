# CodeSystem

## Overview

The [CodeSystem](https://www.hl7.org/fhir/codesystem.html) resource specifies a set of Concepts included in this code system. 

Aidbox assumes a separate creation of the CodeSystem resource and a set of Concepts composing it. This means that the CodeSystem resource describes only meta information of the code system: url, name, publisher, etc. Whereas Concept resources describe the content of the code system and are associated with the code system by the Concept.system attribute with the same value as the CodeSystem.url element.

For FHIR conformance, we allow to record the CodeSystem resource with a list of included concepts. In the moment of saving a CodeSystem, if it contains listed Concepts, then Aidbox saves submitted Concepts as separate resources, and the CodeSystem resource itself is saved without the concept attribute. This method of the CodeSystem creation may be used for small dictionaries \(generally, not more than 100 concepts\). In case when your code system is big, Aidbox strongly recommends to create the CodeSystem resource separately and upload Concepts in parts.

## CRUD

### Create

[`CodeSystem`](https://www.hl7.org/fhir/codesystem.html) resource can be created as a FHIR resource with embedded concepts itself. This approach is applicable for those cases if your code system contains a small number of concepts, usually no more than 100. 

For example, we will create `CodeSystem` for eye color, contained `Brown`, `Blue`, `Green`, `Hazel`,  `Heterochromia` coded concepts.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem

{
	"resourceType" : "CodeSystem",
	"id": "custom-eye-color",
	"status": "draft",
	"url": "http://code.system/eyes.color",
	"content": "example",
	"concept" : [     
		{
			"code": "ec-bn",
			"display": "Brown"
		},
		{
			"code": "ec-be",
			"display": "Blue"
		},
		{
			"code": "ec-gn",
			"display": "Green"
		},
		{
			"code": "ec-hl",
			"display": "Hazel"
		},
		{
			"code": "ec-h",
			"display": "Heterochromia"
		}
	]	
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201

{
    "resourceType": "CodeSystem",
    "id": "custom-eye-color"
    "url": "http://code.system/eyes.color",
    "status": "draft",
    "content": "example"
}
```
{% endtab %}
{% endtabs %}

As you can see request return only `CodeSystem` meta information \(`url`, `status`, `content`....\), and do not return `concept` listed in then request. It is was because `Aidbox` divide `CodeSystem` to `CodeSystem` body and contained concepts list, and create all concepts as a independent `Concept` resources.

And if we get `Concept` with `system` = `http://code.system/eyes.color`

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem?system=http://code.system/eyes.color
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 200

{
    "resourceType": "Bundle",
    "type": "searchset",
    "entry": [
        {
            "resource": {
                "id": "custom-eye-color-ec-bn",
                "code": "ec-bn",
                "system": "http://code.system/eyes.color",
                "display": "Brown",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-be",
                "code": "ec-be",
                "system": "http://code.system/eyes.color",
                "display": "Blue",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-gn",
                "code": "ec-gn",
                "system": "http://code.system/eyes.color",
                "display": "Green",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-hl",
                "code": "ec-hl",
                "system": "http://code.system/eyes.color",
                "display": "Hazel",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-h",
                "code": "ec-h",
                "system": "http://code.system/eyes.color",
                "display": "Heterochromia",
                "resourceType": "Concept"
            }
        }
    ],
    "total": 5,
    ......
}
```
{% endtab %}
{% endtabs %}

The another way how you can 





#### Separate creation of the CodeSystem resource and association of its Concepts

{% tabs %}
{% tab title="Request" %}
Creating an empty `CodeSystem` resource with `url = http://example/code/system`

```javascript

POST [base]/CodeSystem
{
	"resourceType" : "CodeSystem",
	"status": "draft",
	"url": "http://example/code/system",
	"content": "example"
}
```

And create concepts where `Concept.system` equal `CodeSystem.url`

```javascript
// Creating  Concepts
POST [base]/Concept
{
	"resourceType": "Concept",
	"system": "http://example/code/system",
	"code": "ec-bn",
	"display": "Brown"
}
POST [base]/Concept
{
	"resourceType": "Concept",
	"system": "http://example/code/system",
	"code": "ec-be",
	"display": "Blue"
}
POST [base]/Concept
{
	"resourceType": "Concept",
	"system": "http://example/code/system",
	"code": "ec-gn",
	"display": "Green"
}
POST [base]/Concept
{
	"resourceType": "Concept",
	"system": "http://example/code/system",
	"code": "ec-hl",
	"display": "Hazel"
}
POST [base]/Concept
{
	"resourceType": "Concept",
	"system": "http://example/code/system",
	"code": "ec-h",
	"display": "Heterochromia"
}
```
{% endtab %}
{% endtabs %}

### Create using transaction

### Read

At the moment, only code system meta information will be displayed. In the future,  associated concepts will be gathered by means of Clojure and shown as well.

### Update

The CodeSystem resource itself will be updated, all old Concepts will be marked with `deprecated = true`, and new concepts will be inserted with the status `deprecated = false`.

### Delete

On delete, will be removed the CodeSystem resource itself and all Concepts where `system = CodeSystem.url`.



