---
description: This chapter explains how Profiling works in Aidbox
---

# Profiling and validation overview

FHIR resources are very loose in requirements which gives FHIR its flexibility. For example, all elements are optional in the Patient resource, and it's possible to create a Patient resource without any data which does not make much sense. So, sometimes there is a need to constraint resources.

In FHIR, you need to create a StructureDefinition resource and describe the requirements for a resource you want to restrict. And it is definitely not an easy task. There are special tools developed specifically for this.

Aidbox implements two ways of working with profiles: using AidboxProfile resource and with zen-lang schemas.

{% content-ref url="profiling-with-zen-lang/" %}
[profiling-with-zen-lang](profiling-with-zen-lang/)
{% endcontent-ref %}

{% content-ref url="profiling-with-aidboxprofile.md" %}
[profiling-with-aidboxprofile.md](profiling-with-aidboxprofile.md)
{% endcontent-ref %}

### `aidbox-validation-skip` request header

`aidbox-validation-skip` header allows to skip resource reference validations.

The header functionality can be enabled with `box_features_validation_skip_reference` env.

{% code title="Example" %}
```yaml
box_features_validation_skip_reference=true
```
{% endcode %}

#### Usage

Request without `aidbox-validation-skip` request header causes an error.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /fhir/Observation/f001
content-type: text/yaml

resourceType: Observation
id: f001
subject:
  reference: Patient/id-does-not-exist
status: final
code:
  coding:
  - system: http://loinc.org
    code: 15074-8
```
{% endtab %}

{% tab title="Response" %}
```yaml
status: 422
body:
  issue:
  - expression:
    - Observation.subject
    diagnostics: Referenced resource Patient/id-does-not-exist does not exist
```
{% endtab %}
{% endtabs %}

The request is successful when `aidbox-validation-skip` request header is provided.

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /fhir/Observation/f001
content-type: text/yaml
aidbox-validation-skip: reference

resourceType: Observation
id: f001
subject:
  reference: Patient/id-does-not-exist
status: final
code:
  coding:
  - system: http://loinc.org
    code: 15074-8
```
{% endtab %}

{% tab title="Response" %}
```yaml
status: 201
* body is skipped for simplicity *
```
{% endtab %}
{% endtabs %}
