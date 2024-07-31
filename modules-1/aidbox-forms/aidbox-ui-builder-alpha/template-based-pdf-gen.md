---
description: >-
  Customizable PDF generation for Questionnaire and QuestionnaireResponse using the
  $render operation.
---

# Template-based PDF generation

{% hint style="danger" %}
This page is under construction.
{% endhint %}

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
POST [base]/Questionnaire/[questionnaire-id]/$render
Accept: text/html
Content-Type: text/yaml

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
POST [base]/QuestionnaireResponse/[questionnaire-response-id]/$render
Accept: text/html
Content-Type: text/yaml

parameter: 
  - name: 'template-id'
    value: 
      string: 'default-template'
```

## Default Template

Aidbox comes with a predefined template `default-template` which serves as a universal template for both Questionnaire and QuestionnaireResponse. This template renders all types of widgets in the simplest form of presentation with minimal styles. You can use it as a sample for implementing your own templates.

## Creating a Custom Template

Here's how to create your own custom print template:

TODO:
1. write HTML template with Selmer, use linkIDs
2. POST to SDCPrintTemplate
3. $render with custom template-id

### Template Render Context

When rendering a template, the template engine has the following variables in the context:

1. **items** - A vector containing all the widgets. It is important to note that the widgets are in the same order as they appear in the form. The structure is almost flat, meaning that widgets do not contain children, with the exceptions being Choice Matrix, Grid, and Group Table widgets.
2. **title** - The title of the Questionnaire. For QuestionnaireResponse, the title of the associated Questionnaire is used.
3. **repeatedCount** - The specified number of repetitions for widgets that have the `repeats` property set.
4. **is-q** - A boolean value that is true if $render was called for a Questionnaire resource.
5. **is-qr** - A boolean value that is true if $render was called for a QuestionnaireResponse resource.

### Including Other Templates to Your Template

TODO: include-resource

## FAQ

**Q: Why am I Getting 404?** A: Verify that all IDs provided in the request are correct. That includes template, Questionnaire, and QuestionnaireResponse IDs. If any of the IDs cannot be found, either because they do not exist or belong to another [Organization](../../security-and-access-control/multitenancy/organization-based-hierarchical-access-control.md), the operation will return 404.

**Q: Can I use the same template for both Questionnaire and QuestionnaireResponse?** A: Yes, the same SDCPrintTemplate can be used for rendering both Questionnaire and QuestionnaireResponse resources, provided it is designed to accommodate the structure of both resource types. However, if you want to create your own template that is suitable for both Questionnaire and QuestionnaireResponse, you will need to consider the differences in the FHIR standard between these resources.

To differentiate between the resource types in the template, you can use two boolean values in the context: `is-q` and `is-qr`. These values can be used to conditionally render specific sections or elements based on the resource type being processed.
