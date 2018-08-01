---
description: Basic concepts in aidbox
---

# Architecture Overview

Aidbox is a metadata driven platform. What does it mean? It means, that we are making almost everything to be represented as data \(resources\). For example in aidbox REST endpoints \(Operations\), Resources Structure Definitions, Profiles, Access Policies, Periodic Jobs etc are represented as Resources - we call it Meta-Resources. Meta-Resources are play by same rules as other resources  - you can request and manipulate Meta-Resources through unified REST API. 

### Box

If you already have an Aidbox account, then you can create your own boxes. Each box is an instance of FHIR server with a separate database and URL.

For example, you can create several boxes for development, one box for staging, and another for production.

### Resources

In aidbox you define resource, describing it by metadata, then aidbox on fly will generates storage schema in PostgreSQL to save instances of your resource, generates REST routing for CRUD, History and Search Operations, generates json-schema to validate resources. While creating new 

Structure of Resource is defined by to Meta-Resources

### Operations



