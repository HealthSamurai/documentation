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

When a user saves a component, it is saved as a FHIR Questionnaire resource in the active status in the database according to the [FHIR SDC Implementation](https://build.fhir.org/ig/HL7/sdc/modular.html#modular-questionnaires) with the field `experimental: true`

&#x20;and the extension&#x20;

```
  extension:
    - url: >-
        http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-assemble-expectation
      valueCode: assemble-child
```



{% hint style="warning" %}
This Questionnaire is intended to be used as an assembly operation but cannot be used as a root - it must always be a child module
{% endhint %}



{% hint style="info" %}
To use search parameters \``assembled-from`:``token` and `assemble-expectation``:`` token` ``, you need to download [SDC Modular Questionnaire profiles](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-modular.html).
{% endhint %}



## Using a custom component in forms

There are two options for using components in a form.

* Via the reference to the component
* Inline the component into the form

### 1. Via the reference to the component

When a user creates a form in the UI builder, he can use the saved component as a reference.&#x20;

To do this you need:

* click on the add widget button
* on the components tab, select the saved component

The component will be saved to the form as a reference. This allows user to make changes to a component on the fly, and these changes will be picked up by all forms that reference this component.

{% hint style="info" %}
At the moment the form is launched,  [$assemble](https://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-assemble.html) operation occurs and all components are inlined into the form that was launched.
{% endhint %}

### 2. Inline the component into the form

When a user creates a form in the UI builder, he can immediately inline the component into the form and edit it only in this form.&#x20;

To do this you need:

* click on "inline component" icon presented in the outline
* continue working with the form as usual



## Editing a custom component

To edit a component:&#x20;

* open any form in the UI builder
* click on the add widget button
* on the components tab, select the saved component
* click on "edit" component&#x20;

After this, the component will open on a separate page of the UI builder.&#x20;

{% hint style="warning" %}
The user can make any changes. When saving changes, all forms that reference this component will be updated in accordance with these edits.
{% endhint %}
