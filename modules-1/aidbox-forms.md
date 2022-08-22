---
description: Form building and structured data capturing with Aidbox
---

# Aidbox Forms

Form building and structured data capturing is a complex process.&#x20;

One of the solutions is suggested by FHIR. There is [the FHIR SDC Implementation Guide ](https://build.fhir.org/ig/HL7/sdc/index.html)which describes how to collect data using Questionnaire and QuestionnaireResponse resources. It is a good way.

But the implementation on FHIR turned out to be not convenient, a massive QuestionnaireResponse with nested objects does not allow you to conveniently work with this table and make all kinds of queries, as well as build analytics on the data. On the other hand, QuestionnaireResponse is too generic and difficult to work with.

We tried to take into account all these difficulties when developing our product. We developed own **DSL**, which gives **flexibility** and **composabiltity**, with which you can describe any forms with complex logic and embed them in a workflow.

{% hint style="info" %}
&#x20;**Aidbox Forms** is a toolkit that helps EHR vendors create and customize forms for doctors, combine them into a workflow and collect data in a structured form, so that they can then be conveniently used for analytics and other purposes.
{% endhint %}

#### By using Aidbox Forms you can:

* Create any forms with **complex logic**
* **Prefill** forms with existing data&#x20;
* Add any **validation** to the form&#x20;
* Change the **layout** according to your style&#x20;
* **Extract data** and store them in FHIR resources&#x20;
* Describe **workflow** with data prefilled from previous forms or make it dynamic (when next form is suggested based on completed results)
