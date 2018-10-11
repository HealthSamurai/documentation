# CodeSystem Concept Lookup

> Given a code/system, or a Coding, get additional details about the concept, including definition, status, designations, and properties. One of the products of this operation is a full decomposition of a code from a structure terminology

In Aidbox this is essentially the same as search Concept by system and code

```http
GET [base]/fhir/CodeSystem/$lookup?system=http://loinc.org&code=1963-8
```

equals to

```http
GET [base]/Concept?system=http://loinc.org&code=1963-8
```

The only difference is response format.

With Concept Search you can do even more, than just getting one concept. For example search and filter by any concept elements. 

```text
POST [base]/CodeSystem/$lookup
```

More info in [official doc](https://www.hl7.org/fhir/codesystem-operations.html#lookup).

### Parameters

| Parameter | Type | Status |
| :--- | :--- | :--- |
| code | [code](https://www.hl7.org/fhir/datatypes.html#code) | `supported` |
| system | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` |
| version | [string](https://www.hl7.org/fhir/datatypes.html#string) | `supported` |
| coding | [Coding](https://www.hl7.org/fhir/datatypes.html#Coding) | `supported` |
| date | [dateTime](https://www.hl7.org/fhir/datatypes.html#dateTime) | `not supported` |
| displayLanguage | [code](https://www.hl7.org/fhir/datatypes.html#code) | `not supported` |
| property | [code](https://www.hl7.org/fhir/datatypes.html#code) | `not supported` |

### Examples

Available output parameters: `name`, `display`, `version`, `designation`, `property`

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem/$lookup
{
	"resourceType" : "Parameters",  
	"parameter" : [     
		{      
			"name" : "system",      
			"valueUri" : "http://hl7.org/fhir/goal-status"     
			
		},
		{
			"valueCoding": {     		
				"code": "accepted"
			}
		}
	]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "display",
            "valueString": "Accepted"
        },
        {
            "name": "name",
            "valueString": "GoalStatus"
        }
    ]
}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}
```javascript
GET [base]/CodeSystem/$lookup?system=http://hl7.org/fhir/v2/0003&code=RAR
```

`or:`

```javascript
POST [base]/CodeSystem/$lookup
{
	"resourceType" : "Parameters",  
	"parameter" : [     
		{      
			"name" : "system",      
			"valueUri" : "http://hl7.org/fhir/v2/0003"     
			
		},
		{
			"name": "code",
			"valueCode": "RAR"
		}
	]
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "resourceType": "Parameters",
    "parameter": [
        {
            "name": "display",
            "valueString": "RAR - Pharmacy administration information query response"
        },
        {
            "name": "designation",
            "value": {
                "display": {
                    "de": "Antwort bzgl. der Behandlungs-/Darreichungsform",
                    "nl": "RAR - Medicatietoediening-informatie query antwoord"
                }
            }
        },
        {
            "name": "property",
            "value": {
                "v2-0003": {
                    "deprecationDate": "2000-11"
                }
            }
        },
        {
            "name": "name",
            "valueString": "v2 Event Type"
        }
    ]
}
```
{% endtab %}
{% endtabs %}

