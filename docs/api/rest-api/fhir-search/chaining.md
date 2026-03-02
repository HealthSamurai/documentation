---
description: Filter FHIR resources by referenced resource properties using chained search parameters.
---

# Chaining

Chained parameters allow filtering by the parameter of a resource linked via a reference.

Example:

```
GET /fhir/Encounter?subject:Patient.gender=male
```

## Syntax

### Forward chain element

An element of a forward chain is represented as

```
<reference parameter name>:<target resource type>
```

Any chain must finish with a terminal. A terminal is just a search parameter name.

Example:

```
GET /fhir/Encounter?subject:Patient.gender=male
GET /fhir/Encounter?subject:Patient.general-practitioner:Practitioner.family=Smith
```

### Reverse chain element

An element of a reverse chain is represented as

```
_has:<source resource type>:<search parameter>
```

Any chain must finish with a terminal. A terminal is just a search parameter name.

Example:

```
GET /fhir/Observation?_has:Encounter:subject:status=finished
```

### Chain

A chain is composed of an arbitrary number of chain elements and finished with a chain terminal.

There are some rules to join chain elements:

* Forward chain elements are joined using the `.` symbol;
* Reverse chain elements are joined using the `:` symbol;
* Different types of chain elements are joined using the `.` symbol.

If the last chain element is a reverse element, then a terminal is joined using the `:` symbol, otherwise the `.` symbol.

Examples:

```
GET /fhir/Encounter?subject:Patient.name

GET /fhir/Observation?_has:Encounter:subject:code

GET /fhir/Encounter?subject:Patient.organization:Organization.name

GET /fhir/Patient?_has:Organization:_has:Observation:subject:code

GET /fhir/Patient?patient:Patient._has:Group:member:_id=group1

GET /fhir/Encounter?_has:Encounter:subject.practitioner:Practitioner._id=prac1
```

## Semantics

You can interpret chain elements as a semijoin. The difference between forward and reverse chains is where a reference is located: in a forward chain, a reference is located on the left side of a semijoin, and in a reverse chain, a reference is located on the right side of a semijoin.

Example 1:

```
GET /fhir/Organization?_has:Patient:organization:_has:Observation:subject:code=...
```

You can read it as (pseudocode)

```sql
SELECT *
FROM Organizaton AS organizaton
SEMI JOIN Patient AS patient
ON patient.organization REFERS TO patient
SEMI JOIN Observation AS observation
ON observation.subject REFERS TO patient
WHERE observation.code = ...
```

Semantically, this means: find all organizations that have patients who have an observation with the given code.

Example 2:

```
GET /fhir/Observation?subject:Patient.organization:Organization.name=...
```

You can read it as (pseudocode)

```sql
SELECT *
FROM Observation AS observation
SEMI JOIN Patient AS patient
ON observation.subject REFERS TO patient
SEMI JOIN Organization AS organization
ON patient.organization REFERS TO organization
WHERE organization.name = ...
```

This means: find all observations that belong to some patient managed by some organization with the given name.

Example 3:

```
GET /fhir/Patient?_has:Encounter:subject.practitioner:Practitioner._id=...
```

You can read it as (pseudocode):

```sql
SELECT *
FROM Patient AS patient
SEMI JOIN Encounter as encounter
ON encounter.subject REFERS TO patient
SEMI JOIN Practitioner AS practitioner
ON encounter.practitioner REFERS TO practitioner
WHERE practitioner.id = ...
```

This means "find all patients who visited the given practitioner".

## See also:

* [Chain search subselect reference](../../../reference/all-settings.md#fhir.search.chain.subselect)
