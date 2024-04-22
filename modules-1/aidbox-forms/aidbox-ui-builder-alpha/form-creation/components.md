---
description: >-
  this article outlines how to create custom components and reuse it in other
  forms.
---

# Components

Custom components can be used to share content, such as questions or group of questions, across multiple forms. This approach is particularly useful for organizations with research focus or those aiming for consistency and re-usability of questions.&#x20;

Here's how it works:

## **Creating Custom Components**

Users can create custom components that represent question or group of questions that they want to re-use.

These custom components can include any form elements supported by Aidbox Forms, such as text fields, checkboxes, or dropdowns and etc. including their attributes and rules. &#x20;

When a user is designing a form in the UI builder, he can create a component:

* Select question or group of questions and click on "save as a component" icon presented on a field question or group of questions in the outline.&#x20;
* Put the name and the url for this component and save it.
* The saved component will appear in the "components" tab of the `+add widget` popup.&#x20;

This is what happens behind the scenes:

When a user saves a component, it is saved as a FHIR Questionnaire resource in the database according to FHIR SDC Implementation with a field `experimental: true`

&#x20;and extension&#x20;

```
  extension:
    - url: >-
        http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-assemble-expectation
      valueCode: assemble-child
```
