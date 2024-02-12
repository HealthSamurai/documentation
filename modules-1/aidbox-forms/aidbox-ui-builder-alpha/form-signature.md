---
description: This page describes how to enable signatures for forms
---

# Form signature

To use signature feature, your questionnaire must include extension [signatureRequired](http://hl7.org/fhir/StructureDefinition/questionnaire-signatureRequired)

Our form renderer will add additional field to the bottom of the form where user can draw his signature.

<figure><img src="../../../.gitbook/assets/Screenshot 2024-02-12 at 19.52.35.png" alt="" width="367"><figcaption><p>Signature field </p></figcaption></figure>

### Enable signature in Form Builder

To enable signature for Questionnaire, just click on the according checkbox in form settings.

### Storing signature

Signatures are stored in QuestionnaireResponse under [`signature`](http://hl7.org/fhir/StructureDefinition/questionnaireresponse-signature) extensions.

### Validation notes

If signature is required on Questionnaire, our system will prevent completing that that form without it.

Also, according to FHIR R4, [`Signature`](https://hl7.org/fhir/datatypes.html#Signature) type that holds signature data required to fill an author of it.

Aidbox will return a warning in response of [`$populate`](../../../reference/aidbox-forms/fhir-sdc-api.md#populate-questionnaire-usdpopulate) operation, if author was not passed in context.
