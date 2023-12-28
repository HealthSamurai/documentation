---
description: >-
  This article outlines the basic steps to start designing form in Aidbox UI
  builder in browser
---

# Aidbox UI Builder (alpha)

## Overview

{% hint style="info" %}
Aidbox UI builder is the tool for creating form without a code. You just sketch out the logical structure of the form and immediately it is rendered and you can test it.
{% endhint %}

This tool is based on the [FHIR SDC Implementation Guide ](https://build.fhir.org/ig/HL7/sdc/index.html)which describes how to collect data using FHIR Questionnaire and FHIR QuestionnaireResponse resources.

In FHIR, forms are represented using the [Questionnaire](http://hl7.org/fhir/R4/questionnaire.html) resource and completed forms are represented using the [QuestionnaireResponse](http://hl7.org/fhir/R4/questionnaireresponse.html) resource. All these resources will be stored in the AIdbox FHIR Storage.

Aidbox Forms module provides:

* [FHIR API](../../api-1/fhir-api/) - to interact with FHIR Questionnaire, FHIR QuestionnaireResponse and other FHIR resources.
* [FHIR SDC API ](../../reference/aidbox-forms/fhir-sdc-api.md)- a set of aditional operstions to interact with form and data.

