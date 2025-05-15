---
description: >-
  Instructions on how to upload the FHIR Implementation Guide by specifying a
  URL in the Aidbox UI
---

# Public URL to IG Package

Aidbox allows you to upload an arbitrary FHIR Implementation Guide by referring to it by URL. The IG must be in _.targz_ format

### 1. Run Aidbox with FHIR Schema Validation Engine

See [Getting Started locally guide](../../../../getting-started/run-aidbox-locally.md).

### 2. Open the Aidbox UI

1. Navigate to the **FHIR Packages** page.
2. Go to the **Import FHIR Package** sidebar section.
3.  Enter the public **Package URL** via the input from the "**Provide URLs to package .targz files**" section

    _Example #1:_ [https://packages.simplifier.net/hl7.fhir.us.core/5.0.1](https://packages.simplifier.net/hl7.fhir.us.core/5.0.1)

    _Example #2:_ [https://packages2.fhir.org/packages/hl7.fhir.us.core/5.0.1](https://packages2.fhir.org/packages/hl7.fhir.us.core/5.0.1)
4. Click **Submit** button.



Other methods for loading IGs here:

{% content-ref url="../" %}
[..](../README.md)
{% endcontent-ref %}
