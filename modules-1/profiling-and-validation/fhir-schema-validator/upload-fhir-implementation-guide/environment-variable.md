---
description: Uplooad a FHIR Implementation Guide into Aidbox using an environment variable
---

# Environment Variable

You can configure Aidbox to load Implementation Guides at startup using environment variables.

Set the environment variable with the names and versions of the IGs.&#x20;

***

**Example for a single IG:**

```
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1
```

***

**Example for multiple IGs:**

Use a colon `:` as a separator between IGs.

{% code fullWidth="false" %}
```
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1:hl7.fhir.us.davinci-pdex#2.0.0
```
{% endcode %}

***

Aidbox will automatically resolve and load the dependencies of the specified IGs.



The list of supported IGs for use in the environment variable here:

{% content-ref url="../supported-implementation-guides.md" %}
[supported-implementation-guides.md](../supported-implementation-guides.md)
{% endcontent-ref %}

Other methods for loading IGs:

{% content-ref url="aidbox-ui/ig-package-from-aidbox-registry.md" %}
[ig-package-from-aidbox-registry.md](aidbox-ui/ig-package-from-aidbox-registry.md)
{% endcontent-ref %}

{% content-ref url="aidbox-ui/public-url-to-ig-package.md" %}
[public-url-to-ig-package.md](aidbox-ui/public-url-to-ig-package.md)
{% endcontent-ref %}

{% content-ref url="aidbox-ui/local-ig-package.md" %}
[local-ig-package.md](aidbox-ui/local-ig-package.md)
{% endcontent-ref %}
