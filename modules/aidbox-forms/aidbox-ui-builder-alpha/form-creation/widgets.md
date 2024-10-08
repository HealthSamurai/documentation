---
description: This page is under construction.
---

# Widgets

Aidbox Forms provides a set of widgets that allow you to build comprehensive and user-friendly medical forms. Widgets are pre-defined elements that you can add to your forms to capture various types of data from users.

This section details the various widgets available in Aidbox Forms and their corresponding settings.

## Common Widget’s Settings

In the widget settings, you can define various parameters that determine its appearance and behavior. Here are the main settings you can configure for most widgets

### General

### Media

### Attributes

### Rule

### Data extraction

### Population

Aidbox Forms provide three options for pre-filling the field:

1. **Initial Value**: For all fields except groups and display widgets, the user can set the default value. If the user does not change the value, this is what will appear in the completed QuestionnaireResponse.
2. **Observation:** Used to pre-fill a field with values ​​that are stored in the database in the Observation resources. To do this, the user needs to select the time period during which these observations could have been made. The mechanism is described in more detail in the [FHIR SDC specification.](https://build.fhir.org/ig/HL7/sdc/populate.html#observation-based-population)
3. **Expression:** This approach to population is more generic. It supports retrieving data from any queryable FHIR resources available in the database. Those queries can be based on the context in which the QuestionnaireResponse is being generated and/or on the results of other queries. The user needs to use [FHIRPath](https://hl7.org/fhirpath/) for this purpose. For more detail go to the [FHIR SDC specification.](https://build.fhir.org/ig/HL7/sdc/populate.html#expression-based-population)

## Description of widgets

### Annotation Pad

The Annotation Pad widget allows users to draw or write annotations directly within a form. This is particularly useful for capturing visual information, sketches, or handwritten notes that cannot be easily captured through text inputs. The Annotation Pad widget provides a flexible and intuitive way to gather this type of data.

**Settings:**

* **Background image:** An image that will be displayed as the background of the annotation area.
* **Pen Color**: The color of the pen used for drawing or writing.
* **Pen Thickness**: The thickness of the pen used for drawing or writing.
* **Erase Option**: A tool for erasing parts of the drawing or writing.

The annotation will be saved as base64 encoded attachment in QuestionnaireResponse.

### Speech to text

The Speech to Text widget allows users to input text by speaking, which is then automatically transcribed into the form. This is especially useful for capturing detailed information or notes, making the form-filling process faster and more efficient.

**Settings:**

* **Language**: The language in which the speech will be recognised and transcribed. The list of supported languages: Chinese, Croatian, Czech, Danish, Dutch, English, Finnish, French, German, Hungarian, Italian, Japanese, Korean, Polish, Russian, Spanish.

### Choice

The Choice widget allows users to select one or more options from a predefined list of options and it is displayed on the form as a drop-down list.

**Settings:**

* **Multiple Selection**: An option to allow the user to select more than one choice. To enable multiple selection, need to set repeats flag.
* **Options**: A list of values that the user can choose from.
  * Each option should have a display text, code and system. The user can fill in values ​​or use the import option. In this case, the user will be given the opportunity to select a terminology server, select a valueset in it and find the needed coded value, then the code, system and display will be automatically filled in.
  * Optionally, the user can include a score to each option and show the score if needed, which will be taken into account in the calculations. To do this, need to click on the include score and show score boxes.
  * Instead of inlining options into the form, the use can use a ValueSet that is stored in the Aidbox instance. To do this, the user need to click on the ValueSet box and select the ValueSet.

### Open Choice

The Open Choice widget allows users to select from a predefined list of options or input their own custom answers. This is particularly useful in scenarios where the provided options may not cover all possible answers, giving users the flexibility to provide a more accurate response.

it is displayed on the form as a drop-down list.

**Settings:**

* **Options**: A list of values that the user can choose from.
  * Each option should have a display text, code and system. The user can fill in values ​​or use the import option. In this case, the user will be given the opportunity to select a terminology server, select a valueset in it and find the needed coded value, then the code, system and display will be automatically filled in.
  * Optionally, the user can include a score to each option and show the score if needed, which will be taken into account in the calculations. To do this, need to click on the include score and show score boxes.
  * Instead of inlining options into the form, the use can use a ValueSet that is stored in the Aidbox instance. To do this, the user need to click on the ValueSet box and select the ValueSet.
* **Open choice label:** By default the ability to choose your own option is displayed as "specify other..", but the user can redefine the label for this.
* **Multiple Selection**: Not supported yet, but can be supported on demand.

### Checkbox

The Checkbox widget allows users to toggle between two states: checked (true) and unchecked (false). This widget is ideal for scenarios where a binary choice is needed, such as accepting terms and conditions or indicating a simple yes/no answer.

**Settings:**

* **Required**: When using this flag, the initial value for the checkbox is set to false automatically. If the user does not change it when filling out the form, this value is included in the completed QuestionnaireResponse.
* **Repeats:** This option is not available for checkbox.

### Radio Button

The Radio Button widget allows users to select one option from a set of predefined choices. This widget is ideal for scenarios where only a single selection is permitted.

**Settings:**

* **Options**: A list of values that the user can choose from.
  * Each option should have a display text, code and system. The user can fill in values ​​or use the import option. In this case, the user will be given the opportunity to select a terminology server, select a valueset in it and find the needed coded value, then the code, system and display will be automatically filled in.
  * Optionally, the user can include a score to each option and show the score if needed, which will be taken into account in the calculations. To do this, need to click on the include score and show score boxes.
* **Orientation**: Display orientation of the options (vertical or horizontal).
* **Column Count**: The number of columns used to display the options.

### Checkbox List

The Checkbox List widget allows users to select multiple options from a predefined list. This widget is ideal for scenarios where multiple selections are permitted or required.

**Settings:**

* **Options**: A list of values that the user can choose from.
  * Each option should have a display text, code and system. The user can fill in values ​​or use the import option. In this case, the user will be given the opportunity to select a terminology server, select a valueset in it and find the needed coded value, then the code, system and display will be automatically filled in.
  * Optionally, the user can include a score to each option and show the score if needed, which will be taken into account in the calculations. To do this, need to click on the include score and show score boxes.
  * Instead of inlining options into the form, the use can use a ValueSet that is stored in the Aidbox instance. To do this, the user need to click on the ValueSet box and select the ValueSet.
* **Orientation**: Display orientation of the options (vertical or horizontal).
* **Column Count**: The number of columns used to display the options.
* **Multiple Selection**: An option to allow the user to select more than one choice. To enable multiple selection, need to set repeats flag, by default, this flag is set.



### Attachment&#x20;

The Attachment widget allows users to upload files as part of their form submission. This widget is particularly useful for scenarios where users need to upload supporting documents, images, or other types of files.

**Settings:**

* **Allowed file-types**: You can limit the types of files that can be uploaded (image, audio, video, pdf, doc, csv etc or specify other).
* **Max File Size**: Set the maximum file size (in kilobytes) allowed for upload.

**Additional Functionality:**

* **File Preview**: When using the form, the attached file can be viewed by opening it in a separate page. To do this, the user needs to click on the eye icon.

The attached file will be saved as base64 encoded attachment in QuestionnaireResponse.
