# Profiling

## Overview

Aidbox use JSON Schema from AiboxProfile resource for profiling resources

```yaml
resourceType: AidboxProfile
id: custom-patient-constraint
bind:
  id: Patient
  resourceType: Entity
schema:
  required:
    - name
    - gender
```

## AidboxProfile resource structure

### bind 

Type -  [Reference](https://www.hl7.org/fhir/references.html). Specify resource type for what profile will be applied. 

**Example:** Binding to `Patient` resource

```yaml
bind:
  id: Patient
  resourceType: Entity
```

### schema

Type - Object. [JSON Schema ](https://json-schema.org/)which will validate resource 

**Example:**  ...

## Example

Required `name` and `gender` attribute for `Patient` resource

```yaml
resourceType: AidboxProfile
id: custom-patient-constraint
bind:
  id: Patient
  resourceType: Entity
schema:
  required:
    - name
    - gender
```

When trying to create Patient without `name` and/or `gender`  you will receive error.

{% tabs %}
{% tab title="Request" %}
```yaml
POST /Patient

birthDate: "1985-01-11"
```
{% endtab %}

{% tab title="Response" %}
```yaml
Status: 422

resourceType: OperationOutcome
errors:
- path: []
  message: Property name is required
  profile:
    id: custom-patient-constraint
    resourceType: AidboxProfile
- path: []
  message: Property gender is required
  profile:
    id: custom-patient-constraint
    resourceType: AidboxProfile
warnings: []


```
{% endtab %}
{% endtabs %}

