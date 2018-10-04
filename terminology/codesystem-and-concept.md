# CodeSystem

## Overview

A [CodeSystem](https://www.hl7.org/fhir/codesystem.html) resource specifies a set of Concepts included in this code system. 

Aidbox подразумевает раздельное создание ресурса CodeSystem и составляющего его набора концептов. Это означает что ресурс CodeSystem описывает только мета информацию кодовой системы: url, name, publisher.... . В то время как сами Concept ресурсы описывают содержимое этой кодовой системы, и связываются с кодовой системой через Concept.system атрибут который равен CodeSystem.url.

При этом, для поддержания совместимости с FHIR, мы разрешаем запись CodeSystem ресурса в включенным в него списком концептов. В момент сохранения CodeSystem, если в нем перечислены Concept, Aidbox сохраняет переданные Concepts в виде отдельных ресурсов, а сам ресурс CodeSystem сохраняется без concept атрибута. Данный способ создания CodeSystem  может использоваться при создании небольших справочников \(как правило, не более 100 концептов \). В случае если ваша кодовая система большая, Aidbox настоятельно рекомендует отдельно создать CodeSystem ресурс и по частям загружать Concept.

## CRUD

### Create

Разбить CodeSystem на его описание и список концептов. Сохранить CodeSystem и сами концепты

#### Создание ресурса со списком концептов

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/CodeSystem
```

`or`

```javascript
PUT [base]/CodeSystem/[id]

{
	"resourceType" : "CodeSystem",
	"status": "draft",
	"url": "code.system/eyes.color",
	"content": "example",
	"concept" : [     
		{
			"code": "ec-bn",
			"display": "brown"
		},
		{
			"code": "ec-be",
			"display": "blue"
		},
		{
			"code": "ec-gn",
			"display": "green"
		},
		{
			"code": "ec-hl",
			"display": "hazel"
		},
		{
			"code": "ec-h",
			"display": "heterochromia"
		}
	]	
}
```
{% endtab %}

{% tab title="Response" %}
```javascript
{
    "url": "code.system/eyes.color",
    "status": "draft",
    "content": "example",
    "id": "724ba412-a422-4181-bddf-b3e9c4e3b0d3",
    "resourceType": "CodeSystem",
    "meta": {
        "lastUpdated": "2018-10-04T16:00:29.240Z",
        "versionId": "12",
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
{% endtabs %}

### Read

Отдавать только мета информацию  **ИЛИ**  в clojure собирать все concepts ????

### Update

Обновляем сам CodeSystem,  помечаем все старые Concept как deprecated = true, вставляем новые concept с статусом deprecated = false

### Delete

Удаление самого CodeSystem и Concepts where system = CodeSystem.url ......



