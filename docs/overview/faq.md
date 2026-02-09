---
description: Frequently asked questions about Aidbox FHIR server covering API, authentication, validation, and deployment.
---

# FAQ

Frequently asked questions about Aidbox FHIR server.

<!-- SEO: The first H2 section is used for FAQ schema (JSON-LD). Keep important questions here. -->

## About Aidbox

### What is Aidbox?

Aidbox is an enterprise FHIR server and healthcare data platform built on PostgreSQL. It provides a complete solution for building healthcare applications with full FHIR compliance, advanced search, terminology services, authentication, and analytics capabilities.

Learn more: [Features](../features.md)

### Is Aidbox free?

Aidbox is a commercial product (not open source) but developer license is free. 

Learn more: [Licensing and Support](licensing-and-support.md)

### How do I get an Aidbox license?

Register at [aidbox.app](https://aidbox.app) to get a free development license.

### How do I run Aidbox locally?

Make sure Docker & Docker Compose are installed, then run:

```bash
mkdir aidbox && cd aidbox
curl -JO https://aidbox.app/runme && docker compose up
```

Open [http://localhost:8080](http://localhost:8080) and activate with your free Aidbox account.

Learn more: [Run Aidbox Locally](../getting-started/run-aidbox-locally.md)

### How do I run Aidbox in the cloud?

Aidbox offers a free cloud sandbox for evaluation and development. Sign up at the Aidbox portal to get started instantly without any local setup.

Learn more: [Start in the Cloud](../getting-started/run-aidbox-in-sandbox.md)

### What FHIR versions does Aidbox support?

Aidbox supports all FHIR versions starting from **R3** (STU3), including **R4**, **R4B**, **R5**, and [**R6**](../tutorials/other-tutorials/run-aidbox-with-fhir-r6.md) (ballot). You can configure the FHIR version when setting up your Aidbox instance.

Learn more: [Bootstrap FHIR package list](../reference/all-settings.md#bootstrap-fhir-packages)

### How FHIR compliant is Aidbox?

Aidbox strives for **100% FHIR compliance**. We continuously test against the FHIR specification and fix any deviations. If you find a compliance issue, please report it on [Aidbox Zulip](https://connect.health-samurai.io) — we'll fix it.

### What Aidbox SDKs are available?

Aidbox provides official SDKs for multiple languages:

- [TypeScript SDK](../getting-started/typescript.md)
- [Python SDK](../getting-started/python.md)
- [C# SDK](../getting-started/csharp.md)

### How do I get support?

- **Professional support** — Basic, Professional, Enterprise tiers with SLA
- **Professional services** — deployment, migration, training, and custom development

Learn more: [Licensing and Support](licensing-and-support.md)

### What are the key Aidbox features?

Aidbox provides comprehensive FHIR capabilities including:

- Multi-version FHIR support (STU3, R4, R4B, R5, R6)
- Full CRUD, transactions, conditional operations, versioning
- Advanced search with custom SearchParameters
- SQL on FHIR for analytics
- Topic-based subscriptions
- Profiling and validation with 500+ IGs
- SMART on FHIR authorization
- Hybrid terminology engine
- and more

Learn more: [Features](../features.md)

### What is the difference between Aidbox latest and edge versions?

Aidbox provides several release channels:

- **edge** — most recent build, published after every commit. May have issues. Use for testing new features.
- **latest** — last minor version of current release, monthly updates. Passed QA. Use for active development.
- **stable** — last minor version of previous release. Production-ready.

Learn more: [Versioning](versioning.md)

## FHIR Basics

### What are the differences between FHIR versions?

| Version | Release | Status | Notes |
|---------|---------|--------|-------|
| DSTU2 | 2015 | Legacy | Outdated, limited adoption |
| STU3 (R3) | 2017 | Legacy | Still used in some systems |
| R4 | 2019 | **Normative** | Most widely adopted, industry standard |
| R4B | 2022 | Normative | Minor updates to R4, backported SubscriptionTopic |
| R5 | 2023 | Trial Use | New features, but breaking changes and limited adoption |
| R6 | ~2026 | Upcoming | Most resources will become normative, long-term stability |

Learn more: [FHIR R4 vs R5: Choosing the Right Version](https://www.health-samurai.io/articles/fhir-r4-vs-fhir-r5-choosing-the-right-version-for-your-implementation)

### Which FHIR version should I choose?

**Start with R4** — it's the industry standard with the best ecosystem support:

- Major EHRs (Epic, Oracle Health) only support R4
- US Core, IPS, and most Implementation Guides target R4
- Required by ONC regulations in the US

Consider **waiting for R6** rather than adopting R5 — R6 will lock most resources as normative (no breaking changes) and provide long-term stability. Many organizations plan to migrate directly from R4 to R6.

Learn more:
- [Getting Ready for FHIR R6 (blog)](https://www.health-samurai.io/articles/getting-ready-for-fhir-r6-what-you-need-to-know)
- [FHIR: What's Great and What Isn't (blog)](https://www.health-samurai.io/articles/fhir-what-is-great-what-isnt-so-good-and-what-it-is-not)

### What FHIR versions does Aidbox support?

Aidbox supports all FHIR versions starting from **R3** (STU3), including **R4**, **R4B**, **R5**, and [**R6**](../tutorials/other-tutorials/run-aidbox-with-fhir-r6.md) (ballot). You can configure the FHIR version when setting up your Aidbox instance.

Learn more: [Bootstrap FHIR package list](../reference/all-settings.md#bootstrap-fhir-packages)

### What is an Implementation Guide (IG)?

An Implementation Guide (IG) is a package of FHIR conformance resources (profiles, value sets, code systems, search parameters) published together for a specific use case or jurisdiction. Examples:

- [**US Core**](https://hl7.org/fhir/us/core/index.html) — US healthcare interoperability baseline
- [**IPA**](https://build.fhir.org/ig/HL7/fhir-ipa/index.html) (International Patient Access) — cross-border patient data access
- [**mCODE**](https://build.fhir.org/ig/HL7/fhir-mCODE-ig/index.html) — oncology data exchange
- [**C-CDA on FHIR**](https://build.fhir.org/ig/HL7/ccda-on-fhir/index.html) — mapping C-CDA to FHIR
- [**SMART App Launch**](https://build.fhir.org/ig/HL7/smart-app-launch/app-launch.html) — app authorization

### What is a FHIR Profile?

A FHIR Profile is a set of rules that narrows down or extends the base FHIR specification to fit a specific use case. Profiles are defined as `StructureDefinition` resources with `derivation: constraint`. For example, [US Core Patient](https://hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html) profile defines specific elements like race and ethnicity extensions.

Learn more:
- [HL7 FHIR Profiling](https://hl7.org/fhir/R4/profiling.html)
- [Profiling and Validation](../modules/profiling-and-validation/README.md)
- [FHIR Profiling (blog)](https://www.health-samurai.io/articles/fhir-profiling)
- [FHIR Profiling: Extensions (blog)](https://www.health-samurai.io/articles/fhir-profiling-extensions)
- [FHIR Profiling: Slicing (blog)](https://www.health-samurai.io/articles/fhir-profiling-slicing)

### How are Implementation Guides and Profiles related?

An **Implementation Guide contains Profiles**. The IG is the package, profiles are its main building blocks:

- **IG** = collection of profiles + value sets + code systems + search parameters + documentation
- **Profile** = constraints on a single FHIR resource type

For example, US Core IG contains 20+ profiles including US Core Patient, US Core Condition, US Core Observation, etc.

### What Implementation Guides does Aidbox support?

Aidbox supports **all** published FHIR Implementation Guides. You can load any IG from the official FHIR registry or upload custom IGs. Loaded IGs are stored in the Artifact Registry.

Learn more: [Artifact Registry Overview](../artifact-registry/artifact-registry-overview.md)

### What is the Artifact Registry?

The Artifact Registry is Aidbox's centralized storage for FHIR canonical resources:

- **StructureDefinitions** — profiles, extensions, and custom resource types
- **ValueSets** and **CodeSystems** — terminology definitions
- **SearchParameters** — custom search capabilities
- **ConceptMaps** — terminology mappings

Aidbox also supports **custom resources** — you can define entirely new resource types beyond the FHIR specification.

Learn more:
- [Artifact Registry Overview](../artifact-registry/artifact-registry-overview.md)
- [Custom Resources](../tutorials/artifact-registry-tutorials/custom-resources/README.md)
- [How to Create Custom Resources (blog)](https://www.health-samurai.io/articles/how-to-create-custom-resources-in-your-fhir-server)

### How do I create my own Implementation Guide?

Use [FHIR Shorthand (FSH)](https://build.fhir.org/ig/HL7/fhir-shorthand/) and [SUSHI](https://github.com/FHIR/sushi) to author profiles in a developer-friendly syntax, then compile to standard FHIR artifacts with the [IG Publisher](https://confluence.hl7.org/display/FHIR/IG+Publisher+Documentation).

Learn more: [How to Create a FHIR Implementation Guide](https://www.health-samurai.io/articles/how-to-create-a-fhir-implementation-guide)

### How do I load an Implementation Guide into Aidbox?

Aidbox provides multiple methods to load IGs:

**1. Environment variable (at startup):**
```
AIDBOX_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1
```

**2. Aidbox UI:** Navigate to FHIR Packages and select from the registry or upload a local package.

**3. Package API (since 2511):** Use `$fhir-package-install` operation:
```http
POST /fhir/$fhir-package-install
Content-Type: application/json

{"resourceType": "Parameters", "parameter": [{"name": "package", "valueString": "hl7.fhir.us.core@5.0.0"}]}
```

**4. FHIR API:** POST individual StructureDefinitions, ValueSets, and other conformance resources directly.

Learn more:
- [Upload FHIR Implementation Guide](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/README.md)
- [Package API](../reference/package-registry-api.md)

## API & CRUD Operations

### How FHIR compliant is Aidbox?

Aidbox strives for **100% FHIR compliance**. We continuously test against the FHIR specification and fix any deviations. If you find a compliance issue, please report it on [Aidbox Zulip](https://connect.health-samurai.io) — we'll fix it.

### How do I create a resource?

Use HTTP POST to the resource endpoint:

```http
POST /fhir/Patient
Content-Type: application/fhir+json

{"resourceType": "Patient", "name": [{"given": ["John"]}]}
```

Aidbox returns 201 Created with the Location header pointing to the new resource.

Learn more: [Create Operation](../api/rest-api/crud/create.md)

### How do I read a resource?

Use HTTP GET with the resource type and ID:

```http
GET /fhir/Patient/123
```

Learn more: [Read Operation](../api/rest-api/crud/read.md)

### How do I update a resource?

Use HTTP PUT to replace the entire resource:

```http
PUT /fhir/Patient/123
Content-Type: application/fhir+json

{"resourceType": "Patient", "id": "123", "name": [{"given": ["Jane"]}]}
```

Learn more: [Update Operation](../api/rest-api/crud/update.md)

### How do I delete a resource?

Use HTTP DELETE:

```http
DELETE /fhir/Patient/123
```

Learn more: [Delete Operation](../api/rest-api/crud/delete.md)

### What is the difference between PUT and PATCH?

- **PUT** replaces the entire resource — you must send the complete resource
- **PATCH** modifies only specific fields — you send only what you want to change

```http
PATCH /fhir/Patient/123
Content-Type: application/json-patch+json

[{"op": "replace", "path": "/birthDate", "value": "1990-06-15"}]
```

Learn more: [Patch Operation](../api/rest-api/crud/patch.md)

### How do I avoid creating duplicate resources?

Use **conditional create** with the `If-None-Exist` header to create a resource only if no matching resource exists:

```http
POST /fhir/Patient
If-None-Exist: identifier=http://example.org/mrn|12345
Content-Type: application/fhir+json

{"resourceType": "Patient", "identifier": [{"system": "http://example.org/mrn", "value": "12345"}]}
```

For updates, use **conditional update** with search parameters in the URL:

```http
PUT /fhir/Patient?identifier=http://example.org/mrn|12345
```

In **transaction Bundles**, use `ifNoneExist` in the request object for conditional create.

Learn more:
- [Conditional Create](../api/rest-api/crud/create.md#conditional-create)
- [Conditional Update](../api/rest-api/crud/update.md#conditional-update)
- [FHIR Conditional Create](https://hl7.org/fhir/R4/http.html#ccreate)

### How do I access resource version history?

Use the `_history` operation to retrieve previous versions of a resource:

```http
GET /fhir/Patient/123/_history
```

Every create, update, and delete creates a history entry. Results are sorted newest first.

Learn more: [History Operation](../api/rest-api/history.md)

### How do I get all data for a patient?

Use the `$everything` operation to retrieve all resources related to a patient:

```http
GET /fhir/Patient/123/$everything
```

Returns a Bundle containing the patient and all linked resources (Observations, Encounters, Conditions, etc.).

Learn more: [$everything on Patient](../api/rest-api/everything-on-patient.md)

### Why use FHIR transactions?

FHIR transactions ensure **atomicity** — all operations succeed or fail together. Use cases:

- Creating related resources (Patient + Encounter + Observations) that must be consistent
- Importing data where partial imports would leave the database in an invalid state
- Any multi-step operation where rollback on failure is required

In Aidbox, a FHIR transaction is a **real PostgreSQL transaction** under the hood, providing full ACID guarantees.

```json
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {"request": {"method": "POST", "url": "/Patient"}, "resource": {...}},
    {"request": {"method": "POST", "url": "/Encounter"}, "resource": {...}}
  ]
}
```

Learn more: [Batch/Transaction](../api/batch-transaction.md)

## FHIR Search

### How does FHIR Search work?

FHIR Search uses URL query parameters to filter resources:

```http
GET /fhir/Patient?name=John&birthdate=1990-01-01
```

Results are returned as a Bundle (a container resource for collections of resources) containing matched resources with pagination links.

Learn more: [FHIR Search](../api/rest-api/fhir-search/README.md)

### What search features are supported?

Aidbox supports **all** FHIR Search features:

- All search parameter types: string, token, reference, date, number, quantity, uri, composite
- All modifiers: `:exact`, `:contains`, `:missing`, `:not`, `:text`, `:in`, `:below`, etc.
- `_include` and `_revinclude` for fetching related resources
- Chaining and reverse chaining (`_has`)
- `_sort`, `_count`, `_total`, `_elements`
- `_filter` for advanced search expressions

If you find a compliance issue, please report it on [Aidbox Zulip](https://connect.health-samurai.io).

Learn more:
- [FHIR Search Specification](https://www.hl7.org/fhir/search.html)
- [Aidbox FHIR Search](../api/rest-api/fhir-search/README.md)
- [Resolving SearchParameter Conflicts (blog)](https://www.health-samurai.io/articles/resolve-fhir-searchparameters-conflicting)

### What if FHIR Search isn't enough?

Aidbox provides several alternatives when FHIR Search limitations are hit:

- **Aidbox Search** — extended syntax with dot expressions and custom parameters
- **SQL API** — direct SQL access to FHIR data
- **GraphQL API** — flexible queries with nested resource fetching

Learn more:
- [Aidbox Search](../api/rest-api/aidbox-search.md)
- [GraphQL API](../api/graphql-api.md)
- [SQL API](../api/rest-api/other/sql-endpoints.md)

## Access Control

### What authentication methods does Aidbox support?

Aidbox supports multiple authentication methods:

- Basic HTTP Authentication
- OAuth 2.0 (Authorization Code, Client Credentials, Resource Owner Password)
- Token Introspection for external IdPs
- SSO with external Identity Providers

Learn more: [Authentication](../access-control/authentication/README.md)

### How do I set up Basic authentication?

Basic auth is suitable for development and testing. Create a Client resource with a secret:

```yaml
resourceType: Client
id: my-client
secret: my-secret
grant_types: ["basic"]
```

Then use the Authorization header:

```http
Authorization: Basic base64(my-client:my-secret)
```

Learn more: [Basic HTTP Authentication](../access-control/authentication/basic-http-authentication.md)

### How do I configure OAuth 2.0?

Aidbox acts as an OAuth 2.0 authorization server. Configure Client resources with appropriate grant types and redirect URIs.

Learn more: [OAuth 2.0](../access-control/authentication/oauth-2-0.md)

### How do I integrate with external identity providers (SSO)?

Configure Token Introspection to validate tokens from external IdPs, or set up SSO for the Aidbox Console UI.

Learn more:
- [SSO with External Identity Provider](../access-control/authentication/sso-with-external-identity-provider.md)
- [Token Introspection Guide (blog)](https://www.health-samurai.io/articles/token-introspection-in-fhir)

### What are Access Policies?

AccessPolicy resources define authorization rules. Aidbox evaluates policies against incoming requests — if no policy matches, the request is denied (403 Forbidden).

```json
{
  "resourceType": "AccessPolicy",
  "link": [
    {
      "reference": "Client/myclient"
    }
  ],
  "engine": "matcho",
  "matcho": {
    "operation": {
      "id": "FhirRead"
    }
  }
}
```

Learn more:
- [Access Policies](../access-control/authorization/access-policies.md)
- [AccessPolicy Examples](../tutorials/security-access-control-tutorials/accesspolicy-examples.md)
- [Fine-Grained Access Control with Security Labels (blog)](https://www.health-samurai.io/articles/fhir-native-fine-grained-access-control-fhir-security-labels-in-the-real-world)

### What are Access Policy best practices?

Key recommendations:

- **Link policies** to User, Client, or Operation for performance optimization
- **Use `present?`** checks for optional values to avoid null errors
- **Name policies clearly** with prefix like `as-` and describe audience/resources
- **Avoid complex regex** — use simple patterns or multiple policies
- **Restrict unsafe search** parameters to prevent data leaks

Learn more: [AccessPolicy Best Practices](../tutorials/security-access-control-tutorials/accesspolicy-best-practices.md)

### Where can I find Access Policy examples?

Aidbox documentation includes ready-to-use examples:

- Patient sees only their own data
- Practitioner sees their patients
- Organization-based hierarchical access
- GraphQL access control
- JWT validation policies

Learn more: 
-[AccessPolicy Examples](../tutorials/security-access-control-tutorials/accesspolicy-examples.md)

### Why use SMART on FHIR?

SMART on FHIR is the industry standard for healthcare app authorization. Use it when:

- Building apps that integrate with multiple EHRs (Epic, Cerner, etc.)
- Need to pass ONC certification (required for US healthcare)
- Want standardized scopes like `patient/*.read` or `launch/patient`
- Building patient-facing apps with EHR launch context

Learn more:
- [SMART on FHIR](../access-control/authorization/smart-on-fhir/README.md)
- [Extending EMR with SMART on FHIR Apps (blog)](https://www.health-samurai.io/articles/a-practical-guide-for-extending-emr-capabilities-with-smart-on-fhir-applications)
- [RBAC with Keycloak and SMART on FHIR (blog)](https://www.health-samurai.io/articles/implementing-role-based-access-control-for-fhir-resources-with-keycloak-and-smart-on-fhir-v2)

### How do I pass Inferno tests?

Aidbox provides guidance for passing ONC Inferno certification tests, including SMART App Launch and Bulk Data.

Learn more: [Pass Inferno Tests](../access-control/authorization/smart-on-fhir/pass-inferno-tests-with-aidbox.md)

### Which external IdPs are supported?

Aidbox integrates with major identity providers:

- **Okta** — including group-based access management
- **Keycloak** — with auto-create user support
- **Azure AD** — standard and certificate authentication
- **Google** — OAuth 2.0 integration
- **Apple** — Sign in with Apple
- **GitHub** — OAuth authentication

Learn more:
- [Okta Integration](../tutorials/security-access-control-tutorials/okta.md)
- [Keycloak Integration](../tutorials/security-access-control-tutorials/keycloak.md)
- [Azure AD Integration](../tutorials/security-access-control-tutorials/azure-ad.md)
- [Set Up External IdP](../tutorials/security-access-control-tutorials/set-up-external-identity-provider.md)

### Does Aidbox support two-factor authentication?

Yes, Aidbox supports 2FA using TOTP (Time-based One-Time Password) compatible with authenticator apps like Google Authenticator and Authy.

Learn more: [Two-Factor Authentication](../access-control/authentication/two-factor-authentication.md)

### How do I implement relationship-based access control?

Control access based on relationships between users and patients (e.g., care teams, family members). Use AccessPolicies that evaluate relationship references in resources.

Learn more: [Relationship-Based Access Control](../tutorials/security-access-control-tutorials/relationship-based-access-control.md)

### How do I implement consent-based access control?

Use FHIR Consent resources combined with AccessPolicies to enforce patient consent preferences for data sharing.

Learn more: [Consent-Based Access Control](../tutorials/security-access-control-tutorials/how-to-implement-consent-based-access-control-using-fhir-search-and-aidbox-access-policy.md)

### How does audit logging work in Aidbox?

Aidbox provides comprehensive audit logging with FHIR BALP (Basic Audit Log Patterns) implementation:

- **FHIR CRUD & Search** — logs all resource operations
- **Patient compartment** — tracks access to patient data
- **Authentication events** — login, logout, SMART authorization
- **Resource versioning** — full history via `_history` operation

Enable with `BOX_SECURITY_AUDIT_LOG_ENABLED=true`.

Learn more:
- [Audit and Logging](../access-control/audit-and-logging.md)
- [Configure FHIR Audit Log](../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md)

### Can I send audit events to an external repository?

Yes, Aidbox can forward AuditEvent resources to an external Audit Record Repository. Events are batched into FHIR Bundles and sent via HTTP POST.

For advanced audit management, consider **Auditbox** — a dedicated audit log management solution with analytics, search, and compliance reporting.

Learn more:
- [External Audit Repository Configuration](../tutorials/security-access-control-tutorials/how-to-configure-audit-log.md#external-audit-repository-configuration)
- [Auditbox Documentation](https://www.health-samurai.io/docs/auditbox)
- [Auditbox Product Page](https://www.health-samurai.io/auditbox)
- [FHIR-Native Audit Log (blog)](https://www.health-samurai.io/articles/early-access-fhir-native-audit-log-for-modern-healthcare-solutions)

## Data Validation & Profiles

### How does validation work in Aidbox?

Aidbox validates resources using FHIR Schema engine. Validation happens automatically on create/update, or explicitly via `$validate` operation.

- **Automatic validation** — prevents invalid data from being saved. Aidbox automatically validates incoming resources against the profiles listed in  `meta.profile`.
- **Explicit validation** — `POST /fhir/Patient/$validate` checks without saving

Learn more: [Profiling and Validation](../modules/profiling-and-validation/README.md)

### How do I validate a resource?

Use the `$validate` operation to check a resource without saving it:

```http
POST /fhir/Patient/$validate
Content-Type: application/fhir+json

{"resourceType": "Patient", "birthDate": "invalid-date"}
```

Learn more: [$validate Operation](../api/rest-api/other/validate.md)

### What is FHIR Schema?

FHIR Schema is Aidbox's validation engine that uses a JSON Schema-like format internally. It supports FHIRPath invariants, slicing, terminology bindings, and reference validation.

Learn more: [FHIR Schema Validator](../modules/profiling-and-validation/fhir-schema-validator/README.md)

### How do I load an Implementation Guide?

Upload IGs through the FHIR Package API or Aidbox UI. Once loaded, resources marked with profile URLs are automatically validated.

Learn more:
- [Upload FHIR Implementation Guide](../tutorials/artifact-registry-tutorials/upload-fhir-implementation-guide/README.md)
- [4 Ways to Upload IGs to Aidbox (blog)](https://www.health-samurai.io/articles/4-ways-to-upload-igs-to-aidbox-pros-and-cons)

### How do I enable asynchronous validation?

For large datasets, enable async validation to process resources in the background without blocking API responses.

Learn more: [Asynchronous Resource Validation](../modules/profiling-and-validation/asynchronous-resource-validation.md)

## Terminology & ValueSets

### How do I work with ValueSets?

ValueSets define collections of codes for specific contexts. Use `$expand` to get all codes, `$validate-code` to check if a code is valid.

```http
GET /fhir/ValueSet/administrative-gender/$expand
```

Learn more: [ValueSet](../terminology-module/fhir-terminology/valueset.md)

### How do I work with CodeSystems?

CodeSystems define the actual codes and their meanings. Create or load CodeSystems, then reference them in ValueSets.

Learn more: [CodeSystem](../terminology-module/fhir-terminology/codesystem.md)

### How do I use external terminologies (SNOMED, LOINC, ICD)?

Aidbox's terminology module supports three operational modes:

- **Local** — use only terminologies loaded in Artifact Registry. Complete control but requires explicit loading of all CodeSystems
- **Hybrid** — combines local storage with external server fallback. Best for most use cases — local resources are used first, external servers fill gaps
- **Remote** — routes all requests to external terminology servers, bypassing local storage

Configure via `BOX_FHIR_TERMINOLOGY_ENGINE` environment variable (`local`, `hybrid`, or `remote`).

Learn more:
- [Aidbox Terminology Module](../terminology-module/aidbox-terminology-module/README.md)
- [Introducing Hybrid Terminology Engine (blog)](https://www.health-samurai.io/articles/introducing-hybrid-terminology-engine)
- [State of FHIR Terminology 2024 (blog)](https://www.health-samurai.io/articles/state-of-fhir-terminology-2024)

### What is the $expand operation?

`$expand` returns all codes in a ValueSet, optionally filtered:

```http
GET /fhir/ValueSet/condition-codes/$expand?filter=diabetes
```

Learn more: [ValueSet Operations](../terminology-module/fhir-terminology/valueset.md)

### What is $validate-code?

`$validate-code` checks if a specific code is valid within a ValueSet or CodeSystem:

```http
GET /fhir/ValueSet/administrative-gender/$validate-code?code=male&system=http://hl7.org/fhir/administrative-gender
```

Learn more: [Coded Values](../terminology-module/fhir-terminology/coded-values.md)

## SQL on FHIR

### What is SQL on FHIR?

[SQL on FHIR](https://sql-on-fhir.org/) is an **HL7 standard specification** for querying FHIR data using standard SQL. It defines ViewDefinition resources that flatten nested JSON into tabular views for analytics, reporting, and BI tools.

Aidbox is **fully compliant** with the SQL on FHIR specification. See [implementation status](https://sql-on-fhir.org/extra/impls.html).

Learn more:
- [SQL on FHIR in Aidbox](../modules/sql-on-fhir/README.md)
- [SQL on FHIR in PostgreSQL (blog)](https://www.health-samurai.io/articles/sql-on-fhir-in-postgresql)
- [SQL on FHIR: An Inside Look (blog)](https://www.health-samurai.io/articles/sql-on-fhir-an-inside-look)

### What is the SQL on FHIR workflow?

1. **Define** — create a ViewDefinition resource that maps FHIR paths to columns
2. **Materialize** — Aidbox creates a database view or table
3. **Query** — use standard SQL to query the flat view
4. **Connect** — integrate with BI tools (Tableau, Power BI, Metabase)

Learn more: [SQL on FHIR](../modules/sql-on-fhir/README.md)

### How do I create ViewDefinitions?

ViewDefinition resources define how to flatten FHIR resources into tabular views:

```yaml
resourceType: ViewDefinition
name: patient_view
resource: Patient
select:
  - column: [{name: id, path: id}]
  - column: [{name: family, path: name.family}]
```

Learn more:
- [Defining Flat Views](../modules/sql-on-fhir/defining-flat-views-with-view-definitions.md)
- [What is a ViewDefinition (blog)](https://www.health-samurai.io/articles/what-is-a-viewdefinition)

### How do I query flat views?

Once a ViewDefinition is materialized, query it with standard SQL:

```sql
SELECT * FROM patient_view WHERE family LIKE 'Smith%'
```

Learn more: [Query Data from Flat Views](../modules/sql-on-fhir/query-data-from-flat-views.md)

### How do I run a ViewDefinition?

Use the `$run` operation to execute a ViewDefinition and get results:

```http
POST /fhir/ViewDefinition/patient-view/$run
Content-Type: application/json

{"resourceType": "Parameters", "parameter": [{"name": "_format", "valueCode": "json"}]}
```

Learn more: [$run Operation](../modules/sql-on-fhir/operation-run.md)

### How do I materialize ViewDefinitions for better performance?

Use the `$materialize` operation to persist ViewDefinition results as database tables or views:

```http
POST /fhir/ViewDefinition/patient-view/$materialize
```

Learn more:
- [Materialize ViewDefinitions](../modules/sql-on-fhir/operation-materialize.md)
- [Introducing $materialize (blog)](https://www.health-samurai.io/articles/introducing-materialize-sql-interface-for-fhir-data)

### Does ViewDefinition support JOINs?

No, ViewDefinition flattens a single resource type. For JOINs across resource types, use SQL directly on the materialized views:

```sql
SELECT p.id, p.family, e.status
FROM patient_view p
JOIN encounter_view e ON e.subject_id = p.id
```

Learn more: [Query Data from Flat Views](../modules/sql-on-fhir/query-data-from-flat-views.md)

### What SQL functions are available?

Aidbox provides custom SQL functions for working with FHIR data, including functions for JSON manipulation, date handling, and terminology operations.

Learn more: [Aidbox SQL Functions](../reference/aidbox-sql-functions.md)

## Bulk Data & Import/Export

### How do I upload sample data?

Use the built-in sample data loader or import Synthea-generated data via the Bulk API.

Learn more: [Upload Sample Data](../getting-started/upload-sample-data.md)

### What are the differences between Bulk API operations?

| Operation | Direction | Async | Use Case |
|-----------|-----------|-------|----------|
| `$export` | Export | Yes | FHIR-compliant bulk export to cloud storage (GCP, AWS, Azure) |
| `$dump` | Export | No | Real-time streaming export, memory-efficient for data pipelines |
| `$dump-csv` | Export | No | CSV export for spreadsheets and simple analytics |
| `$import` | Import | Yes | High-performance import from URLs (S3, GCS, HTTP) without validation|
| `/fhir` POST | Import | No | Standard FHIR transactions for small datasets with validation|

**Choose `$export`** for FHIR Bulk Data spec compliance and cloud storage integration.
**Choose `$dump`** for real-time streaming to your own pipeline.
**Choose `$import`** for loading large NDJSON files from URLs.

Learn more: [Bulk API](../api/bulk-api/README.md)

### How do I export data ($export)?

Use the FHIR Bulk Data Export operation for large-scale data extraction:

```http
GET /fhir/$export
# or for specific resource types
GET /fhir/$export?_type=Patient,Observation
```

The operation runs asynchronously and produces NDJSON files.

Learn more: [$export](../api/bulk-api/export.md)

### How do I import data?

Aidbox provides multiple import methods:

- `$import` — async import from URLs or direct upload
- `$load` — sync import for smaller datasets
- `/fhir` endpoint — standard FHIR transactions

Learn more: [Import and FHIR Import](../api/bulk-api/import-and-fhir-import.md)

### How do I import from S3?

Configure S3 credentials and use the `$import` operation with S3 URLs:

```http
POST /$import
Content-Type: application/json

{"source": [{"url": "s3://bucket/patients.ndjson"}]}
```

Learn more: [Bulk Import from S3](../api/bulk-api/bulk-import-from-an-s3-bucket.md)

### How do I export to CSV?

Use the `$dump-csv` operation for CSV export:

```http
GET /fhir/Patient/$dump-csv
```

Learn more: [$dump-csv](../api/bulk-api/dump-csv.md)

### What is the $dump operation?

`$dump` streams FHIR resources in NDJSON format in real-time, ideal for data pipelines:

```http
GET /fhir/Patient/$dump
```

Learn more: [$dump](../api/bulk-api/dump.md)

## Subscriptions

### How do Subscriptions work?

Subscriptions enable real-time notifications when resources change. Aidbox uses topic-based subscriptions with support for multiple delivery channels.

Learn more:
- [Subscriptions](../modules/topic-based-subscriptions/README.md)
- [Topic-based Subscriptions: Top 5 Use Cases (blog)](https://www.health-samurai.io/articles/topic-based-subscriptions-top-5-use-cases-for-digital-health)
- [Subscriptions on FHIR Overview (blog)](https://www.health-samurai.io/articles/subscriptions-on-fhir-overview-of-data-integration-challenges-and-solutions)

### What subscription destinations are supported?

Aidbox supports multiple delivery channels for subscriptions:

- **Webhook** — HTTP POST to any URL
- **Kafka** — high-throughput event streaming
- **RabbitMQ** — AMQP message queue
- **GCP Pub/Sub** — Google Cloud messaging
- **NATS** — lightweight messaging
- **ActiveMQ** — JMS message broker
- **ClickHouse** — analytics database

Learn more:
- [Webhook Tutorial](../tutorials/subscriptions-tutorials/webhook-aidboxtopicdestination.md)
- [Kafka Tutorial](../tutorials/subscriptions-tutorials/kafka-aidboxtopicdestination.md)
- [RabbitMQ Tutorial](../tutorials/subscriptions-tutorials/rabbitmq-tutorial.md)
- [GCP Pub/Sub Tutorial](../tutorials/subscriptions-tutorials/gcp-pub-sub-aidboxtopicdestination.md)
- [NATS Tutorial](../tutorials/subscriptions-tutorials/aidboxtopicsubscription-nats-tutorial.md)
- [ActiveMQ Tutorial](../tutorials/subscriptions-tutorials/activemq-tutorial.md)
- [ClickHouse Tutorial](../tutorials/subscriptions-tutorials/clickhouse-aidboxtopicdestination.md)

## Integrations

### How do I convert HL7 v2 to FHIR?

Aidbox provides an HL7 v2 integration toolkit for converting HL7 v2 messages to FHIR resources. Supports custom Z-segments, strict and non-strict parsing, and MLLP protocol.

Learn more:
- [HL7 v2 Integration](../modules/integration-toolkit/hl7-v2-integration/README.md)
- [Tackling Legacy System Integration with FHIR (blog)](https://www.health-samurai.io/articles/tackling-legacy-system-integration-with-fhir)

### How do I convert C-CDA to FHIR?

Bidirectional C-CDA ↔ FHIR conversion with USCDI v1 support. Validates against XSD and Schematron rules.

Learn more: [C-CDA Converter](../modules/integration-toolkit/ccda-converter/README.md)

### Does Aidbox support X12?

Yes, Aidbox can parse and generate X12 EDI messages for healthcare administrative transactions.

Learn more: [X12 Message Converter](../modules/integration-toolkit/x12-message-converter.md)

### How do I send emails from Aidbox?

Aidbox supports email providers for notifications:

- **Mailgun** — transactional email service
- **Postmark** — delivery-focused email API

Learn more:
- [Mailgun Configuration](../reference/email-providers-reference/mailgun-environment-variables.md)
- [Postmark Configuration](../reference/email-providers-reference/postmark-environment-variables.md)
- [Notification Resource](../reference/email-providers-reference/notification-resource-reference.md)

### How do I connect Aidbox to Power BI?

Use SQL on FHIR ViewDefinitions to create flat views, then connect Power BI via PostgreSQL connection string.

Learn more: [Power BI Integration](../modules/integration-toolkit/analytics/power-bi.md)

## Modules & Features

### What is Aidbox Forms?

Aidbox Forms is a module for building digital medical forms and capturing data in FHIR format. Features:

- **UI Builder** — create forms without code (FHIR SDC compliant)
- **Form Gallery** — ready-made medical form templates
- **Data extraction** — store captured data as FHIR resources
- **Pre-fill** — populate forms with existing patient data

Learn more:
- [Aidbox Forms](../modules/aidbox-forms/README.md)
- [Why Building Healthcare Forms is Challenging (blog)](https://www.health-samurai.io/articles/why-building-healthcare-forms-is-so-challenging--and-how-to-fix-it)
- [AI Assistant in Aidbox Forms (blog)](https://www.health-samurai.io/articles/closing-the-loop-from-forms-to-insights-introducing-the-ai-assistant-in-aidbox-forms)
- [Top 10 Medical Forms (blog)](https://www.health-samurai.io/articles/top-10-medical-forms)

### What is the ePrescription module?

Aidbox ePrescription enables sending electronic prescriptions via Surescripts integration:

- Send new prescriptions (NewRx)
- Handle cancellations, renewals, and change requests
- FDB and RxNorm medication search
- Drug-drug and drug-allergy interaction checks

Learn more: [ePrescription](../modules/eprescription/README.md)

### What is the MPI module?

Master Patient Index (MPI) ensures accurate patient identification by detecting and managing duplicate records:

- Probabilistic patient matching with configurable algorithms
- Merge and unmerge operations
- Handles typos and incomplete data
- Audit trail for all operations

Learn more:
- [MDM — Master Data Management](../modules/mdm/README.md)
- [Master Patient Index and Record Linkage (blog)](https://www.health-samurai.io/articles/master-patient-index-and-record-linkage)

### Does Aidbox support file storage with signed URLs?

Yes, Aidbox integrates with cloud storage providers and generates signed URLs for secure file upload/download:

- **AWS S3** — with IAM credentials or instance roles
- **GCP Cloud Storage** — with service accounts or workload identity
- **Azure Blob Storage** — with connection strings or managed identity

Learn more:
- [AWS S3](../file-storage/aws-s3.md)
- [GCP Cloud Storage](../file-storage/gcp-cloud-storage.md)
- [Azure Blob Storage](../file-storage/azure-blob-storage.md)
- [Wearables and Medical IoT Devices to FHIR (blog)](https://www.health-samurai.io/articles/aidbox-for-wearable-and-medical-devices)

### How do I execute custom SQL on resource changes?

Use **AidboxTrigger** to run SQL automatically when resources are created, updated, or deleted:

```yaml
resourceType: AidboxTrigger
resource: Patient
action: [create, update, delete]
sql: "INSERT INTO patient_audit (id, action) VALUES ({{id}}, 'modified');"
```

Triggers run in the same transaction — if SQL fails, the FHIR operation rolls back.

Learn more: [AidboxTrigger](../modules/other-modules/aidbox-trigger.md)

### What System resources does Aidbox have?

Aidbox provides many internal system resources beyond FHIR for configuration and operations. Examples include:

- **Client** — OAuth clients for authentication
- **AccessPolicy** — authorization rules
- **User** — user accounts
- **AidboxTopicDestination** — subscription delivery channels
- **ViewDefinition** — SQL on FHIR views
- **App** — custom application integrations

Browse all available resources in the **Resource Browser** in Aidbox UI.

Learn more: [System Resources Reference](../reference/system-resources-reference/README.md)

### Does Aidbox support ONC Health IT Certification?

Yes, Aidbox provides a pre-configured solution for EHR vendors seeking ONC certification under the 2015 Edition Cures Update:

- FHIR R4 API with US Core validation
- SMART on FHIR authorization (Standalone + EHR Launch)
- Bulk EHI export to S3
- C-CDA document generation
- Complete audit logging

Learn more: [ONC Health IT Certification](../solutions/providers-or-onc-health-it-certification-program/README.md)

### What is the FHIR App Portal?

A unified UI suite for managing SMART on FHIR app ecosystems:

- **Developer Sandbox** — self-service app registration and testing
- **Admin Portal** — app approval, configuration, metrics
- **App Gallery** — patient-facing app discovery with consent management

Learn more: [Aidbox + FHIR App Portal](../solutions/aidbox-+-fhir-app-portal/README.md)

## Developer Experience

### What Aidbox SDKs are available?

Aidbox provides type-safe SDKs generated from FHIR schemas:

- **TypeScript** — generated with `@fhirschema/codegen`
- **Python** — with async support
- **Java** — HAPI FHIR integration
- **C#** — .NET with async/await patterns

Learn more:
- [TypeScript SDK](../getting-started/typescript.md)
- [Python SDK](../getting-started/python.md)
- [Java SDK](../getting-started/java.md)
- [C# SDK](../getting-started/csharp.md)
- [Free FHIR Tools (blog)](https://www.health-samurai.io/articles/free-fhir-tools)

### How do I test the API?

Multiple options for API testing:

- **REST Console** — built-in Aidbox UI for interactive testing
- **Aidbox Notebooks** — shareable interactive tutorials with REST, SQL, RPC
- **Postman** — external HTTP client with Basic Auth
- **HTTP files** — IDE-based testing (VS Code REST Client, IntelliJ)

Learn more:
- [REST Console](../overview/aidbox-ui/rest-console.md)
- [Aidbox Notebooks](../overview/aidbox-ui/aidbox-notebooks.md)

### How do I debug Access Policies?

Aidbox provides several debugging methods:

- **Access Policy Dev Tool** — built-in UI for testing policies
- **`__debug` query parameter** — returns evaluation details in response
- **`x-debug: policy` header** — logs evaluation results
- **`/auth/test-policy` endpoint** — test policies without creating them

Learn more: [Debug Access Control](../tutorials/security-access-control-tutorials/debug-access-control.md)

### Can I use AI assistants with Aidbox?

Yes, Aidbox supports AI integration:

- **MCP Server** — connect Claude, ChatGPT, or other AI assistants to manage FHIR resources
- **AI Prompts** — generate Access Policies, Search Parameters via GitHub Copilot, Cursor, Claude Code

Learn more:
- [Working with AI](../developer-experience/ai.md)
- [MCP FHIR Server (blog)](https://www.health-samurai.io/articles/mcp-fhir-server)
- [AI Assistant for FHIR SDC and Analytics (blog)](https://www.health-samurai.io/articles/ai-assistant-for-fhir-sdc-and-analytics)

### Where can I find code examples?

Aidbox provides extensive examples:

- **Aidbox Examples** — production-ready code samples on [GitHub](https://github.com/Aidbox/examples) with curated selection at [health-samurai.io/docs/aidbox/examples](https://www.health-samurai.io/docs/aidbox/examples)
- **Aidbox Notebooks** — built-in interactive documentation

Learn more: [Developer Experience Overview](../developer-experience/developer-experience-overview.md)

### How do I extend Aidbox with custom logic?

Use the **Apps Framework** to add custom endpoints and operations. Aidbox acts as a reverse proxy — requests to registered paths are forwarded to your application:

```yaml
resourceType: App
id: my-app
apiVersion: 1
endpoint:
  url: http://my-service:8080
  type: http-rpc
operations:
  my-operation:
    method: POST
    path: [my-endpoint]
```

Learn more:
- [Apps Framework](../developer-experience/apps.md)
- [Building Healthcare Microservices (blog)](https://www.health-samurai.io/articles/building-healthcare-microservices-a-fhir-native-approach)
- [Using API Gateway with FHIR API (blog)](https://www.health-samurai.io/articles/using-api-gateway-with-fhir-api)

## Configuration & Deployment

### What deployment options are available?

Aidbox supports multiple deployment models:

- **Docker** — single container or docker-compose for development
- **Kubernetes** — production deployment with Helm charts
- **Cloud** — AWS, Azure, GCP with managed PostgreSQL
- **AWS Marketplace** — one-click deployment on AWS
- **On-premises** — self-hosted in your data center
- **Air-gapped** — for isolated networks without internet access

Learn more:
- [Run Aidbox Locally](../getting-started/run-aidbox-locally.md)
- [Deploy with Helm Charts](../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/deploy-aidbox-with-helm-charts.md)
- [Production-ready Kubernetes Deployment](../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/deploy-production-ready-aidbox-to-kubernetes.md)
- [Run on Managed PostgreSQL](../deployment-and-maintenance/deploy-aidbox/run-aidbox-on-managed-postgresql.md)
- [Aidbox on AWS Marketplace](https://aws.amazon.com/marketplace/pp/prodview-l5djlpvsd6o5g)

### Does Health Samurai offer a managed Aidbox service?

Yes, **Aidbox Cloud Sandbox** is available at [aidbox.app](https://aidbox.app):

- Free development license (no PHI, 5 GB limit)
- Hosted on Google Cloud Platform
- Instant setup with choice of FHIR version
- Personal URLs for each instance

For production managed hosting, contact Health Samurai sales.

Learn more: [Run Aidbox in Sandbox](../getting-started/run-aidbox-in-sandbox.md)

### Does Health Samurai offer support and professional services?

Yes, Health Samurai offers support tiers and professional services:

**Support tiers:** Basic, Professional, Enterprise, and Ultimate — ranging from account management to dedicated engineers and custom Aidbox builds.

**Professional services:**
- Aidbox provisioning and maintenance in your infrastructure
- Production-ready deployment
- Migration from other FHIR servers
- Performance tuning and optimization
- Development and operations training

Learn more: [Licensing and Support](licensing-and-support.md)

### Are Helm charts available?

Yes, official Helm charts are available:

```bash
helm repo add aidbox https://aidbox.github.io/helm-charts
helm install aidbox aidbox/aidbox
```

Charts include Aidbox and AidboxDB (optimized PostgreSQL).

Learn more: [Deploy with Helm Charts](../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/deploy-aidbox-with-helm-charts.md)

### What environment variables should I configure?

The default `runme` script sets recommended values for FHIR compliance, validation, terminology, and security. Review and adjust these settings based on your requirements.

Learn more:
- [Run Aidbox Locally](../getting-started/run-aidbox-locally.md)
- [Recommended Environment Variables](../configuration/recommended-envs.md)

### What is the difference between environment variables and Settings?

Aidbox can be configured in two ways:

- **Environment variables** — set at container startup, require restart to change
- **Settings resource** — stored in database, can be changed at runtime via Aidbox UI

Use environment variables for infrastructure settings (database, license). Use Settings for runtime configuration that may need to change without restart.

Learn more:
- [Settings](../configuration/settings.md)
- [All Settings Reference](../reference/all-settings.md)
- [Configure Aidbox](../configuration/configure-aidbox-and-multibox.md)

### How do I load initial configuration resources on startup?

Use **Init Bundle** to automatically create resources (Clients, AccessPolicies, etc.) when Aidbox starts:

```yaml
# init-bundle.yaml
resourceType: Bundle
type: transaction
entry:
  - resource:
      resourceType: Client
      id: my-client
      secret: my-secret
    request:
      method: PUT
      url: /Client/my-client
```

Set the environment variable:
```
BOX_INIT_BUNDLE=file:///path/to/init-bundle.json
```

Learn more: [Init Bundle](../configuration/init-bundle.md)

### How do I implement multi-tenancy?

Aidbox supports two multi-tenancy approaches:

- **Logical multi-tenancy** — organization-based hierarchical access control. Data is stored in one database but isolated per organization. Each organization gets its own scoped API: `/Organization/<org-id>/fhir`
- **Physical multi-tenancy (Multibox)** — separate databases for each tenant. Complete data isolation.

Learn more:
- [Organization-based Access Control](../access-control/authorization/scoped-api/organization-based-hierarchical-access-control.md)
- [Run Multibox Locally](../tutorials/security-access-control-tutorials/run-multibox-locally.md)
- [Migrate from Multibox to Aidbox](../tutorials/other-tutorials/migrate-from-multibox-to-aidbox.md)
- [Multi-tenant FHIR API Design (blog)](https://www.health-samurai.io/articles/how-to-design-a-multi-tenant-fhir-api-for-an-existing-ehr-system)

### What network access does Aidbox require?

Aidbox requires outbound HTTPS (port 443) to:

| Purpose                     | Address                                        |
|-----------------------------|------------------------------------------------|
| License portal              | `https://aidbox.app`, `https://*.aidbox.app`   |
| Terminology server          | `https://tx.health-samurai.io/fhir`            |
| IG Package list fetch            | `https://storage.googleapis.com/fhir-schema-registry/` |
| IG Package download from NPM registry            | `https://fs.get-ig.org/pkgs` |

### What data is transmitted during license verification?

Only the JWT license token is sent to `https://aidbox.app`. No customer data is transmitted.

### Does Aidbox collect telemetry?

Yes, Aidbox may send basic usage statistics to help improve the product. This can be configured or disabled.

Learn more: [Usage Stats Settings](../reference/all-settings.md#usage-stats)

### How do I set up High Availability?

Aidbox supports HA deployment with:

- Multiple Aidbox replicas behind a load balancer
- PostgreSQL HA with replication and automatic failover
- Kubernetes probes (liveness, readiness, startup)
- Zero-downtime updates

**Note:** Multiple replicas require shared RSA keys for JWT validation.

Learn more: [Highly Available Aidbox](../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/highly-available-aidbox.md)

### How do I backup and restore?

Only PostgreSQL database backup rs required. All standard PostgreSQL backup tools are supported. Managed PostgreSQL services typically provide backup and restore functionality.
Learn more:
- [Backup and Restore](../deployment-and-maintenance/backup-and-restore/README.md)
- [HIPAA-Compliant Instance Synchronization (blog)](https://www.health-samurai.io/articles/hipaa-compliant-aidbox-instance-synchronization)

### Can I use a read-only database replica?

Yes, Aidbox supports delegating read-only queries to a PostgreSQL read replica. Benefits:

- **Resource isolation** — prevents slow read queries from affecting write performance
- **Load distribution** — spreads database load across multiple servers
- **System resilience** — ensures write operations remain responsive under heavy read load

Learn more:
- [Database Overview](../database/overview.md)
- [Read Replica Example](https://github.com/Aidbox/examples/tree/main/aidbox-features/aidbox-with-ro-replica)

## Performance & Troubleshooting

### How do I optimize query performance?

Create database indexes for frequently searched fields. Aidbox can suggest indexes based on query patterns.

Learn more:
- [Indexes](../deployment-and-maintenance/indexes/README.md)
- [Load 1 Billion FHIR Resources in 5 Hours (blog)](https://www.health-samurai.io/articles/how-to-load-1-billion-fhir-resources-into-aidbox-in-5-hours)

### How do I debug slow queries?

Use `_explain=analyze` parameter to see the SQL query and execution plan:

```http
GET /fhir/Patient?name=John&_explain=analyze
```

This shows the generated SQL and PostgreSQL execution plan to identify missing indexes or inefficient queries.

Learn more: [_explain Parameter](../api/rest-api/aidbox-search.md#_explain)

### How do I get suggested indexes?

Use the index suggestion feature to analyze slow queries and get recommendations:

Learn more: [Get Suggested Indexes](../deployment-and-maintenance/indexes/get-suggested-indexes.md)

### How do I create indexes manually?

Create custom indexes on specific resource fields:

```sql
CREATE INDEX patient_birthdate_idx ON patient ((resource->>'birthDate'));
```

Learn more: [Create Indexes Manually](../deployment-and-maintenance/indexes/create-indexes-manually.md)

### How do I monitor Aidbox?

Aidbox provides observability features including metrics, logging, and tracing for monitoring system health and performance.

Learn more: [Observability](../modules/observability/README.md)

### What are PostgreSQL requirements?

Aidbox requires PostgreSQL 12+ with specific extensions. Actively supports the three most recent versions (currently 18, 17, 16).

Learn more: [PostgreSQL Requirements](../database/postgresql-requirements.md)
