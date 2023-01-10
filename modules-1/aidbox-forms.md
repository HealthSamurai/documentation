---
description: Form building and structured data capturing with Aidbox
---

# Aidbox Forms

{% hint style="info" %}
**Aidbox Forms** is a pluggable module for healthcare vendors who develop EHR-like solutions on top of Aidbox. It allows for designing **intelligent** and **styled forms** from scratch with **a flexible DSL** and **layer-by-layer approach**, editing these forms by clinicians, **storing structured data** in one place as linked FHIR resources, and sharing through different and customizable **APIs**: FHIR, export, and reporting API.
{% endhint %}

Building forms and capturing structured data is a complex process.&#x20;

One of the solutions is suggested by FHIR. There is [the FHIR SDC Implementation Guide ](https://build.fhir.org/ig/HL7/sdc/index.html)which describes how to collect data using Questionnaire and QuestionnaireResponse resources. It is a good way. But the implementation on FHIR turned out to be not convenient, a massive QuestionnaireResponse with nested objects does not allow you to conveniently work with this table and make all kinds of queries, as well as build analytics on the data. Additionally, QuestionnaireResponse is too generic and difficult to work with.

We developed our solution based on domain-specific language (**DSL**) which gives **flexibility** and **composability**, with which you can describe **layer-by-layer** any forms  with complex logic and embed them in a workflow, capture structured data in FHIR storage.

### Our solution

We have presented the form as a layered structure. To design the form, you need to describe the necessary layers using DSL.

<figure><img src="../.gitbook/assets/Screenshot 2022-08-23 at 17.24.50.png" alt=""><figcaption></figcaption></figure>

**Each** **layer** is responsible for solving a **specific problem**:

* How structured data will be stored - **Data Model layer** (Document DSL)
* How a form will be rendered - **Layout layer** (Layout DSL)
* How to prefill a form with existing data and set the conext - **Prefill layer** (Launch DSL)
* How data will be itemized & extracted to FHIR resources - **FHIR mapping layer** (Finalize DSL)
* How data will be validated and how to put constraints on the fields - **Validation layer** (FinalizeConstrains DSL)
* How to bind all layers for specific form - **Form layer** (Form DSL)

One layer is required - **Data Model** layer, the rest are optional depending on needs of your practice.&#x20;



### By using Aidbox Forms you can:

* Create forms with **complex logic**
* **Prefill** form with existing data
* Add any **validation rules** to the forms
* **Design the layout** according to your style _(in roadmap)_
* **Extract data** & **store** them in FHIR resources
* **Binding** of forms in the **workflow** with complex logic and data prefilling
* FHIR **SDC support** (_in roadmap_)
* **IDE**: form editor with debug & test mode
* **Form builder** for non-developers (_in roadmap_)
* Wide **templates library** (_in roadmap_)
* Basic **UI component library**&#x20;
* **Coding clinical** data with LOINC, SNOMED, RxNorm others



