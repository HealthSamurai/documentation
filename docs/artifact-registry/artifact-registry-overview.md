---
description: >-
  Comprehensive overview of Aidbox Artifact Registry for storing and managing
  FHIR canonical resources and packages
---

# Artifact Registry overview

FHIR Artifact Registry (FAR) is Aidbox's centralized system for storing, managing, and resolving FHIR canonical resources and packages. 
It serves as the foundation for FHIR validation, profiling, and terminology operations by providing a unified repository for **CodeSystems**, **ValueSets**, 
**StructureDefinitions**, and **SearchParameters**. 
This overview explains how the registry works, its integration with external FHIR package sources, 
and the versioning strategies used to ensure consistent canonical resource resolution across your FHIR implementation.

## What's the Artifact Registry

The Artifact Registry is a specialized repository within Aidbox that manages 
[FHIR canonical resources](https://build.fhir.org/canonicalresource.html)—resources that define the structure, constraints, and terminology used 
in FHIR implementations.

* [**CodeSystems**](../terminology-module/fhir-terminology/codesystem.md) for defining terminologies,
* [**ValueSets**](../terminology-module/fhir-terminology/valueset.md) for grouping codes,
* [**StructureDefinitions**](structuredefinition.md) for profiling resources,
* [**SearchParameters**](../api/rest-api/fhir-search/searchparameter.md) for custom search capabilities.

The registry acts as the authoritative source for these definitions, ensuring consistent [validation](../modules/profiling-and-validation/) and data exchange across your FHIR ecosystem.

Aidbox stores canonical resources in a dedicated database schema called `far` (FHIR Artifact Registry). 
The registry automatically initializes during Aidbox startup using the `BOX_BOOTSTRAP_FHIR_PACKAGES` configuration, 
which typically includes core FHIR packages like `hl7.fhir.r4.core#4.0.1` and all specified Implementation Guides.

```mermaid
graph LR
    subgraph "Aidbox Database"
        A(Regular FHIR Resources</br>public schema):::neutral2
        B(Artifact Registry<br/>Canonical Resources</br>far schema):::blue2
    end
```

## Access Methods to the Artifact Registry

The Artifact Registry provides 2 ways to interact with canonical resources.
You can access registry contents programmatically through standard FHIR REST APIs or manage packages visually through Aidbox's web interface.

### Access via REST APIs

Canonical resources are accessible through standard FHIR REST endpoints following the [FHIR HTTP API specification](https://www.hl7.org/fhir/http.html). You can create, query, retrieve, and search canonical resources using familiar FHIR operations:

* `/fhir/CodeSystem` - Access code system definitions
* `/fhir/ValueSet` - Retrieve value set definitions
* `/fhir/StructureDefinition` - Manage [profiles](https://build.fhir.org/profiling.html) (including custom resources) and extensions
* `/fhir/SearchParameter` - Manage custom search parameters

### Access via Web Interface

Aidbox provides a web-based interface for package management operations. Through this UI, you can import FHIR packages from external registries, view installed packages and their contents, and delete packages when no longer needed.

<figure><img src="../../.gitbook/assets/far.jpg" alt=""><figcaption></figcaption></figure>

### Registry Scope and Limitations

The current implementation of the Artifact Registry focuses on four core types of canonical resources: CodeSystem, ValueSet, StructureDefinition, and SearchParameter. These resource types cover the most common use cases for FHIR validation and profiling. 
Aidbox doesn't currently store other FHIR canonical resource types like ConceptMap, NamingSystem, or ImplementationGuide 
in the registry but may add in future releases based on user requirements.

See also:

{% content-ref url="../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/" %}
[upload-fhir-implementation-guide](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/)
{% endcontent-ref %}

{% content-ref url="../tutorials/artifact-registry-tutorials/define-extensions/" %}
[define-extensions](../tutorials/artifact-registry-tutorials/define-extensions/)
{% endcontent-ref %}

{% content-ref url="../tutorials/artifact-registry-tutorials/custom-resources/" %}
[custom-resources](../tutorials/artifact-registry-tutorials/custom-resources/)
{% endcontent-ref %}

## Package Management

[FHIR packages](https://build.fhir.org/packages.html) are collections of canonical resources bundled together with their dependencies, following a standardized format for distribution and versioning. In the context of the Artifact Registry, packages serve as the primary mechanism for importing and organizing canonical resources. Each package includes metadata about its contents, dependencies on other packages, and version information following semantic versioning principles.

The registry automatically loads all package dependencies when you import a package.

```mermaid
flowchart RL
    A(Import US Core 6.0.0):::blue3
  
    B(us.nlm.vsac 0.9.0):::violet1 --->|depends on| A
    C(hl7.fhir.uv.sdc 3.0.0):::violet1  --->|depends on| A
    D(us.cdc.phinvads 0.12.0):::violet1  --->|depends on| A
    E(hl7.fhir.r4.core 4.0.1):::violet1  --->|depends on| A
    F(hl7.terminology.r4 5.4.0):::violet1  --->|depends on| A
  
    H(Aidbox Artifact Registry):::red2

    A --> H
```

### Integration with Package Registries

The Artifact Registry integrates with the Health Samurai FHIR package registry, which synchronizes with the [official FHIR packages repository](https://packages2.fhir.org/). This integration allows you to import packages from a curated collection stored in the public `fhir-schema-registry` bucket.

The ability to load packages from a URL allows you to import packages from any FHIR package registry, e.g. Simplifier.

```mermaid
flowchart RL
    A(Health Samurai Registry<br/>fhir-schema-registry):::violet2
    B(External Package Registries, e.g. Simplifier):::green1
    C(Local Filesystem):::yellow1
    
    E(Aidbox Artifact Registry):::red2
    
    A -->|Import| E
    B -->|Import| E
    C -->|Import| E
```

See also:

{% content-ref url="../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/aidbox-ui/public-url-to-ig-package.md" %}
[public-url-to-ig-package.md](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/aidbox-ui/public-url-to-ig-package.md)
{% endcontent-ref %}

{% content-ref url="../tutorials/artifact-registry-tutorials/how-to-create-fhir-npm-package.md" %}
[how-to-create-fhir-npm-package.md](../tutorials/artifact-registry-tutorials/how-to-create-fhir-npm-package.md)
{% endcontent-ref %}

## Versioning Strategy

Canonical resources can reference each other using versioned URLs (e.g., `http://example.com/StructureDefinition/Patient|1.0.0`).
If you specify a version, the registry uses it exactly. Otherwise, it automatically picks the latest version.

See also:

{% content-ref url="../tutorials/terminology-tutorials/local-terminlogy.md" %}
[local-terminlogy.md](../tutorials/terminology-tutorials/local-terminlogy.md)
{% endcontent-ref %}
