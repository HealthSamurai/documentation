---
description: Overview of FHIR terminology concepts and Aidbox's terminology server implementation
---

# Terminology Overview

Healthcare systems need to share data reliably across different organizations, vendors, and countries. The challenge? Every system uses different codes for the same concepts. One hospital might code "blood pressure" as "BP", another as "SYSTOLIC_DIASTOLIC", and a third might use a completely different internal system. Poor terminology management leads to data silos, integration nightmares, and ultimately, compromised patient care.

FHIR terminology solves this problem by providing standardized ways to represent and validate coded healthcare data. Aidbox implements a comprehensive FHIR terminology server natively integrated into the platform, delivering compliant and performant terminology operations.

## FHIR Terminology

FHIR builds terminology around core resource types and data structures that work together to organize and constrain coded data.

**Coded values** in FHIR use specialized data types for different scenarios. Simple `code` elements work for fixed vocabularies, `Coding` structures specify both the system and code for precise identification, and `CodeableConcept` allows multiple codings to represent the same concept across different systems simultaneously.

**CodeSystems** define the actual codes and their meanings. Think of SNOMED CT with its comprehensive clinical terminology, or LOINC with standardized laboratory codes. Each CodeSystem establishes what specific codes mean within their domain.

**ValueSets** curate collections of codes from one or more CodeSystems for specific contexts. While LOINC contains thousands of laboratory codes, a ValueSet might select just the subset relevant for "routine blood chemistry panels."

**ConceptMaps** define relationships and translations between codes from different CodeSystems. When you need to map your internal "HTN" code to SNOMED CT's "38341003 | Hypertensive disorder", ConceptMaps provide the structured translation rules.

See also: [FHIR Terminology Concepts](./fhir-terminology/intro.md)

## Aidbox Terminology Module

Aidbox implements a flexible terminology server that adapts to different organizational needs through three operational modes:

**Local mode** uses only CodeSystems and ValueSets stored in Aidbox's FHIR Artifact Registry, providing complete control over terminology content but requiring explicit loading of all needed resources.

**Hybrid mode** combines local storage with external server fallback, balancing performance with comprehensive coverage by using local resources when available and querying external servers when needed.

**Remote (legacy) mode** routes all terminology requests to external terminology servers, useful for organizations relying on established external services or transitioning their terminology infrastructure.

The module supports all standard FHIR terminology operations - `$validate-code`, `$expand`, and `$lookup` - working seamlessly across local and external content depending on your configuration.

See also: [Aidbox Terminology Module](./aidbox-terminology-module/intro.md)

