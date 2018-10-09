# CodeSystem

## Overview

The [CodeSystem](https://www.hl7.org/fhir/codesystem.html) resource specifies a set of Concepts included in this code system. 

Aidbox assumes a separate creation of the CodeSystem resource and a set of Concepts composing it. This means that the CodeSystem resource describes only meta information of the code system: url, name, publisher, etc. Whereas Concept resources describe the content of the code system and are associated with the code system by the Concept.system attribute with the same value as the CodeSystem.url element.

For FHIR conformance, we allow to record the CodeSystem resource with a list of included concepts. In the moment of saving a CodeSystem, if it contains listed Concepts, then Aidbox saves submitted Concepts as separate resources, and the CodeSystem resource itself is saved without the concept attribute. This method of the CodeSystem creation may be used for small dictionaries \(generally, not more than 100 concepts\). In case when your code system is big, Aidbox strongly recommends to create the CodeSystem resource separately and upload Concepts in parts.

## CRUD

### Create

Divide the CodeSystem into its description and a list of its concepts. Save the CodeSystem and Concepts.

#### Creation of the CodeSystem resource containing a list of concepts

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
Status: 201
{
    "resourceType": "CodeSystem",
    "id": "custom-eye-color"
    "url": "http://code.system/eyes.color",
    "status": "draft",
    "content": "example"
}
```

And if we get `Concept` with `system` =  `http://code.system/eyes.color`

```javascript
GET [base]/CodeSystem?system=http://code.system/eyes.color&deprecated:not=true
```

Response

```text

```
{% endtab %}
{% endtabs %}

#### 

#### 

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

### Read

At the moment, only code system meta information will be displayed. In the future,  associated concepts will be gathered by means of Clojure and shown as well.

### Update

The CodeSystem resource itself will be updated, all old Concepts will be marked with `deprecated = true`, and new concepts will be inserted with the status `deprecated = false`.

### Delete

On delete, will be removed the CodeSystem resource itself and all Concepts where `system = CodeSystem.url`.



