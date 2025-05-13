---
description: >-
  Customizable PDF generation for Questionnaire and QuestionnaireResponse using
  the $render operation.
---

# Template-based PDF generation

## Overview

This feature introduces the capability to generate custom PDF representation for Questionnaire and QuestionnaireResponse resources. It is facilitated through the `$render` operation. This document explains how to use this operation and provides examples on how to create custom print templates.

## Implementation Details

The `$render` operation allows you to select how you want your form to look in print. Different representations can be defined using SDCPrintTemplate resources. You can use the default template, or define additional ones for specific forms. The `$render` operation can be applied to both Questionnaire and QuestionnaireResponse resources.

A template is an HTML document utilizing a templating language for dynamic content rendering. It uses [Selmer](https://github.com/yogthos/Selmer) templating system.

The response of `$render` is the result of rendering the selected HTML template with the data from the specified Questionnaire or QuestionnaireResponse. To actually convert it to PDF, you could use `window.print()` on the frontend, or a library like [Puppeteer](https://pptr.dev/guides/pdf-generation) on the backend.

## Using the $render Operation

### Questionnaire

To render a Questionnaire, use the following endpoint:

```yaml
POST /Questionnaire/[questionnaire-id]/$render
Accept: text/html
Content-Type: text/yaml

resourceType: Parameters
parameter: 
  - name: 'template-id'
    value: 
      string: 'default-template'
  - name: 'repeated-items-count'
    value: 
      integer: 2
```

The 'template-id' parameter specifies the id of the SDCPrintTemplate resource to be used for rendering. `repeated-items-count` specifies the number of repetitions for fields marked as `repeats`. If this parameter is not provided, the default value is 1.

### QuestionnaireResponse

To render a QuestionnaireResponse, use the following endpoint:

```yaml
POST /QuestionnaireResponse/[questionnaire-response-id]/$render
Accept: text/html
Content-Type: text/yaml

resourceType: Parameters
parameter: 
  - name: 'template-id'
    value: 
      string: 'default-template'
```

## Default Template

Aidbox comes with a predefined template `default-template` which serves as a universal template for both Questionnaire and QuestionnaireResponse. This template renders all types of widgets in the simplest form of presentation with minimal styles. You can use it as a sample for implementing your own templates.

## Creating a Custom Template

Here's how to create your own custom print template:

Let's consider a basic example for clarity: there is a form with three fields (see Figure 1). The task is to create a print version where a table will be generated. In the left column of the table, the title and response from the textarea field will be placed, in the right column - the response from the datetime field, and the field with the signature should not be displayed at all.

{% tabs %}

{% tab title="Questionnaire" %}
```yaml
url: http://forms.aidbox.io/questionnaire/test-pdf
item:
  - text: Textarea at left side
    type: text
    linkId: textarea
  - text: Date at right side
    type: date
    linkId: date
  - text: Signature
    type: attachment
    linkId: signature
    extension:
      - url: http://hl7.org/fhir/StructureDefinition/questionnaire-itemControl
        value:
          CodeableConcept:
            coding:
              - code: signature
                system: http://aidbox.io/questionnaire-itemControl
title: Test pdf
status: draft
id: >-
  cc5cc148-a051-42d0-a7f2-275f09566593
resourceType: Questionnaire
```
{% endtab %}

{% tab title="QuestionnaireResponse" %}
```yaml
item:
  - text: Textarea at left side
    answer:
      - value:
          string: >-
            As they rounded a bend in the path that ran beside the river, Lara
            recognized the silhouette of a fig tree atop a nearby hill. The
            weather was hot and the days were long.
    linkId: textarea
  - text: Date at right side
    answer:
      - value:
          date: '1066-06-14'
    linkId: date
  - text: Signature
    linkId: signature
status: in-progress
questionnaire: http://forms.aidbox.io/questionnaire/test-pdf
id: >-
  c9dacb6d-b6de-4250-8e08-a77f1d34a119
resourceType: QuestionnaireResponse
```
{% endtab %}
{% endtabs %}

Let's create a custom print template with id `test`. When writing a template, we can use variables from the [render context](template-based-pdf-generation.md#template-render-context). In the template we will implement a loop where we will check the linkId of each widget and depending on that, add a specific HTML fragment. For the Signature widget, we do not specify any condition at all, as it should not be displayed.

```yaml
PUT /SDCPrintTemplate/test-template
Accept: text/yaml
Content-Type: text/yaml

content: |
  <!DOCTYPE html>
  <html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Example for pdf</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
      li {
          list-style-type: disc;
          list-style-position: inside;
        }
    </style>
  </head>

  <body class="p-16 text-sm flex justify-center print:p-0">
    <article class="max-w-screen-lg w-full">
      <h1 class="text-2xl font-semibold mb-5 text-center"> Example for pdf </h1>
      <table class="border-collapse w-full">
        <tr class="break-inside-avoid">
          
{% raw %}
{% for item in items %}
            {% if item.linkId = "textarea" %}
              <td class="border border-slate-700 p-1">
                {{item.text}}: {{ item.widget/value.value.string }}              
              </td>
            {% endif %}
            {% if item.linkId = "date" %}
              <td class="border border-slate-700 min-w-40 p-1">
                {{ item.widget/value.value.date }}
              </td>
            {% endif %}
          {% endfor %}
{% endraw %}
        </tr>
      </table>
    </article>
  </body>

  </html>
```

Now let's render our form using the template we've just created:

```yaml
POST /QuestionnaireResponse/c9dacb6d-b6de-4250-8e08-a77f1d34a119/$render
Accept: text/html
Content-Type: text/yaml

resourceType: Parameters
parameter: 
  - name: 'template-id'
    value: 
      string: 'test-template'
```


### Template Render Context

When rendering a template, the template engine has the following variables in the context:

1. **items** - A vector containing all the widgets. It is important to note that the widgets are in the same order as they appear in the form. The structure is almost flat, meaning that widgets do not contain children, with the exceptions being Choice Matrix, Grid, and Group Table widgets.
2. **title** - The title of the Questionnaire. For QuestionnaireResponse, the title of the associated Questionnaire is used.
3. **repeated-count** - The specified number of repetitions for widgets that have the `repeats` property set.
4. **is-q** - A boolean value that is true if $render was called for a Questionnaire resource.
5. **is-qr** - A boolean value that is true if $render was called for a QuestionnaireResponse resource.

### Including Other Templates to Your Template

To avoid repetitions in templates, we supported a custom tag `include-resource`. Its principle of operation is similar to the Selmer built-in `include` tag, but it allows referencing templates declared as an SDCPrintTemplate resource by specifying the resource id as the tag argument. For an example of usage, consider the our `default-template`, which reuses the `default-widget` template.

## FAQ

**Q: Why am I Getting 404?** A: Verify that all IDs provided in the request are correct. That includes template, Questionnaire, and QuestionnaireResponse IDs. If any of the IDs cannot be found, either because they do not exist or belong to another [Organization](../../../access-control/authorization/scoped-api/organization-based-hierarchical-access-control.md), the operation will return 404.

**Q: Can I use the same template for both Questionnaire and QuestionnaireResponse?** A: Yes, the same SDCPrintTemplate can be used for rendering both Questionnaire and QuestionnaireResponse resources, provided it is designed to accommodate the structure of both resource types. However, if you want to create your own template that is suitable for both Questionnaire and QuestionnaireResponse, you will need to consider the differences in the FHIR standard between these resources.

To differentiate between the resource types in the template, you can use two boolean values in the context: `is-q` and `is-qr`. These values can be used to conditionally render specific sections or elements based on the resource type being processed.

**Q: What does "ERROR: template not found template-id" mean in a rendered form?** A: This message appears when you use the `include-resource` tag in your template and refer to a non-existent template. Double-check that the template with the specified template-id exists.
