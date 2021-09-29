---
description: >-
  Aidbox is a backend development platform for modern healthcare applications on
  FHIR.
---

# Getting started

[Aidbox](https://www.health-samurai.io/aidbox) is a backend development platform for modern healthcare applications. Aidbox users have already built cloud EHR systems, patient-facing mobile applications, data-analytics products, and integration platforms.

Aidbox uses [FHIR](https://www.hl7.org/fhir/overview.html) as a foundation and provides developers with many components that different medical records applications can reuse:

* FHIR-aware PostgreSQL storage with SQL on FHIR support
* FHIR REST API with GraphQL, reactive API & subscriptions
* User management, access control, and audit log
* Built-in FHIR, ICD-10, SNOMED, RxNorm, LOINC, and US NPI terminologies with the ability to load your terminologies and value sets
* Custom resources & operations; first-class extensions
* HL7 v.2, X12, and Apple HealthKit Medical Records adapters
* Plugins extensibility with the use of your favorite technology
* Bulk API and analytics tools integrations that include Tableau, Power BI, and Jupyter
* Automated cloud infrastructure built on Kubernetes for AWS, Azure & GCP

​[Read Features Overview](https://docs.aidbox.app/features)​

![](.gitbook/assets/1-aidbox.jpg)

​[Aidbox](https://www.health-samurai.io/aidbox) is a metadata-driven platform. It means that almost everything in Aidbox is represented as data \(resources\). For example, REST endpoints \(operations\), resource definitions, profiles, and access policies are resources in Aidbox. We call them meta-resources. Meta-resources play by the same rules as other resources - you can request and manipulate meta-resources with the use of the unified REST API.

### Get your Aidbox

You can try Aidbox in the cloud or get a free Devbox version for development and evaluation on your local computer.

To get cloud sandbox, you need to log in with GitHub or Google account at [https://aidbox.app](https://aidbox.app/) and create a new Box. Each Box is a private instance of a FHIR server with a separate database and domain. You can create multiple boxes for development and evaluation.

To try Aidbox locally, you can get a free [Devbox](https://docs.aidbox.app/installation/setup-aidbox.dev) version in a Docker container. Do not forget that you can't load real healthcare data or PHI \(Protected Health Information\) to Aidbox cloud sandbox or Devbox. When setting up a staging or production environment for your healthcare application, please reach out to us for a paid Aidbox license.

Health Samurai provides all kinds of support for Aidbox; you can learn more about it at [Aidbox Licensing and Support](https://docs.aidbox.app/editions-and-pricing).

### FHIR & Aidbox

Aidbox implements most of the [FHIR specification](https://www.hl7.org/fhir/) and supports all official versions of the standard. We designed Aidbox to be FHIR-compatible, but it uses its own framework. Besides FHIR, Aidbox offers many useful additions. The key differences are listed below:

* Aidbox stores Resources in [Aidbox format](https://docs.aidbox.app/modules-1/fhir-resources/aidbox-and-fhir-formats), which is isomorphic to FHIR, but not the same.
* Aidbox serves two sets of API: **Aidbox API** from "/" and **FHIR API** from "/fhir**"**. Aidbox API works with Aidbox format, and FHIR API works with FHIR format. When you interact with FHIR endpoints, Aidbox does on-the-fly conversion between these two formats.
* Aidbox offers **First-Class Extensions** and **Custom Resources**, which FHIR doesn't support, but these additions are very handy for designing real-world systems.
* Aidbox uses its own Entity/Attribute, SearchParameter and AidboxProfile framework instead of FHIR Structure Definitions. FHIR Profiles should be converted to Aidbox meta-resources.

### Resources

In Aidbox, everything is a **Resource.** Each resource type is described with special **Entity** and **Attribute** meta-resources. **Entities** describe resources and types. **Attributes** describe the structure of resources and complex types. For each **Entity**, Aidbox generates database schema in PostgreSQL, REST endpoints for CRUD, history, search and other operations, and JSON-schema for validation.

### Aidbox & FHIR Formats

Aidbox stores FHIR resources almost as is with 3 types of isomorphic transformations:

* References
* Union \(Choice Types\)
* First-Class Extensions

[Read more](https://docs.aidbox.app/modules-1/fhir-resources/aidbox-and-fhir-formats)

