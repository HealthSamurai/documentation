---
description: Validate FHIR Questionnaires against specifications and Implementation Guides using Form Builder's integrated checks.
---

# Validation

Validation ensures that Questionnaire has correct structure and met it's internal constraints. This enables Questionnaire to be correctly interpreted by different systems and working without surprises.

## FHIR Validation

Form builder have integrated FHIR validation throught **Validate** button (debug console).

This process checks the Questionnaire against the FHIR specification and any Implementation
Guides (IGs) installed on the server. It ensures that the Questionnaire is technically valid,
consistent with external IG requirements, and ready to be exchanged across systems.

Use this when you want to confirm compliance with the official FHIR rules, not only the
Builder’s internal checks.

## Builder internal validation

The Builder includes its own validation engine in addition to FHIR validation.
It verifies internal structure, logical consistency, and implementation-specific rules
that FHIR validation alone does not cover.

Validation results appear in the form structure outline. Each issue is marked with an alert
icon, and details can be viewed in a tooltip. There are two levels of severity:
**warnings** and **errors**.

### Warnings

Warnings are displayed with an **orange icon**.
They highlight conditions where the form may still save successfully but will not behave as
expected at runtime. Use warnings as indicators of possible unintended outcomes or
limitations in rendering.

**Example:** In matrix forms, nested child items are ignored. The form saves, but those items
are not visible to users.

### Errors

Errors are displayed with a **red icon**.
They indicate violations that prevent the Questionnaire from being saved. Errors typically
relate to missing required fields, invalid values, or broken structural rules.

**Example:** A missing `url` field will block saving because it is required for identifying
the Questionnaire.

## Validation Rules

The Builder currently enforces the following rules.


### ERROR: URL must not be empty

- **Concept:** Every Questionnaire requires a `url` to uniquely identify it.
- **Example:**
  ```json
  {
    "resourceType": "Questionnaire",
    "url": ""
  }
  ```

- **Error**: URL is required.


### ERROR: URL must not contain invalid characters

- **Concept:** Certain characters such as `,`, `|` and `#` are not allowed in the url.
- **Example:**
  ```json
    http://example.org/questionnaire|demo
  ```
- **Error:** The following characters are not allowed: '|', '#', ','


### ERROR: linkId must be unique

**Concept:** Each item must have a unique linkId for referencing answers.
**Example:**
    ```json
    {
        "item": [
            { "linkId": "q1", "text": "Question A" },
            { "linkId": "q1", "text": "Question B" }
        ]
    }
    ```
- **Error:**  LinkId should be unique


### WARNING: Nested items hidden in matrix forms

- **Concept:** Matrix forms do not support nested child items; these items are ignored in rendering.

**Example:** Adding a group inside a matrix row will not be shown to users.
  ```json
    {
      "text": "Choice Matrix",
      "type": "group",
      "linkId": "dZr7mzi9",
      "extension": [
        {
          "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl",
          "valueCodeableConcept": {
            "coding": [{"code": "table", "system": "http://hl7.org/fhir/ValueSet/questionnaire-item-control"}]
          }
        }
      ],
      "item": [
        {
          "text": "Choice Input",
          "type": "choice",
          "linkId": "VuvOWnnp",
          "item": [
            {
              "type": "string",
              "text": "String Input",
              "linkId": "WawGb4tR"
            }
          ]
        }
      ]
    }
  ```
- **Warning**: Nested items will not be visible in Choice Matrix


### WARNING: Matrix forms must only contain choice items

- **Concept:** A matrix requires choice-type questions
- **Example:** Using a string input inside a matrix will be ignored.
  ```json
    {
      "text": "Choice Matrix",
      "type": "group",
      "linkId": "dZr7mzi9",
      "extension": [
        {
          "url": "http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl",
          "valueCodeableConcept": {
            "coding": [{"code": "table", "system": "http://hl7.org/fhir/ValueSet/questionnaire-item-control"}
            ]
          }
        }
      ],
      "item": [
        {
          "text": "Choice Input",
          "type": "choice",
          "linkId": "VuvOWnnp",
        },
        {
          "type": "string",
          "text": "String Input",
          "linkId": "WawGb4tR"
        }
      ]
    }
  ```
- **Warning**: Nested items will not be visible in Choice Matrix


<!-- - expressions should have only known linkIds -> whole Q, state between items, [breadth first pre-order] -->
<!-- - conditions should have only known linkIds, -> whole Q, state between items, [breadth first pre-order] -->
<!-- - template extractions should have only known linkIds -> whole Q, [pre-order] -->
<!-- - expressions should link only known references -> whole Q, state between items, [pre-order] -->
<!-- - FUTURE: pages should be only on top-level -> whole Q , position, state [any order] -->
