---
description: The article outlines how to use forms from Formbox Gallery
---

# Aidbox Form Gallery

The Formbox Gallery offers forms in FHIR format (FHIR Questionnaire). There are more than 3000 forms in the Formbox Gallery.&#x20;

Previews of these forms are available [here](https://form-builder.aidbox.app/).

These forms can be loaded into Aidbox using various methods:

1. Using the Aidbox Community Notebook (limited set of forms for demo)&#x20;
2. Through the public Aidbox Form Builder
3. Searching and importing a form from the Aidbox Form Gallery inside the Aidbox Form Builder

## 1. Using the Aidbox Community Notebook (limited set of forms for demo)&#x20;

The Formbox Gallery includes the following forms in FHIR format:

* **ROS (Review of System)**
* **Physical Exam**
* **Cough**
* **Vitals Sign** (coded in the LOINC code system)
* **PHQ2 / PHQ9 Depression form** (coded in the LOINC code system)

Users can upload these forms using the `fhir/$load` operation and then open them in the Formbox module for editing according to their requirements.&#x20;

Use the Aidbox Form Gallery notebook (community) available in the Aidbox console of your instance for this purpose.

## 2. Through the public Aidbox Form Builder

The Aidbox Form Gallery is accessible on the[ public Aidbox Form Builder page](https://form-builder.aidbox.app/).

Follow these steps:

* find the form in the [Aidbox Form Gallery](https://form-builder.aidbox.app/)
* open it in Form Builder
* download the form definition
* load the form definition through the Form Builder UI or use an FHIR operation `POST /fhir/Questionnaire`

## 3. Searching and importing a form from the Aidbox Form Gallery inside the Aidbox Instance

Open the Aidbox console and navigate to the Forms page, you will find the Form Gallery section. There, you will discover over 3000 form templates.&#x20;

These templates can be:

* viewed in preview mode&#x20;
* &#x20;imported into your Aidbox Instance

Once you click the import button, the form is saved to the database and opens in the Form Builder.
