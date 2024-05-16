---
description: >-
  Instructions on how to upload the FHIR Implementation Guide by specifying a
  URL in the Aidbox UI
---

# Public URL to IG Package

Aidbox allows you to upload an arbitrary FHIR Implementation Guide by referring to it by URL. The IG must be in _.targz_ format

## 1. Run Aidbox with FHIR Schema Validation Engine

{% content-ref url="../../setup.md" %}
[setup.md](../../setup.md)
{% endcontent-ref %}

## 2. Open the Aidbox UI

1. Navigate to the **FHIR Packages** page.
2. Go to the **Import FHIR Package** sidebar section.
3.  Enter the public **Package URL** via the input from the "**Provide URLs to package .targz files**" section

    _Example #1:_ [https://packages.simplifier.net/hl7.fhir.us.core/5.0.1](https://packages.simplifier.net/hl7.fhir.r4.core/4.0.1)

    _Example #2:_ [https://packages2.fhir.org/packages/hl7.fhir.us.core/5.0.1](https://packages2.fhir.org/packages/hl7.fhir.us.core/5.0.1)
4. Click on **Submit** button.



Learn about other methods for loading IGs here:

{% content-ref url="../environment-variable.md" %}
[environment-variable.md](../environment-variable.md)
{% endcontent-ref %}

{% content-ref url="ig-package-from-aidbox-registry.md" %}
[ig-package-from-aidbox-registry.md](ig-package-from-aidbox-registry.md)
{% endcontent-ref %}

{% content-ref url="local-ig-package.md" %}
[local-ig-package.md](local-ig-package.md)
{% endcontent-ref %}

{% content-ref url="../aidbox-fhir-api.md" %}
[aidbox-fhir-api.md](../aidbox-fhir-api.md)
{% endcontent-ref %}

{% content-ref url="../uploading-fhir-ig-using-uploadfig-library.md" %}
[uploading-fhir-ig-using-uploadfig-library.md](../uploading-fhir-ig-using-uploadfig-library.md)
{% endcontent-ref %}
