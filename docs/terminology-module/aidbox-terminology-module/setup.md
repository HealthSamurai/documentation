---
description: >-
  Setting up and configuring Aidbox terminology module with different
  operational modes
---

# Setup

The Aidbox Terminology Module requires configuration through environment variables to define the operational mode and external server connections (optional). This section covers the essential configuration steps for each operational mode.

## Basic Configuration

### Terminology Engine Mode

Set the terminology engine mode using the `FHIR_TERMINOLOGY_ENGINE` environment variable:

```yaml
# Choose your operational mode
BOX_FHIR_TERMINOLOGY_ENGINE: hybrid  # Options: local, hybrid, legacy
```

**Available modes:**

* `local` - Uses only resources stored in Aidbox's FAR
* `hybrid` - Combines local storage with external server fallback (recommended)
* `legacy` - Routes all requests to external terminology servers

### External Terminology Server (Hybrid Mode)

For Hybrid Mode, configure the external terminology server URL:

<pre class="language-yaml"><code class="lang-yaml"><strong>BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
</strong>BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
</code></pre>

## Implementation Guide Loading

To populate the local terminology content, configure FHIR packages to load at startup:

<pre class="language-yaml"><code class="lang-yaml"><strong>BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1:hl7.terminology.r4#6.4.0
</strong></code></pre>

**Format:** `<package-name>#<version>:<package-name>#<version>...`

**Common packages:**

* `hl7.fhir.r4.core#4.0.1` - Core FHIR R4 resources
* `hl7.terminology.r4#6.4.0` - Core FHIR terminology resources
* `hl7.fhir.us.core#6.0.0` - US Core Implementation Guide

For more information about FHIR package loading and management, see the [FHIR Artifact Registry documentation](../../artifact-registry/artifact-registry-overview.md).

## Complete Configuration Examples

### Example 1: Hybrid Mode (Recommended)

```yaml
# Terminology Module Configuration
BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir

# Load core terminology packages
BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1
```

### Example 2: Local Mode

```yaml
# Terminology Module Configuration  
BOX_FHIR_TERMINOLOGY_ENGINE: local

# Must load all required terminology packages
BOX_BOOTSTRAP_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1
```

### Example 3: Legacy Mode

```yaml
# Legacy terminology configuration (deprecated approach)
BOX_FHIR_TERMINOLOGY_ENGINE: legacy
BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
```

## Verification

After configuration, verify your terminology setup by testing basic operations:

1. **Check loaded packages** via the Aidbox UI at `/ui/packages`
2. **Test expansion** with a simple ValueSet: `GET /fhir/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender`
3. **Test validation** with a coded value: `POST /fhir/ValueSet/$validate-code`

### Upgrading to Aidbox 2507+

If you're upgrading from a previous version of Aidbox to version 2507 or later, you may encounter validation errors when creating FHIR resources after configuring the new terminology module. This is due to changes in how terminology resources are processed in the new implementation.

**Example of the issue:**

When attempting to create a simple Patient resource:

```http
POST /fhir/Patient

gender: male
```

You may receive a `422` status code with an error response:

```json
{
  "resourceType": "OperationOutcome",
  "text": {
    "status": "generated",
    "div": "Invalid resource"
  },
  "issue": [
    {
      "severity": "fatal",
      "code": "invalid",
      "expression": ["Patient."],
      "details": {
        "coding": [
          {
            "system": "http://aidbox.app/CodeSystem/operation-outcome-type",
            "code": "internal-validator-error"
          }
        ]
      },
      "diagnostics": "Internal validator error occurred: External terminology server error.\nServer responded with status 404.\nResponse body: Unknown body type: class clojure.lang.PersistentArrayMap"
    }
  ]
}
```

**Solution:**

Reinstall FHIR packages that contain terminology resources through the FHIR Artifact Registry (FAR):

1. Navigate to the FAR in your Aidbox UI at `/ui/console#/ig`
2. First, try reinstalling terminology-related packages such as:
   * `hl7.fhir.r4.core`
   * `hl7.terminology.r4` (if installed)
3. For each package, click the **"Reinstall"** button at the bottom of the package page
4. Wait for the reinstallation to complete
5. If issues persist, you may need to reinstall additional packages in your configuration

**Verification:**

After reinstalling the packages, test that the issue is resolved by creating the same Patient resource:

```http
POST /fhir/Patient

gender: male
```

You should now receive a successful `201` status code response:

```json
{
  "gender": "male",
  "id": "372535cd-d17b-4e0c-889f-cb0172fe7c23",
  "resourceType": "Patient",
  "meta": {
    "lastUpdated": "2025-08-07T10:07:11.637917Z",
    "versionId": "17",
    "extension": [
      {
        "url": "https://aidbox.app/ex/createdAt",
        "valueInstant": "2025-08-07T10:07:11.637917Z"
      }
    ]
  }
}
```

## Configuration Reference

For complete details on all terminology-related environment variables, see:

* [FHIR Settings Reference](../../reference/all-settings.md#terminology)
* [General Settings Reference](../../reference/all-settings.md#bootstrap-fhir-packages)
