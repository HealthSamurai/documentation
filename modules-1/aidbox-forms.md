---
description: Form building and structured data capturing with Aidbox
---

# Aidbox Forms

Building forms and capturing structured data is a complex process.&#x20;

One of the solutions is suggested by FHIR. There is [the FHIR SDC Implementation Guide ](https://build.fhir.org/ig/HL7/sdc/index.html)which describes how to collect data using Questionnaire and QuestionnaireResponse resources. It is a good way. But the implementation on FHIR turned out to be not convenient, a massive QuestionnaireResponse with nested objects does not allow you to conveniently work with this table and make all kinds of queries, as well as build analytics on the data. Additionally, QuestionnaireResponse is too generic and difficult to work with.

We tried to take into account all these difficulties when developing our product. We developed our own **DSL**, which gives **flexibility** and **composabiltity**, with which you can describe any forms with complex logic and embed them in a workflow.

{% hint style="info" %}
&#x20;**Aidbox Forms** is a toolkit that helps EHR vendors create and customize forms for doctors, combine them into a workflow and collect data in a structured form, so that they can then be conveniently used for analytics and other purposes.
{% endhint %}

### By using Aidbox Forms you can:

* Create any forms with **complex logic**
* **Prefill** forms with existing data&#x20;
* Add any **validation** to the form&#x20;
* Change the **layout** according to your style&#x20;
* **Extract data** and store them in FHIR resources&#x20;
* Describe **workflow** with data prefilled from previous forms or make it dynamic (when next form is suggested based on completed results)

### Our solution

We have presented the form as a layered structure. To design the form, you need to describe the necessary layers by DSL.

Let's look at the diagram how presents the form

<figure><img src="../.gitbook/assets/Screenshot 2022-08-23 at 17.24.50.png" alt=""><figcaption></figcaption></figure>

Two layers are required - **Form Layout** and **Data Model**, the rest can be optional depending on needs of your practice.

**Form Layout layer** describes components and layout of the form. This layer is described by [Layout DSL](../reference/aidbox-forms/layout-dsl.md).

**Data Model layer** specifies the data structure in which form the data will be stored in the database. This layer is described by [Document DSL](../reference/aidbox-forms/document-dsl.md).

**Form Prefill layer** describes how to prefill form by existing data. This layer is described by [Launch DSL](../reference/aidbox-forms/launch-dsl.md).



This page is under construction

