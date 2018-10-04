# Modules

Aidbox is split into modules. We have core module proto, modules for each FHIR version, and additional modules like OpenID Connect, Chat, etc.

Each module can consist of definitions  of:

* Entities and Attributes  - i.e. defines a set of new Resources and/or extensions for existing ones
* Operations - REST endpoints
* SearchParameters

For example, core module **proto** introduces core meta-resources like Entity, Attributes, and basic CRUD & Search Operations. FHIR modules are definitions of FHIR resources by Entity, Attributes which are imported from FHIR metadata.



