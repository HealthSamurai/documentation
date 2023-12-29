---
description: This article outlines operations from the FHIR SDC Implementation Guide.
---

# FHIR SDC API

Aidbox Forms module supports FHIR SDC operations:

* [$populate](https://hl7.org/fhir/uv/sdc/OperationDefinition-Questionnaire-populate.html)  - filling out a form with existing data
* [$extract](https://hl7.org/fhir/uv/sdc/OperationDefinition-QuestionnaireResponse-extract.html) - extract data from QuestionnaireResponse to other FHIR resources
* [$expand](https://www.hl7.org/fhir/valueset-operation-expand.html) - create a simple collection of codes suitable for use for data entry or validation.





## Populate Questionnaire  - $populate

The `populate` operation generates a [QuestionnaireResponse](https://www.hl7.org/fhir/questionnaireresponse.html) based on a specific [Questionnaire](https://www.hl7.org/fhir/questionnaire.html), filling in answers to questions where possible based on information provided as part of the operation or already known by the server about the subject of the Questionnaire.

This implementation allows  the Observation based population.

## Questionnaire response extract to resources - $extract <a href="#root" id="root"></a>

The  `extract` operation takes a completed QuestionnaireResponse and converts it to a FHIR resource or Bundle of resources by using metadata embedded in the Questionnaire the QuestionnaireResponse is based on. The extracted resources might include Observations, MedicationStatements and other standard FHIR resources which can then be shared and manipulated.&#x20;

{% hint style="warning" %}
When invoking the $extract operation, care should be taken that the submitted QuestionnaireResponse is itself valid. If not, the extract operation could fail (with appropriate OperationOutcomes) or, more problematic, might succeed but provide incorrect output.
{% endhint %}

This implementation allows the [Observation based](https://hl7.org/fhir/uv/sdc/extraction.html#observation-based-extraction) extraction.



## ValueSet Expansion - $expand

Value Sets are used to define possible coded answer choices in a questionnaire.

The use of standardized codes is useful when data needs to be populated into the questionnaire or extracted from the questionnaire for other uses.

The `expand` operation expand given ValueSet in to set of concepts.

This operation is described in detail [here](../../terminology/valueset/value-set-expansion.md).
