---
description: Aidbox terminology server implementation modes, FHIR operations, and integration with FHIR Artifact Registry
---

# Aidbox Terminology Module

The Aidbox Terminology Module is a fully conformant [FHIR Terminology Service](https://www.hl7.org/fhir/terminology-service.html) that seamlessly integrates with FHIR workflows including validation, UI components, and translation services. The module implements *most* standard FHIR terminology operations (see [capabilities](./capabilities.md) for details) including `$lookup`, `$validate-code`, `$expand`, `$translate`, and subsumption testing, providing comprehensive terminology capabilities through a RESTful API.

Aidbox's terminology implementation centers on the [FHIR Artifact Registry (FAR)](../../artifact-registry/artifact-registry-overview.md), which serves as the primary storage for canonical resources including CodeSystems and ValueSets. This architecture supports FHIR Implementation Guide delivery mechanisms and knowledge artifact lifecycle.

## Operational Modes

The terminology module operates in three distinct modes to accommodate different deployment scenarios:

- **[Local Mode](./hybrid.md#local-mode)**: Uses only resources stored in Aidbox's FAR. Provides complete control but requires all CodeSystems to be explicitly loaded.

- **[Hybrid Mode](./hybrid.md#hybrid-mode)**: Combines local storage with external server fallback. Balances performance with comprehensive coverage through intelligent request partitioning.

- **[Remote Mode](./hybrid.md#remote-legacy-mode)**: Routes all requests to external terminology servers, bypassing local storage entirely.

See [Operational Modes](./hybrid.md) for detailed explanations with examples and sequence diagrams.

## Key Features

- **FHIR R4 Compliance**: Full conformance with FHIR Terminology Service specification
- **Intelligent Request Routing**: Hybrid engine that optimally partitions mixed ValueSet operations
- **Implementation Guide Integration**: Native support for IG loading and lifecycle management
- **Performance Optimization**: Local caching with external fallback for comprehensive coverage
- **Standard Operations**: Complete support for `$lookup`, `$validate-code`, `$expand`, `$translate`, and subsumption testing

See also:
- [Setup](./setup.md)
- [Hybrid Mode](./hybrid.md)
- [Capabilities](./capabilities.md)
- [FHIR Artifact Registry documentation](../../artifact-registry/artifact-registry-overview.md)
- [FHIR Terminology Server Specification](https://www.hl7.org/fhir/terminology-service.html)

