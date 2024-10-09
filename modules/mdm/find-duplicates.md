---
description: Use $match to find duplicates
---

# Find duplicates

## Use match operation

Use [$match](../../api-1/fhir-api/usdmatch.md) operation or [`aidbox.mdm/match` RPC method](../../reference/rpc-reference/aidbox/mdm/aidbox.mdm-match.md) to find similar resources:

```
POST /fhir/Patient/$match

resourceType: Parameters
parameter:
  - name: resource
    resource:
      name:
        - given:
            - John
          family: Smith-Johnson-Williams
      birthDate: "2000-01-01"
  - name: onlyCertainMatches
    valueBoolean: true
```

Example output:

```
resourceType: Bundle
type: searchset
entry:
  - score: 0.99
    resource:
      name:
        - given:
            - John
          family: Smith-Johnson-Williams
      address:
        - city: London
      birthDate: "2000-01-01"
      resourceType: Patient
      id: pt-1
```
