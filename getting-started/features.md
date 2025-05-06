---
description: Overview of Aidbox features
---

# Features

### Core FHIR Capabilities

* Supports STU3, R4, R4B, R5, R6 ballot3.
* Full CRUD, history/versioning, conditional operations, transactions
* High-performance rich-featured validation
* FHIRPath support for updating, filtering, derived values, and expressions
* Advanced Search:
  * \_include, \_revinclude, \_has, chained parameters, \_filter, \_list
  * Custom SearchParameters
  * Full-text search with optimized indexing
* Full implementation of Structured Data Capture (SDC): $extract, $populate, Questionnaire, and QuestionnaireResponse
* Bulk import and export

### Database&#x20;

* PostgreSQL JSONB data storage model
* Advanced data access
* Analytics and reporting on FHIR data
* Deployable on self-hosted and managed PostgreSQL services (AWS, GCP, Azure)
* Rich Indexing

### FHIR Configuration

* &#x20;FHIR Package Registry including 500+ ready-to-use FHIR IGs.
  * Load official IGs from FHIR Package registry (e.g., US Core, IPS, DE Basisprofil, ISIK, AU Core, CL Core, etc.)
  * Load custom IGs as FHIR packages using UI
* GUI and API support for loading FHIR IGs and canonicals&#x20;
* Support for external terminology services

### Customization & Extensibility

* Custom resource types, extensions,&#x20;
* Custom search parameters and operations&#x20;
* Custom logic via Aidbox Apps

### Advanced Data Access

* SQL API
* SQL-on-FHIR for analytics and reporting&#x20;
* GraphQL API for nested and filtered access
* REST-exposed SQL endpoints

### Subscriptions & Reactive API

* Topic-based FHIR Subscriptions (R4B/R5 compliant)
* Multi-destination push: Kafka, GCP Pub/Sub, webhook
* Polling APIs for resource sync

### Security, Identity & Access Control

* OAuth 2.0, OpenID Connect, Basic Auth, SSO, SCIM
* SMART App Launch (EHR and standalone)
* Access control: RBAC, ABAC
* Scoped APIs&#x20;
* Security Labels&#x20;
* Multitenancy: physical isolation of the databases - Multibox, logical multitenancy  - Organization-based access control
* AuditEvent logging&#x20;

### Integrations

* HL7 v2 inbound module
* C-CDA bidirectional converter
* X12 support (e.g., 270/271, 837)

### Deployment & Operations

* Kubernetes-native (on AWS, Azure, GCP, OpenShift, etc)
* On-premises installations
* Deployment to air-gapped environments
* Horizontal scaling
* Helm charts
* HIPAA-compliant architecture
* OpenTelemetry protocol for metrics, traces, and structured logs
* Performance monitoring tools

### Developer experience and tools

* Local installation support and cloud sandboxes
* Administrative UI
  * REST and SQL consoles
  * Notebooks
  * FHIR resource browser
* Runtime-editable configuration&#x20;
* SDKs for TypeScript, Python
* Code-generation support for creating your own SDKs, with examples provided for Python, TypeScript, and C#
* Template projects and examples for quick start
* Open user community

### Scalability and Performance

* Aidbox’s storage capacity is directly tied to PostgreSQL’s capabilities. We have production installations handling 20+ TBs of data.
* Performance:
  * \~2,500 resources per second using standard RESTful CRUD operations (POST with validation) under concurrent load (300 threads)​
  * \~3,500 resources per second using FHIR transaction bundles (bulk inserts of 10–100 resources each)​
  * Bulk Import: Up to 21,000 resources per second using the optimized /v2/fhir/$import endpoint​
  * Bulk Export: Up to 15,500 resources per second during /fhir/$export of 100M resources​

Load performance testing results [here](https://www.health-samurai.io/downloads/aidbox-performance-report)

### High-availability and Disaster Recovery

* Cloud-native: AWS, Azure, GCP, hybrid, and private
* Zero-downtime updates
* Support for Kubernetes-native HA deployments
* Support for HA PostgreSQL configurations with replication, automated failover, and point-in-time recovery

### Modules

* Aidbox Forms:
  * Ready-made medical form repository ([Aidbox Form Gallery](https://docs.aidbox.app/modules/aidbox-forms/add-aidbox-forms-library))
  * [UI Builder](https://docs.aidbox.app/modules/aidbox-forms/aidbox-ui-builder-alpha) for creating forms without code (based on[ FHIR SDC Implementation Guide](https://build.fhir.org/ig/HL7/sdc/index.html))
  * Form rendering engine
  * FHIR  and Aidbox SDC APIs
* ePrescriptions
* Aidbox Billing
* MDM (Master Data Management)
* Smartbox FHIR API for health plans and EHRs
* Audit record repository

### &#x20;Compliance & Certifications

* and ISO 27001-certified&#x20;
* HIPAA, HITECH, and GDPR compliant
* Secure SDLC: vulnerability scans, dependency SBOM, etc.
* Audit and traceability for all access
