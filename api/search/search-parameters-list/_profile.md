---
description: Search by the resource profile
---

# \_profile

Search by the resource profile

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /fhir/Patient?_profile=http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
GET /Patient?_profile=http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient
```
{% endtab %}
{% endtabs %}

You can search by multiple profiles separated by a comma

{% tabs %}
{% tab title="FHIR format" %}
```yaml
GET /fhir/Patient?_profile=http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient,http://fhir.nl/fhir/StructureDefinition/nl-core-patient
```
{% endtab %}

{% tab title="Aidbox format" %}
```yaml
GET /Patient?_profile=http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient,http://fhir.nl/fhir/StructureDefinition/nl-core-patient
```
{% endtab %}
{% endtabs %}
