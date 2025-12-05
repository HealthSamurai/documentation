---
description: Navigate Aidbox Forms UI including Form Templates, Responses, and Form Gallery with search, filter, and sharing features.
---

# Aidbox Forms Interface

The Forms UI consists of three main sections, accessible through tabs at the left side of the page:

* Form Templates
* Responses
* Form Gallery

## **Form Templates**

The **Form Templates** tab is the default view when you navigate to the Forms page. Initially, this page will be empty. Once you create a form or select one from the form gallery, it will appear in the list of templates.

**Actions Available for Form Templates:**

* **Search**: You can search through the list of form templates to quickly find a specific form.
* **Filter**: You can filter the form templates by category, with the following options:
  * **All**: Displays all available forms and components.
  * **Forms**: Displays forms categorized as questionnaires in FHIR Format.
  * **Components:** Displays custom components that have been created and reused in other forms.
* **Preview**: Click to open a form in a separate popup to view its details.
* **Share**: Generate a shareable link to the form that can be sent to others.

**Additional Actions (Accessible via the Three Dots Menu):**

* **Duplicate Form**: Duplicating a form creates a new form with a unique title and URL.
* **Delete Form or Component**: You can delete a form template or component (sub-form) from Form Templates list. At the same time it will be deleted in the database too.
  * When attempting to delete a form or component, the system checks if the form is in use or if there are any responses associated with it. If applicable, you will be prompted to confirm whether you want to proceed with deletion.

## **Responses**

The **Responses** tab displays user responses to your forms. This tab shows the responses in view mode, allowing you to review completed forms without editing the original submission.

All responses are stored in the database in the QuestionnaireResponse resources.

## **Form Gallery**

The **Form Gallery** allows you to browse through available templates. Once selected, the forms from the gallery will be added to your **Form Templates** tab for use.

There, you will discover over 3000 form templates.

These templates can be:

* viewed in preview mode
* imported into your Aidbox Instance

Once you click the import button, the form is saved to the database and opens in the Form Builder.
