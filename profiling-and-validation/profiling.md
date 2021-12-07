---
description: This chapter explains how Profiling works in Aidbox
---

# Profiling and validation overview

FHIR resources are very loose in requirements which gives FHIR its flexibility. For example, all elements are optional in the Patient resource, and it's possible to create a Patient resource without any data which does not make much sense. So, sometimes there is a need to constraint resources.&#x20;

In FHIR, you need to create a StructureDefinition resource and describe the requirements for a resource you want to restrict. And it is definitely not an easy task. There are special tools developed specifically for this.&#x20;

Aidbox implements two ways of working with profiles: using AidboxProfile resource and with zen-lang schemas.&#x20;

{% content-ref url="profiling-with-zen-lang/" %}
[profiling-with-zen-lang](profiling-with-zen-lang/)
{% endcontent-ref %}

{% content-ref url="profiling-with-aidboxprofile.md" %}
[profiling-with-aidboxprofile.md](profiling-with-aidboxprofile.md)
{% endcontent-ref %}
