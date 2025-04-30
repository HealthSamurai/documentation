---
description: The article outlines the general form settings
---

# Form Settings

## General Form Settings

In the UI builder interface, users can set general settings for the form. These settings can be accessed:

When opening a form in the UI Builder, users can access the general settings for the form by clicking on the name of the form in the form outline.

This action opens a sidebar where users can modify the form's general settings.

These settings include:

### ID

The logical id of the form (Questionnaire resource), as used in the URL for the resource. Once assigned, this value never changes.

{% hint style="info" %}
When saving the form, Aidbox check the id for uniqueness
{% endhint %}

### Form Title

A short, descriptive, user-friendly title for the questionnaire.

### URL

This is the id that will be used to link a QuestionnaireResponse to the Questionnaire the response is for. It is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier.

{% hint style="info" %}
URL is generated automatically. A custom prefix can be specified in the Aidbox Forms module settings.
{% endhint %}

### Version

The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique.

There may be different questionnaires that have the same url but different versions. The version can be appended to the url in a reference to allow a reference to a particular business version of the questionnaire with the format.

{% hint style="info" %}
`Url` and `version` are used as `Questionnaire`uniq identifier - they should be unique globally.
{% endhint %}

For more details, go to the page - [Versioning.](versioning.md)

### Status

The current state of this questionnaire.

FHIR Questionnaire statuses: `draft`, `active`, `retired` and `unknown` (`unknown` should not be used by user)

* `draft` - used when you developing a form
* `active` - used for forms in production - you should be careful, and do not change these forms (small changes are ok).
* `retired`- used for production retired forms - you also should be careful about these, they are used for historical reasons
* `unknown` - used when form status is unknown

### Default Language

This is the default language of the form. Since forms can be multilingual, choosing the default language gives you the opportunity to set the translation of the form in other languages.

### Reusable Context

With this setting, you can specify how the form will be used. If you mark the form as `Component` or a `Form and Component`, then the form will be available in the add widget popup in the components tab and can be included in other forms as component.

* `Form` - This form can be used only as an _independent form_
* `Component` - This form can be used only as an _component_ (included in other forms)
* `Form or Component` - This form can be used only as an _independent form_ or be included in other forms as _component_

### Code

An identifier for this collection of questions in a particular terminology such as LOINC. It allows linking of the complete Questionnaire resources to formal terminologies.

### Tags

Questionnaire contains an element "meta", which is a set of metadata that provides technical and workflow context to the resource.

Tag is an metadata item. Tags are used to relate resources to process and workflow.

For example, you can use custom form typing and in order to filter the required form types you can use the tag mechanism for this.

### Profiles

The Questionnaire resource contains a "meta" element, which is a set of metadata providing technical and workflow context.

Profiles are a type of metadata item used to constrain resources.\
When a user adds a profile to a Questionnaire, Aidbox validates the Questionnaire against the profile during CRUD operations.

### Show outline

You can set the show outline and see how it will look in the form preview. An outline with top-level groups will be displayed on the left side of the form.

This feature is convenient to use if the form is very large and you need to navigate through it.

## Population

In order to pre-fill the form with data that is in the database. You need to define the incomming parameters for the form, which will then be used in specific fields that you want to pre-fill with data.

### **Incoming parameters:**

Parameters that can be passed to form's launch process. Parameters should be enabled before use. Every parameter will be available in populate expressions via it's name

Patient `%patient`

Encounter `%encounter`

Location `%location`

User `%user`

Study `%study`

### **Named Expressions:**

Named expressions can be also used in data pre-population process.

They are useful when:

* you need an additional information, that is not exists in incoming parameters
* you want to share some common pre-calculation with other populate expressions

They can be referenced with `%expr-name` literal.

Example: `%patient.name.first().given` where `%patient` is\
an expression name _patient_, prefixed with _%_ symbol.

For more details of usage, look at how-to guide [here](how-to-guides/how-to-populate-form.md).

## Extraction

Two options of data extraction are supported at the root level of Questionnaire: Definition-Based and Template-Based.

### **Definition-based extraction**

