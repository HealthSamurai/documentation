---
description: This page is under construction.
---

# Form Settings

## General Form Settings

In the UI builder interface, users can set general settings for the form. These settings can be accessed in two ways:

* When users navigate to the UI builder page, they are presented with options to set general settings for the form.
* Users can also access the general settings for the form by clicking on the name of the form in the form outline. This action opens a sidebar where users can modify the form's general settings.

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
* `active` - used for forms in production - you should be carefull, and do not change these forms (small changes are ok).
* `retired`- used for production retired forms - you also should be carefull about these, they are used for historical reasons

### Default Language

This is the default language of the form. Since forms can be multilingual, choosing the default language gives you the opportunity to set the translation of the form in other languages.

### Reusable Context

With this setting, you can specify how the form will be used. If you mark the form as `Component` or a `Form and Component`, then the form will be available in the add widget popup in the components tab and can be included in other forms as component.

* `Form` - This form can be used only as an _independent form_
* `Component` - This form can be used only as an _component_ (included in other forms)
* `Form or Component` - This form can be used only as an _independent form_ or be included in other forms as _component_

### Code

An identifier for this collection of questions in a particular terminology such as LOINC.  It allows linking of the complete Questionnaire resources to formal terminologies.

### Tags

### Show outline

### Population

### Additional Information

In this section, you can add additional information to the form that will be needed when publishing the form.

**Publisher** - the name of the organization or individual that published the questionnaire.

**Contact** - contact details to assist a user in finding and communicating with the publisher. It may be a web site, an email address, a telephone number, etc.

**Derived From (URL)** - the URL of a Questionnaire that this Questionnaire is based on.

**Description** - a free text natural language description of the questionnaire. This description can be used to capture details such as why the questionnaire was built, comments about misuse, instructions for clinical use and interpretation, literature references, examples from the paper world, etc.

**Copyright** - a copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.\\
