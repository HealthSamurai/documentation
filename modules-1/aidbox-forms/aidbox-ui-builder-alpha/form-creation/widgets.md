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
