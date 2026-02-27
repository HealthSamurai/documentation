---
description: This article outlines the the UI Builder Interface
---

# UI Builder Interface

## UI Builder interface overview

When creating a form in the UI builder, the interface includes the following components:

1. **Work Area ( left Side ):**
   * Displays the form outline, showing the structure of the form and the properties of the form and its widgets and components.
   * Users can add, remove, or modify form widgets and components.
2. **Form Preview ( right Side ):**
   * Shows the form itself, reflecting changes made in the form outline in real-time.
   * Users can test this form by filling it out directly.
3. **Debug Console ( at the bottom** ) **:**
   * Debug console allows users to view the Questionnaire resource. Each form is presented as a Questionnaire resource and saved in a database.
   * Users can view the QuestionnaireResponse resource. Data from the form is saved in a QuestionnaireResponse resource in a database.
   * Pre-filling the form with existing data is possible for testing purposes on Population tab.
   * Users can view how data will be extracted to other FHIR resources on Extraction tab.
   * Named Expressions tab displays all variables that are used in the form.
4. **Toolbar ( at the top** )**:**
   * The toolbar contains buttons for various actions.
   * Users can view a preview of the form, set a theme for the form, save the form, and access additional actions.
   *   The toolbar provides tools to help visualize how your form will be displayed across various devices. You can select one of the **available device options** such as desktop, tablet, or mobile.

       Alternatively, you can manually set the **page width** to preview how the form adapts to different dimensions.

This interface provides users with the tools needed to create, test, and manage forms effectively.

## Multilingual UI Builder Interface

If you need support for another language in the UI builder interface or if you are embedding the UI Builder into your application that is used by users from different countries, Formbox provides support for multilingual interface.

To enable this:

1. Click on the Action bar (three dots) in the UI builder and select the "Settings" option.
2. After appearing the settings page, select the language for the UI builder interface.

## List of supported languages

* Chinese `zh`
* Chinese (Hong Kong) `zh-HK`
* Chinese (Taiwan) `zh-TW`
* Croatian `hr`
* Czech `cs`
* Danish `da`
* Dutch `nl`
* Dutch (Belgium) `nl-BE`
* English `en`
* English (US) `en-US`
* English (UK) `en-GB`
* English (Australia) `en-AU`
* English (Canada) `en-CA`
* Finnish `fi`
* French `fr`
* French (Canada) `fr-CA`
* French (Belgium) `fr-BE`
* French (Switzerland) `fr-CH`
* German `de`
* German (Austria) `de-AT`
* German (Switzerland) `de-CH`
* Hungarian `hu`
* Italian `it`
* Italian (Switzerland) `it-CH`
* Japanese `ja`
* Korean `ko`
* Polish `pl`
* Russian `ru`
* Spanish `es`
* Spanish (Mexico) `es-MX`
* Spanish (Argentina) `es-AR`
* Spanish (Colombia) `es-CO`
* Spanish (Spain) `es-ES`

Additional languages can be added upon request.
