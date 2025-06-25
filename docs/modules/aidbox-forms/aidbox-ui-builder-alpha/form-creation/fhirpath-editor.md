---
description: >-
  The article describes how to write FhirPath expressions for calculations,
  enable-when logic and population using the FHIRPath visual editor
---

# FHIRPath Expressions with visual  editor

The **FHIRPath Expression visual editor** is a tool designed for non-technical users working with electronic medical forms based on the FHIR Questionnaire resource. It offers a **low-code environment** for creating, editing, and testing FHIRPath expressions that support dynamic behavior, calculations, data population, and named expressions within forms.

**Key Use Cases:**

* Enable or disable fields dynamically
* Calculate values (e.g., scores, age, BMI)
* Pre-fill form fields using existing data
* Define and reuse named expressions

## How to Use the FHIRPath Visual Editor

**1. Create an Expression**

After creating the form and adding a set of fields:

* Navigate to the Rules section of the field where you want to apply logic.
* Select one of the expressions: Enable-when, Calculated, or Population.
* Click `Visual Mode` to open the FHIRPath Expression Editor.
* Set any variables (named expressions) you want to use in the expression.
* Write your final FHIRPath expression using the editor interface.

**2. Test and Debug an Expression**

After writing your expression:

* Fill out the form as a user would.
* Open the Debug Console to test and debug how your expression behaves with actual data.
* Close the FHIRPath Editor panel when you're finished. Any expressions you've written are saved automatically into the Questionnaire resource.

#### Validation and Error Handling

* Automatic Validation: FHIRPath expressions are validated in real time.
* Error Highlighting: If there are issues with syntax or logic, errors will be highlighted directly in the editor for easy correction.
