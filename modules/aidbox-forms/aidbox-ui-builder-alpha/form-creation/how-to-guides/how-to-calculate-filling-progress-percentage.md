# How to: calculate percentage of filled out answers.

## Problem

Sometimes, especially for long forms, it's useful to have some sort of filling progress indicator. It can be used by end user to understand how much is left, and also it usefull to track progress by system owner and make some analysys.

## Solution 

We can define a Questionnaire.item which smartly calculates progress, and left it in QuestionnaireResponse.

It's not easy and straightforward way to do this - Questionnaire can have a lot of dynamicity and several groups of the questions can be disabled or enabled by some circumstances. 

In this regard our solution has a drawback - it's computes only enabled items and in highly dynamic forms you progress can go back - because one of answers triggers new section of answers to be enabled.


### Warn

This solution will work correctly with Aidbox version v2503 or later (available from March 25, 2025, if you're using the Edge channel)


## Calculate progress of filling form

We need to create an item with `type = integer` and set `readOnly = true`.

This item should be placed at the end of the Questionnaire.item array and must include a calculatedExpression with the following value:


```fhirpath
(%resource.repeat(item)
.where(linkId in 
%questionnaire.repeat(item).where(
type != 'group' 
and type != 'display' 
and extension.where(url = 'http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression').exists().not()
and extension.where(url = 'http://hl7.org/fhir/StructureDefinition/questionnaire-hidden' and value = true).exists().not()
and (readOnly.exists().not() or readOnly = false)
).linkId).answer.count() 
/ 
%resource.repeat(item).where(linkId in 
%questionnaire.repeat(item).where(
type != 'group' 
and type != 'display' 
and extension.where(url = 'http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression').exists().not()
and extension.where(url = 'http://hl7.org/fhir/StructureDefinition/questionnaire-hidden' and value = true).exists().not()
and (readOnly.exists().not() or readOnly = false)
).linkId).count() 
* 100).round()
```

This expression identifies all items that should be answered and that have been already answered.

It also takes into consideration items that should not be taken into account:

- hidden items
- disabled items
- computed items
- readonly items
- group items
- display items


## Final Questionnaire with example items.

> In this example we replace typical numerical widget with slider to make it more visual, but feel free to revert back

```json
{
  "resourceType": "Questionnaire",
  "title": "Form with progress",
  "status": "draft",
  "url": "http://forms.aidbox.io/questionnaire/form-with-progress",
  "item": [
    {
      "type": "integer",
      "text": "Q1",
      "linkId": "q1"
    },
    {
      "text": "Q2",
      "linkId": "qwBWalBR",
      "type": "string"
    },
    {
      "text": "Q3",
      "linkId": "q3",
      "type": "decimal"
    },
    {
      "text": "Progress",
      "linkId": "progress",
      "repeats": false,
      "type": "integer",
      "readOnly": true,
      "extension": [
        {
          "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl",
          "valueCodeableConcept": {
            "coding": [
              {
                "code": "slider"
              }
            ]
          }
        },
        {
          "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression",
          "valueExpression": {
            "expression": "(%resource.repeat(item)\n.where(linkId in \n%questionnaire.repeat(item).where(\ntype != 'group' \nand type != 'display' \nand extension.where(url = 'http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression').exists().not()\nand extension.where(url = 'http://hl7.org/fhir/StructureDefinition/questionnaire-hidden' and value = true).exists().not()\nand (readOnly.exists().not() or readOnly = false)\n).linkId).answer.count() \n/ \n%resource.repeat(item).where(linkId in \n%questionnaire.repeat(item).where(\ntype != 'group' \nand type != 'display' \nand extension.where(url = 'http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression').exists().not()\nand extension.where(url = 'http://hl7.org/fhir/StructureDefinition/questionnaire-hidden' and value = true).exists().not()\nand (readOnly.exists().not() or readOnly = false)\n).linkId).count() \n* 100).round()",
            "language": "text/fhirpath"
          }
        }
      ]
    }
  ]
}
```
