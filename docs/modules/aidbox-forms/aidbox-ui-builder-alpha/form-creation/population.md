---
description: >-
  Aidbox supports 3 options for pre-filling the fields: Default Valie,
  Observation-Based, and Expression-Based. These options provide flexibility in
  how fields can be pre-filled with data.
---

# Population

### Aidbox Forms provide three options for pre-filling the field:

1. **Default Value**: For all fields except groups and display widgets, the user can set the initial value. If the user does not change the value, this is what will appear in the completed QuestionnaireResponse.
2. **Observation:** Used to pre-fill a field with values ​​that are stored in the database in the Observation resources. To do this, the user needs to select the time period during which these observations could have been made. The mechanism is described in more detail in the [FHIR SDC specification.](https://build.fhir.org/ig/HL7/sdc/populate.html#observation-based-population)
3. **Expression:** This approach to population is more generic. It supports retrieving data from any queryable FHIR resources available in the database. Those queries can be based on the context in which the QuestionnaireResponse is being generated and/or on the results of other queries. The user needs to use [FHIRPath](https://hl7.org/fhirpath/) for this purpose or [The FHIRPath Editor](fhirpath-editor.md). For more detail go to the [FHIR SDC specification.](https://build.fhir.org/ig/HL7/sdc/populate.html#expression-based-population)
   1. FHIRPath  Expression supports [Factory API](https://build.fhir.org/fhirpath.html#factory). Populating a choice items became much more easily. Factory API also allows populating choice items that use ValueSet as answer option which was impossible before. Watch our example [how to use Factory API](how-to-guides/how-to-populate-forms-with-data.md#how-to-populate-items-with-factory-api) into forms.

