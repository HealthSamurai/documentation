# Overview

Aidbox is a platform to build FHIR-first applications.
Aidbox is dynamic, performant, flexible and highly customizable, metadata-driven system. 
All tables, views and APIs are on-fly generated and configured from metadata. 
You can load FHIR resources, profiles and search parameters from IGs or create your own.


Aidbox provides you with:

* FHIR Database - performant PostgreSQL-based JSONB storage, you can search any resource by any attribute, join and aggregate data,
write transactional SQL.
* Artifact Repository - manage Canonical resources for all versions of FHIR, with first-class support
for FHIR Packages, Implementation Guides and Terminology
* FHIR+ & SQL API - Comprehensive API for all FHIR resources and Custom resources
* Granular Access Control, including SMART on FHIR, Security Labels, OAuth 2.0 & OIDC, JWT, etc.
* Built-in Auth Server and integration with external Identity Providers (Okta, Azure AD, Google, etc)
* SDKs for different programming languages and example projects to help you get started
* Set of optional plugins like Admin UI, Aidbox Forms (FHIR SDC), MPI etc


TBD: architecture diagram


Aidbox has simple configuration - JVM Service (Clojure, Docker) + PostgreSQL (with support for managed PostgreSQL).
Aidbox can be installed on-premise or managed by Health Samurai in public clouds for you.
We provide free and fully functional local development environment and cloud sandboxes.

Health Samurai provides commercial support and consulting services for Aidbox 
and can help you to leverage FHIR & Our Platform to build future-proof
applications faster and with higher quality.

Aidbox was successfully used for more than decade to build Patient Portals and Telemedicine Platforms, Clinical Data Repositories, 
FHIR-native EHRs and EMRs, Billing Systems, Analytics Platforms and Clinical Decision Support Systems.
Checkout our [Case Studies](/casestudies/README.md) to learn more.




## FHIR Database

Aidbox uses PostgreSQL and JSONB columns to store and query FHIR resources.
Aidbox does not abstract database from developers and SQL is our primary
interface, which complements FHIR API.

Aidbox does not materialize Search Parameters, but generates sophisticated
SQL queries for all FHIR Search Parameters. We use more traditional approach 
to manage indexes for search parameters, it's up to you to decide which
search parameters to index.

[Read more about FHIR Database](/database/README.md)

## FHIR Artifact Repository

FHIR Artifact Repository (FAR) is a component that manages FHIR Canonical resources.
FAR is a source of metadata for API, SDKs and other components.
It supports FHIR Packages, Implementation Guides, Terminology, etc.
FAR understands FHIR Canonical resources and relationships between them.


[Read more about FHIR Artifact Repository](/far/README.md)

## FHIR+ API

On top of database Aidbox provides FHIR API:

* CRUD operations for all FHIR and Custom resources
* Configurable support for Resource's History
* Support for FHIR Batch and Transactions
* Bulk API to efficiently Import and Export resources
* FHIR Search - search any resource by any attribute
  * Dynamic SearchParameters
  * _includes and _revincludes
  * Chained parameters
* Subscriptions for handling real-time updates with WebSockets support and adapters for popular messaging systems (Kafka, NATS, etc)
* Built-in Terminology Service and integration with external Terminology Services
* SQL on FHIR  ViewDefinition runner
* PostgreSQL native SQL API over REST
* GraphQL API
* Other Standard and Custom Operations like compartments, $everything, $lookup etc


[Read more about FHIR+ API](/api/README.md)


## Authentication and Authorization

Aidbox provides a flexible security framework:

* Authentication and Authorization
* Built-in Identity Provider and integration with external Identity Providers (Okta, Azure AD, Google, etc)
* Access Control
  * Security Labels
  * Access Policies
* SMART on FHIR
* Patient andOrganization based API

[Read more about security in Aidbox](/security/README.md)

## SDKs and Extensibility Framework

Aidbox can be extended with Custom Resources and Custom Operations.
Aidbox can work with external FHIR SDKs like HAPI, FHIR.NET, etc.
But because of dynamic nature we provide our own SDKs generators 
for different programming languages.

* TypeScript SDK
* Python SDK
* C# SDK

We also provide a set of template and example projects to help you get started.

* BedaEMR - a complete open source EMR implementation on top of Aidbox
* HL7v2 with python

[Read more about SDKs in Aidbox](/sdk/README.md)

## Plugins

Aidbox offers optional plugins:

* Admin UI - resource browser, REST and DB Consoles
* FHIR SDC - Aidbox Forms
* AuditEvent Repository
* Probabilistic Master Patient Index
* E-prescription module for SureScripts
* HL7v2, CCDA to FHIR Converter, X12 to FHIR Converter


[Read more about plugins](/plugins/README.md)