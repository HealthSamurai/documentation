# Profiling

## Overview

For custom profiling, Aidbox provides additional resource `AidboxProfile`. This resource specifies resource type and [JSON Schema](https://json-schema.org/) which will validate the specified resource type.

**Example:** let's validate a `Patient` resource by specifying that `name` and `gender` properties are required.

```yaml
resourceType: AidboxProfile
id: custom-patient-constraint
bind:
  id: Patient
  resourceType: Entity
schema:
  type: object
  required:
    - name
    - gender
```

## AidboxProfile Resource Structure

### bind 

Element is of type [Reference](https://www.hl7.org/fhir/references.html). Specifies resource type for which the profile will be applied. 

**Example:** Binding to a `Practitioner` resource.

```yaml
bind:
  id: Practitioner # Target resource type "Practitoner"
  resourceType: Entity
```

### schema

Element is of type [JSON Schema ](https://json-schema.org/)object which will validate a resource.

**Example:**  In this case, we are expecting that `name` attribute of the type [HumanName](https://www.hl7.org/fhir/datatypes.html#HumanName) will contain elements `given` and `family`.

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

Let's create an `AidboxProfile` resource that requires `name` and `gender` attributes for `Patient` resource.

```yaml
resourceType: AidboxProfile
id: custom-patient-constraint
bind:
  id: Patient
  resourceType: Entity
schema:
  type: object
  required:
    - name
    - gender
```

{% hint style="info" %}
If you are using Aidbox.Dev, after creating an`AidboxProfile`resource you need to restart your Aidbox.Dev server.

`docker-compose down && docker-compose up -d`
{% endhint %}

When trying to create a Patient resource without `name` and/or `gender`  you will receive the error.

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



