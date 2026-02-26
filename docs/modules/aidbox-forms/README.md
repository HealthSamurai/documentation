---
description: Form building and structured data capturing with Formbox
---

# Formbox  (formerly Aidbox Forms)

{% hint style="info" %}
Formbox was previously known as _Aidbox Forms_.\
The product has been renamed to reflect updated deployment options. The functionality remains the same.
{% endhint %}

Formbox is a platform for building, managing, and running digital healthcare forms with FHIR-based structured data capture.

It follows the [**HL7 FHIR Structured Data Capture (SDC) Implementation Guide**](https://build.fhir.org/ig/HL7/sdc/en/), supporting standard Questionnaire and QuestionnaireResponse workflows, including structured data extraction and population.

### Deployment Options

Formbox can be used in two configurations.

#### 1. Formbox as a Standalone Product

Formbox can run independently as a dedicated form management platform.

In this configuration, it provides:

* No-code Form Builder&#x20;
* Form Renderer
* Form Gallery
* PDF-to-digital form conversion
* Workflow automation&#x20;
* Form and response storage
* FHIR-aligned data model
* Embedding via REST API or web components

Formbox can integrate with external FHIR servers and terminology services.

Pre-fill and data extraction workflows are supported by reading from and writing to external FHIR servers in accordance with FHIR SDC.

In this setup, SQL-on-FHIR analytics can be performed on stored QuestionnaireResponse data.

[**Getting Started (Standalone)**](getting-started-formbox.md)

#### 2. Formbox as an Aidbox Module

Formbox can also be deployed as a module of Aidbox.

In this configuration, Formbox uses Aidbox as the underlying FHIR server and provides access to full FHIR server capabilities, including:

* Native FHIR storage
* Pre-fill and data extraction workflows (data is read from and written directly to Aidbox)
* SQL-on-FHIR and advanced querying across FHIR resources
* Full FHIR API support

In this setup, SQL-on-FHIR analytics can be performed across all FHIR resources stored in Aidbox, not only QuestionnaireResponse data.

The form-building and rendering capabilities remain the same in both deployment models.\
The difference lies in where and how FHIR data is stored and processed.

[**Getting Started (Aidbox Module)**](getting-started.md)

### Additional Resources

Formbox components can also be explored independently.

#### Open-Source Formbox Renderer&#x20;

Formbox includes a headless rendering engine that supports flexible UI customization through themes and design systems.\
It can be used independently of the full platform.

The renderer is available as an open-source project:

[**GitHub repository**](https://github.com/HealthSamurai/formbox-renderer)

[**Renderer documentation**](https://healthsamurai.github.io/formbox-renderer/)

#### Public Form Builder

If you want to explore the Form Builder without installing Formbox or deploying an instance, you can use the public environment:

[**Try the Public Builder**](https://form-builder.aidbox.app/)<br>

\
<br>
