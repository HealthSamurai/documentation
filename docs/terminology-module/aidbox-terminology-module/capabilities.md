---
description: Complete list of supported FHIR terminology operations and capabilities in Aidbox Terminology Module
---

# Capabilities

This page provides a comprehensive overview of all FHIR terminology operations and capabilities supported by the Aidbox Terminology Module.

> **Status Legend**
> - ✅ Full support <sup>\*</sup> - Complete implementation with all parameters
> - 🏗️ In development - Under heavy development, expect partial support in the meantime
> - ❌ No support - Yet to be developed
> 
> <br />
> <small>* Practically full support - edge cases may exist</small>

## Supported Operations

| Resource       | Operation            | Status | FHIR Standard |
| -------------- | -------------------- | ------ | ------------- |
| **CodeSystem** | `$lookup`            | ✅      | ✅             |
|                | `$validate-code`     | ✅      | ✅             |
|                | `$subsumes`          | ❌      | ✅             |
|                | `$find-matches`      | ❌      | ✅             |
|                | `$codesystem-import` | ✅      | ❌             |
|                | `$codesystem-export` | ✅      | ❌             |
| **ValueSet**   | `$expand`            | ✅      | ✅             |
|                | `$validate-code`     | ✅      | ✅             |
| **ConceptMap** | `$translate`         | ✅      | ✅             |
|                | `$closure`           | ❌      | ✅             |

## Features

| Feature                                      | Status | Notes                                                             | Release |
| -------------------------------------------- | ------ | ----------------------------------------------------------------- | ------- |
| Capability Statements                        | ✅      | + TerminologyCapabilities                                         | 2507    |
| CRUD of terminology resources                | ✅      | Create, Read, Update, Delete operations                           | 2507    |
| Pre-coordinated codes                        | ✅      | Standard coded concepts                                           | 2507    |
| Post-coordinated codes                       | ❌      | Complex expressions not yet supported                             |         |
| Intensional ValueSets                        | ✅      | Filter-based ValueSet definitions                                 | 2507    |
| Extensional ValueSets                        | ✅      | Explicit concept enumeration                                      | 2507    |
| ValueSet expansion                           | ✅      | Full expansion with pagination                                    | 2507    |
| ValueSet validation                          | ✅      | Code membership validation                                        | 2507    |
| ValueSet inclusion/exclusion                 | ✅      | Deep set operations support                                       | 2507    |
| Lookup displays, designation, and properties | ✅      | All concept attributes                                            | 2507    |
| Text search filter                           | ✅      | Free-text concept search                                          | 2507    |
| Property filters                             | ✅      | Property-based filtering: `=`, `in`, `regex`, etc                 | 2507    ****|
| Multi-language support                       | ✅      | Translations via `displaylanguage`, HTTP header, designation, etc | 2507    |
| Active/Inactive filtering                    | ✅      | Via `status`, `inactive`, `notSelectable`, etc                    | 2507    |
| Hierarchy via `parent`, `child`              | ✅      |                                                                   | 2507    |
| Nested concepts                              | ✅      | Hierarchy via `concept.concept`                                   | 2507    |
| Subsumption filters                          | ✅      | is-a, descendent-of, generalizes, etc                             | 2507    |
| Supplemental CodeSystems                     | ✅️      | Additional concept properties                                     | 2508    |
| Implicit ValueSets                           | 🏗️      | System-generated ValueSets                                        |         |
| ConceptMap translations                      | ✅      | Code mapping between terminology systems                          | 2508    |
| Multiple ConceptMap matches                  | ✅      | Returns all applicable mappings for source code                   | 2508    |
| Transitive closure table                     | ❌      | $closure                                                          |         |
| Syntax-based code systems                    | ❌      | UCUM, BCP47, etc                                                  |         |
| `tx-resource` parameter                      | ✅      | Inline resource definitions                                       | 2507    |
| Ad-hoc ValueSets                             | ✅      | ValueSet as a `Parameter`                                         | 2507    |
| R4/R5/R6 format conversion                   | ✅      | E.g.: `expansion.contains.concept.property` or extensions         | 2507    |
| Batch validation                             | ❌      |                                                                   |         |
| Streaming operations                         | ✅      | Aidbox specific, large terminologies                              | 2511    |

