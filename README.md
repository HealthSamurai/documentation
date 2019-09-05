---
description: >-
  Aidbox is the backend development platform for your modern healthcare
  application on FHIR.
---

# Aidbox

Aidbox is a Backend for Health Care applications,  which provides you with 80% of typical needs:

* FHIR Informational Model
* Transactional PostgreSQL Storage \(based on best practices of our open source [fhirbase](https://www.health-samurai.io/fhirbase)  project\)
* Flexible schema and dynamic validation
* REST API for CRUD and Search
* OAuth 2.0 Server and Access Control
* Terminology Service

[Read Features Overview](features.md)

With Aidbox you can easily develop new healthcare products and services.  On top of the platform, you capable to build Mobile and Web Applications or integrations for existing electronic health record systems.

[Aidbox](https://www.health-samurai.io/aidbox) is a metadata driven platform.  It means that almost everything is represented as data \(resources\). For example in Aidbox, REST endpoints \(Operations\), Resources Definitions, Profiles, Access Policies, etc are represented as Resources - we call it Meta-Resources. Meta-Resources play by same rules as other resources  - you can request and manipulate Meta-Resources through unified REST API. 

### Get your Box

Register in  [Aidbox.Cloud](https://www.health-samurai.io/aidbox) and create your personal boxes. Each box is an instance of a FHIR server with a separate database and domain. You can create multiple boxes for development, staging and  production. For local development you can run [Aidbox.Dev](installation/setup-aidbox.dev.md) in docker. For production you can buy [Aidbox.One](installation/deploy-aidbox.one.md) or [Aidbox.Enterprise](installation/aidbox.enterprise.md) editions.

### FHIR & Aidbox

Aidbox implements most of [FHIR specification](https://www.hl7.org/fhir/) and supports all official versions of this standard. In addition Aidbox has a lot of useful in "real-life" extensions. Aidbox is designed to be **FHIR** compatible, but uses its own framework.  The key differences are listed below:

* Resources are stored in [Aidbox Format](basic-concepts/#aidbox-and-fhir-formats), which is isomorphic to FHIR, but not the same.
* Aidbox serves two API    from `/` - **Aidbox API** and `/fhir` - **FHIR API**. Aidbox API work with Aidbox Format and FHIR work with FHIR format. When you interact with FHIR endpoints Aidbox does on-fly conversion between this two formats.
* Aidbox supports **First-Class Extensions** and **Custom Resources**, which are prohibited in FHIR, but very handy in "real" systems.
* Aidbox use its own Entity/Attribute, SearchParameter and AidboxProfile framework instead of FHIR Structure Definitions. FHIR Profiles should be converted into Aidbox meta-resources.

### Resources

In Aidbox everything is a **Resource**! Each resource type is described with special **Entity** and **Attribute** meta-resources. **Entity** describes resources and types. **Attributes** describe structure of resources and complex types. For each **Entity** Aidbox generates database schema in PostgreSQL,  REST endpoints for CRUD, History, Search and other operations and JSON-schema for validation. 

### Aidbox & FHIR Format

Aidbox stores FHIR resources almost as is with 3 types of isomorphic transformations:

* References - more structured references
* Union \(Choice Types\) - 
* First-Class Extensions

[Read More](basic-concepts/aidbox-and-fhir-formats.md)

