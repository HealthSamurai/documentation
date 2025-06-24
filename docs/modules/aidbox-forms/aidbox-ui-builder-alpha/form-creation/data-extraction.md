---
description: >-
  Aidbox supports 3 options for data extraction: Observation-Based,
  Definition-Based, and Template-Based. These options provide flexibility in how
  form data is transformed into different FHIR resources
---

# Data Extraction

## **Observation-based extraction**

it allows data collected through a Questionnaire Response (QR) to be extracted and stored in FHIR Observation resources.

{% hint style="info" %}
Ensure that each item has both a **code** and a **system** defined.
{% endhint %}

### **Current Logic:**

* A new Observation is created for each Questionnaire Response instance at the time of data extraction.
* If a QR is amended (modified after submission), the existing Observations linked to that QR are updated accordingly.

For detailed instructions and an example of extracting data into an Observation resource, visit the [Observation-Based Extraction Guide](how-to-guides/how-to-extract-data-from-forms.md#observation-based-extraction).

## **Definition-based extraction**

It maps form data dynamically to the corresponding FHIR resources. When configuring extraction, you must specify a FHIR Path expression in the Path field. This expression defines the resource and field where the data should be extracted.

### **Current Logic:**

Two Options for Resource Extraction:

* **New Resource**: If "new resource" is selected, a new resource is always created during extraction.Comment
* **Existing Resource:** If "existing resource" is selected, the system includes the `questionnaire-itemExtractionContext` extension. This extension can be added either at the root of the Questionnaire or at any item level. It identifies the resource that serves as the context for extraction.Comment

Using `itemExtractionContext`:

* Empty `itemExtractionContext`: When the `itemExtractionContext` is empty, the Questionnaire is used to create a new resource.
* Populated `itemExtractionContext`: If the `itemExtractionContext` contains a resource (or set of resources), the Questionnaire updates the existing resource.

For detailed instructions and an example of extracting data into a Patient resource, visit the [Definition-Based Extraction Guide](how-to-guides/how-to-extract-data-from-forms.md#definition-based-extraction).

## Template-based extraction

It uses predefined templates embedded in the Questionnaire to extract answers into FHIR resource&#x73;**,** along with all the "boiler-plate" content for the resource that is to be extracted..

### **Current Logic:**

* Templates are included in the Questionnaire as **contained resources** and referenced using the [`sdc-questionnaire-templateExtract`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-templateExtract.html) or [`sdc-questionnaire-templateExtractBundle`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-templateExtractBundle.html) extensions.

{% hint style="info" %}
`templateExtract` and `templateExtractBundle` should not be used at the same time.
{% endhint %}

* There is one location where you can place the `templateExtractBundle` extension:
  * **At the root level of the Questionnaire:** Used when you want to extract several resources and control properties of Bundle and it's entries.
* There are two options where you can place the `templateExtract` extension:
  * **At the root level of the Questionnaire:** Used when you want to extract a single resource based on multiple answers from the form.
  * **At the item level:** Used when you want to extract data from specific questions or create a separate resource per repeated item.
* Data is mapped from answers in the QuestionnaireResponse into the template using FHIRPath expressions.
* Repeating items:
  * When `templateExtract` is placed on a repeatable item, the engine creates a new resource for each answer.
  * When extracting to a field that is an array (e.g., `Patient.address`), repeated answers can be added as multiple entries in the same resource.
* Use of the [`allocateId`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-extractAllocateId.html) extension allows generation of unique IDs and referencing between related resources.
* There is a way to make references between extracted resources:
  * In template, where you want to put reference - you put `templateExtractValue` extension with a FHIRPath expression referencing variable (for example `%newPatientId` )
  * In item that contain template which you want to reference - you should put `fullUrl` extension into `templateExtract` extension and reference the same variable (for example `%newPatientId` )
  * You should find common parent item for these 2 items and put there `allocateId` extension with that variable name (for example `newPatientId` - without '%' sign at front).
    * item with `allocateId` extension defines a scope with a variable where temporary created id is accessible.
    * You can put `allocateId` on any item or questionnaire's root.
    * For repeated items you should put `allocateId` into repeated item and there will be created separate id for every iteration.

{% hint style="info" %}
In Form UI Builder referencing resources greatly simplified and many steps are done automatically.

You should just select resource in dropdown when you specify value of a reference (for example RelatedPerson.patient)
{% endhint %}

* There is a way to extract new resource or modify existed.
  * When creating new resource - you fill template as is - without any additional effort
  * When updating existed resource - there are a couple of actions should be done before.
    * hidden text item for resource-id should be created
    * item should be populated with existed resource-id in `$populate` step
    * `resourceId` extension with FHIRPath should be added to `templateExtract` extension, and FHIRPath expression should extracts id from hidden item.

{% hint style="info" %}
In Form UI Builder exracting reference to resource from item slightly simplified, but other parts should be added manually (adding hidden item and it's population)
{% endhint %}

* If the template includes expressions that return no result, the corresponding field or entry is automatically excluded from the output.

### **Steps to create a resource template:**

* Select a predefined resource template from existing ones, or create a new template.
* At this moment, the Resource Template Editor will be opened.
* Set up the template name.
* Select the resource type of the template you want to create (e.g., Patient, Observation).
* Choose `new or existing resource`
* Search and prefill the fields that you need in this resource template.
* Test and debug the extraction using the debug console. For this purpose, enter test data in the form.
* Close the Resource Template Editor panel if everything is correct, or discard changes.

For detailed instructions and examples of using templates for data extraction, visit the Template-Based Extraction Guide.
