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

### Status



### Code

An identifier for this collection of questions in a particular terminology such as LOINC.  It allows linking of the complete Questionnaire resources to formal terminologies.

### Launch Context

### Additional Information

In this section, you can add additional information to the form that will be needed when publishing the form.

**Publisher** - the name of the organization or individual that published the questionnaire.

**Contact** - contact details to assist a user in finding and communicating with the publisher. It may be a web site, an email address, a telephone number, etc.

**Derived From (URL)** - the URL of a Questionnaire that this Questionnaire is based on.

**Description** - a free text natural language description of the questionnaire. This description can be used to capture details such as why the questionnaire was built, comments about misuse, instructions for clinical use and interpretation, literature references, examples from the paper world, etc.

**Copyright** - a copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.\\
