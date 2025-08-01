---
description: Setting up and configuring Aidbox terminology module with different operational modes
---

# Setup

The Aidbox Terminology Module requires configuration through environment variables to define the operational mode and external server connections (optional). This section covers the essential configuration steps for each operational mode.

## Basic Configuration

### Terminology Engine Mode

Set the terminology engine mode using the `FHIR_TERMINOLOGY_ENGINE` environment variable:

```yaml
# Choose your operational mode
FHIR_TERMINOLOGY_ENGINE: hybrid  # Options: local, hybrid, legacy
```

**Available modes:**
- `local` - Uses only resources stored in Aidbox's FAR
- `hybrid` - Combines local storage with external server fallback (recommended)
- `legacy` - Routes all requests to external terminology servers

### External Terminology Server (Hybrid Mode)

For Hybrid Mode, configure the external terminology server URL:

```yaml
FHIR_TERMINOLOGY_ENGINE: hybrid
FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
```

## Implementation Guide Loading

To populate the local terminology content, configure FHIR packages to load at startup:

```yaml
BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1:hl7.terminology.r4#6.4.0
```

**Format:** `<package-name>#<version>:<package-name>#<version>...`

**Common packages:**
- `hl7.fhir.r4.core#4.0.1` - Core FHIR R4 resources
- `hl7.terminology.r4#6.4.0` - Core FHIR terminology resources
- `hl7.fhir.us.core#6.0.0` - US Core Implementation Guide

For more information about FHIR package loading and management, see the [FHIR Artifact Registry documentation](../../artifact-registry/artifact-registry-overview.md).

## Complete Configuration Examples

### Example 1: Hybrid Mode (Recommended)

```yaml
# Terminology Module Configuration
FHIR_TERMINOLOGY_ENGINE: hybrid
FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir

# Load core terminology packages
BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1:hl7.terminology.r4#6.4.0:hl7.fhir.us.core#6.0.0
```

### Example 2: Local Mode

```yaml
# Terminology Module Configuration  
FHIR_TERMINOLOGY_ENGINE: local

# Must load all required terminology packages
BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1:hl7.terminology.r4#6.4.0:your.custom.package#1.0.0
```

### Example 3: Legacy Mode

```yaml
# Legacy terminology configuration (deprecated approach)
FHIR_TERMINOLOGY_ENGINE: legacy
BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
```

## Verification

After configuration, verify your terminology setup by testing basic operations:

1. **Check loaded packages** via the Aidbox UI at `/ui/packages`
2. **Test expansion** with a simple ValueSet: `GET /fhir/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender`
3. **Test validation** with a coded value: `POST /fhir/ValueSet/$validate-code`

## Configuration Reference

For complete details on all terminology-related environment variables, see:
- [FHIR Settings Reference](../../reference/settings/fhir.md#terminology)
- [General Settings Reference](../../reference/settings/general.md#bootstrap-fhir-packages)
