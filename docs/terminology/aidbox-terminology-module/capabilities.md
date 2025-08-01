---
description: Complete list of supported FHIR terminology operations and capabilities in Aidbox Terminology Module
---

# Capabilities

This page provides a comprehensive overview of all FHIR terminology operations and capabilities supported by the Aidbox Terminology Module.

## Supported Operations

### CodeSystem Operations

- `$lookup` - ✅ Supported
- `$validate-code` - ✅ Supported  
- `$subsumes` - ⚠️ Partial support
- `$find-matches` - ❌ Not supported

### ValueSet Operations

- `$expand` - ✅ Supported
- `$validate-code` - ✅ Supported

### ConceptMap Operations

- `$translate` - ✅ Supported
- `$closure` - ❌ Not supported

## Resource Support

### CodeSystem Resource
- Full CRUD operations
- Search parameters
- Canonical URL resolution

### ValueSet Resource  
- Full CRUD operations
- Search parameters
- Canonical URL resolution
- Expansion capabilities

### ConceptMap Resource
- Full CRUD operations
- Search parameters
- Translation capabilities

## Conformance Resources

- **CapabilityStatement** - ✅ Provided
- **TerminologyCapabilities** - ✅ Provided

## Standards Compliance

The Aidbox Terminology Module conforms to:
- FHIR R4 Terminology Service specification
- Required search parameters
- Standard operation definitions
- Canonical URL handling

[Content to be expanded with detailed capability matrix and examples]