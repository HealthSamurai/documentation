# aidbox.mdm/match

Find matches for the given resource

## Syntax

```
POST /rpc

method: aidbox.mdm/match
params:
  resource-type: <string, required>
  threshold: <float, optional>
  resource: <FhirResource, required>
```

## Parameters

`resource-type: <string, required>`

Resource type in which to search for matches. MDM model has to be set up for this resource type.

`threshold: <float, optional>`

Number in range (0, 1). Return only resources, match score for which is greater than threshold.

`resource: <FhirResource>`

FHIR resource of type ResourceType. Search for resources matching the given resource.

## Examples

Find matching patients for the given one

```
POST /rpc

method: aidbox.mdm/match
params:
  resource-type: Patient
  threshold: 0.7
  resource:
    name:
      - given:
          - Jonh
        family: Smtih
    birthDate: 2000-01-01
    address:
      - postalCode: "12345"
```

Example output:

```
result:
  - id: pt-1
    normalized_prob: 0.8
    prob: 4
    resource:
      name:
        - given:
            - John
          family: Smith
      birthDate: 2000-01-01
      address:
        - postalCode: "12345"
      id: pt-1
      resourceType: Patient
```
