---
description: >-
  This article outlines the basic steps to start designing form in Aidbox UI
  builder in browser
---

# Aidbox UI Builder

## Overview

{% hint style="info" %}
Aidbox UI builder is the tool for creating form without a code. You just sketch out the logical structure of the form and immediately it is rendered and you can test it.
{% endhint %}

This tool is based on the [FHIR SDC Implementation Guide ](https://build.fhir.org/ig/HL7/sdc/index.html)which describes how to collect data using FHIR Questionnaire and FHIR QuestionnaireResponse resources.

In FHIR, forms are represented using the [Questionnaire](http://hl7.org/fhir/R4/questionnaire.html) resource and completed forms are represented using the [QuestionnaireResponse](http://hl7.org/fhir/R4/questionnaireresponse.html) resource. All these resources will be stored in the AIdbox FHIR Storage.

Aidbox Forms module provides available APIs:

* [FHIR API](../../../api/rest-api/fhir-search/README.md) - to interact with FHIR Questionnaire, FHIR QuestionnaireResponse and other FHIR resources.
* [FHIR SDC API](../../../reference/aidbox-forms-reference/fhir-sdc-api.md)- a set of aditional operstions to interact with form and data.

## Getting started

* Open [http://localhost:8080/ ](http://localhost:8080/), using login / passwod - admin / admin
* Go to forms by pressing the button `Aidbox Forms` (or by visiting [http://localhost:8080/ui/sdc](http://localhost:8080/ui/sdc) )
* Go to the `Forms` page
* Press on `Create Template` button
* Press on `Create in UI Builder` button

You have two options to create form:

* Create form in UI Builder (from scratch)
* Create form from FHIR Questionnaire ( Import Questionnaire)

## Create form in UI Builder from scratch

Once the UI Builder has opened, you can begin creating the form.

On the left side you sketch out the form, on the right side it is rendered for you and you can immediately test it.

After you have created and tested the form, you click the `Save Questionnaire` button and the form is saved to the FHIR Storage and is available for working with it.

Go to this [page](./form-creation/README.md) to start designing a form.

## Create form from FHIR Questionnaire (Import Quetionnaire)

If you already have ready-made FHIR Questionnaires (your own or from external sources), you can load them into the Aidbox Forms module via `POST/` operation or using Import Questionnaire button on the Form Templates page, open them in the UI Builder, customise and save them.

Go to this [page](./import-questionnaire.md) to start it.
