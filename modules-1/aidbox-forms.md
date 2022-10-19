---
description: Form building and structured data capturing with Aidbox
---

# Aidbox Forms

Building forms and capturing structured data is a complex process.&#x20;

One of the solutions is suggested by FHIR. There is [the FHIR SDC Implementation Guide ](https://build.fhir.org/ig/HL7/sdc/index.html)which describes how to collect data using Questionnaire and QuestionnaireResponse resources. It is a good way. But the implementation on FHIR turned out to be not convenient, a massive QuestionnaireResponse with nested objects does not allow you to conveniently work with this table and make all kinds of queries, as well as build analytics on the data. Additionally, QuestionnaireResponse is too generic and difficult to work with.

We tried to take into account all these difficulties when developing our product. We developed our own **DSL**, which gives **flexibility** and **composabiltity**, with which you can describe any forms with complex logic and embed them in a workflow.

{% hint style="info" %}
&#x20;**Aidbox Forms** is a toolkit that helps health vendors create and customize forms for doctors, combine them into a workflow and collect data in a structured form, so that they can then be conveniently used for analytics and other purposes.
{% endhint %}

### By using Aidbox Forms you can:

* Create any forms with **complex logic**
* **Prefill** forms with existing data&#x20;
* Add any **validation** to the form&#x20;
* Change the **layout** according to your style&#x20;
* **Extract data** and store them in FHIR resources&#x20;
* Describe **workflow** with data prefilled from previous forms or make it dynamic (when next form is suggested based on completed results)

### Our solution

We have presented the form as a layered structure. To design the form, you need to describe the necessary layers using DSL.

Let's look at the diagram to see how the form is built.

<figure><img src="../.gitbook/assets/Screenshot 2022-08-23 at 17.24.50.png" alt=""><figcaption></figcaption></figure>

Two layers are required - **Form Layout** and **Data Model**, the rest are optional depending on needs of your practice.&#x20;

**Form Layout layer** describes components and how they are shown on the form. This layer is described using [Layout DSL](../reference/aidbox-forms/layout-dsl.md).

**Data Model layer** specifies the data structure or how the data will be stored in the database. This layer is described using [Document DSL](../reference/aidbox-forms/document-dsl.md).

**Form Prefill layer** describes how to automatically fill out the form with existing data. This layer is described using [Launch DSL](../reference/aidbox-forms/launch-dsl.md).

**Form Validation layer** describes validation rules and field constraints. This layer is described using [FinalizeConstraints DSL](../reference/aidbox-forms/finalizeconstraints-dsl.md).

**FHIR mapping layer** describes how data will be extracted to FHIR resources (Observation, AllergyIntolerance and others). This layer is described using [Finalize DSL](../reference/aidbox-forms/finalize-dsl.md).

There is the entity that binds all form layers (DSL) - [Form DSL](../reference/aidbox-forms/form-dsl.md)



### Workflow

Forms can be embedded into [a workflow](../reference/aidbox-forms/workflow-reference.md).

{% hint style="info" %}
**Workflow** is a skeleton for forms composition in more complex structures.
{% endhint %}

Initially Workflow have nested structure of items, where each item can be:

* Section - used for forms grouping.
* Form - reference to existed form.

Workflow and items has status model , model is slightly different

**Workflow statuses:**

* `new`
* `in-progress`
* `canceled`
* `completed`
* `in-amendment`
* `amended`

**Item statuses:**

* `new`
* `in-progress`
* `skipped`
* `completed`
* `in-amendment`
* `amended`

{% hint style="warning" %}
**`canceled`** status used for WF because `skipped` status is not obvious in this domain. WF is a process of action, but item is just a step that can be optionally and can be omitted.
{% endhint %}

``

Workflow support 2 additional features:

* `versioning` - is automatic and based on hashing essential fields of definitions. If some essential field of form/wf is changed - created a new version and snapshotted to DB
*   `section id generation -` is generated from item path (path from WF root to item itself).



These features you can configure via [api-constructor](../aidbox-configuration/aidbox-api-constructor.md) in zen-project.



### FHIR to Aidbox Forms conversion and to back.

You may use Aidbox to convert existing [FHIR Questionnaires to Aidbox Forms](../reference/aidbox-forms/api-reference.md#aidbox.sdc-convert-questionnaire) and [SDCDocument to FHIR QuestionnaireResponse resource](../reference/aidbox-forms/api-reference.md#aidbox.sdc-convert-document).

###

\








