---
description: >-
  This article outlines how to create custom components for reusing them in
  other forms and how to reuse forms to assemble a new form.
---

# Components

Custom components can be used to share content, such as questions or group of questions, across multiple forms.  At the same time, forms can be assembled from other forms. This approach is particularly useful for organizations with research focus or those aiming for consistency and re-usability of questions.

Here's how it works:

## **Creating Custom Components**

Users can create custom components that represent question or group of questions that they want to re-use.

These custom components can include any form elements supported by Aidbox Forms, such as text fields, checkboxes, or dropdowns and etc. including their attributes and rules.

When a user is designing a form in the UI builder, he can create a component:

* Select question or group of questions and click on "save as a component" icon presented on a field question or group of questions in the outline.
* Put the name and the url for this component and save it.
* The saved component will appear in the "components" tab of the `+add widget` popup.

**This is what happens behind the scenes:**

When a user saves a component, it is saved as a FHIR Questionnaire resource in the `active status` in the database according to the [FHIR SDC Implementation](https://build.fhir.org/ig/HL7/sdc/modular.html#modular-questionnaires) with the the extension

```
  extension:
    - url: >-
        http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-assemble-expectation
      valueCode: assemble-child
```

Here's an example of how a FHIR Questionnaire as a component looks like:

{% code overflow="wrap" %}
```json
// {
  "title": "Patient Phone",
  "id": "09687a66-6bd7-415a-b10e-82d01029be49",
  "status": "active",
  "url": "http://aidbox.io/component/patient-phone",
  "version": "1",
  "extension": [
    {
      "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-assemble-expectation",
      "valueCode": "assemble-child"
    }
  ],
  "resourceType": "Questionnaire",
  "item": [
    {
      "text": "Patient Phone",
      "type": "integer",
      "linkId": "ginzzecv"
    }
  ]
}
```
{% endcode %}

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

When a user is designing the form in the UI builder, he can use the saved component as a reference in the form.

To do this a user needs:

* click on the `+add widget` button
* on the components tab, select the saved component

The component will be included in the form as a reference. This allows user to make changes to a component on the fly, and these changes will be picked up by all forms that reference this component.

**This is what happens behind the scenes:**

The notion of a modular Questionnaire is that a 'display' item in a parent questionnaire can include an extension pointing to a specific Questionnaire whose items should be embedded in the resulting assembled Questionnaire in place of the 'display' item.

Here is an example of how the original Questionnaire looks like with the component as a reference:

{% code overflow="wrap" %}
```json
// {
  "title": "Patient Contacts (test)",
  "id": "patient-contacts--test",
  "status": "draft",
  "url": "http://forms.aidbox.io/questionnaire/patient-contacts--test",
  "resourceType": "Questionnaire",
  "item": [
    {
      "type": "string",
      "text": "Patient Address",
      "linkId": "XFzB42pV"
    },
    {
      "type": "display",
      "linkId": "Qz__-FDD",
      "text": "Sub-questionnaire cannot be resolved: http://aidbox.io/component/patient-phone",
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-subQuestionnaire",
          "valueCanonical": "http://aidbox.io/component/patient-phone|1"
        }
      ]
    }
  ]
}
```
{% endcode %}

{% hint style="info" %}
At the moment the original Questionnaire is in the active status and saved, [$assemble](https://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-assemble.html) operation occurs and all components are inlined into the Questionnaire that was saved.
{% endhint %}

Here is an example of what the assembled Questionnaire looks like with inlined component:

After saving and assembling the Questionnaire, there are two Questionnaires in the database: the assembled Questionnaire with inlined component and the original Questionnaire with component as a reference.

{% hint style="info" %}
In the[ $populatelink](https://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-populatelink.html) operation, the assembled Questionnaire is used
{% endhint %}

### 2. Inline the component into the form

When a user creates a form in the UI builder, he can immediately inline the component into the form and edit it only in this form.

To do this you need:

* click on "inline component" icon presented in the outline
* continue working with the form as usual

## Editing a custom component

To edit a component:

* open any form in the UI builder
* click on the add widget button
* on the components tab, select the saved component
* click on "edit" component

After this, the component will open on a separate page of the UI builder.

{% hint style="warning" %}
The user can make any changes. When saving changes, all forms that reference this component will be updated in accordance with these edits.
{% endhint %}

There are some use cases with component editing:

1. The user can edit a component and then when saving the form that in the active status and with this component, a rebuild will occur and the assembled Questionnaire will be overwritten. The database will have one original Questionnaire and one assembled Questionnaire.
2. The user can create a new version of the component and save the old version of the form, then there will be one version of the original Questionnaire and the assembled Questionnaire will be overwritten in the database.
3. The user can create a new version of the component and save the new version of the form, then there will be two versions of original Questionnaires and two versions of assembled Questionnaires in the database.

## Assembling a Form from Other Forms

Aidbox Forms allows you to assemble a new form by reusing existing forms as components. This is a powerful feature that lets you build complex forms efficiently using pre-existing templates.

### **Steps to Assemble a Form from Ready-Made Forms:**

1. **Mark Form as Reusable:**
   * First, navigate to the settings of the form you want to use as components.
   * In the **Reused Context** field, select the option **Reusable Form**.
   * Once this option is selected, the form will be available in the list of components.&#x20;

{% hint style="info" %}
Additionally, a specific extension is added to the FHIR Questionnaire resource to mark it as reusable.

```json
"extension": [
    {
      "url": "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-assemble-expectation",
      "valueCode": "assemble-root-or-child"
    }
  ]
  
```
{% endhint %}

2. **Add Reusable Form to a New Form:**

* When creating a new form, click on **Add Widget**.
* Navigate to the **Components** tab, where you'll find the list of reusable forms and components. Select the desired form to include it in your new form.

3. **Using the reusable form as a custom component in a New Form:**

&#x20; The reusable form can be included in two ways:

* **As a** **reference**: The form will appear as a reference within the new form.
* **Inlined**: The form content will be embedded directly into the new form.

More details you can read [here.](components.md#using-a-custom-component-in-forms)

