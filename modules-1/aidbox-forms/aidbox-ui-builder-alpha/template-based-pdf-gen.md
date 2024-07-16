---
description: Custom HTML generation for Questionnaire and QuestionnaireResponse using the $render operation.
---

# WORK IN PROGRESS Custom HTML Generation for Questionnaire and QuestionnaireResponse

## Overview

This feature introduces the capability to generate custom HTML for Questionnaire and QuestionnaireResponse resources. It is facilitated through the `$render` operation. This document details how to use the `$render` operation, including making calls for specific Questionnaire and QuestionnaireResponse resources and the required request body format.

## Implementation Details

The `$render` operation allows for the dynamic generation of custom HTML based on the specified `SDCPrintTemplate` resource. This operation can be applied to both Questionnaire and QuestionnaireResponse resources.

## Using the $render Operation

### Making a Call for a Questionnaire

To render custom HTML for a Questionnaire, use the following endpoint:

```yaml
POST [base]/Questionnaire/[questionnaire-id]/$render
Accept: text/yaml
Content-Type: text/yaml

parameter: 
  - name: 'template-id'
    value: 
      string: 'default-qr-template'
  - name: 'repeated-items-count'
    value: 
      integer: 5
```
The 'template-id' parameter specifies the ID of the SDCPrintTemplate resource to be used for rendering. `repeated-items-count` specifies the number of repetitions for fields marked as `repeats`. If this parameter is not provided, the default value is 1.

### Making a Call for a QuestionnaireResponse

To render custom HTML for a QuestionnaireResponse, use the following endpoint:

```yaml
POST [base]/QuestionnaireResponse/[questionnaire-response-id]/$render
Accept: text/yaml
Content-Type: text/yaml

parameter: 
  - name: 'template-id'
    value: 
      string: 'default-qr-template'
```

## Predefined Template

There exists a predefined template with the ID `default-qr-template` which serves as a universal template for both Questionnaire and QuestionnaireResponse. You can use it as a sample for implementing your own templates. This template renders all types of widgets in the simplest form of presentation with minimal styles and can be used for printing documents.

## Creating a Custom Template

Creating a custom template for rendering Questionnaire or QuestionnaireResponse resources into HTML involves defining the structure and presentation of the data. Here's a guide to creating your own:

### Template Structure

A template is an HTML document with placeholders for data, conditional logic and loops to handle different types of questions and responses. It uses a combination of HTML, CSS and a templating language for dynamic content rendering.

### Key Components

1. **HTML Structure**: Define the overall layout of the document, including headers, tables, and other HTML elements.
2. **CSS**: Utilize CSS for responsive design and styling. For example, Tailwind cab be included via CDN in the template for ease of use, like in idefault template.
3. **Conditional Logic**: Use conditional statements to render different HTML based on the item properties. This is crucial for handling various data types and controls like integers, decimals, strings, dates, and custom widgets.
4. **Loops and Iteration**: Implement loops to handle repeating items or groups of questions within the Questionnaire or QuestionnaireResponse.

### Render Context

The input to the template engine includes a context that contains the following parameters:

1. **items** - A vector that contains all the widgets. It is important to note that the widgets are in the same order as they appear in the form. The structure is almost flat, meaning that widgets do not contain children, with the exceptions being Choice Matrix, Grid, and Group Table widgets.

2. **title** - The title of the Questionnaire. For QuestionnaireResponse, the title of the associated Questionnaire is used.

3. **repeatedCount** - The specified number of repetitions for widgets that have the `repeats` property set.

4. **is-q** - A boolean value that is true if $render was called for a Questionnaire resource.

5. **is-qr** - A boolean value that is true if $render was called for a QuestionnaireResponse resource.

#### Item Structure

The items list elements have the following structure:

path - A list that contains the path to the element in the form. It sequentially lists linkIds mixed with indices to reach the required element in the original form structure.

linkId - A unique identifier for an element on the form. Using it in the render logic is very convenient if you want to create a custom design for a specific form, where behavior can be chosen for each field based on the linkId. This field is also important for working with widgets in QuestionnaireResponse that are marked as repeats.

item - The content of the widget, which corresponds to what is described at https://hl7.org/fhir/questionnaireresponse-definitions.html#QuestionnaireResponse.item and https://hl7.org/fhir/questionnaire-definitions.html#Questionnaire.item in FHIR. In the case of Choice Matrix, Grid, and Group Table widgets, it also contains a list of child widgets, which has a structure similar to what you are currently reading.

extension - The element's extension, which may contain information affecting how the widget should be rendered.

idx - The ordinal number of the repeated question, if it is marked as repeated.

item-control - This field allows determining whether the widget is a Choice Matrix, Grid, Group Table, or Signature.

visual-level - Indicates the level of visual nesting in our Form Renderer, starting from 0.

text - The title of the widget.

## Troubleshooting

### Common Issues

- **Incorrect Template, Questionnaire and QuestionnaireResponse IDs**: Verify that all IDs provided in the request are correct. If any of the IDs cannot be found, either because they do not exist or belong to another tenant, the operation will return a 404 code with an OperationOutcome resource describing which of the resources could not be retrieved for rendering.

## FAQs

**Q: Can I use the same template for both Questionnaire and QuestionnaireResponse?**
A: Yes, the same `SDCPrintTemplate` can be used for rendering both Questionnaire and QuestionnaireResponse resources, provided it is designed to accommodate the structure of both resource types. However, if you want to create your own template that is suitable for both Questionnaire and QuestionnaireResponse, you will need to consider the differences in the FHIR standard between these entities. 

To differentiate between the resource types in the template, you can use two boolean values in the context: `is-q` and `is-qr`. These values can be used to conditionally render specific sections or elements based on the resource type being processed.

