# Architecture Overview

[Aidbox](https://www.health-samurai.io/aidbox) is a metadata driven platform. What does it mean? It means that we are making almost everything to be represented as data \(resources\). For example in Aidbox, REST endpoints \(Operations\), Resources Structure Definitions, Profiles, Access Policies, Periodic Jobs, etc are represented as Resources - we call it Meta-Resources. Meta-Resources play by same rules as other resources  - you can request and manipulate Meta-Resources through unified REST API. 

### Box

If you already have an [Aidbox](https://www.health-samurai.io/aidbox) account, then you can create your own boxes. Each box is an instance of a FHIR server with a separate database and URL.

For example, you can create several boxes for development, one box for staging, and another for production.

### Resources

In [Aidbox](https://www.health-samurai.io/aidbox), you define a resource by describing its metadata then Aidbox will generate storage schema on the fly in PostgreSQL to save instances of your resource, generate REST routing for CRUD, History, and Search Operations, generate JSON-schema to validate resources.

### Operations



