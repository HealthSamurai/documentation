---
description: Overview of Aidbox features
---

# Features

### REST API - FHIR, Aidbox, GraphQL, Reactive API & Subscriptions

Aidbox is a FHIR Server and then more. We developed FHIR API, Subscriptions, GraphQL, and SQL API, which all engineers love. Users can extend Aidbox API with Custom Operations.

Aidbox supports all major FHIR versions: DSTU2, STU3, R4, R4B and R5. Strict validation ensures data consistency and integrity for all FHIR resources. With [Subscriptions](https://docs.aidbox.app/api-1/reactive-api-and-subscriptions), users can execute custom logic in their applications when specific data is changing.

### FHIR-aware PostgreSQL with SQL on FHIR support

Aidbox uses PostgreSQL exclusively but squeezes everything out of this database technology. Most of Aidbox flexibility and performance is coming from advanced PostgreSQL features like binary JSON, rich indexing system, etc. SQL is the second Aidbox API, which gives you extra power on structured data. Read more about our [database internals](https://docs.aidbox.app/storage-1/database).

### OAuth & OIDC; User Management (SCIM)

Aidbox has built-in [OAuth 2.0](../modules/security-and-access-control/) OpenID Connect server and can work as Resource Server.

### Flexible Access Control & Audit Log

Flexible security rules allow granular [access control](../modules/security-and-access-control/security/) to healthcare application data. Aidbox [audit log](../modules/security-and-access-control/audit/audit-logging.md) records details about every event in the system to provide necessary data for security analysis and compliance with HIPAA, HITECH act, and other regulations.

### Built-in Terminology

[Aidbox terminology](../modules/terminology/) comes with FHIR, ICD-10, SNOMED, RxNorm, LOINC, and US NPI. Users can extend it with other terminologies and custom value sets.

### Custom Resources & Operations; First-Class Extensions

Not all healthcare data fits the FHIR data models. Aidbox allows adding [custom resources](https://docs.aidbox.app/modules-1/custom-resources) and [attributes](https://docs.aidbox.app/modules-1/first-class-extensions) with an easy update of metadata over RESTful API.

### Integration adapters

Aidbox comes with [HL7 v.2](https://docs.aidbox.app/modules-1/hl7-v2-integration) and X12 integration modules. Not all the systems that interact with your modern healthcare application speak FHIR yet. Support of other interoperability standards takes a lot of burden from developers.

### Bulk API and analytics tools integrations

Most electronic medical record solutions need reporting and data analytics capabilities. Aidbox integrates with Tableau, Power BI, and Jupyter. [Bulk API](https://docs.aidbox.app/api-1/bulk-api-1) makes the extraction of data from Aidbox easy.

### Plugins extensibility with the use of your favorite technology

Aidbox offers a vibrant ecosystem of plugins that extend Aidbox by adding new resources and operations for specific customer needs.

### SDK

Aidbox integrates quickly and easily with an [SDK](https://docs.aidbox.app/aidbox-sdk) that supports your development team's language of choice.

### Cloud infrastructure

Modern healthcare systems live in the cloud, and we designed Aidbox for the cloud. Aidbox comes with an automated cloud infrastructure built on Kubernetes for deployment to Amazon AWS, Google Cloud Platform & Microsoft Azure.
