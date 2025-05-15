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

* [FTR Specification](ftr-specification.md)

* [Create an FTR instance](creating-aidbox-project-with-ftr/)

### Ready to use FTRs

* [Load SNOMED CT into Aidbox](load-snomed-ct-into-aidbox.md)

* [Load ICD-10-CM into Aidbox](load-icd-10-cm-into-aidbox.md)

* [Load LOINC into Aidbox](load-loinc-into-aidbox.md)

* [Load RxNorm into Aidbox](load-rxnorm-into-aidbox.md)
