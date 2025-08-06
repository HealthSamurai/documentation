---
hidden: true
---

# Find duplicates: $match (original)

FHIR match operation is defined on Patient resource. Aidbox extends it to all resource types for which MDM is configured.

This operation finds all resources which match (i.e. are similar to) the given resource.

FHIR definition of the operation: [http://hl7.org/fhir/OperationDefinition/Patient-match](http://hl7.org/fhir/OperationDefinition/Patient-match)

This operation translates to [aidbox.mdm/match RPC](../../../deprecated/deprecated/zen-related/rpc-reference/aidbox/mdm/aidbox-mdm-match.md) call. The RPC has simpler syntax and you can adjust threshold values. Consider using it if you are not required to use FHIR syntax.

To use `$match` operation you need to set up MDM. Read the [Aidbox MDM manual](./) to learn how to use it.

## Syntax

### FHIR format

```
POST /fhir/<resourceType>/$match

resourceType: Parameters
parameter:
  - name: resource
    resource:
      # Resource in FHIR format to match against.
      # This must be the resource contents,
      # not a reference to another resource
  - name: onlyCertainMatches # This parameter is optional
    valueBoolean: true # or false
```

The `threshold` parameter is not required. It specifies minimum score for a match to be returned.

## Examples

### Find certain matches for the specified resource using FHIR format

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

Example output

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

### Find matches in AIdbox format with specific threshold

```
POST /Patient/$match?threshold=0.95

name:
  - given:
      - John
    family: Smith-Johnson-Williams
birthDate: "2000-01-01"
```

Example output

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
