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

| Resource | Operation | Status |
|----------|-----------|--------|
| **CodeSystem** | `$lookup` | ✅ |
|                | `$validate-code` | ✅ |
|                | `$subsumes` | ❌ |
|                | `$find-matches` | ❌ |
| **ValueSet** | `$expand` | ✅ |
|              | `$validate-code` | ✅ |
| **ConceptMap** | `$translate` | 🏗 |
|                | `$closure` | ❌ |

## Features

| Feature | Status | Notes |
|---------|--------|-------|
| Capability Statements | ✅ | + TerminologyCapabilities |
| CRUD of terminology resources | ✅ | Create, Read, Update, Delete operations |
| Pre-coordinated codes | ✅ | Standard coded concepts |
| Post-coordinated codes | ❌ | Complex expressions not yet supported |
| Intensional ValueSets | ✅ | Filter-based ValueSet definitions |
| Extensional ValueSets | ✅ | Explicit concept enumeration |
| ValueSet expansion | ✅ | Full expansion with pagination |
| ValueSet validation | ✅ | Code membership validation |
| ValueSet inclusion/exclusion | ✅ | Deep set operations support |
| Lookup displays, designation, and properties | ✅ | All concept attributes |
| Text search filter | ✅ | Free-text concept search |
| Property filters | ✅ | Property-based filtering: `=`, `in`, `regex`, etc |
| Multi-language support | ✅ | Translations via `displaylanguage`, HTTP header, designation, etc |
| Active/Inactive filtering | ✅ | Via `status`, `inactive`, `notSelectable`, etc |
| Hierarchy via `parent`, `child` | ✅ |  |
| Nested concepts | ✅ | Hierarchy via `concept.concept` |
| Subsumption filters | ✅ | is-a, descendent-of, generalizes, etc |
| Supplemental CodeSystems | 🏗️ | Additional concept properties |
| Implicit ValueSets | 🏗️ | System-generated ValueSets |
| Translate code from value set to another | 🏗️ | Cross-ValueSet translations |
| Transitive closure table | ❌ | $closure |
| Syntax-based code systems | ❌ | UCUM, BCP47, etc |
| `tx-resource` parameter | ✅ | Inline resource definitions |
| Ad-hoc ValueSets | ✅ | ValueSet as a `Parameter` |
| R4/R5/R6 format conversion | ✅ | E.g.: `expansion.contains.concept.property` or extensions |
| Batch validation | ❌ | |