It maps form data dynamically to the corresponding FHIR resources. When configuring extraction, you must specify a FHIR Path expression in the Path field. This expression defines the resource and field where the data should be extracted.

**Current Logic:**

Two Options for Resource Extraction:

* **New Resource**: If "new resource" is selected, a new resource is always created during extraction.Comment
* **Existing Resource:** If "existing resource" is selected, the system includes the `questionnaire-itemExtractionContext` extension. This extension can be added either at the root of the Questionnaire or at any item level. It identifies the resource that serves as the context for extraction.Comment

Using `itemExtractionContext`:

* Empty `itemExtractionContext`: When the `itemExtractionContext` is empty, the Questionnaire is used to create a new resource.
* Populated `itemExtractionContext`: If the `itemExtractionContext` contains a resource (or set of resources), the Questionnaire updates the existing resource.

For detailed instructions and an example of extracting data into a Patient resource, visit the [Definition-Based Extraction Guide](how-to-guides/how-to-extract-data-from-forms.md#definition-based-extraction).

### Template-based extraction (_alpha_)

It uses predefined templates embedded in the Questionnaire to extract answers into FHIR resource&#x73;**,** along with all the "boiler-plate" content for the resource that is to be extracted..

#### &#x20;  **Current Logic:**

* Templates are included in the Questionnaire as **contained resources** and referenced using the [`sdc-questionnaire-templateExtract`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-templateExtract.html) or [`sdc-questionnaire-templateExtractBundle`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-templateExtractBundle.html) extensions.&#x20;
* There are two options where you can place the `templateExtract` extension:
  * **At the root level of the Questionnaire:** Used when you want to extract a single resource based on multiple answers from the form.
  * **At the item level:** Used when you want to extract data from specific questions or create a separate resource per repeated item.
* Data is mapped from answers in the QuestionnaireResponse into the template using FHIRPath expressions.
* Repeating items:
  * When `templateExtract` is placed on a repeatable item, the engine creates a new resource for each answer.
  * When extracting to a field that is an array (e.g., `Patient.address`), repeated answers can be added as multiple entries in the same resource.
* Use of the [`allocateId`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-extractAllocateId.html) extension allows generation of unique IDs and referencing between related resources.
* If the template includes expressions that return no result, the corresponding field or entry is automatically excluded from the output.

**Steps to create a resource template:**

* Select a predefined resource template from existing ones, or create a new template.
* At this moment, the Resource Template Editor will be opened.
* Set up the template name.
* Select the resource type of the template you want to create (e.g., Patient, Observation).
* Search and prefill the fields that you need in this resource template.
* Test and debug the extraction using the debug console. For this purpose, enter test data in the form.
* Close the Resource Template Editor panel if everything is correct, or discard changes.

For detailed instructions and examples of using templates for data extraction, visit the Template-Based Extraction Guide.

## Additional Information Settings

In this section, you can add additional information to the form that will be needed when publishing the form.

**Publisher** - the name of the organization or individual that published the questionnaire.

**Contact** - contact details to assist a user in finding and communicating with the publisher. It may be a web site, an email address, a telephone number, etc.

**Derived From (URL)** - the URL of a Questionnaire that this Questionnaire is based on.

**Description** - a free text natural language description of the questionnaire. This description can be used to capture details such as why the questionnaire was built, comments about misuse, instructions for clinical use and interpretation, literature references, examples from the paper world, etc.

**Copyright** - a copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.

## Appearance Settings

In this section, you can configure the appearance of your forms, ensuring they display correctly across different devices.

**Max Form Width:**

* The default form width is set to **960 pixels**.
* User can adjust the **maximum width** of the form based on your preferences. The form will be displayed at the maximum width, provided that the device on which the form is opened supports this size.
* User can define this value via [SDCconfig](https://docs.aidbox.app/modules/aidbox-forms/aidbox-ui-builder-alpha/configuration#configuration-resource-structure) and then all forms will have the set width by default. If you set the form width value in the form itself, then this value will override what is in the SDCconfig.

To ensure the form appears as intended, you can use the **Form Preview** feature to see how the form looks with the specified maximum width.
