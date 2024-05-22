---
description: FHIR versions support
---

Aidbox Forms module supports FHIR versions 4.0.0 to 5.0.0.
FHIR version used is taken from the Aidbox. (Data can be stored only on that version)

However, you can load/import questionnaires with FHIR versions ranging from 3.0.0 to 5.x. 
Our module automatically checks the Questionnaire version and handles conversion as needed.


## FHIR Version configuration

To configure Aidbox FHIR version you can read [Requried env variables](../../../reference/configuration/environment-variables/aidbox-required-environment-variables#aidbox_fhir_version) section.


## Import/load questionnare with conversion

- `Load Questionnaire` can be done through `Form Builder UI > 3 dots menu > Load`
- `Import` can be done via - `Form Templates Page > Create template > Import Quetionnaire` 

If Questionnaire's version differs from Aidbox's - you will be notified about this and conversion will be made.

## WARN: some data may be lost due conversion

Because of versions incompatibility - some data may be stripped out.

### 5.x.x -> 4.x.x

-  `Questionnaire.copyrightLabel` 
-  `Questionnaire.versionAlgorithm[x]` 
-  `Questionnaire.item.optionsConstraint = 'optionsOrType'` 

### 3.x.x -> 4.x.x

-  `Questionnaire.item.answerOptions.[valueUri/valueAttachement]` 
