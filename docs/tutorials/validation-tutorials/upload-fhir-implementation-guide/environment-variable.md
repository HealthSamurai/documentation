---
description: Upload a FHIR Implementation Guide into Aidbox using an environment variable
---

# Environment Variable

You can configure Aidbox to load Implementation Guides at startup using environment variables.

Set the environment variable with the names and versions of the IGs.

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



The list of supported IGs for use in the environment variable here is located [here](../../../modules/profiling-and-validation/fhir-schema-validator/aidbox-fhir-igs-registry.md).

Other methods for loading IGs:

* [IG Package from Aidbox Registry](aidbox-ui/ig-package-from-aidbox-registry.md)

* [Public URL to IG Package](aidbox-ui/public-url-to-ig-package.md)

* [Local IG Package](aidbox-ui/local-ig-package.md)
