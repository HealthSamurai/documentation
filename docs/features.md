---
description: Aidbox FHIR server features including CRUD, search, validation, bulk operations, terminology, security, and analytics.
---

# Features

### Core FHIR Capabilities

* Supports STU3, R4, R4B, R5, [R6 ballot3](tutorials/other-tutorials/run-aidbox-with-fhir-r6.md)
* Full [CRUD](api/rest-api/crud/README.md), [history/versioning](api/rest-api/history.md), conditional operations, [transactions](api/rest-api/bundle.md)
* High-performance rich-featured [validation](modules/profiling-and-validation/fhir-schema-validator/README.md)
* [FHIRPath](api/rest-api/crud/patch.md#fhirpath-patch) support for updating, filtering, derived values, and expressions
* Advanced [Search](api/rest-api/fhir-search/README.md):
  * [\_include, \_revinclude](api/rest-api/fhir-search/include-and-revinclude.md), [\_has, chained parameters](api/rest-api/fhir-search/chaining.md), \_filter, \_list
  * Custom SearchParameters
  * Full-text search with optimized indexing
* Full implementation of Structured Data Capture (SDC): $extract, $populate, Questionnaire, and QuestionnaireResponse
* [Bulk import](api/bulk-api/import-and-fhir-import.md) and [export](api/bulk-api/export.md)

### Database

* PostgreSQL JSONB data storage model
* Advanced data access
* Analytics and reporting on FHIR data
* Deployable on self-hosted and [managed PostgreSQL services](deployment-and-maintenance/deploy-aidbox/run-aidbox-on-managed-postgresql.md) (AWS, GCP, Azure)
* Rich [Indexing](deployment-and-maintenance/indexes/README.md)
* Support for [read-only replica](database/overview.md#postgresql-with-read-only-replica) for delegating read-only workload

### FHIR Configuration

* [FHIR Package Registry](artifact-registry/artifact-registry-overview.md) including 500+ ready-to-use FHIR IGs
  * Load official IGs from FHIR Package registry (e.g., US Core, IPS, DE Basisprofil, ISIK, AU Core, CL Core, etc.)
  * Load custom IGs as FHIR packages using UI
  * [Integration with external NPM package registries](artifact-registry/artifact-registry-overview.md#integration-with-package-registries)
  * [Pinning and tree shaking](artifact-registry/artifact-registry-overview.md#pinning-and-tree-shaking) for optimizing IG dependency resolution
  * Support for multiple versions of the same IG
* GUI and API support for loading FHIR IGs and canonicals
* Support for [external terminology services](terminology-module/overview.md)
* [Terminology supplements](terminology-module/aidbox-terminology-module/capabilities.md) for CodeSystems and ValueSets

### Customization & Extensibility

* [Custom resource types](tutorials/artifact-registry-tutorials/custom-resources/custom-resources-using-structuredefinition.md), extensions
* Custom search parameters and operations
* Custom logic via [Aidbox Apps](developer-experience/apps.md)
* [AidboxTrigger](modules/other-modules/aidbox-trigger.md) for automatic SQL execution in response to FHIR resource operations

### Advanced Data Access

* [SQL API](api/rest-api/other/sql-endpoints.md)
* [SQL-on-FHIR](modules/sql-on-fhir/README.md) for analytics and reporting
  * [$run operation](modules/sql-on-fhir/operation-run.md) for direct querying through ViewDefinition
  * [$materialize operation](modules/sql-on-fhir/operation-materialize.md) for creating database tables/views
  * [AI-assisted ViewDefinition generation](modules/aidbox-forms/aidbox-ui-builder-alpha/ai-assistant.md)
* [GraphQL API](api/graphql-api.md) for nested and filtered access (including contained resources)
* REST-exposed [SQL endpoints](api/rest-api/other/sql-endpoints.md)

### Subscriptions

* [Topic-based FHIR Subscriptions](modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md) (R4B/R5 compliant)
* Multi-destination push:
  * [Kafka](tutorials/subscriptions-tutorials/kafka-aidboxtopicdestination.md)
  * [GCP Pub/Sub](tutorials/subscriptions-tutorials/gcp-pub-sub-aidboxtopicdestination.md)
  * [AMQP](modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md#currently-supported-channels)
  * [ClickHouse](tutorials/subscriptions-tutorials/clickhouse-aidboxtopicdestination.md) for real-time analytics
  * Webhook
* [Organization-based hierarchical filtering](modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md#organization-based-hierarchical-filtering) in subscriptions
* %previous and %current functions for tracking changes between resource versions
* [Changes API](api/other/changes-api.md) for polling resource changes

### Security, Identity & Access Control

* [OAuth 2.0, OpenID Connect](access-control/authentication/README.md), Basic Auth, [SSO](access-control/authentication/sso-with-external-identity-provider.md), SCIM
* [External identity providers](tutorials/security-access-control-tutorials/set-up-external-identity-provider.md): [GitHub](tutorials/security-access-control-tutorials/github.md), [Apple](tutorials/security-access-control-tutorials/apple.md), [Okta](tutorials/security-access-control-tutorials/okta.md), [Azure AD](tutorials/security-access-control-tutorials/azure-ad.md), [Keycloak](tutorials/security-access-control-tutorials/keycloak.md)
* [SMART App Launch](modules/smartbox/README.md) (EHR and standalone)
  * SMART-on-FHIR v1 and v2 scopes
  * [SMART scopes with search parameters](access-control/authorization/smart-on-fhir/smart-scopes-for-limiting-access.md#scopes-with-search-parameters)
* Access control: [RBAC, ABAC](access-control/authorization/access-policies.md)
* [Label-based Access Control](access-control/authorization/label-based-access-control.md) with [Security Labels](access-control/authorization/label-based-access-control.md#what-are-security-labels)
* [Scoped APIs](access-control/authorization/scoped-api/README.md)
* Multitenancy: physical isolation (Multibox), [Organization-based hierarchical access control](access-control/authorization/scoped-api/organization-based-hierarchical-access-control.md)
* [AuditEvent logging](access-control/audit-and-logging.md) with support for [external FHIR AuditRecord Repository](access-control/audit-and-logging.md#external-audit-record-repository-support)

### Terminology

* [Terminology engine](terminology-module/overview.md) with local and external server support
* [$translate operation](terminology-module/aidbox-terminology-module/capabilities.md) on ConceptMap resources
* $expand, $validate-code, $lookup operations
* Supplements for CodeSystems and ValueSets

### Integrations

* HL7 v2 inbound module
* [C-CDA bidirectional converter](modules/integration-toolkit/ccda-converter/README.md)
* X12 support (e.g., 270/271, 837)
* [MCP Server](modules/other-modules/mcp.md) for AI-assisted FHIR development

### Deployment & Operations

* Kubernetes-native (on AWS, Azure, GCP, OpenShift, etc)
* On-premises installations
* Deployment to air-gapped environments
* Horizontal scaling
* [Helm charts](https://github.com/Aidbox/helm-charts)
* HIPAA-compliant architecture
* [OpenTelemetry](modules/observability/README.md) protocol for metrics, traces, and structured logs
* Performance monitoring tools
* [Init Bundle](configuration/init-bundle.md) for simple and effective configuration
* [Settings API](configuration/settings.md) for runtime configuration

### File Storage

* [GCP Cloud Storage](file-storage/gcp-cloud-storage.md) with [Workload Identity](file-storage/gcp-cloud-storage.md#workload-identity-recommended-since-2510)
* [Azure Blob Storage](file-storage/azure-blob-storage.md) with [Workload Identity](file-storage/azure-blob-storage.md#workload-identity)
* [AWS S3](file-storage/aws-s3.md)

### Developer experience and tools

* [Local installation](getting-started/run-aidbox-locally.md) support and cloud sandboxes
* Administrative UI
  * REST and SQL consoles
  * Notebooks
  * FHIR resource browser with version history and resource diffs
* Runtime-editable configuration
* SDKs for TypeScript, Python
* [FHIR Schema Code Generator](https://github.com/fhir-schema/fhir-schema-codegen) for TypeScript, C#, Python
* Template projects and [examples](https://github.com/Aidbox/examples) for quick start
* [User community](https://connect.health-samurai.io/)

### Scalability and Performance

* Aidbox's storage capacity is directly tied to PostgreSQL's capabilities. We have production installations handling 20+ TBs of data.
* Performance:
  * \~2,500 resources per second using standard RESTful CRUD operations (POST with validation) under concurrent load (300 threads)
  * \~3,500 resources per second using FHIR transaction bundles (bulk inserts of 10–100 resources each)
  * Bulk Import: Up to 21,000 resources per second using the optimized /v2/fhir/$import endpoint
  * Bulk Export: Up to 15,500 resources per second during /fhir/$export of 100M resources

Load performance testing results [here](https://www.health-samurai.io/downloads/aidbox-performance-report)

### High-availability and Disaster Recovery

* Cloud-native: AWS, Azure, GCP, hybrid, and private
* Zero-downtime updates
* Support for Kubernetes-native HA deployments
* Support for [HA PostgreSQL](database/aidboxdb-image/ha-aidboxdb.md) configurations with replication, automated failover, and point-in-time recovery

### Modules

* [Aidbox Forms](modules/aidbox-forms/README.md):
  * Ready-made medical form repository ([Aidbox Form Gallery](modules/aidbox-forms/aidbox-form-gallery.md))
  * [UI Builder](modules/aidbox-forms/aidbox-ui-builder-alpha/README.md) for creating forms without code (based on [FHIR SDC Implementation Guide](https://build.fhir.org/ig/HL7/sdc/index.html))
  * [AI assistance](modules/aidbox-forms/aidbox-ui-builder-alpha/ai-assistant.md) for creating and editing forms from text descriptions
  * [Adaptive forms](modules/aidbox-forms/adaptive-forms.md) with $next-question operation
  * [Template-based extraction](modules/aidbox-forms/aidbox-ui-builder-alpha/form-creation/form-settings.md#template-based-extraction)
  * [FHIRPath expression editor](modules/aidbox-forms/aidbox-ui-builder-alpha/form-creation/fhirpath-editor.md)
  * [Offline forms](modules/aidbox-forms/aidbox-ui-builder-alpha/offline-forms.md)
  * [Multilingual forms](modules/aidbox-forms/aidbox-ui-builder-alpha/form-creation/multilingual-forms.md)
  * [OpenEHR Templates conversion](modules/aidbox-forms/aidbox-ui-builder-alpha/import-questionnaire.md#id-3.-selecting-an-openehr-template-file)
  * [NHS design system](modules/aidbox-forms/aidbox-ui-builder-alpha/form-creation/nhs-look-and-feel.md) support
  * [External FHIR servers](modules/aidbox-forms/aidbox-ui-builder-alpha/external-fhir-servers-as-a-data-backend.md) as data backend
  * [Multitenancy](modules/aidbox-forms/aidbox-ui-builder-alpha/forms-multitenancy.md) support
  * [Web component embedding](modules/aidbox-forms/aidbox-ui-builder-alpha/embedding.md)
* [ePrescriptions](modules/eprescription/README.md)
* [MPI](modules/mpi/README.md) (Master Patient Index)
* [Smartbox FHIR API](modules/smartbox/README.md) for health plans and EHRs
* [Audit record repository](access-control/audit-and-logging.md#aidbox-as-an-audit-record-repository)

### Compliance & Certifications

* SOC 2 and ISO 27001-certified
* HIPAA, HITECH, and GDPR compliant
* Secure SDLC: vulnerability scans, dependency SBOM, etc.
* [Audit and traceability](access-control/audit-and-logging.md) for all access
