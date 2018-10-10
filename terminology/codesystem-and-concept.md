# CodeSystem

## Overview

The [`CodeSystem`](https://www.hl7.org/fhir/codesystem.html) resource specifies a set of Concepts included in this code system. 

Aidbox assumes a separate creation of the `CodeSystem` resource and a set of `Concepts` composing it. This means that the CodeSystem resource describes only meta information of the code system: url, name, publisher, etc. Whereas Concept resources describe the content of the code system and are associated with the code system by the Concept.system attribute with the same value as the CodeSystem.url element.

\[IMAGE\]

For FHIR conformance, we allow to create the CodeSystem resource with a list of included concepts. In the moment of saving a CodeSystem, if it contains listed Concepts, then Aidbox saves submitted Concepts as separate resources, and the CodeSystem resource itself is saved without the concept attribute. This method of the CodeSystem creation may be used for small dictionaries \(generally, not more than 100 concepts\). In case when your code system is big, Aidbox strongly recommends to create the CodeSystem resource separately and upload Concepts in parts.

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

As you can see request return only `CodeSystem` meta information \(url, status, content....\), and do not return `concept` listed in then request. It is was because `Aidbox` divide `CodeSystem` to `CodeSystem` body and contained concepts list, and create all concepts as a independent `Concept` resources.

And if we get `Concept` with `system` = `http://code.system/eyes.color`, we are receive all concepts for this CodeSystem.

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



The another way to create `CodeSystem` is separate creation CodeSystem body and Concepts.

For example, lets create CodeSystem with custom subset of Units of measurement \(aka [UCUM](http://unitsofmeasure.org/trac)\). This CodeSystem we are compose with most common healthcare units.

| Code | Descriptive Name | Display  |
| :--- | :--- | :--- |
| % | Persent | % |
| /uL | PerMicroLiter | /uL |
| \[iU\]/L | InternationalUnitsPerLiter | IU/L |
| 10\*3/uL | ThousandsPerMicroLiter | K/uL, x10^3/mm^3 |
| 10\*6/uL | MillionsPerMicroLiter | M/uL, x10^6/mm^3 |
| fL | FemtoLiter | fL |
| g/dL | GramsPerDeciLiter | g/dL |
| g/L | GramsPerLiter | g/L |
| g/mL | GramsPerMilliLiter | g/mL |
| kPa | KiloPascal | kPa |
| m\[iU\]/mL | MilliInternationalUnitsPerMilliLiter | mIU/mL |
| meq/L | MilliEquivalentsPerLiter | mEq/L |
| mg/dL | MilliGramsPerDeciLiter | mg/dL |
| mm\[Hg\] | MilliMetersOfMercury | mm Hg |
| mmol/kg | MilliMolesPerKiloGram | mmol/kg |
| mmol/L | MilliMolesPerLiter | mmol/L |
| mosm/kg | MilliOsmolesPerKiloGram | mOsm/kg |
| ng/mL | NanoGramsPerMilliLiter | ng/mL |
| nmol/L | NanoMolesPerLiter | nmol/L |
| pg | PicoGrams | pg |
| pg/mL | PicoGramsPerMilliLiter | pg/mL |
| pmol/L | PicoMolesPerLiter | pmol/L |
| U/L | UnitsPerLiter | U/L |
| u\[iU\]/mL | MicroInternationalUnitsPerMilliLiter | uIU/mL |
| ug/dL | MicroGramsPerDeciLiter | ug/dL |
| ug/L | MicroGramsPerLiter | ug/L |
| ug/mL | MicroGramsPerMilliLiter | ug/mL |
| umol/L | MicroMolesPerLiter | umol/L |

For this subset lets create `CodeSystem` resource.

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

And then, create all of listed concepts. We can create a set of concepts via multiple `POST` requests or via one [`Transaction`](https://www.hl7.org/fhir/http.html#transaction) request.

{% tabs %}
{% tab title="Multiple requests" %}
```javascript
POST [base]/Concept

{
 "resourceType": "Concept",
 "system": "http://code.system/ucum.subset",
 "code": "%",
 "display": "%",
 "definition": "Persent"
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
Full transaction bundle in the single file.json !!!!

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

At the moment, only code system meta information will be displayed. In the future,  associated concepts will be gathered by means of Clojure and shown as well.

Just Fhir Read 

Давать ли возможность читать CS с включенным в него списком концептов????

GET \[base\]/CodeSystem/\[id\]

GET \[base\]/Concept?system=CS.url



### Update

Updating `CodeSystem` resource works the same as FHIR update.  But when Aidbox separate `CodeSystem` and concepts list compose the code system, Aidbox firstly update all previously created concepts for current CodeSystem and marks them as `deprecated = true`. After that Aidbox upsert \(update or insert\) all new concepts from request with mark `deprecated = false`. In this way if you delete any concept from `CodeSystem`, this concepts will be marked as `deprecated = true` , that mean that this concept was deleted.

For example, lets update our  `custom-eye-color` . We will remove `Hazel` color and add `Silver` and `Amber` color.

{% tabs %}
{% tab title="Request" %}
```javascript
PUT [base]/CodeSystem/custom-eye-color

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
			"code": "ec-sl",
			"display": "Silver"
		},
		{
			"code": "ec-am",
			"display": "Amber"
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
STATUS: 200

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

And now lets read all `Concept` for `custom-eye-color` `CodeSystem`.

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem?system=http://code.system/eyes.color
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

Show only active concepts.

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem?system=http://code.system/eyes.color&deprecated:not=true
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

Show only deleted concept.

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem?system=http://code.system/eyes.color&deprecated=true
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

### Delete

On delete, will be removed the CodeSystem resource itself and all Concepts where `system = CodeSystem.url`.



