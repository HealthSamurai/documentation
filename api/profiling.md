# Profiling

## Overview

For custom profiling Aidbox provide additional resource `AidboxProfile`. This resource specify resource type and [JSON Schema](https://json-schema.org/) which will validate specified resource type.

**Example:** validate `Patient` resource, we are specify that `name` and `gender` property is required

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

**Example:** Binding to `Practitioner` resource.

```yaml
bind:
  id: Practitioner # Target resource type
  resourceType: Entity
```

### schema

Type -  [JSON Schema ](https://json-schema.org/) object which will validate resource.

**Example:**  We are expect that `name`  attribute , type of [HumanName](https://www.hl7.org/fhir/datatypes.html#HumanName), contains `given` and `family`.

```yaml
schema:
  type: object
  required: ["name"]
  properties:
    name:
      type: array
      minItems: 1
      items:
        type: object
        required: ["given", "family"]
        properties:
          given:
            type: array
            minItems: 1
            items:
              type: string
          family:
            type: string
```

## Example

Create `AidboxProfile` resource that required `name` and `gender` attribute for `Patient` resource

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

{% hint style="info" %}
If you use Aidbox.Dev, after creating `AidboxProfile` resource, you need restart you Aidbox.Dev

$ docker-compose down && docker-compose up -d
{% endhint %}

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

