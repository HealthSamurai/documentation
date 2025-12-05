---
description: Configure FHIR SDC form entry modes - sequential, prior-edit, or random - to control question presentation and navigation.
---

# Entry Mode

**Entry Mode in FHIR SDC Forms**


[Entry Mode](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-entryMode.html) defines how questions are presented and navigated in your form. 
It controls whether users see one question at a time or all at once, and whether they can modify previous answers.
This feature is particularly important when certain questions must be answered definitively before proceeding,
or when viewing later questions might influence earlier responses.



> This setting guides form rendering applications on how to display and manage user access to questions.

**Key Benefits**
- Optimized for mobile devices and small screens
- Reduces cognitive load by limiting visible content (sequential and prior-edit modes)
- Increases accuracy by focusing user attention
- Controls user navigation through the form
- Helps enforce clinical or business processes

**Usage**

EntryMode setting can be configured via form builder `>` form settings panel `>` entry mode dropdown

**Available Modes:**

- **Sequential**
- **Prior-Edit**
- **Random**

**Sequential**
- Questions appear one at a time, navigation is forward-only
- All answers are mandatory
- Can't be amended after submition
- Reduces distractions and errors by focusing on single question
- Perfect for:
  - Clinical decision support tools
  - Risk assessments where questions build on previous answers
  - Legal or regulatory forms requiring strict progression
- Ideal for mobile data collection

**Prior-Edit**
- Step-by-step presentation with ability to review/edit previous answers
- Can be amended after submition
- Maintains focus while allowing corrections
- Ideal for:
  - Medical history forms
  - Patient intake questionnaires
  - Any form where accuracy matters and users might need to correct earlier entries
- Great for mobile use with complex forms

**Random**
- All questions visible at once and can be edited in any order
- Best for:
  - General health surveys
  - Symptom checklists
  - Quality of life assessments
  - Any form where question order isn't critical
- Better suited for larger screens

> In Aidbox Forms `Random` - means show as is, because our implementaion make this a default approach. 
> But other renderers may choose other direction and `Random` entryMode foreces them to show all questions at once.

Choose based on your clinical/business workflow requirements, user needs, and target device types.

