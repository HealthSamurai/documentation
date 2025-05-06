# How to: calculate form filling percentage

## What is form filling percentage

Form filling percentage is a proportion of a form that has been completed by a user. It is a metric commonly used in UX research, web analytics, and digital form management to evaluate how users interact with forms — whether on websites, applications, or platforms such as surveys, patient intake forms, or insurance claim submissions.

**Why it matters**

Data completeness is essential for downstream use (e.g., analytics, clinical decision support).

User feedback: Form filling percentage gives users a sense of progress and can reduce drop-off rates.

## Example Use Case
A patient intake app displays a FHIR Questionnaire for demographics and medical history. The QuestionnaireResponse captures patient input. You want to show “78% complete” based on how many fields the patient filled before submission.

## Solution 

In this tutorial, we will calculate a form filling percentage using the following formula: 

Form Filling Percentage=(Number of Answered Items/ Total Enabled Questionnaire Items)×100


To show the form filling progress, we define a special Questionnaire.item which calculates the form filling percentage.


### Warning

This solution will work correctly with Aidbox version v2503 or later (available from March 25, 2025, if you're using the Edge channel)


## Steps to calculate the form filling percentage

1. To calculate the form filling progress, we have to create an item with `type = integer` and set `readOnly = true`.

2. This item **must** be placed at the end of the Questionnaire.item array and include a calculatedExpression with the following value:


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

**Specifics of the approach:**

1. Considers nested items (e.g., groups of questions)
2. For Questionnaire with enableWhen logic (conditional questions that appear based on previous answers): only enabled items are taken into account when calculating the form filling percentage. 
This may result in form filling percentage decrease when a user answer triggers a new section of answers to be enabled.
3. Questionnaire.item which are excluded when calculating Total Questionnaire Items:

- hidden items
- disabled items
- computed items
- readonly items
- group items
- display items


## Final Questionnaire with example items.

> In this example we replace typical numerical widget with slider to make it more visual but feel free to revert back:

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
