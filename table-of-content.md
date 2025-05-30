* Overview (bento-style)
* Getting Started (Install locally or try in Sandbox)
* Tutorials
* Architecture
* Features
* Top Level
  - FHIR Configuration  (configuration, registry, ...) <- terminology
  - API
  - Auth
  - Database
  - Extensibility: Apps & SDK
  -----
  - Addon / Plugin / Extensions (Additional Modules)
  - Configuration & Deployment
* API
  - FHIR CRUD & Transactions
    * Create
    * Validation
    * Read
    * Update
    * Delete
    * History
    * Batch & Transaction
  - FHIR Search
    * SearchParameter
    * Include and Revinclude
    * Chained parameters
    * Performance Considerations
  - Aidbox Search 
  - SQL API
    * SQL on FHIR - ViewDefinitions
    * AidboxQuery
  - Bulk Data APIs
    * Import
    * FHIR Bulk Export
  - GraphQL
  - Compartments API
* Subscriptions & Reactive API
  - Topic based subscriptions
  - Polling for changes  
* Auth
  - Identity Management
    * Users
    * Applications (Clients)
  - Authentication
    * Basic
    * OAuth
    * SSO with External IdentityProvider
    * Token Introspection
  - Authorization (Access Control)
    * Access Policies
    * SMART Scopes
    * Scoped APIs
      - Organization-based hierarchical access control
      - Compartments API
      - Patient data access API
    * Security Labels Framework
  - Audit & Logging
    * Audit Events in Aidbox
    * Standalone Audit Event Repository  
* Database
  - Database Schema & Storage Format 
  - Query Resources & Search API SQL
  - Aidbox's functions
  - Indexes & Performance
  - Transactional bulk & migrations
  - Transactional isolation levels & conditional operations
  - Database migrations: Which PostgreSQL user permissions is needed for migrations?
  - AidboxDB
    - Releases/versions
    - PostgreSQL Extensions
    - HA AidboxDB (CloudNativePGOperator/Crunchy)
       
* Storage (TODO: Should we put it to API or keep at the top level)
  - Storing Files  
* FHIR Configuration (Artifact Repository)
  - Choose FHIR Version
  - Load IG (FHIR Packages)
  - Structure Definitions (Logical Models & Profiles)
  - Define Extensions
  - Terminology (Value Sets & Code Systems)
  - Custom Resources
* Extensibility: SDK and Apps!
  - Setting up the project
  - Type Generation
  - TypeScript
  - Python
  - C#
  - Template Projects
  - Aidbox Apps
* Plugins & Integrations (Addons)
  - Aidbox Forms (FHIR SDC)
  - Master Patient Index
  - Audit Logs
  - Termbox
  - Integration
   * HL7v2
   * CCDA
   * X12
* Configuration & Deployment & Maintaince
  - Environments
  - Helm
  - Terraform
  - CI/CD
  - Observability
  - Backups
  - Performance considerations
  - Production Readiness
    * HA
    * Blue/green Deployment
    * Disaster Recovery
    * Security Checklist
    * Production Readiness Checklisk
* Reference
* Resources
  - Licensing and Pricing
  - Release Notes
  - Release Cycle
  - Secure Software Development Policies:
    * How we deal with vulnerabilities (scans, pen-tests)
    * How we protect our images (signing)
  - Compliance
    * HIPAA
    * SOC2
    * ISO/HITRUST
  - Support
  - User Resources
    * Issue tracker
    * User chat (Zulip)
    * Aidbox Examples
    * Newsletter
    * Contact Health Samurai

    


Smartbox?
