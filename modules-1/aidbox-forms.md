---
description: Form building and structured data capturing with Aidbox
---

# Aidbox Forms

{% hint style="info" %}
Aidbox Forms is a **pluggable module** for healthcare vendors who need **to create digital forms**, questionnaires, and surveys for clinical workflows.
{% endhint %}

It allows to design and embed digital medical forms and capture data in FHIR format for reporting and analysis.

{% hint style="info" %}
To play with forms on a test data without authorization and without creating an Aidbox instance, use the [public Aidbox Form Builder](https://form-builder.aidbox.app/). In it, you can create a form, download its definition, or share it via a link.
{% endhint %}

## Aidbox Forms provides:

* Ready-made medical form repository ([Aidbox Form Gallery](aidbox-forms/add-aidbox-forms-library.md))
* [UI Builder](aidbox-forms/aidbox-ui-builder-alpha/) for creating forms without a code (based on [FHIR SDC Implementation Guide](https://build.fhir.org/ig/HL7/sdc/index.html))
* [Aidbox Code Editor](aidbox-forms/aidbox-code-editor/) for creating complex forms (based on DSL) - **deprecated**
* Form rendering engine
* [FHIR SDC API](../reference/aidbox-forms/fhir-sdc-api.md)
* [Aidbox SDC API](../reference/aidbox-forms/aidbox-sdc-api.md)
* [FHIR API](../api-1/fhir-api/)
* [RPC API](../reference/aidbox-forms/api-reference.md)
* FHIR storage

## By using Aidbox Forms you can:

* Create a form with complex logic from scratch in UI Builder or Code editor
* Create a form by importing FHIR Questionnaire from external sources
* Use a ready-made form from Aidbox Form Gallery or customize it according to your requirements
* Test form in UI Builder or Code editor
* Pre-fill form with existing data
* Add any validation rules to the forms
* Extract data and store them in FHIR resources
* Binding of forms in the workflow with complex logic and data prefilling
* Coding clinical data with LOINC, SNOMED, RxNorm and others
* Integrate form into your current solutions with the versatile API, iframe, and generate a link
* Capture and store data in FHIR storage

{% hint style="warning" %}
We strongly recommend using our UI Form Builder based on FHIR SDC (Structured Data Capture), which we continuously develop. The current solution will remain supported but will not receive further development at this time.
{% endhint %}
