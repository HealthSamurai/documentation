---
description: The article outlines Aidbox Forms UI
---

# Forms UI

The Forms UI consists of three main sections, accessible through tabs at the left side of the page:

* Form Templates
* Responses
* Form Gallery

## **Form Templates**

The **Form Templates** tab is the default view when you navigate to the Forms page. Initially, this page will be empty. Once you create a form or select one from the form gallery, it will appear in the list of templates.

**Actions Available for Form Templates:**

* **Search**: You can search through the list of form templates to quickly find a specific form.
* **Filter**: You can filter the form templates by category, with the following options:
  * **All**: Displays all available forms.
  * **Forms**: Filters the list to show only forms in Aidbox Format.
  * **Questionnaires**: Displays forms categorized as questionnaires in FHIR Format.
  * **Workflow**: Shows workflows consist of a set of specific forms.
* **Preview**: Click to open a form in a separate popup to view its details.
* **Share**: Generate a shareable link to the form that can be sent to others.

**Additional Actions (Accessible via the Three Dots Menu):**

* **Duplicate Form**: Duplicating a form creates a new form with a unique title and URL.
* **Delete Form**: You can delete a form template from the database. When attempting to delete a form, the system checks if the form is in use or if there are any responses associated with it. If applicable, you will be prompted to confirm whether you want to proceed with deletion.

## **Responses**

The **Responses** tab displays user responses to your forms. This tab shows the responses in view mode, allowing you to review completed forms without editing the original submission.

All responses are stored in the database in the QuestionnaireResponse resources.

## **Form Gallery**

The **Form Gallery** allows you to browse through available templates. Once selected, the forms from the gallery will be added to your **Form Templates** tab for use.

There, you will discover over 3000 form templates.&#x20;

These templates can be:

* viewed in preview mode&#x20;
* &#x20;imported into your Aidbox Instance

Once you click the import button, the form is saved to the database and opens in the Form Builder.
