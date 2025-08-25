---
description: The article outlines the general form settings
---

# Form Settings

## General Form Settings

In the UI builder interface, users can set general settings for the form. These settings can be accessed:

When opening a form in the UI Builder, users can access the general settings for the form by clicking on the name of the form in the form outline.

This action opens a sidebar where users can modify the form's general settings.

These settings include:

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

### Entry Mode

Entry Mode defines how questions are presented and navigated in your form. For more detailed information, please see [this page](entry-mode.md).

There are options:

* `Sequential Questions -` will be displayed _one-at-a-time_ with no ability to adjust prior entries
* `Prior EditQuestions` -  will be displayed _one-at-a-time_ where all questions up to the current question can be looked at and edited
* `RandomQuestions` -  can be seen and the questions can be filled out in any order desired

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

For detailed instructions and example of populating data visit the[ Population page](population.md) and our [How-to guides](how-to-guides/how-to-populate-forms-with-data.md).

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

For more details of usage, look at how-to guide [here](how-to-guides/how-to-calculate-form-filling-percentage.md).

## Data Extraction

Two options of data extraction are supported at the root level of Questionnaire: Definition-Based and Template-Based.

For detailed instructions and example of extracting data visit the[ Data Extraction page](data-extraction.md) and our [How-to guides](how-to-guides/how-to-extract-data-from-forms.md).

## Extensions

Aidbox Forms provide ability to add and edit extensions that are not covered in Forms Builder. Users can edit raw Questionnaire json.

Extensions editor represents three fields for adding your own custom extension. To add extension you need follow three simple steps:

1. `URL` — write down your extension's Url;
2. `Type` — choose type of extension's value. Also you can write custom FHIRPath / FHIRQuery valueExpression just choose `Expression` type;
3. `Value` — pick a value.

{% hint style="info" %}
Known extensions can not be added
{% endhint %}

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
* User can define this value via SDCconfig and then all forms will have the set width by default. If you set the form width value in the form itself, then this value will override what is in the SDCconfig.

To ensure the form appears as intended, you can use the **Form Preview** feature to see how the form looks with the specified maximum width.
