---
description: FHIR Terminology Repository (FTR), repository layout specification, tools
---

# 🏗️ FHIR Terminology Repository

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Broken link](broken-reference)
{% endhint %}

**FTR** (FHIR Terminology Repository) it's an open specification for repository layout, formats and algorithms to distribute FHIR terminologies as ValueSets.

You can store the resulting FTR on s3 like storage or a regular file system. Aidbox team provides tools for the FTR population and distribution mechanism via zen-packages. Aidbox can sync FTR's come with zen-packages and as a result, validate/lookup ValueSets.

{% content-ref url="ftr-specification.md" %}
[ftr-specification.md](ftr-specification.md)
{% endcontent-ref %}

{% content-ref url="creating-aidbox-project-with-ftr/" %}
[creating-aidbox-project-with-ftr](creating-aidbox-project-with-ftr/README.md)
{% endcontent-ref %}

### Ready to use FTRs

{% content-ref url="load-snomed-ct-into-aidbox.md" %}
[load-snomed-ct-into-aidbox.md](load-snomed-ct-into-aidbox.md)
{% endcontent-ref %}

{% content-ref url="load-icd-10-cm-into-aidbox.md" %}
[load-icd-10-cm-into-aidbox.md](load-icd-10-cm-into-aidbox.md)
{% endcontent-ref %}

{% content-ref url="load-loinc-into-aidbox.md" %}
[load-loinc-into-aidbox.md](load-loinc-into-aidbox.md)
{% endcontent-ref %}

{% content-ref url="load-rxnorm-into-aidbox.md" %}
[load-rxnorm-into-aidbox.md](load-rxnorm-into-aidbox.md)
{% endcontent-ref %}
