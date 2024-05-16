# Profiling and validation

The core FHIR specification details fundamental resources, frameworks, and APIs widely utilized across various healthcare contexts. Given the significant differences in practices, regulations, and requirements across regions and within the healthcare industry, the FHIR specification serves as a "platform specification." \
\
At the same time, FHIR resources are very loose in requirements. For example, all elements are optional in the Patient resource, and it's possible to create a Patient resource without any data which does not make much sense. Consequently, this specification often needs to be tailored to specific use cases. \
\
FHIR defines a cascade of artifacts for this purpose:

<table><thead><tr><th width="166">Artifact</th><th width="425">Description</th><th>Example</th></tr></thead><tbody><tr><td>Implementation Guide (IG)</td><td>A well-defined and bounded set of adaptations, published together as a single entity. Typically consists of profiles and extensions, terminology definitions, operations, search parameters, and examples.</td><td>US Core IG, mCODE</td></tr><tr><td>Conformance Resource</td><td>A single resource in a package that makes rules about how an implementation works</td><td>US Core Condition Codes Value Set</td></tr><tr><td>Profile</td><td>A set of constraints on a resource represented as a structure definition </td><td>US Core Medication Request</td></tr></tbody></table>

### Aidbox validation engines

{% content-ref url="fhir-schema-validator.md" %}
[fhir-schema-validator.md](fhir-schema-validator.md)
{% endcontent-ref %}

{% content-ref url="../../profiling-and-validation/profiling-with-aidboxprofile.md" %}
[profiling-with-aidboxprofile.md](../../profiling-and-validation/profiling-with-aidboxprofile.md)
{% endcontent-ref %}

{% content-ref url="../../profiling-and-validation/profiling-with-zen-lang/" %}
[profiling-with-zen-lang](../../profiling-and-validation/profiling-with-zen-lang/)
{% endcontent-ref %}
