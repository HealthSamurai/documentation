---
description: Learn how to add **“exclusive” answer options** to multichoice questions
---

# Exclusive Option for multichoice questions

## Overview

This feature adds **“exclusive” answer option(s)** support to Aidbox Forms UI for **multichoice** questions. 
When an option is marked as exclusive, it **cannot be selected together** with any other option and will automatically clear conflicting selections.

## What Was Implemented

In item properties → Options, alongside required fields (code, system, text), you can now optionally include an exclusiveness field.

### New Options UI Controls
**Enable the "exclusive" column**

- Open item properties
- Go to Options
- Click the checkbox “Include exclusiveness”

**Once enabled:**
- A new column appears
- The new column includes an Exclusive checkbox per each answer option

**Mark an option as exclusive**

- To make an answer exclusive, check Exclusive for the corresponding option (e.g., “None of the above”)

## Behavior Rules
**Exclusive option clears other selections**

If a user selects multiple answers and then selects an option marked as Exclusive:
- All other selected options are automatically cleared
- Only the exclusive option remains selected
- All options remain visible (only selections are changed)

**Selecting a normal option deselects an exclusive one**

If an exclusive option (e.g., “None of the above”) is already selected and the user selects any other option:
- The exclusive option is automatically deselected
- This guarantees an exclusive option is never selected simultaneously with another option

**Multiple exclusive options are mutually exclusive**

If more than one option is marked as Exclusive, then they are also mutually exclusive:
- Selecting one exclusive option deselects the previously selected exclusive option
- Only one exclusive option can be selected at a time

### Supported Question Types

This feature works with:
1. Open choice input — only when Repeats is enabled
> If Repeats is disabled, the user can select only one option anyway, so exclusiveness is not applicable.
2. Choice input — only when Repeats is enabled
> Same reasoning: without Repeats, selection is single-choice by design.
3. Checkbox lists

### Notes and Limitations

- Exclusiveness is designed for multi-select scenarios.
- For single-select inputs (when Repeats is disabled), exclusive behavior does not change anything because the UI already restricts the user to one selection.

## Example Scenario
**“None of the above” pattern**

This is a small demo form that contains one question: “Primary skin concern.”
In this question, the user can select one or more options from the list:
- Skin lesion
- Atopic dermatitis
- Acne
- Rash / Eruption
- Other
- None of the above (exclusive option)

If the user selects "None of the above", all previously selected options are cleared.
If the user has “None of the above” selected and then selects another option, “None of the above” is cleared automatically.

```json
{
  "resourceType": "Questionnaire",
  "title": "Primary skin concern",
  "id": "3ee28553-4233-40e8-acae-67c82b6ce48e",
  "status": "draft",
  "url": "http://forms.aidbox.io/questionnaire/5774325c-6f1e-40cb-ae45-bf8bc82569aa",
  "meta": {
    "lastUpdated": "2026-02-27T14:42:08.865566Z",
    "versionId": "519",
    "extension": [
      {
        "url": "https://aidbox.app/ex/createdAt",
        "valueInstant": "2026-02-27T14:42:08.865566Z"
      }
    ]
  },
  "item": [
    {
      "text": "Your primary skin concern",
      "type": "choice",
      "linkId": "o7UgD6bi",
      "repeats": true,
      "required": true,
      "extension": [
        {
          "url": "http://hl7.org/fhir/StructureDefinition/entryFormat",
          "valueString": "Select options from the list"
        },
        {
          "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-hidden",
          "valueBoolean": false
        },
        {
          "url": "http://aidbox.io/questionnaire-itemColumnSize",
          "valueInteger": 4
        }
      ],
      "answerOption": [
        {
          "valueCoding": {
            "code": "95324001",
            "system": "http://snomed.info/sct",
            "display": "Skin lesion"
          }
        },
        {
          "valueCoding": {
            "code": "11381005",
            "system": "http://snomed.info/sct",
            "display": "Atopic dermatitis"
          }
        },
        {
          "valueCoding": {
            "code": "24079001",
            "system": "http://snomed.info/sct",
            "display": "Acne"
          }
        },
        {
          "valueCoding": {
            "code": "271807003",
            "system": "http://snomed.info/sct",
            "display": "Rash / Eruption"
          }
        },
        {
          "valueCoding": {
            "code": "74964007",
            "system": "http://snomed.info/sct",
            "display": "Other"
          }
        },
        {
          "valueCoding": {
            "display": "None of the above"
          },
          "extension": [
            {
              "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-optionExclusive",
              "valueBoolean": true
            }
          ]
        }
      ]
    }
  ]
}
```
