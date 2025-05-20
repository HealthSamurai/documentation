---
description: This page is under construction.
---

# Widgets

Aidbox Forms provides a set of widgets that allow you to build comprehensive and user-friendly medical forms. Widgets are pre-defined elements that you can add to your forms to capture various types of data from users.

This section details the various widgets available in Aidbox Forms and their corresponding settings.

## Common Widget’s Settings

In the widget settings, you can define various parameters that determine its appearance and behavior.

The user has the ability to change the widget type by clicking on the "change type" button in the upper right corner of the widget settings sidebar, while some of the settings for the current widget that the user entered will be saved, and some will need to be re-set.

Here are the main settings you can configure for most widgets.

### General section

#### LinkId

An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.

It is generated automatically, but the user can change it.

#### System / Code / Display

The user can manually add several codes from different terminology systems (use 'add code' option). Or import this data from terminologies (use 'import code' option).

`Code` - a terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).

`System` - identity of the terminology system.

`Display` - representation defined by the system

#### Text

The Text field is used to define content for sections, questions, or display items within a form.

#### **Short Text**

The Short Text field provides an alternative, abbreviated version of the text. It is particularly useful when rendering forms on smaller screens, such as mobile devices, where space is limited.

#### Size

The Size parameter allows user to define the width of a widget and arrange multiple widgets horizontally within a form.

* The form layout is based on a 12-column grid system.
* By setting the size of a widget:
  * Maximum Size (12 columns): The widget will span the entire width of the form (by default)
  * Partial Size:
    * If one widget is set to a width of 6 columns and another to 6 columns, both widgets will be displayed side by side on the same line.
    * Similarly, setting widths like 4, 4, and 4 will arrange three widgets horizontally in a row.

#### Hidden, Required, Read Only, and Repeats Settings

* `Hidden:` Hides the form item from user view while still allowing it to be used internally or for conditional logic.
* `Required:` Ensures the user must provide input for the item before submitting the form.
* `Read Only:` Displays the form item as non-editable, allowing users to view but not modify its value.
* `Repeats:` Enables the user to dynamically add multiple instances of the same form item as needed.

{% hint style="info" %}
`Repeats` for Checkbox-list and Choice widgets have different behavior! When `Repeats` are enabled, it makes it possible to perform multi-choice actions within the widget.
{% endhint %}

#### Collapsible

Collapsible section is supported to enhance the usability and navigation of long questionnaires. This feature allows the child items of a group or question item to be displayed in a collapsible form, letting users toggle between showing and hiding nested items.

There are options:

1. `Not Collapsible`: The section is always fully expanded, and its child items are visible.
2. `Collapsible (Open by Default)`: The section is collapsible and starts in an expanded state. Users can collapse it if desired.
3. `Collapsible (Closed by Default)`: The section is collapsible and starts in a collapsed state. Users can expand it to view the child items.

User can set the desired collapsibility option for specific sections or items within the questionnaire.

### Media section

#### **Image & Video**

Allows you to add an image or video next to a form item, enhancing its visual appeal or providing additional context. The user needs to provide a publicly available URL for the image or video.

#### **Tooltip**

Displays additional information when the user clicks the question mark icon in the top-right corner of a field, offering guidance or clarification.

#### Support Link

The Support Link option allows you to associate an external or internal link with a question, group, or display item within a form. This is particularly useful for providing users with additional descriptive details or reference material.

* When a support link is added, an **icon** appears immediately.
* Users can click on the icon to navigate to the linked resource, typically displayed in a separate page or browser tab.

Use Cases:

* Providing contextual help or instructions for complex questions.
* Linking to guidelines, manuals, or regulatory references.
* Adding reference material for specific sections or items in the form.

This feature enhances the usability of forms by ensuring users have access to additional information without cluttering the main form interface.

### Attributes section

Each widget in Aidbox Forms has its own set of customisable attributes that define its behavior, appearance, and functionality.

### Rules section

#### EnableWhen rule

EnableWhen option controls whether an item should be 'enabled' or not, but can handle more sophisticated circumstances. For example, it is possible to calculate a score based on the answer to several questions and then enable other questions based on the score. It's also possible to enable or disable questions based on data passed in as context or retrieved from queries.

Options for Setting EnableWhen Rules:

