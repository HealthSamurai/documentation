# Overview

Aidbox is a platform to build FHIR-first applications.
Aidbox is performant, highly customizable and metadata-driven system. 
All tables, views and APIs are on-fly generated and configured from metadata. 
You can dynamically load FHIR and custom resource definitions, profiles and search parameters 
from IGs or create your own.


Aidbox provides you with:

* FHIR Database - Store data as JSONB in PostgreSQL, search any resource by any attribute, join and aggregate data,
execute transactional SQL.
* Artifact Repository - Manage profiles, search parameters, code systems & value sets, load Implementation Guides, validate resources, and lookup in terminology
* FHIR+ & SQL API - Change and search FHIR resources, import and export, execute SQL on FHIR resources.
* Auth  -  Manage users and apps, Secure API with Access Policies and Security Labels, integrate with external IdP (Okta, Azure AD, Google, etc)
* Admin UI - Introspect and manage resources and configuration in UI
* SDKs - Build FHIR-first applications with SDKs for different programming languages. Get started with example projects.
* Plugins - Extend  Aidbox with additional plugins like Aidbox Forms (FHIR SDC), Master Patient Index, HL7v2, CCDA & X12 Converters, etc.

![arch](/.gitbook/assets/architecture.jpeg)


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

We store resources almost as is in JSONB columns. FHIR Search API and ViewDefinition runner generate
sophisticated queries to search resources by any attribute. For performance you can create indexes.

```sql
CREATE TABLE patient (
    id text PRIMARY KEY DEFAULT gen_random_uuid(),
    -- creation time
    cts timestamp with time zone DEFAULT now(),
    -- last modification time
    ts timestamp with time zone DEFAULT now(),
    -- resource content
    resource jsonb NOT NULL
);

SELECT count(*) FROM patient;
```

[Read more about FHIR Database](/database/README.md)

## FHIR Artifact Repository

FHIR Artifact Repository (FAR) is a component that manages FHIR Canonical resources.
FAR is a source of metadata for API, SDKs and other components.
It supports FHIR Packages, Implementation Guides, Terminology, etc.
FAR understands FHIR Canonical resources and relationships between them.

FAR provides FHIRSchema-based validation engine to validate FHIR resources.

TBD: Hybrid terminology

[Read more about FHIR Artifact Repository](/far/README.md)

## FHIR+ API

On top of database Aidbox provides FHIR API:

* CRUD, PATCH and HISTORY operations for all FHIR and Custom resources
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
* SMART on FHIR launch and Patient API

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


## Admin UI

Aidbox provides a user-friendly configuration and administration UI.

* IG loader and viewer
* Resource Browser
* Notebooks
* REST Console
* DB Console
* DB Viewer


## SDKs and Extensibility Framework

Aidbox can be extended with Custom Resources and Custom Operations.
Aidbox can work with external FHIR SDKs like HAPI, FHIR.NET, etc.
But because of dynamic nature we provide our own SDKs generators 
for different programming languages, with support for Custom Resources,
Profiles and terminology.

* TypeScript SDK
* Python SDK
* C# SDK

Generator framework is based on [FHIR TypeSchema](TBD) and very flexible and expressive.
It's a matter of couple of days to generate SDKs with respect to your conventions and preferences
for any language.

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