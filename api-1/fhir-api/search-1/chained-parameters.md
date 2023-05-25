---
description: Search by associated resources
---

# Chained Parameters

Chained parameters are a special kind of search parameters. You can use them to filter by parameter of a resource linked via a reference.

## Syntax

### Forward chain element

An element of a forward chain is represented as

```
<reference parameter name>:<target resource type>
```

Example:

```
subject:Patient
```

### Reverse chain element

An element of a reverse chain is represented as

```
_has:<source resource type>:<search parameter>
```

Example

```
_has:Observation:subject
```

### Chain terminal

Any chain must finish with a terminal. A terminal is just a search parameter name.

### Chain

A chain is composed from an arbitrary amount of chain elements and finished with a chain terminal.

There are some rules to join chain elements:

* Forward chain elements are joined using the `.` symbol;
* Reverse chain elements are joined using the `:` symbol;
* Different type chain elements are joined using the `.` symbol.

If the last chain element is a reverse element, then a terminal is joined using the `:` symbol, otherwise the `.` symbol.

Examples:

```
subject:Patient.name
_has:Observation:subject:code
subject:Patient.organization:Organization.name
_has:Patient:organization:_has:Observation:subject:code
patient:Patient._has:Group:member:_id=group1
_has:Encounter:subject.practitioner:Practitioner._id=prac1
```

## Semantics

You can interpret chain elements as a semijoin. The difference between forward and reverse chain is where a reference is located: in a forward chain a reference is located on the left side of a semijoin, in a reverse chain a reference is located on the right side of a semijoin.

Example 1:

```
GET /Organization?_has:Patient:organization:_has:Observation:subject:code=...
```

You can read it as (pseudocode)

```
SELECT *
FROM Organizaton AS organizaton
SEMI JOIN Patient AS patient
ON patient.organization REFERS TO patient
SEMI JOIN Observation AS observation
ON observation.subject REFERS TO patient
WHERE observation.code = ...
```

Semantically this means: find all organizations, which have patients that have observation with the given code.

Example 2:

```
GET /Observation?subject:Patient.organization:Organization.name=...
```

You can read it as (pseudocode)

```
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
GET /Patient?_has:Encounter:subject.practitioner:Practitioner._id=...
```

You can read it as (pseudocode)

```
SELECT *
FROM Patient AS patient
SEMI JOIN Encounter as encounter
ON encounter.subject REFERS TO patient
SEMI JOIN Practitioner AS practitioner
ON encounter.practitioner REFERS TO practitioner
WHERE practitioner.id = ...
```

This means: find all patients that visited the given practitioner.

###