* `conditions` - define conditions using the "enableWhen" constructor.
* `expression` - use [FHIRPath](https://hl7.org/fhirpath/) expressions or [the FHIRPath Editor](fhirpath-editor.md) to describe the desired behavior.

{% hint style="info" %}
Be careful when using existence logic for EnableWhen rules. For example, the checkbox widget has `exists = false` when untouched and `exists = true` if the checkbox has been checked or unchecked. Use equality conditions instead.
{% endhint %}

#### Calculation rule

Calculated expression allows answers to questions to be calculated based on answers to other questions. For example, the determination of a score.

This expression will be most used for displaying scores, but can be used for any calculated element - patient age (based on current date and birth date), BMI (based on recent weight and height), estimated cost (based on selected items and quantities), etc.

How to use Calculated expression:

* Create custom [FHIR Path](https://hl7.org/fhirpath/) expressions in the code editor with autocomplete functionality or in [the FHIRPath Editor](fhirpath-editor.md) by clicking on `Visual mode` button .
* Utilize predefined templates under the "question" icon.

Named expressions can be used in data calculation process.\
They are useful for:

* splitting one complex calculatation to smaller ones
* storing intermediate calculations
* sharing common pre-calculation with other expressions

Named expressions will be available in current widget and it's child items (if they)\
Expressions can be referenced with %expr-name literal.

Example:

* Define expression with name `first-name` = `'%resource.repeat(item).where(linkId='first-name').answer.valueString'`
* Use defined variable via literal %first-name : `%first-name + %last-name`

### Data extraction section

Aidbox Forms supports three options for data extraction: **Observation-Based**, **Definition-Based**, and **Template-Based**. These options provide flexibility in how form data is transformed into different FHIR resources.

**Observation-based extraction**

&#x20;It allows data collected through a Questionnaire Response (QR) to be extracted and stored in FHIR Observation resources.

{% hint style="info" %}
Ensure that each item has both a **code** and a **system** defined.
{% endhint %}

**Current Logic:**

* A new Observation is created for each Questionnaire Response instance at the time of data extraction.
* If a QR is amended (modified after submission), the existing Observations linked to that QR are updated accordingly.

For detailed instructions and an example of extracting data into an Observation resource, visit the [Observation-Based Extraction Guide](how-to-guides/how-to-extract-data-from-forms.md#observation-based-extraction).



**Definition-based extraction**

It maps form data dynamically to the corresponding FHIR resources. When configuring extraction, you must specify a FHIR Path expression in the Path field. This expression defines the resource and field where the data should be extracted.

**Current Logic:**

Two Options for Resource Extraction:

* **New Resource**: If "new resource" is selected, a new resource is always created during extraction.Comment
* **Existing Resource:** If "existing resource" is selected, the system includes the `questionnaire-itemExtractionContext` extension. This extension can be added either at the root of the Questionnaire or at any item level. It identifies the resource that serves as the context for extraction.Comment

Using `itemExtractionContext`:

* Empty `itemExtractionContext`: When the `itemExtractionContext` is empty, the Questionnaire is used to create a new resource.
* Populated `itemExtractionContext`: If the `itemExtractionContext` contains a resource (or set of resources), the Questionnaire updates the existing resource.

For detailed instructions and an example of extracting data into a Patient resource, visit the [Definition-Based Extraction Guide](how-to-guides/how-to-extract-data-from-forms.md#definition-based-extraction).

#### Template-based extraction

It uses predefined templates embedded in the Questionnaire to extract answers into FHIR resource&#x73;**,** along with all the "boiler-plate" content for the resource that is to be extracted..

{% hint style="warning" %}
Template-based extraction is currently in **preview.** Not all functionality is available yet and may be subject to change.
{% endhint %}

#### &#x20;  **Current Logic:**

* Templates are included in the Questionnaire as **contained resources** and referenced using the [`sdc-questionnaire-templateExtract`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-templateExtract.html) or [`sdc-questionnaire-templateExtractBundle`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-templateExtractBundle.html) extensions.&#x20;

{% hint style="warning" %}
The `templateExtractBundle` extension is currently in development.
{% endhint %}

* There are two options where you can place the `templateExtract` extension:
  * **At the root level of the Questionnaire:** Used when you want to extract a single resource based on multiple answers from the form.
  * **At the item level:** Used when you want to extract data from specific questions or create a separate resource per repeated item.
* Data is mapped from answers in the QuestionnaireResponse into the template using FHIRPath expressions.
* Repeating items:
  * When `templateExtract` is placed on a repeatable item, the engine creates a new resource for each answer.
  * When extracting to a field that is an array (e.g., `Patient.address`), repeated answers can be added as multiple entries in the same resource.
* Use of the [`allocateId`](https://build.fhir.org/ig/HL7/sdc/StructureDefinition-sdc-questionnaire-extractAllocateId.html) extension allows generation of unique IDs and referencing between related resources.

{% hint style="warning" %}
The `allocateId` extensions are currently in development.
{% endhint %}

* If the template includes expressions that return no result, the corresponding field or entry is automatically excluded from the output.

**Steps to create a resource template:**

* Select a predefined resource template from existing ones, or create a new template.
* At this moment, the Resource Template Editor will be opened.
* Set up the template name.
* Select the resource type of the template you want to create (e.g., Patient, Observation).
* Search and prefill the fields that you need in this resource template.
* Test and debug the extraction using the debug console. For this purpose, enter test data in the form.
* Close the Resource Template Editor panel if everything is correct, or discard changes.

For detailed instructions and examples of using templates for data extraction, visit the Template-Based Extraction Guide.

### Population

Aidbox Forms provide three options for pre-filling the field:

1. **Initial Value**: For all fields except groups and display widgets, the user can set the default value. If the user does not change the value, this is what will appear in the completed QuestionnaireResponse.
2. **Observation:** Used to pre-fill a field with values ​​that are stored in the database in the Observation resources. To do this, the user needs to select the time period during which these observations could have been made. The mechanism is described in more detail in the [FHIR SDC specification.](https://build.fhir.org/ig/HL7/sdc/populate.html#observation-based-population)
3. **Expression:** This approach to population is more generic. It supports retrieving data from any queryable FHIR resources available in the database. Those queries can be based on the context in which the QuestionnaireResponse is being generated and/or on the results of other queries. The user needs to use [FHIRPath](https://hl7.org/fhirpath/) for this purpose or [The FHIRPath Editor](fhirpath-editor.md). For more detail go to the [FHIR SDC specification.](https://build.fhir.org/ig/HL7/sdc/populate.html#expression-based-population)

## Description of widgets

| Widget                                      | Description                                                                                                                                                                                                                                                                               | FHIR / Custom |
| ------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------- |
| Group                                       | An item with no direct answer but should have at least one child item.                                                                                                                                                                                                                    | FHIR          |
| Choice matrix                               | Questions within the group are rows in the table with possible answers as columns. Used for 'choice' questions.                                                                                                                                                                           | FHIR          |
| Grid                                        | Child items of type='group' within the a 'grid' group are rows, and questions beneath the 'row' groups are organized as columns in the grid. The grid might be fully populated, but could be sparse. Questions may support different data types and/or different answer choices.          | FHIR          |
| Group table                                 | Questions within the group are rows in the table with possible answers as columns.                                                                                                                                                                                                        | FHIR          |
| Pages                                       | Indicates that the content within the group should appear as a logical "page" when rendering the form, such that all enabled items within the page are displayed at once, but items in subsequent groups are not displayed until the user indicates a desire to move to the 'next' group. | FHIR          |
| Text                                        | Question with a short (few words to a short sentence) free-text entry answer                                                                                                                                                                                                              | FHIR          |
| Textarea                                    | Question with a long (potentially multi-paragraph) free-text entry answer                                                                                                                                                                                                                 | FHIR          |
| URL                                         | Question with a URL (website, FTP site, etc.) answer                                                                                                                                                                                                                                      | FHIR          |
| Display                                     | Text for display that will not capture an answer or have child items.                                                                                                                                                                                                                     | FHIR          |
| Integer                                     | Question with an integer answer                                                                                                                                                                                                                                                           | FHIR          |
| [Decimal](widgets.md#decimal)               | Question with is a real number answer                                                                                                                                                                                                                                                     | FHIR          |
| Slider                                      | A control where an axis is displayed between the high and low values and the control can be visually manipulated to select a value anywhere on the axis.                                                                                                                                  | FHIR          |
| Quantity                                    | Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer.                                                                                                                                                                         | FHIR          |
| Date                                        | Question with a date answer                                                                                                                                                                                                                                                               | FHIR          |
| Time                                        | Question with a time (hour:minute:second) answer independent of date.                                                                                                                                                                                                                     | FHIR          |
| DateTime                                    | Question with a date and time answer                                                                                                                                                                                                                                                      | FHIR          |
| [Choice](widgets.md#choice)                 | Question with a Coding drawn from a list of possible answers (specified in either the answerOption property, or via the valueset referenced in the answerValueSet property) as an answer                                                                                                  | FHIR          |
| [Open Choice](widgets.md#open-choice)       | Answer is a Coding drawn from a list of possible answers (as with the choice type) or a free-text entry in a string                                                                                                                                                                       | FHIR          |
| [Checkbox](widgets.md#checkbox)             | Question with a yes/no answer                                                                                                                                                                                                                                                             | FHIR          |
| [Attachment](widgets.md#attachment)         | Question with binary content such as an image, PDF, etc. as an answer                                                                                                                                                                                                                     | FHIR          |
| Signature                                   | A control for capturing a signature.                                                                                                                                                                                                                                                      | Custom        |
| [Annotation Pad](widgets.md#annotation-pad) | A control for capturing visual information, sketches, or handwritten notes that cannot be easily captured through text inputs.                                                                                                                                                            | Custom        |
| [Speech to text](widgets.md#speech-to-text) | A control to input text by speaking, which is then automatically transcribed into the form.                                                                                                                                                                                               | Custom        |
| Reference                                   | Question with a reference to another resource (practitioner, organization, etc.) as an answer                                                                                                                                                                                             | FHIR          |
| [Radio Button](widgets.md#radio-button)     | A control where choices are listed with a button beside them. The button can be toggled to select or de-select a given choice. Selecting one item deselects all others.                                                                                                                   | FHIR          |
| [Checkbox List](widgets.md#checkbox-list)   | A control where choices are listed with a box beside them. The box can be toggled to select or de-select a given choice with multiple selections.                                                                                                                                         | FHIR          |

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
* **Use ValueSet**: Use predefined options in ValueSet
  * **Use external terminology server**: You can specify external terminology server which will be used to `$expand` ValueSet options
  * **Expansion parameters**: Optionally, you can set additional parameters for `$expand` operation. For example, you can choose what CodeSystem version need to use.

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

{% hint style="info" %}
Checkboxes have an indeterminate state when the widget is untouched.
{% endhint %}

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

### Attachment

The Attachment widget allows users to upload files as part of their form submission. This widget is particularly useful for scenarios where users need to upload supporting documents, images, or other types of files.

**Settings:**

* **Allowed file-types**: You can limit the types of files that can be uploaded (image, audio, video, pdf, doc, csv etc or specify other).
* **Max File Size**: Set the maximum file size (in kilobytes) allowed for upload.

**Additional Functionality:**

* **File Preview**: When using the form, the attached file can be viewed by opening it in a separate page. To do this, the user needs to click on the eye icon.
* Supported file types for preview in browsers (Chrome , Firefox, Safari):
  * **Images**: PNG, AVIF, GIF, JPEG, SVG, WEBP, BMP, ICO
  * **Video**: MP4, WebM, Ogg
  * **Audio**: MP3, Ogg, Wav
  * **PDF**
  * DOC, CSV and others - on request

The attached file will be saved as base64 encoded attachment in QuestionnaireResponse.

### Decimal

The Decimal widget is used for capturing rational numbers that have a decimal representation. It is particularly useful for fields requiring numeric input with decimal precision, such as measurements, scores.

It can be used in calculation fields where the result of an expression or formula is displayed as a decimal. For example, it can be used for calculated values like BMI, age, ensuring that results are presented with the correct level of precision.

**Settings:**

* **Decimal Representation**: The user can input rational numbers, supporting values like 12,34. The decimal point is not supported as a separator.
* **Max Decimal Places**: The user can set a **maximum number of decimal places** to restrict the number of digits after the decimal point. This serves as a form validation feature, warning users if they input a value with more decimal places than allowed. For example, if the maximum decimal places are set to 2, an error message will appear if the user enters a value like 3,14159 with the message "Answer exceeds maximum decimal places: 2."
