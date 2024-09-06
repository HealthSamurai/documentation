# CodeSystem

## Overview

The [`CodeSystem`](https://www.hl7.org/fhir/codesystem.html) resource specifies a set of Concepts included in this code system.

[Aidbox](https://www.health-samurai.io/aidbox) assumes a separate creation of the `CodeSystem` resource and a set of `Concepts` composing it. This means that the CodeSystem resource describes only the meta information of the code system: URL, name, publisher, etc. Whereas Concept resources describe the content of the code system and are associated with the code system by the Concept.system attribute with the same value as the CodeSystem.url element.

For FHIR conformance, we allow to create the CodeSystem resource with a list of included concepts. If it contains listed Concepts at the moment of saving a CodeSystem, then Aidbox saves submitted Concepts as separate resources. The CodeSystem resource itself is saved without the concept attribute. This method of the CodeSystem creation may be used for small dictionaries (generally, not more than 100 concepts). In the case when your code system is big, Aidbox strongly recommends creating the CodeSystem resource separately and uploading Concepts in parts.

## CRUD

### Create

[`CodeSystem`](https://www.hl7.org/fhir/codesystem.html) resource can be created as a FHIR resource with embedded concepts. This approach is applicable for those cases when your code system contains a small number of concepts, usually no more than 100.

For example, we will create a `CodeSystem` for eye color containing `Brown`, `Blue`, `Green`, `Hazel`, `Heterochromia` coded concepts.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem

{
	"resourceType": "CodeSystem",
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

As you can see, the request returns only `CodeSystem` meta information (url, status, content....), and does not return `concept` listed in that request. It is because `Aidbox` divides `CodeSystem` to `CodeSystem` body and contained concepts list, and creates all concepts as independent `Concept` resources.

And if we get `Concept` with `system` = `http://code.system/eyes.color`, we will receive all concepts for this CodeSystem.

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

Another way to create `CodeSystem` is a separate creation of the CodeSystem body and Concepts.

For example, let's create a CodeSystem with a custom subset of Units of measurement (aka [UCUM](http://unitsofmeasure.org/trac)). We will compose this CodeSystem from the most common healthcare units of measure.

| Code      | Descriptive Name                     | Display |
| --------- | ------------------------------------ | ------- |
| %         | Persent                              | %       |
| /uL       | PerMicroLiter                        | /uL     |
| \[iU]/L   | InternationalUnitsPerLiter           | IU/L    |
| kPa       | KiloPascal                           | kPa     |
| ng/mL     | NanoGramsPerMilliLiter               | ng/mL   |
| U/L       | UnitsPerLiter                        | U/L     |
| u\[iU]/mL | MicroInternationalUnitsPerMilliLiter | uIU/mL  |
| ug/dL     | MicroGramsPerDeciLiter               | ug/dL   |
| ug/L      | MicroGramsPerLiter                   | ug/L    |
| ug/mL     | MicroGramsPerMilliLiter              | ug/mL   |
| umol/L    | MicroMolesPerLiter                   | umol/L  |

For this subset, let's create the `CodeSystem` resource.

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem

{
    "resourceType": "CodeSystem",
    "id": "custom-ucum-subset",
    "url": "http://code.system/ucum.subset",
    "status": "active",
    "content": "complete"
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201

{
    "id": "custom-ucum-subset",
    "resourceType": "CodeSystem"
    "url": "http://code.system/ucum.subset",
    "status": "active",
    "content": "complete"
}
```
{% endtab %}
{% endtabs %}

And then, create all listed concepts. We can create a set of concepts via multiple `POST` requests or via one [`Transaction`](https://www.hl7.org/fhir/http.html#transaction) request.

{% tabs %}
{% tab title="Multiple requests" %}
```javascript
POST [base]/Concept

{
 "resourceType": "Concept",
 "system": "http://code.system/ucum.subset",
 "code": "%",
 "display": "%",
 "definition": "Percent"
}

POST [base]/Concept

{
 "resourceType": "Concept",
 "system": "http://code.system/ucum.subset",
 "code": "/uL",
 "display": "/uL",
 "definition": "PerMicroLiter"
}

POST [base]/Concept

{
 "resourceType": "Concept",
 "system": "http://code.system/ucum.subset",
 "code": "[iU]/L",
 "display": "IU/L",
 "definition": "InternationalUnitsPerLiter"
}

......

POST [base]/Concept

{
 "resourceType": "Concept",
 "system": "http://code.system/ucum.subset",
 "code": "umol/L",
 "display": "umol/L",
 "definition": "MicroMolesPerLiter"
}
```
{% endtab %}

{% tab title="Transaction request" %}
```javascript
POST [base]

{
  "type": "transaction",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "/Concept"
      },
      "resource": {
        "resourceType": "Concept",
        "system": "http://code.system/ucum.subset",
        "code": "%",
        "display": "%",
        "definition": "Persent"
      }
    },
    {
      "request": {
        "method": "POST",
        "url": "/Concept"
      },
      "resource": {
        "resourceType": "Concept",
        "system": "http://code.system/ucum.subset",
        "code": "/uL",
        "display": "/uL",
        "definition": "PerMicroLiter"
      }
    },
    {
      "request": {
        "method": "POST",
        "url": "/Concept"
      },
      "resource": {
        "resourceType": "Concept",
        "system": "http://code.system/ucum.subset",
        "code": "umol/L",
        "display": "umol/L",
        "definition": "MicroMolesPerLiter"
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

### Read

The `read` operation works the same as the FHIR `read` interaction.

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem/custom-eye-color
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

### Update

{% hint style="warning" %}
Aidbox does not support updates for the CodeSystem resource.
{% endhint %}

In the terminology, it is a good practice not to update existing Coded Systems. Aidbox follows this principle. If you need to update any existing CodeSystem resource, you will have to explicitly delete this CodeSystem and re-create it again with your changes.

### Delete

On delete `CodeSystem`, Aidbox deletes `CodeSystem` resource, and marks each `Concept` with the `deprecated` = `true`.

{% tabs %}
{% tab title="Request" %}
```javascript
DELETE [base]/CodeSystem/custom-eye-color
```
{% endtab %}

{% tab title="Response" %}
```javascript
STATUS: 201

{
    "url": "http://code.system/eyes.color",
    "status": "draft",
    "content": "example",
    "id": "custom-eye-color",
    "resourceType": "CodeSystem"
}
```
{% endtab %}
{% endtabs %}

Let's check all the concepts of the system.

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/Concept/?system=http://code.system/eyes.color
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
                "deprecated": true,
                "code": "ec-bn",
                "system": "http://code.system/eyes.color",
                "display": "Brown",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-be",
                "deprecated": true,
                "code": "ec-be",
                "system": "http://code.system/eyes.color",
                "display": "Blue",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-gn",
                "deprecated": true,
                "code": "ec-gn",
                "system": "http://code.system/eyes.color",
                "display": "Green",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-hl",
                "deprecated": true,
                "code": "ec-hl",
                "system": "http://code.system/eyes.color",
                "display": "Hazel",
                "resourceType": "Concept"
            }
        },
        {
            "resource": {
                "id": "custom-eye-color-ec-h",
                "deprecated": true,
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

## Operations

Aidbox supports all standard [FHIR terminology](https://www.hl7.org/fhir/terminology-module.html) CodeSystem operations.

| FHIR specification                                                        | Status      | Documentation and samples                                     |
| ------------------------------------------------------------------------- | ----------- | ------------------------------------------------------------- |
| [$lookup](https://www.hl7.org/fhir/codesystem-operations.html#lookup)     | `supported` | [CodeSystem Concept Lookup](concept-lookup.md)                |
| [$subsumes](https://www.hl7.org/fhir/codesystem-operations.html#subsumes) | `supported` | [CodeSystem Subsumption testing](subsumption-testing.md)      |
| [$compose](https://www.hl7.org/fhir/codesystem-operations.html#compose)   | `supported` | [CodeSystem Code composition](codesystem-code-composition.md) |
